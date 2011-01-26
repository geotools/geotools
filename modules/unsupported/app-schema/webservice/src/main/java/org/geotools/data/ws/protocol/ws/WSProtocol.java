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
package org.geotools.data.ws.protocol.ws;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.data.Query;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FilterCapabilities;

/**
 * 
 * @author rpetty
 * @version $Id$
 * @since 2.6
 * @source $URL:
 *         http://gtsvn.refractions.net/trunk/modules/unsupported/app-schema/webservice/src/main/java/org/geotools/data
 *         /wfs/protocol/wfs/WFSProtocol.java $
 */
public interface WSProtocol {

    /**
     * Returns the set of type names as extracted from the capabilities document, including the
     * namespace and prefix.
     * 
     * @return the set of feature type names as extracted from the capabilities document
     */
    public Set<QName> getFeatureTypeNames();

    public FilterCapabilities getFilterCapabilities();
    
    /**
     * Returns the URL for the given operation name and HTTP protocol as stated in the WFS
     * capabilities.
     * 
     * @return The URL access point for the given operation and method or {@code null} if the
     *         capabilities does not declare an access point for the operation/method combination
     * @see #supportsOperation(WSOperationType)
     */
    public URL getOperationURL();

    /**
     * Issues a GetFeature request for the given request, using POST HTTP method
     * <p>
     */
    public WSResponse issueGetFeature(Query request) throws IOException,
            UnsupportedOperationException;

   public Filter[] splitFilters(Filter filter);
}
