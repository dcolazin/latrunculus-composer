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

package org.vetronauta.latrunculus.core.scheme.expression;

import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.LatrunculusRuntimeException;
import org.vetronauta.latrunculus.core.math.arith.Trigonometry;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;

/**
 * The class of complex number values.
 * 
 * @author Gérard Milmeister
 */
public final class SComplex extends SNumber {

    /**
     * Creates a Scheme value from the complex number <code>c</code>.
     * If <code>c</code> is a real, an SReal instance is created.
     */
    public static SNumber make(Complex c) {
        if (c.getImag() == 0.0) {
            return SReal.make(c.getReal());
        }
        return new SComplex(c);
    }

    /**
     * Creates a Scheme complex number value from <code>c</code>.
     */
    public SComplex(Complex c) {
        this.c = c;
    }

    @Override
    public SType type() {
        return SType.COMPLEX;
    }

    public boolean eq_p(SExpr sexpr) {
        return this == sexpr;
    }
    
    public boolean eqv_p(SExpr sexpr) {
        return (sexpr instanceof SComplex) && ((SComplex)sexpr).c.equals(c);
    }
    
    public boolean equal_p(SExpr sexpr) {
        return (sexpr instanceof SComplex) && ((SComplex)sexpr).c.equals(c);
    }
    
    public boolean equals(Object obj) {
        return (obj instanceof SComplex) && ((SComplex)obj).c.equals(c);
    }
    
    public SNumber add(SNumber n) {
        return n.add(this);
    }

    public SNumber add(SInteger n) {
        return new SComplex(c.sum(new Complex(n.getInt())));
    }

    public SNumber add(SRational n) {
        return new SComplex(c.sum(new Complex(n.getRational().doubleValue())));
    }

    public SNumber add(SReal n) {
        return new SComplex(c.sum(new Complex(n.getDouble())));
    }

    public SNumber add(SComplex n) {
        return new SComplex(c.sum(n.c));
    }
    
    public SNumber subtract(SNumber n) {
        return n.subtractFrom(this);
    }

    public SNumber subtractFrom(SInteger n) {
        return new SComplex(c.difference(new Complex(n.getInt())));
    }

    public SNumber subtractFrom(SRational n) {
        return new SComplex(c.difference(new Complex(n.getRational().doubleValue())));
    }

    public SNumber subtractFrom(SReal n) {
        return SComplex.make(c.difference(new Complex(n.getDouble())));
    }

    public SNumber subtractFrom(SComplex n) {
        return SComplex.make(n.c.difference(n.c));
    }

    public SNumber multiply(SNumber n) {
        return n.multiply(this);
    }
    
    public SNumber multiply(SInteger n) {
        return SComplex.make(c.product(new Complex(n.getInt())));
    }

    public SNumber multiply(SRational n) {
        return SComplex.make(c.product(new Complex(n.getRational().doubleValue())));
    }

    public SNumber multiply(SReal n) {
        return SComplex.make(c.product(new Complex(n.getDouble())));
    }

    public SNumber multiply(SComplex n) {
        return SComplex.make(c.product(n.c));
    }

    public SNumber divide(SNumber n) {
        return n.divideInto(this);
    }
    
    public SNumber divideInto(SInteger n) {
        return SComplex.make(c.inverse().product(new Complex(n.getInt())));
    }
    
    public SNumber divideInto(SRational n) {
        return SComplex.make(c.inverse().product(new Complex(n.getRational().doubleValue())));
    }
    
    public SNumber divideInto(SReal n) {
        return SComplex.make(c.inverse().product(new Complex(n.getDouble())));
    }
    
    public SNumber divideInto(SComplex n) {
        try {
            return SComplex.make(n.c.quotient(c));
        } catch (DivisionException e) {
            throw new LatrunculusRuntimeException(e);
        }
    }
    
    public SNumber neg() {
        return SComplex.make(c.negated());
    }
    
    public SNumber abs() {
        return SReal.make(Trigonometry.abs(c));
    }
    
    public SNumber acos() {
        return new SComplex(Trigonometry.acos(c));
    }

    public SNumber asin() {
        return new SComplex(Trigonometry.asin(c));
    }

    public SNumber atan(SNumber n) {
        if (n.type() == SType.COMPLEX) {
            return new SComplex(Trigonometry.atan(c, ((SComplex)n).c));
        }
        else {
            return new SComplex(Trigonometry.atan(c, new Complex(n.toReal().getDouble())));
        }
    }

    public SNumber ceiling() {
        return SComplex.make(new Complex(Math.ceil(c.getReal()), Math.ceil(c.getImag())));
    }

    public SNumber cos() {
        try {
            return new SComplex(Trigonometry.cos(c));
        } catch (DivisionException e) {
            throw new LatrunculusRuntimeException(e);
        }
    }

    public SNumber exp() {
        return SComplex.make(Trigonometry.exp(c));
    }

    public SNumber floor() {
        return SComplex.make(new Complex(Math.floor(c.getReal()), Math.floor(c.getImag())));
    }

    public SNumber log() {
        return new SComplex(Trigonometry.log(c));
    }

    public SNumber round() {
        return SComplex.make(new Complex(Math.round(c.getReal()), Math.round(c.getImag())));
    }

    public SNumber sin() {
        try {
            return new SComplex(Trigonometry.sin(c));
        } catch (DivisionException e) {
            throw new LatrunculusRuntimeException(e);
        }
    }

    public SNumber tan() {
        try {
            return new SComplex(Trigonometry.tan(c));
        } catch (DivisionException e) {
            throw new LatrunculusRuntimeException(e);
        }
    }

    public SNumber truncate() {
        return SComplex.make(new Complex(SReal.truncate(c.getReal()), SReal.truncate(c.getImag())));
    }

    public SNumber angle() {
        return SReal.make(Trigonometry.arg(c));
    }

    public SNumber expt(SNumber n) {
        if (n.type() == SType.INTEGER) {
            if (n.negative_p()) {
                return new SComplex(c.inverse()).expt(n.neg());
            }
            else {
                int e = ((SInteger)n).getInt();
                Complex res = new Complex(1);
                while (e > 0) {
                    res = res.product(c);
                    e--;
                }
                return new SComplex(res); 
            }
        }
        else if (n.type() == SType.COMPLEX) {
            return new SComplex(Trigonometry.expt(c, ((SComplex)n).c));
        }
        else {
            return new SComplex(Trigonometry.expt(c, new Complex(n.toReal().getDouble())));
        }
    }

    public SNumber imagPart() {
        return SReal.make(c.getImag());
    }

    public SNumber realPart() {
        return SReal.make(c.getReal());
    }

    public SNumber sqrt() {
        return SComplex.make(Trigonometry.sqrt(c));
    }

    public boolean zero_p() {
        return c.isZero();
    }
    
    public boolean positive_p() {
        return c.getReal() > 0.0;
    }
    
    public boolean negative_p() {
        return c.getReal() < 0.0;
    }
    
    public SReal toReal() {
        return SReal.make(Trigonometry.abs(c));
    }
    
    public SNumber toInexact() {
        return this;
    }
    
    /**
     * Returns the complex number in this Scheme value.
     */
    public Complex getComplex() {
        return c;
    }
    
    private Complex c;

}
