package org.rubato.rubettes.bigbang.model.operations;

import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.model.denotators.TransformationProperties;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.List;
import java.util.stream.Collectors;

public class AffineTransformation extends AbstractLocalTransformation {
	
	private List<Real> shift;
	private RMatrix transform;
	
	//used for cloning
	protected AffineTransformation(BigBangModel model, AffineTransformation other) {
		super(model, other);
		this.setParameters(other.shift, other.transform);
	}
	
	public AffineTransformation(BigBangModel model, TransformationProperties properties, List<Real> shift, RMatrix transform2x2) {
		super(model, properties);
		//System.out.println(properties.getCenter()[0] + " " + properties.getCenter()[1]);
		this.setParameters(shift, transform2x2);
	}
	
	public AffineTransformation(BigBangModel model, XMLReader reader, Element element) {
		super(model, reader, element);
		//TODO IMPLEMENT
	}
	
	public void setParameters(List<Real> shift, RMatrix transform2x2) {
		this.shift = shift;
		this.transform = transform2x2;
		this.updateOperation();
	}
	
	//creates a copy of this with the same center and scaleFactors adjusted by the given ratio
	protected AffineTransformation createModifiedCopy(double ratio) {
		AffineTransformation modifiedCopy = (AffineTransformation)this.clone();
		List<Real> partialShift = this.shift.stream().map(s -> s.product(new Real(ratio))).collect(Collectors.toList());
		double[][] scaleMat = {{ratio, 0},{0,ratio}};
		RMatrix partialTransform = this.transform.product(new RMatrix(scaleMat));
		modifiedCopy.setParameters(partialShift, partialTransform);
		return modifiedCopy;
	}
	
	@Override
	protected RMatrix getMatrix() {
		return transform;
	}
	
	protected List<Real> getShift() {
		return this.shift;
	}
	
	@Override
	protected String getSpecificPresentationName() {
		return "Affine Transformation";
	}

}
