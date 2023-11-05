/*
 * Copyright (C) 2001, 2005 Gérard Milmeister
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

import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The ring of polynomials with coefficients in a specified ring
 * modulo another polynomial.
 * @see PolynomialElement
 * 
 * @author Gérard Milmeister
 */
public final class ModularPolynomialRing<B extends RingElement<B>>
        extends Ring<ModularPolynomialElement<B>> {

    //TODO is this correct?
    private final Ring<B> coefficientRing;
    private final Ring<B> baseRing;
    private final PolynomialElement<B> modulus;
    private final PolynomialRing<B> modulusRing;
    private final String indeterminate;

    private ModularPolynomialRing(PolynomialElement<B> modulus) {
        this.modulus = modulus;
        this.indeterminate = modulus.getIndeterminate();
        this.coefficientRing = modulus.getCoefficientRing();
        this.baseRing = coefficientRing;
        this.modulusRing = modulus.getRing();
    }

    public static <X extends RingElement<X>> ModularPolynomialRing<X> make(PolynomialElement<X> modulus) {
        if (modulus.getCoefficientRing().isField()) {
            return new ModularPolynomialRing<>(modulus);
        }
        return null;
    }

    public String getIndeterminate() {
        return indeterminate;
    }

    public boolean hasIndeterminate(String indet) {
        if (getIndeterminate().equals(indet)) {
            return true;
        }
        if (coefficientRing instanceof ModularPolynomialRing) {
            return ((ModularPolynomialRing<?>) coefficientRing).hasIndeterminate(indet);
        }
        if (coefficientRing instanceof PolynomialRing) {
            return ((PolynomialRing<?>) coefficientRing).hasIndeterminate(indet);
        }
        return false;
    }
    
    public List<String> getIndeterminates() {
        if (coefficientRing instanceof ModularPolynomialRing) {
            List<String> indeterminates = ((ModularPolynomialRing<?>)coefficientRing).getIndeterminates();
            indeterminates.add(0, getIndeterminate());
            return indeterminates;
        }
        else if (coefficientRing instanceof PolynomialRing) {
            List<String> indeterminates = ((PolynomialRing<?>)coefficientRing).getIndeterminates();
            indeterminates.add(0, getIndeterminate());
            return indeterminates;
        }
        else {
            List<String> indeterminates = new LinkedList<>();
            indeterminates.add(getIndeterminate());
            return indeterminates;
        }
    }

    public Ring<B> getCoefficientRing() {
        return coefficientRing;
    }
    
    public Ring<B> getBaseRing() {
        return baseRing;
    }

    public PolynomialElement<B> getModulus() {
        return modulus;
    }
    
    public PolynomialRing<B> getModulusRing() {
        return modulusRing;
    }
    
    public ModularPolynomialElement<B> getZero() {
        return new ModularPolynomialElement<>(this, getCoefficientRing().getZero());
    }

    public ModularPolynomialElement<B> getOne() {
        return new ModularPolynomialElement<>(this, getCoefficientRing().getOne());
    }

    public boolean isField() {
        return false;
    }
    
    public boolean isVectorSpace() {
        return false;
    }

    public boolean hasElement(ModuleElement element) {
        if (element instanceof ModularPolynomialElement) {
            return element.getModule().equals(this);
        }
        return false;
    }

    @Override
    public FreeModule<?,ModularPolynomialElement<B>> getFreeModule(int dimension) {
        return PolynomialModuleFactory.makeModular(getModulus(), dimension);
    }

    @Override
    protected boolean nonSingletonEquals(Object object) {
        return object instanceof ModularPolynomialRing && getModulus().equals(((ModularPolynomialRing<?>) object).getModulus());
    }


    public int compareTo(Module object) {
        if (object instanceof ModularPolynomialRing) {
            ModularPolynomialRing p = (ModularPolynomialRing)object;
            int c;
            if ((c = getCoefficientRing().compareTo(p.getCoefficientRing())) != 0) {
                return c;
            }
            else {
                return getIndeterminate().compareTo(p.getIndeterminate());
            }
        }
        else {
            return super.compareTo(object);
        }
    }


    public ModularPolynomialElement<B> createElement(List<? extends ModuleElement<?,?>> elements) {
        if (elements.size() == 1 && (hasElement(elements.get(0)))) {
            return (ModularPolynomialElement<B>) elements.get(0);
        }
        List<B> coeffs = new ArrayList<>(elements.size());
        int i = 0;
        for (ModuleElement<?,?> e : elements) {
            B currentElement = getCoefficientRing().cast(e);
            if (currentElement == null) {
                return null;
            }
            coeffs.add(currentElement);
        }
        return new ModularPolynomialElement<>(this, coeffs);
    }

    
    public ModularPolynomialElement<B> cast(ModuleElement<?,?> element) {
        if (this.equals(element.getModule())) {
            return (ModularPolynomialElement<B>) element;
        }
        if (element instanceof PolynomialElement) {
            PolynomialElement<?> p = (PolynomialElement<?>) element;
            List<? extends RingElement<?>> coeffs = p.getCoefficients();
            List<B> newCoeffs = new ArrayList<>(coeffs.size());
            Ring<B> ring = getCoefficientRing();
            for (RingElement<?> coeff : coeffs) {
                B currentCoeff = ring.cast(coeff);
                if (currentCoeff == null) {
                    return null;
                }
                newCoeffs.add(currentCoeff);
            }
            return new ModularPolynomialElement<>(this, newCoeffs);
        }
        if (element instanceof RingElement) {
            B newCoeff = getCoefficientRing().cast(element);
            return newCoeff != null ? new ModularPolynomialElement<>(this, newCoeff) : null;
        }
        return null;
    }

    
    public String toString() {
        StringBuilder buf = new StringBuilder(40);
        buf.append("ModularPolynomialRing(");
        buf.append(getBaseRing());
        buf.append(")[");
        buf.append(modulus);
        buf.append("]");
        return buf.toString();
    }

    
    public String toVisualString() {
        Ring baseRing = getBaseRing();
        StringBuilder s = new StringBuilder();
        if (baseRing instanceof ProductRing) {
            s.append("(");
            s.append(baseRing.toVisualString());
            s.append(")");
        }
        else {
            s.append(baseRing.toVisualString());
        }
        s.append("[");
        Iterator<String> iter = getIndeterminates().iterator();
        String in = iter.next();
        s.append(in);
        while (iter.hasNext()) {
            s.append(",").append(iter.next());
        }
        s.append("]/(");
        s.append(getModulus());
        s.append(")");
        return s.toString();
    }
    
    public String getElementTypeName() {
        return "ModularPolynomialRing";
    }

    @Override
    protected int nonSingletonHashCode() {
        return modulus.hashCode();
    }

}
