/*
 * latrunculus-composer
 * Copyright (C) 2023 vetronauta
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.vetronauta.latrunculus.core.math.module.generic;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author vetronauta
 */
public class ArithmeticStringMultiModule<N extends ArithmeticNumber<N>> extends ProperFreeModule<ArithmeticStringMultiElement<N>, ArithmeticStringElement<N>> {

    private final ArithmeticStringRing<N> ring;

    protected ArithmeticStringMultiModule(ArithmeticStringRing<N> ring, int dimension) {
        super(dimension);
        this.ring = ring;
    }

    public static <T extends ArithmeticNumber<T>> FreeModule<?, ArithmeticStringElement<T>> make(ArithmeticStringRing<T> ring, int dimension) {
        if (dimension <= 0) {
            return new ArithmeticStringMultiModule<>(ring, 0);
        }
        else if (dimension == 1) {
            return ring;
        }
        else {
            return new ArithmeticStringMultiModule<>(ring, dimension);
        }
    }

    @Override
    public boolean isVectorSpace() {
        return false;
    }

    @Override
    public ArithmeticStringMultiElement<N> getUnitElement(int i) {
        List<RingString<N>> v = new ArrayList<>(getDimension());
        for (int j = 0; j < getDimension(); j++) {
            v.add(RingString.getZero());
        }
        v.set(i, RingString.getOne());
        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ring.getFactorRing(), v); //TODO not cast
    }

    @Override
    public ArithmeticStringMultiElement<N> getZero() {
        List<RingString<N>> res = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            res.add(RingString.getZero());
        }
        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ring.getFactorRing(), res); //TODO not cast
    }

    @Override
    public ArithmeticStringMultiModule<N> getNullModule() {
        return new ArithmeticStringMultiModule<>(ring, 0);
    }

    @Override
    public boolean isNullModule() {
        return this.equals(getNullModule());
    }

    @Override
    public ArithmeticStringRing<N> getRing() {
        return ring;
    }

    @Override
    public ArithmeticStringRing<N> getComponentModule(int i) {
        return ring;
    }

    @Override
    public boolean hasElement(ModuleElement element) {
        return (element instanceof ArithmeticStringMultiElement &&
                element.getLength() == getDimension() &&
                element.getModule().getRing().equals(getRing()));
    }

    @Override
    public ArithmeticStringMultiElement<N> cast(ModuleElement<?, ?> element) {
        if (element.getLength() <= getDimension()) {
            return null;
        }
        List<ModuleElement<?,?>> elementList = new LinkedList<>();
        for (int i = 0; i < getDimension(); i++) {
            ArithmeticStringElement<N> e = ring.cast(element.getComponent(i));
            if (e == null) {
                return null;
            }
            elementList.add(e);
        }
        return createElement(elementList);
    }

    @Override
    public ArithmeticStringMultiElement<N> createElement(List<ModuleElement<?, ?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }
        Iterator<ModuleElement<?, ?>> iter = elements.iterator();
        List<RingString<N>> values = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            ModuleElement<?,?> object = iter.next();
            if (!ring.hasElement(object)) {
                return null;
            }
            values.add(((ArithmeticStringElement<N>)object).getValue());
        }
        return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ring.getFactorRing(), values);
    }

    @Override
    public ArithmeticStringMultiElement<N> parseString(String string) {
        string = TextUtils.unparenthesize(string);
        if (string.equals("Null")) {
            return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ring.getFactorRing(), new ArrayList<>());
        }
        if (string.charAt(0) == '(' && string.charAt(string.length()-1) == ')') {
            string = string.substring(1, string.length()-1);
            String[] strings = TextUtils.split(string, ',');
            if (strings.length != getDimension()) {
                return null;
            }
            else {
                List<RingString<N>> rstrings = new ArrayList<>(getDimension());
                for (int i = 0; i < strings.length; i++) {
                    rstrings.add(ArithmeticParsingUtils.parseString(ring, strings[i]));
                }
                return (ArithmeticStringMultiElement) ArithmeticStringMultiElement.make(ring.getFactorRing(), rstrings);
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String toVisualString() {
        return String.format("%s^%d", ring.toVisualString(), getDimension());
    }

    public String getElementTypeName() {
        return String.format("ArithmeticStringMultiModule<%s>", ring.getElementTypeName());
    }

    @Override
    public String toString() {
        return String.format("ArithmeticStringMultiModule<%s>[%d]", ring.getElementTypeName(), getDimension());
    }

    @Override
    protected ModuleMorphism _getProjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), getDimension(), 1);
        m.setMatrix(0, index, getRing().getOne());
        return m;
    }

    @Override
    protected ModuleMorphism _getInjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), 1, getDimension());
        m.setMatrix(index, 0, getRing().getOne());
        return m;
    }

    @Override
    public int compareTo(Module object) {
        if (object instanceof ArithmeticStringMultiModule && ring.equals(((ArithmeticStringMultiModule<?>) object).ring)) {
            return getDimension()-object.getDimension();
        }
        return super.compareTo(object);
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof ArithmeticStringMultiModule &&
                getDimension() == ((ArithmeticStringMultiModule<?>)object).getDimension() &&
                ring.equals(((ArithmeticStringMultiModule<?>) object).ring));
    }

    @Override
    public int hashCode() {
        return ring.hashCode() + getDimension();
    }

}
