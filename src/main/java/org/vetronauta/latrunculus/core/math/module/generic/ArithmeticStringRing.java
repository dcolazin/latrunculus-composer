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

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;

/**
 * @author vetronauta
 */
public class ArithmeticStringRing<N extends ArithmeticNumber<N>> extends StringRing<ArithmeticStringElement<N>> {

    private final ArithmeticRing<N> factorRing;

    public ArithmeticStringRing(ArithmeticRing<N> factorRing) {
        this.factorRing = factorRing;
    }

    @Override
    public ArithmeticStringElement<N> getZero() {
        return new ArithmeticStringElement<N>(RingString.getZero());
    }

    @Override
    public Module<?, ArithmeticStringElement<N>> getNullModule() {
        return ArithmeticStringMultiModule.make(this, 0);
    }

    @Override
    public ArithmeticStringElement<N> getOne() {
        return new ArithmeticStringElement<N>(RingString.getOne());
    }

    @Override
    public boolean hasElement(ModuleElement element) {
        if (!(element instanceof ArithmeticStringElement)) {
            return false;
        }
        ArithmeticNumber number = ((ArithmeticStringElement)element).getValue().getObjectOne();
        return (factorRing.hasElement(new ArithmeticElement(number)));
    }

    @Override
    public boolean isField() {
        return false;
    }

    @Override
    public FreeModule<?, ArithmeticStringElement<N>> getFreeModule(int dimension) {
        return ArithmeticStringMultiModule.make(this, dimension);
    }

    @Override
    public ArithmeticStringElement<N> parseString(String s) {
        try {
            return new ArithmeticStringElement<>(ArithmeticParsingUtils.parseString(this, TextUtils.unparenthesize(s)));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isVectorSpace() {
        return false;
    }

    @Override
    public ArithmeticRing<N> getFactorRing() {
        return factorRing;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ArithmeticStringRing && factorRing.equals(((ArithmeticStringRing<?>) obj).factorRing);
    }

    @Override
    public int hashCode() {
        return factorRing.hashCode();
    }

    @Override
    public ArithmeticStringElement<N> cast(ModuleElement element) {
        if (element instanceof StringElement) {
            RingString rs = ((StringElement)element).getRingString();
            return new ArithmeticStringElement<N>(new RingString<>(rs));
        }
        ArithmeticElement<N> e = factorRing.cast(element);
        return e != null ? new ArithmeticStringElement<N>(new RingString<>(e.getValue())) : null;
    }

    @Override
    public String toString() {
        return String.format("ArithmeticStringRing<%s>", factorRing);
    }

    public String getElementTypeName() {
        return this.toString();
    }

    @Override
    public String toVisualString() {
        return String.format("%s-String", factorRing.toVisualString());
    }

}
