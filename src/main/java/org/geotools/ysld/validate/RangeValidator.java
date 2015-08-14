package org.geotools.ysld.validate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.yaml.snakeyaml.events.ScalarEvent;

public abstract class RangeValidator<T extends Comparable<T>> extends TupleValidator {
    T min = null;
    
    public RangeValidator() {
        super(Collections.<ScalarValidator> emptyList());
    }
    
    @Override
    protected List<ScalarValidator> getSubValidators() {
        return Arrays.asList((ScalarValidator)new ValueValidator(true), new ValueValidator(false));
    }

    class ValueValidator extends ScalarValidator {
        ValueValidator previous;
        boolean isMin;
        
        public ValueValidator(boolean isMin) {
            super();
            this.isMin = isMin;
        }

        @Override
        protected String validate(String value, ScalarEvent evt,
                YsldValidateContext context) {
            try {
                if(value!=null && !value.isEmpty() && !(isMin?"min":"max").equalsIgnoreCase(value)){
                    T parsed = parse(value);
                    validateParsed(parsed, evt, context);
                    if(isMin) {
                        min = parsed;
                    } else {
                        if(min!=null && parsed!=null && min.compareTo(parsed)>0){
                            return "Minimum is greater than maximum";
                        }
                    }
                }
                return null;
            } catch (IllegalArgumentException ex) {
                return ex.getMessage();
            }
            
        }
    
    }
    
    abstract T parse(String s) throws IllegalArgumentException;
    
    protected void validateParsed(T parsed, ScalarEvent evt, YsldValidateContext context) {
        // Do Nothing
    }
    
    @Override
    void reset() {
        super.reset();
        min=null;
    }
}
