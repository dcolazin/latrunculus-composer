package org.vetronauta.latrunculus.server.xml.writer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.SCHEME;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RepositoryXmlWriter {

    public static void toXml(Repository repository, XMLWriter writer) {
        if (repository.getSchemeCode().length() > 0) {
            writer.openBlock(SCHEME);
            writer.writeTextNode(repository.getSchemeCode().trim()+"\n");
            writer.closeBlock();
        }
        for (String moduleName : repository.getModuleNames()) {
            if (!repository.isBuiltinModule(moduleName)) {
                writer.writeModule(moduleName, repository.getModule(moduleName));
            }
        }
        for (String moduleElementName : repository.getModuleElementNames()) {
            if (!repository.isBuiltinModuleElement(moduleElementName)) {
                writer.writeModuleElement(moduleElementName, repository.getModuleElement(moduleElementName));
            }
        }
        for (String moduleMorphismName : repository.getModuleMorphismNames()) {
            if (!repository.isBuiltinModuleMorphism(moduleMorphismName)) {
                writer.writeModuleMorphism(moduleMorphismName, repository.getModuleMorphism(moduleMorphismName));
            }
        }
        for (Form form : repository.getCustomForms()) {
            writer.writeForm(form);
        }
        for (Denotator denotator : repository.getCustomDenotators()) {
            writer.writeDenotator(denotator);
        }
    }

}
