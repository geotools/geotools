/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.spi.ImageReaderSpi;

import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DecimationPolicy;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Testing {@link OverviewsController}.
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imagemosaic/src/test/java/org/geotools/gce/imagemosaic/OverviewsControllerTest.java $
 */
public class OverviewsControllerTest extends Assert{

    static double THRESHOLD = 0.000001;
    
    static class TestSet {
        
        public TestSet(OverviewConfig[] ot) {
            super();
            this.ot = ot;
        }

        double resolution;
        
        OverviewConfig[] ot;
    }
    
    static class GranuleParams {
        
        public GranuleParams(int imageIndex, int ssx, int ssy) {
            super();
            this.imageIndex = imageIndex;
            this.ssx = ssx;
            this.ssy = ssy;
        }

        int imageIndex;
        
        int ssx;
        
        int ssy;
    }
    
    static class OverviewConfig {
        
        public OverviewConfig(OverviewPolicy ovPolicy, GranuleParams g1, GranuleParams g2) {
            super();
            this.ovPolicy = ovPolicy;
            this.g1 = g1;
            this.g2 = g2;
        }

        OverviewPolicy ovPolicy;
        
        GranuleParams g1;
        
        GranuleParams g2;
        
    }
    
    private static CoordinateReferenceSystem WGS84;
    static{
        try{
            WGS84 = CRS.decode("EPSG:4326",true);
        } catch (FactoryException fe){
            WGS84 = DefaultGeographicCRS.WGS84;
        }
    }
    
    private final static TestSet at1 = new TestSet(new OverviewConfig[]{
            new OverviewConfig(OverviewPolicy.QUALITY, new GranuleParams(3, 1, 1), new GranuleParams(2, 1, 2)),
            new OverviewConfig(OverviewPolicy.SPEED, new GranuleParams(4, 1, 1), new GranuleParams(2, 1, 2)),
            new OverviewConfig(OverviewPolicy.NEAREST, new GranuleParams(3, 1, 1), new GranuleParams(2, 1, 2)),
            new OverviewConfig(OverviewPolicy.IGNORE, new GranuleParams(0, 9, 8), new GranuleParams(0, 5, 5))});
    private final static TestSet at2 = new TestSet(new OverviewConfig[]{
            new OverviewConfig(OverviewPolicy.QUALITY, new GranuleParams(3, 1, 1), new GranuleParams(2, 1, 2)),
            new OverviewConfig(OverviewPolicy.SPEED, new GranuleParams(4, 1, 1), new GranuleParams(2, 1, 2)),
            new OverviewConfig(OverviewPolicy.NEAREST, new GranuleParams(3, 1, 1), new GranuleParams(2, 1, 2)),
            new OverviewConfig(OverviewPolicy.IGNORE, new GranuleParams(0, 9, 9), new GranuleParams(0, 5, 5))});
    
    
    
    private final static Logger LOGGER = Logger.getLogger(OverviewsControllerTest.class.toString());
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(OverviewsControllerTest.class);
    }

    private static final ReferencedEnvelope TEST_BBOX_A = new ReferencedEnvelope(-180,0,-90,90,WGS84);
    private static final ReferencedEnvelope TEST_BBOX_B = new ReferencedEnvelope(0,180,0,90,WGS84);
    
    

    private static final ImageReaderSpi spi = new TIFFImageReaderSpi();
    
    private URL heterogeneousGranulesURL;
	
    /**
     * Tests the {@link OverviewsController} with support for different
     * resolutions/different number of overviews.
     * 
     * world_a.tif => Pixel Size = (0.833333333333333,-0.833333333333333); 4 overviews 
     * world_b.tif => Pixel Size = (1.406250000000000,-1.406250000000000); 2 overviews 
     * 
     * @throws IOException
     * @throws MismatchedDimensionException
     * @throws FactoryException
     * @throws TransformException 
     */
    @Test
    public void testHeterogeneousGranules() throws IOException,
            MismatchedDimensionException, FactoryException, TransformException {

        // //
        //
        // Initialize mosaic variables
        //
        // //
        final AbstractGridFormat format = getFormat(heterogeneousGranulesURL);
        final ImageMosaicReader reader = getReader(heterogeneousGranulesURL, format);
        final int nOv = reader.getNumberOfOvervies();
        final double[] hRes = reader.getHighestRes();
        final RasterManager rasterManager = new RasterManager(reader);
        
        // //
        //
        // Initialize granules related variables 
        //
        // //
        final File g1File = new File(DataUtilities.urlToFile(heterogeneousGranulesURL), "world_a.tif");
        final File g2File = new File(DataUtilities.urlToFile(heterogeneousGranulesURL), "world_b.tif");
        final ImageReadParam readParamsG1 = new ImageReadParam();
        final ImageReadParam readParamsG2 = new ImageReadParam();
        int imageIndexG1 = 0;
        int imageIndexG2 = 0;
        
        final GranuleDescriptor granuleDescriptor1 = new GranuleDescriptor(g1File.getAbsolutePath(), TEST_BBOX_A, spi, (Geometry) null, true);
        final GranuleDescriptor granuleDescriptor2 = new GranuleDescriptor(g2File.getAbsolutePath(), TEST_BBOX_B, spi, (Geometry) null, true);
        assertNotNull(granuleDescriptor1.toString());
        assertNotNull(granuleDescriptor2.toString());
        
        final OverviewsController ovControllerG1 = granuleDescriptor1.overviewsController;
        final OverviewsController ovControllerG2 = granuleDescriptor2.overviewsController;
        
        // //
        //
        // Initializing read request
        //
        // //
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final GridEnvelope originalRange = reader.getOriginalGridRange();
        final Rectangle rasterArea = new Rectangle(0, 0, (int) Math.ceil(originalRange.getSpan(0) / 9.0), (int) Math.ceil(originalRange.getSpan(1) / 9.0));
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        final GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper(range, envelope);
        geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);
        final AffineTransform gridToWorld = geMapper.createAffineTransform();
        final double requestedResolution[] = new double[]{XAffineTransform.getScaleX0(gridToWorld), XAffineTransform.getScaleY0(gridToWorld)}; 
        
        TestSet at = null;
        if (nOv == 4 && Math.abs(hRes[0] - 0.833333333333) <= THRESHOLD) {
            at = at1;
        } else if (nOv == 2 && Math.abs(hRes[0] - 1.40625) <= THRESHOLD) { 
            at = at2;
        } else {
            return;
        }

        
        // //
        //
        // Starting OverviewsController tests
        //
        // //
        final OverviewPolicy[] ovPolicies = new OverviewPolicy[]{OverviewPolicy.QUALITY, OverviewPolicy.SPEED, OverviewPolicy.NEAREST, OverviewPolicy.IGNORE};
        for (int i = 0; i < ovPolicies.length; i++){
            OverviewPolicy ovPolicy = ovPolicies[i]; 
            LOGGER.info("Testing with OverviewPolicy = " + ovPolicy.toString());
            imageIndexG1 = ReadParamsController.setReadParams(requestedResolution, ovPolicy, DecimationPolicy.ALLOW, readParamsG1, rasterManager, ovControllerG1);
            imageIndexG2 = ReadParamsController.setReadParams(requestedResolution, ovPolicy, DecimationPolicy.ALLOW, readParamsG2, rasterManager, ovControllerG2);
            assertSame(at.ot[i].g1.imageIndex, imageIndexG1);
            assertSame(at.ot[i].g2.imageIndex, imageIndexG2);
            assertSame(at.ot[i].g1.ssx, readParamsG1.getSourceXSubsampling());
            assertSame(at.ot[i].g1.ssy, readParamsG1.getSourceYSubsampling());
            assertSame(at.ot[i].g2.ssx, readParamsG2.getSourceXSubsampling());
            assertSame(at.ot[i].g2.ssy, readParamsG2.getSourceYSubsampling());

        }
    }

    /**
     * returns an {@link AbstractGridCoverage2DReader} for the provided
     * {@link URL} and for the providede {@link AbstractGridFormat}.
     * 
     * @param testURL
     *            points to a valid object to create an
     *            {@link AbstractGridCoverage2DReader} for.
     * @param format
     *            to use for instantiating such a reader.
     * @return a suitable {@link ImageMosaicReader}.
     * @throws FactoryException
     * @throws NoSuchAuthorityCodeException
     */
    private ImageMosaicReader getReader(URL testURL, final AbstractGridFormat format)
            throws NoSuchAuthorityCodeException, FactoryException {

        final Hints hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, WGS84);
        final ImageMosaicReader reader = (ImageMosaicReader) format.getReader(testURL, hints);
        Assert.assertNotNull(reader);
        return reader;
    }

    /**
     * Tries to get an {@link AbstractGridFormat} for the provided URL.
     * 
     * @param testURL
     *            points to a shapefile that is the index of a certain mosaic.
     * @return a suitable {@link AbstractGridFormat}.
     * @throws FactoryException
     * @throws NoSuchAuthorityCodeException
     */
    private AbstractGridFormat getFormat(URL testURL) throws NoSuchAuthorityCodeException, FactoryException {

        final Hints hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, WGS84);

        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(testURL, hints);
        Assert.assertNotNull(format);
        Assert.assertFalse("UknownFormat", format instanceof UnknownFormat);
        return format;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TestRunner.run(OverviewsControllerTest.suite());

    }

    @Before
    public void setUp() throws Exception {
        // remove generated file

        cleanUp();

        heterogeneousGranulesURL = TestData.url(this, "heterogeneous");
    }

    /**
     * Cleaning up the generated files (shape and properties so that we recreate
     * them).
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void cleanUp() throws FileNotFoundException, IOException {
        File dir = TestData.file(this, "heterogeneous/");
        File[] files = dir
                .listFiles((FilenameFilter) FileFilterUtils.notFileFilter(FileFilterUtils.orFileFilter(
                        FileFilterUtils.orFileFilter(
                                FileFilterUtils.suffixFileFilter("tif"),
                                FileFilterUtils.suffixFileFilter("aux")),
                        FileFilterUtils.nameFileFilter("datastore.properties"))));
        for (File file : files) {
            file.delete();
        }
    }

    @After
    public void tearDown() throws FileNotFoundException, IOException {
        cleanUp();
    }

}
