/*
 * Copyright (C) 2005 Gérard Milmeister
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

package org.rubato.rubettes.builtin;

import java.util.HashMap;

import org.vetronauta.latrunculus.core.util.TextUtils;

/**
 * Parses an expression of real arithmetic including boolean expressions.
 * 
 * @author Gérard Milmeister
 */
public class ArithCompiler {

    /**
     * Creates a compiler with the given string as input.
     * The result type of the expression is real.
     */
    public ArithCompiler(String expressionString) {
        this(expressionString, true);
    }


    /**
     * Creates a compiler with the given string as input.
     * The boolean parameter indicates whether the result type
     * is real or boolean.
     */
    public ArithCompiler(String expressionString, boolean isResultReal) {
        this.expressionString = expressionString;
        this.nextPos = 0;
        this.isResultReal = isResultReal;
    }

    
    /**
     * Creates a compiler.
     */
    public ArithCompiler() {
        nextPos = 0;
    }
    

    /**
     * Sets the expression string for the compiler.
     * The result type is real.
     */
    public void setExpression(String s) {
        setExpression(s, true);
    }
    

    /**
     * Sets the expression string for the compiler.
     * The boolean parameter indicates whether the result type
     * is real or boolean.
     */
    public void setExpression(String s, boolean isResultReal) {
        this.expressionString = s;
        this.nextPos = 0;
        this.isResultReal = isResultReal;
    }
    

    /**
     * Returns the virtual machine with the code compiled from
     * the expression.
     */
    public ArithVM getVM() {
        vm.finish();
        return vm;
    }
           
    
    //
    // parser
    //

    /**
     * Parses the expression string, assuming there are no
     * arguments.
     */
    public boolean parse() {
        return parse(-1);
    }
    
    
    /**
     * Parses the expression string, assuming there are
     * <code>nArgs</code> arguments.
     */
    public boolean parse(int nrArgs) {
        vm.start();
        error = null;
        nextToken(currentToken);
        
        if (currentToken.isEOS()) {
            setError(BuiltinMessages.getString("ArithCompiler.emptyexpression"));
            return false;
        }

        boolean ok;
        if (isResultReal) {
            ok = parseExpr();
        }
        else {
            ok = parseBooleanExpr();
        }
        if (!ok) {
            return false;
        }
        else if (!currentToken.isEOS()) {
            setError(BuiltinMessages.getString("ArithCompiler.extraneoustokens"));
            return false;
        }
        else if (nrArgs >= 0 && vm.getNrArgs() > nrArgs){
            setError(BuiltinMessages.getString("ArithCompiler.toomanyparams"));
            return false;
        }
        else {
            return true;
        }
    }
    
    
    private void setError(String s) {
        error = s;
    }
    
    
    private void setError(String s, Object ... objects) {
        error = TextUtils.replaceStrings(s, objects);
    }
    

    /**
     * Returns the error if any.
     */
    public String getError() {
        return error;
    }
    

    /**
     * Returns true iff the compilation failed.
     */
    public boolean hasError() {
        return error != null;
    }
    
    
    private boolean parseExpr() {
        if (!parseTerm()) {
            return false;
        }
        while (!currentToken.isEOS() && 
                (currentToken.type == Token.PLUS ||
                 currentToken.type == Token.MINUS)) {
            int op = (currentToken.type == Token.PLUS)?ArithVM.PLUS:ArithVM.MINUS;
            nextToken(currentToken);
            if (currentToken.isEOS()) {
                setError(BuiltinMessages.getString("ArithCompiler.prematureending"));
                return false;
            }
            if (!parseTerm()) {
                setError(BuiltinMessages.getString("ArithCompiler.syntaxerror"), nextPos);
                return false;
            }
            vm.compileOp(op);
        }
        return true;
    }

    
    private boolean parseTerm() {
        if (!parseFactor()) {
            return false;
        }
        while (!currentToken.isEOS() && 
                (currentToken.type == Token.TIMES ||
                 currentToken.type == Token.DIV)) {
            int op = (currentToken.type == Token.TIMES)?ArithVM.TIMES:ArithVM.DIV;
            nextToken(currentToken);
            if (currentToken.isEOS()) {
                setError(BuiltinMessages.getString("ArithCompiler.prematureending"));
                return false;
            }
            if (!parseFactor()) {
                setError(BuiltinMessages.getString("ArithCompiler.syntaxerror")+nextPos);
                return false;
            }
            vm.compileOp(op);
        }
        return true;
    }
    
    
    private boolean parseFactor() {
        if (currentToken.type == Token.MINUS) {
            nextToken(currentToken);
            if (!parseFactor()) {
                return false;
            }
            vm.compileUnaryMinus();
        }
        else if (currentToken.type == Token.NUMBER) {
            vm.compileNumber(currentToken.value);
            nextToken(currentToken);
        }
        else if (currentToken.type == Token.LPAREN) {
            nextToken(currentToken);
            if (currentToken.isEOS()) {
                setError(BuiltinMessages.getString("ArithCompiler.prematureending"));
                return false;
            }
            if (!parseExpr()) {
                setError(BuiltinMessages.getString("ArithCompiler.syntaxerror"), nextPos);
                return false;
            }
            if (currentToken.isEOS() || currentToken.type != Token.RPAREN) {
                setError(BuiltinMessages.getString("ArithCompiler.prematureending"));
                return false;
            }
            nextToken(currentToken);
        }
        else if (currentToken.type == Token.PARAM) {
            vm.compileParam(currentToken.param);
            nextToken(currentToken);
        }
        else if (currentToken.type == Token.ID) {
            int nrArgs = 0;
            String id = currentToken.id;
            nextToken(currentToken);
            if (currentToken.type == Token.LPAREN) {
                nextToken(currentToken);
                if (currentToken.type == Token.RPAREN) {
                    if (!vm.compileId(id, nrArgs)) {
                        setError(BuiltinMessages.getString("ArithCompiler.funapperror"), nextPos);
                        return false;                            
                    }
                    nextToken(currentToken);                    
                }
                else if (parseExpr()) {
                    nrArgs++;
                    while (currentToken.type == Token.COMMA) {
                        nextToken(currentToken);
                        if (!parseExpr()) {
                            setError(BuiltinMessages.getString("ArithCompiler.syntaxerror"), nextPos);
                            return false;
                        }
                        nrArgs++;
                    }
                    if (currentToken.type == Token.RPAREN) {
                        nextToken(currentToken);
                        if (!vm.compileId(id, nrArgs)) {
                            setError(BuiltinMessages.getString("ArithCompiler.funapperror"), nextPos);
                            return false;                            
                        }
                    }
                    else {
                        setError(BuiltinMessages.getString("ArithCompiler.syntaxerror"), nextPos);
                        return false;
                    }
                }
            }
        }
        else if (currentToken.type == Token.IF) {
            nextToken(currentToken);
            if (!parseBooleanExpr()) {
                return false;
            }
            int ifpos = vm.compileIf();
            if (currentToken.type != Token.THEN) {
                setError(BuiltinMessages.getString("ArithCompiler.missingthen"), nextPos);
                return false;                
            }
            nextToken(currentToken);
            if (!parseExpr()) {
                return false;
            }
            int jmppos = vm.compileThen(ifpos);
            if (currentToken.type != Token.ELSE) {
                setError(BuiltinMessages.getString("ArithCompiler.missingelse"), nextPos);
                return false;
            }
            nextToken(currentToken);
            if (!parseExpr()) {
                return false;
            }
            vm.compileElse(jmppos);            
        }
        
        if (currentToken.type == Token.EXP) {
            nextToken(currentToken);
            if (!parseFactor()) {
                return false;
            }
            vm.compileOp(ArithVM.EXP);
            return true;            
        }
        
        return true;
    }
    
    
    private boolean parseBooleanExpr() {
        if (!parseBooleanTerm()) {
            return false;
        }
        while (!currentToken.isEOS() && currentToken.type == Token.OR) {
            nextToken(currentToken);
            if (currentToken.isEOS()) {
                setError(BuiltinMessages.getString("ArithCompiler.prematureending"));
                return false;
            }
            if (!parseBooleanTerm()) {
                setError(BuiltinMessages.getString("ArithCompiler.syntaxerror"), nextPos);
                return false;
            }
            vm.compileOp(ArithVM.OR);
        }
        return true;
    }
    
    
    private boolean parseBooleanTerm() {
        if (!parseBooleanFactor()) {
            return false;
        }
        while (!currentToken.isEOS() && currentToken.type == Token.AND) {
            nextToken(currentToken);
            if (currentToken.isEOS()) {
                setError(BuiltinMessages.getString("ArithCompiler.prematureending"));
                return false;
            }
            if (!parseBooleanFactor()) {
                setError(BuiltinMessages.getString("ArithCompiler.syntaxerror"), nextPos);
                return false;
            }
            vm.compileOp(ArithVM.AND);
        }
        return true;
    }

    
    private boolean parseBooleanFactor() {
        if (currentToken.type == Token.NOT) {
            nextToken(currentToken);
            if (!parseBooleanFactor()) {
                return false;
            }
            vm.compileNot();
            return true;
        }
        else if (currentToken.type == Token.TRUE) {
            vm.compileTrue();
            nextToken(currentToken);
            return true;
        }
        else if (currentToken.type == Token.FALSE) {
            vm.compileFalse();
            nextToken(currentToken);
            return true;
        }
        else if (currentToken.type == Token.LPAREN) {
            nextToken(currentToken);
            if (currentToken.isEOS()) {
                setError(BuiltinMessages.getString("ArithCompiler.prematureending"));
                return false;
            }
            if (!parseBooleanExpr()) {
                setError(BuiltinMessages.getString("ArithCompiler.syntaxerror"), nextPos);
                return false;
            }
            if (currentToken.isEOS() || currentToken.type != Token.RPAREN) {
                setError(BuiltinMessages.getString("ArithCompiler.prematureending"));
                return false;
            }
            nextToken(currentToken);
            return true;
        }
        else {
            return parseComparison();
        }
    }

    
    private boolean parseComparison() {
        if (!parseExpr()) {
            return false;
        }
        int op;
        switch (currentToken.type) {
            case Token.LE: op = ArithVM.LE; break;
            case Token.LT: op = ArithVM.LT; break;
            case Token.GE: op = ArithVM.GE; break;
            case Token.GT: op = ArithVM.GT; break;
            case Token.EQUALS: op = ArithVM.EQUALS; break;
            case Token.NEQ: op = ArithVM.NEQ; break;
            default: {
                setError(BuiltinMessages.getString("ArithCompiler.unknowncompop"), nextPos);
                return false;
            }
        }
        nextToken(currentToken);
        if (!parseExpr()) {
            return false;
        }
        vm.compileOp(op);
        return true;
    }

    
    private Token currentToken = new Token();
    
    
    //
    // scanner
    //
    
    private void nextToken(Token token) {
        token.type  = 0;
        token.value = 0;
        token.param = 0;
        token.start = nextPos;
        token.id = null;
        // check for end of string
        if (nextPos < expressionString.length()) {
            int ch = getChar(nextPos);
            if (ch == '(') {
                token.type = Token.LPAREN; nextPos++;
            }
            else if (ch == ')') {
                token.type = Token.RPAREN; nextPos++;
            }
            else if (ch == '=') {
                token.type = Token.EQUALS; nextPos++;
            }
            else if (ch == '-') {
                token.type = Token.MINUS; nextPos++;
            }
            else if (ch == '^') {
                token.type = Token.EXP; nextPos++;
            }
            else if (ch == '+') {
                token.type = Token.PLUS; nextPos++;
            }
            else if (ch == '*') {
                token.type = Token.TIMES; nextPos++;
            }
            else if (ch == '/') {
                token.type = Token.DIV; nextPos++;
            }
            else if (ch == '&') {
                token.type = Token.AND; nextPos++;
            }
            else if (ch == '|') {
                token.type = Token.OR; nextPos++;
            }
            else if (ch == ',') {
                token.type = Token.COMMA; nextPos++;
            }
            else if (Character.isWhitespace(ch)) {
                consumeSpaces();
                nextToken(token);
            }
            else if (ch == '<' || ch == '>' || ch == '!') {
                opToken(token);
            }
            else if (ch == '#') {
                paramToken(token);
            }
            else if (Character.isDigit(ch)) {
                numToken(token);
            }
            else if (Character.isLetter(ch)) {
                idToken(token);                
            }
            else {
                token.type = Token.EOS;
            }
        }
        else {
            token.type = Token.EOS;
        }
    }

    
    private void opToken(Token token) {
        char ch = getChar(nextPos);
        if (ch == '<') {
            if (getChar(nextPos+1) == '=') {
                token.type = Token.LE;
                nextPos++;
            }
            else {
                token.type = Token.LT;
            }
        }
        else if (ch == '>') {
            if (getChar(nextPos+1) == '=') {
                token.type = Token.GE;
                nextPos++;
            }
            else {
                token.type = Token.GT;
            }            
        }
        else if (ch == '!') {
            if (getChar(nextPos+1) == '=') {
                token.type = Token.NEQ;
                nextPos++;
            }
            else {
                token.type = Token.NOT;
            }            
        }
        nextPos++;
    }
    
    
    private void numToken(Token token) {
        char ch = getChar(nextPos);
        double number = 0;
        double frac = 0;
        while (nextPos < expressionString.length() && Character.isDigit(ch)) {
            number = number*10+(ch-'0');
            nextPos++;
            ch = getChar(nextPos);
        }
        if (ch == '.') {
            nextPos++;
            ch = getChar(nextPos);
            int exp = 10;
            while (nextPos < expressionString.length() && Character.isDigit(ch)) {
                frac = frac+((double)(ch-'0'))/exp;
                exp *= 10;
                nextPos++;
                ch = getChar(nextPos);
            }
        }
        token.type = Token.NUMBER;
        token.value = number+frac;
    }
    
    
    private void paramToken(Token token) {
        if (Character.isDigit(getChar(nextPos+1))) {
            token.type = Token.PARAM;
            token.param = getChar(nextPos+1)-'0';
            nextPos += 2;
        }
        else {
            token.type = Token.ERROR;
        }
    }
    
    
    private void idToken(Token token) {
        StringBuilder buf = new StringBuilder(30);
        char ch = getChar(nextPos);
        while (nextPos < expressionString.length() && Character.isLetter(ch)) {
            buf.append(ch);
            nextPos++;
            ch = getChar(nextPos);
        }
        lookupKeyword(buf.toString(), token);
    }
    
    
    private char getChar(int pos) {
        char ch;
        if (pos < expressionString.length()) {
            ch = expressionString.charAt(pos);
        }
        else {
            ch = '\0';
        }
        return ch;
    }
    
    
    private void consumeSpaces() {
        while (nextPos < expressionString.length() && 
               (expressionString.charAt(nextPos) == ' ' ||
                expressionString.charAt(nextPos) == '\t' ||
                expressionString.charAt(nextPos) == '\n')) {
            nextPos++;
        }
    }
    
    
    private static class Token {
        int    type;
        double value;
        int    param;
        int    start;
        String id;
        
        /**
         * Creates an empty token.
         */
        public Token() { type = EMPTY; }
        
        /**
         * Returns true iff the token is the end of string.
         */
        public boolean isEOS() {
            return type == Token.EOS;
        }
        
        /**
         * Returns string representation.
         */
        @Override
        public String toString() {
            return "Token type="+type+
                   " value="+value+
                   " param="+param+
                   " start="+start+
                   " id="+id;
        }
        
        private static final int LPAREN = 100;
        private static final int RPAREN = 101;
        private static final int EQUALS = 102;
        private static final int LT     = 103;
        private static final int LE     = 104;
        private static final int GT     = 105;
        private static final int GE     = 106;
        private static final int PARAM  = 107;
        private static final int IF     = 108;
        private static final int THEN   = 109;
        private static final int ELSE   = 110;
        private static final int TIMES  = 111;
        private static final int PLUS   = 112;
        private static final int DIV    = 113;
        private static final int MINUS  = 114;
        private static final int NUMBER = 115;
        private static final int TRUE   = 116;
        private static final int FALSE  = 117;
        private static final int EXP    = 121;
        private static final int NOT    = 122;
        private static final int AND    = 123;
        private static final int OR     = 124;
        private static final int NEQ    = 125;
        private static final int ID     = 126;
        private static final int COMMA  = 127;
        private static final int EOS    = 999;
        private static final int ERROR  = 998;
        private static final int EMPTY  = 997;
    }
        
    
    private void lookupKeyword(String keyword, Token token) {
        Integer i = keywords.get(keyword);
        if (i == null) {
            token.type = Token.ID;
            token.id = keyword;
        }
        else {
            token.type = i;
        }
    }
    
    
    private String expressionString;
    
    private static HashMap<String,Integer> keywords = new HashMap<String,Integer>();    
    
    // position to read next token from
    private int    nextPos = 0;
    
    private String error = null;
    
    private boolean isResultReal = true;
    
    private ArithVM vm = new ArithVM();
    
    static {
        keywords.put("if",   Token.IF);
        keywords.put("then", Token.THEN);
        keywords.put("else", Token.ELSE);
        keywords.put("T",    Token.TRUE);
        keywords.put("F",    Token.FALSE);
    }
}
