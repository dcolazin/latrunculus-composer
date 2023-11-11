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

package org.rubato.composer.dialogs.morphisms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.rubato.composer.components.JMorphismEntry;
import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.PowerMorphism;
import org.vetronauta.latrunculus.core.math.morphism.endo.Endomorphism;

class JPowerMorphismType
        extends JMorphismType 
        implements ActionListener, ChangeListener {
    
    public JPowerMorphismType(JMorphismContainer container) {
        this.container = container;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        add(JMorphismDialog.createTitle("f(x) = g^n(x)")); 
        
        add(Box.createVerticalStrut(10));
        
        morphismEntry = new JMorphismEntry(container.getDomain(), container.getCodomain());
        morphismEntry.addActionListener(this);
        add(morphismEntry);
        
        add(Box.createVerticalStrut(5));
        
        spinner = new JSpinner();
        spinner.setValue(Integer.valueOf(1));
        spinner.addChangeListener(this);
        add(spinner);
    }
    
    
    public void actionPerformed(ActionEvent e) {
        createMorphism();
    }
    
    
    public void stateChanged(ChangeEvent e) {
        int power = (Integer)spinner.getValue();
        if (power < 0) {
            spinner.setValue(0);
        }
        createMorphism();
    }
    
    
    private void createMorphism() {
        ModuleMorphism m = morphismEntry.getMorphism();
        int power = (Integer)spinner.getValue();
        if (m instanceof Endomorphism) {
            try {
                container.setMorphism(((Endomorphism<?,?>)m).power(power));
            } catch (CompositionException ex) {
                container.setMorphism(null);
            }
        }
    }
    
    
    public void editMorphism(ModuleMorphism morphism) {
        spinner.setValue(((PowerMorphism)morphism).getExponent());
    }
    
    
    private JMorphismEntry  morphismEntry;
    private JSpinner        spinner;
    
    private final JMorphismContainer container;
}