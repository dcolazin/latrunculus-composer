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
import org.rubato.composer.components.JSelectForm;
import org.vetronauta.latrunculus.client.plugin.icons.Icons;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
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


public class SplitRubette
        extends AbstractRubette
        implements ActionListener {       
    
    public SplitRubette() {
        setInCount(1);
        setOutCount(0);
    }

    
    public void run(RunInfo runInfo) {
        Denotator input = getInput(0);
        if (input == null) {
            addError(BuiltinMessages.getString("SplitRubette.inputnull"));
            return;
        }
        
        if (form == null) {
            addError(BuiltinMessages.getString("SplitRubette.noform"));
            return;
        }        
        
        if (hasErrors()) {
            return;
        }
        
        if (!input.getForm().equals(form)) {
            addError(BuiltinMessages.getString("SplitRubette.wrongform"));
            return;
        }
    
        LimitDenotator ld = (LimitDenotator)input;
        for (int i = 0; i < getOutCount(); i++) {
            setOutput(i, ld.getFactor(selectedForms[i]));
        }
    }

    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "Split";
    }

    
    public Rubette duplicate() {
        SplitRubette newRubette = new SplitRubette();
        newRubette.set((LimitForm)getForm(), selectedForms);
        return newRubette;
    }
    
    
    public boolean hasInfo() {
        return true;
    }
    
    
    public String getInfo() {
        return formName;
    }
    
    
    public boolean hasProperties() {
        return true;
    }


    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();            
            properties.setLayout(new BorderLayout());
            
            formSelector = new JSelectForm(Repository.systemRepository(), FormDenotatorTypeEnum.LIMIT);
            formSelector.addActionListener(this);
            properties.add(formSelector, BorderLayout.NORTH);

            formListModel = new FormListModel();
            formList = new JList(formListModel);
            JScrollPane scrollPane = new JScrollPane(formList);
            scrollPane.setBorder(makeTitledBorder("Output Forms"));
            
            properties.add(scrollPane, BorderLayout.CENTER);
            
            if (form != null) {
                formSelector.setForm(form);
                formListModel.setForm(form);
                if (selectedForms != null) {
                    formList.setSelectedIndices(selectedForms);
                }
            }
        }
        return properties;
    }


    public boolean applyProperties() {
        return set((LimitForm)formSelector.getForm(), formList.getSelectedIndices());
    }


    public void revertProperties() {
        formSelector.setForm(form);
        formListModel.setForm(form);
        if (selectedForms != null) {
            formList.setSelectedIndices(selectedForms);
        }
    }

    
    public String getShortDescription() {
        return "Splits the input denotator into its factors";
    }

    public String getLongDescription() {
        return "The Split Rubette decomposes"+
               " its input denotator of type limit into"+
               " its factors.";
    }


    public String getInTip(int i) {
        return "Input denotator";
    }


    public String getOutTip(int i) {
        if (outTip != null) {
            return outTip[i];
        }
        else {
            return "Output denotator";
        }
    }


    public boolean set(LimitForm limitForm, int[] selected) {
        if (limitForm != null) {
            form = limitForm;
            formName = form.getNameString()+": "+form.getTypeString();

            if (selected != null && selected.length <= 8) {
                selectedForms = selected;
                setOutCount(selectedForms.length);
                outTip = new String[selectedForms.length];
                for (int i = 0; i < selectedForms.length; i++) {
                    Form f = form.getForm(selectedForms[i]);
                    outTip[i] = f.getNameString()+": "+f.getTypeString();
                }
            }
            else {
                setOutCount(0);
                selectedForms = null;
                outTip = null;
            }
            return true;
        }
        else {
            return false;
        }
    }
    
    
    public Form getForm() {
        return form;        
    }
        
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == formSelector) {
            formListModel.setForm((LimitForm)formSelector.getForm());
        }
    }
    
    private JPanel        properties = null;
    private JSelectForm   formSelector = null;
    private JList         formList = null;
    private FormListModel formListModel = null;
    @Getter
    private int[]         selectedForms = null;
    private LimitForm     form = null;
    private String        formName = " ";
    private String[]      outTip;

    protected class FormListModel extends DefaultListModel {

                public void setForm(LimitForm limitForm) {
            removeAllElements();            
            forms = null;
            if (limitForm != null) {
                String label;
                forms = new Form[limitForm.getFormCount()];
                for (int i = 0; i < limitForm.getFormCount(); i++) {
                    forms[i] = limitForm.getForm(i);
                    label = "";
                    if (limitForm.hasLabels()) {
                        label = "["+limitForm.indexToLabel(i)+"] ";
                    }
                    addElement(label+forms[i].getNameString()+": "+forms[i].getTypeString());
                }
            }
        }
        
        public Form getForm(int i) {
            return forms[i];
        }
        
        private Form[]    forms = null;
    }

}
