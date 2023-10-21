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

package org.vetronauta.latrunculus.core.math.module.morphism.generic;

import lombok.Getter;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

/**
 * Affine morphism in a ring.
 * The morphism <code>h</code> is such that <i>h(x) = a*x+b</i>
 * where <code>a</code> and <code>b</code> are arithmetic numbers.
 *
 * @author vetronauta
 */
@Getter
public class ArithmeticAffineMorphism<N extends ArithmeticNumber<N>> extends ArithmeticRingEndomorphism<N> {

    private final N a;
    private final N b;

    public ArithmeticAffineMorphism(Ring<ArithmeticElement<N>> domain, N a, N b) {
        super(domain);
        this.a = a;
        this.b = b;
    }

    @Override
    public N mapValue(N x) {
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

    public ArithmeticAffineMorphism<N> compose(ArithmeticAffineMorphism<N> morphism) {
        return new ArithmeticAffineMorphism<>(getDomain(), a.product(morphism.a), a.product(morphism.b).sum(b));
    }

    public ArithmeticAffineMorphism<N> sum(ArithmeticAffineMorphism<N> morphism) {
        return new ArithmeticAffineMorphism<>(getDomain(), a.sum(morphism.a), b.sum(morphism.b));
    }

    public ArithmeticAffineMorphism<N> difference(ArithmeticAffineMorphism<N> morphism) {
        return new ArithmeticAffineMorphism<>(getDomain(), a.difference(morphism.a),b.difference(morphism.b));
    }

    @Override
    public RingEndomorphism<ArithmeticElement<N>> scaled(ArithmeticElement<N> element) {
        if (element.isZero()) {
            return new RingEndomorphismWrapper<>(getConstantMorphism(element));
        }
        return new ArithmeticAffineMorphism<>(getDomain(), getA().product(element.getValue()), getB().product(element.getValue()));
    }

    @Override
    public ArithmeticElement<N> atZero() {
        return new ArithmeticElement<>(getB());
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ArithmeticAffineMorphism && getDomain().equals(object.getDomain())) {
            ArithmeticAffineMorphism<N> morphism = (ArithmeticAffineMorphism<N>) object;
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
        if (!(object instanceof ArithmeticAffineMorphism)) {
            return false;
        }
        ArithmeticAffineMorphism<?> other = (ArithmeticAffineMorphism<?>) object;
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
