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
import org.vetronauta.latrunculus.core.exception.InverseException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper double class to be used in a <code>RString</code>.
 * @author vetronauta
 */
@AllArgsConstructor
public final class RealWrapper implements ArithmeticNumber<RealWrapper> {

    private final double value;

    @Override
    public int compareTo(RealWrapper arithmeticDouble) {
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
    public RealWrapper deepCopy() {
        return this;
    }

    @Override
    public boolean isZero() {
        return value == 0.0;
    }

    @Override
    public boolean isOne() {
        return value == 1.0;
    }

    @Override
    public boolean isInvertible() {
        return value != 0.0;
    }

    @Override
    public boolean isFieldElement() {
        return true;
    }

    @Override
    public boolean divides(ArithmeticNumber<?> y) {
        return (y instanceof RealWrapper) && !this.isZero();
    }

    @Override
    public RealWrapper sum(RealWrapper other) {
        return new RealWrapper(value + other.value);
    }

    @Override
    public RealWrapper difference(RealWrapper other) {
        return new RealWrapper(value - other.value);
    }

    @Override
    public RealWrapper product(RealWrapper other) {
        return new RealWrapper(value * other.value);
    }

    @Override
    public RealWrapper neg() {
        return new RealWrapper(-value);
    }

    @Override
    public RealWrapper inverse() {
        if (value == 0) {
            throw  new InverseException(this);
        }
        return new RealWrapper(1/value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof RealWrapper)) {
            return false;
        }
        return ((RealWrapper) other).value == value;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    public static RealWrapper[] toArray(double[] array) {
        return Arrays.stream(array)
            .mapToObj(RealWrapper::new)
            .toArray(RealWrapper[]::new);
    }

    public static List<RealWrapper> toList(List<Double> list) {
        return list.stream().map(RealWrapper::new).collect(Collectors.toList());
    }

}
