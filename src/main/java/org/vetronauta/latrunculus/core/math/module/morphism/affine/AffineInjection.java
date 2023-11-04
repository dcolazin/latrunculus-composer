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

package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

/**
 * @author vetronauta
 */
public class AffineInjection<R extends RingElement<R>> extends AffineFreeMorphism<R, Vector<R>, R>  {

    private final Vector<R> matrix;
    private final Vector<R> vector;

    public AffineInjection(Ring<R> ring, Vector<R> matrix, Vector<R> vector) {
        super(ring, new VectorModule<>(ring, vector.getLength()));
        this.matrix = matrix;
        this.vector = vector;
    }

    @Override
    public Vector<R> map(R x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("AffineInjection.map: ", x, this);
        }
        return matrix.scaled(x).sum(vector);
    }

    @Override
    public Vector<R> getVector() {
        return vector.deepCopy();
    }

    public Vector<R> getMatrix() {
        return matrix.deepCopy();
    } //TODO common logic

    @Override
    public boolean isConstant() {
        return matrix.isZero();
    }

    @Override
    public ModuleMorphism compose(ModuleMorphism morphism) throws CompositionException {
        return super.compose(morphism);  //TODO see AffineMultiMorphism
    }

    @Override
    public ModuleMorphism sum(ModuleMorphism morphism) throws CompositionException {
        return super.sum(morphism);  //TODO see AffineMultiMorphism
    }

    @Override
    public ModuleMorphism difference(ModuleMorphism morphism) throws CompositionException {
        return super.difference(morphism);  //TODO see AffineMultiMorphism
    }

    @Override
    public ModuleMorphism scaled(R element) throws CompositionException {
        return super.scaled(element); //TODO see AffineMultiMorphism
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (!(object instanceof AffineInjection)) {
            return super.compareTo(object);
        }
        AffineInjection<?> morphism = (AffineInjection<?>) object;
        if (!getBaseRing().equals(morphism.getBaseRing())) {
            return getBaseRing().compareTo(morphism.getBaseRing());
        }
        int comp = matrix.compareTo(morphism.matrix);
        if (comp != 0) {
            return comp;
        }
        return vector.compareTo(morphism.vector);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AffineInjection)) {
            return false;
        }
        AffineInjection<?> morphism = (AffineInjection<?>) object;
        return matrix.equals(morphism.matrix) && vector.equals(morphism.vector);
    }

    @Override
    public String toString() {
        return String.format("AffineInjection<%s>[%d]",
                getBaseRing().toVisualString(), getCodomain().getDimension());
    }

    public String getElementTypeName() {
        return "AffineInjection";
    }
    
}
