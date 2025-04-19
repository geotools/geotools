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
 * Represents a Compound Coordinate Reference System in PROJJSON.
 *
 * <p>A compound CRS is composed of multiple independent coordinate reference systems, typically combining horizontal
 * and vertical components. For example, a 3D CRS might combine a 2D geographic/projected CRS with a vertical CRS.
 *
 * <p>Example PROJJSON representation:
 *
 * <pre>
 * {
 *   "$schema": "https://proj.org/schemas/v0.7/projjson.schema.json",
 *   "type": "CompoundCRS",
 *   "name": "NAD83 / UTM zone 17N + NAVD88 height",
 *   "components": [
 *     {
 *       "type": "ProjectedCRS",
 *       "name": "NAD83 / UTM zone 17N",
 *       ...
 *     },
 *     {
 *       "type": "VerticalCRS",
 *       "name": "NAVD88 height",
 *       ...
 *     }
 *   ],
 *   "id": {
 *     "authority": "EPSG",
 *     "code": 6349
 *   }
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompoundCRS extends AbstractCRS {

    private List<CoordinateReferenceSystem> components;

    /** Creates a new CompoundCRS with default values. */
    public CompoundCRS() {
        super();
    }

    /**
     * Gets the type of coordinate reference system.
     *
     * @return Always "CompoundCRS" for this implementation
     */
    @Override
    public String getType() {
        return "CompoundCRS";
    }

    /**
     * Gets the component CRSs that make up this compound CRS.
     *
     * @return The list of component CRSs
     */
    @JsonProperty("components")
    public List<CoordinateReferenceSystem> getComponents() {
        return components;
    }

    /**
     * Sets the component CRSs that make up this compound CRS.
     *
     * @param components The list of component CRSs
     */
    public void setComponents(List<CoordinateReferenceSystem> components) {
        this.components = components;
    }
}
