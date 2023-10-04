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
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.InverseException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper int class to be used in a <code>ZString</code>.
 * @author vetronauta
 */
@AllArgsConstructor
public final class ArithmeticInteger extends ArithmeticNumber<ArithmeticInteger> {

    private final int value;

    @Override
    public int compareTo(ArithmeticInteger arithmeticInteger) {
        return value - arithmeticInteger.intValue();
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
    public ArithmeticInteger deepCopy() {
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
        return (y instanceof ArithmeticInteger) && !this.isZero() && (value % ((ArithmeticInteger) y).value == 0);
    }

    @Override
    public ArithmeticInteger sum(ArithmeticInteger other) {
        return new ArithmeticInteger(value + other.value);
    }

    @Override
    public ArithmeticInteger difference(ArithmeticInteger other) {
        return new ArithmeticInteger(value - other.value);
    }

    @Override
    public ArithmeticInteger product(ArithmeticInteger other) {
        return new ArithmeticInteger(value * other.value);
    }

    @Override
    public ArithmeticInteger neg() {
        return new ArithmeticInteger(-value);
    }

    @Override
    public ArithmeticInteger inverse() {
        if (isInvertible()) {
            return this;
        }
        throw new InverseException(this);
    }

    @Override
    public ArithmeticInteger quotient(ArithmeticInteger other) throws DivisionException {
        if (other.value != 0 && value % other.value == 0) {
            return new ArithmeticInteger(value/ other.value);
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
        if (!(other instanceof ArithmeticInteger)) {
            return false;
        }
        return ((ArithmeticInteger) other).value == value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    public static ArithmeticInteger[] toArray(int[] array) {
        return Arrays.stream(array)
                .mapToObj(ArithmeticInteger::new)
                .toArray(ArithmeticInteger[]::new);
    }

    public static List<ArithmeticInteger> toList(List<Integer> list) {
        return list.stream().map(ArithmeticInteger::new).collect(Collectors.toList());
    }

}
