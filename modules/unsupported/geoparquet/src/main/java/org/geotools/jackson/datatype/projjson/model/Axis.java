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
 * Represents a coordinate system axis in PROJJSON.
 *
 * <p>An axis defines a single dimension in a coordinate system, including its name, abbreviation, direction, and the
 * unit of measurement used for values along the axis.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Axis {

    private String name;
    private String abbreviation;
    private String direction;
    private Object unit;

    /** Creates a new Axis with default values. */
    public Axis() {
        // Default constructor for Jackson
    }

    /**
     * Gets the name of the axis.
     *
     * @return The axis name, e.g., "Geodetic latitude", "Easting"
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the axis.
     *
     * @param name The axis name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the abbreviation for the axis.
     *
     * @return The axis abbreviation, e.g., "Lat", "E"
     */
    @JsonProperty("abbreviation")
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Sets the abbreviation for the axis.
     *
     * @param abbreviation The axis abbreviation
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Gets the direction of the axis.
     *
     * @return The axis direction, e.g., "north", "east", "up"
     */
    @JsonProperty("direction")
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the axis.
     *
     * @param direction The axis direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Gets the unit of measurement for the axis.
     *
     * <p>This can be either a string (for common units like "degree" or "metre") or a Unit object with detailed unit
     * information.
     *
     * @return The unit of measurement
     */
    @JsonProperty("unit")
    public Object getUnit() {
        return unit;
    }

    /**
     * Sets the unit of measurement for the axis.
     *
     * @param unit The unit of measurement
     */
    public void setUnit(Object unit) {
        this.unit = unit;
    }
}
