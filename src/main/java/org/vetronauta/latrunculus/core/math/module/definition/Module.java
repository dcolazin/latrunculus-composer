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

import java.io.Serializable;
import java.util.List;

import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

/**
 * The interface for modules.
 * @see ModuleElement
 * 
 * @author Gérard Milmeister
 */
public interface Module<E extends ModuleElement<E,R>, R extends RingElement<R>> extends Serializable, Comparable<Module<?,?>>, MathDefinition {

    /**
     * Returns the zero element in this module.
     */
    E getZero();

    /**
     * Returns the identity morphism in this module.
     */
    ModuleMorphism getIdentityMorphism();

    /**
     * Returns the dimension of this module.
     */
    int getDimension();

    /**
     * Returns the null-module corresponding to this module.
     */
    Module<?,R> getNullModule();
    
    /**
     * Returns true iff this is a null-module.
     */
    boolean isNullModule();
    
    /**
     * Returns true iff this module is a ring.
     */
    boolean isRing();
    
    /**
     * Returns the underlying ring of this module.
     */
    Ring<R> getRing();

    /**
     * Returns the <code>i</code>-th component module.
     */
    Module<?,R> getComponentModule(int i);

    /**
     * Returns true iff <code>element</code> is an element of this module.
     */
    boolean hasElement(ModuleElement<?,?> element);

    /**
     * Returns a morphism that translates by <code>element</code>.
     */
    ModuleMorphism getTranslation(ModuleElement<?,?> element);

    /**
     * Casts <code>element</code> to an element in this module if possible.
     * @return null if cast is not possible
     */
    E cast(ModuleElement<?,?> element);
    
    /**
     * Creates an element in this module from a list of module elements.
     * @return null if no element in this module can be created from
     *         the arguments.
     */
    E createElement(List<ModuleElement<?,?>> elements);

    /**
     * Creates an element in this module from a string representation.
     * @return null if the string is in the wrong format
     */
    E parseString(String string); //TODO this should be in a factory, not in an instance

    /**
     * Returns a human readable string representation of this module.
     * The representation is not meant to be parseable.
     * The string should be a short representation, possibly using
     * Unicode characters.
     */
    String toVisualString();
}
