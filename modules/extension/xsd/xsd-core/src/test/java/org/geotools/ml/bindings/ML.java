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
package org.geotools.ml.bindings;

import javax.xml.namespace.QName;
import org.geotools.xml.XSD;


/**
 * This interface contains the qualified names of all the types in the
 * http://mails/refractions/net schema.
 *
 * @generated
 *
 * @source $URL$
 */
public class ML extends XSD {
    /**
     * singleton instance
     */
    private static ML instance = new ML();
    public static final String NAMESPACE = "http://mails/refractions/net";
    public static final QName ATTACHMENTTYPE = new QName("http://mails/refractions/net",
            "attachmentType");
    public static final QName BODYTYPE = new QName("http://mails/refractions/net", "bodyType");
    public static final QName ENVELOPETYPE = new QName("http://mails/refractions/net",
            "envelopeType");
    public static final QName MAILSTYPE = new QName("http://mails/refractions/net", "mailsType");
    public static final QName MAILTYPE = new QName("http://mails/refractions/net", "mailType");
    public static final QName MIMETOPLEVELTYPE = new QName("http://mails/refractions/net",
            "mimeTopLevelType");

    /**
     * private constructor.
     */
    private ML() {
    }

    /**
     * the singleton instance.
     */
    public static ML getInstance() {
        return instance;
    }

    /**
     * Returns 'http://mails/refractions/net'.
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns the location of 'mails.xsd'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("mails.xsd").toString();
    }
}
