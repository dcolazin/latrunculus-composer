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
import org.vetronauta.latrunculus.math.arith.number.Complex;

import java.util.LinkedList;
import java.util.List;

/**
 * The ring of strings with complex factors.
 */
public final class CString extends RingString<Complex, CString> {

    private CString() {
        super();
    }

    public CString(String word) {
        super(word);
    }
    
    public CString(String word, Complex factor) {
        super(word, factor);
    }

    public CString(String[] words, Complex[] factors) {
        super(words, factors);
    }

    public CString(List<String> words, List<Complex> factors) {
        super(words, factors);
    }    

    public CString(Object... objects) throws LatrunculusCastException {
        super(EntryList.handle(String.class, Complex.class, objects));
    }

    public CString(RingString<?, ?> rs) {
        super(rs);
    }

    public CString(double d) {
        super(new Complex(d));
    }

    public CString(ArithmeticNumber<?> number) {
        super(number);
    }

    @Override
    public Complex canonicalTransformation(ArithmeticNumber<?> number) {
        if (number instanceof Complex) {
            return (Complex) number;
        }
        return new Complex(number.doubleValue());
    }

    /**
     * @deprecated use the RingStringFactory method
     */
    @Deprecated
    public static CString getZero() {
        return new CString();
    }

    /**
     * @deprecated use the RingStringFactory method
     */
    @Deprecated
    public static CString getOne() {
        return new CString(StringUtils.EMPTY);
    }

    //TODO factory method
    public static CString parseCString(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return getOne();
        }
        
        LinkedList<String> words = new LinkedList<>();
        LinkedList<Complex> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            Complex f = Complex.parseComplex(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(f);
            words.add(w);
        }
        
        return new CString(words, factors);
    }

    @Override
    protected Complex getObjectOne() {
        return Complex.getOne();
    }

    @Override
    protected Complex getObjectZero() {
        return Complex.getZero();
    }

    @Override
    public CString deepCopy() {
        return new CString(this);
    }
}
