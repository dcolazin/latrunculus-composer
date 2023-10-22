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

import lombok.NonNull;
import org.vetronauta.latrunculus.core.EntryList;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;

/**
 * Elements in the ring of strings with rational factors.
 * @see QStringRing
 * 
 * @author Gérard Milmeister
 */
public final class QStringElement extends ArithmeticStringElement<QStringElement,Rational> {

    /**
     * Constructs a QStringElement from a QString <code>value</code>.
     */
    public QStringElement(RingString<Rational> value) {
        super(value);
    }


    /**
     * Constructs a QStringElement from a simple string <code>value</code>.
     * The result is a QStringElement of the form 1/1*value.
     */
    public QStringElement(String value) {
        super(value);
    }

    @Override
    protected QStringElement valueOf(@NonNull RingString<Rational> value) {
        return new QStringElement(value);
    }

    public QStringElement(EntryList<String,Rational> entryList) {
        super(entryList);
    }

}
