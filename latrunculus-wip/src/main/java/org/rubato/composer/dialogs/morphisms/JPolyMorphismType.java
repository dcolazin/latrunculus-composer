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

package org.rubato.composer.dialogs.morphisms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import org.rubato.composer.components.JSimpleEntry;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.generic.PolynomialRing;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.PolynomialMorphism;

class JPolyMorphismType extends JMorphismType implements ActionListener {

    public JPolyMorphismType(JMorphismContainer container) {
        this.container = container;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Ring domain = (Ring)container.getDomain();
        String indeterminate = getIndeterminate(domain);
        
        PolynomialRing polynomialRing = PolynomialRing.make(domain, indeterminate); 
        simpleEntry = JSimpleEntry.make(polynomialRing);
        add(simpleEntry);
        
        add(Box.createVerticalStrut(5));
        
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        JButton applyButton = new JButton(MorphismsMessages.getString("JMorphismDialog.apply"));
        applyButton.addActionListener(this);
        buttonBox.add(applyButton);
        add(buttonBox);
    }
    
    
    public void actionPerformed(ActionEvent e) {
        ModuleElement value = simpleEntry.getValue();
        if (value != null) {
            container.setMorphism(new PolynomialMorphism((PolynomialElement)value));
        }
        else {
            container.setMorphism(null);
        }
    }
    
    
    public void editMorphism(ModuleMorphism morphism) {
        PolynomialElement polynomial = ((PolynomialMorphism)morphism).getPolynomial();
        simpleEntry.setValue(polynomial);
    }
    
    
    private String getIndeterminate(Ring ring) {
        int i = 0;
        if (ring instanceof PolynomialRing) {
            while (i < indeterminates.length &&
                  ((PolynomialRing)ring).hasIndeterminate(indeterminates[i])) {
                i++;
            }
        }
        return indeterminates[i]; 
    }
    
    private JSimpleEntry simpleEntry;

    private final JMorphismContainer container;
    
    private static final String[] indeterminates = { "X", "Y", "Z", "U", "V", "W", "S", "T" };
}