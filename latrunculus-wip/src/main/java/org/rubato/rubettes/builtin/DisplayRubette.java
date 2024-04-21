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
import lombok.Setter;
import org.rubato.composer.icons.Icons;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;
import org.vetronauta.latrunculus.server.display.DenotatorDisplay;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.rubato.composer.Utilities.makeTitledBorder;


/**
 * The Display Rubette shows a text or XML representation
 * of the input denotator in its view.        
 * 
 * @author Gérard Milmeister
 */
public class DisplayRubette extends AbstractRubette implements ActionListener {

    public DisplayRubette() {
        setInCount(1);
        setOutCount(0);
    }

    
    public void run(RunInfo runInfo) {
        Denotator input = getInput(0);
        if (input == null) {
            addError(BuiltinMessages.getString("DisplayRubette.inputnotset"));
        }
    }

    
    public Rubette duplicate() {
        DisplayRubette rubette = new DisplayRubette();
        rubette.isXML = isXML;
        return rubette;
    }
    
    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "Display"; 
    }

    
    public boolean hasView() {
        return true;
    }


    public JComponent getView() {
        if (view == null) {
            view = new JPanel();
            view.setLayout(new BorderLayout());
            display = new JTextArea(10, 40);
            display.setEditable(false);
            display.setBorder(textBorder);
            JScrollPane scrollPane = new JScrollPane(display);
            scrollPane.setBorder(makeTitledBorder("Denotator")); 
            view.add(scrollPane, BorderLayout.CENTER);
            
            ButtonGroup buttonGroup = new ButtonGroup();
            textButton = new JRadioButton("Text");
            textButton.addActionListener(this);
            buttonGroup.add(textButton);
            xmlButton = new JRadioButton("XML");
            xmlButton.addActionListener(this);
            buttonGroup.add(xmlButton);
            textButton.setSelected(!isXML);
            xmlButton.setSelected(isXML);
            Box buttonBox = Box.createHorizontalBox();
            buttonBox.add(Box.createHorizontalGlue());
            buttonBox.add(textButton);
            buttonBox.add(Box.createHorizontalStrut(10));
            buttonBox.add(xmlButton);
            buttonBox.add(Box.createHorizontalGlue());
            buttonBox.setBorder(makeTitledBorder("Display type"));
            
            view.add(buttonBox, BorderLayout.SOUTH);
            updateText();
        }
        return view;
    }

    
    public void updateView() {
        getView();
        updateText();
    }

    
    private void updateText() {
        Denotator input = getInput(0);
        if (display != null) {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            if (input != null) {
                PrintStream ps = new PrintStream(bs);
                if (isXML) {
                    XMLWriter writer = new XMLWriter(ps);
                    writer.writeDenotator(input);
                }
                else {
                    DenotatorDisplay.display(input, ps);
                }
                display.setFont(Font.decode("Monospaced"));
                display.setText(bs.toString());
            }
            else {
                display.setText(BuiltinMessages.getString("DisplayRubette.nodenotator"));
            }
        }
    }
    
    
    public ImageIcon getIcon() {
        return icon;
    }

    
    public String getShortDescription() {
        return "Shows a text representation of a denotator";
    }

    
    public String getLongDescription() {
        return "The Display Rubette shows a text or XML representation"
               +" of the input denotator in its view.";        
    }
    
    
    public String getInTip(int i) {
        return BuiltinMessages.getString("DisplayRubette.intip");
    }


    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == textButton || src == xmlButton) {
            isXML = xmlButton.isSelected();
            updateText();
        }
    }
    
    private JPanel       view = null;
    private JTextArea    display = null;
    private JRadioButton textButton;
    private JRadioButton xmlButton;

    @Getter @Setter
    private boolean isXML = false;
    
    private static final ImageIcon icon;
    
    private static final Border textBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    
    static {
        icon = Icons.loadIcon(DisplayRubette.class, "/images/rubettes/builtin/displayicon.png"); 
    }
}
