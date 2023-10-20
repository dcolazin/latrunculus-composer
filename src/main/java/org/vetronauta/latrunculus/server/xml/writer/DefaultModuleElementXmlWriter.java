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
import org.rubato.util.Base64;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.complex.CProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.rational.QProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.rational.QStringElement;
import org.vetronauta.latrunculus.core.math.module.rational.QStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.real.RProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.real.RStringElement;
import org.vetronauta.latrunculus.core.math.module.real.RStringProperFreeElement;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import java.util.List;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.BASE64;
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
        if (object instanceof ZnElement) {
            write((ZnElement) object, writer);
            return;
        }
        if (object instanceof ArithmeticElement) {
            write((ArithmeticElement<?>) object, writer);
            return;
        }
        if (object instanceof CProperFreeElement) {
            write((CProperFreeElement) object, writer);
            return;
        }
        if (object instanceof ZProperFreeElement) {
            write((ZProperFreeElement) object, writer);
            return;
        }
        if (object instanceof ZnProperFreeElement) {
            write((ZnProperFreeElement) object, writer);
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
        if (object instanceof QProperFreeElement) {
            write((QProperFreeElement) object, writer);
            return;
        }
        if (object instanceof RProperFreeElement) {
            write((RProperFreeElement) object, writer);
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
        writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), VALUE_ATTR, element.getValue().toString());
    }
    private void write(ZnElement element, XMLWriter writer) {
        writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), VALUE_ATTR, element.getValue(), MODULUS_ATTR, element.getModulus());
    }

    private void write(CProperFreeElement element, XMLWriter writer) {
        String s = "";
        List<ArithmeticElement<Complex>> value = element.getValue();
        if (value.size() > 0) {
            s += value.get(0);
            for (int i = 1; i < value.size(); i++) {
                s += ","+value.get(i);
            }
        }
        writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), VALUES_ATTR, s);
    }

    private void write(ZProperFreeElement element, XMLWriter writer) {
        if (element.getValue().size() <= 16) {
            String s = "";
            if (element.getValue().size() > 0) {
                s += element.getValue().get(0);
                for (int i = 1; i < element.getValue().size(); i++) {
                    s += ","+element.getValue().get(i);
                }
            }
            writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), VALUES_ATTR, s);
        }
        else {
            writer.openBlockWithType(MODULE_ELEMENT, element.getElementTypeName());
            writer.openBlock(BASE64);
            writer.writeTextNode("\n");
            writer.writeTextNode(Base64.encodeIntArray(element.getValue()));
            writer.writeTextNode("\n");
            writer.closeBlock();
            writer.closeBlock();
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

    private void write(QProperFreeElement element, XMLWriter writer) {
        String s = "";
        if (element.getValue().size() > 0) {
            s += element.getValue().get(0);
            for (int i = 1; i < element.getValue().size(); i++) {
                s += ","+element.getValue().get(i);
            }
        }
        writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), VALUES_ATTR, s);
    }

    private void write(RProperFreeElement element, XMLWriter writer) {
        String s = "";
        if (element.getValue().size() > 0) {
            s += element.getValue().get(0);
            for (int i = 1; i < element.getValue().size(); i++) {
                s += ","+element.getValue().get(i);
            }
        }
        writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(), VALUES_ATTR, s);
    }

    private void write(ZnProperFreeElement element, XMLWriter writer) {
        String s = "";
        if (element.getValue().size() > 0) {
            s += element.getValue().get(0);
            for (int i = 1; i < element.getValue().size(); i++) {
                s += ","+element.getValue().get(i);
            }
        }
        s += "\"";
        writer.emptyWithType(MODULE_ELEMENT, element.getElementTypeName(),
                MODULUS_ATTR, element.getModulus(),
                VALUES_ATTR, s);
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
