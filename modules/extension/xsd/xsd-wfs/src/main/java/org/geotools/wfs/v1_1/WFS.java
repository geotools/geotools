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
package org.geotools.wfs.v1_1;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xsd.ows.OWS;

/**
 * XSD for wfs 1.1.
 *
 * @author Justin Deoliveira, OpenGEO
 */
public final class WFS extends org.geotools.wfs.WFS {

    /** @generated */
    public static final QName InsertResultsType =
            new QName("http://www.opengis.net/wfs", "InsertResultsType");

    /** @generated */
    public static final QName LockFeatureResponseType =
            new QName("http://www.opengis.net/wfs", "LockFeatureResponseType");

    /** @generated */
    public static final QName TransactionResponseType =
            new QName("http://www.opengis.net/wfs", "TransactionResponseType");

    /** @generated */
    public static final QName LockFeatureResponse =
            new QName("http://www.opengis.net/wfs", "LockFeatureResponse");

    /** @generated */
    public static final QName TransactionResponse =
            new QName("http://www.opengis.net/wfs", "TransactionResponse");

    /** singleton instance */
    private static final WFS instance = new WFS();

    /** Returns the singleton instance. */
    public static final WFS getInstance() {
        return instance;
    }

    @Override
    protected void addDependencies(Set dependencies) {
        dependencies.add(OWS.getInstance());
        dependencies.add(OGC.getInstance());
    }

    /** Returns the location of 'wfs.xsd.'. */
    public String getSchemaLocation() {
        return getClass().getResource("wfs.xsd").toString();
    }

    /** Returns '1.1.0'. */
    @Override
    public String getVersion() {
        return "1.1.0";
    }
}
