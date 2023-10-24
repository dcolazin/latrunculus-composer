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

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.DIMENSION_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

/**
 * Free modules over polynomials.
 * @see PolynomialProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class PolynomialProperFreeModule<B extends RingElement<B>> extends ProperFreeModule<PolynomialProperFreeElement<B>,PolynomialElement<B>>
        implements PolynomialFreeModule<PolynomialProperFreeElement<B>,B> {

    public static <X extends RingElement<X>> PolynomialFreeModule<?,X> make(Ring<X> coefficientRing, String indeterminate, int dimension) {
        if (dimension == 1) {
            return PolynomialRing.make(coefficientRing, indeterminate);
        }
        return new PolynomialProperFreeModule<>(coefficientRing, indeterminate, Math.max(dimension, 0));

    }

    public static <X extends RingElement<X>> PolynomialFreeModule<?,X> make(PolynomialRing<X> polyRing, int dimension) {
        if (dimension == 1) {
            return polyRing;
        }
        return new PolynomialProperFreeModule<>(polyRing.getCoefficientRing(), polyRing.getIndeterminate(), dimension);
    }

    @Override
    public PolynomialProperFreeElement<B> getZero() {
        PolynomialElement[] res = new PolynomialElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            res[i] = ring.getZero();
        }
        return (PolynomialProperFreeElement<B>) PolynomialProperFreeElement.make(ring, res);
    }
    
    
    public PolynomialProperFreeElement<B> getUnitElement(int i) {
        PolynomialElement[] v = new PolynomialElement[getDimension()];
        assert(i >= 0 && i < getDimension());
        for (int j = 0; j < getDimension(); j++) {
            v[j] = getRing().getZero();
        }
        v[i] = getRing().getOne();
        return (PolynomialProperFreeElement<B>) PolynomialProperFreeElement.make(getRing(), v);
    }
    

    public PolynomialFreeModule getNullModule() {
        return make(ring.getCoefficientRing(), ring.getIndeterminate(), 0);
    }
    
    
    public boolean isNullModule() {
        return this.getDimension() == 0;
    }

    
    public PolynomialRing getComponentModule(int i) {
        return ring;
    }

    
    public PolynomialRing getRing() {
        return ring;
    }


    public Ring getCoefficientRing() {
        return ring.getCoefficientRing();
    }


    public String getIndeterminate() {
        return ring.getIndeterminate();
    }


    public boolean isVectorSpace() {
        return false;
    }

    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof PolynomialProperFreeElement &&
                element.getLength() == getDimension() &&
                ring.equals(((PolynomialElement)element).getRing()));
    }


    public int compareTo(Module object) {
        if (object instanceof PolynomialProperFreeModule) {
            PolynomialProperFreeModule module = (PolynomialProperFreeModule)object;
            int c = getRing().compareTo(module.getRing());
            if (c != 0) {
                return c;
            }
            int d = getDimension()-module.getDimension();
            if (d != 0) {
                return d;
            }
            return 0;
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public PolynomialProperFreeElement<B> createElement(List<ModuleElement<?,?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }
        PolynomialElement[] values = new PolynomialElement[getDimension()];
        Iterator<ModuleElement<?, ?>> iter = elements.iterator();
        for (int i = 0; i < getDimension(); i++) {
            values[i] = ring.cast(iter.next());
            if (values[i] == null) {
                return null;
            }
        }
        return (PolynomialProperFreeElement)PolynomialProperFreeElement.make(ring, values);
    }
    
   
    public PolynomialProperFreeElement cast(ModuleElement element) {
        int dim = element.getLength();
        if (dim != getDimension()) {
            return null;
        }
        PolynomialElement[] values = new PolynomialElement[dim];
        for (int i = 0; i < dim; i++) {
            values[i] = ring.cast(element.getComponent(i));
            if (values[i] == null) {
                return null;
            }
        }
        return (PolynomialProperFreeElement)PolynomialProperFreeElement.make(ring, values);
    }

    
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof PolynomialProperFreeModule) {
            PolynomialProperFreeModule m = (PolynomialProperFreeModule)object;
            if (getDimension() != m.getDimension()) {
                return false;
            }
            return getRing().equals(m.getRing());
        }
        return false;
    }

    
    public PolynomialProperFreeElement parseString(String string) {
        ArrayList<String> strings = parse(TextUtils.unparenthesize(string));
        if (strings.size() < getDimension()) {
            return null;
        }
        PolynomialElement[] values = new PolynomialElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            String s = strings.get(i);
            values[i] = ring.parseString(s);
            if (values[i] == null) {
                return null;
            }            
        }
        return (PolynomialProperFreeElement)PolynomialProperFreeElement.make(ring, values);
    }
    
    
    private static ArrayList<String> parse(String s) {
        int pos = 0;
        int lastpos = 0;
        int level = 0;
        ArrayList<String> m = new ArrayList<String>();
        while (pos < s.length()) {
            if (s.charAt(pos) == '(') {
                pos++;
                level++;
            }
            else if (s.charAt(pos) == ')') {
                pos++;
                level--;
            }
            else if (s.charAt(pos) == ',' && level == 0) {
                m.add(s.substring(lastpos, pos));                
                pos++;
                lastpos = pos;
            }
            else {
                pos++;
            }
        }
        m.add(s.substring(lastpos,pos).trim());
        return m;
    }

    
    public String toString() {
        return "PolynomialFreeModule["+getCoefficientRing()+","+getIndeterminate()+","+getDimension()+"]";
    }

    
    public String toVisualString() {
        return "("+getRing().toVisualString()+")^"+getDimension();
    }

    public String getElementTypeName() {
        return "PolynomialFreeModule";
    }
    
    
    public int hashCode() {
        return (37*basicHash + getDimension()) ^ ring.hashCode();
    }
    
    
    protected ModuleMorphism _getProjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), getDimension(), 1);
        m.setMatrix(0, index, getRing().getOne());
        return m;
    }
    
    
    protected ModuleMorphism _getInjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), 1, getDimension());
        m.setMatrix(index, 0, getRing().getOne());
        return m;
    }

    
    private PolynomialProperFreeModule(Ring coefficientRing, String indeterminate, int dimension) {
        super(dimension);
        this.ring = PolynomialRing.make(coefficientRing, indeterminate);
        
    }

    
    private PolynomialRing ring;
    
    private static final int basicHash = "PolynomialFreeModule".hashCode();    
}
