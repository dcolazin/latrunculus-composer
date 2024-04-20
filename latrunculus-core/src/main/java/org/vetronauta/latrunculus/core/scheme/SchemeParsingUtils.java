package org.vetronauta.latrunculus.core.scheme;

import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.util.TextUtils;

public class SchemeParsingUtils {

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
        try {
            int pos = s.indexOf("+i*");
            if (pos >= 0) {
                double real = Double.parseDouble(s.substring(0, pos));
                double imag = Double.parseDouble(s.substring(pos+3));
                return new Complex(real, imag);
            }
            pos = s.indexOf("i*");
            if (pos == 0) {
                return new Complex(0, Double.parseDouble(s.substring(pos+2)));
            }
            pos = s.indexOf("i");
            if (pos == 0 && s.length() == 1) {
                return new Complex(0, 1);
            }
            return new Complex(Double.parseDouble(s));
        }
        catch (Exception e) {
            throw new NumberFormatException(e.getMessage());
        }
    }

}
