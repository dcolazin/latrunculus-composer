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

import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * Morphism that represents the sum of two arbitrary morphisms.
 * 
 * @author Gérard Milmeister
 */
public final class SumMorphism<A extends ModuleElement<A, RA>, B extends ModuleElement<B, RB>, RA extends RingElement<RA>, RB extends RingElement<RB>>
        extends ModuleMorphism<A,B,RA,RB> {

    private final ModuleMorphism<A,B,RA,RB> f;
    private final ModuleMorphism<A,B,RA,RB> g;

    /**
     * Creates a morphism from <code>f</code> and <code>g</code>.
     * The resulting morphism <i>h</i> is such that <i>h(x) = f(x)+g(x)</i>.
     *
     * @throws CompositionException if sum is not valid
     */
    public static <X extends ModuleElement<X,RX>, Y extends ModuleElement<Y,RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<X,Y,RX,RY> make(ModuleMorphism<X,Y,RX,RY> f, ModuleMorphism<X,Y,RX,RY> g) throws CompositionException {
        if (!f.getDomain().equals(g.getDomain()) || !f.getCodomain().equals(g.getCodomain())) {
            throw new CompositionException("SumMorphism.make: Cannot add "+g+" to "+f);
        }
        else if (f.isConstant() && g.isConstant()) {
            try {
                X zero = f.getDomain().getZero();
                Y fe = f.map(zero);
                Y ge = g.map(zero);
                return new ConstantMorphism<>(f.getDomain(), fe.sum(ge));
            }
            catch (DomainException | MappingException e) {
                throw new AssertionError("This should never happen!");
            }
        } else {
            return new SumMorphism<>(f, g);
        }
    }

    private SumMorphism(ModuleMorphism<A,B,RA,RB> f, ModuleMorphism<A,B,RA,RB> g) {
        super(f.getDomain(), f.getCodomain());
        this.f = f;
        this.g = g;
    }
    
    public B map(A x) throws MappingException {
        return f.map(x).sum(g.map(x));
    }

    @Override
    public boolean isModuleHomomorphism() {
        return f.isModuleHomomorphism() && g.isModuleHomomorphism();
    }

    @Override
    public boolean isRingHomomorphism() {
        return f.isRingHomomorphism() && g.isRingHomomorphism();
    }

    @Override
    public boolean isLinear() {
        return f.isLinear() && g.isLinear();
    }

    @Override
    public boolean isConstant() {
        return f.isConstant() && g.isConstant();
    }
    
    
    /**
     * Returns the first morphism <i>f</i> of the sum <i>f+g</i>.
     */
    public ModuleMorphism<A,B,RA,RB> getFirstMorphism() {
        return f;
    }

    /**
     * Returns the second morphism <i>g</i> of the sum <i>f+g</i>.
     */
    public ModuleMorphism<A,B,RA,RB> getSecondMorphism() {
        return g;
    }

    public ModuleMorphism<RA,RB,RA,RB> getRingMorphism() {
        return f.getRingMorphism();
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof SumMorphism) {
            SumMorphism<?,?,?,?> morphism = (SumMorphism<?,?,?,?>)object;
            int comp = f.compareTo(morphism.f);
            if (comp == 0) {
                return g.compareTo(morphism.g);
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
        if (object instanceof SumMorphism) {
            SumMorphism<?,?,?,?> m = (SumMorphism<?,?,?,?>)object;
            return f.equals(m.f) && g.equals(m.g);
        }
        else {
            return false;
        }
    }
    
    
    public String toString() {
        return "SumMorphism["+f+","+g+"]";
    }

    public String getElementTypeName() {
        return "SumMorphism";
    }

}
