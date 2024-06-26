/*
 * Copyright (C) 2006 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.scheme.expression;

import java.io.PrintStream;


/**
 * The class wrapping an output port as a Scheme value.
 * 
 * @author Gérard Milmeister
 */
public final class SOutPort extends SExpr {

    /**
     * Creates an output port from a print stream.
     */
    public SOutPort(PrintStream port) {
        this.port = port;
    }


    @Override
    public SType type() {
        return SType.OUTPUT;
    }

    public boolean eq_p(SExpr sexpr) {
        return sexpr == this;
    }
    
    
    public boolean eqv_p(SExpr sexpr) {
        return sexpr == this;
    }
    
    
    public boolean equal_p(SExpr sexpr) {
        return sexpr == this;
    }
    
    
    public boolean equals(Object obj) {
        return obj == this;
    }

    /**
     * Closes the port.
     */
    public void close() {
        port.close();
    }
    
    
    /**
     * Returns the print stream of this output port.
     */
    public PrintStream getPort() {
        return port;
    }
    
    
    private PrintStream port;
}
