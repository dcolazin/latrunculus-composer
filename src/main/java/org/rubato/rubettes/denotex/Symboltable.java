/*
 * Copyright (C) 2002 Gérard Milmeister
 * Copyright (C) 2002 Stefan Müller
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

package org.rubato.rubettes.denotex;


import lombok.Getter;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.NameEntry;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
public final class Symboltable {

    private final Map<NameEntry,Form> forms;
    private final Map<NameEntry,Denotator> namedDenotators;
    private final List<Denotator> anonymousDenotators;
    private final Map<String,Module> modules;

    public Symboltable(Map<NameEntry,Form> forms, Map<NameEntry,Denotator> namedDenotators, List<Denotator> anonymousDenotators,
               Map<String,Module> modules) {
        this.forms = forms;
        this.namedDenotators = namedDenotators;
        this.anonymousDenotators = anonymousDenotators;
        this.modules = modules;
    }

    public Symboltable() {
        this.forms = new Hashtable<>();
        this.namedDenotators = new Hashtable<>();
        this.anonymousDenotators = new LinkedList<>();
        this.modules = new Hashtable<>();
    }

}