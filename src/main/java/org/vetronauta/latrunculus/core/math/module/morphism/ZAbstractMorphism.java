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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;

/**
 * The abstract base class for morphisms in <i>Z</i>.
 * 
 * @author Gérard Milmeister
 */
public abstract class ZAbstractMorphism extends ModuleMorphism {

    protected ZAbstractMorphism() {
        super(ZRing.ring, ZRing.ring);
    }

    public ModuleElement map(ModuleElement x)
            throws MappingException {
        if (getDomain().hasElement(x)) {
            int v = ((ArithmeticElement<ArithmeticInteger>) x.getComponent(0)).getValue().intValue();
            return new ArithmeticElement<>(new ArithmeticInteger(mapValue(v)));
        }
        else {
            throw new MappingException("ZAbstractMorphism.map: ", x, this);
        }
    }

    
    /**
     * The low-level map method.
     * This must be implemented in subclasses.
     */
    public abstract int mapValue(int x);
    
    
    public ModuleMorphism getRingMorphism() {
        return getIdentityMorphism(ZRing.ring);
    }
}
