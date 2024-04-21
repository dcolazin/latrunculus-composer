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

import lombok.Getter;
import org.rubato.composer.components.JFormTree;
import org.rubato.composer.components.JMorphismEntry;
import org.rubato.composer.components.JSelectForm;
import org.rubato.composer.components.JStatusline;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.rubato.composer.Utilities.makeTitledBorder;

public class ModuleMapRubette extends AbstractRubette implements ActionListener {
    
    public ModuleMapRubette() {
        setInCount(1);
        setOutCount(1);
    }

    public ModuleMapRubette(Form form) {
        this();
        this.inputForm = form;
    }

    public ModuleMapRubette(Form form, int[] path) {
        this(form);
        this.path = path;
    }

    public ModuleMapRubette(Form form, int[] path, ModuleMorphism morphism) {
        this(form, path);
        this.morphism = morphism;
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

    @Getter
    private Form           inputForm = null;
    @Getter
    private ModuleMorphism morphism = null;
    @Getter
    private int[]          path = null;

    private JPanel         properties = null;
    private JFormTree      formTree = null;
    private JSelectForm    selectForm = null;
    private JMorphismEntry morphismEntry = null;
    private JStatusline    statusline = null;
}
