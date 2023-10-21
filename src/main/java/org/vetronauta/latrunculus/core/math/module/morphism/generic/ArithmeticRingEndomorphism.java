/*
 * latrunculus-composer
 * Copyright (C) 2023 vetronauta
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.vetronauta.latrunculus.core.math.module.morphism.generic;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

/**
 * @author vetronauta
 */
public abstract class ArithmeticRingEndomorphism<N extends ArithmeticNumber<N>> extends RingEndomorphism<ArithmeticElement<N>> {

    protected ArithmeticRingEndomorphism(Ring<ArithmeticElement<N>> domain) {
        super(domain);
    }

    public final ArithmeticElement<N> map(ArithmeticElement<N> x) {
        return new ArithmeticElement<>(mapValue(x.getValue()));
    }

    /**
     * Maps the number value <code>x</code> under this morphism.
     * This method must be implemented by a specific morphism.
     */
    public abstract N mapValue(N x);


    public ModuleMorphism getRingMorphism() {
        return getIdentityMorphism(getDomain());
    }
}