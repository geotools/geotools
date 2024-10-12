/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertTrue;

import java.io.File;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralBounds;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.Test;

public class RasterLayerRequestTest {

    @Test
    public void testResolutions() throws Exception {
        // get some test data
        final File testMosaic = TestData.file(this, "/overview/0");
        assertTrue(testMosaic.exists());

        // build the objects we need to get to build a raster layer request
        final ImageMosaicReader reader = new ImageMosaicFormat().getReader(testMosaic, null);
        final RasterManager manager = reader.getRasterManager(reader.getGridCoverageNames()[0]);

        GeneralBounds oe = reader.getOriginalEnvelope();
        double offset = oe.getSpan(0) * 0.9;
        GeneralBounds reNative = new GeneralBounds(oe);
        reNative.setRange(0, oe.getMinimum(0) - offset, oe.getMaximum(0) - offset);
        Bounds reTransformed = CRS.transform(reNative, CRS.decode("EPSG:3857", true));
        // System.out.println(reader.getOriginalGridRange());
        // the raster has bands like this, let's ask for the coarser grained overview
        // Band 1 Block=140x58 Type=Byte, ColorInterp=Gray
        // Overviews: 70x94, 35x47
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 35, 47), reTransformed);

        ParameterValue<GridGeometry2D> ggParam = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParam.setValue(gg);

        // Creating a request
        final RasterLayerRequest request = new RasterLayerRequest(new GeneralParameterValue[] {ggParam}, manager);
        double[] rr = request.spatialRequestHelper.getComputedResolution();
        // System.out.println(Arrays.toString(rr));
        double resolution = Math.min(rr[0], rr[1]);
        // System.out.println(resolution);
        // native resolution is ~16, overviews are at 32 and 64, the request really needs 64
        assertTrue(resolution > 60);
        reader.dispose();
    }
}
