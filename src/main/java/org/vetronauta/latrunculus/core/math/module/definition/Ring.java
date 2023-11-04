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

package org.vetronauta.latrunculus.core.math.module.definition;

import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

/**
 * The abstract base class for rings.
 * Rings always have dimension 1.
 * @see RingElement
 * 
 * @author Gérard Milmeister
 */
public abstract class Ring<R extends RingElement<R>> implements FreeModule<R,R> {

    /**
     * Returns the unit element of this ring.
     */
    public abstract R getOne();

    /**
     * Returns the unit vector with 1 at position <code>i</code>.
     * In the case of rings, this is simple the unit.
     */
    public R getUnitElement(int i) {
        return getOne();
    }

    public ModuleMorphism getProjection(int index) {
        return getIdentityMorphism();
    }

    public ModuleMorphism getInjection(int index) {
        return getIdentityMorphism();
    }
    
    /**
     * Returns true if this ring is a field.
     */
    public abstract boolean isField();
    
    /**
     * Returns true if this module is a ring.
     */
    public boolean isRing() {
        return true;
    }

    @Override
    public boolean isVectorSpace() {
        return isField();
    }

    /**
     * Returns the corresponding free module of dimension <code>dim</code>.
     */
    public abstract FreeModule<?,R> getFreeModule(int dimension);

    /**
     * Here, the dimension of a ring as a module is 1.
     */
    public int getDimension() {
        return 1;
    }
    
    /**
     * A ring has just one component module: itself.
     */
    public Ring<R> getComponentModule(int i) {
        return this;
    }   

    /**
     * Here, a ring is not a null-module.  
     */
    public boolean isNullModule() {
        return false;
    }
    
    /**
     * The underlying ring of a ring as a module is itself.
     */
    public Ring<R> getRing() {
        return this;
    }

    public int compareTo(Module object) {
        return toString().compareTo(object.toString());
    }

}
