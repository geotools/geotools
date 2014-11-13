package org.geotools.ysld.parse;

import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.ResourceLocator;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ExternalGraphicParser extends YsldParseHandler {

    ExternalGraphic external;

    public ExternalGraphicParser(Factory factory) {
        super(factory);
        external = factory.style.createExternalGraphic((String)null, null);
    }

    protected abstract void externalGraphic(ExternalGraphic externalGraphic);

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        externalGraphic(external);
        YamlMap map = obj.map();
        if (map.has("url")) {
            String value = map.str("url");
            try {
                external.setLocation(((ResourceLocator)context.getDocHint("resourceLocator")).locateResource(value));
            } catch (IllegalArgumentException e) {
                external.setURI("file:"+value);
                //external.setLocation(DataUtilities.fileToURL(new File(value)));
            }
        }
        external.setFormat(map.str("format"));
    }
}
