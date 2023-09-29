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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * The ring of strings with complex factors.
 */
public final class CString extends RingString<Complex> {

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

    public CString(RingString<?> rs) {
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
    

    protected Object sum(Object x, Object y) {
        return ((Complex)x).sum((Complex)y);
    }

    
    protected Object difference(Object x, Object y) {
        return ((Complex)x).difference((Complex)y);
    }

    
    protected Object product(Object x, Object y) {
        return ((Complex)x).product((Complex)y);
    }

    
    protected Object neg(Object x) {
        return ((Complex)x).negated();
    }

    
    protected boolean equals(Object x, Object y) {
        return x.equals(y);
    }

    
    protected int compare(Object x, Object y) {
        Complex rx = (Complex)x;
        Complex ry = (Complex)y;
        return rx.compareTo(ry);
    }

    
    protected Object getObjectOne() {
        return Complex.getOne();
    }

    
    protected Object getObjectZero() {
        return Complex.getZero();
    }

    
    protected boolean isObjectZero(Object x) {
        return ((Complex)x).isZero();
    }

    
    protected double objectToDouble(Object x) {
        return ((Complex)x).doubleValue();
    }

    @Override
    public RingString deepCopy() {
        CString res = new CString();
        res.dict = new HashMap<>(dict);
        return res;
    }
}
