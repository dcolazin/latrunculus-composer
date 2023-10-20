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

package org.vetronauta.latrunculus.core.math.module;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;
import org.vetronauta.latrunculus.core.math.module.real.RRing;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FoldingModule {

    //TODO make this a proper object to inject when needed

    public static double[] fold(ArithmeticRing<?> ring, ModuleElement<?, ?>[] elements) {
        if (ring instanceof ZRing) {
            return foldInteger(elements);
        }
        if (ring instanceof QRing) {
            return foldRational(elements);
        }
        if (ring instanceof RRing) {
            return foldReal(elements);
        }
        if (ring instanceof CRing) {
            return foldComplex(elements);
        }
        if (ring instanceof ZnRing) {
            return foldModulus(elements);
        }
        throw new UnsupportedOperationException(String.format("cannot fold %s", ring));
    }

    public static double[] foldInteger(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            ArithmeticElement<ArithmeticInteger> e = (ArithmeticElement<ArithmeticInteger>) elements[i];
            res[i] = e.getValue().intValue();
        }
        return res;
    }

    public static double[] foldRational(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            ArithmeticElement<Rational> r = (ArithmeticElement<Rational>)elements[i];
            res[i] = r.getValue().doubleValue();
        }
        return res;
    }

    public static double[] foldReal(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            ArithmeticElement<ArithmeticDouble> e = (ArithmeticElement<ArithmeticDouble>)elements[i];
            res[i] = e.getValue().doubleValue();
        }
        return res;
    }

    public static double[] foldComplex(ModuleElement[] elements) {
        double[][] res = new double[elements.length][2];
        for (int i = 0; i < elements.length; i++) {
            ArithmeticElement<Complex> c = (ArithmeticElement<Complex>)elements[i];
            res[i][0] = c.getValue().getReal();
            res[i][1] = c.getValue().getImag();
        }
        return Folding.fold(res);
    }

    public static double[] foldModulus(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            ArithmeticElement<ArithmeticModulus> e = (ArithmeticElement<ArithmeticModulus>)elements[i];
            res[i] = e.getValue().intValue();
        }
        return res;
    }

}
