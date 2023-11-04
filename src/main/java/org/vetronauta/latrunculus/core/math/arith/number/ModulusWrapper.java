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

package org.vetronauta.latrunculus.core.math.arith.number;

import lombok.Getter;
import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.exception.InverseException;
import org.vetronauta.latrunculus.core.exception.ModulusException;
import org.vetronauta.latrunculus.core.exception.ZeroDivisorException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper modulus class to be used in a <code>ZnString</code>.
 * @author vetronauta
 */
@Getter
public final class ModulusWrapper implements ArithmeticNumber<ModulusWrapper> {

    private final int value;
    private final int modulus;

    public ModulusWrapper(int value, int modulus) {
        this.value = NumberTheory.mod(value, modulus);
        this.modulus = modulus;
    }

    @Override
    public int compareTo(ModulusWrapper arithmeticModulusWrapper) {
        return this.intValue() - arithmeticModulusWrapper.intValue();
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return intValue();
    }

    @Override
    public float floatValue() {
        return intValue();
    }

    @Override
    public double doubleValue() {
        return intValue();
    }

    @Override
    public ModulusWrapper deepCopy() {
        return this;
    }

    @Override
    public boolean isZero() {
        return intValue() == 0;
    }

    @Override
    public boolean isOne() {
        return intValue() == 1;
    }

    @Override
    public boolean isInvertible() {
        try {
            NumberTheory.inverseMod(value, modulus); //TODO is there a faster way?
            return true;
        } catch (ZeroDivisorException e) {
            return false;
        }
    }

    @Override
    public boolean isFieldElement() {
        return NumberTheory.isPrime(modulus);
    }

    @Override
    public boolean divides(ArithmeticNumber<?> y) {
        return (y instanceof ModulusWrapper) && NumberTheory.gcd(value, modulus) == 1;
    }

    @Override
    public ModulusWrapper sum(ModulusWrapper other) {
        assertModulusIsSame(other);
        return new ModulusWrapper(value + other.value, modulus);
    }

    @Override
    public ModulusWrapper difference(ModulusWrapper other) {
        assertModulusIsSame(other);
        return new ModulusWrapper(value - other.value, modulus);
    }

    @Override
    public ModulusWrapper product(ModulusWrapper other) {
        assertModulusIsSame(other);
        return new ModulusWrapper(value * other.value, modulus);
    }

    @Override
    public ModulusWrapper neg() {
        return new ModulusWrapper(-value, modulus);
    }

    @Override
    public ModulusWrapper inverse() {
        try {
            return new ModulusWrapper(NumberTheory.inverseMod(value, modulus), modulus);
        } catch (ZeroDivisorException e) {
            throw new InverseException(this, e);
        }
    }

    @Override
    public String toString() {
        return String.format("%d%%%d", value, modulus);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ModulusWrapper)) {
            return false;
        }
        ModulusWrapper otherModulusWrapper = (ModulusWrapper) other;
        return otherModulusWrapper.value == value && otherModulusWrapper.modulus == modulus;
    }

    @Override
    public int hashCode() {
        return value;
    }

    public static ModulusWrapper[] toArray(int[] array, int m) {
        return Arrays.stream(array)
                .mapToObj(i -> new ModulusWrapper(i, m))
                .toArray(ModulusWrapper[]::new);
    }

    public static List<ModulusWrapper> toList(List<Integer> list, int m) {
        return list.stream().map(i -> new ModulusWrapper(i, m)).collect(Collectors.toList());
    }

    private void assertModulusIsSame(ModulusWrapper other) {
        if (modulus != other.getModulus()) {
            throw new ModulusException(this, other);
        }
    }

}