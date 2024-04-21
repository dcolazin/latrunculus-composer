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

import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORM;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.plugin.base.RunInfo;
import org.rubato.composer.components.JSelectForm;
import org.rubato.composer.icons.Icons;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ColimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.ColimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ListDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;


/**
 * The Constructor Rubette creates a denotator
 * of the form specified in the properties
 * with coordinates at the input connectors.
 * 
 * @author Gérard Milmeister
 */
public class ConstructorRubette extends AbstractRubette {
    
    public ConstructorRubette() {
        setInCount(0);
        setOutCount(1);
    }
    
    
    public void run(RunInfo runInfo) {
        if (form == null) {
            addError(BuiltinMessages.getString("ConstructorRubette.noform"));
            return;            
        }
        switch (form.getType()) {
            case LIMIT: {
                createLimit();
                break;
            }
            case COLIMIT: {
                createColimit();
                break;
            }
            case LIST: {
                createList();
                break;
            }
            case POWER: {
                createPower();
                break;
            }
            default: {
                addError(BuiltinMessages.getString("ConstructorRubette.formwrongtype"));
            }
        }
    }

    
    private void createLimit() {
        LinkedList<Denotator> cds = new LinkedList<Denotator>();
        for (int i = 0; i < getInCount(); i++) {
            Denotator in = getInput(i);
            if (in == null) {
                addError("Input denotator #"+i+" must not be null.");
                return;
            }
            else {
                cds.add(in);
            }
        }
        Denotator out = null;
        try {
            out = new LimitDenotator(null, (LimitForm)form, cds);
        }
        catch (Exception e) {
            addError(e);
            return;
        }
        setOutput(0, out);
    }

    
    private void createColimit() {
        boolean hasInput = false;
        int index = 0;
        Denotator inputDenotator = null;
        for (int i = 0; i < getInCount(); i++) {
            Denotator d = getInput(i);
            if (d != null) {
                if (hasInput) {
                    addError(BuiltinMessages.getString("ConstructorRubette.onlyonenonnullinput"));
                    return;
                }
                inputDenotator = d;
                index = i;
                hasInput = true;
            }
        }
        if (!hasInput) {
            addError(BuiltinMessages.getString("ConstructorRubette.atleastonenonnullinput"));
            return;
        }
        Denotator out = null;
        try {
            out = new ColimitDenotator(null, (ColimitForm)form, index, inputDenotator);
        }
        catch (Exception e) {
            addError(e);
            return;
        }
        setOutput(0, out);
    }

    
    private void createList() {
        LinkedList<Denotator> cds = new LinkedList<Denotator>();
        for (int i = 0; i < getInCount(); i++) {
            Denotator d = getInput(i);
            if (d != null) {
                cds.add(d);
            }
        }
        if (cds.size() == 0) {
            addError(BuiltinMessages.getString("ConstructorRubette.atleastonenonnullinput"));
            return;
        }
        Denotator out = null;
        try {
            out = new ListDenotator(null, (ListForm)form, cds);
        }
        catch (Exception e) {
            addError(e);
            return;
        }
        setOutput(0, out);
    }


    private void createPower() {
        LinkedList<Denotator> cds = new LinkedList<Denotator>();
        for (int i = 0; i < getInCount(); i++) {
            Denotator d = getInput(i);
            if (d != null) {
                cds.add(d);
            }
        }
        if (cds.size() == 0) {
            addError(BuiltinMessages.getString("ConstructorRubette.atleastonenonnullinput"));
            return;
        }
        Denotator out = null;
        try {
            out = new PowerDenotator(null, (PowerForm)form, cds);
        }
        catch (Exception e) {
            addError(e);
            return;
        }
        setOutput(0, out);
    }


    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "Constructor"; 
    }

    
    public Rubette duplicate() {
        ConstructorRubette newRubette = new ConstructorRubette();
        newRubette.setForm(getForm());
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
            ArrayList<FormDenotatorTypeEnum> types = new ArrayList<>();
            types.add(FormDenotatorTypeEnum.LIMIT);
            types.add(FormDenotatorTypeEnum.COLIMIT);
            types.add(FormDenotatorTypeEnum.POWER);
            types.add(FormDenotatorTypeEnum.LIST);
            formSelector = new JSelectForm(Repository.systemRepository(), types);
            if (form != null) {
                formSelector.setForm(form);
            }
            properties.add(formSelector, BorderLayout.CENTER);
        }
        return properties;
    }


    public boolean applyProperties() {
        return setForm(formSelector.getForm());
    }


    public void revertProperties() {
        formSelector.setForm(form);
    }

    
    public String getShortDescription() {
        return "Creates a denotator of a given form";
    }

    
    public ImageIcon getIcon() {
        return icon;
    }
    

    public String getLongDescription() {
        return "The Constructor Rubette creates a denotator"+
               " of the form specified in the properties"+
               " with coordinates at the input connectors.";
    }


    public String getInTip(int i) {
        return (inTip == null)?"Input denotator #"+i:inTip[i]; 
    }


    public String getOutTip(int i) {
        return "Output denotator"; 
    }


    public boolean setForm(Form form) {
        if (form != null) {
            this.form = form;
            formName = form.getNameString()+": "+form.getTypeString(); 
            switch (form.getType()) {
                case LIST:
                case POWER: {
                    int formCount = 8;
                    Form factorForm = form.getForm(0);
                    setInCount(formCount);
                    inTip = new String[formCount];
                    for (int i = 0; i < formCount; i++) {
                        inTip[i] = factorForm.getNameString()+": "+factorForm.getTypeString();  
                    }
                    break;
                }
                case LIMIT:
                case COLIMIT: {
                    int formCount = form.getFormCount();
                    setInCount(formCount);
                    inTip = new String[formCount];
                    for (int i = 0; i < formCount; i++) {
                        Form factorForm = form.getForm(i);
                        inTip[i] = factorForm.getNameString()+": "+factorForm.getTypeString(); 
                    }
                    break;
                }
                default: {
                    return false;
                }
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
    
    public Rubette fromXML(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, FORM);
        Form f = reader.parseAndResolveForm(child);
        ConstructorRubette newRubette = new ConstructorRubette();
        newRubette.setForm(f);
        return newRubette;
    }

    
    private JPanel      properties = null;
    private JSelectForm formSelector = null;
    private Form        form = null;
    private String      formName = " "; 
    private String[]    inTip = null;
    private static final ImageIcon icon;

    static {
        icon = Icons.loadIcon(ConstructorRubette.class, "/images/rubettes/builtin/constricon.png"); 
    }
}
