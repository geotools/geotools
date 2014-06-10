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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.operation.MathTransform2D;

public class RasterLayerResponseTest {

    private final static double DELTA = 1E-5;

    @Test
    public void testHeterogeneous() throws Exception {

        final URL testMosaic = TestData.url(this, "heterogeneous");
        ImageMosaicReader reader = null;
        try {

            reader = (ImageMosaicReader) new ImageMosaicFormat().getReader(testMosaic, null);

            final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                    .createValue();
            final GeneralEnvelope envelope = reader.getOriginalEnvelope();
            final Dimension dim = new Dimension();
            dim.setSize(10, 20);
            final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
            rasterArea.setSize(dim);
            final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
            gg.setValue(new GridGeometry2D(range, envelope));

            final RasterManager manager = reader.getRasterManager(reader.getGridCoverageNames()[0]);
            final RasterLayerRequest request = new RasterLayerRequest(
                    new GeneralParameterValue[] { gg }, manager);
            final RasterLayerResponse response = new RasterLayerResponse(request, manager);
            final Class<?> c = response.getClass();

            // Trigger the grid to world computations
            Method method = c.getDeclaredMethod("prepareResponse");
            method.setAccessible(true);
            method.invoke(response);

            Field finalGridToWorldCorner = c.getDeclaredField("finalGridToWorldCorner");
            finalGridToWorldCorner.setAccessible(true);
            MathTransform2D transform = (MathTransform2D) finalGridToWorldCorner.get(response);
            AffineTransform2D affineTransform = (AffineTransform2D) transform;
            assertEquals(18, XAffineTransform.getScaleX0(affineTransform), DELTA);
            assertEquals(18, XAffineTransform.getScaleY0(affineTransform), DELTA);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {

                }
            }
        }
    }
}
