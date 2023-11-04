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

package org.vetronauta.latrunculus.core.math.element.generic;

import org.apache.commons.lang3.StringUtils;
import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.util.EntryList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import static java.lang.Math.min;

/**
 * @author vetronauta
 */
public class StringMap<R extends RingElement<R>> extends RingElement<StringMap<R>> {

    //TODO null checks

    private Map<String, R> dict; //TODO should be final?
    private final Ring<R> baseRing; //TODO refactor StringElement & StringRing

    public StringMap(Ring<R> ring) {
        this.baseRing = ring;
        this.dict = new HashMap<>();
    }

    public StringMap(Ring<R> ring, String word) {
        this(ring);
        dict.put(word, baseRing.getOne());
    }

    public StringMap(R factor) {
        this(factor.getRing());
        dict.put(StringUtils.EMPTY, factor);
    }

    public StringMap(String word, R factor) {
        this(factor.getRing());
        if (!factor.isZero()) {
            dict.put(word, factor);
        }
    }

    public StringMap(String[] words, R[] factors) {
        this(factors[0].getRing());
        int len = Math.min(factors.length, words.length);
        for (int i = 0; i < len; i++) {
            R factor = factors[i];
            if (!factor.isZero()) {
                performOnMap(words[i], factor, ModuleElement::sum);
            }
        }
    }

    public StringMap(List<String> words, List<R> factors) {
        this(factors.get(0).getRing());
        int len = min(factors.size(), words.size());
        for (int i = 0; i < len; i++) {
            R factor = factors.get(i);
            if (!factor.isZero()) {
                performOnMap(words.get(i), factor, ModuleElement::sum);
            }
        }
    }

    public StringMap(EntryList<String, R> entryList) {
        this(entryList.getKeys(), entryList.getValues());
    }

    @Override
    public boolean isZero() {
        return dict.isEmpty();
    }

    @Override
    public StringMap<R> sum(StringMap<R> element) throws DomainException {
        StringMap<R> res = this.deepCopy();
        res.add(element);
        return res;
    }

    @Override
    public void add(StringMap<R> element) throws DomainException {
        performOnMap(element, ModuleElement::sum);
    }

    @Override
    public StringMap<R> difference(StringMap<R> element) throws DomainException {
        StringMap<R> res = this.deepCopy();
        res.subtract(element);
        return res;
    }

    @Override
    public void subtract(StringMap<R> element) throws DomainException {
        performOnMap(element, ModuleElement::difference);
    }

    @Override
    public StringMap<R> negated() {
        StringMap<R> res = this.deepCopy();
        res.negate();
        return res;
    }

    @Override
    public void negate() {
        performOnMap(ModuleElement::negated);
    }

    @Override
    public boolean isOne() {
        return dict.size() == 1 && Optional.ofNullable(dict.get(StringUtils.EMPTY)).map(RingElement::isOne).orElse(false);
    }

    @Override
    public StringMap<R> product(StringMap<R> element) throws DomainException {
        StringMap<R> res = this.deepCopy();
        res.multiply(element);
        return res;
    }

    @Override
    public void multiply(StringMap<R> element) throws DomainException {
        performOnMap(element, RingElement::product);
    }

    @Override
    public boolean isInvertible() {
        return isOne();
    }

    @Override
    public StringMap<R> inverse() {
        if (isInvertible()) {
            return this;
        }
        throw new InverseException("Inverse of "+this+" does not exist.");
    }

    @Override
    public void invert() {
        if (!isInvertible()) {
            throw new InverseException("Inverse of " + this + " does not exist.");
        }
    }

    @Override
    public StringMap<R> quotient(StringMap<R> element) throws DomainException, DivisionException {
        // TODO: implement division where possible
        throw new DivisionException(this, element);
    }

    @Override
    public void divide(StringMap<R> element) throws DomainException, DivisionException {
        // TODO: implement division where possible
        throw new DivisionException(this, element);
    }

    @Override
    public boolean divides(RingElement<?> element) {
        // TODO: implement division where possible
        return false;
    }

    @Override
    public Ring<StringMap<R>> getRing() {
        return null; //TODO after StringRing refactoring
    }

    @Override
    public StringMap<R> deepCopy() {
        StringMap<R> copy = new StringMap<>(baseRing);
        copy.add(this);
        return copy;
    }

    @Override
    public String toString() {
        return String.format("StringMap<%s>[%s]", getRing().toVisualString(), dict.toString());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof StringMap) {
            if (((StringMap<?>) object).dict.size() != dict.size()) {
                return false;
            }
            for (String key : dict.keySet()) {
                if (!dict.get(key).equals(((StringMap<?>) object).dict.get(key))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return dict.hashCode();
    }

    public Map<String, R> getTerms() {
        return dict; //TODO deepcopy
    }

    //TODO as R is mutable, it might be more efficient to use the mutable operation

    private void performOnMap(StringMap<R> element, BinaryOperator<R> operator) {
        for (Map.Entry<String,R> entry : element.dict.entrySet()) {
            performOnMap(entry.getKey(), entry.getValue(), operator);
        }
    }

    private void performOnMap(String word, R factor, BinaryOperator<R> operator) {
        if (dict.containsKey(word)) {
            R newFactor = operator.apply(dict.get(word), factor);
            if (!newFactor.isZero()) {
                dict.put(word, newFactor);
            } else {
                dict.remove(word);
            }
        } else if (!factor.isZero()) {
            dict.put(word, factor);
        }
    }

    private void performOnMap(UnaryOperator<R> operator) {
        for (Map.Entry<String, R> entry : dict.entrySet()) {
            R newFactor = operator.apply(entry.getValue());
            dict.put(entry.getKey(), newFactor);
        }
    }

}
