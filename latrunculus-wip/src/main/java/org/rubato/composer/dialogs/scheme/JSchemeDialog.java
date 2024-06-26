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

package org.rubato.composer.dialogs.scheme;

import static org.rubato.composer.Utilities.installEscapeKey;

import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.plugin.base.RunInfo;
import org.vetronauta.latrunculus.plugin.base.PluginNode;
import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.scheme.expression.Env;
import org.vetronauta.latrunculus.core.scheme.Evaluator;
import org.vetronauta.latrunculus.core.scheme.expression.SExpr;

public class JSchemeDialog
        extends JDialog
        implements ActionListener, KeyListener, Observer {

    public JSchemeDialog(Frame frame) {
        super(frame, SchemeMessages.getString("JSchemeDialog.schemeinteraction"), false);
        setLayout(new BorderLayout());

        rep = Repository.systemRepository();
        rep.addObserver(this);
        env = rep.getSchemeEnvironment(); 
        runInfo = new EvalRunInfo();
        runInfo.stop = false;
        evaluator = new Evaluator(env);
        evaluator.setRunInfo(runInfo);
        
        textFont = Font.decode("monospaced"); 
        
        outputArea = new JTextArea(15, 50);
        outputArea.setFont(textFont);
        outputArea.setFocusable(false);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        
        inputArea = new JTextArea(5, 0);
        inputArea.setFont(textFont);
        inputArea.addKeyListener(this);
        add(new JScrollPane(inputArea), BorderLayout.NORTH);
        
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        
        errorArea = new JTextArea(1, 0);
        errorArea.setForeground(Color.RED);
        errorArea.setFocusable(false);
        errorArea.setEditable(false);
        bottomPanel.add(new JScrollPane(errorArea), BorderLayout.NORTH);        
        
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        
        buttonBox.add(Box.createHorizontalGlue());

        stopButton = new JButton(SchemeMessages.getString("JSchemeDialog.stopbutton"));
        stopButton.setToolTipText(SchemeMessages.getString("JSchemeDialog.stoptooltip"));
        stopButton.addActionListener(this);
        buttonBox.add(stopButton);

        buttonBox.add(Box.createHorizontalStrut(3));

        clearButton = new JButton(SchemeMessages.getString("JSchemeDialog.clearbutton"));
        clearButton.setToolTipText(SchemeMessages.getString("JSchemeDialog.cleartooltip"));
        clearButton.addActionListener(this);
        buttonBox.add(clearButton);

        buttonBox.add(Box.createHorizontalStrut(3));

        initButton = new JButton(SchemeMessages.getString("JSchemeDialog.initbutton"));
        initButton.setToolTipText(SchemeMessages.getString("JSchemeDialog.inittooltip"));
        initButton.addActionListener(this);
        buttonBox.add(initButton);
        
        buttonBox.add(Box.createHorizontalGlue());
        
        bottomPanel.add(buttonBox);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        for (int i = 0; i < RING_SIZE; i++) {
            ringBuffer[i] = ""; 
        }
        ring_pos = 0;
        select_pos = 0;

        installEscapeKey(this);

        pack();
    }
    

    public void keyTyped(KeyEvent e) {}


    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER &&
            e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
            final String string = inputArea.getText().trim();
            Runnable runnable = new Runnable() {
                public void run() {
                    SExpr res = evaluator.eval(string);
                    finish(res);
                }
            };
            thread = new Thread(runnable);
            thread.start();
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP &&
                 e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
            select_pos = NumberTheory.mod(select_pos-1, RING_SIZE);
            inputArea.setText(ringBuffer[select_pos]);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN &&
                 e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
           select_pos = NumberTheory.mod(select_pos+1, RING_SIZE);
           inputArea.setText(ringBuffer[select_pos]);
        }
    }

    
    public void finish(SExpr res) {
        if (evaluator.hasErrors()) {
            errorArea.setText(evaluator.getErrors().get(0));
        }
        else if (res != null) {                
            outputArea.append(res.toString());
            outputArea.append("\n"); 
            outputArea.setCaretPosition(outputArea.getDocument().getLength()-1);
            ringBuffer[ring_pos] = inputArea.getText();
            ring_pos = (ring_pos+1) % RING_SIZE;
            select_pos = ring_pos;
            inputArea.setText(null);
            errorArea.setText(null);
        }
        evaluator.clearErrors();
    }

    
    public void keyReleased(KeyEvent e) {}

    
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == clearButton) {
            outputArea.setText(null);
        }
        else if (src == stopButton) {
            runInfo.stop = true;
        }
        else if (src == initButton) {
            int res = JOptionPane.showConfirmDialog(this,
                                                    SchemeMessages.getString("JSchemeDialog.10")+
                                                    SchemeMessages.getString("JSchemeDialog.11"),
                                                    SchemeMessages.getString("JSchemeDialog.12"),
                                                    JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                env.resetGlobal();
            }
        }
    }

    
    public void update(Observable o, Object arg) {
        evaluator = new Evaluator(env);
        evaluator.setRunInfo(runInfo);
    }
    

    protected static class EvalRunInfo implements RunInfo {
        public boolean stopped() {
            return stop;
        }
        public boolean stop = false;
        public void addMessage(PluginNode rubette, String msg) {}
    }
    
    protected Evaluator evaluator;
    private   Thread    thread;
    
    private EvalRunInfo runInfo; 

    private JTextArea inputArea;
    private JTextArea outputArea;
    private JTextArea errorArea;
    private JPanel    bottomPanel;
    private JButton   clearButton;
    private JButton   initButton;
    private JButton   stopButton;
    
    private static final int RING_SIZE = 30;
    
    private String[] ringBuffer = new String[RING_SIZE];
    private int ring_pos = 0;
    private int select_pos = 0;
 
    private Env env;
    private Repository rep;
    
    private Font textFont;
}
