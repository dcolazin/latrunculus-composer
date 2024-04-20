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

package org.vetronauta.latrunculus.core.math.module.generic;

import org.vetronauta.latrunculus.core.math.element.generic.DirectSumElement;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Module with arbitrary modules as components.
 * @see DirectSumElement
 * 
 * @author Gérard Milmeister
 */
public final class DirectSumModule<R extends RingElement<R>> implements Module<DirectSumElement<R>,R> {

    private final List<Module<?,R>> components;
    private final Ring<R> ring;

    private DirectSumModule(List<Module<?,R>> components) {
        this.ring = components.get(0).getRing();
        this.components = components;
    }

    private DirectSumModule(Ring<R> ring) {
        this.ring = ring;
        this.components = null;
    }

    public static <X extends RingElement<X>> DirectSumModule<X> make(List<Module<?,X>> components) {
        if (components.isEmpty()) {
            throw new IllegalArgumentException("There must be at least one component module.");
        }
        else if (!checkComponents(components)) {
            throw new IllegalArgumentException("Component modules must all have the same ring.");
        }
        else {
            return new DirectSumModule<>(components);
        }
    }
    
    /**
     * Creates a direct sum with no components of the given <code>ring</code>.
     */
    public static <X extends RingElement<X>> DirectSumModule<X> makeNullModule(Ring<X> ring) {
        return new DirectSumModule<>(ring);
    }

    @Override
    public DirectSumModule<R> getNullModule() {
        return makeNullModule(getRing());
    }
    
    @Override
    public boolean isNullModule() {
        return components == null;
    }    
    
    @Override
    public boolean isRing() {
        return false;
    }
    
    @Override
    public Ring<R> getRing() {
        return ring;
    }
    
    @Override
    public DirectSumElement<R> getZero() {
        if (getDimension() == 0) {
            return DirectSumElement.makeNullElement(getRing());
        }
        List<ModuleElement<?,R>> newComponents = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            newComponents.add(getComponentModule(i).getZero());
        }
        return DirectSumElement.make(newComponents);
    }

    @Override
    public int getDimension() {
        return components.size();
    }
    
    @Override
    public Module<?,R> getComponentModule(int i) {
        return components.get(i);
    }
    
    @Override
    public boolean hasElement(ModuleElement<?,?> element) {
        if (!(element instanceof DirectSumElement) || element.getLength() == getDimension()) {
            return false;
        }
        for (int i = 0; i < getDimension(); i++) {
            if (!getComponentModule(i).hasElement(element.getComponent(i))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public DirectSumElement<R> createElement(List<? extends ModuleElement<?,?>> elements) {
	   if (elements.size() < getDimension()) {
	       return null;
	   }
       if (getDimension() == 0) {
           return DirectSumElement.makeNullElement(getRing());
       }
       List<ModuleElement<?,R>> values = new ArrayList<>(getDimension());
       for (int i = 0; i < components.size(); i++) {
           ModuleElement<?,R> castedElement = getComponentModule(i).cast(elements.get(i));
           if (castedElement == null) {
               return null;
           }
           values.add(castedElement);
	   }
	   return DirectSumElement.make(values);
    }

    public DirectSumElement<R> fill(List<ModuleElement<?,?>> elements) {
        List<ModuleElement<?,R>> newElements = new ArrayList<>(getDimension());
        ModuleElement<?,R> currentElement;
        for (int i = 0; i < getDimension(); i++) {
            Module<?,R> component = getComponentModule(i);
            if (component instanceof DirectSumModule) {
                currentElement = ((DirectSumModule) component).fill(elements);
            } else {
                if (elements.size() < component.getDimension()) {
                    return null;
                }
                List<ModuleElement<?,?>> moduleElements = new ArrayList<>();
                for (int j = 0; j < component.getDimension(); j++) {
                    moduleElements.add(elements.get(i));
                }
                currentElement = component.createElement(moduleElements);
            }
            if (currentElement == null) {
                return null;
            }
            newElements.add(currentElement);
        }
        return DirectSumElement.make(newElements);
    }

    @Override
    public DirectSumElement<R> cast(ModuleElement element) {
        return fill(element.flatComponentList());
    }

    @Override
    public int compareTo(Module object) {
        if (!(object instanceof DirectSumModule)) {
            return toString().compareTo(object.toString());
        }
        //TODO this does not consider the underlying ring
        DirectSumModule<?> module = (DirectSumModule<?>) object;
        int d = getDimension() - module.getDimension();
        if (d != 0) {
            return d;
        }
        for (int i = 0; i < getDimension(); i++) {
            int c = getComponentModule(i).compareTo(module.getComponentModule(i));
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DirectSumModule)) {
            return false;
        }
        DirectSumModule<?> module = (DirectSumModule<?>) object;
        for (int i = 0; i < module.getDimension(); i++) {
            if (!module.getComponentModule(i).equals(getComponentModule(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(50);
        buf.append("DirectSumModule[");
        buf.append(getDimension());
        buf.append(",");
        buf.append(getRing());
        buf.append("][");
        if (getDimension() > 0) {
            buf.append(components.get(0));
            for (int i = 1; i < getDimension(); i++) {
                buf.append(",");
                buf.append(components.get(0));
            }
        }
        buf.append("]");
        return buf.toString();
    }

    @Override
    public String toVisualString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("DirectSumModule[");
        buf.append(getDimension());
        buf.append(",");
        buf.append(getRing());
        buf.append("][");
        if (getDimension() > 0) {
            buf.append(components.get(0).toVisualString());
            for (int i = 1; i < getDimension(); i++) {
                buf.append(",");
                buf.append(components.get(i).toVisualString());
            }
        }
        buf.append("]");
        return buf.toString();
    }

    public String getElementTypeName() {
        return "DirectSumModule";
    }

    @Override
    public int hashCode() {
        int hash = 37*basicHash+getDimension();
        hash = 37*hash + getRing().hashCode();
        for (int i = 0; i < getDimension(); i++) {
            hash = 37*hash + components.get(i).hashCode();
        }
        return hash;
    }
    
    private static <X extends RingElement<X>> boolean checkComponents(List<Module<?,X>> components) {
        Ring<X> ring = components.get(0).getRing();
        for (int i = 1; i < components.size(); i++) {
            if (!ring.equals(components.get(i).getRing())) {
                return false;
            }
        }
        return true;
    }
    
    private static final int basicHash = "DirectSumModule".hashCode();

}
