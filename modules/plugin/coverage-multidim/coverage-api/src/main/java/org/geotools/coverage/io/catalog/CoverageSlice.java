/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.catalog;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;

/** A slice coverageDescriptor represents a feature in the {@link CoverageSlicesCatalog}. */
public class CoverageSlice {

    // Currently features contain 1 time and 1 elevation attributes.
    // We may consider adding endTime and endElevation attributes too.
    public static class Attributes {

        public static final String INDEX = CoverageSlicesCatalog.IMAGE_INDEX_ATTR;

        public static final String LOCATION = "location";

        public static final String TIME = "time";

        public static final String ELEVATION = "elevation";

        public static final String GEOMETRY = "the_geom";

        public static final String BASE_SCHEMA = GEOMETRY + ":Polygon," + INDEX + ":Integer";

        public static final String BASE_SCHEMA_LOCATION =
                GEOMETRY + ":Polygon," + INDEX + ":Integer," + LOCATION + ":String";

        public static final String DEFAULT_SCHEMA =
                BASE_SCHEMA + "," + TIME + ":java.util.Date," + ELEVATION + ":Double";
    }

    ReferencedEnvelope granuleBBOX;

    SimpleFeature originator;

    /** @param feature */
    public CoverageSlice(final SimpleFeature feature) {
        this.granuleBBOX = ReferencedEnvelope.reference(feature.getBounds());
        this.originator = feature;
    }

    public BoundingBox getGranuleBBOX() {
        return granuleBBOX;
    }

    public SimpleFeature getOriginator() {
        return originator;
    }
}
