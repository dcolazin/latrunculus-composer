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
import org.vetronauta.latrunculus.core.scheme.expression.SInteger;
import org.vetronauta.latrunculus.core.scheme.expression.SType;

import static org.vetronauta.latrunculus.core.scheme.expression.SExpr.car;
import static org.vetronauta.latrunculus.core.scheme.expression.SVoid.SCHEME_VOID;

/**
 * Standard primitive procedures for control structures and environments.
 * 
 * @author Gérard Milmeister
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControlPrimitives {

    public static void fillEnvironment(Env env) {
        env.addPrimitive(procedure_p);
        env.addPrimitive(scheme_report_environment);
        env.addPrimitive(null_environment);
        env.addPrimitive(interaction_environment);
        env.addPrimitive(apply);
        env.addPrimitive(map);
    }

    private static final Primitive procedure_p = new Primitive() {
        public String getName() { return "procedure?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr car = car(args);
                return SBoolean.make(car.type() == SType.CLOJURE || car.type() == SType.PRIMITIVE);
            }
            else {
                eval.addError("procedure?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive scheme_report_environment = new Primitive() {
        public String getName() { return "scheme-report-environment"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr car = car(args);
                if (car.type() == SType.INTEGER && ((SInteger)car).getInt() == 5) {
                    return Env.makeStandardEnvironment();
                }
                else {
                    eval.addError("scheme-report-environment: expected argument is 5, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError("scheme-report-environment: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };
    
    private static final Primitive null_environment = new Primitive() {
        public String getName() { return "null-environment"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr car = car(args);
                if (car.type() == SType.INTEGER && ((SInteger)car).getInt() == 5) {
                    return new Env();
                }
                else {
                    eval.addError("null-environment: expected argument is 5, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError("null-environment: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };
    
    private static final Primitive interaction_environment = new Primitive() {
        public String getName() { return "interaction-environment"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 0) {
                return Env.makeGlobalEnvironment();
            }
            else {
                eval.addError("interaction-environment: expected number of arguments is 0, but got %1", args.getLength());
                return null;
            }
        }
    };
    
    public static Primitive map = new Primitive() {
        public String getName() { return "map"; }
        public SExpr call(SExpr args, Evaluator eval) {
            return SCHEME_VOID;
        }
    };

    public static Primitive apply = new Primitive() {
        public String getName() { return "apply"; }
        public SExpr call(SExpr args, Evaluator eval) {
            return SCHEME_VOID;
        }
    };
}
