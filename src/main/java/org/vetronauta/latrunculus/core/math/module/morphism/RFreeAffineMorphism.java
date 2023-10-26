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

import org.vetronauta.latrunculus.core.math.arith.number.Real;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineRingMorphism;
import org.vetronauta.latrunculus.core.math.module.real.RRing;

import java.util.Arrays;

/**
 * Affine morphism in a free <i>R</i>-module.
 * The morphism <i>h</i> is such that <i>h(x) = A*x+b</i> where <i>A</i>
 * is a real matrix and <i>b</i> is a real vector.
 * 
 * @author Gérard Milmeister
 */
public final class RFreeAffineMorphism extends RFreeAbstractMorphism {

    public static ModuleMorphism make(RMatrix A, double[] b) {
        if (A.getColumnCount() == 1 && A.getRowCount() == 1 && b.length == 1) {
            return new ArithmeticAffineRingMorphism<>(RRing.ring, new ArithmeticElement<>(new Real(A.get(0, 0))), new ArithmeticElement<>(new Real(b[0])));
        }
        else {
            return new RFreeAffineMorphism(A, b);
        }
    }

    
    private RFreeAffineMorphism(RMatrix A, double[] b) {
        super(A.getColumnCount(), A.getRowCount());
        if (A.getRowCount() != b.length) {
            String s = "Rows of A ("+A.getRowCount()+") don't match length of b ("
                       +b.length+")";
            throw new IllegalArgumentException(s);
        }
        this.A = A;
        this.b = b;
    }

    
    public double[] mapValue(double[] x) {
        double[] res;
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
        if (morphism instanceof RFreeAffineMorphism) {
            RFreeAffineMorphism rmorphism = (RFreeAffineMorphism) morphism;
            if (getDomain().getDimension() == rmorphism.getCodomain().getDimension()) {
                RMatrix resA = A.product(rmorphism.A);
                double[] resb = A.product(rmorphism.b);
                for (int i = 0; i < resb.length; i++) {
                    resb[i] += b[i];
                }
                return new RFreeAffineMorphism(resA, resb);
            }
        }
        return super.compose(morphism);
    }

    
    public ModuleMorphism sum(ModuleMorphism morphism)
    		throws CompositionException {
        if (morphism instanceof RFreeAffineMorphism) {
            RFreeAffineMorphism rmorphism = (RFreeAffineMorphism) morphism;
            if (getDomain().getDimension() == rmorphism.getDomain().getDimension() &&
                    getCodomain().getDimension() == rmorphism.getCodomain().getDimension()) {
                RMatrix resA = A.sum(rmorphism.A);
                double[] resb = new double[b.length];
                for (int i = 0; i < resb.length; i++) {
                    resb[i] = b[i] + rmorphism.b[i];
                }
                return new RFreeAffineMorphism(resA, resb);
            }
        }
        return super.sum(morphism);
    }

    
    public ModuleMorphism difference(ModuleMorphism morphism)
    		throws CompositionException {
        if (morphism instanceof RFreeAffineMorphism) {
            RFreeAffineMorphism rmorphism = (RFreeAffineMorphism) morphism;
            if (getDomain().getDimension() == rmorphism.getCodomain().getDimension() &&
                    getCodomain().getDimension() == rmorphism.getCodomain().getDimension()) {
                RMatrix resA = A.difference(rmorphism.A);
                double[] resb = new double[b.length];
                for (int i = 0; i < resb.length; i++) {
                    resb[i] = b[i] - rmorphism.b[i];
                }
                return new RFreeAffineMorphism(resA, resb);
            }
        }
        return super.difference(morphism);
    }

    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof RFreeAffineMorphism) {
            RFreeAffineMorphism m = (RFreeAffineMorphism)object;
            int comp = A.compareTo(m.A);
            if (comp == 0) {
                for (int i = 0; i < b.length; i++) {
                    if (b[i] < m.b[i]) {
                        return -1;
                    }
                    else if (b[i] > m.b[i]) {
                        return 1;            
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
        if (object instanceof RFreeAffineMorphism) {
            RFreeAffineMorphism morphism = (RFreeAffineMorphism)object;
            return A.equals(morphism.A) && Arrays.equals(b, morphism.b);
        }
        else {
            return false;
        }
    }

    public String getElementTypeName() {
        return "RFreeAffineMorphism";
    }
    
    
    public String toString() {
        return "RFreeAffineMorphism["+getDomain().getDimension()+","+getCodomain().getDimension()+"]";
    }

    /**
     * Returns the linear part.
     */
    public RMatrix getMatrix() {
        return A;
    }
    

    /**
     * Returns the translation part.
     */
    public double[] getVector() {
        return b;
    }
    
    
    private RMatrix  A;
    private double[] b;
}
