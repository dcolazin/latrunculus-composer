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

import java.util.Iterator;
import java.util.LinkedList;

/**
 * The abstract base class for elements in <i>proper</i> free modules.
 *
 * @author Gérard Milmeister
 */
public abstract class ProperFreeElement<E extends ModuleElement<E,R>, R extends RingElement<R>> implements FreeElement<E,R> {

    //TODO we don't really need this class?

    public int compareTo(ModuleElement object) {
        return getModule().compareTo(object.getModule());
    }
    
    
    public Iterator<R> iterator() {
        LinkedList<R> elements = new LinkedList<>();
        for (int i = 0; i < getLength(); i++) {
            elements.add(getRingElement(i));
        }
        return elements.iterator();
    }
    
    
}
