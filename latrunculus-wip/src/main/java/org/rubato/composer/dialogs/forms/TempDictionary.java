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

package org.rubato.composer.dialogs.forms;

import java.util.HashMap;
import java.util.List;

import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.core.repository.Dictionary;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;

public class TempDictionary implements Dictionary {

    public TempDictionary(Dictionary dict) {
        this.dict = dict;     
        this.forms = new HashMap<String,Form>();
    }
    
    
    public List<Form> getForms() {
        List<Form> res = dict.getForms();
        res.addAll(forms.values());
        return res;
    }

    public Form getForm(String name) {
        Form res = forms.get(name);
        if (res == null) {
            res = dict.getForm(name);
        }
        return res;
    }

    
    public void removeForm(Form form) {
        forms.remove(form.getNameString());
    }
    
    
    public void insertForm(String name, Form form) {
        forms.put(name, form);
    }
    
    
    public void resolveAll() {
        for (Form form : forms.values()) {
            form.resolveReferences(this);
        }
    }
    
    
    public void registerAll(Repository rep) {
        for (Form form : forms.values()) {
            form.resolveReferences(this);
            form._register(rep, false);
        }
    }
    
    
    public List<Denotator> getDenotators() {
        return dict.getDenotators();
    }

    
    public Denotator getDenotator(String name) {
        return dict.getDenotator(name);
    }
    

    public Module getModule(String name) {
        return dict.getModule(name);
    }

    
    public ModuleElement getModuleElement(String name) {
        return dict.getModuleElement(name);
    }

    
    public ModuleMorphism getModuleMorphism(String name) {
        return dict.getModuleMorphism(name);
    }
    
    
    private Dictionary dict;
    private HashMap<String,Form> forms;
}
