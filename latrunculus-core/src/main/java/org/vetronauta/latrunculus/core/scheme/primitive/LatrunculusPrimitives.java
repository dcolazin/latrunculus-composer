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
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.core.logeo.DenoFactory;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.element.generic.FreeElement;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ColimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ListDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.ColimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.scheme.expression.Env;
import org.vetronauta.latrunculus.core.scheme.Evaluator;
import org.vetronauta.latrunculus.core.scheme.expression.SBoolean;
import org.vetronauta.latrunculus.core.scheme.expression.SChar;
import org.vetronauta.latrunculus.core.scheme.expression.SComplex;
import org.vetronauta.latrunculus.core.scheme.expression.SDenotator;
import org.vetronauta.latrunculus.core.scheme.expression.SExpr;
import org.vetronauta.latrunculus.core.scheme.expression.SForm;
import org.vetronauta.latrunculus.core.scheme.expression.SInteger;
import org.vetronauta.latrunculus.core.scheme.expression.SRational;
import org.vetronauta.latrunculus.core.scheme.expression.SReal;
import org.vetronauta.latrunculus.core.scheme.expression.SString;
import org.vetronauta.latrunculus.core.scheme.expression.SType;
import org.vetronauta.latrunculus.core.scheme.expression.SVector;
import org.vetronauta.latrunculus.core.scheme.expression.Symbol;

import java.util.LinkedList;
import java.util.List;

import static org.vetronauta.latrunculus.core.scheme.expression.SExpr.car;
import static org.vetronauta.latrunculus.core.scheme.expression.SExpr.cdr;
import static org.vetronauta.latrunculus.core.scheme.expression.SExpr.cons;
import static org.vetronauta.latrunculus.core.scheme.expression.SNull.SCHEME_NULL;


/**
 * Latrunculus-specific primitive procedures.
 * 
 * @author Gérard Milmeister
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LatrunculusPrimitives {

    public static void fillEnvironment(Env env) {
        env.addPrimitive(form_p);
        env.addPrimitive(denotator_p);
        env.addPrimitive(get_form);
        env.addPrimitive(get_denotator);
        env.addPrimitive(get_all_forms);
        env.addPrimitive(get_all_denotators);
        env.addPrimitive(type_simple_p);
        env.addPrimitive(type_limit_p);
        env.addPrimitive(type_colimit_p);
        env.addPrimitive(type_power_p);
        env.addPrimitive(type_list_p);
        env.addPrimitive(type_of);
        env.addPrimitive(name_of);
        env.addPrimitive(form_of);
        env.addPrimitive(forms_of);
        env.addPrimitive(form_count_of);
        env.addPrimitive(factors_of);
        env.addPrimitive(factor_count_of);
        env.addPrimitive(index_of);
        env.addPrimitive(element_of);
        env.addPrimitive(make_denotator);
        env.addPrimitive(register);
    }

    private static final Primitive form_p = new Primitive() {
        public String getName() { return "form?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr car = car(args);
                return SBoolean.make(car.type() == SType.FORM);
            }
            else {
                eval.addError("form?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive denotator_p = new Primitive() {
        public String getName() { return "denotator?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr car = car(args);
                return SBoolean.make(car.type() == SType.DENOTATOR);
            }
            else {
                eval.addError("denotator?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive get_form = new Primitive() {
        public String getName() { return "get-form"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr car = car(args);
                if (car.type() == SType.SYMBOL) {
                    Form f = rep.getForm(((Symbol)car).getName());
                    return (f == null) ? SCHEME_NULL : new SForm(f);
                }
                else if (car.type() == SType.STRING) {
                    Form f = rep.getForm(((SString)car).getString());
                    return (f == null) ? SCHEME_NULL : new SForm(f);
                }
                else {
                    eval.addError("get-form: expected argument of type symbol or string, but got %1", car);
                    return null;
                }
            }
            else {
                eval.addError("get-form: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive get_denotator = new Primitive() {
        public String getName() { return "get-denotator"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr car = car(args);
                if (car.type() == SType.SYMBOL) {
                    Denotator d = rep.getDenotator(((Symbol)car).getName());
                    return (d == null) ? SCHEME_NULL : new SDenotator(d);
                }
                else if (car.type() == SType.STRING) {
                    Denotator d = rep.getDenotator(((SString)car).getString());
                    return (d == null) ? SCHEME_NULL : new SDenotator(d);
                }
                else {
                    eval.addError("get-denotator: expected argument of type symbol or string, but got %1", car);
                    return null;
                }
            }
            else {
                eval.addError("get-denotator: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive get_all_forms = new Primitive() {
        public String getName() { return "get-all-forms"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 0) {
                SExpr res = SCHEME_NULL;
                for (Form form : rep.getForms()) {
                    res = cons(new SForm(form), res);
                }
                return res;
            }
            else {
                eval.addError("get-all-forms: expected number of arguments is 0, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive get_all_denotators = new Primitive() {
        public String getName() { return "get-all-denotators"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 0) {
                SExpr res = SCHEME_NULL;
                for (Denotator d : rep.getDenotators()) {
                    res = cons(new SDenotator(d), res);
                }
                return res;
            }
            else {
                eval.addError("get-all-denotators: expected number of arguments is 0, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive type_simple_p = new Primitive() {
        public String getName() { return "type-simple?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SForm) {
                    return SBoolean.make(((SForm)arg).getForm().getType() == FormDenotatorTypeEnum.SIMPLE);
                }
                else if (arg instanceof SDenotator) {
                    return SBoolean.make(((SDenotator)arg).getDenotator().getType() == FormDenotatorTypeEnum.SIMPLE);
                }
                else {
                    eval.addError("type-simple?: expected argument of type form or denotator, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("type-simple?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive type_limit_p = new Primitive() {
        public String getName() { return "type-limit?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SForm) {
                    return SBoolean.make(((SForm)arg).getForm().getType() == FormDenotatorTypeEnum.LIMIT);
                }
                else if (arg instanceof SDenotator) {
                    return SBoolean.make(((SDenotator)arg).getDenotator().getType() == FormDenotatorTypeEnum.LIMIT);
                }
                else {
                    eval.addError("type-limit?: expected argument of type form or denotator, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("type-limit?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive type_colimit_p = new Primitive() {
        public String getName() { return "type-colimit?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SForm) {
                    return SBoolean.make(((SForm)arg).getForm().getType() == FormDenotatorTypeEnum.COLIMIT);
                }
                else if (arg instanceof SDenotator) {
                    return SBoolean.make(((SDenotator)arg).getDenotator().getType() == FormDenotatorTypeEnum.COLIMIT);
                }
                else {
                    eval.addError("type-colimit?: expected argument of type form or denotator, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("type-colimit?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive type_power_p = new Primitive() {
        public String getName() { return "type-power?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SForm) {
                    return SBoolean.make(((SForm)arg).getForm().getType() == FormDenotatorTypeEnum.POWER);
                }
                else if (arg instanceof SDenotator) {
                    return SBoolean.make(((SDenotator)arg).getDenotator().getType() == FormDenotatorTypeEnum.POWER);
                }
                else {
                    eval.addError("type-power?: expected argument of type form or denotator, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("type-power?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive type_list_p = new Primitive() {
        public String getName() { return "type-list?"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SForm) {
                    return SBoolean.make(((SForm)arg).getForm().getType() == FormDenotatorTypeEnum.LIST);
                }
                else if (arg instanceof SDenotator) {
                    return SBoolean.make(((SDenotator)arg).getDenotator().getType() == FormDenotatorTypeEnum.LIST);
                }
                else {
                    eval.addError("type-list?: expected argument of type form or denotator, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("type-list?: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive type_of = new Primitive() {
        public String getName() { return "type-of"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SForm) {
                    return type_of(((SForm)arg).getForm().getType());
                }
                else if (arg instanceof SDenotator) {
                    return type_of(((SDenotator)arg).getDenotator().getType());
                }
                else {
                    eval.addError("type-of: expected argument of type form or denotator, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("type-of: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive name_of = new Primitive() {
        public String getName() { return "name-of"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SForm) {
                    return new SString(((SForm)arg).getForm().getNameString());
                }
                else if (arg instanceof SDenotator) {
                    return new SString(((SDenotator)arg).getDenotator().getNameString());
                }
                else {
                    eval.addError("name-of: expected argument of type form or denotator, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("name-of: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive form_of = new Primitive() {
        public String getName() { return "form-of"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SDenotator) {
                    return new SForm(((SDenotator)arg).getDenotator().getForm());
                }
                else {
                    eval.addError("form-of: expected argument of type denotator, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("form-of: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive forms_of = new Primitive() {
        public String getName() { return "forms-of"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SForm) {
                    Form f = ((SForm)arg).getForm();
                    if (f instanceof LimitForm) {
                        return formListToScheme(f.getForms());
                    }
                    else if (f instanceof ColimitForm) {
                        return formListToScheme(f.getForms());
                    }
                    else if (f instanceof PowerForm) {
                        return formListToScheme(f.getForms());
                    }
                    else if (f instanceof ListForm) {
                        return formListToScheme(f.getForms());
                    }
                    else {
                        return SCHEME_NULL;
                    }                    
                }
                else {
                    eval.addError("forms-of: expected argument of type form, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("forms-of: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive form_count_of = new Primitive() {
        public String getName() { return "form-count-of"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SForm) {
                    Form f = ((SForm)arg).getForm();
                    if (f instanceof LimitForm) {
                        return new SInteger(f.getFormCount());
                    }
                    else if (f instanceof ColimitForm) {
                        return new SInteger(f.getFormCount());
                    }
                    else if (f instanceof PowerForm) {
                        return new SInteger(f.getFormCount());
                    }
                    else if (f instanceof ListForm) {
                        return new SInteger(f.getFormCount());
                    }
                    else {
                        return new SInteger(0);
                    }                    
                }
                else {
                    eval.addError("form-count-of: expected argument of type form, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("form-count-of: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive factors_of = new Primitive() {
        public String getName() { return "factors-of"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SDenotator) {
                    Denotator d = ((SDenotator)arg).getDenotator();
                    if (d instanceof LimitDenotator) {
                        return denoListToScheme(((LimitDenotator)d).getFactors());
                    }
                    else if (d instanceof ColimitDenotator) {
                        return denoListToScheme(((ColimitDenotator)d).getFactors());
                    }
                    else if (d instanceof PowerDenotator) {
                        return denoListToScheme(((PowerDenotator)d).getFactors());
                    }
                    else if (d instanceof ListDenotator) {
                        return denoListToScheme(((ListDenotator)d).getFactors());
                    }
                    else {
                        return SCHEME_NULL;
                    }                    
                }
                else {
                    eval.addError("factors-of: expected argument of type denotator, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("factors-of: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive factor_count_of = new Primitive() {
        public String getName() { return "factor-count-of"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SDenotator) {
                    Denotator d = ((SDenotator)arg).getDenotator();
                    if (d instanceof LimitDenotator) {
                        return new SInteger(((LimitDenotator)d).getFactorCount());
                    }
                    else if (d instanceof ColimitDenotator) {
                        return new SInteger(((ColimitDenotator)d).getFactorCount());
                    }
                    else if (d instanceof PowerDenotator) {
                        return new SInteger(((PowerDenotator)d).getFactorCount());
                    }
                    else if (d instanceof ListDenotator) {
                        return new SInteger(((ListDenotator)d).getFactorCount());
                    }
                    else {
                        return new SInteger(0);
                    }                    
                }
                else {
                    eval.addError("factor-count-of: expected argument of type denotator, but got %1", arg);
                    return null;
                }
            }
            else {
                eval.addError("factor-count-of: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive index_of = new Primitive() {
        public String getName() { return "index-of"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SDenotator) {
                    Denotator d = ((SDenotator)arg).getDenotator();
                    if (d instanceof ColimitDenotator) {
                        return new SInteger(((ColimitDenotator)d).getIndex());
                    }
                }
                eval.addError("index-of: expected argument of type colimit denotator, but got %1", arg);
                return null;
            }
            else {
                eval.addError("index-of: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive element_of = new Primitive() {
        public String getName() { return "element-of"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SDenotator) {
                    Denotator d = ((SDenotator)arg).getDenotator();
                    if (d instanceof SimpleDenotator) {
                        ModuleElement element = ((SimpleDenotator)d).getElement();
                        SExpr sexpr = moduleElementToSExpr(element);
                        if (sexpr != null) {
                            return sexpr;
                        }
                        else {
                            eval.addError("element-of: could not convert module element to Scheme expression");
                            return null;
                        }
                    }
                }
                eval.addError("element-of: expected argument of type simple denotator, but got %1", arg);
                return null;
            }
            else {
                eval.addError("element-of: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static final Primitive make_denotator = new Primitive() {
        public String getName() { return "make-denotator"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 3) {
                SExpr arg1 = car(args);
                SExpr arg2 = car(cdr(args));
                SExpr arg3 = car(cdr(cdr(args)));
                String name = null;
                Form form;
                // retrieve name
                if (arg1.type() == SType.STRING) {
                    name = ((SString) arg1).getString();
                }
                else if (arg1.type() == SType.SYMBOL) {
                    name = arg1.toString();
                }
                if (!(arg2.type() == SType.FORM)) {
                    eval.addError("make-denotator: expected 2nd argument of type form, but got %1", arg2);
                    return null;
                }
                form = ((SForm)arg2).getForm();
                Denotator d = null;
                switch (form.getType()) {
                case SIMPLE: {
                    d = makeSimple(eval, name, form, arg3);
                    break;
                }
                case LIMIT: {
                    d = makeLimit(eval, name, form, arg3);
                    break;
                }
                case COLIMIT: {
                    d = makeColimit(eval, name, form, arg3);
                    break;
                }
                case POWER: {
                    d = makePowerList(eval, name, form, arg3);
                    break;
                }
                case LIST: {
                    d = makePowerList(eval, name, form, arg3);
                    break;
                }
                }
                if (d != null) {
                    return new SDenotator(d);
                }
                else {
                    return null;
                }
            }
            else {
                eval.addError("make-denotator: expected number of arguments is 3, but got %1", args.getLength());
                return null;
            }
        }
    };
    
    private static final Primitive register = new Primitive() {
        public String getName() { return "register"; }
        public SExpr call(SExpr args, Evaluator eval) {
            if (args.getLength() == 1) {
                SExpr arg = car(args);
                if (arg instanceof SDenotator) {
                    Denotator d = ((SDenotator)arg).getDenotator();
                    if (d.getNameString().length() > 0) {
                        rep.register(d);
                    }
                }
                eval.addError("register: expected argument of type denotator, but got %1", arg);
                return null;
            }
            else {
                eval.addError("register: expected number of arguments is 1, but got %1", args.getLength());
                return null;
            }
        }
    };

    private static SExpr formListToScheme(List<Form> formList) {
        SExpr res = SCHEME_NULL;
        for (Form form : formList) {
            res = cons(new SForm(form), res);
        }
        return ListPrimitives.reverse(res, SCHEME_NULL);
    }    
    
    private static SExpr denoListToScheme(List<Denotator> denoList) {
        SExpr res = SCHEME_NULL;
        for (Denotator deno : denoList) {
            res = cons(new SDenotator(deno), res);
        }
        return ListPrimitives.reverse(res, SCHEME_NULL);
    }    
    
    private static Symbol type_of(FormDenotatorTypeEnum type) {
        if (type == FormDenotatorTypeEnum.SIMPLE) {
            return simple_sym;
        }
        else if (type == FormDenotatorTypeEnum.LIMIT) {
            return limit_sym;
        }
        else if (type == FormDenotatorTypeEnum.COLIMIT) {
            return colimit_sym;
        }
        else if (type == FormDenotatorTypeEnum.POWER) {
            return power_sym;
        }
        else if (type == FormDenotatorTypeEnum.LIST) {
            return list_sym;
        }
        else {
            return none_sym;
        }
    }
    
    private static Denotator makeSimple(Evaluator eval, String name, Form form, SExpr arg) {
        Denotator d = null;
        ModuleElement element = sexprToModuleElement(arg);
        if (element != null) {
            d = DenoFactory.makeDenotator(name, form, element);
        }
        if (d == null) {
            eval.addError("make-denotator: could not create a simple denotator from arguments");
        }
        return d;
    }
    
    private static Denotator makeLimit(Evaluator eval, String name, Form form, SExpr arg) {
        if (arg.isList()) {
            List<Denotator> denoList = new LinkedList<Denotator>();
            while (!(arg.type() == SType.NULL)) {
                SExpr sexpr = car(arg);
                if (sexpr.type() == SType.DENOTATOR) {
                    denoList.add(((SDenotator)sexpr).getDenotator());
                }
                else {
                    eval.addError("make-denotator: argument list must contain denotators");
                    return null;
                }
            }
            Denotator d = DenoFactory.makeDenotator(name, form, denoList);
            if (d == null) {
                eval.addError("make-denotator: could not create denotator", arg);
            }
            return d;
        }
        else {
            eval.addError("make-denotator: expected argument is a list, but got %1", arg);
            return null;
        }
    }
    
    private static Denotator makeColimit(Evaluator eval, String name, Form form, SExpr arg) {
        Denotator d = null; //TODO implement?
        return d;
    }
    
    private static Denotator makePowerList(Evaluator eval, String name, Form form, SExpr arg) {
        if (arg.isList()) {
            List<Denotator> denoList = new LinkedList<>();
            while (!(arg.type() == SType.NULL)) {
                SExpr sexpr = car(arg);
                if (sexpr.type() == SType.DENOTATOR) {
                    denoList.add(((SDenotator)sexpr).getDenotator());
                }
                else {
                    eval.addError("make-denotator: argument list must contain denotators");
                    return null;
                }
            }
            Denotator d = DenoFactory.makeDenotator(name, form, denoList);
            if (d == null) {
                eval.addError("make-denotator: could not create denotator", arg);
            }
            return d;
        }
        else {
            eval.addError("make-denotator: expected argument is a list, but got %1", arg);
            return null;
        }
    }
    
    private static SExpr moduleElementToSExpr(ModuleElement element) {
        if (element instanceof Complex) {
            return new SComplex(new Complex(((Complex) element).getReal(), ((Complex) element).getImag()));
        }
        if (element instanceof Real) {
            return SReal.make(((Real) element).doubleValue());
        }
        if (element instanceof Rational) {
            return SRational.make((Rational) element);
        }
        if (element instanceof ZInteger) {
            return new SInteger(((ZInteger) element).intValue());
        }
        if (element instanceof FreeElement) {
            FreeElement freeElement = (FreeElement)element;
            int len = freeElement.getLength();
            if (len > 1) {
                SExpr[] sexprList = new SExpr[len];
                for (int i = 0; i < len; i++) {
                    SExpr res = moduleElementToSExpr(freeElement.getComponent(i));
                    if (res == null) {
                        return null;
                    }
                    sexprList[i] = res;
                }
                return new SVector(sexprList);
            }
            else {
                return null;
            }
        }
        return null;
    }
    
    private static ModuleElement sexprToModuleElement(SExpr sexpr) {
        //TODO switch
        if (sexpr.type() == SType.INTEGER) {
            return new ZInteger(((SInteger)sexpr).getInt());
        }
        else if (sexpr.type() == SType.RATIONAL) {
            return ((SRational)sexpr).getRational().deepCopy();
        }
        else if (sexpr.type() == SType.REAL) {
            return new Real((((SReal)sexpr).getDouble()));
        }
        else if (sexpr.type() == SType.COMPLEX) {
            return ((SComplex)sexpr).getComplex();
        }
        else if (sexpr.type() == SType.BOOLEAN) {
            return new ZInteger(sexpr == SBoolean.TRUE ? 1 : 0);
        }
        else if (sexpr.type() == SType.CHAR) {
            return new StringMap<>(ZRing.ring, Character.toString(((SChar)sexpr).getChar()));
        }
        else if (sexpr.type() == SType.STRING) {
            return new StringMap<>(ZRing.ring, ((SString)sexpr).getString());
        }
        else if (sexpr.type() == SType.SYMBOL) {
            return new StringMap<>(ZRing.ring, (sexpr).toString());
        }

        else if (sexpr.type() == SType.VECTOR) {
            SExpr[] v = ((SVector)sexpr).getArray();
            if (v.length == 0) {
                return null;
            }
            else if (v.length == 1) {
                return sexprToModuleElement(v[0]);
            }
            else {
                List<ModuleElement>elementList = new LinkedList<>();
                ModuleElement first = sexprToModuleElement(v[0]); 
                if (first != null) {
                    Module module = first.getModule();
                    elementList.add(first);
                    for (int i = 1; i < v.length; i++) {
                        ModuleElement element = sexprToModuleElement(v[i]);
                        if (element == null || !element.getModule().equals(module)) {
                            return null;
                        }
                        else {
                            elementList.add(element);
                        }
                    }
                    if (module.isRing()) {
                        Module resMod = ((Ring)module).getFreeModule(v.length);
                        return resMod.createElement(elementList);
                    }
                    else {
                        return null;
                    }
                }
                else {
                    return null;
                }
            }
        }
        else {
            return null;
        }
    }
    
    private static final Symbol simple_sym  = Symbol.make("simple");
    private static final Symbol limit_sym   = Symbol.make("limit");
    private static final Symbol colimit_sym = Symbol.make("colimit");
    private static final Symbol power_sym   = Symbol.make("power");
    private static final Symbol list_sym    = Symbol.make("list");
    private static final Symbol none_sym    = Symbol.make("none");

    private static final Repository rep = Repository.systemRepository();
}
