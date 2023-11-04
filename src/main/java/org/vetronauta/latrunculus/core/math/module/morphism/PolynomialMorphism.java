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
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.endo.RingEndomorphism;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;

/**
 * Polynomial mappings.
 * 
 * @author Gérard Milmeister
 */
public class PolynomialMorphism<R extends RingElement<R>> extends RingEndomorphism<R> {

    private final PolynomialElement<R> polynomial;

    /**
     * Creates a mapping which evaluates the given <code>polynomial</code>.
     */
    public PolynomialMorphism(PolynomialElement<R> polynomial) {
        super(polynomial.getRing().getCoefficientRing());
        this.polynomial = polynomial;
    }

    
    public R map(R x) throws MappingException {
        return polynomial.evaluate(x);
    }

    @Override
    public boolean isModuleHomomorphism() {
        return polynomial.getDegree() == 1;
    }

    @Override
    public boolean isRingHomomorphism() {
        return isIdentity() || polynomial.isZero();
    }

    @Override
    public boolean isLinear() {
        return polynomial.getDegree() == 1 && polynomial.getCoefficient(0).isZero();
    }

    @Override
    public boolean isIdentity() {
        return polynomial.getDegree() == 1
               && polynomial.getCoefficient(0).isZero()
               && polynomial.getCoefficient(1).isOne();
    }

    @Override
    public boolean isConstant() {
        return polynomial.getDegree() == 0;
    }

    /**
     * Returns the mapping's polynomial.
     */
    public PolynomialElement<R> getPolynomial() {
        return polynomial;
    }

    public IdentityMorphism<R,R> getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof PolynomialMorphism) {
            PolynomialMorphism<?> morphism = (PolynomialMorphism<?>)object;
            return polynomial.compareTo(morphism.polynomial);
        }
        return super.compareTo(object);
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof PolynomialMorphism) {
            PolynomialMorphism<?> morphism = (PolynomialMorphism<?>)object;
            return polynomial.equals(morphism.polynomial);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "PolynomialMorphism["+polynomial+"]";
    }

    public String getElementTypeName() {
        return "PolynomialMorphism";
    }

}
