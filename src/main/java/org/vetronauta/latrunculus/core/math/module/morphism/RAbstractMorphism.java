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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.real.RRing;

/**
 * The abstract base class for morphisms in <i>R</i>.
 * 
 * @author Gérard Milmeister
 */
public abstract class RAbstractMorphism extends ModuleMorphism {

    protected RAbstractMorphism() {
        super(RRing.ring, RRing.ring);
    }

    
    public final ModuleElement map(ModuleElement x)
            throws MappingException {
        if (getDomain().hasElement(x)) {
            double v = ((ArithmeticElement<ArithmeticDouble>) x.getComponent(0)).getValue().doubleValue();
            return new ArithmeticElement<>(new ArithmeticDouble(mapValue(v)));
        }
        else {
            throw new MappingException("RAbstractMorphism.map: ", x, this);
        }
    }

    
    /**
     * The low-level map method.
     * This must be implemented in subclasses.
     */
    public abstract double mapValue(double x);
    
    
    public ModuleMorphism getRingMorphism() {
        return getIdentityMorphism(RRing.ring);
    }
}
