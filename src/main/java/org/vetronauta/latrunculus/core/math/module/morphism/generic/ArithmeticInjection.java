package org.vetronauta.latrunculus.core.math.module.morphism.generic;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;

import java.util.List;

public abstract class ArithmeticInjection<N extends ArithmeticNumber<N>>
        extends ArithmeticFreeMorphism<ArithmeticElement<N>, ArithmeticMultiElement<N>, N> {

    protected ArithmeticInjection(ArithmeticRing<N> ring, int codomainDim) {
        super(ring, new ArithmeticMultiModule<>(ring, codomainDim));
    }

    public final ArithmeticMultiElement<N> map(ArithmeticElement<N> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ArithmeticMultiMorphism.map: ", x, this);
        }
        return new ArithmeticMultiElement<>((ArithmeticRing<N>) getDomain(), mapValue(x));
    }

    /**
     * The low-level map method.
     * This must be implemented by subclasses.
     */
    public abstract List<ArithmeticElement<N>> mapValue(ArithmeticElement<N> element);

}
