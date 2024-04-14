/*
 * Copyright (C) 2002, 2005 Gérard Milmeister
 * Copyright (C) 2002 Stefan Müller
 * Copyright (C) 2002 Stefan Göller
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

package org.vetronauta.latrunculus.core.math.yoneda.form;

import org.vetronauta.latrunculus.core.exception.RubatoException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ColimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.Diagram;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.ProperIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Colimit form class.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class ColimitForm extends Form {

    /**
     * Generic form constructor.
     */
    public ColimitForm(NameDenotator name, YonedaMorphism identifier) {
        super(name, identifier);
    }


    /**
     * Builds a colimit identity form using a list of forms.
     */
    public ColimitForm(NameDenotator name, List<Form> forms) {
        super(name, new ProperIdentityMorphism(new FormDiagram(forms), FormDenotatorTypeEnum.COLIMIT));
    }


    /**
     * Builds a colimit identity form using a list of forms.
     */
    public ColimitForm(NameDenotator name, List<Form> forms, Map<String, Integer> labels) {
        super(name, new ProperIdentityMorphism(new FormDiagram(forms), FormDenotatorTypeEnum.COLIMIT));
        setLabels(labels);
    }


    /**
     * Builds a colimit identity form using a diagram.
     */
    public ColimitForm(NameDenotator name, Diagram diagram) {
        super(name, new ProperIdentityMorphism(diagram, FormDenotatorTypeEnum.COLIMIT));
    }

    
    /**
     * Returns the type of the form.
     */
    public FormDenotatorTypeEnum getType() {
        return FormDenotatorTypeEnum.COLIMIT;
    }


    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ColimitForm) {
            return equals((ColimitForm)object);
        }
        else {
            return false;
        }
    }


    public boolean equals(ColimitForm f) {
        if (registered && f.registered) {
            return getName().equals(f.getName());
        }
        if (this == f) {
            return true;
        }
        else if (!getName().equals(f.getName())) {
            return false;
        }
        Map<Object,Object> map = new HashMap<>();
        map.put(this, f);
        return identifier.fullEquals(f.identifier, map);
    }


    /**
     * Returns the number of coordinate forms.
     */
    public int getFormCount() {
        return ((FormDiagram)getIdentifier().getCodomainDiagram()).getFormCount();
    }
    

    /**
     * Returns a coordinate form.
     * @param i the coordinate position
     * @return the form at coordinate position i
     */
    public Form getForm(int i) {
        return ((FormDiagram)getIdentifier().getCodomainDiagram()).getForm(i);
    }
    

    /**
     * Returns a coordinate form.
     * @param label the name of the coordinate form
     * @return the form at the coordinate with the given label  
     */
    public Form getForm(String label)
            throws RubatoException {
        int i = labelToIndex(label);
        if (i < 0) {
            throw new RubatoException("ColimitForm.getForm: Label %1 does not exist", label);
        }
        return getForm(i);
    }
    

    /**
     * Sets the labels for the factors of the form.
     * Labels are assigned in the order they occur in the list <code>labels</code>.
     */
    public void setLabels(List<String> labels) {
        if (labels == null) {
            labelMap = null;
            reverseLabelMap = null;
        }
        else {
            labelMap = new HashMap<>();
            reverseLabelMap = new String[labels.size()];
            int i = 0;
            for (String label : labels.subList(0, getFormCount())) {
                labelMap.put(label, i);
                reverseLabelMap[i] = label;
                i++;
            }
        }
    }
    
    private void setLabels(Map<String,Integer> labels) {
        if (labels == null) {
            labelMap = null;
            reverseLabelMap = null;
        }
        else {
            labelMap = labels;
            reverseLabelMap = new String[labels.size()];
            for (String label : labelMap.keySet()) {
                reverseLabelMap[labelMap.get(label)] = label;
            }
        }
    }
    
    
    /**
     * Returns the index corresponding to the given label.
     * @return the index, or -1 if the label does not exist
     */
    public int labelToIndex(String label) {
        if (labelMap == null) {
            int i;
            try {
                i = Integer.parseInt(label);
                if (i >= getFormCount()) {
                    return -1;
                }
                else {
                    return i;
                }
            }
            catch (NumberFormatException e) {
                return -1;
            }
        }
        Integer i = labelMap.get(label);
        if (i == null) {
            return -1;
        }
        else {
            return i;
        }
    }
    
    
    /**
     * Returns the label corresponding to the given index <code>i</code>.
     */
    public String indexToLabel(int i) {
        if (reverseLabelMap == null) {
            return Integer.toString(i);
        }
        else if (i >= reverseLabelMap.length) {
            if (i < getFormCount()) {
                return Integer.toString(i);
            }
            else {
                return null;
            }
        }
        else {
            return reverseLabelMap[i];
        }
    }

    
    /**
     * Returns true iff this form has labels.
     */
    public boolean hasLabels() {
        return labelMap != null;
    }

    public Map<String, Integer> getLabelMap() {
        return labelMap;
    }

    public YonedaMorphism getIdentifier() {
        return identifier;
    }

    public List<Form> getDependencies(List<Form> list) {
        if (!list.contains(this)) {
            list.add(this);
            return identifier.getFormDependencies(list);
        }
        return list;
    }
        
    /**
     * Returns a default denotator of this colimit form.
     */
    public Denotator createDefaultDenotator() {
        Denotator res = null;
        try {
            res = new ColimitDenotator(null, this, 0, getForm(0).createDefaultDenotator());
        }
        catch (RubatoException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    /**
     * Returns a default denotator of this colimit form with the given address.
     */
    public Denotator createDefaultDenotator(Module address) {
        Denotator res = null;
        try {
            res = new ColimitDenotator(null, address, this, 0, getForm(0).createDefaultDenotator());
        }
        catch (RubatoException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    public String toString() {
        StringBuilder buf = new StringBuilder(50);
        buf.append("[");
        buf.append(getNameString());
        buf.append(":.colimit(");
        buf.append(getForm(0).getNameString());
        for (int i = 1; i < getFormCount(); i++) {
            buf.append(",");
            buf.append(getForm(i).getNameString());
        }
        buf.append(")]");
        return buf.toString();
    }
    
    protected double getDimension(int maxDepth, int depth) {
        if (depth > maxDepth) {
            return 1.0;
        }
        
        FormDiagram d = (FormDiagram)identifier.getCodomainDiagram();
        double dimension = d.getVertexCount();
        double oneByN = 1.0 / dimension;
        for (int i = 0; i < dimension; i++) {
            dimension += oneByN * (1.0 - 1.0 / d.getForm(0).getDimension(maxDepth, depth + 1));
        }
        
        return dimension;
    }


    private Map<String,Integer> labelMap = null;
    private String[] reverseLabelMap = null;
}
