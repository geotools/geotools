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
package org.geotools.wfs.v1_0;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.filter.v1_0.OGC;

/**
 * XSD for wfs 1.0; except for WFS 1.0 capabilities document, for which {@link WFSCapabilities}
 * shall be used, as its based on a different schema.
 *
 * @author Justin Deoliveira, OpenGEO
 */
public final class WFS extends org.geotools.wfs.WFS {

    /** @generated */
    public static final QName WFS_TransactionResponseType =
            new QName("http://www.opengis.net/wfs", "WFS_TransactionResponseType");

    /** @generated */
    public static final QName WFS_TransactionResponse =
            new QName("http://www.opengis.net/wfs", "WFS_TransactionResponse");

    /** @generated */
    public static final QName WFS_LockFeatureResponseType =
            new QName("http://www.opengis.net/wfs", "WFS_LockFeatureResponseType");

    /** @generated */
    public static final QName WFS_LockFeatureResponse =
            new QName("http://www.opengis.net/wfs", "WFS_LockFeatureResponse");

    /** @generated */
    public static final QName InsertResultType =
            new QName("http://www.opengis.net/wfs", "InsertResultType");

    public static final QName TransactionResultType =
            new QName("http://www.opengis.net/wfs", "TransactionResultType");

    /** singleton instance */
    private static final WFS instance = new WFS();

    /** Returns the singleton instance. */
    public static final WFS getInstance() {
        return instance;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void addDependencies(Set dependencies) {
        dependencies.add(OGC.getInstance());
    }

    /** Returns the location of 'WFS-transaction.xsd.'. */
    public String getSchemaLocation() {
        return getClass().getResource("WFS-transaction.xsd").toString();
    }

    /** Returns '1.0.0'. */
    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
