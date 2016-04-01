package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class FillHandler extends SldTransformHandler {
    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("CssParameter".equals(name) || "SvgParameter".equals(name)) {
            context.push(new ParameterHandler().rename("fill", "fill-color"));
        }
        else if ("GraphicFill".equals(name)) {
            context.scalar("fill-graphic").push(new GraphicHandler());
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Fill".equals(name)) {
            context.pop();
        }
    }
}
