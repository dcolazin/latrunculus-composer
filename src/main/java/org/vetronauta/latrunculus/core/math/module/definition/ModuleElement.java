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

import org.vetronauta.latrunculus.core.DeepCopyable;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.exception.DomainException;

import java.io.Serializable;

/**
 * The interface for elements in a module.
 * @see Module
 * 
 * @author Gérard Milmeister
 */
public interface ModuleElement<E extends ModuleElement<E,R>, R extends RingElement<R>> extends DeepCopyable<ModuleElement<E,R>>, Serializable, Comparable<ModuleElement<?,?>>, MathDefinition {

    //TODO null checks!

    /**
     * Returns true iff this element is zero.
     */
    boolean isZero();
    
    /**
     * Returns the product of this element with <code>element</code>.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    //TODO find better names for those methods
    E scaled(R element) throws DomainException;

    /**
     * Multiplies this element with <code>element</code>.
     * This is a destructive operation.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    void scale(R element) throws DomainException;

    /**
     * Returns the length of the element.
     */
    int getLength();

    /**
     * Returns the <code>i</code>-th component element.
     */
    ModuleElement<?,R> getComponent(int i);

    /**
     * Returns the sum of this module element and <code>element</code>.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    E sum(E element) throws DomainException;

    /**
     * Adds <code>element</code> to this module element.
     * This is a destructive operation.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    void add(E element) throws DomainException;

    /**
     * Returns the difference of this module element and <code>element</code>.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    E difference(E element) throws DomainException;

    /**
     * Subtracts <code>element</code> from this module element.
     * This is a destructive operation.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    void subtract(E element) throws DomainException;
    
    /**
     * Returns the negative of this module element.
     */
    E negated();

    /**
     * Negate this module element.
     * This is a destructive operation.
     */
    void negate();

    /**
     * Fold <code>elements</code> assuming they are of this same type.
     */
    //TODO proper signature; moreover, should be this notion be in Module/Ring and not in ModuleElement?
    double[] fold(ModuleElement<?,?>[] elements);

    /**
     * Returns the module that this module element is an element of.
     */
    Module<E,R> getModule();

    /**
     * Tries to cast this element to an element in the given module.
     * @return a new module element in the required module
     *         and null if the cast cannot be performed. 
     */
    //TODO is it really necessary to have this, when most of the time is module.cast(this) ?
    default <T extends ModuleElement<T,S>, S extends RingElement<S>> T cast(Module<T,S> module) {
        return module.cast(this);
    }

    /**
     * Returns a string representation of this module element.
     * The representation is meant to be parseable.
     * If the argument parens is present then the the representation
     * is parenthesized if necessary.
     */
    String stringRep(boolean... parens);

}
