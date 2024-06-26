package org.rubato.rubettes.util;

import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.ProductElement;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ColimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.FactorDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author flo
 *
 */
public class ObjectGenerator {
	
	private Form baseForm;
	
	public Denotator createEmptyScore() {
		return this.baseForm.createDefaultDenotator();
	}
	
	public void setBaseForm(Form baseForm) {
		this.baseForm = baseForm;
	}
	
	public Form getBaseForm() {
		return this.baseForm;
	}
	
	/**
	 * @return a converted version of the given denotator if it is compatible with the baseForm, or the given
	 * denotator itself if it is of the same form and does not need to be converted. null if the forms are not
	 * compatible.
	 */
	public Denotator convertDenotatorIfCompatible(Denotator denotator) {
		//for now only compatible if same as baseForm or both are of score type
		if (denotator.getForm().equals(this.baseForm)) {
			return denotator;
		} else if (this.isScoreCompatibleForm(denotator.getForm()) && this.isScoreCompatibleForm(this.baseForm)) {
			return new SoundNoteGenerator().convertScore(denotator);
		}
		return null;
	}
	
	public boolean isFormCompatibleWithBaseForm(Form form) {
		//for now only compatible if same as baseForm or both are of score type
		return form.equals(this.baseForm) || (this.isScoreCompatibleForm(form) && this.isScoreCompatibleForm(this.baseForm));
	}
	
	private boolean isScoreCompatibleForm(Form form) {
		return form.equals(CoolFormRegistrant.SCORE_FORM) || form.equals(CoolFormRegistrant.MACRO_SCORE_FORM) || form.equals(CoolFormRegistrant.SOUND_SCORE_FORM);
	}
	
	public List<Denotator> createObjects(Form objectForm, List<Map<DenotatorPath,Double>> pathsWithValues) {
		List<Denotator> newObjects = new ArrayList<Denotator>();
		for (Map<DenotatorPath,Double> currentPathsWithValues : pathsWithValues) {
			newObjects.add(this.createObject(objectForm, currentPathsWithValues));
		}
		return newObjects;
	}
	
	public Denotator createObject(Form form, Map<DenotatorPath,Double> pathsWithValues) {
		Denotator object = form.createDefaultDenotator();
		object = this.replaceColimitsIfNecessary(object, pathsWithValues.keySet());
		
		for (DenotatorPath currentPath : pathsWithValues.keySet()) {
			Double currentValue = pathsWithValues.get(currentPath);
			object = this.replaceValue(object, currentPath, currentValue);
		}
		return object;
	}
	
	private Denotator replaceColimitsIfNecessary(Denotator object, Set<DenotatorPath> paths) {
		try {
			for (DenotatorPath currentPath : paths) {
				List<DenotatorPath> currentColimitPaths = currentPath.getParentColimitPaths();
				for (DenotatorPath currentColimitPath : currentColimitPaths) {
					ColimitDenotator currentColimit = (ColimitDenotator)object.get(currentColimitPath.toIntArray());
					DenotatorPath currentChildPath = currentPath.subPath(0, currentColimitPath.size()+1);
					if (currentColimit.getIndex() != currentChildPath.getLastIndex()) {
						Form currentChildForm = currentChildPath.getEndForm();
						((ColimitDenotator)object.get(currentColimitPath.toIntArray())).setFactor(currentChildPath.getLastIndex(), currentChildForm.createDefaultDenotator());
						//object = object.replace(currentChildPath.toIntArray(), currentChildForm.createDefaultDenotator());
						//object = object.replace(currentColimitPath.getChildPath(0).toIntArray(), currentChildForm.createDefaultDenotator());
					}
				}
			}
		} catch (LatrunculusCheckedException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	/**
	 * creates the first denotator possible with the first coordinate for all colimits present
	 */
	public Denotator createStandardDenotator(Form form, double... values) {
		Denotator newDenotator = form.createDefaultDenotator();
		List<DenotatorPath> formValuePaths = new FormValueFinder(form, true).getObjectAt(0).getStandardColimitConfigurationValuePaths();
		for (int i = 0; i < Math.min(values.length, formValuePaths.size()); i++) {
			newDenotator = this.replaceValue(newDenotator, formValuePaths.get(i), values[i]);
		}
		return newDenotator;
	}
	
	//TODO: put all of these in specialized class
	public int getIntegerValue(Denotator denotator, int valueIndex) {
		return (int)Math.round(this.getDoubleValue(denotator, valueIndex));
	}
	
	public Double getDoubleValue(Denotator denotator, int valueIndex) {
		DenotatorPath valuePath = new DenotatorValueFinder(denotator, false).getValuePathAt(valueIndex);
		return this.getDoubleValue(denotator, valuePath);
	}
	
	public Double getDoubleValue(Denotator denotator, DenotatorPath valuePath) {
		try {
			if (valuePath.isElementPath()) {
				ModuleElement topElement = ((SimpleDenotator)denotator.get(valuePath.getDenotatorSubpath().toIntArray())).getElement();
				//have to do this like this for the possibility of ProductElements
				return this.extractValue(topElement, valuePath.getElementSubpath().toIntArray());
			} else if (valuePath.getEndForm().getType() == FormDenotatorTypeEnum.SIMPLE) {
				SimpleDenotator simple = (SimpleDenotator)denotator.get(valuePath.toIntArray());
				if (simple != null) {
					return RRing.ring.cast(simple.getElement()).getValue();
				}
				return null;
			}
		} catch (LatrunculusCheckedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Double extractValue(ModuleElement element, int[] elementPath) {
		for (int i = 0; i < elementPath.length; i++) {
			if (element instanceof ProductElement) {
				element = ((ProductElement)element).getFactor(i);
			} else {
				element = element.getComponent(i);
			}
		}
		return RRing.ring.cast(element).getValue();
	}
	
	/**
	 * @return if the denotator is not of the given form, the first instance of that form in the denotator's lower
	 * structure is returned. if there is none, the method checks if the object's form appears 
	 */
	public Denotator convertDenotatorIfNecessary(Denotator denotator, Form form) {
		if (denotator.getForm().equals(form)) {
			return denotator;
		}
		DenotatorPath formInDenotator = this.findFormInDenotator(form, denotator);
		DenotatorPath denotatorInForm = this.findFormInForm(denotator.getForm(), form);
		if (formInDenotator != null && (denotatorInForm == null || formInDenotator.size() <= denotatorInForm.size())) {
			try {
				return denotator.get(formInDenotator.toIntArray());
			} catch (LatrunculusCheckedException e) { e.printStackTrace(); }
		} else if (denotatorInForm != null) {
			return this.createDenotator(form, denotatorInForm, denotator);
		}
		return null;
	}
	
	private DenotatorPath findFormInDenotator(Form form, Denotator denotator) {
		PriorityQueue<DenotatorPath> subPathsQueue = new PriorityQueue<>();
		subPathsQueue.add(new DenotatorPath(denotator.getForm()));
		while (!subPathsQueue.isEmpty()) {
			DenotatorPath currentSubPath = subPathsQueue.poll();
			try {
				Denotator currentDenotator = denotator.get(currentSubPath.toIntArray());
				Form currentForm = currentDenotator.getForm();
				if (currentForm.equals(form)) {
					return currentSubPath;
				} else if (currentForm.getType() != FormDenotatorTypeEnum.SIMPLE && (currentForm.getType() != FormDenotatorTypeEnum.POWER ||
						currentForm.getType() != FormDenotatorTypeEnum.LIST)) {
					for (int i = 0; i < ((FactorDenotator)currentDenotator).getFactorCount(); i++) {
						subPathsQueue.add(currentSubPath.getChildPath(i));
					}
				}
			} catch (LatrunculusCheckedException e) { e.printStackTrace(); }
		}
		return null;
	}
	
	private DenotatorPath findFormInForm(Form form, Form superForm) {
		PriorityQueue<Form> subFormQueue = new PriorityQueue<Form>();
		PriorityQueue<DenotatorPath> subPathQueue = new PriorityQueue<DenotatorPath>();
		subFormQueue.add(superForm);
		subPathQueue.add(new DenotatorPath(superForm));
		while (!subFormQueue.isEmpty()) {
			Form currentForm = subFormQueue.poll();
			DenotatorPath currentPath = subPathQueue.poll();
			if (currentForm.equals(form)) {
				return currentPath;
			} else if (currentForm.getType() != FormDenotatorTypeEnum.SIMPLE && currentForm.getType() != FormDenotatorTypeEnum.POWER
					&& currentForm.getType() != FormDenotatorTypeEnum.LIST) {
				for (int i = 0; i < currentForm.getFormCount(); i++) {
					subFormQueue.add(currentForm.getForm(i));
					subPathQueue.add(currentPath.getChildPath(i));
				}
			}
		}
		return null;
	}
	
	/*
	 * returns a new denotator of the given form where the denotator at the given path is replaced with the given
	 * denotator, if possible
	 */
	private Denotator createDenotator(Form form, DenotatorPath path, Denotator denotator) {
		Denotator defaultDenotator = form.createDefaultDenotator(); 
		try {
			return defaultDenotator.replace(path.toIntArray(), denotator);
		} catch (LatrunculusCheckedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return a copy of object where all simples in its lower structure have been made absolute with respect to the
	 * ones also present in referenceObject. if no simple of the same form is found in referenceObject at the same
	 * path as in object, the first simple of that form found is considered. if none is found, the one in object
	 * stays the same.
	 */
	public Denotator makeObjectAbsolute(Denotator object, Denotator referenceObject) {
		if (object != null) {
			Map<DenotatorPath,SimpleDenotator> objectSimples = this.findSimples(object);
			Map<DenotatorPath,SimpleDenotator> referenceSimples = this.findSimples(referenceObject);
			Map<DenotatorPath,SimpleDenotator> absoluteSimples = new TreeMap<DenotatorPath,SimpleDenotator>();
			for (DenotatorPath currentPath : objectSimples.keySet()) {
				SimpleDenotator currentSimple = objectSimples.get(currentPath);
				SimpleDenotator matchingReferenceSimple = this.getMatchingSimple(currentSimple.getForm(), currentPath, referenceSimples);
				if (matchingReferenceSimple != null) {
					absoluteSimples.put(currentPath, this.createAbsoluteSimple(currentSimple, matchingReferenceSimple));
				}
			}
			return this.replaceSimples(object, absoluteSimples);
		}
		return null;
	}
	
	/**
	 * @return a copy of object where all simples in its lower structure have been made relative of the ones also
	 * present in referenceObject. if no simple of the same form is found in referenceObject at the same path as
	 * in object, the first simple of that form found is considered. if none is found, the one in object stays
	 * absolute.
	 */
	public Denotator makeObjectRelative(Denotator object, Denotator referenceObject) {
		Map<DenotatorPath,SimpleDenotator> objectSimples = this.findSimples(object);
		Map<DenotatorPath,SimpleDenotator> referenceSimples = this.findSimples(referenceObject);
		Map<DenotatorPath,SimpleDenotator> relativeSimples = new TreeMap<DenotatorPath,SimpleDenotator>();
		for (DenotatorPath currentPath : objectSimples.keySet()) {
			SimpleDenotator currentSimple = objectSimples.get(currentPath);
			SimpleDenotator matchingReferenceSimple = this.getMatchingSimple(currentSimple.getForm(), currentPath, referenceSimples);
			if (matchingReferenceSimple != null) {
				relativeSimples.put(currentPath, this.createRelativeSimple(currentSimple, matchingReferenceSimple));
			}
		}
		return this.replaceSimples(object, relativeSimples);
	}
	
	/*
	 * TODO: WHY NOT FIND VALUES??????
	 * returns a list of all simples found in the lower structure of the given denotator
	 */
	private Map<DenotatorPath,SimpleDenotator> findSimples(Denotator object) {
		Map<DenotatorPath,SimpleDenotator> simples = new TreeMap<DenotatorPath,SimpleDenotator>();
		PriorityQueue<DenotatorPath> subPathsQueue = new PriorityQueue<DenotatorPath>();
		subPathsQueue.add(new DenotatorPath(object.getForm()));
		while (!subPathsQueue.isEmpty()) {
			DenotatorPath currentSubPath = subPathsQueue.poll();
			try {
				Denotator currentSubObject = object.get(currentSubPath.toIntArray());
				if (currentSubObject != null) {
					Form currentForm = currentSubObject.getForm();
					if (currentForm.getType() == FormDenotatorTypeEnum.SIMPLE) {
						simples.put(currentSubPath, (SimpleDenotator)currentSubObject);
						//do not search farther if form is either power or list!!
					} else if (currentForm.getType() == FormDenotatorTypeEnum.LIMIT || currentForm.getType() == FormDenotatorTypeEnum.COLIMIT) {
						for (int i = 0; i < currentSubObject.getForm().getFormCount(); i++) {
							subPathsQueue.add(currentSubPath.getChildPath(i));
						}
					}
				}
			} catch (LatrunculusCheckedException e) { e.printStackTrace(); }
		}
		return simples;
	}
	
	private Denotator replaceSimples(Denotator object, Map<DenotatorPath,SimpleDenotator> simples) {
		try {
			for (DenotatorPath currentPath : simples.keySet()) {
				object = object.replace(currentPath.toIntArray(), simples.get(currentPath));
			}
		} catch (LatrunculusCheckedException e) { e.printStackTrace(); }
		return object;
	}
	
	private SimpleDenotator getMatchingSimple(Form form, DenotatorPath path, Map<DenotatorPath,SimpleDenotator> simples) {
		//try at same path
		SimpleDenotator obviousCandidate = simples.get(path);
		if (obviousCandidate != null && obviousCandidate.getForm().equals(form)) {
			return obviousCandidate;
		}
		//search for other instance of same form
		for (SimpleDenotator currentSimple : simples.values()) {
			if (currentSimple.getForm().equals(form)) {
				return currentSimple;
			}
		}
		return null;
	}
	
	private SimpleDenotator createRelativeSimple(SimpleDenotator simple, SimpleDenotator reference) {
		try {
			ModuleElement differenceElement = simple.getElement().difference(reference.getElement());
			return new SimpleDenotator(NameDenotator.make(""), simple.getSimpleForm(), differenceElement);
		} catch(DomainException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private SimpleDenotator createAbsoluteSimple(SimpleDenotator simple, SimpleDenotator reference) {
		try {
			ModuleElement sumElement = simple.getElement().sum(reference.getElement());
			return new SimpleDenotator(NameDenotator.make(""), simple.getSimpleForm(), sumElement);
		} catch(DomainException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Denotator replaceValue(Denotator object, DenotatorPath valuePath, double value) {
		if (valuePath.isElementPath()) {
			int[] simplePath = valuePath.getDenotatorSubpath().toIntArray();
			int[] elementPath = valuePath.getElementSubpath().toIntArray();
			return this.replaceValue(object, simplePath, elementPath, value);
		}
		return this.replaceValue(object, valuePath.toIntArray(), value);
	}
	
	private Denotator replaceValue(Denotator object, int[] simplePath, double value) {
		try {
			SimpleDenotator oldSimple = (SimpleDenotator)object.get(simplePath);
			if (oldSimple != null) {
				ModuleElement newElement = oldSimple.getElement().getModule().cast(new Real((value)));
				SimpleDenotator newSimple = new SimpleDenotator(NameDenotator.make(""), oldSimple.getSimpleForm(), newElement);
				return object.replace(simplePath, newSimple);
			}
		} catch (LatrunculusCheckedException e) { }
		return object;
	}
	
	private Denotator replaceValue(Denotator object, int[] simplePath, int[] elementPath, double value) {
		try {
			SimpleDenotator oldSimple = (SimpleDenotator)object.get(simplePath);
			if (oldSimple != null) {
				ModuleElement newElement = this.createModuleElement(oldSimple.getElement(), elementPath, 0, value);
				SimpleDenotator newSimple = new SimpleDenotator(NameDenotator.make(""), oldSimple.getSimpleForm(), newElement);
				return object.replace(simplePath, newSimple);
			}
		} catch (LatrunculusCheckedException e) { }
		return object;
	}
	
	//TODO: fix things with current position!!!!!
	private ModuleElement createModuleElement(ModuleElement currentElement, int[] elementPath, int currentPosition, double value) {
		int currentIndex = elementPath[currentPosition];
		int currentDimension = currentElement.getModule().getDimension();
		if (currentElement instanceof ProductElement) {
			ProductElement productElement = (ProductElement)currentElement;
			List<RingElement> factors = new ArrayList<>();
			for (int i = 0; i < productElement.getFactorCount(); i++) {
				if (i == currentIndex) {
					factors.add((RingElement)this.createModuleElement(productElement.getFactor(i), elementPath, currentPosition, value));
				} else {
					factors.add(productElement.getFactor(i));
				}
			}
			return productElement.getModule().cast(ProductElement.make(factors));
		} else if (currentDimension > 1) {
			List<Real> values = new VectorModule<>(RRing.ring, currentDimension).cast(currentElement).getValue();
			values.set(elementPath[elementPath.length-1], new Real((value)));
			return currentElement.getModule().cast(new Vector<>(RRing.ring, values));
		} else {
			return currentElement.getModule().cast(new Real((value)));
		}
	}

}
