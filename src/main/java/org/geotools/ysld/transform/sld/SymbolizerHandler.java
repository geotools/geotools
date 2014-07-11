package org.geotools.ysld.transform.sld;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolizerHandler extends SldTransformHandler {

    Map<String,String> options = new LinkedHashMap<String, String>();

    @Override
    public void element(XMLStreamReader xml, SldTransformContext context) throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("Geometry".equals(name)) {
            context.scalar("geometry").push(new ExpressionHandler());
        }
        else if ("VendorOption".equals(name)) {
            options.put(xml.getAttributeValue(null, "name"), xml.getElementText());
        }
    }

    protected SldTransformContext dumpOptions(SldTransformContext context) throws IOException {
        if (!options.isEmpty()) {
            context.scalar("options").mapping();
            for (Map.Entry<String,String> opt : options.entrySet()) {
                context.scalar(fromCamelCase(opt.getKey())).scalar(opt.getValue());
            }
            context.endMapping();
        }
        return context;
    }

    String fromCamelCase(String camelCase) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
                if (sb.length() > 0) {
                    sb.append("-");
                }
            }
            sb.append(ch);
        }
        return sb.toString();
    }
}
