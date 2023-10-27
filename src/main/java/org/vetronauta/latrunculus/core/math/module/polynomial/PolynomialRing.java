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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The ring of polynomials with coefficients in a specified ring.
 * @see PolynomialElement
 * 
 * @author Gérard Milmeister
 */
public final class PolynomialRing<R extends RingElement<R>> extends Ring<PolynomialElement<R>> {

    private final Ring<R> coefficientRing;
    private final Ring<?> baseRing;
    private final String indeterminate;

    private PolynomialRing(Ring<R> coefficientRing, String indeterminate) {
        this.indeterminate = indeterminate;
        this.coefficientRing = coefficientRing;
        this.baseRing = coefficientRing instanceof PolynomialRing ? ((PolynomialRing<?>) coefficientRing).baseRing : coefficientRing;
    }

    public static <X extends RingElement<X>> PolynomialRing<X> make(Ring<X> coefficientRing, String indeterminate) {
        if (coefficientRing instanceof PolynomialRing && ((PolynomialRing<?>) coefficientRing).hasIndeterminate(indeterminate)) {
            throw new IllegalArgumentException("Indeterminate " + indeterminate + " occurs in " + coefficientRing);
        }
        return new PolynomialRing<>(coefficientRing, indeterminate);
    }
    
    public String getIndeterminate() {
        return indeterminate;
    }

    public boolean hasIndeterminate(String indet) {
        if (getIndeterminate().equals(indet)) {
            return true;
        }
        if (coefficientRing instanceof PolynomialRing) {
            return ((PolynomialRing<?>)coefficientRing).hasIndeterminate(indet);
        }
        return false;
    }
    
    public List<String> getIndeterminates() {
        if (coefficientRing instanceof PolynomialRing) {
            List<String> indeterminates = ((PolynomialRing<?>)coefficientRing).getIndeterminates();
            indeterminates.add(0, getIndeterminate());
            return indeterminates;
        }
        List<String> indeterminates = new LinkedList<>();
        indeterminates.add(getIndeterminate());
        return indeterminates;
    }
    
    public Ring<R> getCoefficientRing() {
        return coefficientRing;
    }

    public Ring<?> getBaseRing() {
        return baseRing;
    }
    
    @Override
    public PolynomialElement<R> getZero() {
        return new PolynomialElement<>(this, getCoefficientRing().getZero());
    }

    @Override
    public PolynomialElement<R> getOne() {
        return new PolynomialElement<>(this, getCoefficientRing().getOne());
    }

    @Override
    public PolynomialProperFreeModule<R> getNullModule() {
        return (PolynomialProperFreeModule<R>) PolynomialProperFreeModule.make(this, 0);
    }
    
    @Override
    public boolean isField() {
        return false;
    }
    
    @Override
    public boolean isVectorSpace() {
        return false;
    }

    @Override
    public boolean hasElement(ModuleElement element) {
        if (element instanceof PolynomialElement) {
            return element.getModule().equals(this);
        }
        return false;
    }

    @Override
    public FreeModule<?, PolynomialElement<R>> getFreeModule(int dimension) {
        return PolynomialProperFreeModule.make(this, dimension);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof PolynomialRing) {
            PolynomialRing<?> r = (PolynomialRing<?>)object;
            return (getCoefficientRing().equals(r.getCoefficientRing()) &&
                        getIndeterminate().equals(r.getIndeterminate()));
        }
        return false;
    }

    @Override
    public int compareTo(Module object) {
        if (object instanceof PolynomialRing) {
            PolynomialRing p = (PolynomialRing)object;
            int c = getCoefficientRing().compareTo(p.getCoefficientRing());
            if (c != 0) {
                return c;
            }
            return getIndeterminate().compareTo(p.getIndeterminate());
        }
        return super.compareTo(object);
    }

    @Override
    public PolynomialElement<R> createElement(List<ModuleElement<?, ?>> elements) {
        if (elements.size() == 1 && (hasElement(elements.get(0)))) {
                return (PolynomialElement)elements.get(0);

        }
        List<R> coeffs = new ArrayList<>(elements.size());
        for (ModuleElement<?,?> e : elements) {
            R currentElement = getCoefficientRing().cast(e);
            if (currentElement == null) {
                return null;
            }
            coeffs.add(currentElement);
        }
        return new PolynomialElement<>(this, coeffs);
    }

    @Override
    public PolynomialElement<R> cast(ModuleElement<?,?> element) {
        if (this.equals(element.getModule())) {
            return (PolynomialElement<R>) element;
        }
        if (element instanceof PolynomialElement) {
            PolynomialElement<?> p = (PolynomialElement<?>) element;
            List<? extends RingElement<?>> coeffs = p.getCoefficients();
            List<R> newCoeffs = new ArrayList<>(coeffs.size());
            Ring<R> ring = getCoefficientRing();
            for (RingElement<?> coeff : coeffs) {
                R currentCoeff = ring.cast(coeff);
                if (currentCoeff == null) {
                    return null;
                }
                newCoeffs.add(currentCoeff);
            }
            return new PolynomialElement<>(this, newCoeffs);
        }
        if (element instanceof RingElement) {
            R newCoeff = getCoefficientRing().cast(element);
            if (newCoeff != null) {
                return new PolynomialElement<>(this, newCoeff);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(40);
        buf.append("PolynomialRing(");
        buf.append(getBaseRing());
        buf.append(")[");
        Iterator<String> iter = getIndeterminates().iterator();
        String in = iter.next();
        buf.append(in);
        while (iter.hasNext()) {
            buf.append(",");
            buf.append(iter.next());
        }
        buf.append("]");
        return buf.toString();
    }

    @Override
    public String toVisualString() {
        Ring<?> baseRing = getBaseRing();
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
        return s+"]";
    }

    public String getElementTypeName() {
        return "PolynomialRing";
    }

    @Override
    public int hashCode() {
        int hashCode = basicHash;
        hashCode ^= coefficientRing.hashCode();
        hashCode ^= indeterminate.hashCode();
        return hashCode;
    }

    private static final int basicHash = "PolynomialRing".hashCode();

}
