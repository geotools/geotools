/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jackson.datatype.projjson.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Vertical Coordinate Reference System in PROJJSON.
 *
 * <p>A Vertical CRS is used for elevation or depth measurements. It consists of a vertical datum and a vertical
 * coordinate system.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerticalCRS extends AbstractCRS {

    private Object datum;
    private CoordinateSystem coordinateSystem;

    /** Creates a new VerticalCRS with default values. */
    public VerticalCRS() {
        super();
    }

    /**
     * Gets the type of coordinate reference system.
     *
     * @return Always "VerticalCRS" for this implementation
     */
    @Override
    public String getType() {
        return "VerticalCRS";
    }

    /**
     * Gets the datum of this vertical CRS.
     *
     * @return The vertical datum
     */
    @JsonProperty("datum")
    public Object getDatum() {
        return datum;
    }

    /**
     * Sets the datum of this vertical CRS.
     *
     * @param datum The vertical datum
     */
    public void setDatum(Object datum) {
        this.datum = datum;
    }

    /**
     * Gets the coordinate system of this vertical CRS.
     *
     * @return The coordinate system
     */
    @JsonProperty("coordinate_system")
    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }

    /**
     * Sets the coordinate system of this vertical CRS.
     *
     * @param coordinateSystem The coordinate system
     */
    public void setCoordinateSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }
}
