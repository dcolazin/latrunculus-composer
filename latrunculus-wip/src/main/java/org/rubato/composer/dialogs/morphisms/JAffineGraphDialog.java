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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

import org.vetronauta.latrunculus.core.math.module.generic.Ring;

class JAffineGraphDialog extends JDialog implements ActionListener {

    public static JAffineGraph showDialog(Component comp, Ring ring) {
        Frame frame = JOptionPane.getFrameForComponent(comp);
        JAffineGraphDialog dialog = new JAffineGraphDialog(frame, ring);
        dialog.setLocationRelativeTo(comp);
        dialog.setVisible(true);
        return dialog.getGraph();
    }

    
    public static JAffineGraph showDialog(Component comp, Ring ring, double[][] A, double[] b) {
        Frame frame = JOptionPane.getFrameForComponent(comp);
        JAffineGraphDialog dialog = new JAffineGraphDialog(frame, ring);
        dialog.setTrafo(A, b);
        dialog.setLocationRelativeTo(comp);
        dialog.repaint();
        dialog.setVisible(true);
        return dialog.getGraph();
    }

    
    public JAffineGraphDialog(Frame frame, Ring ring) {
        super(frame, MorphismsMessages.getString("JAffineGraphDialog.designtrafo"), true);
        this.ring = ring;
        createLayout();
    }

    
    public JAffineGraph getGraph() {
        return graph;
    }
    
    
    public void setTrafo(double[][] A, double[] b) {
        graph.setTrafo(A, b);
    }
    
    
    private void createLayout() {
        setLayout(new BorderLayout());
        
        JPanel graphPanel = new JPanel();
        graphPanel.setLayout(new BorderLayout());
        graphPanel.setBorder(paddingBorder);
        
        graph = new JAffineGraph(ring);
        graph.setBorder(lineBorder);
        graph.addActionListener(this);
        graphPanel.add(graph, BorderLayout.CENTER);
        add(graphPanel, BorderLayout.CENTER);
        
        Box bottomBox = new Box(BoxLayout.Y_AXIS);
        
        Box coordBox = new Box(BoxLayout.X_AXIS);
        coordBox.add(Box.createHorizontalStrut(10));
        coordBox.add(new JLabel(" x : ")); 
        xField = new JTextField("0"); 
        xField.setEditable(false);
        coordBox.add(xField);
        coordBox.add(Box.createHorizontalStrut(10));
        coordBox.add(new JLabel(" y : ")); 
        yField = new JTextField("0"); 
        yField.setEditable(false);
        coordBox.add(yField);
        coordBox.add(Box.createHorizontalStrut(10));
        bottomBox.add(coordBox);
        
        bottomBox.add(Box.createVerticalStrut(10));
        
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        buttonBox.add(Box.createGlue());
        applyButton = new JButton(MorphismsMessages.getString("JAffineGraphDialog.apply"));
        applyButton.addActionListener(this);
        buttonBox.add(applyButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        cancelButton = new JButton(MorphismsMessages.getString("JAffineGraphDialog.cancel"));
        cancelButton.addActionListener(this);
        buttonBox.add(cancelButton);
        buttonBox.add(Box.createGlue());        
        bottomBox.add(buttonBox);
        
        add(bottomBox, BorderLayout.SOUTH);
        
        pack();
    }
    
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == applyButton) {
            setVisible(false);
            dispose();
        }
        else if (e.getSource() == cancelButton) {
            graph = null;
            setVisible(false);
            dispose();
        }
        else if (e.getSource() == graph) {
            xField.setText(graph.getCurrentX());
            xField.setCaretPosition(0);
            yField.setText(graph.getCurrentY());
            yField.setCaretPosition(0);
        }
    }

    
    private Ring ring;
    
    private JAffineGraph graph;
    private JButton      applyButton;
    private JButton      cancelButton;
    private JTextField   xField;
    private JTextField   yField;

    private Border paddingBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    private Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
}
