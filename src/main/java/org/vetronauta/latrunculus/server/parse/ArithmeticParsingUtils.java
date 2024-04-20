package org.vetronauta.latrunculus.server.parse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.scheme.SchemeParsingUtils;
import org.vetronauta.latrunculus.core.util.TextUtils;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.StringRing;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArithmeticParsingUtils {

    public static <R extends RingElement<R>> R parse(Ring<R> ring, String s) {
        return (R) parse(detectParsingClass(ring), s);
    }

    public static RingElement<?> parse(String s) {
        return parse(detectParsingClass(s), s);
    }

    private static RingElement<?> parse(Class<?> parsingClass, String s) {
        if (parsingClass == null) {
            throw new NumberFormatException(String.format("cannot detect parsing class for %s", s));
        }
        if (parsingClass.equals(Complex.class)) {
            return parseComplex(s);
        }
        if (parsingClass.equals(ZInteger.class)) {
            return parseZInteger(s);
        }
        if (parsingClass.equals(Rational.class)) {
            return parseRational(s);
        }
        if (parsingClass.equals(Real.class)) {
            return parseReal(s);
        }
        throw new NumberFormatException(String.format("parsing class %s is not supported", parsingClass));
    }

    private static Class<?> detectParsingClass(String s) {
        return null; //TODO
    }

    private static Class<?> detectParsingClass(Ring<?> ring) {
        if (ring instanceof ZRing) {
            return ZInteger.class;
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
        return SchemeParsingUtils.parseComplex(s);
    }

    public static ZInteger parseZInteger(String s) {
        return new ZInteger(Integer.parseInt(s));
    }

    /**
     * Returns the rational correspoding to its string representation <code>s</code>.
     */
    public static Rational parseRational(String s) {
        return SchemeParsingUtils.parseRational(s);
    }

    public static Real parseReal(String s) {
        return new Real(Double.parseDouble(s));
    }

    public static Ring<?> parseRing(String s) {
        return null; //TODO
    }

    public static <R extends RingElement<R>> StringMap<R> parseString(StringRing<R> ring, String s) {
        Ring<R> factorRing = ring.getFactorRing();
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
