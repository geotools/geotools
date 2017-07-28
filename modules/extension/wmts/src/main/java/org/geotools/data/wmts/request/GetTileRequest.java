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
package org.geotools.data.wmts.request;

import java.util.Map;
import java.util.Set;
import org.geotools.data.ows.Layer;

import org.geotools.data.ows.Request;
import org.geotools.data.ows.StyleImpl;
import org.geotools.data.wmts.model.WMTSLayer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.tile.Tile;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Construct a WMTS getTile request.
 * 
 * TODO: SHOULD NOT EXTEND GETMAPREQUEST
 * DOC TODO
 *
 * <p>
 * Constructs a getMapRequest based on the following property values:
 * 
 * <ul>
 * <li>ELEVATION</li>
 * <li>TIME</li>
 * <li>EXCEPTIONS</li>
 * <li>BGCOLOR</li>
 * <li>TRANSPARENT</li>
 * <li>WIDTH</li>
 * <li>HEIGHT</li>
 * <li>SRS</li>
 * <li>REQUEST</li>
 * <li>LAYERS</li>
 * <li>STYLES</li>
 * <li><i>vendor specific parameters</i></li>
 * </ul>
 * </p>
 * 
 * <p>
 * Q: List availableFormats and availableExceptions - why are these here? It
 * looks like they are designed to restrict the values used for SRS, format
 * and exceptions. If so the code never uses them. Q: How constant is the
 * GetMapRequest format across WMS versions? Do we need to generalize here?
 * </p>
 *
 * @author Richard Gould, Refractions Research
 * @author ian
 *
 *
 * @source $URL$
 */
public interface GetTileRequest extends Request {

    /**
     * Sets the version number of the request.
     *
     * @param version A String indicating a WMTS Version ("1.0.0", ...)
     */
//    public void setVersion(String version);

    void setLayer(WMTSLayer layer);

    void setStyle(String styleName);

    Set<Tile> getTiles() throws ServiceException;

    void setRequestedHeight(int height);
    void setRequestedWidth(int width);
    void setRequestedBBox(ReferencedEnvelope bbox);
    void setRequestedTime(String time);

    void setCRS(CoordinateReferenceSystem coordinateReferenceSystem);

    /** HTTP headers required for some WMTS **/
    Map<String, String> getHeaders();
}
