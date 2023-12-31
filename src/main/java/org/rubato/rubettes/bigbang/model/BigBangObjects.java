package org.rubato.rubettes.bigbang.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.rubato.rubettes.bigbang.controller.BigBangController;
import org.rubato.rubettes.bigbang.model.operations.AbstractOperation;
import org.rubato.rubettes.util.CoolFormRegistrant;
import org.rubato.rubettes.util.DenotatorObject;
import org.rubato.rubettes.util.DenotatorObjectConfiguration;
import org.rubato.rubettes.util.DenotatorPath;
import org.rubato.rubettes.util.FormValueFinder;

public class BigBangObjects {
	
	private Form baseForm;
	private FormValueFinder finder;
	private List<String> coordinateSystemValueNames;
	//number for each object type, null if no satellites possible for the type
	private List<Integer> maxSatelliteLevels;
	//keeps track of all objects added at any time, with the adding operation as a key
	private HashMap<AbstractOperation,Set<BigBangObject>> objects;
	//keeps track of all objects currently existing at any state, with the following operation as a key
	private HashMap<AbstractOperation,Map<DenotatorPath,BigBangObject>> objectsMaps;
	private BigBangLayers layers;
	private List<Double> minValues, maxValues;
	
	public BigBangObjects(Form baseForm) {
		this.baseForm = baseForm;
		
		this.finder = new FormValueFinder(this.baseForm, true);
		this.resetMaxSatelliteLevels();
		
		List<String> coordinateSystemValueNames = new ArrayList<String>(finder.getCoordinateSystemValueNames());
		if (this.finder.formAllowsForSatellites()) {
			coordinateSystemValueNames.add(DenotatorValueExtractor.SATELLITE_LEVEL);
			coordinateSystemValueNames.add(DenotatorValueExtractor.SIBLING_NUMBER);
		}
		if (this.finder.formContainsColimits()) {
			coordinateSystemValueNames.add(DenotatorValueExtractor.COLIMIT_INDEX);
		}
		this.coordinateSystemValueNames = coordinateSystemValueNames;
		this.layers = new BigBangLayers();
		
		this.clearObjects();
		this.minValues = new ArrayList<Double>();
		this.maxValues = new ArrayList<Double>();
	}
	
	public void setController(BigBangController controller) {
		this.layers.setController(controller);
	}
	
	public void clearObjects() {
		this.objects = new HashMap<AbstractOperation,Set<BigBangObject>>();
		this.objectsMaps = new HashMap<AbstractOperation,Map<DenotatorPath,BigBangObject>>();
		this.resetMaxSatelliteLevels();
	}
	
	public Integer getMaxSatelliteLevel(int objectIndex) {
		return this.maxSatelliteLevels.get(objectIndex);
	}
	
	public List<String> getCoordinateSystemValueNames() {
		return this.coordinateSystemValueNames;
	}
	
	public int getNumberOfNonAnalyticalCoordinateSystemValues() {
		return this.finder.getCoordinateSystemValueNames().size();
	}
	
	/**
	 * @return true in case these objects contain any time-critical denotators
	 */
	public boolean arePlayedBackInTime() {
		//to be extended later...
		return this.finder.getCoordinateSystemValueNames().contains(CoolFormRegistrant.ONSET_NAME)
				|| this.finder.getCoordinateSystemValueNames().contains(CoolFormRegistrant.BEAT_CLASS_NAME);
	}
	
	public void setMinMaxValues(List<Double> minValues, List<Double> maxValues) {
		this.minValues = minValues;
		this.maxValues = maxValues;
	}
	
	public List<Double> getMinValues() {
		return this.minValues;
	}
	
	public List<Double> getMaxValues() {
		return this.maxValues;
	}
	
	public void updateValues() {
		for (BigBangObject currentObject : this.getAllObjects()) {
			currentObject.updateValues();
		}
	}
	
	public Form getBaseForm() {
		return this.baseForm;
	}
	
	public boolean baseFormAllowsForSatellites() {
		return this.finder.formAllowsForSatellites();
	}
	
	public boolean baseFormContainsColimits() {
		return this.finder.formContainsColimits();
	}
	
	public int getNumberOfColimits(int objectIndex) {
		return this.finder.getObjectAt(objectIndex).getColimits().size();
	}
	
	public void updateMaxSatelliteLevels(BigBangObject object) {
		int objectIndex = this.finder.indexOf(object.getTopDenotatorPath().getEndForm());
		Integer currentLevel = this.maxSatelliteLevels.get(objectIndex);
		if (currentLevel != null) {
			int currentMax = Math.max(currentLevel, (int)object.getNthValue(DenotatorValueExtractor.SATELLITE_LEVEL, 0).doubleValue());
			this.maxSatelliteLevels.set(objectIndex, currentMax);
		}
	}
	
	public void resetMaxSatelliteLevels() {
		this.maxSatelliteLevels = new ArrayList<Integer>();
		for (Boolean currentObjectCanBeSatellite : this.finder.getObjectsCanBeSatellites()) {
			if (currentObjectCanBeSatellite) {
				this.maxSatelliteLevels.add(-1);
			} else {
				this.maxSatelliteLevels.add(null);
			}
		}
	}
	
	public TreeSet<BigBangObject> getAllObjects() {
		TreeSet<BigBangObject> objectUnion = new TreeSet<BigBangObject>();
		for (AbstractOperation currentEdit : this.objects.keySet()) {
			objectUnion.addAll(this.objects.get(currentEdit));
		}
		return objectUnion;
	}
	
	public Set<BigBangObject> getObjectsAt(AbstractOperation operation) {
		if (this.objectsMaps.containsKey(operation)) {
			return new TreeSet<BigBangObject>(this.objectsMaps.get(operation).values());
		} else if (this.objectsMaps.containsKey(null)) {
			return new TreeSet<BigBangObject>(this.objectsMaps.get(null).values());
		}
		return null;
	}
	
	//returns the object that has the given path at the given operation, null if there is none
	private BigBangObject getObject(AbstractOperation operation, DenotatorPath path) {
		if (path != null && this.objectsMaps.containsKey(operation)) {
			return this.objectsMaps.get(operation).get(path);
		}
		return null;
	}
	
	//returns the object that has the given path at the final state, null if there is none
	public BigBangObject getObject(DenotatorPath path) {
		if (this.objectsMaps.containsKey(null)) {
			return this.objectsMaps.get(null).get(path);
		}
		return null;
	}
	
	public synchronized void removeOperation(AbstractOperation operation) {
		for (BigBangObject currentObject : this.getAllObjects()) {
			currentObject.removeOperation(operation);
		}
		this.objectsMaps.remove(operation);
	}
	
	/**
	 * Updates the paths of all objects that existed at previousOperation and were changed according to the given
	 * changedPaths. Sets the new paths at the given operation.
	 */
	public synchronized void updatePaths(AbstractOperation previousOperation, AbstractOperation operation, OperationPathResults pathResults) {
		//System.out.println("UP " + previousOperation + " " + operation + " " + pathResults.getNewPaths() + " " + pathResults.getChangedPaths() + " " + pathResults.getRemovedPaths());
		
		if (this.objectsMaps.containsKey(operation)) {
			this.objectsMaps.get(operation).clear();
		}
		
		if (this.objectsMaps.containsKey(previousOperation)) {
			this.removeObjects(previousOperation, operation, pathResults);
			this.updateObjects(previousOperation, operation, pathResults);
		}
		
		this.addObjects(previousOperation, operation, pathResults);
		//System.out.println("END " + this.getObjectsAt(operation).size());
	}
	
	private void removeObjects(AbstractOperation previousOperation, AbstractOperation operation, OperationPathResults pathResults) {
		for (BigBangObject currentObject : this.objectsMaps.get(previousOperation).values()) {
			DenotatorPath previousPath = currentObject.getTopDenotatorPathAt(previousOperation);
			//REMOVE if in removedPaths
			if (pathResults.getRemovedPaths().contains(previousPath)) {
				this.removeObject(operation, currentObject);
			}
		}
	}
	
	private void updateObjects(AbstractOperation previousOperation, AbstractOperation operation, OperationPathResults pathResults) {
		for (BigBangObject currentObject : this.objectsMaps.get(previousOperation).values()) {
			DenotatorPath previousPath = currentObject.getTopDenotatorPathAt(previousOperation);
			//CHANGE if in changed paths
			if (pathResults.getChangedPaths().keySet().contains(previousPath)) {
				DenotatorPath currentValuePath = pathResults.getChangedPaths().get(previousPath);
				BigBangObject parent = this.getObject(operation, currentValuePath.getAnchorPath());
				this.updateObject(currentObject, parent, operation, currentValuePath);
			//KEEP the same if not in changedPath (may have been changed before)
			} else if (!pathResults.getRemovedPaths().contains(previousPath)) {
				BigBangObject previousParent = currentObject.getParentAt(previousOperation);
				this.updateObject(currentObject, previousParent, operation, previousPath);
			}
		}
	}
	
	private synchronized void addObjects(AbstractOperation previousOperation, AbstractOperation operation, OperationPathResults pathResults) {
		Set<BigBangObject> previouslyAddedObjects = new TreeSet<BigBangObject>();
		if (this.objects.containsKey(previousOperation)) {
			previouslyAddedObjects.addAll(this.objects.get(previousOperation));
		}
		Iterator<BigBangObject> previouslyAddedObjectsIterator = previouslyAddedObjects.iterator();
		
		for (DenotatorPath currentNewPath : pathResults.getNewPaths()) {
			//only add new object if it does not already exist yet (important for non-powerset-toplevels)
			if (this.objectsMaps.get(operation) == null || !this.objectsMaps.get(operation).containsKey(currentNewPath)) {
				//add as many of the objects added in previous iterations as possible
				if (previouslyAddedObjectsIterator.hasNext()) {
					BigBangObject previouslyAddedObject = previouslyAddedObjectsIterator.next();
					//strip them from their previous context
					previouslyAddedObject.removeAllChildren(operation);
					BigBangObject parent = this.getObject(operation, currentNewPath.getAnchorPath());
					this.updateObject(previouslyAddedObject, parent, operation, currentNewPath);
				//if more need to be added, create new ones
				} else {
					DenotatorPath parentPath = currentNewPath.getAnchorPath();
					if (parentPath != null && parentPath.size() > 0) {
						BigBangObject parent = this.getObject(operation, parentPath);
						this.addObject(previousOperation, operation, parent, currentNewPath);
					} else {
						this.addObject(previousOperation, operation, null, currentNewPath);
					}
				}
			}
		}
	}
	
	private void addObject(AbstractOperation previousOperation, AbstractOperation operation, BigBangObject parent, DenotatorPath path) {
		BigBangObject object = new BigBangObject(previousOperation, operation, parent, path, this.layers.get(0));
		this.addObjectToMap(object, operation, path);
		if (!this.objects.containsKey(previousOperation)) {
			this.objects.put(previousOperation, new TreeSet<BigBangObject>());
		}
		this.objects.get(previousOperation).add(object);
	}
	
	private void updateObject(BigBangObject object, BigBangObject parent, AbstractOperation operation, DenotatorPath newPath) {
		//this.removeObjectFromMap(operation, object);
		object.updatePathAndParent(operation, newPath, parent);
		this.updateObjectAndChildrenInMap(object, operation);
	}
	
	private void removeObject(AbstractOperation operation, BigBangObject objectToBeRemoved) {
		objectToBeRemoved.updatePathAndParent(operation, null, null);
		this.removeObjectFromMap(operation, objectToBeRemoved);
	}
	
	private synchronized void addObjectToMap(BigBangObject object, AbstractOperation operation, DenotatorPath newPath) {
		if (newPath != null) {
			if (!this.objectsMaps.containsKey(operation)) {
				this.objectsMaps.put(operation, new TreeMap<DenotatorPath,BigBangObject>());
			}
			this.objectsMaps.get(operation).put(newPath, object);
		}
	}
	
	//recursively updates all children too
	private void updateObjectAndChildrenInMap(BigBangObject object, AbstractOperation operation) {
		//TODO OLD ONE SHOULD BE REMOVED!!!!
		DenotatorPath newPath = object.getTopDenotatorPathAt(operation);
		//System.out.println("HEY "+ newPath + " " + object.getParent().getTopDenotatorPathAt(operation)+ " " + object);
		this.addObjectToMap(object, operation, newPath);
		Set<BigBangObject> children = object.getChildrenAt(operation);
		if (children != null) {
			for (BigBangObject currentChild : children) {
				this.updateObjectAndChildrenInMap(currentChild, operation);
			}
		}
	}
	
	private synchronized void removeObjectFromMap(AbstractOperation operation, BigBangObject objectToBeRemoved) {
		DenotatorPath previousPath = objectToBeRemoved.getTopDenotatorPathAt(operation);
		if (previousPath != null) {
			if (this.objectsMaps.containsKey(operation)) {
				this.objectsMaps.get(operation).remove(previousPath);
			}
		}
	}
	
	/**
	 * @return the number of the instance of the valueName of the value at the given index (number of previous
	 * instances + the instance itself. 1 if it is the first)
	 */
	public int getInstanceNumberOfCoordinateValueName(int coordinateSystemValueIndex) {
		if (coordinateSystemValueIndex < this.finder.getCoordinateSystemValueNames().size()) {
			return this.finder.getInstanceNumberOfCoordinateValueName(coordinateSystemValueIndex);
		}
		//in case of satellitelevel/siblingnumber
		return 0;
	}
	
	public DenotatorObjectConfiguration getObjectType(Form objectForm, DenotatorPath longestColimitPath) {
		return this.finder.getConfiguration(objectForm, longestColimitPath);
	}
	
	public DenotatorObjectConfiguration getStandardObjectType(Form objectForm) {
		return this.finder.getStandardConfiguration(objectForm);
	}
	
	public List<List<DenotatorPath>> getAllObjectConfigurationsValuePathsAt(int coordinateSystemValueIndex) {
		return this.finder.getAllObjectConfigurationsValuePathsAt(coordinateSystemValueIndex);
	}
	
	/**
	 * @return the path of the closest powerset at the given coordinateSystemValueIndex, if it is closer to the given
	 * examplePowersetPath, or the closest object as such if examplePowersetPath is null.
	 */
	public synchronized BigBangObject getClosestObject(int[] coordinateSystemValueIndices, double[] values, DenotatorPath examplePowersetPath) {
		BigBangObject closestObject = null;
		double shortestDistance = Double.MAX_VALUE;
			
		if (this.objectsMaps.containsKey(null)) {
			for (BigBangObject currentObject : this.objectsMaps.get(null).values()) {
				if (examplePowersetPath == null || (currentObject.getTopDenotatorPath().getEndForm().equals(examplePowersetPath.getTopPath().getEndForm())
						&& currentObject.getTopDenotatorPath().size() == examplePowersetPath.getTopPath().size())) {
					//calculate Euclidean distance
					double currentDistance = 0;
					for (int i = 0; i < values.length; i++) {
						String valueName = this.coordinateSystemValueNames.get(coordinateSystemValueIndices[i]);
						int nameIndex = this.getInstanceNumberOfCoordinateValueName(coordinateSystemValueIndices[i]);
						
						Double currentValue = currentObject.getNthValue(valueName, nameIndex);
						if (currentValue != null) {
							currentDistance += Math.pow(currentValue-values[i], 2);
						}
					}
					currentDistance = Math.sqrt(currentDistance);
					if (currentDistance < shortestDistance) {
						shortestDistance = currentDistance;
						closestObject = currentObject;
					}
				}
			}
		}
		return closestObject;
	}
	
	public List<Form> getObjectTypes() {
		return this.finder.getObjectForms();
	}
	
	public DenotatorObject getObjectType(int objectIndex) {
		return this.finder.getObjectAt(objectIndex);
	}
	
	public int getObjectValueIndex(int coordinateSystemValueIndex, int objectIndex, List<Integer> colimitCoordinates) {
		return this.finder.getActiveObjectValueIndex(coordinateSystemValueIndex, objectIndex, colimitCoordinates);
	}
	
	public int getObjectFirstValueIndex(SimpleForm form, int objectIndex, List<Integer> colimitCoordinates) {
		return this.finder.getActiveObjectFirstValueIndex(form, objectIndex, colimitCoordinates);
	}

}
