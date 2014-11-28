package org.geotools.ysld.encode;


import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.ysld.Tuple;
import org.opengis.filter.Filter;

public class RuleEncoder extends YsldEncodeHandler<Rule> {

    public RuleEncoder(FeatureTypeStyle featureStyle) {
        super(featureStyle.rules().iterator());
    }

    @Override
    protected void encode(Rule rule) {
        put("name", rule.getName());
        put("title", rule.getTitle());
        put("abstract", rule.getAbstract());
        if (rule.getFilter() != null && rule.getFilter() != Filter.INCLUDE) {
            put("filter", String.format("${%s}", escapeForEmbededCQL(ECQL.toCQL(rule.getFilter()))));
        }
        if (rule.isElseFilter()) {
            put("else", true);
        }
        
        Tuple t = Tuple.of(toStringOrNull(rule.getMinScaleDenominator()), toStringOrNull(rule.getMaxScaleDenominator()));
        if (!t.isNull()) {
            put("scale", t.toString());
        }
        
        //legend:?
        put("symbolizers", new SymbolizersEncoder(rule));
    }
    
    String toStringOrNull(double d) {
        return d > 0 && !Double.isNaN(d) && !Double.isInfinite(d) ? String.valueOf(d) : null;
    }
}
