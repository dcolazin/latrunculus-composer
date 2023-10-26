package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;

import java.util.List;

public class ArithmeticAffineProjection<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<ArithmeticMultiElement<N>, ArithmeticElement<N>,N> {

    protected ArithmeticAffineProjection(ArithmeticMultiModule<N> domain) {
        super(domain, domain.getRing());
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
