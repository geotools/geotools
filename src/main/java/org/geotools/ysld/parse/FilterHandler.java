package org.geotools.ysld.parse;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.filter.Filter;
import org.yaml.snakeyaml.events.Event;

public abstract class FilterHandler extends ValueHandler {

    protected FilterHandler(Factory factory) {
        super(factory);
    }

    @Override
    protected void value(String value, Event evt) {
        try {
            filter(ECQL.toFilter(value));
        } catch (CQLException e) {
            throw new ParseException("Bad filter: " + value, evt, e);
        }
    }

    protected abstract void filter(Filter filter);
}
