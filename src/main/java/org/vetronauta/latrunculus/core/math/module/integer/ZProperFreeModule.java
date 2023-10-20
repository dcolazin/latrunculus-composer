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

package org.vetronauta.latrunculus.core.math.module.integer;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.matrix.ZMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZFreeAffineMorphism;

/**
 * Free modules over integers.
 * @see ZProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class ZProperFreeModule extends ArithmeticMultiModule<ArithmeticInteger> {

    public static final ZProperFreeModule nullModule = new ZProperFreeModule(0);

    public int compareTo(Module object) {
        if (object instanceof ZProperFreeModule) {
            ZProperFreeModule module = (ZProperFreeModule)object;
            return getDimension()-module.getDimension();
        }
        else {
            return super.compareTo(object);
        }
    }
    
    public boolean equals(Object object) {
        return (object instanceof ZProperFreeModule &&
                getDimension() == ((ZProperFreeModule)object).getDimension());
    }

    public String toString() {
        return "ZFreeModule["+getDimension()+"]";
    }

    public String getElementTypeName() {
        return "ZFreeModule";
    }
    
    public int hashCode() {
        return 37*basicHash + getDimension();
    }
    
    
    protected ModuleMorphism _getProjection(int index) {
        ZMatrix A = new ZMatrix(1, getDimension());
        A.set(0, index, 1);
        return ZFreeAffineMorphism.make(A, new int[] { 0 });
    }
    
    
    protected ModuleMorphism _getInjection(int index) {
        ZMatrix A = new ZMatrix(getDimension(), 1);
        A.set(index, 0, 1);
        int[] b = new int[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            b[i] = 0;
        }
        return ZFreeAffineMorphism.make(A, b);
    }
    
    
    private ZProperFreeModule(int dimension) {
        super(ZRing.ring, dimension);
    }


    private static final int basicHash = "ZFreeModule".hashCode();
}
