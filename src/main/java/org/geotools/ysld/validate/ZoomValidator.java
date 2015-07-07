package org.geotools.ysld.validate;

import org.yaml.snakeyaml.events.ScalarEvent;

/**
 * Validator for Zoom
 * <p>
 * This Validator is stateful, do not re-use it.
 * 
 * @author Kevin Smith, Boundless
 *
 */
public class ZoomValidator extends RangeValidator<Integer> {

    @Override
    Integer parse(String s) throws IllegalArgumentException {
        return Integer.parseInt(s);
    }

    @Override
    protected void validateParsed(Integer parsed, ScalarEvent evt, YsldValidateContext context) {
        if(!context.getZCtxt().isInRange(parsed)){
            context.error(String.format("Zoom level %d is out of range", parsed), evt.getStartMark());
        }
    }

}
