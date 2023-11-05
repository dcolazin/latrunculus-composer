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

package org.vetronauta.latrunculus.core.math.module.polynomial;

import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;

/**
 * @author vetronauta
 */
public class ModularPolynomialModuleFactory {

    public static <X extends RingElement<X>> FreeModule<?,ModularPolynomialElement<X>> make(PolynomialElement<X> modulus, int dimension) {
        Ring<ModularPolynomialElement<X>> ring = ModularPolynomialRing.make(modulus);
        if (ring == null) {
            return null;
        }
        dimension = Math.max(dimension, 0);
        if (dimension == 1) {
            return ring;
        }
        return new VectorModule<>(ring, dimension);
    }

}
