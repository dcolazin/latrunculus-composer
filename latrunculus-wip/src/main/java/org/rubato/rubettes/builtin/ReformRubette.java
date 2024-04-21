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
import org.rubato.composer.icons.Icons;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.logeo.reform.Reformer;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.core.util.TextUtils;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;

import javax.swing.*;
import java.awt.*;


public class ReformRubette extends AbstractRubette {

    public ReformRubette() {
        setInCount(1);
        setOutCount(1);
    }

    public ReformRubette(Form form) {
        this();
        this.outputForm = form;
    }

    public void run(RunInfo runInfo) {
        Denotator d = getInput(0);
        if (d == null) {
            addError("Input denotator is null.");
        }
        else if (outputForm == null) {
            addError("Output form is not set.");
        }
        else {
            Form inputForm = d.getForm();
            Reformer reformer = Reformer.make(inputForm, outputForm);
            if (reformer == null) {
                addError("Cannot reform %%1 to %%2.", inputForm.getNameString(), outputForm.getNameString());
            }
            else {
                try {
                    setOutput(0, reformer.reform(d));
                }
                catch (LatrunculusCheckedException e) {
                    addError(e);
                }
            }
        }
    }

    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }
    

    public String getName() {
        return "Reform"; 
    }

    
    public Rubette duplicate() {
        ReformRubette rubette = new ReformRubette();
        rubette.outputForm = outputForm;
        return rubette;
    }

    
    public boolean hasProperties() {
        return true;
    }


    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();            
            properties.setLayout(new BorderLayout());
            selectForm = new JSelectForm(Repository.systemRepository());
            selectForm.setForm(outputForm);
            properties.add(selectForm, BorderLayout.CENTER);
        }
        return properties;
    }


    public boolean applyProperties() {
        outputForm = selectForm.getForm();
        return true;
    }


    public void revertProperties() {
        selectForm.setForm(outputForm);
    }

    
    public String getShortDescription() {
        return "Reforms a denotator";
    }

    
    public ImageIcon getIcon() {
        return icon;
    }
    

    public String getLongDescription() {
        return "The Reform Rubette cast its input denotator"+
               " to a denotator with a specified form.";
    }


    public String getInTip(int i) {
        return "Input denotator"; 
    }


    public String getOutTip(int i) {
        if (outputForm == null) {
            return "Output denotator"; 
        }
        else {
            return TextUtils.replaceStrings("Output dennotator of form %%1", outputForm.getNameString());
        }
    }

    private JPanel      properties = null;
    private JSelectForm selectForm = null;
    @Getter
    private Form        outputForm = null;
    
    private static final ImageIcon icon;

    static {
        icon = Icons.loadIcon(ReformRubette.class, "/images/rubettes/builtin/reformicon.png"); 
    }
}
