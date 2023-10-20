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
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;

import java.util.LinkedList;

/**
 * The ring of QString.
 * @see QStringElement
 * 
 * @author Gérard Milmeister
 */
public final class QStringRing extends StringRing<QStringElement> {

    public static final QStringRing ring = new QStringRing();

    public QStringElement getZero() {
        return new QStringElement(RingString.getZero());
    }

    
    public QStringElement getOne() {
        return new QStringElement(RingString.getOne());
    }

    
    public QStringProperFreeModule getNullModule() {
        return QStringProperFreeModule.nullModule;
    }
    
    
    public boolean isField() {
        return false;
    }
    
    
    public boolean isVectorSpace() {
        return false;
    }

    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof QStringElement);
    }

    
    public FreeModule getFreeModule(int dimension) {
        return QStringProperFreeModule.make(dimension);
    }

    
    public Ring getFactorRing() {
        return QRing.ring;
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


    public QStringElement cast(ModuleElement element) {
        if (element instanceof QStringElement) {
            return (QStringElement) element;
        }
        else if (element instanceof StringElement) {
            RingString rs = ((StringElement)element).getRingString();
            return new QStringElement(new RingString<>(rs));
        }
        else {
            QElement e = QRing.ring.cast(element);
            if (e == null) {
                return null;
            }
            else {
                return new QStringElement(new RingString<>(e.getValue()));
            }
        }       
    }

    
    public String toString() {
        return "QStringRing";
    }
    
    
    public String toVisualString() {
        return "C-String";
    }

    
    public QStringElement parseString(String string) {
        try {
            return new QStringElement(parse(TextUtils.unparenthesize(string)));
        }
        catch (Exception e) {
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

    public String getElementTypeName() {
        return "QStringRing";
    }
    
    
    public int hashCode() {
        return basicHash;
    }

    
    private static final int basicHash = "QStringRing".hashCode();

    private QStringRing() { /* not allowed */ }
}
