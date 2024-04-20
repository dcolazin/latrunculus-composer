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

import lombok.Getter;
import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.exception.InverseException;
import org.vetronauta.latrunculus.core.exception.ModulusException;
import org.vetronauta.latrunculus.core.exception.ZeroDivisorException;
import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.math.element.generic.Arithmetic;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.factory.RingRepository;

/**
 * @author vetronauta
 */
@Getter
public class Modulus extends RingElement<Modulus> implements Arithmetic {

    private int value;
    private final int modulus;

    public Modulus(int value, int modulus) {
        this.value = NumberTheory.mod(value, modulus);
        this.modulus = modulus;
    }

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
    public Modulus sum(Modulus element) throws DomainException {
        assertModulusIsSame(element);
        return new Modulus(value + element.value, modulus);
    }

    @Override
    public void add(Modulus element) throws DomainException {
        assertModulusIsSame(element);
        value = (value + element.value) % modulus;
    }

    @Override
    public Modulus difference(Modulus element) throws DomainException {
        assertModulusIsSame(element);
        return new Modulus(value - element.value, modulus);
    }

    @Override
    public void subtract(Modulus element) throws DomainException {
        assertModulusIsSame(element);
        value = (value - element.value) % modulus;
    }

    @Override
    public Modulus negated() {
        return new Modulus(-value, modulus);

    }

    @Override
    public void negate() {
        value = (-value) % modulus;
    }

    @Override
    public boolean isOne() {
        return value == 1;
    }

    @Override
    public Modulus product(Modulus element) throws DomainException {
        assertModulusIsSame(element);
        return new Modulus(value * element.value, modulus);
    }

    @Override
    public void multiply(Modulus element) throws DomainException {
        assertModulusIsSame(element);
        value = (value * element.value) % modulus;
    }

    @Override
    public boolean isInvertible() {
        try {
            NumberTheory.inverseMod(value, modulus); //TODO is there a faster way?
            return true;
        } catch (ZeroDivisorException e) {
            return false;
        }
    }

    @Override
    public Modulus inverse() {
        try {
            return new Modulus(NumberTheory.inverseMod(value, modulus), modulus);
        } catch (ZeroDivisorException e) {
            throw new InverseException(this, e);
        }
    }

    @Override
    public void invert() {
        try {
            value = NumberTheory.inverseMod(value, modulus);
        } catch (ZeroDivisorException e) {
            throw new InverseException(this, e);
        }
    }

    @Override
    public Modulus quotient(Modulus element) throws DomainException, DivisionException {
        return product(element.inverse());
    }

    @Override
    public void divide(Modulus element) throws DomainException, DivisionException {
        multiply(element.inverse());
    }

    @Override
    public boolean divides(RingElement<?> element) {
        return element instanceof Modulus && NumberTheory.gcd(value, modulus) == 1;
    }

    @Override
    public Ring<Modulus> getRing() {
        return RingRepository.getModulusRing(modulus);
    }

    @Override
    public Modulus deepCopy() {
        return new Modulus(value, modulus);
    }

    @Override
    public String toString() {
        return String.format("%d%%%d", value, modulus);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Modulus && value == ((Modulus) object).value && modulus == ((Modulus) object).getModulus();
    }

    @Override
    public int hashCode() {
        return value * modulus;
    }

    private void assertModulusIsSame(Modulus other) {
        if (modulus != other.getModulus()) {
            throw new ModulusException(this, other);
        }
    }

    @Override
    protected int sameClassCompare(Modulus other) {
        return intValue() - other.intValue();
    }
}
