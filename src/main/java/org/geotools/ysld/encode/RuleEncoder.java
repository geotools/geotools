package org.geotools.ysld.encode;


import org.geotools.ysld.Tuple;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.opengis.filter.Filter;

public class RuleEncoder extends Encoder<Rule> {

    public RuleEncoder(FeatureTypeStyle featureStyle) {
        super(featureStyle.rules().iterator());
    }

    @Override
    protected void encode(Rule rule) {
        put("name", rule.getName());
        put("title", rule.getTitle());
        put("abstract", rule.getAbstract());
        if (rule.getFilter() != null && rule.getFilter() == Filter.INCLUDE) {
            put("where", ECQL.toCQL(rule.getFilter()));
        }
        if (rule.isElseFilter()) {
            put("else", true);
        }

        Tuple t = Tuple.of(toStringOrNull(rule.getMinScaleDenominator()), toStringOrNull(rule.getMaxScaleDenominator()));
        if (t.isNull()) {
            put("scale", t.toString());
        }

        //legend:?
        put("symbolizers", new SymbolizerEncoder(rule));
    }

    String toStringOrNull(double d) {
        return d > 0 && !Double.isNaN(d) ? String.valueOf(d) : null;
    }
}
