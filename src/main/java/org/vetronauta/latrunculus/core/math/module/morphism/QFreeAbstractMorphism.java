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

import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The abstract base class for morphisms in a free <i>Q</i>-module.
 * 
 * @author Gérard Milmeister
 */
public abstract class QFreeAbstractMorphism extends ModuleMorphism {

    protected QFreeAbstractMorphism(int domDim, int codomDim) {
        super(ArithmeticMultiModule.make(QRing.ring, domDim), ArithmeticMultiModule.make(QRing.ring, codomDim));
    }

    
    public ModuleElement map(ModuleElement x)
            throws MappingException {
        if (getDomain().hasElement(x)) {
            Rational[] rv = new Rational[x.getLength()];
                for (int i = 0; i < x.getLength(); i++) {
                    rv[i] = ((ArithmeticElement<Rational>) x.getComponent(i)).getValue();
                }
            return ArithmeticMultiElement.make(QRing.ring, Arrays.stream(mapValue(rv)).collect(Collectors.toList()));
        }
        throw new MappingException("QFreeAbstractMorphism.map: ", x, this);
    }

    
    /**
     * The low-level map method.
     * This must be implemented in subclasses.
     */
    public abstract Rational[] mapValue(Rational[] rv);

    
    public ModuleMorphism getRingMorphism() {
        return getIdentityMorphism(QRing.ring);
    }
}
