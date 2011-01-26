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
package org.geotools.data.wms.request;

import java.net.URL;

/**
 * Provides functionality for a basic GetLegendGraphic request
 * 
 * @author Richard Gould
 * @source $URL$
 */
public abstract class AbstractGetLegendGraphicRequest extends AbstractWMSRequest implements GetLegendGraphicRequest {

    /**
     * @param onlineResource
     */
    public AbstractGetLegendGraphicRequest( URL onlineResource ) {
        super(onlineResource, null);
    }
    
    protected void initRequest() {
        setProperty(REQUEST, "GetLegendGraphic");
    }
    
    protected abstract void initVersion();

    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setLayer(java.lang.String)
     */
    public void setLayer( String layer ) {
        setProperty(LAYER, layer);
    }
    
    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setStyle(java.lang.String)
     */
    public void setStyle( String style ) {
        setProperty(STYLE, style);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setFeatureType(java.lang.String)
     */
    public void setFeatureType( String featureType ) {
        setProperty(FEATURETYPE, featureType);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setRule(java.lang.String)
     */
    public void setRule( String rule ) {
        setProperty(RULE, rule);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setScale(java.lang.String)
     */
    public void setScale( String scale ) {
        setProperty(SCALE, scale);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setSLD(java.lang.String)
     */
    public void setSLD( String sld ) {
        setProperty(SLD, sld);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setSLDBody(java.lang.String)
     */
    public void setSLDBody( String sldBody ) {
        setProperty(SLD_BODY, sldBody);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setFormat(java.lang.String)
     */
    public void setFormat( String format ) {
        setProperty(FORMAT, format);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setWidth(java.lang.String)
     */
    public void setWidth( String width ) {
        setProperty(WIDTH, width);
    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setHeight(java.lang.String)
     */
    public void setHeight( String height ) {
        setProperty(HEIGHT, height);

    }

    /* (non-Javadoc)
     * @see org.geotools.data.wms.request.GetLegendGraphic#setExceptions(java.lang.String)
     */
    public void setExceptions( String exceptions ) {
        setProperty(EXCEPTIONS, exceptions);
    }
}
