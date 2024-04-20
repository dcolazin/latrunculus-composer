package org.rubato.rubettes.select2d;

import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.LatrunculusRuntimeException;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.server.parse.ArithmeticParsingUtils;

public class JQElementSlider extends JElementSlider {

    public JQElementSlider() {
        super();
        min = new Rational(-10);
        max = new Rational(10);
        setMinField(min.toString());
        setMaxField(max.toString());
        setRange(0, MAX.intValue());
        last = new Rational(0);
        setValue(toInteger(last));
    }
    

    private Rational getRational() {
        try {
            return (new Rational(getValue())).product(max.difference(min).quotient(MAX)).sum(min);
        } catch (DivisionException de) {
            throw new LatrunculusRuntimeException(de);
        }
    }
    
    
    private int toInteger(Rational v) {
        try {
            return (((v.difference(min)).product(MAX)).quotient(max.difference(min))).intValue();
        } catch (DivisionException de) {
            throw new LatrunculusRuntimeException(de);
        }
    }
    
    
    protected Rational getElement() {
        return getRational();
    }


    protected void maxFieldUpdate() {
        Rational cur = getRational();
        try {
            max = ArithmeticParsingUtils.parseRational(getMaxField());
        }
        catch (NumberFormatException e) {}
        if (max.compareTo(min) <= 0) { max = min.sum(new Rational(1)); }
        setMaxField(max.toString());
        if (cur.compareTo(max) >= 0) {
            cur = max;
        }
        else if (cur.compareTo(min) <= 0) {
            cur = min;
        }
        setValue(toInteger(cur));
    }


    protected void minFieldUpdate() {
        Rational cur = getRational();
        try {
            min = ArithmeticParsingUtils.parseRational(getMinField());
        }
        catch (NumberFormatException e) {}
        if (max.compareTo(min) <= 0) { min = max.difference(new Rational(1)); }
        setMinField(min.toString());
        if (cur.compareTo(max) >= 0) {
            cur = max;
        }
        else if (cur.compareTo(min) <= 0) {
            cur = min;
        }
        setValue(toInteger(cur));
    }


    protected void sliderUpdate() {
        Rational cur = getRational();
        if (!cur.equals(last)) {
            setCurrentField(cur.toString());
            fireActionPerformed();
            last = cur;
        }
    }
    
    
    private Rational min;
    private Rational max;
    private Rational last;
    
    private static final Rational MAX =  new Rational(256);
}
