/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.Point2D;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.coverage.grid.io.footprint.FootprintInsetPolicy;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProviderFactory;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.test.TestData;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTWriter;

public class ImageMosaicFootprintsTest {

    private File testMosaic;

    private URL testMosaicUrl;

    private File footprintsSource;

    @Before
    public void cleanup() throws IOException {
        // clean up
        testMosaic = new File(TestData.file(this,"."),"footprintMosaic");
        if (testMosaic.exists()) {
            FileUtils.deleteDirectory(testMosaic);
        }

        // create the base mosaic we are going to use
        File mosaicSource = TestData.file(this,"rgb");
        FileUtils.copyDirectory(mosaicSource, testMosaic);
        testMosaicUrl = DataUtilities.fileToURL(testMosaic);
        
        // footprint source
        footprintsSource = TestData.file(this,"rgb-footprints");
    }

    @Test
    public void testSingleShapefileDefaults() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);

        assertItalyFootprints();
    }

    @Test
    public void testWkbSidecars() throws Exception {
        // create wkb sidecar files
        ShapefileDataStore ds = new ShapefileDataStore(DataUtilities.fileToURL(new File(
                footprintsSource, "footprints.shp")));
        ds.getFeatureSource().getFeatures().accepts(new FeatureVisitor() {

            @Override
            public void visit(Feature feature) {
                try {
                    SimpleFeature sf = (SimpleFeature) feature;
                    String fileName = (String) sf.getAttribute("location");
                    int idx = fileName.lastIndexOf(".");
                    Geometry g = (Geometry) sf.getDefaultGeometry();
                    File wkbFile = new File(testMosaic, fileName.substring(0, idx) + ".wkb");
                    byte[] bytes = new WKBWriter().write(g);
                    FileUtils.writeByteArrayToFile(wkbFile, bytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, null);
        ds.dispose();
        assertItalyFootprints();
    }

    @Test
    public void testWktSidecars() throws Exception {
        // create wkb sidecar files
        ShapefileDataStore ds = new ShapefileDataStore(DataUtilities.fileToURL(new File(
                footprintsSource, "footprints.shp")));
        ds.getFeatureSource().getFeatures().accepts(new FeatureVisitor() {

            @Override
            public void visit(Feature feature) {
                try {
                    SimpleFeature sf = (SimpleFeature) feature;
                    String fileName = (String) sf.getAttribute("location");
                    int idx = fileName.lastIndexOf(".");
                    Geometry g = (Geometry) sf.getDefaultGeometry();
                    File wkbFile = new File(testMosaic, fileName.substring(0, idx) + ".wkt");
                    String wkt = new WKTWriter().write(g);
                    FileUtils.writeStringToFile(wkbFile, wkt);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, null);
        ds.dispose();
        assertItalyFootprints();
    }

    @Test
    public void testShapefileSidecars() throws Exception {
        // create wkb sidecar files
        ShapefileDataStore ds = new ShapefileDataStore(DataUtilities.fileToURL(new File(
                footprintsSource, "footprints.shp")));
        ds.getFeatureSource().getFeatures().accepts(new FeatureVisitor() {

            @Override
            public void visit(Feature feature) {
                try {
                    SimpleFeature sf = (SimpleFeature) feature;
                    String fileName = (String) sf.getAttribute("location");
                    int idx = fileName.lastIndexOf(".");
                    Geometry g = (Geometry) sf.getDefaultGeometry();
                    String filename = fileName.substring(0, idx);
                    File shpFile = new File(testMosaic, filename + ".shp");
                    ShapefileDataStore sds = new ShapefileDataStore(
                            DataUtilities.fileToURL(shpFile));
                    SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
                    tb.setName(filename);
                    GeometryDescriptor gd = sf.getFeatureType().getGeometryDescriptor();
                    tb.add("the_geom", gd.getType().getBinding(), gd.getCoordinateReferenceSystem());
                    SimpleFeatureType sft = tb.buildFeatureType();
                    sds.createSchema(sft);

                    SimpleFeatureBuilder fb = new SimpleFeatureBuilder(sft);
                    fb.add(g);
                    SimpleFeature footprintFeature = fb.buildFeature(null);
                    SimpleFeatureStore fs = (SimpleFeatureStore) sds.getFeatureSource();
                    fs.addFeatures(DataUtilities.collection(footprintFeature));
                    sds.dispose();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, null);

        assertItalyFootprints();
    }

    private void assertItalyFootprints() throws NoSuchAuthorityCodeException, FactoryException,
            IOException {
        GridCoverage2D coverage = readCoverage();

        // RenderedImageBrowser.showChain(coverage.getRenderedImage());
        // System.in.read();

        // check the footprints have been applied by pocking the output image
        byte[] pixel = new byte[3];
        // Mar Ionio, should be black
        coverage.evaluate(new DirectPosition2D(16.87, 40.19), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Golfo di La Spezia, should be black
        coverage.evaluate(new DirectPosition2D(9.12, 44.25), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Sardinia, not black
        coverage.evaluate(new DirectPosition2D(9, 40), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        // Piedmont, not black
        coverage.evaluate(new DirectPosition2D(8, 45), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
    }

    private GridCoverage2D readCoverage() throws NoSuchAuthorityCodeException, FactoryException,
            IOException {
        final AbstractGridFormat format = TestUtils.getFormat(testMosaicUrl);
        final ImageMosaicReader reader = TestUtils.getReader(testMosaicUrl, format);
        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[2];
        ParameterValue<String> footprintManagement = AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Cut.name());
        params[0] = footprintManagement;
        
        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false); 
        params[1] = jaiImageRead;
        GridCoverage2D coverage = reader.read(params);
        reader.dispose();
        assertNotNull(coverage);
        return coverage;
    }
    @Test
    public void testAreaOutside() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);
        Properties p = new Properties();
        p.put(FootprintInsetPolicy.INSET_PROPERTY, "0.1"); 
        saveFootprintProperties(p);
        final AbstractGridFormat format = TestUtils.getFormat(testMosaicUrl);
        final ImageMosaicReader reader = TestUtils.getReader(testMosaicUrl, format);     
        
        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[3];
        ParameterValue<String> footprintManagement = AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.None.name());
        params[0] = footprintManagement;
        
        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false); 
        params[1] = jaiImageRead;
        
        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                .createValue();
        final Dimension dim = new Dimension();
        dim.setSize(4, 4);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, PixelInCell.CELL_CENTER,reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),reader.getCoordinateReferenceSystem(),null));
        params[2]=gg;
        
        GridCoverage2D coverage = reader.read(params);
        reader.dispose();
        assertNotNull(coverage);
    }
    
    @Test
    public void testRequestHole() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);
        Properties p = new Properties();
        p.put(FootprintInsetPolicy.INSET_PROPERTY, "0.1");
        saveFootprintProperties(p);
        final AbstractGridFormat format = TestUtils.getFormat(testMosaicUrl);
        ImageMosaicReader reader = TestUtils.getReader(testMosaicUrl, format);
        reader.dispose();
        // get rid of the sample image
        File sampleImage = new File(testMosaic, "sample_image");
        sampleImage.delete();
        // a new reader without the sample image, in normal conditions it can actually produce
        // output
        reader = TestUtils.getReader(testMosaicUrl, format);

        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[3];
        ParameterValue<String> footprintManagement = AbstractGridFormat.FOOTPRINT_BEHAVIOR
                .createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;

        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false);
        params[1] = jaiImageRead;

        // limit yourself to reading just a bit of it
        MathTransform mt = reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER);
        GridEnvelope2D ge = new GridEnvelope2D(6, 44, 1, 1);
        final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                .createValue();
        gg.setValue(new GridGeometry2D(ge, mt, DefaultGeographicCRS.WGS84));
        params[2] = gg;

        // read the first time, no sample_image yet present
        GridCoverage2D coverage = reader.read(params);
        reader.dispose();
        assertNotNull(coverage);
        RenderedImage ri = coverage.getRenderedImage();
        assertNotEquals(Transparency.OPAQUE, ri.getColorModel().getTransparency());
        reader.dispose();
        
        // read a second time
        reader = TestUtils.getReader(testMosaicUrl, format);
        coverage = reader.read(params);
        reader.dispose();
        assertNotNull(coverage);
        ri = coverage.getRenderedImage();
        assertNotEquals(Transparency.OPAQUE, ri.getColorModel().getTransparency());
        reader.dispose();
    }

    @Test
    public void testInsetsFull() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);
        Properties p = new Properties();
        p.put(FootprintInsetPolicy.INSET_PROPERTY, "0.1"); 
        p.put(FootprintInsetPolicy.INSET_TYPE_PROPERTY, "full");
        saveFootprintProperties(p);

        GridCoverage2D coverage = readCoverage();
        
        // check the footprints have been applied by pocking the output image
        byte[] pixel = new byte[3];
        // Close to San Marino, black if we have the insets
        coverage.evaluate(new DirectPosition2D(12.54, 44.03), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Inner BORDER gets black with FULL insets
        coverage.evaluate(new DirectPosition2D(11.52, 44.57), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Golfo di La Spezia, should be black
        coverage.evaluate(new DirectPosition2D(9.12, 44.25), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Sardinia, not black
        coverage.evaluate(new DirectPosition2D(9, 40), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        // Piedmont, not black
        coverage.evaluate(new DirectPosition2D(8, 45), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        disposeCoverage(coverage);
        
        final ImageMosaicReader reader = TestUtils.getReader(testMosaicUrl, new ImageMosaicFormat());
        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[3];
        ParameterValue<String> footprintManagement = AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;
        
        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false); 
        params[1] = jaiImageRead;
        
        // GridGeometry, small aread at the upper right corner
        final GridEnvelope2D ge2D= new GridEnvelope2D(
                reader.getOriginalGridRange().getHigh(0)-3, 
                reader.getOriginalGridRange().getLow(1), 
                3, 
                3);
        final GridGeometry2D gg2D= new GridGeometry2D(ge2D, reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER), reader.getCoordinateReferenceSystem());
        ParameterValue<GridGeometry2D> gg2DParam = ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
        gg2DParam.setValue(gg2D); 
        params[2] = gg2DParam;
        
        coverage = reader.read(params);
        MathTransform tr = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        reader.dispose();
        assertNotNull(coverage);
     
        // check the footprints have been applied by pocking the output image
        pixel = new byte[4];
        // Close to San Marino, black if we have the insets
        coverage.evaluate(new DirectPosition2D(coverage.getEnvelope().getMinimum(0) + 1e-3, coverage.getEnvelope().getMinimum(1) + 1e-3), pixel);

        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        assertEquals(0, pixel[3]);
        
        disposeCoverage(coverage);
    }
    
    @Test
    public void testInsetsMargin() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);
        Properties p = new Properties();
        p.put(FootprintInsetPolicy.INSET_PROPERTY, "0.1"); 
        p.put(FootprintInsetPolicy.INSET_TYPE_PROPERTY, "border");
        saveFootprintProperties(p);

        GridCoverage2D coverage = readCoverage();
        
//        // check the footprints have been applied by pocking the output image
        byte[] pixel = new byte[3];
        // Close to San Marino, black if we have the insets
        coverage.evaluate(new DirectPosition2D(12.54, 44.03), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Inner BORDER should not get black with border insets
        coverage.evaluate(new DirectPosition2D(11.52, 44.57), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        // Golfo di La Spezia, should be black
        coverage.evaluate(new DirectPosition2D(9.12, 44.25), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Sardinia, not black
        coverage.evaluate(new DirectPosition2D(9, 40), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        // Piedmont, not black
        coverage.evaluate(new DirectPosition2D(8, 45), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        disposeCoverage(coverage);
        
        final ImageMosaicReader reader = TestUtils.getReader(testMosaicUrl, new ImageMosaicFormat());
        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[3];
        ParameterValue<String> footprintManagement = AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;
        
        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false); 
        params[1] = jaiImageRead;
        
        // GridGeometry, small read at the upper right corner
        final GridEnvelope2D ge2D= new GridEnvelope2D(
                reader.getOriginalGridRange().getHigh(0)-3, 
                reader.getOriginalGridRange().getLow(1), 
                3, 
                3);
        final GridGeometry2D gg2D= new GridGeometry2D(ge2D, reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER), reader.getCoordinateReferenceSystem());
        ParameterValue<GridGeometry2D> gg2DParam = ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
        gg2DParam.setValue(gg2D); 
        params[2] = gg2DParam;
        
        coverage = reader.read(params);
        MathTransform tr = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        reader.dispose();
        assertNotNull(coverage);
     
        // check the footprints have been applied by pocking the output image
        pixel = new byte[4];
        // Close to San Marino, black if we have the insets
        coverage.evaluate(new DirectPosition2D(coverage.getEnvelope().getMinimum(0)  + 1e-3 ,coverage.getEnvelope().getMinimum(1) + 1e-3), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        assertEquals(0, pixel[3]);
        
        disposeCoverage(coverage);
    }

    /**
     * Dispose the provided coverage for good.
     * @param coverage
     */
    private void disposeCoverage(GridCoverage2D coverage) {
        if(coverage==null){
            return;
        }
        final RenderedImage im= coverage.getRenderedImage();
        ImageUtilities.disposePlanarImageChain(PlanarImage.wrapRenderedImage(im));
        coverage.dispose(true);
        
    }

    private void saveFootprintProperties(Properties p) throws FileNotFoundException, IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(testMosaic, "footprints.properties"));
            p.store(fos, null);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    @AfterClass
    public static void close(){
    	System.clearProperty("org.geotools.referencing.forceXY");
            CRS.reset("all");
    }

    @BeforeClass
    public static void init(){
    	
    	//make sure CRS ordering is correct
        CRS.reset("all");
        System.setProperty("org.geotools.referencing.forceXY", "true");
    }

    @Test
    public void testInsetsBorder() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);
        Properties p = new Properties();
        p.put(FootprintInsetPolicy.INSET_PROPERTY, "0.1");
        saveFootprintProperties(p);
        GridCoverage2D coverage = readCoverage();
        // check the footprints have been applied by pocking the output image
        byte[] pixel = new byte[3];
        // Close to San Marino, black if we have the insets
        coverage.evaluate(new DirectPosition2D(12.54, 44.03), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Golfo di La Spezia, should be black
        coverage.evaluate(new DirectPosition2D(9.12, 44.25), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Sardinia, not black
        coverage.evaluate(new DirectPosition2D(9, 40), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        // Piedmont, not black
        coverage.evaluate(new DirectPosition2D(8, 45), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        disposeCoverage(coverage);
    }
    
    @Test
    public void testFootprintA() throws IOException {
        ImageMosaicReader reader = (ImageMosaicReader) new ImageMosaicFormatFactory().createFormat()
                .getReader(TestData.file(this,"footprint_a"));
        GeneralParameterValue[] params = new GeneralParameterValue[1];
        ParameterValue<String> footprintManagement = AbstractGridFormat.FOOTPRINT_BEHAVIOR
                .createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;
        
        GridCoverage2D coverage = reader.read(params);
        
        byte[] result = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        position.setLocation(1, 1);
        coverage.evaluate(position, result);

        //RGBA
        assertEquals(4, coverage.getSampleDimensions().length);
        
        //Transparent
        assertEquals(0, result[3]);

        position = new DirectPosition2D();
        position.setLocation(-1, -1);
        coverage.evaluate(position, result);
        
        //Blue
        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
        assertTrue(0 != result[2]);
        assertTrue(0 != result[3]);
    }

    @Test
    public void testFootprintRGB() throws FileNotFoundException, IOException {
        testFootprint(TestData.file(this,"footprint_rgb"));
    }
    
    @Test
    public void testFootprintRGBA() throws FileNotFoundException, IOException {
        testFootprint(TestData.file(this,"footprint_rgba"));
    }
    
    public void testFootprint(File mosaic) throws IOException {
        ImageMosaicReader reader = (ImageMosaicReader) new ImageMosaicFormatFactory().createFormat()
                .getReader(mosaic);
        
        GeneralParameterValue[] params = new GeneralParameterValue[1];
        ParameterValue<String> footprintManagement = AbstractGridFormat.FOOTPRINT_BEHAVIOR
                .createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;
        
        GridCoverage2D coverage = reader.read(params);
        
        byte[] result = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        position.setLocation(1, 1);
        coverage.evaluate(position, result);
        
        //Red
        assertTrue(0 != result[0]);
        assertEquals(0, result[1]);
        assertEquals(0, result[2]);
        assertTrue(0 != result[3]);
        
        position = new DirectPosition2D();
        position.setLocation(-1, -1);
        coverage.evaluate(position, result);
        
        //Blue
        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
        assertTrue(0 != result[2]);
        assertTrue(0 != result[3]);
    }

    @Test
    public void testRasterFootprintExternal() throws Exception {
        // Raster
        File testMosaicRaster = new File(TestData.file(this, "."), "footprintRaster");
        if (testMosaicRaster.exists()) {
            FileUtils.deleteDirectory(testMosaicRaster);
        }
        // Reading Coverage with Raster footprint
        GridCoverage2D coverage = readRasterFootprint("rastermask", testMosaicRaster, false);
        
        // Evaluate results
        byte[] results = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0
        position.setLocation(-86.724, 25.085);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-86.252, 27.7984);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-87.937, 26.144);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-89.084, 27.133);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-89.763, 25.167);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
    }

    @Test
    public void testRasterFootprintSubmsampling() throws Exception {
        // Raster
        File testMosaicRaster = new File(TestData.file(this, "."), "footprintRasterSubsampling");
        if (testMosaicRaster.exists()) {
            FileUtils.deleteDirectory(testMosaicRaster);
        }
        // Reading Coverage with Raster footprint
        GridCoverage2D coverage = readRasterFootprint("masked2", testMosaicRaster, true);

        // check the ROI and the image are black in the same pixels
        ROI roi = CoverageUtilities.getROIProperty(coverage);
        Raster roiImage = roi.getAsImage().getData();
        Raster image = coverage.getRenderedImage().getData();

        int[] px = new int[4];
        int[] rpx = new int[1];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                image.getPixel(j, i, px);
                roiImage.getPixel(j, i, rpx);
                if (px[0] == 0 && px[1] == 0 && px[2] == 0) {
                    assertEquals("Difference at " + i + "," + j, 0, rpx[0]);
                } else {
                    assertEquals("Difference at " + i + "," + j, 1, rpx[0]);
                }
            }
        }
    }

    @Test
    public void testRasterFootprintInternal() throws Exception {
        // Raster
        File testMosaicRaster = new File(TestData.file(this, "."), "footprintRaster");
        if (testMosaicRaster.exists()) {
            FileUtils.deleteDirectory(testMosaicRaster);
        }

        // Reading Coverage with Raster footprint
        GridCoverage2D coverage = readRasterFootprint("rastermask2", testMosaicRaster, false);
        
        // Evaluate results
        byte[] results = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0
        position.setLocation(-86.724, 25.085);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-86.252, 27.7984);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-87.937, 26.144);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-89.084, 27.133);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-89.763, 25.167);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
    }

    @Test
    public void testRasterFootprintExternalMask() throws Exception {
        // Raster
        File testMosaicRaster = new File(TestData.file(this, "."), "footprintRaster");
        if (testMosaicRaster.exists()) {
            FileUtils.deleteDirectory(testMosaicRaster);
        }

        // Reading Coverage with Raster footprint
        GridCoverage2D coverage = readRasterFootprint("rastermask", testMosaicRaster, true);
        
        // Evaluate results
        byte[] results = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0 but since the mask is subsampled, it may happen that the
        // final pixel is not masked
        position.setLocation(-86.724, 25.085);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be > 0
        position.setLocation(-86.252, 27.7984);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be  0 but since the mask is subsampled, it may happen that the
        // final pixel is not masked
        position.setLocation(-87.937, 26.144);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be > 0
        position.setLocation(-89.084, 27.133);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0 
        position.setLocation(-89.763, 25.167);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
    }

    @Test
    public void testRasterFootprintInternalMaskAndOverviews() throws Exception {
        // Raster
        File testMosaicRaster = new File(TestData.file(this, "."), "footprintRaster");
        if (testMosaicRaster.exists()) {
            FileUtils.deleteDirectory(testMosaicRaster);
        }

        // Reading Coverage with Raster footprint
        GridCoverage2D coverage = readRasterFootprint("rastermask2", testMosaicRaster, true);
        
        // Evaluate results
        byte[] results = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0
        position.setLocation(-86.724, 25.085);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-86.252, 27.7984);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be  0
        position.setLocation(-87.937, 26.144);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-89.084, 27.133);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-89.763, 25.167);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
    }

    private GridCoverage2D readRasterFootprint(String path, File testMosaicRaster,
            boolean testOverviews) throws Exception {
        // create the base mosaic we are going to use
        File mosaicSourceRaster = TestData.file(this, path);
        FileUtils.copyDirectory(mosaicSourceRaster, testMosaicRaster);
        URL testMosaicRasterUrl = DataUtilities.fileToURL(testMosaicRaster);
        // copy the footprints mosaic properties
        Properties p = new Properties();
        // Setting Raster property
        p.put(MultiLevelROIProviderFactory.SOURCE_PROPERTY, "raster");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(testMosaicRaster, "footprints.properties"));
            p.store(fos, null);
        } finally {
            IOUtils.closeQuietly(fos);
        }
        final AbstractGridFormat format = TestUtils.getFormat(testMosaicRasterUrl);
        final ImageMosaicReader reader = TestUtils.getReader(testMosaicRasterUrl, format);

        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[3];
        ParameterValue<String> footprintManagement = AbstractGridFormat.FOOTPRINT_BEHAVIOR
                .createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;

        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false);
        params[1] = jaiImageRead;

        // setup how much we are going to read
        final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                .createValue();
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        if (testOverviews) {
            Dimension dim = new Dimension();
            dim.setSize(8, 8);
            rasterArea.setSize(dim);
            final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
            gg.setValue(new GridGeometry2D(range, reader.getOriginalEnvelope()));
            params[2] = gg;
        } else {
            final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
            gg.setValue(new GridGeometry2D(range, PixelInCell.CELL_CENTER, reader
                    .getOriginalGridToWorld(PixelInCell.CELL_CENTER), reader
                    .getCoordinateReferenceSystem(), null));
            params[2] = gg;
        }
        // Read the coverage
        GridCoverage2D coverage = reader.read(params);
        reader.dispose();
        assertNotNull(coverage);
        // Check if ROI is present
        ROI roi = CoverageUtilities.getROIProperty(coverage);
        assertNotNull(roi);

        // Checking ROI Bounds
        // Ensure has the same size of the input image
        Rectangle roiBounds = roi.getBounds();
        Rectangle imgBounds = coverage.getGridGeometry().getGridRange2D();
        assertEquals(imgBounds.x, roiBounds.x);
        assertEquals(imgBounds.y, roiBounds.y);
        assertEquals(imgBounds.width, roiBounds.width);
        assertEquals(imgBounds.height, roiBounds.height);

        return coverage;
    }

    @Rule
    public TemporaryFolder redFootprintFolder = new TemporaryFolder();
    /**
     * When the mosaic bounds don't match the requested image bounds, there's only one granule in the requested bounds
     * and FootprintBehavior is transparent a border is added to the image. This actually only happens in
     * very specific circumstances, like in the test data which is an L shaped. In this case the
     * footprint behavior was not being respected, resulting in a background color even though the
     * background should be transparent.
     *
     */
    @Test
    public void testFootprintWithBorderNeeded() throws IOException {
        File testFolder = redFootprintFolder.newFolder();
        File mosaic = TestData.file(this, "red_footprint_test");
        FileUtils.copyDirectory(mosaic, testFolder);
        ImageMosaicReader reader = (ImageMosaicReader) new ImageMosaicFormatFactory().createFormat()
            .getReader(testFolder);

        ParameterValue<String> footprintBehaviorParam = AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintBehaviorParam.setValue(FootprintBehavior.Transparent.name());

        ParameterValue<GridGeometry2D> readGeom = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();

        CoordinateReferenceSystem coordinateReferenceSystem = reader.getOriginalEnvelope()
            .getCoordinateReferenceSystem();

        GridEnvelope2D gridRange = new GridEnvelope2D(0,0,100,100);
        Envelope requestEnvelope = new ReferencedEnvelope(989964.5828856088,
            990881.0173239836, 218260.08651691137, 219176.52095528613, coordinateReferenceSystem);
        GridGeometry2D readGeometry = new GridGeometry2D(gridRange, requestEnvelope);
        readGeom.setValue(readGeometry);
        GeneralParameterValue[] readParams = new GeneralParameterValue[]{footprintBehaviorParam, readGeom};
        GridCoverage2D coverage = reader.read(readParams);

        int numComponents = coverage.getRenderedImage().getColorModel().getNumComponents();
        assertEquals(numComponents, 4);
    }

}
