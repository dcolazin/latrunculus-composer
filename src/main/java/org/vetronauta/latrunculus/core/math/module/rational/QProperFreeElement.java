/*
 * Copyright (C) 2001, 2005 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.math.module.rational;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Elements in a free module of rationals.
 *
 * @author Gérard Milmeister
 */
public final class QProperFreeElement extends ArithmeticMultiElement<Rational> {

    private QProperFreeElement(List<ArithmeticElement<Rational>> array) {
        super(QRing.ring, array);
    }

    public int compareTo(ModuleElement object) {
        if (object instanceof QProperFreeElement) {
            QProperFreeElement element = (QProperFreeElement)object;
            int l = getLength()-element.getLength();
            if (l != 0) {
                return l;
            }
            else {
                for (int i = 0; i < getLength(); i++) {
                    int c = getValue().get(i).compareTo(element.getValue().get(i));
                    if (c != 0) {
                        return c;
                    }
                }
                return 0;
            }
        }
        else {
            return super.compareTo(object);
        }
    }
    
    public String stringRep(boolean ... parens) {
        if (getLength() == 0) {
            return "Null";
        }
        else {
            StringBuilder res = new StringBuilder(getValue().get(0).toString());
            for (int i = 1; i < getLength(); i++) {
                res.append(',');
                res.append(getValue().get(i));
            }
            if (parens.length > 0) {
                return TextUtils.parenthesize(res.toString());
            }
            else {
                return res.toString();
            }
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("QFreeElement[");
        buf.append(getLength());
        buf.append("][");
        if (getLength() > 0) {
            buf.append(getValue().get(0));
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(getValue().get(i));
            }
        }
        buf.append("]");
        return buf.toString();
    }

    public double[] fold(ModuleElement[] elements) {
        double[][] res = new double[elements.length][];
        // Create an array of double arrays corresponding
        // to the array of RFreeElements
        for (int i = 0; i < elements.length; i++) {
            List<ArithmeticElement<Rational>> r = ((QProperFreeElement)elements[i]).getValue();
            res[i] = new double[elements.length];
            for (int j = 0; i < elements.length; j++) {
                res[i][j] = r.get(j).getValue().doubleValue();
            }
        }
        return Folding.fold(res);
    }

    public String getElementTypeName() {
        return "QFreeElement";
    }

}
