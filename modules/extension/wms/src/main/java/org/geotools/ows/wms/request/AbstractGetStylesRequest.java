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
import org.geotools.ows.wms.Layer;

/**
 * Provides functionality for a basic getStyles request
 *
 * @author Richard Gould
 */
public abstract class AbstractGetStylesRequest extends AbstractWMSRequest
        implements GetStylesRequest {

    private Layer[] layers;

    /** */
    public AbstractGetStylesRequest(URL onlineResource, Properties properties) {
        super(onlineResource, properties);
    }

    protected void initRequest() {
        setProperty(REQUEST, "GetStyles");
    }

    protected abstract void initVersion();

    /* (non-Javadoc)
     * @see GetStylesRequest#setLayers(java.lang.String)
     */
    public void setLayers(String layers) {
        setProperty(LAYERS, layers);
    }

    /* (non-Javadoc)
     * @see GetStylesRequest#setSLDver(java.lang.String)
     */
    public void setSLDver(String sldVer) {
        setProperty(SLDVER, sldVer);
    }

    /* (non-Javadoc)
     * @see GetStylesRequest#getLayers()
     */
    public Layer[] getLayers() {
        return layers;
    }
}
