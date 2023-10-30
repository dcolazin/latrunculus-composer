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
import org.vetronauta.latrunculus.core.math.folding.FoldingElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.real.RRing;

import java.util.HashMap;

/**
 * Morphism that represents the folding of a set of ModuleElements.
 * 
 * @author Gérard Milmeister
 */
public final class FoldingMorphism extends ModuleMorphism {

    /**
     * Create a morphism that maps each of <code>elements</code> to its fold number.
     */
    public FoldingMorphism(ModuleElement[] elements) {
        super(elements[0].getModule(), RRing.ring);
        this.elements = elements;
        double[] f = FoldingElement.fold(elements[0], elements);
        values = new HashMap<>();
        for (int i = 0; i < f.length; i++) {
            values.put(elements[i], new ArithmeticElement<>(new Real(f[i])));
        }
    }

    public ModuleElement[] getElements() {
        return elements;
    }

    
    public ArithmeticElement<Real> map(ModuleElement element) {
        return values.get(element);
    }

    
    public ModuleMorphism getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }

    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof FoldingMorphism) {
            FoldingMorphism morphism = (FoldingMorphism)object;
            int hc1 = values.hashCode();
            int hc2 = morphism.values.hashCode();
            return hc1-hc2;
        }
        else {
            return super.compareTo(object);
        }
    }
    
    
    public boolean equals(Object object) {
        if (object instanceof DifferenceMorphism) {
            FoldingMorphism morphism = (FoldingMorphism)object;
            return values.equals(morphism.values);
        }
        else {
            return false;
        }
    }
    
    
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("FoldingMorphism[");
        if (elements.length > 0) {
            buf.append(elements[0].toString());
            for (int i = 1; i < elements.length; i++) {
                buf.append(",");
                buf.append(elements[i]);
            }
        }
        buf.append("]");
        return buf.toString();
    }

    public String getElementTypeName() {
        return "FoldingMorphism";
    }
    
    
    private final HashMap<ModuleElement,ArithmeticElement<Real>> values;
    private final ModuleElement[] elements;
}
