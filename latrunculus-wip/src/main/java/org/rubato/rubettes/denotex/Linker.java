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

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.map.IndexMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.ListMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.NameEntry;

public final class Linker {

    public static void link(Form form, Symboltable symtab, Repository rep) {
        List<?> list = (List<?>)form.getConnector();
        if (list == null) return;

        // reset connector first (because of circular forms)
        form.setConnector(null);

        if (form.getType() != FormDenotatorTypeEnum.SIMPLE) {
            for (int i = 0; i < list.size(); i++) {
                NameEntry name = (NameEntry)list.get(i);
                Form ff = symtab.getForms().get(name);
                if (ff == null) {
                    ff = rep.getForm(name);
                    if (ff == null)
                        throw new IllegalStateException("Undefined form \""+name+"\"");
                }
                link(ff, symtab, rep);
                ((FormDiagram)form.getIdentifier().getCodomainDiagram()).setForm(i, ff);
            }
        }
    }

    public static void link(Denotator deno, Symboltable t, Repository rep) {
        Form form = deno.getForm();

        // replace all morphism maps that contain forward declared denotators
        switch(form.getType()) {
            case COLIMIT: {
                IndexMorphismMap map = (IndexMorphismMap)deno.getCoordinate().getMap();
                Denotator d = map.getFactor();
                NameEntry name = (NameEntry)d.getConnector();
                if (name != null) {
                    Denotator dd = t.getNamedDenotators().get(name);
                    if (dd == null) {
                        dd = rep.getDenotator(name);
                        if (dd == null)                                                
                            throw new IllegalStateException("Undefined denotator \""+ name+"\"");
                    }
                    map.setFactor(0, dd);
                }
                break;
            }
            case LIMIT:
            case POWER:
            case LIST: {
                ListMorphismMap map = (ListMorphismMap)deno.getCoordinate().getMap();
                for (int i = 0; i < map.getFactorCount(); i++) {
                    Denotator d = map.getFactor(i);
                    NameEntry name = (NameEntry)d.getConnector();
                    if (name != null) {
                        Denotator dd = t.getNamedDenotators().get(name);
                        if (dd == null) {
                            dd = rep.getDenotator(name);
                            if (dd == null)
                                throw new IllegalStateException("Undefined denotator \""+name+"\""); 
                        }
                        map.setFactor(i, dd);
                    }
                }
                break;
            }
        }
    }
    
    public static void link(Symboltable symtab, Repository rep) {
        for (Iterator<Entry<NameEntry,Form>> i = symtab.getForms().entrySet().iterator(); i.hasNext(); ) {
            Entry<NameEntry,Form> e = i.next();
            link(e.getValue(), symtab, rep);
        }
        for (Iterator<Entry<NameEntry,Denotator>> i = symtab.getNamedDenotators().entrySet().iterator(); i.hasNext(); ) {
            Entry<NameEntry,Denotator> e = i.next();
            link(e.getValue(), symtab, rep);
        }
    }

    public static void link(Symboltable symtab) {
        link(symtab, Repository.systemRepository());
    }

    // don't allow construction of linker objects
    private Linker() {}
}
