package org.rubato.rubettes.bigbang.model.denotators;

import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.rubato.rubettes.bigbang.model.OperationPathResults;
import org.rubato.rubettes.util.ArbitraryDenotatorMapper;
import org.rubato.rubettes.util.DenotatorPath;
import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineFreeMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BigBangMapper extends BigBangManipulator {
	
	private ModuleMorphism morphism;
	private boolean copyAndMap;
	private boolean relative;
	
	public BigBangMapper(BigBangDenotatorManager denotatorManager, BigBangTransformation transformation) {
		super(denotatorManager, transformation.getTransformationPaths());
		this.morphism = transformation.getModuleMorphism();
		this.copyAndMap = transformation.isCopyAndMap();
		this.relative = transformation.getAnchorNodePath() != null;
	}
	
	/**
	 * Maps the objects at the given paths
	 * @return the resulting newPaths
	 */
	public OperationPathResults mapCategorizedObjects(Set<DenotatorPath> objectPaths) {
		//TODO REALLY CHECK WHAT HAPPENS WITHOUT CATEGORIZED OBJECTS
		//for (int i = 0; i < objectPaths.size(); i++) {
		this.mapObjects(new ArrayList<>(objectPaths), this.transformationPaths.get(0));
		//}
		//return this.denotatorManager.getCurrentNewPaths();
		return this.denotatorManager.getPathResults();
	}
	
	private void mapObjects(List<DenotatorPath> objectPaths, TransformationPaths transformationPaths) {
		//TODO check if still necessary
		objectPaths = this.denotatorManager.sortAndReverse(objectPaths);
		
		Iterator<DenotatorPath> objectPathsIterator = objectPaths.iterator();
		if (objectPathsIterator.hasNext()) {
			DenotatorPath firstOfNextSiblings = objectPathsIterator.next();
			while (firstOfNextSiblings != null) {
				firstOfNextSiblings = this.mapAndAddNextSiblings(firstOfNextSiblings, objectPathsIterator, transformationPaths);
			}
		}
	}
	
	private DenotatorPath mapAndAddNextSiblings(DenotatorPath firstSiblingPath, Iterator<DenotatorPath> objectPathsIterator, TransformationPaths transformationPaths) {
		//PerformanceCheck.startTask(".first_sib");
		List<Denotator> siblings = new ArrayList<>();
		List<DenotatorPath> siblingsPaths = new ArrayList<>();
		
		Denotator firstSibling = this.denotatorManager.getAbsoluteObject(firstSiblingPath);
		if (firstSibling != null) {
			siblingsPaths.add(firstSiblingPath);
			siblings.add(firstSibling);
		}
		DenotatorPath siblingsAnchorPath = firstSiblingPath.getAnchorPath();
		ModuleMorphism siblingsMorphism = this.morphism;
		if (this.relative) {
			Denotator siblingsAnchor = this.denotatorManager.getAbsoluteObject(siblingsAnchorPath);
			siblingsMorphism = this.generateRelativeMorphism(this.extractValues(siblingsAnchor, transformationPaths));
		}
		
		DenotatorPath currentSiblingPath = firstSiblingPath;
		while (objectPathsIterator.hasNext()) {
			currentSiblingPath = objectPathsIterator.next();
			//PerformanceCheck.startTask(".next_sibs");
			if (currentSiblingPath.isDirectSatelliteOf(siblingsAnchorPath)) {
				Denotator currentSibling = this.denotatorManager.getAbsoluteObject(currentSiblingPath);
				if (currentSibling != null) {
					siblingsPaths.add(currentSiblingPath);
					siblings.add(currentSibling);
				}
			} else {
				this.mapAndReplaceOrAdd(siblings, siblingsAnchorPath, siblingsPaths, siblingsMorphism, transformationPaths);
				return currentSiblingPath;
			}
		}
		this.mapAndReplaceOrAdd(siblings, siblingsAnchorPath, siblingsPaths, siblingsMorphism, transformationPaths);
		return null;
	}
	
	/*
	 * Maps the given objects and adds them to the given anchorPath (they should thus originally be siblings).
	 * Returns a list with all the 
	 */
	private void mapAndReplaceOrAdd(List<Denotator> objects, DenotatorPath anchorPath, List<DenotatorPath> siblingsPaths, ModuleMorphism morphism, TransformationPaths transformationPaths) {
		if (objects.size() > 0) {
			List<Denotator> mappedObjects = new ArrayList<>();
			ArbitraryDenotatorMapper mapper = new ArbitraryDenotatorMapper(morphism, transformationPaths);
			for (int i = 0; i < objects.size(); i++) {
				Denotator currentObject = objects.get(i);
				try {
					mappedObjects.add(mapper.getMappedDenotator(currentObject));
				} catch (LatrunculusCheckedException e) {
					e.printStackTrace();
				}
			}
			if (!this.copyAndMap) {
				this.denotatorManager.replaceSiblingObjects(mappedObjects, siblingsPaths);
			} else {
				//TODO: ADD THEM AS THE SAME TYPE AS THEIR ORIGINAL!! MODULATOR OR SATELLITE! FIGURE OUT POWERSET INDEX!!
				DenotatorPath powersetPath = anchorPath.getPowersetPath(0);
				this.denotatorManager.addObjectsToParent(mappedObjects, powersetPath);
			}
		}
	}
	
	private ModuleMorphism generateRelativeMorphism(double[] anchorLocation) {
		RMatrix identity = new RMatrix(new double[][]{{1,0},{0,1}});
		List<Real> list1 = new ArrayList<>(2);
		list1.add(new Real(-anchorLocation[0]));
		list1.add(new Real(-anchorLocation[1]));
		List<Real> list2 = new ArrayList<>(2);
		list2.add(new Real(anchorLocation[0]));
		list2.add(new Real(anchorLocation[1]));
		ModuleMorphism relativeMorphism = this.morphism;
		try {
			relativeMorphism = relativeMorphism.compose(AffineFreeMorphism.make(RRing.ring, identity, new Vector<>(RRing.ring, list1)));
			relativeMorphism = AffineFreeMorphism.make(RRing.ring, identity, new Vector<>(RRing.ring, list2)).compose(relativeMorphism);
		} catch (CompositionException e) { e.printStackTrace(); } //TODO log vs printstacktrace
		return relativeMorphism;
	}
	
	//TODO: WOOOO REMOVE THIS AND MAKE OBJECT GENERATOR WORK!!!
	private double[] extractValues(Denotator object, TransformationPaths transformationPaths) {
		double v1 = 0, v2 = 0;
		v1 = this.denotatorManager.getObjectGenerator().getDoubleValue(object, transformationPaths.getDomainPath(0, object));
		v2 = this.denotatorManager.getObjectGenerator().getDoubleValue(object, transformationPaths.getDomainPath(1, object));
		return new double[] {v1, v2};
	}

}
