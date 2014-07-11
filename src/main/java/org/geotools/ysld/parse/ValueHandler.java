package org.geotools.ysld.parse;

import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.util.Deque;

public abstract class ValueHandler extends YsldParseHandler {
    protected ValueHandler(Factory factory) {
        super(factory);
    }

    @Override
    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
        value(evt.getValue(), evt);
        handlers.pop();
    }

    protected abstract void value(String value, Event event);
}
