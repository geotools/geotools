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

import org.geotools.data.ows.Request;

/**
 * A request used to get the legend graphic for a given layer
 * 
 * @author Richard Gould
 * @source $URL$
 */
public interface GetLegendGraphicRequest extends Request {
    /* Parameters */
    public static final String LAYER = "LAYER";
    public static final String STYLE = "STYLE";
    public static final String FEATURETYPE = "FEATURETYPE";
    public static final String RULE = "RULE";
    public static final String SCALE = "SCALE";
    public static final String SLD = "SLD";
    public static final String SLD_BODY = "SLD_BODY";
    public static final String FORMAT = "FORMAT";
    public static final String WIDTH = "WIDTH";
    public static final String HEIGHT = "HEIGHT";
    public static final String EXCEPTIONS = "EXCEPTIONS";
    
    public void setLayer(String layer);
        
    public void setStyle(String style);
    
    public void setFeatureType(String featureType);
    
    public void setRule(String rule);
    
    public void setScale(String scale);
    
    public void setSLD(String sld);
    
    public void setSLDBody(String sldBody);
    
    public void setFormat(String format);
    
    public void setWidth(String width);
    
    public void setHeight(String height);
    
    public void setExceptions(String exceptions);
}
