/*
 * Copyright (C) 2002, 2005 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.math.yoneda;

import java.util.*;

/**
 * The unique representation of names.
 * 
 * @author Gérard Milmeister
 */
public final class NameEntry implements Comparable<NameEntry> {

    private static final HashMap<NameEntry,NameEntry> NAME_TABLE = new HashMap<>();
    private static final String SLASH = "/";

    private final ArrayList<String> names;

    public NameEntry() {
        this.names = new ArrayList<>();
    }

    public NameEntry(String name) {
        this.names = new ArrayList<>();
        this.names.addAll(Arrays.asList(name.split(SLASH)));
    }

	public NameEntry(String name1, String name2) {
        this.names = new ArrayList<>();
        this.names.addAll(Arrays.asList(name1.split(SLASH)));
        this.names.addAll(Arrays.asList(name2.split(SLASH)));
    }

    public NameEntry(List<String> nameList) {
        names = new ArrayList<>();
        names.addAll(nameList);
    }

    public void add(String name) {
        names.add(name);
    }

    public String getString() {
        StringJoiner joiner = new StringJoiner(SLASH);
        names.forEach(joiner::add);
        return joiner.toString();
    }

    public String getString(int i) {
        return names.get(i);
    }

    public int getLength() {
        return names.size();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NameEntry)) {
            return false;
        }
        return names.equals(((NameEntry) object).names);
    }

    @Override
    public int compareTo(NameEntry entry) {
        ArrayList<String> names1 = entry.names;
        int size = names.size();
        int size1 = names1.size();
        
        for (int i = 0; i < Math.min(size, size1); i++) {
            int c = names.get(i).compareTo(names1.get(i));
            if (c != 0) {
                return c;
            }
        }
        return size - size1;
    }

    @Override
    public String toString() {
        return getString();        
    }

    @Override
    public int hashCode() {
        return names.hashCode();
    }
    

    //
    // Static lookup methods.
    //

    public static NameEntry lookup(NameEntry e) {
        NameEntry ne = NAME_TABLE.get(e);
        if (ne == null) {
            NAME_TABLE.put(e, e);
            return e;
        }
        return ne;
    }

    public static NameEntry lookup(String name) {
        NameEntry e = new NameEntry(name);
        NameEntry ne = NAME_TABLE.get(e);
        if (ne == null) {
            NAME_TABLE.put(e, e);
            return e;
        }
        return ne;
    }

    public static NameEntry lookup(String name1, String name2) {
        NameEntry e = new NameEntry(name1, name2);
        NameEntry ne = NAME_TABLE.get(e);
        if (ne == null) {
            NAME_TABLE.put(e, e);
            return e;
        }
        return ne;
    }

    public static NameEntry lookup(List<String> names) {
        NameEntry e = new NameEntry(names);
        NameEntry ne = NAME_TABLE.get(e);
        if (ne == null) {
            NAME_TABLE.put(e, e);
            return e;
        }
        return ne;
    }

    public static NameEntry concat(NameEntry a, NameEntry b) {
        LinkedList<String> names = new LinkedList<>(a.names);
        names.addAll(b.names);
        return lookup(names);
    }

}