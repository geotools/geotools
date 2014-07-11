package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class PolygonSymbolizerHandler extends SymbolizerHandler {

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("PolygonSymbolizer".equals(name)) {
            context.mapping().scalar("polygon").mapping();
        }
        else if ("Fill".equals(name)) {
            context.scalar("fill").push(new FillHandler());
        }
        else if ("Stroke".equals(name)) {
            context.scalar("stroke").push(new StrokeHandler());
        }
        else {
            super.element(xml, context);
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("PolygonSymbolizer".equals(name)) {
            dumpOptions(context).endMapping().endMapping().pop();
        }
        super.endElement(xml, context);
    }
}
