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

import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineFreeMorphism;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class JGeometryMorphismType extends JMorphismType implements ActionListener {

    public JGeometryMorphismType(JMorphismContainer container) {
        this.container = container;
        setLayout(new BorderLayout());
        morphismsPanel = new JPanel();
        morphismsPanel.setLayout(new BoxLayout(morphismsPanel, BoxLayout.Y_AXIS));
        buttonBox = new Box(BoxLayout.X_AXIS);
        applyButton = new JButton(Messages.getString("JGeometryMorphism.apply")); 
        applyButton.addActionListener(this);
        buttonBox.add(applyButton);
        buttonBox.add(Box.createHorizontalGlue());
        addButton = new JButton(Messages.getString("JGeometryMorphism.add")); 
        addButton.addActionListener(this);
        buttonBox.add(addButton);
        morphismsPanel.add(buttonBox);        
        add(morphismsPanel, BorderLayout.NORTH);
        JPanel geometryPanel = new JPanel();
        geometryPanel.setLayout(new BorderLayout());
        geometryPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        geometryView = new JGeometryView();
        geometryView.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        geometryPanel.add(geometryView, BorderLayout.CENTER);
        add(geometryPanel, BorderLayout.CENTER);
    }
           
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            JGeometry geo = new JGeometry();
            morphismsList.add(geo);
            relayout();
        }
        else if (e.getSource() == applyButton) {
            apply();
        }
    }
    
    
    protected void relayout() {
        morphismsPanel.removeAll();
        for (JComponent c : morphismsList) {
            morphismsPanel.add(c);
            morphismsPanel.add(Box.createVerticalStrut(10));
        }
        morphismsPanel.add(buttonBox);
        container.pack();
    }
    
    
    private void apply() {
        RMatrix matrix = RMatrix.getUnitMatrix(3);
        for (JGeometry geo : morphismsList) {
            matrix = geo.getMatrix().product(matrix);
        }
        RMatrix A = matrix.getSubMatrix(0, 1, 0, 1);
        List<Real> list = new ArrayList<>(2);
        list.add(matrix.get(0, 2));
        list.add(matrix.get(1, 2));
        container.setMorphism(AffineFreeMorphism.make(RRing.ring, A, new Vector<>(RRing.ring, list)));
        geometryView.setMatrix(matrix);
    }
    
    
    public void editMorphism(ModuleMorphism morphism) {
        // will not be used for editing
    }

    
    private class JGeometry extends JPanel implements KeyListener {
        public JGeometry() {
            final JGeometry geo = this;
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));            
            comboBox = new JComboBox(types);
            comboBox.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   if (!building) { 
                       type = (String)comboBox.getSelectedItem();
                       build();
                   }
               } 
            });
            removeButton = new JButton(Messages.getString("JGeometryMorphism.remove")); 
            removeButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   morphismsList.remove(geo);
                   relayout();
               } 
            });
            build();
        }        
        protected void build() {
            matrix = null;
            removeAll();
            add(comboBox);
            building = true;
            comboBox.setSelectedItem(type);
            building = false;
            add(Box.createHorizontalStrut(10));
            if (type.equals(Messages.getString("JGeometryMorphism.rotation")) || type.equals(Messages.getString("JGeometryMorphism.reflection")) ||  
                type.equals(Messages.getString("JGeometryMorphism.hshearing")) || type.equals(Messages.getString("JGeometryMorphism.vshearing"))) {  
                add(Box.createHorizontalStrut(10));
                add(new JLabel(Messages.getString("JGeometryMorphism.degrees")+":"));  
                add(Box.createHorizontalStrut(10));
                deg = new JTextField("0", 5); 
                deg.addKeyListener(this);
                add(deg);
            }
            else if (type.equals(Messages.getString("JGeometryMorphism.translation"))) { 
                add(Box.createHorizontalStrut(10));
                add(new JLabel(" x:")); 
                add(Box.createHorizontalStrut(10));
                x = new JTextField("0", 5); 
                x.addKeyListener(this);
                add(x);
                add(Box.createHorizontalStrut(10));
                add(new JLabel(" y:")); 
                add(Box.createHorizontalStrut(10));
                y = new JTextField("0", 5); 
                y.addKeyListener(this);
                add(y);                
            }
            else if (type.equals(Messages.getString("JGeometryMorphism.scaling"))) { 
                add(Box.createHorizontalStrut(10));
                add(new JLabel(Messages.getString("JGeometryMorphism.factor")+":"));  
                add(Box.createHorizontalStrut(10));
                factor = new JTextField("1", 5); 
                factor.addKeyListener(this);
                add(factor);
            }
            add(Box.createHorizontalStrut(10));
            add(removeButton);
            relayout();
        }
        public void keyPressed(KeyEvent e) {
            matrix = null;
        }
        public void keyReleased(KeyEvent e) {
            matrix = null;
        }
        public void keyTyped(KeyEvent e) {
            matrix = null;
        }
        public RMatrix getMatrix() {
            if (matrix == null) {
                computeTrafo();
            }
            return matrix;
        }
        private void computeTrafo() {
            if (type.equals(Messages.getString("JGeometryMorphism.rotation"))) { 
                double r = getValue(deg);
                double c = Math.cos(r/180.0*Math.PI);
                double s = Math.sin(r/180.0*Math.PI);
                matrix = RMatrix.getUnitMatrix(3);
                matrix.set(0, 0, c);
                matrix.set(0, 1, -s);
                matrix.set(1, 0, s);
                matrix.set(1, 1, c);
            }
            else if (type.equals(Messages.getString("JGeometryMorphism.reflection"))) { 
                double r = getValue(deg);
                double c = Math.cos(2.0*r*Math.PI/180.0);
                double s = Math.sin(2.0*r*Math.PI/180.0);
                matrix = RMatrix.getUnitMatrix(3);
                matrix.set(0, 0, c);
                matrix.set(0, 1, s);
                matrix.set(1, 0, s);
                matrix.set(1, 1, -c);
            }
            else if (type.equals(Messages.getString("JGeometryMorphism.translation"))) { 
                matrix = RMatrix.getUnitMatrix(3);
                matrix.set(0, 2, getValue(x));
                matrix.set(1, 2, getValue(y));
            }
            else if (type.equals(Messages.getString("JGeometryMorphism.scaling"))) { 
                double s = getValue(factor);
                matrix = RMatrix.getUnitMatrix(3);
                matrix.set(0, 0, s);
                matrix.set(1, 1, s);
            }
            else if (type.equals(Messages.getString("JGeometryMorphism.hshearing"))) { 
                double r = getValue(deg);
                double t = Math.tan(r*Math.PI/180.0); 
                matrix = RMatrix.getUnitMatrix(3);
                matrix.set(0, 1, t);
            }
            else if (type.equals(Messages.getString("JGeometryMorphism.vshearing"))) { 
                double r = getValue(deg);
                double t = Math.tan(r*Math.PI/180.0); 
                matrix = RMatrix.getUnitMatrix(3);
                matrix.set(1, 0, t);
            }
        }
        private double getValue(JTextField f) {
            String s = f.getText();
            double v = 0.0;
            try {
                v = Double.parseDouble(s);
            }
            catch (NumberFormatException e) {
                f.setText("0"); 
            }
            return v;
        }
        protected JComboBox  comboBox;
        private JButton    removeButton;
        protected String   type = Messages.getString("JGeometryMorphism.rotation"); 
        private JTextField x;
        private JTextField y;
        private JTextField factor;
        private JTextField deg;
        private RMatrix    matrix = null;
        protected boolean  building = false;
    }
    
    
    private JMorphismContainer container;
    private JPanel             morphismsPanel;
    private JButton            addButton;
    private Box                buttonBox;
    private JButton            applyButton;
    private JGeometryView      geometryView;
    
    protected ArrayList<JGeometry> morphismsList = new ArrayList<JGeometry>();
    
    protected final String[] types = {
            Messages.getString("JGeometryMorphism.rotation"), 
            Messages.getString("JGeometryMorphism.translation"), 
            Messages.getString("JGeometryMorphism.reflection"), 
            Messages.getString("JGeometryMorphism.scaling"), 
            Messages.getString("JGeometryMorphism.vshearing"), 
            Messages.getString("JGeometryMorphism.hshearing") 
    };
}
