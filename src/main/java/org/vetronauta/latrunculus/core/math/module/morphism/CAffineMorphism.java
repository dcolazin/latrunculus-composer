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
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;

/**
 * Affine morphism in <i>C</i>.
 * The morphism <code>h</code> is such that <i>h(x) = a*x+b</i>
 * where <code>a</code> and <code>b</code> are complex numbers.
 * 
 * @author Gérard Milmeister
 */
public final class CAffineMorphism extends CAbstractMorphism {

    /**
     * Constructs an affine morphism <i>h(x) = a*x+b</i>.
     */
    public CAffineMorphism(Complex a, Complex b) {
        super();
        this.a = a;
        this.b = b;
    }

    
    public Complex mapValue(Complex c) {
        return a.product(c).sum(b);
    }

    
    public boolean isModuleHomomorphism() {
        return true;
    }

    
    public boolean isRingHomomorphism() {
        return b.isZero() && (a.isOne() || b.isZero());
    }
    
    
    public boolean isLinear() {
        return b.isZero();
    }
    
    
    public boolean isIdentity() {
        return a.isOne() && b.isZero();
    }

    
    public boolean isConstant() {
        return a.isZero();
    }
    
    
    public ModuleMorphism compose(ModuleMorphism morphism)
        	throws CompositionException {
        if (morphism instanceof CAffineMorphism) {
            CAffineMorphism rm = (CAffineMorphism)morphism;
            return new CAffineMorphism(a.product(rm.a), a.product(rm.b).sum(b));
        }
        else {
            return super.compose(morphism);
        }
    }

    
    public ModuleMorphism sum(ModuleMorphism morphism)
    		throws CompositionException {
        if (morphism instanceof CAffineMorphism) {
            CAffineMorphism rm = (CAffineMorphism) morphism;
            return new CAffineMorphism(a.sum(rm.a), b.sum(rm.b));
        }
        else {
            return super.sum(morphism);
        }
    }
    

    public ModuleMorphism difference(ModuleMorphism morphism)
        	throws CompositionException {
        if (morphism instanceof CAffineMorphism) {
            CAffineMorphism rm = (CAffineMorphism) morphism;
            return new CAffineMorphism(a.difference(rm.a),b.difference(rm.b));
        }
        else {
            return super.difference(morphism);
        }
    }

    
    public ModuleMorphism scaled(RingElement element) throws CompositionException {
        if (element instanceof ArithmeticElement) {
            ArithmeticNumber<?> number = ((ArithmeticElement<?>) element).getValue();
            if (number instanceof Complex) {
                if (number.isZero()) {
                    return getConstantMorphism(element);
                } else {
                    return new CAffineMorphism(getA().product((Complex) number), getB().product((Complex) number));
                }
            }
        }
        throw new CompositionException("CAffineMorphism.scaled: Cannot scale "+this+" by "+element);
    }

    
    public ArithmeticElement<Complex> atZero() {
        return new ArithmeticElement<>(getB());
    }
    
    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof CAffineMorphism) {
            CAffineMorphism morphism = (CAffineMorphism)object;
            int comp = a.compareTo(morphism.a);
            if (comp == 0) {
                return b.compareTo(morphism.b);
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
        if (object instanceof CAffineMorphism) {
            CAffineMorphism morphism = (CAffineMorphism)object;
            return a.equals(morphism.a) && b.equals(morphism.b);
        }
        else {
            return false;
        }
    }
    
    
    public String toString() {
        return "CAffineMorphism["+a+","+b+"]";
    }

    public String getElementTypeName() {
        return "CAffineMorphism";
    }

    
    /**
     * Returns the linear part.
     */
    public Complex getA() {
        return a;
    }
    

    /**
     * Returns the translation part.
     */
    public Complex getB() {
        return b;
    }
    
    
    private Complex a;
    private Complex b;
}
