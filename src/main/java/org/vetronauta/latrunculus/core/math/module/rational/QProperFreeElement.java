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
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZElement;

/**
 * Elements in a free module of rationals.
 * @see QProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class QProperFreeElement extends ArithmeticMultiElement<QElement> {

    public static final QProperFreeElement nullElement = new QProperFreeElement(new QElement[0]);

    private QProperFreeElement(QElement[] array) {
        super(array);
    }

    public static FreeElement<?, QElement> make(Rational[] v) {
        assert(v != null);
        if (v.length == 0) {
            return nullElement;
        }
        else if (v.length == 1) {
            return new QElement(v[0]);
        }
        else {
            return new QProperFreeElement(toElementArray(v));
        }
    }

    private static QElement[] toElementArray(Rational[] v) {
        QElement[] elements = new QElement[v.length];
        for (int i = 0; i < v.length; i++) {
            elements[i] = new QElement(v[i]);
        }
        return elements;
    }

    public static FreeElement<?, QElement> make(ZElement[] v) {
        assert(v != null);
        if (v.length == 0) {
            return nullElement;
        }
        else if (v.length == 1) {
            return new QElement(v[0].getValue().intValue());
        }
        else {
            Rational[] values = new Rational[v.length];
            for (int i = 0; i < v.length; i++) {
                values[i] = new Rational(v[i].getValue().intValue());
            }
            return new QProperFreeElement(toElementArray(values));
        }
    }
    
    public QProperFreeModule getModule() {
        if (module == null) {
            module = (QProperFreeModule)QProperFreeModule.make(getLength());
        }
        return module;
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
                    int c = getValue()[i].compareTo(element.getValue()[i]);
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
            StringBuilder res = new StringBuilder(getValue()[0].toString());
            for (int i = 1; i < getLength(); i++) {
                res.append(',');
                res.append(getValue()[i]);
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
            buf.append(getValue()[0]);
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(getValue()[i]);
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
            QElement[] r = ((QProperFreeElement)elements[i]).getValue();
            res[i] = new double[elements.length];
            for (int j = 0; i < elements.length; j++) {
                res[i][j] = r[j].getValue().doubleValue();
            }
        }
        return Folding.fold(res);
    }

    public String getElementTypeName() {
        return "QFreeElement";
    }

    private QProperFreeModule module = null;

}
