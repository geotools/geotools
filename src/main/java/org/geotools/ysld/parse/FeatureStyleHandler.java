package org.geotools.ysld.parse;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

import java.util.Deque;

public class FeatureStyleHandler extends YsldParseHandler {

    Style style;
    FeatureTypeStyle featureStyle;

    FeatureStyleHandler(Style style, Factory factory) {
        super(factory);
        this.style = style;
    }

    @Override
    public void mapping(MappingStartEvent evt, Deque<YamlParseHandler> handlers) {
        style.featureTypeStyles().add(featureStyle = factory.style.createFeatureTypeStyle());
    }

    @Override
    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
        if ("name".equals(evt.getValue())) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    featureStyle.setName(value);
                }
            });
        }
        else if ("title".equals(evt.getValue())) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    featureStyle.setTitle(value);
                }
            });
        }
        else if ("abstract".equals(evt.getValue())) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    featureStyle.setAbstract(value);
                }
            });
        }
        else if ("rules".equals(evt.getValue())) {
            handlers.push(new RuleHandler(featureStyle, factory));
        }
    }

    @Override
    public void endSequence(SequenceEndEvent evt, Deque<YamlParseHandler> handlers) {
        handlers.pop();
    }
}
