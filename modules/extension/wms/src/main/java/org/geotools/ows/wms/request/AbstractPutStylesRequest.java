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
package org.geotools.ows.wms.request;

import java.net.URL;
import java.util.Properties;

/**
 * Presents basic functionality for a PutStyles request
 *
 * @author Richard Gould
 */
public abstract class AbstractPutStylesRequest extends AbstractWMSRequest
        implements PutStylesRequest {

    /** */
    public AbstractPutStylesRequest(URL onlineResource, Properties properties) {
        super(onlineResource, properties);
    }

    protected void initRequest() {
        setProperty(REQUEST, "PutStyles");
    }

    protected abstract void initVersion();

    /** @see PutStylesRequest#setMode(java.lang.String) */
    public void setMode(String mode) {
        setProperty(MODE, mode);
    }

    /* (non-Javadoc)
     * @see PutStylesRequest#setSLD(java.lang.String)
     */
    public void setSLD(String sld) {
        setProperty(SLD, sld);
    }

    /* (non-Javadoc)
     * @see PutStylesRequest#setSLDBody(java.lang.String)
     */
    public void setSLDBody(String sldBody) {
        setProperty(SLD_BODY, sldBody);
    }
}
