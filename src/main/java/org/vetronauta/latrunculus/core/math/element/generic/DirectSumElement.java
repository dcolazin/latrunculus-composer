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

package org.vetronauta.latrunculus.core.math.element.generic;

import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.generic.DirectSumModule;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;

import java.util.LinkedList;
import java.util.List;

/**
 * Elements with components from arbitrary modules.
 * @see DirectSumModule
 * 
 * @author Gérard Milmeister
 */
public final class DirectSumElement<R extends RingElement<R>> implements ModuleElement<DirectSumElement<R>, R> {

    private ModuleElement<?,R>[] components;
    private DirectSumModule<R> module;
    private final Ring<R> ring;

    public static <I extends RingElement<I>> DirectSumElement<I> make(ModuleElement<?,I>[] components) {
        if (components.length < 1) {
            throw new IllegalArgumentException("There must be at least one component module.");
        }
        else if (!checkComponents(components)) {
            throw new IllegalArgumentException("Component elements must all be in the same ring.");            
        }
        else {
            return new DirectSumElement<>(components);
        }
    }
    
    
    public static <I extends RingElement<I>> DirectSumElement<I> makeNullElement(Ring<I> ring) {
        return new DirectSumElement<>(ring);
    }
    
    
    public boolean isNullElement() {
        return components == null;
    }

    
    public boolean isZero() {
        for (int i = 0; i < getLength(); i++) {
            if (!components[i].isZero()) {
                return false;
            }
        }
        return true;
    }

    public ModuleElement<?,R>[] getComponents() {
        return components;
    }

    @Override
    public DirectSumElement<R> sum(DirectSumElement<R> element) throws DomainException {
        if (getLength() == element.getLength()) {
            if (isNullElement()) {
                if (getModule().hasElement(element)) {
                    return this;
                }
                else {
                    throw new DomainException(this.getModule(), element.getModule());
                }
            }
            try {
                ModuleElement<?,R>[] c = new ModuleElement[getLength()];
                for (int i = 0; i < getLength(); i++) {
                    c[i] = ((ModuleElement) components[i]).sum(element.getComponent(i));
                }
                return new DirectSumElement<>(c);
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    @Override
    public void add(DirectSumElement<R> element) throws DomainException {
        if (getLength() == element.getLength()) {
            if (isNullElement()) {
                if (getModule().hasElement(element)) {
                    return;
                }
                else {
                    throw new IllegalArgumentException("Cannot sum "+this+" and "+element+".");                    
                }
            }
            try {
                ModuleElement[] newComponents = new ModuleElement[getLength()];
                for (int i = 0; i < getLength(); i++) {
                    newComponents[i] = components[i].deepCopy();
                    newComponents[i].add(element.getComponent(i));
                }
                components = newComponents;
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    @Override
    public DirectSumElement<R> difference(DirectSumElement<R> element) throws DomainException {
        if (getLength() == element.getLength()) {
            if (isNullElement()) {
                if (getModule().hasElement(element)) {
                    return this;
                }
                else {
                    throw new DomainException(this.getModule(), element.getModule());
                }
            }
            try {
                ModuleElement[] c = new ModuleElement[getLength()];
                for (int i = 0; i < getLength(); i++) {
                    c[i] = ((ModuleElement) getComponent(i)).difference(element.getComponent(i));
                }
                return new DirectSumElement<>(c);
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    @Override
    public void subtract(DirectSumElement<R> element) throws DomainException {
        if (getLength() == element.getLength()) {
            if (isNullElement()) {
                if (getModule().hasElement(element)) {
                    return;
                }
                else {
                    throw new IllegalArgumentException("Cannot subtract "+element+" from "+this+".");                    
                }
            }
            try {
                ModuleElement[] newComponents = new ModuleElement[getLength()];
                for (int i = 0; i < getLength(); i++) {
                    newComponents[i] = components[i].deepCopy();
                    newComponents[i].subtract(element.getComponent(i));
                }
                components = newComponents;
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    @Override
    public DirectSumElement<R> negated() {
        if (isNullElement()) {
            return this;
        }
        ModuleElement[] res = new ModuleElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = components[i].negated();
        }
        return new DirectSumElement<>(res);
    }

    @Override
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            components[i].negate();
        }
    }

    @Override
    public DirectSumElement<R> scaled(R element)
            throws DomainException {
        if (isNullElement()) {
            return this;
        }
        ModuleElement<?,R>[] res = new ModuleElement[getLength()];
        try {
            for (int i = 0; i < getLength(); i++) {
                res[i] = components[i].scaled(element);
            }
        }
        catch (DomainException e) {
            throw new DomainException(this.getRing(), element.getModule());
        }
        return new DirectSumElement<>(res);
    }

    @Override
    public void scale(R element) throws DomainException {
        try {
            for (int i = 0; i < getLength(); i++) {
                components[i] = components[i].scaled(element);
            }
        }
        catch (DomainException e) {
            throw new DomainException(this.getRing(), element.getModule());
        }
    }

    @Override
    public ModuleElement<?,R> getComponent(int i) {
        return components[i];
    }

    @Override
    public int getLength() {
        if (components == null) {
            return 0;
        }
        else {
            return components.length;
        }
    }

    @Override
    public DirectSumModule<R> getModule() {
        if (module == null) {
            Module[] res = new Module[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = components[i].getModule();
            }
            module = DirectSumModule.make(res);
        }
        return module;
    }
    
    
    public Ring<R> getRing() {
        return ring;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof ModuleElement) {
            ModuleElement element = (ModuleElement) object;
            if (!getRing().equals(element.getModule().getRing())) {
                return false;
            }
            if (getLength() == element.getLength()) {
                for (int i = 0; i < getLength(); i++) {
                    if (!components[i].equals(element.getComponent(i))) {
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

    @Override
    public int compareTo(ModuleElement object) {
        if (object instanceof DirectSumElement) {
            DirectSumElement element = (DirectSumElement)object;
            if (!getRing().equals(element.getRing())) {
                return getRing().compareTo(element.getRing());
            }
            int d = getLength()-element.getLength();
            if (d != 0) {
                return d;
            }
            else {
	            for (int i = 0; i < getLength(); i++) {
	                int comp = getComponent(i).compareTo(element.getComponent(i));
	                if (comp != 0) {
	                    return comp;
	                }
	            }
	            return 0;
            }
        }
        else {
            return getModule().compareTo(object.getModule());
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(50);
        buf.append("DirectSumElement[");
        buf.append(getLength());
        buf.append(",");
        buf.append(getModule().getRing());
        buf.append("][");
        if (getLength() > 0) {
            buf.append(components[0]);
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(components[i]);
            }
        }
        buf.append("]");
        return buf.toString();
    }

    public List<ModuleElement<?,R>> flatComponentList() {
        List<ModuleElement<?,R>> flattenedList = new LinkedList<>();
        flatComponentList(flattenedList);
        return flattenedList;
    }

    private void flatComponentList(List<ModuleElement<?,R>> list) {
        for (int i = 0; i < getLength(); i++) {
            ModuleElement<?,R> component = getComponent(i);
            if (component instanceof DirectSumElement) {
                ((DirectSumElement<R>) component).flatComponentList(list);
            } else {
                for (int j = 0; j < component.getLength(); j++) {
                    list.add(component.getComponent(j));
                }
            }
        }
    }

    private static boolean checkComponents(ModuleElement[] components) {
        Ring ring = components[0].getModule().getRing();
        for (int i = 1; i < components.length; i++) {
            if (!ring.equals(components[i].getModule().getRing())) {
                return false;
            }
        }
        return true;
    }

    public String getElementTypeName() {
        return "DirectSumElement";
    }
    
    
    public int hashCode() {
        int hash = 7*"DirectSumElement".hashCode();
        hash = 37*hash+getRing().hashCode();
        for (int i = 0; i < getLength(); i++) {
            hash = 37*hash+getComponent(i).hashCode();
        }
        return hash;
    }

    
    private DirectSumElement(ModuleElement[] components) {
        this.components = components;
        this.ring = components[0].getModule().getRing();
    }

    
    private DirectSumElement(Ring ring) {
        this.components = null;
        this.module = DirectSumModule.makeNullModule(ring);
        this.ring = ring;
    }

    @Override
    public DirectSumElement<R> deepCopy() {
        if (isNullElement()) {
            return makeNullElement(getModule().getRing());
        }
        ModuleElement[] newComponents = new ModuleElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            newComponents[i] = components[i].deepCopy();
        }
        return new DirectSumElement<>(newComponents);
    }
}
