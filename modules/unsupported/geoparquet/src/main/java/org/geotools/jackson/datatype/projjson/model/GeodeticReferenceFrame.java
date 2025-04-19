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
 * Represents a Geodetic Reference Frame in PROJJSON.
 *
 * <p>A geodetic reference frame is a type of datum that describes the relationship between a coordinate system and the
 * Earth. It includes an ellipsoid model and optionally a prime meridian.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeodeticReferenceFrame {

    private String type;
    private String name;
    private Ellipsoid ellipsoid;
    private PrimeMeridian primeMeridian;
    private Object id;

    /** Creates a new GeodeticReferenceFrame with default values. */
    public GeodeticReferenceFrame() {
        this.type = "GeodeticReferenceFrame";
    }

    /**
     * Gets the type of datum.
     *
     * @return Always "GeodeticReferenceFrame" for this implementation
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * Sets the type of datum.
     *
     * @param type The datum type
     */
    public void setType(String type) {
        if (!"GeodeticReferenceFrame".equals(type)) {
            throw new IllegalArgumentException("Type must be 'GeodeticReferenceFrame' for this class");
        }
        this.type = type;
    }

    /**
     * Gets the name of the geodetic reference frame.
     *
     * @return The reference frame name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the geodetic reference frame.
     *
     * @param name The reference frame name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the ellipsoid that defines the shape of the Earth in this reference frame.
     *
     * @return The ellipsoid
     */
    @JsonProperty("ellipsoid")
    public Ellipsoid getEllipsoid() {
        return ellipsoid;
    }

    /**
     * Sets the ellipsoid that defines the shape of the Earth in this reference frame.
     *
     * @param ellipsoid The ellipsoid
     */
    public void setEllipsoid(Ellipsoid ellipsoid) {
        this.ellipsoid = ellipsoid;
    }

    /**
     * Gets the prime meridian used in this reference frame.
     *
     * @return The prime meridian
     */
    @JsonProperty("prime_meridian")
    public PrimeMeridian getPrimeMeridian() {
        return primeMeridian;
    }

    /**
     * Sets the prime meridian used in this reference frame.
     *
     * @param primeMeridian The prime meridian
     */
    public void setPrimeMeridian(PrimeMeridian primeMeridian) {
        this.primeMeridian = primeMeridian;
    }

    /**
     * Gets the ID of the geodetic reference frame.
     *
     * @return The reference frame ID
     */
    @JsonProperty("id")
    public Object getId() {
        return id;
    }

    /**
     * Sets the ID of the geodetic reference frame.
     *
     * @param id The reference frame ID
     */
    public void setId(Object id) {
        this.id = id;
    }
}
