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

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringRing;

import java.util.LinkedList;

/**
 * The ring of QString.
 *
 * @author Gérard Milmeister
 */
public final class QStringRing extends ArithmeticStringRing<Rational> {

    public static final QStringRing ring = new QStringRing();

    public QStringProperFreeModule getNullModule() {
        return QStringProperFreeModule.nullModule;
    }
    
    public FreeModule getFreeModule(int dimension) {
        return QStringProperFreeModule.make(dimension);
    }
    
    public boolean equals(Object object) {
        return this == object;
    }

    
    public int compareTo(Module object) {
        if (this == object) {
            return 0;
        }
        else {
            return super.compareTo(object);
        }
    }
    
    public ArithmeticStringElement<Rational> parseString(String string) {
        try {
            return new ArithmeticStringElement<>(parse(TextUtils.unparenthesize(string)));
        } catch (Exception e) {
            return null;
        }
    }

    public static RingString<Rational> parse(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<Rational> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            Rational f = ArithmeticParsingUtils.parseRational(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(f);
            words.add(w);
        }

        return new RingString<>(words, factors);
    }
    
    public int hashCode() {
        return basicHash;
    }

    
    private static final int basicHash = "QStringRing".hashCode();

    private QStringRing() {
        super(QRing.ring);
    }
}
