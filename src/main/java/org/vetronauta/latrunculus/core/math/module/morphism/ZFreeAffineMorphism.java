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

package org.vetronauta.latrunculus.core.math.module.morphism;

import org.vetronauta.latrunculus.core.math.matrix.ZMatrix;

import java.util.Arrays;

/**
 * Affine morphism in a free <i>Z</i> module.
 * The morphism <i>h</i> is such that <i>h(x) = A*x+b</i>
 * where <i>A</i> is an integer matrix and <i>b</i> is an integer vector.
 * 
 * @author Gérard Milmeister
 */
public final class ZFreeAffineMorphism extends ZFreeAbstractMorphism {

    public static ModuleMorphism make(ZMatrix A, int[] b) {
        if (A.getColumnCount() == 1 && A.getRowCount() == 1 && b.length == 1) {
            return new ZAffineMorphism(A.get(0, 0), b[0]);
        }
        else {
            return new ZFreeAffineMorphism(A, b);
        }
    }

    
    private ZFreeAffineMorphism(ZMatrix A, int[] b) {
        super(A.getColumnCount(), A.getRowCount());
        if (A.getRowCount() != b.length) {
            throw new IllegalArgumentException("Rows of A ("
                                        	   +A.getRowCount()
                    		                   +") don't match length of b ("
                    		                   +b.length
                    		                   +")");
        }
        this.A = A;
        this.b = b;
    }


    public int[] mapValue(int[] x) {
        int[] res;
        res = A.product(x);
        for (int i = 0; i < res.length; i++) {
            res[i] += b[i];
        }
        return res;
    }

    
    public boolean isModuleHomomorphism() {
        return true;
    }

    
    public boolean isLinear() {
        for (int i = 0; i < b.length; i++) {
            if (b[i] != 0) {
                return false;
            }
        }
        return true;
    }

    
    public boolean isIdentity() {
        return isLinear() && A.isUnit();
    }

    
    public boolean isConstant() {
        return A.isZero();
    }
    
    
    public ModuleMorphism compose(ModuleMorphism morphism)
        	throws CompositionException {
        if (morphism instanceof ZFreeAffineMorphism) {
            ZFreeAffineMorphism zmorphism = (ZFreeAffineMorphism) morphism;
            if (getDomain().getDimension() == zmorphism.getCodomain().getDimension()) {
                ZMatrix resA = A.product(zmorphism.A);
                int[] resb = A.product(zmorphism.b);
                for (int i = 0; i < resb.length; i++) {
                    resb[i] += b[i];
                }
                return new ZFreeAffineMorphism(resA, resb);
            }
        }
        return super.compose(morphism);
    }

    
    public ModuleMorphism sum(ModuleMorphism morphism)
    		throws CompositionException {
        if (morphism instanceof ZFreeAffineMorphism) {
            ZFreeAffineMorphism zmorphism = (ZFreeAffineMorphism)morphism;
            if (getDomain().getDimension() == zmorphism.getDomain().getDimension() &&
                    getCodomain().getDimension() == zmorphism.getCodomain().getDimension()) {
                ZMatrix resA = A.sum(zmorphism.A);
                int[] resb = new int[b.length];
                for (int i = 0; i < resb.length; i++) {
                    resb[i] = b[i] + zmorphism.b[i];
                }
                return new ZFreeAffineMorphism(resA, resb);
            }
        }
        return super.sum(morphism);
    }
    

    public ModuleMorphism difference(ModuleMorphism morphism)
        	throws CompositionException {
        if (morphism instanceof ZFreeAffineMorphism) {
            ZFreeAffineMorphism zmorphism = (ZFreeAffineMorphism) morphism;
            if (getDomain().getDimension() == zmorphism.getDomain().getDimension() &&
                    getCodomain().getDimension() == zmorphism.getCodomain().getDimension()) {
                ZMatrix resA = A.sum(zmorphism.A);
                int[] resb = new int[b.length];
                for (int i = 0; i < resb.length; i++) {
                    resb[i] = b[i] - zmorphism.b[i];
                }
                return new ZFreeAffineMorphism(resA, resb);
            }
        }
        return super.difference(morphism);
    }

    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ZFreeAffineMorphism) {
            ZFreeAffineMorphism morphism = (ZFreeAffineMorphism)object;
            int comp = A.compareTo(morphism.A);
            if (comp == 0) {
                for (int i = 0; i < b.length; i++) {
                    int d = b[i]-morphism.b[i];
                    if (d != 0) {
                        return d;
                    }
                }
                return 0;
            }
            else {
                return comp;
            }
        }
        else {
            return super.compareTo(object);
        }
    }
    
    
    public boolean equals(Object object) {
        if (object instanceof ZFreeAffineMorphism) {
            ZFreeAffineMorphism morphism = (ZFreeAffineMorphism)object;
            return A.equals(morphism.A) && Arrays.equals(b, morphism.b);
        }
        else {
            return false;
        }
    }
    
    public String toString() {
        return "ZFreeAffineMorphism["+getDomain().getDimension()+","+getCodomain().getDimension()+"]";
    }

    public String getElementTypeName() {
        return "ZFreeAffineMorphism";
    }
    
    
    /**
     * Returns the linear part.
     */
    public ZMatrix getMatrix() {
        return A;
    }
    

    /**
     * Returns the translation part.
     */
    public int[] getVector() {
        return b;
    }
    
    
    private final ZMatrix A;
    private final int[] b;
}
