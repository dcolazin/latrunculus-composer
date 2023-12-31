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

import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.RestrictedElement;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.morphism.EmbeddingMorphism;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;

import java.util.List;

/**
 * Modules with restricted ring of scalars. Instances are created using
 * the static {@link #make(ModuleMorphism, Module)} method.
 * 
 * @author Gérard Milmeister
 */
public class RestrictedModule<B extends ModuleElement<B,R>, R extends RingElement<R>> implements Module<RestrictedElement<B,R>,R> {

    /**
     * Creates a new scalar-restricted module based on <code>module</code>
     * where the restriction is effectuated through <code>morphism</code>.
     * The codomain of <code>morphism</code> must be equal to the base ring
     * of <code>module</code>.
     *
     * @throws DomainException if <code>morphism</code> does not have
     *                         the correct codomain
     */
    public static RestrictedModule make(ModuleMorphism morphism, Module module)
            throws DomainException {
        if (morphism.getDomain().isRing()
            && morphism.getCodomain().equals(module.getRing())) {
            return new RestrictedModule(morphism, module);
        }
        else {
            throw new DomainException(module.getRing(), morphism.getCodomain());
        }
    }
    
    
    /**
     * Creates a new scalar-restricted module based on <code>module</code>
     * where the restriction is effectuated through embedding the given
     * <code>ring</code> in the base ring of <code>module</code>.
     * 
     *  @throws DomainException if the embedding is not possible
     */
    public static RestrictedModule make(Ring ring, Module module)
            throws DomainException {
        ModuleMorphism morphism = EmbeddingMorphism.make(ring, module.getRing());
        if (morphism == null) {
            throw new DomainException(module.getRing(), ring);
        }
        return make(morphism, module);
    }
    
    
    public RestrictedElement<B, R> getZero() {
        return RestrictedElement.getZero(this);
    }
    
    public int getDimension() {
        return module.getDimension();
    }

    
    public Module getNullModule() {
        Module rmodule = null;
        try {
            rmodule = make(morphism, module.getNullModule());
        }
        catch (DomainException e) {}
        return rmodule;
    }

    
    public boolean isNullModule() {
        return module.isNullModule();
    }

    
    public boolean isRing() {
        return false;
    }

    
    public Ring getRing() {
        return (Ring)morphism.getDomain();
    }

    
    public Module getComponentModule(int i) {
        Module rmodule = null;
        try {
            rmodule = make(morphism, module.getComponentModule(i));
        }
        catch (DomainException e) {}
        return rmodule;
    }


    /**
     * Returns the unrestricted module at the origin of this restricted module.
     */
    public Module getUnrestrictedModule() {
        return module;
    }
    
    
    /**
     * Returns the morphism responsible for the restriction.
     */
    public ModuleMorphism getRestrictingMorphism() {
        return morphism;
    }
    
    
    public boolean hasElement(ModuleElement element) {
        return element.getModule().equals(this);
    }
    
    public RestrictedElement cast(ModuleElement element) {
        ModuleElement res = module.cast(element);
        if (res == null) {
            return null;
        }
        try {
            return RestrictedElement.make(this, res);
        }
        catch (DomainException e) {
            return null;
        }
    }

    
    public RestrictedElement createElement(List<? extends ModuleElement<?, ?>> elements) {
        ModuleElement res = module.createElement(elements);
        if (res != null) {
            try {
                return RestrictedElement.make(this, res);
            }
            catch (DomainException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public boolean equals(Object object) {
        if (object instanceof RestrictedModule) {
            RestrictedModule rmodule = (RestrictedModule)object;
            return module.equals(rmodule.module)
                   && morphism.equals(rmodule.morphism);
            
        }
        return false;
    }
    
    
    public int compareTo(Module object) {
        if (object instanceof RestrictedModule) {
            RestrictedModule rmodule = (RestrictedModule)object;
            int c;
            if ((c = module.compareTo(rmodule.module)) != 0) {
                return c;
            }
            else {
                return morphism.compareTo(rmodule.morphism);
            }
        }
        else {
            return toString().compareTo(object.toString());
        }
    }

    
    public String toString() {
        return "RestrictedModule["+morphism+","+module+"]";
    }
    
    
    public String toVisualString() {
        return "("+morphism.getDomain().toVisualString()+")"+module.toVisualString();
    }

    public String getElementTypeName() {
        return "RestrictedModule";
    }
    
    
    private RestrictedModule(ModuleMorphism morphism, Module module) {
        this.morphism = morphism;
        this.module = module;
    }
    
    
    private ModuleMorphism morphism;
    private Module         module;
}
