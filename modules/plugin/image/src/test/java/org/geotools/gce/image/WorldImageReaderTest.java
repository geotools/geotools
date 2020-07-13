/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.image;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.media.jai.ImageLayout;
import javax.media.jai.RenderedOp;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.CloseableIterator;
import org.geotools.data.FileGroupProvider.FileGroup;
import org.geotools.data.FileServiceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

/**
 * TestCase subclass for testing readingb capabilities
 *
 * @author Simone Giannecchini
 * @author Alessio Fabiani
 * @author rgould
 */
public class WorldImageReaderTest extends WorldImageBaseTestCase {

    private Logger logger = org.geotools.util.logging.Logging.getLogger(WorldImageReaderTest.class);

    /** Constructor for WorldImageReaderTest. */
    public WorldImageReaderTest(String arg0) {
        super(arg0);
    }

    /*
     * Can't test this, as these files aren't actually expected to exist.
     * The constructor tries to create an inputStream and then throws
     * an exception. Re-enable this if that behaviour changes, or if you
     * feel like writing a windows-only test.
     */
    //    public void testSource() throws Exception {
    //    	URL altDrive = new URL("file://E:/somedir/foo.tif");
    //    	WorldImageReader r = new WorldImageReader(altDrive);
    //    	File result = (File) r.getSource();
    //    	String s1 = result.getAbsolutePath();
    //    	String s2 = "E:\\somedir\\foo.tif";
    //    	assertTrue(s1.equals(s2));
    //
    //    	URL networkShare = new URL("file://borkServer/somedir/foo.tif");
    //    	r = new WorldImageReader(networkShare);
    //    	result = (File) r.getSource();
    //    	s1 = result.getAbsolutePath();
    //    	s2 = "\\\\borkServer\\somedir\\foo.tif";
    //    	assertTrue(s1.equals(s2));
    //    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testRead() throws IOException {

        // set up
        Object in;

        // checking test data directory for all kind of inputs
        final File test_data_dir = TestData.file(this, null);
        final String[] fileList = test_data_dir.list(new MyFileFilter());
        final int length = fileList.length;
        for (int i = 0; i < length; i++) {
            // file
            in = TestData.file(this, fileList[i]);
            this.read(in);
        }

        // checking a WMS get map
        //		URL url = new URL(
        //
        //	"http://wms.jpl.nasa.gov/wms.cgi?bbox=9,43,12,45&styles=&Format=image/png&request=GetMap&layers=global_mosaic&width=100&height=100&srs=EPSG:4326");
        //		// checking that we have an internet connection active and that the
        //		// website is up
        //		if (url.openConnection() == null)
        //			return;
        //		this.read(url);
    }

    public void testNoWorldFile() throws IOException {
        final File file = TestData.file(this, "box_gcp.tif");
        WorldImageReader wiReader = new WorldImageReader(file);
        assertEquals(DefaultEngineeringCRS.GENERIC_2D, wiReader.getCoordinateReferenceSystem());
        GeneralEnvelope ge = wiReader.getOriginalEnvelope();
        assertEquals(0, ge.getMinimum(0));
        assertEquals(0, ge.getMinimum(1));
        assertEquals(300, ge.getSpan(0));
        assertEquals(300, ge.getSpan(0));
        GridCoverage2D gc = wiReader.read(null);
        Envelope2D envelope = gc.getEnvelope2D();
        assertEquals(0, envelope.getMinimum(0));
        assertEquals(0, envelope.getMinimum(1));
        assertEquals(300, envelope.getSpan(0));
        assertEquals(300, envelope.getSpan(0));
        wiReader.dispose();
    }

    public void testOverviewsNearest() throws IOException {
        final File file = TestData.file(this, "etopo.tif");

        ///////////////////////////////////////////////////////////////////////
        //
        // HINTS
        //
        ///////////////////////////////////////////////////////////////////////
        WorldImageReader wiReader = new WorldImageReader(file);

        // more than native resolution (250 pixel representation for 125 pixels image)
        assertEquals(0, getChosenOverview(250, wiReader));
        // native resolution (125 pixel representation for 125 pixels image)
        assertEquals(0, getChosenOverview(125, wiReader));
        // half of native, the overview is in position 3 (out of order, remember?)
        assertEquals(3, getChosenOverview(73, wiReader));
        // quarter of native, the overview is in position 2
        assertEquals(2, getChosenOverview(31, wiReader));
        // 1/8 of native, the overview is in position 4
        assertEquals(4, getChosenOverview(16, wiReader));
        // 1/16 of native, the overview is in position 1
        assertEquals(1, getChosenOverview(9, wiReader));
        // 1/32 of native, no overview, still 1
        assertEquals(1, getChosenOverview(4, wiReader));

        // 13 is nearer to 16 and to 9
        assertEquals(4, getChosenOverview(13, wiReader));
        // 11 is nearer to 9 than to 16
        assertEquals(4, getChosenOverview(13, wiReader));
        wiReader.dispose();

        ///////////////////////////////////////////////////////////////////////
        //
        // PARAMETER
        //
        ///////////////////////////////////////////////////////////////////////
        wiReader = new WorldImageReader(file);
        final ParameterValue policy =
                ((AbstractGridFormat) wiReader.getFormat()).OVERVIEW_POLICY.createValue();
        policy.setValue(OverviewPolicy.NEAREST);

        // more than native resolution (250 pixel representation for 125 pixels image)
        assertEquals(0, getChosenOverview(250, wiReader, policy));
        // native resolution (125 pixel representation for 125 pixels image)
        assertEquals(0, getChosenOverview(125, wiReader, policy));
        // half of native, the overview is in position 3 (out of order, remember?)
        assertEquals(3, getChosenOverview(73, wiReader, policy));
        // quarter of native, the overview is in position 2
        assertEquals(2, getChosenOverview(31, wiReader, policy));
        // 1/8 of native, the overview is in position 4
        assertEquals(4, getChosenOverview(16, wiReader, policy));
        // 1/16 of native, the overview is in position 1
        assertEquals(1, getChosenOverview(9, wiReader, policy));
        // 1/32 of native, no overview, still 1
        assertEquals(1, getChosenOverview(4, wiReader, policy));

        // 13 is nearer to 16 and to 9
        assertEquals(4, getChosenOverview(13, wiReader));
        // 11 is nearer to 9 than to 16
        assertEquals(4, getChosenOverview(13, wiReader));
        wiReader.dispose();
    }

    public void testOverviewsQuality() throws IOException {
        final File file = TestData.file(this, "etopo.tif");

        ///////////////////////////////////////////////////////////////////////
        //
        // HINTS
        //
        ///////////////////////////////////////////////////////////////////////
        Hints hints = new Hints();
        hints.put(Hints.OVERVIEW_POLICY, OverviewPolicy.QUALITY);
        WorldImageReader wiReader = new WorldImageReader(file, hints);

        // between 16 and 9, any value should report the match of 16
        assertEquals(4, getChosenOverview(16, wiReader));
        assertEquals(4, getChosenOverview(15, wiReader));
        assertEquals(4, getChosenOverview(14, wiReader));
        assertEquals(4, getChosenOverview(13, wiReader));
        assertEquals(4, getChosenOverview(12, wiReader));
        assertEquals(4, getChosenOverview(11, wiReader));
        assertEquals(4, getChosenOverview(10, wiReader));

        ///////////////////////////////////////////////////////////////////////
        //
        // PARAMETER
        //
        ///////////////////////////////////////////////////////////////////////
        // parameter ovverrides hints
        hints.put(Hints.OVERVIEW_POLICY, OverviewPolicy.NEAREST);
        wiReader = new WorldImageReader(file, hints);
        final ParameterValue policy =
                ((AbstractGridFormat) wiReader.getFormat()).OVERVIEW_POLICY.createValue();
        policy.setValue(OverviewPolicy.QUALITY);

        // between 16 and 9, any value should report the match of 16
        assertEquals(4, getChosenOverview(16, wiReader, policy));
        assertEquals(4, getChosenOverview(15, wiReader, policy));
        assertEquals(4, getChosenOverview(14, wiReader, policy));
        assertEquals(4, getChosenOverview(13, wiReader, policy));
        assertEquals(4, getChosenOverview(12, wiReader, policy));
        assertEquals(4, getChosenOverview(11, wiReader, policy));
        assertEquals(4, getChosenOverview(10, wiReader, policy));
    }

    public void testOverviewsSpeed() throws IOException {
        final File file = TestData.file(this, "etopo.tif");

        ///////////////////////////////////////////////////////////////////////
        //
        // HINTS
        //
        ///////////////////////////////////////////////////////////////////////
        Hints hints = new Hints();
        hints.put(Hints.OVERVIEW_POLICY, OverviewPolicy.SPEED);
        WorldImageReader wiReader = new WorldImageReader(file, hints);
        // between 16 and 9, any value should report the match of 16
        assertEquals(1, getChosenOverview(15, wiReader));
        assertEquals(1, getChosenOverview(14, wiReader));
        assertEquals(1, getChosenOverview(13, wiReader));
        assertEquals(1, getChosenOverview(12, wiReader));
        assertEquals(1, getChosenOverview(11, wiReader));
        assertEquals(1, getChosenOverview(10, wiReader));

        ///////////////////////////////////////////////////////////////////////
        //
        // PARAMETER
        //
        ///////////////////////////////////////////////////////////////////////
        // parameter overrides hints
        hints.put(Hints.OVERVIEW_POLICY, OverviewPolicy.NEAREST);
        wiReader = new WorldImageReader(file, hints);
        final ParameterValue policy =
                ((AbstractGridFormat) wiReader.getFormat()).OVERVIEW_POLICY.createValue();
        policy.setValue(OverviewPolicy.SPEED);
        // between 16 and 9, any value should report the match of 16
        assertEquals(1, getChosenOverview(15, wiReader, policy));
        assertEquals(1, getChosenOverview(14, wiReader, policy));
        assertEquals(1, getChosenOverview(13, wiReader, policy));
        assertEquals(1, getChosenOverview(12, wiReader, policy));
        assertEquals(1, getChosenOverview(11, wiReader, policy));
        assertEquals(1, getChosenOverview(10, wiReader, policy));
    }

    public void testOverviewEnvelope() throws Exception {
        final File file = TestData.file(this, "etopo.tif");
        WorldImageReader reader = new WorldImageReader(file);

        // prepare to read an overview
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 64.0,
                reader.getOriginalGridRange().getSpan(1) / 64.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        GridGeometry2D gridGeometry = new GridGeometry2D(range, envelope);
        gg.setValue(gridGeometry);

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {gg});
        assertEquals(coverage.getEnvelope(), reader.getOriginalEnvelope());
    }

    private int getChosenOverview(final int size, WorldImageReader wiReader) throws IOException {
        return getChosenOverview(size, wiReader, null);
    }

    private int getChosenOverview(final int size, WorldImageReader wiReader, ParameterValue policy)
            throws IOException {
        // get the coverage and then the rendered image
        final Parameter readGG = new Parameter(AbstractGridFormat.READ_GRIDGEOMETRY2D);

        readGG.setValue(
                new GridGeometry2D(
                        new GridEnvelope2D(
                                new java.awt.Rectangle(size, (int) (164.0 / 125.0 * size))),
                        new ReferencedEnvelope(
                                118.8, 134.56, 47.819, 63.142, DefaultGeographicCRS.WGS84)));
        final GridCoverage2D coverage =
                wiReader.read(
                        policy != null
                                ? new GeneralParameterValue[] {readGG, policy}
                                : new GeneralParameterValue[] {readGG});
        assertNotNull(coverage);
        assertNotNull((coverage).getRenderedImage());

        RenderedOp op = (RenderedOp) coverage.getRenderedImage();
        while (!op.getOperationName().equals("ImageRead")) op = (RenderedOp) op.getSources().get(0);

        Integer choice = (Integer) op.getParameterBlock().getObjectParameter(1);
        return choice.intValue();
    }

    /**
     * Read, test and show a coverage from the supplied source.
     *
     * @param source Object
     */
    private void read(Object source)
            throws FileNotFoundException, IOException, IllegalArgumentException {

        // can we read it?
        assertTrue(new WorldImageFormat().accepts(source));

        logger.info(((File) source).getAbsolutePath());

        // get a reader
        final WorldImageReader wiReader = new WorldImageReader(source);

        // layout checks
        final ImageLayout layout = wiReader.getImageLayout();
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

        // get the coverage
        final GridCoverage2D coverage = wiReader.read(null);

        // test the coverage
        assertNotNull(coverage);
        assertNotNull((coverage).getRenderedImage());
        assertNotNull(coverage.getEnvelope());

        // log some information
        if (TestData.isInteractiveTest()) {
            logger.info(coverage.getCoordinateReferenceSystem().toWKT());
            logger.info(coverage.getEnvelope().toString());
        }
        // show it, but only if tests are interactive
        if (TestData.isInteractiveTest()) coverage.show();
        else coverage.getRenderedImage().getData();
    }

    public static void main(String[] args) {
        TestRunner.run(WorldImageReaderTest.suite());
    }

    public static final TestSuite suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new WorldImageReaderTest("testOverviewsNearest"));
        suite.addTest(new WorldImageReaderTest("testOverviewsQuality"));
        suite.addTest(new WorldImageReaderTest("testOverviewsSpeed"));
        suite.addTest(new WorldImageReaderTest("testRead"));
        suite.addTest(new WorldImageReaderTest("testFileGroup"));
        return suite;
    }

    public void testFileGroup() throws Exception {
        final File file = TestData.file(this, "etopo.tif");
        WorldImageReader reader = new WorldImageReader(file);

        // prepare to read an overview
        ServiceInfo info = reader.getInfo();
        assertTrue(info instanceof FileServiceInfo);
        CloseableIterator<FileGroup> iterator = null;
        try {
            iterator = ((FileServiceInfo) info).getFiles(null);
            FileGroup group = iterator.next();
            assertTrue(group.getMainFile().getName().endsWith("etopo.tif"));
            List<File> files = group.getSupportFiles();
            assertFalse(files.isEmpty());
            assertEquals(2, files.size());
        } finally {
            if (iterator != null) {
                iterator.close();
            }
        }
    }
}
