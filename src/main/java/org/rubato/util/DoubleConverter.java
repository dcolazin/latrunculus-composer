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

import org.vetronauta.latrunculus.core.math.arith.number.IntegerWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.ModulusWrapper;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;


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

    private static class ZnConverter extends DoubleConverter<ArithmeticElement<ModulusWrapper>> {
        public ZnConverter(int modulus) {
            this.modulus = modulus;
        }
        public double toDouble(ArithmeticElement<ModulusWrapper> x) {
            return x.getValue().doubleValue();
        }
        public ArithmeticElement<ModulusWrapper> fromDouble(double x) {
            return new ArithmeticElement<>(new ModulusWrapper((int)Math.round(x), modulus));
        }
        private int modulus;
    }
    
    private static final DoubleConverter<Real> rconverter = new DoubleConverter<Real>() {
        public double toDouble(Real x) {
            return x.getValue();
        }
        public Real fromDouble(double x) {
            return new Real((x));
        }
    };

    private static final DoubleConverter<ArithmeticElement<IntegerWrapper>> zconverter = new DoubleConverter<ArithmeticElement<IntegerWrapper>>() {
        public double toDouble(ArithmeticElement<IntegerWrapper> x) {
            return x.getValue().doubleValue();
        }
        public ArithmeticElement<IntegerWrapper> fromDouble(double x) {
            return new ArithmeticElement<>(new IntegerWrapper((int) Math.round(x)));
        }
    };

    private static final DoubleConverter<Rational> qconverter = new DoubleConverter<Rational>() {
        public double toDouble(Rational x) {
            return x.doubleValue();
        }
        public Rational fromDouble(double x) {
            return new Rational(x);
        }
    };
}
