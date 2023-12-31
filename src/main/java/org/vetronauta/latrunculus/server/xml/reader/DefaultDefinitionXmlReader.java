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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.vetronauta.latrunculus.core.exception.LatrunculusUnsupportedException;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.Diagram;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.map.MorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class DefaultDefinitionXmlReader implements LatrunculusRestrictedXmlReader<MathDefinition> {

    private static final String ERROR_MESSAGE = "Unknown MathDefinition class: %s, superclass %s";

    //TODO chain of responsibility

    private final LatrunculusXmlReader<Module> moduleReader;
    private final LatrunculusXmlReader<ModuleMorphism> moduleMorphismReader;
    private final LatrunculusXmlReader<ModuleElement> moduleElementReader;
    private final LatrunculusXmlReader<MorphismMap> morphismMapReader;
    private final LatrunculusXmlReader<Denotator> denotatorReader;
    private final LatrunculusXmlReader<YonedaMorphism> yonedaMorphismReader;
    private final LatrunculusXmlReader<Diagram> diagramReader;
    private final LatrunculusXmlReader<Form> formReader;

    public DefaultDefinitionXmlReader() {
        this.moduleReader = new DefaultModuleXmlReader();
        this.moduleMorphismReader = new DefaultModuleMorphismReader();
        this.moduleElementReader = new DefaultModuleElementXmlReader();
        this.morphismMapReader = new DefaultMorphismMapXmlReader();
        this.denotatorReader = new DefaultDenotatorXmlReader();
        this.yonedaMorphismReader = new DefaultYonedaMorphismXmlReader();
        this.diagramReader = new DefaultDiagramXmlReader();
        this.formReader = new DefaultFormXmlReader();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <S extends R, R extends MathDefinition> R fromXML(@NonNull Element element,
                 @NonNull Class<S> clazz, @NonNull Class<R> superClass, @NonNull XMLReader reader) {
        if (Module.class.isAssignableFrom(clazz)) {
            return (R) moduleReader.fromXML(element, (Class<? extends Module>) clazz, reader);
        }
        if (ModuleMorphism.class.isAssignableFrom(clazz)) {
            return (R) moduleMorphismReader.fromXML(element, (Class<? extends ModuleMorphism>) clazz, reader);
        }
        if (ModuleElement.class.isAssignableFrom(clazz)) {
            return (R) moduleElementReader.fromXML(element, (Class<? extends ModuleElement>) clazz, reader);
        }
        if (MorphismMap.class.isAssignableFrom(clazz)) {
            return (R) morphismMapReader.fromXML(element, (Class<? extends MorphismMap>) clazz, reader);
        }
        if (Denotator.class.isAssignableFrom(clazz)) {
            return (R) denotatorReader.fromXML(element, (Class<? extends Denotator>) clazz, reader);
        }
        if (YonedaMorphism.class.isAssignableFrom(clazz)) {
            return (R) yonedaMorphismReader.fromXML(element, (Class<? extends YonedaMorphism>) clazz, reader);
        }
        if (Diagram.class.isAssignableFrom(clazz)) {
            return (R) diagramReader.fromXML(element, (Class<? extends Diagram>) clazz, reader);
        }
        if (Form.class.isAssignableFrom(clazz)) {
            return (R) formReader.fromXML(element, (Class<? extends Form>) clazz, reader);
        }
        throw new LatrunculusUnsupportedException(String.format(ERROR_MESSAGE, clazz, superClass));
    }
    
}
