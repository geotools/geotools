/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.imagemosaic.granulecollector.DefaultSubmosaicProducerFactory;
import org.geotools.geometry.GeneralBounds;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.test.TestData;
import org.junit.Test;

public class RasterLayerResponseTest {

    private static final double DELTA = 1E-5;

    @Test
    public void testHeterogeneous() throws Exception {

        final URL testMosaic = TestData.url(this, "heterogeneous");
        ImageMosaicReader reader = null;
        try {

            reader = new ImageMosaicFormat().getReader(testMosaic);

            final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            final GeneralBounds envelope = reader.getOriginalEnvelope();
            final Dimension dim = new Dimension();
            dim.setSize(10, 20);
            final Rectangle rasterArea = (GridEnvelope2D) reader.getOriginalGridRange();
            rasterArea.setSize(dim);
            final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
            GridGeometry2D gridGeometryValue = new GridGeometry2D(range, envelope);
            gg.setValue(gridGeometryValue);

            final RasterManager manager = reader.getRasterManager(reader.getGridCoverageNames()[0]);
            final RasterLayerRequest request = new RasterLayerRequest(new GeneralParameterValue[] {gg}, manager);
            final RasterLayerResponse response =
                    new RasterLayerResponse(request, manager, new DefaultSubmosaicProducerFactory());
            final Class<?> c = response.getClass();

            // Trigger the grid to world computations
            Method method = c.getDeclaredMethod("prepareResponse");
            method.setAccessible(true);
            method.invoke(response);

            Field finalGridToWorldCorner = c.getDeclaredField("finalGridToWorldCorner");
            finalGridToWorldCorner.setAccessible(true);
            MathTransform2D transform = (MathTransform2D) finalGridToWorldCorner.get(response);
            AffineTransform2D affineTransform = (AffineTransform2D) transform;
            AffineTransform2D gridToCRS = (AffineTransform2D) gridGeometryValue.getGridToCRS2D();

            // heteroegenous mode, the response code should not be picking a target resolution, just
            // reflect the requested one and let the GranuleDescriptor own overview controller pick
            // the best one for that granule
            assertEquals(gridToCRS.getScaleX(), XAffineTransform.getScaleX0(affineTransform), DELTA);
            assertEquals(Math.abs(gridToCRS.getScaleY()), XAffineTransform.getScaleY0(affineTransform), DELTA);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {

                }
            }
        }
    }

    /**
     * Test that {@link GridCoverage2DReader#SOURCE_URL_PROPERTY} is correctly set on a coverage created by
     * {@link RasterLayerResponse}.
     */
    @Test
    public void testSourceUrl() throws Exception {
        final URL testMosaic = TestData.url(this, "heterogeneous");
        ImageMosaicReader reader = null;
        try {
            reader = new ImageMosaicFormat().getReader(testMosaic, null);
            ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            GeneralBounds envelope = reader.getOriginalEnvelope();
            Dimension dim = new Dimension();
            dim.setSize(10, 20);
            Rectangle rasterArea = (GridEnvelope2D) reader.getOriginalGridRange();
            rasterArea.setSize(dim);
            GridEnvelope2D range = new GridEnvelope2D(rasterArea);
            gg.setValue(new GridGeometry2D(range, envelope));
            RasterManager manager = reader.getRasterManager(reader.getGridCoverageNames()[0]);
            RasterLayerRequest request = new RasterLayerRequest(new GeneralParameterValue[] {gg}, manager);
            RasterLayerResponse response =
                    new RasterLayerResponse(request, manager, new DefaultSubmosaicProducerFactory());
            GridCoverage2D coverage = response.createResponse();
            URL sourceUrl = (URL) coverage.getProperty(GridCoverage2DReader.SOURCE_URL_PROPERTY);
            assertNotNull(sourceUrl);
            assertEquals("file", sourceUrl.getProtocol());
            assertTrue(sourceUrl.getPath().endsWith(".tif"));
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // we tried
                }
            }
        }
    }
}
