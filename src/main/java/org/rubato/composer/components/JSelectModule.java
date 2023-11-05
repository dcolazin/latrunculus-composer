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

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialModuleFactory;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.repository.RingRepository;
import org.vetronauta.latrunculus.core.math.module.repository.StringRingRepository;
import org.vetronauta.latrunculus.server.parse.ModuleElementParser;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.rubato.composer.Utilities.makeTitledBorder;

/**
 * A component for creating modules.
 * 
 * @author Gérard Milmeister
 */
public class JSelectModule
        extends JPanel
        implements ActionListener, ChangeListener, Scrollable {

    /**
     * Creates a JSelectModule component.
     */
    public JSelectModule() {
        this(false);
    }

    /**
     * Creates a JSelectModule component.
     * 
     * @param isring if true restrict options to creating rings
     */
    public JSelectModule(boolean isring) {
        this.isring = isring;
        setLayout(new BorderLayout());
        createLayout();
    }
    
    
    public void clear() {
        createLayout();
        repack();
    }
    
    
    private void createLayout() {
        removeAll();

        JPanel ringSelectPanel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        ringSelectPanel.setLayout(gridbag);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;        
        c.insets = new Insets(2, 2, 2, 2); 

        JLabel ringLabel = new JLabel(Messages.getString("JSelectModule.ring")+": ");  
        gridbag.setConstraints(ringLabel, c);
        ringSelectPanel.add(ringLabel);
        
        ringSelect = new JComboBox(rings);
        ringSelect.setToolTipText(Messages.getString("JSelectModule.baseringtooltip")); 
        ringSelect.addActionListener(this);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        gridbag.setConstraints(ringSelect, c);
        ringSelectPanel.add(ringSelect);
                
        modLabel = new JLabel(Messages.getString("JSelectModule.modulus")+": ");  
        modLabel.setVisible(false);
        c.gridwidth = 1;
        c.weightx = 0.0;
        gridbag.setConstraints(modLabel, c);
        ringSelectPanel.add(modLabel);

        modSpinner = new JIntegerSpinner(2, Integer.MAX_VALUE);
        modSpinner.setToolTipText(Messages.getString("JSelectModule.modtooltip")); 
        modSpinner.setVisible(false);
        modSpinner.setValue(Integer.valueOf(2));
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        gridbag.setConstraints(modSpinner, c);
        ringSelectPanel.add(modSpinner);

        JLabel polLabel = new JLabel(Messages.getString("JSelectModule.polynomial")+": ");          
        c.gridwidth = 1;
        c.weightx = 0.0;
        gridbag.setConstraints(polLabel, c);
        ringSelectPanel.add(polLabel);

        polSwitch = new JCheckBox();
        polSwitch.setToolTipText(Messages.getString("JSelectModule.polytooltip")); 
        polSwitch.setSelected(false);
        polSwitch.addChangeListener(this);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        gridbag.setConstraints(polSwitch, c);
        ringSelectPanel.add(polSwitch);

        indLabel = new JLabel(Messages.getString("JSelectModule.indeterminate")+": ");  
        indLabel.setVisible(false);
        c.gridwidth = 1;
        c.weightx = 0.0;
        gridbag.setConstraints(indLabel, c);
        ringSelectPanel.add(indLabel);

        indField = new JTextField("X"); 
        indField.setToolTipText(Messages.getString("JSelectModule.indtooltip")); 
        indField.setHorizontalAlignment(SwingConstants.RIGHT);
        indField.setVisible(false);
        indField.addActionListener(this);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        gridbag.setConstraints(indField, c);
        ringSelectPanel.add(indField);
        
        pmodLabel = new JLabel("Polynomial modulus: ");
        pmodLabel.setVisible(false);
        c.gridwidth = 1;
        c.weightx = 0.0;
        gridbag.setConstraints(pmodLabel, c);
        ringSelectPanel.add(pmodLabel);

        pmodField = new JTextField("");
        pmodField.setToolTipText("Polynomial modulus");
        pmodField.setHorizontalAlignment(SwingConstants.RIGHT);
        pmodField.setVisible(false);
        pmodField.addActionListener(this);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        gridbag.setConstraints(pmodField, c);
        ringSelectPanel.add(pmodField);

        dimLabel = new JLabel(Messages.getString("JSelectModule.dimension")+": ");  
        dimLabel.setVisible(!this.isring);
        c.gridwidth = 1;
        c.weightx = 0.0;
        gridbag.setConstraints(dimLabel, c);
        ringSelectPanel.add(dimLabel);

        dimSpinner = new JIntegerSpinner(0, Integer.MAX_VALUE);
        dimSpinner.setToolTipText(Messages.getString("JSelectModule.dimensiontooltip")); 
        dimSpinner.setVisible(!this.isring);
        dimSpinner.setValue(Integer.valueOf(1));
//        dimSpinner.addChangeListener(this);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        gridbag.setConstraints(dimSpinner, c);
        ringSelectPanel.add(dimSpinner);
                
        factorLabel = new JLabel(Messages.getString("JSelectModule.factors")+": ");  
        factorLabel.setVisible(false);
        c.gridwidth = 1;
        c.weightx = 0.0;
        gridbag.setConstraints(factorLabel, c);
        ringSelectPanel.add(factorLabel);

        factorSpinner = new JIntegerSpinner(2, Integer.MAX_VALUE);
        factorSpinner.setToolTipText(Messages.getString("JSelectModule.factornrtooltip")); 
        factorSpinner.setVisible(false);
        factorSpinner.setValue(Integer.valueOf(2));
        factorSpinner.addChangeListener(this);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        gridbag.setConstraints(factorSpinner, c);
        ringSelectPanel.add(factorSpinner);
     
        factorView = new JPanel();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        gridbag.setConstraints(factorView, c);
        ringSelectPanel.add(factorView);
                
        add(ringSelectPanel, BorderLayout.NORTH);
    }
    
    
    /**
     * Returns the constructed module.
     */
    public Module getModule() {
        Module module = null;
        int dim = polSwitch.isSelected()?1:getDimension();
        switch (ringSelect.getSelectedIndex()) {
        case ZRING: {
            module = new VectorModule<>(ZRing.ring, dim);
            break;
        }
        case ZNRING: {
            module = new VectorModule<>(RingRepository.getModulusRing(getModulus()), dim);
            break;
        }
        case QRING: {
            module = new VectorModule<>(QRing.ring, dim);
            break;
        }
        case RRING: {
            module = new VectorModule<>(RRing.ring, dim);
            break;
        }
        case CRING: {
            module = new VectorModule<>(CRing.ring, dim);
            break;
        }
        case ZSTRING: {
            module = new VectorModule<>(StringRingRepository.getRing(ZRing.ring), dim);
            break;
        }
        case ZNSTRING: {
            module = new VectorModule<>(StringRingRepository.getModulusRing(getModulus()), dim);
            break;
        }
        case QSTRING: {
            module = new VectorModule<>(StringRingRepository.getRing(QRing.ring), dim);
            break;
        }
        case RSTRING: {
            module = new VectorModule<>(StringRingRepository.getRing(RRing.ring), dim);
            break;
        }
        case PRODUCT: {
            int factorCount = getFactorCount();
            Ring[] factorRings = new Ring[factorCount];
            for (int i = 0; i < factorCount; i++) {
                factorRings[i] = (Ring)factorModules[i].getModule();
                if (factorRings[i] == null) {
                    return null;
                }
            }
            module = new VectorModule<>(ProductRing.make(factorRings), dim);
            break;
        }
        default:
            break;
        }
        if (polSwitch.isSelected() && module != null) {
            if (getIndeterminate().length() == 0) {
                return null;
            }
            String pmod = pmodField.getText().trim();
            if (pmod.length() > 0) {
                PolynomialRing ring = PolynomialRing.make((Ring)module, getIndeterminate());
                PolynomialElement modulus = (PolynomialElement) ModuleElementParser.parseElement(ring, pmod);
                if (modulus != null) {
                    module = PolynomialModuleFactory.makeModular(modulus, getDimension());
                }
                else {
                    return null;
                }
            }
            else {
                module = PolynomialModuleFactory.make((Ring)module, getIndeterminate(), getDimension());
            }
        }
        return module;
    }
    
    
    public void actionPerformed(ActionEvent e) {
        boolean repack = false;
        if (e.getSource() == ringSelect) {
            int index = ringSelect.getSelectedIndex();
            if (index == ZNRING || index == ZNSTRING) {
                modLabel.setVisible(true);
                modSpinner.setVisible(true);
                repack = true;
            }
            else {
                modLabel.setVisible(false);
                modSpinner.setVisible(false);
                repack = true;
            }
            if (index == PRODUCT) {
                factorLabel.setVisible(true);
                factorSpinner.setVisible(true);
                buildFactorView();
                repack = true;                
            }
            else {
                factorLabel.setVisible(false);
                factorSpinner.setVisible(false);
                factorView.removeAll();
                repack = true;
            }
        }
        else if (e.getSource() == indField) {
            indField.setText(indField.getText().trim());
            if (indField.getText().length() == 0) {
                indField.setText("X"); 
            }
        }
        if (repack) {
            repack();
        }
    }
    
    
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == factorSpinner) {
            buildFactorView();
            repack();
        }
        else if (e.getSource() == polSwitch) {
            indLabel.setVisible(polSwitch.isSelected());
            indField.setVisible(polSwitch.isSelected());
            pmodLabel.setVisible(polSwitch.isSelected());
            pmodField.setVisible(polSwitch.isSelected());
            repack();
        }
    }


    private void buildFactorView() {
        factorView.removeAll();
        factorView.setLayout(new BoxLayout(factorView, BoxLayout.Y_AXIS));
        int factorCount = getFactorCount();
        factorModules = new JSelectModule[factorCount];
        for (int i = 0; i < factorCount; i++) {
            String title = Messages.getString("JSelectModule.factor")+" "+i;  
            JPanel factorViewPart = new JPanel();
            factorViewPart.setToolTipText(TextUtils.replaceStrings(Messages.getString("JSelectModule.factornr"), i)); 
            factorViewPart.setLayout(new BorderLayout());
            factorViewPart.setBorder(makeTitledBorder(title));
            factorModules[i] = new JSelectModule(true);
            factorViewPart.add(factorModules[i]);
            factorView.add(factorViewPart);
        }
    }

    
    private int getDimension() {
        return dimSpinner.getInteger();
    }
    
    
    private int getModulus() {
        return modSpinner.getInteger();
    }
    
    
    private int getFactorCount() {
        return factorSpinner.getInteger();
    }
    
    
    private String getIndeterminate() {
        return indField.getText();
    }
    
    
    private void repack() {
        Component comp = getParent();
        while (!(comp instanceof Window)) {
            comp = comp.getParent();
        }
        ((Window)comp).pack();
    }
    

    public Dimension getPreferredScrollableViewportSize() {
        Dimension dim = getPreferredSize();
        if (dim.height > 500) {
            dim.height = 500;
        }
        return dim;
    }

    
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 50;
    }

    
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 50;
    }

    
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    
    private boolean isring;
    
    private JComboBox       ringSelect;
    private JLabel          dimLabel;
    private JIntegerSpinner dimSpinner;
    private JLabel          modLabel;
    private JIntegerSpinner modSpinner;
    private JLabel          factorLabel;
    private JIntegerSpinner factorSpinner;
    private JCheckBox       polSwitch;
    private JLabel          indLabel;
    private JTextField      indField;
    private JLabel          pmodLabel;
    private JTextField      pmodField;
    private JPanel          factorView;
    
    private JSelectModule[] factorModules;

    //TODO enum
    private static final int ZRING    = 0;
    private static final int ZNRING   = 1;
    private static final int QRING    = 2;
    private static final int RRING    = 3;
    private static final int CRING    = 4;
    private static final int ZSTRING  = 5;
    private static final int ZNSTRING = 6;
    private static final int QSTRING  = 7;
    private static final int RSTRING  = 8;
    private static final int PRODUCT  = 9;
    
    private String[] rings = {
            "Z ("+Messages.getString("JSelectModule.integers")+")",   //$NON-NLS-3$
            "Zn ("+Messages.getString("JSelectModule.modintegers")+")",   //$NON-NLS-3$
            "Q ("+Messages.getString("JSelectModule.rationals")+")",   //$NON-NLS-3$
            "R ("+Messages.getString("JSelectModule.reals")+")",   //$NON-NLS-3$
            "C ("+Messages.getString("JSelectModule.complexes")+")",   //$NON-NLS-3$
            "Z-String", 
            "Zn-String", 
            "C-String", 
            "R-String", 
            Messages.getString("JSelectModule.productring") 
    };
}
