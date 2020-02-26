/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map;

import java.util.logging.Level;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.Style;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Layer used to draw a raster {@link GridCoverage}.
 *
 * <p>Direct access to the {@link GridCoverage} is available using {@link #getCoverage()}, the
 * outline of the raster is also available via {@link #toFeatureCollection()} for vector based
 * rendering systems.
 *
 * @author Jody Garnett
 * @version 8.0
 * @since 2.7
 */
public class GridCoverageLayer extends RasterLayer {
    /** Grid Coverage to be drawn. */
    protected GridCoverage2D coverage;

    /**
     * Create layer to draw the provided grid coverage.
     *
     * @param coverage The new layer that has been added.
     */
    public GridCoverageLayer(GridCoverage2D coverage, Style style) {
        super(style);
        this.coverage = coverage;
    }
    /** Create layer to draw the provided grid coverage. */
    public GridCoverageLayer(GridCoverage2D coverage, Style style, String title) {
        super(style, title);
        this.coverage = coverage;
    }

    @Override
    public void dispose() {
        preDispose();
        if (coverage != null) {
            try {
                coverage.dispose(true);
            } catch (Exception e) {
                // eat me
            }
            coverage = null;
        }
        if (style != null) {
            style = null;
        }
        super.dispose();
    }

    /**
     * Access to the grid coverage being drawn.
     *
     * @return grid coverage being drawn.
     */
    public GridCoverage2D getCoverage() {
        return coverage;
    }
    /**
     * Layer bounds generated from the grid coverage.
     *
     * @return layer bounds generated from the grid coverage.
     */
    public ReferencedEnvelope getBounds() {
        if (coverage != null) {
            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
            Envelope2D bounds = coverage.getEnvelope2D();
            if (bounds != null) {
                return new ReferencedEnvelope(bounds);
            } else if (crs != null) {
                return new ReferencedEnvelope(crs);
            }
        }
        return null;
    }

    public SimpleFeatureCollection toFeatureCollection() {
        SimpleFeatureCollection collection;
        try {
            collection = FeatureUtilities.wrapGridCoverage(coverage);
            return collection;
        } catch (Exception e) {
            LOGGER.log(Level.FINER, "Coverage could not be converted to FeatureCollection", e);
            return null;
        }
    }
}
