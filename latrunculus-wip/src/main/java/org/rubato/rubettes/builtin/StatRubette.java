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
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;
import org.rubato.composer.components.JSelectForm;
import org.rubato.composer.icons.Icons;
import org.vetronauta.latrunculus.core.logeo.DenoFactory;
import org.vetronauta.latrunculus.core.logeo.Select;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.generic.FreeElement;
import org.vetronauta.latrunculus.core.math.module.generic.FreeModule;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.vetronauta.latrunculus.server.xml.XMLConstants;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.w3c.dom.Element;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.List;

import static org.rubato.composer.Utilities.makeTitledBorder;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.OPERATION;
import static org.vetronauta.latrunculus.plugin.xml.PluginXmlConstants.OP_ATTR;

public class StatRubette extends AbstractRubette {

    public StatRubette() {
        setInCount(1);
        setOutCount(1);
    }
    

    public void run(RunInfo runInfo) {
        Denotator input = getInput(0);
        Denotator res = null;
        if (input == null) {
            addError("Input denotator is null.");
        }
        else if (form != null && form instanceof SimpleForm) {
            try {
                List<Denotator> denoList = Select.select(form, input);
                switch (op) {
                case MIN: {
                    res = doMinMax(denoList, -1);
                    break;
                }
                case MAX: {
                    res = doMinMax(denoList, 1);
                    break;
                }
                case MEAN: {
                    res = doMean(denoList);
                    break;
                }
                case VARIANCE: {
                    res = doVariance(denoList);
                    break;
                }
                case SUM: {
                    res = doSum(denoList);
                    break;
                }
                case PRODUCT: {
                    res = doProduct(denoList);
                    break;
                }
                }
            }
            catch (LatrunculusCheckedException e) {
                addError(e);
            }
        }
        else {
            addError("Form is not set or is not of type simple.");
        }
        setOutput(0, res);
    }


    private Denotator doMinMax(List<Denotator> denoList, int i) {
        Denotator res = null;
        if (denoList.size() > 0) {
            Iterator<Denotator> iter = denoList.iterator();
            res = iter.next();
            while (iter.hasNext()) {
                Denotator d = iter.next();
                res = (i*res.compareTo(d))<0?d:res;
            }
        }
        else {
            addError("Number of denotators must be at least 1.");
        }
        return res;
    }
    
    
    private Denotator doMean(List<Denotator> denoList) {
        Denotator res = null;
        if (denoList.size() > 0) {
            Iterator<Denotator> iter = denoList.iterator();
            Denotator d = iter.next();
            ModuleElement cur = (ModuleElement) ((SimpleDenotator)d).getElement().deepCopy();
            try {
                while (iter.hasNext()) {
                    d = iter.next();
                    ModuleElement m = ((SimpleDenotator)d).getElement();
                    cur.add(m);
                }
                divide(cur, denoList.size());
                res = DenoFactory.makeDenotator(form, cur);
            }
            catch (LatrunculusCheckedException e) {
                addError(e);
            }
        }
        else {
            addError("Number of denotators must be at least 1.");
        }
        return res;
    }
    
    
    private Denotator doVariance(List<Denotator> denoList) {
        Denotator res = null;
        if (!(((SimpleForm)form).getModule() instanceof FreeModule)) {
            addError("In the case of product, module elements must be free elements.");
        }
        else if (denoList.size() > 0) {
            Iterator<Denotator> iter;
            Denotator d;
            ModuleElement cur;
            try {
                // compute mean
                iter = denoList.iterator();
                d = iter.next();
                cur = (ModuleElement) ((SimpleDenotator)d).getElement().deepCopy();
                while (iter.hasNext()) {
                    d = iter.next();
                    ModuleElement m = ((SimpleDenotator)d).getElement();
                    cur.add(m);
                }
                divide(cur, denoList.size());
                ModuleElement mean = cur;

                // compute variance
                iter = denoList.iterator();
                cur = mean.getModule().getZero();
                while (iter.hasNext()) {
                    d = iter.next();
                    ModuleElement m = ((SimpleDenotator)d).getElement();
                    FreeElement t = (FreeElement)m.difference(mean);
                    cur.add(t.productCW(t));
                }
                divide(cur, denoList.size());
                
                res = DenoFactory.makeDenotator(form, cur);
            }
            catch (LatrunculusCheckedException e) {
                addError(e);
            }
        }
        else {
            addError("Number of denotators must be at least 1.");
        }
        return res;
    }
    
    
    private Denotator doSum(List<Denotator> denoList) {
        Denotator res = null;
        if (denoList.size() > 0) {
            Iterator<Denotator> iter = denoList.iterator();
            Denotator d = iter.next();
            ModuleElement cur = (ModuleElement) ((SimpleDenotator)d).getElement().deepCopy();
            try {
                while (iter.hasNext()) {
                    d = iter.next();
                    ModuleElement m = ((SimpleDenotator)d).getElement();
                    cur.add(m);
                }
                res = DenoFactory.makeDenotator(form, cur);
            }
            catch (DomainException e) {
                addError(e);
            }
        }
        else {
            addError("Number of denotators must be at least 1.");
        }
        return res;
    }
    
    
    private Denotator doProduct(List<Denotator> denoList) {
        Denotator res = null;
        if (!(((SimpleForm)form).getModule() instanceof FreeModule)) {
            addError("In the case of product, module elements must be free elements.");
        }
        else if (denoList.size() > 0) {
            Iterator<Denotator> iter = denoList.iterator();
            Denotator d = iter.next();
            FreeElement cur = (RingElement)((SimpleDenotator)d).getElement().deepCopy();
            try {
                while (iter.hasNext()) {
                    d = iter.next();
                    FreeElement m = (FreeElement)((SimpleDenotator)d).getElement();
                    cur.multiplyCW(m);
                }
                res = DenoFactory.makeDenotator(form, cur);
            }
            catch (DomainException e) {
                addError(e);
            }
        }
        else {
            addError("Number of denotators must be at least 1.");
        }
        return res;
    }
    
    
    private void divide(ModuleElement e, int s) throws LatrunculusCheckedException {
        Ring r = e.getModule().getRing();
        if (r.equals(RRing.ring)) {
            e.scale(new Real((1/(double)s)));
        }
        else if (r.equals(QRing.ring)) {
            e.scale(new Rational(1, s));
        }
        else if (r.equals(CRing.ring)) {
            e.scale(new Complex(1/(double)s));
        }
        else {
            throw new LatrunculusCheckedException("Cannot take averages over ring "+r);
        }
    }
    
    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "Stat";
    }

    
    public boolean hasInfo() {
        return true;
    }
    
    
    public String getInfo() {
        String s = opNames[op];
        if (form != null && form.getType() == FormDenotatorTypeEnum.SIMPLE) {
            s += "("+form.getNameString()+")";
        }
        return s;
    }

    
    public ImageIcon getIcon() {
        return icon;
    }
    

    public String getShortDescription() {
        return "Perform several statistical operations";
    }
    
    
    public String getLongDescription() {
        return "The Stat Rubette performs several statistical "+
               "operations such as minimum, maximum and average "+
               "on the selected set of denotators of the specified "+
               "form.";
    }
    
    
    public boolean hasProperties() {
        return true;
    }
    
    
    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();
            properties.setLayout(new BorderLayout());
            
            selectForm = new JSelectForm(Repository.systemRepository(), FormDenotatorTypeEnum.SIMPLE);
            selectForm.setBorder(makeTitledBorder("Form to extract"));
            selectForm.setForm(form);
            properties.add(selectForm, BorderLayout.NORTH);

            JLabel opLabel = new JLabel("Function"+": ");
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
        form = selectForm.getForm();
        op = opSelect.getSelectedIndex();
        return true;
    }
    
    
    public void revertProperties() {
        selectForm.setForm(form);
        opSelect.setSelectedIndex(op);
    }

    
    public Rubette duplicate() {
        StatRubette rubette = new StatRubette();
        rubette.op = op;
        rubette.form = form;
        return rubette;
    }

    public Rubette fromXML(XMLReader reader, Element element) {
        // read operation type
        Element child = XMLReader.getChild(element, OPERATION);
        if (child == null) {
            return null;
        }
        int op0 = XMLReader.getIntAttribute(child, OP_ATTR, 0, opNames.length-1, 0);
        
        Form form0 = null;
        child = XMLReader.getNextSibling(child, XMLConstants.FORM);
        if (child != null) {
            form0 = reader.parseAndResolveForm(child);
        }
        
        StatRubette newRubette = new StatRubette();
        newRubette.op = op0;
        newRubette.form = form0;
        return newRubette;
    }


    private JPanel      properties = null;
    private JSelectForm selectForm = null;
    private JComboBox   opSelect   = null;

    private static final ImageIcon icon;

    @Getter
    private int  op   = MIN;
    @Getter
    private Form form = null;

    //TODO enum
    private static final int MIN      = 0;
    private static final int MAX      = 1;
    private static final int MEAN     = 2;
    private static final int VARIANCE = 3;
    private static final int SUM      = 4;
    private static final int PRODUCT  = 5;

    private static final String[] opNames = {
        "Minimum",
        "Maximum",
        "Mean",
        "Variance",
        "Sum",
        "Product"
    };
    
    static {
        icon = Icons.loadIcon(StatRubette.class, "/images/rubettes/builtin/staticon.png");
    }
}
