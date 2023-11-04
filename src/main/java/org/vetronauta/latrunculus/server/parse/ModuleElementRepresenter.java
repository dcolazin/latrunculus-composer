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
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialRing;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModuleElementRepresenter {

    //TODO make it a proper object?
    //TODO common logic

    public static String stringRepresentation(ModuleElement<?,?> element, boolean... parenthesis) {
        if (element instanceof ArithmeticElement) {
            return toString((ArithmeticElement<?>) element, parenthesis);
        }
        if (element instanceof Vector) {
            return toString((Vector<?>) element, parenthesis);
        }
        if (element instanceof StringMap) {
            return (stringRepresentation(((StringMap<?>) element).getTerms()));
        }
        if (element instanceof ArithmeticStringMultiElement) {
            return toString((ArithmeticStringMultiElement<?>) element, parenthesis);
        }
        if (element instanceof DirectSumElement) {
            return toString((DirectSumElement<?>) element, parenthesis);
        }
        if (element instanceof ModularPolynomialElement) {
            return toString((ModularPolynomialElement<?>) element, parenthesis);
        }
        if (element instanceof ModularPolynomialProperFreeElement) {
            return toString((ModularPolynomialProperFreeElement<?>) element, parenthesis);
        }
        if (element instanceof PolynomialElement) {
            return toString((PolynomialElement<?>) element, parenthesis);
        }
        if (element instanceof PolynomialProperFreeElement) {
            return toString((PolynomialProperFreeElement<?>) element, parenthesis);
        }
        if (element instanceof ProductElement) {
            return toString((ProductElement) element, parenthesis);
        }
        if (element instanceof ProductProperFreeElement) {
            return toString((ProductProperFreeElement) element, parenthesis);
        }
        if (element instanceof RestrictedElement) {
            // TODO: not yet implemented
            throw new UnsupportedOperationException("Not implemented");
        }
        throw new UnsupportedOperationException(String.format("cannot parse %s", element));
    }

    private static String toString(ArithmeticElement<?> element, boolean... parens) {
        String representation = element.getValue().toString();
        if (parens.length > 0 && !Character.isDigit(representation.charAt(0))) {
            return TextUtils.parenthesize(representation);
        }
        return representation;
    }

    private static String toString(Vector<?> element, boolean... parens) {
        if (element.getLength() == 0) {
            return "Null";
        }
        StringBuilder res = new StringBuilder(30);
        res.append(element.getValue().get(0));
        for (int i = 1; i < element.getLength(); i++) {
            res.append(',');
            res.append(element.getValue().get(i));
        }
        return parens.length > 0 ? TextUtils.parenthesize(res.toString()) : res.toString();
    }

    private static String toString(ArithmeticStringMultiElement<?> element, boolean... parens) {
        if (element.getLength() == 0) {
            return "Null";
        }
        StringBuilder res = new StringBuilder(30);
        res.append(stringRepresentation(element.getValue().get(0)));
        for (int i = 1; i < element.getLength(); i++) {
            res.append(',');
            res.append(stringRepresentation(element.getValue().get(i)));
        }
        return parens.length > 0 ? TextUtils.parenthesize(res.toString()) : res.toString();
    }

    private static String toString(DirectSumElement element, boolean... parens) {
        if (element.getLength() == 0) {
            return "Null["+element.getRing()+"]";
        }
        else {
            StringBuilder res = new StringBuilder("("+element.getComponent(0)+")");
            for (int i = 1; i < element.getLength(); i++) {
                res.append(',');
                res.append('(');
                res.append(element.getComponent(i));
                res.append(')');
            }
            if (parens.length > 0) {
                return TextUtils.parenthesize(res.toString());
            }
            else {
                return res.toString();
            }
        }
    }

    private static String toString(ModularPolynomialElement element, boolean... parens) {
        return stringRepresentation(element.getPolynomial(), parens);
    }

    private static String toString(ModularPolynomialProperFreeElement element, boolean ... parens) {
        if (element.getLength() == 0) {
            return "Null";
        }
        else {
            StringBuilder buf = new StringBuilder(30);
            buf.append(stringRepresentation(element.getValue(0)));
            for (int i = 1; i < element.getLength(); i++) {
                buf.append(",");
                buf.append(stringRepresentation(element.getValue(i)));
            }
            if (parens.length > 0) {
                return TextUtils.parenthesize(buf.toString());
            }
            else {
                return buf.toString();
            }
        }
    }

    private static String toString(PolynomialElement<?> element, boolean ... parens) {
        if (element.isZero()) {
            return "0";
        }
        if (element.isOne()) {
            return "1";
        }
        boolean paren = element.getRing().getCoefficientRing() instanceof PolynomialRing ||
                element.getRing().getCoefficientRing() instanceof CRing;
        StringBuilder buf = new StringBuilder(30);
        String ind = element.getRing().getIndeterminate();
        if (paren) {
            buf.append("(");
        }
        buf.append(stringRepresentation(element.getCoefficients().get(element.getCoefficients().size()-1),true));
        if (paren) {
            buf.append(")");
        }
        if (element.getCoefficients().size()-1 > 1) {
            buf.append("*");
            buf.append(ind);
            buf.append("^");
            buf.append(element.getCoefficients().size()-1);
        }
        else if (element.getCoefficients().size()-1 == 1) {
            buf.append("*");
            buf.append(ind);
        }
        for (int i = element.getCoefficients().size()-2; i >= 0; i--) {
            if (!element.getCoefficients().get(i).isZero()) {
                buf.append("+");
                if (paren) {
                    buf.append("(");
                }
                buf.append(stringRepresentation(element.getCoefficients().get(i),true));
                if (paren) {
                    buf.append(")");
                }
                if (i > 1) {
                    buf.append("*");
                    buf.append(ind);
                    buf.append("^");
                    buf.append(i);
                }
                else if (i == 1) {
                    buf.append("*");
                    buf.append(ind);
                }
            }
        }
        if (parens.length > 0) {
            return TextUtils.parenthesize(buf.toString());
        }
        else {
            return buf.toString();
        }
    }

    private static String toString(PolynomialProperFreeElement element, boolean ... parens) {
        if (element.getLength() == 0) {
            return "Null";
        }
        else {
            StringBuilder buf = new StringBuilder(30);
            buf.append(stringRepresentation(element.getValue(0)));
            for (int i = 1; i < element.getLength(); i++) {
                buf.append(",");
                buf.append(stringRepresentation(element.getValue(i)));
            }
            if (parens.length > 0) {
                return TextUtils.parenthesize(buf.toString());
            }
            else {
                return buf.toString();
            }
        }
    }

    private static String toString(ProductElement element, boolean ... parens) {
        StringBuilder buf = new StringBuilder(30);
        buf.append("(");
        if (element.getFactorCount() > 0) {
            buf.append(stringRepresentation(element.getFactor(0)));
        }
        for (int i = 1; i < element.getFactorCount(); i++) {
            buf.append(",");
            buf.append(stringRepresentation(element.getFactor(i)));
        }
        buf.append(")");
        return buf.toString();
    }

    private static String toString(ProductProperFreeElement element, boolean ... parens) {
        if (element.getLength() == 0) {
            return "Null";
        }
        else {
            StringBuilder res = new StringBuilder(stringRepresentation(element.getValue(0)));
            for (int i = 1; i < element.getLength(); i++) {
                res.append(',');
                res.append(stringRepresentation(element.getValue(i)));
            }
            if (parens.length > 0) {
                return TextUtils.parenthesize(res.toString());
            }
            else {
                return res.toString();
            }
        }
    }

    public static String stringRepresentation(Map<String,?> map) {
        String word;
        Object factor;
        StringBuilder buf = new StringBuilder();
        Set<String> keyset = map.keySet();
        if (keyset.isEmpty()) return "Null";
        Iterator<String> keys = keyset.iterator();
        if (keys.hasNext()) {
            word = keys.next();
            factor = map.get(word);
            buf.append(factor);
            buf.append("*");
            buf.append("\"");
            buf.append(TextUtils.escape(word));
            buf.append("\"");
            while (keys.hasNext()) {
                word = keys.next();
                factor = map.get(word);
                buf.append("+");
                buf.append(factor);
                buf.append("*");
                buf.append("\"");
                buf.append(TextUtils.escape(word));
                buf.append("\"");
            }
        }
        return buf.toString();
    }

}
