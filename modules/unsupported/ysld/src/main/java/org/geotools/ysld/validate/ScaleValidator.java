package org.geotools.ysld.validate;

import org.yaml.snakeyaml.events.ScalarEvent;

/**
 * Validator for Scale
 * <p>
 * This Validator is stateful, do not re-use it.
 * 
 * @author Kevin Smith, Boundless
 *
 */
public class ScaleValidator extends RangeValidator<Double> {

    @Override
    Double parse(String s) throws IllegalArgumentException {
        return Double.parseDouble(s);
    }

    @Override
    protected void validateParsed(Double parsed, ScalarEvent evt,
            YsldValidateContext context) {
        if(parsed.compareTo(0d)<0) {
            context.error("scale denominator must be non-negative", evt.getStartMark());
        } else if(parsed.isNaN()) {
            context.error("scale denominator is not a number", evt.getStartMark());
        }
    }
    
}
