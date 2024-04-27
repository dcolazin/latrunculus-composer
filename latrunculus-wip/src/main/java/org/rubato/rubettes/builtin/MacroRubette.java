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

import org.rubato.composer.icons.Icons;
import org.rubato.composer.network.NetworkModel;
import org.vetronauta.latrunculus.plugin.base.PluginNode;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;

import javax.swing.*;


public class MacroRubette extends AbstractRubette {

    public MacroRubette() {
        setInCount(0);
        setOutCount(0);
    }

    
    public void run(RunInfo runInfo) {
        if (inputRubette != null) {
            for (int i = 0; i < getInCount(); i++) {
                inputRubette.setValue(i, getInput(i));
            }
        }
        
        for (int i = 0; i < networkModel.getDependents().size(); i++) {
            if (runInfo.stopped()) { break; }
            PluginNode model = networkModel.getDependents().get(i);
            Rubette rubette = model.getRubette();
            rubette.clearErrors();
            try {
                rubette.run(runInfo);
                if (rubette.hasErrors()) {
                    addError(BuiltinMessages.getString("MacroRubette.error"));
                }
                else {
                    rubette.updateView();
                }
            }
            catch (Exception e) {
                addError(e);
            }
        }
        
        if (outputRubette != null) {
            for (int i = 0; i < getOutCount(); i++) {
                setOutput(i, outputRubette.getValue(i));
            }
        }
    }

    
    public void setNetworkModel(NetworkModel model) {
        networkModel = model;
        for (PluginNode rmodel : model.getRubettes()) {
            Rubette arubette = rmodel.getRubette();
            if (arubette instanceof MacroInputRubette) {
                inputRubette = (MacroInputRubette)arubette;
            }
            else if (arubette instanceof MacroOutputRubette) {
                outputRubette = (MacroOutputRubette)arubette;
            }
        }
        
        networkModel.computeDependencyTree();
        
        if (inputRubette != null) {
            setInCount(inputRubette.getOutCount());
        }
        else {
            setInCount(0);
        }
        if (outputRubette != null) {
            setOutCount(outputRubette.getInCount());
        }
        else {
            setOutCount(0);
        }
    }
    
    
    public NetworkModel getNetworkModel() {
        return networkModel;        
    }
    
    
    public String getGroup() {
        return RubatoConstants.MACRO_GROUP;
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String s) {
        name = s;
        if (networkModel != null) {
            networkModel.setName(name);
        }
    }
    
    
    public void setInfo(String s) {
        info = s;
    }


    public boolean hasInfo() {
        return getInfo().length() != 0;
    }
    
    
    public String getInfo() {
        return info==null?"":info;
    }
    
    
    public Rubette newInstance() {
        return duplicate();
    }
    

    public Rubette duplicate() {
        MacroRubette newRubette = new MacroRubette();
        newRubette.setName(getName());
        newRubette.setInfo(getInfo());
        newRubette.setShortDescription(getShortDescription());
        newRubette.setLongDescription(getLongDescription());
        if (networkModel != null) {
            NetworkModel newModel = networkModel.newInstance();
            newRubette.setNetworkModel(newModel);
        }
        return newRubette;
    }
    
    
    public void setShortDescription(String s) {
        shortDesc = s;
    }

    
    public String getShortDescription() {
        return shortDesc;
    }

    
    public ImageIcon getIcon() {
        return icon;
    }
    

    public void setLongDescription(String s) {
        longDesc = s;
    }


    public String getLongDescription() {
        return longDesc;
    }


    public String getInTip(int i) {
        if (inputRubette != null) {
            return inputRubette.getOutTip(i);
        }
        else {
            return "";
        }
    }


    public String getOutTip(int i) {
        if (outputRubette != null) {
            return outputRubette.getInTip(i);
        }
        else {
            return "";
        }
    }

    public String toString() {
        return "MacroRubette["+networkModel+"]";
    }
    
    
    private String name      = "MacroRubette";
    private String info      = null;
    private String shortDesc = "";
    private String longDesc  = "";
    
    private NetworkModel       networkModel  = null;
    private MacroInputRubette  inputRubette  = null;
    private MacroOutputRubette outputRubette = null;
    
    private static final ImageIcon icon;

    static {
        icon = Icons.loadIcon(MacroRubette.class, "/images/rubettes/builtin/neticon.png");
    }
}
