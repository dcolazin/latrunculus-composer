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
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.RestrictedModule;
import org.vetronauta.latrunculus.core.math.module.generic.Module;

/**
 * Elements in a restricted module. Instances are created using
 * the static {@link #make(RestrictedModule, ModuleElement)} method.
 * 
 * @author Gérard Milmeister
 */
public class RestrictedElement<B extends ModuleElement<B,R>, R extends RingElement<R>> implements ModuleElement<RestrictedElement<B,R>,R> {

    public static RestrictedElement make(RestrictedModule module, ModuleElement element)
            throws DomainException {
        if (module.getUnrestrictedModule().hasElement(element)) {
            return new RestrictedElement(module, element);
        }
        else {
            throw new DomainException(module.getUnrestrictedModule(), element.getModule());
        }
    }

    
    public static RestrictedElement getZero(RestrictedModule module) {
        return new RestrictedElement(module, module.getUnrestrictedModule().getZero());
    }
    
    
    public boolean isZero() {
        return moduleElement.isZero();
    }

    
    public RestrictedElement<B,R> scaled(R element)
            throws DomainException {
        if (module.getRestrictingMorphism().getDomain().hasElement(element)) {            
            ModuleElement res = null;
            try {
                res = module.getRestrictingMorphism().map(element);
            }
            catch (MappingException e) {}
            return new RestrictedElement(module, this.moduleElement.scaled((R)res));
        }
        else {
            throw new DomainException(module.getRestrictingMorphism().getDomain().getRing(), element.getRing());
        }
    }

    
    public void scale(R element)
            throws DomainException {
        if (module.getRestrictingMorphism().getDomain().hasElement(element)) {            
            ModuleElement res = null;
            try {
                res = module.getRestrictingMorphism().map(element);
            }
            catch (MappingException e) {}
            this.moduleElement.scale((R)res);
        }
        else {
            throw new DomainException(module.getRestrictingMorphism().getDomain().getRing(), element.getRing());
        }
    }

    
    public int getLength() {
        return moduleElement.getLength();
    }

    
    public ModuleElement getComponent(int i) {
        return new RestrictedElement((RestrictedModule)module.getComponentModule(i), moduleElement.getComponent(i));
    }

    
    public RestrictedElement<B,R> sum(RestrictedElement<B,R> element)
            throws DomainException {
        if (module.equals(element.getModule())) {
            return new RestrictedElement(module, this.moduleElement.sum(element.getUnrestrictedElement()));
        }
        else {
            throw new DomainException(module, element.getModule());
        }
    }

    
    public void add(RestrictedElement<B,R> element)
            throws DomainException {
        if (module.equals(element.getModule())) {
            this.moduleElement.add((element).getUnrestrictedElement());
        }
        else {
            throw new DomainException(module, element.getModule());
        }
    }

    
    public RestrictedElement<B,R> difference(RestrictedElement<B,R> element)
            throws DomainException {
        if (module.equals(element.getModule())) {            
            return new RestrictedElement(module, this.moduleElement.difference((element).getUnrestrictedElement()));
        }
        else {
            throw new DomainException(module, element.getModule());
        }
    }

    
    public void subtract(RestrictedElement<B,R> element)
            throws DomainException {
        if (module.equals(element.getModule())) {
            this.moduleElement.subtract((element).getUnrestrictedElement());
        }
        else {
            throw new DomainException(module, element.getModule());
        }
    }

    
    public RestrictedElement<B,R> negated() {
        return new RestrictedElement(module, moduleElement.negated());
    }

    
    public void negate() {
        moduleElement.negate();
    }

    
    public Module getModule() {
        return module;
    }

    
    public B getUnrestrictedElement() {
        return moduleElement;
    }

    public boolean equals(Object object) {
        if (object instanceof RestrictedElement) {
            RestrictedElement relement = (RestrictedElement)object;
            return module.equals(relement.module)
                   && moduleElement.equals(relement.moduleElement);
        }
        else {
            return false;
        }
    }
    
    
    public int compareTo(ModuleElement object) {
        if (object instanceof RestrictedElement) {
            RestrictedElement relement = (RestrictedElement)object;
            int comp = module.compareTo(relement.module);
            if (comp == 0) {
                return moduleElement.compareTo(relement.moduleElement);
            }
            else {
                return comp;
            }
        }
        else {
            return getModule().compareTo(object.getModule());
        }
    }
    
    public String toString() {
        return "RestrictedElement["+getModule()+","+moduleElement+"]";
    }

    public String getElementTypeName() {
        return "RestrictedElement";
    }
    

    private RestrictedElement(RestrictedModule module, B element) {
        this.module = module;
        this.moduleElement = element;
    }
    
    public ModuleElement getModuleElement() {
        return moduleElement;
    }

    private final RestrictedModule module;
    private final B    moduleElement;

    @Override
    public RestrictedElement deepCopy() {
        return new RestrictedElement(module, moduleElement.deepCopy());
    }
}
