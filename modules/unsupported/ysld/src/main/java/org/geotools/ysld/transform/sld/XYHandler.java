package org.geotools.ysld.transform.sld;


import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

public class XYHandler extends SldTransformHandler {

    String x, y;
    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if (name.endsWith("X")) {
            context.push(new ExpressionHandler() {
                @Override
                protected SldTransformContext onValue(String value, SldTransformContext context) throws IOException {
                    x = value;
                    return context;
                }
            });
        }
        if (name.endsWith("Y")) {
            context.push(new ExpressionHandler() {
                @Override
                protected SldTransformContext onValue(String value, SldTransformContext context) throws IOException {
                    y = value;
                    return context;
                }
            });
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if (!name.endsWith("X") && !name.endsWith("Y")) {
            context.tuple(x, y).pop();
        }
    }
}
