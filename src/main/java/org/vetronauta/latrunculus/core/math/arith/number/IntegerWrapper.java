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
import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.InverseException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper int class to be used in a <code>ZString</code>.
 * @author vetronauta
 */
@AllArgsConstructor
public final class IntegerWrapper implements ArithmeticNumber<IntegerWrapper> {

    private final int value;

    @Override
    public int compareTo(IntegerWrapper integerWrapper) {
        return value - integerWrapper.intValue();
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
    public IntegerWrapper deepCopy() {
        return this;
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public boolean isOne() {
        return value == 1;
    }

    @Override
    public boolean isInvertible() {
        return (value == 1 || value == -1);
    }

    @Override
    public boolean isFieldElement() {
        return false;
    }

    @Override
    public boolean divides(ArithmeticNumber<?> y) {
        return (y instanceof IntegerWrapper) && !this.isZero() && (value % ((IntegerWrapper) y).value == 0);
    }

    @Override
    public IntegerWrapper sum(IntegerWrapper other) {
        return new IntegerWrapper(value + other.value);
    }

    @Override
    public IntegerWrapper difference(IntegerWrapper other) {
        return new IntegerWrapper(value - other.value);
    }

    @Override
    public IntegerWrapper product(IntegerWrapper other) {
        return new IntegerWrapper(value * other.value);
    }

    @Override
    public IntegerWrapper neg() {
        return new IntegerWrapper(-value);
    }

    @Override
    public IntegerWrapper inverse() {
        if (isInvertible()) {
            return this;
        }
        throw new InverseException(this);
    }

    @Override
    public IntegerWrapper quotient(IntegerWrapper other) throws DivisionException {
        if (other.value != 0 && value % other.value == 0) {
            return new IntegerWrapper(value/ other.value);
        } else {
            throw new DivisionException(this, other);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IntegerWrapper)) {
            return false;
        }
        return ((IntegerWrapper) other).value == value;
    }

    @Override
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
