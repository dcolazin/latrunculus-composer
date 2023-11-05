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

package org.vetronauta.latrunculus.core.math.module.definition;

import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.List;

/**
 * The abstract base class for rings with RingString elements.
 *
 * @author Gérard Milmeister
 */
public class StringRing<R extends RingElement<R>> extends Ring<StringMap<R>> {

    private final Ring<R> baseRing;

    public StringRing(Ring<R> baseRing) {
        this.baseRing = baseRing;
    }

    @Override
    public StringMap<R> getOne() {
        return new StringMap<>(baseRing.getOne());
    }

    @Override
    public boolean isField() {
        return false;
    }

    @Override
    public FreeModule<?, StringMap<R>> getFreeModule(int dimension) {
        return null; //TODO after refactoring
    }

    @Override
    public StringMap<R> getZero() {
        return new StringMap<>(baseRing);
    }

    @Override
    public Module<?, StringMap<R>> getNullModule() {
        return null; //TODO after refactoring
    }

    @Override
    public StringRing<R> getComponentModule(int i) {
        return this;
    }

    @Override
    public StringMap<R> cast(ModuleElement<?, ?> element) {
        return null; //TODO consider extracting the map into a class
    }

    public boolean hasElement(ModuleElement e) {
        return e instanceof StringMap && this.equals(((StringMap<?>) e).getRing());
    }
    
    public StringMap<R> createElement(List<? extends ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return this.cast(elements.get(0));
        }
        else {
            return null;
        }
    }

    @Override
    public String toVisualString() {
        return String.format("%s-String", baseRing.toVisualString());
    }

    /**
     * Returns the ring of the factors.
     */
    public Ring<R> getFactorRing() {
        return baseRing;
    }
    
    public int compareTo(Module object) {
        if (object instanceof StringRing) {
            Ring other = ((StringRing)object).getFactorRing();
            return getFactorRing().compareTo(other);
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    protected boolean nonSingletonEquals(Object object) {
        return object instanceof StringRing && baseRing.equals(((StringRing<?>) object).baseRing);
    }

    @Override
    protected int nonSingletonHashCode() {
        return baseRing.hashCode();
    }

    public String getElementTypeName() {
        return this.toString();
    }

}
