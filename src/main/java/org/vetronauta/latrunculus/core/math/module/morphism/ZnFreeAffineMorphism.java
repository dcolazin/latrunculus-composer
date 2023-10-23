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

import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.matrix.ZnMatrix;

import java.util.Arrays;

/**
 * Affine morphism in a free <i>Zn</i>-module.
 * The morphism <i>h</i> is such that <i>h(x) = A*x+b mod n</i>
 * where </i>A</i> is an integer matrix and <i>b</i> is an integer vector.
 * 
 * @author Gérard Milmeister
 */
public final class ZnFreeAffineMorphism extends ZnFreeAbstractMorphism {

    public static ModuleMorphism make(ZnMatrix A, int[] b) {
        if (A.getColumnCount() == 1 && A.getRowCount() == 1 && b.length == 1) {
            return new ZnAffineMorphism(A.get(0, 0), b[0], A.getModulus());
        }
        else {
            return new ZnFreeAffineMorphism(A, b);
        }
    }

    
    private ZnFreeAffineMorphism(ZnMatrix A, int[] b) {
        super(A.getColumnCount(), A.getRowCount(), A.getModulus());
        if (A.getRowCount() != b.length) {
            throw new IllegalArgumentException("Rows of A don't match length of b");
        }
        this.A = A;
        this.b = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            this.b[i] = NumberTheory.mod(b[i], getModulus());
        }
    }

    
    public int[] mapValue(int[] x) {
        int[] res;
        res = A.product(x);
        for (int i = 0; i < res.length; i++) {
            res[i] = NumberTheory.mod(res[i] + b[i], getModulus());
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
        if (morphism instanceof ZnFreeAffineMorphism) {
            ZnFreeAffineMorphism zpm = (ZnFreeAffineMorphism) morphism;
            if (getModulus() == zpm.getModulus()) {
                if (getDomain().getDimension() == zpm.getCodomain().getDimension()) {
                    ZnMatrix resA = A.product(zpm.A);
                    int[] resb = A.product(zpm.b);
                    for (int i = 0; i < resb.length; i++) {
                        resb[i] = NumberTheory.mod(resb[i] + b[i], getModulus());
                    }
                    return new ZnFreeAffineMorphism(resA, resb);
                }
            }
        }
        return super.compose(morphism);
    }
    

    public ModuleMorphism sum(ModuleMorphism morphism)
    		throws CompositionException {
        if (morphism instanceof ZnFreeAffineMorphism) {
            ZnFreeAffineMorphism zpm = (ZnFreeAffineMorphism) morphism;
            if (getModulus() == zpm.getModulus()) {
                if (getDomain().getDimension() == zpm.getDomain().getDimension() &&
                        getCodomain().getDimension() == zpm.getCodomain().getDimension()) {
                    ZnMatrix resA = A.sum(zpm.A);
                    int[] resb = new int[b.length];
                    for (int i = 0; i < resb.length; i++) {
                        resb[i] = NumberTheory.mod(b[i] + zpm.b[i], getModulus());
                    }
                    return new ZnFreeAffineMorphism(resA, resb);
                }
            }
        }
        return super.sum(morphism);
    }

    
    public ModuleMorphism difference(ModuleMorphism morphism)
        	throws CompositionException {
        if (morphism instanceof ZnFreeAffineMorphism) {
            ZnFreeAffineMorphism zpm = (ZnFreeAffineMorphism) morphism;
            if (getModulus() == zpm.getModulus()) {
                if (getDomain().getDimension() == zpm.getDomain().getDimension() &&
                        getCodomain().getDimension() == zpm.getCodomain().getDimension()) {
                    ZnMatrix resA = A.sum(zpm.A);
                    int[] resb = new int[b.length];
                    for (int i = 0; i < resb.length; i++) {
                        resb[i] = NumberTheory.mod(b[i] - zpm.b[i], getModulus());
                    }
                    return new ZnFreeAffineMorphism(resA, resb);
                }
            }
        }
        return super.difference(morphism);
    }

    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ZnFreeAffineMorphism) {
            ZnFreeAffineMorphism m = (ZnFreeAffineMorphism)object;
            int res = A.compareTo(m.A);
            if (res == 0) {
                for (int i = 0; i < b.length; i++) {
                    int d = b[i]-m.b[i];
                    if (d != 0) {
                        return d;
                    }
                }
                return 0;
            }
            else {
                return res;
            }
        }
        else {
            return super.compareTo(object);
        }
    }
    
    
    public boolean equals(Object object) {
        if (object instanceof ZnFreeAffineMorphism) {
            ZnFreeAffineMorphism m = (ZnFreeAffineMorphism)object;
            return A.equals(m.A) && Arrays.equals(b, m.b);
        }
        else {
            return false;
        }
    }
    
    
    public String toString() {
        return "ZnFreeAffineMorphism["+getModulus()+"]["
               +getDomain().getDimension()+","
               +getCodomain().getDimension()+"]";
    }

    public String getElementTypeName() {
        return "ZnFreeAffineMorphism";
    }
    
    
    /**
     * Returns the linear part.
     */
    public ZnMatrix getMatrix() {
        return A;
    }
    

    /**
     * Returns the translation part.
     */
    public int[] getVector() {
        return b;
    }
    
    
    private ZnMatrix A;
    private int[]    b;
}
