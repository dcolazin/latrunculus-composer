package org.vetronauta.latrunculus.core.math.element.generic;

import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Vector<R extends RingElement<R>> extends ProperFreeElement<Vector<R>, R> {

    //TODO equals considering value.size() == 1

    private final List<R> value;
    private final Ring<R> ring;
    private FreeModule<?, R> module;

    public Vector(Ring<R> ring, List<R> value) {
        this.value = value != null ? value : new ArrayList<>();
        this.ring = ring;
    }

    public static <R extends RingElement<R>> Vector<R> zero(Ring<R> ring, int dimension) {
        if (dimension < 1) {
            return new Vector<>(ring, null);
        }
        List<R> list = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            list.add(ring.getZero());
        }
        return new Vector<>(ring, list);
    }

    @Override
    public R getRingElement(int i) {
        if (i >= value.size()) {
            throw new IndexOutOfBoundsException(String.format("Cannot access index %d for Vector of length %d", i, value.size()));
        }
        return value.get(i).deepCopy();
    }

    @Override
    public Vector<R> productCW(Vector<R> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        List<R> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).product(element.value.get(i)));
        }
        return new Vector<>(ring, res);
    }

    @Override
    public void multiplyCW(Vector<R> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getLength(); i++) {
            value.get(i).product(element.value.get(i));
        }
    }

    @Override
    public boolean isZero() {
        return value.stream().allMatch(RingElement::isZero);
    }

    @Override
    public Vector<R> scaled(R element) throws DomainException {
        List<R> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).product(element));
        }
        return new Vector<>(ring, res);
    }

    @Override
    public void scale(R element) throws DomainException {
        for (R r : value) {
            r.multiply(element);
        }
    }

    @Override
    public int getLength() {
        return value.size();
    }

    @Override
    public R getComponent(int i) {
        return getRingElement(i);
    }

    @Override
    public Vector<R> sum(Vector<R> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        List<R> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).sum(element.value.get(i)));
        }
        return new Vector<>(ring, res);
    }

    @Override
    public void add(Vector<R> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getLength(); i++) {
            value.get(i).add(element.value.get(i));
        }
    }

    @Override
    public Vector<R> difference(Vector<R> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        List<R> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).difference(element.value.get(i)));
        }
        return new Vector<>(ring, res);
    }

    @Override
    public void subtract(Vector<R> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getLength(); i++) {
            value.get(i).subtract(element.value.get(i));
        }
    }

    @Override
    public Vector<R> negated() {
        List<R> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).negated());
        }
        return new Vector<>(ring, res);
    }

    @Override
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value.get(i).negate();
        }
    }

    @Override
    public Module<Vector<R>, R> getModule() {
        return null; //TODO after the ArithmeticMultiModule refactoring
    }

    @Override
    public Vector<R> resize(int n) {
        if (n == value.size()) {
            return this;
        }
        if (n <= 0) {
            return new Vector<>(ring, null);
        }
        int minLength = Math.min(n, getLength());
        List<R> res = new ArrayList<>(n);
        for (int i = 0; i < minLength; i++) {
            res.add(value.get(i));
        }
        for (int i = value.size(); i < n; i++) {
            res.add(ring.getZero());
        }
        return new Vector<>(ring, res);
    }

    @Override
    public Vector<R> deepCopy() {
        return new Vector<>(ring, value.stream().map(RingElement::deepCopy).collect(Collectors.toList()));
    }

}
