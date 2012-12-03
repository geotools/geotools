package org.geotools.kml.v22;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.impl.ElementHandler;
import org.geotools.xml.impl.Handler;
import org.geotools.xml.impl.HandlerFactory;
import org.geotools.xml.impl.HandlerFactoryImpl;
import org.geotools.xml.impl.ParserHandler;

public class KMLCustomSchemaHandlerFactory extends HandlerFactoryImpl implements HandlerFactory {

    private final SchemaRegistry schemaRegistry;

    public KMLCustomSchemaHandlerFactory(SchemaRegistry schemaRegistry) {
        super();
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public ElementHandler createElementHandler(QName qName, Handler parent, ParserHandler parser) {
        String name = qName.getLocalPart();
        if (schemaRegistry.get(name) != null) {
            // we found a custom schema element
            // let's treat it as if we've found a placemark
            SchemaIndex schemaIndex = parser.getSchemaIndex();
            XSDElementDeclaration element = schemaIndex.getElementDeclaration(KML.Placemark);
            if (element != null) {
                return createElementHandler(element, parent, parser);
            }
        }
        return super.createElementHandler(qName, parent, parser);
    }
}
