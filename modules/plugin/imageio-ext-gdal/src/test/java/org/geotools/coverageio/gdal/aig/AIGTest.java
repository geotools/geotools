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
package org.geotools.coverageio.gdal.aig;

import java.awt.RenderingHints;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.coverageio.gdal.GDALTestCase;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 *     <p>Testing {@link IDRISIReader}
 */
public final class AIGTest extends GDALTestCase {
    /** file name of a valid AIG sample data to be used for tests. */
    //     private final static String fileName = "abc3x1/hdr.adf";
    private static final String fileName = "hdr.adf";

    /** Creates a new instance of {@code IDRISIReader} */
    public AIGTest() {
        super("Aig", new AIGFormatFactory());
    }

    @Test
    public void test() throws Exception {
        if (!testingEnabled()) {
            return;
        }

        File file;
        try {
            file = TestData.file(this, fileName);
        } catch (FileNotFoundException fnfe) {
            LOGGER.warning("test-data not found: " + fileName + "\nTests are skipped");
            return;
        } catch (IOException ioe) {
            LOGGER.warning("test-data not found: " + fileName + "\nTests are skipped");
            return;
        }

        // Preparing an useful layout in case the image is striped.
        final ImageLayout l = new ImageLayout();
        l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(3).setTileWidth(1);

        Hints hints = new Hints();
        hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

        // get a reader
        final URL url = file.toURI().toURL();
        final Object source = url;
        final BaseGDALGridCoverage2DReader reader = new AIGReader(source, hints);
        checkReader(reader);

        // Testing the getSource method
        Assert.assertEquals(reader.getSource(), source);

        // /////////////////////////////////////////////////////////////////////
        //
        // read once
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D gc = (GridCoverage2D) reader.read(null);
        forceDataLoading(gc);
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

            if (fac instanceof AIGFormatFactory) {
                found = true;

                break;
            }
        }

        Assert.assertTrue("IDRISIFormatFactory not registered", found);
        Assert.assertTrue("IDRISIFormatFactory not available", fac.isAvailable());
        Assert.assertNotNull(new AIGFormatFactory().createFormat());
    }
}
