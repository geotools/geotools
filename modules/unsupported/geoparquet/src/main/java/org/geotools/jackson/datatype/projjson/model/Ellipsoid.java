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
 * Represents an ellipsoid definition in PROJJSON.
 *
 * <p>An ellipsoid is a mathematical model of the shape of the Earth (or other celestial body). It is defined by a
 * semi-major axis (equatorial radius) and either a semi-minor axis (polar radius) or an inverse flattening value.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ellipsoid {

    private String name;
    private double semiMajorAxis;
    private Double semiMinorAxis;
    private Double inverseFlattening;
    private Object id;

    /** Creates a new Ellipsoid with default values. */
    public Ellipsoid() {
        // Default constructor for Jackson
    }

    /**
     * Gets the name of the ellipsoid.
     *
     * @return The ellipsoid name, e.g., "WGS 84"
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the ellipsoid.
     *
     * @param name The ellipsoid name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the semi-major axis (equatorial radius) of the ellipsoid.
     *
     * @return The semi-major axis length in meters
     */
    @JsonProperty("semi_major_axis")
    public double getSemiMajorAxis() {
        return semiMajorAxis;
    }

    /**
     * Sets the semi-major axis (equatorial radius) of the ellipsoid.
     *
     * @param semiMajorAxis The semi-major axis length in meters
     */
    public void setSemiMajorAxis(double semiMajorAxis) {
        this.semiMajorAxis = semiMajorAxis;
    }

    /**
     * Gets the semi-minor axis (polar radius) of the ellipsoid.
     *
     * @return The semi-minor axis length in meters, or null if not specified
     */
    @JsonProperty("semi_minor_axis")
    public Double getSemiMinorAxis() {
        return semiMinorAxis;
    }

    /**
     * Sets the semi-minor axis (polar radius) of the ellipsoid.
     *
     * @param semiMinorAxis The semi-minor axis length in meters
     */
    public void setSemiMinorAxis(Double semiMinorAxis) {
        this.semiMinorAxis = semiMinorAxis;
    }

    /**
     * Gets the inverse flattening value of the ellipsoid.
     *
     * @return The inverse flattening value, or null if not specified
     */
    @JsonProperty("inverse_flattening")
    public Double getInverseFlattening() {
        return inverseFlattening;
    }

    /**
     * Sets the inverse flattening value of the ellipsoid.
     *
     * @param inverseFlattening The inverse flattening value
     */
    public void setInverseFlattening(Double inverseFlattening) {
        this.inverseFlattening = inverseFlattening;
    }

    /**
     * Gets the ID of the ellipsoid.
     *
     * @return The ellipsoid ID
     */
    @JsonProperty("id")
    public Object getId() {
        return id;
    }

    /**
     * Sets the ID of the ellipsoid.
     *
     * @param id The ellipsoid ID
     */
    public void setId(Object id) {
        this.id = id;
    }
}
