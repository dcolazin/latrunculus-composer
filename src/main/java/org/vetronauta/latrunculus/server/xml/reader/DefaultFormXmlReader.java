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

import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.yoneda.form.ColimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.FormReference;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.LinkedList;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.COLIMIT_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.HIVALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LABEL;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LABELS;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LIMIT_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LIST_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LOVALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_ELEMENT;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.NAME_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.POS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.POWER_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.SIMPLE_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

/**
 * @author vetronauta
 */
public class DefaultFormXmlReader implements LatrunculusXmlReader<Form> {
    
    @Override
    public Form fromXML(Element element, Class<? extends Form> clazz, XMLReader reader) {
        if (SimpleForm.class.isAssignableFrom(clazz)) {
            return readSimpleForm(element, reader);
        }
        if (LimitForm.class.isAssignableFrom(clazz)) {
            return readLimitForm(element, reader);
        }
        if (ColimitForm.class.isAssignableFrom(clazz)) {
            return readColimitForm(element, reader);
        }
        if (ListForm.class.isAssignableFrom(clazz)) {
            return readListForm(element, reader);
        }
        if (PowerForm.class.isAssignableFrom(clazz)) {
            return readPowerForm(element, reader);
        }
        return null;
    }

    private static SimpleForm readSimpleForm(Element element, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(SIMPLE_TYPE_VALUE));
        if (!element.hasAttribute(NAME_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%3.", SIMPLE_TYPE_VALUE, FORM, NAME_ATTR);
            return null;
        }

        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement == null) {
            reader.setError("Type %%1 of element <%2> is missing element <%3>.", SIMPLE_TYPE_VALUE, FORM, MODULE);
            return null;
        }

        Module module = reader.parseModule(childElement);
        if (module == null) {
            return null;
        }

        childElement = XMLReader.getNextSibling(childElement, LOVALUE);
        if (childElement != null) {
            ModuleElement loValue = null;
            ModuleElement hiValue = null;
            Element loValueElement = XMLReader.getChild(childElement, MODULE_ELEMENT);
            if (loValueElement != null) {
                loValue = reader.parseModuleElement(loValueElement);
            }
            Element hiValueElement = XMLReader.getNextSibling(loValueElement, HIVALUE);
            if (hiValueElement != null) {
                hiValue = reader.parseModuleElement(loValueElement);
            }

            if (hiValue != null && loValue != null) {
                return new SimpleForm(NameDenotator.make(element.getAttribute(NAME_ATTR)), module, loValue, hiValue);
            }
            else {
                reader.setError("<%1> and <%2> of element <%3> of type %%4 don't have the right shape.", LOVALUE, HIVALUE, FORM, SIMPLE_TYPE_VALUE);
                return null;
            }
        }

        return new SimpleForm(NameDenotator.make(element.getAttribute(NAME_ATTR)), module);
    }

    private static LimitForm readLimitForm(Element element, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(LIMIT_TYPE_VALUE));
        if (!element.hasAttribute(NAME_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%3.", LIMIT_TYPE_VALUE, FORM, NAME_ATTR);
            return null;
        }

        HashMap<String,Integer> labels = null;
        Element childElement;

        childElement = XMLReader.getChild(element, LABELS);
        if (childElement != null) {
            labels = new HashMap<String,Integer>();
            childElement = XMLReader.getChild(childElement, LABEL);
            while (childElement != null) {
                String label = XMLReader.getStringAttribute(childElement, NAME_ATTR);
                int pos = XMLReader.getIntAttribute(childElement, POS_ATTR, 0);
                labels.put(label, pos);
                childElement = XMLReader.getNextSibling(childElement, LABEL);
            }
        }

        childElement = XMLReader.getChild(element, FORM);
        if (childElement == null) {
            reader.setError("Type %%1 of element <%2> is missing elements of type <%2>.", LIMIT_TYPE_VALUE, FORM);
            return null;
        }

        LinkedList<Form> forms = new LinkedList<Form>();
        boolean references = false;
        while (childElement != null) {
            Form form = reader.parseForm(childElement);
            if (form == null) {
                return null;
            }
            forms.add(form);
            if (form instanceof FormReference) {
                references = true;
            }
            childElement = XMLReader.getNextSibling(childElement, FORM);
        }

        LimitForm limitForm = new LimitForm(NameDenotator.make(element.getAttribute("name")), forms, labels);
        if (references) {
            reader.addFormToBeResolved(limitForm);
        }
        return limitForm;
    }

    private static ColimitForm readColimitForm(Element element, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(COLIMIT_TYPE_VALUE));
        if (!element.hasAttribute(NAME_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%3.", COLIMIT_TYPE_VALUE, FORM, NAME_ATTR);
            return null;
        }

        HashMap<String,Integer> labels = null;
        Element childElement;

        childElement = XMLReader.getChild(element, LABELS);
        if (childElement != null) {
            labels = new HashMap<>();
            childElement = XMLReader.getChild(childElement, LABEL);
            while (childElement != null) {
                String label = XMLReader.getStringAttribute(childElement, NAME_ATTR);
                int pos = XMLReader.getIntAttribute(childElement, POS_ATTR, 0);
                labels.put(label, pos);
                childElement = XMLReader.getNextSibling(childElement, "Label");
            }
        }

        childElement = XMLReader.getChild(element, FORM);
        if (childElement == null) {
            reader.setError("Type %%1 of element <%2> is missing elements of type <%2>.", COLIMIT_TYPE_VALUE, FORM);
            return null;
        }

        LinkedList<Form> forms = new LinkedList<>();
        boolean references = false;
        while (childElement != null) {
            Form form = reader.parseForm(childElement);
            if (form == null) {
                return null;
            }
            forms.add(form);
            if (form instanceof FormReference) {
                references = true;
            }
            childElement = XMLReader.getNextSibling(childElement, FORM);
        }

        ColimitForm colimitForm = new ColimitForm(NameDenotator.make(element.getAttribute(NAME_ATTR)), forms, labels);
        if (references) {
            reader.addFormToBeResolved(colimitForm);
        }
        return colimitForm;
    }

    private static ListForm readListForm(Element element, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(LIST_TYPE_VALUE));
        if (!element.hasAttribute(NAME_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%3.", LIST_TYPE_VALUE, FORM, NAME_ATTR);
            return null;
        }

        Element childElement = XMLReader.getChild(element, FORM);
        if (childElement == null) {
            reader.setError("Type %%1 of element <%2> is missing elements of type <%2>.", LIST_TYPE_VALUE, FORM);
            return null;
        }

        Form form = reader.parseForm(childElement);
        if (form == null) {
            return null;
        }

        ListForm listForm = new ListForm(NameDenotator.make(element.getAttribute("name")), form);
        if (form instanceof FormReference) {
            reader.addFormToBeResolved(listForm);
        }
        return listForm;
    }

    private static PowerForm readPowerForm(Element element, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(POWER_TYPE_VALUE));
        if (!element.hasAttribute(NAME_ATTR)) {
            reader.setError("Type %%1 of element <%2> is missing attribute %%3.", POWER_TYPE_VALUE, FORM, NAME_ATTR);
            return null;
        }

        Element childElement = XMLReader.getChild(element, FORM);
        if (childElement == null) {
            reader.setError("Type %%1 of element <%2> is missing element <%2>.", POWER_TYPE_VALUE, FORM);
            return null;
        }

        Form form = reader.parseForm(childElement);
        if (form == null) {
            return null;
        }

        PowerForm powerForm = new PowerForm(NameDenotator.make(element.getAttribute("name")), form);
        if (form instanceof FormReference) {
            reader.addFormToBeResolved(powerForm);
        }
        return powerForm;
    }

}
