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

package org.rubato.rubettes.builtin.address;

import org.apache.commons.collections4.CollectionUtils;
import org.rubato.base.AbstractRubette;
import org.rubato.base.Repository;
import org.rubato.base.RubatoConstants;
import org.rubato.base.RubatoException;
import org.rubato.base.Rubette;
import org.rubato.composer.RunInfo;
import org.rubato.composer.components.JModuleElementEntry;
import org.rubato.composer.components.JModuleElementList;
import org.rubato.composer.components.JModuleEntry;
import org.rubato.composer.components.JMorphismEntry;
import org.rubato.composer.components.JSelectForm;
import org.rubato.composer.components.JStatusline;
import org.rubato.composer.icons.Icons;
import org.rubato.logeo.DenoFactory;
import org.rubato.rubettes.builtin.address.JGraphSelect.QConfiguration;
import org.rubato.rubettes.builtin.address.JGraphSelect.RConfiguration;
import org.rubato.rubettes.builtin.address.JGraphSelect.ZConfiguration;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.arith.number.ModulusWrapper;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.module.FreeUtils;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.repository.RingRepository;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.FactorDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ListDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.server.xml.writer.DefaultDefinitionXmlWriter;
import org.vetronauta.latrunculus.server.xml.writer.LatrunculusXmlWriter;
import org.w3c.dom.Element;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.rubato.composer.Utilities.getJDialog;
import static org.rubato.composer.Utilities.makeTitledBorder;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_ELEMENT;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_MORPHISM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUE_ATTR;

/**
 * 
 * @author Gérard Milmeister
 */
public final class AddressEvalRubette extends AbstractRubette implements ActionListener {

    //TODO writer for rubettes and properties
    private final LatrunculusXmlWriter<MathDefinition> definitionXmlWriter = new DefaultDefinitionXmlWriter();

    public AddressEvalRubette() {
        setInCount(1);
        setOutCount(1);
    }
    

    public void run(RunInfo runInfo)  {
        Denotator input = getInput(0);
        Denotator res = null;
        if (input == null) {
            addError(INPUT_NULL_ERROR);
        }
        else {
            if (evalType == EVAL_TYPE_NULL) {
                res = input.atNull();
            }
            else if (evalType == EVAL_TYPE_ELEMENT) {
                res = runEvalTypeElement(input);
            }
            else if (evalType == EVAL_TYPE_LIST) {
                res = runEvalTypeList(input);
            }
            else if (evalType == EVAL_TYPE_CHANGE) {
                res = runEvalTypeChange(input);
            }
            else if (evalType == EVAL_TYPE_INPUT) {
                res = runEvalTypeInput(input);
            }
        }
        setOutput(0, res);
    }

    
    /**
     * Evaluates denotator <code>input</code> at the
     * configured module element.
     */
    private Denotator runEvalTypeElement(Denotator input) {
        Denotator res = null;
        if (moduleElement == null){
            // element not configured
            addError(ELEMENTNOTSET_ERROR);
        }
        else if (module == null) {
            // module not configured
            addError(MODULENOTSET_ERROR);
        }
        else if (!input.getAddress().equals(module)) {
            // address of the input denotator must be the same
            // as the configured module
            addError(ADDRESSMODULE_ERROR, input.getAddress(), module);
        }
        else {
            try {
                res = input.at(moduleElement);
            }
            catch (MappingException e) {
                addError(e);
            }
        }
        return res;
    }
    

    /**
     * Evaluates denotator <code>input</code> at the configured
     * list of elements. The result is a power or list denotator
     * whose form hat been configured before. The input denotator
     * must have the same form as the base form of the power
     * or list form.
     */
    private Denotator runEvalTypeList(Denotator input) {
        Denotator res = null;
        if (elements == null) {
            // not elements configured
            addError(LISTNOTSET_ERROR);
        }
        else if (module == null) {
            // module not configured
            addError(MODULENOTSET_ERROR);
        }
        else if (!input.getAddress().equals(module)) {
            // address of the input denotator must be the same
            // as the configured module
            addError(ADDRESSMODULE_ERROR, input.getAddress(), module);
        }
        else if (outputForm == null) {
            // output form must be configured
            addError(OUTPUTFORMNOTSET_ERROR);
        }
        else if (!input.hasForm(outputForm.getForm(0))) {
            // input form has not the form required by the
            // output power or list form
            addError(INPUT_WRONG_FORM, input.getForm(), outputForm.getForm(0));
        }
        else {
            try {
                LinkedList<Denotator> denoList = new LinkedList<>();
                for (ModuleElement e : elements) {
                    denoList.add(input.at(e));
                }
                if (outputForm instanceof PowerForm) {
                    res = new PowerDenotator(null, module.getNullModule(), (PowerForm)outputForm, denoList);
                }
                else if (outputForm instanceof ListForm) {
                    res = new ListDenotator(null, module.getNullModule(), (ListForm)outputForm, denoList);
                }
            }
            catch (MappingException e) {
                addError(e);
            }
            catch (RubatoException e) {
                addError(e);
            }
        }
        return res;
    }
    

    /**
     * Changes address of the input denotator using the configured
     * address changing morphism.
     */
    private Denotator runEvalTypeChange(Denotator input) {
        Denotator res = null;
        if (morphism == null) {
            addError(MORPHISMNOTSET_ERROR);
        }
        else if (!input.getAddress().equals(morphism.getCodomain())) {
            addError(ADDRESSMORPHISM_ERROR, input.getAddress(), morphism);
        }
        else {
            res = input.changeAddress(morphism);
        }
        return res;
    }

    
    /**
     * Evaluates the input denotator at the element(s) in
     * a second input denotator.
     */
    private Denotator runEvalTypeInput(Denotator input) {
        Denotator res = null;
        Denotator input2 = getInput(1);
        if (input2 == null) {
            addError(INPUT2NOTSET_ERROR);
        }
        else if (outputForm == null) {
            // if no output form is configured
            // 2nd input denotator must be of type simple
            if (input2 instanceof SimpleDenotator) {
                SimpleDenotator d = (SimpleDenotator)input2;
                ModuleElement el = d.getElement();
                try {
                    res = input.at(el);
                }
                catch (MappingException e) {
                    addError(INPUT2WRONGTYPE_ERROR);
                }
            }
            else {
                addError(INPUT2WRONGTYPE_ERROR);
            }
        }
        else {
            // if an output form is configured
            // 2nd input denotator may be of type power or list
            // containing denotators of type simple
            if (input2 instanceof PowerDenotator ||
                input2 instanceof ListDenotator) {
                List<Denotator> list = null;
                list = ((FactorDenotator)input2).getFactors();
                if (CollectionUtils.isEmpty(list)) {
                    res = DenoFactory.makeDenotator(outputForm);
                }
                else {
                    if (list.get(0) instanceof SimpleDenotator) {
                        try {
                            LinkedList<Denotator> reslist = new LinkedList<>();
                            for (Denotator d : list) {
                                reslist.add(input.at(((SimpleDenotator)d).getElement()));
                            }
                            res = DenoFactory.makeDenotator(outputForm, reslist);
                        }
                        catch (MappingException e) {
                            addError(INPUT2WRONGTYPE_ERROR);
                        }
                    }
                    else {
                        addError(INPUT2WRONGTYPE_ERROR);
                    }
                }
            }
            else if (input2 instanceof SimpleDenotator) {
                res = input.changeAddress(((SimpleDenotator)input2).getModuleMorphism());
                if (res == null) {
                    addError(INPUT2WRONGTYPE_ERROR);
                }
            }
            else {
                addError(INPUT2WRONGTYPE_ERROR);
            }
        }
        return res;
    }
    
    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "AddressEval"; 
    }

    
    public Rubette duplicate() {
        AddressEvalRubette rubette = new AddressEvalRubette();
        rubette.moduleElement = moduleElement;
        if (elements != null) {
            rubette.elements = new LinkedList<>(elements);
        }
        rubette.evalType = evalType;
        rubette.outputForm = outputForm;
        rubette.module = module;
        return rubette;
    }
    
    
    public boolean hasProperties() {
        return true;
    }
    
    
    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();
            properties.setLayout(new BorderLayout());
            
            Box box = Box.createVerticalBox();
            
            JPanel evalTypeSelectPanel = new JPanel();
            evalTypeSelectPanel.setLayout(new BorderLayout());
            evalTypeSelectPanel.setBorder(makeTitledBorder(Messages.getString("AddressEvalRubette.evaltype"))); 
            evalTypeSelect = new JComboBox(evalTypes);
            evalTypeSelect.setToolTipText(Messages.getString("AddressEvalRubette.evaltypetooltip")); 
            evalTypeSelect.setEditable(false);
            evalTypeSelect.setSelectedIndex(evalType);
            evalTypeSelect.addActionListener(this);
            evalTypeSelectPanel.add(evalTypeSelect, BorderLayout.CENTER);
            box.add(evalTypeSelectPanel);
            
            properties.add(box, BorderLayout.NORTH);
            
            addressPanel = new JPanel();
            addressPanel.setLayout(new BorderLayout());                        
            layoutAddressPanel(evalType);
            
            properties.add(addressPanel, BorderLayout.CENTER);
            
            statusline = new JStatusline();
            statusline.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));
            
            properties.add(statusline, BorderLayout.SOUTH);
            
        }
        return properties;
    }

    
    public boolean applyProperties() {
        statusline.clear();
        int t = evalTypeSelect.getSelectedIndex();
        if (t == EVAL_TYPE_ELEMENT) {
            Module mod = elementEntry.getModule();
            ModuleElement el = elementEntry.getModuleElement();
            if (mod == null) {
                statusline.setError(NOMODULE_ERROR);
                return false;
            }
            if (el == null) {
                statusline.setError(NOELEMENT_ERROR);
                return false;
            }
            evalType   = t;
            outputForm = null;
            module     = mod;
            moduleElement    = el;
        }
        else if (t == EVAL_TYPE_LIST) {
            Form oform = outputFormSelect.getForm();
            Module mod = listModuleEntry.getModule();
            List<ModuleElement> list = elementList.getElements();
            if (oform == null) {
                statusline.setError(NOOUTPUTFORM_ERROR);
                return false;
            }
            if (mod == null) {
                statusline.setError(NOMODULE_ERROR);
                return false;
            }
            if (list == null) {
                statusline.setError(NOELEMENTS_ERROR);
                return false;
            }
            evalType   = t;
            outputForm = oform;
            module     = mod;
            elements   = list;
        }
        else if (t == EVAL_TYPE_CHANGE) {
            ModuleMorphism m = morphismEntry.getMorphism();
            if (m == null) {
                statusline.setError(NOMORPHISM_ERROR);
                return false;
            }
            evalType   = t;
            outputForm = null;
            morphism   = m;
            elements   = null;
        }
        else if (t == EVAL_TYPE_INPUT) {
            evalType   = t;
            outputForm = null;
            morphism   = null;
            elements   = null;
            if (listButton.isSelected()) {
                Form oform = outputFormSelect.getForm();
                if (oform == null) {
                    statusline.setError(NOOUTPUTFORM_ERROR);
                    return false;
                }
                outputForm = oform;
            }
            setInCount(2);
        }
        else {            
            evalType   = EVAL_TYPE_NULL;
            outputForm = null;
            module     = null;
            moduleElement    = null;
        }
        return true;
    }

    
    public void revertProperties() {
        evalTypeSelect.setSelectedIndex(evalType);
        layoutAddressPanel(evalType);
        getJDialog(properties).pack();
    }
    
    
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == evalTypeSelect) {
            layoutAddressPanel(evalTypeSelect.getSelectedIndex());
            getJDialog(properties).pack();
        }
        else if (src == listModuleEntry) {
            module = listModuleEntry.getModule();
            layoutAddressPanel(evalTypeSelect.getSelectedIndex());
            getJDialog(properties).pack();
        }
        else if (src == basisButton) {
            if (module instanceof FreeModule) {
                FreeModule m = (FreeModule)module;
                elementList.addElement(m.getZero());
                for (int i = 0; i < m.getDimension(); i++) {
                    elementList.addElement(m.getUnitElement(i));                    
                }
            }
        }
        else if (src == listButton || src == simpleButton) {
            if (simpleButton.isSelected()) {
                outputFormSelect.setEnabled(false);
            }
            else {
                outputFormSelect.setEnabled(true);
            }
        }
        else if (src == graphButton) {
            showGraphDialog();
        }
    }
    
    
    public ImageIcon getIcon() {
        return icon;
    }
    
    
    public boolean hasInfo() {
        return true;
    }
    
    
    public String getInfo() {
        return evalTypes[evalType];
    }
    
    
    public String getShortDescription() {
        return "Evaluates input denotator at a given address"; 
    }
    
    
    public String getLongDescription() {
        return "The AddressEval Rubette evaluates the input "+ 
               "denotator at one or more addresses specified "+ 
               "in the properties."; 
    }

    
    public String getInTip(int i) {
        if (i == 0) {
           if (outputForm == null) {
               return Messages.getString("AddressEvalRubette.inputdeno"); 
           }
           else {
               return TextUtils.replaceStrings(Messages.getString("AddressEvalRubette.inputdenotator"), outputForm.getForm(0).getNameString()); 
           }
        }
        else {
            return Messages.getString("AddressEvalRubette.evaldenotator"); 
        }
    }
    
    
    public String getOutTip(int i) {
        String name = Messages.getString("AddressEvalRubette.outputdeno"); 
        if (outputForm != null) {
            name = TextUtils.replaceStrings(Messages.getString("AddressEvalRubette.outputdenotator"), outputForm.getNameString()); 
        }
        return name;
    }


    private static final String EVALTYPE = "EvalType"; 
    
    public void toXML(XMLWriter writer) {
        writer.empty(EVALTYPE, VALUE_ATTR, evalType);
        if (evalType == EVAL_TYPE_ELEMENT) {
            definitionXmlWriter.toXML(moduleElement, writer);
        }
        else if (evalType == EVAL_TYPE_LIST) {
            if (outputForm != null) {
                writer.writeFormRef(outputForm);
                definitionXmlWriter.toXML(module, writer);
                for (ModuleElement el : elements) {
                    definitionXmlWriter.toXML(el, writer);
                }
            }
        }
        else if (evalType == EVAL_TYPE_CHANGE) {
            definitionXmlWriter.toXML(morphism, writer);
        }
        else if (evalType == EVAL_TYPE_INPUT && (outputForm != null)) {
                writer.writeFormRef(outputForm);

        }
    }
    
    
    public Rubette fromXML(XMLReader reader, Element element) {
        int t = 0;
        AddressEvalRubette newRubette = null;
        
        Element child = XMLReader.getChild(element, EVALTYPE);

        if (child == null) {
            // there must be a type
            reader.setError(Messages.getString("AddressEvalRubette.missingelement"), EVALTYPE); 
            return null;
        }
        
        t = XMLReader.getIntAttribute(child, VALUE_ATTR, 0, evalTypes.length-1, 0);

        if (t == EVAL_TYPE_ELEMENT) {
            // type evaluate at element
            child = XMLReader.getNextSibling(child, MODULE_ELEMENT);
            if (child != null) {
                ModuleElement mel = reader.parseModuleElement(child);
                if (mel != null) {
                    newRubette = new AddressEvalRubette();
                    newRubette.evalType = t;
                    newRubette.moduleElement  = mel;
                    newRubette.module   = mel.getModule();
                }                
            }
            else {
                reader.setError(Messages.getString("AddressEvalRubette.missingelement"), MODULE_ELEMENT); 
            }
        }
        else if (t == EVAL_TYPE_LIST) {
            // type evaluate at list of elements
            child = XMLReader.getNextSibling(child, FORM);
            if (child == null) {
                // no output form has been given
                // there must be an output form
                reader.setError(Messages.getString("AddressEvalRubette.missingelement"), FORM); 
                return null;
            }
            // get output form
            Form oform = reader.parseAndResolveForm(child);
            child = XMLReader.getNextSibling(child, MODULE);
            if (child == null) {
                // no module has been given
                // there must be a module
                reader.setError(Messages.getString("AddressEvalRubette.missingelement"), MODULE); 
                return null;
            }
            Module module0 = reader.parseModule(child);
            if (module0 == null) {
                return null;
            }
            LinkedList<ModuleElement> list = new LinkedList<>();
            child = XMLReader.getNextSibling(child, MODULE_ELEMENT);
            while (child != null) {
                ModuleElement e = reader.parseModuleElement(child);
                if (e == null) {
                    return null;
                }
                if (!module0.hasElement(e)) {
                    reader.setError(Messages.getString("AddressEvalRubette.wrongmodule"), e.getModule(), module0); 
                    return null;
                }
                list.add(e);
                child = XMLReader.getNextSibling(child, MODULE_ELEMENT);
            }
            newRubette = new AddressEvalRubette();
            newRubette.evalType = t;
            newRubette.outputForm = oform;
            newRubette.elements = list;
            newRubette.module = module0;
        }
        else if (t == EVAL_TYPE_CHANGE) {
            // type change address
            child = XMLReader.getNextSibling(child, MODULE_MORPHISM);
            if (child == null) {
                // no module morphism has been given
                // there must be a module morphism
                reader.setError(Messages.getString("AddressEvalRubette.missingelement"), MODULE_MORPHISM); 
                return null;
            }
            ModuleMorphism morphism0 = reader.parseModuleMorphism(child);
            if (morphism0 == null) {
                return null;
            }
            newRubette = new AddressEvalRubette();
            newRubette.evalType = t;
            newRubette.morphism = morphism0;
        }
        else if (t == EVAL_TYPE_INPUT) {
            // get output form if any
            Form oform = null;
            child = XMLReader.getNextSibling(child, FORM);
            if (child != null) {
                oform = reader.parseAndResolveForm(child);
            }
            newRubette = new AddressEvalRubette();
            newRubette.evalType = t;
            newRubette.outputForm = oform;
            newRubette.setInCount(2);
        }
        else {
            newRubette = new AddressEvalRubette();
            newRubette.evalType = 0;
        }
        
        return newRubette;
    }

    
    private void layoutAddressPanel(int type) {
        addressPanel.removeAll();
        if (type == EVAL_TYPE_ELEMENT) {
            Module m = (module == null)? ZRing.ring:module;
            elementEntry = new JModuleElementEntry(m);
            elementEntry.setBorder(makeTitledBorder(Messages.getString("AddressEvalRubette.moduleelement"))); 
            elementEntry.setToolTipText(Messages.getString("AddressEvalRubette.selectelement")); 
            if (moduleElement != null) {
                elementEntry.setModuleElement(moduleElement);
            }
            addressPanel.add(elementEntry, BorderLayout.CENTER);
        }
        else if (type == EVAL_TYPE_LIST) {
            Box box = Box.createVerticalBox();
            outputFormSelect = new JSelectForm(Repository.systemRepository(), FormDenotatorTypeEnum.POWER, FormDenotatorTypeEnum.LIST);
            outputFormSelect.setBorder(makeTitledBorder(Messages.getString("AddressEvalRubette.outputform"))); 
            outputFormSelect.setToolTipText(Messages.getString("AddressEvalRubette.setoutputform")); 
            if (outputForm != null) {
                outputFormSelect.setForm(outputForm);
            }
            box.add(outputFormSelect);
            
            Module m = (module == null)?ZRing.ring:module;
            
            listModuleEntry = new JModuleEntry();
            listModuleEntry.setBorder(makeTitledBorder(Messages.getString("AddressEvalRubette.elementmodule"))); 
            listModuleEntry.setModule(m);
            listModuleEntry.addActionListener(this);
            box.add(listModuleEntry);
            
            elementList = new JModuleElementList(m);
            elementList.setBorder(makeTitledBorder(Messages.getString("AddressEvalRubette.elementlist"))); 
            elementList.setToolTipText(Messages.getString("AddressEvalRubette.elementlisttooltip")); 
            if (elements != null) {
                elementList.addElements(elements);
            }
            box.add(elementList, BorderLayout.CENTER);
            addressPanel.add(box, BorderLayout.CENTER);
            
            box = Box.createHorizontalBox();
            box.add(Box.createHorizontalGlue());
            basisButton = new JButton(Messages.getString("AddressEvalRubette.basisvectors")); 
            basisButton.setToolTipText(Messages.getString("AddressEvalRubette.basistooltip")); 
            basisButton.addActionListener(this);
            basisButton.setEnabled(module instanceof FreeModule);                
            box.add(basisButton);
            box.add(Box.createHorizontalStrut(5));
            graphButton = new JButton(Messages.getString("AddressEvalRubette.graphical")); 
            graphButton.setToolTipText(Messages.getString("AddressEvalRubette.graphtooltip")); 
            graphButton.addActionListener(this);
            box.add(graphButton);
            box.add(Box.createHorizontalGlue());
            graphButton.setEnabled(isGraphical(module));
            
            addressPanel.add(box, BorderLayout.SOUTH);
        }
        else if (type == EVAL_TYPE_CHANGE) {
            morphismEntry = new JMorphismEntry(null, null);
            if (morphism != null) {
                morphismEntry.setMorphism(morphism);
            }
            morphismEntry.setBorder(makeTitledBorder(Messages.getString("AddressEvalRubette.changemorph"))); 
            morphismEntry.setToolTipText(Messages.getString("AddressEvalRubette.changemorphtooltip")); 
            addressPanel.add(morphismEntry, BorderLayout.CENTER);
        }
        else if (type == EVAL_TYPE_INPUT) {
            Box box = Box.createVerticalBox();
            JPanel typePanel = new JPanel();
            typePanel.setBorder(makeTitledBorder(Messages.getString("AddressEvalRubette.resulttype"))); 
            ButtonGroup group = new ButtonGroup();
            simpleButton = new JRadioButton(SIMPLE);
            group.add(simpleButton);
            listButton = new JRadioButton(LISTORPOWER);
            group.add(listButton);
            simpleButton.setSelected(outputForm == null);
            listButton.setSelected(outputForm != null);
            simpleButton.addActionListener(this);
            listButton.addActionListener(this);
            typePanel.add(simpleButton);
            typePanel.add(listButton);
            box.add(typePanel);            
            
            outputFormSelect = new JSelectForm(Repository.systemRepository(), FormDenotatorTypeEnum.POWER, FormDenotatorTypeEnum.LIST);
            outputFormSelect.setBorder(makeTitledBorder(Messages.getString("AddressEvalRubette.outputform"))); 
            outputFormSelect.setToolTipText(Messages.getString("AddressEvalRubette.setoutputform")); 
            if (outputForm != null) {
                outputFormSelect.setForm(outputForm);
            }
            box.add(outputFormSelect);
            
            addressPanel.add(box);
        }
    }

    
    private boolean isGraphical(Module module) {
        if (module == CRing.ring) {
            return true;
        }
        else if (module instanceof FreeModule) {
            if (module.getDimension() != 2) {
                return false;
            }
            else {
                return FreeUtils.isArithmetic(module);
            }            
        }
        else {
            return false;
        }
    }
    
    private void showGraphDialog() {
        if (module instanceof CRing) {
            LinkedList<ModuleElement> elements0 = new LinkedList<>();
            for (ModuleElement m : elementList.getElements()) {
                Complex c = (Complex) m;
                List<Real> list = new ArrayList<>(2);
                list.add(new Real((c.getReal())));
                list.add(new Real((c.getImag())));
                elements0.add(new Vector<>(RRing.ring, list));
            }
            JGraphSelect select = JGraphSelectDialog.showDialog(graphButton, RRing.ring, elements0);
            if (select != null) {
                elementList.clear();
                RConfiguration config = (RConfiguration)select.getConfiguration();
                for (int i = 0; i < config.getSize(); i++) {
                    double[] p = new double[] { config.px.get(i), config.py.get(i) };
                    elementList.addElement(new Complex(p[0], p[1]));
                }
            }
            return;
        }
        if (!(module instanceof VectorModule)) {
            return;
        }
        Ring<?> moduleRing = module.getRing();
        if (moduleRing instanceof RRing) {
            VectorModule<Real> m = (VectorModule<Real>)module;
            if (m.getDimension() == 2) {
                JGraphSelect select = JGraphSelectDialog.showDialog(graphButton, RRing.ring, elementList.getElements());
                if (select != null) {
                    elementList.clear();
                    RConfiguration config = (RConfiguration)select.getConfiguration();
                    for (int i = 0; i < config.getSize(); i++) {
                        List<Real> list = new ArrayList<>(2);
                        list.add(new Real((config.px.get(i))));
                        list.add(new Real((config.py.get(i))));
                        elementList.addElement(new Vector<>(RRing.ring, list));
                    }
                }
            }
        }
        else if (moduleRing instanceof QRing) {
            VectorModule<Rational> m = (VectorModule<Rational>)module;
            if (m.getDimension() == 2) {
                JGraphSelect select = JGraphSelectDialog.showDialog(graphButton, QRing.ring, elementList.getElements());
                if (select != null) {
                    elementList.clear();
                    QConfiguration config = (QConfiguration)select.getConfiguration();
                    for (int i = 0; i < config.getSize(); i++) {
                        List<Rational> list = new ArrayList<>(2);
                        list.add(config.qpx.get(i));
                        list.add(config.qpy.get(i));
                        elementList.addElement(new Vector<>(QRing.ring, list));
                    }
                }
            }
        }
        else if (moduleRing instanceof ZRing) {
            VectorModule<ZInteger> m = (VectorModule<ZInteger>) module;
            if (m.getDimension() == 2) {
                JGraphSelect select = JGraphSelectDialog.showDialog(graphButton, ZRing.ring, elementList.getElements());
                if (select != null) {
                    elementList.clear();
                    ZConfiguration config = (ZConfiguration)select.getConfiguration();
                    for (int i = 0; i < config.getSize(); i++) {
                        List<ZInteger> pList = new ArrayList<>();
                        pList.add(new ZInteger(config.ipx.get(i)));
                        pList.add(new ZInteger(config.ipy.get(i)));
                        elementList.addElement(new Vector<>(ZRing.ring, pList));
                    }
                }
            }
        }
        else if (moduleRing instanceof ZnRing) {
            VectorModule<Modulus> m = (VectorModule<Modulus>) module;
            if (m.getDimension() == 2) {
                JGraphSelect select = JGraphSelectDialog.showDialog(graphButton, m.getRing(), elementList.getElements());
                elementList.clear();
                if (select != null) {
                    ZConfiguration config = (ZConfiguration)select.getConfiguration();
                    for (int i = 0; i < config.getSize(); i++) {
                        int modulus = m.getRing().getOne().getModulus(); //TODO ugly way to get the modulus
                        int[] p = new int[] { config.ipx.get(i), config.ipy.get(i) };
                        List<Modulus> pList = Arrays.stream(p)
                                .mapToObj(elementP -> new Modulus(elementP, modulus))
                                .collect(Collectors.toList());
                        elementList.addElement(new Vector<>(RingRepository.getModulusRing(modulus), pList));
                    }
                }
            }
        }
    }

    
    private JPanel      properties       = null;
    private JPanel      addressPanel     = null;
    private JComboBox   evalTypeSelect   = null;
    private JSelectForm outputFormSelect = null;
    private JStatusline statusline       = null;
    
    private JModuleElementEntry elementEntry    = null;
    private JModuleElementList  elementList     = null;
    private JModuleEntry        listModuleEntry = null;
    private JMorphismEntry      morphismEntry   = null;
    private JButton             basisButton     = null;
    private JButton             graphButton     = null;
    private JRadioButton        simpleButton    = null;
    private JRadioButton        listButton      = null;
    
    private Form          outputForm    = null;
    private Module        module        = null;
    private ModuleElement moduleElement = null;
    private int           evalType      = EVAL_TYPE_NULL;
    
    private List<ModuleElement> elements = null;
    private ModuleMorphism      morphism = null; 
    
    private static final int EVAL_TYPE_NULL    = 0;
    private static final int EVAL_TYPE_ELEMENT = 1;
    private static final int EVAL_TYPE_LIST    = 2;
    private static final int EVAL_TYPE_CHANGE  = 3;
    private static final int EVAL_TYPE_INPUT   = 4;
    
    private static final String[] evalTypes = {
        Messages.getString("AddressEvalRubette.evalnull"), 
        Messages.getString("AddressEvalRubette.evalelement"), 
        Messages.getString("AddressEvalRubette.evalllist"), 
        Messages.getString("AddressEvalRubette.changeaddress"), 
        Messages.getString("AddressEvalRubette.evalinput") 
    };
    
    // Message strings
    private static final String INPUT_NULL_ERROR    = Messages.getString("AddressEvalRubette.inputnullerror");
    private static final String INPUT_WRONG_FORM    = Messages.getString("AddressEvalRubette.inputwrongform");
    private static final String ELEMENTNOTSET_ERROR = Messages.getString("AddressEvalRubette.elementnotset"); 
    private static final String MODULENOTSET_ERROR  = Messages.getString("AddressEvalRubette.modulenotset"); 
    private static final String ADDRESSMODULE_ERROR = Messages.getString("AddressEvalRubette.addressmoduleerror");     
    private static final String LISTNOTSET_ERROR    = Messages.getString("AddressEvalRubette.listnotset"); 
    private static final String OUTPUTFORMNOTSET_ERROR = Messages.getString("AddressEvalRubette.outputformnotset"); 
    private static final String MORPHISMNOTSET_ERROR = Messages.getString("AddressEvalRubette.morphismnotset"); 
    private static final String ADDRESSMORPHISM_ERROR = Messages.getString("AddressEvalRubette.addressmorphismerror"); 
    private static final String NOMODULE_ERROR      = Messages.getString("AddressEvalRubette.modnotset"); 
    private static final String NOELEMENT_ERROR     = Messages.getString("AddressEvalRubette.modelnotset"); 
    private static final String NOOUTPUTFORM_ERROR  = Messages.getString("AddressEvalRubette.oformnotset"); 
    private static final String NOELEMENTS_ERROR    = Messages.getString("AddressEvalRubette.noellist"); 
    private static final String NOMORPHISM_ERROR    = Messages.getString("AddressEvalRubette.modmorphnotset"); 
    private static final String INPUT2WRONGTYPE_ERROR = Messages.getString("AddressEvalRubette.secinputwrongtype"); 
    private static final String INPUT2NOTSET_ERROR  = Messages.getString("AddressEvalRubette.secinputnull"); 
    private static final String SIMPLE              = Messages.getString("AddressEvalRubette.simple"); 
    private static final String LISTORPOWER         = Messages.getString("AddressEvalRubette.listorpower"); 

    private static final ImageIcon icon;

    static {
        icon = Icons.loadIcon(AddressEvalRubette.class, "/images/rubettes/builtin/address/addressicon.png"); 
    }
}
