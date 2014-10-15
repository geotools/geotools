package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolizerHandler extends SldTransformHandler {

    public static String OPTION_PREFIX = "-";

    
    Map<String,String> options = new LinkedHashMap<String, String>();

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Geometry".equals(name)) {
            context.scalar("geometry").push(new ExpressionHandler());
        }
        else if ("VendorOption".equals(name)) {
            String option = xml.getAttributeValue(null, "name");
            options.put(option, xml.getElementText());
        }
    }

    protected SldTransformContext dumpOptions(SldTransformContext context) throws IOException {
        if (!options.isEmpty()) {
            for (Map.Entry<String, String> opt : options.entrySet()) {
                String option = OPTION_PREFIX + opt.getKey();
                context.scalar(option).scalar(opt.getValue());
            }
        }
        return context;
    }
}
