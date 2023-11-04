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
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringRingRepository {

    //TODO make this a proper object to inject when needed

    private static final Map<Integer, StringRing<Modulus>> modRingMap = new HashMap<>();
    private static final Map<Ring, StringRing> stringRingMap = new HashMap<>();

    public static StringRing<Modulus> getModulusRing(int modulus) {
        return modRingMap.computeIfAbsent(modulus, x -> new StringRing<>(RingRepository.getModulusRing(modulus)));
    }

    public static <R extends RingElement<R>> StringRing<R> getRing(Ring<R> factorRing) {
        if (factorRing instanceof ZnRing) {
            return (StringRing<R>) getModulusRing((((ZnRing) factorRing).getModulus()));
        }
        return stringRingMap.computeIfAbsent(factorRing, StringRing::new);
    }

}
