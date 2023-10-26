package org.vetronauta.latrunculus.core.math.module.morphism.generic;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

public abstract class ArithmeticFreeMorphism<A extends FreeElement<A,ArithmeticElement<N>>, B extends FreeElement<B,ArithmeticElement<N>>, N extends ArithmeticNumber<N>>
        extends ModuleMorphism<A,B,ArithmeticElement<N>,ArithmeticElement<N>> {

    //TODO make static method to use domainDim and codomainDim with ArithmeticMultiModule.make

    protected ArithmeticFreeMorphism(Module<A, ArithmeticElement<N>> domain, Module<B, ArithmeticElement<N>> codomain) {
        super(domain, codomain);
    }

    @Override
    public ModuleMorphism<ArithmeticElement<N>,ArithmeticElement<N>,ArithmeticElement<N>,ArithmeticElement<N>> getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }


}
