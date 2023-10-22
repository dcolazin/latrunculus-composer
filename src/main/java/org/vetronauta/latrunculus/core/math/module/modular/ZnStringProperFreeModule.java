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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.repository.ArithmeticRingRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Free modules over ZnStringRing.
 * @see ZnStringProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class ZnStringProperFreeModule extends ProperFreeModule<ArithmeticStringMultiElement<ZnStringElement,ArithmeticModulus>,ZnStringElement> {

    public static FreeModule<?, ZnStringElement> make(int dimension, int modulus) {
        dimension = Math.max(dimension, 0);
        if (dimension == 1) {
            return ZnStringRing.make(modulus);
        }
        else {
            return new ZnStringProperFreeModule(dimension, modulus);
        }
    }
    
    
    public ArithmeticStringMultiElement getZero() {
        List<RingString<ArithmeticModulus>> res = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            res.add(RingString.getZero());
        }
        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ArithmeticRingRepository.getModulusRing(getModulus()), res); //TODO do not cast
    }
    
    
    public ArithmeticStringMultiElement getUnitElement(int i) {
        List<RingString<ArithmeticModulus>> v = new ArrayList<>(getDimension());
        for (int j = 0; j < getDimension(); j++) {
            v.add(RingString.getZero());
        }
        v.set(i, RingString.getOne());
        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ArithmeticRingRepository.getModulusRing(getModulus()), v);
    }
    

    public Module getNullModule() {
        return ZnStringProperFreeModule.make(0, modulus);
    }
    
    
    public boolean isNullModule() {
        return getDimension() == 0;
    }

    
    public Module getComponentModule(int i) {
        return ZnStringRing.make(modulus);
    }

    
    public Ring getRing() {
        return ZnStringRing.make(modulus);
    }


    public boolean isVectorSpace() {
        return false;
    }

    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof ZnStringProperFreeElement &&
                element.getLength() == getDimension() &&
                (element).getModule().equals(this));
    }


    public int compareTo(Module object) {
        if (object instanceof ZnStringProperFreeModule) {
            ZnStringProperFreeModule module = (ZnStringProperFreeModule)object;
            int m = getModulus()-module.getModulus();
            if (m == 0) {
                return getDimension()-module.getDimension();
            }
            else {
                return m;
            }
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
        List<RingString<ArithmeticModulus>> values = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            ModuleElement object = iter.next();
            if (object instanceof ZnStringElement) {
                values.add(((ZnStringElement)object).getValue());
            }
            else {
                return null;
            }
        }

        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ArithmeticRingRepository.getModulusRing(getModulus()), values);
    }
    
   
    public ArithmeticStringMultiElement cast(ModuleElement element) {
        if (element.getLength() > getDimension()) {
            Ring ring = getRing();
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

    
    public int getModulus() {
        return modulus;
    }
    
    
    public boolean equals(Object object) {
        return (object instanceof ZnStringProperFreeModule &&
                getDimension() == ((ZnStringProperFreeModule)object).getDimension() &&
                getModulus() == ((ZnStringProperFreeModule)object).getModulus());
    }

    
    public ArithmeticStringMultiElement parseString(String string) {
        string = TextUtils.unparenthesize(string);
        if (string.equals("Null")) {
            return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ArithmeticRingRepository.getModulusRing(getModulus()), new ArrayList<>());
        }
        if (string.charAt(0) == '(' && string.charAt(string.length()-1) == ')') {
            string = string.substring(1, string.length()-1);
            String[] strings = TextUtils.split(string, ',');
            if (strings.length != getDimension()) {
                return null;
            }
            else {
                List<RingString<ArithmeticModulus>> zstrings = new ArrayList<>(getDimension());
                for (int i = 0; i < strings.length; i++) {
                    zstrings.add(ZnStringRing.parse(strings[i], getModulus()));
                }
                return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ArithmeticRingRepository.getModulusRing(getModulus()), zstrings);
            }            
        }
        else {
            return null;
        }
    }
    
    
    public String toString() {
        return "ZnStringFreeModule["+getDimension()+"]";
    }


    public String toVisualString() {
        return "Z_"+getModulus()+"-String)^"+getDimension();
    }

    public String getElementTypeName() {
        return "ZnStringFreeModule";
    }

    public int hashCode() {
        return 11*(37*basicHash + getDimension())+getModulus();        
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

    
    private ZnStringProperFreeModule(int dimension, int modulus) {
        super(dimension);
        this.modulus = modulus;
    }

    private int modulus;

    private static final int basicHash = "ZnStringFreeModule".hashCode();
}
