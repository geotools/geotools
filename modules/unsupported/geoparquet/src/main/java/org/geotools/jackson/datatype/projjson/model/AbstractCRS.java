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
 * Abstract base class for all coordinate reference system implementations.
 *
 * <p>This class implements common properties defined in the PROJJSON schema that are shared across all CRS types.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractCRS implements CoordinateReferenceSystem {

    private String name;
    private Identifier id;
    private String scope;
    private String remarks;
    private String area;
    private List<Double> bbox;

    /** Creates a new AbstractCRS with default values. */
    protected AbstractCRS() {
        // Default constructor for Jackson
    }

    /**
     * Gets the name of the coordinate reference system.
     *
     * @return The CRS name
     */
    @Override
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the coordinate reference system.
     *
     * @param name The CRS name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the ID of the coordinate reference system.
     *
     * <p>According to the PROJJSON v0.7 schema, ID is an Identifier object with required "authority" and "code"
     * properties.
     *
     * @return The CRS Identifier
     */
    @Override
    @JsonProperty("id")
    public Identifier getId() {
        return id;
    }

    /**
     * Sets the ID of the coordinate reference system.
     *
     * @param id The CRS Identifier
     */
    public void setId(Identifier id) {
        this.id = id;
    }

    /**
     * Gets the scope of the coordinate reference system.
     *
     * @return The CRS scope
     */
    @Override
    @JsonProperty("scope")
    public String getScope() {
        return scope;
    }

    /**
     * Sets the scope of the coordinate reference system.
     *
     * @param scope The CRS scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Gets the remarks about the coordinate reference system.
     *
     * @return The CRS remarks
     */
    @Override
    @JsonProperty("remarks")
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the remarks about the coordinate reference system.
     *
     * @param remarks The CRS remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * Gets the area of use for this coordinate reference system.
     *
     * @return The area of use
     */
    @Override
    @JsonProperty("area")
    public String getArea() {
        return area;
    }

    /**
     * Sets the area of use for this coordinate reference system.
     *
     * @param area The area of use
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * Gets the bounding box that describes the area of use.
     *
     * @return The bounding box coordinates [west, south, east, north]
     */
    @Override
    @JsonProperty("bbox")
    public List<Double> getBbox() {
        return bbox;
    }

    /**
     * Sets the bounding box that describes the area of use.
     *
     * @param bbox The bounding box coordinates [west, south, east, north]
     */
    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }
}
