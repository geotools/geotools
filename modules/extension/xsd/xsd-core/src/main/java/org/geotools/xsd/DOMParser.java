/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.xsd;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import org.geotools.util.NullEntityResolver;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.xml.XMLUtils;
import org.geotools.xsd.impl.ParserHandler;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

/**
 * Parses a DOM (Document Object Model) using the geotools xml binding system.
 *
 * <p>This parser should be used if the source xml being parsed has already been parsed into a {@link Document}. If not
 * use one of {@link Parser} or {@link Parser}.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class DOMParser {
    Configuration configuration;
    Document document;
    EntityResolver entityResolver = null;
    ParserHandler handler;

    /**
     * Creates a new instance of the parser.
     *
     * @param configuration Object representing the configuration of the parser.
     * @param document An xml document.
     */
    public DOMParser(Configuration configuration, Document document) {
        this(configuration, document, null);
    }

    /**
     * Creates a new instance of the parser.
     *
     * @param configuration Object representing the configuration of the parser.
     * @param document An xml document.
     * @param entityResolver An entity resolver to use when parsing the document, or {@code null} to use the default
     *     one.
     */
    public DOMParser(Configuration configuration, Document document, EntityResolver entityResolver) {
        this.configuration = configuration;
        this.document = document;
        this.entityResolver = entityResolver;
    }

    /**
     * Parses the supplied DOM returning a single object representing the result of the parse.
     *
     * @return The object representation of the root element of the document.
     */
    public Object parse() throws IOException, SAXException, ParserConfigurationException {
        // Prepare the DOM source
        Source source = new DOMSource(document);

        // "Parser" traverses Document issuing SAX events (fake as document has already been parsed)
        Parser fake = new Parser(configuration);
        if (entityResolver != null) {
            Hints hints = new Hints();
            hints.put(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);

            fake.setEntityResolver(GeoTools.getEntityResolver(hints));
        } else {
            fake.setEntityResolver(GeoTools.getEntityResolver(null));
        }

        // Create the handler to handle the SAX events
        handler = fake.getParserHandler();
        try {
            // Prepare the result
            SAXResult result = new SAXResult(handler);
            // add lexical handler to spot CDATA
            result.setLexicalHandler(handler);

            // Create a transformer
            Transformer xformer = XMLUtils.newTransformer();

            // Traverse the DOM tree
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            new ParserConfigurationException().initCause(e);
        } catch (TransformerException e) {
            throw (IOException) new IOException().initCause(e);
        }

        return handler.getValue();
    }
}
