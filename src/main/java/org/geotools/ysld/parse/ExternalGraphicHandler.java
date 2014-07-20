package org.geotools.ysld.parse;

import org.geotools.styling.ExternalGraphic;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ExternalGraphicHandler extends YsldParseHandler {

    ExternalGraphic external;

    public ExternalGraphicHandler(Factory factory) {
        super(factory);
        external = factory.style.createExternalGraphic((String)null, null);
        externalGraphic(external);
    }

    protected abstract void externalGraphic(ExternalGraphic externalGraphic);

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        YamlMap map = obj.map();
        if (map.has("url")) {
            String value = map.str("url");
            try {
                external.setLocation(new URL(value));
            } catch (MalformedURLException e) {
                external.setURI("file:"+value);
                //external.setLocation(DataUtilities.fileToURL(new File(value)));
            }
        }
        external.setFormat(map.str("format"));
    }
}
