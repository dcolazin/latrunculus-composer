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

package org.vetronauta.latrunculus.core.math.module.modular;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.matrix.ZnMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRingRepository;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZnFreeAffineMorphism;

/**
 * Free modules over integers mod <i>n</i>.
 * @see ZnProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class ZnProperFreeModule extends ArithmeticMultiModule<ArithmeticModulus> implements Modular {

    public int getModulus() {
        return modulus;
    }

    protected ModuleMorphism _getProjection(int index) {
        ZnMatrix A = new ZnMatrix(1, getDimension(), getModulus());
        A.set(0, index, 1);
        return ZnFreeAffineMorphism.make(A, new int[] { 0 });
    }
    
    protected ModuleMorphism _getInjection(int index) {
        ZnMatrix A = new ZnMatrix(getDimension(), 1, getModulus());
        A.set(index, 0, 1);
        int[] b = new int[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            b[i] = 0;
        }
        return ZnFreeAffineMorphism.make(A, b);
    }
    
    
    private ZnProperFreeModule(int dimension, int modulus) {
        super(ArithmeticRingRepository.getModulusRing(modulus), dimension);
        this.modulus = modulus;
    }

    private final int    modulus;

}
