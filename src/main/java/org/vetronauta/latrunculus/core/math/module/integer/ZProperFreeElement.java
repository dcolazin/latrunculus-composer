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

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;

import java.util.Arrays;

/**
 * Elements in a free module over integers.
 * @see ZProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class ZProperFreeElement extends ProperFreeElement<ZProperFreeElement,ZElement> implements ZFreeElement<ZProperFreeElement> {

    public static ZFreeElement nullElement = new ZProperFreeElement(new int[0]);

    private final int[] value;
    private ZProperFreeModule module;

    public static ZFreeElement make(int[] v) {
        assert(v != null);
        if (v.length == 0) {
            return nullElement;
        }
        else if (v.length == 1) {
            return new ZElement(v[0]);
        }
        else {
            return new ZProperFreeElement(v);
        }
    }

    
    public boolean isZero() {
        for (int i = 0; i < value.length; i++) {
            if (value[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    public ZProperFreeElement sum(ZProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            int res[] = new int[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i] + element.value[i];
            }
            return new ZProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void add(ZProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value[i] += element.value[i];
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public ZProperFreeElement difference(ZProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            int res[] = new int[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i] - element.value[i];
            }
            return new ZProperFreeElement(res);        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void subtract(ZProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value[i] -= element.value[i];
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public ZProperFreeElement productCW(ZProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            int[] res = new int[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i]*element.value[i];
            }
            return new ZProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void multiplyCW(ZProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value[i] *= element.value[i];
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public ZProperFreeElement negated() {
        int[] res = new int[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = -value[i];
        }
        return new ZProperFreeElement(res);
    }

    
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value[i] = -value[i];
        }
    }

    public ZProperFreeElement scaled(ZElement element) {
        int val = element.getValue();
        int res[] = new int[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i]*val;
        }
        return new ZProperFreeElement(res);        
    }

    public void scale(ZElement element) {
        int val = element.getValue();
        for (int i = 0; i < getLength(); i++) {
            value[i] *= val;
        }
    }

    
    public ZElement getComponent(int i) {
        assert(i < getLength());
        return new ZElement(value[i]);
    }
    

    public ZElement getRingElement(int i) {
        assert(i < getLength());
        return new ZElement(value[i]);
    }
    

    public int getLength() {
        return value.length;
    }
    

    public ZProperFreeModule getModule() {
        if (module == null) {
            module = (ZProperFreeModule)ZProperFreeModule.make(getLength());
        }
        return module;
    }
    

    public int[] getValue() {
        return value;
    }
    

    public int getValue(int i) {
        return value[i];
    }
    

    public ZFreeElement resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            int[] values = new int[n];
            for (int i = 0; i < minlen; i++) {
                values[i] = getValue(i);
            }
            for (int i = minlen; i < n; i++) {
                values[i] = 0;
            }
            return ZProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof ZProperFreeElement) {
            ZProperFreeElement e = (ZProperFreeElement) object;
            if (getLength() == e.getLength()) {
                for (int i = 0; i < getLength(); i++) {
                    if (value[i] != e.value[i]) {
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
        if (object instanceof ZProperFreeElement) {
            ZProperFreeElement element = (ZProperFreeElement)object;
            int l = getLength()-element.getLength();
            if (l != 0) {
                return l;
            }
            else {
                for (int i = 0; i < getLength(); i++) {
                    int d = value[i]-element.value[i];
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
            res.append(value[0]);
            for (int i = 1; i < getLength(); i++) {
                res.append(',');
                res.append(value[i]);
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
            buf.append(value[0]);
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(value[i]);
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
                res[i][j] = ((ZProperFreeElement)elements[i]).getValue()[j];
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
            val ^= value[i];
        }
        return val;
    }
    

    private ZProperFreeElement(int[] value) {
        this.value = value;
    }

    @Override
    public ModuleElement deepCopy() {
        return new ZProperFreeElement(Arrays.copyOf(value, value.length));
    }
}
