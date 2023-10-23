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

package org.vetronauta.latrunculus.core.math.module;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.arith.number.Real;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.Modulus;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringRing;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FreeUtils {

    //TODO temp method...
    public static boolean isUsualFree(Module<?,?> module) {
        if (!(module instanceof FreeModule && module.checkRingElement(ArithmeticElement.class))) {
            return false;
        }
        ArithmeticNumber<?> number = ((ArithmeticElement<?>) module.getZero()).getValue();
        return number instanceof ArithmeticInteger ||
                number instanceof Modulus ||
                number instanceof Rational ||
                number instanceof Real ||
                number instanceof Complex;
    }

    //TODO temp method...
    public static boolean isUsualStringFree(Module<?,?> module) {
        if (!(module instanceof FreeModule && (module.checkRingElement(ArithmeticStringElement.class)))) {
            return false;
        }
        ArithmeticNumber<?> number = ((ArithmeticStringRing<?>) module.getRing()).getFactorRing().getZero().getValue();
        return number instanceof ArithmeticInteger ||
                number instanceof Modulus ||
                number instanceof Rational ||
                number instanceof Real;
    }
}
