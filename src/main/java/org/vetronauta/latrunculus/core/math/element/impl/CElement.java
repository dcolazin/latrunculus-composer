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

import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;

/**
 * @author vetronauta
 */
public class CElement extends RingElement<CElement> {

    //TODO rename after deleting arith.number.Complex

    private double real;
    private double imag;

    /**
     * Creates the complex number 0+i0.
     */
    public CElement() {
        real = 0.0;
        imag = 0.0;
    }

    /**
     * Creates the complex number real+i*imag.
     */
    public CElement(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    /**
     * Creates the complex number real+i0.
     */
    public CElement(double real) {
        this.real = real;
        this.imag = 0.0;
    }

    @Override
    public boolean isZero() {
        return real == 0.0 && imag == 0.0;
    }

    @Override
    public CElement scaled(CElement element) throws DomainException {
        return product(element);
    }

    @Override
    public void scale(CElement element) throws DomainException {
        multiply(element);
    }

    @Override
    public CElement sum(CElement element) throws DomainException {
        return new CElement(real + element.real, imag + element.imag);
    }

    @Override
    public void add(CElement element) throws DomainException {
        real += element.real;
        imag += element.imag;
    }

    @Override
    public CElement difference(CElement element) throws DomainException {
        return new CElement(real - element.real, imag - element.imag);
    }

    @Override
    public void subtract(CElement element) throws DomainException {
        real -= element.real;
        imag -= element.imag;
    }

    @Override
    public CElement negated() {
        return new CElement(-real, -imag);
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
    public CElement product(CElement element) throws DomainException {
        return new CElement(real * element.real - imag * element.imag, real * element.imag + imag * element.real);

    }

    @Override
    public void multiply(CElement element) throws DomainException {
        real = real * element.real - imag * element.imag;
        imag = real * element.imag + imag * element.real;
    }

    @Override
    public boolean isInvertible() {
        return !isZero();
    }

    @Override
    public CElement inverse() {
        double norm = real * real + imag * imag;
        return new CElement(real/norm, -imag/norm);
    }

    @Override
    public void invert() {
        double norm = norm();
        real /= norm;
        imag /= -norm;
    }

    @Override
    public CElement quotient(CElement element) throws DomainException, DivisionException {
        double otherNorm = element.norm();
        double newr = (real * element.real + imag * element.imag)/otherNorm;
        double newi = (imag * element.real - real * element.imag)/otherNorm;
        return new CElement(newr, newi);
    }

    @Override
    public void divide(CElement element) throws DomainException, DivisionException {
        double otherNorm = element.norm();
        real = (real * element.real + imag * element.imag)/otherNorm;
        imag = (imag * element.real - real * element.imag)/otherNorm;
    }

    @Override
    public boolean divides(RingElement<?> element) {
        return element instanceof CElement && !this.isZero();
    }

    @Override
    public Ring<CElement> getRing() {
        return CRing.ring;
    }

    @Override
    public CElement deepCopy() {
        return new CElement(real, imag);
    }

    private double norm() {
        return real * real + imag * imag;
    }

}
