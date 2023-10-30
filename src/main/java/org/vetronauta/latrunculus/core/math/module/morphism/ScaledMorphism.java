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

import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * Morphism that represents a scaled arbitrary morphism.
 * 
 * @author Gérard Milmeister
 */
public final class ScaledMorphism<A extends ModuleElement<A, RA>, B extends ModuleElement<B, RB>, RA extends RingElement<RA>, RB extends RingElement<RB>>
        extends ModuleMorphism<A,B,RA,RB> {

    private final ModuleMorphism<A,B,RA,RB> f;
    private final RB scalar;

    private ScaledMorphism(ModuleMorphism<A,B,RA,RB> f, RB scalar) {
        super(f.getDomain(), f.getCodomain());
        this.f = f;
        this.scalar = scalar;
    }

    /**
     * Create a morphism from <code>f</code> and scalar <code>value</code>.
     * The resulting morphism <i>h</i> is such that <i>h(x) = value*f(x)</i>.
     * This is a virtual constructor so that simplifications can be made.
     * 
     * @return null if <code>f</code> cannot be scaled by <code>value</code> 
     */
    public static <X extends ModuleElement<X, RX>, Y extends ModuleElement<Y, RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<X,Y,RX,RY> make(ModuleMorphism<X,Y,RX,RY> f, RY scalar) {
        if (scalar.isOne()) {
            return f;
        }
        if (scalar.isZero()) {
            return getConstantMorphism(f.getDomain(), f.getCodomain().getZero());
        }
        if (f.isConstant()) {
            try {
                return new ConstantMorphism<>(f.getDomain(), f.map(f.getDomain().getZero()).scaled(scalar));
            }
            catch (MappingException e) {
                throw new AssertionError("This should never happen!");
            }

        }
        return new ScaledMorphism<>(f, scalar);
    }
    
    
    public B map(A x) throws MappingException {
        return f.map(x).scaled(scalar);
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ScaledMorphism) {
            ScaledMorphism<?,?,?,?> m = (ScaledMorphism<?,?,?,?>) object;
            int comp = f.compareTo(m.f);
            if (comp == 0) {
                return scalar.compareTo(m.scalar);
            }
            else {
                return comp;
            }
        }
        else {
            return super.compareTo(object);
        }
    }
    
    @Override
    public boolean isModuleHomomorphism() {
        return f.isModuleHomomorphism();
    }

    @Override
    public boolean isLinear() {
        return f.isLinear();
    }

    @Override
    public boolean isConstant() {
        return f.isConstant();
    }
    
    @Override
    public ModuleMorphism<RA, RB, RA, RB> getRingMorphism() {
        return f.getRingMorphism();
    }

    
    /**
     * Returns the morphism <i>f</i> from <i>a*f</i>.
     */
    public ModuleMorphism<A,B,RA,RB> getMorphism() {
        return f;
    }
    

    /**
     * Returns the scalar <i>a</i> from <i>a*f</i>.
     */
    public RB getScalar() {
        return scalar;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof ScaledMorphism) {
            ScaledMorphism<?,?,?,?> morphism = (ScaledMorphism<?,?,?,?>) object;
            return f.equals(morphism.f) && scalar.equals(morphism.scalar);
        }
        else {
            return false;
        }
    }
    
    public String toString() {
        return "ScaledMorphism["+f+","+scalar+"]";
    }

    public String getElementTypeName() {
        return "ScaledMorphism";
    }

}
