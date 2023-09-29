/*
 * Copyright (C) 2001 Gérard Milmeister
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

package org.vetronauta.latrunculus.math.arith.string;

import org.apache.commons.lang3.StringUtils;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.EntryList;
import org.vetronauta.latrunculus.exception.LatrunculusCastException;
import org.vetronauta.latrunculus.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.math.arith.number.Rational;

import java.util.LinkedList;
import java.util.List;

/**
 * The ring of strings with rational factors.
 */
public final class QString extends RingString<Rational, QString> {

    private QString() {
        super();
    }

    public QString(String word) {
        super(word);
    }

    public QString(String word, Rational factor) {
        super(word, factor);
    }

    public QString(String[] words, Rational[] factors) {
        super(words, factors);
    }

    public QString(List<String> words, List<Rational> factors) {
        super(words, factors);
    }

    public QString(Object... objects) throws LatrunculusCastException {
        super(EntryList.handle(String.class, Rational.class, objects));
    }

    public QString(RingString<?, ?> rs) {
        super(rs);
    }

    public QString(double d) {
        super(new Rational(d));
    }

    public QString(ArithmeticNumber<?> number) {
        super(number);
    }

    @Override
    public Rational canonicalTransformation(ArithmeticNumber<?> number) {
        if (number instanceof Rational) {
            return (Rational) number;
        }
        return new Rational(number.doubleValue());
    }

    /**
     * @deprecated use the RingStringFactory method
     */
    @Deprecated
    public static QString getZero() {
        return new QString();
    }

    /**
     * @deprecated use the RingStringFactory method
     */
    @Deprecated
    public static QString getOne() {
        return new QString(StringUtils.EMPTY);
    }

    //TODO factory method
    public static QString parseQString(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return getOne();
        }
        
        LinkedList<String> words = new LinkedList<>();
        LinkedList<Rational> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            Rational f = Rational.parseRational(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(f);
            words.add(w);
        }
        
        return new QString(words, factors);
    }

    @Override
    protected Rational getObjectOne() {
        return new Rational(1);
    }

    @Override
    protected Rational getObjectZero() {
        return new Rational(0);
    }

    @Override
    public QString deepCopy() {
        return new QString(this);
    }
}
