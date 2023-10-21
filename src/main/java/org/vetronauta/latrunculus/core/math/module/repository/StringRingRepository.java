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

package org.vetronauta.latrunculus.core.math.module.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringRing;
import org.vetronauta.latrunculus.core.math.module.rational.QStringRing;
import org.vetronauta.latrunculus.core.math.module.real.RStringRing;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringRingRepository {

    //TODO make this a proper object to inject when needed

    private static final Map<Integer, ZnStringRing> modRingMap = new HashMap<>();

    public static ZnStringRing getModulusRing(int modulus) {
        return modRingMap.computeIfAbsent(modulus, ZnStringRing::make);
    }

    public static <S extends ArithmeticStringElement<S,N>, N extends ArithmeticNumber<N>> StringRing<S> getRing(ArithmeticStringElement<S,N> element) {
        ArithmeticNumber<N> number = element.getValue().getObjectOne();
        if (number instanceof ArithmeticInteger) {
            return (StringRing<S>) ZStringRing.ring;
        }
        if (number instanceof Rational) {
            return (StringRing<S>) QStringRing.ring;
        }
        if (number instanceof ArithmeticDouble) {
            return (StringRing<S>) RStringRing.ring;
        }
        if (number instanceof ArithmeticModulus) {
            return (StringRing<S>) getModulusRing(((ArithmeticModulus) number).getModulus());
        }
        throw new UnsupportedOperationException(String.format("cannot retrieve ring for %s", number.getClass()));
    }

}
