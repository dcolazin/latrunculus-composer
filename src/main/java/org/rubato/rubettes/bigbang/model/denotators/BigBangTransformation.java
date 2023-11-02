package org.rubato.rubettes.bigbang.model.denotators;

import org.rubato.rubettes.util.DenotatorPath;
import org.vetronauta.latrunculus.core.math.arith.number.Real;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.matrix.Matrix;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineMultiMorphism;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;

import java.util.List;

public class BigBangTransformation {
	
	private ModuleMorphism morphism; 
	private List<TransformationPaths> transformationPaths;
	private boolean copyAndMap; 
	private DenotatorPath anchorNodePath;
	
	public BigBangTransformation(ModuleMorphism morphism, List<TransformationPaths> transformationPaths, boolean copyAndMap, DenotatorPath anchorNodePath) {
		this.morphism = morphism;
		this.transformationPaths = transformationPaths;
		this.copyAndMap = copyAndMap;
		this.anchorNodePath = anchorNodePath;
	}
	
	public ModuleMorphism getModuleMorphism() {
		return this.morphism;
	}
	
	public List<TransformationPaths> getTransformationPaths() {
		return this.transformationPaths;
	}
	
	public void setCopyAndMap(boolean copyAndMap) {
		this.copyAndMap = copyAndMap;
	}
	
	public boolean isCopyAndMap() {
		return this.copyAndMap;
	}
	
	public DenotatorPath getAnchorNodePath() {
		return this.anchorNodePath;
	}
	
	public BigBangTransformation inverse() {
		ArithmeticAffineMultiMorphism<Real> morphism = (ArithmeticAffineMultiMorphism<Real>)this.morphism;
		Matrix<ArithmeticElement<Real>> inverseMatrix = morphism.getMatrix().inverse();
		Vector<ArithmeticElement<Real>> inverseShift = inverseMatrix.product(morphism.getVector()).negated();
		ArithmeticAffineMultiMorphism<Real> inverseMorphism = new ArithmeticAffineMultiMorphism<>(RRing.ring, inverseMatrix, inverseShift);
		return new BigBangTransformation(inverseMorphism, this.transformationPaths, this.copyAndMap, this.anchorNodePath);
	}
	
	public String toString() {
		return this.morphism.toString();
	}

}
