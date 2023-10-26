package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.List;
import java.util.stream.Collectors;

public class ArithmeticAffineInjection<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<ArithmeticElement<N>,ArithmeticMultiElement<N>,N> {

    private final List<ArithmeticElement<N>> matrix;
    private final List<ArithmeticElement<N>> vector;

    public ArithmeticAffineInjection(ArithmeticRing<N> ring, List<N> matrix, List<N> vector) {
        super(ring, new ArithmeticMultiModule<>(ring, matrix.size()));
        this.matrix = matrix.stream().map(ArithmeticElement::new).collect(Collectors.toList());
        this.vector = vector.stream().map(ArithmeticElement::new).collect(Collectors.toList());
    }

    @Override
    public ArithmeticMultiElement<N> map(ArithmeticElement<N> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ArithmeticAffineInjection.map: ", x, this);
        }
        return new ArithmeticMultiElement<>((ArithmeticRing<N>) getDomain().getRing(), mapValue(x));
    }

    private List<ArithmeticElement<N>> mapValue(ArithmeticElement<N> value) {
        return null; //TODO
    }

    @Override
    public List<ArithmeticElement<N>> getVector() {
        return vector;
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
        if (!(object instanceof ArithmeticAffineInjection)) {
            return super.compareTo(object);
        }
        ArithmeticAffineInjection<?> morphism = (ArithmeticAffineInjection<?>) object;
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
        if (!(object instanceof ArithmeticAffineInjection)) {
            return false;
        }
        ArithmeticAffineInjection<?> morphism = (ArithmeticAffineInjection<?>) object;
        for (int i = 0; i < matrix.size(); i++) {
            if (!matrix.get(i).equals(morphism.matrix.get(i))) {
                return false;
            }
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
        return String.format("ArithmeticAffineInjection<%s>[%d]",
                getBaseRing().toVisualString(), getCodomain().getDimension());
    }

    public String getElementTypeName() {
        return "ArithmeticAffineInjection";
    }

}
