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
import java.util.LinkedList;
import java.util.List;

/**
 * Module with arbitrary modules as components.
 * @see DirectSumElement
 * 
 * @author Gérard Milmeister
 */
public final class DirectSumModule<R extends RingElement<R>> implements Module<DirectSumElement<R>,R> {

    /**
     * Creates a direct sum with the given <code>components</code>.
     * All components must be modules of the same ring.
     */
    public static DirectSumModule make(Module... components) {
        if (components.length < 1) {
            throw new IllegalArgumentException("There must be at least one component module.");
        }
        else if (!checkComponents(components)) {
            throw new IllegalArgumentException("Component modules must all have the same ring.");            
        }
        else {
            return new DirectSumModule(components);
        }
    }

    public static <X extends RingElement<X>> DirectSumModule<X> make(List<Module<?,X>> components) {
       return make(components.toArray(new Module[]{}));
    }
    
    /**
     * Creates a direct sum with no components of the given <code>ring</code>.
     */
    public static <X extends RingElement<X>> DirectSumModule<X> makeNullModule(Ring<X> ring) {
        return new DirectSumModule(ring);
    }
    

    public DirectSumModule getNullModule() {
        return makeNullModule(getRing());
    }
    
    
    public boolean isNullModule() {
        return components == null;
    }    
    
    
    public boolean isRing() {
        return false;
    }
    
    
    public Ring<R> getRing() {
        return ring;
    }
    
    
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

    public int getDimension() {
        return components.length;
    }
    

    public Module<?,R> getComponentModule(int i) {
        return components[i];
    }
    

    public boolean hasElement(ModuleElement element) {
        if (element instanceof DirectSumElement) {
            if (element.getLength() == getDimension()) {
                for (int i = 0; i < getDimension(); i++) {
                    if (!getComponentModule(i).hasElement(element.getComponent(i))) {
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
    

    public DirectSumElement<R> createElement(List<? extends ModuleElement<?,?>> elements) {
	   if (elements.size() < getDimension()) {
	       return null;
	   }
       if (getDimension() == 0) {
           return DirectSumElement.makeNullElement(getRing());
       }
       List<ModuleElement<?,R>> values = new ArrayList<>(getDimension());
       for (int i = 0; i < components.length; i++) {
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
                    moduleElements.add(elements.remove(0));
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

    
    public DirectSumElement<R> cast(ModuleElement element) {
        return fill(element.flatComponentList());
    }

    public int compareTo(Module object) {
        if (object instanceof DirectSumModule) {
            DirectSumModule module = (DirectSumModule)object; 
            int d = getDimension()-module.getDimension();
            if (d != 0) {
                return d;
            }
            else {
                for (int i = 0; i < getDimension(); i++) {
                    int c = getComponentModule(i).compareTo(module.getComponentModule(i));
                    if (c != 0) {
                        return c;
                    }
                }
                return 0;
            }
        }
        else {
            return toString().compareTo(object.toString());
        }
    }

    
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof DirectSumModule) {
            DirectSumModule module = (DirectSumModule)object;
            for (int i = 0; i < module.getDimension(); i++) {
                if (!module.getComponentModule(i).equals(getComponentModule(i))) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder(50);
        buf.append("DirectSumModule[");
        buf.append(getDimension());
        buf.append(",");
        buf.append(getRing());
        buf.append("][");
        if (getDimension() > 0) {
            buf.append(components[0]);
            for (int i = 1; i < getDimension(); i++) {
                buf.append(",");
                buf.append(components[i]);
            }
        }
        buf.append("]");
        return buf.toString();
    }

    public String toVisualString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("DirectSumModule[");
        buf.append(getDimension());
        buf.append(",");
        buf.append(getRing());
        buf.append("][");
        if (getDimension() > 0) {
            buf.append(components[0].toVisualString());
            for (int i = 1; i < getDimension(); i++) {
                buf.append(",");
                buf.append(components[i].toVisualString());
            }
        }
        buf.append("]");
        return buf.toString();
    }

    public String getElementTypeName() {
        return "DirectSumModule";
    }

    
    public int hashCode() {
        int hash = 37*basicHash+getDimension();
        hash = 37*hash + getRing().hashCode();
        for (int i = 0; i < getDimension(); i++) {
            hash = 37*hash + components[i].hashCode();
        }
        return hash;
    }

    
    private DirectSumModule(Module[] components) {
        this.ring = components[0].getRing();
        this.components = components;
    }

    
    private DirectSumModule(Ring<R> ring) {
        this.ring = ring;
        this.components = null;
    }

    
    private static boolean checkComponents(Module[] components) {
        Ring ring = components[0].getRing();
        for (int i = 1; i < components.length; i++) {
            if (!ring.equals(components[i].getRing())) {
                return false;
            }
        }
        return true;
    }

    
    private static final int basicHash = "DirectSumModule".hashCode();

    private Module[] components;
    private Ring<R>	 ring;
}
