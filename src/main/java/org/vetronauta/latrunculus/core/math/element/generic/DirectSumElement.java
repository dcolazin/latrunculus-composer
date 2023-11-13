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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Elements with components from arbitrary modules.
 * @see DirectSumModule
 * 
 * @author Gérard Milmeister
 */
public final class DirectSumElement<R extends RingElement<R>> implements ModuleElement<DirectSumElement<R>, R> {

    private final List<ModuleElement<?,R>> components;
    private DirectSumModule<R> module;
    private final Ring<R> ring;

    private DirectSumElement(List<ModuleElement<?,R>> components) {
        this.components = components;
        this.ring = components.get(0).getModule().getRing();
    }

    private DirectSumElement(Ring<R> ring) {
        this.components = null;
        this.module = DirectSumModule.makeNullModule(ring);
        this.ring = ring;
    }

    public static <I extends RingElement<I>> DirectSumElement<I> make(List<ModuleElement<?,I>> components) {
        if (components.size() < 1) {
            throw new IllegalArgumentException("There must be at least one component module.");
        }
        if (!checkComponents(components)) {
            throw new IllegalArgumentException("Component elements must all be in the same ring.");            
        }
        return new DirectSumElement<>(components);
    }
    
    public static <I extends RingElement<I>> DirectSumElement<I> makeNullElement(Ring<I> ring) {
        return new DirectSumElement<>(ring);
    }
    
    
    public boolean isNullElement() {
        return components == null;
    }

    
    public boolean isZero() {
        for (int i = 0; i < getLength(); i++) {
            if (!components.get(i).isZero()) {
                return false;
            }
        }
        return true;
    }

    public List<ModuleElement<?,R>> getComponents() {
        return components;
    }

    @Override
    public DirectSumElement<R> sum(DirectSumElement<R> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        if (isNullElement()) {
            if (getModule().hasElement(element)) {
                return this;
            }
            throw new DomainException(this.getModule(), element.getModule());
        }
        try {
            List<ModuleElement<?,R>> c = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                c.add(((ModuleElement) components.get(i)).sum(element.getComponent(i)));
            }
            return new DirectSumElement<>(c);
        } catch (DomainException e) {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    @Override
    public void add(DirectSumElement<R> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        if (isNullElement()) {
            if (getModule().hasElement(element)) {
                return;
            }
            else {
                throw new IllegalArgumentException("Cannot sum "+this+" and "+element+".");
            }
        }
        try {
            for (int i = 0; i < getLength(); i++) {
                ((ModuleElement) components.get(i)).add(element.getComponent(i));
            }
        }
        catch (DomainException e) {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    @Override
    public DirectSumElement<R> difference(DirectSumElement<R> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        if (isNullElement()) {
            if (getModule().hasElement(element)) {
                return this;
            }
            throw new DomainException(this.getModule(), element.getModule());
        }
        try {
            List<ModuleElement<?,R>> c = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                c.add(((ModuleElement) components.get(i)).difference(element.getComponent(i)));
            }
            return new DirectSumElement<>(c);
        } catch (DomainException e) {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    @Override
    public void subtract(DirectSumElement<R> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        if (isNullElement()) {
            if (getModule().hasElement(element)) {
                return;
            }
            else {
                throw new IllegalArgumentException("Cannot sum "+this+" and "+element+".");
            }
        }
        try {
            for (int i = 0; i < getLength(); i++) {
                ((ModuleElement) components.get(i)).subtract(element.getComponent(i));
            }
        }
        catch (DomainException e) {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    @Override
    public DirectSumElement<R> negated() {
        if (isNullElement()) {
            return this;
        }
        List<ModuleElement<?,R>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(components.get(i).negated());
        }
        return new DirectSumElement<>(res);
    }

    @Override
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            components.get(i).negate();
        }
    }

    @Override
    public DirectSumElement<R> scaled(R element)
            throws DomainException {
        if (isNullElement()) {
            return this;
        }
        List<ModuleElement<?,R>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(components.get(i).scaled(element));
        }
        return new DirectSumElement<>(res);
    }

    @Override
    public void scale(R element) throws DomainException {
        for (int i = 0; i < getLength(); i++) {
            components.get(i).scale(element);
        }
    }

    @Override
    public ModuleElement<?,R> getComponent(int i) {
        return components.get(i);
    }

    @Override
    public int getLength() {
        return components != null ? components.size() : 0;
    }

    @Override
    public DirectSumModule<R> getModule() {
        if (module == null) {
            List<Module<?,R>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(components.get(i).getModule());
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
        //TODO is it correct to only check for ModuleElement and not for DirectSumElement?
        if (!(object instanceof ModuleElement)) {
            return false;
        }
        ModuleElement<?,?> element = (ModuleElement<?,?>) object;
        if (!getRing().equals(element.getModule().getRing()) || getLength() != element.getLength()) {
            return false;
        }
        for (int i = 0; i < getLength(); i++) {
            if (!components.get(i).equals(element.getComponent(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(ModuleElement object) {
        if (!(object instanceof DirectSumElement)) {
            return getModule().compareTo(object.getModule());
        }
        DirectSumElement<?> element = (DirectSumElement<?>) object;
        if (!getRing().equals(element.getRing())) {
            return getRing().compareTo(element.getRing());
        }
        int d = getLength() - element.getLength();
        if (d != 0) {
            return d;
        }
        for (int i = 0; i < getLength(); i++) {
            int comp = getComponent(i).compareTo(element.getComponent(i));
            if (comp != 0) {
                return comp;
            }
        }
        return 0;
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
            buf.append(components.get(0));
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(components.get(i));
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

    private static <X extends RingElement<X>> boolean checkComponents(List<ModuleElement<?,X>> components) {
        Ring<X> ring = components.get(0).getModule().getRing();
        for (int i = 1; i < components.size(); i++) {
            if (!ring.equals(components.get(i).getModule().getRing())) {
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

    @Override
    public DirectSumElement<R> deepCopy() {
        if (isNullElement()) {
            return makeNullElement(getModule().getRing());
        }
        List<ModuleElement<?,R>> newComponents = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            newComponents.add(components.get(i).deepCopy());
        }
        return new DirectSumElement<>(newComponents);
    }

}
