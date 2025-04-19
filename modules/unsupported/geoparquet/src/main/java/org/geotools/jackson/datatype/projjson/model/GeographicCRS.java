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
 * Represents a Geographic Coordinate Reference System in PROJJSON.
 *
 * <p>A geographic CRS is a coordinate reference system based on an ellipsoidal approximation of the Earth's surface.
 * Coordinates are expressed in angular units (degrees or radians).
 *
 * <p>Example PROJJSON representation:
 *
 * <pre>
 * {
 *   "$schema": "https://proj.org/schemas/v0.7/projjson.schema.json",
 *   "type": "GeographicCRS",
 *   "name": "WGS 84",
 *   "datum": {
 *     "type": "GeodeticReferenceFrame",
 *     "name": "World Geodetic System 1984",
 *     "ellipsoid": {
 *       "name": "WGS 84",
 *       "semi_major_axis": 6378137,
 *       "inverse_flattening": 298.257223563
 *     }
 *   },
 *   "coordinate_system": {
 *     "type": "ellipsoidal",
 *     "axes": [
 *       {
 *         "name": "Geodetic latitude",
 *         "abbreviation": "Lat",
 *         "direction": "north",
 *         "unit": "degree"
 *       },
 *       {
 *         "name": "Geodetic longitude",
 *         "abbreviation": "Lon",
 *         "direction": "east",
 *         "unit": "degree"
 *       }
 *     ]
 *   },
 *   "id": {
 *     "authority": "EPSG",
 *     "code": 4326
 *   }
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeographicCRS extends AbstractCRS {

    private GeodeticReferenceFrame datum;
    private CoordinateSystem coordinateSystem;

    /** Creates a new GeographicCRS with default values. */
    public GeographicCRS() {
        super();
    }

    /**
     * Gets the type of coordinate reference system.
     *
     * @return Always "GeographicCRS" for this implementation
     */
    @Override
    public String getType() {
        return "GeographicCRS";
    }

    /**
     * Gets the geodetic datum defining this geographic CRS.
     *
     * @return The geodetic reference frame
     */
    @JsonProperty("datum")
    public GeodeticReferenceFrame getDatum() {
        return datum;
    }

    /**
     * Sets the geodetic datum defining this geographic CRS.
     *
     * @param datum The geodetic reference frame
     */
    public void setDatum(GeodeticReferenceFrame datum) {
        this.datum = datum;
    }

    /**
     * Gets the coordinate system that describes how coordinates are expressed.
     *
     * @return The coordinate system
     */
    @JsonProperty("coordinate_system")
    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }

    /**
     * Sets the coordinate system that describes how coordinates are expressed.
     *
     * @param coordinateSystem The coordinate system
     */
    public void setCoordinateSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }
}
