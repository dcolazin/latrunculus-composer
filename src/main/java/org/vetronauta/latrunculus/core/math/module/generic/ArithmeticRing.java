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

import lombok.RequiredArgsConstructor;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.List;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class ArithmeticRing<T extends ArithmeticElement<T,?>> extends Ring<T> {

    private final ArithmeticElement<T,?> zero;
    private final ArithmeticElement<T,?> one;

    //TODO proper consturctor

    @Override
    public boolean isVectorSpace() {
        return one.isFieldElement();
    }

    @Override
    public T getZero() {
        return zero.deepCopy();
    }

    @Override
    public final ModuleMorphism getIdentityMorphism() {
        return ModuleMorphism.getIdentityMorphism(this);
    }

    @Override
    public Module<?, T> getNullModule() {
        return null; //TODO
    }

    @Override
    public boolean hasElement(ModuleElement<?, ?> element) {
        return false; //TODO
    }

    @Override
    public T cast(ModuleElement<?, ?> element) {
        return null; //TODO
    }

    @Override
    public T createElement(List<ModuleElement<?, ?>> elements) {
        return null; //TODO
    }

    @Override
    public String toVisualString() {
        return null; //TODO
    }

    @Override
    public T getOne() {
        return one.deepCopy();
    }

    @Override
    public boolean isField() {
        return one.isFieldElement();
    }

    @Override
    public FreeModule<?, T> getFreeModule(int dimension) {
        return null; //TODO
    }

    @Override
    public T parseString(String s) {
        return null; //TODO
    }
}
