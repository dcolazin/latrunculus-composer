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

package org.vetronauta.latrunculus.core.scheme.primitive;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.scheme.expression.Env;
import org.vetronauta.latrunculus.core.scheme.Evaluator;
import org.vetronauta.latrunculus.core.scheme.expression.SBoolean;
import org.vetronauta.latrunculus.core.scheme.expression.SExpr;
import org.vetronauta.latrunculus.core.scheme.expression.SNumber;
import org.vetronauta.latrunculus.core.scheme.expression.SString;
import org.vetronauta.latrunculus.core.scheme.expression.SType;
import org.vetronauta.latrunculus.core.scheme.expression.Symbol;

import static org.vetronauta.latrunculus.core.scheme.expression.SExpr.*;

/**
 * Standard primitive procedures dealing with strings.
 * 
 * @author Gérard Milmeister
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringPrimitives {

    public static void fillEnvironment(Env env) {
        env.addPrimitive(symbol_p);
        env.addPrimitive(string_p);
        env.addPrimitive(char_p);
        env.addPrimitive(symbol_to_string);
        env.addPrimitive(string_to_symbol);
        env.addPrimitive(number_to_string);
        env.addPrimitive(string_to_number);
    }
    
    
    private static final Primitive symbol_p = new Primitive() {
        public String getName() { return "symbol?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                return SBoolean.make(car(args).type() == SType.SYMBOL);
            }
            else {
                eval.addError("symbol?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }        
    };

    private static final Primitive string_p = new Primitive() {
        public String getName() { return "string?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                return SBoolean.make(car(args).type() == SType.STRING);
            }
            else {
                eval.addError("string?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }        
    };

    private static final Primitive char_p = new Primitive() {
        public String getName() { return "char?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                return SBoolean.make(car(args).type() == SType.CHAR);
            }
            else {
                eval.addError("char?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }        
    };

    private static final Primitive symbol_to_string = new Primitive() {
        public String getName() { return "symbol->string"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr s = car(args);
                if (s.type() == SType.SYMBOL) {
                    return new SString(((Symbol)s).getName());
                }
                else {
                    eval.addError("symbol->string: expected argument of type symbol, but got %1", s);
                    return null;
                }
            }
            else {
                eval.addError("symbol->string: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }        
    };

    private static final Primitive string_to_symbol = new Primitive() {
        public String getName() { return "string->symbol"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr s = args.nth(0);
                if (s.type() == SType.STRING) {
                    return Symbol.make(((SString)s).getString());
                }
                else {
                    eval.addError("string->symbol: expected argument of type string, but got %1", s);
                    return null;
                }
            }
            else {
                eval.addError("string->symbol: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }        
    };
    
    private static final Primitive number_to_string = new Primitive() {
        public String getName() { return "number->string"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr n = car(args);
                if (n.isNumber()) {
                    return new SString(((SNumber)n).toString());
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", n);
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }        
    };

    private static final Primitive string_to_number = new Primitive() {
        public String getName() { return "string->number"; }
        public SExpr call(SExpr args, Evaluator eval) {
            int l = args.getLength();
            if (l == 1) {
                SExpr s = car(args);
                if (s.type() == SType.STRING) {
                    // TODO: not yet implemented
                    return null;
                }
                else {
                    eval.addError(getName()+": expected argument of type string, but got %1", s);
                    return null;
                }
            }
            else if (l == 2) {
                SExpr s = car(args);
                SExpr r = car(cdr(args));
                if (s.type() == SType.STRING) {
                    if (r.type() == SType.INTEGER) {
                        // TODO: not yet implemented
                        return null;
                    }
                    else {
                        eval.addError(getName()+": expected 2nd argument of type integer, but got %1", r);
                        return null;
                    }
                }
                else {
                    eval.addError(getName()+": expected 1st argument of type string, but got %1", s);
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }        
    };
}
