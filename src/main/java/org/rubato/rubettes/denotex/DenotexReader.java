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

import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.server.display.DenotatorDisplay;
import org.vetronauta.latrunculus.server.display.FormDisplay;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class DenotexReader {

    public DenotexReader(InputStream in) {
        this(in, Repository.systemRepository());
    }

    public DenotexReader(InputStream in, Repository r) {
        rep = r;
        symtab = new Symboltable();
        parser = new DenotexParser(in, symtab, rep);
    }


    public DenotexReader(Reader rdr) {
        this(rdr, Repository.systemRepository());
    }

    
    public DenotexReader(Reader rdr, Repository r) {
        rep = r;
        symtab = new Symboltable();
        parser = new DenotexParser(rdr, symtab, rep);
    }
    

    public void read() {
        if (!read) {            
            read = true;
            try {
                parser.parse();
                Linker.link(symtab);
            }
            catch (ParseException e) {
                error = true;
                errorMsg = e.getMessage();
            }
        }
    }
    
    
    public Form readForm() {        
        try {
            parser.parseForm();
            Linker.link(symtab);
            if (!symtab.getForms().values().isEmpty()) {
                return symtab.getForms().values().iterator().next();
            }
        }
        catch (ParseException e) {
            error = true;
            errorMsg = e.getMessage();
        }
        return null;
    }
    
    
    public static Form readForm(String s) {
        StringReader sreader = new StringReader(s);
        DenotexReader reader = new DenotexReader(sreader);
        return reader.readForm();
    }
    
    
    public Denotator readDenotator() {        
        try {
            parser.parseDenotator();
            Linker.link(symtab);
            if (!symtab.getNamedDenotators().values().isEmpty()) {
                return symtab.getNamedDenotators().values().iterator().next();
            }
            else if (!symtab.getAnonymousDenotators().isEmpty()) {
                return symtab.getAnonymousDenotators().iterator().next();
            }
        }
        catch (ParseException e) {
            error = true;
            errorMsg = e.getMessage();
        }
        return null;
    }
    
    
    public static Denotator readDenotator(String s) {
        StringReader sreader = new StringReader(s);
        DenotexReader reader = new DenotexReader(sreader);
        return reader.readDenotator();
    }

    
    public List<Form> getForms() {
        read();
        if (error) {
            return null;
        }
        LinkedList<Form> formList = new LinkedList<>();
        formList.addAll(symtab.getForms().values());
        return formList;
    }    


    public List<Denotator> getDenotators() {
        read();
        if (error) {
            return null;
        }
        LinkedList<Denotator> denoList = new LinkedList<>();
        denoList.addAll(symtab.getNamedDenotators().values());
        denoList.addAll(symtab.getAnonymousDenotators());
        return denoList;
    }    


    public boolean hasError() {
        return error;
    }
    
    
    public String getErrorMsg() {
        return errorMsg;
    }
    
    
    private Repository rep;
    private Symboltable symtab;
    private DenotexParser parser;
    private boolean read = false;
    private boolean error = false;
    private String errorMsg;

    public static void main(String[] args) { //TODO remove all the debugging mains around, move them to test?
        String formstr = "A1:.limit[Integer,Integer]";
        Form f = readForm(formstr);
        Repository.systemRepository().register(f);
        FormDisplay.display(f);
        String denostr = "D1:@A1[0,0]";
        Denotator d = readDenotator(denostr);
        if (d == null)
            System.out.println("Error");
        else
            DenotatorDisplay.display(d);
    }
}
