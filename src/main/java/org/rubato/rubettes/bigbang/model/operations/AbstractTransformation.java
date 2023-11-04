package org.rubato.rubettes.bigbang.model.operations;

import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.model.BigBangObject;
import org.rubato.rubettes.bigbang.model.OperationPathResults;
import org.rubato.rubettes.bigbang.model.denotators.BigBangTransformation;
import org.rubato.rubettes.bigbang.model.denotators.TransformationPaths;
import org.rubato.rubettes.bigbang.model.denotators.TransformationProperties;
import org.rubato.rubettes.util.DenotatorPath;
import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.arith.number.RealWrapper;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.AffineFreeMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineFreeMorphism;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class AbstractTransformation extends AbstractOperation {
	
	private Set<BigBangObject> objects;
	private BigBangObject anchor;
	private List<TransformationPaths> transformationPaths;
	protected double[] center;
	protected double[] endingPoint;
	private boolean copyAndTransform;
	private ModuleMorphism transformation;
	
	//used for cloning
	protected AbstractTransformation(BigBangModel model, AbstractTransformation other) {
		super(model);
		this.init();
		if (model == other.model) {
			this.objects = other.objects;
		}
		this.transformationPaths = other.transformationPaths;
		this.anchor = other.anchor;
		this.copyAndTransform = other.copyAndTransform;
		this.center = other.center;
		this.endingPoint = other.endingPoint;
		this.modificationRatio = other.modificationRatio;
	}
	
	protected AbstractTransformation(BigBangModel model, TransformationProperties properties) {
		super(model);
		this.init();
		this.setProperties(properties);
	}
	
	protected AbstractTransformation(BigBangModel model, XMLReader reader, Element element) {
		super(model, reader, element);
		this.init();
		this.fromXML(reader, element);
	}
	
	private void init() {
		this.isAnimatable = true;
		this.isSplittable = true;
		this.objects = new TreeSet<>();
	}
	
	private void setProperties(TransformationProperties properties) {
		this.objects = properties.getObjects();
		this.anchor = properties.getAnchor();
		this.transformationPaths = properties.getTransformationPaths();
		this.copyAndTransform = properties.copyAndTransform();
		this.center = properties.getCenter();
		this.endingPoint = properties.getEndPoint();
	}
	
	public void updateProperties(TransformationProperties properties) {
		this.setProperties(properties);
		this.updateOperation();
	}
	
	public void modifyCenter(double[] newValues) {
		this.center = newValues;
		this.updateOperation();
	}
	
	public void modify(double[] newValues) {
		this.endingPoint = newValues;
		this.updateOperation();
	}
	
	protected void initTransformation(RMatrix matrix, List<Real> shift) {
		List<RMatrix> matrices = new ArrayList<>();
		matrices.add(matrix);
		List<List<Real>> shifts = new ArrayList<>();
		shifts.add(shift);
		this.initTransformation(matrices, shifts);
	}
	
	protected void initTransformation(List<RMatrix> matrices, List<List<Real>> shifts) {
		ModuleMorphism morphism = AffineFreeMorphism.make(RRing.ring, matrices.get(0), new Vector<>(RRing.ring, shifts.get(0)));
		for (int i = 1; i < matrices.size(); i++) {
			try {
				morphism = AffineFreeMorphism.make(RRing.ring, matrices.get(i), new Vector<>(RRing.ring, shifts.get(i))).compose(morphism);
			} catch (CompositionException e) { e.printStackTrace(); }
		}
		this.transformation = morphism;
	}
	
	@Override
	public OperationPathResults execute() {
		Set<DenotatorPath> objectPaths = this.getObjectPaths(this.objects);
		DenotatorPath anchorPath = null;
		if (this.anchor != null) {
			anchorPath = this.anchor.getTopDenotatorPathAt(this);
		}
		BigBangTransformation transformation = new BigBangTransformation(this.transformation, this.transformationPaths, this.copyAndTransform, anchorPath);
		return this.model.getDenotatorManager().addTransformation(objectPaths, anchorPath, transformation);
	}
	
	public int[] getXYViewParameters() {
		return this.transformationPaths.get(0).getXYCoordinates();
	}
	
	public double[] getCenter() {
		return this.center;
	}
	
	public abstract double[] getEndingPoint();
	
	protected double round(double number) {
		return ((double)Math.round(number*100))/100;
	}
	
	private static final String TRANSFORMATION_TAG = "Transformation";
	private static final String CENTER_ATTR = "center";
	private static final String ENDING_POINT_ATTR = "endingPoint";
	private static final String COPY_AND_TRANSFORM_ATTR = "copyAndTransform";
	
	public void toXML(XMLWriter writer) {
		super.toXML(writer);
		writer.openBlock(TRANSFORMATION_TAG, CENTER_ATTR, Arrays.toString(this.center),
				ENDING_POINT_ATTR, Arrays.toString(this.endingPoint),
				COPY_AND_TRANSFORM_ATTR, this.copyAndTransform);
		for (TransformationPaths currentTransformationPath : this.transformationPaths) {
			currentTransformationPath.toXML(writer);
		}
		writer.closeBlock();
	}
	
	private void fromXML(XMLReader reader, Element element) {
		Element transformationElement = XMLReader.getChild(element, TRANSFORMATION_TAG);
		this.center = XMLReader.getDoubleArrayAttribute(transformationElement, CENTER_ATTR);
		this.endingPoint = XMLReader.getDoubleArrayAttribute(transformationElement, ENDING_POINT_ATTR);
		this.copyAndTransform = XMLReader.getBooleanAttribute(transformationElement, COPY_AND_TRANSFORM_ATTR);
		this.transformationPaths = new ArrayList<TransformationPaths>();
		Element pathsElement = XMLReader.getChild(transformationElement, TransformationPaths.TRANSFORMATION_PATHS_TAG);
		while (pathsElement != null) {
			this.transformationPaths.add(new TransformationPaths(reader, pathsElement));
			pathsElement = XMLReader.getNextSibling(pathsElement, TransformationPaths.TRANSFORMATION_PATHS_TAG);
		}
	}

}
