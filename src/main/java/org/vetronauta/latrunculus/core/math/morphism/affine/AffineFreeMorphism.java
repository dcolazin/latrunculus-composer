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

package org.vetronauta.latrunculus.core.math.morphism.affine;

import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.matrix.Matrix;
import org.vetronauta.latrunculus.core.math.element.generic.FreeElement;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.generic.FreeMorphism;

/**
 * @author vetronauta
 */
public abstract class AffineFreeMorphism<A extends FreeElement<A,R>, B extends FreeElement<B,R>, R extends RingElement<R>>
        extends FreeMorphism<A,B,R> {

    protected AffineFreeMorphism(Module<A, R> domain, Module<B, R> codomain) {
        super(domain, codomain);
    }

    public static <X extends RingElement<X>> ModuleMorphism<?,?,X,X>
    make(Ring<X> ring, Matrix<X> matrix, Vector<X> vector) {
        if (matrix.getRowCount() != vector.getLength()) {
            throw new IllegalArgumentException("Rows of A don't match length of b.");
        }
        if (matrix.getColumnCount() == 1 && matrix.getRowCount() == 1) {
            return new AffineRingMorphism<>(ring, matrix.get(0, 0), vector.getComponent(0));
        }
        if (matrix.getColumnCount() == 1) {
            return new AffineInjection<>(ring, matrix.getColumn(0), vector);
        }
        if (matrix.getRowCount() == 1) {
            return new AffineProjection<>(ring, matrix.getRow(0), vector.getComponent(0));
        }
        return new AffineMultiMorphism<>(ring, matrix, vector);
    }

    public Ring<R> getBaseRing() {
        return getDomain().getRing();
    }

    @Override
    public boolean isModuleHomomorphism() {
        return true;
    }

    @Override
    public boolean isLinear() {
        return getVector().isZero();
    }

    protected abstract FreeElement<?, R> getVector();

}
