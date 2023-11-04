package org.rubato.rubettes.select2d;

import org.vetronauta.latrunculus.server.parse.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.RationalWrapper;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;

public class JQElementSlider extends JElementSlider {

    public JQElementSlider() {
        super();
        min = new RationalWrapper(-10);
        max = new RationalWrapper(10);
        setMinField(min.toString());
        setMaxField(max.toString());
        setRange(0, MAX.intValue());
        last = new RationalWrapper(0);
        setValue(toInteger(last));
    }
    

    private RationalWrapper getRational() {
        return (new RationalWrapper(getValue())).product(max.difference(min).quotient(MAX)).sum(min);
    }
    
    
    private int toInteger(RationalWrapper v) {
        return (((v.difference(min)).product(MAX)).quotient(max.difference(min))).intValue();
    }
    
    
    protected ArithmeticElement<RationalWrapper> getElement() {
        return new ArithmeticElement<>(getRational());
    }


    protected void maxFieldUpdate() {
        RationalWrapper cur = getRational();
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
        RationalWrapper cur = getRational();
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
        RationalWrapper cur = getRational();
        if (!cur.equals(last)) {
            setCurrentField(cur.toString());
            fireActionPerformed();
            last = cur;
        }
    }
    
    
    private RationalWrapper min;
    private RationalWrapper max;
    private RationalWrapper last;
    
    private static final RationalWrapper MAX =  new RationalWrapper(256);
}
