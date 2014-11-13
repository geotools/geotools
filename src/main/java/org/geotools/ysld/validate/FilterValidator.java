package org.geotools.ysld.validate;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.ysld.parse.Util;
import org.opengis.filter.Filter;
import org.yaml.snakeyaml.events.ScalarEvent;

public class FilterValidator extends ScalarValidator {
    @Override
    protected String validate(String value, ScalarEvent evt, YsldValidateContext context) {
        try {
            ECQL.toFilter(Util.removeExpressionBrackets(value));
            return null;
        } catch (CQLException e) {
            return e.getSyntaxError();
        }
    }
}
