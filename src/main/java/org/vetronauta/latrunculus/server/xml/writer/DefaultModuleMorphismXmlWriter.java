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
import org.vetronauta.latrunculus.core.math.module.morphism.CanonicalMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.CastMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.CompositionMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ConjugationMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ConstantMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.DifferenceMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.EmbeddingMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.FoldingMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericBasisMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.IdentityMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuloMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.PolynomialMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.PowerMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ProductMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ProjectionMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ReorderMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ScaledMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.SplitMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.SumMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.TranslationMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.AffineInjection;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.AffineMultiMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.AffineProjection;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.AffineRingMorphism;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.A_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.B_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.COLUMNS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.DIMENSION_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.INDEX_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_MORPHISM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULUS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.ORDERING_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.POWER_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.ROWS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class DefaultModuleMorphismXmlWriter implements LatrunculusXmlWriter<ModuleMorphism> {

    //TODO extract common logic

    private final LatrunculusXmlWriter<MathDefinition> definitionWriter;

    @Override
    public void toXML(ModuleMorphism object, XMLWriter writer) {
        if (object instanceof AffineRingMorphism) {
            write((AffineRingMorphism<?>) object, writer);
            return;
        }
        if (object instanceof AffineInjection) {
            write((AffineInjection<?>) object, writer);
            return;
        }
        if (object instanceof AffineProjection) {
            write((AffineProjection<?>) object, writer);
            return;
        }
        if (object instanceof AffineMultiMorphism) {
            write((AffineMultiMorphism<?>) object, writer);
            return;
        }
        if (object instanceof CanonicalMorphism) {
            write((CanonicalMorphism) object, writer);
            return;
        }
        if (object instanceof CastMorphism) {
            write((CastMorphism) object, writer);
            return;
        }
        if (object instanceof CompositionMorphism) {
            write((CompositionMorphism) object, writer);
            return;
        }
        if (object instanceof ConjugationMorphism) {
            write((ConjugationMorphism) object, writer);
            return;
        }
        if (object instanceof ConstantMorphism) {
            write((ConstantMorphism) object, writer);
            return;
        }
        if (object instanceof DifferenceMorphism) {
            write((DifferenceMorphism) object, writer);
            return;
        }
        if (object instanceof EmbeddingMorphism) {
            write((EmbeddingMorphism) object, writer);
            return;
        }
        if (object instanceof FoldingMorphism) {
            write((FoldingMorphism) object, writer);
            return;
        }
        if (object instanceof GenericAffineMorphism) {
            write((GenericAffineMorphism) object, writer);
            return;
        }
        if (object instanceof GenericBasisMorphism) {
            write((GenericBasisMorphism) object, writer);
            return;
        }
        if (object instanceof IdentityMorphism) {
            write((IdentityMorphism) object, writer);
            return;
        }
        if (object instanceof ModuloMorphism) {
            write((ModuloMorphism) object, writer);
            return;
        }
        if (object instanceof PolynomialMorphism) {
            write((PolynomialMorphism) object, writer);
            return;
        }
        if (object instanceof PowerMorphism) {
            write((PowerMorphism) object, writer);
            return;
        }
        if (object instanceof ProductMorphism) {
            write((ProductMorphism) object, writer);
            return;
        }
        if (object instanceof ProjectionMorphism) {
            write((ProjectionMorphism) object, writer);
            return;
        }
        if (object instanceof ReorderMorphism) {
            write((ReorderMorphism) object, writer);
            return;
        }
        if (object instanceof ScaledMorphism) {
            write((ScaledMorphism) object, writer);
            return;
        }
        if (object instanceof SplitMorphism) {
            write((SplitMorphism) object, writer);
            return;
        }
        if (object instanceof SumMorphism) {
            write((SumMorphism) object, writer);
            return;
        }
        if (object instanceof TranslationMorphism) {
            write((TranslationMorphism) object, writer);
        }
    }

    private void write(AffineRingMorphism<?> morphism, XMLWriter writer) {
        writer.emptyWithType(MODULE_MORPHISM, morphism.getElementTypeName(), A_ATTR, morphism.getA(), B_ATTR, morphism.getB());
    }

    private void write(AffineProjection<?> morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName(), ROWS_ATTR, morphism.getMatrix().getLength());
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < morphism.getMatrix().getLength(); i++) {
            if (i != 0) {
                s.append(",");
            }
            s.append(morphism.getMatrix().getComponent(i));
        }
        writer.openInline(A_ATTR);
        writer.text(s.toString());
        writer.closeInline();
        s = new StringBuilder(morphism.getVector().toString());
        writer.openInline(B_ATTR);
        writer.text(s.toString());
        writer.closeInline();
        writer.closeBlock();
    }

    private void write(AffineMultiMorphism<?> morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName(), ROWS_ATTR, morphism.getMatrix().getRowCount(), COLUMNS_ATTR, morphism.getMatrix().getColumnCount());
        String s = "";
        for (int i = 0; i < morphism.getMatrix().getRowCount(); i++) {
            for (int j = 0; j < morphism.getMatrix().getColumnCount(); j++) {
                if (i != 0 || j != 0) {
                    s += ",";
                }
                s += morphism.getMatrix().get(i, j);
            }
        }
        writer.openInline(A_ATTR);
        writer.text(s);
        writer.closeInline();
        s = morphism.getVector().getComponent(0).toString();
        for (int i = 1; i < morphism.getVector().length(); i++) {
            s += ","+morphism.getVector().getComponent(i);
        }
        writer.openInline(B_ATTR);
        writer.text(s);
        writer.closeInline();
        writer.closeBlock();
    }

    private void write(AffineInjection<?> morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName(), ROWS_ATTR, morphism.getMatrix().getLength(), COLUMNS_ATTR);
        String s = "";
        for (int i = 0; i < morphism.getMatrix().getLength(); i++) {
            if (i != 0) {
                s += ",";
            }
            s += morphism.getMatrix().getComponent(i);
        }
        writer.openInline(A_ATTR);
        writer.text(s);
        writer.closeInline();
        s = morphism.getVector().getComponent(0).toString();
        for (int i = 1; i < morphism.getVector().getLength(); i++) {
            s += ","+morphism.getVector().getComponent(0);
        }
        writer.openInline(B_ATTR);
        writer.text(s);
        writer.closeInline();
        writer.closeBlock();
    }

    private void write(CanonicalMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getDomain(), writer);
        definitionWriter.toXML(morphism.getCodomain(), writer);
        writer.closeBlock();
    }

    private void write(CastMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getDomain(), writer);
        definitionWriter.toXML(morphism.getCodomain(), writer);
        writer.closeBlock();
    }

    private void write(CompositionMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getFirstMorphism(), writer);
        definitionWriter.toXML(morphism.getSecondMorphism(), writer);
        writer.closeBlock();
    }

    private void write(ConjugationMorphism morphism, XMLWriter writer) {
        writer.openInline(MODULE_MORPHISM, TYPE_ATTR, morphism.getElementTypeName(), DIMENSION_ATTR, morphism.getDomain().getDimension());
    }

    private void write(ConstantMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        if (!morphism.getDomain().equals(morphism.getCodomain())) {
            definitionWriter.toXML(morphism.getDomain(), writer);
        }
        definitionWriter.toXML(morphism.getValue(), writer);
        writer.closeBlock();
    }

    private void write(DifferenceMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getFirstMorphism(), writer);
        definitionWriter.toXML(morphism.getSecondMorphism(), writer);
        writer.closeBlock();
    }

    private void write(EmbeddingMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getDomain(), writer);
        definitionWriter.toXML(morphism.getCodomain(), writer);
        writer.closeBlock();
    }

    private void write(FoldingMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        for (int i = 0; i < morphism.getElements().length; i++) {
            definitionWriter.toXML(morphism.getElements()[i], writer);
        }
        writer.closeBlock();
    }

    private void write(GenericAffineMorphism<?,?,?> morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getDomain(), writer);
        definitionWriter.toXML(morphism.getCodomain(), writer);
        for (int i = 0; i < morphism.getCodomainDimension(); i++) {
            for (int j = 0; j < morphism.getDomainDimension(); j++) {
                definitionWriter.toXML(morphism.getMatrix().get(i,j), writer);
            }
        }
        for (int i = 0; i < morphism.getCodomainDimension(); i++) {
            definitionWriter.toXML(morphism.getVector().get(i), writer);
        }
        writer.closeBlock();
    }

    private void write(GenericBasisMorphism<?,?,?> morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getDomain(), writer);
        definitionWriter.toXML(morphism.getCodomain(), writer);
        for (int i = 0; i < morphism.getModuleElements().size(); i++) {
            definitionWriter.toXML(morphism.getModuleElements().get(i), writer);
        }
        writer.closeBlock();
    }

    private void write(IdentityMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getDomain(), writer);
        writer.closeBlock();
    }

    private void write(ModuloMorphism morphism, XMLWriter writer) {
        writer.emptyWithType(MODULE_MORPHISM, morphism.getElementTypeName(), DIMENSION_ATTR, morphism.getDimension(), MODULUS_ATTR, morphism.getModulus());
    }

    private void write(PolynomialMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getPolynomial(), writer);
        writer.closeBlock();
    }

    private void write(PowerMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName(), POWER_ATTR, morphism.getExponent());
        definitionWriter.toXML(morphism.getBaseMorphism(), writer);
        writer.closeBlock();
    }

    private void write(ProductMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getFirstMorphism(), writer);
        definitionWriter.toXML(morphism.getSecondMorphism(), writer);
        writer.closeBlock();
    }

    private void write(ProjectionMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName(), INDEX_ATTR, morphism.getIndex());
        definitionWriter.toXML(morphism.getDomain(), writer);
        writer.closeBlock();
    }

    private void write(ReorderMorphism morphism, XMLWriter writer) {
        StringBuilder res = new StringBuilder();
        res.append(morphism.getOrdering()[0]);
        for (int i = 1; i < morphism.getOrdering().length; i++) {
            res.append(",");
            res.append(morphism.getOrdering()[i]);
        }
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName(), ORDERING_ATTR, res);
        definitionWriter.toXML(morphism.getDomain(), writer);
        writer.closeBlock();
    }

    private void write(ScaledMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getMorphism(), writer);
        definitionWriter.toXML(morphism.getScalar(), writer);
        writer.closeBlock();
    }

    private void write(SplitMorphism<?,?> morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getDomain(), writer);
        for (ModuleMorphism m : morphism.getMorphisms()) {
            definitionWriter.toXML(m, writer);
        }
        writer.closeBlock();
    }

    private void write(SumMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getFirstMorphism(), writer);
        definitionWriter.toXML(morphism.getSecondMorphism(), writer);
        writer.closeBlock();
    }

    private void write(TranslationMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getDomain(), writer);
        definitionWriter.toXML(morphism.getTranslate(), writer);
        writer.closeBlock();
    }

}
