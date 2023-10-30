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

package org.vetronauta.latrunculus.core.math.arith.string;

import org.apache.commons.lang3.StringUtils;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.util.DeepCopyable;
import org.vetronauta.latrunculus.core.util.EntryList;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.min;

/**
 * The ring of strings.
 * Strings are represented by sum(a_i*s_i), 
 * where the a_i are elements in a ring
 * and the s_i are character strings (<code>String</code>).
 */
public class RingString<T extends ArithmeticNumber<T>> implements DeepCopyable<RingString<T>>, Comparable<RingString<T>>, Serializable {

    //TODO null checks everywhere

    private Map<String, T> dict; //TODO should be final?

    /**
     * Creates a new <code>RingString</code> instance.
     * This is the Zero RingString.
     */
    public RingString() {
        this.dict = new HashMap<>();
    }

    /**
     * Creates a new <code>RingString</code> instance.
     * The resulting string is represented as 1*word.
     */
    public RingString(String word) {
        this();
        dict.put(word, getObjectOne());
    }

    /**
     * Creates a new <code>RingString</code> instance.
     * The resulting string is represented as factor*"" after the canonical transformation.
     */
    public RingString(ArithmeticNumber<?> factor) {
        this();
        dict.put(StringUtils.EMPTY, canonicalTransformation(factor));
    }

    /**
     * Creates a new <code>RingString</code> instance.
     * The resulting string is represented as factor*word.
     */
    public RingString(String word, T factor) {
        this();
        if (!factor.isZero()) {
            dict.put(word, factor);
        }
    }

    /**
     * Creates a new <code>RingString</code> instance.
     * The resulting string is represented as sum(factors[i]*words[i]).
     */
    public RingString(String[] words, T[] factors) {
        this();
        int len = Math.min(factors.length, words.length);
        for (int i = 0; i < len; i++) {
            T factor = factors[i];
            if (!factor.isZero()) {
                add(words[i], factor);
            }
        }
    }

    /**
     * Creates a new <code>RingString</code> instance.
     * The resulting string is represented as sum(values[i]*keys[i]).
     */
    public RingString(EntryList<String, T> entryList) {
        this(entryList.getKeys(), entryList.getValues());
    }

    /**
     * Creates a new <code>RingString</code> instance.
     * The resulting string is represented as sum(factors[i]*words[i]).
     */
    public RingString(List<String> words, List<T> factors) {
        this();
        int len = min(factors.size(), words.size());
        for (int i = 0; i < len; i++) {
            T factor = factors.get(i);
            if (!factor.isZero()) {
                add(words.get(i), factor);
            }
        }
    }

    /**
     * Creates a new <code>RingString</code> instance, in case converting the values with the canonical transformation
     */
    public RingString(RingString<?> rs) {
        this();
        for (Map.Entry<String, ? extends ArithmeticNumber<?>> entry : rs.dict.entrySet()) {
            T factor = canonicalTransformation(entry.getValue());
            if (!factor.isZero()) {
                add(entry.getKey(), factor);
            }
        }
    }

    public static <I extends ArithmeticNumber<I>> RingString<I> getZero() {
        return new RingString<>();
    }

    public static <I extends ArithmeticNumber<I>> RingString<I> getOne() {
        return new RingString<>(StringUtils.EMPTY);
    }

    public boolean isOne() {
        return dict.size() == 1 && Optional.ofNullable(dict.get(StringUtils.EMPTY)).map(ArithmeticNumber::isOne).orElse(false);
    }

    public boolean isZero() {
        return dict.size() == 0;
    }

    public T canonicalTransformation(ArithmeticNumber<?> number) {
        return null; //TODO!!! from ring?
    }

    /**
     * Returns one character string in the RingString.
     * If the RingString has more than one term, the returned
     * string may be any one of these.
     * If the RingString is empty, null is returned.
     */
    public String getString() {
        Set<String> keys = dict.keySet();
        if (!keys.isEmpty()) {
            return keys.iterator().next();
        }
        return null;
    }

    /**
     * Returns a Set of all the strings in the
     * terms of the RingString.
     */
    public Set<String> getStrings() {
        return dict.keySet();
    }

    /**
     * Returns a Set of all the strings in the
     * terms of the RingString.
     */
    public Collection<T> getFactors() {
        return dict.values();
    }

    /**
     * Returns the factor for the character string.
     */
    public T getFactorForString(String word) {
        if (dict.containsKey(word)) {
            return dict.get(word);
        }
        return getObjectZero();
    }

    /**
     * Returns the sum of this and <code>x</code>.
     * @return a new RingString object
     */
    public RingString<T> sum(RingString<T> x) {
        RingString<T> res = this.deepCopy();
        res.add(x);
        return res;
    }

    /**
     * Add <code>x</code> to this.
     */
    public void add(RingString<T> x) {
        for (Map.Entry<String, T> entry : x.dict.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns the difference of this and <code>x</code>.
     * @return a new RingString object.
     */
    public RingString<T> difference(RingString<T> x) {
        RingString<T> res = this.deepCopy();
        res.subtract(x);
        return res;
    }

    /**
     * Subtract <code>x</code> from this.
     */
    public void subtract(RingString<T> x) {
        for (Map.Entry<String, T> entry : x.dict.entrySet()) {
            subtract(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns the product of this and <code>x</code>
     * @return a new RingString object
     */
    public RingString<T> product(RingString<T> x) {
        RingString<T> res = this.deepCopy();
        res.multiply(x);
        return res;
    }

    /**
     * Multiply this by <code>x</code>.
     */
    public void multiply(RingString<T> x) {
        Iterator<String> keys = x.dict.keySet().iterator();
        Map<String, T> myDict = dict;
        dict = new HashMap<>();
        while (keys.hasNext()) {
            String key = keys.next();
            T factor = x.dict.get(key);
            for (Map.Entry<String, T> entry : myDict.entrySet()) {
                add(entry.getKey() + key, factor.product(entry.getValue()));
            }
        }
    }

    /**
     * Returns this with all factors negated.
     */
    public RingString<T> negated() {
        RingString<T> res = this.deepCopy();
        res.negate();
        return res;
    }

    /**
     * Negate all factors in this.
     */
    public void negate() {
        for (Map.Entry<String, T> entry : dict.entrySet()) {
            T newFactor = entry.getValue().neg();
            dict.put(entry.getKey(), newFactor);
        }
    }

    /**
     * Returns this scaled by <code>x</code>.
     * @return a new RingString object
     */
    public RingString<T> scaled(T x) {
        RingString<T> res = this.deepCopy();
        res.scale(x);
        return res;
    }

    /**
     * Scale this by <code>x</code>.
     */
    public void scale(T x) {
        for (Map.Entry<String, T> entry : dict.entrySet()) {
            T newFactor = x.product(entry.getValue());
            if (isObjectZero(newFactor)) {
                dict.remove(entry.getKey());
            } else {
                dict.put(entry.getKey(), newFactor);
            }
        }
    }

    /**
     * Two RingString's are equal if they have the same
     * character strings and the corresponding factors
     * of each are equal.
     */
    public boolean equals(Object object) {
        if (!(object instanceof RingString)) {
            return false;
        }
        Map<String, T> ht = ((RingString) object).dict; //TODO this cast is not always correct
        Set<String> otherObjectSet = ht.keySet();
        Set<String> set = dict.keySet();
        if (otherObjectSet.size() != set.size()) {
            return false;
        }
        for (String key : set) {
            if (!equals(dict.get(key), ht.get(key))) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     */
    public int compareTo(RingString<T> rs) {
        Object[] otherKeyArray = rs.dict.keySet().toArray();
        Object[] thisKeyArray = dict.keySet().toArray();
        Arrays.sort(otherKeyArray);
        Arrays.sort(thisKeyArray);
        int len = Math.min(otherKeyArray.length, thisKeyArray.length);
        for (int i = 0; i < len; i++) {
            int stringCompare = ((String)thisKeyArray[i]).compareTo((String)otherKeyArray[i]);
            if (stringCompare == 0) {
                int comp = compare(dict.get(thisKeyArray[i]), rs.dict.get(otherKeyArray[i]));
                if (comp != 0) {
                    return comp;
                }
            } else {
                return stringCompare;
            }
        }
        return (thisKeyArray.length - otherKeyArray.length);
    }

    /**
     * The equality operation of two factor objects.
     */
    private boolean equals(T x, T y) {
        if (x == null && y == null) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
    }

    /**
     * Compare two factor objects, like compareTo.
     */
    protected int compare(T x, T y) {
        if (x == null && y == null) {
            return 0;
        }
        if (x == null || y == null) {
            return x != null ? 1 : -1;
        }
        return x.compareTo(y);
    }

    /**
     * Returns the unit factor object.
     */
    private T getObjectOne() {
        return null; //TODO!!! from ring?
    }

    /**
     * Returns the zero factor object.
     */
    private T getObjectZero() {
        return null; //TODO!!! from ring?
    }

    /**
     * True, if <code>x</code> is the zero factor object.
     */
    private boolean isObjectZero(T x) {
        return x.isZero();
    }

    /**
     * Add string <code>word</code> with factor <code>factor</code> to this.
     */
    protected void add(String word, T factor) {
        if (dict.containsKey(word)) {
            T newFactor = factor.sum(dict.get(word));
            if (!isObjectZero(newFactor)) {
                dict.put(word, newFactor);
            } else {
                dict.remove(word);
            }
        } else if (!isObjectZero(factor)) {
            dict.put(word, factor);
        }
    }

    /**
     * Subtract string <code>word</code> with factor <code>factor</code> to this.
     */
    protected void subtract(String word, T factor) {
        if (dict.containsKey(word)) {
            T newFactor = dict.get(word).difference(factor);
            if (!isObjectZero(newFactor)) {
                dict.put(word, newFactor);
            } else {
                dict.remove(word);
            }
        } else if (!isObjectZero(factor)) {
            dict.put(word, factor);
        }
    }
    
    public int hashCode() {
        return dict.hashCode();
    }
    
    
    public String toString() {
        StringBuilder buf = new StringBuilder(dict.size()*10);
        buf.append("[");

        String word;
        Object factor;
        Iterator<String> keys = dict.keySet().iterator();
        if (keys.hasNext()) {
            word = keys.next();
            factor = dict.get(word);
            buf.append(factor);
            buf.append("*");
            buf.append("\"");
            buf.append(TextUtils.escape(word));
            buf.append("\"");
            while (keys.hasNext()) {
                word = keys.next();
                factor = dict.get(word);
                buf.append("+");
                buf.append(factor);
                buf.append("*");
                buf.append("\"");
                buf.append(TextUtils.escape(word));
                buf.append("\"");
            }
        }
        buf.append("]");
        return buf.toString();
    }

    public static double stringToDouble(String s) {
        double sum = 0.0;
        double oneByAscii = 1.0 / 256.0;
        double factor = oneByAscii;
        for (int i = 0; i < s.length(); i++) {
            sum += factor * Character.getNumericValue(s.charAt(i));
            factor *= oneByAscii;
        }
        return sum;
    }

    @Override
    public RingString<T> deepCopy() {
        return new RingString<>(this);
    }

    /*
     * Implementation of RingString Folding.
     */
    static class Word implements Comparable<Word> {

        public Word(String s, double f) {
            word = s;
            word_value = stringToDouble(s);
            word_factor = f;
        }

        @Override
        public int compareTo(Word x) {
            if (word_value != x.word_value) {
                return Double.compare(word_value, x.word_value);
            }
            return Double.compare(word_factor, x.word_factor);
        }

        public String toString() {
            return "["+word+","+word_value+","+word_factor+"]";
        }

        String word;
        double word_value;
        double word_factor;
        double word_low;
        double word_high;
        double fold_value;
        double next_fold;
    }

    private Word[] wordToArray() {
        Word res[] = new Word[dict.size()];
        String word;
        T factor;
        int i = 0;
        Iterator<String> keys = dict.keySet().iterator();
        while (keys.hasNext()) {
            word = keys.next();
            factor = dict.get(word);
            res[i++] = new Word(word, factor.doubleValue());
        }
        Arrays.sort(res);
        return res;
    }

    public static double[] fold(RingString[] elements) {
        Word[][] strings = new Word[elements.length][];
        int nr_words = 0;
        for (int i = 0; i < elements.length; i++) {
            strings[i] = elements[i].wordToArray();
            nr_words += strings[i].length;
        }

        // All words into a sorted array
        Word[] words = new Word[nr_words];
        int i = 0;
        for (int j = 0; j < elements.length; j++) {
            for (int k = 0; k < strings[j].length; k++) {
                words[i] = strings[j][k];
                i++;
            }
        }
        Arrays.sort(words);

        // Special case: all words are identical
        if (words[0].word_value == words[words.length - 1].word_value) {
            double l = words[0].word_value - 1.0 / 2;
            double h = words[0].word_value + 1.0 / 2;
            for (int j = 0; j < words.length; j++) {
                words[j].word_low = l;
                words[j].word_high = h;
            }
            computeOneFold(words);
        }
        else {
            // General case
            computeHighLow(words);
            computeOneFold(words);
            computeNextFold(words);
        }

        double[] res = new double[elements.length];
        for (int j = 0; j < res.length; j++) {
            res[j] = computeWordFold(strings[j]);
        }

        return res;
    }

    private static void computeHighLow(Word[] words) {
        for (int i = 0; i < words.length; i++) {
            words[i].word_low = (words[i].word_value + searchLow(words, i)) / 2;
            words[i].word_high = (words[i].word_value + searchHigh(words, i)) / 2;
        }
    }

    private static double searchLow(Word[] words, int i) {
        int j = i - 1;
        while (j >= 0 && words[j].word_value == words[i].word_value)
            j--;
        if (j < 0)
            return words[i].word_value + words[i].word_value - searchHigh(words, i);
        return words[j].word_value;
    }

    private static double searchHigh(Word[] words, int i) {
        int j = i + 1;
        while (j < words.length && words[j].word_value == words[i].word_value)
            j++;
        if (j == words.length)
            return words[i].word_value + words[i].word_value - searchLow(words, i);
        return words[j].word_value;
    }

    private static void computeOneFold(Word[] words) {
        for (int i = 0; i < words.length; i++) {
            //	    words[i].fold_value = Folding.foldElement(words[i].word_value,
            //						      words[i].word_factor,
            //						      words[i].word_low,
            //						      words[i].word_high);
            words[i].fold_value =
                simpleArcTan(words[i].word_factor, words[i].word_low, words[i].word_high);
        }
    }

    private static void computeNextFold(Word[] words) {
        double last_fold_value = words[words.length - 1].fold_value;
        words[words.length - 1].next_fold = 0;
        for (int i = words.length - 2; i >= 0; i--) {
            if (words[i].fold_value == last_fold_value) {
                words[i].next_fold = words[i + 1].next_fold;
            }
            else {
                words[i].next_fold = words[i + 1].fold_value;
                last_fold_value = words[i].fold_value;
            }
        }
    }

    private static double computeWordFold(Word[] string) {
        int len = string.length;
        double res = 0.0;
        if (len > 0) {
            res = string[len - 1].fold_value;
            for (int i = len - 2; i >= 0; i--) {
                res = simpleArcTan(res, string[i].fold_value, string[i].next_fold);
            }
        }
        return res;
    }

    private static double simpleArcTan(double x, double a, double b) {
        double v = atan(x);
        return ((b - a) / PI) * v + (a + b) / 2;
    }

}
