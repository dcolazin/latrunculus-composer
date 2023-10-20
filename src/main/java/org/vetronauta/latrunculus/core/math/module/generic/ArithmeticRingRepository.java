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

package org.vetronauta.latrunculus.core.math.module.generic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;
import org.vetronauta.latrunculus.core.math.module.real.RRing;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArithmeticRingRepository {

    //TODO make this a proper object to inject when needed

    private static final Map<Integer, ArithmeticRing<ArithmeticModulus>> modRingMap = new HashMap<>();

    public static Ring<? extends ArithmeticElement<?>> getRing(ArithmeticElement<?> element) {
        ArithmeticNumber<?> number = element.getValue();
        if (number instanceof ArithmeticInteger) {
            return ZRing.ring;
        }
        if (number instanceof Rational) {
            return QRing.ring;
        }
        if (number instanceof ArithmeticDouble) {
            return RRing.ring;
        }
        if (number instanceof Complex) {
            return CRing.ring;
        }
        if (number instanceof ArithmeticModulus) {
            return modRingMap.computeIfAbsent(((ArithmeticModulus) number).getModulus(), ZnRing::make);
        }
        throw new UnsupportedOperationException(String.format("cannot retrieve ring for %s", number.getClass()));
    }

}