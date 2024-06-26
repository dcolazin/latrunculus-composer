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

package org.vetronauta.latrunculus.core.math.yoneda.form;

import org.vetronauta.latrunculus.core.repository.Dictionary;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;

import java.util.List;

/**
 * A placeholder for forms that are not yet known.
 * Mainly used during parsing.
 * 
 * @author Gérard Milmeister
 */
public final class FormReference extends Form {

    public FormReference(String name) {
        this(name, FormDenotatorTypeEnum.SIMPLE);
    }
    
    public FormReference(String name, FormDenotatorTypeEnum type) {
        super(NameDenotator.make(name), null);
        this.type = type;
    }

    public FormDenotatorTypeEnum getType() {
        return type;
    }

    protected double getDimension(int maxDepth, int depth) {
        throw new UnsupportedOperationException();
    }

    public Denotator createDefaultDenotator() {
        throw new UnsupportedOperationException();
    }
    
    public Denotator createDefaultDenotator(Module address) {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Object object) {
        throw new UnsupportedOperationException();
    }

    public int getFormCount() {
        throw new UnsupportedOperationException();
    }

    public Form getForm(int i) {
        throw new UnsupportedOperationException();
    }

    public List<Form> getDependencies(List<Form> list) {
        throw new UnsupportedOperationException();
    }
    
    public boolean resolveReferences(Dictionary dict) {
        return true;
    }
    
    public String toString() {
        return "["+getNameString()+":.reference]";
    }
    
    private FormDenotatorTypeEnum type;
}
