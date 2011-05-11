/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid;

import org.geotools.grid.ortholine.OrthoLineFeatureBuilder;
import java.util.Collection;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.ortholine.OrthoLineBuilder;
import org.geotools.grid.ortholine.OrthoLineDef;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * A utility class to create line grids with basic attributes. 
 * @author mbedward
 * @since 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/grid/src/main/java/org/geotools/grid/Grids.java $
 * @version $Id: Grids.java 37149 2011-05-10 11:47:02Z mbedward $
 */
public class Lines {

    /**
     * Creates a grid of ortho-lines. Lines are parallel to the bounding envelope's
     * X-axis, Y-axis or both according to the provided line definitions.
     * 
     * @param bounds the bounding envelope
     * @param lineDefs one or more ortho-line definitions
     * @return the vector grid of lines
     * 
     * @see OrthoLineDef
     */
    public static SimpleFeatureSource createOrthoLines(ReferencedEnvelope bounds,
            Collection<OrthoLineDef> lineDefs) {

        return createOrthoLines(bounds, lineDefs, 0.0);
    }

    /**
     * Creates a grid of ortho-lines. Lines are parallel to the bounding envelope's
     * X-axis, Y-axis or both according to the provided line definitions.
     * Densified lines (lines strings with additional vertices along their length) can be
     * created by setting the value of {@code vertexSpacing} greater than zero; if so, any
     * lines more than twice as long as this value will be densified.
     * 
     * @param bounds the bounding envelope
     * @param lineDefs one or more ortho-line definitions
     * @param vertexSpacing maximum distance between adjacent vertices along a line
     * @return the vector grid of lines
     */
    public static SimpleFeatureSource createOrthoLines(ReferencedEnvelope bounds,
            Collection<OrthoLineDef> lineDefs, double vertexSpacing) {

        return createOrthoLines(bounds, lineDefs, vertexSpacing,
                new OrthoLineFeatureBuilder(bounds.getCoordinateReferenceSystem()));
    }

    /**
     * Creates a grid of ortho-lines. Lines are parallel to the bounding envelope's
     * X-axis, Y-axis or both according to the provided line definitions. 
     * Line features will be created using the supplied feature builder.
     * Densified lines (lines strings with additional vertices along their length) can be
     * created by setting the value of {@code vertexSpacing} greater than zero; if so, any
     * lines more than twice as long as this value will be densified.
     * 
     * @param bounds the bounding envelope
     * @param lineDefs one or more ortho-line definitions
     * @param vertexSpacing maximum distance between adjacent vertices along a line
     * @param lineFeatureBuilder feature build to create line features
     * @return the vector grid of lines
     */
    public static SimpleFeatureSource createOrthoLines(ReferencedEnvelope bounds, 
            Collection<OrthoLineDef> lineDefs,
            double vertexSpacing,
            GridFeatureBuilder lineFeatureBuilder) {

        if (bounds == null || bounds.isEmpty() || bounds.isNull()) {
            throw new IllegalArgumentException("The bounds should not be null or empty");
        }

        if (lineDefs == null || lineDefs.isEmpty()) {
            throw new IllegalArgumentException("One or more line controls must be provided");
        }

        CoordinateReferenceSystem boundsCRS = bounds.getCoordinateReferenceSystem();
        CoordinateReferenceSystem builderCRS = 
                lineFeatureBuilder.getType().getCoordinateReferenceSystem();
        if (boundsCRS != null && builderCRS != null &&
                !CRS.equalsIgnoreMetadata(boundsCRS, builderCRS)) {
            throw new IllegalArgumentException("Different CRS set for bounds and the feature builder");
        }

        final SimpleFeatureCollection fc = new ListFeatureCollection(lineFeatureBuilder.getType());
        OrthoLineBuilder lineBuilder = new OrthoLineBuilder(bounds);
        lineBuilder.buildGrid(lineDefs, lineFeatureBuilder, vertexSpacing, fc);
        return DataUtilities.source(fc);
    }

}
