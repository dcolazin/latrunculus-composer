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

package org.vetronauta.latrunculus.core.scheme;

import static org.vetronauta.latrunculus.core.scheme.expression.SExpr.*;
import static org.vetronauta.latrunculus.core.scheme.expression.SNull.SCHEME_NULL;
import static org.vetronauta.latrunculus.core.scheme.expression.SVoid.SCHEME_VOID;

import java.util.LinkedList;
import java.util.List;

import org.vetronauta.latrunculus.core.util.Stoppable;
import org.vetronauta.latrunculus.core.util.TextUtils;
import org.vetronauta.latrunculus.core.scheme.expression.Env;
import org.vetronauta.latrunculus.core.scheme.expression.SBoolean;
import org.vetronauta.latrunculus.core.scheme.expression.SClosure;
import org.vetronauta.latrunculus.core.scheme.expression.SCons;
import org.vetronauta.latrunculus.core.scheme.expression.SExpr;
import org.vetronauta.latrunculus.core.scheme.expression.SInPort;
import org.vetronauta.latrunculus.core.scheme.expression.SOutPort;
import org.vetronauta.latrunculus.core.scheme.expression.SPrimitive;
import org.vetronauta.latrunculus.core.scheme.expression.SType;
import org.vetronauta.latrunculus.core.scheme.expression.Symbol;
import org.vetronauta.latrunculus.core.scheme.primitive.ControlPrimitives;


/**
 * This class provides the means for evaluating Scheme
 * expressions.
 * 
 * @author Gérard Milmeister
 */
public class Evaluator {
    
    /**
     * Creates an evaluator with a global environment
     * as the initial environment.
     */
    public Evaluator() {
        environment = Env.makeGlobalEnvironment();
    }
    
    
    /**
     * Creates an evaluator with the specified
     * initial environment.
     */
    public Evaluator(Env env) {
        environment = env;
    }
    
    
    /**
     * Resets the initial environment to the global environment.
     */
    public void resetGlobal() {
        environment.resetGlobal();
    }
    

    /**
     * Sets the initial environment to <code>env</code>.
     */
    public void setEnvironment(Env env) {
        environment = env;
        
    }
    
    
    /**
     * Sets the runinfo for the next evaluation.
     * The evaluator periodically checks this object and
     * terminates whenever <code>runInfo.stopped()</code> is true. 
     */
    public void setRunInfo(Stoppable runInfo) {
        this.runInfo = runInfo;
    }
    
       
    /**
     * Parses and evaluates the code <code>s</code> in the
     * initial environment. 
     */
    public SExpr eval(String s) {
        List<SExpr> sexpr_list = parser.parse(s);
        if (sexpr_list != null) {
            return eval(sexpr_list);
        }
        else {
            addError("syntax error");
            return SCHEME_VOID;
        }
    }
    
    
    /**
     * Evaluates the specified list of expressions in the
     * initial environment. 
     */
    public SExpr eval(List<SExpr> sexpr_list) {
        clearErrors();
        SExpr res = null;
        for (SExpr sexpr : sexpr_list) {
            res = eval(sexpr);
        }
        return res;
    }

    
    /**
     * Evaluates the specified expression in the
     * initial environment. 
     */
    public SExpr eval(SExpr sexpr) {
        return eval(sexpr, environment);
    }

    
    /**
     * Evaluates the specified expression in the
     * given environment <code>env</code>. 
     */
    @SuppressWarnings("fallthrough")
    public SExpr eval(SExpr sexpr, Env env) {
        exp = sexpr;
        val = sexpr;
        cont = Label.EVAL_FINISH;
        cur_label = Label.EVAL_DISPATCH;
        
        labelStack.clear();
        regStack.clear();
        
        // TODO: correctly close all ports
        outPortStack.clear();
        outPortStack.add(0, new SOutPort(System.out));
        inPortStack.clear();
        inPortStack.add(0, new SInPort(System.in));
        
        finished = false;
        while (!finished) {
            if (runInfo != null && runInfo.stopped()) {
                addError("Evaluation stopped");
                val = SCHEME_VOID;
                finish();
            }
            switch (cur_label) {
            case EVAL_DISPATCH:
                if (exp.type() == SType.NULL) {
                    addError("cannot evaluate ()");
                    finish();
                }
                else if (isSelfEval(exp)) { gotoLabel(Label.EVAL_SELF); }
                else if (isVar(exp)) { gotoLabel(Label.EVAL_VAR); }
                else if (exp.type() == SType.CONS) {
                    SExpr car = car(exp);
                    if (car == Token.QUOTE) { gotoLabel(Label.EVAL_QUOTE); }
                    else if (car == Token.COND) { gotoLabel(Label.EVAL_COND); }
                    else if (car == Token.IF) { gotoLabel(Label.EVAL_IF); }
                    else if (car == Token.DEFINE) { gotoLabel(Label.EVAL_DEFINE); }
                    else if (car == Token.SET) { gotoLabel(Label.EVAL_SET); }
                    else if (car == Token.LAMBDA) { gotoLabel(Label.EVAL_LAMBDA); }
                    else if (car == Token.LET) { gotoLabel(Label.EVAL_LET); }
                    else if (car == Token.LET_STAR) { gotoLabel(Label.EVAL_LET_STAR); }
                    else if (car == Token.AND) { gotoLabel(Label.EVAL_AND); }
                    else if (car == Token.OR) { gotoLabel(Label.EVAL_OR); }
                    else { gotoLabel(Label.EVAL_APP); }
                }
                else if (exp.type() == SType.VOID) {
                    addError("cannot evaluate void value");
                    finish();
                }
                break;
                
            case EVAL_SELF:
                val = exp;
                gotoLabel(cont);
                break;
                
            case EVAL_QUOTE:
                if (!(cdr(exp).type() == SType.CONS)) {
                    addError("quote: malformed expression");
                    finish();
                }
                else {
                    val = car(cdr(exp));
                }
                gotoLabel(cont);
                break;
                
            case EVAL_VAR:
                val = env.get((Symbol)exp);
                if (val == null) {
                    // no binding for symbol exp
                    addError("variable %%1 not defined", exp);
                    finish();
                }
                gotoLabel(cont);
                break;
                
                // lambda and let
                
            case EVAL_LAMBDA:
                val = makeClosure(cdr(exp), env);
                gotoLabel(cont);
                break;
                
            case EVAL_LET:
                exp = letToLambda(exp);
                gotoLabel(Label.EVAL_DISPATCH);
                break;
                
            case EVAL_LET_STAR:
                exp = letStarToLet(exp);
                gotoLabel(Label.EVAL_DISPATCH);
                break;

                // function application
                
            case EVAL_APP:
                unev = getOperands(exp);
                exp = getOperator(exp);
                saveLabel(cont);
                saveReg(env);
                saveReg(unev);
                cont = Label.EVAL_ARGS;
                gotoLabel(Label.EVAL_DISPATCH);
                break;
                
            case EVAL_ARGS:
                unev = restoreReg();
                env = (Env)restoreReg();                
                fun = val;
                if (unev.type() == SType.NULL) {
                    argl = SCHEME_NULL;
                    gotoLabel(Label.APPLY_DISPATCH);
                }
                else {
                    saveReg(fun);
                    argl = SCHEME_NULL;
                    gotoLabel(Label.EVAL_ARG_LOOP);
                }
                break;
                
            case EVAL_ARG_LOOP:
                saveReg(argl);
                if (!(unev.type() == SType.CONS)) {
                    addError("app: argument list malformed");
                    finish();
                    break;
                }
                exp = getFirstOperand(unev);
                if (isLastOperand(unev)) {
                    gotoLabel(Label.EVAL_LAST_ARG); 
                }
                else {
                    saveReg(env);
                    saveReg(unev);
                    cont = Label.ACCUMULATE_ARG;
                    gotoLabel(Label.EVAL_DISPATCH);
                }
                break;
                
            case ACCUMULATE_ARG:
                unev = restoreReg();
                env  = (Env)restoreReg();
                argl = restoreReg();
                argl = cons(val, argl);
                unev = getRestOperands(unev);
                gotoLabel(Label.EVAL_ARG_LOOP);
                break;
                
            case EVAL_LAST_ARG:
                cont = Label.ACCUMULATE_LAST_ARG;
                gotoLabel(Label.EVAL_DISPATCH);
                break;
                
            case ACCUMULATE_LAST_ARG:
                argl = restoreReg();
                argl = new SCons(val, argl);
                fun = restoreReg();
                gotoLabel(Label.APPLY_DISPATCH);
                break;
                
                // function application dispatch
                
            case APPLY_DISPATCH:
                if (isPrimitiveProcedure(fun)) { 
                    gotoLabel(Label.PRIMITIVE_APPLY);
                }
                else if (isClosure(fun)) {
                    gotoLabel(Label.CLOSURE_APPLY);
                }
                else {
                    addError("app: expected procedure as first argument, but got %1", fun);
                    finish();
                }
                break;
                
            case PRIMITIVE_APPLY:
                SPrimitive prim = (SPrimitive)fun;
                if (prim.getPrimitive() == ControlPrimitives.apply) {
                    gotoLabel(Label.EVAL_APPLY);
                    break;
                }
                else if (prim.getPrimitive() == ControlPrimitives.map) {
                    gotoLabel(Label.EVAL_MAP);
                    break;
                }
                else {
                    val = applyPrimitiveProcedure(prim, argl);
                    cont = restoreLabel();
                    gotoLabel(cont);
                    break;
                }
                
            case CLOSURE_APPLY:
                env = makeBindings(fun, argl);
                unev = getProcedureBody(fun);
                gotoLabel(Label.EVAL_SEQUENCE);
                break;
                
                // sequence evaluation and tail recursion

            case EVAL_SEQUENCE:
                exp = getFirstExp(unev);
                if (isLastExp(unev)) { 
                    gotoLabel(Label.EVAL_SEQUENCE_LAST_EXP); 
                }
                else {
                    saveReg(unev);
                    saveReg(env);
                    cont = Label.EVAL_SEQUENCE_CONT;
                    gotoLabel(Label.EVAL_DISPATCH);
                }
                break;

            case EVAL_SEQUENCE_CONT:
                env = (Env)restoreReg();
                unev = restoreReg();
                unev = getRestExps(unev);
                gotoLabel(Label.EVAL_SEQUENCE);
                break;
              
            case EVAL_SEQUENCE_LAST_EXP:
                cont = restoreLabel();
                gotoLabel(Label.EVAL_DISPATCH);
                break;
                
            case EVAL_MAP:
                if (argl.getLength() != 2) {
                    addError("map: expected number of arguments %1, but got %2", 2, argl.getLength());
                    finish();
                    break;
                }
                else if (!car(argl).isList()) {
                    addError("map: second argument must be a list, but got %1", car(argl));
                    finish();
                    break;
                }
                argl = reverseList(argl, SCHEME_NULL);
                fun = car(argl);
                argl = car(cdr(argl));
                if (argl.type() == SType.NULL) {
                    gotoLabel(Label.EVAL_MAP_END);
                    break;
                }
                else {
                    val = SCHEME_NULL;
                    saveReg(val);
                    saveReg(fun);
                    saveReg(argl);
                    argl = cons(car(argl), SCHEME_NULL);
                }

            case EVAL_MAP_LOOP:
                saveLabel(Label.EVAL_MAP_NEXT);
                gotoLabel(Label.APPLY_DISPATCH);
                break;
                
            case EVAL_MAP_NEXT:
                argl = cdr(restoreReg());
                fun = restoreReg();
                val = cons(val, restoreReg());
                if (argl.type() == SType.CONS) {
                    saveReg(val);
                    saveReg(fun);
                    saveReg(argl);
                    argl = cons(car(argl), SCHEME_NULL);
                    gotoLabel(Label.EVAL_MAP_LOOP);
                    break;                    
                }
                
            case EVAL_MAP_END:
                val = reverseList(val, SCHEME_NULL);
                cont = restoreLabel();
                gotoLabel(cont);
                break;
                
            case EVAL_APPLY:
                if (argl.getLength() != 2) {
                    addError("apply: expected number of arguments %1, but got %2", 2, argl.getLength());
                    finish();
                    break;
                }
                else if (!car(argl).isList()) {
                    addError("apply: second argument must be a list, but got %1", car(argl));
                    finish();
                    break;
                }
                argl = reverseList(argl, SCHEME_NULL);
                fun = car(argl);
                argl = car(cdr(argl));
                gotoLabel(Label.APPLY_DISPATCH);
                break;
                
                // and sequence

            case EVAL_AND:
              saveLabel(cont);
              unev = cdr(exp);
              if (unev.type() == SType.NULL) {
                  exp = SBoolean.TRUE;
                  gotoLabel(Label.EVAL_AND_LAST_EXP);
                  break;
              }
              
            case EVAL_AND_SEQ:
              exp = car(unev);
              if (cdr(unev).type() == SType.NULL) {
                  gotoLabel(Label.EVAL_AND_LAST_EXP);
                  break;
              }
              else {
                  saveReg(unev);
                  saveReg(env);      
                  cont = Label.EVAL_AND_SEQ_CONT;
                  gotoLabel(Label.EVAL_DISPATCH);
                  break;
              }
              
            case EVAL_AND_SEQ_CONT:
              env = (Env)restoreReg();
              unev = restoreReg();
              if (val != SBoolean.TRUE) {
                  gotoLabel(Label.EVAL_AND_LAST_EXP);
                  break;
              }
              else {
                  unev = cdr(unev);
                  gotoLabel(Label.EVAL_AND_SEQ);
                  break;
              }
              
            case EVAL_AND_LAST_EXP:
              cont = restoreLabel();
              gotoLabel(Label.EVAL_DISPATCH);
              break;

              // or sequence

            case EVAL_OR:
              saveLabel(cont);
              unev = cdr(exp);
              if (unev.type() == SType.NULL) {
                  exp = SBoolean.FALSE;
                  gotoLabel(Label.EVAL_OR_LAST_EXP);
                  break;
              }
              
            case EVAL_OR_SEQ:
              exp = car(unev);
              if (cdr(unev).type() == SType.NULL) {
                  gotoLabel(Label.EVAL_OR_LAST_EXP);
                  break;
              }
              else {
                  saveReg(unev);
                  saveReg(env);      
                  cont = Label.EVAL_OR_SEQ_CONT;
                  gotoLabel(Label.EVAL_DISPATCH);
                  break;
              }
              
            case EVAL_OR_SEQ_CONT:
              env = (Env)restoreReg();
              unev = restoreReg();
              if (val == SBoolean.TRUE) {
                  gotoLabel(Label.EVAL_OR_LAST_EXP);
                  break;
              }
              else {
                  unev = cdr(unev);
                  gotoLabel(Label.EVAL_OR_SEQ);
                  break;
              }
              
            case EVAL_OR_LAST_EXP:
              cont = restoreLabel();
              gotoLabel(Label.EVAL_DISPATCH);
              break;
              
              // cond conditional
                
            case EVAL_COND:
                saveLabel(cont);
                cont = Label.COND_DECIDE;
                unev = getClauses(exp);

            case EVAL_COND_PRED:
                if (hasNoClauses(unev)) { gotoLabel(Label.EVAL_COND_RETURN_NIL); break;}
                exp = getFirstClause(unev);
                if (isElseClause(exp)) { gotoLabel(Label.EVAL_COND_ELSE_CLAUSE); break; }
                saveReg(env);
                saveReg(unev);
                exp = getPredicate(exp);
                gotoLabel(Label.EVAL_DISPATCH);
                break;

            case EVAL_COND_RETURN_NIL:
                cont = restoreLabel();
                val = SCHEME_NULL;
                gotoLabel(cont);
                break;

            case COND_DECIDE:
                unev = restoreReg();
                env = (Env)restoreReg();
                if (isTrue(val)) {
                    gotoLabel(Label.EVAL_COND_TRUE_PREDICATE);
                }
                else {
                    unev = getRestClauses(unev);
                    gotoLabel(Label.EVAL_COND_PRED);
                }
                break;

              case EVAL_COND_TRUE_PREDICATE:
                exp = getFirstClause(unev);

              case EVAL_COND_ELSE_CLAUSE:
                unev = getActions(exp);
                gotoLabel(Label.EVAL_SEQUENCE);
                break;

                // if conditional

              case EVAL_IF:
                unev = getIfTestAndAlt(exp);
                exp = getIfTest(unev);
                saveReg(env);
                saveReg(unev);
                saveLabel(cont);
                cont = Label.EVAL_IF_TEST;
                gotoLabel(Label.EVAL_DISPATCH);
                break;

              case EVAL_IF_TEST:
                cont = restoreLabel();
                unev = restoreReg();
                env = (Env)restoreReg();
                if (isTrue(val)) {
                    gotoLabel(Label.EVAL_IF_TRUE_PRED);
                }
                else {
                    exp = getIfElsePart(unev);
                    gotoLabel(Label.EVAL_DISPATCH);
                }
                break;

              case EVAL_IF_TRUE_PRED:
                exp = getIfTruePart(unev);
                gotoLabel(Label.EVAL_DISPATCH);
                break;

                // assignments and definitions

              case EVAL_SET:
                unev = setGetVariable(exp);
                saveReg(unev);
                exp = setGetValue(exp);
                saveReg(env);
                saveLabel(cont);
                cont = Label.EVAL_SET_A;
                gotoLabel(Label.EVAL_DISPATCH);
                break;

              case EVAL_SET_A:
                cont = restoreLabel();
                env = (Env)restoreReg();
                unev = restoreReg();
                val = setVariableValue(unev, val, env);
                gotoLabel(cont);
                break;
                
              case EVAL_DEFINE:
                unev = defineGetVariable(exp);
                saveReg(unev);
                exp = defineGetValue(exp);
                saveReg(env);
                saveLabel(cont);
                cont = Label.EVAL_DEFINE_A;
                gotoLabel(Label.EVAL_DISPATCH);
                break;

              case EVAL_DEFINE_A:
                cont = restoreLabel();
                env = (Env)restoreReg();
                unev = restoreReg();
                val = defineVariableValue(unev, val, env);
                gotoLabel(cont);
                break;
                
            case EVAL_FINISH:
                finish();
                break;
            }
        }
        return val;
    }
    
    
    private Env   environment;
    private SExpr unev;
    private SExpr exp;
    private SExpr val;
    private SExpr fun;
    private SExpr argl;
    private Label cur_label;
    private Label cont;
    
    private boolean finished;
    private Stoppable runInfo;
    
    private List<Label>    labelStack = new LinkedList<Label>();
    private List<SExpr>    regStack = new LinkedList<SExpr>();
    private List<SOutPort> outPortStack = new LinkedList<SOutPort>();
    private List<SInPort>  inPortStack = new LinkedList<SInPort>();
    private List<String>   errors = new LinkedList<String>();

    private Parser parser = new Parser();
    
    private static enum Label {
        EVAL_DISPATCH,
        EVAL_SELF,
        EVAL_FINISH,
        EVAL_VAR,
        EVAL_APP,
        EVAL_ARGS,
        EVAL_ARG_LOOP,
        EVAL_LAST_ARG,
        APPLY_DISPATCH,
        ACCUMULATE_LAST_ARG,
        ACCUMULATE_ARG,
        CLOSURE_APPLY,
        PRIMITIVE_APPLY,
        EVAL_SEQUENCE,
        EVAL_QUOTE,
        EVAL_COND,
        EVAL_IF,
        EVAL_DEFINE,
        EVAL_SET,
        EVAL_LAMBDA,
        EVAL_LET,
        EVAL_LET_STAR,
        COND_DECIDE,
        EVAL_DEFINE_A,
        EVAL_SET_A,
        EVAL_IF_TRUE_PRED,
        EVAL_COND_RETURN_NIL,
        EVAL_IF_TEST,
        EVAL_COND_TRUE_PREDICATE,
        EVAL_COND_PRED,
        EVAL_COND_ELSE_CLAUSE,
        EVAL_SEQUENCE_LAST_EXP,
        EVAL_SEQUENCE_CONT,
        EVAL_AND,
        EVAL_AND_LAST_EXP,
        EVAL_AND_SEQ,                
        EVAL_OR_SEQ_CONT,
        EVAL_AND_SEQ_CONT,
        EVAL_OR,
        EVAL_OR_LAST_EXP,
        EVAL_OR_SEQ,
        EVAL_MAP,
        EVAL_APPLY,
        EVAL_MAP_END,
        EVAL_MAP_LOOP,
        EVAL_MAP_NEXT
    }
    
    
    private void finish() {
        finished = true;
    }
    

    /**
     * Removes all errors that occurred during the last
     * evaluation.
     */
    public final void clearErrors() {
        errors.clear();
    }
    
    
    /**
     * Returns the list of errors that occurred during the
     * last evaluation.
     */
    public final List<String> getErrors() {
        return errors;
    }
    
    
    /**
     * Adds a new error message.
     */
    public final void addError(String msg, Object ... objects) {
        errors.add(TextUtils.replaceStrings(msg, objects));
    }
    

    /**
     * Returns true iff the last evaluation had errors.
     */
    public final boolean hasErrors() {
        return errors.size() > 0;
    }
    

    /**
     * Returns the current output port.
     */
    public SOutPort getCurrentOutputPort() {
        return outPortStack.get(0);
    }
    
    
    /**
     * Returns the current input port.
     */
    public SInPort getCurrentInputPort() {
        return inPortStack.get(0);
    }
    
    
    private boolean isSelfEval(SExpr sexpr) {
        return !(sexpr.type() == SType.CONS) && !(sexpr.type() == SType.SYMBOL);
    }
    
    
    private boolean isVar(SExpr sexpr) {
        return sexpr.type() == SType.SYMBOL;
    }
    
    
    private SExpr getOperands(SExpr sexpr) {
        return cdr(sexpr);
    }
    
    
    private SExpr getOperator(SExpr sexpr) {
        return car(sexpr);
    }
    

    private SExpr getFirstOperand(SExpr sexpr) {
        return car(sexpr);
    }
    
    
    private SExpr getRestOperands(SExpr sexpr) {
        return cdr(sexpr);
    }
    
    
    private boolean isLastOperand(SExpr sexpr) {
        return cdr(sexpr).type() == SType.NULL;
    }
    
    
    private SClosure makeClosure(SExpr sexpr, Env env) {
        if (sexpr.type() == SType.CONS) {
            SExpr args = car(sexpr);
            if (cdr(sexpr).type() == SType.CONS) {
                SExpr body = cdr(sexpr);
                return new SClosure(args, body, env);
            }
        }
        addError("lambda: malformed expression");
        finish();
        return null;
    }
    
    
    private SExpr letToLambda(SExpr sexpr) {
        try {
            SExpr variables = car(cdr(sexpr));
            SExpr body = cdr(cdr(sexpr));
            SExpr new_vars = SCHEME_NULL;
            SExpr new_pars = SCHEME_NULL;
    
            while (!(variables.type() == SType.NULL)) {
              new_vars = cons(car(car(variables)), new_vars);
              new_pars = cons(car(cdr(car(variables))), new_pars);
              variables = cdr(variables);
            }

            sexpr.setCar(cons(Token.LAMBDA, cons(new_vars, body)));
            sexpr.setCdr(new_pars);
            return sexpr;
        }
        catch (NullPointerException e) {
            addError("let: malformed expression");
            finish();
            return SCHEME_VOID;
        }
    }
    

    private SExpr letStarToLet(SExpr sexpr) {
        try {
            SExpr variables = car(cdr(sexpr));
            SExpr body = cdr(cdr(sexpr));
            SExpr new_let_star;

            if (variables.type() == SType.NULL) {
                sexpr.setCar(cons(Token.LAMBDA, cons(SCHEME_NULL, body)));
                sexpr.setCdr(SCHEME_NULL);
                return sexpr;
            }
            if (cdr(variables).type() == SType.NULL) {
                sexpr.setCar(Token.LET);
                return sexpr;
            }
            new_let_star = cons(Token.LET_STAR, cons(cdr(variables), body));
            sexpr.setCar(Token.LET);
            sexpr.setCdr(cons(cons(car(variables), SCHEME_NULL), cons(new_let_star, SCHEME_NULL)));
            return sexpr;
        }
        catch (NullPointerException e) {
            addError("let*: malformed expression");
            finish();
            return SCHEME_VOID;
        }
    }

    
    private SExpr getClauses(SExpr sexpr) {
        return cdr(sexpr);
    }
    
    
    private boolean hasNoClauses(SExpr clauses) {
        return clauses.type() == SType.NULL;
    }
    
    
    private SExpr getIfTestAndAlt(SExpr sexpr) {
        return cdr(sexpr); 
    }
    
    
    private SExpr getIfTest(SExpr sexpr) {
        if (sexpr.type() == SType.NULL) {
            addError("if: missing test expression");
            finish();
            return SCHEME_VOID;
        }
        else {
            return car(sexpr);
        }
    }
    
    
    private SExpr getIfTruePart(SExpr sexpr) {
        if (cdr(sexpr).type() == SType.NULL) {
            addError("if: missing true part");
            finish();
            return SCHEME_VOID;
        }
        else {
            return car(cdr(sexpr));
        }
    }
    
    
    private SExpr getActions(SExpr clause) {
        return cdr(clause);
    }
    
    
    private SExpr setGetVariable(SExpr sexpr) {
        if (cdr(sexpr).type() == SType.NULL || cdr(cdr(sexpr)).type() == SType.NULL) {
            addError ("set!: wrong number of arguments");
            finish();
            return SCHEME_VOID;
        }
        else {  
            return car(cdr(sexpr));
        }
    }
    
    
    private SExpr setGetValue(SExpr sexpr) {
        return car(cdr(cdr(sexpr)));
    }
    
    
    private SExpr setVariableValue(SExpr var, SExpr val, Env env) {
        if (!env.set((Symbol)var, val)) {
            addError("set!: unbound variable %%1", var);
            finish();
        }
        return SCHEME_VOID;
    }
    
    
    private SExpr defineGetVariable(SExpr sexpr) {
        if (isVar(car(cdr(sexpr)))) {
            // variable definition
            return car(cdr(sexpr));
        }
        else if (car(cdr(sexpr)).type() == SType.CONS) {
            // procedure definition
            return car(car(cdr(sexpr)));
        }
        else {
            addError("define: no variable in define expression");
            finish();
            return SCHEME_VOID;
        }
    }
    
    
    private SExpr defineGetValue(SExpr sexpr) {
        if (isVar(car(cdr(sexpr)))) {
            // variable
            return car(cdr(cdr(sexpr)));
        }
        else {
            // procedure
            return cons(Token.LAMBDA, cons(cdr(car(cdr(sexpr))), cdr(cdr(sexpr))));
        }
    }
    
    
    private SExpr defineVariableValue(SExpr variable, SExpr value, Env env) {
        env.put((Symbol)variable, value);
        return SCHEME_VOID;
    }
    
    
    private boolean isTrue(SExpr expr) {
        return expr == SBoolean.TRUE;
    }
    
    
    private boolean isElseClause(SExpr clause) {
        return getPredicate(clause) == Token.ELSE;
    }

    
    private SExpr getPredicate(SExpr clause) {
        return car(clause);
    }
    
    
    private SExpr getFirstClause(SExpr clauses) {
        return car(clauses);
    }
    

    private SExpr getRestClauses(SExpr clauses) {
        return cdr(clauses);
    }
    

    private SExpr getIfElsePart(SExpr sexpr) {
        if (cdr(sexpr).type() == SType.NULL) {
            addError("if: missing else part");
            finish();
            return SCHEME_VOID;            
        }
        else if (cdr(cdr(sexpr)).type() == SType.NULL) {
            return SCHEME_VOID;
        }
        else {
            return car(cdr(cdr(sexpr)));
        }
    }
    

    private SExpr getFirstExp(SExpr sexpr) {
        return car(sexpr);
    }
    
    
    private SExpr getRestExps(SExpr sexpr) {
        return cdr(sexpr);
    }
    
    
    private boolean isLastExp(SExpr sexpr) {
        return cdr(sexpr).type() == SType.NULL;
    }
    
    
    private boolean isPrimitiveProcedure(SExpr sexpr) {
        return sexpr.type() == SType.PRIMITIVE;
    }
    

    private boolean isClosure(SExpr sexpr) {
        return sexpr.type() == SType.CLOJURE;
    }
    
    
    private SExpr applyPrimitiveProcedure(SPrimitive prim, SExpr args) {
        SExpr sexpr = reverseList(args, SCHEME_NULL);
        if (sexpr == null) {
            return SCHEME_NULL;
        }
        sexpr = prim.getPrimitive().call(sexpr, this);
        if (hasErrors()) {
            finish();
            return SCHEME_NULL;
        }
        else if (sexpr == null) {
            addError("app: can not apply %1 to %2", prim, args);
            finish();
            return SCHEME_NULL;
        }
        else {
            return sexpr;
        }
    }

    
    private SExpr reverseList(SExpr sexpr, SExpr acc) {
        if (sexpr.type() == SType.NULL) {
            return acc;
        }
        else if (sexpr.type() == SType.CONS) {
            return reverseList(cdr(sexpr), cons(car(sexpr), acc));
        }
        else {
            return null;
        }
    }
    
    private Env makeBindings(SExpr funExpr, SExpr args) {
        SClosure closure = (SClosure)funExpr;
        SExpr sargs = reverseList(args, SCHEME_NULL);
        Env newEnv = new Env(closure.getEnv());
        if (!newEnv.extendEnv(closure.getArgs(), sargs)) {
            addError("app: cannot bind variables %1 to values %2", closure.getArgs(), sargs);
            finish();
            return null;
        }
        else {
            return newEnv;
        }
    }
    
    
    private SExpr getProcedureBody(SExpr funExpr) {
        SClosure closure = (SClosure)funExpr;
        return closure.getBody();
    }

    
    private void saveLabel(Label label) {
        labelStack.add(0, label);
    }
    
    
    private Label restoreLabel() {
        return labelStack.remove(0);
    }
    
    
    private void saveReg(SExpr sexpr) {
        regStack.add(0, sexpr);
    }
    
    
    private SExpr restoreReg() {
        return regStack.remove(0);
    }
    
    private void gotoLabel(Label label) {
        cur_label = label;
    }
}
