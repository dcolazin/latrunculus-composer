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
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeElement;

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
public final class QStringProperFreeElement extends ProperFreeElement<QStringProperFreeElement,QStringElement> implements FreeElement<QStringProperFreeElement,QStringElement> {

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
    
    public boolean isZero() {
        for (int i = 0; i < getLength(); i++) {
            if (!value.get(i).isZero()) {
                return false;
            }
        }
        return true;
    }
    
    public QStringProperFreeElement sum(QStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<Rational>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).sum(element.value.get(i)));
            }
            return new QStringProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void add(QStringProperFreeElement element)
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
    
    public QStringProperFreeElement difference(QStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<Rational>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).difference(element.value.get(i)));
            }
            return new QStringProperFreeElement(res);        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void subtract(QStringProperFreeElement element)
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
    
    public QStringProperFreeElement productCW(QStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<Rational>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).product(element.value.get(i)));
            }
            return new QStringProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void multiplyCW(QStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).multiply(element.value.get(i));
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    

    public QStringProperFreeElement negated() {
        List<RingString<Rational>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).negated());
        }
        return new QStringProperFreeElement(res);
    }

    
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value.get(i).negate();
        }
    }

    public QStringProperFreeElement scaled(QStringElement element) {
        RingString<Rational> val = element.getValue();
        List<RingString<Rational>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).product(val));
        }
        return new QStringProperFreeElement(res);        
    }

    public void scale(QStringElement element) {
        RingString<Rational> val = element.getValue();
        for (int i = 0; i < getLength(); i++) {
            value.get(i).multiply(val);
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
    

    public int getLength() {
        return value.size();
    }
    

    public Module getModule() { //TODO the return values of this method for the string are different than usual
        if (module == null) {
            module = QProperFreeModule.make(getLength());
        }
        return module;
    }
    

    public List<RingString<Rational>> getValue() {
        return value;
    }
    

    public RingString<Rational> getValue(int i) {
        return value.get(i);
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
        if (object instanceof ZProperFreeElement) {
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
        this.value = value;
    }

    private QStringProperFreeElement(RingString<Rational>[] value) {
        this.value = Arrays.stream(value).collect(Collectors.toList());
    }

    @Override
    public ModuleElement deepCopy() {
        List<RingString<Rational>> v = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            v.add(value.get(i).deepCopy());
        }
        return new QStringProperFreeElement(v);
    }
}
