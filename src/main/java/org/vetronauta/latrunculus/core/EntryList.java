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

package org.vetronauta.latrunculus.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.vetronauta.latrunculus.core.exception.LatrunculusCastException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author vetronauta
 */
@Getter
@AllArgsConstructor
public class EntryList<K, V> {

    private List<K> keys;
    private List<V> values;

    public static <I1, I2> EntryList<I1, I2> handle(Class<I1> inputKeyClass, Class<I2> inputValueClass, Object[] args)
            throws LatrunculusCastException {
        return handle(inputKeyClass, Function.identity(), inputValueClass, Function.identity(), args);
    }

    public static <I1, O1, I2, O2> EntryList<O1, O2> handle(
            Class<I1> inputKeyClass, Function<I1, O1> keyFunction,
            Class<I2> inputValueClass, Function<I2, O2> valueFunction,
            Object[] args) throws LatrunculusCastException {
        List<O1> keyList = new ArrayList<>();
        List<O2> valueList = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (i % 2 == 0) {
                keyList.add(castAndConvert(inputKeyClass, keyFunction, args[i]));
            } else {
                valueList.add(castAndConvert(inputValueClass, valueFunction, args[i]));
            }
        }
        return new EntryList<>(keyList, valueList);
    }

    private static <I, O> O castAndConvert(Class<I> inputClass, Function<I, O> function, Object obj)
            throws LatrunculusCastException{
        if (!inputClass.isInstance(obj)) {
            throw new LatrunculusCastException(inputClass, obj.getClass());
        }
        return function.apply(inputClass.cast(obj));
    }

}
