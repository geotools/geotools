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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.media.jai.RenderedOp;

import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;


/**
 * TestCase subclass for testing readingb capabilities
 * 
 * @author Simone Giannecchini
 * @author Alessio Fabiani
 * @author rgould
 * @source $URL$
 */
public class WorldImageReaderTest extends WorldImageBaseTestCase {

	private Logger logger = org.geotools.util.logging.Logging.getLogger(WorldImageReaderTest.class
			.toString());

	/**
	 * Constructor for WorldImageReaderTest.
	 * 
	 * @param arg0
	 */
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
//				"http://wms.jpl.nasa.gov/wms.cgi?bbox=9,43,12,45&styles=&Format=image/png&request=GetMap&layers=global_mosaic&width=100&height=100&srs=EPSG:4326");
//		// checking that we have an internet connection active and that the
//		// website is up
//		if (url.openConnection() == null)
//			return;
//		this.read(url);
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
        assertEquals(0, getChosenOverview(250,wiReader));
        // native resolution (125 pixel representation for 125 pixels image)
        assertEquals(0, getChosenOverview(125,wiReader));
        // half of native, the overview is in position 3 (out of order, remember?)
        assertEquals(3, getChosenOverview(73,wiReader));
        // quarter of native, the overview is in position 2
        assertEquals(2, getChosenOverview(31,wiReader));
        // 1/8 of native, the overview is in position 4
        assertEquals(4, getChosenOverview(16,wiReader));
        // 1/16 of native, the overview is in position 1
        assertEquals(1, getChosenOverview(9,wiReader));
        // 1/32 of native, no overview, still 1
        assertEquals(1, getChosenOverview(4,wiReader));
        
        // 13 is nearer to 16 and to 9
        assertEquals(4, getChosenOverview(13,wiReader));
        // 11 is nearer to 9 than to 16
        assertEquals(4, getChosenOverview(13,wiReader));
        wiReader.dispose();
        
        
        ///////////////////////////////////////////////////////////////////////
        //
        // PARAMETER
        //
        ///////////////////////////////////////////////////////////////////////
        wiReader = new WorldImageReader(file);
		final ParameterValue policy = (ParameterValue) ((AbstractGridFormat) wiReader
				.getFormat()).OVERVIEW_POLICY.createValue();	
		policy.setValue(OverviewPolicy.NEAREST);

        // more than native resolution (250 pixel representation for 125 pixels image)
        assertEquals(0, getChosenOverview(250,wiReader,policy));
        // native resolution (125 pixel representation for 125 pixels image)
        assertEquals(0, getChosenOverview(125,wiReader,policy));
        // half of native, the overview is in position 3 (out of order, remember?)
        assertEquals(3, getChosenOverview(73,wiReader,policy));
        // quarter of native, the overview is in position 2
        assertEquals(2, getChosenOverview(31,wiReader,policy));
        // 1/8 of native, the overview is in position 4
        assertEquals(4, getChosenOverview(16,wiReader,policy));
        // 1/16 of native, the overview is in position 1
        assertEquals(1, getChosenOverview(9,wiReader,policy));
        // 1/32 of native, no overview, still 1
        assertEquals(1, getChosenOverview(4,wiReader,policy));
        
        // 13 is nearer to 16 and to 9
        assertEquals(4, getChosenOverview(13,wiReader));
        // 11 is nearer to 9 than to 16
        assertEquals(4, getChosenOverview(13,wiReader));
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
        WorldImageReader wiReader = new WorldImageReader(file,hints);
        
        // between 16 and 9, any value should report the match of 16
        assertEquals(4, getChosenOverview(16,wiReader));
        assertEquals(4, getChosenOverview(15,wiReader));
        assertEquals(4, getChosenOverview(14,wiReader));
        assertEquals(4, getChosenOverview(13,wiReader));
        assertEquals(4, getChosenOverview(12,wiReader));
        assertEquals(4, getChosenOverview(11,wiReader));
        assertEquals(4, getChosenOverview(10,wiReader));
        
        ///////////////////////////////////////////////////////////////////////
        //
        // PARAMETER
        //
        ///////////////////////////////////////////////////////////////////////
        //parameter ovverrides hints
        hints.put(Hints.OVERVIEW_POLICY, OverviewPolicy.NEAREST);
        wiReader = new WorldImageReader(file, hints);
		final ParameterValue policy = (ParameterValue) ((AbstractGridFormat) wiReader
				.getFormat()).OVERVIEW_POLICY.createValue();	
		policy.setValue(OverviewPolicy.QUALITY);
		
        // between 16 and 9, any value should report the match of 16
        assertEquals(4, getChosenOverview(16,wiReader,policy));
        assertEquals(4, getChosenOverview(15,wiReader,policy));
        assertEquals(4, getChosenOverview(14,wiReader,policy));
        assertEquals(4, getChosenOverview(13,wiReader,policy));
        assertEquals(4, getChosenOverview(12,wiReader,policy));
        assertEquals(4, getChosenOverview(11,wiReader,policy));
        assertEquals(4, getChosenOverview(10,wiReader,policy));
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
        assertEquals(1, getChosenOverview(15,wiReader));
        assertEquals(1, getChosenOverview(14,wiReader));
        assertEquals(1, getChosenOverview(13,wiReader));
        assertEquals(1, getChosenOverview(12,wiReader));
        assertEquals(1, getChosenOverview(11,wiReader));
        assertEquals(1, getChosenOverview(10,wiReader));
        
        
        ///////////////////////////////////////////////////////////////////////
        //
        // PARAMETER
        //
        ///////////////////////////////////////////////////////////////////////
        //parameter overrides hints
        hints.put(Hints.OVERVIEW_POLICY, OverviewPolicy.NEAREST);
        wiReader = new WorldImageReader(file, hints);
		final ParameterValue policy = (ParameterValue) ((AbstractGridFormat) wiReader
				.getFormat()).OVERVIEW_POLICY.createValue();	
		policy.setValue(OverviewPolicy.SPEED);
        // between 16 and 9, any value should report the match of 16
        assertEquals(1, getChosenOverview(15,wiReader,policy));
        assertEquals(1, getChosenOverview(14,wiReader,policy));
        assertEquals(1, getChosenOverview(13,wiReader,policy));
        assertEquals(1, getChosenOverview(12,wiReader,policy));
        assertEquals(1, getChosenOverview(11,wiReader,policy));
        assertEquals(1, getChosenOverview(10,wiReader,policy));
    }

    private int getChosenOverview(final int size, WorldImageReader wiReader)
			throws IOException {
		return getChosenOverview(size, wiReader, null);
	}
    private int getChosenOverview(final int size, WorldImageReader wiReader,
			ParameterValue policy) throws IOException {
		// get the coverage and then the rendered image
		final Parameter readGG = new Parameter(
				AbstractGridFormat.READ_GRIDGEOMETRY2D);

		readGG.setValue(new GridGeometry2D(new GridEnvelope2D(
				new java.awt.Rectangle(size, (int) (164.0 / 125.0 * size))),
				new ReferencedEnvelope(118.8, 134.56, 47.819, 63.142,
						DefaultGeographicCRS.WGS84)));
		final GridCoverage2D coverage = (GridCoverage2D) wiReader
				.read(policy != null ? new GeneralParameterValue[] { readGG,
						policy } : new GeneralParameterValue[] { readGG });
		assertNotNull(coverage);
		assertNotNull((coverage).getRenderedImage());

		RenderedOp op = (RenderedOp) coverage.getRenderedImage();
		while (!op.getOperationName().equals("ImageRead"))
			op = (RenderedOp) op.getSources().get(0);

		Integer choice = (Integer) op.getParameterBlock().getObjectParameter(1);
		return choice.intValue();
	}

	/**
	 * Read, test and show a coverage from the supplied source.
	 * 
	 * @param source
	 *            Object
	 * 
	 * @throws FileNotFoundException
	 *             DOCUMENT ME!
	 * @throws IOException
	 *             DOCUMENT ME!
	 * @throws IllegalArgumentException
	 *             DOCUMENT ME!
	 */
	private void read(Object source) throws FileNotFoundException, IOException,
			IllegalArgumentException {

		// can we read it?
		assertTrue(new WorldImageFormat().accepts(source));

		logger.info(((File)source).getAbsolutePath());
		
		// get a reader
		final WorldImageReader wiReader = new WorldImageReader(source);

		// get the coverage
		final GridCoverage2D coverage = (GridCoverage2D) wiReader.read(null);

		// test the coverage
		assertNotNull(coverage);
		assertNotNull((coverage).getRenderedImage());
		assertNotNull(coverage.getEnvelope());

		// log some information
		if(TestData.isInteractiveTest()){
			logger.info(coverage.getCoordinateReferenceSystem().toWKT());
			logger.info(coverage.getEnvelope().toString());
		}
		// show it, but only if tests are interactive
		if(TestData.isInteractiveTest())
			coverage.show();
		else
			coverage.getRenderedImage().getData();
	}

	public static void main(String[] args) {
		TestRunner.run(WorldImageReaderTest.suite());
	}
	public final static TestSuite suite(){
		final TestSuite suite= new TestSuite();
		suite.addTest(new WorldImageReaderTest("testOverviewsNearest"));
		suite.addTest(new WorldImageReaderTest("testOverviewsQuality"));
		suite.addTest(new WorldImageReaderTest("testOverviewsSpeed"));
		suite.addTest(new WorldImageReaderTest("testRead"));
		return suite;
	}
}
