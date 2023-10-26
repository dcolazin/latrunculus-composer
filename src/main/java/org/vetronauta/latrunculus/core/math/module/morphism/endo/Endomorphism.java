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

package org.vetronauta.latrunculus.core.math.module.morphism.endo;

import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.PowerMorphism;

/**
 * @author vetronauta
 */
public abstract class Endomorphism<A extends ModuleElement<A, RA>, RA extends RingElement<RA>> extends ModuleMorphism<A,A,RA,RA> {

    protected Endomorphism(Module<A, RA> domain) {
        super(domain, domain);
    }

    /**
     * Returns this module morphism raise to the power <code>n</code>.
     * @throws CompositionException if power could not be performed
     */
    public Endomorphism<A,RA> power(int n) throws CompositionException {
        return PowerMorphism.make(this, n);
    }

}
