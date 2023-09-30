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
public interface DefinitionXmlWriter {

    void toXML(Module object, XMLWriter writer);
    void toXML(ModuleMorphism object, XMLWriter writer);
    void toXML(ModuleElement object, XMLWriter writer);
    void toXML(MorphismMap object, XMLWriter writer);
    void toXML(Denotator object, XMLWriter writer);

}
