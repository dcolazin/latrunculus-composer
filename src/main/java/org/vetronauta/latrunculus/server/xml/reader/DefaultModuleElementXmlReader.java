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

package org.vetronauta.latrunculus.server.xml.reader;

import org.rubato.util.Base64;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.string.QString;
import org.vetronauta.latrunculus.core.math.arith.string.RString;
import org.vetronauta.latrunculus.core.math.arith.string.ZString;
import org.vetronauta.latrunculus.core.math.arith.string.ZnString;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.complex.CElement;
import org.vetronauta.latrunculus.core.math.module.complex.CProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedElement;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialRing;
import org.vetronauta.latrunculus.core.math.module.rational.QElement;
import org.vetronauta.latrunculus.core.math.module.rational.QProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.rational.QStringElement;
import org.vetronauta.latrunculus.core.math.module.rational.QStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.real.RElement;
import org.vetronauta.latrunculus.core.math.module.real.RProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.real.RStringElement;
import org.vetronauta.latrunculus.core.math.module.real.RStringProperFreeElement;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.Iterator;
import java.util.LinkedList;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.BASE64;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FACTOR_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.INDETERMINATE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_ELEMENT;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULUS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUES_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.WORD;

/**
 * @author vetronauta
 */
public class DefaultModuleElementXmlReader implements LatrunculusXmlReader<ModuleElement> {
    
    @Override
    public ModuleElement fromXML(Element element, Class<? extends ModuleElement> clazz, XMLReader reader) {
        if (CElement.class.isAssignableFrom(clazz)) {
            return readCElement(element, clazz, reader);
        }
        if (ZElement.class.isAssignableFrom(clazz)) {
            return readZElement(element, clazz, reader);
        }
        if (ZnElement.class.isAssignableFrom(clazz)) {
            return readZnElement(element, clazz, reader);
        }
        if (QElement.class.isAssignableFrom(clazz)) {
            return readQElement(element, clazz, reader);
        }
        if (RElement.class.isAssignableFrom(clazz)) {
            return readRElement(element, clazz, reader);
        }
        if (CProperFreeElement.class.isAssignableFrom(clazz)) {
            return readCProperFreeElement(element, clazz, reader);
        }
        if (ZProperFreeElement.class.isAssignableFrom(clazz)) {
            return readZProperFreeElement(element, clazz, reader);
        }
        if (ZnProperFreeElement.class.isAssignableFrom(clazz)) {
            return readZnProperFreeElement(element, clazz, reader);
        }
        if (ZStringElement.class.isAssignableFrom(clazz)) {
            return readZStringElement(element, clazz, reader);
        }
        if (ZnStringElement.class.isAssignableFrom(clazz)) {
            return readZnStringElement(element, clazz, reader);
        }
        if (QStringElement.class.isAssignableFrom(clazz)) {
            return readQStringElement(element, clazz, reader);
        }
        if (RStringElement.class.isAssignableFrom(clazz)) {
            return readRStringElement(element, clazz, reader);
        }
        if (ZStringProperFreeElement.class.isAssignableFrom(clazz)) {
            return readZStringProperFreeElement(element, clazz, reader);
        }
        if (ZnStringProperFreeElement.class.isAssignableFrom(clazz)) {
            return readZnStringProperFreeElement(element, clazz, reader);
        }
        if (QStringProperFreeElement.class.isAssignableFrom(clazz)) {
            return readQStringProperFreeElement(element, clazz, reader);
        }
        if (RStringProperFreeElement.class.isAssignableFrom(clazz)) {
            return readRStringProperFreeElement(element, clazz, reader);
        }
        if (QProperFreeElement.class.isAssignableFrom(clazz)) {
            return readQProperFreeElement(element, clazz, reader);
        }
        if (RProperFreeElement.class.isAssignableFrom(clazz)) {
            return readRProperFreeElement(element, clazz, reader);
        }
        if (DirectSumElement.class.isAssignableFrom(clazz)) {
            return readDirectSumElement(element, clazz, reader);
        }
        if (ProductElement.class.isAssignableFrom(clazz)) {
            return readProductElement(element, clazz, reader);
        }
        if (ProductProperFreeElement.class.isAssignableFrom(clazz)) {
            return readProductProperFreeElement(element, clazz, reader);
        }
        if (PolynomialElement.class.isAssignableFrom(clazz)) {
            return readPolynomialElement(element, clazz, reader);
        }
        if (PolynomialProperFreeElement.class.isAssignableFrom(clazz)) {
            return readPolynomialProperFreeElement(element, clazz, reader);
        }
        if (ModularPolynomialElement.class.isAssignableFrom(clazz)) {
            return readModularPolynomialElement(element, clazz, reader);
        }
        if (ModularPolynomialProperFreeElement.class.isAssignableFrom(clazz)) {
            return readModularPolynomialProperFreeElement(element, clazz, reader);
        }
        if (RestrictedElement.class.isAssignableFrom(clazz)) {
            return readRestrictedElement(element, clazz, reader);
        }
        return null;
    }

    private ModuleElement readCElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (element.hasAttribute(VALUE_ATTR)) {
            try {
                Complex val = Complex.parseComplex(element.getAttribute(VALUE_ATTR));
                return new CElement(val);
            }
            catch (NumberFormatException e) {
                reader.setError("Attribute %%1 of type %%2 must be a complex number.", VALUE_ATTR, getElementTypeName(clazz));
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUE_ATTR);
            return null;
        }
    }

    private ModuleElement readZElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (element.hasAttribute(VALUE_ATTR)) {
            try {
                int val = Integer.parseInt(element.getAttribute(VALUE_ATTR));
                return new ZElement(val);
            }
            catch (NumberFormatException e) {
                reader.setError("Attribute %%1 of type %%2 must be an integer.", VALUE_ATTR, getElementTypeName(clazz));
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUE_ATTR);
            return null;
        }
    }

    private ModuleElement readZnElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(VALUE_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUE_ATTR);
            return null;
        }

        if (!element.hasAttribute(MODULUS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), MODULUS_ATTR);
            return null;
        }

        int mod;

        try {
            mod = Integer.parseInt(element.getAttribute(MODULUS_ATTR));
            if (mod < 2) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", MODULUS_ATTR, getElementTypeName(clazz));
            return null;
        }

        try {
            int val = Integer.parseInt(element.getAttribute(VALUE_ATTR));
            return new ZnElement(val, mod);
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", VALUE_ATTR, getElementTypeName(clazz));
            return null;
        }
    }

    private ModuleElement readQElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (element.hasAttribute(VALUE_ATTR)) {
            try {
                Rational val = Rational.parseRational(element.getAttribute(VALUE_ATTR));
                return new QElement(val);
            }
            catch (NumberFormatException e) {
                reader.setError("Attribute %%1 of type %%2 must be a rational number.", VALUE_ATTR, getElementTypeName(clazz));
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUE_ATTR);
            return null;
        }
    }

    private ModuleElement readRElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (element.hasAttribute(VALUE_ATTR)) {
            try {
                double val = Double.parseDouble(element.getAttribute(VALUE_ATTR));
                return new RElement(val);
            }
            catch (NumberFormatException e) {
                reader.setError("Attribute %%1 of type %%2 must be a real number.", VALUE_ATTR, getElementTypeName(clazz));
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUE_ATTR);
            return null;
        }
    }

    private ModuleElement readCProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(VALUES_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUES_ATTR);
            return null;
        }

        String[] values = element.getAttribute(VALUES_ATTR).split(",");
        Complex[] complexValues = new Complex[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                complexValues[i] = Complex.parseComplex(values[i]);
            }
            catch (NumberFormatException e) {
                reader.setError("Values in type %%1 must be a comma-separated list of rationals.", getElementTypeName(clazz));
                return null;
            }
        }

        return CProperFreeElement.make(complexValues);
    }

    private ModuleElement readZProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(VALUES_ATTR)) {
            Element child = XMLReader.getChild(element, BASE64);
            if (child == null) {
                reader.setError("Type %%1 is missing attribute %%2 or child element <%3>.", getElementTypeName(clazz), VALUES_ATTR, BASE64);
                return null;
            }

            String s = XMLReader.getText(child);
            int[] intValues = Base64.decodeIntArray(s);
            return ZProperFreeElement.make(intValues);
        }

        String[] values = element.getAttribute(VALUES_ATTR).split(",");
        int[] intValues = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                intValues[i] = Integer.parseInt(values[i]);
            }
            catch (NumberFormatException e) {
                reader.setError("Values in type %%1 must be a comma-separated list of integers.", getElementTypeName(clazz));
                return null;
            }
        }

        return ZProperFreeElement.make(intValues);
    }

    private ModuleElement readZnProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(VALUES_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUES_ATTR);
            return null;
        }
        if (!element.hasAttribute(MODULUS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), MODULUS_ATTR);
            return null;
        }

        int mod;
        try {
            mod = Integer.parseInt(element.getAttribute(MODULUS_ATTR));
            if (mod < 2) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer > 1.", MODULUS_ATTR, getElementTypeName(clazz));
            return null;
        }

        String[] values = element.getAttribute(VALUES_ATTR).split(",");
        int[] intValues = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                intValues[i] = Integer.parseInt(values[i]);
            }
            catch (NumberFormatException e) {
                reader.setError("Values in type %%1 must be a comma-separated list of integers.", getElementTypeName(clazz));
                return null;
            }
        }

        return ZnProperFreeElement.make(intValues, mod);
    }

    private ModuleElement readZStringElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, WORD);
        if (childElement != null) {
            LinkedList<Integer> factors = new LinkedList<>();
            LinkedList<String> words = new LinkedList<>();
            String factor;
            int i;

            if (!childElement.hasAttribute(FACTOR_ATTR)) {
                reader.setError("Element <%1> is missing attribute %%2.", WORD, FACTOR_ATTR);
                return null;
            }
            factor = childElement.getAttribute(FACTOR_ATTR);
            try {
                i = Integer.parseInt(factor);
            }
            catch (NumberFormatException e) {
                reader.setError("Attribute %%2 must be an integer.", FACTOR_ATTR);
                return null;
            }
            factors.add(i);
            words.add(childElement.getTextContent());
            Element next = XMLReader.getNextSibling(childElement, WORD);
            while (next != null) {
                if (!next.hasAttribute(FACTOR_ATTR)) {
                    reader.setError("Element <%1> is missing attribute %%2.", WORD, FACTOR_ATTR);
                    return null;
                }
                factor = childElement.getAttribute(FACTOR_ATTR);
                try {
                    i = Integer.parseInt(factor);
                }
                catch (NumberFormatException e) {
                    reader.setError("Attribute %%1 must be an integer.", FACTOR_ATTR);
                    return null;
                }
                factors.add(i);
                words.add(next.getTextContent());
                next = XMLReader.getNextSibling(next, WORD);
            }
            int[] factorArray = new int[factors.size()];
            String[] wordArray = new String[factors.size()];
            int j = 0;
            Iterator<Integer> fiter = factors.iterator();
            Iterator<String> witer = words.iterator();
            while (fiter.hasNext()) {
                factorArray[j] = fiter.next();
                wordArray[j] = witer.next();
                j++;
            }
            ZString zstring = new ZString(wordArray, factorArray);
            return new ZStringElement(zstring);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), WORD);
            return null;
        }
    }

    private ModuleElement readZnStringElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(MODULUS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), MODULUS_ATTR);
            return null;
        }

        int mod;
        try {
            mod = Integer.parseInt(element.getAttribute(MODULUS_ATTR));
            if (mod < 2) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer > 1.", MODULUS_ATTR, getElementTypeName(clazz));
            return null;
        }

        Element childElement = XMLReader.getChild(element, WORD);
        if (childElement != null) {
            LinkedList<Integer> factors = new LinkedList<Integer>();
            LinkedList<String> words = new LinkedList<String>();
            String factor;
            Integer integer;

            if (!childElement.hasAttribute(FACTOR_ATTR)) {
                reader.setError("Element <%1> is missing attribute %%2.", WORD, FACTOR_ATTR);
                return null;
            }
            factor = childElement.getAttribute(FACTOR_ATTR);
            try {
                integer = Integer.valueOf(Integer.parseInt(factor));
            }
            catch (NumberFormatException e) {
                reader.setError("Attribute %%1 must be an integer.", FACTOR_ATTR);
                return null;
            }
            factors.add(integer);
            words.add(childElement.getTextContent());
            Element next = XMLReader.getNextSibling(childElement, WORD);
            while (next != null) {
                if (!next.hasAttribute(FACTOR_ATTR)) {
                    reader.setError("Element <%1> is missing attribute %%2.", WORD, FACTOR_ATTR);
                    return null;
                }
                factor = childElement.getAttribute(FACTOR_ATTR);
                try {
                    integer = Integer.valueOf(Integer.parseInt(factor));
                }
                catch (NumberFormatException e) {
                    reader.setError("Attribute %%1 must be a real number.", FACTOR_ATTR);
                    return null;
                }
                factors.add(integer);
                words.add(next.getTextContent());
                next = XMLReader.getNextSibling(next, WORD);
            }
            int[] factorArray = new int[factors.size()];
            String[] wordArray = new String[factors.size()];
            int i = 0;
            Iterator<Integer> fiter = factors.iterator();
            Iterator<String> witer = words.iterator();
            while (fiter.hasNext()) {
                factorArray[i] = fiter.next().intValue();
                wordArray[i] = witer.next();
                i++;
            }
            ZnString znstring = new ZnString(wordArray, factorArray, mod);
            return new ZnStringElement(znstring);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), WORD);
            return null;
        }
    }

    private ModuleElement readQStringElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, WORD);
        if (childElement != null) {
            LinkedList<Rational> factors = new LinkedList<Rational>();
            LinkedList<String> words = new LinkedList<String>();
            String factor;
            Rational r;

            if (!childElement.hasAttribute(FACTOR_ATTR)) {
                reader.setError("Element <%1> is missing attribute %%2.", WORD, FACTOR_ATTR);
                return null;
            }
            factor = childElement.getAttribute(FACTOR_ATTR);
            try {
                r = Rational.parseRational(factor);
            }
            catch (NumberFormatException e) {
                reader.setError("Attribute %%1 must be a rational number.", FACTOR_ATTR);
                return null;
            }
            factors.add(r);
            words.add(childElement.getTextContent());
            Element next = XMLReader.getNextSibling(childElement, WORD);
            while (next != null) {
                if (!next.hasAttribute(FACTOR_ATTR)) {
                    reader.setError("Element <$1> is missing attribute %%2.", WORD, FACTOR_ATTR);
                    return null;
                }
                factor = childElement.getAttribute(FACTOR_ATTR);
                try {
                    r = Rational.parseRational(factor);
                }
                catch (NumberFormatException e) {
                    reader.setError("Attribute %%1 must be a rational number.", FACTOR_ATTR);
                    return null;
                }
                factors.add(r);
                words.add(next.getTextContent());
                next = XMLReader.getNextSibling(next, WORD);
            }
            Rational[] factorArray = new Rational[factors.size()];
            String[] wordArray = new String[factors.size()];
            int i = 0;
            Iterator<Rational> fiter = factors.iterator();
            Iterator<String> witer = words.iterator();
            while (fiter.hasNext()) {
                factorArray[i] = fiter.next();
                wordArray[i] = witer.next();
                i++;
            }
            QString qstring = new QString(wordArray, factorArray);
            return new QStringElement(qstring);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), WORD);
            return null;
        }
    }

    private ModuleElement readRStringElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, WORD);
        if (childElement != null) {
            LinkedList<Double> factors = new LinkedList<Double>();
            LinkedList<String> words = new LinkedList<String>();
            String factor;
            double d;

            if (!childElement.hasAttribute(FACTOR_ATTR)) {
                reader.setError("Element <%1> is missing attribute %%2.", WORD, FACTOR_ATTR);
                return null;
            }
            factor = childElement.getAttribute(FACTOR_ATTR);
            try {
                d = Double.parseDouble(factor);
            }
            catch (NumberFormatException e) {
                reader.setError("Attribute %%1 must be a real number.", FACTOR_ATTR);
                return null;
            }
            factors.add(d);
            words.add(childElement.getTextContent());
            Element next = XMLReader.getNextSibling(childElement, WORD);
            while (next != null) {
                if (!next.hasAttribute(FACTOR_ATTR)) {
                    reader.setError("Element <%1> is missing attribute %%2.", WORD, FACTOR_ATTR);
                    return null;
                }
                factor = childElement.getAttribute(FACTOR_ATTR);
                try {
                    d = Double.valueOf(Double.parseDouble(factor));
                }
                catch (NumberFormatException e) {
                    reader.setError("Attribute %%1 must be a real number.", FACTOR_ATTR);
                    return null;
                }
                factors.add(d);
                words.add(next.getTextContent());
                next = XMLReader.getNextSibling(next, WORD);
            }
            double[] factorArray = new double[factors.size()];
            String[] wordArray = new String[factors.size()];
            int i = 0;
            Iterator<Double> fiter = factors.iterator();
            Iterator<String> witer = words.iterator();
            while (fiter.hasNext()) {
                factorArray[i] = fiter.next().doubleValue();
                wordArray[i] = witer.next();
                i++;
            }
            RString rstring = new RString(wordArray, factorArray);
            return new RStringElement(rstring);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), WORD);
            return null;
        }
    }

    private ModuleElement readZStringProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            LinkedList<ZStringElement> elements = new LinkedList<ZStringElement>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (!(moduleElement instanceof ZStringElement)) {
                reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "ZStringElement");
                return null;
            }
            ZStringElement ringElement = (ZStringElement)moduleElement;
            elements.add(ringElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                if (!(moduleElement instanceof ZStringElement)) {
                    reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "ZStringElement");
                    return null;
                }
                ringElement = (ZStringElement)moduleElement;
                elements.add(ringElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            ZString[] coefficients = new ZString[elements.size()];
            Iterator<ZStringElement> iter = elements.iterator();
            int i = 0;
            while (iter.hasNext()) {
                coefficients[i++] = iter.next().getValue();
            }
            return ZStringProperFreeElement.make(coefficients);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleElement readZnStringProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        int modulus0 = XMLReader.getIntAttribute(element, MODULUS_ATTR, 2, Integer.MAX_VALUE, 2);
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            LinkedList<ZnStringElement> elements = new LinkedList<ZnStringElement>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (!(moduleElement instanceof ZnStringElement)) {
                reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "ZnStringElement");
                return null;
            }
            ZnStringElement ringElement = (ZnStringElement)moduleElement;
            elements.add(ringElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                if (!(moduleElement instanceof ZnStringElement)) {
                    reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "ZnStringElement");
                    return null;
                }
                ringElement = (ZnStringElement)moduleElement;
                elements.add(ringElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            ZnString[] coefficients = new ZnString[elements.size()];
            Iterator<ZnStringElement> iter = elements.iterator();
            int i = 0;
            while (iter.hasNext()) {
                coefficients[i++] = iter.next().getValue();
            }
            return ZnStringProperFreeElement.make(coefficients, modulus0);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleElement readQStringProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            LinkedList<QStringElement> elements = new LinkedList<QStringElement>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (!(moduleElement instanceof QStringElement)) {
                reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "QStringElement");
                return null;
            }
            QStringElement ringElement = (QStringElement)moduleElement;
            elements.add(ringElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                if (!(moduleElement instanceof QStringElement)) {
                    reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "QStringElement");
                    return null;
                }
                ringElement = (QStringElement)moduleElement;
                elements.add(ringElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            QString[] coefficients = new QString[elements.size()];
            Iterator<QStringElement> iter = elements.iterator();
            int i = 0;
            while (iter.hasNext()) {
                coefficients[i++] = iter.next().getValue();
            }
            return QStringProperFreeElement.make(coefficients);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleElement readRStringProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            LinkedList<RStringElement> elements = new LinkedList<RStringElement>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (!(moduleElement instanceof RStringElement)) {
                reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "RStringElement");
                return null;
            }
            RStringElement ringElement = (RStringElement)moduleElement;
            elements.add(ringElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                if (!(moduleElement instanceof RStringElement)) {
                    reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "RStringElement");
                    return null;
                }
                ringElement = (RStringElement)moduleElement;
                elements.add(ringElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            RString[] coefficients = new RString[elements.size()];
            Iterator<RStringElement> iter = elements.iterator();
            int i = 0;
            while (iter.hasNext()) {
                coefficients[i++] = iter.next().getValue();
            }
            return RStringProperFreeElement.make(coefficients);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleElement readQProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(VALUES_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUES_ATTR);
            return null;
        }

        String[] values = element.getAttribute(VALUES_ATTR).split(",");
        Rational[] rationalValues = new Rational[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                rationalValues[i] = Rational.parseRational(values[i]);
            }
            catch (NumberFormatException e) {
                reader.setError("Values in type %%1 must be a comma-separated list of rationals.", getElementTypeName(clazz));
                return null;
            }
        }

        return QProperFreeElement.make(rationalValues);
    }

    private ModuleElement readRProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(VALUES_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUES_ATTR);
            return null;
        }

        String[] values = element.getAttribute(VALUES_ATTR).split(",");
        double[] doubleValues = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                doubleValues[i] = Double.parseDouble(values[i]);
            }
            catch (NumberFormatException e) {
                reader.setError("Values in type %%1 must be a comma-separated list of reals.", getElementTypeName(clazz));
                return null;
            }
        }

        return RProperFreeElement.make(doubleValues);
    }

    private ModuleElement readDirectSumElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            LinkedList<ModuleElement> elements = new LinkedList<>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            elements.add(moduleElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                elements.add(moduleElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            ModuleElement[] coefficients = new ModuleElement[elements.size()];
            int i = 0;
            for (ModuleElement e : elements) {
                coefficients[i++] = e;
            }
            return DirectSumElement.make(coefficients);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleElement readProductElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            // ProductElement has children (i.e., factors)
            LinkedList<RingElement> elements = new LinkedList<RingElement>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (!(moduleElement instanceof RingElement)) {
                reader.setError("Type %%1 must have ring element children.", getElementTypeName(clazz));
                return null;
            }
            elements.add((RingElement)moduleElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                if (!(moduleElement instanceof RingElement)) {
                    reader.setError("Type %%1 must have ring element children.", getElementTypeName(clazz));
                    return null;
                }
                elements.add((RingElement)moduleElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            RingElement[] factors0 = new RingElement[elements.size()];
            int i = 0;
            for (RingElement e : elements) {
                factors0[i++] = e;
            }
            ProductElement result = ProductElement.make(factors0);
            return result;
        }
        else {
            reader.setError("There must be at least 2 <%1> elements in type %%2.", MODULE, getElementTypeName(clazz));
            return null;
        }

    }

    private ModuleElement readProductProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement;

        // get product ring
        childElement = XMLReader.getChild(element, MODULE);
        if (childElement == null) {
            reader.setError("Type %%1 must have a first child of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
        Module module0 = reader.parseModule(childElement);
        if (!(module0 instanceof ProductRing)) {
            reader.setError("Module in %%1 must be a product ring.", getElementTypeName(clazz));
            return null;
        }
        ProductRing productRing = (ProductRing)module0;

        // get components
        childElement = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
        if (childElement != null) {
            LinkedList<ProductElement> elements = new LinkedList<>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (!(moduleElement instanceof ProductElement)) {
                reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "ProductElement");
                return null;
            }
            ProductElement productElement = (ProductElement)moduleElement;
            elements.add(productElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                if (!(moduleElement instanceof ProductElement)) {
                    reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "ProductElement");
                    return null;
                }
                productElement = (ProductElement)moduleElement;
                elements.add(productElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            ProductElement[] components = new ProductElement[elements.size()];
            Iterator<ProductElement> iter = elements.iterator();
            int i = 0;
            while (iter.hasNext()) {
                components[i++] = iter.next();
            }
            return ProductProperFreeElement.make(productRing, components);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleElement readPolynomialElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(INDETERMINATE_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), INDETERMINATE_ATTR);
            return null;
        }
        String indeterminate = element.getAttribute(INDETERMINATE_ATTR);
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            LinkedList<RingElement> elements = new LinkedList<RingElement>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (!(moduleElement instanceof RingElement)) {
                reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "RingElement");
                return null;
            }
            RingElement ringElement = (RingElement)moduleElement;
            Ring ring0 = ringElement.getRing();
            elements.add(ringElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                if (!(moduleElement instanceof RingElement)) {
                    reader.setError("Type %%1 must have children of a subtype of %%2.", getElementTypeName(clazz), "RingElement");
                    return null;
                }
                ringElement = (RingElement)moduleElement;
                if (!ring0.hasElement(ringElement)) {
                    reader.setError("Type %%1 must have children all of the same type.", getElementTypeName(clazz));
                    return null;
                }
                elements.add(ringElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            RingElement[] coeffs = new RingElement[elements.size()];
            int i = 0;
            for (RingElement e : elements) {
                coeffs[i++] = e;
            }
            PolynomialElement result = new PolynomialElement(indeterminate, coeffs);
            return result;
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleElement readPolynomialProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            LinkedList<PolynomialElement> elements = new LinkedList<PolynomialElement>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (!(moduleElement instanceof PolynomialElement)) {
                reader.setError("Children of type %%1 must be of type %%2.", getElementTypeName(clazz), "PolynomialElement");
                return null;
            }
            elements.add((PolynomialElement)moduleElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                if (!(moduleElement instanceof PolynomialElement)) {
                    reader.setError("Children of type %%1 must be of type %%2.", getElementTypeName(clazz), "PolynomialElement");
                    return null;
                }
                elements.add((PolynomialElement)moduleElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            PolynomialElement[] values = new PolynomialElement[elements.size()];
            Iterator<PolynomialElement> iter = elements.iterator();
            int i = 0;
            while (iter.hasNext()) {
                values[i++] = iter.next();
            }
            PolynomialRing rng = values[0].getRing();
            PolynomialFreeElement result = PolynomialProperFreeElement.make(rng, values);
            return result;
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleElement readModularPolynomialElement(Element element, Class<?> clazz, XMLReader reader) {
        element = XMLReader.getChild(element, MODULE_ELEMENT);
        if (element == null) {
            reader.setError("Type %%1 is missing child of type %%1.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
        Module m = reader.parseModule(element);
        if (m != null && m instanceof ModularPolynomialRing) {
            element = XMLReader.getNextSibling(element, MODULE_ELEMENT);
            ModuleElement e = reader.parseModuleElement(element);
            if (e != null && e instanceof PolynomialElement) {
                try {
                    return new ModularPolynomialElement((ModularPolynomialRing)m, (PolynomialElement)e);
                }
                catch (IllegalArgumentException ex) {
                    reader.setError(ex.getMessage());
                }
            }
            else {
                reader.setError("Type %%1 is missing child of type %%1.", getElementTypeName(clazz), "ModularPolynomialRing");
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type %%1.", getElementTypeName(clazz), "PolynomialElement");
        }
        return null;
    }

    private ModuleElement readModularPolynomialProperFreeElement(Element element, Class<?> clazz, XMLReader reader) {
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            LinkedList<ModularPolynomialElement> elements = new LinkedList<ModularPolynomialElement>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (!(moduleElement instanceof ModularPolynomialElement)) {
                reader.setError("Children of type %%1 must be of type %%2.", getElementTypeName(clazz), "ModularPolynomialElement");
                return null;
            }
            elements.add((ModularPolynomialElement)moduleElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                if (!(moduleElement instanceof ModularPolynomialElement)) {
                    reader.setError("Children of type %%1 must be of type %%2.", getElementTypeName(clazz), "ModularPolynomialElement");
                    return null;
                }
                elements.add((ModularPolynomialElement)moduleElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            ModularPolynomialElement[] values = new ModularPolynomialElement[elements.size()];
            Iterator<ModularPolynomialElement> iter = elements.iterator();
            int i = 0;
            while (iter.hasNext()) {
                values[i++] = iter.next();
            }
            ModularPolynomialRing rng = values[0].getRing();
            ModularPolynomialFreeElement result = ModularPolynomialProperFreeElement.make(rng, values);
            return result;
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleElement readRestrictedElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            Module module0 = reader.parseModule(childElement);
            if (module0 == null) {
                return null;
            }
            if (!(module0 instanceof RestrictedModule)) {
                reader.setError("Module in type %%1 must be of type %%2.", getElementTypeName(clazz), "RestrictedModule");
                return null;
            }
            RestrictedModule rmodule = (RestrictedModule)module0;
            childElement = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            if (childElement != null) {
                ModuleElement el = reader.parseModuleElement(childElement);
                if (el == null) {
                    return null;
                }
                RestrictedElement relement = null;
                try {
                    relement = RestrictedElement.make(rmodule, el);
                }
                catch (DomainException e) {
                    reader.setError(e);
                }
                return relement;
            }
            else {
                reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }

    //TODO name repository as some subclasses are serialized as superclasses
    private String getElementTypeName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
    
}