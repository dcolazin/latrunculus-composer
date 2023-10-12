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
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;

/**
 * Elements in a free module over reals.
 * @see RProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class RProperFreeElement extends ArithmeticMultiElement<RElement> {

    public static RProperFreeElement nullElement = new RProperFreeElement(new double[0]);
    
    public static FreeElement<?, RElement> make(double[] v) {
        assert(v != null);
        if (v.length == 0) {
            return nullElement;
        }
        else if (v.length == 1) {
            return new RElement(v[0]);
        }
        else {
            return new RProperFreeElement(v);
        }
    }

    public static FreeElement<?, RElement> make(RElement[] v) {
        assert(v != null);
        if (v.length == 0) {
            return nullElement;
        }
        else if (v.length == 1) {
            return v[0];
        }
        else {
            return new RProperFreeElement(v);
        }
    }

    public Module getModule() {
        if (module == null) {
            module = (RProperFreeModule) RProperFreeModule.make(getLength());
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
                    double d = getValue()[i].difference(element.getValue()[i]).getValue().doubleValue();
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
            res.append(getValue()[0]);
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
        buf.append("RFreeElement[");
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
            res[i] = new double[elements.length];
            RElement[] r = ((RProperFreeElement)elements[i]).getValue();
            for (int j = 0; i < elements.length; j++) {
                res[i][j] = r[j].getValue().doubleValue();
            }
        }
        return Folding.fold(res);
    }

    public String getElementTypeName() {
        return "RFreeElement";
    }
    
    private RProperFreeElement(double[] value) {
        super(toElementArray(value));
    }

    private RProperFreeElement(RElement[] value) {
        super(value);
    }

    private static RElement[] toElementArray(double[] v) {
        RElement[] elements = new RElement[v.length];
        for (int i = 0; i < v.length; i++) {
            elements[i] = new RElement(v[i]);
        }
        return elements;
    }

    
    private RProperFreeModule module = null;

}
