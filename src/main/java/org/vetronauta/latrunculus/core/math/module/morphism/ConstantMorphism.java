/*
 * Copyright (C) 2001, 2005 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.math.module.morphism;

import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;

/**
 * Constant mappings between modules.
 * A constant morphism encapsulates a value, an element in a module.
 * The codomain is always the module of the element. The domain
 * may default to the codomain, or it can be specified.
 * 
 * @author Gérard Milmeister
 */
public final class ConstantMorphism extends ModuleMorphism {

    /**
     * Creates a constant morphism with value <code>v</code>
     * and domain <code>m</code>.
     */
    public ConstantMorphism(Module m, ModuleElement value) {
        super(m, value.getModule());
        this.value = value;
    }


    /**
     * Creates a constant morphism with value <code>v</code>.
     * The domain is the same as the codomain.
     */
    public ConstantMorphism(ModuleElement value) {
        super(value.getModule(), value.getModule());
        this.value = value;
    }


    public ModuleElement map(ModuleElement x) {
        return value;
    }

    
    public boolean isModuleHomomorphism() {
        return true;
    }
    
    
    public boolean isRingHomomorphism() {
        return value.isZero();
    }
    
    
    public boolean isLinear() {
        return value.isZero();
    }
    
    
    public boolean isConstant() {
        return true;
    }

    
    public ModuleMorphism getRingMorphism() {
        Ring domainRing = getDomain().getRing();
        Ring codomainRing = getCodomain().getRing();
        return CanonicalMorphism.make(domainRing, codomainRing);
    }
    
    
    public ModuleElement atZero() {
        return getValue();
    }

    
    /**
     * Returns the constant value.
     */
    public ModuleElement getValue() {
        return value;
    }
    
    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ConstantMorphism) {
            ConstantMorphism cm = (ConstantMorphism)object;
            int comp = getDomain().compareTo(cm.getDomain());
            if (comp == 0) {
                return value.compareTo(cm.getValue());
            }
            else {
                return comp;
            }
        }
        else {
            return super.compareTo(object);
        }
    }        


    public boolean equals(Object object) {
        if (object instanceof ConstantMorphism) {
            ConstantMorphism cm = (ConstantMorphism)object;
            return getDomain().equals(cm.getDomain())
                   && value.equals(cm.getValue());
        }
        else {
            return false;
        }
    }

    
    public String toString() {
        return "ConstantMorphism["+getValue()+"]";
    }

    public String getElementTypeName() {
        return "ConstantMorphism";
    }
    
    
    private ModuleElement value;
}
