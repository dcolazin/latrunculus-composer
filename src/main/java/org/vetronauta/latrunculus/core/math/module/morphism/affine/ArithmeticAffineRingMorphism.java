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

package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import lombok.Getter;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.generic.ArithmeticRingEndomorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.endo.RingEndomorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.endo.RingEndomorphismWrapper;

/**
 * Affine morphism in a ring.
 * The morphism <code>h</code> is such that <i>h(x) = a*x+b</i>
 * where <code>a</code> and <code>b</code> are arithmetic numbers.
 *
 * @author vetronauta
 */
@Getter
public class ArithmeticAffineRingMorphism<N extends ArithmeticNumber<N>> extends ArithmeticRingEndomorphism<N> {

    private final ArithmeticElement<N> a;
    private final ArithmeticElement<N> b;

    public ArithmeticAffineRingMorphism(Ring<ArithmeticElement<N>> domain, ArithmeticElement<N> a, ArithmeticElement<N> b) {
        super(domain);
        this.a = a;
        this.b = b;
    }

    @Override
    public ArithmeticElement<N> map(ArithmeticElement<N> x) {
        return a.product(x).sum(b);
    }

    @Override
    public boolean isModuleHomomorphism() {
        return true;
    }

    @Override
    public boolean isRingHomomorphism() {
        return b.isZero() && a.isOne();
    }

    @Override
    public boolean isLinear() {
        return b.isZero();
    }

    @Override
    public boolean isIdentity() {
        return a.isOne() && b.isZero();
    }

    @Override
    public boolean isConstant() {
        return a.isZero();
    }

    public ArithmeticAffineRingMorphism<N> compose(ArithmeticAffineRingMorphism<N> morphism) {
        return new ArithmeticAffineRingMorphism<>(getDomain(), a.product(morphism.a), a.product(morphism.b).sum(b));
    }

    public ArithmeticAffineRingMorphism<N> sum(ArithmeticAffineRingMorphism<N> morphism) {
        return new ArithmeticAffineRingMorphism<>(getDomain(), a.sum(morphism.a), b.sum(morphism.b));
    }

    public ArithmeticAffineRingMorphism<N> difference(ArithmeticAffineRingMorphism<N> morphism) {
        return new ArithmeticAffineRingMorphism<>(getDomain(), a.difference(morphism.a),b.difference(morphism.b));
    }

    @Override
    public RingEndomorphism<ArithmeticElement<N>> scaled(ArithmeticElement<N> element) {
        if (element.isZero()) {
            return new RingEndomorphismWrapper<>(getConstantMorphism(element));
        }
        return new ArithmeticAffineRingMorphism<>(getDomain(), getA().product(element), getB().product(element));
    }

    @Override
    public ArithmeticElement<N> atZero() {
        return getB().deepCopy();
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ArithmeticAffineRingMorphism && getDomain().equals(object.getDomain())) {
            ArithmeticAffineRingMorphism<N> morphism = (ArithmeticAffineRingMorphism<N>) object;
            int comp = a.compareTo(morphism.a);
            if (comp == 0) {
                return b.compareTo(morphism.b);
            } else {
                return comp;
            }
        }
        return super.compareTo(object);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArithmeticAffineRingMorphism)) {
            return false;
        }
        ArithmeticAffineRingMorphism<?> other = (ArithmeticAffineRingMorphism<?>) object;
        return getDomain().equals(other.getDomain()) && a.equals(other.a) && b.equals(other.b);
    }

    @Override
    public String toString() {
        return String.format("ArithmeticAffineMorphism<%s>[%s,%s]", getDomain().getRing().toVisualString(), a, b);
    }

    public String getElementTypeName() {
        return String.format("ArithmeticAffineMorphism<%s>", getDomain().getRing().toVisualString());
    }
}
