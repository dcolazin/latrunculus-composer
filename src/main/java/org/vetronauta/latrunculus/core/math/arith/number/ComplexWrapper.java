/*
 * Copyright (C) 2001 Gérard Milmeister
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.vetronauta.latrunculus.core.math.arith.number;

/**
 * Complex number arithmetic.
 * 
 * @author Gérard Milmeister
 */
public final class ComplexWrapper {

    //TODO remove this class after Scheme refactoring

    private final double real;
    private final double imag;

    /**
     * Creates the complex number real+i*imag. 
     */
    public ComplexWrapper(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }
    
    
    /**
     * Creates the complex number real+i0.
     */
    public ComplexWrapper(double real) {
        this.real = real;
        this.imag = 0.0;
    }
    
    /**
     * Creates a new complex number from <code>c</code>.
     */
    public ComplexWrapper(ComplexWrapper c) {
        real = c.real;
        imag = c.imag;
    }
    
    /**
     * Creates a complex number with the polar representation r*e^(i*phi).
     * @param r   the absolute value of the complex number
     * @param phi the argument of the complex number
     */
    public static ComplexWrapper fromPolar(double r, double phi) {
        return new ComplexWrapper(r*Math.cos(phi), r*Math.sin(phi));
    }

    
    /**
     * Returns the real part of the complex number rounded to an integer.
     */
    public int intValue() {
        return (int)Math.round(real);
    }

    
    /**
     * Returns the real part of the complex number rounded to a long.
     */
    public long longValue() {
        return Math.round(real);
    }

    
    /**
     * Returns the real part of the complex number as a float.
     */
    public float floatValue() {
        return (float)real;
    }

    
    /**
     * Returns the real part of the complex number as a double.
     */
    public double doubleValue() {
        return real;
    }

    
    /**
     * Returns the complex number 1+i0.
     */
    public static ComplexWrapper getOne() {
        return new ComplexWrapper(1.0);
    }

    
    /**
     * Returns the complex number 0+i0.
     */
    public static ComplexWrapper getZero() {
        return new ComplexWrapper(0.0);
    }


    /**
     * Returns the sum of this number and <code>c</code>.
     */
    public ComplexWrapper sum(ComplexWrapper c) {
        return new ComplexWrapper(real+c.real, imag+c.imag);
    }


    /**
     * Returns the sum of this number and <code>x</code>.
     */
    public ComplexWrapper sum(double x) {
        return new ComplexWrapper(real+x, imag);
    }

    /**
     * Returns the difference of this number and <code>c</code>.
     */
    public ComplexWrapper difference(ComplexWrapper c) {
        return new ComplexWrapper(real-c.real, imag-c.imag);
    }

    
    /**
     * Returns the difference of this number and <code>x</code>.
     */
    public ComplexWrapper difference(double x) {
        return new ComplexWrapper(real-x, imag);
    }

    /**
     * Returns the product of this number and <code>c</code>.
     */
    public ComplexWrapper product(ComplexWrapper c) {
        return new ComplexWrapper(real*c.real-imag*c.imag, real*c.imag+imag*c.real);
    }

    
    /**
     * Returns the product of this number and the real number <code>x</code>.
     */
    public ComplexWrapper product(double x) {
        return new ComplexWrapper(x*real, x*imag);
    }

    /**
     * Returns the quotient of this number by <code>c</code>.
     */
    public ComplexWrapper quotient(ComplexWrapper c) {
        double d = c.real*c.real+c.imag*c.imag;
        double newr = (real*c.real+imag*c.imag)/d;
        double newi = (imag*c.real-real*c.imag)/d;        
        return new ComplexWrapper(newr, newi);
    }

    
    /**
     * Returns the quotient of this number by <code>x</code>.
     */
    public ComplexWrapper quotient(double x) {
        double newr = real/x;
        double newi = imag/x;        
        return new ComplexWrapper(newr, newi);
    }

    /**
     * Returns the inverse of this number.
     */
    public ComplexWrapper inverse() {
        double d = real*real+imag*imag;
        return new ComplexWrapper(real/d, -imag/d);
    }

    /**
     * Returns the negative of this number.
     */
    public ComplexWrapper neg() {
        return new ComplexWrapper(-real, -imag);
    }

    /**
     * Returns true iff this number is 0+i0.
     */
    public boolean isZero() {
        return real == 0.0 && imag == 0.0;
    }

    
    /**
     * Returns true iff this number is 1+i0.
     */
    public boolean isOne() {
        return real == 1.0 && imag == 0.0;
    }

    public boolean isInvertible() {
        return !isZero();
    }

    public boolean isFieldElement() {
        return true;
    }

    public boolean divides(ArithmeticNumber<?> y) {
        return false;
        //return (y instanceof ComplexWrapper) && !this.isZero();
    }


    /**
     * Returns the real part of this number.
     */
    public double getReal() {
        return real;
    }

    
    /**
     * Returns the imaginary part of this number.
     */
    public double getImag() {
        return imag;
    }

    
    /**
     * Returns true iff the imaginary part is zero.
     */
    public boolean isReal() {
        return imag == 0.0;
    }

    
    /**
     * Returns the absolute value of this number.
     */
    public double abs() {
        return Math.sqrt(real*real+imag*imag);
    }
    

    /**
     * Returns the argument of this number.
     */
    public double arg() {
        return Math.atan2(imag, real);
    }

    
    /**
     * Returns the square root of this number.
     */
    public ComplexWrapper sqrt() {
        double m = Math.sqrt(abs());
        double a = arg()/2;
        return fromPolar(m, a);
    }
    
    
    /**
     * Returns the natural logarithm of this number.
     */
    public ComplexWrapper log() {
        double a = Math.log(abs());
        double b = arg();
        return new ComplexWrapper(a, b);
    }
    
    
    /**
     * Returns the exponential of this number.
     */
    public ComplexWrapper exp() {
        double r = Math.exp(getReal());
        double i = getImag();
        return fromPolar(r, i);
    }
    
    
    /**
     * Returns this number raised to the power <code>c</code>.
     */
    public ComplexWrapper expt(ComplexWrapper c) {
        double a1 = abs();
        double b1 = arg();
        ComplexWrapper c1 = c.product(Math.log(a1)).exp();
        ComplexWrapper c2 = new ComplexWrapper(0, b1).product(c).exp();
        return c1.product(c2);
    }
    
    
    /**
     * Returns the sine of this number.
     */
    public ComplexWrapper sin() {
        ComplexWrapper a = new ComplexWrapper(0, 1);
        a = a.product(this);
        a = a.exp();

        ComplexWrapper b = new ComplexWrapper(0, -1);
        b = b.product(this);
        b = b.exp();

        a = a.difference(b);
        a = a.quotient(new ComplexWrapper(0, 2));
        
        return a;
    }
    

    /**
     * Returns the cosine of this number.
     */
    public ComplexWrapper cos() {
        ComplexWrapper a = new ComplexWrapper(0, 1);
        a = a.product(this);
        a = a.exp();

        ComplexWrapper b = new ComplexWrapper(0, -1);
        b = b.product(this);
        b = b.exp();

        a = a.sum(b);
        a = a.quotient(new ComplexWrapper(2, 0));
        
        return a;
    }

    
    /**
     * Returns the tangent of this number.
     */
    public ComplexWrapper tan() {
        ComplexWrapper a = new ComplexWrapper(0, 2);
        a = a.product(this);
        a = a.exp();        

        ComplexWrapper b = new ComplexWrapper(a);
        
        a = a.difference(1);
        b = b.sum(1);
        
        b = b.product(new ComplexWrapper(0, 1));
        a = a.quotient(b);
        
        return a;
    }

    
    /**
     * Returns the arcsine of this number.
     */
    public ComplexWrapper asin() {
        ComplexWrapper a = this.product(this);
        a = a.neg();
        a = a.sum(1);
        a = a.sqrt();
        a = a.sum(this.product(new ComplexWrapper(0, 1)));
        a = a.log();
        a = a.product(new ComplexWrapper(0,1));
        a = a.neg();
        return a;
    }
    
    
    /**
     * Returns the arccosine of this number.
     */
    public ComplexWrapper acos() {
        ComplexWrapper a = this.product(this);
        a = a.neg();
        a = a.sum(1);
        a = a.sqrt();
        a = a.sum(this.product(new ComplexWrapper(0, 1)));
        a = a.log();
        a = a.product(new ComplexWrapper(0,1));
        a = a.sum(Math.PI/2);
        return a;
    }
    
    
    /**
     * Returns the arctangent of this number and <code>c</code>.
     */
    public ComplexWrapper atan(ComplexWrapper c) {
        // TODO: not yet implemented
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ComplexWrapper)) {
            return false;
        }
        ComplexWrapper otherComplexWrapper = (ComplexWrapper) other;
        return real == otherComplexWrapper.real && imag == otherComplexWrapper.imag;
    }

    /**
     * Returns hashcode for this number.
     */
    public int hashCode() {
        long h = Double.doubleToRawLongBits(real);
        h = h*17+Double.doubleToLongBits(imag);
        return (int)((h >> 32)^h);
    }


    /**
     * Returns the string representation of this number.
     */
    public String toString() {
        return real+"+i*"+imag;
    }
    
    /**
     * Compares this number with <code>object</code>.
     * Since complex numbers are not linearly ordered, the comparison
     * is lexicographic.
     */
    public int compareTo(ComplexWrapper c) {
        if (real != c.real) {
            return Double.compare(real, c.real);
        }
        return Double.compare(imag, c.imag);
    }

    public ComplexWrapper deepCopy() {
        return new ComplexWrapper(real, imag);
    }
}
