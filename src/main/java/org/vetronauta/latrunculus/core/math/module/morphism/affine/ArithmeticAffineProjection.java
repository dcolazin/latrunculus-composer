package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;

import java.util.List;

public class ArithmeticAffineProjection<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<ArithmeticMultiElement<N>, ArithmeticElement<N>,N> {

    private final List<N> matrix;
    private final N vector;

    public ArithmeticAffineProjection(ArithmeticRing<N> ring, List<N> matrix, N vector) {
        super(new ArithmeticMultiModule<>(ring, matrix.size()), ring);
        this.matrix = matrix;
        this.vector = vector;
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
    public boolean equals(Object object) {
        return false; //TODO
    }

    @Override
    public String toString() {
        return null; //TODO
    }

}
