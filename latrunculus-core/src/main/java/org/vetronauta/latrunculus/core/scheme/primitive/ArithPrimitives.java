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

import static org.vetronauta.latrunculus.core.scheme.expression.SExpr.*;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.arith.Trigonometry;
import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.scheme.expression.Env;
import org.vetronauta.latrunculus.core.scheme.Evaluator;
import org.vetronauta.latrunculus.core.scheme.expression.SBoolean;
import org.vetronauta.latrunculus.core.scheme.expression.SComplex;
import org.vetronauta.latrunculus.core.scheme.expression.SExpr;
import org.vetronauta.latrunculus.core.scheme.expression.SInteger;
import org.vetronauta.latrunculus.core.scheme.expression.SNumber;
import org.vetronauta.latrunculus.core.scheme.expression.SRational;
import org.vetronauta.latrunculus.core.scheme.expression.SReal;
import org.vetronauta.latrunculus.core.scheme.expression.SType;


/**
 * Standard primitive procedures dealing with numbers.
 * 
 * @author Gérard Milmeister
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArithPrimitives {

    public static void fillEnvironment(Env env) {
        env.addPrimitive(plus);
        env.addPrimitive(minus);
        env.addPrimitive(times);
        env.addPrimitive(divide);
        env.addPrimitive(equality);
        env.addPrimitive(number_p);
        env.addPrimitive(complex_p);
        env.addPrimitive(real_p);
        env.addPrimitive(rational_p);
        env.addPrimitive(integer_p);
        env.addPrimitive(even_p);
        env.addPrimitive(odd_p);
        env.addPrimitive(positive_p);
        env.addPrimitive(negative_p);
        env.addPrimitive(zero_p);
        env.addPrimitive(exact_p);
        env.addPrimitive(inexact_p);
        env.addPrimitive(boolean_p);
        env.addPrimitive(not);
        env.addPrimitive(less);
        env.addPrimitive(greater);
        env.addPrimitive(less_equal);
        env.addPrimitive(greater_equal);
        env.addPrimitive(max);
        env.addPrimitive(min);
        env.addPrimitive(abs);
        env.addPrimitive(quotient);
        env.addPrimitive(remainder);
        env.addPrimitive(modulo);
        env.addPrimitive(gcd);
        env.addPrimitive(lcm);
        env.addPrimitive(numerator);
        env.addPrimitive(denominator);
        env.addPrimitive(cos);
        env.addPrimitive(sin);
        env.addPrimitive(tan);
        env.addPrimitive(acos);
        env.addPrimitive(asin);
        env.addPrimitive(atan);
        env.addPrimitive(expt);
        env.addPrimitive(exp);
        env.addPrimitive(log);
        env.addPrimitive(sqrt);
        env.addPrimitive(ceiling);
        env.addPrimitive(floor);
        env.addPrimitive(round);
        env.addPrimitive(truncate);
        env.addPrimitive(angle);
        env.addPrimitive(magnitude);
        env.addPrimitive(imag_part);
        env.addPrimitive(real_part);
        env.addPrimitive(make_rectangular);
        env.addPrimitive(make_polar);
        env.addPrimitive(exact_to_inexact);
        env.addPrimitive(inexact_to_exact);
    }
    
    private static final Primitive plus = new Primitive() {
        public String getName() { return "+"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.type() == SType.NULL) {
                return new SInteger(0);
            }
            else if (args.type() == SType.CONS) {
                SExpr cur = args;
                SNumber res = new SInteger(0);
                while (cur.type() == SType.CONS) {
                    SExpr car = car(cur);
                    if (car instanceof SNumber) {
                        res = res.add((SNumber)car);
                        cur = cdr(cur);
                    }
                    else {
                        eval.addError("+: expected argument of type number, but got %1", car);
                        return null;
                    }
                }
                return res;
            }
            else {
                return null;
            }
        }        
    };

    private static final Primitive minus = new Primitive() {
        public String getName() { return "-"; }
        public SExpr call(SExpr args, Evaluator eval) {
            int len = args.getLength();
            if (len < 1) {
                eval.addError("-: expected number of arguments > 0, but got %1", args.getLength());
                return null;
            }
            else if (len == 1) {
                if (car(args) instanceof SNumber) {
                    return ((SNumber)car(args)).neg();
                }
                else {
                    eval.addError("-: expected argument of type number, but got %1", args.nth(0));
                    return null;
                }
            }
            else {
                SExpr cur = args;
                if (car(cur) instanceof SNumber) {
                    SNumber n = (SNumber)car(cur);
                    cur = cdr(cur);
                    while (cur.type() == SType.CONS) {
                        if (cur.getCar() instanceof SNumber) {
                            n = n.subtract((SNumber)car(cur));
                            cur = cdr(cur);
                        }
                        else {
                            eval.addError("-: expected argument of type number, but got %1", args.nth(0));
                            return null;
                        }
                    }
                    return n;                    
                }
                else {
                    eval.addError("-: expected argument of type number, but got %1", args.nth(0));
                    return null;
                }
            }
        }
    };
    
    private static final Primitive times = new Primitive() {
        public String getName() { return "*"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.type() == SType.NULL) {
                return new SInteger(1);
            }
            else if (args.type() == SType.CONS) {
                SExpr cur = args;
                SNumber res = new SInteger(1);
                while (cur.type() == SType.CONS) {
                    SExpr car = car(cur);
                    if (car instanceof SNumber) {
                        res = res.multiply((SNumber)car);
                        cur = cdr(cur);
                    }
                    else {
                        eval.addError("*: expected argument of type number, but got %1", car);
                    }
                }
                return res;
            }
            else {
                return null;
            }
        }        
    };
    
    private static final Primitive divide = new Primitive() {
        public String getName() { return "/"; }
        public SExpr call(SExpr args, Evaluator eval) {
            int l = args.getLength();
            if (l == 1) {
                SExpr a = car(args);
                if (a instanceof SNumber) {
                    try {
                        return ((SNumber)a).divideInto(new SInteger(1));
                    }
                    catch (ArithmeticException e) {
                        eval.addError("/: division by zero");
                        return null;
                    }
                }
                else {
                    eval.addError("/: expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else if (l > 1) {
                SExpr a = car(args);
                if (a instanceof SNumber) {
                    SNumber res = (SNumber)a;
                    SExpr cdr = cdr(args);
                    try {
                        while (!(cdr.type() == SType.NULL)) {
                            SExpr b = car(cdr);
                            if (b instanceof SNumber) {
                                res = res.divide((SNumber)b);
                            }
                            else {
                                eval.addError("/: expected argument of type number, but got %1", b);
                                return null;
                            }
                            cdr = cdr(cdr);                        
                        }
                        return res;
                    }
                    catch (ArithmeticException e) {
                        eval.addError("/: division by zero");
                        return null;
                    }
                }
                else {
                    eval.addError("/: expected argument of type number, but got %1", a);
                    return null;
                }
            }
            else {
                eval.addError("/: expected number of arguments > 1, but got %1", args.getLength());
                return null;
            }
        }        
    };
    
    private static final Primitive equality = new Primitive() {
        public String getName() { return "="; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() >= 2) {
                SExpr first = car(args);
                if (!first.isNumber()) {
                    eval.addError("=: expected argument of type number, but got %1", first);
                    return null;
                }
                SExpr cdr = cdr(args);
                while (cdr.type() == SType.CONS) {
                    SExpr car = car(cdr);
                    if (!car.isNumber()) {
                        eval.addError("=: expected argument of type, but got %1", car);
                        return null;
                    }
                    else if (!car.equals(first)) {
                        return SBoolean.FALSE;
                    }
                    first = car;
                    cdr = cdr(cdr);
                }
                return SBoolean.TRUE;
            }
            else {
                eval.addError("=: expected number of arguments > 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive number_p = new Primitive() {
        public String getName() { return "number?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                return SBoolean.make(car(args).isNumber());
            }
            else {
                eval.addError("number?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive complex_p = new Primitive() {
        public String getName() { return "real?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                return SBoolean.make(car(args).isNumber());
            }
            else {
                eval.addError("complex?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive real_p = new Primitive() {
        public String getName() { return "real?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                return SBoolean.make(arg.type() == SType.REAL || args.type() == SType.INTEGER || args.type() == SType.RATIONAL);
            }
            else {
                eval.addError("real?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive rational_p = new Primitive() {
        public String getName() { return "rational?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                return SBoolean.make(arg.type() == SType.RATIONAL || arg.type() == SType.INTEGER);
            }
            else {
                eval.addError("rational?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive integer_p = new Primitive() {
        public String getName() { return "integer?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                return SBoolean.make(car(args).type() == SType.INTEGER);
            }
            else {
                eval.addError("integer?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive odd_p = new Primitive() {
        public String getName() { return "odd?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = args.nth(0);
                if (arg instanceof SInteger) {
                    return SBoolean.make(((SInteger)arg).getInt()%2 == 1);
                }
                else {
                    eval.addError("odd?: expected integer argument, but got %1", args.nth(0));
                }
            }
            else {
                eval.addError("odd?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive even_p = new Primitive() {
        public String getName() { return "even?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = args.nth(0);
                if (arg instanceof SInteger) {
                    return SBoolean.make(((SInteger)arg).getInt()%2 == 0);
                }
                else {
                    eval.addError("even?: expected integer argument, but got %1", args.nth(0));
                }
            }
            else {
                eval.addError("even?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive positive_p = new Primitive() {
        public String getName() { return "positive?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr x = args.nth(0);
                if (x instanceof SNumber && !(x.type() == SType.COMPLEX)) {
                    return SBoolean.make(((SNumber)x).positive_p());
                }
                else {
                    eval.addError("positive?: wrong type of argument");
                }
            }
            else {
                eval.addError("even?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive negative_p = new Primitive() {
        public String getName() { return "negative?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr x = args.nth(0);
                if (x instanceof SNumber && !(x.type() == SType.COMPLEX)) {
                    return SBoolean.make(((SNumber)x).negative_p());
                }
                else {
                    eval.addError("negative?: wrong type of argument");
                }
            }
            else {
                eval.addError("negative?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive zero_p = new Primitive() {
        public String getName() { return "zero?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr x = args.nth(0);
                if (x instanceof SNumber) {
                    return SBoolean.make(((SNumber)x).zero_p());
                }
                else {
                    eval.addError("zero?: wrong type of argument");
                }
            }
            else {
                eval.addError("zero?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive exact_p = new Primitive() {
        public String getName() { return "exact?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr sexpr = args.nth(0);
                return SBoolean.make(sexpr.type() == SType.INTEGER || sexpr.type() == SType.RATIONAL);
            }
            else {
                eval.addError("exact?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive inexact_p = new Primitive() {
        public String getName() { return "inexact?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr sexpr = args.nth(0);
                return SBoolean.make(sexpr.type() == SType.REAL || sexpr.type() == SType.COMPLEX);
            }
            else {
                eval.addError("inexact?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive boolean_p = new Primitive() {
        public String getName() { return "boolean?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                return SBoolean.make(args.nth(0).type() == SType.BOOLEAN);
            }
            else {
                eval.addError("boolean?: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };

    private static final Primitive not = new Primitive() {
        public String getName() { return "not"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                return SBoolean.make(args.nth(0) == SBoolean.FALSE);
            }
            else {
                eval.addError("not: expected number of arguments is 1, but got %1", args.getLength());
            }
            return null;
        }
    };
    
    private static final Primitive less = new Primitive() {
        public String getName() { return "<"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() >= 2) {
                if (!car(args).isNumber() || car(args).type() == SType.COMPLEX) {
                    eval.addError("<: expected argument of type real, but got %1", car(args));
                    return null;
                }
                SNumber last = (SNumber)car(args);
                SExpr cdr = cdr(args);
                while (!(cdr.type() == SType.NULL)) {
                    if (!car(cdr).isNumber() || car(cdr).type() == SType.COMPLEX) {
                        eval.addError("<: expected argument of type real, but got %1", car(cdr));
                        return null;
                    }
                    SNumber next = (SNumber)car(cdr);
                    if (!next.subtract(last).positive_p()) {
                        return SBoolean.FALSE;
                    }
                    last = next;
                    cdr = cdr(cdr);
                }
                return SBoolean.TRUE;
            }
            else {
                eval.addError("<: expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive greater = new Primitive() {
        public String getName() { return ">"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() >= 2) {
                if (!car(args).isNumber() || car(args).type() == SType.COMPLEX) {
                    eval.addError(">: expected argument of type real, but got %1", car(args));
                    return null;
                }
                SNumber last = (SNumber)car(args);
                SExpr cdr = cdr(args);
                while (!(cdr.type() == SType.NULL)) {
                    if (!car(cdr).isNumber() || car(cdr).type() == SType.COMPLEX) {
                        eval.addError(">: expected argument of type real, but got %1", car(cdr));
                        return null;
                    }
                    SNumber next = (SNumber)car(cdr);
                    if (!next.subtract(last).negative_p()) {
                        return SBoolean.FALSE;
                    }
                    last = next;
                    cdr = cdr(cdr);
                }
                return SBoolean.TRUE;
            }
            else {
                eval.addError(">: expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive less_equal = new Primitive() {
        public String getName() { return "<="; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() >= 2) {
                if (!car(args).isNumber() || car(args).type() == SType.COMPLEX) {
                    eval.addError("<=: expected argument of type real, but got %1", car(args));
                    return null;
                }
                SNumber last = (SNumber)car(args);
                SExpr cdr = cdr(args);
                while (!(cdr.type() == SType.NULL)) {
                    if (!car(cdr).isNumber() || car(cdr).type() == SType.COMPLEX) {
                        eval.addError("<=: expected argument of type real, but got %1", car(cdr));
                        return null;
                    }
                    SNumber next = (SNumber)car(cdr);
                    SNumber res = next.subtract(last);
                    if (!(res.positive_p() || res.zero_p())) {
                        return SBoolean.FALSE;
                    }
                    last = next;
                    cdr = cdr(cdr);
                }
                return SBoolean.TRUE;
            }
            else {
                eval.addError("<=: expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive greater_equal = new Primitive() {
        public String getName() { return ">="; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() >= 2) {
                if (!car(args).isNumber() || car(args).type() == SType.COMPLEX) {
                    eval.addError(">=: expected argument of type real, but got %1", car(args));
                    return null;
                }
                SNumber last = (SNumber)car(args);
                SExpr cdr = cdr(args);
                while (!(cdr.type() == SType.NULL)) {
                    if (!car(cdr).isNumber() || car(cdr).type() == SType.COMPLEX) {
                        eval.addError(">=: expected argument of type real, but got %1", car(cdr));
                        return null;
                    }
                    SNumber next = (SNumber)car(cdr);
                    SNumber res = next.subtract(last);
                    if (!(res.negative_p() || res.zero_p())) {
                        return SBoolean.FALSE;
                    }
                    last = next;
                    cdr = cdr(cdr);
                }
                return SBoolean.TRUE;
            }
            else {
                eval.addError(">=: expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive  max = new Primitive() {
        public String getName() { return "max"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() >= 1) {
                if (!car(args).isNumber() || car(args).type() == SType.COMPLEX) {
                    eval.addError("max: expected argument of type real, but got %1", car(args));
                    return null;
                }
                SNumber max0 = (SNumber)car(args);
                SExpr cdr = cdr(args);
                while (!(cdr.type() == SType.NULL)) {
                    if (!car(cdr).isNumber() || car(cdr).type() == SType.COMPLEX) {
                        eval.addError("max: expected argument of type real, but got %1", car(cdr));
                        return null;
                    }
                    SNumber next = (SNumber)car(cdr);
                    SNumber res = next.subtract(max0);
                    if (res.positive_p()) {
                        max0 = next;
                    }
                    cdr = cdr(cdr);
                }
                return max0;
            }
            else {
                eval.addError("max: expected number of arguments is >= 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive  min = new Primitive() {
        public String getName() { return "min"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() >= 1) {
                if (!car(args).isNumber() || car(args).type() == SType.COMPLEX) {
                    eval.addError("min: expected argument of type real, but got %1", car(args));
                    return null;
                }
                SNumber min0 = (SNumber)car(args);
                SExpr cdr = cdr(args);
                while (!(cdr.type() == SType.NULL)) {
                    if (!car(cdr).isNumber() || car(cdr).type() == SType.COMPLEX) {
                        eval.addError("min: expected argument of type real, but got %1", car(cdr));
                        return null;
                    }
                    SNumber next = (SNumber)car(cdr);
                    SNumber res = next.subtract(min0);
                    if (res.negative_p()) {
                        min0 = next;
                    }
                    cdr = cdr(cdr);
                }
                return min0;
            }
            else {
                eval.addError("min: expected number of arguments is >= 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive abs = new Primitive() {
        public String getName() { return "abs"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).abs();
                }
                else {
                    eval.addError("abs: expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError("abs: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive quotient = new Primitive() {
        public String getName() { return "quotient"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 2) {
                SExpr a = car(args);
                SExpr b = car(cdr(args));
                if (a.type() == SType.INTEGER && b.type() == SType.INTEGER) {
                    try {
                        return new SInteger(((SInteger)a).getInt()/((SInteger)b).getInt());
                    }
                    catch (ArithmeticException e) {
                        eval.addError("quotient: division by zero");
                        return null;
                    }
                }
                else {
                    eval.addError("quotient: expected arguments of type integer, but got %1 and %2", a, b);
                    return null;
                }
            }
            else {
                eval.addError("quotient: expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive remainder = new Primitive() {
        public String getName() { return "remainder"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 2) {
                SExpr a = car(args);
                SExpr b = car(cdr(args));
                if (a.type() == SType.INTEGER && b.type() == SType.INTEGER) {
                    try {
                        return new SInteger(NumberTheory.mod(((SInteger)a).getInt(), ((SInteger)b).getInt()));
                    }
                    catch (ArithmeticException e) {
                        eval.addError("remainder: division by zero");
                        return null;
                    }
                }
                else {
                    eval.addError("remainder: expected arguments of type integer, but got %1 and %2", a, b);
                    return null;
                }
            }
            else {
                eval.addError("remainder: expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive modulo = new Primitive() {
        public String getName() { return "modulo"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 2) {
                SExpr a = car(args);
                SExpr b = car(cdr(args));
                if (a.type() == SType.INTEGER && b.type() == SType.INTEGER) {
                    try {
                        return new SInteger(((SInteger)a).getInt()%((SInteger)b).getInt());
                    }
                    catch (ArithmeticException e) {
                        eval.addError("modulo: division by zero");
                        return null;
                    }
                }
                else {
                    eval.addError("module: expected arguments of type integer, but got %1 and %2", a, b);
                    return null;
                }
            }
            else {
                eval.addError("modulo: expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive gcd = new Primitive() {
        public String getName() { return "gcd"; }
        public SExpr call(SExpr args, Evaluator eval) {
            int l = args.getLength();
            if (l == 0) {
                return new SInteger(0);
            }
            else if (l == 1) {
                if (car(args).type() == SType.INTEGER) {
                    return car(args);
                }
                else {
                    eval.addError("gcd: expected argument of type integer, but got %1", car(args));
                    return null;
                }
            }
            else {
                if (!(car(args).type() == SType.INTEGER)) {
                    eval.addError("gcd: expected argument of type integer, but got %1", car(args));
                    return null;
                }
                int gcd_val = ((SInteger)car(args)).getInt();
                SExpr cdr = cdr(args);
                while (!(cdr.type() == SType.NULL)) {
                    if (!(car(cdr).type() == SType.INTEGER)) {
                        eval.addError("gcd: expected argument of type integer, but got %1", car(cdr));
                        return null;
                    }
                    SInteger next = (SInteger)car(cdr);
                    gcd_val = NumberTheory.gcd(gcd_val, next.getInt());
                    cdr = cdr(cdr);
                }
                return new SInteger(gcd_val);
            }
        }
    };

    private static final Primitive lcm = new Primitive() {
        public String getName() { return "lcm"; }
        public SExpr call(SExpr args, Evaluator eval) {
            int l = args.getLength();
            if (l == 0) {
                return new SInteger(1);
            }
            else if (l == 1) {
                if (car(args).type() == SType.INTEGER) {
                    return car(args);
                }
                else {
                    eval.addError("lcm: expected argument of type integer, but got %1", car(args));
                    return null;
                }
            }
            else {
                if (!(car(args).type() == SType.INTEGER)) {
                    eval.addError("lcm: expected argument of type integer, but got %1", car(args));
                    return null;
                }
                int gcd_val = ((SInteger)car(args)).getInt();
                SExpr cdr = cdr(args);
                while (!(cdr.type() == SType.NULL)) {
                    if (!(car(cdr).type() == SType.INTEGER)) {
                        eval.addError("lcm: expected argument of type integer, but got %1", car(cdr));
                        return null;
                    }
                    SInteger next = (SInteger)car(cdr);
                    gcd_val = NumberTheory.lcm(gcd_val, next.getInt());
                    cdr = cdr(cdr);
                }
                return new SInteger(gcd_val);
            }
        }
    };

    private static final Primitive numerator = new Primitive() {
        public String getName() { return "numerator"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).type() == SType.RATIONAL) {
                    return new SInteger(((SRational)car(args)).getRational().getNumerator());
                }
                else if (car(args).type() == SType.INTEGER) {
                    return car(args);
                }
                else {
                    eval.addError("numerator: expected argument of type rational, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError("numerator: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive denominator = new Primitive() {
        public String getName() { return "denominator"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).type() == SType.RATIONAL) {
                    return new SInteger(((SRational)car(args)).getRational().getDenominator());
                }
                else if (car(args).type() == SType.INTEGER) {
                    return new SInteger(1);
                }
                else {
                    eval.addError("denominator: expected argument of type rational, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError("denominator: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive sin = new Primitive() {
        public String getName() { return "sin"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).sin();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive cos = new Primitive() {
        public String getName() { return "cos"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).cos();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive tan = new Primitive() {
        public String getName() { return "tan"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).tan();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive asin = new Primitive() {
        public String getName() { return "asin"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).asin();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive acos = new Primitive() {
        public String getName() { return "acos"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).acos();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive atan = new Primitive() {
        public String getName() { return "atan"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 2) {
                if (car(args).isNumber() && car(cdr(args)).isNumber()) {
                    return ((SNumber)car(args)).atan((SNumber)car(cdr(args)));
                }
                else {
                    eval.addError(getName()+": expected arguments of type number, but got %1 and %2", car(args), car(cdr(args)));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive expt = new Primitive() {
        public String getName() { return "expt"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 2) {
                if (car(args).isNumber() && car(cdr(args)).isNumber()) {
                    return ((SNumber)car(args)).expt((SNumber)car(cdr(args)));
                }
                else {
                    eval.addError(getName()+": expected arguments of type number, but got %1 and %2", car(args), car(cdr(args)));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive exp = new Primitive() {
        public String getName() { return "exp"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).exp();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive log = new Primitive() {
        public String getName() { return "log"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).log();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive sqrt = new Primitive() {
        public String getName() { return "sqrt"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).exp();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive ceiling = new Primitive() {
        public String getName() { return "ceiling"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).ceiling();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive floor = new Primitive() {
        public String getName() { return "floor"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).floor();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive round = new Primitive() {
        public String getName() { return "round"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).round();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive truncate = new Primitive() {
        public String getName() { return "truncate"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).truncate();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive angle = new Primitive() {
        public String getName() { return "angle"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).angle();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive magnitude = new Primitive() {
        public String getName() { return "magnitude"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).abs().toReal();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive imag_part = new Primitive() {
        public String getName() { return "imag-part"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).imagPart();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive real_part = new Primitive() {
        public String getName() { return "real-part"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                if (car(args).isNumber()) {
                    return ((SNumber)car(args)).realPart();
                }
                else {
                    eval.addError(getName()+": expected argument of type number, but got %1", car(args));
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive make_rectangular = new Primitive() {
        public String getName() { return "make-rectangular"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 2) {
                SExpr a = car(args);
                SExpr b = car(cdr(args));
                if ((a.type() == SType.INTEGER || a.type() == SType.RATIONAL || a.type() == SType.REAL) &&
                    (b.type() == SType.INTEGER || b.type() == SType.RATIONAL || b.type() == SType.REAL)) {
                    return new SComplex(new Complex(((SNumber)a).toReal().getDouble(), ((SNumber)b).toReal().getDouble()));
                }
                else {
                    eval.addError(getName()+": expected arguments of type real, but got %1 and %2", a, b);
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive make_polar = new Primitive() {
        public String getName() { return "make-polar"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 2) {
                SExpr a = car(args);
                SExpr b = car(cdr(args));
                if ((a.type() == SType.INTEGER || a.type() == SType.RATIONAL || a.type() == SType.REAL) &&
                    (b.type() == SType.INTEGER || b.type() == SType.RATIONAL || b.type() == SType.REAL)) {
                    return new SComplex(Trigonometry.fromPolar(((SNumber)a).toReal().getDouble(), ((SNumber)b).toReal().getDouble()));
                }
                else {
                    eval.addError(getName()+": expected arguments of type real, but got %1 and %2", a, b);
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 2, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive exact_to_inexact = new Primitive() {
        public String getName() { return "exact->inexact"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr a = car(args);                
                if (a.type() == SType.INTEGER || a.type() == SType.RATIONAL) {
                    return ((SNumber)a).toReal();
                }
                else if (a.type() == SType.REAL || a.type() == SType.COMPLEX) {
                    return a;
                }
                else {
                    eval.addError(getName()+": expected arguments of type number, but got %1", a);
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive inexact_to_exact = new Primitive() {
        public String getName() { return "inexact->exact"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr a = car(args);                
                if (a.type() == SType.INTEGER || a.type() == SType.RATIONAL) {
                    return a;
                }
                else if (a.type() == SType.REAL) {
                    return SRational.make(new Rational(((SReal)a).getDouble()));
                }
                else if (a.type() == SType.COMPLEX) {
                    eval.addError(getName()+": complex argument not supported");
                    return null;
                }
                else {
                    eval.addError(getName()+": expected arguments of type number, but got %1", a);
                    return null;
                }
            }
            else {
                eval.addError(getName()+": expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };
}

