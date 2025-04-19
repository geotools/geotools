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

/**
 * Represents the covering metadata for a geometry column in GeoParquet.
 *
 * <p>Covering metadata provides information about how a dataset's spatial extents may be indexed or partitioned for
 * more efficient access.
 */
public class Covering {
    @JsonProperty(value = "bbox", required = true)
    protected BboxCovering bbox;

    /**
     * Gets the bounding box covering information.
     *
     * @return the bounding box covering
     */
    public BboxCovering getBbox() {
        return bbox;
    }

    /**
     * Sets the bounding box covering information.
     *
     * @param bbox the bounding box covering
     */
    public void setBbox(BboxCovering bbox) {
        this.bbox = bbox;
    }
}
