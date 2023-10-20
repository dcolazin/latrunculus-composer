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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;

import java.util.ArrayList;
import java.util.List;


/**
 * Elements in a free module of ZString.
 * @see ZStringProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class ZStringProperFreeElement extends ProperFreeElement<ZStringProperFreeElement,ZStringElement> implements FreeElement<ZStringProperFreeElement,ZStringElement> {

    public static ZStringProperFreeElement nullElement = new ZStringProperFreeElement(new ArrayList<>());

    private final List<RingString<ArithmeticInteger>> value;
    private FreeModule<?, ArithmeticElement<ArithmeticInteger>> module;

    public static FreeElement<?,ZStringElement> make(List<RingString<ArithmeticInteger>> v) {
        assert(v != null);
        if (v.isEmpty()) {
            return nullElement;
        }
        else if (v.size() == 1) {
            return new ZStringElement(v.get(0));
        }
        else {
            return new ZStringProperFreeElement(v);
        }
    }

    
    public boolean isZero() {
        RingString<ArithmeticInteger> zero = RingString.getZero();
        for (int i = 0; i < getLength(); i++) {
            if (!value.get(i).equals(zero)) {
                return false;
            }
        }
        return true;
    }
    
    public ZStringProperFreeElement sum(ZStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<ArithmeticInteger>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).sum(element.value.get(i)));
            }
            return new ZStringProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void add(ZStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).add(element.value.get(i));
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public ZStringProperFreeElement difference(ZStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<ArithmeticInteger>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).difference(element.value.get(i)));
            }
            return new ZStringProperFreeElement(res);        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void subtract(ZStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).subtract(element.value.get(i));
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public ZStringProperFreeElement productCW(ZStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<ArithmeticInteger>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).product(element.value.get(i)));
            }
            return new ZStringProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void multiplyCW(ZStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).add(element.value.get(i));
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public ZStringProperFreeElement negated() {
        List<RingString<ArithmeticInteger>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).negated());
        }
        return new ZStringProperFreeElement(res);
    }

    
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value.get(i).negate();
        }
    }

    public ZStringProperFreeElement scaled(ZStringElement element) {
        RingString<ArithmeticInteger> val = element.getValue();
        List<RingString<ArithmeticInteger>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).product(val));
        }
        return new ZStringProperFreeElement(res);        
    }

    public void scale(ZStringElement element) {
        RingString<ArithmeticInteger> val = element.getValue();
        for (int i = 0; i < getLength(); i++) {
            value.get(i).multiply(val);
        }
    }

    
    public ModuleElement getComponent(int i) {
        assert(i < getLength());
        return new ZStringElement(value.get(i));
    }
    

    public RingElement getRingElement(int i) {
        assert(i < getLength());
        return new ZStringElement(value.get(i));
    }
    

    public int getLength() {
        return value.size();
    }
    

    public Module getModule() { //TODO this is wrong, the meaning of getModule is different from usual getModules types
        if (module == null) {
            module = ArithmeticMultiModule.make(ZRing.ring, getLength());
        }
        return module;
    }
    

    public List<RingString<ArithmeticInteger>> getValue() {
        return value;
    }
    

    public RingString<ArithmeticInteger> getValue(int i) {
        return value.get(i);
    }
    

    public FreeElement resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            List<RingString<ArithmeticInteger>> values = new ArrayList<>(n);
            for (int i = 0; i < minlen; i++) {
                values.add(getValue(i));
            }
            for (int i = minlen; i < n; i++) {
                values.add(RingString.getZero());
            }
            return ZStringProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof ZStringProperFreeElement) {
            ZStringProperFreeElement e = (ZStringProperFreeElement) object;
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
        if (object instanceof ZProperFreeElement) {
            ZStringProperFreeElement element = (ZStringProperFreeElement)object;
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
        StringBuilder buf = new StringBuilder(50);
        buf.append("ZStringFreeElement[");
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
        return "ZStringFreeElement";
    }
    
    
    public int hashCode() {
        int val = 0;
        for (int i = 0; i < getLength(); i++) {
            val ^= value.get(i).hashCode();
        }
        return val;
    }
    

    private ZStringProperFreeElement(List<RingString<ArithmeticInteger>> value) {
        this.value = value;
    }

    @Override
    public ZStringProperFreeElement deepCopy() {
        List<RingString<ArithmeticInteger>> v = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            v.add(value.get(i).deepCopy());
        }
        return new ZStringProperFreeElement(v);
    }
}
