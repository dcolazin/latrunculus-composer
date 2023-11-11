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
import lombok.Getter;
import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.exception.InverseException;
import org.vetronauta.latrunculus.core.math.element.generic.Arithmetic;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;

/**
 * @author vetronauta
 */
@Getter
@AllArgsConstructor
public class Real extends RingElement<Real> implements Arithmetic {

    private double value;

    @Override
    public boolean isZero() {
        return value == 0.0;
    }

    @Override
    public Real sum(Real element) {
        return new Real(value + element.value);
    }

    @Override
    public void add(Real element) {
        value += element.value;
    }

    @Override
    public Real difference(Real element) {
        return new Real(value - element.value);
    }

    @Override
    public void subtract(Real element) {
        value -= element.value;
    }

    @Override
    public Real negated() {
        return new Real(-value);
    }

    @Override
    public void negate() {
        value = -value;
    }

    @Override
    public boolean isOne() {
        return value == 1.0;
    }

    @Override
    public Real product(Real element) {
        return new Real(value * element.value);
    }

    @Override
    public void multiply(Real element) {
        value *= element.value;
    }

    @Override
    public boolean isInvertible() {
        return value != 0.0;
    }

    @Override
    public Real inverse() {
        if (value == 0) {
            throw new InverseException(this);
        }
        return new Real(1 / value);
    }

    @Override
    public void invert() {
        if (value == 0) {
            throw new InverseException(this);
        }
        value = 1 / value;
    }

    @Override
    public Real quotient(Real element) throws DomainException, DivisionException {
        if (element.value == 0) {
            throw new DivisionException(this, element);
        }
        return new Real(value / element.value);
    }

    @Override
    public void divide(Real element) throws DomainException, DivisionException {
        if (element.value == 0) {
            throw new DivisionException(this, element);
        }
        value /= element.value;
    }

    @Override
    public boolean divides(RingElement<?> element) {
        return element instanceof Real && this.isInvertible();
    }

    @Override
    public Ring<Real> getRing() {
        return RRing.ring;
    }

    @Override
    public Real deepCopy() {
        return new Real(value);
    }

    @Override
    public int intValue() {
        return (int) Math.round(value);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Real && value == ((Real) other).value;
    }

    @Override
    public int hashCode() {
        return intValue();
    }

    @Override
    protected int sameClassCompare(Real other) {
        return Double.compare(value, other.value);
    }

}
