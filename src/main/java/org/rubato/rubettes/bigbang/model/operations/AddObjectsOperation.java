package org.rubato.rubettes.bigbang.model.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.rubato.rubettes.bigbang.model.BigBangModel;
import org.rubato.rubettes.bigbang.model.OperationPathResults;
import org.rubato.rubettes.util.DenotatorPath;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.w3c.dom.Element;

public class AddObjectsOperation extends AbstractOperation {
	
	private final int MAX_DISTANCE_FOR_UNADDING = 5;
	
	private List<DenotatorPath> definitePowersetPaths;
	private List<Map<DenotatorPath,Double>> definitePathsWithValues;
	private List<DenotatorPath> previewedPowersetPaths;
	private List<Map<DenotatorPath,Double>> previewedPathsWithValues;
	private List<DenotatorPath> modifiedPowersetPaths;
	private List<List<Map<DenotatorPath,Double>>> modifiedPathsWithValues;
	private Form objectForm;
	
	protected AddObjectsOperation(BigBangModel model, AddObjectsOperation other) {
		this(model, new ArrayList<Map<DenotatorPath,Double>>(other.definitePathsWithValues), 
				new ArrayList<DenotatorPath>(other.definitePowersetPaths));
	}
	
	public AddObjectsOperation(BigBangModel model, List<Map<DenotatorPath,Double>> pathsWithValues, List<DenotatorPath> powersetPaths) {
		this(model, pathsWithValues, powersetPaths, false);
	}
	
	public AddObjectsOperation(BigBangModel model, List<Map<DenotatorPath,Double>> pathsWithValues, List<DenotatorPath> powersetPaths, boolean inPreviewMode) {
		super(model);
		this.init();
		this.setPathsAndValues(pathsWithValues, powersetPaths, inPreviewMode);
		this.updateOperation();
	}
	
	public AddObjectsOperation(BigBangModel model, XMLReader reader, Element element) {
		super(model, reader, element);
		this.init();
		this.fromXML(reader, element);
		this.updateOperation();
	}
	
	private void init() {
		this.minModRatio = 0.0;
		this.maxModRatio = 1.0;
		this.isSplittable = true;
		this.definitePowersetPaths = new ArrayList<DenotatorPath>();
		this.previewedPowersetPaths = new ArrayList<DenotatorPath>();
		this.definitePathsWithValues = new ArrayList<Map<DenotatorPath,Double>>();
		this.previewedPathsWithValues = new ArrayList<Map<DenotatorPath,Double>>();
	}
	
	private void setPathsAndValues(List<Map<DenotatorPath,Double>> pathsWithValues, List<DenotatorPath> powersetPaths, boolean inPreviewMode) {
		if (powersetPaths != null && !powersetPaths.isEmpty()) {
			this.setObjectForm(powersetPaths.get(0));
			this.addObjects(pathsWithValues, powersetPaths, inPreviewMode);
		}
	}
	
	private void setObjectForm(DenotatorPath powersetPath) {
		if (powersetPath != null) {
			this.objectForm = powersetPath.getChildPath(0).getEndForm();
		} else {
			this.objectForm = this.model.getComposition().getForm();
		}
	}
	
	/*
	 * adds the adjusted the number of objects according to this.modificationRatio
	 */
	protected void updateOperation() {
		this.modifiedPowersetPaths = new ArrayList<DenotatorPath>();
		this.modifiedPathsWithValues = new ArrayList<List<Map<DenotatorPath,Double>>>();
		int totalNumberOfObjects = this.definitePathsWithValues.size() + this.previewedPathsWithValues.size();
		int modifiedNumberOfObjects = (int)Math.round(this.modificationRatio*totalNumberOfObjects);
		for (int i = 0; i < modifiedNumberOfObjects; i++) {
			if (i < this.definitePathsWithValues.size()) {
				this.addPathsWithValuesToModifiedList(this.definitePowersetPaths.get(i), this.definitePathsWithValues.get(i));
			} else {
				int index = i-this.definitePathsWithValues.size();
				this.addPathsWithValuesToModifiedList(this.previewedPowersetPaths.get(index), this.previewedPathsWithValues.get(index));
			}
		}
	}
	
	public List<AbstractOperation> getSplitOperations(double ratio) {
		List<AbstractOperation> splitOperations = new ArrayList<AbstractOperation>();
		int firstNumberOfObjects = (int)Math.round(ratio*this.definitePathsWithValues.size());
		splitOperations.add(new AddObjectsOperation(this.model,
				this.definitePathsWithValues.subList(0, firstNumberOfObjects),
				this.definitePowersetPaths.subList(0, firstNumberOfObjects)));
		splitOperations.add(new AddObjectsOperation(this.model,
				this.definitePathsWithValues.subList(firstNumberOfObjects, this.definitePathsWithValues.size()),
				this.definitePowersetPaths.subList(firstNumberOfObjects, this.definitePathsWithValues.size())));
		return splitOperations;
	}

	private void addPathsWithValuesToModifiedList(DenotatorPath powersetPath, Map<DenotatorPath,Double> pathsWithValues) {
		if (!this.modifiedPowersetPaths.contains(powersetPath)) {
			this.modifiedPowersetPaths.add(powersetPath);
			this.modifiedPathsWithValues.add(new ArrayList<Map<DenotatorPath,Double>>());
		}
		this.modifiedPathsWithValues.get(this.modifiedPowersetPaths.indexOf(powersetPath)).add(pathsWithValues);
	}
	
	/*would have been cool, but can be done later...
	private List<Map<DenotatorPath,Double>> lagrangePredictValues(List<Map<DenotatorPath,Double>> pathsWithValues, int numberOfValues) {
		List<Map<DenotatorPath,Double>> predictedValues = new ArrayList<Map<DenotatorPath,Double>>();
		//add necessary maps
		for (int i = 0; i < numberOfValues; i++) {
			predictedValues.add(new TreeMap<DenotatorPath,Double>());
		}
		//fill with predicted values
		for (DenotatorPath currentPath : pathsWithValues.get(0).keySet()) {
			List<Double> currentValues = new ArrayList<Double>();
			for (Map<DenotatorPath,Double> currentPathsWithValues : pathsWithValues) {
				currentValues.add(currentPathsWithValues.get(currentPath));
			}
			List<Double> currentPredictedValues = GeometryTools.lagrangePredictValues(currentValues, numberOfValues);
			//add values to maps
			for (int i = 0; i < currentPredictedValues.size(); i++) {
				predictedValues.get(i).put(currentPath, currentPredictedValues.get(i));
			}
		}
		return predictedValues;
	}*/
	
	public Form getObjectForm() {
		return this.objectForm;
	}
	
	public boolean addObjects(List<Map<DenotatorPath,Double>> pathsWithValues, List<DenotatorPath> powersetPaths, boolean inPreviewMode) {
		if (this.objectForm == null && !powersetPaths.isEmpty()) {
			this.setObjectForm(powersetPaths.get(0));
		}
		//reset previewed objects and even remove the definite object if the new object is on the topmost level
		this.previewedPowersetPaths = new ArrayList<DenotatorPath>();
		this.previewedPathsWithValues = new ArrayList<Map<DenotatorPath,Double>>();
		if (powersetPaths.size() > 0 && powersetPaths.get(0) == null) {
			this.definitePowersetPaths = new ArrayList<DenotatorPath>();
			this.definitePathsWithValues = new ArrayList<Map<DenotatorPath,Double>>();
		}
		//if on top level or same form, add objects
		if (pathsWithValues.isEmpty() || (powersetPaths.get(0) == null || powersetPaths.get(0).getChildPath(0).getEndForm().equals(this.objectForm))) {
			if (inPreviewMode) {
				this.addObjects(pathsWithValues, powersetPaths, this.previewedPathsWithValues, this.previewedPowersetPaths);
			} else {
				this.addObjects(pathsWithValues, powersetPaths, this.definitePathsWithValues, this.definitePowersetPaths);
			}
			this.updateOperation();
			return true;
		}
		return false;
	}
	
	private void addObjects(List<Map<DenotatorPath,Double>> pathsWithValues, List<DenotatorPath> powersetPaths, List<Map<DenotatorPath,Double>> thisPathsWithValues, List<DenotatorPath> thisPowersetPaths) {
		for (int i = 0; i < pathsWithValues.size(); i++) {
			thisPowersetPaths.add(powersetPaths.get(i));
			thisPathsWithValues.add(pathsWithValues.get(i));
		}
	}
	
	public void unAddObjects(Set<Map<DenotatorPath,Double>> pathsWithValues) {
		for (Map<DenotatorPath,Double> currentPathsWithValues : pathsWithValues) {
			this.removeClosestObject(currentPathsWithValues);
		}
		this.updateOperation();
	}
	
	private void removeClosestObject(Map<DenotatorPath,Double> pathsWithValues) {
		int closestObjectIndex = -1;
		double shortestDistance = Double.MAX_VALUE;
		for (int i = 0; i < this.definitePathsWithValues.size(); i++) {
			Map<DenotatorPath,Double> currentPathsWithValues = this.definitePathsWithValues.get(i);
			//calculate Euclidean distance
			double currentDistance = 0;
			for (DenotatorPath currentPath : pathsWithValues.keySet()) {
				Double currentValue = currentPathsWithValues.get(currentPath);
				if (currentValue != null) {
					currentDistance += Math.pow(currentValue-pathsWithValues.get(currentPath), 2);
				}
			}
			currentDistance = Math.sqrt(currentDistance);
			if (currentDistance < shortestDistance) {
				shortestDistance = currentDistance;
				closestObjectIndex = i;
			}
		}
		if (closestObjectIndex >= 0 && shortestDistance <= MAX_DISTANCE_FOR_UNADDING) {
			this.definitePathsWithValues.remove(closestObjectIndex);
			this.definitePowersetPaths.remove(closestObjectIndex);
		}
	}
	
	public OperationPathResults execute() {
		OperationPathResults pathResults = new OperationPathResults();
		for (int i = 0; i < this.modifiedPowersetPaths.size(); i++) {
			pathResults.addPaths(this.model.getDenotatorManager().addObjects(this.modifiedPowersetPaths.get(i), this.modifiedPathsWithValues.get(i)));
		}
		return pathResults;
	}
	
	@Override
	protected String getSpecificPresentationName() {
		if (this.objectForm != null) {
			String presentationName = "Add " + this.objectForm.getNameString();
			if (this.definitePathsWithValues.size() > 1 || this.previewedPathsWithValues.size() > 1) {
				presentationName += "s";
			}
			return presentationName;
		}
		return "Add";
	}
	
	private static final String OBJECT_TAG = "Object";
	private static final String VALUEPATH_TAG = "ValuePath";
	private static final String VALUE_ATTR = "value";
	
	public void toXML(XMLWriter writer) {
		super.toXML(writer);
		for (int i = 0; i < this.definitePathsWithValues.size(); i++) {
			Map<DenotatorPath,Double> currentPathsWithValues = this.definitePathsWithValues.get(i);
			DenotatorPath currentPowersetPath = this.definitePowersetPaths.get(i);
			writer.openBlock(OBJECT_TAG);
			currentPowersetPath.toXML(writer);
			for (DenotatorPath currentValuePath : currentPathsWithValues.keySet()) {
				writer.openBlock(VALUEPATH_TAG, VALUE_ATTR, currentPathsWithValues.get(currentValuePath));
				currentValuePath.toXML(writer);
				writer.closeBlock();
			}
			writer.closeBlock();
		}
	}
	
	private void fromXML(XMLReader reader, Element element) {
		List<Map<DenotatorPath,Double>> pathsWithValues = new ArrayList<Map<DenotatorPath,Double>>();
		List<DenotatorPath> powersetPaths = new ArrayList<DenotatorPath>();
		Element currentObjectElement = XMLReader.getChild(element, OBJECT_TAG);
		while (currentObjectElement != null) {
			Element currentPowersetPathElement = XMLReader.getChild(currentObjectElement, DenotatorPath.DENOTATOR_PATH_TAG);
			DenotatorPath currentPowersetPath = new DenotatorPath(reader, currentPowersetPathElement);
			Map<DenotatorPath,Double> currentPathsWithValues = new TreeMap<DenotatorPath,Double>();
			Element currentValuePathElement = XMLReader.getNextSibling(currentPowersetPathElement, VALUEPATH_TAG);
			while (currentValuePathElement != null) {
				double currentValue = XMLReader.getRealAttribute(currentValuePathElement, VALUE_ATTR, 0);
				Element currentPathElement = XMLReader.getChild(currentValuePathElement, DenotatorPath.DENOTATOR_PATH_TAG);
				DenotatorPath currentPath = new DenotatorPath(reader, currentPathElement);
				currentPathsWithValues.put(currentPath, currentValue);
				currentValuePathElement = XMLReader.getNextSibling(currentValuePathElement, VALUEPATH_TAG);
			}
			pathsWithValues.add(currentPathsWithValues);
			powersetPaths.add(currentPowersetPath);
			currentObjectElement = XMLReader.getNextSibling(currentObjectElement, OBJECT_TAG);
		}
		this.setPathsAndValues(pathsWithValues, powersetPaths, false);
	}

}
