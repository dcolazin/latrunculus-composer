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

import org.vetronauta.latrunculus.core.math.arith.string.RingString;

import java.util.Map;

/**
 * Elements in a string ring.
 * @see StringRing
 * 
 * @author Gérard Milmeister
 */
public abstract class StringElement<R extends RingElement<R>> extends RingElement<R> {

    /**
     * Convenience method for getting the string in a <code>StringElement</code>.
     * Only useful if the <code>RingString</code> has only one term.
     */
    public String getString() {
        return getRingString().getString();
    }

    
    /**
     * Returns the underlying <code>RingString</code>.
     */
    public abstract RingString getRingString();

    
    public boolean isInvertible() {
        return isOne();
    }
    
    
    /**
     * Returns the terms of the string as a map from strings to factors.
     */
    public abstract Map<String,RingElement> getTerms(); //TODO proper signature
    
    @Override
    public int hashCode() {
        return getRingString().hashCode();
    }
}
