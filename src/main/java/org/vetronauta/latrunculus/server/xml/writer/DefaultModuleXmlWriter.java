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
import org.vetronauta.latrunculus.core.math.module.complex.CProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ProductProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedModule;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialFreeModule;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialRing;
import org.vetronauta.latrunculus.core.math.module.rational.QProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;
import org.vetronauta.latrunculus.core.math.module.rational.QStringProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.rational.QStringRing;
import org.vetronauta.latrunculus.core.math.module.real.RProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.real.RRing;
import org.vetronauta.latrunculus.core.math.module.real.RStringProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.real.RStringRing;
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

    private final DefinitionXmlWriter definitionXmlWriter;

    @Override
    public void toXML(Module object, XMLWriter writer) {
        if (object instanceof CProperFreeModule) {
            write((CProperFreeModule) object, writer);
            return;
        }
        if (object instanceof ZProperFreeModule) {
            write((ZProperFreeModule) object, writer);
            return;
        }
        if (object instanceof ZnProperFreeModule) {
            write((ZnProperFreeModule) object, writer);
            return;
        }
        if (object instanceof QProperFreeModule) {
            write((QProperFreeModule) object, writer);
            return;
        }
        if (object instanceof RProperFreeModule) {
            write((RProperFreeModule) object, writer);
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
        if (object instanceof ZStringProperFreeModule) {
            write((ZStringProperFreeModule) object, writer);
            return;
        }
        if (object instanceof ZnStringProperFreeModule) {
            write((ZnStringProperFreeModule) object, writer);
            return;
        }
        if (object instanceof QStringProperFreeModule) {
            write((QStringProperFreeModule) object, writer);
            return;
        }
        if (object instanceof RStringProperFreeModule) {
            write((RStringProperFreeModule) object, writer);
            return;
        }
        if (object instanceof ZStringRing) {
            write((ZStringRing) object, writer);
            return;
        }
        if (object instanceof ZnStringRing) {
            write((ZnStringRing) object, writer);
            return;
        }
        if (object instanceof QStringRing) {
            write((QStringRing) object, writer);
            return;
        }
        if (object instanceof RStringRing) {
            write((RStringRing) object, writer);
            return;
        }
        if (object instanceof DirectSumModule) {
            write((DirectSumModule) object, writer);
            return;
        }
        if (object instanceof ProductProperFreeModule) {
            write((ProductProperFreeModule) object, writer);
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
        if (object instanceof ModularPolynomialProperFreeModule) {
            write((ModularPolynomialProperFreeModule) object, writer);
            return;
        }
        if (object instanceof ModularPolynomialRing) {
            write((ModularPolynomialRing) object, writer);
            return;
        }
        if (object instanceof PolynomialFreeModule) {
            write((PolynomialFreeModule) object, writer);
            return;
        }
        if (object instanceof PolynomialRing) {
            //TODO why this should be false?
            write((PolynomialRing) object, writer);
        }
    }

    private void write(CProperFreeModule module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension());
    }

    private void write(ZProperFreeModule module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension());
    }

    private void write(ZnProperFreeModule module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension(), MODULUS_ATTR, module.getModulus());
    }

    private void write(QProperFreeModule module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension());
    }

    private void write(RProperFreeModule module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension());
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

    private void write(ZStringProperFreeModule module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension());
    }

    private void write(ZnStringProperFreeModule module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension(), MODULUS_ATTR, module.getModulus());
    }

    private void write(QStringProperFreeModule module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension());
    }

    private void write(RStringProperFreeModule module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension());
    }

    private void write(ZStringRing module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName());
    }

    private void write(ZnStringRing module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName(), MODULUS_ATTR, module.getModulus());
    }

    private void write(QStringRing module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName());
    }

    private void write(RStringRing module, XMLWriter writer) {
        writer.emptyWithType(MODULE, module.getElementTypeName());
    }

    private void write(DirectSumModule module, XMLWriter writer) {
        writer.openBlockWithType(MODULE, module.getElementTypeName());
        for (int i = 0; i < module.getDimension(); i++) {
            definitionXmlWriter.toXML(module.getComponentModule(i), writer);
        }
        writer.closeBlock();
    }

    private void write(ProductProperFreeModule module, XMLWriter writer) {
        writer.openBlockWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension());
        definitionXmlWriter.toXML(module.getRing(), writer);
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

    private void write(ModularPolynomialProperFreeModule module, XMLWriter writer) {
        writer.openBlockWithType(MODULE, module.getElementTypeName(), DIMENSION_ATTR, module.getDimension());
        definitionXmlWriter.toXML(module.getRing().getModulus(), writer);
        writer.closeBlock();
    }

    private void write(PolynomialFreeModule module, XMLWriter writer) {
        writer.openBlockWithType(MODULE, module.getElementTypeName(), INDETERMINATE_ATTR, module.getIndeterminate(), DIMENSION_ATTR, module.getDimension());
        definitionXmlWriter.toXML(module.getCoefficientRing(), writer);
        writer.closeBlock();
    }

    private void write(PolynomialRing module, XMLWriter writer) {
        writer.openBlockWithType(MODULE, module.getElementTypeName(), INDETERMINATE_ATTR, module.getIndeterminate());
        definitionXmlWriter.toXML(module.getCoefficientRing(), writer);
        writer.closeBlock();
    }

}
