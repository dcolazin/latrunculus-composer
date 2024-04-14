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

import org.vetronauta.latrunculus.core.repository.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import static org.rubato.composer.Utilities.installEscapeKey;

public final class JSelectFormDialog extends JDialog {

    public static Form showDialog(Component comp, RubatoDictionary dict) {
        Frame frame = JOptionPane.getFrameForComponent(comp);
        JSelectFormDialog dialog = new JSelectFormDialog(frame, comp);
        dialog.setForms(dict.getForms());
        dialog.setVisible(true);
        return dialog.getValue();
    }
    
    
    public static Form showDialog(Component comp, RubatoDictionary dict,
                                  Form baseForm, ArrayList<FormDenotatorTypeEnum> types) {
        Frame frame = JOptionPane.getFrameForComponent(comp);
        JSelectFormDialog dialog = new JSelectFormDialog(frame, comp, types);
        LinkedList<Form> newList = new LinkedList<>();
        for (Form f : dict.getForms()) {
            if (types.contains(f.getType())) {
                if (f instanceof PowerForm 
                    && ((PowerForm)f).getForm().equals(baseForm)) {
                    newList.add(f);
                }
                else if (f instanceof ListForm
                         && ((ListForm)f).getForm().equals(baseForm)) {
                    newList.add(f);
                }
            }
        }
        dialog.setForms(newList);
        dialog.setVisible(true);
        return dialog.getValue();
    }

    
    public static Form showDialog(Component comp, RubatoDictionary dict, ArrayList<FormDenotatorTypeEnum> types) {
        Frame frame = JOptionPane.getFrameForComponent(comp);
        JSelectFormDialog dialog = new JSelectFormDialog(frame, comp, types);
        LinkedList<Form> newList = new LinkedList<>();
        for (Form f : dict.getForms()) {
            if (types.contains(f.getType())) {
                newList.add(f);
            }
        }
        dialog.setForms(newList);
        dialog.setVisible(true);
        return dialog.getValue();
    }

    
    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }
    
    
    private JSelectFormDialog(Frame frame, Component comp) {
        super(frame, Messages.getString("JSelectFormDialog.selectform"), true); 
        createContents();
        setLocationRelativeTo(comp);
        installEscapeKey(this);
    }
    
    
    private JSelectFormDialog(Frame frame, Component comp, ArrayList<FormDenotatorTypeEnum> types) {
        super(frame, Messages.getString("JSelectFormDialog.selectform"), true); 
        createContents();
        String s = Messages.getString("JSelectFormDialog.type")+": "+types.get(0);
        
        for (int i = 1; i < types.size(); i++) {
            s += ", "+types.get(i);
        }
        infoLabel.setText(s);
        setLocationRelativeTo(comp);
        installEscapeKey(this);
    }
    
    
    private void createContents() {
        setLayout(new BorderLayout());
        infoLabel = new JLabel();
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(infoLabel, BorderLayout.NORTH);
        
        listModel = new FormListModel();
        formList = new JList(listModel);
        formList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    ok();
                }
                super.mouseClicked(e);
            }
        });
        formList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                FormInfo info = (FormInfo)formList.getSelectedValue();
                setValue(info.form);
            }
        });
        JScrollPane scrollPane = new JScrollPane(formList);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 5, 5));
        
        cancelButton = new JButton(Messages.getString("JSelectFormDialog.cancel")); 
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        buttonPanel.add(cancelButton);
        
        okButton = new JButton(Messages.getString("JSelectFormDialog.ok")); 
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ok();
            } 
        });
        buttonPanel.add(okButton);
        
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(buttonPanel, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(okButton);
        pack();
        setAlwaysOnTop(true);
        setValue(null);
    }
    
    
    private void setForms(Collection<Form> c) {
        FormInfo[] infoList = new FormInfo[c.size()];
        int i = 0;
        for (Form f : c) {
            infoList[i++] = new FormInfo(f);
        }
        Arrays.sort(infoList);
        listModel.setForms(infoList);
    }
    
    
    private void setValue(Form f) {
        value = f;
    }
    
    
    private Form getValue() {
        return value;
    }
    
    
    private void cancel() {
        setValue(null);
        setVisible(false);
    }
    
    
    private void ok() {
        setVisible(false);        
    }
    
    
    private JLabel    infoLabel;
    private JList   formList;
    private JButton   cancelButton;
    private JButton   okButton;
    private Form      value;
    private FormListModel listModel;
    
    private static final Dimension PREFERRED_SIZE = new Dimension(200, 300);
    
    class FormListModel extends DefaultListModel {
        
        public void setForms(FormInfo[] infos) {
            for (FormInfo info : infos) {
                addElement(info);
            }
        }
    }
    
    
    class FormInfo implements Comparable<FormInfo> {
        
        public FormInfo(Form f) {
            form = f;
            name = f.getNameString()+": "+f.getTypeString(); 
        }
        
        public int compareTo(FormInfo object) {
            return name.compareTo(object.name);
        }
        
        public String toString() {
            return name;
        }
        
        public Form   form;        
        public String name;        
    }
}
