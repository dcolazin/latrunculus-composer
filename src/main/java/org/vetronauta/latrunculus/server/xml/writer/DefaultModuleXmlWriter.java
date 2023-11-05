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
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedModule;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialRing;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.DIMENSION_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.INDETERMINATE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULUS_ATTR;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class DefaultModuleXmlWriter implements LatrunculusXmlWriter<Module> {

    private final LatrunculusXmlWriter<MathDefinition> definitionXmlWriter;

    @Override
    public void toXML(Module object, XMLWriter writer) {
        if (object instanceof VectorModule) {
            write((VectorModule<?>) object, writer);
            return;
        }
        if (object instanceof CRing) {
            write((CRing) object, writer);
            return;
        }
        if (object instanceof ZRing) {
            write((ZRing) object, writer);
            return;
        }
        if (object instanceof ZnRing) {
            write((ZnRing) object, writer);
            return;
        }
        if (object instanceof QRing) {
            write((QRing) object, writer);
            return;
        }
        if (object instanceof RRing) {
            write((RRing) object, writer);
            return;
        }
        if (object instanceof StringRing) {
            write((StringRing) object, writer);
            return;
        }
        if (object instanceof DirectSumModule) {
            write((DirectSumModule) object, writer);
            return;
        }
        if (object instanceof ProductRing) {
            write((ProductRing) object, writer);
            return;
        }
        if (object instanceof RestrictedModule) {
            write((RestrictedModule) object, writer);
            return;
        }
        if (object instanceof ModularPolynomialRing) {
            write((ModularPolynomialRing) object, writer);
            return;
        }
        if (object instanceof PolynomialRing) {
            write((PolynomialRing) object, writer);
        }
    }

    private void write(VectorModule<?> module, XMLWriter writer) {
        if (module.getRing().getClass().isAssignableFrom(ZnRing.class)) {
            writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension(), MODULUS_ATTR, ((Modulus)module.getRing().getOne()).getModulus()); //TODO ugly way to get the modulus
        } else {
            writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension());
        }
    }

    private void write(CRing module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName());
    }

    private void write(ZRing module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName());
    }

    private void write(ZnRing module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), MODULUS_ATTR, module.getModulus());
    }

    private void write(QRing module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName());
    }

    private void write(RRing module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName());
    }

    private void write(StringRing module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName());
    }

    private void write(DirectSumModule module, XMLWriter writer) {
        writer.openBlockWithType(MODULE, module.getElementTypeName());
        for (int i = 0; i < module.getDimension(); i++) {
            definitionXmlWriter.toXML(module.getComponentModule(i), writer);
        }
        writer.closeBlock();
    }

    private void write(ProductRing module, XMLWriter writer) {
        writer.openBlockWithType(MODULE, module.getElementTypeName());
        for (int i = 0; i < module.getFactorCount(); i++) {
            toXML(module.getFactor(i), writer);
        }
        writer.closeBlock();
    }

    private void write(RestrictedModule module, XMLWriter writer) {
        writer.openBlockWithType(MODULE, module.getElementTypeName());
        definitionXmlWriter.toXML(module.getRestrictingMorphism(), writer);
        definitionXmlWriter.toXML(module.getUnrestrictedModule(), writer);
        writer.closeBlock();
    }

    private void write(ModularPolynomialRing module, XMLWriter writer) {
        writer.openBlockWithType(MODULE, module.getElementTypeName());
        definitionXmlWriter.toXML(module.getModulus(), writer);
        writer.closeBlock();
    }

    private void write(PolynomialRing module, XMLWriter writer) {
        writer.openBlockWithType(MODULE, module.getElementTypeName(), INDETERMINATE_ATTR, module.getIndeterminate());
        definitionXmlWriter.toXML(module.getCoefficientRing(), writer);
        writer.closeBlock();
    }

}
