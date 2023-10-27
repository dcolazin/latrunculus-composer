/*
 * Copyright (C) 2007 Gérard Milmeister
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.vetronauta.latrunculus.core.math.module.polynomial;

import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

import java.util.List;

/**
 * Elements in a ring of polynomials.
 * @see PolynomialRing
 * 
 * @author Gérard Milmeister
 */
public final class ModularPolynomialElement<B extends RingElement<B>> extends RingElement<ModularPolynomialElement<B>> {

    private PolynomialElement<B> polynomial;
    private final ModularPolynomialRing<B> ring;

    /**
     * Constructs a modular polynomial in a specified ring from he given polynomial.
     */
    public ModularPolynomialElement(ModularPolynomialRing<B> ring, PolynomialElement<B> polynomial) {
        this.ring = ring;
        this.polynomial = polynomial;
        normalize();
    }

    /**
     * Constructs a modular polynomial in a specified ring with the given coefficents.
     * The array of <code>coefficients</code> contains the coefficient of power <code>n</code>
     * at index <code>n</code>.
     * @param ring the ring of modular polynomials
     * @param coefficients elements of the coefficient ring
     */
    public ModularPolynomialElement(ModularPolynomialRing<B> ring, B coefficients) {
        this(ring, new PolynomialElement<>(ring.getModulusRing(), coefficients));
    }

    public ModularPolynomialElement(ModularPolynomialRing<B> ring, List<B> coefficients) {
        this(ring, new PolynomialElement<>(ring.getModulusRing(), coefficients));
    }

    @Override
    public boolean isOne() {
        return polynomial.isOne();
    }  

    @Override
    public boolean isZero() {
        return polynomial.isZero();
    }

    @Override
    public ModularPolynomialElement<B> sum(ModularPolynomialElement<B> element) {
        return new ModularPolynomialElement<>(ring, polynomial.sum(element.polynomial));
    }

    @Override
    public void add(ModularPolynomialElement<B> element) {
        polynomial = this.sum(element).polynomial;
    }

    @Override
    public ModularPolynomialElement<B> difference(ModularPolynomialElement<B> element) {
        return new ModularPolynomialElement<>(ring, polynomial.difference(element.polynomial));
    }

    @Override
    public void subtract(ModularPolynomialElement<B> element) {
        polynomial = this.difference(element).polynomial;
    }

    @Override
    public ModularPolynomialElement<B> negated() {
        return new ModularPolynomialElement<>(ring, polynomial.negated());
    }

    @Override
    public void negate() {
        polynomial = this.negated().polynomial;
    }
    
    //TODO this should have parameter B; yet again the algebra vs module definition
    @Override
    public ModularPolynomialElement<B> scaled(ModularPolynomialElement<B> element) {
        return product(element);
    }

    //TODO this should have parameter B; yet again the algebra vs module definition
    @Override
    public void scale(ModularPolynomialElement<B> element) {
        multiply(element);
    }

    @Override
    public ModularPolynomialElement<B> product(ModularPolynomialElement<B> element) {
        return new ModularPolynomialElement<>(ring, polynomial.product(element.polynomial));

    }

    @Override
    public void multiply(ModularPolynomialElement<B> element) {
        polynomial = this.product(element).polynomial;
    }

    @Override
    public boolean isInvertible() {        
        try {
            PolynomialElement<B> g = polynomial.gcd(ring.getModulus());
            return g.getDegree() == 0;
        }
        catch (DomainException | DivisionException e) {
            return false;
        }

    }
    
    @Override
    public ModularPolynomialElement<B> inverse() {
        try {
            PolynomialElement res[] = new PolynomialElement[2];
            PolynomialElement<B> g = polynomial.extendedGcd(ring.getModulus(), res);
            if (g.getDegree() == 0) {
                return new ModularPolynomialElement<>(ring,  res[0]);
            }
        } catch (Exception ignored) {}
        throw new InverseException("Inverse of "+this+" does not exist");
    }

    @Override
    public void invert() {
        try {
            PolynomialElement res[] = new PolynomialElement[2];
            PolynomialElement g = polynomial.extendedGcd(ring.getModulus(), res);
            if (g.getDegree() == 0) {
                polynomial = res[0];
                return;
            }
        }
        catch (Exception e) {}
        throw new InverseException("Inverse of "+this+" does not exist");
    }

    @Override
    public ModularPolynomialElement<B> quotient(ModularPolynomialElement<B> element) throws DivisionException {
        try {
            ModularPolynomialElement<B> p = element.inverse();
            return product(p);
        }
        catch (InverseException ignored) {}
        throw new DivisionException(this, element);
    }

    @Override
    public void divide(ModularPolynomialElement<B> element) throws DivisionException {
        try {
            PolynomialElement res[] = new PolynomialElement[2];
            PolynomialElement<B> g = element.polynomial.extendedGcd(ring.getModulus(), res);
            if (g.getDegree() == 0) {
                polynomial = polynomial.product(res[0]);
                normalize();
                return;
            }
        }
        catch (Exception ignored) {}
        throw new DivisionException(this, element);
    }

    @Override
    public boolean divides(RingElement element) {
        if (element instanceof ModularPolynomialElement) {
            return isInvertible();
        }
        else {
            return false;
        }
    }

    public B evaluate(B element) {
        return polynomial.evaluate(element);
    }
    
    @Override
    public ModularPolynomialRing<B> getModule() {
        return ring;
    }

    @Override
    public ModularPolynomialRing<B> getRing() {
        return ring;
    }

    
    public List<B> getCoefficients() {
        return polynomial.getCoefficients();
    }
    
    
    public B getCoefficient(int power) {
        return polynomial.getCoefficient(power);
    }
      

    public B getLeadingCoefficient() {
        return polynomial.getLeadingCoefficient();
    }
    

    public int getDegree() {
        return polynomial.getDegree();
    }

    
    public Ring<B> getCoefficientRing() {
        return getRing().getCoefficientRing();
    }
    
    
    public String getIndeterminate() {
        return getRing().getIndeterminate();
    }
    
    
    public PolynomialElement<B> getModulus() {
        return ring.getModulus();
    }
    
    
    public FreeElement<?,ModularPolynomialElement<B>> resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return ModularPolynomialProperFreeElement.make(getRing(), new ModularPolynomialElement[0]);
        }
        else {
            ModularPolynomialElement[] values = new ModularPolynomialElement[n];
            values[0] = this;
            for (int i = 1; i < n; i++) {
                values[i] = getRing().getZero();
            }
            return ModularPolynomialProperFreeElement.make(getRing(), values);
        }
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ModularPolynomialElement) {
            ModularPolynomialElement<?> p = (ModularPolynomialElement<?>) object;
            return ring.equals(p.ring) && polynomial.equals(p.polynomial); 
        }
        else {
            return false;
        }
    }

    @Override
    public int compareTo(ModuleElement object) {
        if (object instanceof ModularPolynomialElement) {
            ModularPolynomialElement<?> p = (ModularPolynomialElement<?>) object;
            int c = ring.compareTo(p.ring);
            if (c == 0) {
                c = polynomial.compareTo(p.polynomial);
            }
            return c;
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public ModularPolynomialElement<B> deepCopy() {
        return new ModularPolynomialElement<>(ring, polynomial.deepCopy());
    }

    public PolynomialElement<B> getPolynomial() {
        return polynomial;
    }

    
    public String toString() {
        return "ModularPolynomial["+polynomial+","+ring.getModulus()+"]";
    }
    
    
    public double[] fold(ModuleElement[] elements) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getElementTypeName() {
        return "ModularPolynomialElement";
    }

    
    public int hashCode() {
        int hashCode = basicHash;
        hashCode ^= polynomial.hashCode();
        hashCode ^= ring.getModulus().hashCode();
        return hashCode;
    }
    
    
    private void normalize() {
        try {
            polynomial = polynomial.remainder(ring.getModulus());
        }
        catch (Exception ignored) {}
    }


    private static final int basicHash = "ModularPolynomialElement".hashCode();

}
