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

package org.rubato.rubettes.builtin;

import org.rubato.composer.components.JConnectorSliders;
import org.rubato.composer.icons.Icons;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;

import javax.swing.*;
import java.awt.*;


/**
 * The Latch Rubette stores its input denotator
 * and provides it on its outputs the number of
 * which can be configured.
 * 
 * @author Gérard Milmeister
 */
public class LatchRubette extends AbstractRubette {

    public LatchRubette() {
        setInCount(1);
        setOutCount(1);
    }
    

    public void run(RunInfo runInfo) {
        Denotator d = getInput(0);
        for (int i = 0; i < getOutCount(); i++) {
            setOutput(i, d);
        }
    }

    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }
    

    public String getName() {
        return "Latch"; 
    }

    
    public Rubette duplicate() {
        LatchRubette newRubette = new LatchRubette();
        newRubette.setOutCount(getOutCount());
        return newRubette;
    }

    
    public boolean hasProperties() {
        return true;
    }


    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();            
            properties.setLayout(new BorderLayout());
            
            outSlider = new JConnectorSliders(false, true);
            outSlider.setOutLimits(1, 8);
            outSlider.setOutValue(getOutCount());
            properties.add(outSlider, BorderLayout.CENTER);
        }
        return properties;
    }


    public boolean applyProperties() {
        setOutCount(outSlider.getOutValue());
        return true;
    }


    public void revertProperties() {
        outSlider.setOutValue(getOutCount());
    }

    
    public String getShortDescription() {
        return "Stores an input denotator";
    }

    
    public ImageIcon getIcon() {
        return icon;
    }
    

    public String getLongDescription() {
        return "The Latch Rubette stores its input denotator"+
               " and provides it on its outputs the number of"+
               " which can be configured.";
    }


    public String getInTip(int i) {
        return "Input denotator"; 
    }


    public String getOutTip(int i) {
        return "Output denotator #"+i; 
    }
    
    private JPanel properties = null;
    private JConnectorSliders outSlider = null;
    private static final ImageIcon icon;

    static {
        icon = Icons.loadIcon(LatchRubette.class, "/images/rubettes/builtin/latchicon.png"); 
    }
}
