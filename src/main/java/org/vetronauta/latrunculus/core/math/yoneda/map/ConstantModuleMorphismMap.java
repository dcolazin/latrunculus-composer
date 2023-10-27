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

import org.rubato.base.RubatoException;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ConstantMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.Map;

/**
 * Morphism map containing a constant module morphism.
 *
 * @author Gérard Milmeister
 */
public final class ConstantModuleMorphismMap extends ModuleMorphismMap {

    /**
     * Creates a constant morphism with constant <code>element</code>.
     * The codomain is domain of the element.
     * The domain is the null module corresponding to the codomain. 
     */
    public ConstantModuleMorphismMap(ModuleElement element) {
        super();
        this.codomain = null;
        this.domain = null;
        this.moduleElement = element;
    }

    
    /**
     * Creates a constant morphism with constant <code>element</code>.
     * The codomain is domain of the element.
     * The domain is the specified one. 
     */
    public ConstantModuleMorphismMap(Module domain, ModuleElement element) {
        super();
        this.moduleElement = element;
        this.domain = domain;
        this.codomain = null;
    }

    
    public ModuleElement getElement() {
        return moduleElement;
    }

    
    public ModuleMorphism getMorphism() {
        if (moduleMorphism == null) {
            moduleMorphism = new ConstantMorphism(getDomain(), getElement());
        }
        return moduleMorphism;
    }
    
    
    public Module getDomain() {
        if (domain == null) {
            domain = getCodomain().getNullModule();
        }
        return domain;
    }


    public Module getCodomain() {
        if (codomain == null) {
            codomain = moduleElement.getModule();
        }
        return codomain;
    }

    
    public void setElement(ModuleElement element) {
        assert(element.getModule().equals(getCodomain()));
        this.moduleElement = element;
    }

    
    public int compareTo(MorphismMap object) {
        if (object instanceof ConstantModuleMorphismMap) {
            return moduleElement.compareTo(((ConstantModuleMorphismMap)object).getElement());
        }
        else if (object instanceof ModuleMorphismMap) {
            return getMorphism().compareTo(((ModuleMorphismMap)object).getMorphism());
        }
        else {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ConstantModuleMorphismMap) {
            return moduleElement.equals(((ConstantModuleMorphismMap)object).getElement());
        }
        else if (object instanceof ModuleMorphismMap) {
            return getMorphism().equals(((ModuleMorphismMap)object).getMorphism());
        }
        else {
            return false;
        }
    }
   
   
    public boolean fullEquals(MorphismMap map, Map<Object,Object> s) {
        return equals(map);
    }

    
    public ModuleElement map(ModuleElement element) {
        return this.moduleElement;
    }

    
    public MorphismMap at(ModuleElement element) {
        if (domain == null || getDomain().isNullModule()) {
            return this;
        }
        else {
            return new ConstantModuleMorphismMap(getDomain().getNullModule(), getElement());
        }
    }
    
    
    public ModuleMorphismMap changeDomain(ModuleMorphism morphism) {
        if (morphism.getCodomain().equals(getDomain())) {
            Module newDomain = morphism.getDomain();
            if (newDomain.equals(getDomain())) {
                return this;
            }
            else {
                return new ConstantModuleMorphismMap(newDomain, getElement());
            }
        }
        else {
            return null;
        }
    }
    

    public ModuleMorphismMap changeDomain(Module newAddress) {
        return new ConstantModuleMorphismMap(newAddress, getElement());
    }

    
    public ModuleMorphismMap map(ModuleMorphism morphism)
            throws RubatoException {
        if (morphism.getDomain().equals(morphism.getCodomain())) {
            ModuleElement res = morphism.map(moduleElement);
            return new ConstantModuleMorphismMap(getDomain(), res);
        }
        throw new RubatoException("ConstantModuleMorphismMap.map: Domain and codomain of morphism must be equal");
    }
    
    
    public ModuleMorphismMap sum(ModuleMorphismMap map) {
        if (map instanceof ConstantModuleMorphismMap) {
            ConstantModuleMorphismMap nullMap = (ConstantModuleMorphismMap)map;
            try {
                return new ConstantModuleMorphismMap(getElement().sum(nullMap.getElement()));
            }
            catch (DomainException e) {
                return null;
            }
        }
        else {
            try {
                return new ModuleMorphismMap(getMorphism().sum(map.getMorphism()));
            }
            catch (CompositionException e) {
                return null;
            }
        }
    }
    
    
    public ModuleMorphismMap difference(ModuleMorphismMap map) {
        if (map instanceof ConstantModuleMorphismMap) {
            ConstantModuleMorphismMap nullMap = (ConstantModuleMorphismMap)map;
            try {
                return new ConstantModuleMorphismMap(getElement().difference(nullMap.getElement()));
            }
            catch (DomainException e) {
                return null;
            }
        }
        else {
            try {
                return new ModuleMorphismMap(getMorphism().difference(map.getMorphism()));
            }
            catch (CompositionException e) {
                return null;
            }
        }
    }

    
    public boolean isConstant() {
        return true;
    }

    @Override
    public ModuleMorphismMap deepCopy() {
        return new ConstantModuleMorphismMap(moduleElement);
    }

    public String toString() {
        return "ConstantModuleMorphismMap["+moduleElement+"]";
    }

    public String getElementTypeName() {
        return "ConstantModuleMorphismMap";
    }
    
    
    public int hashCode() {
        return moduleElement.hashCode();
    }


    private ModuleElement  moduleElement;
    private Module         domain   = null;
    private Module         codomain = null;
}
