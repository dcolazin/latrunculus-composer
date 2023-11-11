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

package org.vetronauta.latrunculus.core.math.morphism.endo;

import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;

/**
 * @author vetronauta
 */
public abstract class RingEndomorphism<RA extends RingElement<RA>> extends Endomorphism<RA,RA> {

    protected RingEndomorphism(Ring<RA> domain) {
        super(domain);
    }

    @Override
    public Ring<RA> getDomain() {
        return super.getDomain().getRing();
    }

    @Override
    public RingEndomorphism<RA> sum(ModuleMorphism<RA,RA,RA,RA> other) throws CompositionException {
        return new RingEndomorphismWrapper<>(super.sum(other));
    }

    @Override
    public RingEndomorphism<RA> difference(ModuleMorphism<RA,RA,RA,RA> other) throws CompositionException {
        return new RingEndomorphismWrapper<>(super.difference(other));
    }

    @Override
    public Endomorphism<RA, RA> getRingMorphism() {
        return ModuleMorphism.getIdentityMorphism(getDomain());
    }


}
