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

package org.vetronauta.latrunculus.core.math.morphism.wrapper;

import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;

/**
 * @author vetronauta
 */
public class VectorDewrapperMorphism<R extends RingElement<R>> extends ModuleMorphism<Vector<R>,R,R,R> {

    //TODO another solution is to use FreeElements instead of Vectors

    public VectorDewrapperMorphism(Ring<R> codomain) {
        super(new VectorModule<>(codomain, 1), codomain);
    }

    private Ring<R> getRing() {
        return (Ring<R>) getCodomain();
    }

    @Override
    public R map(Vector<R> x) throws MappingException {
        return x.getComponent(0);
    }

    @Override
    public ModuleMorphism<R, R, R, R> getRingMorphism() {
        return ModuleMorphism.getIdentityMorphism(getRing());
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof VectorDewrapperMorphism && getRing().equals(((VectorDewrapperMorphism<?>) object).getRing());
    }

    @Override
    public String toString() {
        return String.format("VectorDewrapperMorphism<%s>", getRing().toVisualString());
    }
}
