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
package org.geotools.data.ows;


/**
 * Available WMS Operations are listed in a Request element.
 *
 * @author rgould
 *
 * @source $URL$
 */
public class WMSRequest {
    private OperationType getCapabilities;
    private OperationType getMap;
    private OperationType getFeatureInfo;
    private OperationType describeLayer;
    private OperationType getLegendGraphic;
    private OperationType getStyles;
    private OperationType putStyles;

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
    public OperationType getGetMap() {
        return getMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param getMap The getMap to set.
     */
    public void setGetMap(OperationType getMap) {
        this.getMap = getMap;
    }
    public OperationType getDescribeLayer() {
        return describeLayer;
    }
    public void setDescribeLayer( OperationType describeLayer ) {
        this.describeLayer = describeLayer;
    }
    public OperationType getGetLegendGraphic() {
        return getLegendGraphic;
    }
    public void setGetLegendGraphic( OperationType getLegendGraphic ) {
        this.getLegendGraphic = getLegendGraphic;
    }
    public OperationType getGetStyles() {
        return getStyles;
    }
    public void setGetStyles( OperationType getStyles ) {
        this.getStyles = getStyles;
    }
    public OperationType getPutStyles() {
        return putStyles;
    }
    public void setPutStyles( OperationType putStyles ) {
        this.putStyles = putStyles;
    }
}
