/*
 * Copyright (C) 2005, 2006 Gérard Milmeister
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

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import org.rubato.base.*;
import org.rubato.base.RunInfo;
import org.rubato.composer.components.JConnectorSliders;
import org.rubato.composer.icons.Icons;
import org.rubato.logeo.DenoFactory;
import org.rubato.logeo.Sets;
import org.vetronauta.latrunculus.core.exception.RubatoException;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.w3c.dom.Element;


public class SetRubette extends AbstractRubette {

    public SetRubette() {
        setInCount(2);
        setOutCount(1);
    }
    

    public void run(RunInfo runInfo) {
        Denotator res = null;
        switch (op) {
            case UNION: {
                res = doUnion(inputsToArray());
                break;
            }
            case INTERSECTION: {
                res = doIntersection(inputsToArray());
                break;
            }
            case DIFFERENCE: {
                res = doDifference(inputsToArray());
                break;
            }
            case SYMMETRIC: {
                res = doSymmetric(inputsToArray());
                break;
            }
            case ADDELEMENT: {
                res = doAddElements();
                break;
            }
            case UNIONALL: {
                res = doUnionAll(getInput(0));
                break;
            }
            case INTERALL: {
                res = doInterAll(getInput(0));
                break;
            }
        }
        setOutput(0, res);
    }

    
    private PowerDenotator[] inputsToArray() {
        Form baseForm = null;
        LinkedList<PowerDenotator> denoList = new LinkedList<PowerDenotator>();
        for (int i = 0; i < getInCount(); i++) {
            PowerDenotator d = toPowerDenotator(getInput(i), i);
            if (d != null) {
                if (baseForm == null) {
                    baseForm = d.getPowerForm().getForm();
                    denoList.add(d);
                }
                else {
                    if (!d.getPowerForm().getForm().equals(baseForm)) {
                        addError(Messages.getString("SetRubette.wrongbaseform"), i); 
                    }
                    else {
                        denoList.add(d);
                    }
                }
            }
        }
        PowerDenotator[] res = new PowerDenotator[denoList.size()];
        int i = 0;
        for (PowerDenotator d : denoList) {
            res[i++] = d;
        }
        return res;
    }
    
    
    private PowerDenotator toPowerDenotator(Denotator d, int i) {
        if (d == null) {
            return null;
        }
        else if (d instanceof PowerDenotator) {
            return (PowerDenotator)d;
        }
        else {
            addError(Messages.getString("SetRubette.notpowererror"), i); 
            return null;
        }
    }
    
    
    private Denotator doUnion(PowerDenotator[] denotators) {
        if (denotators.length == 0) {
            addError(Messages.getString("SetRubette.novalidinput")); 
            return null;
        }
        PowerDenotator res = null;
        try {
            res = Sets.union(denotators);
        }
        catch (RubatoException e) {
            addError(e);
        }
        return res;
    }

    
    private Denotator doIntersection(PowerDenotator[] denotators) {
        if (denotators.length == 0) {
            addError(Messages.getString("SetRubette.novalidinput")); 
            return null;
        }
        PowerDenotator res = null;
        try {
            res = Sets.intersection(denotators);
        }
        catch (RubatoException e) {
            addError(e);
        }
        return res;
    }

    
    private Denotator doSymmetric(PowerDenotator[] denotators) {
        if (denotators.length == 0) {
            addError(Messages.getString("SetRubette.novalidinput")); 
            return null;
        }
        PowerDenotator res = null;
        try {
            res = Sets.symmetric(denotators);
        }
        catch (RubatoException e) {
            addError(e);
        }
        return res;
    }

    
    private Denotator doDifference(PowerDenotator[] denotators) {
        if (denotators.length == 0) {
            addError(Messages.getString("SetRubette.novalidinput")); 
            return null;
        }
        PowerDenotator res = null;
        try {
            res = Sets.difference(denotators);
        }
        catch (RubatoException e) {
            addError(e);
        }
        return res;
    }

    
    private Denotator doAddElements() {
        Denotator d = getInput(0);
        if (d == null) {
            addError(Messages.getString("SetRubette.notnull")); 
            return null;
        }
        else if (!(d instanceof PowerDenotator)) {
            addError(Messages.getString("SetRubette.powerdeno")); 
            return null;
        }
        PowerDenotator p = (PowerDenotator)d;
        Form baseForm = p.getPowerForm().getForm();
        
        List<Denotator> denoList = new LinkedList<Denotator>();
        for (int i = 1; i < getInCount(); i++) {
            Denotator di = getInput(i);
            if (di != null) {
                if (di.getForm().equals(baseForm)) {
                    denoList.add(di);
                }
                else {
                    addError(Messages.getString("SetRubette.wrongform"), i+1); 
                }
            }
        }
        
        PowerDenotator res = null;
        try {
            res = Sets.addElements(p, denoList);
        }
        catch (RubatoException e) {
            addError(e);
        }
        return res;
    }

    
    private Denotator doUnionAll(Denotator input) {
        if (input instanceof PowerDenotator) {
            PowerDenotator d = (PowerDenotator)input;
            Form f = d.getPowerForm().getForm();
            if (f instanceof PowerForm) {
                Form baseForm = f;
                List<Denotator> factors = d.getFactors();
                List<Denotator> denoList = new LinkedList<Denotator>();
                for (Denotator factor : factors) {
                    PowerDenotator p = (PowerDenotator)factor;
                    denoList.addAll(p.getFactors());                    
                }
                return DenoFactory.makeDenotator(baseForm, denoList);
            }
            else {
                addError(Messages.getString("SetRubette.basenotpower")); 
                return null;
            }
        }
        else {
            addError(Messages.getString("SetRubette.notpower")); 
            return null;
        }
    }
    

    private Denotator doInterAll(Denotator input) {
        if (input instanceof PowerDenotator) {
            PowerDenotator d = (PowerDenotator)input;
            Form f = d.getPowerForm().getForm();
            if (f instanceof PowerForm) {
                List<Denotator> factors = d.getFactors();
                Iterator<Denotator> iter = factors.iterator();
                if (!iter.hasNext()) {
                    addError(Messages.getString("SetRubette.onefactor")); 
                    return null;                    
                }
                else {
                    try {
                        PowerDenotator factor = (PowerDenotator)iter.next();
                        while (iter.hasNext()) {
                            factor = Sets.intersection(factor, (PowerDenotator)iter.next());
                        }
                        return factor;
                    }
                    catch (RubatoException e) {
                        addError(e);
                        return null;
                    }
                }
            }
            else {
                addError(Messages.getString("SetRubette.basenotpower")); 
                return null;
            }
        }
        else {
            addError(Messages.getString("SetRubette.notpower")); 
            return null;
        }
    }
    
    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "Set"; 
    }

    

    public Rubette duplicate() {
        SetRubette newRubette = new SetRubette();
        newRubette.op = op;
        newRubette.setInCount(this.getInCount());
        return newRubette;
    }
    
    
    public boolean hasInfo() {
        return true;
    }
    
    
    public String getInfo() {
        return opNames[op];
    }

    
    public boolean hasProperties() {
        return true;
    }


    private void configureSlider(int operation) {
        if (operation == UNIONALL || operation == INTERALL) {
            inSlider.setInLimits(1, 1);
        }
        else {
            inSlider.setInLimits(2, 8);
        }
    }

    
    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();            
            properties.setLayout(new BorderLayout());

            inSlider = new JConnectorSliders(true, false);
            configureSlider(op);
            inSlider.setInValue(getInCount());
            properties.add(inSlider, BorderLayout.NORTH);
            
            JLabel opLabel = new JLabel(Messages.getString("SetRubette.operation")+": ");  
            properties.add(opLabel, BorderLayout.WEST);
            
            opSelect = new JComboBox();
            opSelect.setEditable(false);
            for (int i = 0; i < opNames.length; i++) {
                opSelect.addItem(opNames[i]);
            }
            opSelect.setSelectedIndex(op);
            
            properties.add(opSelect, BorderLayout.CENTER);
        }
        return properties;
    }


    public boolean applyProperties() {
        op = opSelect.getSelectedIndex();
        configureSlider(op);
        setInCount(inSlider.getInValue());
        return true;
    }


    public void revertProperties() {
        configureSlider(op);
        inSlider.setInValue(getInCount());
        opSelect.setSelectedIndex(op);
    }

    
    public String getShortDescription() {
        return "Performs a set operation on its input denotators"; 
    }

    
    public ImageIcon getIcon() {
        return icon;
    }
    

    public String getLongDescription() {
        return "The Set Rubette performs a set operation, e.g., "+ 
               "union or intersection, on its input denotators."; 
    }


    public String getInTip(int i) {
        return "Input denotator #"+i; 
    }


    public String getOutTip(int i) {
        return "Output denotator"; 
    }

    
    private static final String INPUTS      = "Inputs"; 
    private static final String NUMBER_ATTR = "number"; 
    private static final String OPERATION   = "Operation"; 
    private static final String OP_ATTR     = "op"; 
    
    public void toXML(XMLWriter writer) {
        writer.empty(OPERATION, OP_ATTR, op);
        writer.empty(INPUTS, NUMBER_ATTR, getInCount());
    }
    
    
    public Rubette fromXML(XMLReader reader, Element element) {
        // read operation type
        Element child = XMLReader.getChild(element, OPERATION);
        if (child == null) {
            return null;
        }
        int op0 = XMLReader.getIntAttribute(child, OP_ATTR, 0, opNames.length-1, 0);

        // read number of inputs
        child = XMLReader.getChild(element, INPUTS);
        if (child == null) {
            return null;
        }
        int n = XMLReader.getIntAttribute(child, NUMBER_ATTR, 1, 8, 2);
        
        SetRubette newRubette = new SetRubette();
        newRubette.op = op0;
        newRubette.setInCount(n);
        return newRubette;
    }

    
    private JPanel            properties = null;
    private JComboBox         opSelect = null;
    private JConnectorSliders inSlider = null;
    
    private static final ImageIcon icon;
    
    private int op = UNION;
    
    private static final int UNION        = 0;
    private static final int INTERSECTION = 1;
    private static final int DIFFERENCE   = 2;
    private static final int SYMMETRIC    = 3;
    private static final int ADDELEMENT   = 4;
    private static final int UNIONALL     = 5;
    private static final int INTERALL     = 6;

    private static final String[] opNames = {
            Messages.getString("SetRubette.union"), 
            Messages.getString("SetRubette.intersection"), 
            Messages.getString("SetRubette.difference"), 
            Messages.getString("SetRubette.symdiff"), 
            Messages.getString("SetRubette.addelement"), 
            Messages.getString("SetRubette.unionall"), 
            Messages.getString("SetRubette.interall") 
    };

    static {
       icon = Icons.loadIcon(SetRubette.class, "/images/rubettes/builtin/seticon.png"); 
    }
}
