/*
 * Copyright (C) 2002, 2005 Gérard Milmeister
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

import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.morphism.ConstantMorphism;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;

import java.util.Map;

/**
 * Morphism map containing a constant module morphism.
 *
 * @author Gérard Milmeister
 */
public final class ConstantModuleMorphismMap<A extends ModuleElement<A,RA>, B extends ModuleElement<B,RB>, RA extends RingElement<RA>, RB extends RingElement<RB>>
        extends ModuleMorphismMap<A,B,RA,RB> {

    private final B moduleElement;
    private Module<A,RA> domain;
    private Module<B,RB> codomain;

    /**
     * Creates a constant morphism with constant <code>element</code>.
     * The codomain is domain of the element.
     * The domain is the null module corresponding to the codomain. 
     */
    public ConstantModuleMorphismMap(B element) {
        //TODO a little wrong: extend this class to a <NullElement<RA>,A,RA,RA> class...
        super((ModuleMorphism) new ConstantMorphism<>(element.getModule().getNullModule(), element));
        this.moduleElement = element;
    }
    
    /**
     * Creates a constant morphism with constant <code>element</code>.
     * The codomain is domain of the element.
     * The domain is the specified one. 
     */
    public ConstantModuleMorphismMap(Module<A,RA> domain, B element) {
        super(new ConstantMorphism<>(domain, element));
        this.moduleElement = element;
        this.domain = domain;
    }

    @Override
    public B getElement() {
        return moduleElement;
    }

    @Override
    public ModuleMorphism<A,B,RA,RB> getMorphism() {
        return super.getMorphism();
    }
    
    @Override
    public Module<A,RA> getDomain() {
        return domain;
    }

    @Override
    public Module<B,RB> getCodomain() {
        if (codomain == null) {
            codomain = moduleElement.getModule();
        }
        return codomain;
    }

    @Override
    public int compareTo(MorphismMap object) {
        if (object instanceof ConstantModuleMorphismMap) {
            return moduleElement.compareTo(((ConstantModuleMorphismMap<?,?,?,?>)object).getElement());
        }
        if (object instanceof ModuleMorphismMap) {
            return getMorphism().compareTo(((ModuleMorphismMap<?,?,?,?>)object).getMorphism());
        }
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ConstantModuleMorphismMap) {
            return moduleElement.equals(((ConstantModuleMorphismMap<?,?,?,?>)object).getElement());
        }
        if (object instanceof ModuleMorphismMap) {
            return getMorphism().equals(((ModuleMorphismMap)object).getMorphism());
        }
        return false;
    }
   
    @Override
    public boolean fullEquals(MorphismMap map, Map<Object,Object> s) {
        return equals(map);
    }

    @Override
    public B map(A element) {
        return this.moduleElement;
    }

    @Override
    public MorphismMap at(ModuleElement element) {
        if (domain == null || getDomain().isNullModule()) {
            return this;
        }
        return new ConstantModuleMorphismMap<>(getDomain().getNullModule(), getElement());
    }
    
    @Override
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphismMap<C,B,RC,RB>
    changeDomain(ModuleMorphism<C,A,RC,RA> otherMorphism) {
        if (!otherMorphism.getCodomain().equals(getDomain())) {
            return null;
        }
        Module<C,RC> newDomain = otherMorphism.getDomain();
        if (newDomain.equals(getDomain())) {
            return (ModuleMorphismMap) this;
        }
        return new ConstantModuleMorphismMap<>(newDomain, getElement());
    }
    
    @Override
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphismMap<C,B,RC,RB>
    changeDomain(Module<C,RC> newDomain) {
        return new ConstantModuleMorphismMap<>(newDomain, getElement());
    }

    @Override
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphismMap<C,B,RC,RB>
    map(ModuleMorphism<C,A,RC,RA> otherMorphism) throws LatrunculusCheckedException {
        if (otherMorphism.getDomain().equals(otherMorphism.getCodomain())) {
            return new ConstantModuleMorphismMap<>(otherMorphism.getDomain(), getElement());
        }
        throw new LatrunculusCheckedException("ConstantModuleMorphismMap.map: Domain and codomain of morphism must be equal");
    }
    
    @Override
    public ModuleMorphismMap<A,B,RA,RB> sum(ModuleMorphismMap<A,B,RA,RB> map) {
        if (map instanceof ConstantModuleMorphismMap) {
            ConstantModuleMorphismMap<A,B,RA,RB> nullMap = (ConstantModuleMorphismMap<A,B,RA,RB>) map;
            return new ConstantModuleMorphismMap<>(getElement().sum(nullMap.getElement()));
        }
        try {
            return new ModuleMorphismMap<>(getMorphism().sum(map.getMorphism()));
        }
        catch (CompositionException e) {
            return null;
        }
    }
    
    @Override
    public ModuleMorphismMap<A,B,RA,RB> difference(ModuleMorphismMap<A,B,RA,RB> map) {
        if (map instanceof ConstantModuleMorphismMap) {
            ConstantModuleMorphismMap<A,B,RA,RB> nullMap = (ConstantModuleMorphismMap<A,B,RA,RB>) map;
            try {
                return new ConstantModuleMorphismMap<>(getElement().difference(nullMap.getElement()));
            }
            catch (DomainException e) {
                return null;
            }
        }
        else {
            try {
                return new ModuleMorphismMap<>(getMorphism().difference(map.getMorphism()));
            }
            catch (CompositionException e) {
                return null;
            }
        }
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public ConstantModuleMorphismMap<A,B,RA,RB> deepCopy() {
        return new ConstantModuleMorphismMap<>(moduleElement.deepCopy());
    }

    @Override
    public String toString() {
        return "ConstantModuleMorphismMap["+moduleElement+"]";
    }

    @Override
    public String getElementTypeName() {
        return "ConstantModuleMorphismMap";
    }
    
    @Override
    public int hashCode() {
        return moduleElement.hashCode();
    }

}
