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

package org.rubato.scheme;

import java.util.HashSet;

import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.server.parse.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ComplexWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.RationalWrapper;


/**
 * Class representing a syntactical Scheme object.
 * 
 * @author Gérard Milmeister
 */
public final class Token {

    public Token() {}
    
    
    /**
     * Creates a new token with the given <code>type</code>.
     */
    public Token(TokenType type) {
        this.type = type;
    }
    
    
    public String toString() {
        return type.toString();
    }
    
    
    public TokenType type;    
    public Symbol    symbol;
    public String    string;
    public int       i;
    public double    d;
    public RationalWrapper r;
    public ComplexWrapper c;
    public boolean   b;
    public char      chr;
    
    
    public static boolean isSymbolStartChar(char c) {
        return Character.isLetter(c) || symbolChars.contains(c);
    }
    
    
    public static boolean isSymbolChar(char c) {
        return Character.isLetterOrDigit(c) || symbolChars.contains(c);
    }
    
    
    public static Double toReal(String s) {
        try {
            Double d = Double.valueOf(s);
            return d;
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    
    public static Integer toInt(String s) {
        try {
            Integer i = Integer.valueOf(s);
            return i;
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
    
    
    public static RationalWrapper toRational(String s) {
        try {
            Rational q = ArithmeticParsingUtils.parseRational(s);
            return new RationalWrapper(q.getNumerator(), q.getDenominator());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
    
    
    public static ComplexWrapper toComplex(String s) {
        try {
            ComplexWrapper c = ArithmeticParsingUtils.parseComplex(s);
            return c;
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
    
    
    public static Token toNumberToken(String s) {
        Token token = new Token();
        token.type = TokenType.ERROR;
        
        Integer i = toInt(s);
        if (i != null) {
            token.type = TokenType.INTEGER;
            token.i = i.intValue();
            return token;
        }
        Double d = toReal(s);
        if (d != null) {
            token.type = TokenType.REAL;
            token.d = d.doubleValue();
            return token;
        }
        RationalWrapper r = toRational(s);
        if (r != null) {
            token.type = TokenType.RATIONAL;
            token.r = r;
            return token;
        }
        ComplexWrapper c = toComplex(s);
        if (c != null) {
            token.type = TokenType.COMPLEX;
            token.c = c;
            return token;
        }
        
        return token;
    }
    
    
    /**
     * This enumeration type classifies all Scheme tokens.
     * 
     * @author Gérard Milmeister
     */
    public static enum TokenType {
        LPAREN,
        RPAREN,
        INTEGER,
        REAL,
        RATIONAL,
        COMPLEX,
        SYMBOL,
        STRING,
        BOOLEAN,
        DOT,
        HASH,
        CHAR,
        SQUOTE,
        ARRAY,
        ERROR,
        EOF
    }
    
    
    public static final Symbol AND      = Symbol.make("and");
    public static final Symbol BEGIN    = Symbol.make("begin");
    public static final Symbol COND     = Symbol.make("cond");
    public static final Symbol CONS     = Symbol.make("cons");
    public static final Symbol DEFINE   = Symbol.make("define");
    public static final Symbol ELSE     = Symbol.make("else");
    public static final Symbol IF       = Symbol.make("if");
    public static final Symbol LAMBDA   = Symbol.make("lambda");
    public static final Symbol LET      = Symbol.make("let");
    public static final Symbol LET_STAR = Symbol.make("let*");
    public static final Symbol OR       = Symbol.make("or");
    public static final Symbol QUOTE    = Symbol.make("quote");
    public static final Symbol SET      = Symbol.make("set!");
    
    
    private static HashSet<Character> symbolChars = new HashSet<Character>();
    
    static {
        symbolChars.add('+');
        symbolChars.add('-');
        symbolChars.add('*');
        symbolChars.add('/');
        symbolChars.add('!');
        symbolChars.add('>');
        symbolChars.add('<');
        symbolChars.add('?');
        symbolChars.add('=');
        symbolChars.add('$');
        symbolChars.add('%');
        symbolChars.add('&');
        symbolChars.add('.');
        symbolChars.add(':');
        symbolChars.add('@');
        symbolChars.add('_');
        symbolChars.add('~');
    }
}
