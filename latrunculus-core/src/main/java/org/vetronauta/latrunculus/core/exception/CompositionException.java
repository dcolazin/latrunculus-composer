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

package org.vetronauta.latrunculus.core.exception;

import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;

/**
 * Exception thrown if composition of morphisms fails.
 * Examples of composition are: compose, sum, difference.
 * 
 * @author Gérard Milmeister
 */
public final class CompositionException extends LatrunculusCheckedException {

    /**
     * Creates a CompositionException with the specified message.
     */
    public CompositionException(String msg) {
        super(msg);
    }
    

    /**
     * Creates a CompositionException with the specified message,
     * with a predefined message generated from <code>f</code> and
     * <code>g</code>, which are the components of the composition, appended.
     */
    public CompositionException(String msg, ModuleMorphism<?,?,?,?> f, ModuleMorphism<?,?,?,?> g) {
        super(msg+"Failed to compose "+f+" and "+g);
    }


    /**
     * Creates a CompositionException with
     * predefined message generated from <code>f</code> and
     * <code>g</code>, which are the components of the composition, appended.
     */
    public CompositionException(ModuleMorphism<?,?,?,?> f, ModuleMorphism<?,?,?,?> g) {
        this("", f, g);
    }

}
