package org.vetronauta.latrunculus.server.xml.writer;

import lombok.RequiredArgsConstructor;
import org.vetronauta.latrunculus.core.exception.LatrunculusUnsupportedException;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.Diagram;
import org.vetronauta.latrunculus.core.math.yoneda.Form;
import org.vetronauta.latrunculus.core.math.yoneda.MorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class DefaultDefinitionXmlWriter implements LatrunculusXmlWriter<MathDefinition> {

    private final LatrunculusXmlWriter<Module> moduleWriter;
    private final LatrunculusXmlWriter<ModuleMorphism> moduleMorphismWriter;
    private final LatrunculusXmlWriter<ModuleElement> moduleElementWriter;
    private final LatrunculusXmlWriter<MorphismMap> moduleMapWriter;
    private final LatrunculusXmlWriter<Denotator> denotatorXmlWriter;
    private final LatrunculusXmlWriter<YonedaMorphism> yonedaMorphismXmlWriter;
    private final LatrunculusXmlWriter<Diagram> diagramXmlWriter;
    private final LatrunculusXmlWriter<Form> formXmlWriter;

    public DefaultDefinitionXmlWriter() {
        this.moduleWriter = new DefaultModuleXmlWriter(this);
        this.moduleMorphismWriter = new DefaultModuleMorphismXmlWriter(this);
        this.moduleElementWriter = new DefaultModuleElementXmlWriter(this);
        this.moduleMapWriter = new DefaultMorphismMapXmlWriter(this);
        this.denotatorXmlWriter = new DefaultDenotatorXmlWriter(this);
        this.yonedaMorphismXmlWriter = new DefaultYonedaMorphismXmlWriter(this);
        this.diagramXmlWriter = new DefaultDiagramXmlWriter(this);
        this.formXmlWriter = new DefaultFormXmlWriter(this);
    }

    @Override
    public void toXML(MathDefinition object, XMLWriter writer) {
        if (object == null) {
            return;
        }
        if (object instanceof Module) {
            moduleWriter.toXML((Module) object, writer);
            return;
        }
        if (object instanceof ModuleMorphism) {
            moduleMorphismWriter.toXML((ModuleMorphism) object, writer);
            return;
        }
        if (object instanceof ModuleElement) {
            moduleElementWriter.toXML((ModuleElement) object, writer);
            return;
        }
        if (object instanceof MorphismMap) {
            moduleMapWriter.toXML((MorphismMap) object, writer);
            return;
        }
        if (object instanceof Denotator) {
            denotatorXmlWriter.toXML((Denotator) object, writer);
            return;
        }
        if (object instanceof YonedaMorphism) {
            yonedaMorphismXmlWriter.toXML((YonedaMorphism) object, writer);
            return;
        }
        if (object instanceof Diagram) {
            diagramXmlWriter.toXML((Diagram) object, writer);
            return;
        }
        if (object instanceof Form) {
            formXmlWriter.toXML((Form) object, writer);
            return;
        }
        throw new LatrunculusUnsupportedException(String.format("Unknown MathDefinition class: %s", object.getClass()));
    }

}
