package org.vetronauta.latrunculus.core.math.module.morphism.generic;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;

import java.util.List;

public abstract class ArithmeticProjection<N extends ArithmeticNumber<N>>
        extends ArithmeticFreeMorphism<ArithmeticMultiElement<N>, ArithmeticElement<N>, N> {

    protected ArithmeticProjection(ArithmeticRing<N> ring, int domainDim) {
        super(new ArithmeticMultiModule<>(ring, domainDim), ring);
    }

    @Override
    public final ArithmeticElement<N> map(ArithmeticMultiElement<N> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ArithmeticMultiMorphism.map: ", x, this);
        }
        return mapValue(x.getValue());
    }

    /**
     * The low-level map method.
     * This must be implemented by subclasses.
     */
    public abstract ArithmeticElement<N> mapValue(List<ArithmeticElement<N>> list);

}
