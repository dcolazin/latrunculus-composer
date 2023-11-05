package org.rubato.rubettes.util;

import org.rubato.base.RubatoException;
import org.rubato.rubettes.bigbang.model.denotators.TransformationPaths;
import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.morphism.CanonicalMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ConstantMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.EmbeddingMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ProjectionMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.SumMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.AffineProjection;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author flo
 * TODO: make everything DenotatorPath!! (adjust wallpaper rubette)
 */
public class ArbitraryDenotatorMapper<A extends ModuleElement<A, RA>, B extends ModuleElement<B, RB>, RA extends RingElement<RA>, RB extends RingElement<RB>> {
	
	private ModuleMorphism<A,B,RA,RB> morphism;
	private final TransformationPaths transformationPaths;
	private Module<A,RA> domain;
	private int domainDim;
	private int codomainDim;
	private List<ModuleMorphism> injectionMorphisms;
	
	public ArbitraryDenotatorMapper(ModuleMorphism<A,B,RA,RB> morphism, TransformationPaths paths) {
		this.transformationPaths = paths;
		this.init(morphism);
	}
	
	//define morphism-specific variables
	private void init(ModuleMorphism<A,B,RA,RB> morphism) {
		this.morphism = morphism;
		this.domain = this.morphism.getDomain();
		this.domainDim = this.domain.getDimension();
		this.codomainDim = this.morphism.getCodomain().getDimension();
		this.injectionMorphisms = this.makeInjectionMorphisms(domain);
	}
	
	/*
	 * maps the input denotator using the input morphism and respecting the paths selected
	 * in the morphisms table. The output denotator is of the same form as the input denotator.
	 */
	public PowerDenotator getMappedPowerDenotator(PowerDenotator input) throws RubatoException {
		//prepare output
		PowerDenotator output = new PowerDenotator(NameDenotator.make(""), input.getAddress(), input.getPowerForm(), new ArrayList<>());
		
		if (this.transformationPaths != null && this.transformationPaths.getDomainDim() == this.domainDim
				&& this.transformationPaths.getCodomainDim() == this.codomainDim) {
			//iterate through the coordinates of the input and add their mapping to the output
			Iterator<Denotator> inputCoordinates = input.iterator();
			Denotator currentCoordinate; //später allgemein!!
			while (inputCoordinates.hasNext()) {
				currentCoordinate = inputCoordinates.next();
				
				Denotator mappedCoordinate = this.getMappedDenotator(currentCoordinate);
				
				if (!output.getAddress().equals(mappedCoordinate.getAddress())) {
					mappedCoordinate = mappedCoordinate.changeAddress(output.getAddress());
				}
				output.appendFactor(mappedCoordinate);
			}
		}
		return output;
	}
	
	public Denotator getMappedDenotator(Denotator denotator) throws RubatoException {
		ModuleMorphism morphism = this.morphism.compose(this.makeInitialInjectionSum(denotator));
		return this.makeFinalProjections(denotator, morphism);
	}
	
	/*
	 * adapt the morphism of every simple denotator in morphismPaths to the main morphism
	 * by composing it with an injection
	 */
	private ModuleMorphism makeInitialInjectionSum(Denotator denotator) throws RubatoException {
		ModuleMorphism injectionSum = null;
		for (int j = 0; j < this.domainDim; j++) {
			
			//TODO: mapping relatively easy for now since we're already doing powersets element by element..
			
			DenotatorPath currentPath = this.transformationPaths.getDomainPath(j, denotator);
			if (currentPath == null) {
				continue;
			}

			ModuleMorphism currentMorphism = null;
			if (currentPath.isElementPath()) {
				currentMorphism = this.makeInitialProjection(denotator, currentPath);
			} else {
				SimpleDenotator currentSimple = this.getSimpleDenotator(denotator, currentPath.toIntArray());
				if (currentSimple != null) {
					currentMorphism = currentSimple.getModuleMorphism();
				}
			}

			if (currentMorphism != null) {
				//give the current morphism a new codomain
				Module newCodomain = this.domain.getComponentModule(j);
				currentMorphism = this.getCastedMorphism(currentMorphism, newCodomain);
				//inject into domain of main morphism
				currentMorphism = this.injectionMorphisms.get(j).compose(currentMorphism);
				//sum all morphisms
				if (injectionSum == null) {
					injectionSum = currentMorphism;
				} else {
					injectionSum = injectionSum.sum(currentMorphism);
				}
			}
		}
		return injectionSum;
	}
	
	private ModuleMorphism makeInitialProjection(Denotator denotator, DenotatorPath path) throws RubatoException {
		SimpleDenotator simple = this.getSimpleDenotator(denotator, path.getDenotatorSubpath().toIntArray());
		ModuleMorphism currentMorphism = simple.getModuleMorphism();
		ModuleElement currentElement = simple.getElement();
		DenotatorPath elementPath = path.getElementSubpath();
		for (int currentIndex : elementPath.toIntArray()) {
			if (currentElement instanceof ProductElement) {
				currentMorphism = this.makeProjection(currentMorphism, currentIndex);
				currentElement = ((ProductElement)currentElement).getFactor(currentIndex);
			} else {
				currentMorphism = this.makeProjection(currentMorphism, currentIndex);
				currentElement = currentElement.getComponent(currentIndex);
			}
		}
		return currentMorphism;
	}
	
	private Denotator makeFinalProjections(Denotator mappedDenotator, ModuleMorphism m) throws RubatoException {
		for (int i = 0; i < this.codomainDim; i++) {
			ModuleMorphism projectedM = m;
			
			//make projection if necessary
			if (this.codomainDim > 1) {
				projectedM = this.makeProjection(m, i);
			}
			
			//replace original coordinate by mapped coordinate 
			DenotatorPath currentCodomainPath = this.transformationPaths.getCodomainPath(i, mappedDenotator);
			
			if (currentCodomainPath != null) {
				//currentCodomainPath = currentCodomainPath.neutralizeColimitIndices();
				SimpleDenotator oldSimple;
				Module newCodomain = null;
				if (currentCodomainPath.isElementPath()) {
					oldSimple = this.getSimpleDenotator(mappedDenotator, currentCodomainPath.getDenotatorSubpath().toIntArray());
					newCodomain = this.getElement(oldSimple, currentCodomainPath.getElementSubpath()).getModule();
				} else {
					oldSimple = this.getSimpleDenotator(mappedDenotator, currentCodomainPath.toIntArray());
					if (oldSimple != null) {
						newCodomain = oldSimple.getModuleMorphism().getCodomain();
					}
				}
				
				if (newCodomain != null) {
					projectedM = this.getCastedMorphism(projectedM, newCodomain);
					
					if (currentCodomainPath.isElementPath()) {
						//ModuleMorphism finalInjection = this.makeFinalInjection(oldSimple, this.denotatorPaths.get(this.domainDim + i));
						int dimension = oldSimple.getElement().getModule().getDimension();
						if (oldSimple.getElement().getModule() instanceof ProductRing) {
							dimension = ((ProductRing)oldSimple.getElement().getModule()).getFactorCount();
						}
						ModuleMorphism sum = null;
						for (int j = 0; j < dimension; j++) {
							ModuleMorphism currentAddend;
							if (j == currentCodomainPath.getLastIndex()) {
								currentAddend = this.makeFinalInjection(oldSimple, currentCodomainPath).compose(projectedM);
							} else {
								DenotatorPath replacedPath = currentCodomainPath.replaceLast(j);
								currentAddend = this.makeInitialProjection(mappedDenotator, replacedPath);
								currentAddend = this.makeFinalInjection(oldSimple, replacedPath).compose(currentAddend);
							}
						
							if (sum != null) {
								sum = SumMorphism.make(sum, currentAddend);
							} else {
								sum = currentAddend;
							}
						}
						
						projectedM = sum;
					}
						
					SimpleForm currentForm = (SimpleForm)oldSimple.getForm();
					try {
						Denotator currentSimpleDenotator;
						//strange that null-addressed denotator yields constantmorphism
						if (oldSimple.getAddress().isNullModule()) {
							currentSimpleDenotator = new SimpleDenotator(NameDenotator.make(""), currentForm, (((ConstantMorphism)projectedM).getValue()));
						} else {
							currentSimpleDenotator = new SimpleDenotator(NameDenotator.make(""), currentForm, projectedM);
						}
						if (currentCodomainPath.size() == 0) {
							mappedDenotator = currentSimpleDenotator;
						} else if (this.transformationPaths != null && currentCodomainPath.isElementPath()) {
							mappedDenotator = mappedDenotator.replace(currentCodomainPath.getDenotatorSubpath().toIntArray(), currentSimpleDenotator);
						} else {
							mappedDenotator = mappedDenotator.replace(currentCodomainPath.toIntArray(), currentSimpleDenotator);
						}
					} catch (DomainException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		
		return mappedDenotator;
	}
	
	//TODO: put all these somewhere else
	private ModuleElement getElement(SimpleDenotator denotator, DenotatorPath elementPath) {
		ModuleElement currentElement = denotator.getElement();
		for (int currentIndex : elementPath.toIntArray()) {
			if (currentElement instanceof ProductElement) {
				currentElement = ((ProductElement)currentElement).getFactor(currentIndex);
			} else {
				currentElement = currentElement.getComponent(currentIndex);
			}
		}
		return currentElement; 
	}
	
	private ModuleMorphism makeFinalInjection(SimpleDenotator simple, DenotatorPath path) throws CompositionException {
		Module currentModule = simple.getElement().getModule();
		ModuleMorphism finalInjection = null;
		for (int currentIndex : path.toIntArray()) {
			ModuleMorphism currentInjection = this.makeInjectionMorphism(currentModule.getRing(), currentModule.getDimension(), currentIndex);
			if (finalInjection != null) {
				finalInjection = currentInjection.compose(finalInjection);
			} else {
				finalInjection = currentInjection;
			}
			if (currentModule instanceof ProductRing) {
				currentModule = ((ProductRing)currentModule).getFactor(currentIndex);
			} else {
				currentModule = currentModule.getComponentModule(currentIndex);
			}
		}
		return finalInjection;
	}
	
	/*
	 * returns the simple denotator along the path within the specified denotator.
	 * also handles the empty path [] in case the denotator itself is of type simple
	 */
	//TODO: put all these somewhere else
	private SimpleDenotator getSimpleDenotator(Denotator denotator, int[] path) throws RubatoException {
		if (path.length == 0){
			return (SimpleDenotator)denotator;
		}
		return (SimpleDenotator)denotator.get(path);
	}
	
	/*
	 * returns a list of injection morphisms, one for each dimension of the codomain module
	 * example: c:Q^2, then return [Q->Q^2, Q->Q^2]
	 * TODO: does not work for product rings, does it?
	 */
	private List<ModuleMorphism> makeInjectionMorphisms(Module<A,RA> codomain) {
		List<ModuleMorphism> injections = new ArrayList<>();
		int codim = codomain.getDimension();
		Ring<RA> ring = codomain.getRing();
		for (int i = 0; i < codim; i++) {
			injections.add(this.makeInjectionMorphism(ring, codim, i));
		}
		return injections;
	}
	
	private ModuleMorphism makeInjectionMorphism(Ring<RA> ring, int codomainDim, int index) {
		if (ring instanceof ProductRing) {
			ProductRing product = (ProductRing)ring;
			return EmbeddingMorphism.makeProductRingEmbedding(product.getFactor(index), product, index);
		}
		GenericAffineMorphism injection = GenericAffineMorphism.make(ring, 1, codomainDim);
		RA one = ring.getOne();
		injection.setMatrix(index, 0, one);
		return injection;
	}
	
	/*
	 * composes the input morphism with a projection on the component at 'index'.
	 * example: m:R^2->R^2, i:1, then return m:R^2->R with m(x,y) = m(y)
	 */
	private ModuleMorphism makeProjection(ModuleMorphism morphism, int index) throws CompositionException {
		if (morphism.getCodomain() instanceof ProductRing) {
			ModuleMorphism projection = ProjectionMorphism.make((ProductRing)morphism.getCodomain(), index);
			return projection.compose(morphism);
		}
		if (morphism.getCodomain() instanceof VectorModule) {
			VectorModule<?> multiModule = (VectorModule<?>) morphism.getCodomain();
			ModuleMorphism projection = new AffineProjection(multiModule.getRing(), multiModule.getUnitElement(index), multiModule.getRing().getZero());
			return projection.compose(morphism);
		}
		if (morphism.getCodomain() instanceof Ring) {
			return morphism;
		}
		return null;
	}
	
	/*
	 * composes the input morphism with a casting morphism for the specified codomain.
	 * example: m:Q->Q, c:R, then return m:Q->R 
	 */
	private ModuleMorphism getCastedMorphism(ModuleMorphism morphism, Module newCodomain) throws CompositionException {
		Module oldCodomain = morphism.getCodomain();
		if (!newCodomain.equals(oldCodomain)) {
			ModuleMorphism castingMorphism = CanonicalMorphism.make(oldCodomain, newCodomain);
			morphism = castingMorphism.compose(morphism);
		}
		return morphism;
	}

}
