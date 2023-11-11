/*
 * Copyright (C) 2006 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.math.morphism;

import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;

/**
 * Morphism that represents the product of two morphisms with
 * the same domains and codomains, respectively,
 * provided that the codomain is a ring.
 * 
 * @author Gérard Milmeister
 */
public final class ProductMorphism<A extends ModuleElement<A,RA>, RA extends RingElement<RA>, RB extends RingElement<RB>>
        extends ModuleMorphism<A,RB,RA,RB> {

    private final ModuleMorphism<A,RB,RA,RB> f;
    private final ModuleMorphism<A,RB,RA,RB> g;

    /**
     * Creates a morphism from <code>f</code> and <code>g</code>.
     * The resulting morphism <i>h</i> is such that <i>h(x) = f(x)*g(x)</i>.
     * The codomain must be a ring
     */
    public static <X extends ModuleElement<X,RX>, RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<X,RY,RX,RY> make(ModuleMorphism<X,RY,RX,RY> f, ModuleMorphism<X,RY,RX,RY> g) {
        if (f.isIdentity()) {
            return g;
        }
        if (g.isIdentity()) {
            return f;
        }
        if (f.isConstant() && g.isConstant()) {
            RY fe = f.atZero();
            RY ge = g.atZero();
            return getConstantMorphism(f.getDomain(), fe.product(ge));
        }
        else {
            return new ProductMorphism<>(f, g);
        }
    }

    private ProductMorphism(ModuleMorphism<A,RB,RA,RB> f, ModuleMorphism<A,RB,RA,RB> g) {
        super(f.getDomain(), f.getCodomain());
        this.f = f;
        this.g = g;
    }

    @Override
    public RB map(A x) throws MappingException {
        try {
            return f.map(x).product(g.map(x));
        }
        catch (DomainException e) {
            throw new MappingException("ProductMorphism.map: ", x, this);
        }
    }
    
    @Override
    public boolean isModuleHomomorphism() {
        return false;
    }

    @Override
    public boolean isRingHomomorphism() {
        return f.isRingHomomorphism() && g.isRingHomomorphism();
    }
    
    @Override
    public boolean isLinear() {
        return false;
    }
    
    @Override
    public boolean isConstant() {
        return f.isConstant() && g.isConstant();
    }
    
    
    /**
     * Returns the first morphism <i>f</i> of the product <i>f*g</i>.
     */
    public ModuleMorphism<A,RB,RA,RB> getFirstMorphism() {
        return f;
    }
    
    
    /**
     * Returns the second morphism <i>g</i> of the sum <i>f*g</i>.
     */
    public ModuleMorphism<A,RB,RA,RB> getSecondMorphism() {
        return g;
    }
    
    @Override
    public ModuleMorphism<RA,RB,RA,RB> getRingMorphism() {
        return f.getRingMorphism();
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ProductMorphism) {
            ProductMorphism<?,?,?> morphism = (ProductMorphism<?,?,?>) object;
            int comp = f.compareTo(morphism.f);
            if (comp == 0) {
                return g.compareTo(morphism.g);
            }
            else {
                return comp;
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProductMorphism) {
            ProductMorphism<?,?,?> m = (ProductMorphism<?,?,?>) object;
            return f.equals(m.f) && g.equals(m.g);
        }
        else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "ProductMorphism["+f+","+g+"]";
    }

    public String getElementTypeName() {
        return "SumMorphism";
    }

}
