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
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

    public FreeElement resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return QStringProperFreeElement.make(new ArrayList<>());
        }
        else {
            List<RingString<Rational>> values = new ArrayList<>(n);
            values.add(new RingString<>(getValue()));
            for (int i = 1; i < n; i++) {
                values.add(new RingString<>());
            }
            return QStringProperFreeElement.make(values);
        }
    }

    public String toString() {
        return "QStringElement["+getValue()+"]";
    }
    
    public HashMap<String,RingElement> getTerms() {
        HashMap<String,RingElement> map = new HashMap<>();
        Set<String> strings = getValue().getStrings();
        for (String s : strings) {
            map.put(s, new ArithmeticElement<>(((Rational)getValue().getFactorForString(s))));
        }
        return map;
    }

    public String getElementTypeName() {
        return "QStringElement";
    }

}
