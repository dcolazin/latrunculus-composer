package org.rubato.rubettes.bigbang.model.operations;

import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.model.denotators.TransformationProperties;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractLocalTransformation extends AbstractTransformation {
	
	private double[] shift1;
	private double[] shift2;
	
	//used for cloning
	protected AbstractLocalTransformation(BigBangModel model, AbstractLocalTransformation other) {
		super(model, other);
	}
	
	protected AbstractLocalTransformation(BigBangModel model, TransformationProperties properties) {
		super(model, properties);
	}

	protected AbstractLocalTransformation(BigBangModel model, XMLReader reader, Element element) {
		super(model, reader, element);
	}
	
	public void updateOperation() {
		this.shift1 = new double[]{-1*this.center[0],-1*this.center[1]};
		this.shift2 = new double[]{this.center[0],this.center[1]};
		this.initTransformation(this.getMatrix(), this.getShift());
	}

	@Override
	protected void initTransformation(RMatrix matrix, List<Real> shift) {
		RMatrix identity = new RMatrix(new double[][]{{1,0},{0,1}});
		List<RMatrix> matrices = new ArrayList<>();
		matrices.add(identity);
		matrices.add(matrix);
		matrices.add(identity);
		List<List<Real>> shifts = new ArrayList<>();
		shifts.add(Arrays.stream(this.shift1).mapToObj(Real::new).collect(Collectors.toList()));
		shifts.add(shift);
		shifts.add(Arrays.stream(this.shift2).mapToObj(Real::new).collect(Collectors.toList()));
		this.initTransformation(matrices, shifts);
	}
	
	protected abstract RMatrix getMatrix();

	@Override
	public List<AbstractOperation> getSplitOperations(double ratio) {
		this.modify(1);
		List<AbstractOperation> shearings = new ArrayList<>();
		shearings.add(this.createModifiedCopy(ratio));
		shearings.add(this.createModifiedCopy(1-ratio));
		return shearings;
	}
	
	protected abstract AbstractLocalTransformation createModifiedCopy(double ratio);

	@Override
	public double[] getCenter() {
		return this.center;
	}
	
	public double[] getEndingPoint() {
		double x = this.center[0]+(this.endingPoint[0]-this.center[0])*this.modificationRatio;
		double y = this.center[1]+(this.endingPoint[1]-this.center[0])*this.modificationRatio;
		return new double[]{x,y};
	}
	
	protected List<Real> getShift() {
		List<Real> list = new ArrayList<>(2);
		list.add(new Real(0));
		list.add(new Real(0));
		return list;
	}

	@Override
	public void toXML(XMLWriter writer) {
		super.toXML(writer);
	}
	
}