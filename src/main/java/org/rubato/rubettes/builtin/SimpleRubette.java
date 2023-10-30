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

import org.rubato.base.AbstractRubette;
import org.rubato.base.Repository;
import org.rubato.base.RubatoConstants;
import org.rubato.base.Rubette;
import org.rubato.composer.RunInfo;
import org.rubato.composer.components.JMorphismEntry;
import org.rubato.composer.components.JSelectForm;
import org.rubato.composer.components.JSimpleEntry;
import org.rubato.composer.icons.Icons;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.map.ConstantModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.server.xml.writer.DefaultDefinitionXmlWriter;
import org.vetronauta.latrunculus.server.xml.writer.LatrunculusXmlWriter;
import org.w3c.dom.Element;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.rubato.composer.Utilities.getJDialog;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.DENOTATOR;


public class SimpleRubette extends AbstractRubette implements ActionListener {    

    //TODO rubette writer
    private final LatrunculusXmlWriter<MathDefinition> definitionXmlWriter = new DefaultDefinitionXmlWriter();

    public SimpleRubette() {
        setInCount(0);
        setOutCount(1);
    }
    
    //
    // SimpleRubette interface
    //
    
    public void setDenotator(SimpleDenotator d) {
        denotator = d;
        if (d != null) { setForm(d.getSimpleForm()); }
    }
    
    
    public Denotator getDenotator() {
        return denotator;
    }

    
    public void setForm(SimpleForm f) {
        form = f;
        if (f == null) {
            name = " ";
        }
        else {
            name = form.getNameString()+": "+form.getTypeString();
        }
    }    
    
    //
    // Rubette interface implementation
    //
    
    public void run(RunInfo runInfo) {
        setOutput(0, denotator);
    }

    
    public Rubette duplicate() {
        SimpleRubette rubette = new SimpleRubette();
        rubette.setDenotator(denotator);
        rubette.setForm(form);
        return rubette;
    }

    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "Simple";
    }

    
    public boolean hasProperties() {
        return true;
    }
    

    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();            
            properties.setLayout(new BorderLayout());
            
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new GridLayout(2, 1));
            
            selector = new JSelectForm(Repository.systemRepository(), FormDenotatorTypeEnum.SIMPLE);
            selector.addActionListener(this);
            selector.setForm(form);
            topPanel.add(selector);

            // address type buttons
            ButtonGroup buttonGroup = new ButtonGroup();
            Box addressButtonBox = Box.createHorizontalBox();
            addressButtonBox.add(Box.createHorizontalGlue());
            nonNullButton = new JRadioButton(Messages.getString("SimpleRubette.nonnull"));
            buttonGroup.add(nonNullButton);
            addressButtonBox.add(nonNullButton);
            addressButtonBox.add(Box.createHorizontalStrut(10));
            nullButton = new JRadioButton(Messages.getString("SimpleRubette.null"));
            buttonGroup.add(nullButton);
            addressButtonBox.add(nullButton);
            addressButtonBox.add(Box.createHorizontalGlue());
            addressButtonBox.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), Messages.getString("SimpleRubette.addresstype")));
            if (denotator != null) {
                if (denotator.getModuleMorphismMap() instanceof ConstantModuleMorphismMap) {
                    nullButton.setSelected(true);
                }
                else {
                    nonNullButton.setSelected(true);
                }
            }
            else {
                nullButton.setSelected(true);
            }
            nonNullButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addressAction();
                }
            });
            nullButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addressAction();
                }
            });
            topPanel.add(addressButtonBox);
            
            properties.add(topPanel, BorderLayout.NORTH);
            
            JPanel middlePanel = new JPanel();
            middlePanel.setLayout(new BorderLayout());
            
            // element and morphism panel            
            valuePanel = new JPanel();
            valuePanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), Messages.getString("SimpleRubette.value")));
            valuePanel.setLayout(new BorderLayout());
            fillValuePanel();
            middlePanel.add(valuePanel, BorderLayout.CENTER);

            // denotator name
            JPanel namePanel = new JPanel();
            namePanel.setLayout(new BorderLayout());
            namePanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Name"));
            nameField = new JTextField();
            if (denotator != null) { nameField.setText(denotator.getNameString()); }
            namePanel.add(nameField, BorderLayout.CENTER);
            middlePanel.add(namePanel, BorderLayout.SOUTH);
            
            properties.add(middlePanel, BorderLayout.CENTER);
            
            infoLabel = new JLabel(" ");
            infoLabel.setForeground(Color.RED);
            infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            properties.add(infoLabel, BorderLayout.SOUTH);
        }
        return properties;
    }    
    
    
    private void fillValuePanel() {
        valuePanel.removeAll();
        if (form == null) {
            valuePanel.add(emptyLabel, BorderLayout.NORTH);
        }
        else {
            if (nullButton.isSelected()) {
                simpleEntry = JSimpleEntry.make(form.getModule());
                if (denotator != null) {
                    simpleEntry.setValue(denotator.getElement());
                }
                morphismEntry = null;
                valuePanel.add(simpleEntry, BorderLayout.NORTH);
            }
            else if (nonNullButton.isSelected()) {
                morphismEntry = new JMorphismEntry(null, form.getModule());
                simpleEntry = null;
                if (denotator != null) {
                    morphismEntry.setMorphism(denotator.getModuleMorphism());
                }
                valuePanel.add(morphismEntry, BorderLayout.NORTH);
            }
        }
    }
    
    
    protected void addressAction() {
        denotator = null;
        infoLabel.setText(" ");
        fillValuePanel();
        getJDialog(properties).pack();
    }
    

    public void actionPerformed(ActionEvent e) {
        denotator = null;
        infoLabel.setText(" ");
        form = (SimpleForm)selector.getForm();
        if (form != null) {
            fillValuePanel();
            getJDialog(properties).pack();
        }
    }

    
    public boolean applyProperties() {
        infoLabel.setText(" ");
        ModuleElement element = null;
        ModuleMorphism morphism = null;
        NameDenotator nameDenotator = null;
        if (selector.getForm() == null) {
            infoLabel.setText(Messages.getString("SimpleRubette.noform"));
            return false;
        }
        if (simpleEntry != null) {
            element = simpleEntry.getValue();
        }
        if (morphismEntry != null) {
            morphism = morphismEntry.getMorphism();
        }
        if (nameField != null) {
            String text = nameField.getText().trim();
            if (text.length() > 0) {
                nameDenotator = NameDenotator.make(text);
            }
        }
        if (element == null && morphism == null) {
            infoLabel.setText(Messages.getString("SimpleRubette.wrongformat"));
        }
        else {
            setForm((SimpleForm)selector.getForm());
            try {
                if (element != null) {
                    denotator = new SimpleDenotator(nameDenotator, form, element);
                }
                else  {
                    denotator = new SimpleDenotator(nameDenotator, form, morphism);
                }
                assert(denotator.check());
            }
            catch (DomainException e) {
                throw new AssertionError("This should never happen!");
            }
        }
        return (element != null || morphism != null);
    }
    
    
    public void revertProperties() {
        fillValuePanel();
    }

    
    public boolean hasInfo() {
        return true;
    }
    
    
    public String getInfo() {
        return name;
    }

    
    public ImageIcon getIcon() {
        return icon;
    }

    
    public String getShortDescription() {
        return "Contains a simple denotator";
    }

    
    public String getLongDescription() {
        return "The Source Rubette stores a denotator of type simple.";
    }
    
    
    public String getOutTip(int i) {
        return Messages.getString("SimpleRubette.storeddenotator");
    }

    
    public void toXML(XMLWriter writer) {
        if (denotator != null) {
            definitionXmlWriter.toXML(denotator, writer);
        }
    }
    
    
    public Rubette fromXML(XMLReader reader, Element element) {
        Element child = XMLReader.getChild(element, DENOTATOR);
        Denotator d = null;
        if (child != null) {
            d = reader.parseDenotator(child);
        }
        SimpleRubette rubette = new SimpleRubette();
        if (d instanceof SimpleDenotator) {
            rubette.setDenotator((SimpleDenotator)d);
        }
        return rubette;
    }

    
    private JPanel          properties    = null;
    private JLabel          emptyLabel    = new JLabel(Messages.getString("SimpleRubette.novalue"));
    private SimpleForm      form          = null;
    private JSelectForm     selector;
    private JSimpleEntry    simpleEntry   = null;
    private JMorphismEntry  morphismEntry = null;
    private JPanel          valuePanel;
    private JLabel          infoLabel;
    private JRadioButton    nonNullButton;
    private JRadioButton    nullButton;
    private String          name          = " ";
    private JTextField      nameField;
    private SimpleDenotator denotator     = null;
    private static final ImageIcon icon;

    static {
        icon = Icons.loadIcon(SimpleRubette.class, "/images/rubettes/builtin/simpleicon.png");
    }
}
