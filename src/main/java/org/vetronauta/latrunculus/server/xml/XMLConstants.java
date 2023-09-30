/*
 * Copyright (C) 2005 Gérard Milmeister
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.vetronauta.latrunculus.server.xml;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Commonly used constants for XML tags and attributes.
 * 
 * @author Gérard Milmeister
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class XMLConstants {

    public static final String ROOT_ELEMENT = "Rubato";
    public static final String DEFINE_MODULE = "DefineModule";
    public static final String DEFINE_MODULE_ELEMENT = "DefineElement";
    public static final String DEFINE_MODULE_MORPHISM = "DefineModuleMorphism";
    public static final String DENOTATOR = "Denotator";
    public static final String FORM = "Form";
    public static final String MODULE = "Module";
    public static final String MODULE_ELEMENT = "ModuleElement";
    public static final String MODULE_MORPHISM = "ModuleMorphism";
    public static final String MORPHISM = "Morphism";
    public static final String MORPHISM_MAP = "MorphismMap";
    public static final String DIAGRAM = "Diagram";
    public static final String RUBETTE = "Rubette";
    public static final String NETWORK = "Network";
    public static final String LINK = "Link";
    public static final String NOTE = "Note";
    public static final String SCHEME = "Scheme";

    public static final String NAME_ATTR = "name";
    public static final String CLASS_ATTR = "class";
    public static final String REF_ATTR = "ref";
    public static final String FORM_ATTR = "form";
    public static final String TYPE_ATTR = "type";
    public static final String VALUE_ATTR = "value";
    public static final String VALUES_ATTR = "values";
    public static final String DIMENSION_ATTR = "dimension";
    public static final String MODULUS_ATTR = "modulus";
    public static final String ROWS_ATTR = "rows";
    public static final String COLUMNS_ATTR = "columns";
    public static final String SERIAL_ATTR = "serial";
    public static final String X_ATTR = "x";
    public static final String Y_ATTR = "y";
    public static final String SRC_ATTR = "src";
    public static final String SRCPOS_ATTR = "srcPos";
    public static final String DEST_ATTR = "dest";
    public static final String DESTPOS_ATTR = "destPos";

    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";

    public static final String DQUOTE_SYMBOL = "\"";
    public static final String QUOTE_SYMBOL = "'";
    public static final String EQUALS_SYMBOL = "=";
    public static final String TAG_OPEN = "<";
    public static final String TAG_CLOSE = ">";
    public static final String SPACE = " ";

    public static final String BASE64 = "Base64";

    public static final String WORD        = "Word";
    public static final String FACTOR_ATTR = "factor";
    public static final String KIND_ATTR   = "kind";
    public static final String INDETERMINATE_ATTR = "indeterminate";
    public static final String INDEX_ATTR = "index";

}
