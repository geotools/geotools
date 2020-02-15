/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.gtopo30;

import java.io.File;
import java.net.URL;
import javax.media.jai.ImageLayout;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.test.TestData;
import org.opengis.coverage.grid.GridCoverageWriter;

/**
 * Purpose of this class is testing the ability of this plug in to read and write back the in
 * gtopo30 format.
 *
 * @author Simone Giannecchini
 */
public class GT30ReaderWriterTest extends GT30TestBase {
    /** Constructor for GT30ReaderTest. */
    public GT30ReaderWriterTest(String arg0) {
        super(arg0);
    }

    /**
     * Testing reader and writer for gtopo. This test first of all read an existing gtopo tessel
     * into a coverage object, therefore it writes it back onto the disk. Once the coverage is
     * written back\ it loads it again building a new coverage which is finally visualized.
     */
    public void test() throws Exception {

        URL statURL =
                TestData.url(this, (new StringBuffer(this.fileName).append(".DEM").toString()));
        AbstractGridFormat format = (AbstractGridFormat) new GTopo30FormatFactory().createFormat();

        if (format.accepts(statURL)) {

            /** STEP 1 Reading the coverage into memory in order to write it down again */
            // get a reader
            GridCoverage2DReader reader = format.getReader(statURL);

            // layout checks
            final ImageLayout layout = reader.getImageLayout();
            assertNotNull(layout);
            assertNotNull(layout.getColorModel(null));
            assertNotNull(layout.getSampleModel(null));
            assertEquals(0, layout.getMinX(null));
            assertEquals(0, layout.getMinY(null));
            assertTrue(layout.getWidth(null) > 0);
            assertTrue(layout.getHeight(null) > 0);
            assertEquals(0, layout.getTileGridXOffset(null));
            assertEquals(0, layout.getTileGridYOffset(null));
            assertTrue(layout.getTileHeight(null) > 0);
            assertTrue(layout.getTileWidth(null) > 0);

            // get a grid coverage
            gc = ((GridCoverage2D) reader.read(null));
            if (TestData.isInteractiveTest()) gc.show();

            // preparing to write it down
            File testDir = TestData.file(this, "");
            newDir = new File(testDir.getAbsolutePath() + "/newDir");
            newDir.mkdir();

            // writing it down
            GridCoverageWriter writer = format.getWriter(newDir);
            writer.write(gc, null);

            /** STEP 2 Reading back into memory the previos coverage. */
            // preparing the URL
            statURL = TestData.getResource(this, "newDir/" + this.fileName + ".DEM");

            // read it again
            reader = format.getReader(statURL);
            GridCoverage2D gc1 = ((GridCoverage2D) reader.read(null));

            /**
             * STEP 3 Visualizing the lcoverage we just read in order to see if everything is fine.
             */
            if (TestData.isInteractiveTest()) {
                gc1.show();
                // logging some info
                logger.info(gc.getCoordinateReferenceSystem2D().toWKT() + "\n" + gc.toString());
                logger.info(gc1.getCoordinateReferenceSystem2D().toWKT() + "\n" + gc1.toString());
            } else {
                gc1.getRenderedImage().getData();
            }
        }
    }

    public static final void main(String[] args) throws Exception {
        junit.textui.TestRunner.run(GT30ReaderWriterTest.class);
    }
}
