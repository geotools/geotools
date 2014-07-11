package org.geotools.ysld.parse;

import org.geotools.styling.ExternalGraphic;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Deque;

public abstract class ExternalGraphicHandler extends YsldParseHandler {

    ExternalGraphic external;

    public ExternalGraphicHandler(Factory factory) {
        super(factory);
        external = factory.style.createExternalGraphic((String)null, null);
    }

    protected abstract void externalGraphic(ExternalGraphic externalGraphic);

    @Override
    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
        String val = evt.getValue();
        if ("url".equals(val)) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event event) {
                    try {
                        external.setLocation(new URL(value));
                    } catch (MalformedURLException e) {
                        external.setURI("file:"+value);
                        //external.setLocation(DataUtilities.fileToURL(new File(value)));
                    }
                }
            });
        }
        if ("format".equals(val)) {
            handlers.push(new ValueHandler(factory) {
                @Override
                protected void value(String value, Event event) {
                    external.setFormat(value);
                }
            });
        }
    }

    @Override
    public void endMapping(MappingEndEvent evt, Deque<YamlParseHandler> handlers) {
        externalGraphic(external);
        handlers.pop();
    }
}
