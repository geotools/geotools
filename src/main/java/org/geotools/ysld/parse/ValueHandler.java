package org.geotools.ysld.parse;

import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;

public abstract class ValueHandler extends YsldParseHandler {
    protected ValueHandler(Factory factory) {
        super(factory);
    }

    @Override
    public void scalar(ScalarEvent evt, YamlParseContext context) {
        value(evt.getValue(), evt);
        context.pop();
    }

    protected abstract void value(String value, Event event);
}
