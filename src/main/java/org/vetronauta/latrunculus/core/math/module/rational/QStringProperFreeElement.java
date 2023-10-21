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

import lombok.NonNull;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Elements in the free module of QString.
 * @see QStringProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class QStringProperFreeElement extends ArithmeticStringMultiElement<QStringElement,Rational> {

    public static final QStringProperFreeElement nullElement = new QStringProperFreeElement(new ArrayList<>());

    private final List<RingString<Rational>> value;
    private FreeModule<?, ArithmeticElement<Rational>> module = null;

    public static FreeElement<?, QStringElement> make(List<RingString<Rational>> v) {
        assert(v != null);
        if (v.isEmpty()) {
            return nullElement;
        }
        else if (v.size() == 1) {
            return new QStringElement(v.get(0));
        }
        else {
            return new QStringProperFreeElement(v);
        }
    }
    
    public ModuleElement getComponent(int i) {
        assert(i < getLength());
        return new QStringElement(value.get(i));
    }
    

    public RingElement getRingElement(int i) {
        assert(i < getLength());
        return new QStringElement(value.get(i));
    }

    public Module getModule() { //TODO the return values of this method for the string are different than usual
        if (module == null) {
            module = ArithmeticMultiModule.make(QRing.ring, getLength());
        }
        return module;
    }

    public void setValue(int i, RingString<Rational> string) {
        value.add(string);
    }
    

    public FreeElement resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            List<RingString<Rational>> values = new ArrayList<>(n);
            for (int i = 0; i < minlen; i++) {
                values.add( getValue(i));
            }
            for (int i = minlen; i < n; i++) {
                values.add(RingString.getZero());
            }
            return QStringProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof QStringProperFreeElement) {
            QStringProperFreeElement e = (QStringProperFreeElement) object;
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
        if (object instanceof QStringProperFreeElement) {
            QStringProperFreeElement element = (QStringProperFreeElement)object;
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
            res.append(value.get(0).stringRep());
            for (int i = 1; i < getLength(); i++) {
                res.append(',');
                res.append(value.get(i).stringRep());
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
        StringBuilder res = new StringBuilder(30);
        res.append("QStringFreeElement[");
        res.append(getLength());
        res.append("][");
        if (getLength() > 0) {
            res.append(value.get(0));
            for (int i = 1; i < getLength(); i++) {
                res.append(",");
                res.append(value.get(i));
            }
        }
        res.append("]");
        return res.toString();
    }
    

    public double[] fold(ModuleElement[] elements) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getElementTypeName() {
        return "QStringFreeElement";
    }
    
    
    public int hashCode() {
        int val = 0;
        for (int i = 0; i < getLength(); i++) {
            val ^= value.get(i).hashCode();
        }
        return val;
    }
    

    private QStringProperFreeElement(List<RingString<Rational>> value) {
        super(value);
        this.value = value;
    }

    @Override
    protected ArithmeticStringMultiElement<QStringElement, Rational> valueOf(@NonNull List<RingString<Rational>> value) {
        return new QStringProperFreeElement(value);
    }

    private QStringProperFreeElement(RingString<Rational>[] value) {
        super(Arrays.stream(value).collect(Collectors.toList()));
        this.value = Arrays.stream(value).collect(Collectors.toList());
    }

}
