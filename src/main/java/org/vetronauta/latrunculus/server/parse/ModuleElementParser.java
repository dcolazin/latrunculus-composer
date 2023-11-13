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

package org.vetronauta.latrunculus.server.parse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.element.generic.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.generic.DirectSumModule;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.ProductElement;
import org.vetronauta.latrunculus.core.math.module.generic.ProductRing;
import org.vetronauta.latrunculus.core.math.module.generic.RestrictedModule;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.StringVectorModule;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.element.generic.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.generic.ModularPolynomialRing;
import org.vetronauta.latrunculus.core.math.element.generic.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.generic.PolynomialRing;
import org.vetronauta.latrunculus.core.util.Wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModuleElementParser {

    //TODO make it a proper object?
    //TODO common logic

    public static <E extends ModuleElement<E,R>, R extends RingElement<R>> E parseElement(Module<E,R> module, String s) {
        if (module instanceof VectorModule) {
            return (E) parse((VectorModule) module, s);
        }
        if (module instanceof StringVectorModule) {
            return (E) parse((StringVectorModule) module, s);
        }
        if (module instanceof StringRing) {
            return (E) parse((StringRing) module, s);
        }
        if (module instanceof CRing) {
            return (E) parseC(s);
        }
        if (module instanceof RRing) {
            return (E) parseR(s);
        }
        if (module instanceof QRing) {
            return (E) parseQ(s);
        }
        if (module instanceof ZRing) {
            return (E) parseZ(s);
        }
        if (module instanceof ZnRing) {
            return (E) parseZn((ZnRing) module, s);
        }
        if (module instanceof DirectSumModule) {
            return (E) parse((DirectSumModule) module, s);
        }
        if (module instanceof ModularPolynomialRing) {
            return (E) parse((ModularPolynomialRing) module, s);
        }
        if (module instanceof PolynomialRing) {
            return (E) parse((PolynomialRing) module, s);
        }
        if (module instanceof ProductRing) {
            return (E) parse((ProductRing) module, s);
        }
        if (module instanceof RestrictedModule) {
            throw new UnsupportedOperationException("Not implemented");
        }
        throw new UnsupportedOperationException(String.format("cannot parse in %s", module));
    }

    private static <R extends RingElement<R>> Vector<R> parse(VectorModule<R> module, String string) {
        string = TextUtils.unparenthesize(string);
        String[] components = string.split(",");
        if (components.length != module.getDimension()) {
            return null;
        }
        List<R> values = new ArrayList<>(components.length);
        for (String component : components) {
            try {
                values.add(ArithmeticParsingUtils.parse(module.getRing(), component));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return new Vector<>(module.getRing(), values);
    }

    private static <R extends RingElement<R>> Vector<StringMap<R>> parse(StringVectorModule<R> module, String string) {
        string = TextUtils.unparenthesize(string);
        if (string.equals("Null")) {
            return new Vector<>(module.getRing(), new ArrayList<>());
        }
        if (string.charAt(0) == '(' && string.charAt(string.length()-1) == ')') {
            string = string.substring(1, string.length()-1);
            String[] strings = TextUtils.split(string, ',');
            if (strings.length != module.getDimension()) {
                return null;
            }
            else {
                List<StringMap<R>> rstrings = new ArrayList<>(module.getDimension());
                for (int i = 0; i < strings.length; i++) {
                    rstrings.add(ArithmeticParsingUtils.parseString(module.getRing(), strings[i]));
                }
                return new Vector<>(module.getRing(), rstrings);
            }
        }
        else {
            return null;
        }
    }

    private static <R extends RingElement<R>> StringMap<R> parse(StringRing<R> module, String string) {
        try {
            return ArithmeticParsingUtils.parseString(module, TextUtils.unparenthesize(string));
        } catch (Exception e) {
            return null;
        }
    }

    private static Complex parseC(String string) {
        try {
            return ArithmeticParsingUtils.parseComplex(TextUtils.unparenthesize(string));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private static Real parseR(String string) {
        try {
            double value = Double.parseDouble(TextUtils.unparenthesize(string));
            return new Real((value));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private static Rational parseQ(String string) {
        try {
            return ArithmeticParsingUtils.parseRational(TextUtils.unparenthesize(string));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private static ZInteger parseZ(String s) {
        try {
            int value = Integer.parseInt(TextUtils.unparenthesize(s));
            return new ZInteger(value);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private static Modulus parseZn(ZnRing ring, String s) {
        try {
            int value = Integer.parseInt(TextUtils.unparenthesize(s));
            return new Modulus(value, ring.getModulus());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private static DirectSumElement parse(DirectSumModule module, String string) {
        ArrayList<String> m = internalParseDirectSum(TextUtils.unparenthesize(string));
        if (m.size() != module.getDimension()) {
            return null;
        }

        List<ModuleElement<?,?>> comps = new ArrayList<>(module.getDimension());
        for (int i = 0; i < module.getDimension(); i++) {
            ModuleElement element = parseElement(module.getComponentModule(i), m.get(i));
            if (element == null) {
                return null;
            }
            comps.add(element);
        }
        return DirectSumElement.make((List) comps);
    }

    private static ArrayList<String> internalParseDirectSum(String s) {
        int pos = 0;
        int lastpos = 0;
        int level = 0;
        ArrayList<String> m = new ArrayList<>();
        while (pos < s.length()) {
            if (s.charAt(pos) == '(') {
                pos++;
                level++;
            }
            else if (s.charAt(pos) == ')') {
                pos++;
                level--;
            }
            else if (s.charAt(pos) == ',' && level == 0) {
                m.add(s.substring(lastpos, pos));
                pos++;
                lastpos = pos;
            }
            else {
                pos++;
            }
        }
        m.add(s.substring(lastpos,pos));
        return m;
    }

    private static ModularPolynomialElement parse(ModularPolynomialRing module, String string) {
        PolynomialElement p = (PolynomialElement) parseElement(module.getModulusRing(), string);
        if (p != null) {
            try {
                return new ModularPolynomialElement(module, p);
            }
            catch (IllegalArgumentException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    private static ArrayList<String> internalParsePolyProper(String s) {
        int pos = 0;
        int lastpos = 0;
        int level = 0;
        ArrayList<String> m = new ArrayList<String>();
        while (pos < s.length()) {
            if (s.charAt(pos) == '(') {
                pos++;
                level++;
            }
            else if (s.charAt(pos) == ')') {
                pos++;
                level--;
            }
            else if (s.charAt(pos) == ',' && level == 0) {
                m.add(s.substring(lastpos, pos));
                pos++;
                lastpos = pos;
            }
            else {
                pos++;
            }
        }
        m.add(s.substring(lastpos,pos).trim());
        return m;
    }

    private static <R extends RingElement<R>> PolynomialElement<R> parse(PolynomialRing<R> module, String string) {
        string = string.trim();
        if (string.equals("0")) {
            return module.getZero();
        }
        if (string.equals("1")) {
            return module.getOne();
        }

        ArrayList<String> m = internalParsePoly(TextUtils.unparenthesize(string));
        int[] exp = new int[1];
        Wrapper<R> factor = new Wrapper<>();
        java.util.Vector<R> terms = new java.util.Vector<>(30); //TODO do not use java.util.Vector
        terms.setSize(1);
        int maxexp = 0;
        try {
            for (String s : m) {
                if (splitTerm(module, s, exp, factor)) {
                    if (maxexp < exp[0]) {
                        maxexp = exp[0];
                        terms.setSize(exp[0] + 1);
                    }
                    if (terms.get(exp[0]) != null) {
                        (terms.get(exp[0])).add(factor.get());
                    } else {
                        terms.set(exp[0], factor.get());
                    }
                } else {
                    return null;
                }
            }
        }
        catch (DomainException e) {
            return null;
        }

        List<R> ringElements = new ArrayList<>(terms.size());
        for (R element : terms) {
            if (element == null) {
                element = module.getCoefficientRing().getZero();
            }
            ringElements.add(element);
        }

        return new PolynomialElement<>(module, ringElements);
    }

    private static ArrayList<String> internalParsePoly(String s) {
        int pos = 0;
        int lastpos = 0;
        int level = 0;
        ArrayList<String> m = new ArrayList<>();
        while (pos < s.length()) {
            if (s.charAt(pos) == '(') {
                pos++;
                level++;
            }
            else if (s.charAt(pos) == ')') {
                pos++;
                level--;
            }
            else if (s.charAt(pos) == '+' && level == 0) {
                m.add(s.substring(lastpos, pos));
                pos++;
                lastpos = pos;
            }
            else {
                pos++;
            }
        }
        m.add(s.substring(lastpos,pos).trim());
        return m;
    }

    private static <R extends RingElement<R>> boolean splitTerm(PolynomialRing<R> module, String s, int[] exp, Wrapper<R> factor) {
        String[] timesSplit = splitTimes(module, s);

        String[] indSplit = splitInd(timesSplit[1]);
        if (indSplit == null) {
            return false;
        }

        // get coefficient
        factor.set(parseElement(module.getCoefficientRing(), timesSplit[0]));
        if (factor.get() == null) {
            return false;
        }

        // check indeterminate
        if (!module.getIndeterminate().equals(indSplit[0])) {
            return false;
        }

        // get exponent
        try {
            exp[0] = Integer.parseInt(indSplit[1]);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return exp[0] >= 0;
    }

    private static <R extends RingElement<R>> String[] splitTimes(PolynomialRing<R> module, String s) {
        int pos = 0;
        int lastpos = 0;
        int level = 0;
        boolean seenTimes = false;
        String m[] = new String[2];
        while (pos < s.length()) {
            if (s.charAt(pos) == '(') {
                pos++;
                level++;
            }
            else if (s.charAt(pos) == ')') {
                pos++;
                level--;
            }
            else if (s.charAt(pos) == '*' && level == 0) {
                m[0] = s.substring(lastpos, pos);
                seenTimes = true;
                pos++;
                lastpos = pos;
            }
            else {
                pos++;
            }
        }
        if (seenTimes) {
            m[1] = s.substring(lastpos,pos).trim();
        }
        else if (s.startsWith(module.getIndeterminate())) {
            m[0] = "1";
            m[1] = s;
        }
        else {
            m[0] = s.trim();
            m[1] = module.getIndeterminate()+"^0";
        }
        return m;
    }

    private static String[] splitInd(String s) {
        String[] strings = s.split("\\^");
        if (strings.length == 2) {
            return strings;
        }
        else if (strings.length == 1) {
            return new String[] { strings[0], "1" };
        }
        else {
            return null;
        }
    }

    private static ArrayList<String> internalParseMulti(String s) {
        int pos = 0;
        int lastpos = 0;
        int level = 0;
        ArrayList<String> m = new ArrayList<String>();
        while (pos < s.length()) {
            if (s.charAt(pos) == '(') {
                pos++;
                level++;
            }
            else if (s.charAt(pos) == ')') {
                pos++;
                level--;
            }
            else if (s.charAt(pos) == ',' && level == 0) {
                m.add(s.substring(lastpos, pos));
                pos++;
                lastpos = pos;
            }
            else {
                pos++;
            }
        }
        m.add(s.substring(lastpos,pos));
        return m;
    }

    private static ProductElement parse(ProductRing ring, String string) {
        String s = string.trim();
        String re = "\\(.*\\)";
        if (s.matches(re)) {
            ArrayList<String> m = internalParseProduct(s.substring(1, s.length()-1));
            if (m.size() != ring.getFactorCount()) {
                return null;
            }
            RingElement factors0[] = new RingElement[ring.getFactorCount()];
            for (int i = 0; i < m.size(); i++) {
                factors0[i] = (RingElement) parseElement(ring.getFactor(i), m.get(i).trim());
                if (factors0[i] == null) {
                    return null;
                }
            }
            return ProductElement.make(factors0);
        }
        return null;
    }

    private static ArrayList<String> internalParseProduct(String s) {
        int pos = 0;
        int lastpos = 0;
        int level = 0;
        ArrayList<String> m = new ArrayList<String>();
        while (pos < s.length()) {
            if (s.charAt(pos) == '(') {
                pos++;
                level++;
            }
            else if (s.charAt(pos) == ')') {
                pos++;
                level--;
            }
            else if (s.charAt(pos) == ',' && level == 0) {
                m.add(s.substring(lastpos, pos));
                pos++;
                lastpos = pos;
            }
            else {
                pos++;
            }
        }
        m.add(s.substring(lastpos,pos));
        return m;
    }

}
