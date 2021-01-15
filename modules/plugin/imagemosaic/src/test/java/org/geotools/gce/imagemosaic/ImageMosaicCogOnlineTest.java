/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.HarvestedSource;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.properties.FSDateExtractorSPI;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.TestData;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

/** Testing using COG remote granules on an ImageMosaic */
public class ImageMosaicCogOnlineTest {

    @BeforeClass
    public static void init() {
        System.setProperty("user.timezone", "GMT");
    }

    @AfterClass
    public static void close() {
        System.clearProperty("user.timezone");
    }

    private static final ImageMosaicFormat IMAGE_MOSAIC_FORMAT = new ImageMosaicFormat();

    @Test
    public void testCogMosaic() throws Exception {
        File workDir = prepareWorkingDir("cogtest.zip", "cogtest", "");
        ImageMosaicReader reader = IMAGE_MOSAIC_FORMAT.getReader(workDir);
        GridCoverage2D coverage = reader.read(null);
        Assert.assertNotNull(coverage);
        RenderedImage image = coverage.getRenderedImage();
        int numTileX = image.getNumXTiles();
        int numTileY = image.getNumYTiles();
        Raster raster = image.getTile(numTileX / 2, numTileY / 2);
        Assert.assertEquals(512, raster.getWidth());
        Assert.assertEquals(512, raster.getHeight());
        Assert.assertEquals(1, raster.getNumBands());
        reader.dispose();
    }

    @Test
    public void testCogMosaicOverview() throws Exception {
        File workDir = prepareWorkingDir("cogtest.zip", "overview", "cogtest");
        ImageMosaicReader reader = IMAGE_MOSAIC_FORMAT.getReader(workDir);

        GeneralParameterValue[] params = new GeneralParameterValue[1];
        // Define a GridGeometry in order to reduce the output
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 24,
                reader.getOriginalGridRange().getSpan(1) / 24);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));
        params[0] = gg;

        GridCoverage2D coverage = reader.read(params);
        RenderedImage image = coverage.getRenderedImage();
        Assert.assertNotNull(coverage);
        int numTileX = image.getNumXTiles();
        int numTileY = image.getNumYTiles();
        Raster raster = image.getTile(numTileX / 2, numTileY / 2);
        Assert.assertEquals(512, raster.getWidth());
        Assert.assertEquals(512, raster.getHeight());
        Assert.assertEquals(1, raster.getNumBands());
        Object fileLocation =
                coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        Assert.assertNotNull(fileLocation);
        Assert.assertTrue(fileLocation instanceof String);
        String path = (String) fileLocation;
        Assert.assertFalse(path.isEmpty());
        Assert.assertTrue(path.endsWith(".ovr"));
        reader.dispose();
    }

    @Test
    public void testCogMosaicDefaultConfig() throws Exception {
        File workDir = prepareWorkingDir("cogtest.zip", "default", "cogtest");
        File file = new File(workDir, "cogtest.properties");
        Properties properties = new Properties();
        try (FileInputStream fin = new FileInputStream(file)) {
            properties.load(fin);
        }

        try (FileWriter fw = new FileWriter(file)) {
            Assert.assertNotNull(properties.remove("CogRangeReader"));
            Assert.assertNotNull(properties.remove("SuggestedSPI"));
            properties.store(fw, "");
        }

        ImageMosaicReader reader = IMAGE_MOSAIC_FORMAT.getReader(workDir);
        GridCoverage2D coverage = reader.read(null);
        Assert.assertNotNull(coverage);
        RenderedImage image = coverage.getRenderedImage();
        int numTileX = image.getNumXTiles();
        int numTileY = image.getNumYTiles();
        Raster raster = image.getTile(numTileX / 2, numTileY / 2);
        Assert.assertEquals(512, raster.getWidth());
        Assert.assertEquals(512, raster.getHeight());
        Assert.assertEquals(1, raster.getNumBands());
        reader.dispose();
    }

    @Test
    public void testHarvestSingleURL() throws Exception {
        File workDir = prepareWorkingDir("cogtest.zip", "harvest", "cogtest");
        File file = new File(workDir, "indexer.properties");
        Properties properties = new Properties();
        try (FileInputStream fin = new FileInputStream(file)) {
            properties.load(fin);
        }

        try (FileWriter fw = new FileWriter(file)) {
            Assert.assertNotNull(properties.remove("UseExistingSchema"));
            properties.store(fw, "");
        }

        ImageMosaicReader reader = IMAGE_MOSAIC_FORMAT.getReader(workDir);
        String coverageName = reader.getGridCoverageNames()[0];
        GranuleSource granules = reader.getGranules(coverageName, true);

        // Only 1 granule available before doing the harvest
        Assert.assertEquals(1, granules.getCount(Query.ALL));

        try {
            // now go and harvest the url
            URL source =
                    new URL(
                            "https://s3-us-west-2.amazonaws.com/landsat-pds/c1/L8/153/075/LC08_L1TP_153075_20190515_20190515_01_RT/LC08_L1TP_153075_20190515_20190515_01_RT_B3.TIF");
            List<HarvestedSource> summary = reader.harvest(null, source, null);
            HarvestedSource hf = summary.get(0);

            // check the granule catalog
            granules = reader.getGranules(coverageName, true);

            // We now have 2 granules
            Assert.assertEquals(2, granules.getCount(Query.ALL));
        } finally {
            reader.dispose();
        }
    }

    /** Simple test method to test emptyMosaic creation support followed by harvesting a URL */
    @Test
    public void testEmptyMosaic() throws Exception {
        final File workDir = prepareWorkingDir("emptycog.zip", "emptyCogMosaic", "");
        try (FileWriter out =
                new FileWriter(
                        new File(
                                TestData.file(this, "."),
                                "/emptyCogMosaic/datastore.properties"))) {
            out.write("database=cogmosaic\n");
            out.write(ImageMosaicReaderTest.H2_SAMPLE_PROPERTIES);
            out.flush();
        }
        ImageMosaicReader reader = IMAGE_MOSAIC_FORMAT.getReader(workDir);
        GranuleCatalog originalCatalog = reader.granuleCatalog;

        try {
            // now go and harvest a granule
            String granuleUrl =
                    "https://s3-us-west-2.amazonaws.com/landsat-pds/c1/L8/153/075/LC08_L1TP_153075_20190515_20190515_01_RT/LC08_L1TP_153075_20190515_20190515_01_RT_B3.TIF";
            URL source = new URL(granuleUrl);
            List<HarvestedSource> summary = reader.harvest(null, source, null);
            Assert.assertSame(originalCatalog, reader.granuleCatalog);
            Assert.assertEquals(1, summary.size());

            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            Assert.assertEquals(1, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            try (SimpleFeatureIterator fi = granules.getGranules(q).features()) {
                Assert.assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();
                Assert.assertEquals(granuleUrl, f.getAttribute("location"));
            }

        } finally {
            reader.dispose();
        }
    }

    /** Checking time get extracted from remote URL too. */
    @Test
    public void testTimeDimensionMosaic() throws Exception {

        final File workDir = prepareWorkingDir("emptycog.zip", "timeMosaic", "");
        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), "/timeMosaic/datastore.properties"))) {
            out.write("database=cogmosaic\n");
            out.write(ImageMosaicReaderTest.H2_SAMPLE_PROPERTIES);
            out.flush();
        }
        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), "/timeMosaic/timeregex.properties"))) {
            out.write("regex=[0-9]{8}");
            out.flush();
        }
        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), "/timeMosaic/indexer.properties"),
                        true)) {
            out.write("\nPropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)");
            out.write("\nTimeAttribute=time");
            out.write("\nSchema=location:String,time:java.util.Date,*the_geom:Polygon\n");
            out.flush();
        }

        final AbstractGridFormat IMAGE_MOSAIC_FORMAT = new ImageMosaicFormat();
        ImageMosaicReader reader = (ImageMosaicReader) IMAGE_MOSAIC_FORMAT.getReader(workDir);
        GranuleCatalog originalCatalog = reader.granuleCatalog;

        try {
            // now go and harvest 2 granules
            List<URL> urls = new LinkedList<>();
            urls.add(
                    new URL(
                            "https://s3-us-west-2.amazonaws.com/landsat-pds/c1/L8/153/075/LC08_L1TP_153075_20190429_20190429_01_RT/LC08_L1TP_153075_20190429_20190429_01_RT_B1.TIF"));
            urls.add(
                    new URL(
                            "https://s3-us-west-2.amazonaws.com/landsat-pds/c1/L8/153/075/LC08_L1TP_153075_20190515_20190515_01_RT/LC08_L1TP_153075_20190515_20190515_01_RT_B3.TIF"));
            List<HarvestedSource> summary = reader.harvest(null, urls, null);
            Assert.assertSame(originalCatalog, reader.granuleCatalog);
            Assert.assertEquals(2, summary.size());

            Assert.assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            Assert.assertEquals(
                    "2019-04-29T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
            Assert.assertEquals(
                    "2019-05-15T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        } finally {
            reader.dispose();
        }
    }

    /** Checking time get extracted from remote URL too. */
    @Test
    public void testFSDateCollect() throws Exception {
        URL url =
                new URL(
                        "https://s3-us-west-2.amazonaws.com/landsat-pds/c1/L8/153/075/LC08_L1TP_153075_20190429_20190429_01_RT/LC08_L1TP_153075_20190429_20190429_01_RT_B1.TIF");
        final FSDateExtractorSPI spi = new FSDateExtractorSPI();
        final PropertiesCollector collector = spi.create(spi, Arrays.asList("createdate"));
        final SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
        featureTypeBuilder.setName("runtimeT");
        featureTypeBuilder.add("createdate", Date.class);
        SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        SimpleFeature feature = featureBuilder.buildFeature("0");
        collector.collect(url);
        collector.setProperties(feature);
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        Date date = (Date) feature.getAttribute("createdate");
        calendar.setTime(date);
        Assert.assertEquals(2019, calendar.get(Calendar.YEAR));
        Assert.assertEquals(29, calendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(4, calendar.get(Calendar.MONTH) + 1);
    }

    private File prepareWorkingDir(String zipName, String folder, String subFolder)
            throws IOException {
        File workDir = new File(TestData.file(this, "."), folder);
        String destinationPath = folder + "/";
        if (StringUtils.isNotBlank(subFolder)) {
            workDir = new File(workDir, subFolder);
            destinationPath += (subFolder + "/");
        }
        destinationPath += zipName;
        if (!workDir.mkdirs()) {
            FileUtils.deleteDirectory(workDir);
            Assert.assertTrue("Unable to create workdir:" + workDir, workDir.mkdirs());
        }
        File zipFile = new File(workDir, zipName);
        FileUtils.copyFile(TestData.file(this, zipName), zipFile);
        TestData.unzipFile(this, destinationPath);
        FileUtils.deleteQuietly(zipFile);
        return workDir;
    }
}
