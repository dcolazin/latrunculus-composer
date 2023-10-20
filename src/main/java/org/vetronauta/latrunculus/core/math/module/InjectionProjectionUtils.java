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
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.matrix.CMatrix;
import org.vetronauta.latrunculus.core.math.matrix.QMatrix;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.matrix.ZMatrix;
import org.vetronauta.latrunculus.core.math.matrix.ZnMatrix;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.core.math.module.morphism.CFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.QFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.RFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZnFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;
import org.vetronauta.latrunculus.core.math.module.real.RRing;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InjectionProjectionUtils {

    //TODO temp class before the matrix generic refactoring

    public static ModuleMorphism getProjection(ArithmeticRing<?> ring, int index, int dimension) {
        if (ring instanceof CRing) {
            return getProjectionComplex(index, dimension);
        }
        if (ring instanceof ZRing) {
            return getProjectionInteger(index, dimension);
        }
        if (ring instanceof ZnRing) {
            return getProjectionModular(index, dimension, ((ZnRing) ring).getModulus());
        }
        if (ring instanceof QRing) {
            return getProjectionRational(index, dimension);
        }
        if (ring instanceof RRing) {
            return getProjectionReal(index, dimension);
        }
        throw new UnsupportedOperationException(String.format("cannot project %s", ring));
    }

    public static ModuleMorphism getInjection(ArithmeticRing<?> ring, int index, int dimension) {
        if (ring instanceof CRing) {
            return getInjectionComplex(index, dimension);
        }
        if (ring instanceof ZRing) {
            return getInjectionInteger(index, dimension);
        }
        if (ring instanceof ZnRing) {
            return getInjectionModular(index, dimension, ((ZnRing) ring).getModulus());
        }
        if (ring instanceof QRing) {
            return getInjectionRational(index, dimension);
        }
        if (ring instanceof RRing) {
            return getInjectionReal(index, dimension);
        }
        throw new UnsupportedOperationException(String.format("cannot inject %s", ring));
    }

    private static ModuleMorphism getProjectionComplex(int index, int dimension) {
        CMatrix A = new CMatrix(1, dimension);
        A.set(0, index, Complex.getOne());
        return CFreeAffineMorphism.make(A, new Complex[] { Complex.getZero() });
    }

    private static ModuleMorphism getInjectionComplex(int index, int dimension) {
        CMatrix A = new CMatrix(dimension, 1);
        A.set(index, 0, Complex.getOne());
        Complex[] b = new Complex[dimension];
        for (int i = 0; i < dimension; i++) {
            b[i] = Complex.getZero();
        }
        return CFreeAffineMorphism.make(A, b);
    }

    private static ModuleMorphism getProjectionInteger(int index, int dimension) {
        ZMatrix A = new ZMatrix(1, dimension);
        A.set(0, index, 1);
        return ZFreeAffineMorphism.make(A, new int[] { 0 });
    }

    private static ModuleMorphism getInjectionInteger(int index, int dimension) {
        ZMatrix A = new ZMatrix(dimension, 1);
        A.set(index, 0, 1);
        int[] b = new int[dimension];
        for (int i = 0; i < dimension; i++) {
            b[i] = 0;
        }
        return ZFreeAffineMorphism.make(A, b);
    }

    private static ModuleMorphism getProjectionModular(int index, int dimension, int modulus) {
        ZnMatrix A = new ZnMatrix(1, dimension, modulus);
        A.set(0, index, 1);
        return ZnFreeAffineMorphism.make(A, new int[] { 0 });
    }

    private static ModuleMorphism getInjectionModular(int index, int dimension, int modulus) {
        ZnMatrix A = new ZnMatrix(dimension, 1, modulus);
        A.set(index, 0, 1);
        int[] b = new int[dimension];
        for (int i = 0; i < dimension; i++) {
            b[i] = 0;
        }
        return ZnFreeAffineMorphism.make(A, b);
    }

    private static ModuleMorphism getProjectionRational(int index, int dimension) {
        QMatrix A = new QMatrix(1, dimension);
        A.set(0, index, Rational.getOne());
        return QFreeAffineMorphism.make(A, new Rational[] { Rational.getZero() });
    }

    private static ModuleMorphism getInjectionRational(int index, int dimension) {
        QMatrix A = new QMatrix(dimension, 1);
        A.set(index, 0, Rational.getOne());
        Rational[] b = new Rational[dimension];
        for (int i = 0; i < dimension; i++) {
            b[i] = Rational.getZero();
        }
        return QFreeAffineMorphism.make(A, b);
    }

    private static ModuleMorphism getProjectionReal(int index, int dimension) {
        RMatrix A = new RMatrix(1, dimension);
        A.set(0, index, 1);
        return RFreeAffineMorphism.make(A, new double[] { 0.0 });
    }

    private static ModuleMorphism getInjectionReal(int index, int dimension) {
        RMatrix A = new RMatrix(dimension, 1);
        A.set(index, 0, 1.0);
        double[] b = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            b[i] = 0.0;
        }
        return RFreeAffineMorphism.make(A, b);
    }

}
