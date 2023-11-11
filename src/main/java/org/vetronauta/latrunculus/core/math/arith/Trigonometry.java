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

package org.vetronauta.latrunculus.core.math.arith;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Trigonometry {

    /**
     * Creates a complex number with the polar representation r*e^(i*phi).
     * @param r   the absolute value of the complex number
     * @param phi the argument of the complex number
     */
    public static Complex fromPolar(double r, double phi) {
        return new Complex(r*Math.cos(phi), r*Math.sin(phi));
    }

    /**
     * Returns the exponential of the complex number.
     */
    public static Complex exp(Complex complex) {
        double r = Math.exp(complex.getReal());
        double i = complex.getImag();
        return fromPolar(r, i);
    }

    /**
     * Returns this number raised to the power <code>c</code>.
     */
    public static Complex expt(Complex complex, Complex exponent) {
        double a1 = abs(complex);
        double b1 = arg(complex);
        Complex c1 = exp(exponent.product(new Complex(Math.log(a1))));
        Complex c2 = exp(new Complex(0, b1).product(exponent));
        return c1.product(c2);
    }

    /**
     * Returns the square root of this number.
     */
    public static Complex sqrt(Complex complex) {
        double m = Math.sqrt(abs(complex));
        double a = arg(complex)/2;
        return fromPolar(m, a);
    }

    /**
     * Returns the argument of this number.
     */
    public static double arg(Complex complex) {
        return Math.atan2(complex.getImag(), complex.getReal());
    }

    /**
     * Returns the natural logarithm of this number.
     */
    public static Complex log(Complex complex) {
        double a = Math.log(abs(complex));
        double b = arg(complex);
        return new Complex(a, b);
    }

    public static double abs(Complex complex) {
        return Math.sqrt(complex.norm());

    }

    /**
     * Returns the sine of this number.
     */
    public static Complex sin(Complex complex) throws DivisionException {
        Complex a = new Complex(0, 1);
        a = a.product(complex);
        a = exp(a);

        Complex b = new Complex(0, -1);
        b = b.product(complex);
        b = exp(b);

        a = a.difference(b);
        a = a.quotient(new Complex(0, 2));

        return a;
    }

    /**
     * Returns the cosine of this number.
     */
    public static Complex cos(Complex complex) throws DivisionException {
        Complex a = new Complex(0, 1);
        a = a.product(complex);
        a = exp(a);

        Complex b = new Complex(0, -1);
        b = b.product(complex);
        b = exp(b);

        a = a.sum(b);
        a = a.quotient(new Complex(2, 0));

        return a;
    }

    /**
     * Returns the tangent of the complex number.
     */
    public static Complex tan(Complex complex) throws DivisionException {
        Complex a = new Complex(0, 2);
        a = a.product(complex);
        a = exp(a);

        Complex b = a.deepCopy();

        a = a.difference(new Complex(1));
        b = b.sum(new Complex(1));

        b = b.product(new Complex(0, 1));
        a = a.quotient(b);

        return a;
    }

    /**
     * Returns the arcsine of this number.
     */
    public static Complex asin(Complex complex) {
        Complex a = complex.product(complex);
        a = a.negated();
        a = a.sum(new Complex(1));
        a = sqrt(a);
        a = a.sum(complex.product(new Complex(0, 1)));
        a = log(a);
        a = a.product(new Complex(0,1));
        a = a.negated();
        return a;
    }

    /**
     * Returns the arccosine of the complex number.
     */
    public static Complex acos(Complex complex) {
        Complex a = complex.product(complex);
        a = a.negated();
        a = a.sum(new Complex(1));
        a = sqrt(a);
        a = a.sum(complex.product(new Complex(0, 1)));
        a = log(a);
        a = a.product(new Complex(0,1));
        a = a.sum(new Complex(Math.PI/2));
        return a;
    }

    /**
     * Returns the arctangent of this number and <code>c</code>.
     */
    public static Complex atan(Complex complex, Complex other) {
        // TODO: not yet implemented
        return null;
    }

}
