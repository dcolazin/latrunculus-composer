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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;

/**
 * The function that takes a complex number (or vector) to its conjugate.
 * 
 * @author Gérard Milmeister
 */
public final class ConjugationMorphism extends ModuleMorphism {

    public ConjugationMorphism(int dimension) {
        super(ArithmeticMultiModule.make(CRing.ring, dimension), ArithmeticMultiModule.make(CRing.ring, dimension));
        this.dimension = dimension;
    }

    
    public ModuleElement map(ModuleElement x)
            throws MappingException {
        if (x instanceof ArithmeticElement) {
            ArithmeticNumber<?> number = ((ArithmeticElement<?>) x).getValue();
            if (number instanceof Complex) {
                return new ArithmeticElement<>(((Complex) number).conjugated());
            }
        } else if (x instanceof ArithmeticMultiElement && (((ArithmeticMultiElement<?>) x).getRing() instanceof CRing)) {
                ArithmeticMultiElement<Complex> element = (ArithmeticMultiElement<Complex>) x;
                Complex[] res = new Complex[element.getValue().size()];
                for (int i = 0; i < element.getValue().size(); i++) {
                    res[i] = element.getValue().get(i).getValue().conjugated();
                }
                return ArithmeticMultiElement.make(CRing.ring, res);

        }
        throw new MappingException("ConjugationMorphism.map: ", x, this);
    }
    
    public boolean isRingHomomorphism() {
        return getDomain().isRing();
    }
    
    
    public ModuleMorphism getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }

    
    public ModuleMorphism compose(ModuleMorphism morphism)
            throws CompositionException {
        if (morphism instanceof ConjugationMorphism) {
            ConjugationMorphism m = (ConjugationMorphism)morphism;
            if (m.dimension == dimension) {
                return getIdentityMorphism(getDomain());
            }
            else {
                throw new CompositionException("ConjugationMorphism.compose: ", this, morphism);
            }
        }
        else {
            return super.compose(morphism);
        }
    }
    
    
    public ModuleElement atZero() {
        return getCodomain().getZero();
    }
    
    
    public ModuleMorphism power(int n)
            throws CompositionException {
        if (n % 2 == 0) {
            return getIdentityMorphism(getDomain());
        }
        else {
            return this;
        }
    }
    
    
    public boolean equals(Object object) {
        if (object instanceof ConjugationMorphism) {
            return getDomain().getDimension() == ((ConjugationMorphism)object).getDomain().getDimension(); 
        }
        else {
            return false;
        }
    }
    
    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ConjugationMorphism) {
            return getDomain().getDimension()-((ConjugationMorphism)object).getDomain().getDimension();
        }
        else {
            return super.compareTo(object);
        }
    }
    
    
    public String toString() {
        return "ConjugationMorphism["+getDomain().getDimension()+"]";
    }

    public String getElementTypeName() {
        return "ConjugationMorphism";
    }
    
    
    private int dimension;
}
