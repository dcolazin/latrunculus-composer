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

package org.vetronauta.latrunculus.core.math.module.complex;

import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.matrix.CMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.morphism.CFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

/**
 * The free modules over complex numbers.
 * @see CProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class CProperFreeModule extends ArithmeticMultiModule<Complex> {

    public static final CProperFreeModule nullModule = new CProperFreeModule(0);

    public int compareTo(Module object) {
        if (object instanceof CProperFreeModule) {
            CProperFreeModule module = (CProperFreeModule)object;
            return getDimension()-module.getDimension();
        }
        else {
            return super.compareTo(object);
        }
    }

    public boolean equals(Object object) {
        return (object instanceof CProperFreeModule &&
                getDimension() == ((CProperFreeModule)object).getDimension());
    }

    public String toString() {
        return "CFreeModule["+getDimension()+"]";
    }
    
    public String getElementTypeName() {
        return "CFreeModule";
    }

    public int hashCode() {
        return 37*basicHash + getDimension();
    }

    protected ModuleMorphism _getProjection(int index) {
        CMatrix A = new CMatrix(1, getDimension());
        A.set(0, index, Complex.getOne());
        return CFreeAffineMorphism.make(A, new Complex[] { Complex.getZero() });
    }

    protected ModuleMorphism _getInjection(int index) {
        CMatrix A = new CMatrix(getDimension(), 1);
        A.set(index, 0, Complex.getOne());
        Complex[] b = new Complex[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            b[i] = Complex.getZero();
        }
        return CFreeAffineMorphism.make(A, b);
    }

    private CProperFreeModule(int dimension) {
        super(CRing.ring, dimension);
    }
    
    private static final int basicHash = "CFreeModule".hashCode();
}
