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

package org.vetronauta.latrunculus.math.arith;

import static java.lang.Math.min;

import java.util.*;

import org.rubato.util.TextUtils;

/**
 * The ring of strings with real factors.
 */
@SuppressWarnings("nls")
public final class RString extends RingString {

    public RString(String word) {
        dict = new HashMap<String,Object>();
        dict.put(word, getObjectOne());
    }

    
    public RString(String word, double factor) {
        dict = new HashMap<String,Object>();
        if (factor != 0.0) {
            add(word, factor);
        }
    }

    
    public RString(String[] words, double[] factors) {
        dict = new HashMap<String,Object>();
        int len = min(factors.length, words.length);
        for (int i = 0; i < len; i++) {
            if (factors[i] != 0.0) {
                add(words[i], factors[i]);
            }
        }
    }

    
    public RString(List<String> words, List<Double> factors) {
        dict = new HashMap<String,Object>();
        int len = min(factors.size(), words.size());
        Iterator<String> witer = words.iterator();
        Iterator<Double> fiter = factors.iterator();
        for (int i = 0; i < len; i++) {            
            String w = witer.next();
            double f = fiter.next();
            if (f != 0.0) {
                add(w, i);
            }
        }
    }
    

    public RString(Object ... objects) {
        for (int i = 0; i < objects.length; i += 2) {
            String w = (String)objects[i]; 
            double f = (Double)objects[i+1];
            if (f != 0.0) {
                add(w, f);
            }
        }
    }
    

    public RString(RingString rs) {
        if (rs instanceof RString) {
            dict = new HashMap<String,Object>(rs.dict);
        }
        else {
            dict = new HashMap<String,Object>();
            for (String key : rs.dict.keySet()) {
                Object value = rs.dict.get(key);
                Double f = ObjectDouble(value);
                if (f != 0.0) {
                    add(key, f);
                }
            }
        }
    }

    
    public RString(int i) {
        this("", i);
    }

    
    public RString(Rational r) {
        this("", r.doubleValue());
    }

    
    public RString(double d) {
        this("", d);
    }
    
    
    public RString(Complex c) {
        this("", c.doubleValue());
    }


    public static RString getZero() {
        RString res = new RString();
        res.dict = new HashMap<>();
        return res;
    }


    public static RString getOne() {
        return new RString("");
    }


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
    

    private RString() { /* do nothing */ }
    

    protected Object sum(Object x, Object y) {
        double ix = (Double) x;
        double iy = (Double) y;
        return ix + iy;
    }
    

    protected Object difference(Object x, Object y) {
        double ix = (Double) x;
        double iy = (Double) y;
        return ix - iy;
    }

    
    protected Object product(Object x, Object y) {
        double ix = (Double) x;
        double iy = (Double) y;
        return ix * iy;
    }
    

    protected Object neg(Object x) {
        double ix = (Double) x;
        return -ix;
    }
    

    protected boolean equals(Object x, Object y) {
        double ix = (Double) x;
        double iy = (Double) y;
        return ix == iy;
    }
    

    protected int compare(Object x, Object y) {
        double ix = (Double) x;
        double iy = (Double) y;
        if (ix < iy) {
            return -1;
        }
        else if (ix > iy) {
            return 1;
        }
        else {
            return 0;
        }
    }

    
    protected Object getObjectOne() {
        return 1.0;
    }

    
    protected Object getObjectZero() {
        return 0.0;
    }

    
    protected boolean isObjectZero(Object x) {
        double ix = (Double) x;
        return ix == 0.0;
    }

    
    protected double ObjectToDouble(Object x) {
        return (Double) x;
    }

    @Override
    public RingString deepCopy() {
        RString res = new RString();
        res.dict = new HashMap<>(dict);
        return res;
    }
}
