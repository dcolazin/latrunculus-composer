package org.vetronauta.latrunculus.server.xml.writer;

import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.MorphismMap;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

/**
 * @author vetronauta
 */
public class DefaultModuleDefinitionXmlWriter implements ModuleDefinitionXmlWriter {

    private final LatrunculusXmlWriter<Module> moduleWriter;
    private final LatrunculusXmlWriter<ModuleMorphism> moduleMorphismWriter;
    private final LatrunculusXmlWriter<ModuleElement> moduleElementWriter;
    private final LatrunculusXmlWriter<MorphismMap> moduleMapWriter;

    public DefaultModuleDefinitionXmlWriter() {
        this.moduleWriter = null;
        this.moduleMorphismWriter = null;
        this.moduleElementWriter = new DefaultModuleElementXmlWriter(this);
        this.moduleMapWriter = null;
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


}
