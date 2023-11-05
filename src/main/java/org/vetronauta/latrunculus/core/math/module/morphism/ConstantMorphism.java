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
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * Constant mappings between modules.
 * A constant morphism encapsulates a value, an element in a module.
 * The codomain is always the module of the element. The domain
 * may default to the codomain, or it can be specified.
 * 
 * @author Gérard Milmeister
 */
public final class ConstantMorphism<A extends ModuleElement<A, RA>, B extends ModuleElement<B, RB>, RA extends RingElement<RA>, RB extends RingElement<RB>>
        extends ModuleMorphism<A,B,RA,RB> {

    private final B value;

    /**
     * Creates a constant morphism with value <code>v</code>
     * and domain <code>m</code>.
     */
    public ConstantMorphism(Module<A,RA> m, B value) {
        super(m, value.getModule());
        this.value = value;
    }

    /**
     * Creates a constant morphism with value <code>v</code>.
     * The domain is the same as the codomain.
     */
    public static <X extends ModuleElement<X,RX>, RX extends RingElement<RX>> ConstantMorphism<X,X,RX,RX> make(X value) {
        return new ConstantMorphism<>(value.getModule(), value);
    }

    public B map(A x) {
        return value;
    }

    @Override
    public boolean isModuleHomomorphism() {
        return true;
    }

    @Override
    public boolean isRingHomomorphism() {
        return value.isZero();
    }

    @Override
    public boolean isLinear() {
        return value.isZero();
    }
    
    @Override
    public boolean isConstant() {
        return true;
    }

    
    public ModuleMorphism<RA,RB,RA,RB> getRingMorphism() {
        Ring<RA> domainRing = getDomain().getRing();
        Ring<RB> codomainRing = getCodomain().getRing();
        return CanonicalMorphism.make(domainRing, codomainRing);
    }

    @Override
    public B atZero() {
        return getValue();
    }

    /**
     * Returns the constant value.
     */
    public B getValue() {
        return value;
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ConstantMorphism) {
            ConstantMorphism<?,?,?,?> cm = (ConstantMorphism<?,?,?,?>)object;
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
            ConstantMorphism<?,?,?,?> cm = (ConstantMorphism<?,?,?,?>)object;
            return getDomain().equals(cm.getDomain())
                   && value.equals(cm.getValue());
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("ConstantMorphism<%s,%s>[%s]", getDomain(), getCodomain(), getValue());
    }

    public String getElementTypeName() {
        return "ConstantMorphism";
    }
    
}
