package org.rubato.rubettes.util;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ProductRing;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Just interesting for comparing paths denoting coordinates of the same denotator,
 * i.e. same size and only last index deferring.
 * @author flo
 */
public class DenotatorPath implements Comparable<DenotatorPath> {
	
	private List<Integer> indices;
	private Form baseForm;
	//form at which the path ends
	private Form endForm;
	//null if not a simple form
	private Module module;
	private int elementPathIndex;
	
	public DenotatorPath(Form baseForm) {
		this.baseForm = baseForm;
		this.indices = new ArrayList<>();
		this.updateFormAndModule();
	}
	
	public DenotatorPath(Form baseForm, int[] path) {
		this(baseForm);
		for (int currentIndex: path) {
			this.indices.add(currentIndex);
		}
		this.updateFormAndModule();
	}
	
	public DenotatorPath(Form baseForm, List<Integer> path) {
		this(baseForm);
		this.indices = path;
		this.updateFormAndModule();
	}
	
	public DenotatorPath(XMLReader reader, Element element) {
		this.fromXML(reader, element);
		this.updateFormAndModule();
	}
	
	private void updateFormAndModule() {
		Form currentForm = this.baseForm;
		Module currentModule = null;
		this.elementPathIndex = -1;
		for (int i = 0; i < this.indices.size(); i++) {
			int currentIndex = this.indices.get(i);
			if (currentForm.getType() == FormDenotatorTypeEnum.POWER || currentForm.getType() == FormDenotatorTypeEnum.LIST) {
				currentForm = currentForm.getForm(0);
			} else if (currentForm.getType() != FormDenotatorTypeEnum.SIMPLE) {
				if (currentForm.getFormCount() <= currentIndex) {
					//hahaha
					try { throw new LatrunculusCheckedException("Illegal DenotatorPath: " + this.baseForm.getNameString() + ": " + this); }
					catch (LatrunculusCheckedException e) { e.printStackTrace(); }
				}
				currentForm = currentForm.getForm(currentIndex);
			} else {
				currentModule = ((SimpleForm)currentForm).getModule();
				this.elementPathIndex = i;
				for (int j = i; j < this.indices.size(); j++) {
					if (currentModule instanceof ProductRing) {
						currentModule = ((ProductRing)currentModule).getFactor(j);
					} else {
						currentModule = currentModule.getComponentModule(j);
					}
				}
			}
		}
		this.endForm = currentForm;
		this.module = currentModule;
	}
	
	public int size() {
		return this.indices.size();
	}
	
	public boolean isElementPath() {
		return this.module != null;
	}
	
	public DenotatorPath getDenotatorSubpath() {
		if (this.elementPathIndex >= 0) {
			return this.subPath(0, this.elementPathIndex);
		}
		return this.clone();
	}
	
	public DenotatorPath getElementSubpath() {
		if (this.elementPathIndex >= 0) {
			return this.subPath(this.elementPathIndex);
		}
		return null;
	}
	
	public boolean inConflictingColimitPositions(DenotatorPath path) {
		Set<DenotatorPath> thisColimits = new TreeSet<>(this.getParentColimitPaths());
		Set<DenotatorPath> pathColimits = new TreeSet<>(path.getParentColimitPaths());
		for (DenotatorPath currentColimitPath : thisColimits) {
			if (pathColimits.contains(currentColimitPath)) {
				if (this.subPath(0, currentColimitPath.size()+1).getLastIndex()
						!= path.subPath(0, currentColimitPath.size()+1).getLastIndex()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public DenotatorPath clone() {
		return new DenotatorPath(this.baseForm, new ArrayList<>(this.indices));
	}
	
	/**
	 * @return a copy of this path, the beginning part of the given length replaced with the given model 
	 */
	public DenotatorPath changeBeginning(DenotatorPath model, int beginningLength) {
		ArrayList<Integer> newIndices = new ArrayList<>(this.indices.subList(beginningLength, this.indices.size()));
		newIndices.addAll(0, model.indices);
		return new DenotatorPath(model.baseForm, newIndices);
	}
	
	public DenotatorPath replaceLast(int index) {
		DenotatorPath path = this.getParentPath();
		path.add(index);
		return path;
	}
	
	public DenotatorPath subPath(int fromIndex) {
		return this.subPath(fromIndex, this.indices.size());
	}
	
	public DenotatorPath subPath(int fromIndex, int toIndex) {
		try {
			Form subForm = new DenotatorPath(this.baseForm, new ArrayList<>(this.indices.subList(0, fromIndex))).getEndForm();
			return new DenotatorPath(subForm, new ArrayList<>(this.indices.subList(fromIndex, toIndex)));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	public DenotatorPath append(DenotatorPath appendix) {
		DenotatorPath concatenation = this.clone();
		concatenation.addAll(appendix.indices);
		return concatenation;
	}
	
	/**
	 * @return a descendant path of this path that ends with whatever modelPath has that this does not 
	 * can be used to create specific powerset paths from an abstract one
	 */
	public DenotatorPath getDescendantPathAccordingTo(DenotatorPath modelPath) {
		DenotatorPath descendantPath = this.clone();
		for (int i = this.size(); i < modelPath.size(); i++) {
			descendantPath.add(modelPath.indices.get(i));
		}
		return descendantPath;
	}
	
	/**
	 * Shorter paths and there the paths having smaller indices are smaller.
	 */
	public int compareTo(DenotatorPath object) {
		if (object == null) {
			throw new NullPointerException("DenotatorPath expected, got null");
		}
		if (this.size() == object.size()) {
			for (int i = 0; i < this.size(); i++) {
				Integer thisIndex = this.indices.get(i);
				Integer otherIndex = object.indices.get(i);
				if (!thisIndex.equals(otherIndex)) {
					return thisIndex.compareTo(otherIndex);
				}
			}
			return 0;
		}
		return this.size() - object.size();
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof DenotatorPath)) {
			return false;
		}
		return this.indices.equals(((DenotatorPath)object).indices);
	}
	
	public boolean equalsExceptForPowersetIndices(DenotatorPath path) {
		if (path == null || path.size() != this.size() || path.getBaseForm() != this.getBaseForm()) {
			return false;
		}
		for (int i = 0; i < this.indices.size(); i++) {
			Form currentParentForm = this.subPath(0, i+1).getParentForm();
			if (currentParentForm.getType() != FormDenotatorTypeEnum.POWER && currentParentForm.getType() != FormDenotatorTypeEnum.LIST) {
				if (this.indices.get(i) != path.indices.get(i)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Returns an array conversion of a List path
	 */
	public int[] toIntArray() {
		int[] arrayPath = new int[this.indices.size()];
		for (int i = 0; i < arrayPath.length; i++) {
			arrayPath[i] = this.indices.get(i).intValue();
		}
		return arrayPath;
	}
	
	private void add(int index) {
		this.indices.add(index);
		this.updateFormAndModule();
	}
	
	private void addAll(List<Integer> indices) {
		this.indices.addAll(indices);
		this.updateFormAndModule();
	}
	
	public DenotatorPath setIndex(int index, int value) {
		DenotatorPath clone = this.clone();
		clone.indices.set(index, value);
		return clone;
	}
	
	public int getFirstIndex() {
		return this.indices.get(0);
	}
	
	public int getLastIndex() {
		return this.indices.get(this.indices.size()-1);
	}
	
	/**
	 * @param childIndex
	 * @return the path of the childIndex-th child of this path, regardless wether it exists or not
	 */
	public DenotatorPath getChildPath(int childIndex) {
		if (this.endForm.getFormCount() > childIndex || this.endForm.getType() == FormDenotatorTypeEnum.POWER || this.endForm.getType() == FormDenotatorTypeEnum.LIST
				|| (this.endForm.getType() == FormDenotatorTypeEnum.SIMPLE && this.getSubModuleOrRing(childIndex) != null)) {
			DenotatorPath childPath = this.clone();
			childPath.add(childIndex);
			return childPath;
		}
		return null;
	}
	
	/**
	 * @return the path of the denotator this object is contained in
	 */
	public DenotatorPath getParentPath() {
		return this.subPath(0, this.indices.size()-1);
	}
	
	public DenotatorPath getSatellitePath(int index, int powersetIndex) {
		DenotatorPath powersetPath = this.getPowersetPath(powersetIndex);
		return powersetPath.getChildPath(index);
	}
	
	/**
	 * @return the top path of the connected object this object is a satellite of. null if there is none or if it is the topmost object
	 */
	public DenotatorPath getAnchorPath() {
		DenotatorPath anchorPowersetPath = this.getAnchorPowersetPath();
		if (anchorPowersetPath != null) {
			return anchorPowersetPath.getTopPath();
		}
		return null;
	}
	
	/**
	 * @return a list of the paths of all anchor notes through the hierarchy starting with the direct anchor
	 */
	public List<DenotatorPath> getAnchorPaths() {
		List<DenotatorPath> anchorPaths = new ArrayList<>();
		DenotatorPath currentAnchor = this.getAnchorPath();
		while (currentAnchor != null) {
			anchorPaths.add(currentAnchor);
			currentAnchor = currentAnchor.getAnchorPath();
		}
		return anchorPaths;
	}
	
	/**
	 * @return the path of the powerset this connected object is in
	 */
	public DenotatorPath getAnchorPowersetPath() {
		return this.getTopPath().getParentPath();
	}
	
	/**
	 * @return the index of this connected object in the powerset it is in
	 */
	public int getObjectIndex() {
		return this.getTopPath().getLastIndex();
	}
	
	public List<DenotatorPath> getParentColimitPaths() {
		DenotatorPath currentPath = this.getParentPath();
		List<DenotatorPath> parentColimitPaths = new ArrayList<>();
		while (currentPath != null) {
			if (currentPath.getEndForm().getType() == FormDenotatorTypeEnum.COLIMIT) {
				parentColimitPaths.add(currentPath);
			}
			currentPath = currentPath.getParentPath();
		}
		Collections.reverse(parentColimitPaths);
		return parentColimitPaths;
	}
	
	/**
	 * @return the top path of this connected object, meaning the path at which it appears in a powerset above
	 */
	public DenotatorPath getTopPath() {
		DenotatorPath currentPath = this;
		Form currentParentForm = currentPath.getParentForm();
		while(currentParentForm != null && currentParentForm.getType() != FormDenotatorTypeEnum.POWER && currentParentForm.getType() != FormDenotatorTypeEnum.LIST) {
			currentPath = currentPath.getParentPath();
			currentParentForm = currentPath.getParentForm();
		}
		return currentPath;
	}
	
	private Form getParentForm() {
		DenotatorPath parentPath = this.getParentPath(); 
		if (parentPath != null) {
			return parentPath.getEndForm();
		}
		return null;
	}
	
	/**
	 * @return true if this path is a direct satellite of the given path, i.e. contained in one of its powersets
	 */
	public boolean isDirectSatelliteOf(DenotatorPath path) {
		DenotatorPath anchorPath = this.getTopPath().getAnchorPath();
		if (path != null && anchorPath != null) {
			return anchorPath.equals(path.getTopPath());
		}
		return false;
	}
	
	/**
	 * @return true if this path is a direct or indirect satellite of the given path, i.e. maybe connected by
	 * intermediate satellite levels.
	 */
	public boolean isSatelliteOf(DenotatorPath path) {
		DenotatorPath currentPath = this;
		while (currentPath != null) {
			if (currentPath.isDirectSatelliteOf(path)) {
				return true;
			}
			currentPath = currentPath.getAnchorPath();
		}
		return false;
	}
	
	public boolean isPartOfSameObjectAs(DenotatorPath path) {
		if (this.size() >= path.size()) {
			if (this.subPath(0, path.size()).equals(path)) {
				for (int i = path.size()+1; i < this.size(); i++) {
					if (this.subPath(0, i).getEndForm().getType() == FormDenotatorTypeEnum.POWER) {
						return false;
					}
				}
				return true;
			}
		} else {
			return path.isPartOfSameObjectAs(this);
		}
		return false;
	}
	
	/*public DenotatorPath getMinimalCommonParentPath(DenotatorPath path) {
		if (path != null) {
			DenotatorPath minimalCommonParentPath = new DenotatorPath(this.baseForm);
			for (int i = 0; i < Math.min(this.size(), path.size()); i++) {
				Integer currentIndex = this.indices.get(i);
				if (currentIndex.equals(path.indices.get(i))) {
					minimalCommonParentPath.add(currentIndex);
				} else break;
			}
			return minimalCommonParentPath;
		}
		return null;
	}*/
	
	public static List<DenotatorPath> getAnchorPaths(List<DenotatorPath> denotatorPaths) {
		List<DenotatorPath> parentPaths = new ArrayList<>();
		for (DenotatorPath currentObjectPath: denotatorPaths) {
			parentPaths.add(currentObjectPath.getAnchorPath());
		}
		return parentPaths;
	}
	
	public static List<DenotatorPath> getGrandAnchorPowersetPaths(List<DenotatorPath> denotatorPaths) {
		List<DenotatorPath> grandParentPaths = new ArrayList<>();
		for (DenotatorPath currentNotePath: denotatorPaths) {
			grandParentPaths.add(currentNotePath.getAnchorPath().getAnchorPowersetPath());
		}
		return grandParentPaths;
	}
	
	/**
	 * TODO: this will have to get all the powerset indices for the given paths!!!!!!!! OHOooH
	 */
	public static int[] getPowersetIndices(List<DenotatorPath> paths) {
		int[] powersetIndices = new int[paths.size()];
		for (int i = 0; i < paths.size(); i++) {
			DenotatorPath currentPath = paths.get(i);
			powersetIndices[i] = 0; //currentPath.getPowersetIndex();
		}
		return powersetIndices;
	}
	
	public Form getBaseForm() {
		return this.baseForm;
	}
	
	/**
	 * @return the form that the path ends with, even if it goes on into module elements
	 */
	public Form getEndForm() {
		return this.endForm;
	}
	
	public Module getModule() {
		return this.module;
	}
		
	private Module getSubModuleOrRing(int index) {
		if (this.endForm.getType() == FormDenotatorTypeEnum.SIMPLE && this.module == null) {
			return this.getSubModule(((SimpleForm)this.endForm).getModule(), index); 
		} else if (this.module != null) {
			return this.getSubModule(this.module, index);
		}
		return null;
	}
	
	private Module getSubModule(Module module, int index) {
		if (module instanceof ProductRing) {
			return ((ProductRing)module).getFactor(index);
		}
		return module.getComponentModule(index);
	}
	
	public DenotatorPath getFirstPowersetPath() {
		return this.getPowersetPath(0);
	}
	
	/**
	 * @return the path of the powersetIndex-th powerset found, null if there are none within the given
	 * search depth or less than reached by the powersetIndex. search does not continue through powersets. if the
	 * given form itself is a powerset and powersetIndex is 0 it is returned instantly.
	 */
	public DenotatorPath getPowersetPath(int powersetIndex) {
		PriorityQueue<DenotatorPath> pathQueue = new PriorityQueue<>();
		int foundCount = 0;
		pathQueue.add(this.getTopPath());
		while (!pathQueue.isEmpty()) {
			DenotatorPath currentPath = pathQueue.poll();
			Form currentForm = currentPath.getEndForm();
			//TODO: LIST TOO, change all names!!!
			if (currentForm.getType() == FormDenotatorTypeEnum.POWER || currentForm.getType() == FormDenotatorTypeEnum.LIST) {
				if (foundCount >= powersetIndex) {
					return currentPath;
				}
				foundCount++;
			} else {
				for (int i = 0; i < currentForm.getFormCount(); i++) {
					pathQueue.add(currentPath.getChildPath(i));
				}
			}
		}
		//gets here when there are less powersets than reached by powersetIndex
		return null;
	}
	
	/*
	 * currently only works for forms containing one instance of each powerset form.
	 * indices within intermediary powersets are always 0
	 */
	public DenotatorPath getPowersetPath(int satelliteLevel, Form satelliteForm) {
		if (satelliteLevel > 0) {
			DenotatorPath satellitePath = this.clone();
			for (int i = 0; i < satelliteLevel; i++) {
				satellitePath = satellitePath.getPowersetPath(satellitePath.getSatelliteIndex(satelliteForm)).getChildPath(0);
			}
			if (satellitePath != null && satellitePath.getEndForm().equals(satelliteForm)) {
				return satellitePath.getParentPath();
			}
			return null;
		}
		//can be null if this is not in a powerset
		if (this.getAnchorPowersetPath() != null) {
			return this.getAnchorPowersetPath().getPowersetPath(satelliteLevel+1, satelliteForm);
		}
		return null;
	}
	
	private int getSatelliteIndex(Form satelliteForm) {
		int powersetIndex = 0;
		DenotatorPath powersetPath = this.getPowersetPath(powersetIndex);
		while (powersetPath != null) {
			if (powersetPath.getChildPath(0).getEndForm().equals(satelliteForm)) {
				return powersetIndex;
			}
			powersetIndex++;
			powersetPath = this.getPowersetPath(powersetIndex);
		}
		return -1;
	}
	
	public String toString() {
		return this.indices.toString();
	}
	
	public static final String DENOTATOR_PATH_TAG = "DenotatorPath";
	private static final String INDICES_ATTR = "indices";
	
	public void toXML(XMLWriter writer) {
		writer.openBlock(DENOTATOR_PATH_TAG, INDICES_ATTR, this.indices.toString());
		writer.writeFormRef(this.baseForm);
		writer.closeBlock();
	}
	
	private void fromXML(XMLReader reader, Element element) {
		this.indices = XMLReader.getIntListAttribute(element, INDICES_ATTR);
		this.baseForm = reader.parseAndResolveForm(XMLReader.getChild(element, FORM));
	}

}
