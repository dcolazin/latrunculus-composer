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

package org.vetronauta.latrunculus.core.math.yoneda;

import org.rubato.base.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.IdentityHashMap;
import java.util.LinkedList;

/**
 * Identity morphism representing a module "object".
 * Thus it is "representable". 
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class RepresentableIdentityMorphism extends IdentityMorphism {

    /**
     * Creates an identity morphism representing the given module.
     */
    public RepresentableIdentityMorphism(Module module) {
        this.module = module;
        this.lowValue = null;
        this.highValue = null;
    }


    /**
     * Creates an identity morphism representing the given module.
     * This variant specifies lower and upper bounds for the values
     * contained in the module.
     */
    public RepresentableIdentityMorphism(Module module, ModuleElement lowValue, ModuleElement highValue) {
        this.module = module;
        this.lowValue = lowValue;
        this.highValue = highValue;
    }


    public Diagram getDiagram() {
        return FormDiagram.emptyFormDiagram;
    }


    public Module getModule() {
        return module;
    }
    

    public ModuleElement getLowValue() {
        return lowValue;
    }

    
    public ModuleElement getHighValue() {
        return highValue;
    }


    public boolean hasBounds() {
        return (lowValue != null) && (highValue != null);
    }


    public int getType() {
        return SIMPLE;
    }


    public boolean isRepresentable() {
        return true;
    }
    

    public YonedaMorphism at(ModuleElement element) {
        return this;
    }

    
    public YonedaMorphism changeAddress(Module address) {
        return this;
    }


    public YonedaMorphism changeAddress(ModuleMorphism morphism) {
        return this;
    }

    
    public RepresentableIdentityMorphism copy() {
        return new RepresentableIdentityMorphism(module, lowValue, highValue); 
    }


    public int compareTo(YonedaMorphism object) {
        if (this == object) {
            return 0;
        }
        else if (object instanceof RepresentableIdentityMorphism) {
            RepresentableIdentityMorphism m = (RepresentableIdentityMorphism)object;
            int c = module.compareTo(m.getModule());
            if (c == 0) {
                if (highValue != null) {
                    if (m.highValue != null) {
                        int comp1 = highValue.compareTo(m.highValue);
                        if (comp1 == 0) {
                            return lowValue.compareTo(m.lowValue);
                        }
                        else {
                            return comp1;
                        }
                    }
                    else {
                        return 1;
                    }                    
                }
            }
            return c;
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        else if (object instanceof RepresentableIdentityMorphism) {
            RepresentableIdentityMorphism m = (RepresentableIdentityMorphism)object;
            if (module.equals(m.module)) {
                return compareBounds(m);
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    
    public boolean fullEquals(YonedaMorphism m, IdentityHashMap<Object,Object> s) {
        return equals(m);
    }
    
    
    public LinkedList<Form> getFormDependencies(LinkedList<Form> list) {
        return list;
    }
    
    
    public LinkedList<Denotator> getDenotatorDependencies(LinkedList<Denotator> list) {
        return list;
    }
    
    
    public String toString() {
        return "RepresentableIdentityMorphism["+module+"]";
    }
    
    public String getElementTypeName() {
        return "RepresentableIdentityMorphism";
    }
    
    
    public int hashCode() {
        int hash = 7;
        hash = 37*hash + module.hashCode();
        if (lowValue != null) {
            hash = 37*hash + lowValue.hashCode();
        }
        if (highValue != null) {
            hash = 37*hash + highValue.hashCode();
        }
        return hash;
    }
    

    public int getMorphOrder() {
        return 0xEDDA;
    }


    boolean resolveReferences(RubatoDictionary dict, IdentityHashMap<?,?> history) {
        return true;
    }
    
    
    private boolean compareBounds(RepresentableIdentityMorphism m) {
        if (hasBounds() && m.hasBounds()) {
            return lowValue.equals(m.lowValue) && highValue.equals(m.highValue);
        }
        else {
            return hasBounds() == m.hasBounds();
        }
    }

    
    private Module        module;
    private ModuleElement lowValue;
    private ModuleElement highValue;
}
