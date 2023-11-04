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

import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.exception.InverseException;

/**
 * Rational number arithmetic.
 * 
 * @author Gérard Milmeister
 */
public final class RationalWrapper implements ArithmeticNumber<RationalWrapper> {

    //TODO quantization should be changed at factory level, not at class level
    private static final int INITIAL_DEFAULT_QUANT = 128*3*5;
    private static int DEFAULT_QUANT = 128*3*5;

    private final int num;
    private final int denom;

    /**
     * Creates a new rational number <code>n</code>/1.
     */
    public RationalWrapper(int n) {
        num = n;
        denom = 1;
    }


    /**
     * Creates a new rational <code>n</code>/<code>d</code>.
     * @param n is the numerator
     * @param d is the denominator
     */
    public RationalWrapper(int n, int d) {
        int g = NumberTheory.gcd(n, d);
        if (d > 0) {
            num = n/g;
            denom = d/g;
        } else {
            num = -n/g;
            denom = -d/g;
        }
    }


    /**
     * Creates a new rational from <code>r</code>.
     * Copy constructor.
     */
    public RationalWrapper(RationalWrapper r) {
        num = r.num;
        denom = r.denom;
    }


    /**
     * Creates a new rational from a double <code>d</code>.
     * Converts <code>d</code> with quantization <code>quant</code>.
     */
    public RationalWrapper(double d, int quant) {
        this((int)Math.round(d*quant), quant);
    }


    /**
     * Creates a new rational from a double <code>d</code>.
     * Converts <code>d</code> with default quantization.
     */
    public RationalWrapper(double d) {
        this((int)Math.round(d*DEFAULT_QUANT), DEFAULT_QUANT);
    }


    /**
     * Creates a new rational from another rational using given quantization.
     * A new rational <code>x</code>/<code>quant</code> is created
     * such that <code>x</code>/<code>quant</code> <= <code>r</code> < (<code>x</code>+1)/<code>quant</code>.
     */
    public RationalWrapper(RationalWrapper r, int quant) {
        int qu = quant;
        if (qu < 0) qu = -qu;
        int numerator = r.getNumerator();
        int denominator = r.getDenominator();
        int sign = 1; 
        if (numerator < 0) {
            sign = -1;
            numerator = -numerator;
        }
        long q = (numerator*qu)/denominator;
        long s = (numerator*qu)%denominator;
        if (s*2 > denominator) q++;
        RationalWrapper newRationalWrapper = new RationalWrapper(sign*(int)q, quant); //TODO hack...
        num = newRationalWrapper.num;
        denom = newRationalWrapper.denom;
    }

    /**
     * Returns the rational 0/1.
     */
    public static RationalWrapper getZero() {
        return new RationalWrapper(0);
    }


    /**
     * Returns the rational 1/1.
     */
    public static RationalWrapper getOne() {
        return new RationalWrapper(1);
    }


    public boolean equals(Object other) {
        if (!(other instanceof RationalWrapper)) {
            return false;
        }
        RationalWrapper otherRationalWrapper = (RationalWrapper) other;
        return otherRationalWrapper.num == num && otherRationalWrapper.denom == denom;
    }

    /**
     * Returns true iff this number is 0/1.
     */
    @Override
    public boolean isZero() {
        return num == 0;
    }


    /**
     * Returns true iff this number is 1/1.
     */
    public boolean isOne() {
        return num == 1 && denom == 1;
    }

    @Override
    public boolean isInvertible() {
        return !isZero();
    }

    @Override
    public boolean isFieldElement() {
        return true;
    }

    @Override
    public boolean divides(ArithmeticNumber<?> y) {
        return (y instanceof RationalWrapper) && !this.isZero();
    }

    @Override
    public int compareTo(RationalWrapper r) {
        int a, b;
        a = num * r.getDenominator();
        b = r.getNumerator() * denom;
        return a-b;
    }


    /**
     * Returns the sum of this number and <code>r</code>.
     */
    @Override
    public RationalWrapper sum(RationalWrapper r) {
        return new RationalWrapper(num * r.denom + denom * r.num, denom * r.denom);
    }


    /**
     * Returns the sum of this number and the integer <code>n</code>.
     */
    public RationalWrapper sum(int n) {
        return new RationalWrapper(num + n * denom, denom);
    }

    /**
     * Returns the difference of this number and <code>r</code>.
     */
    @Override
    public RationalWrapper difference(RationalWrapper r) {
        return new RationalWrapper(num * r.denom - denom * r.num, denom * r.denom);
    }


    /**
     * Returns the difference of this number and the integer <code>n</code>.
     */
    public RationalWrapper difference(int n) {
        return new RationalWrapper(num - n * denom, denom);
    }

    /**
     * Returns the product of this number and <code>r</code>.
     */
    public RationalWrapper product(RationalWrapper r) {
        int g = NumberTheory.gcd(r.denom, num) * NumberTheory.gcd(r.num, denom);
        return new RationalWrapper(num * r.num / g, denom * r.denom / g); //TODO do not recheck gcd
    }


    /**
     * Returns the product of this number and the integer <code>n</code>.
     */
    public RationalWrapper product(int n) {
        int g = NumberTheory.gcd(n, denom);
        return new RationalWrapper(num * n / g, denom / g); //TODO do not recheck gcd
    }

    /**
     * Returns the quotient of this number and <code>r</code>.
     */
    public RationalWrapper quotient(RationalWrapper r) {
        int g = NumberTheory.gcd(r.denom, denom) * NumberTheory.gcd(r.num, num);
        int n = num * r.denom / g;
        int d = denom * r.num / g;
        if (d == 0) {
            throw new ArithmeticException();
        }
        if (d < 0) {
            n = -n;
            d = -d;
        }
        return new RationalWrapper(n,d); //TODO avoid rechecking gcd
    }


    /**
     * Returns the quotient of this number and the integer <code>n</code>.
     */
    public RationalWrapper quotient(int n) {
        int g = NumberTheory.gcd(n, num);
        int m = num / g;
        int d = denom * (n / g);
        if (d == 0) {
            throw new ArithmeticException();
        }
        if (d < 0) {
            m = -m;
            d = -d;
        }
        return new RationalWrapper(m,d); //TODO avoid rechecking gcd
    }

    /**
     * Returns the inverse of this rational.
     */
    public RationalWrapper inverse() {
        if (num == 0) {
            throw new InverseException(this);
        }
        if (num < 0) {
            return new RationalWrapper(-denom,-num); //TODO avoid check gcd
        } else {
            return new RationalWrapper(denom,num); //TODO avoid check gcd
        }
    }

    /**
     * Returns -this number.
     */
    @Override
    public RationalWrapper neg() {
        return new RationalWrapper(-num, denom);
    }

    /**
     * Returns the numerator of this rational.
     */
    public int getNumerator() {
        return num;
    }

    
    /**
     * Returns the denominator of this rational.
     */
    public int getDenominator() {
        return denom;
    }

    
    
    /**
     * Returns the absolute value of this rational.
     */
    public RationalWrapper abs() {
        if (num < 0) {
            return new RationalWrapper(-num,denom); //TODO avoid check denominator
        } else {
            return this;
        }
    }
    
    
    /**
     * Converts this rational to a double.
     */
    public double doubleValue() {
        return (double)num/(double)denom;
    }


    /**
     * Converts this rational to a float.
     */
    public float floatValue() {
        return (float)doubleValue();
    }


    public long longValue() {
        return Math.round(doubleValue());
    }

    
    public int intValue() {
        return (int)Math.round(doubleValue());
    }
    
    public boolean isIntegral() {
        return denom == 1;
    }
    
    /**
     * Returns the string representation of this rational.
     */
    public String toString() {
        if (denom == 1) {
            return ""+num;
        }
        else {            
            return num+"/"+denom;
        }
    }

    public RationalWrapper deepCopy() {
        return this;
    }


    /**
     * Returns the hashcode for this rational.
     */
    public int hashCode() {
        return num ^ denom;
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
    

    /**
     * Resets the default quantization to the initial value.
     */
    public static void resetDefaultQuantization() {
        DEFAULT_QUANT = INITIAL_DEFAULT_QUANT;
    }
    
    
    /**
     * Returns the current default quantization;
     */
    public static int getDefaultQuantization() {
        return DEFAULT_QUANT;
    }


}
