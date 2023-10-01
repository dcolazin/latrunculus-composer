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

import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;

/**
 * Morphism that represents the difference of two arbitrary morphisms.
 *
 * @author Gérard Milmeister
 */
public final class DifferenceMorphism extends ModuleMorphism {
    
    /**
     * Creates a morphism from <code>f</code> and <code>g</code>.
     * The resulting morphism h is such that h(x) = f(x)-g(x).
     * 
     * @throws CompositionException if difference is not valid
     */
    public static ModuleMorphism make(ModuleMorphism f, ModuleMorphism g)
            throws CompositionException {
        if (!f.getDomain().equals(g.getDomain()) ||
            !f.getCodomain().equals(g.getCodomain())) {
            throw new CompositionException("DifferenceMorphism.make: Cannot subtract "+g+" from "+f);
        }
        else if (f.isConstant() && g.isConstant()) {
            try {
                ModuleElement zero = f.getDomain().getZero();
                ModuleElement fe = f.map(zero);
                ModuleElement ge = g.map(zero);
                return new ConstantMorphism(fe.difference(ge));
            }
            catch (DomainException e) {
                throw new AssertionError("This should never happen!");
            }
            catch (MappingException e) {
                throw new AssertionError("This should never happen!");
            }
        }
        else {
            return new DifferenceMorphism(f, g);
        }
    }
    
    
    private DifferenceMorphism(ModuleMorphism f, ModuleMorphism g) {
        super(f.getDomain(), f.getCodomain());
        this.f = f;
        this.g = g;
    }

    
    public ModuleElement map(ModuleElement x)
            throws MappingException {
        try {
            return f.map(x).difference(g.map(x));
        }
        catch (DomainException e) {
            throw new MappingException("DifferenceMorphism.map: ", x, this);
        }
    }

    
    public boolean isModuleHomomorphism() {
        return f.isModuleHomomorphism() && g.isModuleHomomorphism();
    }
    

    public boolean isRingHomomorphism() {
        return f.isRingHomomorphism() && g.isRingHomomorphism();
    }
    
    
    public boolean isLinear() {
        return f.isLinear() && g.isLinear();
    }
    
    
    /**
     * Returns the morphism <i>f</i> of the difference <i>f-g</i>.
     */
    public ModuleMorphism getFirstMorphism() {
        return f;
    }
    
    
    /**
     * Returns the morphism <i>g</i> of the difference <i>f-g</i>.
     */
    public ModuleMorphism getSecondMorphism() {
        return g;
    }
    
    
    public ModuleMorphism getRingMorphism() {
        return f.getRingMorphism();
    }

    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof DifferenceMorphism) {
            DifferenceMorphism morphism = (DifferenceMorphism)object;
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

    
    public boolean equals(Object object) {
        if (object instanceof DifferenceMorphism) {
            DifferenceMorphism m = (DifferenceMorphism)object;
            return f.equals(m.f) && g.equals(m.g);
        }
        else {
            return false;
        }
    }
    
    
    public String toString() {
        return "DifferenceMorphism["+f+","+g+"]";
    }

    public String getElementTypeName() {
        return "DifferenceMorphism";
    }
    
    
    private ModuleMorphism f;
    private ModuleMorphism g;
}
