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

package org.vetronauta.latrunculus.core.math.module.modular;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Elements in a free module over integers mod <i>n</i>.
 *
 * @author Gérard Milmeister
 */
public class ZnProperFreeElement extends ArithmeticMultiElement<ArithmeticModulus> {

    public static FreeElement<?, ArithmeticElement<ArithmeticModulus>> make(int[] v, int modulus) {
        assert(v != null);
        assert(modulus > 1);
        if (v.length == 0) {
            return new ZnProperFreeElement(toElementList(v, modulus), modulus);
        }
        else if (v.length == 1) {
            return new ArithmeticElement<>(new ArithmeticModulus(v[0], modulus));
        }
        else {
            return new ZnProperFreeElement(toElementList(v, modulus), modulus);
        }
    }

    public static FreeElement<?, ArithmeticElement<ArithmeticModulus>> make(List<ArithmeticElement<ArithmeticInteger>> v, int modulus) {
        assert(v != null);
        assert(modulus > 1);
        if (v.size() == 0) {
            return new ZnProperFreeElement(new ArrayList<>(), modulus);
        }
        else if (v.size() == 1) {
            return new ArithmeticElement<>(new ArithmeticModulus(v.get(0).getValue().intValue(), modulus));
        }
        else {
            return new ZnProperFreeElement(v.stream()
                    .map(ArithmeticElement::getValue)
                    .map(ArithmeticInteger::intValue)
                    .map(i -> new ArithmeticModulus(i, modulus))
                    .map(ArithmeticElement::new)
                    .collect(Collectors.toList()),
                    modulus);
        }
    }
    private static List<ArithmeticElement<ArithmeticModulus>> toElementList(int[] array, int modulus) {
        List<ArithmeticElement<ArithmeticModulus>> elements = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++) {
            elements.add(new ArithmeticElement<>(new ArithmeticModulus(array[i], modulus)));
        }
        return elements;
    }

    public Module getModule() {
        if (module == null) {
            module = (ZnProperFreeModule) ZnProperFreeModule.make(getLength(), modulus);
        }
        return module;
    }

    public int getModulus() {
        return modulus;
    }
    
    public int compareTo(ModuleElement object) {
        if (object instanceof ZnProperFreeElement) {
            ZnProperFreeElement element = (ZnProperFreeElement)object;
            int m = getModulus()-element.getModulus();
            if (m < 0) {
                return -1;
            }
            else if (m > 0) {
                return 1;
            }
            else {
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
        buf.append("ZnFreeElement(");
        buf.append(getModulus());
        buf.append(")[");
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
        int len = (elements[0]).getLength();
        // Create an array of double arrays corresponding
        // to the array of RFreeElements
        for (int i = 0; i < elements.length; i++) {
            res[i] = new double[len];
            for (int j = 0; j < len; j++) {
                res[i][j] = ((ZnProperFreeElement)elements[i]).getValue().get(j).getValue().intValue();
            }
        }
        return Folding.fold(res);
    }

    public String getElementTypeName() {
        return "ZnFreeElement";
    }

    private ZnProperFreeElement(List<ArithmeticElement<ArithmeticModulus>> value, int modulus) {
        super(value);
        this.modulus = modulus;
    }
    
    private int          modulus;
    private ZnProperFreeModule module = null;

}
