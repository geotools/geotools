package org.geotools.ysld.parse;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.yaml.snakeyaml.events.Event;

import java.util.regex.Pattern;

public abstract class ExpressionHandler extends ValueHandler {

    static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("\\[.+\\]");

    static Expression parse(String value, Event evt, Factory factory) {
        try {
            Expression expr = ECQL.toExpression(value, factory.filter);
            if (expr instanceof PropertyName && !ATTRIBUTE_PATTERN.matcher(value).matches()) {
                // treat as literal
                return factory.filter.literal(((PropertyName) expr).getPropertyName());
            }
            return expr;
        } catch (CQLException e) {
            //TODO: log this?
            return factory.filter.literal(value);
            //throw new ParseException("Bad expression: "+value, evt, e);
        }
    }

    protected ExpressionHandler(Factory factory) {
        super(factory);
    }

    @Override
    protected void value(String value, Event evt) {
        expression(parse(value, evt, factory));
    }

    protected abstract void expression(Expression expr);
}
