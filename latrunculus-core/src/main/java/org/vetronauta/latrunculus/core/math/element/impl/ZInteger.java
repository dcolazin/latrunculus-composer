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

package org.vetronauta.latrunculus.core.math.element.impl;

import lombok.AllArgsConstructor;
import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.exception.InverseException;
import org.vetronauta.latrunculus.core.math.element.generic.Arithmetic;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;

/**
 * @author vetronauta
 */
@AllArgsConstructor
public class ZInteger extends RingElement<ZInteger> implements Arithmetic {

    private int value;

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public ZInteger sum(ZInteger element) throws DomainException {
        return new ZInteger(value + element.value);
    }

    @Override
    public void add(ZInteger element) throws DomainException {
        value += element.value;
    }

    @Override
    public ZInteger difference(ZInteger element) throws DomainException {
        return new ZInteger(value - element.value);
    }

    @Override
    public void subtract(ZInteger element) throws DomainException {
        value -= element.value;
    }

    @Override
    public ZInteger negated() {
        return new ZInteger(-value);
    }

    @Override
    public void negate() {
        value = -value;
    }

    @Override
    public boolean isOne() {
        return value == 1;
    }

    @Override
    public ZInteger product(ZInteger element) throws DomainException {
        return new ZInteger(value * element.value);
    }

    @Override
    public void multiply(ZInteger element) throws DomainException {
        value *= element.value;
    }

    @Override
    public boolean isInvertible() {
        return (value == 1 || value == -1);
    }

    @Override
    public ZInteger inverse() {
        if (isInvertible()) {
            return this;
        }
        throw new InverseException(this);
    }

    @Override
    public void invert() {
        if (!isInvertible()) {
            throw new InverseException(this);
        }
    }

    @Override
    public ZInteger quotient(ZInteger element) throws DomainException, DivisionException {
        if (element.value != 0 && value % element.value == 0) {
            return new ZInteger(value/ element.value);
        }
        throw new DivisionException(this, element);
    }

    @Override
    public void divide(ZInteger element) throws DomainException, DivisionException {
        if (element.value != 0 && value % element.value == 0) {
            value /= element.value;
        }
        throw new DivisionException(this, element);
    }

    @Override
    public boolean divides(RingElement<?> element) {
        return (element instanceof ZInteger) && !this.isZero() && (((ZInteger) element).value % value == 0);
    }

    @Override
    public Ring<ZInteger> getRing() {
        return ZRing.ring;
    }

    @Override
    public ZInteger deepCopy() {
        return new ZInteger(value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ZInteger && value == ((ZInteger) other).value;
    }

    @Override
    public int hashCode() {
        return intValue();
    }

    @Override
    protected int sameClassCompare(ZInteger other) {
        return value - other.value;
    }

}
