package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

public class ArithmeticAffineProjection<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<Vector<ArithmeticElement<N>>, ArithmeticElement<N>,N> {

    private final Vector<ArithmeticElement<N>> matrix;
    private final ArithmeticElement<N> vector;

    public ArithmeticAffineProjection(ArithmeticRing<N> ring, Vector<ArithmeticElement<N>> matrix, ArithmeticElement<N> vector) {
        super(new VectorModule<>(ring, matrix.getLength()), ring);
        this.matrix = matrix;
        this.vector = vector;
    }

    @Override
    public ArithmeticElement<N> map(Vector<ArithmeticElement<N>> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ArithmeticAffineProjection.map: ", x, this);
        }
        return matrix.scalarProduct(x).sum(vector);
    }

    @Override
    public ArithmeticElement<N> getVector() {
        return vector.deepCopy();
    }

    public Vector<ArithmeticElement<N>> getMatrix() {
        return matrix.deepCopy();
    }

    @Override
    public boolean isConstant() {
        return matrix.isZero();
    }

    @Override
    public ModuleMorphism compose(ModuleMorphism morphism) throws CompositionException {
        return super.compose(morphism);  //TODO see ArithmeticAffineMultiMorphism
    }

    @Override
    public ModuleMorphism sum(ModuleMorphism morphism) throws CompositionException {
        return super.sum(morphism);  //TODO see ArithmeticAffineMultiMorphism
    }

    @Override
    public ModuleMorphism difference(ModuleMorphism morphism) throws CompositionException {
        return super.difference(morphism);  //TODO see ArithmeticAffineMultiMorphism
    }

    @Override
    public ModuleMorphism scaled(ArithmeticElement<N> element) throws CompositionException {
        return super.scaled(element); //TODO see ArithmeticAffineMultiMorphism
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (!(object instanceof ArithmeticAffineProjection)) {
            return super.compareTo(object);
        }
        ArithmeticAffineProjection<?> morphism = (ArithmeticAffineProjection<?>) object;
        int comp = matrix.compareTo(morphism.matrix);
        if (comp != 0) {
            return comp;
        }
        return vector.compareTo(morphism.vector);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArithmeticAffineProjection)) {
            return false;
        }
        ArithmeticAffineProjection<?> morphism = (ArithmeticAffineProjection<?>) object;
        return matrix.equals(morphism.matrix) && vector.equals(morphism.vector);
    }

    @Override
    public String toString() {
        return String.format("ArithmeticAffineProjection<%s>[%d]",
                getBaseRing().toVisualString(), getDomain().getDimension());
    }

    public String getElementTypeName() {
        return "ArithmeticAffineProjection";
    }

}
