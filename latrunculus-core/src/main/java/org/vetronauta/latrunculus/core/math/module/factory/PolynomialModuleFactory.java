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

package org.vetronauta.latrunculus.core.math.module.factory;

import org.vetronauta.latrunculus.core.math.module.generic.FreeModule;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.element.generic.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.generic.ModularPolynomialRing;
import org.vetronauta.latrunculus.core.math.element.generic.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.generic.PolynomialRing;

/**
 * @author vetronauta
 */
public class PolynomialModuleFactory {

    public static <X extends RingElement<X>> FreeModule<?, ModularPolynomialElement<X>> makeModular(PolynomialElement<X> modulus, int dimension) {
        Ring<ModularPolynomialElement<X>> ring = ModularPolynomialRing.make(modulus);
        if (ring == null) {
            return null;
        }
        return new VectorModule<>(ring, dimension);
    }

    public static <X extends RingElement<X>> FreeModule<?, PolynomialElement<X>> make(Ring<X> coefficientRing, String indeterminate, int dimension) {
        Ring<PolynomialElement<X>> polynomialRing = PolynomialRing.make(coefficientRing, indeterminate);
        if (dimension == 1) {
            return polynomialRing;
        }
        return new VectorModule<>(polynomialRing, Math.max(dimension, 0));
    }

}
