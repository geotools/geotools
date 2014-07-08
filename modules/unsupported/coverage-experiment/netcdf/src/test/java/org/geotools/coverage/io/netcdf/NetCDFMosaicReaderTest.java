/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2014, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.utilities.ImageIOUtilities;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;
import javax.swing.JFrame;

import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GridFormatFinder;
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
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

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

    private static boolean INTERACTIVE;

    /**
     * Simple test method accessing time and 2 custom dimensions for the sample dataset
     * 
     * @throws IOException
     * @throws FactoryException
     * @throws NoSuchAuthorityCodeException
     * @throws ParseException +
     */
    @Test
    @Ignore
    @SuppressWarnings("rawtypes")
    public void timeAdditionalDimRanges() throws Exception {
        final String dlrFolder = "/work/data/DLR/samplesForMosaic";
        final File file = new File(dlrFolder);
        final URL url = DataUtilities.fileToURL(file);
        final Hints hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));
        
        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(url, hints);
        final ImageMosaicReader reader = getReader(url, format);
        String[] names = reader.getGridCoverageNames();
        
        for (String name: names) {
            LOGGER.info("Coverage: " + name);
            final String[] metadataNames = reader.getMetadataNames(name);
            assertNotNull(metadataNames);
            assertEquals(metadataNames.length, 12);
            assertEquals("true", reader.getMetadataValue(name, "HAS_TIME_DOMAIN"));
            assertEquals(
                    "2012-04-01T00:00:00.000Z,2012-04-01T01:00:00.000Z,2012-04-01T02:00:00.000Z,2012-04-01T03:00:00.000Z,2012-04-01T04:00:00.000Z,2012-04-01T05:00:00.000Z,2012-04-01T06:00:00.000Z,2012-04-01T07:00:00.000Z,2012-04-01T08:00:00.000Z,2012-04-01T09:00:00.000Z,2012-04-01T10:00:00.000Z,2012-04-01T11:00:00.000Z,2012-04-01T12:00:00.000Z,2012-04-01T13:00:00.000Z,2012-04-01T14:00:00.000Z,2012-04-01T15:00:00.000Z,2012-04-01T16:00:00.000Z,2012-04-01T17:00:00.000Z,2012-04-01T18:00:00.000Z,2012-04-01T19:00:00.000Z,2012-04-01T20:00:00.000Z,2012-04-01T21:00:00.000Z,2012-04-01T22:00:00.000Z,2012-04-01T23:00:00.000Z",
                    reader.getMetadataValue(name, "TIME_DOMAIN"));
            assertEquals("2012-04-01T00:00:00.000Z", reader.getMetadataValue(name, "TIME_DOMAIN_MINIMUM"));
            assertEquals("2012-04-01T23:00:00.000Z", reader.getMetadataValue(name, "TIME_DOMAIN_MAXIMUM"));
        
            assertEquals("true", reader.getMetadataValue(name, "HAS_ELEVATION_DOMAIN"));
            assertEquals("10.0,35.0,75.0,125.0,175.0,250.0,350.0,450.0,550.0,700.0,900.0,1250.0,1750.0,2500.0", reader.getMetadataValue(name, "ELEVATION_DOMAIN"));
            assertEquals("10.0", reader.getMetadataValue(name, "ELEVATION_DOMAIN_MINIMUM"));
            assertEquals("2500.0", reader.getMetadataValue(name, "ELEVATION_DOMAIN_MAXIMUM"));
            
            assertEquals("true", reader.getMetadataValue(name, "HAS_RUNTIME_DOMAIN"));
            assertEquals("2012-05-09T12:29:30.000Z,2013-03-30T16:15:58.648Z", reader.getMetadataValue(name, "RUNTIME_DOMAIN"));
            assertEquals("2012-05-09T12:29:30.000Z", reader.getMetadataValue(name, "RUNTIME_DOMAIN_MINIMUM"));
            assertEquals("2013-03-30T16:15:58.648Z", reader.getMetadataValue(name, "RUNTIME_DOMAIN_MAXIMUM"));
        
            // use imageio with defined tiles
            final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
            useJai.setValue(false);
        
            // specify time
            final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
            final Date timeD = parseTimeStamp("2012-04-01T00:00:00.000Z");
            time.setValue(new ArrayList() {
                {
                    add(timeD);
                }
            });
        
            final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
            elevation.setValue(new ArrayList() {
                {
                    add(75d); // Elevation
                }
            });
        
            Set<ParameterDescriptor<List>> params = reader.getDynamicParameters(name);
            ParameterValue<List<String>> runtime = null;
            final String selectedWaveLength = "2013-03-30T16:15:58.648Z";
            for (ParameterDescriptor param : params) {
                if (param.getName().getCode().equalsIgnoreCase("RUNTIME")) {
                    runtime = param.createValue();
                    runtime.setValue(new ArrayList<String>() {
                        {
                            add(selectedWaveLength);
                        }
                    });
                }
            }
            assertNotNull(runtime);
            
            // Test the output coverage
            GeneralParameterValue[] values = new GeneralParameterValue[] { useJai, time, elevation, runtime};
            final GridCoverage2D coverage = (GridCoverage2D) reader.read(name, values);
            Assert.assertNotNull(coverage);
            final String fileSource = (String) coverage
                    .getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        
            // Check the proper granule has been read
            final String baseName = FilenameUtils.getBaseName(fileSource);
            assertEquals(baseName, "20130102polyphemus");
        }
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
        } finally {
            if(it != null) {
                it.close();
            }
            reader.dispose();
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
        final File sampleImage1 = new File(TestData.file(this,"."),"nc_sampleimages/BrOsample_image");
        final File sampleImage2 = new File(TestData.file(this,"."),"nc_sampleimages/NO2sample_image");
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
    
    private Date parseTimeStamp(String timeStamp) throws ParseException {
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
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

        INTERACTIVE = TestData.isInteractiveTest();
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
