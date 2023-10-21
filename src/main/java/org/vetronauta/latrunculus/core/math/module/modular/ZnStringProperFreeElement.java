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

import lombok.NonNull;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;
import org.vetronauta.latrunculus.core.math.module.repository.ArithmeticRingRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Elements in a free modules over ZnString.
 * @see ZnStringProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class ZnStringProperFreeElement extends ArithmeticStringMultiElement<ZnStringElement,ArithmeticModulus> {

    //TODO various consistency checks for modulus

    private final List<RingString<ArithmeticModulus>> value;
    private final int modulus;
    private ArithmeticMultiModule<ArithmeticModulus> module;

    public static FreeElement<?, ZnStringElement> make(List<RingString<ArithmeticModulus>> v, int modulus) {
        if (v == null || v.isEmpty()) {
            return new ZnStringProperFreeElement(new ArrayList<>(0), modulus);
        }
        else if (v.size() == 1) {
            return new ZnStringElement(v.get(0), modulus);
        }
        return new ZnStringProperFreeElement(v, modulus);
    }

    @Override
    protected ArithmeticStringMultiElement<ZnStringElement, ArithmeticModulus> valueOf(@NonNull List<RingString<ArithmeticModulus>> value) {
        return new ZnStringProperFreeElement(value, value.get(0).getObjectOne().getModulus());
    }
    
    public ModuleElement getComponent(int i) {
        assert(i < getLength());
        return new ZnStringElement(value.get(i));
    }
    

    public RingElement getRingElement(int i) {
        assert(i < getLength());
        return new ZnStringElement(value.get(i));
    }

    public Module getModule() {
        if (module == null) {
            module = new ArithmeticMultiModule(ArithmeticRingRepository.getModulusRing(modulus), getLength());
        }
        return module;
    }

    public int getModulus() {
        return modulus;
    }
    
    
    public FreeElement resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            List<RingString<ArithmeticModulus>> values = new ArrayList<>(n);
            for (int i = 0; i < minlen; i++) {
                values.add(getValue(i));
            }
            for (int i = minlen; i < n; i++) {
                values.add(RingString.getZero());
            }
            return ZnStringProperFreeElement.make(values, modulus);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof ZnStringProperFreeElement) {
            ZnStringProperFreeElement e = (ZnStringProperFreeElement) object;
            if (getLength() == e.getLength()) {
                for (int i = 0; i < getLength(); i++) {
                    if (!value.get(i).equals(e.value.get(i))) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    
    public int compareTo(ModuleElement object) {
        if (object instanceof ZnStringProperFreeElement) {
            ZnStringProperFreeElement element = (ZnStringProperFreeElement)object;
            int l = getLength()-element.getLength();
            if (l != 0) {
                return l;
            }
            else {
                for (int i = 0; i < getLength(); i++) {
                    int d = value.get(i).compareTo(element.value.get(i));
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
            res.append("(");
            res.append(value.get(0).stringRep());
            for (int i = 1; i < getLength(); i++) {
                res.append(',');
                res.append(value.get(i).stringRep());
            }
            res.append(")");
            if (parens.length > 0) {
                return TextUtils.parenthesize(res.toString());
            }
            else {
                return res.toString();
            }
        }
    }

    
    public String toString() {
        StringBuilder buf = new StringBuilder(50);
        buf.append("ZnStringFreeElement[");
        buf.append(getLength());
        buf.append("][");
        if (getLength() > 0) {
            buf.append(value.get(0));
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(value.get(i));
            }
        }
        buf.append("]");
        return buf.toString();
    }
    

    public double[] fold(ModuleElement[] elements) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getElementTypeName() {
        return "ZnStringFreeElement";
    }
    
    
    public int hashCode() {
        int val = 0;
        for (int i = 0; i < getLength(); i++) {
            val ^= value.get(i).hashCode();
        }
        return val;
    }
    

    private ZnStringProperFreeElement(List<RingString<ArithmeticModulus>> value, int modulus) {
        super(value);
        this.value = value;
        this.modulus = modulus;
    }

}
