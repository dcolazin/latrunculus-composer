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
import org.vetronauta.latrunculus.client.plugin.icons.Icons;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;

import javax.swing.*;
import java.awt.*;


public class MuxRubette extends AbstractRubette {

    public MuxRubette() {
        setInCount(2);
        setOutCount(1);
    }

    
    public void run(RunInfo runInfo) {
        int n = 0;
        Denotator d = getInput(0);
        if (d instanceof SimpleDenotator) {
            SimpleDenotator sd = (SimpleDenotator)d;
            if (sd.getSimpleForm().getModule() == ZRing.ring) {
                n = sd.getInteger();
                if (n < 0) { n = 0; }
                if (n > getInCount()-2) { n = getInCount()-2; }
            }
            else {
                addError(BuiltinMessages.getString("MuxRubette.nonintegererror"));
                return;
            }
        }
        else {
            addError(BuiltinMessages.getString("MuxRubette.nonintegererror"));
            return;            
        }
        Denotator output = getInput(n+1);
        if (output == null) {
            addError(BuiltinMessages.getString("MuxRubette.nullerror"), n+1);
            return;
        }
        setOutput(0, output);
    }


    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "Mux"; 
    }

    
    public Rubette duplicate() {
        MuxRubette newRubette = new MuxRubette();
        newRubette.setInCount(getInCount());
        return newRubette;
    }

    
    public boolean hasProperties() {
        return true;
    }


    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();            
            properties.setLayout(new BorderLayout());
            
            inSlider = new JConnectorSliders(true, false);
            inSlider.setInLimits(2, 8);
            inSlider.setInValue(getInCount());
            properties.add(inSlider, BorderLayout.CENTER);
        }
        return properties;
    }


    public boolean applyProperties() {
        setInCount(inSlider.getInValue());
        return true;
    }


    public void revertProperties() {
        inSlider.setInValue(getInCount());
    }

    
    public String getShortDescription() {
        return "Selects an input denotator";
    }

    public String getLongDescription() {
        return "The Mux Rubette selects in input denotator"+
               " based on the integer at input #0.";
    }


    public String getInTip(int i) {
        if (i == 0) {
            return "Integer selector"; 
        }
        else {
            return "Input denotator #"+(i-1); 
        }
    }


    public String getOutTip(int i) {
        return "Output denotator"; 
    }
    
    private JPanel properties = null;
    private JConnectorSliders inSlider = null;

}
