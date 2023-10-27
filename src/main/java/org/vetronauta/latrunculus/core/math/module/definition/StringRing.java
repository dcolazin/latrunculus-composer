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

import java.util.List;

/**
 * The abstract base class for rings with RingString elements.
 * @see StringElement
 * 
 * @author Gérard Milmeister
 */
public abstract class StringRing<R extends StringElement<R>> extends Ring<R> {

    public abstract R getOne();

    @Override
    public StringRing<R> getComponentModule(int i) {
        return this;
    }
    
    public abstract boolean hasElement(ModuleElement e);
    
    public R createElement(List<? extends ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return this.cast(elements.get(0));
        }
        else {
            return null;
        }
    }
    
    /**
     * Returns the ring of the factors.
     */
    public abstract Ring<?> getFactorRing();
    
    public int compareTo(Module object) {
        if (object instanceof StringRing) {
            Ring other = ((StringRing)object).getFactorRing();
            return getFactorRing().compareTo(other);
        }
        else {
            return super.compareTo(object);
        }
    }
    
    public abstract boolean equals(Object obj);

}
