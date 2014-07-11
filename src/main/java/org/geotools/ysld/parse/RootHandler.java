package org.geotools.ysld.parse;

import org.geotools.styling.*;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.util.Deque;

public class RootHandler extends YsldParseHandler {

    StyledLayerDescriptor sld;
    Style style;

    public RootHandler() {
        super(new Factory());
    }

    @Override
    public void mapping(MappingStartEvent evt, Deque<YamlParseHandler> handlers) {
        sld = factory.style.createStyledLayerDescriptor();

        NamedLayer layer = factory.style.createNamedLayer();
        sld.layers().add(layer);

        layer.styles().add(style = factory.style.createStyle());
    }

    @Override
    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
        if ("name".equals(evt.getValue())) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    style.setName(value);
                }
            });
        }
        else if ("title".equals(evt.getValue())) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    style.setTitle(value);
                }
            });
        }
        else if ("abstract".equals(evt.getValue())) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event evt) {
                    style.setAbstract(value);
                }
            });
        }
        else if ("feature-styles".equals(evt.getValue())) {
            handlers.push(new FeatureStyleHandler(style, factory));
        }
    }

    public StyledLayerDescriptor sld() {
        return sld;
    }
}
