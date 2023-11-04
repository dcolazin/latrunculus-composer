package org.vetronauta.latrunculus.server.parse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.IntegerWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.ComplexWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.ModulusWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.RationalWrapper;
import org.vetronauta.latrunculus.core.math.arith.number.RealWrapper;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;

import java.util.LinkedList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArithmeticParsingUtils {

    public static <N extends ArithmeticNumber<N>> N parse(ArithmeticRing<N> ring, String s) {
        return (N) parse(detectParsingClass(ring), s);
    }

    public static ArithmeticNumber<?> parse(String s) {
        return parse(detectParsingClass(s), s);
    }

    private static ArithmeticNumber<?> parse(Class<?> parsingClass, String s) {
        if (parsingClass == null) {
            throw new NumberFormatException(String.format("cannot detect parsing class for %s", s));
        }
        if (parsingClass.equals(Complex.class)) {
            //TODO after complete refactoring of ArithmeticNumber
            throw new UnsupportedOperationException("...");
            //return parseComplex(s);
        }
        if (parsingClass.equals(ZInteger.class)) {
            //TODO after complete refactoring of ArithmeticNumber
            throw new UnsupportedOperationException("...");
            //return parseArithmeticInteger(s);
        }
        if (parsingClass.equals(Rational.class)) {
            //TODO after complete refactoring of ArithmeticNumber
            throw new UnsupportedOperationException("...");
            //return parseRational(s);
        }
        if (parsingClass.equals(Real.class)) {
            //TODO after complete refactoring of ArithmeticNumber
            throw new UnsupportedOperationException("...");
            //return parseArithmeticDouble(s);
        }
        throw new NumberFormatException(String.format("parsing class %s is not supported", parsingClass));
    }

    private static Class<?> detectParsingClass(String s) {
        return null; //TODO
    }

    private static Class<?> detectParsingClass(Ring<?> ring) {
        if (ring instanceof ZRing) {
            return IntegerWrapper.class;
        }
        if (ring instanceof ZnRing) {
            return ModulusWrapper.class;
        }
        if (ring instanceof QRing) {
            return RationalWrapper.class;
        }
        if (ring instanceof RRing) {
            return RealWrapper.class;
        }
        if (ring instanceof CRing) {
            return ComplexWrapper.class;
        }
        return null;
    }

    /**
     * Returns the complex number with the string representation <code>s</code>
     */
    public static ComplexWrapper parseComplex(String s) {
        if (s.length() == 0) {
            throw new NumberFormatException("Empty string makes no complex");
        }
        else if (s.charAt(0) == '(' && s.charAt(s.length()-1) == ')') {
            s = s.substring(1, s.length()-1);
        }
        try {
            int pos = s.indexOf("+i*");
            if (pos >= 0) {
                double real = Double.parseDouble(s.substring(0, pos));
                double imag = Double.parseDouble(s.substring(pos+3));
                return new ComplexWrapper(real, imag);
            }
            pos = s.indexOf("i*");
            if (pos == 0) {
                return new ComplexWrapper(0, Double.parseDouble(s.substring(pos+2)));
            }
            pos = s.indexOf("i");
            if (pos == 0 && s.length() == 1) {
                return new ComplexWrapper(0, 1);
            }
            return new ComplexWrapper(Double.parseDouble(s));
        }
        catch (Exception e) {
            throw new NumberFormatException(e.getMessage());
        }
    }

    public static IntegerWrapper parseArithmeticInteger(String s) {
        return new IntegerWrapper(Integer.parseInt(s));
    }

    /**
     * Returns the rational correspoding to its string representation <code>s</code>.
     */
    public static Rational parseRational(String s) {
        String unparenthesized = TextUtils.unparenthesize(s);
        int divpos = unparenthesized.indexOf("/");
        if (divpos > -1) {
            try {
                int n = Integer.parseInt(unparenthesized.substring(0, divpos));
                int d = Integer.parseInt(unparenthesized.substring(divpos + 1));
                return new Rational(n,d);
            }
            catch (Exception e) {
                throw new NumberFormatException();
            }
        }
        else {
            try {
                return new Rational(Integer.parseInt(s));
            }
            catch (Exception e) {
                throw new NumberFormatException();
            }
        }
    }

    public static RealWrapper parseArithmeticDouble(String s) {
        return new RealWrapper(Double.parseDouble(s));
    }

    public static ArithmeticRing<?> parseRing(String s) {
        return null; //TODO
    }

    public static <N extends ArithmeticNumber<N>> RingString<N> parseString(ArithmeticStringRing<N> ring, String s) {
        ArithmeticRing<N> factorRing = ring.getFactorRing();
        //TODO fix after RingString refactoring
        /*
        if (factorRing instanceof RRing) {
            return (RingString<N>) parseRString(s);
        }
        if (factorRing instanceof QRing) {
            return (RingString<N>) parseQString(s);
        }


        if (factorRing instanceof ZRing) {
            return (RingString<N>) parseZString(s);
        }

         */
        /*
        if (factorRing instanceof ZnRing) {
            return (RingString<N>) parseZnString(s, ((ZnRing) factorRing).getModulus());
        }

         */
        throw new NumberFormatException(String.format("parsing string ring %s is not supported", ring));
    }

    //TODO fix after RingString refactoring
    /*
    public static RingString<RealWrapper> parseRString(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<RealWrapper> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            double f = Double.parseDouble(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(new RealWrapper(f));
            words.add(w);
        }

        return new RingString<>(words, factors);
    }

     */

    //TODO fix after RingString refactoring
/*
    public static RingString<RationalWrapper> parseQString(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<RationalWrapper> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            //RationalWrapper f = ArithmeticParsingUtils.parseRational(term[0]);
            RationalWrapper f = null; //TODO after StringElement refactoring
            String w = TextUtils.unquote(term[1]);
            factors.add(f);
            words.add(w);
        }

        return new RingString<>(words, factors);
    }

 */

    /*
    public static RingString<IntegerWrapper> parseZString(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<IntegerWrapper> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            int f = Integer.parseInt(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(new IntegerWrapper(f));
            words.add(w);
        }

        return new RingString<>(words, factors);
    }

     */

    /*
    public static RingString<ModulusWrapper> parseZnString(String string, int modulus) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<ModulusWrapper> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            int f = Integer.parseInt(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(new ModulusWrapper(f, modulus));
            words.add(w);
        }

        return new RingString<>(words, factors);
    }

     */

}
