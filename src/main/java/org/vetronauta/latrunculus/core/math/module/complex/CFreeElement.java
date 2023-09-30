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

package org.vetronauta.latrunculus.core.math.module.complex;

import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;

/**
 * The interface for elements in the free modules of complex numbers.
 * @see CFreeModule
 * 
 * @author Gérard Milmeister
 */
public interface CFreeElement extends FreeElement {

    /**
     * Returns the conjugate of this element.
     */
    CFreeElement conjugated();

    /**
     * Conjugates this element.
     */
    void conjugate();

}