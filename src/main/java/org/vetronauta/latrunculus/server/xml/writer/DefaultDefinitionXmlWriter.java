package org.vetronauta.latrunculus.server.xml.writer;

import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.MorphismMap;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

/**
 * @author vetronauta
 */
public class DefaultDefinitionXmlWriter implements DefinitionXmlWriter {

    private final LatrunculusXmlWriter<Module> moduleWriter;
    private final LatrunculusXmlWriter<ModuleMorphism> moduleMorphismWriter;
    private final LatrunculusXmlWriter<ModuleElement> moduleElementWriter;
    private final LatrunculusXmlWriter<MorphismMap> moduleMapWriter;
    private final LatrunculusXmlWriter<Denotator> denotatorXmlWriter;

    public DefaultDefinitionXmlWriter() {
        this.moduleWriter = null; //TODO
        this.moduleMorphismWriter = null; //TODO
        this.moduleElementWriter = new DefaultModuleElementXmlWriter(this);
        this.moduleMapWriter = new DefaultMorphismMapXmlWriter(this);
        this.denotatorXmlWriter = null; //TODO
    }

    @Override
    public void toXML(Module object, XMLWriter writer) {
        moduleWriter.toXML(object, writer);
    }

    @Override
    public void toXML(ModuleMorphism object, XMLWriter writer) {
        moduleMorphismWriter.toXML(object, writer);
    }

    @Override
    public void toXML(ModuleElement object, XMLWriter writer) {
        moduleElementWriter.toXML(object, writer);
    }

    @Override
    public void toXML(MorphismMap object, XMLWriter writer) {
        moduleMapWriter.toXML(object, writer);
    }

    @Override
    public void toXML(Denotator object, XMLWriter writer) {
        denotatorXmlWriter.toXML(object, writer);
    }


}