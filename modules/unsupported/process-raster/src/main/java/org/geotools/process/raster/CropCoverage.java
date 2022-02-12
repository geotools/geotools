/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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
package org.geotools.process.raster;

import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.ProgressListener;

/**
 * Crops the coverage along the specified
 *
 * @author Andrea Aime - GeoSolutions
 * @author ETj <etj at geo-solutions.it>
 */
@DescribeProcess(
        title = "Crop Coverage",
        description = "Returns the portion of a raster bounded by a given geometry.")
public class CropCoverage implements RasterProcess {

    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();
    // private static final Operation CROP = PROCESSOR.getOperation("CoverageCrop");

    @DescribeResult(name = "result", description = "Cropped raster")
    public GridCoverage2D execute(
            @DescribeParameter(name = "coverage", description = "Input raster")
                    GridCoverage2D coverage,
            @DescribeParameter(name = "cropShape", description = "Geometry used to crop the raster")
                    Geometry cropShape,
            ProgressListener progressListener)
            throws IOException {
        // get the bounds
        CoordinateReferenceSystem crs;
        if (cropShape.getUserData() instanceof CoordinateReferenceSystem) {
            crs = (CoordinateReferenceSystem) cropShape.getUserData();
        } else {
            // assume the geometry is in the same crs
            crs = coverage.getCoordinateReferenceSystem();
        }
        GeneralEnvelope bounds =
                new GeneralEnvelope(new ReferencedEnvelope(cropShape.getEnvelopeInternal(), crs));

        // force it to a collection if necessary
        GeometryCollection roi;
        if (!(cropShape instanceof GeometryCollection)) {
            roi = cropShape.getFactory().createGeometryCollection(new Geometry[] {cropShape});
        } else {
            roi = (GeometryCollection) cropShape;
        }

        // perform the crops
        final ParameterValueGroup param = PROCESSOR.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("Envelope").setValue(bounds);
        param.parameter("ROI").setValue(roi);

        Hints hints = null;
        if (new ImageWorker(coverage.getRenderedImage()).getNoData() == null)
            hints = new Hints(ImageWorker.FORCE_MOSAIC_ROI_PROPERTY, true);

        return (GridCoverage2D) PROCESSOR.doOperation(param, hints);
    }
}
