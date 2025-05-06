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
 * Represents a Projected Coordinate Reference System in PROJJSON.
 *
 * <p>A projected CRS is derived from a geographic CRS by applying a map projection to transform spherical coordinates
 * into a plane. Coordinates are typically expressed in linear units (meters, feet, etc.).
 *
 * <p>Example PROJJSON representation:
 *
 * <pre>
 * {
 *   "$schema": "https://proj.org/schemas/v0.7/projjson.schema.json",
 *   "type": "ProjectedCRS",
 *   "name": "WGS 84 / UTM zone 31N",
 *   "base_crs": {
 *     "type": "GeographicCRS",
 *     "name": "WGS 84",
 *     "datum": {
 *       "type": "GeodeticReferenceFrame",
 *       "name": "World Geodetic System 1984",
 *       "ellipsoid": {
 *         "name": "WGS 84",
 *         "semi_major_axis": 6378137,
 *         "inverse_flattening": 298.257223563
 *       }
 *     },
 *     "coordinate_system": {
 *       "type": "ellipsoidal",
 *       "axes": [
 *         {
 *           "name": "Geodetic latitude",
 *           "abbreviation": "Lat",
 *           "direction": "north",
 *           "unit": "degree"
 *         },
 *         {
 *           "name": "Geodetic longitude",
 *           "abbreviation": "Lon",
 *           "direction": "east",
 *           "unit": "degree"
 *         }
 *       ]
 *     }
 *   },
 *   "conversion": {
 *     "name": "UTM zone 31N",
 *     "method": {
 *       "name": "Transverse Mercator",
 *       "id": {
 *         "authority": "EPSG",
 *         "code": 9807
 *       }
 *     },
 *     "parameters": [
 *       {
 *         "name": "Latitude of natural origin",
 *         "value": 0,
 *         "unit": "degree"
 *       },
 *       {
 *         "name": "Longitude of natural origin",
 *         "value": 3,
 *         "unit": "degree"
 *       },
 *       {
 *         "name": "Scale factor at natural origin",
 *         "value": 0.9996,
 *         "unit": "unity"
 *       },
 *       {
 *         "name": "False easting",
 *         "value": 500000,
 *         "unit": "metre"
 *       },
 *       {
 *         "name": "False northing",
 *         "value": 0,
 *         "unit": "metre"
 *       }
 *     ]
 *   },
 *   "coordinate_system": {
 *     "type": "Cartesian",
 *     "axes": [
 *       {
 *         "name": "Easting",
 *         "abbreviation": "E",
 *         "direction": "east",
 *         "unit": "metre"
 *       },
 *       {
 *         "name": "Northing",
 *         "abbreviation": "N",
 *         "direction": "north",
 *         "unit": "metre"
 *       }
 *     ]
 *   },
 *   "id": {
 *     "authority": "EPSG",
 *     "code": 32631
 *   }
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectedCRS extends AbstractCRS {

    private CoordinateReferenceSystem baseCrs;
    private Conversion conversion;
    private CoordinateSystem coordinateSystem;

    /** Creates a new ProjectedCRS with default values. */
    public ProjectedCRS() {
        super();
    }

    /**
     * Gets the type of coordinate reference system.
     *
     * @return Always "ProjectedCRS" for this implementation
     */
    @Override
    public String getType() {
        return "ProjectedCRS";
    }

    /**
     * Gets the base CRS that this projected CRS is derived from.
     *
     * @return The base CRS (typically a GeographicCRS)
     */
    @JsonProperty("base_crs")
    public CoordinateReferenceSystem getBaseCrs() {
        return baseCrs;
    }

    /**
     * Sets the base CRS that this projected CRS is derived from.
     *
     * @param baseCrs The base CRS (typically a GeographicCRS)
     */
    public void setBaseCrs(CoordinateReferenceSystem baseCrs) {
        this.baseCrs = baseCrs;
    }

    /**
     * Gets the conversion (map projection) used to derive this CRS from the base CRS.
     *
     * @return The conversion
     */
    @JsonProperty("conversion")
    public Conversion getConversion() {
        return conversion;
    }

    /**
     * Sets the conversion (map projection) used to derive this CRS from the base CRS.
     *
     * @param conversion The conversion
     */
    public void setConversion(Conversion conversion) {
        this.conversion = conversion;
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
