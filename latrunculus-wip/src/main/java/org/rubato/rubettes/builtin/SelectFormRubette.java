/*
 * Copyright (C) 2006 Gérard Milmeister
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
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.logeo.DenoFactory;
import org.vetronauta.latrunculus.core.logeo.Select;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.core.util.TextUtils;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelectFormRubette extends AbstractRubette {

    public SelectFormRubette() {
        setInCount(1);
        setOutCount(1);
    }
    
    
    public void run(RunInfo runInfo) {
        Denotator input = getInput(0);
        Denotator output = null;
        if (input == null) {
            addError("Input denotator is null.");
        }
        else if (outputForm != null) {
            try {
                List<Denotator> denoList = Select.select(baseForm, input);
                output = DenoFactory.makeDenotator(outputForm, denoList);                
            }
            catch (LatrunculusCheckedException e) {
                addError(e.getMessage()+".");
            }
        }
        setOutput(0, output);
    }

    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }
    

    public String getName() {
        return "SelectForm"; 
    }

    
    public Rubette duplicate() {
        SelectFormRubette rubette = new SelectFormRubette();
        rubette.setOutputForm(outputForm);
        return rubette;
    }
    
    
    public boolean hasInfo() {
        return true;
    }
    
    
    public String getInfo() {
        if (outputForm == null) {
            return "Not set";
        }
        else {
            return outputForm.getNameString();
        }
    }
    

    public boolean hasProperties() {
        return true;
    }


    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();            
            properties.setLayout(new BorderLayout());
            selectForm = new JSelectForm(Repository.systemRepository(), FormDenotatorTypeEnum.LIST, FormDenotatorTypeEnum.POWER);
            selectForm.setForm(outputForm);
            properties.add(selectForm, BorderLayout.CENTER);
        }
        return properties;
    }


    public boolean applyProperties() {
        setOutputForm(selectForm.getForm());
        return true;
    }


    public void revertProperties() {
        selectForm.setForm(outputForm);
    }

    
    public String getShortDescription() {
        return "Selects all denotators of a given form";
    }

    public String getLongDescription() {
        return "The SelectForm Rubette selects all denotators "+
               "of a given form from the input denotators "+
               "and creates the result as a denotator of "+
               "type power or list.";
    }


    public String getInTip(int i) {
        return "Input denotator"; 
    }


    public String getOutTip(int i) {
        if (outputForm != null) {
            return TextUtils.replaceStrings("Output denotator of form %%1", outputForm.getNameString());
        }
        else {
            return "Output denotator";
        }
    }

    public void setOutputForm(Form form) {
        if (form == null) {
            outputForm = null;
            baseForm = null;
        }
        else if (form instanceof PowerForm) {
            outputForm = form;
            baseForm = ((PowerForm)form).getForm();
        }
        else if (form instanceof ListForm) {
            outputForm = form;
            baseForm = ((ListForm)form).getForm();
        }
        else {
            outputForm = null;
            baseForm = null;
        }
    }

    
    private JPanel properties = null;
    private JSelectForm selectForm = null;
    @Getter
    private Form outputForm = null; 
    private Form baseForm = null;
}
