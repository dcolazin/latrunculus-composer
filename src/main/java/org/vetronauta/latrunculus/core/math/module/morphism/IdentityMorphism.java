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

/**
 * Identity mappings on a module.
 * 
 * @author Gérard Milmeister
 */
public final class IdentityMorphism extends ModuleMorphism {

    /**
     * Creates an identity morphism on the module <code>m</code>.
     */
    public IdentityMorphism(Module m) {
        super(m, m);
    }
    

    public ModuleElement map(ModuleElement x)
            throws MappingException {
        if (getDomain().hasElement(x)) {
            return x;
        }
        else {
            throw new MappingException("IdentityMorphism.map: ", x, this);
        }
    }
    

    public boolean isModuleHomomorphism() {
        return true;
    }

    
    public boolean isRingHomomorphism() {
        return true;
    }
    
    
    public boolean isLinear() {
        return true;
    }
    
    
    public boolean isIdentity() {
        return true;
    }
    
    
    public boolean isConstant() {
        return false;
    }
    
    
    public ModuleMorphism getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }
    

    public ModuleMorphism compose(ModuleMorphism morphism)
            throws CompositionException {
        if (composable(this, morphism)) {
            return morphism;
        }
        else {
            return super.compose(morphism);
        }
    }
    
    
    public ModuleMorphism power(int n) {
        return this;
    }
    
    
    public ModuleElement atZero() {
        return getCodomain().getZero();
    }
    

    public int compareTo(ModuleMorphism object) {
        if (object instanceof IdentityMorphism) {
            return object.getDomain().compareTo(getDomain());
        }
        else {
            return super.compareTo(object);
        }
    }
    
    
    public boolean equals(Object object) {
        if (object instanceof IdentityMorphism) {
            return getDomain().equals(((IdentityMorphism)object).getDomain());
        }
        else {
            return false;
        }
    }
    
    
    public String toString() {
        return "IdentityMorphism["+getDomain()+"]";
    }

    public String getElementTypeName() {
        return "IdentityMorphism";
    }
}