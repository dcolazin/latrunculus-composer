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

package org.rubato.composer.objectbrowser;

import static org.rubato.composer.Utilities.installEscapeKey;
import static org.rubato.composer.Utilities.makeTitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.vetronauta.latrunculus.core.repository.Repository;
import org.rubato.composer.dialogs.morphisms.JMorphismDialog;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.scheme.expression.Env;
import org.vetronauta.latrunculus.core.scheme.expression.SExpr;
import org.vetronauta.latrunculus.core.scheme.expression.SType;
import org.vetronauta.latrunculus.core.scheme.expression.Symbol;
import org.vetronauta.latrunculus.server.display.DenotatorDisplay;
import org.vetronauta.latrunculus.server.display.FormDisplay;

/** 
 * @author Gérard Milmeister
 */
public class ObjectBrowser
        extends JDialog
        implements ListSelectionListener, ActionListener, Observer {

    public ObjectBrowser(Frame frame) {
        super(frame, ObjectBrowseMessages.getString("ObjectBrowser.objectbrowser"), false);
        createLayout();
        installEscapeKey(this);
        rep.addObserver(this);
    }

    
    public void reset() {
        updateObjectList();
    }


    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            Object src = e.getSource();
            if (src == typeList) {
                selectedType = typeList.getSelectedIndex();
                updateObjectList();
                objectList.setSelectedIndex(0);
                objectListBorder.setTitle(ObjectBrowseMessages.getString("ObjectBrowser.objects")+" - "+typeStrings[selectedType]);
                repaint();
            }
            else if (src == objectList) {
                ListItem item = (ListItem)objectList.getSelectedValue();
                if (item != null) {
                    objectViewBorder.setTitle(ObjectBrowseMessages.getString("ObjectBrowser.objectview")+" - "+item.name);
                    objectView.setText(item.toDisplay());
                    objectView.setCaretPosition(0);
                }
                else {
                    objectViewBorder.setTitle(ObjectBrowseMessages.getString("ObjectBrowser.objectview"));
                    objectView.setText(null);
                }
                repaint();
            }
        }
    }
    
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editButton) {
            ListItem item = (ListItem)objectList.getSelectedValue();
            item.edit();
        }
    }
    
    
    public void update(Observable o, Object arg) {
        updateObjectList();
    }
    
    
    private void createLayout() {
        setLayout(new BorderLayout());
        
        JPanel listPanel = new JPanel();        
        listPanel.setLayout(new GridLayout(1, 2));
        
        JScrollPane scrollPane;
        
        typeList = new JList(typeStrings);
        typeList.setBorder(BorderFactory.createEtchedBorder());
        typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        typeList.addListSelectionListener(this);
        scrollPane = new JScrollPane(typeList);
        scrollPane.setBorder(makeTitledBorder(ObjectBrowseMessages.getString("ObjectBrowser.objecttype")));
        listPanel.add(scrollPane);
        
        objectList = new JList();
        objectList.setBorder(BorderFactory.createEtchedBorder());
        objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectList.addListSelectionListener(this);
        scrollPane = new JScrollPane(objectList);
        objectListBorder = (TitledBorder)makeTitledBorder(ObjectBrowseMessages.getString("ObjectBrowser.objects"));
        scrollPane.setBorder(objectListBorder);
        listPanel.add(scrollPane);
        
        add(listPanel, BorderLayout.NORTH);
        
        JPanel objectPanel = new JPanel();
        objectPanel.setLayout(new BorderLayout());
        objectView = new JEditorPane();
        objectView.setPreferredSize(new Dimension(500, 300));
        objectView.setEditable(false);
        objectView.setFocusable(false);
        scrollPane = new JScrollPane(objectView);
        objectViewBorder = (TitledBorder)makeTitledBorder(ObjectBrowseMessages.getString("ObjectBrowser.objectview"));
        objectPanel.setBorder(objectViewBorder);
        objectPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(objectPanel, BorderLayout.CENTER);
               
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        editButton = new JButton(ObjectBrowseMessages.getString("ObjectBrowser.editbutton"));
        editButton.addActionListener(this);
        editButton.setEnabled(false);
        buttonBox.add(editButton);
        buttonBox.add(Box.createHorizontalGlue());
        
        add(buttonBox, BorderLayout.SOUTH);
        
        typeList.requestFocus();
        typeList.setSelectedIndex(0);
        pack();
    }
    
    
    private void updateObjectList() {
        editButton.setEnabled(false);
        ListItem[] items;
        switch (selectedType) {
        case MODULE: {
            List<String> moduleNames = rep.getModuleNames();
            items = new ListItem[moduleNames.size()];
            int i = 0;
            for (String name : moduleNames) {
                Module module = rep.getModule(name);
                ListItem item = new ModuleListItem(name, module);
                items[i++] = item;
            }
            break;
        }
        case MODULE_ELEMENT: {
            List<String> elementNames = rep.getModuleElementNames();
            items = new ListItem[elementNames.size()];
            int i = 0;
            for (String name : elementNames) {
                ModuleElement element = rep.getModuleElement(name);
                ListItem item = new ModuleElementListItem(name, element);
                items[i++] = item;
            }
            break;
        }
        case MODULE_MORPHISM: {
            editButton.setEnabled(true);
            List<String> morphismNames = rep.getModuleMorphismNames();
            items = new ListItem[morphismNames.size()];
            int i = 0;
            for (String name : morphismNames) {
                ModuleMorphism morphism = rep.getModuleMorphism(name);
                ListItem item = new ModuleMorphismListItem(name, morphism);
                items[i++] = item;
            }
            break;
        }
        case FORM: {
            List<Form> forms = rep.getForms();
            items = new ListItem[forms.size()];
            int i = 0;
            for (Form form : forms) {
                ListItem item = new FormListItem(form.getNameString(), form);
                items[i++] = item;
            }
            break;
        }
        case DENOTATOR: {
            List<Denotator> denotators = rep.getDenotators();
            items = new ListItem[denotators.size()];
            int i = 0;
            for (Denotator denotator : denotators) {
                ListItem item = new DenotatorListItem(denotator.getNameString(), denotator);
                items[i++] = item;
            }
            break;
        }
        case SCHEME: {
            Env env = rep.getSchemeEnvironment();
            ArrayList<ListItem> list = new ArrayList<ListItem>();
            Set<Entry<Symbol,SExpr>> bindings = env.getBindings();
            for (Entry<Symbol,SExpr> entry : bindings) {
                String key = entry.getKey().toString();
                SExpr val = entry.getValue();
                if (!(val.type() == SType.PRIMITIVE)) {
                    list.add(new SchemeListItem(key, val));
                }
            }
            items = new ListItem[list.size()];
            items = list.toArray(items);
            break;
        }
        default: {
            return;
        }
        }
        Arrays.sort(items);
        objectList.setListData(items);
    }
    
    
    private abstract class ListItem implements Comparable<ListItem> {
        public String name;
        public ListItem(String n) {
            name = n;
        }
        public String toString() {
            return name;
        }
        public String toDisplay() {
            return name;
        }
        public int compareTo(ListItem item) {
            return name.compareTo(item.name);
        }
        public abstract void edit();
    }
    
    
    private class ModuleListItem extends ListItem {
        Module module;
        public ModuleListItem(String n, Module m) {
            super(n);
            module = m;
        }
        public String toDisplay() {
            return module.toVisualString();
        }
        public void edit() {}
    }
    
    
    private class ModuleElementListItem extends ListItem {
        ModuleElement element;
        public ModuleElementListItem(String n, ModuleElement m) {
            super(n);
            element = m;
        }
        public String toDisplay() {
            return element.toString();
        }
        public void edit() {}
    }
    
    
    private class ModuleMorphismListItem extends ListItem {
        ModuleMorphism morphism;
        public ModuleMorphismListItem(String n, ModuleMorphism m) {
            super(n);
            morphism = m;
        }
        public String toDisplay() {
            return morphism.toString();
        }
        public void edit() {
            JMorphismDialog moduleDialog = new JMorphismDialog(JOptionPane.getFrameForComponent(ObjectBrowser.this), true, morphism);
            moduleDialog.setName(name);
            moduleDialog.setLocationRelativeTo(ObjectBrowser.this);
            moduleDialog.setVisible(true);
        }
    }
    
    
    private class FormListItem extends ListItem {
        Form form;
        public FormListItem(String n, Form f) {
            super(n);
            form = f;
        }
        public String toString() {
            return "<html>"+name+": <i>"+form.getTypeString()+"</i></html>";   //$NON-NLS-3$
        }
        public String toDisplay() {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            FormDisplay.display(form, new PrintStream(bs));
            return bs.toString();
        }
        public void edit() {}
    }
    
    
    private class DenotatorListItem extends ListItem {
        Denotator denotator;
        public DenotatorListItem(String n, Denotator d) {
            super(n);
            denotator = d;
        }
        public String toString() {
            return "<html>"+name+": <i>"+denotator.getForm().getNameString()+"</i></html>";   //$NON-NLS-3$
        }
        public String toDisplay() {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            DenotatorDisplay.display(denotator, new PrintStream(bs));
            return bs.toString();
        }
        public void edit() {}
    }

    
    private class SchemeListItem extends ListItem {
        SExpr exp;
        public SchemeListItem(String n, SExpr exp) {
            super(n);
            this.exp = exp;
        }
        public String toString() {
            return "<html>"+name+"</html>";  
        }
        public String toDisplay() {
            return exp.toString();
        }
        public void edit() {}
    }

    
    private JList     typeList;
    private JList     objectList;
    private JEditorPane objectView;
    private JButton   editButton;
    
    private int selectedType;
    
    private TitledBorder objectListBorder;
    private TitledBorder objectViewBorder;

    private static final Repository rep = Repository.systemRepository();
    
    private static final int MODULE          = 0;
    private static final int MODULE_ELEMENT  = 1;
    private static final int MODULE_MORPHISM = 2;
    private static final int FORM            = 3;
    private static final int DENOTATOR       = 4;
    private static final int SCHEME          = 5;
    
    private static final String[] typeStrings = {
        ObjectBrowseMessages.getString("ObjectBrowser.modules"),
        ObjectBrowseMessages.getString("ObjectBrowser.moduleelements"),
        ObjectBrowseMessages.getString("ObjectBrowser.modulemorphisms"),
        ObjectBrowseMessages.getString("ObjectBrowser.forms"),
        ObjectBrowseMessages.getString("ObjectBrowser.denotators"),
        ObjectBrowseMessages.getString("ObjectBrowser.schemeobjects")
    };
}
