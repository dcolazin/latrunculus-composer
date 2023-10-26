package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;

import java.util.List;

public class ArithmeticAffineMultiMorphism<N extends ArithmeticNumber<N>> extends
        ArithmeticAffineFreeMorphism<ArithmeticMultiElement<N>,ArithmeticMultiElement<N>,N> {

    protected ArithmeticAffineMultiMorphism(ArithmeticMultiModule<N> domain, ArithmeticMultiModule<N> codomain) {
        super(domain, codomain);
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
    public boolean equals(Object object) {
        return false; //TODO
    }

    @Override
    public String toString() {
        return null; //TODO
    }

}
