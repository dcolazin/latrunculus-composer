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

package org.vetronauta.latrunculus.core.math.morphism;

import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.morphism.endo.Endomorphism;

/**
 * Identity mappings on a module.
 * 
 * @author Gérard Milmeister
 */
public final class IdentityMorphism<A extends ModuleElement<A, RA>, RA extends RingElement<RA>> extends Endomorphism<A,RA> {

    /**
     * Creates an identity morphism on the module <code>m</code>.
     */
    public IdentityMorphism(Module<A,RA> m) {
        super(m);
    }

    public A map(A x) {
        return x;
    }
    
    @Override
    public boolean isModuleHomomorphism() {
        return true;
    }

    @Override
    public boolean isRingHomomorphism() {
        return true;
    }
    
    @Override
    public boolean isLinear() {
        return true;
    }
    
    @Override
    public boolean isIdentity() {
        return true;
    }
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    @Override
    public IdentityMorphism<RA, RA> getRingMorphism() {
        return ModuleMorphism.getIdentityMorphism(getDomain().getRing());
    }
    
    @Override
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphism<C,A,RC,RA> compose(ModuleMorphism<C,A,RC,RA> morphism) throws CompositionException {
        if (ModuleMorphism.composable(this, morphism)) {
            return morphism;
        }
        return super.compose(morphism);
    }
    
    @Override
    public IdentityMorphism<A, RA> power(int n) {
        return this;
    }
    
    @Override
    public A atZero() {
        return getCodomain().getZero();
    }

    public int compareTo(ModuleMorphism object) {
        if (object instanceof IdentityMorphism) {
            return object.getDomain().compareTo(getDomain());
        }
        return super.compareTo(object);
    }

    public boolean equals(Object object) {
        if (object instanceof IdentityMorphism) {
            return getDomain().equals(((IdentityMorphism)object).getDomain());
        }
        return false;
    }

    public String toString() {
        return "IdentityMorphism["+getDomain()+"]";
    }

    public String getElementTypeName() {
        return "IdentityMorphism";
    }
}