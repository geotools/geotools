/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.elasticsearch;

import com.github.davidmoten.geo.GeoHash;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.Operations;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;

class GridCoverageUtil {

    public static GridCoverage2D scale(GridCoverage2D coverage, float width, float height) {
        final RenderedImage renderedImage = coverage.getRenderedImage();
        final Raster renderedGrid = renderedImage.getData();
        float yScale = width / renderedGrid.getWidth();
        float xScale = height / renderedGrid.getHeight();

        final Operations ops = new Operations(null);
        return (GridCoverage2D) ops.scale(coverage, xScale, yScale, 0, 0);
    }

    public static GridCoverage2D crop(GridCoverage2D coverage, Envelope envelope) {
        final CoverageProcessor processor = new CoverageProcessor();

        final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();

        final GeneralEnvelope crop = new GeneralEnvelope(envelope);
        param.parameter("Source").setValue(coverage);
        param.parameter("Envelope").setValue(crop);

        return (GridCoverage2D) processor.doOperation(param);
    }

    /**
     * Adds a buffer of one row and column around the envelope, respecting the latitude max values.
     * Since it's called only with reprojection enabled, assuming the original envelope is in the
     * normal range of lat and lon
     */
    public static ReferencedEnvelope pad(
            org.locationtech.jts.geom.Envelope envelope, int precision) {
        double cellWidth = GeoHash.widthDegrees(precision);
        double cellHeight = GeoHash.widthDegrees(precision);
        double minLon = Math.max(-180 + cellWidth / 2, envelope.getMinX() - cellWidth);
        double maxLon = Math.min(180 - cellWidth / 2, envelope.getMaxX() + cellWidth);
        double minLat = Math.max(-90 + cellHeight / 2, envelope.getMinY() - cellHeight);
        double maxLat = Math.min(90 - cellHeight / 2, envelope.getMaxY() + cellHeight);
        return new ReferencedEnvelope(minLon, maxLon, minLat, maxLat, DefaultGeographicCRS.WGS84);
    }
}
