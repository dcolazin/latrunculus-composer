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

import org.rubato.composer.Utilities;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.matrix.ArrayMatrix;
import org.vetronauta.latrunculus.core.math.matrix.Matrix;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineFreeMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineMultiMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineRingMorphism;
import org.vetronauta.latrunculus.server.parse.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.server.parse.ModuleElementParser;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

class JAffineMorphismType
        extends JMorphismType
        implements ActionListener, CaretListener, FocusListener {

    public JAffineMorphismType(JMorphismContainer container) {
        this.container = container;
        rows = container.getCodomain().getDimension();
        cols = container.getDomain().getDimension();
        ring = container.getDomain().getRing();
        setLayout(new BorderLayout(0, 5));
        setBorder(Utilities.makeTitledBorder("f(x) = A*x+b"));
        
        // matrix and vector
        JPanel valuesPanel = new JPanel();
        valuesPanel.setLayout(new BorderLayout());
                
        Box matrixVectorBox = new Box(BoxLayout.X_AXIS);
                
        // matrix
        Box matrixBox = new Box(BoxLayout.Y_AXIS);
        JPanel matrixPanel = new JPanel();
        matrixPanel.setLayout(new GridLayout(rows+1, cols+1));
        matrixEntries = new JTextField[rows][cols];
        matrixPanel.add(new JLabel("A", SwingConstants.CENTER));
        for (int j = 0; j < cols; j++) {
            matrixPanel.add(new JLabel(Integer.toString(j+1), SwingConstants.CENTER));
        }
        for (int i = 0; i < rows; i++) {
            matrixPanel.add(new JLabel(Integer.toString(i+1), SwingConstants.CENTER));
            for (int j = 0; j < cols; j++) {
                String s = i==j?"1":"0";  
                matrixEntries[i][j] = new JTextField(s, 5);
                matrixEntries[i][j].addCaretListener(this);
                matrixEntries[i][j].addFocusListener(this);
                matrixEntries[i][j].setToolTipText("A["+(i+1)+","+(j+1)+"]");
                matrixPanel.add(matrixEntries[i][j]);
            }
        }
        matrixBox.add(matrixPanel);
        matrixVectorBox.add(matrixBox);
        matrixVectorBox.add(Box.createHorizontalStrut(12));
        
        // vector
        Box vectorBox = new Box(BoxLayout.Y_AXIS);
        JPanel vectorPanel = new JPanel();
        vectorPanel.setLayout(new GridLayout(rows+1, 1));
        vectorPanel.add(new JLabel("b", SwingConstants.CENTER));
        vectorEntries = new JTextField[rows];
        for (int i = 0; i < rows; i++) {
            vectorEntries[i] = new JTextField("0", 5); 
            vectorEntries[i].addCaretListener(this);
            vectorEntries[i].addFocusListener(this);
            vectorEntries[i].setToolTipText("v["+(i+1)+"]");
            vectorPanel.add(vectorEntries[i]);
        }
        vectorBox.add(vectorPanel);
        matrixVectorBox.add(vectorBox);
        
        boolean horizontalScroll = cols > 15;
        boolean verticalScroll = rows > 20;
        if (horizontalScroll || verticalScroll) {
            JScrollPane scrollPane = new JScrollPane(matrixVectorBox);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setMaximumSize(new Dimension(500, 300));
            scrollPane.setPreferredSize(new Dimension(500, 300));
            valuesPanel.add(scrollPane);
        }
        else {
            valuesPanel.add(matrixVectorBox);
        }
        add(valuesPanel, BorderLayout.CENTER);
        
        // buttons
        if ((ring instanceof RRing || ring instanceof QRing ||
             ring instanceof ZRing || ring instanceof ZnRing ||
             ring instanceof CRing)
            && rows == 2 && cols == 2) {
            graphButton = new JButton(Messages.getString("JAffineMorphism.graphical")); 
            graphButton.addActionListener(this);
            add(graphButton, BorderLayout.SOUTH);
        }
    }
    
    
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof JTextField) {
            ((JTextField)e.getSource()).selectAll();
        }
    }
    
    
    public void focusLost(FocusEvent e) {}

    
    public void caretUpdate(CaretEvent e) {
        apply();
    }
    
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == graphButton) {            
            JAffineGraph graph = showGraphDialog();
            if (graph != null) {
                    Matrix matrix = graph.getMatrix();
                    Vector vector = graph.getVector();
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                            setMatrixEntry(i, j, matrix.get(i, j).toString());

                        }
                        setVectorEntry(i, vector.getComponent(i).toString());
                    }
                    container.setMorphism(AffineFreeMorphism.make(ring, matrix, vector));

            }
        }
    }
    
    
    private void apply() {
        boolean ok = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String s = matrixEntries[i][j].getText();
                if (ModuleElementParser.parseElement(ring, s) == null) {
                    ok = false;
                    matrixEntries[i][j].setBackground(Utilities.ERROR_BG_COLOR);
                }
                else {
                    matrixEntries[i][j].setBackground(Color.WHITE);
                }
            }
            String s = vectorEntries[i].getText();
            if (ModuleElementParser.parseElement(ring, s) == null) {
                ok = false;
                vectorEntries[i].setBackground(Utilities.ERROR_BG_COLOR);
            }
            else {
                vectorEntries[i].setBackground(Color.WHITE);
            }
        }
        if (ok) {
            container.setMorphism(createMorphism());
        }
        else {
            container.setMorphism(null);
        }
    }
    
    
    private ModuleMorphism createMorphism() {
        ArrayMatrix matrix = new ArrayMatrix(ring, rows, cols);
        List<RingElement> vector = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.set(i,j,getValue(i, j));
            }
            vector.add(getValue(i));
        }
        return AffineFreeMorphism.make(ring, matrix, new Vector<>(ring, vector));
    }
    

    private String getStringValue(int i, int j) {
        return matrixEntries[i][j].getText().trim();                
    }

    
    private String getStringValue(int i) {
        return vectorEntries[i].getText().trim();                
    }

    
    private RingElement getValue(int i, int j) {
        return (RingElement) ModuleElementParser.parseElement(ring, getStringValue(i, j));
    }

    
    private RingElement getValue(int i) {
        return (RingElement) ModuleElementParser.parseElement(ring, getStringValue(i));
    }

    
    private void setMatrixEntry(int i, int j, String s) {
        matrixEntries[i][j].setText(s);
        matrixEntries[i][j].setCaretPosition(0);
    }
    
    
    private void setVectorEntry(int i, String s) {
        vectorEntries[i].setText(s);
        vectorEntries[i].setCaretPosition(0);
    }
    
    
    private JAffineGraph showGraphDialog() {
        if (ring instanceof RRing ||
            ring instanceof ZRing ||
            ring instanceof QRing) {
            boolean qring = ring instanceof QRing;
            double[][] A = new double[2][2];
            double[] b = new double[2];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    try {
                        if (qring) {
                            A[i][j] = ArithmeticParsingUtils.parseRational(getStringValue(i, j)).doubleValue();
                        }
                        else {
                            A[i][j] = Double.parseDouble(getStringValue(i, j));
                        }
                    }
                    catch (NumberFormatException e) {
                        A[i][j] = 0.0;
                    }
                }
                try {
                    if (qring) {
                        b[i] = ArithmeticParsingUtils.parseRational(getStringValue(i)).doubleValue();
                    }
                    else {
                        b[i] = Double.parseDouble(getStringValue(i));
                    }
                }
                catch (NumberFormatException e) {
                    b[i] = 0.0;
                }
            }
            return JAffineGraphDialog.showDialog(this, ring, A, b);
        }
        else {
            return JAffineGraphDialog.showDialog(this, ring);
        }
    }
    
    
    public void editMorphism(ModuleMorphism morphism) {
        if (morphism instanceof AffineRingMorphism) {
            AffineRingMorphism<?> m = (AffineRingMorphism<?>) morphism;
            matrixEntries[0][0].setText(String.valueOf(m.getA()));
            vectorEntries[0].setText(String.valueOf(m.getB()));
            return;
        }
        if (morphism instanceof AffineMultiMorphism) {
            AffineMultiMorphism m = (AffineMultiMorphism)morphism;
            Matrix matrix = m.getMatrix();
            Vector vector = m.getVector();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrixEntries[i][j].setText(String.valueOf(matrix.get(i, j)));
                }
                vectorEntries[i].setText(String.valueOf(vector.getComponent(i)));
            }
        }
        //TODO injection/projection cases
    }

    
    private int  rows;
    private int  cols;
    private Ring ring;
    
    private JTextField[][]     matrixEntries;
    private JTextField[]       vectorEntries;
    private JMorphismContainer container;
    private JButton            graphButton;
}
