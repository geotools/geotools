/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.geotools.ows.wms.Layer;

/** A base class for GetFeatureInfoRequests that provides some functionality. */
public abstract class AbstractGetFeatureInfoRequest extends AbstractWMSRequest
        implements GetFeatureInfoRequest {
    /** A set of type Layer, each of which is to be queried in the request */
    private Set queryLayers;

    /**
     * Constructs a GetFeatureInfoRequest. It will set the REQUEST and VERSION parameters,
     * over-writing and values set there previously.
     *
     * @param onlineResource the URL pointing to the place to execute a GetFeatureInfo request
     * @param request a previously configured GetMapRequest that the query will be executed on
     */
    public AbstractGetFeatureInfoRequest(URL onlineResource, GetMapRequest request) {
        super(onlineResource, getMapProperties(request));

        queryLayers = new TreeSet();
    }

    /**
     * GetMap request fills in the LAYERS and STYLES properties only when getFinalURL() is called,
     * so we force it to fill them in before copying the properties
     */
    static Properties getMapProperties(GetMapRequest request) {
        request.getFinalURL();
        return request.getProperties();
    }

    /** @see org.geotools.data.wms.request.Request#getFinalURL() */
    public URL getFinalURL() {
        Iterator iter = queryLayers.iterator();
        String initialQueryLayerString =
                properties.getProperty(QUERY_LAYERS) == null
                        ? ""
                        : properties.getProperty(QUERY_LAYERS); // $NON-NLS-1$
        String queryLayerString =
                properties.getProperty(QUERY_LAYERS) == null
                        ? ""
                        : properties.getProperty(QUERY_LAYERS); // $NON-NLS-1$

        while (iter.hasNext()) {
            Layer layer = (Layer) iter.next();
            try {
                // spaces are converted to plus signs, but must be %20 for url calls [GEOT-4317]
                queryLayerString =
                        queryLayerString
                                + URLEncoder.encode(layer.getName(), "UTF-8")
                                        .replaceAll("\\+", "%20");
            } catch (UnsupportedEncodingException | NullPointerException e) {
                queryLayerString = queryLayerString + layer.getName();
            }

            if (iter.hasNext()) {
                queryLayerString = queryLayerString + ","; // $NON-NLS-1$
            }
        }

        setProperty(QUERY_LAYERS, queryLayerString);
        URL url = super.getFinalURL();

        setProperty(QUERY_LAYERS, initialQueryLayerString);

        return url;
    }

    /** @see GetFeatureInfoRequest#addQueryLayer(Layer) */
    public void addQueryLayer(Layer layer) {
        queryLayers.add(layer);
    }

    /** @see GetFeatureInfoRequest#setQueryLayers(java.util.Set) */
    public void setQueryLayers(Set layers) {
        queryLayers = layers;
    }

    /** @see GetFeatureInfoRequest#setInfoFormat(java.lang.String) */
    public void setInfoFormat(String infoFormat) {
        setProperty(INFO_FORMAT, infoFormat);
    }

    /** @see GetFeatureInfoRequest#setFeatureCount(java.lang.String) */
    public void setFeatureCount(String featureCount) {
        setProperty(FEATURE_COUNT, featureCount);
    }

    /** @see GetFeatureInfoRequest#setFeatureCount(int) */
    public void setFeatureCount(int featureCount) {
        setFeatureCount(Integer.toString(featureCount));
    }

    /** @see GetFeatureInfoRequest#setQueryPoint(int, int) */
    public void setQueryPoint(int x, int y) {
        setProperty(getQueryX(), Integer.toString(x));
        setProperty(getQueryY(), Integer.toString(y));
    }

    /**
     * Created because the 1.3.0 spec changes this parameter name. The 1.3.0 spec should over-ride
     * this method.
     *
     * @return a String representing the x-axis query point
     */
    protected String getQueryX() {
        return QUERY_X;
    }

    /**
     * Created because the 1.3.0 spec changes this parameter name. The 1.3.0 spec should over-ride
     * this method.
     *
     * @return a String representing the y-axis query point
     */
    protected String getQueryY() {
        return QUERY_Y;
    }

    protected void initRequest() {
        setProperty(REQUEST, "feature_info"); // $NON-NLS-1$
    }

    protected abstract void initVersion();
}
