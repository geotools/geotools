package org.geotools.ysld.validate;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.ysld.parse.Factory;
import org.geotools.ysld.parse.Util;
import org.opengis.filter.Filter;
import org.yaml.snakeyaml.events.ScalarEvent;

/**
 * Validator for expressions
 * 
 * @author Kevin Smith, Boundless
 * 
 */
public class NameValidator extends ScalarValidator {
    
    @Override
    protected String validate(String value, ScalarEvent evt, YsldValidateContext context) {
        try {
            Util.expression(value, context.factory);
            return null;
        } catch (IllegalArgumentException e) {
            if(e.getCause() instanceof CQLException) {
                return ((CQLException) e.getCause()).getSyntaxError();
            } else {
                throw e;
            }
        }
    }
}
