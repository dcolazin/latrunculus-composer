package org.vetronauta.latrunculus.core.scheme;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.vetronauta.latrunculus.core.scheme.expression.SBoolean;
import org.vetronauta.latrunculus.core.scheme.expression.SChar;
import org.vetronauta.latrunculus.core.scheme.expression.SComplex;
import org.vetronauta.latrunculus.core.scheme.expression.SCons;
import org.vetronauta.latrunculus.core.scheme.expression.SDenotator;
import org.vetronauta.latrunculus.core.scheme.expression.SExpr;
import org.vetronauta.latrunculus.core.scheme.expression.SForm;
import org.vetronauta.latrunculus.core.scheme.expression.SInPort;
import org.vetronauta.latrunculus.core.scheme.expression.SInteger;
import org.vetronauta.latrunculus.core.scheme.expression.SOutPort;
import org.vetronauta.latrunculus.core.scheme.expression.SPrimitive;
import org.vetronauta.latrunculus.core.scheme.expression.SRational;
import org.vetronauta.latrunculus.core.scheme.expression.SReal;
import org.vetronauta.latrunculus.core.scheme.expression.SString;
import org.vetronauta.latrunculus.core.scheme.expression.SType;
import org.vetronauta.latrunculus.core.scheme.expression.SVector;
import org.vetronauta.latrunculus.core.scheme.expression.Symbol;

//TODO move to server?
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Display {

    public static String display(SExpr sExpr) {
        if (sExpr == null) {
            return null; //TODO is this correct? throw NPE/IllegalArgument?
        }
        switch (sExpr.type()) {
            case ENV:
                return "#<environment:" + sExpr.hashCode() + ">"; //TODO format
            case BOOLEAN:
                return "#" + (sExpr == SBoolean.TRUE ? "t" : "f");
            case CHAR:
                return displayChar((SChar) sExpr);
            case CLOJURE:
                return "#<closure:" + sExpr.hashCode() + ">";
            case COMPLEX:
                return ((SComplex) sExpr).getComplex().toString();
            case CONS:
                return displayCons((SCons) sExpr);
            case DENOTATOR:
                return "#<denotator:" + ((SDenotator) sExpr).getDenotator().toString() + ">";
            case FORM:
                return "#<form:" + ((SForm) sExpr).getForm().toString() + ">";
            case INPUT:
                return displayInput((SInPort) sExpr);
            case INTEGER:
                return Integer.toString(((SInteger) sExpr).getInt());
            case NULL:
                return "()";
            case OUTPUT:
                return displayOutput((SOutPort) sExpr);
            case PRIMITIVE:
                return "#<primitive:" + ((SPrimitive) sExpr).getPrimitive().getName() + ">";
            case RATIONAL:
                return ((SRational) sExpr).getRational().toString();
            case REAL:
                return Double.toString(((SReal) sExpr).getDouble());
            case STRING:
                return ((SString) sExpr).getString();
            case VECTOR:
                return displayVector((SVector) sExpr);
            case VOID:
                return StringUtils.EMPTY;
            case SYMBOL:
                return ((Symbol) sExpr).getName();
            default:
                throw new NotImplementedException(String.format("display not implemented for type %s class %s", sExpr.type(), sExpr.getClass()));
        }
    }

    private static String displayChar(SChar sChar) {
        char c = sChar.getChar();
        if (c == ' ') {
            return "#\\space";
        }
        else if (c == '\n') {
            return "#\\newline";
        }
        else {
            return "#\\" + c;
        }
    }

    private static String displayCons(SCons sCons) {
        if (!sCons.getCdr().isList()) {
            return "(" + display(sCons.getCar()) + " . " + display(sCons.getCdr()) + ")";
        }
        SCons cur_cons = sCons;
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        while (cur_cons != null) {
            buf.append(display(cur_cons.getCar()));
            if (cur_cons.getCdr().type() == SType.CONS) {
                buf.append(" ");
                cur_cons = (SCons)cur_cons.getCdr();
            } else if (cur_cons.getCdr().type() == SType.NULL) {
                cur_cons = null;
            } else {
                buf.append(" . ");
                buf.append(display(cur_cons.getCdr()));
                cur_cons = null;
            }
        }
        buf.append(")");
        return buf.toString();
    }

    private static String displayInput(SInPort sInPort) {
        return "#<input-port:" + ((sInPort.getPort() == System.in) ? "stdin" : sInPort.getPort().hashCode()) + ">";
    }

    private static String displayOutput(SOutPort sOutPort) {
        return "#<input-port:" + ((sOutPort.getPort() == System.out) ? "stdout" : sOutPort.getPort().hashCode()) + ">";
    }

    private static String displayVector(SVector sVector) {
        StringBuilder buf = new StringBuilder();
        buf.append("#(");
        SExpr[] v = sVector.getArray();
        if (v.length >= 1) {
            buf.append(display(v[0]));
            for (int i = 1; i < v.length; i++) {
                buf.append(" ");
                buf.append(display(v[i]));
            }
        }
        buf.append(")");
        return buf.toString();
    }

}
