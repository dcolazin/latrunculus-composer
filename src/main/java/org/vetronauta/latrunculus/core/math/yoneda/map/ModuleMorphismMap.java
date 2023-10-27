/*
 * Copyright (C) 2002, 2005 Gérard Milmeister
 * Copyright (C) 2002 Stefan Müller
 * Copyright (C) 2002 Stefan Göller
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.vetronauta.latrunculus.core.math.yoneda.map;

import org.rubato.base.RubatoDictionary;
import org.rubato.base.RubatoException;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.CanonicalMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;

import java.util.List;
import java.util.Map;

/**
 * Morphism map containing a module morphism (for type simple).
 * Domain and codomain are modules.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public class ModuleMorphismMap<A extends ModuleElement<A,RA>, B extends ModuleElement<B,RB>, RA extends RingElement<RA>, RB extends RingElement<RB>>
        implements MorphismMap {

    private final ModuleMorphism<A,B,RA,RB> moduleMorphism;

    /**
     * Creates a module morphism map with the specified module morphism.
     */
    public ModuleMorphismMap(ModuleMorphism<A,B,RA,RB> morphism) {
        this.moduleMorphism = morphism;
    }

    public static <A extends ModuleElement<A,RA>, B extends ModuleElement<B,RB>, RA extends RingElement<RA>, RB extends RingElement<RB>>
    ModuleMorphismMap<A,B,RA,RB> make(ModuleMorphism<A,B,RA,RB> morphism) {
        Module<A,RA> domain = morphism.getDomain();
        if (domain.isNullModule() || morphism.isConstant()) {
            try {
                return new ConstantModuleMorphismMap<>(domain, morphism.map(domain.getZero()));
            }
            catch (MappingException e) {
                // should never reach this point
                throw new AssertionError(e);
            }
        }
        return new ModuleMorphismMap<>(morphism);
    }

    /**
     * Returns the element resulting from applying the morphism to zero.
     */
    public B getElement() {
        try {
            return moduleMorphism.map(getDomain().getZero());
        }
        catch (MappingException e) {
            throw new AssertionError("Should never reach this point!");
        }
    }

    /**
     * Returns the module morphism.
     */
    public ModuleMorphism<A,B,RA,RB> getMorphism() {
        return moduleMorphism;
    }

    /**
     * Returns the domain of the map.
     */
    public Module<A,RA> getDomain() {
        return getMorphism().getDomain();
    }

    /**
     * Returns the codomain of the map.
     */
    public Module<B,RB> getCodomain() {
        return getMorphism().getCodomain();
    }
    
    @Override
    public int compareTo(MorphismMap object) {
        if (object instanceof ConstantModuleMorphismMap) {
            //TODO wrong? it does not ensure that a.compare(b) != b.compare(a)
            return 1;
        }
        if (object instanceof ModuleMorphismMap){
            return moduleMorphism.compareTo(((ModuleMorphismMap<?,?,?,?>)object).getMorphism());
        }
        return -1;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ConstantModuleMorphismMap) {
            //TODO wrong? it does not ensure that a.equals(b) == b.equals(a)
            return false;
        }
        if (object instanceof ModuleMorphismMap) {
            return moduleMorphism.equals(((ModuleMorphismMap<?,?,?,?>)object).moduleMorphism);
        }
        return false;
    }
   
    @Override
    public boolean fullEquals(MorphismMap map, Map<Object,Object> s) {
        return equals(map);
    }


    /**
     * Maps the specified module element using the module morphism.
     */
    public B map(A x) throws MappingException {
        return moduleMorphism.map(x);
    }

    public ModuleElement<?,RB> getElement(int[] path, int currentPosition) {
        ModuleElement<?,RB> x = getElement();
        for (int i = currentPosition; i < path.length; i++) {
            if (x != null) {
                x = x.getComponent(path[i]);
            }
        }
        return x;
    }

    
    /**
     * Creates a constant morphism from this morphism by
     * evaluating it at the specified element.
     * 
     * @return a ConstantModuleMorphismMap
     * @throws MappingException if evaluation fails
     */
    @Override
    public MorphismMap at(ModuleElement element) throws MappingException {
        //TODO do not cast...
        return new ConstantModuleMorphismMap<>(getDomain().getNullModule(), map((A) element));
    }

    /**
     * Returns a module morphism map with the given new domain.
     * 
     * @return null if a new module morphism map could not be created
     */
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphismMap<C,B,RC,RB>
    changeDomain(Module<C,RC> newDomain) {
        ModuleMorphism<C,A,RC,RA> m = CanonicalMorphism.make(newDomain, getDomain());
        if (m != null) {
            return changeDomain(m);
        }
        return null;
    }

    
    /**
     * Compose <code>morphism</code> with the morphism contained
     * in this ModuleMorphismMap.
     * 
     * @return a new ModuleMorphismMap or null if composition failed
     */    
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphismMap<C,B,RC,RB>
    changeDomain(ModuleMorphism<C,A,RC,RA> otherMorphism) {
        try {
            return new ModuleMorphismMap<>(getMorphism().compose(otherMorphism));
        } catch (CompositionException e) {
            return null;
        }
    }

    @Override
    public MorphismMap changeAddress(Module address) {
        return changeDomain(address);
    }
    
    @Override
    public MorphismMap changeAddress(ModuleMorphism otherMorphism) {
        return changeDomain(otherMorphism);
    }

    
    /**
     * Composes the morphism contained in this map with the
     * given <code>morphism</code>. The resulting morphism
     * is f(x) = this.morphism(morphism(x)).
     */
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphismMap<C,B,RC,RB>
    map(ModuleMorphism<C,A,RC,RA> otherMorphism) throws RubatoException {
        ModuleMorphismMap<C,B,RC,RB> newMap = changeDomain(otherMorphism);
        if (newMap == null) {
            throw new RubatoException("ModuleMorphismMap.map: Domain and codomain of morphism must be equal");
        }
        return newMap;
    }

    /**
     * Returns the sum of this map with the given map.
     * 
     * @return null if the sum fails
     */
    public ModuleMorphismMap<A,B,RA,RB> sum(ModuleMorphismMap<A,B,RA,RB> map) {
        try {
            return new ModuleMorphismMap<>(moduleMorphism.sum(map.getMorphism()));
        } catch (CompositionException e) {
            return null;
        }            
    }
    
    
    /**
     * Returns the difference of this map with the given map.
     * 
     * @return null if the difference fails
     */
    public ModuleMorphismMap<A,B,RA,RB> difference(ModuleMorphismMap<A,B,RA,RB> map) {
        try {
            return new ModuleMorphismMap<>(moduleMorphism.difference(map.getMorphism()));
        } catch (CompositionException e) {
            return null;
        }                        
    }

    public String getElementTypeName() {
        return "ModuleMorphismMap";
    }

    @Override
    public List<Form> getFormDependencies(List<Form> list) {
        return list;
    }

    @Override
    public List<Denotator> getDenotatorDependencies(List<Denotator> list) {
        return list;
    }

    
    /**
     * Resolves the references resulting from parsing.
     * @return true iff all references have been resolved.
     */
    @Override
    public boolean resolveReferences(RubatoDictionary reader, Map<Object,Object> history) {
        return true;
    }
    
    
    public boolean isConstant() {
        return moduleMorphism.isConstant();
    }

    @Override
    public ModuleMorphismMap<A,B,RA,RB> deepCopy() {
        return new ModuleMorphismMap<>(moduleMorphism.deepCopy());
    }

    @Override
    public String toString() {
        return "ModuleMorphismMap["+moduleMorphism+"]";
    }

    /**
     * Returns a hash code of this module morphism map.
     */
    @Override
    public int hashCode() {
        return moduleMorphism.hashCode();
    }

}
