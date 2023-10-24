package org.vetronauta.latrunculus.core.math.module.morphism.generic;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.morphism.MappingException;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.List;

public abstract class ArithmeticMultiMorphism<N extends ArithmeticNumber<N>>
        extends ModuleMorphism<ArithmeticMultiElement<N>,ArithmeticMultiElement<N>,ArithmeticElement<N>,ArithmeticElement<N>> {

    protected ArithmeticMultiMorphism(ArithmeticRing<N> ring, int domDim, int codomDim) {
        super(new ArithmeticMultiModule<>(ring, domDim), new ArithmeticMultiModule<>(ring, codomDim));
    }

    public final ArithmeticMultiElement<N> map(ArithmeticMultiElement<N> x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("CFreeAbstractMorphism.map: ", x, this);
        }
        return new ArithmeticMultiElement<>(((ArithmeticMultiModule<N>)getDomain()).getRing(), mapValue(x.getValue()));
    }

    /**
     * The low-level map method.
     * This must be implemented by subclasses.
     */
    public abstract List<ArithmeticElement<N>> mapValue(List<ArithmeticElement<N>> list);

}
