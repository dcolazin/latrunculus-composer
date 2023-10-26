package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;

import java.util.List;

public class ArithmeticAffineInjection<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<ArithmeticElement<N>,ArithmeticMultiElement<N>,N> {

    private final List<N> matrix;
    private final List<N> vector;

    public ArithmeticAffineInjection(ArithmeticRing<N> ring, List<N> matrix, List<N> vector) {
        super(ring, new ArithmeticMultiModule<>(ring, matrix.size()));
        this.matrix = matrix;
        this.vector = vector;
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
    public boolean equals(Object object) {
        return false; //TODO
    }

    @Override
    public String toString() {
        return null; //TODO
    }
}
