package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.matrix.ArithmeticMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.List;

public class ArithmeticAffineMultiMorphism<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<ArithmeticMultiElement<N>,ArithmeticMultiElement<N>,N> {

    private final ArithmeticMatrix<N> matrix;
    private final ArithmeticMultiElement<N> vector;

    public ArithmeticAffineMultiMorphism(ArithmeticRing<N> ring, ArithmeticMatrix<N> matrix, ArithmeticMultiElement<N> vector) {
        super(new ArithmeticMultiModule<>(ring, matrix.getColumnCount()), new ArithmeticMultiModule<>(ring, matrix.getRowCount()));
        this.matrix = matrix;
        this.vector = vector;
    }

    @Override
    public ArithmeticMultiElement<N> map(ArithmeticMultiElement<N> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ArithmeticAffineMultiMorphism.map: ", x, this);
        }
        return matrix.product(x).sum(vector);
    }

    @Override
    public ArithmeticMultiElement<N> getVector() {
        return vector;
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
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphism<C,ArithmeticMultiElement<N>,RC,ArithmeticElement<N>>
    compose(ModuleMorphism<C,ArithmeticMultiElement<N>,RC,ArithmeticElement<N>> morphism) throws CompositionException {
        if (!composable(this, morphism)) {
            throw new CompositionException("CompositionMorphism.make: Cannot compose " + this + " with " + morphism);
        }
        if (morphism instanceof ArithmeticAffineMultiMorphism) {
            ArithmeticAffineMultiMorphism<N> other = (ArithmeticAffineMultiMorphism) morphism;
            return (ModuleMorphism) new ArithmeticAffineMultiMorphism<>(getBaseRing(), matrix.product(other.matrix), matrix.product(other.vector).sum(vector));
        }
        return super.compose(morphism);  //TODO see CFreeAffineMorphism
    }

    @Override
    public ModuleMorphism sum(ModuleMorphism morphism) throws CompositionException {
        return super.sum(morphism);  //TODO see CFreeAffineMorphism
    }

    @Override
    public ModuleMorphism difference(ModuleMorphism morphism) throws CompositionException {
        return super.difference(morphism);  //TODO see CFreeAffineMorphism
    }

    @Override
    public ModuleMorphism scaled(ArithmeticElement<N> element) throws CompositionException {
        return super.scaled(element); //TODO see CFreeAffineMorphism
    }

    @Override
    public ArithmeticMultiElement<N> atZero() {
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
