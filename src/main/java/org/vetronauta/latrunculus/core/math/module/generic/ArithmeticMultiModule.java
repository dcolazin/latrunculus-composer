package org.vetronauta.latrunculus.core.math.module.generic;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArithmeticMultiModule<N extends ArithmeticNumber<N>> extends ProperFreeModule<ArithmeticMultiElement<N>, ArithmeticElement<N>> {

    private final ArithmeticRing<N> ring;

    public ArithmeticMultiModule(ArithmeticRing<N> ring, int dimension) {
        super(dimension);
        if (dimension != 0 && dimension < 1) {
            throw new IllegalArgumentException("ArithmeticMultiModule must have dimension >= 2 or 0");
        }
        this.ring = ring;
    }

    /**
     * Constructs a free module over the given ring with given <code>dimension</code>.
     *
     * @param dimension the dimension of the free module over the ring; if < 0, assumed to be 0
     */
    public static <T extends ArithmeticNumber<T>> FreeModule<?,ArithmeticElement<T>> make(ArithmeticRing<T> ring, int dimension) {
        if (dimension <= 0) {
            return ring.getNullModule();
        }
        if (dimension == 1) {
            return ring;
        }
        return new ArithmeticMultiModule<>(ring, dimension);
    }

    @Override
    public boolean isVectorSpace() {
        return ring.isField();
    }

    @Override
    public ArithmeticMultiElement<N> getUnitElement(int i) {
        List<ArithmeticElement<N>> list = new ArrayList<>(getDimension());
        assert(i >= 0 && i < getDimension());
        for (int j = 0; j < getDimension(); j++) {
            list.add(ring.getZero());
        }
        list.set(i, ring.getOne());
        return new ArithmeticMultiElement<>(ring, list);
    }

    @Override
    public ArithmeticMultiElement<N> getZero() {
        List<ArithmeticElement<N>> list = new ArrayList<>(getDimension());
        for (int j = 0; j < getDimension(); j++) {
            list.add(ring.getZero());
        }
        return new ArithmeticMultiElement<>(ring, list);
    }

    @Override
    public Module<?, ArithmeticElement<N>> getNullModule() {
        return ring.getNullModule();
    }

    @Override
    public boolean isNullModule() {
        return getDimension() == 0;
    }

    @Override
    public Ring<ArithmeticElement<N>> getRing() {
        return ring;
    }

    @Override
    public Module<?, ArithmeticElement<N>> getComponentModule(int i) {
        return ring;
    }

    @Override
    public boolean hasElement(ModuleElement<?, ?> element) {
        if (!(element instanceof ArithmeticMultiElement)) {
            return false;
        }
        return this.equals(element.getModule());
    }

    @Override
    public ArithmeticMultiElement<N> cast(ModuleElement<?, ?> element) {
        if (element.getLength() == getDimension()) {
            if (element instanceof DirectSumElement) {
                return element.cast(this);
            }
            List<ArithmeticElement<N>> elements = new ArrayList<>(getDimension());
            for (int i = 0; i < getDimension(); i++) {
                ArithmeticElement<N> castElement = ring.cast(element.getComponent(i));
                if (castElement == null) {
                    return null;
                }
                elements.add(castElement);
            }
            return new ArithmeticMultiElement<>(ring, elements);
        }
        return null;
    }

    @Override
    public ArithmeticMultiElement<N> createElement(List<ModuleElement<?, ?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }
        List<ArithmeticElement<N>> values = new ArrayList<>(getDimension());
        Iterator<ModuleElement<?, ?>> iter = elements.iterator();
        for (int i = 0; i < getDimension(); i++) {
            ArithmeticElement<N> castElement = iter.next().cast(ring);
            if (castElement == null) {
                return null;
            }
            values.add(castElement);
        }

        return new ArithmeticMultiElement<>(ring, values);
    }

    @Override
    public ArithmeticMultiElement<N> parseString(String string) {
        string = TextUtils.unparenthesize(string);
        String[] components = string.split(",");
        if (components.length != getDimension()) {
            return null;
        }
        List<ArithmeticElement<N>> values = new ArrayList<>(components.length);
        for (int i = 0; i < components.length; i++) {
            try {
                values.add(new ArithmeticElement<>(ArithmeticParsingUtils.parse(ring, components[i])));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return new ArithmeticMultiElement<>(ring, values);
    }

    @Override
    public String toVisualString() {
        return String.format("%s^%d", ring.toVisualString(), getDimension());
    }

    @Override
    protected ModuleMorphism _getProjection(int index) {
        return null; //TODO
    }

    @Override
    protected ModuleMorphism _getInjection(int index) {
        return null; //TODO
    }
}
