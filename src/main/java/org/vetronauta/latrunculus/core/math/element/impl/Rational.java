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
import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.math.element.generic.Arithmetic;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;

/**
 * @author vetronauta
 */
@Getter
public class Rational extends RingElement<Rational> implements Arithmetic {

    //TODO do not recheck gcd at every constructor, but only when necessary
    //TODO ensure that also after sum p/q are coprime

    //TODO quantization should be changed at factory level, not at class level
    private static final int INITIAL_DEFAULT_QUANT = 128*3*5;
    private static int DEFAULT_QUANT = 128*3*5;

    private int numerator;
    private int denominator;

    /**
     * Creates a new rational number <code>n</code>/1.
     */
    public Rational(int n) {
        numerator = n;
        denominator = 1;
    }


    /**
     * Creates a new rational <code>n</code>/<code>d</code>.
     * @param n is the numerator
     * @param d is the denominator
     */
    public Rational(int n, int d) {
        int g = NumberTheory.gcd(n, d);
        if (d > 0) {
            numerator = n/g;
            denominator = d/g;
        } else {
            numerator = -n/g;
            denominator = -d/g;
        }
    }

    /**
     * Creates a new rational from a double <code>d</code>.
     * Converts <code>d</code> with quantization <code>quant</code>.
     */
    public Rational(double d, int quant) {
        this((int)Math.round(d*quant), quant);
    }

    /**
     * Creates a new rational from a double <code>d</code>.
     * Converts <code>d</code> with default quantization.
     */
    public Rational(double d) {
        this((int)Math.round(d*DEFAULT_QUANT), DEFAULT_QUANT);
    }

    @Override
    public int intValue() {
        return (int) Math.round(doubleValue());
    }

    @Override
    public double doubleValue() {
        return ((double) numerator) / denominator;
    }

    @Override
    public boolean isZero() {
        return numerator == 0;
    }

    @Override
    public Rational sum(Rational element) throws DomainException {
        return new Rational(numerator * element.denominator + denominator * element.numerator, denominator * element.denominator);
    }

    @Override
    public void add(Rational element) throws DomainException {
        numerator = numerator * element.denominator + denominator * element.numerator;
        denominator = denominator * element.denominator;
    }

    @Override
    public Rational difference(Rational element) throws DomainException {
        return new Rational(numerator * element.denominator - denominator * element.numerator, denominator * element.denominator);
    }

    @Override
    public void subtract(Rational element) throws DomainException {
        numerator = numerator * element.denominator - denominator * element.numerator;
        denominator = denominator * element.denominator;
    }

    @Override
    public Rational negated() {
        return new Rational(-numerator, denominator);
    }

    @Override
    public void negate() {
        numerator = -numerator;
    }

    @Override
    public boolean isOne() {
        return numerator == denominator;
    }

    @Override
    public Rational product(Rational element) throws DomainException {
        int g = NumberTheory.gcd(element.denominator, numerator) * NumberTheory.gcd(element.numerator, denominator);
        return new Rational(numerator * element.numerator / g, denominator * element.denominator / g);
    }

    @Override
    public void multiply(Rational element) throws DomainException {
        int g = NumberTheory.gcd(element.denominator, numerator) * NumberTheory.gcd(element.numerator, denominator);
        numerator *= element.numerator / g;
        denominator *= element.denominator / g;
    }

    @Override
    public boolean isInvertible() {
        return !isZero();
    }

    @Override
    public Rational inverse() {
        if (numerator == 0) {
            throw new InverseException(this);
        }
        if (numerator < 0) {
            return new Rational(-denominator,-numerator);
        }
        return new Rational(denominator, numerator);
    }

    @Override
    public void invert() {
        int swap = numerator;
        numerator = denominator;
        denominator = swap;
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
    }

    @Override
    public Rational quotient(Rational element) throws DomainException, DivisionException {
        int g = NumberTheory.gcd(element.denominator, denominator) * NumberTheory.gcd(element.numerator, numerator);
        int n = numerator * element.denominator / g;
        int d = denominator * element.numerator / g;
        if (d == 0) {
            throw new DivisionException(this, element);
        }
        if (d < 0) {
            n = -n;
            d = -d;
        }
        return new Rational(n,d);
    }

    @Override
    public void divide(Rational element) throws DomainException, DivisionException {
        int g = NumberTheory.gcd(element.denominator, denominator) * NumberTheory.gcd(element.numerator, numerator);
        int n = numerator * element.denominator / g;
        int d = denominator * element.numerator / g;
        if (d == 0) {
            throw new DivisionException(this, element);
        }
        if (d < 0) {
            n = -n;
            d = -d;
        }
        numerator = n;
        denominator = d;
    }

    @Override
    public boolean divides(RingElement<?> element) {
        return element instanceof Rational && this.isInvertible();
    }

    @Override
    public Ring<Rational> getRing() {
        return QRing.ring;
    }

    @Override
    public Rational deepCopy() {
        return new Rational(numerator, denominator);
    }

    @Override
    public String toString() {
        return String.format("%d/%d", numerator, denominator);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Rational && (numerator * ((Rational) other).denominator) == (denominator * ((Rational) other).numerator);
    }

    @Override
    public int hashCode() {
        return numerator * denominator;
    }

    /**
     * Sets the default quantization to the given value.
     * The quantization is taken to be the absolute value of <code>quant</code>.
     * If <code>quant</code> is 0, the initial default value is
     * used.
     */
    public static void setDefaultQuantization(int quant) {
        int q = Math.abs(quant);
        if (q == 0) {
            DEFAULT_QUANT = INITIAL_DEFAULT_QUANT;
        }
        else {
            DEFAULT_QUANT = q;
        }
    }

}
