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
 * Wrapper double class to be used in a <code>RString</code>.
 * @author vetronauta
 */
@AllArgsConstructor
public class ArithmeticDouble extends ArithmeticNumber<ArithmeticDouble> {

    private double value;

    @Override
    public int compareTo(ArithmeticDouble arithmeticDouble) {
        return Double.compare(value, arithmeticDouble.doubleValue());
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public ArithmeticDouble deepCopy() {
        return new ArithmeticDouble(value);
    }

    @Override
    public boolean isZero() {
        return value == 0.0;
    }

    @Override
    public ArithmeticDouble sum(ArithmeticDouble other) {
        return new ArithmeticDouble(value + other.value);
    }

    @Override
    public ArithmeticDouble difference(ArithmeticDouble other) {
        return new ArithmeticDouble(value - other.value);
    }

    @Override
    public ArithmeticDouble product(ArithmeticDouble other) {
        return new ArithmeticDouble(value * other.value);
    }

    @Override
    public ArithmeticDouble neg() {
        return new ArithmeticDouble(-value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ArithmeticDouble)) {
            return false;
        }
        return ((ArithmeticDouble) other).value == value;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    public static ArithmeticDouble[] toArray(double[] array) {
        return Arrays.stream(array)
            .mapToObj(ArithmeticDouble::new)
            .toArray(ArithmeticDouble[]::new);
    }

    public static List<ArithmeticDouble> toList(List<Double> list) {
        return list.stream().map(ArithmeticDouble::new).collect(Collectors.toList());
    }

}
