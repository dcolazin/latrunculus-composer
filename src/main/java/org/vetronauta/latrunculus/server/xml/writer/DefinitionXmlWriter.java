package org.vetronauta.latrunculus.server.xml.writer;

import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.Diagram;
import org.vetronauta.latrunculus.core.math.yoneda.Form;
import org.vetronauta.latrunculus.core.math.yoneda.MorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.YonedaMorphism;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

/**
 * @author vetronauta
 */
public interface DefinitionXmlWriter {

    //TODO "Definition" interface to just expose one method

    //TODO implement chain of responsibility pattern to have a chain of definition writer
    //TODO do the same for the a Definition reader

    void toXML(Module object, XMLWriter writer);
    void toXML(ModuleMorphism object, XMLWriter writer);
    void toXML(ModuleElement object, XMLWriter writer);
    void toXML(MorphismMap object, XMLWriter writer);
    void toXML(Denotator object, XMLWriter writer);
    void toXML(YonedaMorphism object, XMLWriter writer);
    void toXML(Diagram object, XMLWriter writer);
    void toXML(Form object, XMLWriter writer);

}
