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
import org.vetronauta.latrunculus.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.math.arith.number.ArithmeticNumber;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * The ring of strings with real factors.
 */
public final class RString extends RingString<ArithmeticDouble, RString> {

    private RString() {
        super();
    }

    public RString(String word) {
        super(word);
    }

    public RString(String word, double factor) {
        super(word, new ArithmeticDouble(factor));
    }

    public RString(String[] words, double[] factors) {
        super(words, ArithmeticDouble.toArray(factors));
    }

    public RString(List<String> words, List<Double> factors) {
        super(words, ArithmeticDouble.toList(factors));
    }

    public RString(Object... objects) throws LatrunculusCastException {
        super(EntryList.handle(String.class, Function.identity(), Double.class, ArithmeticDouble::new, objects));
    }

    public RString(RingString<?, ?> rs) {
        super(rs);
    }

    public RString(double d) {
        super(new ArithmeticDouble(d));
    }

    public RString(ArithmeticNumber<?> number) {
        super(number);
    }

    @Override
    public ArithmeticDouble canonicalTransformation(ArithmeticNumber<?> number) {
        if (number instanceof ArithmeticDouble) {
            return (ArithmeticDouble) number;
        }
        return new ArithmeticDouble(number.doubleValue());
    }

    /**
     * @deprecated use the RingStringFactory method
     */
    @Deprecated
    public static RString getZero() {
        return new RString();
    }

    /**
     * @deprecated use the RingStringFactory method
     */
    @Deprecated
    public static RString getOne() {
        return new RString(StringUtils.EMPTY);
    }

    //TODO factory method
    public static RString parseRString(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return getOne();
        }
        
        LinkedList<String> words = new LinkedList<>();
        LinkedList<Double> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            double f = Double.parseDouble(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(f);
            words.add(w);
        }
        
        return new RString(words, factors);
    }

    @Override
    protected ArithmeticDouble getObjectOne() {
        return new ArithmeticDouble(1);
    }

    @Override
    protected ArithmeticDouble getObjectZero() {
        return new ArithmeticDouble(0);
    }

    @Override
    public RString deepCopy() {
        return new RString(this);
    }
}
