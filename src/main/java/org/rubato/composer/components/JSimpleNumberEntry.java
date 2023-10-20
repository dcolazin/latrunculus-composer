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

import org.rubato.composer.preferences.UserPreferences;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialRing;
import org.vetronauta.latrunculus.core.math.module.rational.QStringRing;
import org.vetronauta.latrunculus.core.math.module.real.RStringRing;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.util.LinkedList;

public class JSimpleNumberEntry
        extends JSimpleEntry
        implements CaretListener {

    public JSimpleNumberEntry(Module module) {
        this.module = module;
        int dim = module.getDimension();
        
        inputFields = new JTextField[dim];        
        for (int i = 0; i < dim; i++) {
            inputFields[i] = new JTextField();
        }
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gbl);

        c.ipadx = 10;
        c.anchor = GridBagConstraints.NORTH;
        for (int i = 0; i < dim; i++) {
            c.weightx = 0.0;
            c.fill = GridBagConstraints.NONE;
            c.gridwidth = GridBagConstraints.RELATIVE;
            JLabel label = (dim == 1)?new JLabel(getSymbol(module)+":"):new JLabel("#"+i+" "+getSymbol(module)+":");       //$NON-NLS-3$ //$NON-NLS-4$
            gbl.setConstraints(label, c);
            add(label);
            
            c.weightx = 1.0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = GridBagConstraints.REMAINDER;
            gbl.setConstraints(inputFields[i], c);
            inputFields[i].addCaretListener(this);
            add(inputFields[i]);
        }
    }
    

    public void caretUpdate(CaretEvent e) {
        fireActionEvent();
    }
    
    public boolean valueIsValid() {
        return false;
    }

    
    public void clear() {
        for (int i = 0; i < inputFields.length; i++) {
            inputFields[i].setText("");  
        }
    }
    
    
    public ModuleElement getValue() {
        boolean error = false;
        LinkedList<ModuleElement> list = new LinkedList<>();
        Ring ring = module.getRing();
        for (int i = 0; i < inputFields.length; i++) {
            String s = inputFields[i].getText();
            ModuleElement element = ring.parseString(s);            
            if (element == null) {
                error = true;
                inputFields[i].setBackground(prefs.getEntryErrorColor());
            }
            else {
                inputFields[i].setBackground(Color.WHITE);
                list.add(element);
            }
        }
        return error?null:module.createElement(list);
    }
    
    
    public void setValue(ModuleElement element) {
        for (int i = 0; i < inputFields.length; i++) {
            String s = element.getComponent(i).stringRep();
            inputFields[i].setText(s);
        }
    }

    
    public static String getSymbol(Module<?,?> module) {
        String s = "";  
        if (module instanceof FreeModule) {
            if (module.checkRingElement(ArithmeticElement.class)) {
                ArithmeticNumber<?> number = ((ArithmeticElement<?>) module.getZero()).getValue();
                if (number instanceof ArithmeticInteger) {
                    s = "Z";
                } else if (number instanceof ArithmeticModulus) {
                    s = "Z" + ((ZnRing) module.getRing()).getModulus();
                } else if (number instanceof ArithmeticDouble) {
                    s = "R";
                } else if (number instanceof Complex) {
                    s = "C";
                } else if (number instanceof Rational) {
                    s = "Q";
                }
            } else if (module instanceof ZStringRing) {
                s = "Z-String"; 
            } else if (module instanceof ZnStringRing) {
                s = "Z_" + ((ZnRing) module.getRing()).getModulus() + "-String";
            } else if (module instanceof RStringRing) {
                s = "R-String"; 
            } else if (module instanceof QStringRing) {
                s = "Q-String"; 
            } else if (module instanceof PolynomialRing) {
                PolynomialRing r = (PolynomialRing) module;
                s = getSymbol(r.getCoefficientRing()) + "[" + r.getIndeterminate() + "]";  
            } else if (module instanceof ModularPolynomialRing) {
                ModularPolynomialRing r = (ModularPolynomialRing) module;
                s = getSymbol(r.getCoefficientRing()) + "[" + r.getIndeterminate() + "]/(" + r.getModulus().stringRep() + ")";  
            }
        }
        return s;
    }
    
    
    private JTextField[] inputFields;
    private Module       module;
    
    private static final UserPreferences prefs = UserPreferences.getUserPreferences();
}
