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
package org.geotools.coverageio.gdal.envihdr;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.coverageio.gdal.GDALTestCase;
import org.geotools.geometry.GeneralBounds;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mathew Wyatt, CSIRO Australia
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class EnviHdrTest extends GDALTestCase {

    /** file name of a valid EnviHdr sample data to be used for tests. */
    private static final String fileName = "envihdr.dat";

    /** Creates a new instance of {@code EnviHdrTest} */
    public EnviHdrTest() {
        super("EnviHdr", new EnviHdrFormatFactory());
    }

    @Test
    public void test() throws Exception {
        File file = null;
        try {
            file = TestData.file(this, fileName);
        } catch (IOException fnfe) {
            LOGGER.warning("test-data not found: " + fileName + "\nTests are skipped");
            return;
        }
        // Preparing an useful layout in case the image is striped.
        final ImageLayout l = new ImageLayout();
        l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(512).setTileWidth(512);

        Hints hints = new Hints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

        // get a reader
        final URL url = file.toURI().toURL();
        final Object source = url;
        final BaseGDALGridCoverage2DReader reader = new EnviHdrReader(source, hints);
        // Testing the getSource method
        Assert.assertEquals(reader.getSource(), source);

        // /////////////////////////////////////////////////////////////////////
        //
        // read once
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D gc = reader.read();
        forceDataLoading(gc);

        // /////////////////////////////////////////////////////////////////////
        //
        // read again with subsampling and crop
        //
        // /////////////////////////////////////////////////////////////////////
        final double cropFactor = 2.0;
        final Rectangle range = (GridEnvelope2D) reader.getOriginalGridRange();
        final GeneralBounds oldEnvelope = reader.getOriginalEnvelope();
        final GeneralBounds cropEnvelope = new GeneralBounds(
                new double[] {
                    oldEnvelope.getLowerCorner().getOrdinate(0) + oldEnvelope.getSpan(0) / cropFactor,
                    oldEnvelope.getLowerCorner().getOrdinate(1) + oldEnvelope.getSpan(1) / cropFactor
                },
                new double[] {
                    oldEnvelope.getUpperCorner().getOrdinate(0),
                    oldEnvelope.getUpperCorner().getOrdinate(1)
                });
        cropEnvelope.setCoordinateReferenceSystem(reader.getCoordinateReferenceSystem());

        final ParameterValue gg = ((AbstractGridFormat) reader.getFormat()).READ_GRIDGEOMETRY2D.createValue();
        gg.setValue(new GridGeometry2D(
                new GridEnvelope2D(new Rectangle(
                        0, 0, (int) (range.width / 4.0 / cropFactor), (int) (range.height / 4.0 / cropFactor))),
                cropEnvelope));
        gc = reader.read(new GeneralParameterValue[] {gg});
        Assert.assertNotNull(gc);
        // NOTE: in some cases might be too restrictive
        Assert.assertTrue(cropEnvelope.equals(
                gc.getEnvelope(),
                XAffineTransform.getScale((AffineTransform) gc.getGridGeometry().getGridToCRS2D()) / 2,
                true));

        forceDataLoading(gc);
    }

    @Test
    public void testIsAvailable() throws NoSuchAuthorityCodeException, FactoryException {
        GridFormatFinder.scanForPlugins();

        Iterator list = GridFormatFinder.getAvailableFormats().iterator();
        boolean found = false;
        GridFormatFactorySpi fac = null;

        while (list.hasNext()) {
            fac = (GridFormatFactorySpi) list.next();

            if (fac instanceof EnviHdrFormatFactory) {
                found = true;

                break;
            }
        }

        Assert.assertTrue("EnviHdrFormatFactory not registered", found);
        Assert.assertTrue("EnviHdrFormatFactory not available", fac.isAvailable());
        Assert.assertNotNull(new EnviHdrFormatFactory().createFormat());
    }

    @Test
    public void testDimensionNames() throws Exception {
        String fileName = "multiband.bsq";
        File file = null;
        try {
            file = TestData.file(this, fileName);
        } catch (IOException fnfe) {
            LOGGER.warning("test-data not found: " + fileName + "\nTests are skipped");
            return;
        }
        // Preparing an useful layout in case the image is striped.
        final ImageLayout l = new ImageLayout();
        l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(512).setTileWidth(512);

        Hints hints = new Hints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

        // get a reader
        final URL url = file.toURI().toURL();
        final Object source = url;
        final BaseGDALGridCoverage2DReader reader = new EnviHdrReader(source, hints);
        // Testing the getSource method
        Assert.assertEquals(reader.getSource(), source);

        // /////////////////////////////////////////////////////////////////////
        //
        // read once
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D gc = reader.read();
        forceDataLoading(gc);

        GridSampleDimension[] sampleDimensions = gc.getSampleDimensions();
        Assert.assertEquals(9, sampleDimensions.length);
        for (int i = 0; i < sampleDimensions.length; i++) {
            Assert.assertEquals(
                    "Band" + (i + 1), sampleDimensions[i].getDescription().toString());
        }
    }
}
