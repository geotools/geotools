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
 * Represents a Bound Coordinate Reference System in PROJJSON.
 *
 * <p>A bound CRS is a CRS that is related to another CRS (the source CRS) through a transformation to a target CRS. For
 * example, a local engineering CRS can be bound to WGS84 to provide global context.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundCRS extends AbstractCRS {

    private CoordinateReferenceSystem sourceCrs;
    private CoordinateReferenceSystem targetCrs;
    private Transformation transformation;

    /** Creates a new BoundCRS with default values. */
    public BoundCRS() {
        super();
    }

    /**
     * Gets the type of coordinate reference system.
     *
     * @return Always "BoundCRS" for this implementation
     */
    @Override
    public String getType() {
        return "BoundCRS";
    }

    /**
     * Gets the source CRS that is being bound.
     *
     * @return The source CRS
     */
    @JsonProperty("source_crs")
    public CoordinateReferenceSystem getSourceCrs() {
        return sourceCrs;
    }

    /**
     * Sets the source CRS that is being bound.
     *
     * @param sourceCrs The source CRS
     */
    public void setSourceCrs(CoordinateReferenceSystem sourceCrs) {
        this.sourceCrs = sourceCrs;
    }

    /**
     * Gets the target CRS that the source CRS is bound to.
     *
     * @return The target CRS
     */
    @JsonProperty("target_crs")
    public CoordinateReferenceSystem getTargetCrs() {
        return targetCrs;
    }

    /**
     * Sets the target CRS that the source CRS is bound to.
     *
     * @param targetCrs The target CRS
     */
    public void setTargetCrs(CoordinateReferenceSystem targetCrs) {
        this.targetCrs = targetCrs;
    }

    /**
     * Gets the transformation between the source and target CRSs.
     *
     * @return The transformation
     */
    @JsonProperty("transformation")
    public Transformation getTransformation() {
        return transformation;
    }

    /**
     * Sets the transformation between the source and target CRSs.
     *
     * @param transformation The transformation
     */
    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }
}
