/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.coverageio.gdal.srp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.eclipse.imagen.ImageLayout;
import org.eclipse.imagen.JAI;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.coverageio.gdal.GDALTestCase;
import org.geotools.geometry.GeneralBounds;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.junit.Test;

/**
 * @author Andrea Aime, GeoSolutions
 *     <p>Testing {@link SRPReader}
 */
public final class SRPTest extends GDALTestCase {

    /** file name of a valid VRT file to be used for tests. */
    private static final String fileName = "FKUSRP01.IMG";

    public SRPTest() {
        super("SRP", new SRPFormatFactory());
    }

    @Test
    public void testService() throws NoSuchAuthorityCodeException, FactoryException {
        GridFormatFinder.scanForPlugins();

        Iterator<GridFormatFactorySpi> list =
                GridFormatFinder.getAvailableFormats().iterator();
        boolean found = false;
        GridFormatFactorySpi fac = null;

        while (list.hasNext()) {
            fac = list.next();

            if (fac instanceof SRPFormatFactory) {
                found = true;

                break;
            }
        }

        assertTrue("SRPFormatFactory not registered", found);
        assertTrue("SRPFormatFactory not available", fac.isAvailable());
        assertNotNull(new SRPFormatFactory().createFormat());
    }

    @Test
    public void testFile() throws Exception {
        final File file = TestData.file(this, fileName);
        checkSource(file);
    }

    @Test
    public void testPath() throws Exception {
        final File file = TestData.file(this, fileName);
        checkSource(file.getAbsolutePath());
    }

    public void checkSource(Object source) throws IOException {
        // Preparing an useful layout in case the image is striped.
        final ImageLayout l = new ImageLayout();
        l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(64).setTileWidth(64);

        Hints hints = new Hints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

        // get a reader
        final BaseGDALGridCoverage2DReader reader = new SRPReader(source, hints);

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
        final int oldW = gc.getRenderedImage().getWidth();
        final int oldH = gc.getRenderedImage().getHeight();

        assertEquals(128, oldW);
        assertEquals(128, oldH);
        // check for expected data type
        assertEquals(
                DataBuffer.TYPE_BYTE, gc.getRenderedImage().getSampleModel().getDataType());

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

        final ParameterValue gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        gg.setValue(new GridGeometry2D(
                new GridEnvelope2D(new Rectangle(
                        0, 0, (int) (range.width / 2.0 / cropFactor), (int) (range.height / 2.0 / cropFactor))),
                cropEnvelope));
        GridCoverage2D gcSubsampled = reader.read(new GeneralParameterValue[] {gg});
        assertEquals(32, gcSubsampled.getGridGeometry().getGridRange2D().width);
        assertEquals(32, gcSubsampled.getGridGeometry().getGridRange2D().height);
        forceDataLoading(gcSubsampled);
        gcSubsampled.dispose(true);
    }
}
