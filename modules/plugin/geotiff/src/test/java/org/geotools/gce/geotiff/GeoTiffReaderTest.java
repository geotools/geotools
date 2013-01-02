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
package org.geotools.gce.geotiff;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import junit.framework.Assert;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;
import org.geotools.data.DataSourceException;
import org.geotools.data.PrjFileReader;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Testing {@link GeoTiffReader} as well as {@link IIOMetadataDumper}.
 * 
 * @author Simone Giannecchini
 *
 * @source $URL$
 */
public class GeoTiffReaderTest extends Assert {
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(GeoTiffReaderTest.class.toString());

	/**
	 * Testing proper CRS override with PRJ.
	 * 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws FactoryException
	 */
	    @Test
	    public void prjOverrideTesting1() throws IllegalArgumentException, IOException,
	            FactoryException {

	        //
	        // PRJ override
	        //
	        final File noCrs = TestData.file(GeoTiffReaderTest.class, "override/sample.tif");
	        final AbstractGridFormat format = new GeoTiffFormat();
	        assertTrue(format.accepts(noCrs));
	        GeoTiffReader reader = (GeoTiffReader) format.getReader(noCrs);
	        CoordinateReferenceSystem crs=reader.getCrs();
	        
	        final File prj= TestData.file(GeoTiffReaderTest.class, "override/sample.prj");
	        final CoordinateReferenceSystem crs_=new PrjFileReader(new FileInputStream(prj).getChannel()).getCoordinateReferenceSystem();
	        assertTrue(CRS.equalsIgnoreMetadata(crs, crs_));
	        GridCoverage2D coverage=reader.read(null);
	        assertTrue(CRS.equalsIgnoreMetadata(coverage.getCoordinateReferenceSystem(), crs_));

	        coverage.dispose(true);
	    }
	    
		/**
		 * Testing proper CRS override with PRJ.
		 * 
		 * @throws IllegalArgumentException
		 * @throws IOException
		 * @throws FactoryException
		 */
		    @Test
		    public void prjOverrideTesting2() throws IllegalArgumentException, IOException,
		            FactoryException {

		        //
		        // PRJ override
		        //
		        final File noCrs = TestData.file(GeoTiffReaderTest.class, "override/sample.tif");

		        
		        final File prj= TestData.file(GeoTiffReaderTest.class, "override/sample.prj");
		        final CoordinateReferenceSystem crs_=new PrjFileReader(new FileInputStream(prj).getChannel()).getCoordinateReferenceSystem();

		        

		        // NO override
		        GeoTiffReader.OVERRIDE_INNER_CRS=false;

		        // getting a reader
		        GeoTiffReader reader = new GeoTiffReader(noCrs);
		        
		        if(TestData.isInteractiveTest()){
                            IIOMetadataDumper iIOMetadataDumper = new IIOMetadataDumper(
                                            ((GeoTiffReader) reader).getMetadata()
                                                            .getRootNode());
                            System.out.println(iIOMetadataDumper.getMetadata());		        
		        }
		        // reading the coverage
		        GridCoverage2D coverage1 = (GridCoverage2D) reader.read(null);

		        // check coverage and crs
		        assertNotNull(coverage1);
		        assertNotNull(coverage1.getCoordinateReferenceSystem());
		        assertNotSame(coverage1.getCoordinateReferenceSystem(),crs_);
		        reader.dispose();

		        coverage1.dispose(true);
		        System.setProperty(GeoTiffReader.OVERRIDE_CRS_SWITCH, "True");
		    }
    /**
     * Test for reading bad/strange geotiff files
     * 
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws FactoryException
     */
    @Test
//    @Ignore
    public void testReaderBadGeotiff() throws IllegalArgumentException, IOException,
            FactoryException {

        //
        // no crs geotiff
        //
        final File noCrs = TestData.file(GeoTiffReaderTest.class, "no_crs.tif");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(noCrs));
        GeoTiffReader reader = (GeoTiffReader) format.getReader(noCrs);
        CoordinateReferenceSystem crs=reader.getCrs();
        assertTrue(CRS.equalsIgnoreMetadata(crs, DefaultEngineeringCRS.GENERIC_2D));
        GridCoverage2D coverage=reader.read(null);
        assertTrue(CRS.equalsIgnoreMetadata(coverage.getCoordinateReferenceSystem(), DefaultEngineeringCRS.GENERIC_2D));
        

        // hint for CRS
        crs = CRS.decode("EPSG:32632", true);
        final Hints hint = new Hints();
        hint.put(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, crs);

        // getting a reader
        reader = new GeoTiffReader(noCrs, hint);

        // reading the coverage
        GridCoverage2D coverage1 = (GridCoverage2D) reader.read(null);

        // check coverage and crs
        assertNotNull(coverage1);
        assertNotNull(coverage1.getCoordinateReferenceSystem());
        assertEquals(CRS.lookupIdentifier(coverage1.getCoordinateReferenceSystem(), true),
                "EPSG:32632");
        reader.dispose();

        //
        // use prj and wld
        //
        final File wldprjFile = TestData.file(GeoTiffReaderTest.class, "no_crs_no_envelope.tif");
        assertTrue(format.accepts(wldprjFile));

        // getting a reader
        reader = new GeoTiffReader(wldprjFile);

        // reading the coverage
        GridCoverage2D coverage2 = (GridCoverage2D) reader.read(null);

        // check coverage and crs
        assertNotNull(coverage2);
        assertNotNull(coverage2.getCoordinateReferenceSystem());
        assertEquals(CRS.lookupIdentifier(coverage2.getCoordinateReferenceSystem(), true),
                "EPSG:32632");
        reader.dispose();

        //
        // use prj and hint
        //
        final File wldFile = TestData.file(GeoTiffReaderTest.class, "no_crs_no_envelope2.tif");
        assertTrue(format.accepts(wldFile));

        // getting a reader
        reader = new GeoTiffReader(wldFile, hint);

        // reading the coverage
        GridCoverage2D coverage3 = (GridCoverage2D) reader.read(null);

        // check coverage and crs
        assertNotNull(coverage3);
        assertNotNull(coverage3.getCoordinateReferenceSystem());
        assertEquals(CRS.lookupIdentifier(coverage3.getCoordinateReferenceSystem(), true),
                "EPSG:32632");
        reader.dispose();
        
        coverage1.dispose(true);
        coverage2.dispose(true);
        coverage3.dispose(true);
    }

    /**
     * Test for reading geotiff files
     * 
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws NoSuchAuthorityCodeException
     */
    @Test
    public void testReader() throws Exception {
    
    	final File baseDirectory = TestData.file(GeoTiffReaderTest.class, ".");
    	final File writeDirectory =new File(baseDirectory,Long.toString(System.currentTimeMillis()));
    	writeDirectory.mkdir();
    	final File files[] = baseDirectory.listFiles();
    	final int numFiles = files.length;
    	final AbstractGridFormat format = new GeoTiffFormat();
    	for (int i = 0; i < numFiles; i++) {
    		StringBuilder buffer = new StringBuilder();
    		final String path = files[i].getAbsolutePath().toLowerCase();
    		if (!path.endsWith("tif") && !path.endsWith("tiff")||path.contains("no_crs"))
    			continue;
    
    		buffer.append(files[i].getAbsolutePath()).append("\n");
    		Object o;
    		if (i % 2 == 0)
    			// testing file
    			o = files[i];
    		else
    			// testing url
    			o = files[i].toURI().toURL();
    		if (format.accepts(o)) {
    			buffer.append("ACCEPTED").append("\n");
    
    			// getting a reader
    			GeoTiffReader reader = new GeoTiffReader(o, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
    			if (reader != null) {
    
    				// reading the coverage
    			        final GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
    
    				// Crs and envelope
    				if (TestData.isInteractiveTest()) {
    					buffer.append("CRS: ").append(
    							coverage.getCoordinateReferenceSystem2D()
    									.toWKT()).append("\n");
    					buffer.append("GG: ").append(
    							coverage.getGridGeometry().toString()).append("\n");
    				}
    				// display metadata
    				if (org.geotools.TestData.isExtensiveTest()) {
    					IIOMetadataDumper iIOMetadataDumper = new IIOMetadataDumper(
    							((GeoTiffReader) reader).getMetadata()
    									.getRootNode());
    					buffer.append("TIFF metadata: ").append(
    							iIOMetadataDumper.getMetadata()).append("\n");
    				}
    				// showing it
    				if (TestData.isInteractiveTest()){
    				    coverage.show();
    				}
    				else {
    				    PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
    				}
    				
    				// write and read back
    				final File destFile = File.createTempFile("test", ".tif",writeDirectory);				
    				final GeoTiffWriter writer= new GeoTiffWriter(destFile);
    				writer.write(coverage, null);
    				writer.dispose();
    				
    				// read back
    				assertTrue(format.accepts(destFile));
    				reader = new GeoTiffReader(destFile, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
    				final GridCoverage2D destCoverage = (GridCoverage2D) reader.read(null);
    				reader.dispose();
    				
    				final double eps=XAffineTransform.getScaleX0((AffineTransform)coverage.getGridGeometry().getGridToCRS())*1E-2;
    				assertTrue("CRS comparison failed:" +o.toString(),CRS.findMathTransform(coverage.getCoordinateReferenceSystem(), destCoverage.getCoordinateReferenceSystem(), true).isIdentity());
    				assertTrue("CRS comparison failed:" +o.toString(),CRS.equalsIgnoreMetadata(coverage.getCoordinateReferenceSystem(), destCoverage.getCoordinateReferenceSystem()));
    				assertTrue("GridRange comparison failed:" +o.toString(),coverage.getGridGeometry().getGridRange().equals(destCoverage.getGridGeometry().getGridRange()));
    				assertTrue("Envelope comparison failed:" +o.toString(),((GeneralEnvelope)coverage.getGridGeometry().getEnvelope()).equals(destCoverage.getGridGeometry().getEnvelope(),eps,false));
    				coverage.dispose(true);
    				destCoverage.dispose(true);
    			}
    
    		} else
    			buffer.append("NOT ACCEPTED").append("\n");
    		if (TestData.isInteractiveTest())
    			LOGGER.info(buffer.toString());
    
    	}
    
    }
    
    public void testBandNames() throws Exception {
        final File file = TestData.file(GeoTiffReaderTest.class, "wind.tiff");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        GridCoverage2D coverage = format.getReader(file).read(null);
        String band1Name = coverage.getSampleDimension(0).getDescription().toString();
        String band2Name = coverage.getSampleDimension(1).getDescription().toString();
        assertEquals("Band1", band1Name);
        assertEquals("Band2", band2Name);
    }
    
    /**
     * Test what we can do and what not with 
     */
    @Test
//    @Ignore
    public void testTransparencySettings() throws Exception {

        
        final AbstractGridFormat format = new GeoTiffFormat();
        File file = TestData.file(GeoTiffReaderTest.class,"002025_0100_010722_l7_01_utm2.tiff");        
        if (format.accepts(file)) {
            // getting a reader
            GeoTiffReader reader = new GeoTiffReader(file, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
            if (reader != null) {
                // reading the coverage
                GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
                assertNotNull(coverage);
                assertTrue(coverage.getRenderedImage().getSampleModel().getNumBands() == 1);
                final ParameterValue<Color> colorPV = AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
                colorPV.setValue(Color.BLACK);
                coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] { colorPV });
                assertNotNull(coverage);
                assertTrue(coverage.getRenderedImage().getSampleModel().getNumBands() == 2);

                // showing it
                if (TestData.isInteractiveTest())
                    coverage.show();
                else
                    PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();

            }

        } else
            assertFalse(true); // we should not get here

        
        file = TestData.file(GeoTiffReaderTest.class,"gaarc_subset.tiff");        
        if (format.accepts(file)) {
            // getting a reader
            GeoTiffReader reader = new GeoTiffReader(file, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
            if (reader != null) {
                // reading the coverage
                GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
                assertNotNull(coverage);
                assertTrue(coverage.getRenderedImage().getSampleModel().getNumBands() == 3);
                final ParameterValue<Color> colorPV = AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
                colorPV.setValue(new Color(34,53,87));
                coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] { colorPV });
                assertNotNull(coverage);
                assertTrue(coverage.getRenderedImage().getSampleModel().getNumBands() == 4);

                // showing it
                if (TestData.isInteractiveTest())
                    coverage.show();
                else
                    PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();

            }

        } else
            assertFalse(true); // we should not get here
        
        // now we test that we cannot do colormasking on a non-rendered output
        file = TestData.file(GeoTiffReaderTest.class,"wind.tiff");        
        if (format.accepts(file)) {
            // getting a reader
            GeoTiffReader reader = new GeoTiffReader(file, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
            if (reader != null) {
                // reading the coverage
                GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
                assertNotNull(coverage);
                assertTrue(coverage.getRenderedImage().getSampleModel().getNumBands() == 2);
                final ParameterValue<Color> colorPV = AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
                colorPV.setValue(new Color(34,53,87));
                try{
                    coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] { colorPV });
                    assertFalse(true); // we should not get here
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        } 
            
    }
    
    @Test
//    @Ignore
    public void testExternalOverviews() throws Exception {
        final File file = TestData.file(GeoTiffReaderTest.class, "ovr.tif");
        assertNotNull(file);
        assertEquals(true, file.exists());
        GeoTiffReaderTester reader = new GeoTiffReaderTester(file);
        final int nOvrs = reader.getNumOverviews();
        LOGGER.info("Number of external overviews: " + nOvrs);
        assertEquals(4, nOvrs);
        double[][] overviewResolutions = reader.getOverviewResolutions();
        assertEquals(overviewResolutions.length, 4);
        
        final ParameterValue<GridGeometry2D> gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(reader.getOriginalGridRange().getSpan(0)/4.0, reader.getOriginalGridRange().getSpan(1)/4.0);
        final Rectangle rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range= new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range,envelope));
        
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {gg});
        RenderedImage image = coverage.getRenderedImage();
        assertEquals(image.getWidth(), 32);
        assertEquals(image.getHeight(), 32);
        
        final double delta = 0.00001;
        assertEquals(overviewResolutions[0][0], 10, delta);
        assertEquals(overviewResolutions[0][1], 10, delta);

        assertEquals(overviewResolutions[1][0], 20, delta);
        assertEquals(overviewResolutions[1][1], 20, delta);

        assertEquals(overviewResolutions[2][0], 40, delta);
        assertEquals(overviewResolutions[2][1], 40, delta);
        
        assertEquals(overviewResolutions[3][0], 80, delta);
        assertEquals(overviewResolutions[3][1], 80, delta);
        
    }

    class GeoTiffReaderTester extends GeoTiffReader {
        public GeoTiffReaderTester(Object input) throws DataSourceException {
            super(input);
        }

        int getNumOverviews() {
            return numOverviews;
        }

        double[][] getOverviewResolutions() {
            return overViewResolutions;
        }
    }
}
