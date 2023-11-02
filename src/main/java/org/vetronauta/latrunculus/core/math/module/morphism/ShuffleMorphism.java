/*
 * Copyright (C) 2005 Gérard Milmeister
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

import org.vetronauta.latrunculus.core.math.matrix.ArrayMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineFreeMorphism;

/**
 * This morphism reorders the components of an element of
 * the domain.
 * 
 * @author Gérard Milmeister
 */
public abstract class ShuffleMorphism {

    /**
     * Creates a morphisms that reorder an element from <code>domain</code>,
     * according to the array <code>shuffle</code>.
     * This means, that the component at <code>i</code> is mapped to <code>shuffle[i]</code>.
     * Components not hit are set to zero. 
     */
    public static ModuleMorphism make(FreeModule domain, FreeModule codomain, int[] shuffle) {
        if (domain.getRing().equals(codomain.getRing())) {
            Ring ring = domain.getRing();
            if (shuffle.length != domain.getDimension()) {
                return null;
            }
            for (int i = 0; i < shuffle.length; i++) {
                if (shuffle[i] > codomain.getDimension()-1) {
                    return null;
                }
                if (shuffle[i] >= 0) {
                    for (int j = 0; j < i; j++) {
                        if (shuffle[i] == shuffle[j]) {
                            return null;
                        }
                    }
                }
            }
            if (domain.getDimension() == codomain.getDimension()) {
                boolean id = true;
                for (int i = 0; i < shuffle.length; i++) {
                    if (shuffle[i] != i) {
                        id = false;
                    }
                }
                if (id) {
                    return new IdentityMorphism(domain);
                }
            }
            return createShuffleMorphism(ring, domain, codomain, shuffle);
        }
        else {
            return null;
        }        
    }
    
    
    private static ModuleMorphism createShuffleMorphism(Ring ring, FreeModule domain, FreeModule codomain, int[] shuffle) {
        if (ring instanceof ArithmeticRing) {
            ArrayMatrix m = new ArrayMatrix(ring, codomain.getDimension(), domain.getDimension());
            for (int i = 0; i < m.getRowCount(); i++) {
                for (int j = 0; j < m.getColumnCount(); j++) {
                    if (shuffle[j] == i) {
                        m.set(i, j, ((ArithmeticRing<?>) ring).getOne());
                    } else {
                        m.set(i, j, ((ArithmeticRing<?>) ring).getZero());
                    }
                }
            }
            return ArithmeticAffineFreeMorphism.make((ArithmeticRing) ring, m, ArithmeticMultiElement.zero((ArithmeticRing) ring, codomain.getDimension()));
        }
        int dim = domain.getDimension();
        int codim = codomain.getDimension();
        GenericAffineMorphism morphism = GenericAffineMorphism.make(ring, dim, codim);
        for (int i = 0; i < codim; i++) {
            for (int j = 0; j < dim; j++) {
                if (shuffle[j] == i) {
                    morphism.setMatrix(i, j, ring.getOne());
                }
            }
        }
        return morphism;
    }
    
    
    private ShuffleMorphism() {}
}
