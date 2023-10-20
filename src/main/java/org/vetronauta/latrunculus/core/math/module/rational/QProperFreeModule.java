/*
 * Copyright (C) 2001, 2005 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.math.module.rational;

import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.matrix.QMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.QFreeAffineMorphism;

/**
 * Free modules over rationals.
 * @see QProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class QProperFreeModule extends ArithmeticMultiModule<Rational> {

    public static final QProperFreeModule nullModule = new QProperFreeModule(0);

    protected ModuleMorphism _getProjection(int index) {
        QMatrix A = new QMatrix(1, getDimension());
        A.set(0, index, Rational.getOne());
        return QFreeAffineMorphism.make(A, new Rational[] { Rational.getZero() });
    }

    protected ModuleMorphism _getInjection(int index) {
        QMatrix A = new QMatrix(getDimension(), 1);
        A.set(index, 0, Rational.getOne());
        Rational[] b = new Rational[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            b[i] = Rational.getZero();
        }
        return QFreeAffineMorphism.make(A, b);
    }

    private QProperFreeModule(int dimension) {
        super(QRing.ring, dimension);
    }

}
