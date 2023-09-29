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
import org.vetronauta.latrunculus.math.arith.NumberTheory;
import org.vetronauta.latrunculus.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.math.arith.number.Complex;
import org.vetronauta.latrunculus.math.arith.number.Rational;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * The ring of strings with integer factors mod <i>p</i>.
 */
public final class ZnString extends RingString<ArithmeticModulus> {

    private int modulus;

    private ZnString(int modulus) {
        super();
        this.modulus = modulus;
    }

    public ZnString(String word, int modulus) {
        super(word);
        this.modulus = modulus;
    }
    
    public ZnString(String word, int factor, int modulus) {
        super(word, new ArithmeticModulus(factor, modulus));
        this.modulus = modulus;
    }

    public ZnString(String[] words, int[] factors, int modulus) {
        super(words, ArithmeticModulus.toArray(factors, modulus));
        this.modulus = modulus;
    }

    public ZnString(List<String> words, List<Integer> factors, int modulus) {
        super(words, ArithmeticModulus.toList(factors, modulus));
        this.modulus = modulus;
    }

    public ZnString(int modulus, Object... objects) throws LatrunculusCastException {
        super(EntryList.handle(String.class, Function.identity(), Integer.class, i -> new ArithmeticModulus(i, modulus), objects));
        this.modulus = modulus;
    }

    public ZnString(RingString<?> rs, int modulus) {
        super(rs); //TODO this will not work!!!
        this.modulus = modulus;
    }

    public ZnString(double d, int modulus) {
        super(new ArithmeticModulus((int) d, modulus));
        this.modulus = modulus;
    }

    public ZnString(int i, int modulus) {
        super(new ArithmeticModulus(i, modulus));
        this.modulus = modulus;
    }

    public ZnString(ArithmeticNumber<?> number, int modulus) {
        super(number);
        this.modulus = modulus;
    }

    @Override
    public ArithmeticModulus canonicalTransformation(ArithmeticNumber<?> number) {
        if (number instanceof ArithmeticModulus && (((ArithmeticModulus) number).getModulus() == modulus)) {
            return (ArithmeticModulus) number;
        }
        return new ArithmeticModulus(number.intValue(), modulus);
    }

    /**
     * @deprecated use the RingStringFactory method
     */
    @Deprecated
    public static ZnString getZero(int modulus) {
        return new ZnString(modulus);
    }

    /**
     * @deprecated use the RingStringFactory method
     */
    @Deprecated
    public static ZnString getOne(int modulus) {
        return new ZnString(StringUtils.EMPTY, modulus);
    }


    public static ZnString parseZnString(String string, int modulus) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return getOne(modulus);
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
        
        return new ZnString(words, factors, modulus);
    }

    
    protected Object sum(Object x, Object y) {
        int ix = (Integer) x;
        int iy = (Integer) y;
        return NumberTheory.mod(ix + iy, modulus);
    }

    
    protected Object difference(Object x, Object y) {
        int ix = (Integer) x;
        int iy = (Integer) y;
        return NumberTheory.mod(ix - iy, modulus);
    }

    
    protected Object product(Object x, Object y) {
        int ix = (Integer) x;
        int iy = (Integer) y;
        return NumberTheory.mod(ix * iy, modulus);
    }

    
    protected Object neg(Object x) {
        int ix = (Integer) x;
        return NumberTheory.mod(-ix, modulus);
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

    
    public int getModulus() {
        return modulus;
    }

    @Override
    public RingString deepCopy() {
        ZnString res = new ZnString(modulus);
        res.modulus = modulus;
        res.dict = new HashMap<>(dict);
        return res;
    }
}
