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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.matrix.CMatrix;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineRingMorphism;

import java.util.Arrays;

/**
 * Affine morphism in a free <i>C</i>-module.
 * The morphism h is such that <i>h(x) = A*x+b</i> where <i>A</i>
 * is a complex matrix and <i>b</i> is a complex vector.
 * 
 * @author Gérard Milmeister
 */
public final class CFreeAffineMorphism extends CFreeAbstractMorphism {

    public static ModuleMorphism make(CMatrix A, Complex[] b) {
        if (A.getColumnCount() == 1 && A.getRowCount() == 1 && b.length == 1) {
            return new ArithmeticAffineRingMorphism<>(CRing.ring, new ArithmeticElement<>(A.get(0, 0)), new ArithmeticElement<>(b[0]));
        }
        else {
            return new CFreeAffineMorphism(A, b);
        }
    }

    
    private CFreeAffineMorphism(CMatrix A, Complex[] b) {
        super(A.getColumnCount(), A.getRowCount());
        if (A.getRowCount() != b.length) {
            throw new IllegalArgumentException("Rows of A don't match length of b.");
        }
        this.A = A;
        this.b = b;
    }

    
    public Complex[] mapValue(Complex[] rv) {
        Complex[] res;
        res = A.product(rv);
        for (int i = 0; i < res.length; i++) {
            res[i] = res[i].sum(b[i]);
        }
        return res;
    }

    
    public boolean isModuleHomomorphism() {
        return true;
    }
    
    
    public boolean isLinear() {
        for (int i = 0; i < b.length; i++) {
            if (!b[i].isZero()) {
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
        if (morphism instanceof CFreeAffineMorphism) {
            CFreeAffineMorphism qm = (CFreeAffineMorphism)morphism;
            if (getDomain().getDimension() == qm.getCodomain().getDimension()) {
                CMatrix resA = A.product(qm.A);
                Complex[] resb = A.product(qm.b);
                for (int i = 0; i < resb.length; i++) {
                    resb[i] = resb[i].sum(b[i]);
                }
                return new CFreeAffineMorphism(resA, resb);
            }
        }
        return super.compose(morphism);
    }

    
    public ModuleMorphism sum(ModuleMorphism morphism)
    		throws CompositionException {
        if (morphism instanceof CFreeAffineMorphism) {
            CFreeAffineMorphism qm = (CFreeAffineMorphism) morphism;
            if (getDomain().getDimension() == qm.getDomain().getDimension() &&
                    getCodomain().getDimension() == qm.getCodomain().getDimension()) {
                CMatrix resA = A.sum(qm.A);
                Complex[] resb = new Complex[b.length];
                for (int i = 0; i < resb.length; i++) {
                    resb[i] = b[i].sum(qm.b[i]);
                }
                return new CFreeAffineMorphism(resA, resb);
            }
        }
        return super.sum(morphism);
    }

    
    public ModuleMorphism difference(ModuleMorphism morphism)
        	throws CompositionException {
        if (morphism instanceof CFreeAffineMorphism) {
            CFreeAffineMorphism qm = (CFreeAffineMorphism) morphism;
            if (getDomain().getDimension() == qm.getDomain().getDimension() &&
                    getCodomain().getDimension() == qm.getCodomain().getDimension()) {
                CMatrix resA = A.difference(qm.A);
                Complex[] resb = new Complex[b.length];
                for (int i = 0; i < resb.length; i++) {
                    resb[i] = b[i].difference(qm.b[i]);
                }
                return new CFreeAffineMorphism(resA, resb);
            }
        }
        return super.difference(morphism);
    }


    public ModuleMorphism scaled(RingElement element) throws CompositionException {
        if (element instanceof ArithmeticElement) {
            ArithmeticNumber<?> number = ((ArithmeticElement<?>) element).getValue();
            if (number instanceof Complex) {
                if (number.isZero()) {
                    return getConstantMorphism(getCodomain().getZero());
                } else {
                    Complex[] oldv = getVector();
                    Complex[] newv = new Complex[oldv.length];
                    for (int i = 0; i < oldv.length; i++) {
                        newv[i] = oldv[i].product((Complex) number);
                    }
                    return new CFreeAffineMorphism(getMatrix().scaled((Complex) number), newv);
                }
            }
        }
        throw new CompositionException("CAffineMorphism.scaled: Cannot scale "+this+" by "+element);
    }


    public ModuleElement atZero() {
        return ArithmeticMultiElement.make(CRing.ring, getVector());
    }


    public int compareTo(ModuleMorphism object) {
        if (object instanceof CFreeAffineMorphism) {
            CFreeAffineMorphism morphism = (CFreeAffineMorphism)object;
            int comp = A.compareTo(morphism.A);
            if (comp == 0) {
                for (int i = 0; i < b.length; i++) {
                    int comp1 = b[i].compareTo(morphism.b[i]);
                    if (comp1 != 0) {
                        return comp1;
                    }
                }
                return 0;
            }
            else
                return comp;
        }
        else {
            return super.compareTo(object);
        }
    }
    
    
    public boolean equals(Object object) {
        if (object instanceof CFreeAffineMorphism) {
            CFreeAffineMorphism morphism = (CFreeAffineMorphism)object;
            return A.equals(morphism.A) && Arrays.equals(b, morphism.b);
        }
        else {
            return false;
        }
    }
    
    
    public String toString() {
        return "CFreeAffineMorphism["+getDomain().getDimension()+","+getCodomain().getDimension()+"]";
    }

    public String getElementTypeName() {
        return "CFreeAffineMorphism";
    }
    
    
    /**
     * Returns the linear part.
     */
    public CMatrix getMatrix() {
        return A;
    }
    

    /**
     * Returns the translation part. 
     */
    public Complex[] getVector() {
        return b;
    }
    
    
    private CMatrix   A;
    private Complex[] b;
}
