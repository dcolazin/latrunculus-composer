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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
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
 * Free modules over RStringRing.
 *
 * @author Gérard Milmeister
 */
public final class RStringProperFreeModule extends ProperFreeModule<ArithmeticStringMultiElement<ArithmeticDouble>, ArithmeticStringElement<ArithmeticDouble>> {

    public static final RStringProperFreeModule nullModule = new RStringProperFreeModule(0);

    public static FreeModule<?, ArithmeticStringElement<ArithmeticDouble>> make(int dimension) {
        dimension = Math.max(dimension, 0);
        if (dimension == 0) {
            return nullModule;
        }
        else if (dimension == 1) {
            return RStringRing.ring;
        }
        else {
            return new RStringProperFreeModule(dimension);
        }
    }
    
    
    public ArithmeticStringMultiElement getZero() {
        List<RingString<ArithmeticDouble>> res = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            res.add(RingString.getZero());
        }
        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(RRing.ring, res); //TODO not cast
    }
    
    
    public ArithmeticStringMultiElement getUnitElement(int i) {
        List<RingString<ArithmeticDouble>> v = new ArrayList<>(getDimension());
        for (int j = 0; j < getDimension(); j++) {
            v.add(RingString.getZero());
        }
        v.set(i, RingString.getOne());
        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(RRing.ring, v);
    }
    

    public Module getNullModule() {
        return nullModule;
    }
    
    
    public boolean isNullModule() {
        return this == nullModule;
    }

    
    public Module getComponentModule(int i) {
        return RStringRing.ring;
    }

    
    public Ring getRing() {
        return RStringRing.ring;
    }


    public boolean isVectorSpace() {
        return false;
    }

    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof ArithmeticStringMultiElement &&
                element.getLength() == getDimension() &&
                element.getModule().getRing().equals(getRing()));
    }


    public int compareTo(Module object) {
        if (object instanceof RStringProperFreeModule) {
            RStringProperFreeModule module = (RStringProperFreeModule)object;
            return getDimension()-module.getDimension();
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public ArithmeticStringMultiElement createElement(List<ModuleElement<?, ?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }

        Iterator<ModuleElement<?, ?>> iter = elements.iterator();
        List<RingString<ArithmeticDouble>> values = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            ModuleElement object = iter.next();
            if (object instanceof RStringElement) {
                values.add(((ArithmeticStringElement<ArithmeticDouble>)object).getValue());
            }
            else {
                return null;
            }
        }

        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(RRing.ring, values);
    }
    
   
    public ArithmeticStringMultiElement cast(ModuleElement element) {
        if (element.getLength() > getDimension()) {
            RStringRing ring = RStringRing.ring;
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
        return (object instanceof RStringProperFreeModule &&
                getDimension() == ((RStringProperFreeModule)object).getDimension());
    }

    
    public ArithmeticStringMultiElement parseString(String string) {
        string = TextUtils.unparenthesize(string);
        if (string.equals("Null")) {
            return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(RRing.ring, new ArrayList<>());
        }
        if (string.charAt(0) == '(' && string.charAt(string.length()-1) == ')') {
            string = string.substring(1, string.length()-1);
            String[] strings = TextUtils.split(string, ',');
            if (strings.length != getDimension()) {
                return null;
            }
            else {
                List<RingString<ArithmeticDouble>> rstrings = new ArrayList<>(getDimension());
                for (int i = 0; i < strings.length; i++) {
                    rstrings.add(RStringRing.parse(strings[i]));
                }
                return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(RRing.ring, rstrings);
            }            
        }
        else {
            return null;
        }
    }
    
    
    public String toString() {
        return "RStringFreeModule["+getDimension()+"]";
    }


    public String toVisualString() {
        return "(R-String)^"+getDimension();
    }
    
    public String getElementTypeName() {
        return "RStringFreeModule";
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

    
    private RStringProperFreeModule(int dimension) {
        super(dimension);
    }

    
    private static final int basicHash = "RStringFreeModule".hashCode();    
}
