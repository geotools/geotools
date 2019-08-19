/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.ows.wmts.request;

import java.util.Map;
import java.util.Set;
import org.geotools.data.ows.Request;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.tile.Tile;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Construct a WMTS getTile request.
 *
 * <p>(Based on existing work by rgould for WMS service)
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public interface GetTileRequest extends Request {

    void setLayer(WMTSLayer layer);

    void setStyle(String styleName);

    Set<Tile> getTiles() throws ServiceException;

    void setRequestedHeight(int height);

    void setRequestedWidth(int width);

    void setRequestedBBox(ReferencedEnvelope bbox);

    void setRequestedTime(String time);

    void setCRS(CoordinateReferenceSystem coordinateReferenceSystem);

    /** HTTP headers required for some WMTS * */
    Map<String, String> getHeaders();
}
