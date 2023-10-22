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
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Free modules over ZStringRing.
 *
 * @author Gérard Milmeister
 */
public final class ZStringProperFreeModule extends ProperFreeModule<ArithmeticStringMultiElement<ArithmeticInteger>,ArithmeticStringElement<ArithmeticInteger>> {

    public static final ZStringProperFreeModule nullModule = new ZStringProperFreeModule(0);

    public static FreeModule<?, ArithmeticStringElement<ArithmeticInteger>> make(int dimension) {
        dimension = Math.max(dimension, 0);
        if (dimension == 0) {
            return nullModule;
        }
        else if (dimension == 1) {
            return ZStringRing.ring;
        }
        else {
            return new ZStringProperFreeModule(dimension);
        }
    }
    
    
    public ArithmeticStringMultiElement getZero() {
        List<RingString<ArithmeticInteger>> res = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            res.add(RingString.getZero());
        }
        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ZRing.ring, res); //TODO do not cast
    }
    
    
    public ArithmeticStringMultiElement getUnitElement(int i) {
        List<RingString<ArithmeticInteger>> res = new ArrayList<>(getDimension());
        for (int j = 0; j < getDimension(); j++) {
            res.add(RingString.getZero());
        }
        res.set(i, RingString.getOne());
        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ZRing.ring, res);
    }
    

    public Module getNullModule() {
        return nullModule;
    }
    
    
    public boolean isNullModule() {
        return this == nullModule;
    }

    
    public Module getComponentModule(int i) {
        return ZStringRing.ring;
    }

    
    public Ring getRing() {
        return ZStringRing.ring;
    }


    public boolean isVectorSpace() {
        return false;
    }

    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof ArithmeticStringMultiElement &&
                element.getLength() == getDimension() && element.getModule().getRing().equals(getRing()));
    }


    public int compareTo(Module object) {
        if (object instanceof ZStringProperFreeModule) {
            ZStringProperFreeModule module = (ZStringProperFreeModule)object;
            return getDimension()-module.getDimension();
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public ArithmeticStringMultiElement<ArithmeticInteger> createElement(List<ModuleElement<?, ?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }

        Iterator<ModuleElement<?, ?>> iter = elements.iterator();
        List<RingString<ArithmeticInteger>> values = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            Object object = iter.next();
            if (object instanceof ArithmeticStringElement) {
                values.add(((ArithmeticStringElement<ArithmeticInteger>)object).getValue());
            } else {
                return null;
            }
        }
        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ZRing.ring, values);
    }
    
   
    public ArithmeticStringMultiElement cast(ModuleElement element) {
        if (element.getLength() > getDimension()) {
            ZStringRing ring = ZStringRing.ring;
            List<ModuleElement<?,?>> elementList = new LinkedList<>();
            for (int i = 0; i < getDimension(); i++) {
                ModuleElement e = ring.cast(element.getComponent(i));
                if (e != null) {
                    elementList.add(e);
                }
                else {
                    return null;
                }
            }
            return createElement(elementList);
        }
        return null;
    }

    
    public boolean equals(Object object) {
        return (object instanceof ZStringProperFreeModule &&
                getDimension() == ((ZStringProperFreeModule)object).getDimension());
    }

    
    public ArithmeticStringMultiElement parseString(String string) {
        string = TextUtils.unparenthesize(string);
        if (string.equals("Null")) {
            return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ZRing.ring, new ArrayList<>());
        }
        if (string.charAt(0) == '(' && string.charAt(string.length()-1) == ')') {
            string = string.substring(1, string.length()-1);
            String[] strings = TextUtils.split(string, ',');
            if (strings.length != getDimension()) {
                return null;
            }
            else {
                List<RingString<ArithmeticInteger>> zstrings = new ArrayList<>(getDimension());
                for (int i = 0; i < strings.length; i++) {
                    zstrings.add(ZStringRing.parse(strings[i]));
                }
                return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ZRing.ring, zstrings);
            }            
        }
        return null;
    }
    
    
    public String toString() {
        return "ZStringFreeModule["+getDimension()+"]";
    }


    public String toVisualString() {
        return "(Z-String)^"+getDimension();
    }
    
    public String getElementTypeName() {
        return "ZStringFreeModule";
    }

    public int hashCode() {
        return 37*basicHash + getDimension();
    }
    
    
    protected ModuleMorphism _getProjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), getDimension(), 1);
        m.setMatrix(0, index, getRing().getOne());
        return m;
    }
    
    
    protected ModuleMorphism _getInjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), 1, getDimension());
        m.setMatrix(index, 0, getRing().getOne());
        return m;
    }

    
    private ZStringProperFreeModule(int dimension) {
        super(dimension);
    }


    private static final int basicHash = "ZStringFreeModule".hashCode();
}
