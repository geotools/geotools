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
import org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem;
import org.locationtech.jts.geom.Envelope;

/**
 * Represents the metadata for a geometry column in a GeoParquet file, including required encoding and geometry types,
 * and optional attributes like bounding box, CRS, and edge type.
 */
public class Geometry {
    @JsonProperty(value = "encoding", required = true)
    protected String encoding;

    @JsonProperty(value = "geometry_types", required = true)
    protected List<String> geometryTypes;

    @JsonProperty("crs")
    protected CoordinateReferenceSystem crs;

    @JsonProperty("edges")
    protected String edges;

    @JsonProperty("orientation")
    protected String orientation;

    @JsonProperty("bbox")
    protected List<Double> bbox;

    @JsonProperty("epoch")
    protected Double epoch;

    @JsonProperty("covering")
    protected Covering covering;

    /**
     * Creates an envelope from the bounding box information.
     *
     * @return the envelope representing this geometry's bounds
     */
    public Envelope bounds() {
        List<Double> bb = getBbox();
        if (bb == null || bb.size() < 4) {
            return new Envelope();
        }
        double minx = bb.get(0);
        double miny = bb.get(1);
        double maxx = bb.get(bb.size() == 6 ? 4 : 2);
        double maxy = bb.get(bb.size() == 6 ? 5 : 3);
        return new Envelope(minx, maxx, miny, maxy);
    }

    /**
     * Gets the encoding format for this geometry column.
     *
     * @return the encoding format (e.g., "WKB", "point", etc.)
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the encoding format for this geometry column.
     *
     * @param encoding the encoding format
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Gets the geometry types supported in this column.
     *
     * @return the list of geometry types
     */
    public List<String> getGeometryTypes() {
        return geometryTypes;
    }

    /**
     * Sets the geometry types supported in this column.
     *
     * @param geometryTypes the list of geometry types
     */
    public void setGeometryTypes(List<String> geometryTypes) {
        this.geometryTypes = geometryTypes;
    }

    /**
     * Gets the coordinate reference system for this geometry column.
     *
     * <p>The CRS is represented using a strongly-typed PROJJSON model object that follows the v0.7 schema as defined by
     * the OGC GeoParquet specification. This includes proper handling of CRS identifiers with authority and code
     * properties.
     *
     * <p>This method returns the CRS as parsed from the GeoParquet metadata 'geo' field. If no CRS information is
     * available in the metadata, this method returns null.
     *
     * @return the CRS as a strongly-typed PROJJSON model object, or null if not specified
     */
    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    /**
     * Sets the coordinate reference system for this geometry column.
     *
     * <p>The CRS should be provided as a strongly-typed PROJJSON model object following the v0.7 schema as defined by
     * the OGC GeoParquet specification. This includes proper handling of CRS identifiers with authority and code
     * properties.
     *
     * <p>This method allows setting the CRS information that will be used when building GeoParquet metadata for the
     * 'geo' field.
     *
     * @param crs the CRS as a strongly-typed PROJJSON model object
     */
    public void setCrs(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    /**
     * Gets the edge type for this geometry.
     *
     * @return the edge type (e.g., "spherical", "planar")
     */
    public String getEdges() {
        return edges;
    }

    /**
     * Sets the edge type for this geometry.
     *
     * @param edges the edge type
     */
    public void setEdges(String edges) {
        this.edges = edges;
    }

    /**
     * Gets the orientation for this geometry.
     *
     * @return the orientation (e.g., "counterclockwise")
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation for this geometry.
     *
     * @param orientation the orientation
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     * Gets the bounding box for this geometry column.
     *
     * @return the bounding box coordinates (minx, miny, [minz], maxx, maxy, [maxz])
     */
    public List<Double> getBbox() {
        return bbox;
    }

    /**
     * Sets the bounding box for this geometry column.
     *
     * @param bbox the bounding box coordinates
     */
    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    /**
     * Gets the epoch for this geometry's CRS.
     *
     * @return the epoch value
     */
    public Double getEpoch() {
        return epoch;
    }

    /**
     * Sets the epoch for this geometry's CRS.
     *
     * @param epoch the epoch value
     */
    public void setEpoch(Double epoch) {
        this.epoch = epoch;
    }

    /**
     * Gets the covering metadata for this geometry column.
     *
     * @return the covering metadata
     */
    public Covering getCovering() {
        return covering;
    }

    /**
     * Sets the covering metadata for this geometry column.
     *
     * @param covering the covering metadata
     */
    public void setCovering(Covering covering) {
        this.covering = covering;
    }
}
