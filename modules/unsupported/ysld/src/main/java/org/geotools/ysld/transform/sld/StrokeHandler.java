package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class StrokeHandler extends SldTransformHandler {

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("CssParameter".equals(name) || "SvgParameter".equals(name)) {
            context.push(new ParameterHandler().rename("stroke", "stroke-color"));
        }
        else if ("GraphicStroke".equals(name)) {
            context.scalar("stroke-graphic-stroke").push(new GraphicHandler());
        }
        else if ("GraphicFill".equals(name)) {
            context.scalar("stroke-graphic-fill").push(new GraphicHandler());
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Stroke".equals(name)) {
            context.pop();
        }
    }
}
