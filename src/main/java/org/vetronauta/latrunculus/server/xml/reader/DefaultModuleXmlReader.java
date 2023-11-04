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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumModule;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.generic.StringVectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.LinkedList;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.DIMENSION_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.INDETERMINATE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_ELEMENT;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_MORPHISM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULUS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

/**
 * @author vetronauta
 */
public class DefaultModuleXmlReader implements LatrunculusXmlReader<Module> {

    //TODO common logic
    //TODO asserts vs proper checks
    
    @Override
    public Module fromXML(Element element, Class<? extends Module> clazz, XMLReader reader) {
        if (VectorModule.class.isAssignableFrom(clazz)) {
                return readVectorModule(element, clazz, reader);
           }
        if (CRing.class.isAssignableFrom(clazz)) {
                return readCRing(element, clazz, reader);
           }
        if (ZRing.class.isAssignableFrom(clazz)) {
                return readZRing(element, clazz, reader);
           }
        if (ZnRing.class.isAssignableFrom(clazz)) {
                return readZnRing(element, clazz, reader);
           }
        if (QRing.class.isAssignableFrom(clazz)) {
                return readQRing(element, clazz, reader);
           }
        if (RRing.class.isAssignableFrom(clazz)) {
                return readRRing(element, clazz, reader);
           }
        if (StringVectorModule.class.isAssignableFrom(clazz)) {
                return readArithmeticStringMultiModule(element, clazz, reader);
           }
        if (StringRing.class.isAssignableFrom(clazz)) {
                return readStringRing(element, clazz, reader);
           }
        if (DirectSumModule.class.isAssignableFrom(clazz)) {
                return readDirectSumModule(element, clazz, reader);
           }
        if (ProductProperFreeModule.class.isAssignableFrom(clazz)) {
                return readProductProperFreeModule(element, clazz, reader);
           }
        if (ProductRing.class.isAssignableFrom(clazz)) {
                return readProductRing(element, clazz, reader);
           }
        if (RestrictedModule.class.isAssignableFrom(clazz)) {
                return readRestrictedModule(element, clazz, reader);
           }
        if (ModularPolynomialProperFreeModule.class.isAssignableFrom(clazz)) {
                return readModularPolynomialProperFreeModule(element, clazz, reader);
           }
        if (ModularPolynomialRing.class.isAssignableFrom(clazz)) {
                return readModularPolynomialRing(element, clazz, reader);
           }
        if (PolynomialRing.class.isAssignableFrom(clazz)) {
                return readPolynomialRing(element, clazz, reader);
           }
        if (PolynomialProperFreeModule.class.isAssignableFrom(clazz)) {
            return readPolynomialProperFreeModule(element, clazz, reader);
        }
        return null;
    }

    private VectorModule<?> readVectorModule(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz))); //TODO proper check
        if (!element.hasAttribute(DIMENSION_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), DIMENSION_ATTR);
            return null;
        }

        int dimension;
        try {
            dimension = Integer.parseInt(element.getAttribute(DIMENSION_ATTR));
        } catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", DIMENSION_ATTR, getElementTypeName(clazz));
            return null;
        }
        if (dimension < 0) {
            reader.setError("Attribute %%1 of type %%2 must be an integer >= 0.", DIMENSION_ATTR, getElementTypeName(clazz));
            return null;
        }

        int mod;
        try {
            mod = element.hasAttribute(MODULUS_ATTR) ? Integer.parseInt(element.getAttribute(MODULUS_ATTR)) : 0;
            if (mod != 0 && mod < 2) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer > 1.", MODULUS_ATTR, getElementTypeName(clazz));
            return null;
        }

        return new VectorModule<>(retrieveRing(element.getAttribute(TYPE_ATTR), mod), dimension);
    }

    private static ArithmeticRing<?> retrieveRing(String moduleName, int mod) {
        return null; //TODO
    }

    private Module readCRing(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        return CRing.ring;
    }

    private Module readZRing(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        return ZRing.ring;
    }

    private Module readZnRing(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (element.hasAttribute(MODULUS_ATTR)) {
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

            return ZnRing.make(mod);
        }
        else {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), MODULUS_ATTR);
            return null;
        }
    }

    private Module readQRing(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        return QRing.ring;
    }

    private Module readRRing(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        return RRing.ring;
    }

    private Module readArithmeticStringMultiModule(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));

        if (!element.hasAttribute(DIMENSION_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), DIMENSION_ATTR);
            return null;
        }

        int dimension;
        try {
            dimension = Integer.parseInt(element.getAttribute(DIMENSION_ATTR));
            if (dimension < 0) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer >= 0.", DIMENSION_ATTR, getElementTypeName(clazz));
            return null;
        }

        Integer modulus = element.hasAttribute(MODULUS_ATTR) ? XMLReader.getIntAttribute(element, MODULUS_ATTR, 2, Integer.MAX_VALUE, 2) : null;
        ArithmeticRing<?> arithmeticRing = detectRing(element);
        return makeArithmeticMultiModule(arithmeticRing, dimension);
    }

    private ArithmeticRing<?> detectRing(Element element) {
        return null; //TODO
    }

    private <N extends ArithmeticNumber<N>> Module makeArithmeticMultiModule(ArithmeticRing<N> arithmeticRing, int dimension) {
        return new StringVectorModule(new StringRing(arithmeticRing), dimension);
    }

    private Module readStringRing(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        return new StringRing(detectRing(element));
    }

    private Module readDirectSumModule(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            LinkedList<Module> elements = new LinkedList<>();
            Module module = reader.parseModule(childElement);
            if (module == null) {
                return null;
            }
            elements.add(module);
            Element next = XMLReader.getNextSibling(childElement, MODULE);
            while (next != null) {
                module = reader.parseModule(next);
                if (module == null) {
                    return null;
                }
                elements.add(module);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            Module[] coefficients = new Module[elements.size()];
            int i = 0;
            for (Module m : elements) {
                coefficients[i++] = m;
            }
            try {
                return DirectSumModule.make(coefficients);
            }
            catch (IllegalArgumentException e) {
                reader.setError(e.getMessage());
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private Module readProductProperFreeModule(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));

        if (!element.hasAttribute(DIMENSION_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), DIMENSION_ATTR);
            return null;
        }

        int dimension;
        try {
            dimension = Integer.parseInt(element.getAttribute(DIMENSION_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", DIMENSION_ATTR, getElementTypeName(clazz));
            return null;
        }
        if (dimension < 0) {
            reader.setError("Attribute %%1 of type %%2 must be an integer >= 0.", DIMENSION_ATTR, getElementTypeName(clazz));
            return null;
        }

        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement == null) {
            reader.setError("Type %%1 must have a child of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
        Module module = reader.parseModule(childElement);
        if (module == null || !(module instanceof ProductRing)) {
            reader.setError("Module in %%1 must be a product ring.", getElementTypeName(clazz));
            return null;
        }
        ProductRing productRing = (ProductRing)module;

        return ProductProperFreeModule.make(productRing, dimension);
    }

    private Module readProductRing(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement == null) {
            reader.setError("Missing element <%1> in type %%2.", MODULE, getElementTypeName(clazz));
        }
        LinkedList<Ring> factors0 = new LinkedList<>();
        while (childElement != null) {
            Module module = reader.parseModule(childElement);
            if (!(module instanceof Ring)) {
                reader.setError("Module in %%1 must be a ring.", getElementTypeName(clazz));
                return null;
            }
            factors0.add((Ring)module);
            childElement = XMLReader.getNextSibling(childElement, MODULE);
        }
        if (factors0.size() < 2) {
            reader.setError("There must be at least 2 elements <%1> in type %%2.", MODULE, getElementTypeName(clazz));
            return null;
        }
        else {
            return ProductRing.make(factors0);
        }
    }

    private Module readRestrictedModule(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_MORPHISM);
        if (childElement != null) {
            ModuleMorphism morphism0 = reader.parseModuleMorphism(childElement);
            if (morphism0 == null) {
                return null;
            }
            childElement = XMLReader.getNextSibling(childElement, MODULE);
            if (childElement != null) {
                Module module0 = reader.parseModule(childElement);
                if (module0 == null) {
                    return null;
                }
                RestrictedModule rmodule = null;
                try {
                    rmodule = RestrictedModule.make(morphism0, module0);
                }
                catch (DomainException e) {
                    reader.setError(e);
                }
                return rmodule;

            }
            else {
                reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE);
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
            return null;
        }
    }

    private Module readModularPolynomialProperFreeModule(Element element, Class<?> clazz, XMLReader reader) {
        int dimension = XMLReader.getIntAttribute(element, DIMENSION_ATTR, 0, Integer.MAX_VALUE, 0);
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            ModuleElement me = reader.parseModuleElement(childElement);
            if (me == null) {
                reader.setError("Type %%1 must have a child of type %%2.", getElementTypeName(clazz), "PolynomialElement");
                return null;
            }
            if (!(me instanceof PolynomialElement)) {
                reader.setError("Type %%1 must have a child of type %%2.", getElementTypeName(clazz), "PolynomialElement");
                return null;
            }
            PolynomialElement pe = (PolynomialElement)me;
            FreeModule<?, ? extends ModularPolynomialElement<?>> mpfm = ModularPolynomialProperFreeModule.make(pe, dimension);
            return mpfm;
        }
        else {
            reader.setError("Type %%1 must have a child of type %%2.", getElementTypeName(clazz), "Ring");
            return null;
        }
    }

    private Module readModularPolynomialRing(Element element, Class<?> clazz, XMLReader reader) {
        element = XMLReader.getChild(element, MODULE_ELEMENT);
        if (element != null) {
            ModuleElement m = reader.parseModuleElement(element);
            if (m instanceof PolynomialElement) {
                return ModularPolynomialRing.make((PolynomialElement)m);
            }
        }
        reader.setError("Type %%1 is missing child of type %%2.", getElementTypeName(clazz), "PolynomialElement");
        return null;
    }

    private Module readPolynomialRing(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(INDETERMINATE_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), INDETERMINATE_ATTR);
            return null;
        }
        String indeterminate0 = element.getAttribute(INDETERMINATE_ATTR);
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            Module module = reader.parseModule(childElement);
            if (module == null) {
                return null;
            }
            if (!(module instanceof Ring)) {
                reader.setError("Type %%1 must have children of type %%2.", getElementTypeName(clazz), "Ring");
                return null;
            }
            Ring ring = (Ring)module;
            return PolynomialRing.make(ring, indeterminate0);
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }

    private Module readPolynomialProperFreeModule(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!(element.hasAttribute("indeterminate"))) {
            reader.setError("Type %%1 must have attribute %%2.", getElementTypeName(clazz), INDETERMINATE_ATTR);
        }
        String indeterminate = element.getAttribute(INDETERMINATE_ATTR);
        int dimension = XMLReader.getIntAttribute(element, DIMENSION_ATTR, 0, Integer.MAX_VALUE, 0);
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            Module module = reader.parseModule(childElement);
            if (module == null) {
                return null;
            }
            if (!(module instanceof Ring)) {
                reader.setError("Type %%1 must have a child of type %%2.", getElementTypeName(clazz), "Ring");
                return null;
            }
            Ring rng = (Ring)module;
            return PolynomialProperFreeModule.make(rng, indeterminate, dimension);
        }
        else {
            reader.setError("Type %%1 must have a child of type %%2.", getElementTypeName(clazz), "Ring");
            return null;
        }
    }
    
    //TODO name repository as some subclasses are serialized as superclasses
    private String getElementTypeName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

}
