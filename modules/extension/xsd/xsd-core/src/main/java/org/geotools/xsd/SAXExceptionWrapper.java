/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.xsd;

import java.io.Serial;
import org.xml.sax.SAXException;

class SAXExceptionWrapper extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public SAXExceptionWrapper(String message, SAXException cause) {
        super(message, cause);
    }

    public SAXExceptionWrapper(SAXException cause) {
        super(cause);
    }
}
