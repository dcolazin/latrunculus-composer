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

import static java.lang.Math.min;

import java.util.*;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.math.arith.number.Complex;
import org.vetronauta.latrunculus.math.arith.number.Rational;

/**
 * The ring of strings with integer factors.
 */
public final class ZString extends RingString {

    public ZString(String word) {
        dict = new HashMap<>();
        dict.put(word, getObjectOne());
    }
    

    public ZString(String word, int factor) {
        dict = new HashMap<>();
        if (factor != 0) {
            add(word, factor);
        }
    }
    

    public ZString(String[] words, int[] factors) {
        dict = new HashMap<>();
        int len = min(factors.length, words.length);
        for (int i = 0; i < len; i++) {
            if (factors[i] != 0) {
                add(words[i], factors[i]);
            }
        }
    }
    
    
    public ZString(List<String> words, List<Integer> factors) {
        dict = new HashMap<>();
        int len = Math.min(factors.size(), words.size());
        Iterator<String> witer = words.iterator();
        Iterator<Integer> fiter = factors.iterator();
        for (int i = 0; i < len; i++) {
            String w = witer.next();
            int f = fiter.next();
            if (f != 0) {
                add(w, i);
            }
        }
    }
    
    
    public ZString(Object ... objects) {
        for (int i = 0; i < objects.length; i += 2) {
            String w = (String)objects[i]; 
            int f = (Integer)objects[i+1];
            if (f != 0) {
                add(w, f);
            }
        }
    }
    

    public ZString(RingString rs) {
        if (rs instanceof ZString) {
            dict = new HashMap<>(rs.dict);
        }
        else {
            dict = new HashMap<>();
            for (String key : rs.dict.keySet()) {
                Object value = rs.dict.get(key);
                Integer i = ObjectInteger(value);
                if (i != 0) {
                    add(key, i);
                }
            }
        }
    }
    
    
    public ZString(int i) {
        this("", i);
    }

    
    public ZString(Rational r) {
        this("", r.intValue());
    }

    
    public ZString(double d) {
        this("", (int)Math.round(d));
    }
    
    
    public ZString(Complex c) {
        this("", c.intValue());
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

    
    protected double ObjectToDouble(Object x) {
        return ((Integer)x).doubleValue();
    }

    @Override
    public RingString deepCopy() {
        ZString res = new ZString();
        res.dict = new HashMap<>(dict);
        return res;
    }
}
