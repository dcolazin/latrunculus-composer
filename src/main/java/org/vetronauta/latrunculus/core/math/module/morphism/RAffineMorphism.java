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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;

/**
 * Affine morphism in <i>R</i>.
 * The morphism <i>h</i> is such that <i>h(x) = a*x+b</i>
 * where <i>a</i> and <i>b</i> are reals.
 * 
 * @author Gérard Milmeister
 */
public final class RAffineMorphism extends RAbstractMorphism {

    public RAffineMorphism(double a, double b) {
        super();
        this.a = a;
        this.b = b;
    }

    
    public double mapValue(double x) {
        return a*x+b;
    }

    
    public boolean isModuleHomomorphism() {
        return true;
    }

    
    public boolean isRingHomomorphism() {
        return (b == 0) && (a == 1 || a == 0);
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
        if (morphism instanceof RAffineMorphism) {
            RAffineMorphism rmorphism = (RAffineMorphism) morphism;
            return new RAffineMorphism(a * rmorphism.a, a * rmorphism.b + b);
        }
        else {
            return super.compose(morphism);
        }
    }

    
    public ModuleMorphism sum(ModuleMorphism morphism)
    		throws CompositionException {
        if (morphism instanceof RAffineMorphism) {
            RAffineMorphism rmorphism = (RAffineMorphism) morphism;
            return new RAffineMorphism(a + rmorphism.a, b + rmorphism.b);
        }
        else {
            return super.sum(morphism);
        }
    }

    
    public ModuleMorphism difference(ModuleMorphism morphism)
    		throws CompositionException {
        if (morphism instanceof RAffineMorphism) {
            RAffineMorphism rmophism = (RAffineMorphism)morphism;
            return new RAffineMorphism(a - rmophism.a, b - rmophism.b);
        }
        else {
            return super.difference(morphism);
        }
    }

    
    public ModuleMorphism scaled(RingElement element)
        throws CompositionException {
        if (element instanceof ArithmeticElement) {
            ArithmeticNumber<?> number = ((ArithmeticElement<?>) element).getValue();
            if (number instanceof ArithmeticDouble) {
                if (number.doubleValue() == 0.0) {
                    return getConstantMorphism(element);
                } else {
                    return new RAffineMorphism(getA() * number.doubleValue(), getB() * number.doubleValue());
                }
            }
        }
        throw new CompositionException("RAffineMorphism.scaled: Cannot scale "+this+" by "+element);
    }


    public ArithmeticElement<ArithmeticDouble> atZero() {
        return new ArithmeticElement<>(new ArithmeticDouble(getB()));
    }


    public int compareTo(ModuleMorphism object) {
        if (object instanceof RAffineMorphism) {
            RAffineMorphism morphism = (RAffineMorphism)object;
            if (a == morphism.a) {
                if (b < morphism.b) {
                    return -1;
                }
                else if (b > morphism.b) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
            else if (a < morphism.a) {
                return -1;
            }
            else {
                return 1;
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public boolean equals(Object object) {
        if (object instanceof RAffineMorphism) {
            RAffineMorphism morphism = (RAffineMorphism)object;
            return a == morphism.a && b == morphism.b;
        }
        else {
            return false;
        }
    }
    
    public String toString() {
        return "RAffineMorphism["+a+","+b+"]";
    }


    public String getElementTypeName() {
        return "RAffineMorphism";
    }
    
    
    /**
     * Returns the linear part.
     */
    public double getA() {
        return a;
    }
    

    /**
     * Returns the translation part.
     */
    public double getB() {
        return b;
    }
    
    
    private double a;
    private double b;
}
