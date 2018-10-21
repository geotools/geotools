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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.ows.ServiceException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

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
     * @param inputStream
     * @throws JDOMException
     * @throws IOException
     */
    public static ServiceException parse(InputStream inputStream)
            throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        builder.setExpandEntities(false);
        Document document = builder.build(inputStream);

        Element root = document.getRootElement();
        List<Element> serviceExceptions = root.getChildren("ServiceException");

        /*
         * ServiceExceptions with codes get bumped to the top of the list.
         */
        List<ServiceException> parsedExceptions =
                serviceExceptions
                        .stream()
                        .map(ServiceExceptionParser::parseSE)
                        .sorted(ServiceExceptionParser::compare)
                        .collect(Collectors.toList());
        /*
         * Now chain them.
         */
        ServiceException firstException = null;
        ServiceException recentException = null;
        for (ServiceException exception : parsedExceptions) {
            if (firstException == null) {
                firstException = exception;
                recentException = exception;
            } else {
                recentException.setNext(exception);
                recentException = exception;
            }
        }

        return firstException;
    }

    private static ServiceException parseSE(Element element) {
        String errorMessage = element.getText();
        String code = element.getAttributeValue("code");

        return new ServiceException(errorMessage, code);
    }

    private static int sortValue(ServiceException exception) {
        return exception.getCode() != null && !exception.getCode().isEmpty() ? 0 : 1;
    }

    private static int compare(ServiceException exception1, ServiceException exception2) {
        return sortValue(exception1) - sortValue(exception2);
    }
}
