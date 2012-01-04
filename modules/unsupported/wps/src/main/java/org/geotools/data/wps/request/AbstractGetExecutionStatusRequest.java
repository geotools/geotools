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
package org.geotools.data.wps.request;

import java.net.URL;
import java.util.Properties;


/**
 * Describes an abstract GetExecutionStatus request. Provides everything except
 * the versioning info, which subclasses must implement.
 *
 * @author afabiani
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/wps/src/main/java/org/geotools/data/wps/request/AbstractGetExecutionStatusRequest $
 */
public abstract class AbstractGetExecutionStatusRequest extends AbstractWPSRequest implements GetExecutionStatusRequest
{

    /**
     * Constructs a basic GetExecutionStatusRequest, without versioning info.
     *
     * @param onlineResource the location of the request
     * @param properties a set of properties to use. Can be null.
     */
    public AbstractGetExecutionStatusRequest(URL onlineResource, Properties properties)
    {
        super(onlineResource, properties);
    }

    protected void initRequest()
    {
        setProperty(REQUEST, "GetExecutionStatus");
    }

    /**
    * @see org.geotools.data.wps.request.GetExecutionStatusRequest#setIdentifier(java.lang.String)
    */
    public void setIdentifier(String identifiers)
    {
        setProperty(IDENTIFIER, identifiers);
    }

    protected abstract void initVersion();
}
