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

import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.rational.QElement;

/**
 * Affine morphism in <i>Q</i>.
 * The morphism h is such that h(x) = a*x+b where a and b are rationals.
 * 
 * @author Gérard Milmeister
 */
public class QAffineMorphism extends QAbstractMorphism {

    public QAffineMorphism(Rational a, Rational b) {
        super();
        this.a = a;
        this.b = b;
    }

    
    public Rational mapValue(Rational r) {
        return r.product(a).sum(b);
    }

    
    public boolean isModuleHomomorphism() {
        return true;
    }
    
    
    public boolean isRingHomomorphism() {
        return b.isZero() && (a.isOne() || a.isZero());
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
        if (morphism instanceof QAffineMorphism) {
            QAffineMorphism qm = (QAffineMorphism) morphism;
            return new QAffineMorphism(a.product(qm.a), a.product(qm.b).sum(b));
        }
        else {
            return super.compose(morphism);
        }
    }

    
    public ModuleMorphism sum(ModuleMorphism module) 
    		throws CompositionException {
        if (module instanceof QAffineMorphism) {
            QAffineMorphism qm = (QAffineMorphism) module;
            return new QAffineMorphism(a.sum(qm.a), b.sum(qm.b));
        }
        else {
            return super.sum(module);
        }
    }
    

    public ModuleMorphism difference(ModuleMorphism module)
        	throws CompositionException {
        if (module instanceof QAffineMorphism) {
            QAffineMorphism qm = (QAffineMorphism) module;
            return new QAffineMorphism(a.difference(qm.a), b.difference(qm.b));
        }
        else {
            return super.difference(module);
        }
    }
    

    public ModuleMorphism scaled(RingElement element)
            throws CompositionException {
        if (element instanceof QElement) {
            Rational s = ((QElement)element).getValue();
            if (s.isZero()) {
                return getConstantMorphism(element);
            }
            else {
                return new QAffineMorphism(getA().product(s), getB().product(s));
            }
        }
        else {
            throw new CompositionException("QAffineMorphism.scaled: Cannot scale "+this+" by "+element);
        }
    }
    
    
    public ModuleElement atZero() {
        return new QElement(getB());
    }
    
    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof QAffineMorphism) {
            QAffineMorphism morphism = (QAffineMorphism)object;
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
        if (object == this) {
            return true;
        }
        else if (object instanceof QAffineMorphism) {
            QAffineMorphism morphism = (QAffineMorphism)object;
            return a.equals(morphism.a) && b.equals(morphism.b);
        }
        else {
            return false;
        }
    }
    
    
    public String toString() {
        return "QAffineMorphism["+a+","+b+"]";
    }

    public String getElementTypeName() {
        return "QAffineMorphism";
    }
    
    
    /**
     * Returns the linear part.
     */
    public Rational getA() {
        return a;
    }
    

    /**
     * Returns the translation part.
     */
    public Rational getB() {
        return b;
    }
    
    
    private Rational a;
    private Rational b;
}
