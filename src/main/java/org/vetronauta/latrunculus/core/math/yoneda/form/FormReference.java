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

import org.rubato.base.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;

import java.io.PrintStream;
import java.util.LinkedList;

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

    protected void display(PrintStream out, LinkedList<Form> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \""+getNameString()+"\"");
        out.println("; Type: "+getTypeString());
        indent(out, indent+4);
        out.println("Reference");
    }

    public LinkedList<Form> getDependencies(LinkedList<Form> list) {
        throw new UnsupportedOperationException();
    }
    
    public boolean resolveReferences(RubatoDictionary dict) {
        return true;
    }
    
    public String toString() {
        return "["+getNameString()+":.reference]";
    }
    
    private FormDenotatorTypeEnum type;
}
