/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.Viewer;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.junit.Test;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.TransformException;

/** Tests the BandMerge operation. */
public final class BandMergeTest extends GridProcessingTestBase {

    /** Tests the "BandMerge" operation */
    @Test
    public void testBandMerge() throws TransformException, IOException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        /*
         * Get the source coverage and build the rgb version.
         */
        GridCoverage2D source = EXAMPLES.get(4);
        Envelope originalEnvelope = source.getEnvelope();
        final List<GridCoverage2D> coverages = new ArrayList<GridCoverage2D>();
        final RenderedImage byteImage =
                new ImageWorker(source.getRenderedImage()).rescaleToBytes().getRenderedImage();
        source =
                CoverageFactoryFinder.getGridCoverageFactory(null)
                        .create("sample", byteImage, source.getEnvelope());

        coverages.add(source);
        coverages.add(source);
        coverages.add(source);

        /*
         * Do the band merge
         */
        ParameterValueGroup param = processor.getOperation("BandMerge").getParameters();
        param.parameter("sources").setValue(coverages);
        GridCoverage2D merged = (GridCoverage2D) processor.doOperation(param);
        NoDataContainer noData = CoverageUtilities.getNoDataProperty(merged);

        if (SHOW) {
            Viewer.show(source);
            Viewer.show(merged);

        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(merged.getRenderedImage()).getTiles());
        }

        RenderedImage raster = merged.getRenderedImage();
        assertEquals(3, raster.getSampleModel().getNumBands());
        assertNotNull(raster.getColorModel());

        // Checking the envelope isn't shifted and the images have same size
        assertEquals(originalEnvelope, merged.getEnvelope());
        assertEquals(byteImage.getWidth(), raster.getWidth());
        assertEquals(byteImage.getHeight(), raster.getHeight());
        assertEquals(byteImage.getMinX(), raster.getMinX());
        assertEquals(byteImage.getMinY(), raster.getMinY());
    }
}
