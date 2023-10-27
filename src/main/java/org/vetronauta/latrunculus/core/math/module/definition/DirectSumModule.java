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
    
    
    /**
     * Creates a direct sum with no components of the given <code>ring</code>.
     */
    public static DirectSumModule makeNullModule(Ring ring) {
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
    
    
    public Ring getRing() {
        return ring;
    }
    
    
    public DirectSumElement getZero() {
        if (getDimension() == 0) {
            return DirectSumElement.makeNullElement(getRing());
        }
        ModuleElement[] newComponents = new ModuleElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            newComponents[i] = getComponentModule(i).getZero();
        }
        return DirectSumElement.make(newComponents);
    }

    public int getDimension() {
        return components.length;
    }
    

    public Module getComponentModule(int i) {
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
    

    public DirectSumElement<R> createElement(List<ModuleElement<?,?>> elements) {
	   if (elements.size() < getDimension()) {
	       return null;
	   }

       if (getDimension() == 0) {
           return DirectSumElement.makeNullElement(getRing());
       }
       
       ModuleElement[] values = new ModuleElement[getDimension()];        
	   int i = 0;
	
       for (ModuleElement e : elements) {
           values[i] = getComponentModule(i).cast(e);
           if (values[i] == null) {
               return null;
           }
	       i++;
	   }
	
	   return DirectSumElement.make(values);
    }

    
    public DirectSumElement fill(List<ModuleElement> elements) {
        ModuleElement[] newElements = new ModuleElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            Module component = getComponentModule(i);
            if (component instanceof DirectSumModule) {
                newElements[i] = ((DirectSumModule)component).fill(elements);
                if (newElements[i] == null) {
                    return null;
                }
            }
            else {
                LinkedList<ModuleElement> moduleElements = new LinkedList<ModuleElement>();
                for (int j = 0; j < component.getDimension(); j++) {
                    if (elements.size() > 0) {
                        moduleElements.add(elements.remove(0));
                    }
                    else {
                        return null;
                    }
                }
                newElements[i] = component.createElement(moduleElements);
                if (newElements[i] == null) {
                    return null;
                }
            }
        }
        return DirectSumElement.make(newElements);
    }

    
    public DirectSumElement cast(ModuleElement element) {
        if (element instanceof DirectSumElement) {
            return (DirectSumElement)element.cast(this);
        }
        else {
            LinkedList<ModuleElement> elements = new LinkedList<ModuleElement>();
            for (int i = 0; i < element.getLength(); i++) {
                elements.add(element.getComponent(i));
            }
            return fill(elements);
        }
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

    
    private DirectSumModule(Ring ring) {
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
    private Ring	 ring;
}
