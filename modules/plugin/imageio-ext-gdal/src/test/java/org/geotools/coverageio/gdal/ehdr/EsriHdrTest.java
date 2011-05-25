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
 *
 */
package org.geotools.coverageio.gdal.ehdr;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.coverageio.gdal.GDALTestCase;
import org.geotools.coverageio.gdal.ecw.ECWReader;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * @author Alex Petkov, Missoula Fire Sciences Laboratory
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * 
 * Testing {@link ECWReader}
 *
 *
 * @source $URL$
 */
public final class EsriHdrTest extends GDALTestCase {
    /**
     * file name of a valid EHdr sample data to be used for tests.
     */
    private final static String fileName = "elggll.bil";

    /**
     * Creates a new instance of {@code EsriHdrTest}
     * 
     * @param name
     */
    public EsriHdrTest() {
        super("EHdr", new EsriHdrFormatFactory());
    }


    @Test
    public void test() throws Exception {
        if (!testingEnabled()) {
            return;
        }

        File file =null;
        try {
            file = TestData.file(this, fileName);
        }catch (FileNotFoundException fnfe){
            LOGGER.warning("test-data not found: " + fileName + "\nTests are skipped");
            return;
        } catch (IOException ioe) {
            LOGGER.warning("test-data not found: " + fileName + "\nTests are skipped");
            return;
        }
        
        // Preparing an useful layout in case the image is striped.
        final ImageLayout l = new ImageLayout();
        l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(512)
                .setTileWidth(512);

        Hints hints = new Hints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

        // get a reader
        final URL url = file.toURI().toURL();
        final Object source = url;
        final BaseGDALGridCoverage2DReader reader = new EsriHdrReader(source,
                hints);
        // Testing the getSource method
        Assert.assertEquals(reader.getSource(), source);

        // /////////////////////////////////////////////////////////////////////
        //
        // read once
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D gc = (GridCoverage2D) reader.read(null);
        forceDataLoading(gc);

        // /////////////////////////////////////////////////////////////////////
        //
        // read again with subsampling and crop
        //
        // /////////////////////////////////////////////////////////////////////
        final double cropFactor = 2.0;
        final int oldW = gc.getRenderedImage().getWidth();
        final int oldH = gc.getRenderedImage().getHeight();
        final Rectangle range =((GridEnvelope2D)reader.getOriginalGridRange());
        final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
        final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
                oldEnvelope.getLowerCorner().getOrdinate(0)
                        + (oldEnvelope.getLength(0) / cropFactor),

                oldEnvelope.getLowerCorner().getOrdinate(1)
                        + (oldEnvelope.getLength(1) / cropFactor) },
                new double[] { oldEnvelope.getUpperCorner().getOrdinate(0),
                        oldEnvelope.getUpperCorner().getOrdinate(1) });
        cropEnvelope.setCoordinateReferenceSystem(reader.getCrs());

        final ParameterValue gg = (ParameterValue) ((AbstractGridFormat) reader
                .getFormat()).READ_GRIDGEOMETRY2D.createValue();
        gg.setValue(new GridGeometry2D(new GridEnvelope2D(new Rectangle(0, 0,
                (int) (range.width / 4.0 / cropFactor),
                (int) (range.height / 4.0 / cropFactor))), cropEnvelope));
        gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg });
        Assert.assertNotNull(gc);
        // NOTE: in some cases might be too restrictive
        Assert.assertTrue(cropEnvelope.equals(gc.getEnvelope(), XAffineTransform
                .getScale(((AffineTransform) ((GridGeometry2D) gc
                        .getGridGeometry()).getGridToCRS2D())) / 2, true));

        forceDataLoading(gc);

        // /////////////////////////////////////////////////////////////////////
        //
        // Attempt to read an envelope which doesn't intersect the dataset one
        //
        // /////////////////////////////////////////////////////////////////////
        final double translate0 = oldEnvelope.getLength(0) + 100;
        final double translate1 = oldEnvelope.getLength(1) + 100;
        final GeneralEnvelope wrongEnvelope = new GeneralEnvelope(new double[] {
                oldEnvelope.getLowerCorner().getOrdinate(0) + translate0,
                oldEnvelope.getLowerCorner().getOrdinate(1) + translate1 },
                new double[] {
                        oldEnvelope.getUpperCorner().getOrdinate(0)
                                + translate0,

                        oldEnvelope.getUpperCorner().getOrdinate(1)
                                + translate1 });
        wrongEnvelope.setCoordinateReferenceSystem(reader.getCrs());

        final ParameterValue gg2 = (ParameterValue) ((AbstractGridFormat) reader
                .getFormat()).READ_GRIDGEOMETRY2D.createValue();
        gg2.setValue(new GridGeometry2D(new GridEnvelope2D(new Rectangle(0,
                0, (int) (range.width), (int) (range.height))), wrongEnvelope));

        gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg2 });
        Assert.assertNull("Wrong envelope requested", gc);
    }
    
    @Test
    public void testIsAvailable() throws NoSuchAuthorityCodeException, FactoryException {
        if (!testingEnabled()) {
            return;
        }

        GridFormatFinder.scanForPlugins();

        Iterator list = GridFormatFinder.getAvailableFormats().iterator();
        boolean found = false;
        GridFormatFactorySpi fac = null;

        while (list.hasNext()) {
            fac = (GridFormatFactorySpi) list.next();

            if (fac instanceof EsriHdrFormatFactory) {
                found = true;

                break;
            }
        }

        Assert.assertTrue("EsriHdrFormatFactory not registered", found);
        Assert.assertTrue("EsriHdrFormatFactory not available", fac.isAvailable());
        Assert.assertNotNull(new EsriHdrFormatFactory().createFormat());
    }
}
