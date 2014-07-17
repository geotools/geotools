/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

/**
 * A slice coverageDescriptor represents a feature in the {@link CoverageSlicesCatalog}.
 */
public class CoverageSlice {

    // Currently features contain 1 time and 1 elevation attributes. 
    // We may consider adding endTime and endElevation attributes too.
    public static class Attributes {

        public final static String INDEX = CoverageSlicesCatalog.IMAGE_INDEX_ATTR;

        public final static String TIME = "time";

        public final static String ELEVATION = "elevation";

        public final static String GEOMETRY = "the_geom";

        public static final String BASE_SCHEMA = GEOMETRY + ":Polygon," + INDEX + ":Integer";

        public static final String DEFAULT_SCHEMA = BASE_SCHEMA+"," + TIME + ":java.util.Date," + ELEVATION + ":Double";
    }

    ReferencedEnvelope granuleBBOX;

    SimpleFeature originator;

    /**
     * 
     * @param feature
     * @param suggestedSPI
     * @param pathType
     * @param locationAttribute
     * @param parentLocation
     */
    public CoverageSlice(final SimpleFeature feature/*, final ImageReaderSpi suggestedSPI*/) {
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
