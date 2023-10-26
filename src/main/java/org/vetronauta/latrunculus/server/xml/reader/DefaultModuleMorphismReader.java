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

import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.matrix.CMatrix;
import org.vetronauta.latrunculus.core.math.matrix.QMatrix;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.matrix.ZMatrix;
import org.vetronauta.latrunculus.core.math.matrix.ZnMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.CAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.CFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.CanonicalMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.CastMorphism;
import org.vetronauta.latrunculus.core.math.exception.CompositionException;
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
import org.vetronauta.latrunculus.core.math.module.morphism.QAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.QFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.RAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.RFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ReorderMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ScaledMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.SplitMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.SumMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.TranslationMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZnAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZnFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.endo.Endomorphism;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.A_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.B_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.COLUMNS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.DIMENSION_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.INDEX_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_ELEMENT;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_MORPHISM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULUS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.ORDERING_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.POWER_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.ROWS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

/**
 * @author vetronauta
 */
public class DefaultModuleMorphismReader implements LatrunculusXmlReader<ModuleMorphism> {
    
    //TODO common logic
    //TODO asserts vs proper checks

    @Override
    public ModuleMorphism fromXML(Element element, Class<? extends ModuleMorphism> clazz, XMLReader reader) {
        if (CAffineMorphism.class.isAssignableFrom(clazz)) {
            return readCAffineMorphism(element, clazz, reader);
        }
        if (QAffineMorphism.class.isAssignableFrom(clazz)) {
            return readQAffineMorphism(element, clazz, reader);
        }
        if (RAffineMorphism.class.isAssignableFrom(clazz)) {
            return readRAffineMorphism(element, clazz, reader);
        }
        if (ZnAffineMorphism.class.isAssignableFrom(clazz)) {
            return readZnAffineMorphism(element, clazz, reader);
        }
        if (ZAffineMorphism.class.isAssignableFrom(clazz)) {
            return readZAffineMorphism(element, clazz, reader);
        }
        if (CFreeAffineMorphism.class.isAssignableFrom(clazz)) {
            return readCFreeAffineMorphism(element, clazz, reader);
        }
        if (QFreeAffineMorphism.class.isAssignableFrom(clazz)) {
            return readQFreeAffineMorphism(element, clazz, reader);
        }
        if (RFreeAffineMorphism.class.isAssignableFrom(clazz)) {
            return readRFreeAffineMorphism(element, clazz, reader);
        }
        if (ZnFreeAffineMorphism.class.isAssignableFrom(clazz)) {
            return readZnFreeAffineMorphism(element, clazz, reader);
        }
        if (ZFreeAffineMorphism.class.isAssignableFrom(clazz)) {
            return readZFreeAffineMorphism(element, clazz, reader);
        }
        if (CanonicalMorphism.class.isAssignableFrom(clazz)) {
            return readCanonicalMorphism(element, clazz, reader);
        }
        if (CastMorphism.class.isAssignableFrom(clazz)) {
            return readCastMorphism(element, clazz, reader);
        }
        if (CompositionMorphism.class.isAssignableFrom(clazz)) {
            return readCompositionMorphism(element, clazz, reader);
        }
        if (ConjugationMorphism.class.isAssignableFrom(clazz)) {
            return readConjugationMorphism(element, clazz, reader);
        }
        if (ConstantMorphism.class.isAssignableFrom(clazz)) {
            return readConstantMorphism(element, clazz, reader);
        }
        if (DifferenceMorphism.class.isAssignableFrom(clazz)) {
            return readDifferenceMorphism(element, clazz, reader);
        }
        if (EmbeddingMorphism.class.isAssignableFrom(clazz)) {
            return readEmbeddingMorphism(element, clazz, reader);
        }
        if (FoldingMorphism.class.isAssignableFrom(clazz)) {
            return readFoldingMorphism(element, clazz, reader);
        }
        if (GenericAffineMorphism.class.isAssignableFrom(clazz)) {
            return readGenericAffineMorphism(element, clazz, reader);
        }
        if (GenericBasisMorphism.class.isAssignableFrom(clazz)) {
            return readGenericBasisMorphism(element, clazz, reader);
        }
        if (IdentityMorphism.class.isAssignableFrom(clazz)) {
            return readIdentityMorphism(element, clazz, reader);
        }
        if (ModuloMorphism.class.isAssignableFrom(clazz)) {
            return readModuloMorphism(element, clazz, reader);
        }
        if (PolynomialMorphism.class.isAssignableFrom(clazz)) {
            return readPolynomialMorphism(element, clazz, reader);
        }
        if (PowerMorphism.class.isAssignableFrom(clazz)) {
            return readPowerMorphism(element, clazz, reader);
        }
        if (ProductMorphism.class.isAssignableFrom(clazz)) {
            return readProductMorphism(element, clazz, reader);
        }
        if (ProjectionMorphism.class.isAssignableFrom(clazz)) {
            return readProjectionMorphism(element, clazz, reader);
        }
        if (ReorderMorphism.class.isAssignableFrom(clazz)) {
            return readReorderMorphism(element, clazz, reader);
        }
        if (ScaledMorphism.class.isAssignableFrom(clazz)) {
            return readScaledMorphism(element, clazz, reader);
        }
        if (SplitMorphism.class.isAssignableFrom(clazz)) {
            return readSplitMorphism(element, clazz, reader);
        }
        if (SumMorphism.class.isAssignableFrom(clazz)) {
            return readSumMorphism(element, clazz, reader);
        }
        if (TranslationMorphism.class.isAssignableFrom(clazz)) {
            return readTranslationMorphism(element, clazz, reader);
        }
        return null;
    }

    private ModuleMorphism readCAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute("a")) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), A_ATTR);
            return null;
        }
        if (!element.hasAttribute("b")) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), B_ATTR);
            return null;
        }

        Complex a0;
        Complex b0;
        try {
            a0 = ArithmeticParsingUtils.parseComplex(element.getAttribute(A_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be a complex number.", A_ATTR, getElementTypeName(clazz));
            return null;
        }
        try {
            b0 = ArithmeticParsingUtils.parseComplex(element.getAttribute(B_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be a complex number.", B_ATTR, getElementTypeName(clazz));
            return null;
        }

        return new CAffineMorphism(a0, b0);
    }

    private ModuleMorphism readQAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(A_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), A_ATTR);
            return null;
        }
        if (!element.hasAttribute(B_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), B_ATTR);
            return null;
        }

        Rational aValue;
        Rational bValue;
        try {
            aValue = ArithmeticParsingUtils.parseRational(element.getAttribute(A_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be a rational number.", A_ATTR, getElementTypeName(clazz));
            return null;
        }
        try {
            bValue = ArithmeticParsingUtils.parseRational(element.getAttribute(B_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be a rational number.", B_ATTR, getElementTypeName(clazz));
            return null;
        }

        return new QAffineMorphism(aValue, bValue);
    }

    private ModuleMorphism readRAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(A_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), A_ATTR);
            return null;
        }
        if (!element.hasAttribute(B_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), B_ATTR);
            return null;
        }

        double a0;
        double b0;
        try {
            a0 = Double.parseDouble(element.getAttribute(A_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be a real number.", A_ATTR, getElementTypeName(clazz));
            return null;
        }
        try {
            b0 = Double.parseDouble(element.getAttribute(B_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be a real number.", B_ATTR, getElementTypeName(clazz));
            return null;
        }

        return new RAffineMorphism(a0, b0);
    }

    private ModuleMorphism readZnAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(A_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), A_ATTR);
            return null;
        }
        if (!element.hasAttribute(B_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), B_ATTR);
            return null;
        }
        if (!element.hasAttribute(MODULUS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), MODULUS_ATTR);
            return null;
        }

        int a0;
        int b0;
        int modulus0;

        try {
            modulus0 = Integer.parseInt(element.getAttribute(MODULUS_ATTR));
            if (modulus0 < 2) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer > 1.", MODULUS_ATTR, getElementTypeName(clazz));
            return null;
        }

        try {
            a0 = Integer.parseInt(element.getAttribute(A_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", A_ATTR, getElementTypeName(clazz));
            return null;
        }

        try {
            b0 = Integer.parseInt(element.getAttribute(B_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", B_ATTR, getElementTypeName(clazz));
            return null;
        }

        return new ZnAffineMorphism(a0, b0, modulus0);
    }
    
    private ModuleMorphism readZAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(A_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), A_ATTR);
            return null;
        }
        if (!element.hasAttribute(B_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), B_ATTR);
            return null;
        }

        int a0;
        int b0;
        try {
            a0 = Integer.parseInt(element.getAttribute(A_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", A_ATTR, getElementTypeName(clazz));
            return null;
        }
        try {
            b0 = Integer.parseInt(element.getAttribute(B_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", B_ATTR, getElementTypeName(clazz));
            return null;
        }

        return new ZAffineMorphism(a0, b0);
    }

    private ModuleMorphism readCFreeAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));

        if (!element.hasAttribute(ROWS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), ROWS_ATTR);
            return null;
        }
        int rows;
        try {
            rows = Integer.parseInt(element.getAttribute(ROWS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", ROWS_ATTR, getElementTypeName(clazz));
            return null;
        }

        if (!element.hasAttribute(COLUMNS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), COLUMNS_ATTR);
            return null;
        }
        int columns;
        try {
            columns = Integer.parseInt(element.getAttribute(COLUMNS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", COLUMNS_ATTR, getElementTypeName(clazz));
            return null;
        }

        final int numberCount = rows*columns;
        Element aElement = XMLReader.getChild(element, A_ATTR);
        if (aElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), A_ATTR);
            return null;
        }
        String[] numbers = aElement.getTextContent().trim().split(",");
        if (numbers.length != numberCount) {
            reader.setError("Element <%1> must have a comma-separated list with %2 complex numbers.", A_ATTR, numberCount);
            return null;
        }

        CMatrix A0 = new CMatrix(rows, columns);
        try {
            int n = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    A0.set(i, j, ArithmeticParsingUtils.parseComplex(numbers[n]));
                    n++;
                }
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 complex numbers.", A_ATTR, numberCount);
            return null;
        }

        Element bElement = XMLReader.getChild(element, "b");
        if (bElement == null) {
            reader.setError("Type %%1 is missing child of type <b>.", getElementTypeName(clazz));
            return null;
        }
        numbers = bElement.getTextContent().trim().split(",");
        if (numbers.length != rows) {
            reader.setError("Element <%1> must have a comma-separated list with %2 complex numbers.", B_ATTR, rows);
            return null;
        }

        Complex b0[]= new Complex[rows];
        try {
            for (int i = 1; i <= rows; i++) {
                b0[i] = ArithmeticParsingUtils.parseComplex(numbers[i]);
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 complex numbers.", B_ATTR, rows);
            return null;
        }

        return CFreeAffineMorphism.make(A0, b0);
    }

    private ModuleMorphism readQFreeAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));

        if (!element.hasAttribute(ROWS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), ROWS_ATTR);
            return null;
        }
        int rows;
        try {
            rows = Integer.parseInt(element.getAttribute(ROWS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", ROWS_ATTR, getElementTypeName(clazz));
            return null;
        }

        if (!element.hasAttribute(COLUMNS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), COLUMNS_ATTR);
            return null;
        }
        int columns;
        try {
            columns = Integer.parseInt(element.getAttribute(COLUMNS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", COLUMNS_ATTR, getElementTypeName(clazz));
            return null;
        }

        final int numberCount = rows*columns;
        Element aElement = XMLReader.getChild(element, A_ATTR);
        if (aElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), A_ATTR);
            return null;
        }
        String[] numbers = aElement.getTextContent().trim().split(",");
        if (numbers.length != numberCount) {
            reader.setError("Element <%1> must have a comma-separated list with %2 rational numbers.", A_ATTR, numberCount);
            return null;
        }

        QMatrix A0 = new QMatrix(rows, columns);
        try {
            int n = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    A0.set(i, j, ArithmeticParsingUtils.parseRational(numbers[n]));
                    n++;
                }
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 rational numbers.", A_ATTR, numberCount);
            return null;
        }

        Element bElement = XMLReader.getChild(element, B_ATTR);
        if (bElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), B_ATTR);
            return null;
        }
        numbers = bElement.getTextContent().trim().split(",");
        if (numbers.length != rows) {
            reader.setError("Element <%1> must have a comma-separated list with %2 rational numbers.", B_ATTR, rows);
            return null;
        }

        Rational b0[]= new Rational[rows];
        try {
            for (int i = 0; i < rows; i++) {
                b0[i] = ArithmeticParsingUtils.parseRational(numbers[i]);
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 rational numbers.", B_ATTR, rows);
            return null;
        }

        return QFreeAffineMorphism.make(A0, b0);
    }

    private ModuleMorphism readRFreeAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));

        if (!element.hasAttribute(ROWS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), ROWS_ATTR);
            return null;
        }
        int rows;
        try {
            rows = Integer.parseInt(element.getAttribute(ROWS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", ROWS_ATTR, getElementTypeName(clazz));
            return null;
        }

        if (!element.hasAttribute(COLUMNS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), COLUMNS_ATTR);
            return null;
        }
        int columns;
        try {
            columns = Integer.parseInt(element.getAttribute(COLUMNS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", COLUMNS_ATTR, getElementTypeName(clazz));
            return null;
        }

        final int numberCount = rows*columns;
        Element aElement = XMLReader.getChild(element, A_ATTR);
        if (aElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), A_ATTR);
            return null;
        }
        String[] numbers = aElement.getTextContent().trim().split(",");
        if (numbers.length != numberCount) {
            reader.setError("Element <%1> must have a comma-separated list with %2 real numbers.", A_ATTR, numberCount);
            return null;
        }

        RMatrix A0 = new RMatrix(rows, columns);
        try {
            int n = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    A0.set(i, j, Double.parseDouble(numbers[n]));
                    n++;
                }
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 real numbers.", A_ATTR, numberCount);
            return null;
        }

        Element bElement = XMLReader.getChild(element, B_ATTR);
        if (bElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), B_ATTR);
            return null;
        }
        numbers = bElement.getTextContent().trim().split(",");
        if (numbers.length != rows) {
            reader.setError("Element <%1> must have a comma-separated list with %2 real numbers.", B_ATTR, rows);
            return null;
        }

        double b0[]= new double[rows];
        try {
            for (int i = 0; i < rows; i++) {
                b0[i] = Double.parseDouble(numbers[i]);
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 real numbers.", B_ATTR, rows);
            return null;
        }

        return RFreeAffineMorphism.make(A0, b0);
    }

    private ModuleMorphism readZnFreeAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));

        if (!element.hasAttribute(ROWS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), ROWS_ATTR);
            return null;
        }
        int rows;
        try {
            rows = Integer.parseInt(element.getAttribute(ROWS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", ROWS_ATTR, getElementTypeName(clazz));
            return null;
        }

        if (!element.hasAttribute(COLUMNS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), COLUMNS_ATTR);
            return null;
        }
        int columns;
        try {
            columns = Integer.parseInt(element.getAttribute(COLUMNS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", COLUMNS_ATTR, getElementTypeName(clazz));
            return null;
        }

        int modulus;
        try {
            modulus = Integer.parseInt(element.getAttribute(MODULUS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", MODULUS_ATTR, getElementTypeName(clazz));
            return null;
        }
        if (modulus < 2) {
            reader.setError("Attribute %%1 of type %%2 must be an integer > 1.", MODULUS_ATTR, getElementTypeName(clazz));
            return null;
        }

        final int numberCount = rows*columns;
        Element aElement = XMLReader.getChild(element, A_ATTR);
        if (aElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), A_ATTR);
            return null;
        }
        String[] numbers = aElement.getTextContent().trim().split(",");
        if (numbers.length != numberCount) {
            reader.setError("Element <%1> must have a comma-separated list with %2 integers.", A_ATTR, numberCount);
            return null;
        }

        ZnMatrix A0 = new ZnMatrix(rows, columns, modulus);
        try {
            int n = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    A0.set(i, j, Integer.parseInt(numbers[n]));
                    n++;
                }
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 integers.", A_ATTR, numberCount);
            return null;
        }

        Element bElement = XMLReader.getChild(element, B_ATTR);
        if (bElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), B_ATTR);
            return null;
        }
        numbers = bElement.getTextContent().trim().split(",");
        if (numbers.length != rows) {
            reader.setError("Element <%1> must have a comma-separated list with %2 integers.", B_ATTR, rows);
            return null;
        }

        int b0[]= new int[rows];
        try {
            for (int i = 0; i < rows; i++) {
                b0[i] = Integer.parseInt(numbers[i]);
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 integers.", B_ATTR, rows);
            return null;
        }

        return ZnFreeAffineMorphism.make(A0, b0);
    }

    private ModuleMorphism readZFreeAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));

        if (!element.hasAttribute(ROWS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), ROWS_ATTR);
            return null;
        }
        int rows;
        try {
            rows = Integer.parseInt(element.getAttribute(ROWS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", ROWS_ATTR, getElementTypeName(clazz));
            return null;
        }

        if (!element.hasAttribute(COLUMNS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), COLUMNS_ATTR);
            return null;
        }
        int columns;
        try {
            columns = Integer.parseInt(element.getAttribute(COLUMNS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", COLUMNS_ATTR, getElementTypeName(clazz));
            return null;
        }

        final int numberCount = rows*columns;
        Element aElement = XMLReader.getChild(element, A_ATTR);
        if (aElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), A_ATTR);
            return null;
        }
        String[] numbers = aElement.getTextContent().trim().split(",");
        if (numbers.length != numberCount) {
            reader.setError("Element <%1> must have a comma-separated list with %2 integers.", A_ATTR, numberCount);
            return null;
        }

        ZMatrix A0 = new ZMatrix(rows, columns);
        try {
            int n = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    A0.set(i, j, Integer.parseInt(numbers[n]));
                    n++;
                }
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 integers.", A_ATTR, numberCount);
            return null;
        }

        Element bElement = XMLReader.getChild(element, B_ATTR);
        if (bElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), B_ATTR);
            return null;
        }
        numbers = bElement.getTextContent().trim().split(",");
        if (numbers.length != rows) {
            reader.setError("Element <%1> must have a comma-separated list with %2 integers.", B_ATTR, rows);
            return null;
        }

        int b0[]= new int[rows];
        try {
            for (int i = 0; i < rows; i++) {
                b0[i] = Integer.parseInt(numbers[i]);
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 integers.", B_ATTR, rows);
            return null;
        }

        return ZFreeAffineMorphism.make(A0, b0);
    }

    private ModuleMorphism readCanonicalMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Module m1;
        Module m2;
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            m1 = reader.parseModule(childElement);
            if (m1 == null) {
                return null;
            }
            childElement = XMLReader.getNextSibling(childElement, MODULE);
            if (childElement != null) {
                m2 = reader.parseModule(childElement);
                if (m2 == null) {
                    return null;
                }
                ModuleMorphism m = CanonicalMorphism.make(m1, m2);
                if (m == null) {
                    reader.setError("Cannot create an canonical morphism from %1 to %2.", m1.toString(), m2.toString());
                }
                return m;
            }
            else {
                reader.setError("Type %%1 is missing second child <%2>.", getElementTypeName(clazz), MODULE);
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing element <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }

    private ModuleMorphism readCastMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Module m1;
        Module m2;
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            m1 = reader.parseModule(childElement);
            if (m1 == null) {
                return null;
            }
            childElement = XMLReader.getNextSibling(childElement, MODULE);
            if (childElement != null) {
                m2 = reader.parseModule(childElement);
                if (m2 == null) {
                    return null;
                }
                if (!(m1 instanceof Ring && m2 instanceof Ring)) {
                    reader.setError("Cannot create an cast morphism from %1 to %2.", m1.toString(), m2.toString());
                    return null;
                }
                ModuleMorphism m = CastMorphism.make((Ring)m1, (Ring)m2);
                if (m == null) {
                    reader.setError("Cannot create a cast morphism from %1 to %2.", m1.toString(), m2.toString());
                }
                return m;
            }
            else {
                reader.setError("Type %%1 is missing second child <%2>.", getElementTypeName(clazz), MODULE);
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing element <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }

    private ModuleMorphism readCompositionMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_MORPHISM);
        if (childElement != null) {
            ModuleMorphism f0 = reader.parseModuleMorphism(childElement);
            Element el = XMLReader.getNextSibling(childElement, MODULE_MORPHISM);
            if (el == null) {
                reader.setError("Type %%1 is missing second child of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
                return null;
            }
            ModuleMorphism g0 = reader.parseModuleMorphism(el);
            if (f0 == null || g0 == null) {
                return null;
            }
            try {
                ModuleMorphism morphism = CompositionMorphism.make(f0, g0);
                return morphism;
            }
            catch (CompositionException e) {
                reader.setError("Cannot compose morphism %%1 with %%2", f0.toString(), g0.toString());
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>", getElementTypeName(clazz), MODULE_MORPHISM);
            return null;
        }
    }

    private ModuleMorphism readConjugationMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(DIMENSION_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), DIMENSION_ATTR);
            return null;
        }
        int dim = XMLReader.getIntAttribute(element, DIMENSION_ATTR, 0);
        return ConjugationMorphism.make(dim);
    }

    private ModuleMorphism readConstantMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Module domain = null;
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            domain = reader.parseModule(childElement);
            if (domain == null) {
                return null;
            }
            childElement = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
        }
        else {
            childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        }

        if (childElement != null) {
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (domain == null) {
                return ConstantMorphism.make(moduleElement);
            }
            else {
                return new ConstantMorphism(domain, moduleElement);
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleMorphism readDifferenceMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_MORPHISM);
        if (childElement != null) {
            ModuleMorphism f0 = reader.parseModuleMorphism(childElement);
            Element el = XMLReader.getNextSibling(childElement, MODULE_MORPHISM);
            if (el == null) {
                reader.setError("Type %%1 is missing second child of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
                return null;
            }
            ModuleMorphism g0 = reader.parseModuleMorphism(el);
            if (f0 == null || g0 == null) {
                return null;
            }
            try {
                ModuleMorphism morphism = DifferenceMorphism.make(f0, g0);
                return morphism;
            }
            catch (CompositionException e) {
                reader.setError("Cannot take the difference of the two morphisms %1 and %2.", f0.toString(), g0.toString());
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
            return null;
        }
    }

    private ModuleMorphism readEmbeddingMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Module m1;
        Module m2;
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            m1 = reader.parseModule(childElement);
            if (m1 == null) {
                return null;
            }
            childElement = XMLReader.getNextSibling(childElement, MODULE);
            if (childElement != null) {
                m2 = reader.parseModule(childElement);
                if (m2 == null) {
                    return null;
                }
                if (m1 instanceof FreeModule && m2 instanceof FreeModule) {
                    ModuleMorphism m = EmbeddingMorphism.make((FreeModule)m1, (FreeModule)m2);
                    if (m == null) {
                        reader.setError("Cannot create an embedding morphism from "+m1+" to "+m2+".");
                    }
                    return m;
                }
                else {
                    reader.setError("Modules in %%1 must be free modules.", getElementTypeName(clazz));
                    return null;
                }
            }
            else {
                reader.setError("Type %%1 is missing second child <%2>.", getElementTypeName(clazz), MODULE);
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing element <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }

    private ModuleMorphism readFoldingMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            LinkedList<ModuleElement> elements0 = new LinkedList<ModuleElement>();
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            elements0.add(moduleElement);
            Element next = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            while (next != null) {
                moduleElement = reader.parseModuleElement(next);
                if (moduleElement == null) {
                    return null;
                }
                elements0.add(moduleElement);
                next = XMLReader.getNextSibling(next, MODULE_ELEMENT);
            }
            ModuleElement[] newElements = new ModuleElement[elements0.size()];
            int i = 0;
            for (ModuleElement e : elements0) {
                newElements[i++] = e;
            }
            ModuleMorphism result = new FoldingMorphism(newElements);
            return result;
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleMorphism readGenericAffineMorphism(Element element, Class<?> clazz, XMLReader reader) {
        Element m = XMLReader.getChild(element, MODULE);
        if (m == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
        Module domain = reader.parseModule(m);
        if (domain == null) {
            return null;
        }
        m = XMLReader.getNextSibling(m, MODULE);
        if (m == null) {
            reader.setError("Type %%1 is missing second child of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
        Module codomain = reader.parseModule(m);
        if (codomain == null) {
            return null;
        }
        if (!codomain.getRing().equals(domain.getRing())) {
            reader.setError("Domain and codomain must be modules over the same ring.");
            return null;
        }
        Ring ring0 = domain.getRing();
        int dim0 = domain.getDimension();
        int codim0 = codomain.getDimension();
        int n = 0;
        int count = dim0*codim0+codim0;
        LinkedList<RingElement> ringElements = new LinkedList<RingElement>();
        m = XMLReader.getNextSibling(m, MODULE_ELEMENT);
        while (m != null) {
            ModuleElement e = reader.parseModuleElement(m);
            if (e == null || !ring0.hasElement(e)) {
                reader.setError("Wrong element type.");
                return null;
            }
            ringElements.add((RingElement)e);
            m = XMLReader.getNextSibling(m, MODULE_ELEMENT);
            n++;
        }
        if (n != count) {
            reader.setError("Wrong number of elements.");
            return null;
        }
        GenericAffineMorphism res = new GenericAffineMorphism(ring0, dim0, codim0);
        Iterator<RingElement> iter = ringElements.iterator();
        for (int i = 0; i < codim0; i++) {
            for (int j = 0; j < dim0; j++) {
                res.setMatrix(i, j, iter.next());
            }
        }
        for (int i = 0; i < codim0; i++) {
            res.setVector(i, iter.next());
        }
        return res;
    }

    private GenericBasisMorphism readGenericBasisMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Module domain = null;
        Module codomain = null;
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            domain = reader.parseModule(childElement);
            if (domain == null) {
                return null;
            }
            if (!(domain instanceof FreeModule)) {
                reader.setError("Domain of type %%1 must be a free module.", getElementTypeName(clazz));
                return null;
            }
            childElement = XMLReader.getNextSibling(childElement, MODULE);
            if (childElement != null) {
                codomain = reader.parseModule(childElement);
                if (codomain == null) {
                    return null;
                }
                List<ModuleElement> elementList = new ArrayList<>();
                childElement = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
                while (childElement != null) {
                    ModuleElement<?,?> melement = reader.parseModuleElement(childElement);
                    if (melement == null) {
                        return null;
                    }
                    elementList.add(melement);
                }
                try {
                    GenericBasisMorphism morphism = GenericBasisMorphism.make((FreeModule)domain, codomain, elementList);
                    return morphism;
                }
                catch (DomainException e) {
                    reader.setError(e);
                    return null;
                }
            }
            else {
                reader.setError("Type %%1 is missing second element <%2>.", getElementTypeName(clazz), MODULE);
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing element <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }

    private ModuleMorphism readIdentityMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            Module module = reader.parseModule(childElement);
            if (module == null) {
                return null;
            }
            return new IdentityMorphism(module);
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }

    private ModuleMorphism readModuloMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        int dimension0 = XMLReader.getIntAttribute(element, DIMENSION_ATTR, 0);
        if (dimension0 < 0) {
            reader.setError("Dimension in type %%1 must be > 0.", getElementTypeName(clazz));
            return null;
        }
        int modulus0 = XMLReader.getIntAttribute(element, MODULUS_ATTR, 2);
        if (modulus0 < 2) {
            reader.setError("Modulus in type %%1 must be > 1.", getElementTypeName(clazz));
            return null;
        }

        return ModuloMorphism.make(dimension0, modulus0);
    }

    private ModuleMorphism readPolynomialMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_ELEMENT);
        if (childElement != null) {
            ModuleElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (moduleElement instanceof PolynomialElement) {
                return new PolynomialMorphism((PolynomialElement)moduleElement);
            }
            else {
                reader.setError("Type %%1 is missing child of type %%2.", getElementTypeName(clazz), "PolynomialElement");
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
    }

    private ModuleMorphism readPowerMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(POWER_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), POWER_ATTR);
            return null;
        }
        int power;
        try {
            power = Integer.parseInt(element.getAttribute(POWER_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", POWER_ATTR, getElementTypeName(clazz));
            return null;
        }
        Element childElement = XMLReader.getChild(element, MODULE_MORPHISM);
        if (childElement != null) {
            Endomorphism<?,?> f0 = (Endomorphism<?, ?>) reader.parseModuleMorphism(childElement);
            if (f0 == null) {
                return null;
            }
            try {
                return PowerMorphism.make(f0, power);
            }
            catch (CompositionException e) {
                reader.setError(e.getMessage());
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
            return null;
        }
    }

    private ModuleMorphism readProductMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_MORPHISM);
        if (childElement != null) {
            ModuleMorphism f0 = reader.parseModuleMorphism(childElement);
            Element el = XMLReader.getNextSibling(childElement, MODULE_MORPHISM);
            if (el == null) {
                reader.setError("Type %%1 is missing second child of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
                return null;
            }
            ModuleMorphism g0 = reader.parseModuleMorphism(el);
            if (f0 == null || g0 == null) {
                return null;
            }
            ModuleMorphism morphism = ProductMorphism.make(f0, g0);
            return morphism;
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
            return null;
        }
    }

    private ModuleMorphism readProjectionMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(INDEX_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), INDEX_ATTR);
            return null;
        }
        int index0 = XMLReader.getIntAttribute(element, INDEX_ATTR, 0);
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            Module m = reader.parseModule(childElement);
            if (m == null) {
                return null;
            }
            if (m instanceof ProductRing) {
                ProjectionMorphism pm = ProjectionMorphism.make((ProductRing)m, index0);
                return pm;
            }
            else {
                reader.setError("The module in type %%1 must be a product ring.", getElementTypeName(clazz));
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }

    private ModuleMorphism readReorderMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        if (!element.hasAttribute(ORDERING_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(clazz), ORDERING_ATTR);
            return null;
        }
        String[] values = element.getAttribute(ORDERING_ATTR).split(",");
        int[] order = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                order[i] = Integer.parseInt(values[i]);
            }
            catch (NumberFormatException e) {
                reader.setError("Attribute %%1 in type %%2 must be a comma-separated list of integers.", ORDERING_ATTR, getElementTypeName(clazz));
                return null;
            }
        }

        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            Module m = reader.parseModule(childElement);
            if (m == null) {
                return null;
            }
            if (m instanceof ProductRing) {
                return ReorderMorphism.make((ProductRing)m, order);
            }
            else if (m instanceof Ring) {
                return ReorderMorphism.make((Ring)m, order.length);
            }
            else {
                reader.setError("The module in type %%2 must be a ring.", getElementTypeName(clazz));
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }

    private ModuleMorphism readScaledMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_MORPHISM);
        if (childElement != null) {
            ModuleMorphism f0 = reader.parseModuleMorphism(childElement);
            Element el = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            if (el == null) {
                reader.setError("Type %%1 is missing second child of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
                return null;
            }
            ModuleElement value = reader.parseModuleElement(el);
            if (f0 == null || value == null) {
                return null;
            }
            if (value instanceof RingElement) {
                ModuleMorphism m = ScaledMorphism.make(f0, (RingElement)value);
                if (m == null) {
                    reader.setError("Cannot scale %1 by %2.", f0, value);
                }
                return m;
            }
            else {
                reader.setError("The ModuleElement in type %%1 must be a ring element.", getElementTypeName(clazz));
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
            return null;
        }
    }

    private ModuleMorphism readSplitMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            Module domain = reader.parseModule(childElement);
            if (domain == null) {
                return null;
            }
            if (!(domain instanceof FreeModule)) {
                reader.setError("Module in type %%1 must be a free module.", getElementTypeName(clazz));
                return null;
            }
            LinkedList<ModuleMorphism> morphismList = new LinkedList<ModuleMorphism>();
            childElement = XMLReader.getNextSibling(childElement, MODULE_MORPHISM);
            if (childElement == null) {
                reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
                return null;
            }
            while (childElement != null) {
                ModuleMorphism m = reader.parseModuleMorphism(childElement);
                if (m == null) {
                    return null;
                }
                morphismList.add(m);
                childElement = XMLReader.getNextSibling(childElement, MODULE_MORPHISM);
            }
            ModuleMorphism res = SplitMorphism.make((FreeModule)domain, morphismList);
            if (res == null) {
                reader.setError("Cannot create a split morphism.");
                return null;
            }
            else {
                return res;
            }
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }

    private ModuleMorphism readSumMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE_MORPHISM);
        if (childElement != null) {
            ModuleMorphism f0 = reader.parseModuleMorphism(childElement);
            Element el = XMLReader.getNextSibling(childElement, MODULE_MORPHISM);
            if (el == null) {
                reader.setError("Type %%1 is missing second child of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
                return null;
            }
            ModuleMorphism g0 = reader.parseModuleMorphism(el);
            if (f0 == null || g0 == null) {
                return null;
            }
            try {
                ModuleMorphism morphism = SumMorphism.make(f0, g0);
                return morphism;
            }
            catch (CompositionException e) {
                reader.setError("Cannot take the sum of the two morphisms %1 and %2.", f0, g0);
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(clazz), MODULE_MORPHISM);
            return null;
        }
    }

    private ModuleMorphism readTranslationMorphism(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            Module f = reader.parseModule(childElement);
            Element el = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
            if (el == null) {
                reader.setError("Type %%1 is missing second child of type <%2>.", getElementTypeName(clazz), MODULE_ELEMENT);
                return null;
            }
            ModuleElement trslte = reader.parseModuleElement(el);
            if (f == null || trslte == null) {
                return null;
            }
            try {
                ModuleMorphism morphism = new TranslationMorphism(trslte);
                return morphism;
            }
            catch (IllegalArgumentException e) {
                reader.setError(e.getMessage());
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(clazz), MODULE);
            return null;
        }
    }
    
    //TODO name repository as some subclasses are serialized as superclasses
    private String getElementTypeName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
    
}
