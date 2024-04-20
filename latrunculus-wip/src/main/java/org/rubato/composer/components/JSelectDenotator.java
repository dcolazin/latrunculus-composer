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

package org.rubato.composer.components;

import static org.rubato.composer.Utilities.makeTitledBorder;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import org.vetronauta.latrunculus.core.repository.Dictionary;
import org.rubato.composer.dialogs.JSelectDenotatorDialog;
import org.rubato.composer.dialogs.denotators.JDenotatorDialog;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;


public class JSelectDenotator extends JPanel implements ActionListener {

    public JSelectDenotator(Dictionary dict) {
        this.dict = dict;
        createLayout();
    }

    
    public JSelectDenotator(Dictionary dict, FormDenotatorTypeEnum type) {
        this.type = type;
        this.dict = dict;
        createLayout();
    }
    

    public JSelectDenotator(Dictionary dict, Form form) {
        this.dict = dict;
        this.form = form;
        createLayout();
    }
    
    
    public JSelectDenotator(Dictionary dict, Form form, String label) {
        this.dict = dict;
        this.form = form;
        this.label = label;
        createLayout();
    }
    
    
    public Denotator getDenotator() {
        return denotator;
    }
    
    
    public void setDenotator(Denotator d) {
        denotator = d;
        updateDenotatorLabel();
    }
    
    
    public void clear() {
        denotator = null;
        updateDenotatorLabel();
    }
    
    
    public void disableBorder() {
        setBorder(null);
    }
    
    
    private void createLayout() {
        setLayout(new BorderLayout(5, 0));        
        StringBuilder buf = new StringBuilder(30);
        if (label == null) {
            buf.append(SELECT_DENOTATOR);
            buf.append(": "); 
        }
        else {
            buf.append("["); 
            buf.append(label);
            buf.append("] "); 
        }
        if (form != null) {
            buf.append(form.getNameString());
        }
        else if (type != null) {
            buf.append(type);
        }
        setBorder(makeTitledBorder(buf.toString()));
        
        denotatorLabel = new JLabelField();
        denotatorLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectDenotator();
            }
        });
        add(denotatorLabel, BorderLayout.CENTER);
        
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalStrut(5));
        if (form != null) {
            createButton = new JButton(CREATE_BUTTON);
            createButton.setToolTipText(CREATE_BUTTON_TIP);
            createButton.addActionListener(this);
            buttonBox.add(createButton);
            buttonBox.add(Box.createHorizontalStrut(5));
        }
        selectButton = new JButton(SELECT_BUTTON);
        selectButton.setToolTipText(SELECT_BUTTON_TIP);        
        selectButton.addActionListener(this);
        buttonBox.add(selectButton);
        
        add(buttonBox, BorderLayout.EAST);
    }
    

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == selectButton) {
            selectDenotator();
        }
        else if (src == createButton) {
            createDenotator();
        }
    }

    
    protected void selectDenotator() {
        Denotator res = null;
        if (type != null) {
            res = JSelectDenotatorDialog.showDialog(this, dict, type);
        }
        else if (form != null) {
            res = JSelectDenotatorDialog.showDialog(this, dict, form);
        }
        else {
            res = JSelectDenotatorDialog.showDialog(this, dict);
        }
        if (res != null) {
            denotator = res;
            updateDenotatorLabel();
        }
        fireActionEvent();
    }
    
    
    private void createDenotator() {
        if (form != null) {
            Frame frame = JOptionPane.getFrameForComponent(this);
            JDenotatorDialog dialog = new JDenotatorDialog(frame, true, false, form);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            Denotator res = dialog.getDenotator();
            if (res != null) {
                denotator = res;
                updateDenotatorLabel();
                fireActionEvent();        
            }
        }
    }
    
    
    private void updateDenotatorLabel() {
        String name;
        if (denotator == null) {
            name = " "; 
        }
        else {
            name = denotator.getNameString();
            if (name.length() == 0) {
                name += ANONYMOUS;
            }
            name += " : "+denotator.getForm().getNameString(); 
        }
        denotatorLabel.setText(name);
    }
    

    private ActionEvent       actionEvent  = null;
    private EventListenerList listenerList = new EventListenerList();
    private static final String EMPTY_STRING = ""; 

    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }


    protected void fireActionEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ActionListener.class) {
                if (actionEvent == null) {
                    actionEvent = new ActionEvent(this, 0, EMPTY_STRING);
                }
                ((ActionListener)listeners[i+1]).actionPerformed(actionEvent);
            }
        }
    }

    
    private JLabelField denotatorLabel;
    private JButton     createButton;
    private JButton     selectButton;
    
    private Form      form      = null;
    private FormDenotatorTypeEnum type      = null;
    private String    label     = null;
    private Denotator denotator = null;
    
    private Dictionary dict = null;
    
    private static final String CREATE_BUTTON = Messages.getString("JSelectDenotator.create"); 
    private static final String CREATE_BUTTON_TIP = Messages.getString("JSelectDenotator.createtip"); 
    private static final String SELECT_BUTTON = Messages.getString("JSelectDenotator.select"); 
    private static final String SELECT_BUTTON_TIP = Messages.getString("JSelectDenotator.selecttip"); 
    private static final String ANONYMOUS = Messages.getString("JSelectDenotator.anonymous"); 
    private static final String SELECT_DENOTATOR = Messages.getString("JSelectDenotator.selectdenotator");  
}