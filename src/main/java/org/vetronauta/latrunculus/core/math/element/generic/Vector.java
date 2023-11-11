package org.vetronauta.latrunculus.core.math.element.generic;

import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Vector<R extends RingElement<R>> implements FreeElement<Vector<R>, R> {

    //TODO as much as possible, try to not leak this class around, but just use for matrix use and for elements for size != 1

    private final List<R> value;
    private final Ring<R> ring;
    private VectorModule<R> module;

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

    public List<R> getValue() {
        return value;
    }

    public Ring<R> getRing() {
        return ring;
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
        if (module == null) {
            module = new VectorModule<>(ring, value.size());
        }
        return module;
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

    public R scalarProduct(Vector<R> other) {
        if (getLength() != other.getLength()) {
            throw new DomainException(this.getModule(), other.getModule());
        }
        R sp = ring.getZero();
        for (int i = 0; i < getLength(); i++) {
            sp.add(value.get(i).product(other.value.get(i)));
        }
        return sp;
    }

    public int length() {
        return value.size();
    }

    @Override
    public Vector<R> deepCopy() {
        return new Vector<>(ring, value.stream().map(RingElement::deepCopy).collect(Collectors.toList()));
    }

    public String getElementTypeName() {
        return String.format("Vector<%s>", ring.toVisualString());
    }

    @Override
    public int compareTo(ModuleElement object) {
        if (object instanceof RingElement && length() == 1) {
            return value.get(0).compareTo(object);
        }
        if (!(object instanceof Vector)) {
            return getModule().compareTo(object.getModule());
        }
        int currentCompare = length() - object.getLength();
        if (currentCompare != 0) {
            return currentCompare;
        }
        for (int i = 0; i < getLength(); i++) {
            currentCompare = value.get(i).compareTo(object.getComponent(i));
            if (currentCompare != 0) {
                return currentCompare;
            }
        }
        return 0;
    }

    @Override
    public Iterator<R> iterator() {
        return value.stream().map(RingElement::deepCopy).iterator();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RingElement && length() == 1) {
            return value.get(0).equals(object);
        }
        if (!(object instanceof Vector) || length() != ((Vector<?>) object).getLength()) {
            return false;
        }
        for (int i = 0; i < getLength(); i++) {
            if (!value.get(i).equals(((Vector<?>) object).getComponent(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return value.stream().mapToInt(Objects::hashCode).sum();
    }

}
