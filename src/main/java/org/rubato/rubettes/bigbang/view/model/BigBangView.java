package org.rubato.rubettes.bigbang.view.model;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.rubato.rubettes.bigbang.controller.BigBangController;
import org.rubato.rubettes.bigbang.model.BigBangObject;
import org.rubato.rubettes.bigbang.model.BigBangObjects;
import org.rubato.rubettes.bigbang.model.Model;
import org.rubato.rubettes.bigbang.model.denotators.TransformationPaths;
import org.rubato.rubettes.bigbang.model.denotators.TransformationProperties;
import org.rubato.rubettes.bigbang.model.graph.CompositionState;
import org.rubato.rubettes.bigbang.model.operations.AbstractOperation;
import org.rubato.rubettes.bigbang.model.operations.AbstractTransformation;
import org.rubato.rubettes.bigbang.model.operations.AddObjectsOperation;
import org.rubato.rubettes.bigbang.model.operations.AddWallpaperDimensionOperation;
import org.rubato.rubettes.bigbang.model.operations.AlterationOperation;
import org.rubato.rubettes.bigbang.model.operations.RotationTransformation;
import org.rubato.rubettes.bigbang.view.View;
import org.rubato.rubettes.bigbang.view.controller.ViewController;
import org.rubato.rubettes.bigbang.view.model.tools.DisplayTool;
import org.rubato.rubettes.bigbang.view.model.tools.SelectionTool;
import org.rubato.rubettes.bigbang.view.subview.DisplayObjects;
import org.rubato.rubettes.util.CoolFormRegistrant;
import org.rubato.rubettes.util.DenotatorPath;
import org.rubato.rubettes.util.Point;
import org.rubato.rubettes.util.Point2D;
import org.rubato.rubettes.util.PointND;
import org.rubato.rubettes.util.Rectangle2D;

public abstract class BigBangView extends Model implements View {
	
	protected BigBangController controller;
	protected ViewController viewController;
	//protected BigBangPlayer player;
	//protected BigBangRecorder recorder;
	//protected BigBangMidiReceiver midiReceiver;
	//protected BigBangMidiTransmitter midiTransmitter;
	protected FactsViewState factsViewState;
	//private DisplayModeAdapter displayMode;
	//TODO reassess what's needed here. MTView used only a fraction of this. Will Android?
	protected boolean mainOptionsVisible;
	protected boolean viewParametersVisible;
	protected Map<String,Double> standardDenotatorValues;
	protected boolean satellitesConnected;
	protected DisplayObjects displayObjects;
	protected DisplayTool displayTool;
	protected AbstractOperation selectedOperation;
	
	public BigBangView(BigBangController controller) {
		this.controller = controller;
		this.controller.addView(this);
		this.initPlayer();
		//this.player = new BigBangPlayer();
		this.initViewMVC();
		this.initViewParameterControls();
		this.initStandardDenotatorValues();
		this.setSatellitesConnected(true);
		//this.setDisplayMode(new DrawingModeAdapter(this.viewController));
		//TODO:make this automatic when displaynotes loaded!!! depending on max/min and window size
		this.factsViewState.setDisplayPosition(new Point(20, 660));
		this.factsViewState.setZoomFactors(5.0, 5.0);
		//this.initMidiAndSound();
	}
	
	public void dispose() {
		this.controller.removeView(this);
	}
	
	protected abstract void initPlayer();
	
	private void initViewMVC() {
		this.viewController = new ViewController();
		this.viewController.addModel(this);
		this.initFactsViewState();
		this.initVisibleInterface();
	}
	
	protected void initFactsViewState() {
		this.factsViewState = new FactsViewState(this.viewController, true);
	}
	
	protected abstract void initVisibleInterface();
	
	private void initViewParameterControls() {
		//this.viewParametersVisible = false;
		//this.toggleViewParametersVisible();
		this.mainOptionsVisible = true;
		this.toggleMainOptionsVisible();
	}
	
	private void initStandardDenotatorValues() {
		this.standardDenotatorValues = new TreeMap<String,Double>();
		//insert some preset standard values
		this.standardDenotatorValues.put(CoolFormRegistrant.PITCH_NAME, 60.0);
		this.standardDenotatorValues.put(CoolFormRegistrant.LOUDNESS_NAME, 120.0);
		this.standardDenotatorValues.put(CoolFormRegistrant.DURATION_NAME, 1.0);
		this.standardDenotatorValues.put(CoolFormRegistrant.PAN_NAME, 64.0);
	}
	
	/*private void initMidiAndSound() {
		//this.midiReceiver = new BigBangMidiReceiver(this.viewController, this.controller);
		this.recorder = new BigBangRecorder(this, this.player, this.controller);
		this.setTempo(BigBangPlayer.INITIAL_BPM);
		//this.firePropertyChange(ViewController.MIDI_IN, null, this.midiReceiver.getSelectedInDeviceName());
		this.firePropertyChange(ViewController.MIDI_OUT, null, this.player.getSelectedMidiOutDeviceName());
	}*/
	
	public void showWindowPreferences() {
		this.firePropertyChange(ViewController.SHOW_WINDOW_PREFERENCES, null, null);
	}
	
	/*public void setDisplayMode(DisplayModeAdapter newMode) {
		if (newMode instanceof TemporaryDisplayMode) {
			((TemporaryDisplayMode)newMode).setPreviousDisplayMode(this.displayMode);
		}
		this.displayMode = newMode;
		/*if (!(newMode instanceof AlterationCompositionSelectionMode && this.selectedOperation instanceof AlterationEdit)) {
			this.deselectOperations();
		}
		this.firePropertyChange(ViewController.DISPLAY_MODE, null, newMode);
	}
	
	public boolean inDrawingMode() {
		return this.displayMode instanceof DrawingModeAdapter;
	}*/
	
	public void toggleMainOptionsVisible() {
		boolean oldValue = this.mainOptionsVisible;
		this.mainOptionsVisible = !oldValue;
		this.firePropertyChange(ViewController.MAIN_OPTIONS_VISIBLE, oldValue, this.mainOptionsVisible);
	}
	
	public void toggleViewParametersVisible() {
		boolean oldValue = this.viewParametersVisible;
		this.viewParametersVisible = !oldValue;
		this.firePropertyChange(ViewController.VIEW_PARAMETERS_VISIBLE, oldValue, this.viewParametersVisible);
	}
	
	public void setSatellitesConnected(boolean satellitesConnected) {
		this.satellitesConnected = satellitesConnected;
		this.firePropertyChange(ViewController.SATELLITES_CONNECTED, null, this.satellitesConnected);
	}
	
	public void centerView() {
		this.firePropertyChange(ViewController.CENTER_VIEW, null, null);
	}
	
	/*public void togglePlayMode() {
		this.player.togglePlayMode();
		//this.panel.toggleTimedRepaint();
		this.firePropertyChange(ViewController.PLAY_MODE, null, this.player.isPlaying());
		//stop recording if playing stopped
		if (!this.player.isPlaying() && this.recorder.isRecording()) {
			this.toggleRecordMode();
		}
	}
	
	public void toggleRecordMode() {
		this.recorder.toggleRecordMode();
		this.firePropertyChange(ViewController.RECORD_MODE, null, this.recorder.isRecording());
		//start playing if recording started
		if (this.recorder.isRecording() && !this.player.isPlaying()) {
			this.togglePlayMode();
		}
	}*/
	
	public void toggleObjectSelection(Point2D location) {
		int selectedObjectCount = this.displayObjects.selectTopOrDeselectAllObjects(location);
		this.firePropertyChange(ViewController.OBJECT_SELECTION, null, selectedObjectCount);
	}
	
	public void toggleAnchorObjectSelection(Point2D location) {
		this.displayObjects.selectOrDeselectAnchorObject(location);
		this.firePropertyChange(ViewController.ANCHOR_OBJECT_SELECTION, null, null);
	}
	
	public void selectObjects(SelectionTool tool, Boolean stillSelecting) {
		this.selectObjects(tool.getArea(), tool, stillSelecting);
	}
	
	protected void selectObjects(Rectangle2D area, SelectionTool tool, boolean stillSelecting) {
		if (stillSelecting) {
			this.displayObjects.tempSelectObjects(area);
			this.firePropertyChange(ViewController.DISPLAY_TOOL, null, tool);
		} else {
			int selectedObjectCount = this.displayObjects.selectObjects(area);
			//TODO WHY SELECTEDOBJECTCOUNT??
			this.firePropertyChange(ViewController.OBJECT_SELECTION, null, selectedObjectCount);
			this.firePropertyChange(ViewController.DISPLAY_TOOL, null, null);
		}
	}
	
	private void selectObjects(Set<BigBangObject> newSelectedObjects) {
		this.displayObjects.selectObjects(newSelectedObjects);
		this.firePropertyChange(ViewController.OBJECT_SELECTION, null, newSelectedObjects.size());
	}
	
	public void setDisplayTool(DisplayTool tool) {
		this.displayTool = tool;
		this.firePropertyChange(ViewController.DISPLAY_TOOL, null, this.displayTool);
	}
	
	public void clearDisplayTool() {
		this.displayTool = null;
		this.firePropertyChange(ViewController.DISPLAY_TOOL, null, null);
	}
	
	public void modelPropertyChange(PropertyChangeEvent event) {
		String propertyName = event.getPropertyName();
		if (propertyName.equals(BigBangController.COMPOSITION)) {
			this.initDisplayAndJSynObjects((BigBangObjects)event.getNewValue());
		} else if (propertyName.equals(BigBangController.OBJECT_SELECTION)) {
			this.selectObjects((Set<BigBangObject>)event.getNewValue());
		} else if (propertyName.equals(BigBangController.LAYERS)) {
			this.firePropertyChange(ViewController.LAYERS, null, event.getNewValue());
			if (this.displayObjects != null) {
				this.displayObjects.updateObjectSelections();
				this.firePropertyChange(ViewController.DISPLAY_OBJECTS, null, this.displayObjects);
				//this.player.updatePerformances();
			}
		} else if (propertyName.equals(BigBangController.UNDO)) {
			this.firePropertyChange(ViewController.UNDO, null, event.getNewValue());
		} else if (propertyName.equals(BigBangController.REDO)) {
			this.firePropertyChange(ViewController.REDO, null, event.getNewValue());
		} else if (propertyName.equals(BigBangController.INPUT_ACTIVE)) {
			this.firePropertyChange(ViewController.INPUT_ACTIVE, null, event.getNewValue());
		} else if (propertyName.equals(BigBangController.MODIFY_OPERATION)) {
			this.selectOperation((AbstractOperation)event.getNewValue());
		}
	}
	
	protected void initDisplayAndJSynObjects(BigBangObjects newObjects) {
		if (this.displayObjects == null || !this.displayObjects.getBaseForm().equals(newObjects.getBaseForm())) {
			if (newObjects.baseFormAllowsForSatellites()) {
				//do not select parameters for satellite and sibling number...
				this.factsViewState.getViewParameters().initSelections(newObjects.getCoordinateSystemValueNames().size()-2);
			} else {
				this.factsViewState.getViewParameters().initSelections(newObjects.getCoordinateSystemValueNames().size());
			}
			this.displayObjects = new DisplayObjects(newObjects);
			//this.player.setScore(new JSynScore(new TreeSet<BigBangObject>(), newObjects.getBaseForm(), newObjects.arePlayedBackInTime()));
			this.firePropertyChange(ViewController.FORM, null, this.displayObjects);
			this.firePropertyChange(ViewController.ACTIVE_COLIMIT_COORDINATE, null, this.displayObjects.getActiveColimitCoordinates());
		}
		this.addDisplayAndJSynObjects(newObjects);
	}
	
	private void addDisplayAndJSynObjects(BigBangObjects newObjects) {
		//System.out.println("DO " +newObjects.getAllObjects()+ " " +this.displayObjects.size());
		//System.out.println("DO " + newObjects.size()  + " " +this.displayObjects.size());
		Set<BigBangObject> lastObjects = newObjects.getObjectsAt(null);
		this.displayObjects.addObjects(lastObjects);
		//this.player.setScore(new JSynScore(lastObjects, newObjects.getBaseForm(), newObjects.arePlayedBackInTime()));
		
		//TODO UPDATE ALL THESE THINGS!!!
		this.factsViewState.getViewParameters().setDenotatorMinAndMaxValues(newObjects.getMinValues(), newObjects.getMaxValues());
		this.firePropertyChange(ViewController.DISPLAY_OBJECTS, null, this.displayObjects);
		this.firePropertyChange(ViewController.STANDARD_DENOTATOR_VALUES, null, this.getStandardDenotatorValues());
		this.firePropertyChange(ViewController.MAX_SATELLITE_LEVEL, null, this.displayObjects.getMaxSatelliteLevelOfActiveObject());
		
		//not every sounding object makes sense to be played alone (e.g. modulator, or overtone)
		//this.playObject((Denotator)event.getNewValue());
	}
	
	/*private void removeDisplayObjects(Set<BigBangObject> removedObjects) {
		this.displayObjects.removeObjects(removedObjects);
		
		//TODO REMOVE JSYNOBJECTS!!! this.player.setScore(this.extractor.getJSynScore());
		
		//TODO UPDATE ALL THESE THINGS!!!
		this.viewParameters.setDenotatorMinAndMaxValues(minValues, maxValues);
		this.firePropertyChange(ViewController.DISPLAY_OBJECTS, null, this.displayObjects);
		this.firePropertyChange(ViewController.MAX_SATELLITE_LEVEL, null, this.displayObjects.getMaxSatelliteLevelOfActiveObject());
	}*/
	
	public DisplayObjects getDisplayObjects() {
		return this.displayObjects;
	}
	
	public void setStandardDenotatorValue(Integer index, Double value) {
		this.standardDenotatorValues.put(this.displayObjects.getCoordinateSystemValueNames().get(index), value);
		this.firePropertyChange(ViewController.STANDARD_DENOTATOR_VALUES, null, this.getStandardDenotatorValues());
	}
	
	private List<Double> getStandardDenotatorValues() {
		List<Double> values = new ArrayList<Double>();
		for (String currentValueName : this.displayObjects.getCoordinateSystemValueNames()) {
			if (this.standardDenotatorValues.containsKey(currentValueName)) {
				values.add(this.standardDenotatorValues.get(currentValueName));
			} else {
				values.add(null);
			}
		}
		return values;
	}
	
	public void setActiveObject(Integer objectTypeIndex) {
		this.displayObjects.setIndexOfActiveObjectType(objectTypeIndex);
		this.firePropertyChange(ViewController.ACTIVE_OBJECT, null, objectTypeIndex);
		this.firePropertyChange(ViewController.MAX_SATELLITE_LEVEL, null, this.displayObjects.getMaxSatelliteLevelOfActiveObject());
	}
	
	public void setActiveColimitCoordinate(Integer colimitIndex, Integer coordinateIndex) {
		this.displayObjects.setActiveColimitCoordinate(colimitIndex, coordinateIndex);
		this.firePropertyChange(ViewController.ACTIVE_COLIMIT_COORDINATE, null, this.displayObjects.getActiveColimitCoordinates());
	}
	
	public void setActiveSatelliteLevel(Integer satelliteLevel) {
		this.displayObjects.setActiveSatelliteLevel(satelliteLevel);
		this.firePropertyChange(ViewController.ACTIVE_SATELLITE_LEVEL, null, satelliteLevel);
	}
	
	public void selectPreviousCompositionState() {
		this.controller.selectPreviousCompositionState();
	}
	
	public void selectNextCompositionState() {
		this.controller.selectNextCompositionState();
	}
	
	public void selectCompositionState(CompositionState state) {
		this.controller.selectCompositionState(state);
	}
	
	public void selectCompositionState(Integer stateIndex) {
		this.controller.selectCompositionState(stateIndex);
	}
	
	public void deselectCompositionStates() {
		this.controller.deselectCompositionStates();
	}
	
	public void selectOperation(AbstractOperation operation) {
		if (operation != null) {
			//select perspective first
			if (operation instanceof AbstractTransformation) {
				AbstractTransformation transformationEdit = (AbstractTransformation)operation;
				this.factsViewState.getViewParameters().setSelectedXYViewParameters(transformationEdit.getXYViewParameters());
				//TODO: center view?????
			
				//TODO: then select notes!!!
			}
			
			//then select displaymode and convert values!!
			/*if (operation instanceof AddObjectsOperation) {
				this.setDisplayMode(new DrawingModeAdapter(this.viewController));
			} else if (operation instanceof TranslationTransformation) {
				double[] startingPoint = this.panel.getXYDisplayValues(((TranslationTransformation)operation).getStartingPoint());
				double[] endingPoint = this.panel.getXYDisplayValues(((TranslationTransformation)operation).getEndingPoint());
				this.setDisplayMode(new TranslationModeAdapter(this.viewController, startingPoint, endingPoint));
			} else if (operation instanceof AbstractLocalTransformation) {
				AbstractLocalTransformation localEdit = (AbstractLocalTransformation)operation;
				double[] center = this.panel.getXYDisplayValues(localEdit.getCenter());
				double[] endingPoint = this.panel.getXYDisplayValues(localEdit.getEndingPoint());
				if (operation instanceof RotationTransformation) {
					double[] startingPoint = ((RotationTransformation)operation).getStartingPoint();
					double angle = ((RotationTransformation)operation).getAngle();
					this.setDisplayMode(new RotationModeAdapter(this.viewController, center, startingPoint, endingPoint, angle));
				} else if (operation instanceof ScalingTransformation) {
					double[] scaleFactors = ((ScalingTransformation)operation).getScaleFactors();
					this.setDisplayMode(new ScalingModeAdapter(this.viewController, center, endingPoint, scaleFactors));
				} else if (operation instanceof ShearingTransformation) {
					double[] shearingFactors = ((ShearingTransformation)operation).getShearingFactors();
					this.setDisplayMode(new ShearingModeAdapter(this.viewController, center, endingPoint, shearingFactors));
				} else if (operation instanceof ReflectionTransformation) {
					double[] reflectionVector = ((ReflectionTransformation)operation).getReflectionVector();
					this.setDisplayMode(new ReflectionModeAdapter(this.viewController, center, reflectionVector));
				}
			}*/
			
			//then select operation if not the same as the selected one
			if (!operation.equals(this.selectedOperation)) {
				this.selectedOperation = operation;
				this.firePropertyChange(ViewController.SELECT_OPERATION, null, operation);
				this.controller.selectOperation(operation);
			}
		} else if (operation == null && this.selectedOperation != null) {
			this.deselectOperations();
		}
	}
	
	public void deselectOperations() {
		if (this.selectedOperation != null) {
			this.selectedOperation = null;
			this.clearDisplayTool();
			this.firePropertyChange(ViewController.SELECT_OPERATION, null, null);
			this.controller.deselectOperations();
		}
	}
	
	public void modifyCenterOfSelectedTransformation(Point2D newCenter) {
		((AbstractTransformation)this.selectedOperation).modifyCenter(this.factsViewState.getXYZDenotatorValues(new PointND(newCenter)));
		this.controller.operationModified();
	}
	
	public void modifyEndPointOfSelectedTransformation(Point2D newEndPoint) {
		//TODO not great: only used in translation
		((AbstractTransformation)this.selectedOperation).modify(this.factsViewState.getXYZDenotatorValues(new PointND(newEndPoint)));
		this.controller.operationModified();
	}
	
	public void modifySelectedTransformation(double[] newValues) {
		if (this.selectedOperation != null) {
			((AbstractTransformation)this.selectedOperation).modify(newValues);
			this.controller.operationModified();
		}
	}
	
	public void modifyRotationAngle(Double angle) {
		((RotationTransformation)this.selectedOperation).modifyAngle(angle);
		this.controller.operationModified();
	}
	
	public void modifyOperation(Integer operationIndex, Integer midiValue) {
		//ratio between 0 and 2
		double ratio = ((double)midiValue)/127*2;
		this.controller.modifyOperation(operationIndex, ratio);
	}
	
	public void translateSelectedObjects(Point2D center, Point2D endingPoint, Boolean copyAndTransform, Boolean startNewTransformation) {
		TransformationProperties properties = this.getLocalTransformationProperties(center, endingPoint, copyAndTransform, startNewTransformation);
		this.controller.translateObjects(properties);
	}
	
	public void rotateSelectedObjects(Point2D center, Point2D startPoint, Point2D endPoint, Double angle, Boolean copyAndTransform, Boolean startNewTransformation) {
		TransformationProperties properties = this.getLocalTransformationProperties(center, endPoint, copyAndTransform, startNewTransformation);
		this.controller.rotateObjects(properties, new double[]{startPoint.getX(),startPoint.getY()}, angle);
	}
	
	public void scaleSelectedObjects(Point2D center, Point2D endPoint, double[] scaleFactors, Boolean copyAndTransform, Boolean startNewTransformation) {
		TransformationProperties properties = this.getLocalTransformationProperties(center, endPoint, copyAndTransform, startNewTransformation);
		this.controller.scaleObjects(properties, scaleFactors);
	}
	
	public void reflectSelectedObjects(Point2D center, Point2D endPoint, double[] reflectionVector, Boolean copyAndTransform, Boolean startNewTransformation) {
		TransformationProperties properties = this.getLocalTransformationProperties(center, endPoint, copyAndTransform, startNewTransformation);
		this.controller.reflectObjects(properties, reflectionVector);
	}
	
	public void shearSelectedObjects(Point2D center, Point2D endPoint, double[] shearingFactors, Boolean copyAndTransform, Boolean startNewTransformation) {
		TransformationProperties properties = this.getLocalTransformationProperties(center, endPoint, copyAndTransform, startNewTransformation);
		this.controller.shearObjects(properties, shearingFactors);
	}
	
	public void affineTransformSelectedObjects(Point2D center, Point2D endPoint, double[] shift, RMatrix transform, Boolean copyAndTransform, Boolean startNewTransformation) {
		TransformationProperties properties = this.getLocalTransformationProperties(center, endPoint, copyAndTransform, startNewTransformation);
		this.controller.affineTransformObjects(properties, shift, transform);
	}
	
	public void shapeSelectedObjects(TreeMap<Integer,Integer> locations, Boolean copyAndTransform, Boolean startNewTransformation) {
		TransformationProperties properties = this.getTransformationProperties(copyAndTransform, startNewTransformation);
		this.controller.shapeObjects(properties, this.getXYDenotatorValues(locations));
	}
	
	public void addWallpaperDimension() {
		this.controller.addWallpaperDimension(this.displayObjects.getSelectedBigBangObjects(), 0, 5);
	}
	
	public void modifyWallpaperRange(Boolean rangeTo, Integer value) {
		((AddWallpaperDimensionOperation)this.selectedOperation).setRange(rangeTo, value);
		this.controller.operationModified();
	}
	
	public void addAlteration() {
		this.controller.addAlteration(this.displayObjects.getActiveObjectValuePathAt(this.factsViewState.getViewParameters().getSelectedXYViewParameters()[0]));
	}
	
	public void setAlterationComposition(Integer index) {
		Set<BigBangObject> selectedObjects = this.displayObjects.getSelectedBigBangObjects();
		if (this.selectedOperation instanceof AlterationOperation) {
			if (index == 0) {
				((AlterationOperation)this.selectedOperation).setForegroundComposition(selectedObjects);
			} else {
				((AlterationOperation)this.selectedOperation).setBackgroundComposition(selectedObjects);
			}
		}
	}
	
	public void setAlterationCoordinates(ArrayList<Integer> selectedCoordinates) {
		if (this.selectedOperation instanceof AlterationOperation) {
			List<DenotatorPath> alterationCoordinates = new ArrayList<DenotatorPath>();
			for (int currentIndex : selectedCoordinates) {
				alterationCoordinates.add(this.displayObjects.getActiveObjectValuePathAt(currentIndex));
			}
			((AlterationOperation)this.selectedOperation).setAlterationCoordinates(alterationCoordinates);
		}
	}
	
	//also in ViewParameters. this one just updates alteration
	public void setSelectedViewParameters(int[] newSelections) {
		this.controller.setAlterationDegreesDimension(this.displayObjects.getActiveObjectValuePathAt(newSelections[0]));
	}
	
	private TransformationProperties getLocalTransformationProperties(Point2D center, Point2D endPoint, boolean copyAndTransform, boolean startNewTransformation) {
		//the end point is merely recorded for the display tool to be the same size....
		TransformationProperties properties = this.getTransformationProperties(copyAndTransform, startNewTransformation);
		double[] denotatorCenter = this.factsViewState.getXYZDenotatorValues(new PointND(center));
		double[] denotatorEndPoint = this.factsViewState.getXYZDenotatorValues(new PointND(endPoint));
		if (properties.getAnchor() != null) {
			/*if (this.selectedObjectsPaths != null) {
				properties.setAnchorNodePath(this.selectedObjectsPaths.getAnchorPath());
			}*/
			double[] anchorValues = this.factsViewState.getXYZDenotatorValues(new PointND(this.displayObjects.getSelectedAnchorCenter()));
			denotatorCenter[0] -= anchorValues[0];
			denotatorCenter[1] -= anchorValues[1];
		}
		properties.setCenter(denotatorCenter);
		properties.setEndPoint(denotatorEndPoint);
		return properties;
	}
	
	private TransformationProperties getTransformationProperties(boolean copyAndTransform, boolean startNewTransformation) {
		//System.out.println(this.selectedObjectsPaths + " ... " + objectsPaths + " ... " + this.displayObjects.getSelectedObjectsPaths() + " ... " + this.displayObjects.getCategorizedSelectedObjectsPaths());
		Set<BigBangObject> selectedObjects = this.displayObjects.getSelectedBigBangObjects();
		BigBangObject selectedAnchor = this.displayObjects.getSelectedBigBangAnchor();
		List<TransformationPaths> valuePaths = this.getXYTransformationPaths();
		
		return new TransformationProperties(selectedObjects, selectedAnchor, valuePaths, copyAndTransform, startNewTransformation);
	}
	
	public void addObjects(ArrayList<PointND> locations, Boolean inPreviewMode) {
		if (this.displayObjects != null) { //&& this.displayMode instanceof DrawingModeAdapter) {
			List<Map<DenotatorPath,Double>> objectValues = new ArrayList<Map<DenotatorPath,Double>>();
			List<DenotatorPath> powersetPaths = new ArrayList<DenotatorPath>();
			for (PointND currentLocation : locations) {
				Map<DenotatorPath,Double> currentValues = this.displayObjects.getActiveObjectStandardValues(this.standardDenotatorValues);
				//TODO PUT THIS ENTIRE CLOSEST POWERSET LOGIC IN MODEL!!!!
				DenotatorPath currentPowersetPath = this.editObjectValuesAndFindClosestPowerset(currentLocation, currentValues);
				//only add object if there are some screen values to be converted
				if (!currentValues.isEmpty()) {
					objectValues.add(currentValues);
					powersetPaths.add(currentPowersetPath);
				}
			}
			//TODO REALLY BAD!!!
			if (this.selectedOperation instanceof AddObjectsOperation) {
				((AddObjectsOperation)this.selectedOperation).addObjects(objectValues, powersetPaths, inPreviewMode);
				this.controller.operationModified();
			} else {
				this.controller.addObjects(objectValues, powersetPaths, inPreviewMode);
			}
		}
	}
	
	public void unAddObjects(ArrayList<PointND> locations) {
		Set<Map<DenotatorPath,Double>> objectValues = new TreeSet<Map<DenotatorPath,Double>>();
		if (this.displayObjects != null) {// && this.displayMode instanceof DrawingModeAdapter) {
			for (PointND currentLocation : locations) {
				Map<DenotatorPath,Double> currentValues = this.displayObjects.getActiveObjectStandardValues(this.standardDenotatorValues);
				this.editObjectValuesAndFindClosestPowerset(currentLocation, currentValues);
				//only add object if there are some screen values to be converted
				if (!currentValues.isEmpty()) {
					objectValues.add(currentValues);
				}
			}
		}
		if (objectValues.size() > 0) {
			this.controller.unAddObjects(objectValues);
		}
	}
	
	public void deleteSelectedObjects() {
		Set<BigBangObject> objects = this.displayObjects.getSelectedBigBangObjects();
		if (objects.size() > 0) {
			this.controller.deleteObjects(objects);
			this.displayObjects.deselectAllObjects();
		}
	}
	
	public void setObjectsOnLayerSelected(Integer layerIndex, Boolean selected) {
		this.displayObjects.setObjectsOnLayerSelected(layerIndex, selected);
		this.firePropertyChange(ViewController.LAYER_SELECTED, null, layerIndex);
		this.firePropertyChange(ViewController.OBJECT_SELECTION, null, this.displayObjects.getSelectedBigBangObjects().size());
	}
	
	public void addSelectedObjectsToNewLayer() {
		this.controller.addObjectsToNewLayer(this.displayObjects.getSelectedBigBangObjects());
	}
	
	public void addSelectedObjectsToLayer(Integer layerIndex) {
		this.controller.addObjectsToLayer(layerIndex, this.displayObjects.getSelectedBigBangObjects());
	}
	
	public void moveSelectedObjectsToNewLayer() {
		this.controller.moveObjectsToNewLayer(this.displayObjects.getSelectedBigBangObjects());
	}
	
	public void moveSelectedObjectsToLayer(Integer layerIndex) {
		this.controller.moveObjectsToLayer(layerIndex, this.displayObjects.getSelectedBigBangObjects());
	}
	
	public void addSelectedObjectsAsSatellitesTo(DisplayObject anchor, Integer powersetIndex) {
		Set<BigBangObject> objects = this.displayObjects.getSelectedBigBangObjects();
		this.controller.buildSatellites(objects, anchor.getBigBangObject(), powersetIndex);
	}
	
	public void flattenSelectedObjects() {
		Set<BigBangObject> objects = this.displayObjects.getSelectedBigBangObjects();
		this.controller.flattenObjects(objects);
	}
	
	private TreeMap<Double,Double> getXYDenotatorValues(TreeMap<Integer,Integer> locations) {
		Set<Integer> xPositions = locations.keySet();
		TreeMap<Double,Double> translatedValues = new TreeMap<Double,Double>();
		for (Integer currentXPosition: xPositions) {
			Point2D currentPoint = new Point2D(currentXPosition, locations.get(currentXPosition));
			double[] currentXYDenotatorValues = this.factsViewState.getXYZDenotatorValues(new PointND(currentPoint));
			translatedValues.put(currentXYDenotatorValues[0], currentXYDenotatorValues[1]);
		}
		return translatedValues;
	}
	
	private DenotatorPath editObjectValuesAndFindClosestPowerset(PointND location, Map<DenotatorPath,Double> denotatorValues) {
		DenotatorPath parentPowersetPath = this.displayObjects.getActiveObjectAndLevelPowersetPath();
		int[] xyzParameters = this.factsViewState.getViewParameters().getSelectedViewParameters();
		int xValueIndex = this.displayObjects.getActiveObjectValueIndex(xyzParameters[0]);
		int yValueIndex = this.displayObjects.getActiveObjectValueIndex(xyzParameters[1]);
		int zValueIndex = this.displayObjects.getActiveObjectValueIndex(xyzParameters[2]);
		double[] xyzDenotatorValues = this.factsViewState.getXYZDenotatorValues(location);
		
		if (xValueIndex >= 0) {
			this.replaceDenotatorValue(location.getCoord(0), xValueIndex, 0, denotatorValues);
		}
		if (yValueIndex >= 0 && (xValueIndex < 0 || yValueIndex != xValueIndex)) {
			this.replaceDenotatorValue(location.getCoord(1), yValueIndex, 1, denotatorValues);
		}
		if (location.getDimension() > 2 && zValueIndex >= 0 && (zValueIndex != yValueIndex && zValueIndex != xValueIndex)) {
			this.replaceDenotatorValue(location.getCoord(2), zValueIndex, 2, denotatorValues);
		}
		DenotatorPath closestPowersetPath = this.displayObjects.findClosestPowersetPath(xyzParameters, xyzDenotatorValues, parentPowersetPath);
		return closestPowersetPath;
	}
	
	private void replaceDenotatorValue(double displayValue, int valueIndex, int parameterIndex, Map<DenotatorPath,Double> values) {
		double denotatorValue = this.factsViewState.getDenotatorValue(displayValue, parameterIndex);
		
		//TODO CONVERSION!!!!!!
		
		DenotatorPath associatedPath = this.displayObjects.getActiveObjectValuePathAt(valueIndex);
		//null happens when parent value, satellite/sibling level is selected, or when path not in active colimits
		if (associatedPath != null) {
			values.put(associatedPath, denotatorValue);
		}
	}
	
	private List<TransformationPaths> getXYTransformationPaths() {
		List<TransformationPaths> paths = new ArrayList<TransformationPaths>();
		int[] xyParameters = this.factsViewState.getViewParameters().getSelectedXYViewParameters();
		for (int i = 0; i < this.displayObjects.getObjectTypes().size(); i++) {
			TransformationPaths currentPaths = new TransformationPaths();
			//TODO: bad so redundant
			currentPaths.setXYCoordinates(xyParameters);
			paths.add(currentPaths);
		}
		//only one parameter might be selected... TODO does the list need to be null or can it just be empty??
		for (int i = 0; i < xyParameters.length; i++) {
			if (xyParameters[i] >= 0) {
				List<List<DenotatorPath>> currentPaths = this.displayObjects.getAllObjectConfigurationsValuePathsAt(xyParameters[i]);
				for (int j = 0; j < currentPaths.size(); j++) {
					paths.get(j).setDomainPaths(i, currentPaths.get(j));
					paths.get(j).setCodomainPaths(i, currentPaths.get(j));
				}
			} else {
				for (TransformationPaths currentPaths : paths) {
					currentPaths.setDomainPaths(i, null);
					currentPaths.setCodomainPaths(i, null);
				}
			}
		}
		return paths;
	}
	
	public void undo() {
		this.controller.undo();
	}
	
	public void redo() {
		this.controller.redo();
	}
	
	public void setInputActive(Boolean inputActive) {
		this.controller.changeInputActive(inputActive);
	}
	
	/*public void setSynthActive(Boolean synthActive) {
		this.player.setSynthActive(synthActive);
		this.firePropertyChange(ViewController.SYNTH_ACTIVE, null, synthActive);
	}
	
	public void setIsLooping(Boolean isLooping) {
		this.player.setIsLooping(isLooping);
		this.firePropertyChange(ViewController.IS_LOOPING, null, isLooping);
	}
	
	public void setTempo(Integer tempo) {
		this.player.setTempo(tempo);
		this.firePropertyChange(ViewController.TEMPO, null, tempo);
	}
	
	public void setPlaybackPosition(Point2D location) {
		int timeAxisIndex = this.displayObjects.getTimeAxisIndex(this.viewParameters);
		if (timeAxisIndex != -1) {
			double[] xyDenotatorValues = this.factsView.getXYZDenotatorValues(new PointND(location));
			this.player.setPlaybackPosition(xyDenotatorValues[timeAxisIndex]);
		}
	}
	
	public void setWaveform(String waveform) {
		this.player.setWaveform(waveform);
		this.firePropertyChange(ViewController.WAVEFORM, null, waveform);
	}*/
	
	/*public void setMidiIn(String inDeviceName) {
		this.midiReceiver.setSelectedInDevice(inDeviceName);
	}
	
	public void setMidiOut(String outDeviceName) {
		this.player.setMidiOutDevice(outDeviceName);
	}*/
	
	/*public void pressMidiKey(Integer channel, Integer pitch, Integer velocity) {
		if (channel < 0) {
			channel = this.getChannel();
		}
		if (velocity < 0) {
			velocity = this.getVelocity();
		}
		if (this.recorder.isRecording()) {
			this.recorder.pressMidiKey(channel, pitch, velocity);
			this.player.pressMidiKey(channel, pitch, velocity, true);
		} else {
			this.player.pressMidiKey(channel, pitch, velocity, false);
		}
	}
	
	public void releaseMidiKey(Integer channel, Integer pitch) {
		if (channel < 0) {
			channel = this.getChannel();
		}
		if (this.recorder.isRecording()) {
			this.recorder.releaseMidiKey(channel, pitch);
			this.player.releaseMidiKey(channel, pitch, true);
		} else {
			this.player.releaseMidiKey(channel, pitch, false);
		}
	}*/
	
	/*private int getVelocity() {
		return this.standardDenotatorValues.get(CoolFormRegistrant.LOUDNESS_NAME).intValue();
	}
	
	private int getChannel() {
		if (this.standardDenotatorValues.containsKey(CoolFormRegistrant.VOICE_NAME)) {
			return this.standardDenotatorValues.get(CoolFormRegistrant.VOICE_NAME).intValue();
		}
		return 0;
	}*/
	
	/*public void changeOctave(Boolean up) {
		if (!this.recorder.isRecording()) {
			this.player.transposeAllScoreVersionsByOctave(up);
		}
	}
	
	public void changeVelocity(Integer velocity) {
		this.player.changeVelocity(velocity);
	}
	
	public void changeRateModifier(Double rateModifier) {
		this.player.changeRate(rateModifier);
	}*/

}
