/*
 * Copyright (C) 2005 Gérard Milmeister
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

package org.rubato.util;

import org.vetronauta.latrunculus.core.math.arith.number.Real;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.Modulus;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;


/**
 * Subclasses of this class specialize in converting module
 * elements to and from double.
 * 
 * @author Gérard Milmeister
 */
public abstract class DoubleConverter<T extends ModuleElement<T,?>> {

    /**
     * Converts the module element <code>x</code> to double.
     */
    public abstract double toDouble(T x);

    
    /**
     * Converts the double <code>x</code> to a module element.
     */
    public abstract T fromDouble(double x);
    
    
    /**
     * Creates a new converter that converts between double
     * and the elements from the module <code>m</code>.
     * 
     * @return null if no such converter could be created
     */
    public static DoubleConverter makeDoubleConverter(Module m) {
        if (m.equals(RRing.ring)) {
            return rconverter;
        }
        else if (m.equals(ZRing.ring)) {
            return zconverter;            
        }
        else if (m instanceof ZnRing) {
            return new ZnConverter(((ZnRing)m).getModulus());            
        }
        else if (m instanceof QRing) {
            return qconverter;            
        }
        else {
            return null;
        }
    }

    private static class ZnConverter extends DoubleConverter<ArithmeticElement<Modulus>> {
        public ZnConverter(int modulus) {
            this.modulus = modulus;
        }
        public double toDouble(ArithmeticElement<Modulus> x) {
            return x.getValue().doubleValue();
        }
        public ArithmeticElement<Modulus> fromDouble(double x) {
            return new ArithmeticElement<>(new Modulus((int)Math.round(x), modulus));
        }
        private int modulus;
    }
    
    private static final DoubleConverter<ArithmeticElement<Real>> rconverter = new DoubleConverter<ArithmeticElement<Real>>() {
        public double toDouble(ArithmeticElement<Real> x) {
            return x.getValue().doubleValue();
        }
        public ArithmeticElement<Real> fromDouble(double x) {
            return new ArithmeticElement<>(new Real(x));
        }
    };

    private static final DoubleConverter<ArithmeticElement<ArithmeticInteger>> zconverter = new DoubleConverter<ArithmeticElement<ArithmeticInteger>>() {
        public double toDouble(ArithmeticElement<ArithmeticInteger> x) {
            return x.getValue().doubleValue();
        }
        public ArithmeticElement<ArithmeticInteger> fromDouble(double x) {
            return new ArithmeticElement<>(new ArithmeticInteger((int) Math.round(x)));
        }
    };

    private static final DoubleConverter<ArithmeticElement<Rational>> qconverter = new DoubleConverter<ArithmeticElement<Rational>>() {
        public double toDouble(ArithmeticElement<Rational> x) {
            return x.getValue().doubleValue();
        }
        public ArithmeticElement<Rational> fromDouble(double x) {
            return new ArithmeticElement<>(new Rational(x));
        }
    };
}
