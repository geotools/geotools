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
package org.geotools.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Wraps a WMS layer into a {@link MapLayer} for interactive rendering usage TODO: expose a
 * GetFeatureInfo that returns a feature collection TODO: expose the list of named styles and allow
 * choosing which style to use
 * 
 * @author Andrea Aime - OpenGeo
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/wms/src/main/java/org/geotools/map/WMSMapLayer.java $
 */
public class WMSMapLayer extends MapLayer {
    
    /** Internal wmsLayer used for delegate operations */
    private org.geotools.map.WMSLayer wmsLayer;
    /**
     * Builds a new WMS layer
     * 
     * @param wms
     * @param layer
     */
    public WMSMapLayer(WebMapServer wms, Layer layer) {
        super( new WMSLayer( wms, layer ) );
        this.wmsLayer = (WMSLayer) this.internal;
    }

    /**
     * 
     * @see org.geotools.map.Layer#dispose()
     */
    public void dispose() {
        wmsLayer.dispose();
    }
    
    public synchronized ReferencedEnvelope getBounds() {
        return wmsLayer.getBounds();
    }

    /**
     * Retrieves the feature info as text (assuming "text/plain" is a supported feature info format)
     * 
     * @param pos
     *            the position to be checked, in real world coordinates
     * @return
     * @throws IOException
     */
    public String getFeatureInfoAsText(DirectPosition2D pos, int featureCount) throws IOException {
        return wmsLayer.getFeatureInfoAsText(pos, featureCount);
    }

    /**
     * Retrieves the feature info as a generic input stream, it's the duty of the caller to
     * interpret the contents and ensure the stream is closed feature info format)
     * 
     * @param pos
     *            the position to be checked, in real world coordinates
     * @param infoFormat
     *            The INFO_FORMAT parameter in the GetFeatureInfo request
     * @return
     * @throws IOException
     */
    public InputStream getFeatureInfo(DirectPosition2D pos, String infoFormat, int featureCount)
            throws IOException {
        return getFeatureInfo(pos, infoFormat, featureCount);
    }

    /**
     * Allows to run a standalone GetFeatureInfo request, without the need to have previously run a
     * GetMap request on this layer. Mostly useful for stateless users that rebuild the map context
     * for each rendering operation (e.g., GeoServer)
     * 
     * @param pos
     * @param infoFormat
     *            The INFO_FORMAT parameter in the GetFeatureInfo request
     * @return
     * @throws IOException
     */
    public InputStream getFeatureInfo(ReferencedEnvelope bbox, int width, int height, int x, int y,
            String infoFormat, int featureCount) throws IOException {
        return wmsLayer.getFeatureInfo(bbox, width, height, x, y, infoFormat, featureCount);
    }

    /**
     * Returns the {@link WebMapServer} used by this layer
     * 
     * @return
     */
    public WebMapServer getWebMapServer() {
        return wmsLayer.getWebMapServer();
    }

    /**
     * Returns the WMS {@link Layer} used by this layer
     * 
     * @return
     */
    public List<Layer> getWMSLayers() {
        return wmsLayer.getWMSLayers();
    }

    /**
     * Returns the CRS used to make requests to the remote WMS
     * 
     * @return
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return wmsLayer.getCoordinateReferenceSystem();
    }

    /**
     * Returns last GetMap request performed by this layer
     * 
     * @return
     */
    public GetMapRequest getLastGetMap() {
        return wmsLayer.getLastGetMap();
    }

    /**
     * Allows to add another WMS layer into the GetMap requests
     * 
     * @param layer
     */
    public void addLayer(Layer layer) {
        wmsLayer.addLayer(layer);
    }

    /**
     * Returns true if the specified CRS can be used directly to perform WMS requests. Natively
     * supported crs will provide the best rendering quality as no client side reprojection is
     * necessary, the image coming from the WMS server will be used as-is
     * 
     * @param crs
     * @return
     */
    public boolean isNativelySupported(CoordinateReferenceSystem crs) {
        return wmsLayer.isNativelySupported(crs);
    }
}
