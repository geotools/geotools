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
import java.util.List;

/**
 * Represents a coordinate system definition in PROJJSON.
 *
 * <p>A coordinate system defines the axes, their order, orientation, and units for a coordinate reference system.
 * Common types include ellipsoidal (for geographic CRS), Cartesian (for projected CRS), and vertical.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoordinateSystem {

    private String type;
    private String subtype;
    private List<Axis> axes;

    /** Creates a new CoordinateSystem with default values. */
    public CoordinateSystem() {
        // Default constructor for Jackson
    }

    /**
     * Gets the type of coordinate system.
     *
     * @return The coordinate system type, e.g., "ellipsoidal", "Cartesian", "vertical"
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * Sets the type of coordinate system.
     *
     * @param type The coordinate system type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the subtype of coordinate system (v0.4 schema).
     *
     * @return The coordinate system subtype
     */
    @JsonProperty("subtype")
    public String getSubtype() {
        return subtype;
    }

    /**
     * Sets the subtype of coordinate system (v0.4 schema).
     *
     * @param subtype The coordinate system subtype
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     * Gets the axes that define this coordinate system.
     *
     * @return The list of coordinate system axes
     */
    @JsonProperty("axes")
    public List<Axis> getAxes() {
        return axes;
    }

    /**
     * Alternative getter for axes to support v0.4 schema which uses "axis" instead of "axes".
     *
     * @return The list of coordinate system axes
     */
    @JsonProperty("axis")
    public List<Axis> getAxis() {
        return axes;
    }

    /**
     * Sets the axes that define this coordinate system.
     *
     * @param axes The list of coordinate system axes
     */
    public void setAxes(List<Axis> axes) {
        this.axes = axes;
    }

    /**
     * Alternative setter for axes to support v0.4 schema which uses "axis" instead of "axes".
     *
     * @param axes The list of coordinate system axes
     */
    public void setAxis(List<Axis> axes) {
        this.axes = axes;
    }
}
