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

import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.generic.Endomorphism;

/**
 * Morphism that represents an iterated arbitrary morphism.
 * 
 * @author Gérard Milmeister
 */
public final class PowerMorphism<A extends ModuleElement<A, RA>, RA extends RingElement<RA>> extends Endomorphism<A,RA> {

    private final Endomorphism<A,RA> f;
    private final int exponent;

    private PowerMorphism(Endomorphism<A,RA> f, int exp) {
        super(f.getDomain());
        this.f = f;
        this.exponent = exp;
    }

    /**
     * Creates a morphism from <code>f</code> raised to <code>power</code>.
     * The resulting morphism h is such that h(x) = f(f(...(f(x))...)),
     * where there are <code>exp</code> repetitions of <code>f</code>.
     * This is a virtual constructor, so that simplifications can be made.
     */
    public static <X extends ModuleElement<X,RX>, RX extends RingElement<RX>> Endomorphism<X,RX> make(Endomorphism<X,RX> f, int exp) throws CompositionException {
        if (exp < 0) {
            throw new CompositionException("PowerMorphism.make: Cannot raise "+f+" to a negative power "+exp);
        }
        if (exp == 0) {
            return getIdentityMorphism(f.getDomain());
        }
        if (exp == 1 || f.isIdentity() || f.isConstant()) {
            return f;
        }
        return new PowerMorphism<>(f, exp);
    }

    @Override
    public A map(A x) throws MappingException {
        A res = x;
        for (int i = 1; i < exponent; i++) {
            res = f.map(res);
        }
        return res;
    }

    @Override
    public boolean isModuleHomomorphism() {
        return f.isModuleHomomorphism();
    }

    @Override
    public boolean isRingHomomorphism() {
        return f.isRingHomomorphism();        
    }
    
    @Override
    public boolean isLinear() {
        return f.isLinear();
    }
    
    @Override
    public boolean isIdentity() {
        return exponent == 0 || f.isIdentity();
    }
    
    @Override
    public boolean isConstant() {
        return f.isConstant();
    }
    
    @Override
    public IdentityMorphism<RA, RA> getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }

    /**
     * Returns the base morphism <i>f</i> of the power <i>f^n</i>.
     */
    public Endomorphism<A, RA> getBaseMorphism() {
        return f;
    }
    
    /**
     * Returns the exponent <i>n</i> of the power <i>f^n</i>. 
     */
    public int getExponent() {
        return exponent;
    }


    @Override
    public Endomorphism<A, RA> power(int n) throws CompositionException {
        return make(f, exponent * n);
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof PowerMorphism) {
            PowerMorphism morphism = (PowerMorphism)object;
            int res = f.compareTo(morphism.f);
            if (res == 0) {
                return exponent-morphism.exponent;
            }
            else {
                return res;
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PowerMorphism) {
            PowerMorphism morphism = (PowerMorphism)object;
            return (f.equals(morphism.f) && exponent == morphism.exponent);
        }
        else {
            return false;
        }
    }
    
    
    public String toString() {
        return "PowerMorphism["+f+","+exponent+"]";
    }

    public String getElementTypeName() {
        return "PowerMorphism";
    }

}
