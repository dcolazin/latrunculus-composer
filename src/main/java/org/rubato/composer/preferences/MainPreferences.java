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

package org.rubato.composer.preferences;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.vetronauta.latrunculus.core.math.element.impl.Rational;

public class MainPreferences extends JPreferencesPanel {

    public MainPreferences(UserPreferences userPrefs) {
        super(userPrefs);
    }


    protected void createLayout() {
        zigzagBox = new JComboBox(linkTypes);
        zigzagBox.setSelectedIndex(userPrefs.getLinkType());
        addPreference(Messages.getString("MainPreferences.defaultlink"), zigzagBox); 
        
        saveGeometryButton = new JCheckBox();
        saveGeometryButton.setSelected(userPrefs.getGeometrySaved());
        addPreference(Messages.getString("MainPreferences.savesize"), saveGeometryButton); 

        askBeforeLeavingButton = new JCheckBox();
        askBeforeLeavingButton.setSelected(userPrefs.getAskBeforeLeaving());
        addPreference(Messages.getString("MainPreferences.askleaving"), askBeforeLeavingButton); 

        Box box = Box.createHorizontalBox();
        defaultQuantField = new JTextField();
        defaultQuantField.setText(Integer.toString(userPrefs.getDefaultQuantization()));
        defaultQuantField.setMinimumSize(new Dimension(100, 0));
        defaultQuantField.setPreferredSize(new Dimension(100, 0));
        box.add(defaultQuantField);
        box.add(Box.createHorizontalStrut(5));
        JButton quantReset = new JButton(Messages.getString("MainPreferences.reset")); 
        quantReset.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               defaultQuantField.setText("1920"); 
           } 
        });
        box.add(quantReset);
        addPreference(Messages.getString("MainPreferences.defaultquant"), box);  
        
        showProgressButton= new JCheckBox();
        showProgressButton.setSelected(userPrefs.getShowProgress());
        addPreference("Show progress dialog:", showProgressButton);
    }
    
    
    public void apply() {
        userPrefs.setLinkType(zigzagBox.getSelectedIndex());
        userPrefs.setGeometrySaved(saveGeometryButton.isSelected());
        userPrefs.setAskBeforeLeaving(askBeforeLeavingButton.isSelected());
        userPrefs.setDefaultQuantization(Integer.parseInt(defaultQuantField.getText()));
        userPrefs.setShowProgress(showProgressButton.isSelected());
        Rational.setDefaultQuantization(userPrefs.getDefaultQuantization());
    }
    
    
    public String getTitle() {
        return Messages.getString("MainPreferences.main"); 
    }
    
    
    private static final String[] linkTypes = {
        Messages.getString("MainPreferences.line"), 
        Messages.getString("MainPreferences.zigzag"), 
        Messages.getString("MainPreferences.curve") 
    };
    
    private   JComboBox  zigzagBox;
    private   JCheckBox  saveGeometryButton;
    private   JCheckBox  askBeforeLeavingButton;
    private   JCheckBox  showProgressButton;
    protected JTextField defaultQuantField;
}
