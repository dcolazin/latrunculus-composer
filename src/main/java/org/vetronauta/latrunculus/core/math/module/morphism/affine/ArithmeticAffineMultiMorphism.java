package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.matrix.Matrix;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

public class ArithmeticAffineMultiMorphism<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<Vector<ArithmeticElement<N>>,Vector<ArithmeticElement<N>>,N> {

    //TODO generalize to any ring

    private final Matrix<ArithmeticElement<N>> matrix;
    private final Vector<ArithmeticElement<N>> vector;

    public ArithmeticAffineMultiMorphism(ArithmeticRing<N> ring, Matrix<ArithmeticElement<N>> matrix, Vector<ArithmeticElement<N>> vector) {
        super(new VectorModule<>(ring, matrix.getColumnCount()), new VectorModule<>(ring, matrix.getRowCount()));
        this.matrix = matrix;
        this.vector = vector;
    }

    @Override
    public Vector<ArithmeticElement<N>> map(Vector<ArithmeticElement<N>> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ArithmeticAffineMultiMorphism.map: ", x, this);
        }
        return matrix.product(x).sum(vector);
    }

    @Override
    public Vector<ArithmeticElement<N>> getVector() {
        return vector;
    }

    public Matrix<ArithmeticElement<N>> getMatrix() {
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
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphism<C,Vector<ArithmeticElement<N>>,RC,ArithmeticElement<N>>
    compose(ModuleMorphism<C,Vector<ArithmeticElement<N>>,RC,ArithmeticElement<N>> morphism) throws CompositionException {
        if (!composable(this, morphism)) {
            throw new CompositionException("CompositionMorphism.make: Cannot compose " + this + " with " + morphism);
        }
        if (morphism instanceof ArithmeticAffineMultiMorphism) {
            ArithmeticAffineMultiMorphism<N> other = (ArithmeticAffineMultiMorphism) morphism;
            return (ModuleMorphism) new ArithmeticAffineMultiMorphism<>(getBaseRing(), matrix.product(other.matrix), matrix.product(other.vector).sum(vector));
        }
        if (morphism instanceof ArithmeticAffineInjection) {
            ArithmeticAffineInjection<N> other = (ArithmeticAffineInjection) morphism;
            return (ModuleMorphism) new ArithmeticAffineInjection<>(getBaseRing(), matrix.product(other.getMatrix()), matrix.product(other.getVector()).sum(vector));
        }
        return super.compose(morphism);
    }

    @Override
    public ModuleMorphism<Vector<ArithmeticElement<N>>,Vector<ArithmeticElement<N>>,ArithmeticElement<N>,ArithmeticElement<N>>
    sum(ModuleMorphism<Vector<ArithmeticElement<N>>,Vector<ArithmeticElement<N>>,ArithmeticElement<N>,ArithmeticElement<N>> morphism) throws CompositionException {
        if (morphism instanceof ArithmeticAffineMultiMorphism) {
            ArithmeticAffineMultiMorphism<N> other = (ArithmeticAffineMultiMorphism<N>) morphism;
            return new ArithmeticAffineMultiMorphism<>(getBaseRing(), matrix.sum(other.matrix), vector.sum(other.vector));
        }
        return super.sum(morphism);
    }

    @Override
    public ModuleMorphism<Vector<ArithmeticElement<N>>,Vector<ArithmeticElement<N>>,ArithmeticElement<N>,ArithmeticElement<N>>
    difference(ModuleMorphism<Vector<ArithmeticElement<N>>,Vector<ArithmeticElement<N>>,ArithmeticElement<N>,ArithmeticElement<N>> morphism) throws CompositionException {
        if (morphism instanceof ArithmeticAffineMultiMorphism) {
            ArithmeticAffineMultiMorphism<N> other = (ArithmeticAffineMultiMorphism<N>) morphism;
            return new ArithmeticAffineMultiMorphism<>(getBaseRing(), matrix.difference(other.matrix), vector.difference(other.vector));
        }
        return super.difference(morphism);
    }

    @Override
    public ArithmeticAffineMultiMorphism<N> scaled(ArithmeticElement<N> element) throws CompositionException {
        return new ArithmeticAffineMultiMorphism<>(getBaseRing(), matrix.scaled(element), vector.scaled(element));
    }

    @Override
    public Vector<ArithmeticElement<N>> atZero() {
        return vector.deepCopy();
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (!(object instanceof ArithmeticAffineMultiMorphism)) {
            return super.compareTo(object);
        }
        ArithmeticAffineMultiMorphism<?> morphism = (ArithmeticAffineMultiMorphism<?>)object;
        int comp = matrix.compareTo(morphism.matrix);
        if (comp != 0) {
            return comp;
        }
        return vector.compareTo(morphism.getVector());
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArithmeticAffineMultiMorphism)) {
            return false;
        }
        ArithmeticAffineMultiMorphism<?> morphism = (ArithmeticAffineMultiMorphism<?>)object;
        return matrix.equals(morphism.matrix) && vector.equals(morphism.vector);
    }

    @Override
    public String toString() {
        return String.format("ArithmeticAffineMultiMorphism<%s>[%d,%d]",
                getBaseRing().toVisualString(), getDomain().getDimension(), getCodomain().getDimension());
    }

    public String getElementTypeName() {
        return "ArithmeticAffineMultiMorphism";
    }

}
