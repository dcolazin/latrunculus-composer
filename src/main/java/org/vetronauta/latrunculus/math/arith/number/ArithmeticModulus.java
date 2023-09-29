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

package org.vetronauta.latrunculus.math.arith.number;

import lombok.Getter;
import org.vetronauta.latrunculus.math.arith.NumberTheory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper modulus class to be used in a <code>ZnString</code>.
 * @author vetronauta
 */
@Getter
public class ArithmeticModulus extends ArithmeticNumber<ArithmeticModulus> {

    private int value;
    private int modulus;

    public ArithmeticModulus(int value, int modulus) {
        this.value = NumberTheory.mod(value, modulus);
        this.modulus = modulus;
    }

    @Override
    public int compareTo(ArithmeticModulus arithmeticModulus) {
        return this.intValue() - arithmeticModulus.intValue();
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return intValue();
    }

    @Override
    public float floatValue() {
        return intValue();
    }

    @Override
    public double doubleValue() {
        return intValue();
    }

    @Override
    public ArithmeticModulus deepCopy() {
        return new ArithmeticModulus(value, modulus);
    }

    @Override
    public boolean isZero() {
        return intValue() == 0;
    }

    public static ArithmeticModulus[] toArray(int[] array, int m) {
        return Arrays.stream(array)
                .mapToObj(i -> new ArithmeticModulus(i, m))
                .toArray(ArithmeticModulus[]::new);
    }

    public static List<ArithmeticModulus> toList(List<Integer> list, int m) {
        return list.stream().map(i -> new ArithmeticModulus(i, m)).collect(Collectors.toList());
    }

}
