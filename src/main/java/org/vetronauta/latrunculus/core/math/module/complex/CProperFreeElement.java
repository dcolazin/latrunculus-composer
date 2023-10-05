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

package org.vetronauta.latrunculus.core.math.module.complex;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.module.definition.ConjugableElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;

/**
 * Elements in the free modules of complex numbers.
 * @see CProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class CProperFreeElement extends ArithmeticMultiElement<CElement> implements ConjugableElement<CProperFreeElement> {

    private CProperFreeModule module = null;

    private CProperFreeElement(CElement[] value) {
        super(value);
    }

    /**
     * Creates a CFreeElement from an array of Complex.
     */
    public static FreeElement<?, CElement> make(Complex[] v) { //TODO generalize as possible
        if (v.length == 1) {
            return new CElement(v[0]);
        }
        return new CProperFreeElement(toElementArray(v));
    }

    private static CElement[] toElementArray(Complex[] v) {
        CElement[] elements = new CElement[v.length];
        for (int i = 0; i < v.length; i++) {
            elements[i] = new CElement(v[i]);
        }
        return elements;
    }

    public CProperFreeElement conjugated() {
        CElement[] res = new CElement[getValue().length];
        for (int i = 0; i < getValue().length; i++) {
            res[i] = getValue()[i].conjugated();
        }
        return new CProperFreeElement(res);
    }

    public void conjugate() { //TODO hack
        CProperFreeElement conj = this.conjugated();
        subtract(this);
        add(conj);
    }

    @Override
    public CProperFreeModule getModule() {
        if (module == null) {
            module = (CProperFreeModule)CProperFreeModule.make(getLength());
        }
        return module;
    }

    @Override
    public int compareTo(ModuleElement object) {
        if (object instanceof CProperFreeElement) {
            CProperFreeElement element = (CProperFreeElement)object;
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

    @Override
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

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("CFreeElement[");
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
    
    @Override
    public double[] fold(ModuleElement<?,?>[] elements) {
        double[][] res = new double[elements.length][getLength()*2];
        for (int i = 0; i < elements.length; i++) {
            CElement[] c = ((CProperFreeElement)elements[i]).getValue();
            for (int j = 0; j < getLength(); j++) {
                res[i][2*j] = c[j].getValue().getReal();
                res[i][2*j+1] = c[j].getValue().getImag();
            }
        }
        return Folding.fold(res);
    }

    public String getElementTypeName() {
        return "CFreeElement";
    }

}
