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
import org.vetronauta.latrunculus.core.math.arith.number.IntegerWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.ModulusWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.RationalWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.RealWrapper;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FreeUtils {

    //TODO complete refactoring of those methods

    //TODO temp method...
    public static boolean isUsualFree(Module<?,?> module) {
        if (!isArithmetic(module)) {
            return false;
        }
        ArithmeticNumber<?> number = retrieveNumber(module);
        return number instanceof IntegerWrapper ||
                number instanceof ModulusWrapper ||
                number instanceof RationalWrapper ||
                number instanceof RealWrapper;
    }

    //TODO temp method...
    public static boolean isUsualStringFree(Module<?,?> module) {
        if (!isArithmetic(module)) {
            return false;
        }
        ArithmeticNumber<?> number = retrieveNumber(module);
        return number instanceof RingString;
    }

    public static boolean isArithmetic(Module<?,?> module) {
        return module instanceof ArithmeticRing || module instanceof VectorModule;
    }

    public static ArithmeticNumber<?> retrieveNumber(Module<?,?> module) {
        if (module instanceof ArithmeticRing) {
            return ((ArithmeticRing<?>) module).getZero().getValue();
        }
        return ((ArithmeticRing<?>) module.getRing()).getZero().getValue();
    }
}
