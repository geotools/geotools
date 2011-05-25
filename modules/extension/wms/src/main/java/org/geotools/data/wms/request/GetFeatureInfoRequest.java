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

import java.util.Set;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.Request;



/**
 * Information required for a GetFeatureInfo request.
 
 * <p>
 * Q: queryableLayers is a Set - is this true? Or is order important Q:
 * infoFormats - what does this do? Do these match up with querableLayers? Or
 * is it a list of formats our client is willing to understand?
 * </p>
 *
 * @author Richard Gould, Refractions Research
 *
 * @source $URL$
 */
public interface GetFeatureInfoRequest extends Request{
    /** Represents the INFO_FORMAT parameter */
    public static final String INFO_FORMAT = "INFO_FORMAT"; //$NON-NLS-1$
    /** Represents the FEATURE_COUNT parameter */
    public static final String FEATURE_COUNT = "FEATURE_COUNT"; //$NON-NLS-1$
    /** Represents the X parameter */
    public static final String QUERY_X = "X"; //$NON-NLS-1$
    /** Represents the Y parameter */
    public static final String QUERY_Y = "Y"; //$NON-NLS-1$
    /** Represents the QUERY_LAYERS parameter */
    public static final String QUERY_LAYERS = "QUERY_LAYERS"; //$NON-NLS-1$

    
    /**
     * An unordered set of type Layer. These are the layers that the 
     * GetFeatureInfo request will be performed on. 
     *
     * @param layers A Set of type Layer, each to be queried
     */
    public void setQueryLayers(Set layers);
    
    /**
     * Add a Layer to the set of layers to be queried in the request.
     * This Layer must have queryable set to true.
     * 
     * @param layer a queryable Layer
     */
    public void addQueryLayer(Layer layer);
    
    /**
     * Sets the INFO_FORMAT parameter, which specifies the format of the 
     * GetFeatureInfoResponse. Valid values are available in getInfoFormats()
     * 
     * @param infoFormat a value from getInfoFormats()
     */
    public void setInfoFormat(String infoFormat);
    
    /**
     * @param featureCount the maximum number of features to return in the response
     */
    public void setFeatureCount(String featureCount);
    
    /**
     * @param featureCount the maximum number of features to return in the response
     */
    public void setFeatureCount(int featureCount);
    
    /**
     * The point on the image (in pixels) to be queried. The image is 
     * represented by the GetMapRequest passed into the constructor.
     * 
     * @param x the x point, in pixels
     * @param y the y point, in pixels
     */
    public void setQueryPoint(int x, int y);
}
