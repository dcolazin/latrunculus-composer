/*
 * Copyright (C) 2006 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.scheme;

import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.LatrunculusRuntimeException;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;

/**
 * The class of rational values.
 * 
 * @author Gérard Milmeister
 */
public final class SRational extends SNumber {

    /**
     * Creates a Scheme value from the rational number <code>q</code>.
     */
    public static SNumber make(Rational q) {
        if (q.getDenominator() == 1) {
            return new SInteger(q.getNumerator());
        }
        return new SRational(q);
    }

    @Override
    public SType type() {
        return SType.RATIONAL;
    }

    public boolean eq_p(SExpr sexpr) {
        return this == sexpr;
    }
    
    public boolean eqv_p(SExpr sexpr) {
        return (sexpr instanceof SRational) && ((SRational)sexpr).q.equals(q);
    }
    
    public boolean equal_p(SExpr sexpr) {
        return (sexpr instanceof SRational) && ((SRational)sexpr).q.equals(q);
    }
    
    public boolean equals(Object obj) {
        return (obj instanceof SRational) && ((SRational)obj).q.equals(q);
    }

    public SNumber add(SNumber n) {
        return n.add(this);
    }

    public SNumber add(SInteger n) {
        return SRational.make(q.sum(new Rational(n.getInt())));
    }

    public SNumber add(SRational n) {
        return SRational.make(n.q.sum(q));
    }

    public SNumber add(SReal n) {
        return SReal.make(q.doubleValue()+n.getDouble());
    }

    public SNumber add(SComplex n) {
        return SComplex.make(n.getComplex().sum(new Complex(q.doubleValue())));
    }
    
    public SNumber subtract(SNumber n) {
        return n.subtractFrom(this);
    }

    public SNumber subtractFrom(SInteger n) {
        return SRational.make(q.difference(new Rational(n.getInt())));
    }

    public SNumber subtractFrom(SRational n) {
        return SRational.make(q.difference(n.q));
    }

    public SNumber subtractFrom(SReal n) {
        return SReal.make(n.getDouble()-q.doubleValue());
    }

    public SNumber subtractFrom(SComplex n) {
        return SComplex.make(n.getComplex().difference(new Complex(q.doubleValue())));
    }

    public SNumber multiply(SNumber n) {
        return n.multiply(this);
    }
    
    public SNumber multiply(SInteger n) {
        return new SRational(q.product(new Rational(n.getInt())));
    }

    public SNumber multiply(SRational n) {
        return new SRational(n.q.product(n.q));
    }

    public SNumber multiply(SReal n) {
        return SReal.make(n.getDouble()*q.doubleValue());
    }

    public SNumber multiply(SComplex n) {
        return SComplex.make(n.getComplex().product(new Complex(q.doubleValue())));
    }

    public SNumber divide(SNumber n) {
        return n.divideInto(this);
    }
    
    public SNumber divideInto(SInteger n) {
        return SRational.make(q.inverse().product(new Rational(n.getInt())));
    }
    
    public SNumber divideInto(SRational n) {
        try {
            return SRational.make(n.q.quotient(q));
        } catch (DivisionException e) {
            throw new LatrunculusRuntimeException(e);
        }
    }
    
    public SNumber divideInto(SReal n) {
        return SReal.make(n.getDouble()/q.doubleValue());
    }
    
    public SNumber divideInto(SComplex n) {
        try {
            return SComplex.make(n.getComplex().quotient(new Complex(q.doubleValue())));
        } catch (DivisionException e) {
            throw new LatrunculusRuntimeException(e);
        }
    }
    
    public SNumber neg() {
        return new SRational(q.negated());
    }

    public SNumber abs() {
        Rational abs = q.getNumerator() >= 0 ? q.deepCopy() : q.inverse();
        return new SRational(abs);
    }

    public SNumber acos() {
        return SReal.make(Math.acos(q.doubleValue()));
    }

    public SNumber asin() {
        return SReal.make(Math.asin(q.doubleValue()));
    }

    public SNumber atan(SNumber n) {
        return toReal().atan(n);
    }

    public SNumber ceiling() {
        return SReal.make(Math.ceil(q.doubleValue()));
    }

    public SNumber cos() {
        return SReal.make(Math.cos(q.doubleValue()));
    }

    public SNumber exp() {
        return SReal.make(Math.exp(q.doubleValue()));
    }

    public SNumber floor() {
        return SReal.make(Math.floor(q.doubleValue()));
    }

    public SNumber log() {
        return SReal.make(Math.log(q.doubleValue()));
    }

    public SNumber round() {
        return SReal.make(Math.round(q.doubleValue()));
    }

    public SNumber sin() {
        return SReal.make(Math.sin(q.doubleValue()));
    }

    public SNumber tan() {
        return SReal.make(Math.tan(q.doubleValue()));
    }

    public SNumber truncate() {
        return toReal().truncate();
    }

    public SNumber angle() {
        return q.doubleValue() >= 0?SReal.make(0):SReal.make(Math.PI);
    }

    public SNumber expt(SNumber n) {
        if (n.type() == SType.INTEGER) {
            if (n.negative_p()) {
                return new SRational(q.inverse()).expt(n.neg());
            }
            else {
                int e = ((SInteger)n).getInt();
                Rational res = new Rational(1);
                while (e > 0) {
                    res = res.product(q);
                    e--;
                }
                return new SRational(res); 
            }
        }
        else {
            return toReal().expt(n);
        }
    }

    public SNumber imagPart() {
        return SReal.make(0);
    }

    public SNumber realPart() {
        return toReal();
    }

    public SNumber sqrt() {
        return SReal.make(Math.sqrt(q.doubleValue()));
    }

    public boolean zero_p() {
        return q.isZero();
    }
    
    public boolean positive_p() {
        return q.compareTo(zero) > 0;
    }

    public boolean negative_p() {
        return q.compareTo(zero) < 0;
    }

    public SReal toReal() {
        return SReal.make(q.doubleValue());
    }
    
    public SNumber toInexact() {
        return toReal();
    }
    
    public String toString() {
        return q.toString();
    }
    
    public String display() {
        return q.toString();
    }
    
    /**
     * Returns the rational number in this Scheme value.
     */
    public Rational getRational() {
        return q;
    }
    
    private Rational q;
    
    private SRational(Rational q) {
        this.q = q;
    }
    
    private static final Rational zero = new Rational(0);
}
