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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * Base interface for all coordinate reference system types in PROJJSON.
 *
 * <p>This interface represents the common structure of a coordinate reference system in the PROJJSON format. Specific
 * implementations handle different CRS types.
 *
 * <p>According to the PROJJSON schema, a CRS can be one of:
 *
 * <ul>
 *   <li>GeographicCRS - A CRS based on a geographic coordinate system
 *   <li>ProjectedCRS - A CRS based on a projected coordinate system
 *   <li>VerticalCRS - A CRS for elevation or depth measurements
 *   <li>CompoundCRS - A CRS that combines multiple component CRSs
 *   <li>TemporalCRS - A CRS for time measurements
 *   <li>EngineeringCRS - A non-Earth-referenced CRS
 *   <li>ParametricCRS - A CRS for parameter measurements
 *   <li>DerivedGeographicCRS - A CRS derived from a GeographicCRS
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = GeographicCRS.class, name = "GeographicCRS"),
    @JsonSubTypes.Type(value = ProjectedCRS.class, name = "ProjectedCRS"),
    @JsonSubTypes.Type(value = CompoundCRS.class, name = "CompoundCRS"),
    @JsonSubTypes.Type(value = BoundCRS.class, name = "BoundCRS"),
    @JsonSubTypes.Type(value = Transformation.class, name = "Transformation"),
    @JsonSubTypes.Type(value = VerticalCRS.class, name = "VerticalCRS")
})
public interface CoordinateReferenceSystem {

    /**
     * Gets the type of coordinate reference system.
     *
     * @return The CRS type string
     */
    @JsonProperty("type")
    String getType();

    /**
     * Gets the name of the coordinate reference system.
     *
     * @return The CRS name
     */
    @JsonProperty("name")
    String getName();

    /**
     * Gets the ID of the coordinate reference system.
     *
     * <p>According to the PROJJSON v0.7 schema, ID is an Identifier object with required "authority" and "code"
     * properties.
     *
     * @return The CRS Identifier
     */
    @JsonProperty("id")
    Identifier getId();

    /**
     * Gets the scope of the coordinate reference system.
     *
     * @return The CRS scope
     */
    @JsonProperty("scope")
    String getScope();

    /**
     * Gets the remarks about the coordinate reference system.
     *
     * @return The CRS remarks
     */
    @JsonProperty("remarks")
    String getRemarks();

    /**
     * Gets the area of use for this coordinate reference system.
     *
     * @return The area of use
     */
    @JsonProperty("area")
    String getArea();

    /**
     * Gets the bounding box that describes the area of use.
     *
     * @return The bounding box as an Object which can be either a List<Double> (v0.7 schema) or a BBox object (v0.4
     *     schema)
     */
    @JsonProperty("bbox")
    Object getBbox();
}
