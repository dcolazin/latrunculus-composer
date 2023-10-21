/*
 * latrunculus-composer
 * Copyright (C) 2023 vetronauta
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.vetronauta.latrunculus.core.math.module.morphism.generic;

import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.MappingException;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

/**
 * @author vetronauta
 */
public class RingEndomorphismWrapper<RA extends RingElement<RA>> extends RingEndomorphism<RA> {

    private final ModuleMorphism<RA,RA,RA,RA> internalMorphism;

    public RingEndomorphismWrapper(ModuleMorphism<RA,RA,RA,RA> morphism) {
        super(morphism.getDomain().getRing());
        this.internalMorphism = morphism;
    }

    @Override
    public RA map(RA x) throws MappingException {
        return internalMorphism.map(x);
    }

    @Override
    public ModuleMorphism<RA, RA, RA, RA> getRingMorphism() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        return internalMorphism.equals(object);
    }

    @Override
    public String toString() {
        return String.format("RingEndomorphism:%s", internalMorphism);
    }
}
