package org.rubato.rubettes.bigbang.model.denotators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.rubato.rubettes.bigbang.model.OperationPathResults;
import org.rubato.rubettes.util.DenotatorPath;

public class BigBangWallpaper {
	
	private BigBangDenotatorManager denotatorManager;
	private Denotator compositionBeforeWallpaper;
	private Set<DenotatorPath> motif;
	private List<BigBangWallpaperDimension> dimensions;
	
	public BigBangWallpaper(BigBangDenotatorManager denotatorManager, Set<DenotatorPath> motif) {
		this.denotatorManager = denotatorManager;
		this.compositionBeforeWallpaper = denotatorManager.getComposition();
		this.motif = motif;
		this.dimensions = new ArrayList<BigBangWallpaperDimension>();
	}
	
	public Denotator getCompositionBeforeWallpaper() {
		return this.compositionBeforeWallpaper;
	}
	
	public void addDimension(int rangeFrom, int rangeTo) {
		this.dimensions.add(new BigBangWallpaperDimension(rangeFrom, rangeTo));
	}
	
	public void addTransformationToLastDimension(BigBangTransformation transformation) {
		transformation.setCopyAndMap(true);
		this.dimensions.get(dimensions.size()-1).addTransformation(transformation);
	}
	
	public OperationPathResults update() {
		Set<DenotatorPath> currentMotif = this.motif;
		//records the path results of the last transformation of the last dimension which need to be returned
		OperationPathResults lastTransformationResults = null;
		for (int i = 0; i < this.dimensions.size(); i++) {
			if (i == this.dimensions.size()-1) {
				lastTransformationResults = new OperationPathResults();
			}
			BigBangWallpaperDimension currentDimension = this.dimensions.get(i);
			currentMotif = this.mapDimension(currentMotif, currentDimension, lastTransformationResults);
		}
		return lastTransformationResults;
	}
	
	private Set<DenotatorPath> mapDimension(Set<DenotatorPath> currentPaths, BigBangWallpaperDimension dimension, OperationPathResults lastTransformationResults) {
		int rangeFrom = dimension.getRangeFrom();
		int rangeTo = dimension.getRangeTo();
		
		//keeps track of all new paths generated by this dimensions
		OperationPathResults dimensionResults = new OperationPathResults();
		dimensionResults.setNewPaths(new TreeSet<DenotatorPath>(currentPaths));
		
		int i = 0;
		if (rangeFrom > 0) {
			while (i < rangeFrom) {
				currentPaths = this.mapIteration(currentPaths, dimension, false, false, dimensionResults, null);
				i++;
			}
			//treat moved motif as new motif..
			dimensionResults.resetChangedAndRemovedPaths();
		} else {
			while (i > rangeFrom) {
				currentPaths = this.mapIteration(currentPaths, dimension, false, true, dimensionResults, lastTransformationResults);
				i--;
			}
		}
		
		while (i < rangeTo) {
			currentPaths = this.mapIteration(currentPaths, dimension, true, false, dimensionResults, lastTransformationResults);
			i++;
		}
		
		//all paths created by this dimension will be the motif for the next dimension
		return this.getSetWithoutSatellites((TreeSet<DenotatorPath>)dimensionResults.getNewPaths());
	}
	
	private Set<DenotatorPath> mapIteration(Set<DenotatorPath> currentPaths, BigBangWallpaperDimension dimension, boolean copyAndMap, boolean inverse, OperationPathResults dimensionResults, OperationPathResults lastTransformationResults) {
		List<BigBangTransformation> transformations = new ArrayList<BigBangTransformation>(dimension.getTransformations());
		if (inverse) {
			Collections.reverse(transformations);
		}
		OperationPathResults iterationPathResults = new OperationPathResults();
		for (int i = 0; i < transformations.size(); i++) {
			//System.out.println(currentPaths);
			BigBangTransformation currentTransformation = transformations.get(i); 
			if (inverse) {
				currentTransformation = currentTransformation.inverse();
			}
			currentTransformation.setCopyAndMap(copyAndMap && i == 0);
			OperationPathResults currentPathResults = new BigBangMapper(this.denotatorManager, currentTransformation).mapCategorizedObjects(currentPaths);
			iterationPathResults.updatePaths(currentPathResults);
			if (dimensionResults != null) {
				dimensionResults.updatePaths(currentPathResults);
			}
			//if last transformation of last dimension, record changed paths
			if (lastTransformationResults != null && i == transformations.size()-1) {
				lastTransformationResults.updatePaths(currentPathResults);
			}
			//if an anchor is copied, new paths include its satellites. need to be removed in order to yield motif
			currentPaths = this.getSetWithoutSatellites((TreeSet<DenotatorPath>)iterationPathResults.getNewPaths());
			if (currentPaths.isEmpty()) {
				currentPaths = dimensionResults.getNewPaths();
			}
		}
		//return new paths of created iteration (motif for next iteration)
		return currentPaths;
	}
	
	/*
	 * returns a copy of the given set without all satellites whose anchors are present in the set
	 */
	private Set<DenotatorPath> getSetWithoutSatellites(TreeSet<DenotatorPath> paths) {
		Set<DenotatorPath> anchorPaths = new TreeSet<DenotatorPath>();
		Iterator<DenotatorPath> pathsIterator = paths.descendingIterator();
		while (pathsIterator.hasNext()) {
			DenotatorPath currentPath = pathsIterator.next();
			DenotatorPath currentAnchor = currentPath.getAnchorPath();
			if (currentAnchor == null || !paths.contains(currentAnchor)) {
				anchorPaths.add(currentPath);
			}
		}
		return anchorPaths;
	}
	
	public String toString() {
		return this.dimensions.toString();
	}

}
