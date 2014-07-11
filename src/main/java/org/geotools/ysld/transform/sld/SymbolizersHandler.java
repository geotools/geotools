package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class SymbolizersHandler extends SldTransformHandler {
    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("PointSymbolizer".equals(name)) {
            context.push(new PointSymbolizerHandler());
        }
        else if ("LineSymbolizer".equals(name)) {
            context.push(new LineSymbolizerHandler());
        }
        else if ("PolygonSymbolizer".equals(name)) {
            context.push(new PolygonSymbolizerHandler());
        }
        else if ("TextSymbolizer".equals(name)) {
            context.push(new TextSymbolizerHandler());
        }
        else if ("RasterSymbolizer".equals(name)) {
            context.push(new RasterSymbolizerHandler());
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if (name.equals("Rule")) {
            context.pop();
        }
    }

}
