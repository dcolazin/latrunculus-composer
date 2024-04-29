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

import lombok.Getter;
import org.rubato.composer.components.JModuleElementEntry;
import org.rubato.composer.components.JModuleElementList;
import org.rubato.composer.components.JModuleEntry;
import org.rubato.composer.components.JMorphismEntry;
import org.rubato.composer.components.JSelectForm;
import org.rubato.composer.components.JStatusline;
import org.rubato.rubettes.builtin.address.JGraphSelect.QConfiguration;
import org.rubato.rubettes.builtin.address.JGraphSelect.RConfiguration;
import org.rubato.rubettes.builtin.address.JGraphSelect.ZConfiguration;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.module.FreeUtils;
import org.vetronauta.latrunculus.core.math.module.factory.RingRepository;
import org.vetronauta.latrunculus.core.math.module.generic.FreeModule;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.core.util.TextUtils;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;
import org.vetronauta.latrunculus.plugin.impl.AddressEvalPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.rubato.composer.Utilities.getJDialog;
import static org.rubato.composer.Utilities.makeTitledBorder;

/**
 * 
 * @author Gérard Milmeister
 */
public final class AddressEvalRubette extends AbstractRubette implements ActionListener {

    @Getter
    private AddressEvalPlugin plugin; //TODO final

    public AddressEvalRubette() {
        setInCount(1);
        setOutCount(1);
        this.plugin = new AddressEvalPlugin(AddressEvalPlugin.EvalType.NULL); //TODO for the instance method
    }

    public AddressEvalRubette(AddressEvalPlugin.EvalType evalType) {
        this();
        this.plugin = new AddressEvalPlugin(evalType);
    }

    public AddressEvalRubette(AddressEvalPlugin.EvalType evalType, ModuleElement<?, ?> moduleElement, Module<?, ?> module) {
        this();
        this.plugin = new AddressEvalPlugin(evalType, moduleElement, module);
    }

    public AddressEvalRubette(AddressEvalPlugin.EvalType evalType, Form form) {
        this();
        this.plugin = new AddressEvalPlugin(evalType, form);
    }

    public AddressEvalRubette(AddressEvalPlugin.EvalType evalType, Form form, List<? extends ModuleElement> elements, Module<?, ?> module) {
        this();
        this.plugin = new AddressEvalPlugin(evalType, form, elements, module);
    }

    public AddressEvalRubette(AddressEvalPlugin.EvalType evalType, ModuleMorphism<?, ?, ?, ?> morphism) {
        this();
        this.plugin = new AddressEvalPlugin(evalType, morphism);
    }

    private AddressEvalRubette(AddressEvalPlugin plugin) {
        this();
        this.plugin = plugin;
    }

    @Override
    public void run(RunInfo runInfo)  {
        plugin.run(runInfo);
    }

    @Override
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    @Override
    public String getName() {
        return "AddressEval"; 
    }

    @Override
    public Rubette duplicate() {
        return new AddressEvalRubette(plugin.duplicate());
    }

    @Override
    public boolean hasProperties() {
        return true;
    }
    
    @Override
    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();
            properties.setLayout(new BorderLayout());
            
            Box box = Box.createVerticalBox();
            
            JPanel evalTypeSelectPanel = new JPanel();
            evalTypeSelectPanel.setLayout(new BorderLayout());
            evalTypeSelectPanel.setBorder(makeTitledBorder(AddressMessages.getString("AddressEvalRubette.evaltype")));
            evalTypeSelect = new JComboBox(evalTypes);
            evalTypeSelect.setToolTipText(AddressMessages.getString("AddressEvalRubette.evaltypetooltip"));
            evalTypeSelect.setEditable(false);
            evalTypeSelect.setSelectedIndex(plugin.getEvalType().ordinal());
            evalTypeSelect.addActionListener(this);
            evalTypeSelectPanel.add(evalTypeSelect, BorderLayout.CENTER);
            box.add(evalTypeSelectPanel);
            
            properties.add(box, BorderLayout.NORTH);
            
            addressPanel = new JPanel();
            addressPanel.setLayout(new BorderLayout());                        
            layoutAddressPanel(plugin.getEvalType());
            
            properties.add(addressPanel, BorderLayout.CENTER);
            
            statusline = new JStatusline();
            statusline.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));
            
            properties.add(statusline, BorderLayout.SOUTH);
            
        }
        return properties;
    }

    @Override
    public boolean applyProperties() {
        statusline.clear();
        AddressEvalPlugin.EvalType t = AddressEvalPlugin.EvalType.ofIndex(evalTypeSelect.getSelectedIndex());
        if (t == null) {
            return false;
        }
        switch (t) {
            case ELEMENT:
                return applyElement();
            case LIST:
                return applyList();
            case CHANGE:
                return applyChange();
            case INPUT:
                return applyInput();
            case NULL:
                this.plugin = new AddressEvalPlugin(AddressEvalPlugin.EvalType.NULL);
                return true;
            default:
                return false;
        }
    }

    private boolean applyElement() {
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
        this.plugin = new AddressEvalPlugin(AddressEvalPlugin.EvalType.ELEMENT, el, mod);
        return true;
    }

    private boolean applyList() {
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
        this.plugin = new AddressEvalPlugin(AddressEvalPlugin.EvalType.LIST, oform, list, mod);
        return true;
    }

    private boolean applyChange() {
        ModuleMorphism m = morphismEntry.getMorphism();
        if (m == null) {
            statusline.setError(NOMORPHISM_ERROR);
            return false;
        }
        this.plugin = new AddressEvalPlugin(AddressEvalPlugin.EvalType.CHANGE, m);
        return true;
    }

    private boolean applyInput() {
        Form oform = null;
        if (listButton.isSelected()) {
            oform = outputFormSelect.getForm();
            if (oform == null) {
                statusline.setError(NOOUTPUTFORM_ERROR);
                return false;
            }
        }
        setInCount(2);
        this.plugin = oform != null ?
            new AddressEvalPlugin(AddressEvalPlugin.EvalType.INPUT, oform) : new AddressEvalPlugin(AddressEvalPlugin.EvalType.INPUT);
        return true;
    }

    @Override
    public void revertProperties() {
        evalTypeSelect.setSelectedIndex(plugin.getEvalType().ordinal());
        layoutAddressPanel(plugin.getEvalType());
        getJDialog(properties).pack();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == evalTypeSelect) {
            layoutAddressPanel(evalTypeSelect.getSelectedIndex());
            getJDialog(properties).pack();
        }
        else if (src == listModuleEntry) {
            this.plugin.setModule(listModuleEntry.getModule());
            layoutAddressPanel(evalTypeSelect.getSelectedIndex());
            getJDialog(properties).pack();
        }
        else if (src == basisButton) {
            if (this.plugin.getModule() instanceof FreeModule) {
                FreeModule m = (FreeModule) this.plugin.getModule();
                elementList.addElement(m.getZero());
                for (int i = 0; i < m.getDimension(); i++) {
                    elementList.addElement(m.getUnitElement(i));                    
                }
            }
        }
        else if (src == listButton || src == simpleButton) {
            outputFormSelect.setEnabled(!simpleButton.isSelected());
        }
        else if (src == graphButton) {
            showGraphDialog();
        }
    }

    @Override
    public boolean hasInfo() {
        return true;
    }
    
    @Override
    public String getInfo() {
        return evalTypes[plugin.getEvalType().ordinal()];
    }
    
    @Override
    public String getShortDescription() {
        return "Evaluates input denotator at a given address"; 
    }
    
    @Override
    public String getLongDescription() {
        return "The AddressEval Rubette evaluates the input "+ 
               "denotator at one or more addresses specified "+ 
               "in the properties."; 
    }

    @Override
    public String getInTip(int i) {
        if (i == 0) {
           if (plugin.getOutputForm() == null) {
               return AddressMessages.getString("AddressEvalRubette.inputdeno");
           }
           else {
               return TextUtils.replaceStrings(AddressMessages.getString("AddressEvalRubette.inputdenotator"),
                   plugin.getOutputForm().getForm(0).getNameString());
           }
        }
        else {
            return AddressMessages.getString("AddressEvalRubette.evaldenotator");
        }
    }
    
    @Override
    public String getOutTip(int i) {
        String name = AddressMessages.getString("AddressEvalRubette.outputdeno");
        if (plugin.getOutputForm() != null) {
            name = TextUtils.replaceStrings(AddressMessages.getString("AddressEvalRubette.outputdenotator"),
                plugin.getOutputForm().getNameString());
        }
        return name;
    }

    private void layoutAddressPanel(int i) {
        layoutAddressPanel(AddressEvalPlugin.EvalType.ofIndex(i));
    }

    private void layoutAddressPanel(AddressEvalPlugin.EvalType type) {
        addressPanel.removeAll();
        switch (type) {
            case ELEMENT:
                layoutElement();
                return;
            case LIST:
                layoutList();
                return;
            case CHANGE:
                layoutChange();
                return;
            case INPUT:
                layoutInput();
                return;
        }
    }

    private void layoutElement() {
        Module<?, ?> m = plugin.getModule();
        if (m == null) {
            m = ZRing.ring;
        }
        elementEntry = new JModuleElementEntry(m);
        elementEntry.setBorder(makeTitledBorder(AddressMessages.getString("AddressEvalRubette.moduleelement")));
        elementEntry.setToolTipText(AddressMessages.getString("AddressEvalRubette.selectelement"));
        if (plugin.getModuleElement() != null) {
            elementEntry.setModuleElement(plugin.getModuleElement());
        }
        addressPanel.add(elementEntry, BorderLayout.CENTER);
    }

    private void layoutList() {
        Box box = Box.createVerticalBox();
        outputFormSelect = new JSelectForm(Repository.systemRepository(), FormDenotatorTypeEnum.POWER, FormDenotatorTypeEnum.LIST);
        outputFormSelect.setBorder(makeTitledBorder(AddressMessages.getString("AddressEvalRubette.outputform")));
        outputFormSelect.setToolTipText(AddressMessages.getString("AddressEvalRubette.setoutputform"));
        if (plugin.getOutputForm() != null) {
            outputFormSelect.setForm(plugin.getOutputForm());
        }
        box.add(outputFormSelect);

        Module m = (plugin.getModule() == null) ? ZRing.ring : plugin.getModule();

        listModuleEntry = new JModuleEntry();
        listModuleEntry.setBorder(makeTitledBorder(AddressMessages.getString("AddressEvalRubette.elementmodule")));
        listModuleEntry.setModule(m);
        listModuleEntry.addActionListener(this);
        box.add(listModuleEntry);

        elementList = new JModuleElementList(m);
        elementList.setBorder(makeTitledBorder(AddressMessages.getString("AddressEvalRubette.elementlist")));
        elementList.setToolTipText(AddressMessages.getString("AddressEvalRubette.elementlisttooltip"));
        if (plugin.getElements() != null) {
            elementList.addElements(plugin.getElements());
        }
        box.add(elementList, BorderLayout.CENTER);
        addressPanel.add(box, BorderLayout.CENTER);

        box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        basisButton = new JButton(AddressMessages.getString("AddressEvalRubette.basisvectors"));
        basisButton.setToolTipText(AddressMessages.getString("AddressEvalRubette.basistooltip"));
        basisButton.addActionListener(this);
        basisButton.setEnabled(plugin.getModule() instanceof FreeModule);
        box.add(basisButton);
        box.add(Box.createHorizontalStrut(5));
        graphButton = new JButton(AddressMessages.getString("AddressEvalRubette.graphical"));
        graphButton.setToolTipText(AddressMessages.getString("AddressEvalRubette.graphtooltip"));
        graphButton.addActionListener(this);
        box.add(graphButton);
        box.add(Box.createHorizontalGlue());
        graphButton.setEnabled(isGraphical(plugin.getModule()));

        addressPanel.add(box, BorderLayout.SOUTH);
    }

    private void layoutChange() {
        morphismEntry = new JMorphismEntry(null, null);
        if (plugin.getMorphism() != null) {
            morphismEntry.setMorphism(plugin.getMorphism());
        }
        morphismEntry.setBorder(makeTitledBorder(AddressMessages.getString("AddressEvalRubette.changemorph")));
        morphismEntry.setToolTipText(AddressMessages.getString("AddressEvalRubette.changemorphtooltip"));
        addressPanel.add(morphismEntry, BorderLayout.CENTER);
    }

    private void layoutInput() {
        Box box = Box.createVerticalBox();
        JPanel typePanel = new JPanel();
        typePanel.setBorder(makeTitledBorder(AddressMessages.getString("AddressEvalRubette.resulttype")));
        ButtonGroup group = new ButtonGroup();
        simpleButton = new JRadioButton(SIMPLE);
        group.add(simpleButton);
        listButton = new JRadioButton(LISTORPOWER);
        group.add(listButton);
        simpleButton.setSelected(plugin.getOutputForm() == null);
        listButton.setSelected(plugin.getOutputForm() != null);
        simpleButton.addActionListener(this);
        listButton.addActionListener(this);
        typePanel.add(simpleButton);
        typePanel.add(listButton);
        box.add(typePanel);

        outputFormSelect = new JSelectForm(Repository.systemRepository(), FormDenotatorTypeEnum.POWER, FormDenotatorTypeEnum.LIST);
        outputFormSelect.setBorder(makeTitledBorder(AddressMessages.getString("AddressEvalRubette.outputform")));
        outputFormSelect.setToolTipText(AddressMessages.getString("AddressEvalRubette.setoutputform"));
        if (plugin.getOutputForm() != null) {
            outputFormSelect.setForm(plugin.getOutputForm());
        }
        box.add(outputFormSelect);

        addressPanel.add(box);
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
        Module<?, ?> module = plugin.getModule();
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

    private static final String[] evalTypes = {
        AddressMessages.getString("AddressEvalRubette.evalnull"),
        AddressMessages.getString("AddressEvalRubette.evalelement"),
        AddressMessages.getString("AddressEvalRubette.evalllist"),
        AddressMessages.getString("AddressEvalRubette.changeaddress"),
        AddressMessages.getString("AddressEvalRubette.evalinput")
    };

    public static final int EVAL_TYPE_TYPE_LENGTH   = evalTypes.length;

    // Message strings
    private static final String NOMODULE_ERROR      = AddressMessages.getString("AddressEvalRubette.modnotset");
    private static final String NOELEMENT_ERROR     = AddressMessages.getString("AddressEvalRubette.modelnotset");
    private static final String NOOUTPUTFORM_ERROR  = AddressMessages.getString("AddressEvalRubette.oformnotset");
    private static final String NOELEMENTS_ERROR    = AddressMessages.getString("AddressEvalRubette.noellist");
    private static final String NOMORPHISM_ERROR    = AddressMessages.getString("AddressEvalRubette.modmorphnotset");
    private static final String SIMPLE              = AddressMessages.getString("AddressEvalRubette.simple");
    private static final String LISTORPOWER         = AddressMessages.getString("AddressEvalRubette.listorpower");

}
