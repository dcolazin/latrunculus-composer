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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.rational.QStringElement;
import org.vetronauta.latrunculus.core.math.module.rational.QStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.real.RStringElement;
import org.vetronauta.latrunculus.core.math.module.real.RStringProperFreeElement;
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
        if (object instanceof ZStringElement) {
            write((ZStringElement) object, writer);
            return;
        }
        if (object instanceof ZnStringElement) {
            write((ZnStringElement) object, writer);
            return;
        }
        if (object instanceof QStringElement) {
            write((QStringElement) object, writer);
            return;
        }
        if (object instanceof RStringElement) {
            write((RStringElement) object, writer);
            return;
        }
        if (object instanceof ZStringProperFreeElement) {
            write((ZStringProperFreeElement) object, writer);
            return;
        }
        if (object instanceof ZnStringProperFreeElement) {
            write((ZnStringProperFreeElement) object, writer);
            return;
        }
        if (object instanceof QStringProperFreeElement) {
            write((QStringProperFreeElement) object, writer);
            return;
        }
        if (object instanceof RStringProperFreeElement) {
            write((RStringProperFreeElement) object, writer);
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
        if (number instanceof ArithmeticModulus) {
            writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), VALUE_ATTR, number, MODULUS_ATTR, ((ArithmeticModulus) number).getModulus());
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

    private void write(ZStringElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (String word : element.getValue().getStrings()) {
            int factor = (Integer) element.getValue().getFactorForString(word);
            writer.openInline(WORD, FACTOR_ATTR, factor);
            writer.text(word);
            writer.closeInline();
        }
        writer.closeBlock();
    }

    private void write(QStringElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (String word : element.getValue().getStrings()) {
            Rational factor = ((Rational)element.getValue().getFactorForString(word));
            writer.openInline(WORD, FACTOR_ATTR, factor);
            writer.text(word);
            writer.closeInline();
        }
        writer.closeBlock();
    }

    private void write(ZnStringElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName(),
                MODULUS_ATTR, element.getModulus());
        for (String word : element.getValue().getStrings()) {
            int factor = (Integer) element.getValue().getFactorForString(word);
            writer.openInline(WORD, FACTOR_ATTR, factor);
            writer.text(word);
            writer.closeInline();
        }
        writer.closeBlock();
    }

    private void write(RStringElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (String word : element.getValue().getStrings()) {
            double factor = (Double) element.getValue().getFactorForString(word);
            writer.openInline(WORD, FACTOR_ATTR, factor);
            writer.text(word);
            writer.closeInline();
        }
        writer.closeBlock();
    }

    private void write(ZStringProperFreeElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (int i = 0; i < element.getValue().size(); i++) {
            write(new ZStringElement(element.getValue().get(i)), writer);
        }
        writer.closeBlock();
    }

    private void write(ZnStringProperFreeElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName(),
                MODULUS_ATTR, element.getModulus());
        for (int i = 0; i < element.getValue().size(); i++) {
            write(new ZnStringElement(element.getValue().get(i)), writer);
        }
        writer.closeBlock();
    }

    private void write(QStringProperFreeElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (int i = 0; i < element.getValue().size(); i++) {
            write(new QStringElement(element.getValue().get(i)), writer);
        }
        writer.closeBlock();
    }

    private void write(RStringProperFreeElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
        for (int i = 0; i < element.getValue().size(); i++) {
            write(new RStringElement(element.getValue().get(i)), writer);
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

    private void write(PolynomialElement element, XMLWriter writer) {
        writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName(),
                INDETERMINATE_ATTR, element.getRing().getIndeterminate());
        for (int i = 0; i < element.getCoefficients().length; i++) {
            definitionWriter.toXML(element.getCoefficients()[i], writer);
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
