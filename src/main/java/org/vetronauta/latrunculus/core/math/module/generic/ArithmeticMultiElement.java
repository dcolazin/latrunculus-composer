/*
 * latrunculus-composer
 * Copyright (C) 2023 vetronauta
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.vetronauta.latrunculus.core.math.module.generic;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author vetronauta
 */
@Getter
public class ArithmeticMultiElement<N extends ArithmeticNumber<N>>
        extends ProperFreeElement<ArithmeticMultiElement<N>, ArithmeticElement<N>> {

    //TODO make it abstract and have a N list instead of a ArithmeticElement one

    private final List<ArithmeticElement<N>> value;
    private final ArithmeticRing<N> ring;
    private ArithmeticMultiModule<N> module;

    public ArithmeticMultiElement(ArithmeticRing<N> ring, List<ArithmeticElement<N>> value) {
        if (value == null || value.size() == 1) {
            throw new IllegalArgumentException("MultiElement must have length > 1");
        }
        this.value = value;
        this.ring = ring;
    }

    public static <N extends ArithmeticNumber<N>> FreeElement<?, ArithmeticElement<N>> make(ArithmeticRing<N> ring, N[] elements) {
        return make(ring, Arrays.stream(elements).collect(Collectors.toList()));
    }

    public static <N extends ArithmeticNumber<N>> FreeElement<?, ArithmeticElement<N>> make(ArithmeticRing<N> ring, List<N> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return new ArithmeticMultiElement<>(ring, new ArrayList<>());
        }
        if (elements.size() == 1) {
            return new ArithmeticElement<>(elements.get(0));
        }
        return new ArithmeticMultiElement<>(ring, elements.stream().map(ArithmeticElement::new).collect(Collectors.toList()));
    }

    @Override
    public ArithmeticMultiElement<N> deepCopy() {
        List<ArithmeticElement<N>> v = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            v.add(value.get(i).deepCopy());
        }
        return new ArithmeticMultiElement<>(ring, v);
    }

    @Override
    public ArithmeticElement<N> getRingElement(int i) {
        if (i >= value.size()) {
            throw new IndexOutOfBoundsException(String.format("Cannot access index %d for MultiElement of length %d", i, value.size()));
        }
        return value.get(i).deepCopy();
    }

    @Override
    public ArithmeticMultiElement<N> productCW(ArithmeticMultiElement<N> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        List<ArithmeticElement<N>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).product(element.value.get(i)));
        }
        return new ArithmeticMultiElement<>(ring, res);
    }

    @Override
    public void multiplyCW(ArithmeticMultiElement<N> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getLength(); i++) {
            value.get(i).product(element.value.get(i));
        }
    }

    @Override
    public FreeElement<?, ArithmeticElement<N>> resize(int n) {
        if (n == value.size()) {
            return this;
        }
        if (n == 1) {
            return value.get(0);
        }
        int minLength = Math.min(n, getLength());
        List<ArithmeticElement<N>> res = new ArrayList<>(n);
        for (int i = 0; i < minLength; i++) {
            res.add(value.get(i));
        }
        ArithmeticElement<N> zero = ring.getZero();
        for (int i = value.size(); i < n; i++) {
            res.add(zero);
        }
        return new ArithmeticMultiElement<>(ring, res);
    }

    @Override
    public boolean isZero() {
        return value.stream().allMatch(ArithmeticElement::isZero);
    }

    @Override
    public ArithmeticMultiElement<N> scaled(ArithmeticElement<N> element) throws DomainException {
        List<ArithmeticElement<N>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).product(element));
        }
        return new ArithmeticMultiElement<>(ring, res);
    }

    @Override
    public void scale(ArithmeticElement<N> element) throws DomainException {
        for (int i = 0; i < value.size(); i++) {
            value.get(i).multiply(element);
        }
    }

    @Override
    public int getLength() {
        return value.size();
    }

    @Override
    public ModuleElement<?, ArithmeticElement<N>> getComponent(int i) {
        return getRingElement(i);
    }

    @Override
    public ArithmeticMultiElement<N> sum(ArithmeticMultiElement<N> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        List<ArithmeticElement<N>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).sum(element.value.get(i)));
        }
        return new ArithmeticMultiElement<>(ring, res);
    }

    @Override
    public void add(ArithmeticMultiElement<N> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getLength(); i++) {
            value.get(i).add(element.value.get(i));
        }
    }

    @Override
    public ArithmeticMultiElement<N> difference(ArithmeticMultiElement<N> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        List<ArithmeticElement<N>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).difference(element.value.get(i)));
        }
        return new ArithmeticMultiElement<>(ring, res);
    }

    @Override
    public void subtract(ArithmeticMultiElement<N> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getLength(); i++) {
            value.get(i).subtract(element.value.get(i));
        }
    }

    @Override
    public ArithmeticMultiElement<N> negated() {
        List<ArithmeticElement<N>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).negated());
        }
        return new ArithmeticMultiElement<>(ring, res);
    }

    @Override
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value.get(i).negate();
        }
    }

    @Override
    public double[] fold(ModuleElement<?, ?>[] elements) {
        return new double[0]; //TODO
    }

    @Override
    public ArithmeticMultiModule<N> getModule() {
        if (module == null) {
            module = new ArithmeticMultiModule<>(ring, getLength());
        }
        return module;
    }

    @Override
    public String stringRep(boolean... parens) {
        return null; //TODO
    }

    @Override
    public int hashCode() {
        int val = 11;
        for (int i = 0; i < getLength(); i++) {
            val = value.get(i).hashCode()*17+val;
        }
        return val;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArithmeticMultiElement)) {
            return false;
        }
        ArithmeticMultiElement<?> otherElement = (ArithmeticMultiElement<?>) object;
        if (getLength() == otherElement.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                if (!Objects.equals(value.get(i), otherElement.value.get(i))) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

}
