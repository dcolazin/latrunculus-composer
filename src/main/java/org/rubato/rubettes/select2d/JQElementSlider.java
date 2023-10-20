package org.rubato.rubettes.select2d;

import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;

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
        return (new Rational(getValue())).product(max.difference(min).quotient(MAX)).sum(min);
    }
    
    
    private int toInteger(Rational v) {
        return (((v.difference(min)).product(MAX)).quotient(max.difference(min))).intValue();
    }
    
    
    protected ArithmeticElement<Rational> getElement() {
        return new ArithmeticElement<>(getRational());
    }


    protected void maxFieldUpdate() {
        Rational cur = getRational();
        try {
            max = ArithmeticParsingUtils.parseRational(getMaxField());
        }
        catch (NumberFormatException e) {}
        if (max.compareTo(min) <= 0) { max = min.sum(1); }
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
        if (max.compareTo(min) <= 0) { min = max.difference(1); }
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
