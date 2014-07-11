package org.geotools.ysld.parse;

import org.geotools.ysld.Tuple;
import org.geotools.styling.AnchorPoint;
import org.opengis.filter.expression.Expression;
import org.yaml.snakeyaml.events.Event;

public abstract class AnchorHandler extends ValueHandler {

    AnchorHandler(Factory factory) {
        super(factory);
    }

    @Override
    protected void value(String value, Event event) {
        Tuple t = null;
        try {
            t = Tuple.of(2).parse(value);
        }
        catch(IllegalArgumentException e) {
            throw new ParseException(String.format("Bad anchor: '%s', must be of form (<x>,<y>)", value), event);
        }

        Expression x = t.at(0) != null ? ExpressionHandler.parse(t.at(0), event, factory) :
            factory.filter.literal(0);
        Expression y = t.at(1) != null ? ExpressionHandler.parse(t.at(1), event, factory) :
            factory.filter.literal(0);
        anchor(factory.style.createAnchorPoint(x, y));
    }

    protected abstract void anchor(AnchorPoint anchor);
}
