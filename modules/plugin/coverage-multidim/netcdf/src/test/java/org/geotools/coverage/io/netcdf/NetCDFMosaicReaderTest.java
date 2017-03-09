/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.media.jai.ImageLayout;
import javax.media.jai.PlanarImage;
import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.HarvestedSource;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.imageio.netcdf.NetCDFImageReaderSpi;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import it.geosolutions.imageio.utilities.ImageIOUtilities;
import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;
import ucar.nc2.Variable;

/**
 * Testing {@link ImageMosaicReader}.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de
 * @since 2.3
 * 
 * 
 * 
 * @source $URL$
 */
public class NetCDFMosaicReaderTest extends Assert {

    private final static Logger LOGGER = Logging.getLogger(NetCDFMosaicReaderTest.class.toString());

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(NetCDFMosaicReaderTest.class);
    }

    @Test
    public void testHarvestAddTime() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this,"polyphemus_20130301_test.nc");
        File mosaic = new File(TestData.file(this,"."),"nc_harvest1");
        if(mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);
        
        // The indexer
        String indexer = "TimeAttribute=time\n" + 
        		"Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);
        
        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);
        
        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals("O3", names[0]);
            
            // check we have the two granules we expect
            GranuleSource source = reader.getGranules("O3", true);
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            Query q = new Query(Query.ALL);
            q.setSortBy(new SortBy[] {ff.sort("time", SortOrder.ASCENDING)});
            SimpleFeatureCollection granules = source.getGranules(q);
            assertEquals(2, granules.size());
            it = granules.features();
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T01:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();
            
            // now add another netcdf and harvest it
            File nc2 = TestData.file(this,"polyphemus_20130302_test.nc");
            FileUtils.copyFileToDirectory(nc2, mosaic);
            File fileToHarvest = new File(mosaic, "polyphemus_20130302_test.nc");
            List<HarvestedSource> harvestSummary = reader.harvest(null, fileToHarvest, null);
            assertEquals(1, harvestSummary.size());
            HarvestedSource hf = harvestSummary.get(0);
            assertEquals("polyphemus_20130302_test.nc", ((File) hf.getSource()).getName());
            assertTrue(hf.success());
            assertEquals(1, reader.getGridCoverageNames().length);
            
            // check that we have four times now
            granules = source.getGranules(q);
            assertEquals(4, granules.size());
            it = granules.features();
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T01:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            f = it.next();
            assertEquals("polyphemus_20130302_test.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-03-02T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130302_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals("2013-03-02T01:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();

            ImageLayout layout = reader.getImageLayout();
            SampleModel sampleModel = layout.getSampleModel(null);
            assertEquals(DataBuffer.TYPE_FLOAT, sampleModel.getDataType());
        } finally {
            if(it != null) {
                it.close();
            }
            reader.dispose();
        }
    }
    
    @Test
    public void testHeterogeneous() throws IOException, InvalidParameterValueException, ParseException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this,"polyphemus_20130301_test.nc");
        File mosaic = new File(TestData.file(this,"."),"nc_poly_hetero");
        if(mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);
        
        // The indexer
        String indexer = "TimeAttribute=time\n" + 
                "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);
        
        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);
        
        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        assertNotNull(reader);
        reader.dispose();
        
        // now force heterogeneous interpretation
        Properties mosaicProps = new Properties();
        File mosaicPropsFile = new File(mosaic, "O3.properties");
        try(FileInputStream fis = new FileInputStream(mosaicPropsFile)) {
            mosaicProps.load(fis);
        }
        mosaicProps.put("Heterogeneous", "true");
        try(FileOutputStream fos = new FileOutputStream(mosaicPropsFile)) {
            mosaicProps.store(fos, "Now with hetero flag up");
        }
        
        // load two different times, make sure we actually read two different slices
        String t1 = "2013-03-01T00:00:00.000Z";
        String t2 = "2013-03-01T01:00:00.000Z";
        reader = format.getReader(mosaic);
        try {
            // prepare params
            final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
            useJai.setValue(false);
            ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
            time.setValue(Arrays.asList(parseTimeStamp(t1)));
            GeneralParameterValue[] params = new GeneralParameterValue[] { useJai, time };
            // read first
            GridCoverage2D coverage1 = reader.read(params);
            time.setValue(Arrays.asList(parseTimeStamp(t2)));
            GridCoverage2D coverage2 = reader.read(params);
            
            DirectPosition center = reader.getOriginalEnvelope().getMedian();
            float[] v1 = (float[]) coverage1.evaluate(center);
            float[] v2 = (float[]) coverage2.evaluate(center);
            assertNotEquals(v1[0], v2[0], 0f);
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testCustomTimeAttribute() throws IOException {
        File nc1 = TestData.file(this,"polyphemus_20130301_NO2_time2.nc");
        File mosaic = new File(TestData.file(this,"."),"nc_time2");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);

        // The indexer
        String indexer = "TimeAttribute=time\n" + 
                        "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"; 
        final String auxiliaryFilePath = mosaic.getAbsolutePath() + File.separatorChar + ".polyphemus_20130301_NO2_time2";
        final File auxiliaryFileDir = new File(auxiliaryFilePath);
        assertTrue(auxiliaryFileDir.mkdirs());

        File nc1Aux = TestData.file(this,"polyphemus_20130301_NO2_time2.xml");
        FileUtils.copyFileToDirectory(nc1Aux, auxiliaryFileDir);

        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);
        File dsp = TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        NetCDFImageReader imageReader = null;
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals("NO2", names[0]);

            // check we have the two granules we expect
            GranuleSource source = reader.getGranules("NO2", true);
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            Query q = new Query(Query.ALL);
            q.setSortBy(new SortBy[] {ff.sort("time", SortOrder.ASCENDING)});
            SimpleFeatureCollection granules = source.getGranules(q);
            assertEquals(2, granules.size());
            it = granules.features();
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            assertEquals("polyphemus_20130301_NO2_time2.nc", f.getAttribute("location"));
            SimpleFeatureType featureType = f.getType();

            // check the underlying data has a time2 dimension 
            imageReader = (NetCDFImageReader) new NetCDFImageReaderSpi().createReaderInstance();
            imageReader.setInput(nc1);
            Variable var = imageReader.getVariableByName("NO2");
            String dimensions = var.getDimensionsString();
            assertTrue(dimensions.contains("time2"));

            // check I'm getting a "time" attribute instead of "time2" due to the
            // uniqueTimeAttribute remap
            assertNotNull(featureType.getDescriptor("time"));

        } finally {
            if (it != null) {
                it.close();
            }

            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Exception e) {
                    // Ignore exception on dispose
                }
            }
            if (imageReader != null) {
                try {
                    imageReader.dispose();
                } catch (Exception e) {
                    // Ignore exception on dispose
                }
            }
        }
    }

    @Test
    public void testReHarvest() throws Exception {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this,"polyphemus_20130301_test.nc");
        File mosaic = new File(TestData.file(this,"."),"nc_harvest4");
        if(mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);
        
        // The indexer
        String indexer = "TimeAttribute=time\n" + 
                "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);
        
        // the datastore.properties file is also mandatory...
        File dsp =TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);
        
        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals("O3", names[0]);
            
            // check we have the two granules we expect
            GranuleSource source = reader.getGranules("O3", true);
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            Query q = new Query(Query.ALL);
            q.setSortBy(new SortBy[] {ff.sort("time", SortOrder.ASCENDING)});
            SimpleFeatureCollection granules = source.getGranules(q);
            assertEquals(2, granules.size());
            it = granules.features();
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T01:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();
            
            // close the reader and re-open it
            reader.dispose();
            reader = format.getReader(mosaic);
            source = reader.getGranules("O3", true);
            
            // wait a bit, we have to make sure the old indexes are recognized as old
            Thread.sleep(1000);
            
            // now replace the netcdf file with a more up to date version of the same 
            File nc2 = TestData.file(this,"polyphemus_20130301_test_more_times.nc");
            File target = new File(mosaic, "polyphemus_20130301_test.nc");
            FileUtils.copyFile(nc2, target, false);
            File fileToHarvest = new File(mosaic, "polyphemus_20130301_test.nc");
            List<HarvestedSource> harvestSummary = reader.harvest(null, fileToHarvest, null);
            assertEquals(1, harvestSummary.size());
            HarvestedSource hf = harvestSummary.get(0);
            assertEquals("polyphemus_20130301_test.nc", ((File) hf.getSource()).getName());
            assertTrue(hf.success());
            assertEquals(1, reader.getGridCoverageNames().length);
            
            // check that we have four times now
            source = reader.getGranules("O3", true);
            granules = source.getGranules(q);
            assertEquals(4, granules.size());
            it = granules.features();
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T01:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(2, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T02:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(3, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T03:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();
        } finally {
            if(it != null) {
                it.close();
            }
            reader.dispose();
        }
    }

    @Test
    public void testHarvestHDF5Data() throws IOException {
        File nc1 = TestData.file(this,"2DLatLonCoverage.nc");
        File nc2 = TestData.file(this,"2DLatLonCoverage2.nc");
        File mosaic = new File(TestData.file(this,"."),"simpleMosaic");
        if(mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);
        FileUtils.copyFileToDirectory(nc2, mosaic);

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        File xml =  TestData.file(this,"hdf5Coverage2D.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer = "TimeAttribute=time\n"
                + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
              //  + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "hdf5Coverage2D.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);

        //simply test if the mosaic can be read without exceptions
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        reader.read("L1_V2",null);
    }
    
    @Test
    public void testHarvestAddVariable() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this,"polyphemus_20130301_test.nc");
        File mosaic = new File(TestData.file(this,"."),"nc_harvest2");
        if(mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);
        
        // The indexer
        String indexer = "TimeAttribute=time\n" + 
                "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);
        
        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);
        
        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals("O3", names[0]);
            
            // check we have the two granules we expect
            GranuleSource source = reader.getGranules("O3", true);
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            Query q = new Query(Query.ALL);
            q.setSortBy(new SortBy[] {ff.sort("time", SortOrder.ASCENDING)});
            SimpleFeatureCollection granules = source.getGranules(q);
            assertEquals(2, granules.size());
            it = granules.features();
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T01:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();
            
            // now add another netcdf and harvest it
            File nc2 = TestData.file(this,"polyphemus_20130301_NO2.nc");
            FileUtils.copyFileToDirectory(nc2, mosaic);
            File fileToHarvest = new File(mosaic, "polyphemus_20130301_NO2.nc");
            List<HarvestedSource> harvestSummary = reader.harvest(null, fileToHarvest, null);
            assertEquals(1, harvestSummary.size());
            HarvestedSource hf = harvestSummary.get(0);
            assertEquals("polyphemus_20130301_NO2.nc", ((File) hf.getSource()).getName());
            assertTrue(hf.success());
            // check we have two coverages now
            names = reader.getGridCoverageNames();
            Arrays.sort(names);
            assertEquals(2, names.length);
            assertEquals("NO2", names[0]);
            assertEquals("O3", names[1]);
            
            // test the newly ingested granules, which are in a separate coverage
            q.setTypeName("NO2");
            granules = source.getGranules(q);
            assertEquals(2, granules.size());
            it = granules.features();
            f = it.next();
            assertEquals("polyphemus_20130301_NO2.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T00:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_NO2.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals("2013-03-01T01:00:00.000Z", ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();
        } finally {
            if(it != null) {
                it.close();
            }
            reader.dispose();
        }
    }

    @Test
    public void testMultipleGranules() throws IOException, ParseException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY_new.nc");
        File nc2 = TestData.file(this, "20130108.METOPA.GOME2.NO2.DUMMY_new.nc");
        File mosaic = new File(TestData.file(this, "."), "nc_heterogen");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);
        FileUtils.copyFileToDirectory(nc2, mosaic);

        File xml = TestData.file(this, "GOME2.NO2_new.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer = "TimeAttribute=time\n"
                + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "GOME2.NO2_new.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex);

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        try {
            // use imageio with defined tiles
            final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD
                    .createValue();
            useJai.setValue(false);
            // specify time
            ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
            final Date timeD = parseTimeStamp("2013-01-01T00:00:00.000");
            time.setValue(new ArrayList() {
                {
                    add(timeD);
                }
            });
            GeneralParameterValue[] params = new GeneralParameterValue[] { useJai, time };
            GridCoverage2D coverage1 = reader.read(params);
            // Specify a new time (Check if two times returns two different coverages)
            final Date timeD2 = parseTimeStamp("2013-01-08T00:00:00.000");
            time.setValue(new ArrayList() {
                {
                    add(timeD2);
                }
            });
            params = new GeneralParameterValue[] { useJai, time };
            GridCoverage2D coverage2 = reader.read(params);

            // Ensure that the two images are different (different location)
            String property = (String) coverage1.getProperty("OriginalFileSource");
            String property2 = (String) coverage2.getProperty("OriginalFileSource");
            assertNotEquals(property, property2);

            // Ensure that only one coverage is present
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals("NO2", names[0]);
        } finally {
            if (it != null) {
                it.close();
            }
            reader.dispose();
        }
    }

    @Test
    public void testHarvest3Gome() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 =  TestData.file(this,"20130101.METOPA.GOME2.NO2.DUMMY.nc");
        File mosaic = new File(TestData.file(this,"."),"nc_harvest");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);

        File xml =  TestData.file(this,".DUMMY.GOME2.NO2.PGL/GOME2.NO2.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer = "TimeAttribute=time\n"
                + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "GOME2.NO2.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex);

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals("NO2", names[0]);

            // check we have the two granules we expect
            GranuleSource source = reader.getGranules("NO2", true);
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            Query q = new Query(Query.ALL);
            q.setSortBy(new SortBy[] { ff.sort("time", SortOrder.DESCENDING) });
            SimpleFeatureCollection granules = source.getGranules(q);
            assertEquals(1, granules.size());
            it = granules.features();
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            assertEquals("20130101.METOPA.GOME2.NO2.DUMMY.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-01-01T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();

            // now add another netcdf and harvest it
            File nc2 =  TestData.file(this,"20130116.METOPA.GOME2.NO2.DUMMY.nc");
            FileUtils.copyFileToDirectory(nc2, mosaic);
            File fileToHarvest = new File(mosaic, "20130116.METOPA.GOME2.NO2.DUMMY.nc");
            List<HarvestedSource> harvestSummary = reader.harvest("NO2", fileToHarvest, null);
            assertEquals(1, harvestSummary.size());
            granules = source.getGranules(q);
            assertEquals(2, granules.size());
            
            HarvestedSource hf = harvestSummary.get(0);
            assertEquals("20130116.METOPA.GOME2.NO2.DUMMY.nc", ((File) hf.getSource()).getName());
            assertTrue(hf.success());
            assertEquals(1, reader.getGridCoverageNames().length);

            File nc3 =  TestData.file(this,"20130108.METOPA.GOME2.NO2.DUMMY.nc");
            FileUtils.copyFileToDirectory(nc3, mosaic);
            fileToHarvest = new File(mosaic, "20130108.METOPA.GOME2.NO2.DUMMY.nc");
            harvestSummary = reader.harvest("NO2", fileToHarvest, null);
            assertEquals(1, harvestSummary.size());
            hf = harvestSummary.get(0);
            assertEquals("20130108.METOPA.GOME2.NO2.DUMMY.nc", ((File) hf.getSource()).getName());
            assertTrue(hf.success());
            assertEquals(1, reader.getGridCoverageNames().length);

            // check that we have 2 times now
            granules = source.getGranules(q);
            assertEquals(3, granules.size());
            it = granules.features();
            f = it.next();
            assertEquals("20130116.METOPA.GOME2.NO2.DUMMY.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-01-16T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("20130108.METOPA.GOME2.NO2.DUMMY.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-01-08T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            f = it.next();
            assertEquals("20130101.METOPA.GOME2.NO2.DUMMY.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals("2013-01-01T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));

            it.close();
        } finally {
            if (it != null) {
                it.close();
            }
            reader.dispose();
        }
    }
    
    @Test
    public void testReadCoverageGome() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this,"20130101.METOPA.GOME2.NO2.DUMMY.nc");
        File mosaic = new File(TestData.file(this,"."),"nc_harvest3");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);

        File xml = TestData.file(this,".DUMMY.GOME2.NO2.PGL/GOME2.NO2.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer = "TimeAttribute=time\n"
                + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "GOME2.NO2.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex);

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        GridCoverage2D coverage = null;
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals("NO2", names[0]);
            
            GranuleSource source = reader.getGranules("NO2", true);
            SimpleFeatureCollection granules = source.getGranules(Query.ALL);
            assertEquals(1, granules.size());
            
            assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, reader.getCoordinateReferenceSystem()));
            GeneralEnvelope envelope = reader.getOriginalEnvelope("NO2");
            assertEquals(-360, envelope.getMinimum(0), 0d);
            assertEquals(360, envelope.getMaximum(0), 0d);
            assertEquals(-180, envelope.getMinimum(1), 0d);
            assertEquals(180, envelope.getMaximum(1), 0d);

            // check we can read a coverage out of it
            coverage = reader.read(null);
            reader.dispose();

            // Checking we can read again from the coverage once it has been configured.
            reader = format.getReader(mosaic);
            coverage = reader.read(null);
            assertNotNull(coverage);
            
        } finally {
            if(coverage != null) {
                ImageUtilities.disposePlanarImageChain((PlanarImage) coverage.getRenderedImage());
                coverage.dispose(true);
            }
            reader.dispose();
        }
    }

    @Test
    public void testAuxiliaryFileHasRelativePath() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY.nc");
        File mosaic = new File(TestData.file(this, "."), "nc_harvestRP");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);

        final String auxFileName = "GOME2.NO2.xml";
        File xml = TestData.file(this, ".DUMMY.GOME2.NO2.PGL/" + auxFileName);
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer = "TimeAttribute=time\n"
                + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "GOME2.NO2.xml\n";

        // Setting RelativePath behavior
        indexer += Prop.ABSOLUTE_PATH + "=" + "false";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex);

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        InputStream inStream = null;
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals("NO2", names[0]);

            GranuleSource source = reader.getGranules("NO2", true);
            SimpleFeatureCollection granules = source.getGranules(Query.ALL);
            assertEquals(1, granules.size());
            Properties props = new Properties();
            final File file = new File(mosaic, "nc_harvestRP.properties");
            inStream = new FileInputStream(file);
            props.load(inStream);
            // Before the fix, the AuxiliaryFile was always an absolute path
            assertEquals(auxFileName, (String) props.getProperty(Prop.AUXILIARY_FILE));
        } finally {
            if (inStream != null) {
                IOUtils.closeQuietly(inStream);
            }
            if (reader != null) {
                try {

                    reader.dispose();
                } catch (Throwable t) {
                    // Ignore exception on close attempt
                }
            }
        }
    }

    
    @Test
    public void testDeleteCoverageGome() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this,"O3-NO2.nc");
        File mosaic = new File(TestData.file(this,"."),"nc_deleteCoverage");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);

        File xml = TestData.file(this,".O3-NO2/O3-NO2.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer = "TimeAttribute=time\n"
                + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "O3-NO2.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);


        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        GridCoverage2D coverage = null;
        assertNotNull(reader);
        try {
            assertEquals(2, reader.getGridCoverageNames().length);

            File[] files = mosaic.listFiles();
            assertEquals(15, files.length);
            
            reader.dispose();
            reader = format.getReader(mosaic);
            
            reader.delete(false);
            files = mosaic.listFiles();
            assertEquals(1, files.length);
            
        } finally {
            if(coverage != null) {
                ImageUtilities.disposePlanarImageChain((PlanarImage) coverage.getRenderedImage());
                coverage.dispose(true);
            }
            reader.dispose();
        }
    }

    @Test
    public void testReadCoverageGome2Names() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 =  TestData.file(this,"20130101.METOPA.GOME2.NO2.DUMMY.nc");
        File mosaic = new File(TestData.file(this,"."),"nc_gome2");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);
        
        nc1 =  TestData.file(this,"20130101.METOPA.GOME2.BrO.DUMMY.nc");
        FileUtils.copyFileToDirectory(nc1, mosaic);
        
        File xml =  TestData.file(this,"DUMMYGOME2.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer = "TimeAttribute=time\n"
                + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "DUMMYGOME2.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex);

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        GridCoverage2D coverage = null;
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertEquals(2, names.length);
            assertEquals("NO2", names[0]);
            assertEquals("BrO", names[1]);
            
            GranuleSource source = reader.getGranules("NO2", true);
            SimpleFeatureCollection granules = source.getGranules(Query.ALL);
            assertEquals(1, granules.size());
            
            assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, reader.getCoordinateReferenceSystem("NO2")));
            GeneralEnvelope envelope = reader.getOriginalEnvelope("NO2");
            assertEquals(-360, envelope.getMinimum(0), 0d);
            assertEquals(360, envelope.getMaximum(0), 0d);
            assertEquals(-180, envelope.getMinimum(1), 0d);
            assertEquals(180, envelope.getMaximum(1), 0d);

            // check we can read a coverage out of it
            coverage = reader.read("NO2", null);
            reader.dispose();

            // Checking we can read again from the coverage (using a different name this time) once it has been configured.
            reader = format.getReader(mosaic);
            coverage = reader.read("BrO", null);
            assertNotNull(coverage);
            
        } finally {
            if(coverage != null) {
                ImageUtilities.disposePlanarImageChain((PlanarImage) coverage.getRenderedImage());
                coverage.dispose(true);
            }
            reader.dispose();
        }
    }
    
    @Test
    public void testCheckDifferentSampleImages() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this,"20130101.METOPA.GOME2.NO2.DUMMY.nc");
        File mosaic =new File(TestData.file(this,"."),"nc_sampleimages");
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        assertTrue(mosaic.mkdirs());
        FileUtils.copyFileToDirectory(nc1, mosaic);
        
        nc1 =  TestData.file(this,"20130101.METOPA.GOME2.BrO.DUMMY.nc");
        FileUtils.copyFileToDirectory(nc1, mosaic);
        
        File xml =  TestData.file(this,"DUMMYGOME2.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer = "TimeAttribute=time\n"
                + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "DUMMYGOME2.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer);

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex);

        // the datastore.properties file is also mandatory...
        File dsp =  TestData.file(this,"datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        assertNotNull(reader);
        
        // Checking whether different sample images have been created
        final File sampleImage1 = new File(TestData.file(this,"."),"nc_sampleimages/BrOsample_image.dat");
        final File sampleImage2 = new File(TestData.file(this,"."),"nc_sampleimages/NO2sample_image.dat");
        assertTrue(sampleImage1.exists());
        assertTrue(sampleImage2.exists());
        reader.dispose();
    }

    @Test
    @Ignore
        public void oracle() throws IOException, ParseException, NoSuchAuthorityCodeException, FactoryException {
                final File workDir=new File("C:\\data\\dlr\\ascatL1_mosaic");
                
            
                final AbstractGridFormat format = new ImageMosaicFormat();
                assertNotNull(format);
                ImageMosaicReader reader = (ImageMosaicReader) format.getReader(workDir.toURI().toURL());
                assertNotNull(format);
                String[] names = reader.getGridCoverageNames();
                String name = names[1];

                
                final String[] metadataNames = reader.getMetadataNames(name);
                assertNotNull(metadataNames);
                assertEquals(metadataNames.length, 18);
                
                assertEquals("false", reader.getMetadataValue(name, "HAS_TIME_DOMAIN"));
                
                assertEquals("true", reader.getMetadataValue(name, "HAS_NUMSIGMA_DOMAIN"));
                assertEquals("0,1,2",reader.getMetadataValue(name, "NUMSIGMA_DOMAIN"));
                assertEquals("java.lang.Integer", reader.getMetadataValue(name, "NUMSIGMA_DOMAIN_DATATYPE"));

                assertEquals("true", reader.getMetadataValue(name, "HAS_RUNTIME_DOMAIN"));
                assertEquals("false", reader.getMetadataValue(name, "HAS_ELEVATION_DOMAIN"));
                assertEquals("false", reader.getMetadataValue(name, "HAS_XX_DOMAIN"));
                assertEquals("20110620020000", reader.getMetadataValue(name, "RUNTIME_DOMAIN"));
                assertEquals("java.lang.String", reader.getMetadataValue(name, "RUNTIME_DOMAIN_DATATYPE"));

                
                // limit yourself to reading just a bit of it
                final ParameterValue<GridGeometry2D> gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
                final GeneralEnvelope envelope = reader.getOriginalEnvelope(name);
                final Dimension dim= new Dimension();
                dim.setSize(reader.getOriginalGridRange(name).getSpan(0)/2.0, reader.getOriginalGridRange(name).getSpan(1)/2.0);
                final Rectangle rasterArea=(( GridEnvelope2D)reader.getOriginalGridRange(name));
                rasterArea.setSize(dim);
                final GridEnvelope2D range= new GridEnvelope2D(rasterArea);
                gg.setValue(new GridGeometry2D(range,envelope));
                
                
                final ParameterValue<Boolean> direct= ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
                direct.setValue(false);
                
                final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
                bkg.setValue(new double[]{-9999.0});
                
                ParameterValue<List<String>> dateValue = null;
                ParameterValue<List<String>> sigmaValue = null;
                final String selectedSigma = "1";
                final String selectedRuntime = "20110620020000";
            Set<ParameterDescriptor<List>> params = reader.getDynamicParameters(name);
                for (ParameterDescriptor param : params) {
                    if (param.getName().getCode().equalsIgnoreCase("RUNTIME")) {
                        dateValue = param.createValue();
                        dateValue.setValue(new ArrayList<String>() {
                            {
                                add(selectedRuntime);
                            }
                        });
                    } else if (param.getName().getCode().equalsIgnoreCase("NUMSIGMA")) {
                        sigmaValue = param.createValue();
                        sigmaValue.setValue(new ArrayList<String>() {
                            {
                                add(selectedSigma);
                            }
                        });
                    }
                }
                // Test the output coverage
                GridCoverage2D coverage = reader.read(name, new GeneralParameterValue[] {gg, bkg, direct, sigmaValue, dateValue});
                assertNotNull(coverage);
                
                
        }

    /**
     * Test that expected data values can be read from an ImageMosaic of multi-coverage NetCDF files.
     * 
     * @throws Exception
     */
    @Test
    public void testMultiCoverage() throws Exception {
        File testDir = new File("target", "multi-coverage");
        URL testUrl = DataUtilities.fileToURL(testDir);
        if (testDir.exists()) {
            FileUtils.deleteDirectory(testDir);
        }
        FileUtils.copyDirectory(TestData.file(this, "multi-coverage"), testDir);
        ImageMosaicReader reader = null;
        try {
            reader = new ImageMosaicReader(testUrl);
            assertNotNull(reader);
            checkMultiCoverage(reader, "air_temperature", -85, 26, "2017-02-06T00:00:00.000", 295);
            checkMultiCoverage(reader, "sea_surface_temperature", -85, 26, "2017-02-06T00:00:00.000", 296);
            checkMultiCoverage(reader, "air_temperature", -85, 26, "2017-02-06T12:00:00.000", 296);
            checkMultiCoverage(reader, "sea_surface_temperature", -85, 26, "2017-02-06T12:00:00.000", 295);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    /**
     * Check that reading a single data value from an ImageMosaic of multi-coverage NetCDF files yields the expected value.
     * 
     * @param reader
     * @param coverageName
     * @param longitude
     * @param latitude
     * @param timestamp
     * @param expected
     * @throws Exception
     */
    private void checkMultiCoverage(ImageMosaicReader reader, String coverageName, double longitude,
            double latitude, String timestamp, double expected) throws Exception {
        ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);
        @SuppressWarnings("rawtypes")
        ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        time.setValue(Arrays.asList(new Date[] { parseTimeStamp(timestamp) }));
        GeneralParameterValue[] params = new GeneralParameterValue[] { useJai, time };
        GridCoverage2D coverage = reader.read(coverageName, params);
        assertNotNull(coverage);
        // delta is zero because an exact match is expected
        assertEquals(expected,
                coverage.evaluate(new Point2D.Double(longitude, latitude), (double[]) null)[0], 0);
    }

    private Date parseTimeStamp(String timeStamp) throws ParseException {
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        formatD.setTimeZone(TimeZone.getTimeZone("Zulu"));
        return formatD.parse(timeStamp);
    }

    /**
     * Shows the provided {@link RenderedImage} ina {@link JFrame} using the provided <code>title</code> as the frame's title.
     * 
     * @param image to show.
     * @param title to use.
     */
    static void show(RenderedImage image, String title) {
        ImageIOUtilities.visualize(image, title);

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TestRunner.run(NetCDFMosaicReaderTest.suite());

    }

    @Before
    public void init() {

        // make sure CRS ordering is correct
        System.setProperty("org.geotools.referencing.forceXY", "true");
        System.setProperty("user.timezone", "GMT");
        System.setProperty("org.geotools.shapefile.datetime", "true");
        CRS.reset("all");

    }

    @AfterClass
    public static void close() {
        System.clearProperty("org.geotools.referencing.forceXY");
        System.clearProperty("user.timezone");
        System.clearProperty("org.geotools.shapefile.datetime");
        CRS.reset("all");
    }

    /**
     * returns an {@link AbstractGridCoverage2DReader} for the provided {@link URL} and for the providede {@link AbstractGridFormat}.
     * 
     * @param testURL points to a valid object to create an {@link AbstractGridCoverage2DReader} for.
     * @param format to use for instantiating such a reader.
     * @return a suitable {@link ImageMosaicReader}.
     * @throws FactoryException
     * @throws NoSuchAuthorityCodeException
     */
    static ImageMosaicReader getReader(URL testURL, final AbstractGridFormat format)
            throws NoSuchAuthorityCodeException, FactoryException {

        // final Hints hints= new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        return getReader(testURL, format, null);

    }

    static ImageMosaicReader getReader(URL testURL, final AbstractGridFormat format, Hints hints) {
        // Get a reader
        final ImageMosaicReader reader = (ImageMosaicReader) format.getReader(testURL, hints);
        Assert.assertNotNull(reader);
        return reader;
    }

}
