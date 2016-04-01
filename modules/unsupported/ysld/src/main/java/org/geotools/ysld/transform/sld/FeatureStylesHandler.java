package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class FeatureStylesHandler extends SldTransformHandler {
    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("FeatureTypeStyle".equals(name)) {
            context.mapping();
        }
        else if ("Name".equals(name)) {
            context.scalar("name");
            context.scalar(xml.getElementText());
        }
        else if ("Title".equals(name)) {
            context.scalar("title");
            context.scalar(xml.getElementText());
        }
        else if ("Abstract".equals(name)) {
            context.scalar("abstract");
            context.scalar(xml.getElementText());
        }
        else if ("Rule".equals(name)) {
            context.scalar("rules").sequence().push(new RulesHandler());
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        if ("FeatureTypeStyle".equals(xml.getLocalName())) {
            context.endSequence().endMapping();
        }
        else if ("UserStyle".equals(xml.getLocalName())) {
            context.pop();
        }
    }
}
