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

package org.rubato.composer.rubette;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.rubato.base.Rubette;
import org.rubato.rubettes.builtin.MacroRubette;
import org.rubato.xml.XMLWriter;

@Getter
@Setter
public class RubetteModel {

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
    private final LinkedList<RubetteModel> dependencies = new LinkedList<>();
    private final LinkedList<RubetteModel> dependents = new LinkedList<>();

    public RubetteModel(@NonNull Rubette rubette, String name) {
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
        for (int i = 0; i < Math.min(inputs.length, newInputs.length); i++) {
            newInputs[i] = inputs[i];            
        }
        inputs = newInputs;
        //TODO test before change
        //inputs = Arrays.copyOf(inputs, Math.min(inputs.length, rubette.getInCount()));
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
    
    public List<RubetteModel> getFirstDependents() {
        List<RubetteModel> alldependents = new LinkedList<>();
        for (Link link : outLinks) {
            alldependents.add(link.getDestModel());
        }
        return alldependents;
    }
    
    
    public List<RubetteModel> getDependencies() {
        return dependencies;
    }
    
    
    public List<RubetteModel> getFirstDependencies() {
        List<RubetteModel> alldependencies = new LinkedList<>();
        for (Link link : inLinks) {
            alldependencies.add(link.getSrcModel());
        }
        return alldependencies;
    }
    
    
    public void computeDependents() {
        dependents.clear();
        for (Link link : outLinks) {
            RubetteModel dest = link.getDestModel();
            dependents.add(dest);
            for (RubetteModel model : dest.getDependents()) {
                if (!dependents.contains(model)) {
                    dependents.add(model);
                }
            }
        }
    }

    
    public void computeDependencies() {
        dependencies.clear();
        for (Link link : inLinks) {
            RubetteModel src = link.getSrcModel();
            dependencies.add(src);
            for (RubetteModel model : src.getDependencies()) {
                if (!dependencies.contains(model)) {
                    dependencies.add(model);
                }
            }
        }
    }
    
    
    public RubetteModel duplicate() {
        RubetteModel newModel = new RubetteModel(rubette.duplicate(), name);
        newModel.setLocation(getLocation());
        return newModel;
    }
    
    
    public void toXML(XMLWriter writer) {
        rubette.toXML(writer);
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
    
    
    public RubetteModel newInstance() {
        return duplicate();
    }
    
    
    public String toString() {
        return "RubetteModel["+getName()+"]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
