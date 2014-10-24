package org.geotools.ysld.parse;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.ysld.Tuple;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;

import com.google.common.base.Optional;

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
            Optional<ScaleRange> range = parseScale(r).or(parseZoom(r, context));
            if(range.isPresent()) {
            	range.get().applyTo(rule);
            }
            
            context.push(r, "symbolizers", new SymbolizersParser(rule, factory));
        }
    }

    private Optional<ScaleRange> parseScale(YamlMap r) {
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
            return Optional.of(new ScaleRange(min, max));
        } else  {
            return Optional.absent();
        }
    }

    private Optional<ScaleRange> parseZoom(YamlMap r, YamlParseContext context) {
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
            Optional<Integer> min = Optional.absent();
            Optional<Integer> max = Optional.absent();
            if (t.at(0) != null) {
                min = Optional.of(Integer.parseInt(t.strAt(0)));
            }
            if (t.at(1) != null) {
                max =  Optional.of(Integer.parseInt(t.strAt(1)));
            }
            return Optional.of(zCtxt.getRange(min, max));
        } else {
            return Optional.absent();
        }

    }

    @SuppressWarnings("unchecked")
    protected ZoomContext getZoomContext(YamlParseContext context) {
        return ((Optional<ZoomContext>)context.getDocHint(ZoomContext.HINT_ID))
            .or(Util.getWellKnownZoomContext("WebMercator")).get();
    }
}
