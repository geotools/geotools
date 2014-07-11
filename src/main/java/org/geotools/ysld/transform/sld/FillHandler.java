package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class FillHandler extends SldTransformHandler {
    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Fill".equals(name)) {
            context.mapping();
        }
        else if ("CssParameter".equals(name) || "SvgParameter".equals(name)) {
            context.push(new ParameterHandler().rename("fill", "color").strip("fill"));
        }
        else if ("GraphicFill".equals(name)) {
            context.scalar("graphic").push(new GraphicHandler());
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Fill".equals(name)) {
            context.endMapping().pop();
        }
    }
}
