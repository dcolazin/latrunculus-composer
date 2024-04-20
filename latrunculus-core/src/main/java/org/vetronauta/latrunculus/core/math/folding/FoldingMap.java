/*
 * latrunculus-composer
 * Copyright (C) 2023 vetronauta
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.vetronauta.latrunculus.core.math.folding;

import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Math.PI;
import static java.lang.Math.atan;

/**
 * @author vetronauta
 */
public class FoldingMap {

    public static double[] fold(List<Map<String, ZInteger>> elements) {
        Word[][] strings = new Word[elements.size()][];
        int nr_words = 0;
        for (int i = 0; i < elements.size(); i++) {
            strings[i] = wordToArray(elements.get(i));
            nr_words += strings[i].length;
        }

        // All words into a sorted array
        Word[] words = new Word[nr_words];
        int i = 0;
        for (int j = 0; j < elements.size(); j++) {
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

        double[] res = new double[elements.size()];
        for (int j = 0; j < res.length; j++) {
            res[j] = computeWordFold(strings[j]);
        }

        return res;
    }

    private static Word[] wordToArray(Map<String, ZInteger> rs) {
        Word[] res = new Word[rs.size()];
        int i = 0;
        for(Map.Entry<String,ZInteger> entry : rs.entrySet()) {
            res[i++] = new Word(entry.getKey(), entry.getValue().doubleValue());

        }
        Arrays.sort(res);
        return res;
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

    private static double stringToDouble(String s) {
        double sum = 0.0;
        double oneByAscii = 1.0 / 256.0;
        double factor = oneByAscii;
        for (int i = 0; i < s.length(); i++) {
            sum += factor * Character.getNumericValue(s.charAt(i));
            factor *= oneByAscii;
        }
        return sum;
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
