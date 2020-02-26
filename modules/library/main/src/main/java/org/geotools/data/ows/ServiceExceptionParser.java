/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows;

import static javax.xml.stream.XMLInputFactory.IS_COALESCING;
import static javax.xml.stream.XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES;
import static javax.xml.stream.XMLInputFactory.SUPPORT_DTD;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.geotools.ows.ServiceException;

/**
 * Utility class that will parse ServiceExceptions out of an inputStream.
 *
 * @author rgould
 */
public class ServiceExceptionParser {

    /**
     * Tries to read a ServiceExceptionReport from the input stream, and construct a chain of
     * ServiceExceptions.
     *
     * <p>ServiceExceptions beyond the first can be accessed using ServiceException.next();
     *
     * @param inputStream stream to parse the exception report from, not closed by this method
     */
    public static ServiceException parse(InputStream inputStream) throws IOException {
        List<ServiceException> exceptions = new ArrayList<>();
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // disable resolving of external DTD entities (coalescing needs to be false)
            inputFactory.setProperty(IS_COALESCING, false);
            inputFactory.setProperty(IS_REPLACING_ENTITY_REFERENCES, false);
            // disallow DTDs entirely
            inputFactory.setProperty(SUPPORT_DTD, false);

            XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream, "UTF-8");
            int tag;
            while ((tag = reader.next()) != END_DOCUMENT) {
                if (tag == START_ELEMENT && reader.getLocalName().equals("ServiceException")) {
                    String code = parseServiceExceptionCode(reader);
                    String errorMessage = parseServiceExceptionMessage(reader);
                    exceptions.add(new ServiceException(errorMessage, code));
                }
            }
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
        /*
         * ServiceExceptions with codes get bumped to the top of the list.
         */
        Collections.sort(exceptions, ServiceExceptionParser::compare);
        /*
         * Now chain them.
         */
        for (int i = 0; i < exceptions.size() - 1; i++) {
            exceptions.get(i).setNext(exceptions.get(i + 1));
        }
        return exceptions.isEmpty() ? null : exceptions.get(0);
    }

    private static String parseServiceExceptionMessage(XMLStreamReader reader)
            throws XMLStreamException {
        reader.require(START_ELEMENT, null, "ServiceException");
        return reader.getElementText();
    }

    private static String parseServiceExceptionCode(XMLStreamReader reader)
            throws XMLStreamException {
        reader.require(START_ELEMENT, null, "ServiceException");
        String value = null;
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            if ("code".equals(reader.getAttributeLocalName(i))) {
                value = reader.getAttributeValue(i);
                break;
            }
        }
        return value;
    }

    private static int sortValue(ServiceException exception) {
        return exception.getCode() != null && !exception.getCode().isEmpty() ? 0 : 1;
    }

    private static int compare(ServiceException exception1, ServiceException exception2) {
        return sortValue(exception1) - sortValue(exception2);
    }
}
