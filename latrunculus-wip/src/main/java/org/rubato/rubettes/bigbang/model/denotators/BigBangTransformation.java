package org.rubato.rubettes.bigbang.model.denotators;

import org.rubato.rubettes.util.DenotatorPath;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.matrix.Matrix;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineMultiMorphism;

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
		AffineMultiMorphism<Real> morphism = (AffineMultiMorphism<Real>) this.morphism;
		Matrix<Real> inverseMatrix = morphism.getMatrix().inverse();
		Vector<Real> inverseShift = inverseMatrix.product(morphism.getVector()).negated();
		AffineMultiMorphism<Real> inverseMorphism = new AffineMultiMorphism<>(RRing.ring, inverseMatrix, inverseShift);
		return new BigBangTransformation(inverseMorphism, this.transformationPaths, this.copyAndMap, this.anchorNodePath);
	}
	
	public String toString() {
		return this.morphism.toString();
	}

}
