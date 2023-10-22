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
import org.vetronauta.latrunculus.core.math.exception.InverseException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper double class to be used in a <code>RString</code>.
 * @author vetronauta
 */
@AllArgsConstructor
public final class Real implements ArithmeticNumber<Real> {

    private final double value;

    @Override
    public int compareTo(Real arithmeticDouble) {
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
    public Real deepCopy() {
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
        return (y instanceof Real) && !this.isZero();
    }

    @Override
    public Real sum(Real other) {
        return new Real(value + other.value);
    }

    @Override
    public Real difference(Real other) {
        return new Real(value - other.value);
    }

    @Override
    public Real product(Real other) {
        return new Real(value * other.value);
    }

    @Override
    public Real neg() {
        return new Real(-value);
    }

    @Override
    public Real inverse() {
        if (value == 0) {
            throw  new InverseException(this);
        }
        return new Real(1/value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Real)) {
            return false;
        }
        return ((Real) other).value == value;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    public static Real[] toArray(double[] array) {
        return Arrays.stream(array)
            .mapToObj(Real::new)
            .toArray(Real[]::new);
    }

    public static List<Real> toList(List<Double> list) {
        return list.stream().map(Real::new).collect(Collectors.toList());
    }

}
