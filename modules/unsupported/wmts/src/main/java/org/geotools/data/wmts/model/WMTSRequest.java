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
package org.geotools.data.wmts.model;

import org.geotools.data.ows.OperationType;

/**
 * Available WMS Operations are listed in a Request element.
 *
 * @author rgould
 *
 *
 * @source $URL$
 */
public class WMTSRequest {
    private OperationType getCapabilities;

    private OperationType getFeatureInfo;
    private OperationType getLegendGraphic;
    private OperationType getTile;

    /**
     * DOCUMENT ME!
     *
     * @return Returns the getCapabilities.
     */
    public OperationType getGetCapabilities() {
        return getCapabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @param getCapabilities The getCapabilities to set.
     */
    public void setGetCapabilities(OperationType getCapabilities) {
        this.getCapabilities = getCapabilities;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the getFeatureInfo.
     */
    public OperationType getGetFeatureInfo() {
        return getFeatureInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param getFeatureInfo The getFeatureInfo to set.
     */
    public void setGetFeatureInfo(OperationType getFeatureInfo) {
        this.getFeatureInfo = getFeatureInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the getMap.
     */
    public OperationType getGetTile() {
        return getTile;
    }

    /**
     * DOCUMENT ME!
     *
     * @param getMap The getMap to set.
     */
    public void setGetTile(OperationType getTile) {
        this.getTile = getTile;
    }
    
}
