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

package org.vetronauta.latrunculus.core.math.arith.number;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper int class to be used in a <code>ZString</code>.
 * @author vetronauta
 */
@AllArgsConstructor
public final class IntegerWrapper {

    private final int value;

    public int compareTo(IntegerWrapper integerWrapper) {
        return value - integerWrapper.intValue();
    }

    public int intValue() {
        return value;
    }

    public long longValue() {
        return intValue();
    }

    public float floatValue() {
        return intValue();
    }

    public double doubleValue() {
        return intValue();
    }

    public IntegerWrapper deepCopy() {
        return this;
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isOne() {
        return value == 1;
    }

    public boolean isInvertible() {
        return (value == 1 || value == -1);
    }

    public boolean isFieldElement() {
        return false;
    }

    public IntegerWrapper sum(IntegerWrapper other) {
        return new IntegerWrapper(value + other.value);
    }

    public IntegerWrapper difference(IntegerWrapper other) {
        return new IntegerWrapper(value - other.value);
    }

    public IntegerWrapper product(IntegerWrapper other) {
        return new IntegerWrapper(value * other.value);
    }

    public IntegerWrapper neg() {
        return new IntegerWrapper(-value);
    }

    public String toString() {
        return String.valueOf(value);
    }

    public boolean equals(Object other) {
        if (!(other instanceof IntegerWrapper)) {
            return false;
        }
        return ((IntegerWrapper) other).value == value;
    }

    public int hashCode() {
        return value;
    }

    public static IntegerWrapper[] toArray(int[] array) {
        return Arrays.stream(array)
                .mapToObj(IntegerWrapper::new)
                .toArray(IntegerWrapper[]::new);
    }

    public static List<IntegerWrapper> toList(List<Integer> list) {
        return list.stream().map(IntegerWrapper::new).collect(Collectors.toList());
    }

}
