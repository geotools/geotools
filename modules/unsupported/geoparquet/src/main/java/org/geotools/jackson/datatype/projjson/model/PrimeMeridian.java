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
 * Represents a Prime Meridian definition in PROJJSON.
 *
 * <p>A prime meridian defines the origin of the longitude values in a geographic CRS. It is defined by its name, the
 * longitude value relative to Greenwich, and the unit of measurement.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrimeMeridian {

    private String name;
    private double longitude;
    private Object unit;
    private Object id;

    /** Creates a new PrimeMeridian with default values. */
    public PrimeMeridian() {
        // Default constructor for Jackson
    }

    /**
     * Gets the name of the prime meridian.
     *
     * @return The prime meridian name, e.g., "Greenwich"
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the prime meridian.
     *
     * @param name The prime meridian name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the longitude value of the prime meridian relative to Greenwich.
     *
     * @return The longitude value in the specified unit
     */
    @JsonProperty("longitude")
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude value of the prime meridian relative to Greenwich.
     *
     * @param longitude The longitude value in the specified unit
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the unit of measurement for the longitude value.
     *
     * <p>This can be either a string (for common units like "degree") or a Unit object with detailed unit information.
     *
     * @return The unit of measurement
     */
    @JsonProperty("unit")
    public Object getUnit() {
        return unit;
    }

    /**
     * Sets the unit of measurement for the longitude value.
     *
     * @param unit The unit of measurement
     */
    public void setUnit(Object unit) {
        this.unit = unit;
    }

    /**
     * Gets the ID of the prime meridian.
     *
     * @return The prime meridian ID
     */
    @JsonProperty("id")
    public Object getId() {
        return id;
    }

    /**
     * Sets the ID of the prime meridian.
     *
     * @param id The prime meridian ID
     */
    public void setId(Object id) {
        this.id = id;
    }
}
