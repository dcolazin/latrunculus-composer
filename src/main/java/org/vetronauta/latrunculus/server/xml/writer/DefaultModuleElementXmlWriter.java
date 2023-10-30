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
import org.vetronauta.latrunculus.core.math.arith.number.Modulus;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialProperFreeElement;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import java.util.List;

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
        if (object instanceof ArithmeticElement) {
            write((ArithmeticElement<?>) object, writer);
            return;
        }
        if (object instanceof ArithmeticMultiElement) {
            write((ArithmeticMultiElement<?>) object, writer);
            return;
        }
        if (object instanceof ArithmeticStringMultiElement) {
            write((ArithmeticStringMultiElement<?>) object, writer);
            return;
        }
        if (object instanceof ArithmeticStringElement) {
            write((ArithmeticStringElement<?>) object, writer);
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
        if (object instanceof ProductProperFreeElement) {
            write((ProductProperFreeElement) object, writer);
            return;
        }
        if (object instanceof PolynomialElement) {
            write((PolynomialElement) object, writer);
            return;
        }
        if (object instanceof PolynomialProperFreeElement) {
            write((PolynomialProperFreeElement) object, writer);
            return;
        }
        if (object instanceof ModularPolynomialElement) {
            write((ModularPolynomialElement) object, writer);
            return;
        }
        if (object instanceof ModularPolynomialProperFreeElement) {
            write((ModularPolynomialProperFreeElement) object, writer);
            return;
        }
        if (object instanceof RestrictedElement) {
            write((RestrictedElement) object, writer);
        }
    }

    private void write(ArithmeticElement<?> element, XMLWriter writer) {
        ArithmeticNumber<?> number = element.getValue();
        if (number instanceof Modulus) {
            writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), VALUE_ATTR, number, MODULUS_ATTR, ((Modulus) number).getModulus());
        } else {
            writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), VALUE_ATTR, number);
        }
    }

    private void write(ArithmeticMultiElement<?> element, XMLWriter writer) {
        StringBuilder s = new StringBuilder();
        List<? extends ArithmeticElement<?>> value = element.getValue();
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

    private void write(ArithmeticStringElement<?> element, XMLWriter writer) {
        if (element.getRing().getFactorRing().getZero().getValue() instanceof Modulus) {
            writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName(), MODULUS_ATTR, ((Modulus) element.getRing().getFactorRing().getZero().getValue()).getModulus());
        } else {
            writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        }
        for (String word : element.getValue().getStrings()) {
            writer.openInline(WORD, FACTOR_ATTR, element.getValue().getFactorForString(word));
            writer.text(word);
            writer.closeInline();
        }
        writer.closeBlock();
    }

    private void write(ArithmeticStringMultiElement<?> element, XMLWriter writer) {
        if (element.getModule().getRing().getFactorRing().getZero().getValue() instanceof Modulus) {
            writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName(), MODULUS_ATTR, ((Modulus) element.getModule().getRing().getFactorRing().getZero().getValue()).getModulus());
        } else {
            writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        }
        for (int i = 0; i < element.getValue().size(); i++) {
            write(new ArithmeticStringElement(element.getModule().getRing(), element.getValue().get(i)), writer);
        }
        writer.closeBlock();
    }

    private void write(DirectSumElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (int i = 0; i < element.getComponents().length; i++) {
            definitionWriter.toXML(element.getComponent(i), writer);
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

    private void write(ProductProperFreeElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        definitionWriter.toXML(element.getRing(), writer);
        for (int i = 1; i < element.getLength(); i++) {
            definitionWriter.toXML(element.getComponent(i), writer);
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

    private void write(PolynomialProperFreeElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (int i = 0; i < element.getValue().length; i++) {
            write(element.getValue(i), writer);
        }
        writer.closeBlock();
    }

    private void write(ModularPolynomialElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        definitionWriter.toXML(element.getRing(), writer);
        write(element.getPolynomial(), writer);
        writer.closeBlock();
    }

    private void write(ModularPolynomialProperFreeElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (int i = 0; i < element.getValue().length; i++) {
            write(element.getValue(i), writer);
        }
        writer.closeBlock();
    }

    private void write(RestrictedElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        definitionWriter.toXML(element.getModule(), writer);
        definitionWriter.toXML(element.getModuleElement(), writer);
        writer.closeBlock();
    }
}
