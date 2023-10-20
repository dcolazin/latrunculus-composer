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

package org.vetronauta.latrunculus.core.math.module.integer;

import lombok.NonNull;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Elements in a free module over integers.
 * @see ZProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class ZProperFreeElement extends ArithmeticMultiElement<ArithmeticInteger> {

    public static final ZProperFreeElement nullElement = new ZProperFreeElement(new ArrayList<>());

    private ArithmeticMultiModule<ArithmeticInteger> module;

    private ZProperFreeElement(List<ArithmeticElement<ArithmeticInteger>> value) {
        super(value);
    }

    public static FreeElement<?, ArithmeticElement<ArithmeticInteger>> make(@NonNull int[] v) {
        if (v.length == 0) {
            return nullElement;
        }
        else if (v.length == 1) {
            return new ArithmeticElement<>(new ArithmeticInteger(v[0]));
        }
        else {
            return new ZProperFreeElement(toElementList(v));
        }
    }

    public static FreeElement<?, ArithmeticElement<ArithmeticInteger>> make(@NonNull List<ArithmeticInteger> v) {
        if (v.isEmpty()) {
            return nullElement;
        }
        else if (v.size() == 1) {
            return new ArithmeticElement<>(v.get(0));
        }
        else {
            return new ZProperFreeElement(v.stream().map(ArithmeticElement::new).collect(Collectors.toList()));
        }
    }

    private static List<ArithmeticElement<ArithmeticInteger>> toElementList(int[] v) {
        List<ArithmeticElement<ArithmeticInteger>> elements = new ArrayList<>(v.length);
        for (int j : v) {
            elements.add(new ArithmeticElement<>(new ArithmeticInteger(j)));
        }
        return elements;
    }

    @Override
    public ArithmeticMultiModule<ArithmeticInteger> getModule() {
        if (module == null) {
            module = new ArithmeticMultiModule<>(ZRing.ring, getLength());
        }
        return module;
    }

    public int compareTo(ModuleElement object) {
        if (object instanceof ZProperFreeElement) {
            ZProperFreeElement element = (ZProperFreeElement)object;
            int l = getLength()-element.getLength();
            if (l != 0) {
                return l;
            }
            else {
                for (int i = 0; i < getLength(); i++) {
                    int d = getValue().get(i).getValue().intValue()-element.getValue().get(i).getValue().intValue();
                    if (d != 0) {
                        return d;
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
        buf.append("ZFreeElement[");
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
        assert(elements.length > 0);
        double[][] res = new double[elements.length][];
        int len = elements[0].getLength();
        // Create an array of double arrays corresponding
        // to the array of RFreeElements
        for (int i = 0; i < elements.length; i++) {
            res[i] = new double[len];
            for (int j = 0; j < len; j++) {
                res[i][j] = ((ZProperFreeElement)elements[i]).getValue().get(j).getValue().intValue();
            }
        }
        return Folding.fold(res);
    }

    public String getElementTypeName() {
        return "ZFreeElement";
    }
    
    
    public int hashCode() {
        int val = 0;
        for (int i = 0; i < getLength(); i++) {
            val ^= getValue().get(i).getValue().intValue();
        }
        return val;
    }

}
