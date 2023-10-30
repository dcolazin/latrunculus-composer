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

import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.morphism.endo.Endomorphism;

/**
 * The function that takes a complex number (or vector) to its conjugate.
 * 
 * @author Gérard Milmeister
 */
public final class ConjugationMorphism<A extends FreeElement<A,ArithmeticElement<Complex>>> extends Endomorphism<A,ArithmeticElement<Complex>> {

    public static ConjugationMorphism<?> make(int dimension) {
        FreeModule<?,ArithmeticElement<Complex>> domain = ArithmeticMultiModule.make(CRing.ring, dimension);
        return new ConjugationMorphism<>(domain);
    }

    private ConjugationMorphism(FreeModule<A,ArithmeticElement<Complex>> domain) {
        super(domain);
    }

    //TODO make in abstract and map the two cases separately
    public A map(A x) throws MappingException {
        if (x instanceof ArithmeticElement) {
            Complex number = ((ArithmeticElement<Complex>) x).getValue();
            return (A) new ArithmeticElement<>(number.conjugated());
        } else if (x instanceof ArithmeticMultiElement) {
                ArithmeticMultiElement<Complex> element = (ArithmeticMultiElement<Complex>) x;
                Complex[] res = new Complex[element.getValue().size()];
                for (int i = 0; i < element.getValue().size(); i++) {
                    res[i] = element.getValue().get(i).getValue().conjugated();
                }
                return (A) ArithmeticMultiElement.make(CRing.ring, res);

        }
        throw new MappingException("ConjugationMorphism.map: ", x, this);
    }

    @Override
    public boolean isRingHomomorphism() {
        return getDomain().isRing();
    }
    
    @Override
    public IdentityMorphism<ArithmeticElement<Complex>, ArithmeticElement<Complex>> getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }

    @Override
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphism<C,A,RC,ArithmeticElement<Complex>>
    compose(ModuleMorphism<C,A,RC,ArithmeticElement<Complex>> morphism) throws CompositionException {
        if (morphism instanceof ConjugationMorphism) {
            if (morphism.getDomain().getDimension() == getDomain().getDimension()) {
                return (ModuleMorphism) getIdentityMorphism(getDomain());
            }
            throw new CompositionException("ConjugationMorphism.compose: ", this, morphism);
        }
        return super.compose(morphism);
    }
    
    @Override
    public A atZero() {
        return getCodomain().getZero();
    }
    
    @Override
    public Endomorphism<A,ArithmeticElement<Complex>> power(int n) throws CompositionException {
        return n % 2 == 0 ? getIdentityMorphism(getDomain()) : this;
    }
    
    public boolean equals(Object object) {
        if (object instanceof ConjugationMorphism) {
            return getDomain().getDimension() == ((ConjugationMorphism)object).getDomain().getDimension(); 
        }
        else {
            return false;
        }
    }
    
    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ConjugationMorphism) {
            return getDomain().getDimension()-object.getDomain().getDimension();
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
    
}
