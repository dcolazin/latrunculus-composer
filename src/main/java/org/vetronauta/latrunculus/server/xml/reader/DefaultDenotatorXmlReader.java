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

import org.vetronauta.latrunculus.core.math.yoneda.ColimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.ColimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.CompoundMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.Form;
import org.vetronauta.latrunculus.core.math.yoneda.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.ListDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.ModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.MorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.PowerForm;
import org.vetronauta.latrunculus.core.math.yoneda.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.SimpleForm;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.LinkedList;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.COLIMIT_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.DENOTATOR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORM_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.INDEX_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LIMIT_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LIST_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MORPHISM_MAP;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.NAME_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.POWER_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.SIMPLE_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

/**
 * @author vetronauta
 */
public class DefaultDenotatorXmlReader implements LatrunculusXmlReader<Denotator> {

    @Override
    public Denotator fromXML(Element element, Class<? extends Denotator> clazz, XMLReader reader) {
        if (SimpleDenotator.class.isAssignableFrom(clazz)) {
            return readSimpleDenotator(element, reader);
        }
        if (LimitDenotator.class.isAssignableFrom(clazz)) {
            return readLimitDenotator(element, reader);
        }
        if (ColimitDenotator.class.isAssignableFrom(clazz)) {
            return readColimitDenotator(element, reader);
        }
        if (ListDenotator.class.isAssignableFrom(clazz)) {
            return readListDenotator(element, reader);
        }
        if (PowerDenotator.class.isAssignableFrom(clazz)) {
            return readPowerDenotator(element, reader);
        }
        return null;
    }

    private static SimpleDenotator readSimpleDenotator(Element element, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(SIMPLE_TYPE_VALUE));

        // read the form
        if (!element.hasAttribute(FORM_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%3.", SIMPLE_TYPE_VALUE, DENOTATOR, FORM_ATTR);
            return null;
        }
        String formName = element.getAttribute(FORM_ATTR);
        Form form = reader.getForm(formName);
        if (form == null) {
            reader.setError("Form with name %%1 does not exist.", formName);
            return null;
        }
        if (!(form instanceof SimpleForm)) {
            reader.setError("Form with name %%1 is not a form of type %%2.", formName, SIMPLE_TYPE_VALUE);
            return null;
        }

        // read the name
        NameDenotator name = null;
        if (element.hasAttribute(NAME_ATTR)) {
            String nameString = element.getAttribute(NAME_ATTR);
            name = NameDenotator.make(nameString);
        }

        // read the coordinate (MorphismMap)
        MorphismMap map;
        CompoundMorphism cm;
        CompoundMorphism fcm;
        Element childElement = XMLReader.getChild(element, MORPHISM_MAP);
        if (childElement != null) {
            map = reader.parseMorphismMap(childElement);

            if (map == null) {
                return null;
            }
            if (map instanceof ModuleMorphismMap) {
                ModuleMorphismMap mm = (ModuleMorphismMap)map;
                fcm = cm = new CompoundMorphism(mm.getDomain(), mm.getCodomain(), mm);
            }
            else {
                reader.setError("Morphism map in a simple denotator must be a module morphism map.");
                return null;
            }

            // read the frame coordinate, if present
            childElement = XMLReader.getChild(childElement, MORPHISM_MAP);
            if (childElement != null) {
                map = reader.parseMorphismMap(childElement);

                if (map == null) {
                    return null;
                }
                if (map instanceof ModuleMorphismMap) {
                    ModuleMorphismMap mm = (ModuleMorphismMap)map;
                    fcm = new CompoundMorphism(mm.getDomain(), mm.getCodomain(), mm);
                }
                else {
                    reader.setError("Morphism map in a simple denotator must be a module morphism map.");
                    return null;
                }
            }

            return SimpleDenotator._make_unsafe(name, (SimpleForm) form, cm, fcm); //TODO find a better way
        }
        else {
            reader.setError("Missing element <%1>.", MORPHISM_MAP);
            return null;
        }
    }

    private static LimitDenotator readLimitDenotator(Element element, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(LIMIT_TYPE_VALUE));

        if (!element.hasAttribute(FORM_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%3.", LIMIT_TYPE_VALUE, DENOTATOR, FORM_ATTR);
            return null;
        }
        String formName = element.getAttribute(FORM_ATTR);
        Form form = reader.getForm(formName);
        if (form == null) {
            reader.setError("Form with name %%1 does not exist.", formName);
            return null;
        }
        if (!(form instanceof LimitForm)) {
            reader.setError("Form with name %%1 is not a form of type %%2.", formName, LIMIT_TYPE_VALUE);
            return null;
        }
        LimitForm limitForm = (LimitForm)form;

        NameDenotator name = null;
        if (element.hasAttribute(NAME_ATTR)) {
            String nameString = element.getAttribute(NAME_ATTR);
            name = NameDenotator.make(nameString);
        }

        Element childElement = XMLReader.getChild(element, DENOTATOR);
        if (childElement != null) {
            LinkedList<Denotator> factorList = new LinkedList<Denotator>();
            while (childElement != null) {
                Denotator denotator = reader.parseDenotator(childElement);
                if (denotator == null) {
                    return null;
                }
                else {
                    factorList.add(denotator);
                }
                childElement = XMLReader.getNextSibling(childElement, DENOTATOR);
            }
            try {
                LimitDenotator denotator = new LimitDenotator(name, limitForm, factorList);
                return denotator;
            }
            catch (Exception e) {
                reader.setError(e.getMessage());
                return null;
            }
        }
        else {
            reader.setError("Denotator type %%1 is missing child element <%2>.", LIMIT_TYPE_VALUE, DENOTATOR);
            return null;
        }
    }

    private static ColimitDenotator readColimitDenotator(Element element, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(COLIMIT_TYPE_VALUE));

        if (!element.hasAttribute(FORM_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%3", COLIMIT_TYPE_VALUE, DENOTATOR, FORM_ATTR);
            return null;
        }
        String formName = element.getAttribute(FORM_ATTR);
        Form form = reader.getForm(formName);
        if (form == null) {
            reader.setError("Form with name %%1 does not exist", formName);
            return null;
        }
        if (!(form instanceof ColimitForm)) {
            reader.setError("Form with name %%1 is not a form of type %%2", formName, COLIMIT_TYPE_VALUE);
            return null;
        }
        ColimitForm colimitForm = (ColimitForm)form;

        int index;
        if (!element.hasAttribute(INDEX_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%3", COLIMIT_TYPE_VALUE, DENOTATOR, INDEX_ATTR);
            return null;
        }
        try {
            index = Integer.parseInt(element.getAttribute(INDEX_ATTR));
            if (index < 0) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of element <%2> must be an integer >= 0", INDEX_ATTR, DENOTATOR);
            return null;
        }

        NameDenotator name = null;
        if (element.hasAttribute(NAME_ATTR)) {
            String nameString = element.getAttribute(NAME_ATTR);
            name = NameDenotator.make(nameString);
        }

        Element childElement = XMLReader.getChild(element, DENOTATOR);
        if (childElement != null) {
            Denotator denotator = reader.parseDenotator(childElement);
            if (denotator == null) {
                return null;
            }
            try {
                return new ColimitDenotator(name, colimitForm, index, denotator);
            }
            catch (Exception e) {
                reader.setError(e.getMessage());
                return null;
            }
        }
        else {
            reader.setError("Denotator type %%1 is missing child element <%2>", COLIMIT_TYPE_VALUE, DENOTATOR);
            return null;
        }
    }

    private static ListDenotator readListDenotator(Element element, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(LIST_TYPE_VALUE));

        if (!element.hasAttribute(FORM_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%3.", LIST_TYPE_VALUE, DENOTATOR, FORM_ATTR);
            return null;
        }
        String formName = element.getAttribute(FORM_ATTR);
        Form form = reader.getForm(formName);
        if (form == null) {
            reader.setError("Form with name %%1 does not exist.", formName);
            return null;
        }
        if (!(form instanceof ListForm)) {
            reader.setError("Form with name %%1 is not a form of type %%2.", formName, LIST_TYPE_VALUE);
            return null;
        }
        ListForm listForm = (ListForm)form;

        NameDenotator name = null;
        if (element.hasAttribute(NAME_ATTR)) {
            String nameString = element.getAttribute(NAME_ATTR);
            name = NameDenotator.make(nameString);
        }

        LinkedList<Denotator> factorList = new LinkedList<Denotator>();
        Element childElement = XMLReader.getChild(element, DENOTATOR);

        while (childElement != null) {
            Denotator denotator = reader.parseDenotator(childElement);
            if (denotator == null) {
                return null;
            }
            factorList.add(denotator);
            childElement = XMLReader.getNextSibling(childElement, DENOTATOR);
        }

        try {
            ListDenotator denotator = new ListDenotator(name, listForm, factorList);
            return denotator;
        }
        catch (Exception e) {
            reader.setError(e.getMessage());
            return null;
        }
    }

    private static PowerDenotator readPowerDenotator(Element element, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(POWER_TYPE_VALUE));

        if (!element.hasAttribute(FORM_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%2.", POWER_TYPE_VALUE, DENOTATOR, FORM_ATTR);
            return null;
        }
        String formName = element.getAttribute(FORM_ATTR);
        Form form = reader.getForm(formName);
        if (form == null) {
            reader.setError("Form with name %%1 does not exist.", formName);
            return null;
        }
        if (!(form instanceof PowerForm)) {
            reader.setError("Form with name %%1 is not a form of type %%2.", formName, POWER_TYPE_VALUE);
            return null;
        }
        PowerForm powerForm = (PowerForm)form;

        NameDenotator name = null;
        if (element.hasAttribute(NAME_ATTR)) {
            String nameString = element.getAttribute(NAME_ATTR);
            name = NameDenotator.make(nameString);
        }

        LinkedList<Denotator> factorList = new LinkedList<Denotator>();
        Element childElement = XMLReader.getChild(element, DENOTATOR);

        while (childElement != null) {
            Denotator denotator = reader.parseDenotator(childElement);
            if (denotator == null) {
                return null;
            }
            else {
                factorList.add(denotator);
            }
            childElement = XMLReader.getNextSibling(childElement, DENOTATOR);
        }

        try {
            PowerDenotator denotator = new PowerDenotator(name, powerForm, factorList);
            return denotator;
        }
        catch (Exception e) {
            reader.setError(e.getMessage());
            return null;
        }
    }

}
