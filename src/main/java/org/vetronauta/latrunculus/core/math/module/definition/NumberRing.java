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

import lombok.NonNull;

/**
 * Subclasses of this abstract class are the rings of modular integers, integers,
 * rationals, reals and complex numbers. They are ordered in
 * the way just given.
 * 
 * @author Gérard Milmeister
 */
public interface NumberRing {

    default int order(@NonNull NumberRing object) {
        return getNumberRingOrder() - object.getNumberRingOrder();
    }

    /**
     * Returns a number that indicates the position in
     * the order of rings.
     */
    int getNumberRingOrder();

}
