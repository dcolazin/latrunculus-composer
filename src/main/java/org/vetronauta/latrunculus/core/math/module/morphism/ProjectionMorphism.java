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

import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;

/**
 * A projection from a ProductRing to one its factors.
 * Instances are created using the {@link #make(ProductRing,int)} method.
 * 
 * @see ProductRing
 * @author Gérard Milmeister
 */
public class ProjectionMorphism extends ModuleMorphism {

    /**
     * Creates a projection from <code>domain</code> to the factor <code>i</code>
     * of <code>domain</code>. 
     * @param domain a product ring
     * @param i the index of codomain factor
     */
    public static ProjectionMorphism make(ProductRing domain, int i) {
        if (i < 0) {
            i = 0;
        }
        else if (i >= domain.getFactorCount()) {
            i = domain.getFactorCount()-1;
        }
        Ring codomain = domain.getFactor(i);
        return new ProjectionMorphism(domain, codomain, i);
    }
    

    /**
     * Returns the index of the codomain factor.
     */
    public int getIndex() {
        return index;
    }
    
    
    public ModuleElement map(ModuleElement x)
            throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ProjectionMorphism.map: ", x, this);
        }
        return ((ProductElement)x).getFactor(index);
    }

    
    public boolean isRingHomomorphism() {
        return true;
    }
    
    
    public ModuleMorphism getRingMorphism() {
        return this;
    }

    
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ProjectionMorphism) {
            ProjectionMorphism m = (ProjectionMorphism)object;
            return getDomain().equals(m.getDomain()) && getIndex() == m.getIndex();
        }
        else {
            return false;
        }
    }

    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ProjectionMorphism) {
            ProjectionMorphism m = (ProjectionMorphism)object;
            int res = getDomain().compareTo(m.getDomain());
            if (res == 0) {
                return getIndex()-m.getIndex();
            }
            else {
                return res;
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public String toString() {
        return "ProductProjectMorphism["+getDomain()+","+index+"]";
    }

    public String getElementTypeName() {
        return "ProjectionMorphism";
    }
    
    
    private ProjectionMorphism(ProductRing domain, Ring codomain, int index) {
        super(domain, codomain);
        this.index = index;
    }
    
    
    private int index;
}
