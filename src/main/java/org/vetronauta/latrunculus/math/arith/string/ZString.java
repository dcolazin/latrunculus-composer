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

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.EntryList;
import org.vetronauta.latrunculus.exception.LatrunculusCastException;
import org.vetronauta.latrunculus.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.math.arith.number.Complex;
import org.vetronauta.latrunculus.math.arith.number.Rational;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * The ring of strings with integer factors.
 */
public final class ZString extends RingString<ArithmeticInteger> {

    public ZString(String word) {
        super(word);
    }

    public ZString(String word, int factor) {
        super(word, new ArithmeticInteger(factor));
    }

    public ZString(String[] words, int[] factors) {
        super(words, ArithmeticInteger.toArray(factors));
    }

    public ZString(List<String> words, List<Integer> factors) {
        super(words, ArithmeticInteger.toList(factors));
    }

    public ZString(Object... objects) throws LatrunculusCastException {
        super(EntryList.handle(String.class, Function.identity(), Integer.class, ArithmeticInteger::new, objects));
    }

    public ZString(RingString<?> rs) {
        super(rs);
    }

    public ZString(double d) {
        super(new ArithmeticInteger((int) d));
    }

    public ZString(int i) {
        super(new ArithmeticInteger(i));
    }
    
    public ZString(ArithmeticNumber<?> number) {
        super(number);
    }

    @Override
    public ArithmeticInteger canonicalTransformation(ArithmeticNumber<?> number) {
        if (number instanceof ArithmeticInteger) {
            return (ArithmeticInteger) number;
        }
        return new ArithmeticInteger(number.intValue());
    }

    public static ZString getZero() {
        ZString res = new ZString();
        res.dict = new HashMap<>();
        return res;
    }


    public static ZString getOne() {
        return new ZString("");
    }


    public static ZString parseZString(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');        
        if (terms.length == 0) {
            return getOne();
        }
        
        LinkedList<String> words = new LinkedList<>();
        LinkedList<Integer> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            int f = Integer.parseInt(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(f);
            words.add(w);
        }
        
        return new ZString(words, factors);
    }
    

    private ZString() { /* do nothing */ }

    
    protected Object sum(Object x, Object y) {
        int ix = (Integer) x;
        int iy = (Integer) y;
        return ix + iy;
    }

    
    protected Object difference(Object x, Object y) {
        int ix = (Integer) x;
        int iy = (Integer) y;
        return ix - iy;
    }

    
    protected Object product(Object x, Object y) {
        int ix = (Integer) x;
        int iy = (Integer) y;
        return ix * iy;
    }

    
    protected Object neg(Object x) {
        int ix = (Integer) x;
        return -ix;
    }

    
    protected boolean equals(Object x, Object y) {
        int ix = (Integer) x;
        int iy = (Integer) y;
        return ix == iy;
    }

    
    protected int compare(Object x, Object y) {
        int ix = (Integer) x;
        int iy = (Integer) y;
        return ix-iy;
    }

    
    protected Object getObjectOne() {
        return 1;
    }

    
    protected Object getObjectZero() {
        return 0;
    }

    
    protected boolean isObjectZero(Object x) {
        int ix = (Integer) x;
        return ix == 0;
    }

    
    protected double objectToDouble(Object x) {
        return ((Integer)x).doubleValue();
    }

    @Override
    public RingString deepCopy() {
        ZString res = new ZString();
        res.dict = new HashMap<>(dict);
        return res;
    }
}
