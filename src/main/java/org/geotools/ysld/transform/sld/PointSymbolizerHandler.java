package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class PointSymbolizerHandler extends SymbolizerHandler {
    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("PointSymbolizer".equals(name)) {
            context.mapping().scalar("point").push(new GraphicHandler());
        }

        super.element(xml, context);
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("PointSymbolizer".equals(name)) {
            dumpOptions(context).endMapping().pop();
        }
        super.endElement(xml, context);
    }
}
