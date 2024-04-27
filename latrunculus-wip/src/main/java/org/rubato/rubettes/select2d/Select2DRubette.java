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

package org.rubato.rubettes.select2d;

import org.vetronauta.latrunculus.client.plugin.icons.Icons;
import org.vetronauta.latrunculus.core.logeo.DenoFactory;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.FactorDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Select2DRubette extends AbstractRubette {

    public Select2DRubette() {
        setInCount(1);
        setOutCount(2);
    }

    public Select2DRubette(Form form, ArrayList<Select2DPanel> selections) {
        this();
        this.form = form;
        this.selections = selections;
    }

    
    public void run(RunInfo runInfo) {
        Denotator input = getInput(0);
        if (input == null) {
            addError(INPUTNULL_ERROR);
        }
        else if (form == null) {
            addError(FORMNOTSET_ERROR);
        }
        else if (input.hasForm(form)){
            List<Denotator> denoList = ((FactorDenotator)input).getFactors();
            denotators = new ArrayList<Denotator>(denoList.size());
            for (Denotator d : denoList) {
                denotators.add(d);
            }
            
            if (select2DDialog != null) {
                Denotator output0, output1;
                select2DDialog.setDenotators(denotators);
                List<Denotator> selected = new LinkedList<Denotator>();
                List<Denotator> notSelected = new LinkedList<Denotator>();
                if (select2DDialog.hasSelections()) {
                    select2DDialog.getSelectedDenotators(selected, notSelected);
                    output0 = DenoFactory.makeDenotator(form, selected);
                    output1 = DenoFactory.makeDenotator(form, notSelected);
                }
                else {
                    // if there is no selection  at all,
                    // simply pass through input
                    output0 = input;
                    output1 = input.getForm().createDefaultDenotator(input.getAddress());
                }
                setOutput(0, output0);
                setOutput(1, output1);
            }
            else {
                setOutput(0, input);
                setOutput(1, input);
            }
        }
    }


    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "Select2D"; 
    }

    public boolean hasInfo() {
        return true;
    }
    
    
    public String getInfo() {
        if (form == null) {
            return FORMNOTSET_INFO;
        }
        else {
            return form.getNameString()+": "+form.getTypeString(); 
        }
    }
    
    
    public String getShortDescription() {
        return "Selects a subset of a denotator of "+ 
               "type power or list"; 
    }
    
    
    public String getLongDescription() {
        return "The Select2D Rubette selects a subset of "+ 
               "a denotator of type power or list according "+ 
               "to 2D graphical selection criteria specfied "+ 
               "in the properties."; 
    }
    
    
    public boolean hasProperties() {
        return true;
    }
    
    
    public JComponent getProperties() {
        if (select2DDialog ==  null) {
            select2DDialog = new Select2DDialog();
        }
        if (form != null) {
            // set properties
            select2DDialog.setForm(form);
            if (selections != null) {
                select2DDialog.setSelections(selections);
            }
            if (denotators != null) {
                select2DDialog.setDenotators(denotators);
            }
        }
        return select2DDialog;
    }

    
    public void revertProperties() {
        select2DDialog.setForm(form);
        select2DDialog.setSelections(selections);
    }
    
    
    public boolean applyProperties() {
        form = select2DDialog.getForm();
        selections = select2DDialog.getSelections();
        return true;
    }

    
    public String getInTip(int i) {
        if (form == null) {
            return Select2DRubetteMessages.getString("Select2DRubette.inputdenotip");
        }
        else {
            return Select2DRubetteMessages.getString("Select2DRubette.inputdenoformtip")+form.getNameString();
        }
    }


    public String getOutTip(int i) {
        if (form == null) {
            return Select2DRubetteMessages.getString("Select2DRubette.outputdenotip");
        }
        else {
            return Select2DRubetteMessages.getString("Select2DRubette.outputdenoformtip")+form.getNameString();
        }
    }

    
    public Rubette newInstance() {
        return new Select2DRubette();
    }
    
    
    public Rubette duplicate() {
        Select2DRubette rubette = new Select2DRubette();
        rubette.setForm(getForm());
        for (Select2DPanel s : getSelections()) {
            rubette.addSelection(s.duplicate());
        }
        rubette.getProperties();
        return rubette;
    }    

    
    public Form getForm() {
        return form;
    }
    
    
    public void setForm(Form form) {
        this.form = form;
    }
    
    
    public ArrayList<Select2DPanel> getSelections() {
        if (selections == null) {
            selections = new ArrayList<>();
        }
        return selections;
    }
    
    
    public void addSelection(Select2DPanel selection) {
        if (selections == null) {
            selections = new ArrayList<>();
        }
        selections.add(selection);
    }

    // properties
    private Form                     form = null;
    private ArrayList<Select2DPanel> selections = new ArrayList<Select2DPanel>();
    
    // volatile data
    private Select2DDialog           select2DDialog = null;
    private ArrayList<Denotator>     denotators = null;

    private static final String INPUTNULL_ERROR  = Select2DRubetteMessages.getString("Select2DRubette.inputnullerror"); ;
    private static final String FORMNOTSET_ERROR = Select2DRubetteMessages.getString("Select2DRubette.formnotseterror"); ;
    private static final String FORMNOTSET_INFO  = Select2DRubetteMessages.getString("Select2DRubette.noformsetinfo"); ;

}
