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

import org.vetronauta.latrunculus.server.xml.XMLInputOutput;
import org.vetronauta.latrunculus.core.DeepCopyable;
import org.vetronauta.latrunculus.core.math.exception.DomainException;

/**
 * The interface for elements in a module.
 * @see Module
 * 
 * @author Gérard Milmeister
 */
public interface ModuleElement extends DeepCopyable<ModuleElement>, Serializable, Comparable<ModuleElement>, XMLInputOutput<ModuleElement> {

    /**
     * Returns true iff this element is zero.
     */
    boolean isZero();
    
    /**
     * Returns the product of this element with <code>element</code>.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    ModuleElement scaled(RingElement element) throws DomainException;

    /**
     * Multiplies this element with <code>element</code>.
     * This is a destructive operation.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    void scale(RingElement element) throws DomainException;

    /**
     * Returns the length of the element.
     */
    int getLength();

    /**
     * Returns the <code>i</code>-th component element.
     */
    ModuleElement getComponent(int i);

    /**
     * Returns the sum of this module element and <code>element</code>.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    ModuleElement sum(ModuleElement element) throws DomainException;

    /**
     * Adds <code>element</code> to this module element.
     * This is a destructive operation.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    void add(ModuleElement element) throws DomainException;

    /**
     * Returns the difference of this module element and <code>element</code>.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    ModuleElement difference(ModuleElement element) throws DomainException;

    /**
     * Subtracts <code>element</code> from this module element.
     * This is a destructive operation.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    void subtract(ModuleElement element) throws DomainException;
    
    /**
     * Returns the negative of this module element.
     */
    ModuleElement negated();

    /**
     * Negate this module element.
     * This is a destructive operation.
     */
    void negate();

    /**
     * Fold <code>elements</code> assuming they are of this same type.
     */
    double[] fold(ModuleElement[] elements);

    /**
     * Returns the module that this module element is an element of.
     */
    Module getModule();

    /**
     * Tries to cast this element to an element in the given module.
     * @return a new module element in the required module
     *         and null if the cast cannot be performed. 
     */
    ModuleElement cast(Module module);

    /**
     * Returns a string representation of this module element.
     * The representation is meant to be parseable.
     * If the argument parens is present then the the representation
     * is parenthesized if necessary.
     */
    String stringRep(boolean... parens);

}
