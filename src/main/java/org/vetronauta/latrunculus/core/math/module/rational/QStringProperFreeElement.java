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

package org.vetronauta.latrunculus.core.math.module.rational;

import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Elements in the free module of QString.
 * @see QStringProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class QStringProperFreeElement extends ArithmeticStringMultiElement<QStringElement,Rational> {

    public static final QStringProperFreeElement nullElement = new QStringProperFreeElement(new ArrayList<>());

    public static FreeElement<?, QStringElement> make(List<RingString<Rational>> v) {
        assert(v != null);
        if (v.isEmpty()) {
            return nullElement;
        }
        else if (v.size() == 1) {
            return new QStringElement(v.get(0));
        }
        else {
            return new QStringProperFreeElement(v);
        }
    }

    private QStringProperFreeElement(List<RingString<Rational>> value) {
        super(QRing.ring, value);
    }

}
