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

import static org.rubato.composer.Utilities.installEscapeKey;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.*;

public class JPreferences extends JDialog {

    public JPreferences(Frame frame) {
        super(frame);
        userPrefs = UserPreferences.getUserPreferences();
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JButton closeButton = new JButton(closeAction);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(closeButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        JButton applyButton = new JButton(applyAction);
        bottomPanel.add(applyButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        JButton applyAndCloseButton = new JButton(applyAndCloseAction);
        bottomPanel.add(applyAndCloseButton);
        bottomPanel.add(Box.createHorizontalGlue());
        
        add(bottomPanel, BorderLayout.SOUTH);

        addPrefsPanel(new MainPreferences(userPrefs));
        
        pack();
        setResizable(false);
        setTitle("Rubato Composer: "+ PreferencesMessages.getString("JPreferences.preferences"));
        
        Rectangle r = frame.getBounds();
        Rectangle r1 = getBounds();
        setLocation(r.x+r.width/2-r1.width/2, r.y+r.height/2-r1.height/2);
        installEscapeKey(this);
    }
    
    
    private void addPrefsPanel(JPreferencesPanel prefsPanel) {
        tabbedPane.add(prefsPanel.getTitle(), prefsPanel);
        prefsPanels.add(prefsPanel);
    }
    
    
    protected void apply() {
        for (JPreferencesPanel panel : prefsPanels) {
            panel.apply();
        }
        userPrefs.save();
    }
    
    
    private Action applyAction = new AbstractAction(PreferencesMessages.getString("JPreferences.apply")) {
        public void actionPerformed(ActionEvent e) {
            apply();
        }
    };
    
    
    private Action closeAction = new AbstractAction(PreferencesMessages.getString("JPreferences.close")) {
        public void actionPerformed(ActionEvent e) {
            JPreferences.this.setVisible(false);
        }
    };
    

    private Action applyAndCloseAction = new AbstractAction(PreferencesMessages.getString("JPreferences.applyandclose")) {
        public void actionPerformed(ActionEvent e) {
            apply();
            JPreferences.this.setVisible(false);
        }
    };
        
    
    private JTabbedPane tabbedPane;
    private ArrayList<JPreferencesPanel> prefsPanels = new ArrayList<JPreferencesPanel>();
    private UserPreferences userPrefs;
}
