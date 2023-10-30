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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Modulus;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringRingRepository {

    //TODO make this a proper object to inject when needed

    private static final Map<Integer, ArithmeticStringRing<Modulus>> modRingMap = new HashMap<>();
    private static final Map<ArithmeticRing, ArithmeticStringRing> stringRingMap = new HashMap<>();

    public static ArithmeticStringRing<Modulus> getModulusRing(int modulus) {
        return modRingMap.computeIfAbsent(modulus, x -> new ArithmeticStringRing<>(ArithmeticRingRepository.getModulusRing(modulus)));
    }

    public static <N extends ArithmeticNumber<N>> ArithmeticStringRing<N> getRing(ArithmeticRing<N> factorRing) {
        if (factorRing instanceof ZnRing) {
            return (ArithmeticStringRing<N>) getModulusRing((((ZnRing) factorRing).getModulus()));
        }
        return stringRingMap.computeIfAbsent(factorRing, ArithmeticStringRing::new);
    }

}
