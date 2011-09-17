/*
 *    GeoTool s - The Open Source Java GIS Toolkit
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
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import junit.framework.Assert;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DecimationPolicy;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.test.TestData;
import org.junit.Ignore;
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
 *
 * @sourceFile $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/geotiff_new/src/test/java/org/geotools/gce/geotiff/GeoTiffReaderTest.java $
 */
public class GeoTiffReaderTest extends Assert {
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(GeoTiffReaderTest.class.toString());

    /**
     * Test for reading bad/strange geotiff files
     * 
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws FactoryException
     */
    @Test
    @Ignore
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
     * Test for reading bad/strange geotiff files
     * 
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws FactoryException
     */
    @Test
//    @Ignore
    public void testDirectRead() throws IllegalArgumentException, IOException,
            FactoryException {

        //
        // no crs geotiff
        //
        final File sampleFile = TestData.file(GeoTiffReaderTest.class, "geo.tiff");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(sampleFile));
        GeoTiffReader reader = (GeoTiffReader) format.getReader(sampleFile);
        ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);
        
        ParameterValue<OverviewPolicy> op = AbstractGridFormat.OVERVIEW_POLICY.createValue();
        op.setValue(OverviewPolicy.NEAREST);
        
        ParameterValue<DecimationPolicy> dp = AbstractGridFormat.DECIMATION_POLICY.createValue();
        dp.setValue(DecimationPolicy.ALLOW);
        
        ParameterValue<Color> color = AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
        color.setValue(new Color(0,0,0));
        
        ParameterValue<double[]> bv = GeoTiffFormat.BACKGROUND_VALUES.createValue();
        bv.setValue(new double[]{255,255,255});
        
        ParameterValue<String> ts = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        ts.setValue("512,512");
        
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[]{useJai, dp, op, color, bv, ts});

        assertNotNull(coverage);
        reader.dispose();
        coverage.dispose(true);
    }
    
    /**
     * Test for reading geotiff files
     * 
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws NoSuchAuthorityCodeException
     */
    @Test
//    @Ignore
    public void testReader() throws IllegalArgumentException, IOException,
            NoSuchAuthorityCodeException {

        final File file = TestData.file(this, ".");
        final File files[] = file.listFiles();
        final int numFiles = files.length;
        final AbstractGridFormat format = new GeoTiffFormat();
        for (int i = 0; i < numFiles; i++) {
    		StringBuilder buffer = new StringBuilder();
            final String path = files[i].getAbsolutePath().toLowerCase();
    		if (!path.endsWith("tif") && !path.endsWith("tiff")||path.contains("no_crs"))
                continue;
            if (path.contains("ovr.tif"))
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
                GeoTiffReader reader = new GeoTiffReader(o, new Hints(
                        Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));

                if (reader != null) {

                    // reading the coverage
                    GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

                    // Crs and envelope
                    if (TestData.isInteractiveTest()) {
                        buffer.append("CRS: ")
                                .append(coverage.getCoordinateReferenceSystem2D().toWKT())
                                .append("\n");
                        buffer.append("GG: ").append(coverage.getGridGeometry().toString())
                                .append("\n");
                    }

                    // showing it
                    if (TestData.isInteractiveTest())
                        coverage.show();
                    else
    					PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();

                }

            } else
                buffer.append("NOT ACCEPTED").append("\n");
            if (TestData.isInteractiveTest())
                LOGGER.info(buffer.toString());

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
        LOGGER.info("Number of external levels: " + nOvrs);
        assertEquals(4, nOvrs);
        double[][] overviewResolutions = reader.getOverviewResolutions();
        assertEquals(overviewResolutions.length, 4);

        final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(reader.getOriginalGridRange().getSpan(0) / 2.0, reader.getOriginalGridRange()
                .getSpan(1) / 2.0);
        Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] { gg });
        RenderedImage image = coverage.getRenderedImage();
        assertEquals(image.getWidth(), 180);
        assertEquals(image.getHeight(), 90);
        
        dim.setSize(reader.getOriginalGridRange().getSpan(0) / 4.0, reader.getOriginalGridRange()
                .getSpan(1) / 4.0);
        rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        coverage = reader.read(new GeneralParameterValue[] { gg });
        image = coverage.getRenderedImage();
        assertEquals(image.getWidth(), 90);
        assertEquals(image.getHeight(), 45);

        final double delta = 0.00001;
        assertEquals(overviewResolutions[0][0], 2.0, delta);
        assertEquals(overviewResolutions[0][1], 2.0, delta);

        assertEquals(overviewResolutions[1][0], 4.0, delta);
        assertEquals(overviewResolutions[1][1], 4.0, delta);

        assertEquals(overviewResolutions[2][0], 8, delta);
        assertEquals(overviewResolutions[2][1], 7.826086956, delta);

        assertEquals(overviewResolutions[3][0], 15.6521739, delta);
        assertEquals(overviewResolutions[3][1], 15, delta);
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
