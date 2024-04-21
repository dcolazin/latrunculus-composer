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

package org.rubato.composer.network;

import org.rubato.composer.JComposer;
import org.rubato.composer.notes.NoteModel;
import org.rubato.composer.rubette.Link;
import org.rubato.composer.rubette.RubetteModel;

import java.util.ArrayList;
import java.util.Collections;

public class NetworkModel {

    public NetworkModel(JNetwork jnetwork, String name) {
        this.jnetwork = jnetwork;
        this.name = name;
    }
    
    
    public NetworkModel(String name) {
        this.jnetwork = null;
        this.name = name;
    }
    
    
    public JNetwork getJNetwork() {
        return jnetwork;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getInfo() {
        return info;
    }
    
    
    public void setInfo(String info) {
        this.info = info;
    }
    
    
    public void addRubette(RubetteModel rubette) {
        rubettes.add(rubette);
    }
    
    
    public void removeRubette(RubetteModel rubette) {
        rubettes.remove(rubette);
    }
    
    
    public ArrayList<RubetteModel> getRubettes() {
        return rubettes;
    }
    
    
    public void computeDependencyTree() {
        computeRootsAndCoroots();
        computeDependents();
        computeDependencies();
    }
    
    
    public ArrayList<RubetteModel> getDependents() {
        return dependents;
    }
    
    
    private void computeRootsAndCoroots() {
        roots.clear();
        coroots.clear();
        for (RubetteModel rubette : rubettes) {
            if (rubette.getInLinkCount() == 0) {
                roots.add(rubette);
            }
            else if (rubette.getOutLinkCount() == 0) {
                coroots.add(rubette);
            }
        }        
    }
    
    
    private void computeDependents() {
        dependents.clear();
        for (RubetteModel model : roots) {
            computeDependents(model, dependents);
            dependents.add(model);
        }
        // reverse list
        Collections.reverse(dependents);
    }
    
    
    private void computeDependents(RubetteModel model, ArrayList<RubetteModel> list) {
        for (RubetteModel rmodel : model.getFirstDependents()) {
            computeDependents(rmodel, list);
            if (!(list.contains(rmodel))) {
                list.add(rmodel);
            }
        }
    }
    
    
    private void computeDependencies() {
        dependencies.clear();
        for (RubetteModel model : coroots) {
            computeDependencies(model, dependencies);
            dependencies.add(model);
        }
        // reverse list
        Collections.reverse(dependencies);
    }
    
    
    private void computeDependencies(RubetteModel model, ArrayList<RubetteModel> list) {
        for (RubetteModel rmodel : model.getFirstDependencies()) {
            computeDependencies(rmodel, list);
            if (!(list.contains(rmodel))) {
                list.add(rmodel);
            }
        }
    }
    
    
    public void addNote(NoteModel note) {
        notes.add(note);
    }
    
    
    public void removeNote(NoteModel note) {
        notes.remove(note);
    }
    
    
    public ArrayList<NoteModel> getNotes() {
        return notes;
    }

    public NetworkModel newInstance() {
        ArrayList<RubetteModel> newRubettes = new ArrayList<>(rubettes.size());
        for (int i = 0; i < rubettes.size(); i++) {
            RubetteModel rmodel = rubettes.get(i);
            rmodel.setSerial(i);
            newRubettes.add(rmodel.newInstance());
        }
        for (RubetteModel rmodel : rubettes) {
            for (int j = 0; j < rmodel.getInLinkCount(); j++) {
                Link link = rmodel.getInLink(j);
                int src = link.getSrcModel().getSerial();
                RubetteModel srcModel = newRubettes.get(src);
                int srcPos = link.getSrcPos();
                int dest = link.getDestModel().getSerial();
                RubetteModel destModel = newRubettes.get(dest);
                int destPos = link.getDestPos();
                Link newLink = new Link(srcModel, srcPos, destModel, destPos);
                newLink.setType(link.getType());
                srcModel.addOutLink(newLink);
                destModel.setInLink(newLink);
            }
        }
        
        ArrayList<NoteModel> newNotes = new ArrayList<>(notes.size());
        for (NoteModel nmodel : notes) {
            newNotes.add(nmodel.newInstance());
        }
        
        NetworkModel newModel = new NetworkModel(getName());
        newModel.rubettes = newRubettes;
        newModel.notes = newNotes;
        newModel.computeDependencyTree();
        return newModel;
    }
    
    
    public JNetwork createJNetwork(JComposer jcomposer) {
        JNetwork newNetwork = new JNetwork(jcomposer);
        newNetwork.setModel(this);
        jnetwork = newNetwork;
        return newNetwork;
    }
    
    
    public JNetwork createJMacroRubetteView(JComposer jComposer, JNetwork jNetwork) {
        JNetwork newNetwork = new JMacroRubetteView(jComposer, jNetwork);
        newNetwork.setModel(this);
        return newNetwork;
    }

    
    public String toString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("NetworkModel["); 
        buf.append(getName());
        for (RubetteModel rubette : rubettes) {
            buf.append(","); 
            buf.append(rubette);
        }
        buf.append("]"); 
        return buf.toString();
    }       
    
    
    private JNetwork  jnetwork;
    private String    name;
    private String    info = ""; 
    private ArrayList<RubetteModel> rubettes = new ArrayList<>();
    private ArrayList<RubetteModel> roots = new ArrayList<>();
    private ArrayList<RubetteModel> coroots = new ArrayList<>();
    private ArrayList<RubetteModel> dependents = new ArrayList<>(100);
    private ArrayList<RubetteModel> dependencies = new ArrayList<>(100);
    private ArrayList<NoteModel> notes = new ArrayList<>();
}
