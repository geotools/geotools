package org.geotools.ysld.parse;

import org.geotools.ysld.Tuple;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.opengis.filter.Filter;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

public class RuleHandler extends YsldParseHandler {

    FeatureTypeStyle featureStyle;
    Rule rule;

    public RuleHandler(FeatureTypeStyle featureStyle, Factory factory) {
        super(factory);
        this.featureStyle = featureStyle;
    }

    @Override
    public void mapping(MappingStartEvent evt, YamlParseContext context) {
        featureStyle.rules().add(rule = factory.style.createRule());
    }

    @Override
    public void scalar(ScalarEvent evt, YamlParseContext context) {
        String val = evt.getValue();
        if ("name".equals(val)) {
            context.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    rule.setName(value);
                }
            });
        }
        else if ("title".equals(val)) {
            context.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    rule.setTitle(value);
                }
            });
        }
        else if ("abstract".equals(val)) {
            context.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    rule.setAbstract(value);
                }
            });
        }
        else if ("filter".equals(val)) {
            context.push(new FilterHandler(factory) {
                @Override
                protected void filter(Filter filter) {
                    rule.setFilter(filter);
                }
            });
        }
        else if ("else".equals(val)) {
            context.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    rule.setElseFilter(Boolean.valueOf(value));
                }
            });
        }
        else if ("scale".equals(val)) {
            context.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event event) {
                    Tuple t = null;
                    try {
                        t = Tuple.of(2).parse(value);
                    }
                    catch(IllegalArgumentException e) {
                        throw new ParseException(
                            String.format("Bad scale value: '%s', must be of form (<min>,<max>)", value), event);
                    }
                    if (t.at(0) != null) {
                        rule.setMinScaleDenominator(Double.parseDouble(t.at(0)));
                    }
                    if (t.at(1) != null) {
                        rule.setMaxScaleDenominator(Double.parseDouble(t.at(1)));
                    }
                }
            });
        }
        else if ("symbolizers".equals(val)) {
            context.push(new SymbolizersHandler(rule, factory));
        }
    }

    @Override
    public void endSequence(SequenceEndEvent evt, YamlParseContext context) {
        context.pop();
    }
}
