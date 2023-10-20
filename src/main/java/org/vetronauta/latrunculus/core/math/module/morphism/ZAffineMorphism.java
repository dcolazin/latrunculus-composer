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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;

/**
 * Affine morphism in the <i>Z</i>.
 * The morphism h is such that <i>h(x) = a*x+b</i>
 * where <i>a</i> and <i>b</i> are integers.
 * 
 * @author Gérard Milmeister
 */
public final class ZAffineMorphism extends ZAbstractMorphism {

    /**
     * Creates an affine morphism <i>h(x) = a*x+b</i>.
     */
    public ZAffineMorphism(int a, int b) {
        super();
        this.a = a;
        this.b = b;
    }

    
    public int mapValue(int x) {
        return a*x+b;
    }

    
    public boolean isModuleHomomorphism() {
        return true;
    }

    
    public boolean isRingHomomorphism() {
        return b == 0 && (a == 1 || a == 0);
    }
    
    
    public boolean isLinear() {
        return b == 0;
    }
    
    
    public boolean isIdentity() {
        return (a == 1) && (b == 0);
    }

    
    public boolean isConstant() {
        return a == 0;
    }
    
    
    public ModuleMorphism compose(ModuleMorphism morphism)
        	throws CompositionException {
        if (morphism instanceof ZAffineMorphism) {
            ZAffineMorphism zmorphism = (ZAffineMorphism) morphism;
            return new ZAffineMorphism(a * zmorphism.a, a * zmorphism.b + b);
        }
        return super.compose(morphism);
    }

    
    public ModuleMorphism sum(ModuleMorphism morphism)
    		throws CompositionException {
        if (morphism instanceof ZAffineMorphism) {
            ZAffineMorphism zmorphism = (ZAffineMorphism)morphism;
            return new ZAffineMorphism(a + zmorphism.a, b + zmorphism.b);
        }
        return super.sum(morphism);
    }

    
    public ModuleMorphism difference(ModuleMorphism morphism)
        	throws CompositionException {
        if (morphism instanceof ZAffineMorphism) {
            ZAffineMorphism zmorphism = (ZAffineMorphism) morphism;
            return new ZAffineMorphism(a - zmorphism.a, b - zmorphism.b);
        }
        return super.difference(morphism);
    }
    

    public ModuleMorphism scaled(RingElement element)
            throws CompositionException {
        if (element instanceof ArithmeticElement) {
            ArithmeticNumber<?> number = ((ArithmeticElement<?>) element).getValue();
            if (number instanceof ArithmeticInteger) {
                int s = number.intValue();
                if (s == 0) {
                    return getConstantMorphism(element);
                } else {
                    return new ZAffineMorphism(getA() * s, getB() * s);
                }
            }
        }
        throw new CompositionException("ZAffineMorphism.scaled: Cannot scale "+this+" by "+element);
    }
    
    
    public ArithmeticElement<ArithmeticInteger> atZero() {
        return new ArithmeticElement<>(new ArithmeticInteger(getB()));
    }
    
    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ZAffineMorphism) {
            ZAffineMorphism morphism = (ZAffineMorphism)object;
            if (a == morphism.a) {
                return b-morphism.b;
            }
            else {
                return a-morphism.a;
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public boolean equals(Object object) {
        if (object instanceof ZAffineMorphism) {
            ZAffineMorphism morphism = (ZAffineMorphism)object;
            return (a == morphism.a && b == morphism.b);
        }
        else {
            return false;
        }
    }
        
    
    public String toString() {
        return "ZAffineMorphism["+a+","+b+"]";
    }

    public String getElementTypeName() {
        return "ZAffineMorphism";
    }
    
    
    /**
     * Returns the linear part.
     */
    public int getA() {
        return a;
    }
    

    /**
     * Returns the translation part.
     */
    public int getB() {
        return b;
    }
    
    
    private final int a;
    private final int b;
}
