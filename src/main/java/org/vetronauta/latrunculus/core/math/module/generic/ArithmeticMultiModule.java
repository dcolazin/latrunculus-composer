package org.vetronauta.latrunculus.core.math.module.generic;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineInjection;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineProjection;

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
    public ArithmeticRing<N> getRing() {
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
                return this.createElement(element.flatComponentList());
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
    public ArithmeticMultiElement<N> createElement(List<? extends ModuleElement<?, ?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }
        List<ArithmeticElement<N>> values = new ArrayList<>(getDimension());
        Iterator<? extends ModuleElement<?, ?>> iter = elements.iterator();
        for (int i = 0; i < getDimension(); i++) {
            ArithmeticElement<N> castElement = ring.cast(iter.next());
            if (castElement == null) {
                return null;
            }
            values.add(castElement);
        }

        return new ArithmeticMultiElement<>(ring, values);
    }

    @Override
    public String toVisualString() {
        return String.format("%s^%d", ring.toVisualString(), getDimension());
    }

    @Override
    protected ArithmeticAffineProjection<N> _getProjection(int index) {
        return new ArithmeticAffineProjection<>(getRing(), getUnitElement(index), ring.getZero());
    }

    @Override
    protected ArithmeticAffineInjection<N> _getInjection(int index) {
        return new ArithmeticAffineInjection<>(getRing(), getUnitElement(index), getZero());
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArithmeticMultiModule)) {
            return false;
        }
        return getDimension() == ((ArithmeticMultiModule<?>) object).getDimension()
            && ring.equals(((ArithmeticMultiModule<?>) object).getRing());
    }

    @Override
    public int hashCode() {
        return ring.hashCode() + getDimension();
    }

    @Override
    public int compareTo(Module object) {
        if (object instanceof ArithmeticMultiModule) {
            ArithmeticMultiModule<?> module = (ArithmeticMultiModule<?>) object;
            if (ring.equals(module.getRing())) {
                return getDimension() - module.getDimension();
            }
        }
        return super.compareTo(object);
    }

    @Override
    public String toString() {
        return String.format("ArithmeticMultiModule<%s>[%s]", ring.toVisualString(), getDimension());
    }

    public String getElementTypeName() {
        return String.format("ArithmeticMultiModule<%s>", ring.toVisualString());
    }

}
