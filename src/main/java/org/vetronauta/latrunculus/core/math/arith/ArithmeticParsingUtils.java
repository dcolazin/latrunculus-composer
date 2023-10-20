package org.vetronauta.latrunculus.core.math.arith;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArithmeticParsingUtils {

    //TODO this stuff should go in the server package, not in the math one

    public static ArithmeticNumber<?> parse(String s) {
        Class<?> parsingClass = detectParsingClass(s);
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
        if (parsingClass.equals(ArithmeticDouble.class)) {
            return parseArithmeticDouble(s);
        }
        throw new NumberFormatException(String.format("parsing class %s is not supported", parsingClass));
    }

    private static Class<?> detectParsingClass(String s) {
        return null; //TODO
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

    public static ArithmeticDouble parseArithmeticDouble(String s) {
        return new ArithmeticDouble(Double.parseDouble(s));
    }

}
