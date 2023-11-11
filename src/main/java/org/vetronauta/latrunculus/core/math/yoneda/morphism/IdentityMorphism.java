/*
 * Copyright (C) 2002, 2005 Gérard Milmeister
 * Copyright (C) 2002 Stefan Müller
 * Copyright (C) 2002 Stefan Göller
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

package org.vetronauta.latrunculus.core.math.yoneda.morphism;

import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.Diagram;
import org.vetronauta.latrunculus.core.math.yoneda.map.EmptyMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.map.MorphismMap;

/**
 * Abstract base class for identity morphisms.
 * Identity morphisms represent "objects" in our categorical setting.
 * Domain and codomain are the same and are the effective "object".
 * The implemented map is simply the identity map.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public abstract class IdentityMorphism extends YonedaMorphism {

    /**
     * Returns the diagram of this identity morphism.
     */
    public abstract Diagram getDiagram();
    
    /**
     * Returns the module of this identity morphism.
     */
    public abstract Module getModule();

    /**
     * Returns the type of this identity morphism.
     */
    public abstract FormDenotatorTypeEnum getType();
    
    public Diagram getDomainDiagram() { 
        return getDiagram();
    }

    public Diagram getCodomainDiagram() { 
        return getDiagram(); 
    }
    
    public Module getDomainModule() {
        return getModule();
    }
    
    public Module getCodomainModule() {
        return getModule();
    }
    
    public FormDenotatorTypeEnum getDomainType() {
        return getType(); 
    }
    
    public FormDenotatorTypeEnum getCodomainType() {
        return getType();
    }

    public IdentityMorphism getDomain() {
        return this;
    }
    
    public IdentityMorphism getCodomain() {
        return this;
    }

    public MorphismMap getMap() {
        return EmptyMorphismMap.emptyMorphismMap;
    }
    
    public void setMap(MorphismMap map) {
        throw new UnsupportedOperationException("Setting a map not allowed for identity morphisms.");
    }
    
    public final boolean isIdentity() {
        return true;
    }
    
    public boolean isRepresentable() {
        return getType() == FormDenotatorTypeEnum.SIMPLE;
    }
    
    public abstract int hashCode();
}
