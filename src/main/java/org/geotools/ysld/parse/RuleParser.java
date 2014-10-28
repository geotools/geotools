package org.geotools.ysld.parse;

import javax.annotation.Nullable;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.ysld.Tuple;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;

public class RuleParser extends YsldParseHandler {

    FeatureTypeStyle featureStyle;

    public RuleParser(FeatureTypeStyle featureStyle, Factory factory) {
        super(factory);
        this.featureStyle = featureStyle;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlSeq seq = obj.seq();
        for (YamlObject o : seq) {
            YamlMap r = o.map();
            
            Rule rule = factory.style.createRule();
            featureStyle.rules().add(rule);
            
            rule.setName(r.str("name"));
            if (r.has("title")) {
                rule.setTitle(r.str("title"));
            }
            if (r.has("abstract")) {
                rule.setAbstract(r.str("abstract"));
            }
            rule.setTitle(r.str("title"));
            rule.setAbstract(r.str("abstract"));
            
            if (r.has("filter")) {
                try {
                    rule.setFilter(ECQL.toFilter(r.str("filter")));
                } catch (CQLException e) {
                    throw new RuntimeException("Error parsing filter", e);
                }
            }
            
            rule.setElseFilter(r.boolOr("else", false));
            
            // Prefer scale over zoom
            @Nullable ScaleRange range = Util.defaultForNull(parseScale(r),parseZoom(r, context));
            if(range!=null) {
            	range.applyTo(rule);
            }
            
            context.push(r, "symbolizers", new SymbolizersParser(rule, factory));
        }
    }

    private @Nullable ScaleRange parseScale(YamlMap r) {
        if (r.has("scale")) {
            String value = r.str("scale");
            Tuple t = null;
            try {
                t = Tuple.of(2).parse(value);
            }
            catch(IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    String.format("Bad scale value: '%s', must be of form (<min>,<max>)", value), e);
            } 
            double min = 0;
            double max = Double.POSITIVE_INFINITY;
            if (t.at(0) != null) {
                min = Double.parseDouble(t.strAt(0));
            }
            if (t.at(1) != null) {
                max = Double.parseDouble(t.strAt(1));
            }
            return new ScaleRange(min, max);
        } else  {
            return null;
        }
    }

    private ScaleRange parseZoom(YamlMap r, YamlParseContext context) {
        if (r.has("zoom")) {
            ZoomContext zCtxt = getZoomContext(context);
            String value = r.str("zoom");
            Tuple t = null;
            try {
                t = Tuple.of(2).parse(value);
            }
            catch(IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        String.format("Bad zoom value: '%s', must be of form (<min>,<max>)", value), e);
            }
            @Nullable Integer min = null;
            @Nullable Integer max = null;
            if (t.at(0) != null) {
                min = Integer.parseInt(t.strAt(0));
            }
            if (t.at(1) != null) {
                max =  Integer.parseInt(t.strAt(1));
            }
            return zCtxt.getRange(min, max);
        } else {
            return null;
        }

    }

    protected ZoomContext getZoomContext(YamlParseContext context) {
        return Util.forceDefaultForNull(
                (ZoomContext)context.getDocHint(ZoomContext.HINT_ID),
                WellKnownZoomContextFinder.getInstance().get("DEFAULT"));
    }
}
