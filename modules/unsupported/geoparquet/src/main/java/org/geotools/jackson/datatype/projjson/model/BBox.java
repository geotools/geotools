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
 * Represents a bounding box in PROJJSON.
 *
 * <p>In PROJJSON v0.4, a bbox is represented as an object with south_latitude, west_longitude, north_latitude, and
 * east_longitude properties.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BBox {

    private Double southLatitude;
    private Double westLongitude;
    private Double northLatitude;
    private Double eastLongitude;

    /** Creates a new BBox with default values. */
    public BBox() {
        // Default constructor for Jackson
    }

    /**
     * Gets the south latitude of the bounding box.
     *
     * @return The south latitude
     */
    @JsonProperty("south_latitude")
    public Double getSouthLatitude() {
        return southLatitude;
    }

    /**
     * Sets the south latitude of the bounding box.
     *
     * @param southLatitude The south latitude
     */
    public void setSouthLatitude(Double southLatitude) {
        this.southLatitude = southLatitude;
    }

    /**
     * Gets the west longitude of the bounding box.
     *
     * @return The west longitude
     */
    @JsonProperty("west_longitude")
    public Double getWestLongitude() {
        return westLongitude;
    }

    /**
     * Sets the west longitude of the bounding box.
     *
     * @param westLongitude The west longitude
     */
    public void setWestLongitude(Double westLongitude) {
        this.westLongitude = westLongitude;
    }

    /**
     * Gets the north latitude of the bounding box.
     *
     * @return The north latitude
     */
    @JsonProperty("north_latitude")
    public Double getNorthLatitude() {
        return northLatitude;
    }

    /**
     * Sets the north latitude of the bounding box.
     *
     * @param northLatitude The north latitude
     */
    public void setNorthLatitude(Double northLatitude) {
        this.northLatitude = northLatitude;
    }

    /**
     * Gets the east longitude of the bounding box.
     *
     * @return The east longitude
     */
    @JsonProperty("east_longitude")
    public Double getEastLongitude() {
        return eastLongitude;
    }

    /**
     * Sets the east longitude of the bounding box.
     *
     * @param eastLongitude The east longitude
     */
    public void setEastLongitude(Double eastLongitude) {
        this.eastLongitude = eastLongitude;
    }

    /**
     * Converts this BBox object to an array in the format [west, south, east, north].
     *
     * @return An array of coordinates
     */
    public Double[] toArray() {
        return new Double[] {westLongitude, southLatitude, eastLongitude, northLatitude};
    }
}
