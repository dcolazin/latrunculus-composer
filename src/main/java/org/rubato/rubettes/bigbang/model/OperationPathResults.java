package org.rubato.rubettes.bigbang.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.rubato.rubettes.util.DenotatorPath;

public class OperationPathResults {
	
	private Set<DenotatorPath> newPaths;
	private Set<DenotatorPath> removedPaths;
	private BidiMap<DenotatorPath, DenotatorPath> changedPaths;
	private Set<DenotatorPath> pathsToBeSelected;
	
	public OperationPathResults() {
		this.newPaths = new TreeSet<>();
		this.removedPaths = new TreeSet<>();
		this.changedPaths = new TreeBidiMap<>();
		this.pathsToBeSelected = new TreeSet<>();
	}
	
	public OperationPathResults(Set<DenotatorPath> pathsToBeSelected) {
		this();
		this.pathsToBeSelected = pathsToBeSelected;
	}
	
	public OperationPathResults(Set<DenotatorPath> newPaths, Set<DenotatorPath> pathsToBeSelected) {
		this(pathsToBeSelected);
		if (newPaths != null) {
			this.newPaths = newPaths;
		}
	}
	
	public OperationPathResults(Map<DenotatorPath, DenotatorPath> changedPaths) {
		this();
		this.changedPaths = new TreeBidiMap<>(changedPaths);
	}
	
	public OperationPathResults(Set<DenotatorPath> newPaths, Set<DenotatorPath> removedPaths, Map<DenotatorPath, DenotatorPath> changedPaths) {
		this(changedPaths);
		if (newPaths != null) {
			this.newPaths = newPaths;
		}
		if (removedPaths != null) {
			this.removedPaths = removedPaths;
		}
	}
	
	public void addPaths(OperationPathResults paths) {
		this.newPaths.addAll(paths.getNewPaths());
		this.changedPaths.putAll(paths.getChangedPaths());
		this.pathsToBeSelected.addAll(paths.getPathsToBeSelected());
	}
	
	public void updatePaths(OperationPathResults pathResults) {
		this.updatePaths(pathResults.getChangedPaths(), pathResults.getNewPaths(), null);
	}
	
	public void updatePaths(List<DenotatorPath> previousPaths, List<DenotatorPath> currentPaths, Collection<DenotatorPath> newPaths) {
		Map<DenotatorPath,DenotatorPath> changedPaths = new TreeMap<>();
		Set<DenotatorPath> removedPaths = new TreeSet<>();
		for (int i = previousPaths.size()-1; i >= 0; i--) {
			DenotatorPath keyPath = previousPaths.get(i);
			DenotatorPath valuePath = currentPaths.get(i);
			if (valuePath != null) {
				changedPaths.put(keyPath, valuePath);
			} else {
				removedPaths.add(keyPath);
			}
		}
		this.updatePaths(changedPaths, newPaths, removedPaths);
	}
	
	private void updatePaths(Map<DenotatorPath,DenotatorPath> changedPaths, Collection<DenotatorPath> newPaths, Set<DenotatorPath> removedPaths) {
		//need to build new sets and maps separately so that no values are changed back and forth,
		//e.g. in cases of two paths being switched
		Set<DenotatorPath> newNewPaths = new TreeSet<>();
		BidiMap<DenotatorPath,DenotatorPath> newChangedPaths = new TreeBidiMap<>(this.changedPaths);
		
		//go through all newPaths and see if they or any of their anchors are in the given changedPaths
		//replace them or their anchor path and add to newNewPath
		Set<DenotatorPath> newPathsToRemove = new TreeSet<>();
		for (DenotatorPath currentNewPath : this.newPaths) {
			DenotatorPath currentAnchorPath = currentNewPath;
			while (currentAnchorPath != null) {
				if (changedPaths.containsKey(currentAnchorPath)) {
					DenotatorPath newAnchorPath = changedPaths.get(currentAnchorPath);
					newPathsToRemove.add(currentNewPath);
					//currentNewPath itself changed
					if (currentAnchorPath.size() == currentNewPath.size()) {
						newNewPaths.add(newAnchorPath);
					//some anchor path of currentNewPath changed
					} else {
						newNewPaths.add(currentNewPath.changeBeginning(newAnchorPath, currentAnchorPath.size()));
					}
				}
				currentAnchorPath = currentAnchorPath.getAnchorPath();
			}
		}
		//System.out.println("r " +newPathsToRemove);
		this.newPaths.removeAll(newPathsToRemove);
			
		//adjust changedPaths according to the given changedPaths
		for (DenotatorPath keyPath : changedPaths.keySet()) {
			DenotatorPath valuePath = changedPaths.get(keyPath);
			if (this.changedPaths.containsValue(keyPath)) {
				DenotatorPath oldKey = this.changedPaths.getKey(keyPath);
				if (!oldKey.equals(valuePath)) {
					//adjust previous changed path
					newChangedPaths.put(oldKey, valuePath);
				} else {
					//remove if results in x=x
					newChangedPaths.remove(oldKey);
				}
			} else if (!keyPath.equals(valuePath)){
				newChangedPaths.put(keyPath, valuePath);
			}
		}
		//add unchanged newPaths to newNewPaths
		newNewPaths.addAll(this.newPaths);
		this.newPaths = newNewPaths;
		this.changedPaths = newChangedPaths;
		
		//System.out.println("p " +this.newPaths);
		
		//only add new and removed paths after changes made!!
		if (newPaths != null) {
			this.newPaths.addAll(newPaths);
			//System.out.println("p2 " +this.newPaths);
			//this.lastNewPaths.addAll(newPaths);
		}
		if (removedPaths != null) {
			this.removedPaths.addAll(removedPaths);
		}
	}
	
	public void resetChangedAndRemovedPaths() {
		this.changedPaths = new TreeBidiMap<>();
		this.removedPaths = new TreeSet<>();
	}
	
	public void setNewPaths(Set<DenotatorPath> newPaths) {
		this.newPaths = newPaths;
	}
	
	public Set<DenotatorPath> getNewPaths() {
		return this.newPaths;
	}
	
	public Set<DenotatorPath> getRemovedPaths() {
		return this.removedPaths;
	}
	
	public Map<DenotatorPath, DenotatorPath> getChangedPaths() {
		return this.changedPaths;
	}
	
	public Set<DenotatorPath> getPathsToBeSelected() {
		return this.pathsToBeSelected;
	}
	
	public String toString() {
		return this.newPaths + " " + this.changedPaths + " " + this.removedPaths;
	}

}
