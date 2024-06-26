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

package org.rubato.composer.dialogs;

import static org.rubato.composer.Utilities.installEnterKey;
import static org.rubato.composer.Utilities.installEscapeKey;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.vetronauta.latrunculus.core.repository.Repository;
import org.rubato.composer.components.JSelectModule;
import org.vetronauta.latrunculus.core.math.module.generic.Module;


/**
 * @author Gérard Milmeister
 */
public final class JModuleDialog 
        extends JDialog
        implements ActionListener, CaretListener {

    public static Module showDialog(Component comp, boolean naming) {
        Frame frame = JOptionPane.getFrameForComponent(comp);
        JModuleDialog dialog = new JModuleDialog(frame, true, naming);
        dialog.setLocationRelativeTo(comp);
        dialog.setVisible(true);
        return dialog.getModule();
    }

    
    public JModuleDialog(Frame frame, boolean modal, boolean naming) {
        super(frame, DialogsMessages.getString("JModuleDialog.createmodule"), modal);
        this.naming = naming;
        
        setLayout(new BorderLayout(0, 5));
                
        selectModule = new JSelectModule();
        JScrollPane scrollPane = new JScrollPane(selectModule);
        scrollPane.setBorder(emptyBorder);
        add(scrollPane, BorderLayout.CENTER);
        
        Box bottomBox = new Box(BoxLayout.Y_AXIS);
        bottomBox.setBorder(emptyBorder);
        
        nameok = true;
        if (naming) {
            nameok = false;
            Box nameBox = new Box(BoxLayout.X_AXIS);
            JLabel nameLabel = new JLabel(DialogsMessages.getString("JModuleDialog.name")+":");
            nameBox.add(nameLabel);
            nameBox.add(Box.createHorizontalStrut(10));
            nameField = new JTextField();
            nameField.setToolTipText(DialogsMessages.getString("JModuleDialog.modulenametooltip"));
            nameField.addCaretListener(this);
            nameBox.add(nameField);
            bottomBox.add(nameBox);
            bottomBox.add(Box.createVerticalStrut(10));
        }
        
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        createButton = new JButton(DialogsMessages.getString("JModuleDialog.create"));
        createButton.setToolTipText(DialogsMessages.getString("JModuleDialog.createtooltip"));
        createButton.addActionListener(this);
        buttonBox.add(createButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        
        cancelButton = new JButton(DialogsMessages.getString("JModuleDialog.cancel"));
        cancelButton.setToolTipText(DialogsMessages.getString("JModuleDialog.canceltooltip"));
        cancelButton.addActionListener(this);
        buttonBox.add(cancelButton);
        bottomBox.add(buttonBox);
        
        add(bottomBox, BorderLayout.SOUTH);
        
        installEscapeKey(this);
        Action enterAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createButton.doClick();
            }
        };         
        installEnterKey(this, enterAction);
        
        pack();
        
        updateButtonState();
    }

    
    public void reset() {
        if (naming) {
            nameField.setText("");
        }
        if (selectModule != null) {
            selectModule.clear();
        }
    }
    
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            module = null;
            setVisible(false);
        }
        else if (e.getSource() == createButton) {
            module = selectModule.getModule();
            if (naming) {
                String name = nameField.getText().trim();
                if (name.length() == 0) {
                    JOptionPane.showMessageDialog(this, DialogsMessages.getString("JModuleDialog.namenotempty"), DialogsMessages.getString("JModuleDialog.nameerror"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (module != null) {
                    rep.registerModule(name, module);
                }
            }
            if (module != null) {
                setVisible(false);
            }
            else {
                JOptionPane.showMessageDialog(this, "Cannot create module.", "Error creating module", JOptionPane.ERROR_MESSAGE);
            }
        }
        updateButtonState();
    }
    
    
    public Module getModule() {
        return module;
    }
    

    public void caretUpdate(CaretEvent e) {
        if (e.getSource() == nameField) {
            nameok = nameField.getText().trim().length() > 0;
            updateButtonState();
        }
    }
    
    
    private void updateButtonState() {
        createButton.setEnabled(nameok);
    }
    

    private JSelectModule selectModule;
    protected  JButton    createButton;
    private JButton       cancelButton;
    private JTextField    nameField;
    
    private boolean naming;
    private boolean nameok;
    private Module  module;
    
    private static final Repository rep = Repository.systemRepository();
    private static final Border emptyBorder = BorderFactory.createEmptyBorder(2, 5, 2, 5);
}
