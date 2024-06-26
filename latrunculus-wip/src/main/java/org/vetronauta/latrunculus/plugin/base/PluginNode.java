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

package org.vetronauta.latrunculus.plugin.base;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.rubato.composer.rubette.JRubette;
import org.rubato.rubettes.builtin.MacroRubette;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

@Getter
@Setter
public class PluginNode {

    private JRubette jRubette;
    private String name;
    private int serial;
    private Point location;
    private boolean passThrough;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Link[] inputs;

    private final Rubette rubette;
    private final List<Link> inLinks = new ArrayList<>();
    private final List<Link> outLinks = new ArrayList<>();
    private final LinkedList<PluginNode> dependencies = new LinkedList<>();
    private final LinkedList<PluginNode> dependents = new LinkedList<>();

    public PluginNode(@NonNull Rubette rubette, String name) {
        rubette.setModel(this);
        this.rubette = rubette;
        this.name = name;
        this.inputs = new Link[rubette.getInCount()];
    }
    
    public ImageIcon getIcon() {
        return rubette.getIcon();
    }
    
    
    public boolean hasInfo() {
        return rubette.hasInfo();        
    }
    
    
    public boolean hasProperties() {
        return rubette.hasProperties();
    }
    
    
    public boolean hasView() {
        return rubette.hasView();
    }
    
    
    public int getInCount() {
        return rubette.getInCount();
    }
    
    
    public void resizeInputs() {
        Link[] newInputs = new Link[rubette.getInCount()];
        System.arraycopy(inputs, 0, newInputs, 0, Math.min(inputs.length, newInputs.length));
        inputs = newInputs;
    }

    
    public int getOutCount() {
        return rubette.getOutCount();
    }
    
    //
    // Links
    //
    
    public void setInLink(Link link) {
        inLinks.add(link);
        inputs[link.getDestPos()] = link;
    }
    
    
    public Link getInLink(int i) {
        return inputs[i];
    }

    
    public void addOutLink(Link link) {
        outLinks.add(link);        
    }
    
    
    public void removeInLink(Link link) {
        inLinks.remove(link);
        inputs[link.getDestPos()] = null;
    }
    
    
    public void removeOutLink(Link link) {
        outLinks.remove(link);
    }
    
    
    public String getInTip(int i) {
        String s = rubette.getInTip(i);
        return (s != null) ? s : Rubette.defaultInTip(i);
    }
    
    
    public String getOutTip(int i) {
        String s = rubette.getOutTip(i);
        return (s != null) ? s : Rubette.defaultOutTip(i);
    }
    
    public String getShortDescription() {
        return rubette.getShortDescription();
    }

    
    public void setName(String name) {
        this.name = name;
        if (rubette instanceof MacroRubette) {
            ((MacroRubette)rubette).setName(name);
        }
    }
    
    
    public String getInfo() {
        return rubette.getInfo();
    }


    public int getInLinkCount() {
        return inLinks.size();
    }
    

    public int getOutLinkCount() {
        return outLinks.size();
    }
    
    public List<PluginNode> getFirstDependents() {
        List<PluginNode> alldependents = new LinkedList<>();
        for (Link link : outLinks) {
            alldependents.add(link.getDestModel());
        }
        return alldependents;
    }
    
    public List<PluginNode> getFirstDependencies() {
        List<PluginNode> alldependencies = new LinkedList<>();
        for (Link link : inLinks) {
            alldependencies.add(link.getSrcModel());
        }
        return alldependencies;
    }

    public PluginNode duplicate() {
        PluginNode newModel = new PluginNode(rubette.duplicate(), name);
        newModel.setLocation(getLocation());
        return newModel;
    }
    
    
    public void toXML(XMLWriter writer) {
        writer.writeRubette(rubette);
    }

    
    public Point getLocation() {
        if (jRubette != null) {
            location = jRubette.getLocation();
        }
        return location;
    }
    
    public void togglePassThrough() {
        passThrough = !passThrough;
    }

    
    public boolean canPassThrough() {
        return getInCount() > 0 && getOutCount() > 0;
    }
    
    
    public PluginNode newInstance() {
        return duplicate();
    }
    
    
    public String toString() {
        return "RubetteModel["+getName()+"]";  
    }

}
