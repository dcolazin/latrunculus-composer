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
import org.vetronauta.latrunculus.core.math.matrix.Matrix;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

/**
 * @author vetronauta
 */
public class AffineMultiMorphism<R extends RingElement<R>> extends AffineFreeMorphism<Vector<R>,Vector<R>,R> {

    private final Matrix<R> matrix;
    private final Vector<R> vector;

    public AffineMultiMorphism(Ring<R> ring, Matrix<R> matrix, Vector<R> vector) {
        super(new VectorModule<>(ring, matrix.getColumnCount()), new VectorModule<>(ring, matrix.getRowCount()));
        this.matrix = matrix;
        this.vector = vector;
    }

    @Override
    public Vector<R> map(Vector<R> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("AffineMultiMorphism.map: ", x, this);
        }
        return matrix.product(x).sum(vector);
    }

    @Override
    public Vector<R> getVector() {
        return vector;
    }

    public Matrix<R> getMatrix() {
        return matrix; //TODO deepcopy
    }

    @Override
    public boolean isIdentity() {
        return isLinear() && matrix.isUnit();
    }

    @Override
    public boolean isConstant() {
        return matrix.isZero();
    }

    @Override
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphism<C,Vector<R>,RC,R>
    compose(ModuleMorphism<C,Vector<R>,RC,R> morphism) throws CompositionException {
        if (!composable(this, morphism)) {
            throw new CompositionException("CompositionMorphism.make: Cannot compose " + this + " with " + morphism);
        }
        if (morphism instanceof AffineMultiMorphism) {
            AffineMultiMorphism<R> other = (AffineMultiMorphism) morphism;
            return (ModuleMorphism) new AffineMultiMorphism<>(getBaseRing(), matrix.product(other.matrix), matrix.product(other.vector).sum(vector));
        }
        if (morphism instanceof AffineInjection) {
            AffineInjection<R> other = (AffineInjection) morphism;
            return (ModuleMorphism) new AffineInjection<>(getBaseRing(), matrix.product(other.getMatrix()), matrix.product(other.getVector()).sum(vector));
        }
        return super.compose(morphism);
    }

    @Override
    public ModuleMorphism<Vector<R>,Vector<R>,R,R>
    sum(ModuleMorphism<Vector<R>,Vector<R>,R,R> morphism) throws CompositionException {
        if (morphism instanceof AffineMultiMorphism) {
            AffineMultiMorphism<R> other = (AffineMultiMorphism<R>) morphism;
            return new AffineMultiMorphism<>(getBaseRing(), matrix.sum(other.matrix), vector.sum(other.vector));
        }
        return super.sum(morphism);
    }

    @Override
    public ModuleMorphism<Vector<R>,Vector<R>,R,R>
    difference(ModuleMorphism<Vector<R>,Vector<R>,R,R> morphism) throws CompositionException {
        if (morphism instanceof AffineMultiMorphism) {
            AffineMultiMorphism<R> other = (AffineMultiMorphism<R>) morphism;
            return new AffineMultiMorphism<>(getBaseRing(), matrix.difference(other.matrix), vector.difference(other.vector));
        }
        return super.difference(morphism);
    }

    @Override
    public AffineMultiMorphism<R> scaled(R element) throws CompositionException {
        return new AffineMultiMorphism<>(getBaseRing(), matrix.scaled(element), vector.scaled(element));
    }

    @Override
    public Vector<R> atZero() {
        return vector.deepCopy();
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (!(object instanceof AffineMultiMorphism)) {
            return super.compareTo(object);
        }
        AffineMultiMorphism<?> morphism = (AffineMultiMorphism<?>)object;
        int comp = matrix.compareTo(morphism.matrix);
        if (comp != 0) {
            return comp;
        }
        return vector.compareTo(morphism.getVector());
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AffineMultiMorphism)) {
            return false;
        }
        AffineMultiMorphism<?> morphism = (AffineMultiMorphism<?>)object;
        return matrix.equals(morphism.matrix) && vector.equals(morphism.vector);
    }

    @Override
    public String toString() {
        return String.format("AffineMultiMorphism<%s>[%d,%d]",
                getBaseRing().toVisualString(), getDomain().getDimension(), getCodomain().getDimension());
    }

    public String getElementTypeName() {
        return "AffineMultiMorphism";
    }
    
}
