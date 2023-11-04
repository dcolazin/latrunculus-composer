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

import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author vetronauta
 */
public class VectorModule<R extends RingElement<R>> implements FreeModule<Vector<R>,R> {

    private final Ring<R> ring;
    private final int dimension;

    public VectorModule(Ring<R> ring, int dimension) {
        this.ring = ring;
        this.dimension = Math.max(0, dimension);
    }

    @Override
    public boolean isVectorSpace() {
        return ring.isField();
    }

    @Override
    public Vector<R> getUnitElement(int i) {
        assert(i >= 0 && i < dimension);
        List<R> list = new ArrayList<>(dimension);
        for (int j = 0; j < dimension; j++) {
            list.add(i == j ? ring.getOne() : ring.getZero());
        }
        return new Vector<>(ring, list);
    }

    @Override
    public ModuleMorphism getProjection(int index) {
        //int actualIndex = Math.max(0, Math.min(getDimension() - 1, index));
        //return new ArithmeticAffineProjection<>(ring, getUnitElement(actualIndex), ring.getZero());
        return null; //TODO after ArithmeticAffineProjection refactoring
    }

    @Override
    public ModuleMorphism getInjection(int index) {
        //int actualIndex = Math.max(0, Math.min(getDimension() - 1, index));
        //return new ArithmeticAffineInjection<>(ring, getUnitElement(actualIndex), getZero());
        return null; //TODO after ArithmeticAffineInjection refactoring
    }

    @Override
    public Vector<R> getZero() {
        List<R> list = new ArrayList<>(dimension);
        for (int j = 0; j < dimension; j++) {
            list.add(ring.getZero());
        }
        return new Vector<>(ring, list);
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public VectorModule<R> getNullModule() {
        return new VectorModule<>(ring, 0); //TODO repository?
    }

    @Override
    public boolean isNullModule() {
        return dimension == 0;
    }

    @Override
    public boolean isRing() {
        return dimension == 1;
    }

    @Override
    public Ring<R> getRing() {
        return ring;
    }

    @Override
    public Ring<R> getComponentModule(int i) {
        return ring;
    }

    @Override
    public boolean hasElement(ModuleElement<?, ?> element) {
        return (dimension == 1 && ring.hasElement(element)) || (element instanceof Vector && this.equals(element.getModule()));
    }

    @Override
    public Vector<R> cast(ModuleElement<?, ?> element) {
        if (element.getLength() == getDimension()) {
            if (element instanceof DirectSumElement) {
                return this.createElement(element.flatComponentList());
            }
            List<R> elements = new ArrayList<>(dimension);
            for (int i = 0; i < getDimension(); i++) {
                R castElement = ring.cast(element.getComponent(i));
                if (castElement == null) {
                    return null;
                }
                elements.add(castElement);
            }
            return new Vector<>(ring, elements);
        }
        return null;
    }

    @Override
    public Vector<R> createElement(List<? extends ModuleElement<?, ?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }
        List<R> values = new ArrayList<>(dimension);
        Iterator<? extends ModuleElement<?, ?>> iter = elements.iterator();
        for (int i = 0; i < dimension; i++) {
            R castElement = ring.cast(iter.next());
            if (castElement == null) {
                return null;
            }
            values.add(castElement);
        }

        return new Vector<>(ring, values);
    }

    @Override
    public String toVisualString() {
        return String.format("%s^%d", ring.toVisualString(), dimension);
    }

    @Override
    public int compareTo(Module<?, ?> module) {
        if (module instanceof VectorModule && ring.equals(module.getRing())) {
            return getDimension() - module.getDimension();
        }
        if (module instanceof FreeModule) {
            FreeModule<?,?> m = (FreeModule<?,?>) module;
            int c = getRing().compareTo(m.getRing());
            if (c != 0) {
                return c;
            }
            return getDimension()-m.getDimension();
        }
        return toString().compareTo(module.toString());
    }
}
