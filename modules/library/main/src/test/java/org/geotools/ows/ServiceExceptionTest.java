/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xml.sax.SAXException;

/** Tests service exception handling of Java 5 initCause. */
public class ServiceExceptionTest {
    @Test
    public void testCause() {
        Exception throwable = new Exception("Placeholder");

        SAXException sax2 = new SAXException(throwable);
        assertSame(throwable, sax2.getCause());
        assertSame(throwable, sax2.getException());

        // Workaround with ServiceException
        ServiceException serviceException1 =
                (ServiceException) new ServiceException("example").initCause(throwable);
        assertSame(throwable, serviceException1.getCause());
        assertSame(throwable, serviceException1.getException());
    }
}
