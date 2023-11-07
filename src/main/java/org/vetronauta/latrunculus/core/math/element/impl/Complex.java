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
import org.vetronauta.latrunculus.core.math.element.generic.Arithmetic;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;

/**
 * @author vetronauta
 */
@Getter
public class Complex extends RingElement<Complex> implements Arithmetic {

    private double real;
    private double imag;

    /**
     * Creates the complex number 0+i0.
     */
    public Complex() {
        real = 0.0;
        imag = 0.0;
    }

    /**
     * Creates the complex number real+i*imag.
     */
    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    /**
     * Creates the complex number real+i0.
     */
    public Complex(double real) {
        this.real = real;
        this.imag = 0.0;
    }

    @Override
    public boolean isZero() {
        return real == 0.0 && imag == 0.0;
    }

    @Override
    public Complex sum(Complex element) throws DomainException {
        return new Complex(real + element.real, imag + element.imag);
    }

    @Override
    public void add(Complex element) throws DomainException {
        real += element.real;
        imag += element.imag;
    }

    @Override
    public Complex difference(Complex element) throws DomainException {
        return new Complex(real - element.real, imag - element.imag);
    }

    @Override
    public void subtract(Complex element) throws DomainException {
        real -= element.real;
        imag -= element.imag;
    }

    @Override
    public Complex negated() {
        return new Complex(-real, -imag);
    }

    @Override
    public void negate() {
        real = -real;
        imag = -imag;
    }

    @Override
    public boolean isOne() {
        return real == 1.0 && imag == 0.0;
    }

    @Override
    public Complex product(Complex element) throws DomainException {
        return new Complex(real * element.real - imag * element.imag, real * element.imag + imag * element.real);

    }

    @Override
    public void multiply(Complex element) throws DomainException {
        real = real * element.real - imag * element.imag;
        imag = real * element.imag + imag * element.real;
    }

    @Override
    public boolean isInvertible() {
        return !isZero();
    }

    @Override
    public Complex inverse() {
        double norm = real * real + imag * imag;
        return new Complex(real/norm, -imag/norm);
    }

    @Override
    public void invert() {
        double norm = norm();
        real /= norm;
        imag /= -norm;
    }

    @Override
    public Complex quotient(Complex element) throws DomainException, DivisionException {
        double otherNorm = element.norm();
        double newr = (real * element.real + imag * element.imag)/otherNorm;
        double newi = (imag * element.real - real * element.imag)/otherNorm;
        return new Complex(newr, newi);
    }

    @Override
    public void divide(Complex element) throws DomainException, DivisionException {
        double otherNorm = element.norm();
        real = (real * element.real + imag * element.imag)/otherNorm;
        imag = (imag * element.real - real * element.imag)/otherNorm;
    }

    @Override
    public boolean divides(RingElement<?> element) {
        return element instanceof Complex && !this.isZero();
    }

    @Override
    public Ring<Complex> getRing() {
        return CRing.ring;
    }

    @Override
    public Complex deepCopy() {
        return new Complex(real, imag);
    }

    public double norm() {
        return real * real + imag * imag;
    }

    @Override
    public int intValue() {
        return (int) Math.round(real);
    }

    @Override
    public double doubleValue() {
        return real;
    }

    public Complex conjugated() {
        return new Complex(real, -imag);
    }

    @Override
    protected int sameClassCompare(Complex other) {
        if (real != other.real) {
            return Double.compare(real, other.real);
        }
        return Double.compare(imag, other.imag);
    }

}
