/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.sfs;

import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Stores a single layer information from the capabilities
 */
class SFSLayer {

    Name typeName;

    boolean xyOrder;

    String layerSRS;

    CoordinateReferenceSystem coordinateReferenceSystem;

    Envelope bounds;

    public SFSLayer(Name typeName, boolean layerAxisOrder, String layerSRS,
            CoordinateReferenceSystem crs, Envelope bounds) {
        super();
        this.typeName = typeName;
        this.xyOrder = layerAxisOrder;
        this.layerSRS = layerSRS;
        this.coordinateReferenceSystem = crs;
        this.bounds = bounds;
    }

    public Name getTypeName() {
        return typeName;
    }

    public void setTypeName(Name typeName) {
        this.typeName = typeName;
    }

    public boolean isXYOrder() {
        return xyOrder;
    }

    public void setXyOrder(boolean layerAxisOrder) {
        this.xyOrder = layerAxisOrder;
    }

    public String getLayerSRS() {
        return layerSRS;
    }

    public void setLayerSRS(String layerSRS) {
        this.layerSRS = layerSRS;
    }

    public Envelope getBounds() {
        return bounds;
    }

    public void setBounds(Envelope bounds) {
        this.bounds = bounds;
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return coordinateReferenceSystem;
    }

    public void setCoordinateReferenceSystem(CoordinateReferenceSystem coordinateReferenceSystem) {
        this.coordinateReferenceSystem = coordinateReferenceSystem;
    }
}
