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

package org.vetronauta.latrunculus.core.math.folding;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.arith.number.IntegerWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.ModulusWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.RationalWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.RealWrapper;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;

import java.util.List;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FoldingModule {

    //TODO make this a proper object to inject when needed

    public static double[] fold(Ring<?> ring, ModuleElement<?, ?>[] elements) {
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
            ZInteger e = (ZInteger) elements[i];
            res[i] = e.intValue();
        }
        return res;
    }

    public static double[] foldRational(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            Rational r = (Rational)elements[i];
            res[i] = r.doubleValue();
        }
        return res;
    }

    public static double[] foldReal(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            Real e = (Real)elements[i];
            res[i] = e.getValue();
        }
        return res;
    }

    public static double[] foldComplex(ModuleElement[] elements) {
        double[][] res = new double[elements.length][2];
        for (int i = 0; i < elements.length; i++) {
            Complex c = (Complex) elements[i];
            res[i][0] = c.getReal();
            res[i][1] = c.getImag();
        }
        return Folding.fold(res);
    }

    public static double[] foldModulus(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            ArithmeticElement<ModulusWrapper> e = (ArithmeticElement<ModulusWrapper>)elements[i];
            res[i] = e.getValue().intValue();
        }
        return res;
    }

    public static double[] multiFold(Ring<?> ring, ModuleElement<?, ?>[] elements, int length) {
        if (ring instanceof ZRing) {
            return multiFoldInteger(elements);
        }
        if (ring instanceof QRing) {
            return multiFoldRational(elements);
        }
        if (ring instanceof RRing) {
            return multiFoldReal(elements);
        }
        if (ring instanceof CRing) {
            return multiFoldComplex(elements, length);
        }
        if (ring instanceof ZnRing) {
            return multiFoldModulus(elements);
        }
        throw new UnsupportedOperationException(String.format("cannot fold %s", ring));
    }

    public static double[] multiFoldInteger(ModuleElement<?,?>[] elements) {
        double[][] res = new double[elements.length][];
        int len = elements[0].getLength();
        // Create an array of double arrays corresponding
        // to the array of RFreeElements
        for (int i = 0; i < elements.length; i++) {
            res[i] = new double[len];
            for (int j = 0; j < len; j++) {
                res[i][j] = ((Vector<ZInteger>)elements[i]).getValue().get(j).intValue();
            }
        }
        return Folding.fold(res);
    }

    public static double[] multiFoldRational(ModuleElement<?,?>[] elements) {
        double[][] res = new double[elements.length][];
        // Create an array of double arrays corresponding
        // to the array of RFreeElements
        for (int i = 0; i < elements.length; i++) {
            List<Rational> r = ((Vector<Rational>)elements[i]).getValue();
            res[i] = new double[elements.length];
            for (int j = 0; j < elements.length; j++) {
                res[i][j] = r.get(j).doubleValue();
            }
        }
        return Folding.fold(res);
    }

    public static double[] multiFoldReal(ModuleElement<?,?>[] elements) {
        double[][] res = new double[elements.length][];
        // Create an array of double arrays corresponding
        // to the array of RFreeElements
        for (int i = 0; i < elements.length; i++) {
            res[i] = new double[elements.length];
            List<Real> r = ((Vector<Real>)elements[i]).getValue();
            for (int j = 0; j < elements.length; j++) {
                res[i][j] = r.get(j).getValue();
            }
        }
        return Folding.fold(res);
    }

    public static double[] multiFoldComplex(ModuleElement<?,?>[] elements, int length) {
        double[][] res = new double[elements.length][length*2];
        for (int i = 0; i < elements.length; i++) {
            List<Complex> c = ((Vector<Complex>)elements[i]).getValue();
            for (int j = 0; j < length; j++) {
                res[i][2*j] = c.get(j).getReal();
                res[i][2*j+1] = c.get(j).getImag();
            }
        }
        return Folding.fold(res);
    }

    public static double[] multiFoldModulus(ModuleElement<?,?>[] elements) {
        double[][] res = new double[elements.length][];
        int len = (elements[0]).getLength();
        // Create an array of double arrays corresponding
        // to the array of RFreeElements
        for (int i = 0; i < elements.length; i++) {
            res[i] = new double[len];
            for (int j = 0; j < len; j++) {
                res[i][j] = ((Vector<ArithmeticElement<ModulusWrapper>>)elements[i]).getValue().get(j).getValue().intValue();
            }
        }
        return Folding.fold(res);
    }

}
