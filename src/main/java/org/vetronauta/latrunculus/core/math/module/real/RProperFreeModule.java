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

package org.vetronauta.latrunculus.core.math.module.real;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.RFreeAffineMorphism;

/**
 * Free modules over real numbers.
 * @see RProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class RProperFreeModule extends ArithmeticMultiModule<ArithmeticDouble> {

    public static final RProperFreeModule nullModule = new RProperFreeModule(0);

    protected ModuleMorphism _getProjection(int index) {
        RMatrix A = new RMatrix(1, getDimension());
        A.set(0, index, 1);
        return RFreeAffineMorphism.make(A, new double[] { 0.0 });
    }
    
    protected ModuleMorphism _getInjection(int index) {
        RMatrix A = new RMatrix(getDimension(), 1);
        A.set(index, 0, 1.0);
        double[] b = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            b[i] = 0.0;
        }
        return RFreeAffineMorphism.make(A, b);
    }

    private RProperFreeModule(int dimension) {
        super(RRing.ring, dimension);
    }

}
