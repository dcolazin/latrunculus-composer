package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArithmeticAffineProjection<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<ArithmeticMultiElement<N>, ArithmeticElement<N>,N> {

    private final List<ArithmeticElement<N>> matrix;
    private final ArithmeticElement<N> vector;

    public ArithmeticAffineProjection(ArithmeticRing<N> ring, List<N> matrix, N vector) {
        super(new ArithmeticMultiModule<>(ring, matrix.size()), ring);
        this.matrix = matrix.stream().map(ArithmeticElement::new).collect(Collectors.toList());
        this.vector = new ArithmeticElement<>(vector);
    }

    @Override
    public ArithmeticElement<N> map(ArithmeticMultiElement<N> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ArithmeticAffineProjection.map: ", x, this);
        }
        return mapValue(x.getValue());
    }

    private ArithmeticElement<N> mapValue(List<ArithmeticElement<N>> value) {
        return null; //TODO
    }

    @Override
    public List<ArithmeticElement<N>> getVector() {
        List<ArithmeticElement<N>> list = new ArrayList<>();
        list.add(vector);
        return list;
    }

    @Override
    public boolean isConstant() {
        return matrix.stream().allMatch(ArithmeticElement::isZero);
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
    public int compareTo(ModuleMorphism object) {
        if (!(object instanceof ArithmeticAffineProjection)) {
            return super.compareTo(object);
        }
        ArithmeticAffineProjection<?> morphism = (ArithmeticAffineProjection<?>) object;
        if (!getBaseRing().equals(morphism.getBaseRing())) {
            return getBaseRing().compareTo(morphism.getBaseRing());
        }
        int comp = matrix.size() - morphism.matrix.size();
        if (comp != 0) {
            return comp;
        }
        for (int i = 0; i < matrix.size(); i++) {
            int comp1 = matrix.get(i).compareTo(morphism.matrix.get(i));
            if (comp1 != 0) {
                return comp1;
            }
        }
        return vector.compareTo(morphism.vector);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArithmeticAffineProjection)) {
            return false;
        }
        ArithmeticAffineProjection<?> morphism = (ArithmeticAffineProjection<?>) object;
        for (int i = 0; i < matrix.size(); i++) {
            if (!matrix.get(i).equals(morphism.matrix.get(i))) {
                return false;
            }
        }
        return vector.equals(morphism.vector);
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
