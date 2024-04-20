/*
 * Copyright (C) 2005 Gérard Milmeister
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.rubato.rubettes.builtin;

import org.rubato.base.AbstractRubette;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.rubato.base.RubatoConstants;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.rubato.base.Rubette;
import org.rubato.base.RunInfo;
import org.rubato.composer.components.JFormTree;
import org.rubato.composer.components.JMorphismEntry;
import org.rubato.composer.components.JSelectForm;
import org.rubato.composer.components.JStatusline;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.server.xml.writer.DefaultDefinitionXmlWriter;
import org.vetronauta.latrunculus.server.xml.writer.LatrunculusXmlWriter;
import org.w3c.dom.Element;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.rubato.composer.Utilities.makeTitledBorder;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_MORPHISM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUE_ATTR;

public class ModuleMapRubette extends AbstractRubette implements ActionListener {

    //TODO rubette writer
    private final LatrunculusXmlWriter<MathDefinition> definitionXmlWriter = new DefaultDefinitionXmlWriter();
    
    public ModuleMapRubette() {
        setInCount(1);
        setOutCount(1);
    }
    

    public void run(RunInfo runInfo) {
        Denotator input = getInput(0);
        Denotator res = null;
        if (input == null) {
            addError("Input denotator is null.");
        }
        else if (path == null) {
            addError("Path is not set.");
        }
        else if (morphism == null) {
            addError("Module morphism is not set.");
        }
        else {
            try {
                res = input.map(path, morphism);
            }
            catch (LatrunculusCheckedException e) {
                addError("Could not map %1 onto %2.", morphism, input);
            }
        }
        setOutput(0, res);
    }

    
    public Rubette duplicate() {
        ModuleMapRubette rubette = new ModuleMapRubette();
        rubette.inputForm = inputForm;
        rubette.morphism = morphism;
        rubette.path = path;
        return rubette;
    }

    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "ModuleMap"; 
    }

    
    public boolean hasProperties() {
        return true;
    }


    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();            
            properties.setLayout(new BorderLayout());
            selectForm = new JSelectForm(Repository.systemRepository());
            selectForm.setBorder(makeTitledBorder("Input form"));
            selectForm.addActionListener(this);
            properties.add(selectForm, BorderLayout.NORTH);
            
            formTree = new JFormTree();
            formTree.setPreferredSize(new Dimension(200, 300));
            formTree.addActionListener(this);
            JScrollPane scrollPane = new JScrollPane(formTree);
            scrollPane.setBorder(makeTitledBorder("Path"));
            properties.add(scrollPane, BorderLayout.CENTER);
        
            Box box = Box.createVerticalBox();
            morphismEntry = new JMorphismEntry(null, null);
            morphismEntry.setBorder(makeTitledBorder("Module morphism"));
            morphismEntry.setEnabled(false);
            box.add(morphismEntry);
            
            statusline = new JStatusline();
            box.add(statusline);
            
            properties.add(box, BorderLayout.SOUTH);

            if (inputForm != null) {
                selectForm.setForm(inputForm);
                formTree.setForm(inputForm);
                formTree.setSelectedPath(path);
                morphismEntry.setEnabled(true);
                if (morphism != null) {
                    morphismEntry.setMorphism(morphism);
                }
            }
            updateFields();
        }
        return properties;
    }


    public boolean applyProperties() {
        Form f = selectForm.getForm();
        int[] p = formTree.getSelectedPath();
        ModuleMorphism m = morphismEntry.getMorphism();
        if (f != null && p != null && m != null) {
            inputForm = f;
            path = p;
            morphism = m;
            statusline.clear();
            return true;
        }
        else {
            statusline.setError("Input data incomplete");
            return false;
        }
    }


    public void revertProperties() {
        selectForm.setForm(inputForm);
        formTree.setForm(inputForm);
        formTree.setSelectedPath(path);
        morphismEntry.setMorphism(morphism);
        updateFields();
    }

    
    private void updateFields() {
        if (selectForm.getForm() != null && 
            formTree.getSelectedPath() != null &&
            formTree.getSelectedForm() instanceof SimpleForm) {
            morphismEntry.setEnabled(true);
        }
        else {
            morphismEntry.setEnabled(false);
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == selectForm) {
            Form form = selectForm.getForm();
            if (form != null) {
                formTree.setForm(form);
                morphismEntry.clear();
            }
        }
        else if (src == formTree) {
            Form form = formTree.getSelectedForm();
            if (form != null) {
                if (form instanceof SimpleForm) {
                    Module module = ((SimpleForm)form).getModule();
                    if (module != morphismEntry.getDomain() ||
                        module != morphismEntry.getCodomain()) {
                        morphismEntry.clear();
                        morphismEntry.setDomain(module);
                        morphismEntry.setCodomain(module);
                    }
                }
            }
        }
        updateFields();
    }
    

    public boolean hasInfo() {
        return true;
    }
    
    
    public String getInfo() {
        if (inputForm == null) {
            return "Input form not set";
        }
        else {
            return inputForm.getNameString()+": "+inputForm.getTypeString(); 
        }
    }
    
    
    public String getShortDescription() {
        return "Maps a module morphism onto a denotator";
    }

    
    public String getLongDescription() {
        return "Maps a module morphism onto a denotator "+
               "along a given path.";        
    }
    
    
    public String getInTip(int i) {
        return "Input denotator";
    }
    
    
    public String getOutTip(int i) {
        return "Mapped output denotator";
    }

    
    private static final String PATH = "Path";
    
    public void toXML(XMLWriter writer) {
        if (inputForm != null) {
            writer.writeFormRef(inputForm);
            if (path != null) {
                StringBuilder buf = new StringBuilder(20);
                if (path.length > 0) {
                    buf.append(path[0]);
                    for (int i = 1; i < path.length; i++) {
                        buf.append(",");
                        buf.append(path[i]);
                    }
                }
                writer.empty(PATH, VALUE_ATTR, buf.toString());
                if (morphism != null) {
                    definitionXmlWriter.toXML(morphism, writer);
                }
            }
        }
    }
    
    
    public Rubette fromXML(XMLReader reader, Element element) {
        Form iform = null;
        ModuleMapRubette newRubette = null;
        
        Element child = XMLReader.getChild(element, FORM);
        if (child == null) {
            // no input form has been given
            newRubette = new ModuleMapRubette();
            return newRubette;
        }
        
        // get input form
        iform = reader.parseAndResolveForm(child);
        if (iform == null) {
            return null;
        }
        
        // get path
        child = XMLReader.getNextSibling(child, PATH);
        if (child == null) {
            // no path has been given
            newRubette = new ModuleMapRubette();
            newRubette.inputForm = iform;
            return newRubette;
        }        
        String pathString = child.getAttribute(VALUE_ATTR);
        int[] p = parsePath(pathString);
        if (p == null) {
            reader.setError("Path has not the correct format");
            return null;
        }
        
        // get module morphism
        child = XMLReader.getNextSibling(child, MODULE_MORPHISM);
        if (child == null) {
            // no module morphism has been given
            newRubette = new ModuleMapRubette();
            newRubette.inputForm = iform;
            newRubette.path = p;
            return newRubette;
        }
        ModuleMorphism m = reader.parseModuleMorphism(child);
        if (m == null) {
            return null;
        }
        
        newRubette = new ModuleMapRubette();
        newRubette.inputForm = iform;
        newRubette.path = p;
        newRubette.morphism = m;
        return newRubette;
    }
    
    
    private static int[] parsePath(String pathString) {
        String p = pathString.trim();
        if (p.length() == 0) {
            return new int[0];
        }
        String[] strings = pathString.trim().split(",");
        int[] path = new int[strings.length];
        for (int i = 0; i < path.length; i++) {
            try {
                path[i] = Integer.parseInt(strings[i]);
            }
            catch (NumberFormatException e) {
                return null;
            }
        }
        return path;
    }


    private Form           inputForm = null;
    private ModuleMorphism morphism = null;
    private int[]          path = null;

    private JPanel         properties = null;
    private JFormTree      formTree = null;
    private JSelectForm    selectForm = null;
    private JMorphismEntry morphismEntry = null;
    private JStatusline    statusline = null;
}
