package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.matrix.ArithmeticMatrix;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.List;
import java.util.stream.Collectors;

public class ArithmeticAffineMultiMorphism<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<ArithmeticMultiElement<N>,ArithmeticMultiElement<N>,N> {

    private final ArithmeticMatrix<N> matrix;
    private final List<ArithmeticElement<N>> vector;

    public ArithmeticAffineMultiMorphism(ArithmeticRing<N> ring, ArithmeticMatrix<N> matrix, List<N> vector) {
        super(new ArithmeticMultiModule<>(ring, matrix.getColumnCount()), new ArithmeticMultiModule<>(ring, matrix.getRowCount()));
        this.matrix = matrix;
        this.vector = vector.stream().map(ArithmeticElement::new).collect(Collectors.toList());
    }

    @Override
    public ArithmeticMultiElement<N> map(ArithmeticMultiElement<N> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ArithmeticAffineMultiMorphism.map: ", x, this);
        }
        return new ArithmeticMultiElement<>((ArithmeticRing<N>) getDomain().getRing(), mapValue(x.getValue()));
    }

    private List<ArithmeticElement<N>> mapValue(List<ArithmeticElement<N>> value) {
        return null; //TODO
    }

    @Override
    public List<ArithmeticElement<N>> getVector() {
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
    public ModuleMorphism compose(ModuleMorphism morphism) throws CompositionException {
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
        return new ArithmeticMultiElement<>(getBaseRing(), vector);
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
        for (int i = 0; i < vector.size(); i++) {
            int comp1 = vector.get(i).compareTo(morphism.vector.get(i));
            if (comp1 != 0) {
                return comp1;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArithmeticAffineMultiMorphism)) {
            return false;
        }
        ArithmeticAffineMultiMorphism<?> morphism = (ArithmeticAffineMultiMorphism<?>)object;
        if (!matrix.equals(morphism.matrix)) {
            return false;
        }
        for (int i = 0; i < vector.size(); i++) {
            if (!vector.get(i).equals(morphism.vector.get(i))) {
                return false;
            }
        }
        return true;
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
