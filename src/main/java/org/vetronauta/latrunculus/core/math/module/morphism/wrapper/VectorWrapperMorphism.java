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

package org.vetronauta.latrunculus.core.math.module.morphism.wrapper;

import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vetronauta
 */
public class VectorWrapperMorphism<R extends RingElement<R>> extends ModuleMorphism<R,Vector<R>,R,R> {

    //TODO another solution is to use FreeElements instead of Vectors

    public VectorWrapperMorphism(Ring<R> domain) {
        super(domain, new VectorModule<>(domain, 1));
    }

    private Ring<R> getRing() {
        return (Ring<R>) getDomain();
    }

    @Override
    public Vector<R> map(R x) throws MappingException {
        List<R> list = new ArrayList<>(1);
        list.add(x);
        return new Vector<>(x.getRing(), list);
    }

    @Override
    public ModuleMorphism<R, R, R, R> getRingMorphism() {
        return ModuleMorphism.getIdentityMorphism(getRing());
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof VectorWrapperMorphism && getRing().equals(((VectorWrapperMorphism<?>) object).getRing());
    }

    @Override
    public String toString() {
        return String.format("VectorWrapperMorphism<%s>", getRing().toVisualString());
    }
}
