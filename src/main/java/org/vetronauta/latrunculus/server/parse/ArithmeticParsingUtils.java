package org.vetronauta.latrunculus.server.parse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Modulus;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.number.Real;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringRing;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;
import org.vetronauta.latrunculus.core.math.module.real.RRing;

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
            return parseComplex(s);
        }
        if (parsingClass.equals(ArithmeticInteger.class)) {
            return parseArithmeticInteger(s);
        }
        if (parsingClass.equals(Rational.class)) {
            return parseRational(s);
        }
        if (parsingClass.equals(Real.class)) {
            return parseArithmeticDouble(s);
        }
        throw new NumberFormatException(String.format("parsing class %s is not supported", parsingClass));
    }

    private static Class<?> detectParsingClass(String s) {
        return null; //TODO
    }

    private static Class<?> detectParsingClass(ArithmeticRing<?> ring) {
        if (ring instanceof ZRing) {
            return ArithmeticInteger.class;
        }
        if (ring instanceof ZnRing) {
            return Modulus.class;
        }
        if (ring instanceof QRing) {
            return Rational.class;
        }
        if (ring instanceof RRing) {
            return Real.class;
        }
        if (ring instanceof CRing) {
            return Complex.class;
        }
        return null;
    }

    /**
     * Returns the complex number with the string representation <code>s</code>
     */
    public static Complex parseComplex(String s) {
        if (s.length() == 0) {
            throw new NumberFormatException("Empty string makes no complex");
        }
        else if (s.charAt(0) == '(' && s.charAt(s.length()-1) == ')') {
            s = s.substring(1, s.length()-1);
        }
        return new Complex(s);
    }

    public static ArithmeticInteger parseArithmeticInteger(String s) {
        return new ArithmeticInteger(Integer.parseInt(s));
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

    public static Real parseArithmeticDouble(String s) {
        return new Real(Double.parseDouble(s));
    }

    public static ArithmeticRing<?> parseRing(String s) {
        return null; //TODO
    }

    public static <N extends ArithmeticNumber<N>> RingString<N> parseString(ArithmeticStringRing<N> ring, String s) {
        ArithmeticRing<N> factorRing = ring.getFactorRing();
        if (factorRing instanceof RRing) {
            return (RingString<N>) parseRString(s);
        }
        if (factorRing instanceof QRing) {
            return (RingString<N>) parseQString(s);
        }
        if (factorRing instanceof ZRing) {
            return (RingString<N>) parseZString(s);
        }
        if (factorRing instanceof ZnRing) {
            return (RingString<N>) parseZnString(s, ((ZnRing) factorRing).getModulus());
        }
        throw new NumberFormatException(String.format("parsing string ring %s is not supported", ring));
    }

    public static RingString<Real> parseRString(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<Real> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            double f = Double.parseDouble(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(new Real(f));
            words.add(w);
        }

        return new RingString<>(words, factors);
    }

    public static RingString<Rational> parseQString(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<Rational> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            Rational f = ArithmeticParsingUtils.parseRational(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(f);
            words.add(w);
        }

        return new RingString<>(words, factors);
    }

    public static RingString<ArithmeticInteger> parseZString(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<ArithmeticInteger> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            int f = Integer.parseInt(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(new ArithmeticInteger(f));
            words.add(w);
        }

        return new RingString<>(words, factors);
    }

    public static RingString<Modulus> parseZnString(String string, int modulus) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<Modulus> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            int f = Integer.parseInt(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(new Modulus(f, modulus));
            words.add(w);
        }

        return new RingString<>(words, factors);
    }

}
