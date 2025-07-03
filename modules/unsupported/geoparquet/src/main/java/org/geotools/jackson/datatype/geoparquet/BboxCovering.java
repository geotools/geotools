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
package org.geotools.jackson.datatype.geoparquet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents the bounding box covering structure in GeoParquet metadata, mapping min/max coordinates to column names.
 *
 * <p>This structure is used to map bounding box components (xmin, xmax, ymin, ymax) to specific column names in the
 * dataset, enabling spatial indexing and optimized querying. It also supports optional Z dimension components (zmin,
 * zmax) for 3D geometries.
 */
public class BboxCovering {
    @JsonProperty(value = "xmin", required = true)
    protected List<String> xmin;

    @JsonProperty(value = "xmax", required = true)
    protected List<String> xmax;

    @JsonProperty(value = "ymin", required = true)
    protected List<String> ymin;

    @JsonProperty(value = "ymax", required = true)
    protected List<String> ymax;

    @JsonProperty("zmin")
    protected List<String> zmin;

    @JsonProperty("zmax")
    protected List<String> zmax;

    /**
     * Gets the column names for the minimum X coordinate.
     *
     * @return the list of column names
     */
    public List<String> getXmin() {
        return xmin;
    }

    /**
     * Sets the column names for the minimum X coordinate.
     *
     * @param xmin the list of column names
     */
    public void setXmin(List<String> xmin) {
        this.xmin = xmin;
    }

    /**
     * Gets the column names for the maximum X coordinate.
     *
     * @return the list of column names
     */
    public List<String> getXmax() {
        return xmax;
    }

    /**
     * Sets the column names for the maximum X coordinate.
     *
     * @param xmax the list of column names
     */
    public void setXmax(List<String> xmax) {
        this.xmax = xmax;
    }

    /**
     * Gets the column names for the minimum Y coordinate.
     *
     * @return the list of column names
     */
    public List<String> getYmin() {
        return ymin;
    }

    /**
     * Sets the column names for the minimum Y coordinate.
     *
     * @param ymin the list of column names
     */
    public void setYmin(List<String> ymin) {
        this.ymin = ymin;
    }

    /**
     * Gets the column names for the maximum Y coordinate.
     *
     * @return the list of column names
     */
    public List<String> getYmax() {
        return ymax;
    }

    /**
     * Sets the column names for the maximum Y coordinate.
     *
     * @param ymax the list of column names
     */
    public void setYmax(List<String> ymax) {
        this.ymax = ymax;
    }

    /**
     * Gets the column names for the minimum Z coordinate.
     *
     * @return the list of column names, or null if not defined
     */
    public List<String> getZmin() {
        return zmin;
    }

    /**
     * Sets the column names for the minimum Z coordinate.
     *
     * @param zmin the list of column names
     */
    public void setZmin(List<String> zmin) {
        this.zmin = zmin;
    }

    /**
     * Gets the column names for the maximum Z coordinate.
     *
     * @return the list of column names, or null if not defined
     */
    public List<String> getZmax() {
        return zmax;
    }

    /**
     * Sets the column names for the maximum Z coordinate.
     *
     * @param zmax the list of column names
     */
    public void setZmax(List<String> zmax) {
        this.zmax = zmax;
    }
}
