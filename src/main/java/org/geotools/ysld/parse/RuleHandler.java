package org.geotools.ysld.parse;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.ysld.Tuple;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;
import org.opengis.filter.Filter;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

public class RuleHandler extends YsldParseHandler {

    FeatureTypeStyle featureStyle;

    public RuleHandler(FeatureTypeStyle featureStyle, Factory factory) {
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

                if (t.at(0) != null) {
                    rule.setMinScaleDenominator(Double.parseDouble(t.strAt(0)));
                }
                if (t.at(1) != null) {
                    rule.setMaxScaleDenominator(Double.parseDouble(t.strAt(1)));
                }
            }

            context.push(r, "symbolizers", new SymbolizersHandler(rule, factory));
        }
    }
}
