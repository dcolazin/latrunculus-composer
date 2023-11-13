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

package org.vetronauta.latrunculus.core.math.morphism;

import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.generic.DirectSumModule;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.exception.MappingException;

import java.util.ArrayList;
import java.util.List;

/**
 * The abstract base class for morphisms in a composite module.
 * 
 * @author Gérard Milmeister
 */
public abstract class DirectSumMorphism<RA extends RingElement<RA>, RB extends RingElement<RB>>
        extends ModuleMorphism<DirectSumElement<RA>,DirectSumElement<RB>,RA,RB> {

    protected DirectSumMorphism(DirectSumModule<RA> domain, DirectSumModule<RB> codomain) {
        super(domain, codomain);
    }

    public final DirectSumElement<RB> map(DirectSumElement<RA> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("DirectSumAbstractMorphism.map: ", x, this);
        }
        List<ModuleElement<?,RA>> components = new ArrayList<>(x.getLength());
        for (int i = 0; i < x.getLength(); i++) {
            components.add(x.getComponent(i));
        }
        return mapValue(DirectSumElement.make(components));
    }
    
    /**
     * The low-level map method.
     * This must be implemented by subclasses.
     */
    public abstract DirectSumElement<RB> mapValue(DirectSumElement<RA> x);
}
