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

package org.vetronauta.latrunculus.server.xml.writer;

import lombok.RequiredArgsConstructor;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.element.generic.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.ProductElement;
import org.vetronauta.latrunculus.core.math.element.generic.RestrictedElement;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.element.generic.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.element.generic.PolynomialElement;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import java.util.List;
import java.util.Map;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.FACTOR_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.INDETERMINATE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_ELEMENT;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULUS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUES_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.WORD;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class DefaultModuleElementXmlWriter implements LatrunculusXmlWriter<ModuleElement> {

    //TODO extract common logic

    private final LatrunculusXmlWriter<MathDefinition> definitionWriter;

    public void toXML(Module module, XMLWriter writer) {
        definitionWriter.toXML(module, writer);
    }

    @Override
    public void toXML(ModuleElement object, XMLWriter writer) {
        if (object instanceof Vector) {
            write((Vector<?>) object, writer);
            return;
        }
        if (object instanceof StringMap) {
            write((StringMap<?>) object, writer);
            return;
        }
        if (object instanceof DirectSumElement) {
            write((DirectSumElement) object, writer);
            return;
        }
        if (object instanceof ProductElement) {
            write((ProductElement) object, writer);
            return;
        }
        if (object instanceof PolynomialElement) {
            write((PolynomialElement) object, writer);
            return;
        }
        if (object instanceof ModularPolynomialElement) {
            write((ModularPolynomialElement) object, writer);
            return;
        }
        if (object instanceof RestrictedElement) {
            write((RestrictedElement) object, writer);
        }
        if (object instanceof RingElement) {
            writeRingElement((RingElement<?>) object, writer);
            return;
        }
    }

    private void writeRingElement(RingElement<?> element, XMLWriter writer) {
        if (element instanceof Modulus) {
            writer.emptyWithType(MODULE_ELEMENT, element.toString(), VALUE_ATTR, element.toString(), MODULUS_ATTR, ((Modulus) element).getModulus());
        } else {
            writer.emptyWithType(MODULE_ELEMENT, element.toString(), VALUE_ATTR, element.toString());
        }
    }

    private void write(Vector<?> element, XMLWriter writer) {
        StringBuilder s = new StringBuilder();
        List<? extends RingElement<?>> value = element.getValue();
        if (!value.isEmpty()) {
            s.append(value.get(0));
            for (int i = 1; i < value.size(); i++) {
                s.append(",").append(value.get(i));
            }
        }
        if (element.getRing() instanceof ZnRing) {
            writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), MODULUS_ATTR, ((ZnRing) element.getRing()).getModulus(), VALUES_ATTR, s);

        } else {
            writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), VALUES_ATTR, s.toString());
        }
    }

    private void write(StringMap<?> element, XMLWriter writer) {
        if (element.getRing().getFactorRing() instanceof ZnRing) {
            writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName(), MODULUS_ATTR, ((ZnRing) element.getRing().getFactorRing()).getModulus());
        } else {
            writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        }
        for (Map.Entry<String,?> entry : element.getTerms().entrySet()) {
            writer.openInline(WORD, FACTOR_ATTR, entry.getValue());
            writer.text(entry.getKey());
            writer.closeInline();
        }
        writer.closeBlock();
    }

    private void write(DirectSumElement<?> element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (ModuleElement<?,?> internalElement :  element.getComponents()) {
            definitionWriter.toXML(internalElement, writer);
        }
        writer.closeBlock();
    }

    private void write(ProductElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (int i = 0; i < element.getFactorCount(); i++) {
            definitionWriter.toXML(element.getValue(i), writer);
        }
        writer.closeBlock();
    }

    private void write(PolynomialElement<?> element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName(),
                INDETERMINATE_ATTR, element.getRing().getIndeterminate());
        for (int i = 0; i < element.getCoefficients().size(); i++) {
            definitionWriter.toXML(element.getCoefficients().get(i), writer);
        }
        writer.closeBlock();
    }

    private void write(ModularPolynomialElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        definitionWriter.toXML(element.getRing(), writer);
        write(element.getPolynomial(), writer);
        writer.closeBlock();
    }

    private void write(RestrictedElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        definitionWriter.toXML(element.getModule(), writer);
        definitionWriter.toXML(element.getModuleElement(), writer);
        writer.closeBlock();
    }
}
