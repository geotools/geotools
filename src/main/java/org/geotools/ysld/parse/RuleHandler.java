package org.geotools.ysld.parse;

import org.geotools.ysld.Tuple;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.opengis.filter.Filter;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

import java.util.Deque;

public class RuleHandler extends YsldParseHandler {

    FeatureTypeStyle featureStyle;
    Rule rule;

    public RuleHandler(FeatureTypeStyle featureStyle, Factory factory) {
        super(factory);
        this.featureStyle = featureStyle;
    }

    @Override
    public void mapping(MappingStartEvent evt, Deque<YamlParseHandler> handlers) {
        featureStyle.rules().add(rule = factory.style.createRule());
    }

    @Override
    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
        String val = evt.getValue();
        if ("name".equals(val)) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    rule.setName(value);
                }
            });
        }
        else if ("title".equals(val)) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    rule.setTitle(value);
                }
            });
        }
        else if ("abstract".equals(val)) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    rule.setAbstract(value);
                }
            });
        }
        else if ("filter".equals(val)) {
            handlers.push(new FilterHandler(factory) {
                @Override
                protected void filter(Filter filter) {
                    rule.setFilter(filter);
                }
            });
        }
        else if ("else".equals(val)) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    rule.setElseFilter(Boolean.valueOf(value));
                }
            });
        }
        else if ("scale".equals(val)) {
            handlers.push(new ValueHandler(factory) {
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
            handlers.push(new SymbolizersHandler(rule, factory));
        }
    }

    @Override
    public void endSequence(SequenceEndEvent evt, Deque<YamlParseHandler> handlers) {
        handlers.pop();
    }
}
