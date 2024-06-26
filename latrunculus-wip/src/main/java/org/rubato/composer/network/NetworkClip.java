/*
 * Copyright (C) 2007 Gérard Milmeister
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

import java.util.ArrayList;
import java.util.IdentityHashMap;

import org.rubato.composer.rubette.JRubette;
import org.vetronauta.latrunculus.plugin.base.Link;
import org.vetronauta.latrunculus.plugin.base.PluginNode;

public class NetworkClip {

    private final ArrayList<PluginNode> rubettes = new ArrayList<>();

    public NetworkClip(ArrayList<JRubette> selection) {
        makeCopy(selection);
    }

    public void paste(JNetwork jnetwork) {
        IdentityHashMap<PluginNode, JRubette> map = new IdentityHashMap<PluginNode, JRubette>();
        jnetwork.clearSelection();
        for (PluginNode model : rubettes) {
            PluginNode modelCopy = model.duplicate();
            JRubette jrubette = new JRubette(modelCopy);
            if (jnetwork.canAdd(jrubette)) {
                jnetwork.addRubette(jrubette, modelCopy.getLocation());
                jnetwork.toggleSelection(jrubette);
                map.put(model, jrubette);
            }
        }
        for (PluginNode model : map.keySet()) {
            JRubette srcRubette = map.get(model);
            for (Link link : model.getOutLinks()) {
                JRubette destRubette = map.get(link.getDestModel());
                if (destRubette != null) {
                    int srcPos = link.getSrcPos();
                    int destPos = link.getDestPos();
                    jnetwork.makeLink(srcRubette, srcPos, destRubette, destPos);
                }
            }
        }
    }
    
    
    public String toString() {
        StringBuilder buf = new StringBuilder(256);
        for (PluginNode model : rubettes) {
            buf.append(model);
            buf.append("\n");
            for (Link link : model.getOutLinks()) {
                buf.append(link);
                buf.append("\n");
            }
        }
        return buf.toString();
    }

    
    private void makeCopy(ArrayList<JRubette> selection) {
        IdentityHashMap<PluginNode, PluginNode>  map = new IdentityHashMap<PluginNode, PluginNode>();
        for (JRubette jrubette : selection) {
            PluginNode originalModel = jrubette.getModel();
            PluginNode modelCopy = originalModel.duplicate();
            modelCopy.getLocation().translate(5, 5);
            rubettes.add(modelCopy);
            map.put(originalModel, modelCopy);
        }
        for (PluginNode originalModel : map.keySet()) {
            for (Link link : originalModel.getOutLinks()) {
                PluginNode destModel = link.getDestModel();
                int srcPos = link.getSrcPos();
                int destPos = link.getDestPos();
                destModel = map.get(destModel);
                if (destModel != null) {
                    PluginNode srcModel = map.get(originalModel);
                    srcModel.addOutLink(new Link(srcModel, srcPos, destModel, destPos));
                }
            }
        }
    }
    
}
