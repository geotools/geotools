package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class LineSymbolizerHandler extends SymbolizerHandler {

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("LineSymbolizer".equals(name)) {
            context.mapping().scalar("line").mapping();
        }
        else if ("Stroke".equals(name)) {
            context.push(new StrokeHandler());
        }
        else if ("PerpindicularOffset".equals(name)) {
            context.scalar("offset").push(new ExpressionHandler());
        }
        else {
            super.element(xml, context);
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("LineSymbolizer".equals(name)) {
            dumpOptions(context).endMapping().endMapping().pop();
        }
        super.endElement(xml, context);
    }
}
