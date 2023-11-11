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

package org.vetronauta.latrunculus.core.math.morphism.affine;

import lombok.Getter;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.endo.RingEndomorphism;
import org.vetronauta.latrunculus.core.math.morphism.endo.RingEndomorphismWrapper;

/**
 * @author vetronauta
 */
@Getter
public class AffineRingMorphism<R extends RingElement<R>> extends RingEndomorphism<R> {

    private final R a;
    private final R b;

    public AffineRingMorphism(Ring<R> domain, R a, R b) {
        super(domain);
        this.a = a;
        this.b = b;
    }

    @Override
    public R map(R x) {
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

    public AffineRingMorphism<R> compose(AffineRingMorphism<R> morphism) {
        return new AffineRingMorphism<>(getDomain(), a.product(morphism.a), a.product(morphism.b).sum(b));
    }

    public AffineRingMorphism<R> sum(AffineRingMorphism<R> morphism) {
        return new AffineRingMorphism<>(getDomain(), a.sum(morphism.a), b.sum(morphism.b));
    }

    public AffineRingMorphism<R> difference(AffineRingMorphism<R> morphism) {
        return new AffineRingMorphism<>(getDomain(), a.difference(morphism.a),b.difference(morphism.b));
    }

    @Override
    public RingEndomorphism<R> scaled(R element) {
        if (element.isZero()) {
            return new RingEndomorphismWrapper<>(ModuleMorphism.getConstantMorphism(element));
        }
        return new AffineRingMorphism<>(getDomain(), getA().product(element), getB().product(element));
    }

    @Override
    public R atZero() {
        return getB().deepCopy();
    }

    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof AffineRingMorphism && getDomain().equals(object.getDomain())) {
            AffineRingMorphism<R> morphism = (AffineRingMorphism<R>) object;
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
        if (!(object instanceof AffineRingMorphism)) {
            return false;
        }
        AffineRingMorphism<?> other = (AffineRingMorphism<?>) object;
        return getDomain().equals(other.getDomain()) && a.equals(other.a) && b.equals(other.b);
    }

    @Override
    public String toString() {
        return String.format("AffineRingMorphism<%s>[%s,%s]", getDomain().getRing().toVisualString(), a, b);
    }

    public String getElementTypeName() {
        return String.format("AffineRingMorphism<%s>", getDomain().getRing().toVisualString());
    }
    
}
