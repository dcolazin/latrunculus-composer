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

package org.vetronauta.latrunculus.core.math.module.real;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Elements in a free module over reals.
 * @see RProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class RProperFreeElement extends ArithmeticMultiElement<ArithmeticDouble> {

    public static RProperFreeElement nullElement = new RProperFreeElement(new double[0]);
    
    public static FreeElement<?, ArithmeticElement<ArithmeticDouble>> make(double[] v) {
        assert(v != null);
        if (v.length == 0) {
            return nullElement;
        }
        else if (v.length == 1) {
            return new ArithmeticElement<>(new ArithmeticDouble(v[0]));
        }
        else {
            return new RProperFreeElement(v);
        }
    }

    public static FreeElement<?, ArithmeticElement<ArithmeticDouble>> make(List<ArithmeticElement<ArithmeticDouble>> v) {
        assert(v != null);
        if (v.size() == 0) {
            return nullElement;
        }
        else if (v.size() == 1) {
            return v.get(0);
        }
        else {
            return new RProperFreeElement(v);
        }
    }

    @Override
    public ArithmeticMultiModule<ArithmeticDouble> getModule() {
        if (module == null) {
            module = new ArithmeticMultiModule<>(RRing.ring, getLength());
        }
        return module;
    }

    public int compareTo(ModuleElement object) {
        if (object instanceof RProperFreeElement) {
            RProperFreeElement element = (RProperFreeElement)object;
            int l = getLength()-element.getLength();
            if (l < 0) {
                return -1;
            }
            else if (l > 0) {
                return 1;
            }
            else {
                for (int i = 0; i < getLength(); i++) {
                    double d = getValue().get(i).difference(element.getValue().get(i)).getValue().doubleValue();
                    if (d < 0) {
                        return -1;
                    }
                    else if (d > 0) {
                        return 1;
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
            StringBuilder res = new StringBuilder(30);
            res.append(getValue().get(0));
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
        buf.append("RFreeElement[");
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
            res[i] = new double[elements.length];
            List<ArithmeticElement<ArithmeticDouble>> r = ((RProperFreeElement)elements[i]).getValue();
            for (int j = 0; i < elements.length; j++) {
                res[i][j] = r.get(j).getValue().doubleValue();
            }
        }
        return Folding.fold(res);
    }

    public String getElementTypeName() {
        return "RFreeElement";
    }
    
    private RProperFreeElement(double[] value) {
        super(Arrays.stream(value).mapToObj(ArithmeticDouble::new).map(ArithmeticElement::new).collect(Collectors.toList()));
    }

    private RProperFreeElement(List<ArithmeticElement<ArithmeticDouble>> value) {
        super(value);
    }
    
    private ArithmeticMultiModule<ArithmeticDouble> module = null;

}
