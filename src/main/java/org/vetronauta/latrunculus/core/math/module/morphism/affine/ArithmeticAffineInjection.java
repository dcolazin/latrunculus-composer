package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

public class ArithmeticAffineInjection<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<ArithmeticElement<N>,ArithmeticMultiElement<N>,N> {

    private final ArithmeticMultiElement<N> matrix;
    private final ArithmeticMultiElement<N> vector;

    public ArithmeticAffineInjection(ArithmeticRing<N> ring, ArithmeticMultiElement<N> matrix, ArithmeticMultiElement<N> vector) {
        super(ring, new ArithmeticMultiModule<>(ring, matrix.getLength()));
        this.matrix = matrix;
        this.vector = vector;
    }

    @Override
    public ArithmeticMultiElement<N> map(ArithmeticElement<N> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ArithmeticAffineInjection.map: ", x, this);
        }
        return matrix.scaled(x).sum(vector);
    }

    @Override
    public ArithmeticMultiElement<N> getVector() {
        return vector.deepCopy();
    }

    public ArithmeticMultiElement<N> getMatrix() {
        return matrix.deepCopy();
    } //TODO common logic

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
        if (!(object instanceof ArithmeticAffineInjection)) {
            return super.compareTo(object);
        }
        ArithmeticAffineInjection<?> morphism = (ArithmeticAffineInjection<?>) object;
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
        if (!(object instanceof ArithmeticAffineInjection)) {
            return false;
        }
        ArithmeticAffineInjection<?> morphism = (ArithmeticAffineInjection<?>) object;
        return matrix.equals(morphism.matrix) && vector.equals(morphism.vector);
    }

    @Override
    public String toString() {
        return String.format("ArithmeticAffineInjection<%s>[%d]",
                getBaseRing().toVisualString(), getCodomain().getDimension());
    }

    public String getElementTypeName() {
        return "ArithmeticAffineInjection";
    }

}
