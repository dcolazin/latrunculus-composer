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

import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.element.generic.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.ProductElement;
import org.vetronauta.latrunculus.core.math.element.generic.RestrictedElement;
import org.vetronauta.latrunculus.core.math.module.generic.RestrictedModule;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.element.generic.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.generic.ModularPolynomialRing;
import org.vetronauta.latrunculus.core.math.element.generic.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.factory.RingRepository;
import org.vetronauta.latrunculus.server.parse.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
        if (Vector.class.isAssignableFrom(clazz)) {
            if (element.hasAttribute(MODULUS_ATTR)) {
                return readZnVector(element, clazz, reader);
            }
            return readVector(element, clazz, reader);
        }
        if (StringMap.class.isAssignableFrom(clazz)) {
            return readStringMap(element, clazz, reader);
        }
        if (DirectSumElement.class.isAssignableFrom(clazz)) {
            return readDirectSumElement(element, clazz, reader);
        }
        if (ProductElement.class.isAssignableFrom(clazz)) {
            return readProductElement(element, clazz, reader);
        }
        if (PolynomialElement.class.isAssignableFrom(clazz)) {
            return readPolynomialElement(element, clazz, reader);
        }
        if (ModularPolynomialElement.class.isAssignableFrom(clazz)) {
            return readModularPolynomialElement(element, clazz, reader);
        }
        if (RestrictedElement.class.isAssignableFrom(clazz)) {
            return readRestrictedElement(element, clazz, reader);
        }
        if (element.hasAttribute(MODULUS_ATTR)) {
            return readModularArithmeticElement(element, clazz, reader);
        }
        return readArithmeticElement(element, clazz, reader);
    }

    private RingElement<?> readArithmeticElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (element.hasAttribute(VALUE_ATTR)) {
            try {
                return ArithmeticParsingUtils.parse(element.getAttribute(VALUE_ATTR));
            }
            catch (NumberFormatException e) {
                reader.setError("Attribute %%1 of type %%2 must be a parsable number.", VALUE_ATTR, getElementTypeName(clazz));
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUE_ATTR);
            return null;
        }
    }
    
    private Modulus readModularArithmeticElement(Element element, Class<?> clazz, XMLReader reader) {
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
            return new Modulus(val, mod);
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", VALUE_ATTR, getElementTypeName(clazz));
            return null;
        }
    }

    private Vector<?> readVector(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(VALUES_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), VALUES_ATTR);
            return null;
        }

        Ring<?> ring = ArithmeticParsingUtils.parseRing(element.getAttribute(VALUE_ATTR));
        try {
            return parseWithRing(ring, element.getAttribute(VALUES_ATTR).split(","));
        } catch (NumberFormatException e) {
            reader.setError("Values in type %%1 must be a comma-separated list of rationals.", getElementTypeName(clazz));
            return null;
        }
    }

    private static <X extends RingElement<X>> Vector<X> parseWithRing(Ring<X> ring, String[] values) {
        List<X> elements = new ArrayList<>(values.length);
        for (int i = 0; i < values.length; i++) {
            elements.add(ArithmeticParsingUtils.parse(ring, values[i]));
        }
        return new Vector<>(ring, elements);
    }

    private ModuleElement readZnVector(Element element, Class<?> clazz, XMLReader reader) {
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
        List<Modulus> intValues = new ArrayList<>(values.length);
        for (int i = 0; i < values.length; i++) {
            try {
                intValues.add(new Modulus(Integer.parseInt(values[i]), mod));
            } catch (NumberFormatException e) {
                reader.setError("Values in type %%1 must be a comma-separated list of integers.", getElementTypeName(clazz));
                return null;
            }
        }

        return new Vector<>(RingRepository.getModulusRing(mod), intValues);
    }

    private ModuleElement readStringMap(Element element, Class<?> clazz, XMLReader reader) {
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
            //TODO actually read proper ArithmeticNumber
            ZInteger[] factorArray = new ZInteger[factors.size()];
            String[] wordArray = new String[factors.size()];
            int j = 0;
            Iterator<Integer> fiter = factors.iterator();
            Iterator<String> witer = words.iterator();
            while (fiter.hasNext()) {
                factorArray[j] = new ZInteger(fiter.next());
                wordArray[j] = witer.next();
                j++;
            }
            return new StringMap<>(wordArray, factorArray);

        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), WORD);
            return null;
        }
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

    private ModuleElement readPolynomialElement(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(INDETERMINATE_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), INDETERMINATE_ATTR);
            return null;
        }
        String indeterminate = element.getAttribute(INDETERMINATE_ATTR);
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            List<RingElement<?>> elements = new LinkedList<>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (!(moduleElement instanceof RingElement)) {
                reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "RingElement");
                return null;
            }
            RingElement<?> ringElement = (RingElement<?>) moduleElement;
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
            PolynomialElement result = new PolynomialElement(indeterminate, elements);
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
