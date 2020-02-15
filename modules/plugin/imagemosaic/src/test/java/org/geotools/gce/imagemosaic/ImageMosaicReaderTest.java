/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.gce.imagemosaic.TestUtils.getReader;
import static org.geotools.gce.imagemosaic.TestUtils.setupTestDirectory;
import static org.geotools.util.URLs.fileToUrl;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand;
import it.geosolutions.imageio.pam.PAMParser;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.media.jai.Interpolation;
import javax.media.jai.RenderedOp;
import javax.swing.*;
import junit.framework.JUnit4TestAdapter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GranuleRemovalPolicy;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GranuleStore;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.HarvestedSource;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.CloseableIterator;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FileGroupProvider.FileGroup;
import org.geotools.data.FileResourceInfo;
import org.geotools.data.Query;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType;
import org.geotools.gce.imagemosaic.catalog.index.ParametersType.Parameter;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Testing {@link ImageMosaicReader}.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de
 * @since 2.3
 */
public class ImageMosaicReaderTest extends Assert {

    private static final String OS_NAME = System.getProperty("os.name");

    private static final FilterFactory2 FF = FeatureUtilities.DEFAULT_FILTER_FACTORY;

    private static final double DELTA = 1E-4;

    private static final Logger LOGGER = Logger.getLogger(ImageMosaicReaderTest.class.toString());

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ImageMosaicReaderTest.class);
    }

    private URL rgbURL;

    private URL mixedSampleModelURL;
    private URL coverageBandsURL;

    private URL heterogeneousGranulesURL;

    private URL indexURL;

    private URL index2URL;

    private URL indexAlphaURL;

    private URL grayURL;

    private URL index_unique_paletteAlphaURL;

    private URL rgbAURL;

    private URL rgbAURLTiff;

    private URL rgbaExtraURLTiff;

    private URL overviewURL;

    static boolean INTERACTIVE;

    private URL timeURL;

    private URL timeAdditionalDomainsURL;

    private URL timeAdditionalDomainsRangeURL;

    private URL timeRangesURL;

    private URL imposedEnvelopeURL;

    private static final String H2_SAMPLE_PROPERTIES =
            "SPI=org.geotools.data.h2.H2DataStoreFactory\n"
                    + "dbtype=h2\n"
                    + "Loose\\ bbox=true #important for performances\n"
                    + "Estimated\\ extends=false #important for performances\n"
                    + "user=gs\n"
                    + "passwd=gs\n"
                    + "validate \\connections=true #important for avoiding errors\n"
                    + "Connection\\ timeout=3600\n"
                    + "max \\connections=10 #important for performances, internal pooling\n"
                    + "min \\connections=5  #important for performances, internal pooling\n";

    private URL timeFormatURL;

    private URL oneBitURL;

    @Rule public TemporaryFolder tempFolder = new TemporaryFolder();

    /** Testing crop capabilities. */
    @Test
    public void crop() throws Exception {
        imageMosaicCropTest(rgbURL, "crop-rgbURL", false);

        imageMosaicCropTest(indexURL, "crop-indexURL", false);

        imageMosaicCropTest(grayURL, "crop-grayURL", true);

        imageMosaicCropTest(overviewURL, "crop-overviewURL", true);

        imageMosaicCropTest(indexAlphaURL, "crop-indexAlphaURL", false);

        imageMosaicCropTest(rgbAURL, "crop-rgbAURL", false);

        imageMosaicCropTest(
                index_unique_paletteAlphaURL, "crop-index_unique_paletteAlphaURL", true);
    }

    /** Tests the {@link ImageMosaicReader} with default parameters for the various input params. */
    @Test
    //        @Ignore
    public void alpha() throws Exception {

        final String testName = "alpha-";
        if (INTERACTIVE)
            imageMosaicSimpleParamsTest(
                    rgbURL, null, null, testName + rgbURL.getFile() + "-original", false);
        GridCoverage2D coverage =
                imageMosaicSimpleParamsTest(
                        rgbURL, Color.black, Color.black, testName + rgbURL.getFile(), false);
        ColorModel colorModel = coverage.getRenderedImage().getColorModel();
        assertTrue(colorModel.hasAlpha());
        assertTrue(colorModel instanceof ComponentColorModel);

        if (INTERACTIVE)
            // the input images have transparency and they do overlap, we need
            // to ask for blending mosaic.
            imageMosaicSimpleParamsTest(
                    rgbAURL, null, null, testName + rgbAURL.getFile() + "-original", true);
        coverage =
                imageMosaicSimpleParamsTest(
                        rgbAURL, Color.black, Color.black, testName + rgbAURL.getFile(), false);
        colorModel = coverage.getRenderedImage().getColorModel();
        assertTrue(colorModel.hasAlpha());
        assertTrue(colorModel instanceof ComponentColorModel);

        // //
        //
        // This images have borders that are black and have a color model that
        // is IndexColorModel but all with different palette hence a color
        // conversion will be applied to go to RGB.
        //
        // When we do the input transparent color we will add transparency to
        // the images but only where the transparent color resides. Moreover the
        // background will be transparent.
        //
        // //
        if (INTERACTIVE)
            imageMosaicSimpleParamsTest(
                    indexURL, null, null, testName + indexURL.getFile() + "-original", false);
        coverage =
                imageMosaicSimpleParamsTest(
                        indexURL,
                        new Color(58, 49, 8),
                        Color.black,
                        testName + indexURL.getFile(),
                        false);
        colorModel = coverage.getRenderedImage().getColorModel();
        assertTrue(colorModel.hasAlpha());
        assertTrue(colorModel instanceof ComponentColorModel);

        if (INTERACTIVE)
            imageMosaicSimpleParamsTest(
                    overviewURL, null, null, testName + overviewURL.getFile() + "-original", false);
        coverage =
                imageMosaicSimpleParamsTest(
                        overviewURL,
                        new Color(58, 49, 8),
                        Color.black,
                        testName + overviewURL.getFile() + "-indexURL",
                        false);
        colorModel = coverage.getRenderedImage().getColorModel();
        assertTrue(colorModel.hasAlpha());
        assertTrue(colorModel instanceof ComponentColorModel);

        if (INTERACTIVE)
            imageMosaicSimpleParamsTest(
                    indexAlphaURL,
                    null,
                    null,
                    testName + indexAlphaURL.getFile() + "-original",
                    false);
        coverage =
                imageMosaicSimpleParamsTest(
                        indexAlphaURL,
                        new Color(41, 41, 33),
                        Color.black,
                        testName + indexAlphaURL.getFile(),
                        false);
        colorModel = coverage.getRenderedImage().getColorModel();
        assertTrue(colorModel.hasAlpha());
        assertTrue(colorModel instanceof ComponentColorModel);

        if (INTERACTIVE)
            imageMosaicSimpleParamsTest(
                    grayURL, null, null, testName + grayURL.getFile() + "-original", false);
        coverage =
                imageMosaicSimpleParamsTest(
                        grayURL, Color.black, Color.black, testName + grayURL.getFile(), false);
        colorModel = coverage.getRenderedImage().getColorModel();
        assertTrue(colorModel.hasAlpha());
        assertTrue(colorModel instanceof ComponentColorModel);
        ;
    }

    /** Tests the {@link ImageMosaicReader} with default parameters for the various input params. */
    @Test
    //	@Ignore
    public void overviews() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(overviewURL);
        ParameterValueGroup readParams = format.getReadParameters();
        final DefaultParameterDescriptorGroup descriptor =
                (DefaultParameterDescriptorGroup) readParams.getDescriptor();
        List<GeneralParameterDescriptor> descriptors = descriptor.descriptors();
        boolean hasOverviewPolicyParam = false;
        for (GeneralParameterDescriptor desc : descriptors) {
            if (AbstractGridFormat.OVERVIEW_POLICY
                    .getName()
                    .toString()
                    .equalsIgnoreCase(desc.getName().toString())) {
                hasOverviewPolicyParam = true;
                break;
            }
        }
        assertTrue(hasOverviewPolicyParam);
        final ImageMosaicReader reader = getReader(overviewURL, format);

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        final ParameterValue<String> tileSize =
                AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        tileSize.setValue("128,128");

        // Test the output coverage
        TestUtils.checkCoverage(
                reader, new GeneralParameterValue[] {gg, useJai, tileSize}, "overviews test");
        reader.dispose();
    }

    /** */
    @Test
    // @Ignore
    public void readingResolutions() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(overviewURL);
        final ImageMosaicReader reader = getReader(overviewURL, format);
        double[] result =
                reader.getReadingResolutions(OverviewPolicy.QUALITY, new double[] {32, 32});
        assertEquals(16.0714285714285, result[0], DELTA);
        assertEquals(16.0427807486631, result[1], DELTA);
        reader.dispose();
    }

    @Test
    public void testReadFromString() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(overviewURL);
        File mosaicFile = URLs.urlToFile(overviewURL);
        final ImageMosaicReader reader =
                (ImageMosaicReader) format.getReader(mosaicFile.getAbsolutePath());
        double[] result =
                reader.getReadingResolutions(OverviewPolicy.QUALITY, new double[] {32, 32});
        assertEquals(16.0714285714285, result[0], DELTA);
        assertEquals(16.0427807486631, result[1], DELTA);
        reader.dispose();
    }

    @Test
    // @Ignore
    public void timeElevationH2() throws Exception {

        final File workDir = new File(TestData.file(this, "."), "water_temp3");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, "water_temp3/watertemp.zip");
        final URL timeElevURL = TestData.url(this, "water_temp3");

        // place H2 file in the dir
        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), "/water_temp3/datastore.properties"))) {
            out.write("database=imagemosaic\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(reader);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 12);

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
        assertNotNull(timeMetadata);
        assertEquals(2, timeMetadata.split(",").length);
        assertEquals(timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals(timeMetadata.split(",")[1], reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
        assertNotNull(elevationMetadata);
        assertEquals("0,100", elevationMetadata);
        assertEquals(2, elevationMetadata.split(",").length);
        assertEquals(
                Double.parseDouble(elevationMetadata.split(",")[0]),
                Double.parseDouble(reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM")),
                1E-6);
        assertEquals(
                Double.parseDouble(elevationMetadata.split(",")[1]),
                Double.parseDouble(reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM")),
                1E-6);
        assertEquals("java.lang.Integer", reader.getMetadataValue("ELEVATION_DOMAIN_DATATYPE"));

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final List<Date> timeValues = new ArrayList<Date>();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = sdf.parse("2008-10-31T00:00:00.000Z");
        timeValues.add(date);
        time.setValue(timeValues);

        final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bkg.setValue(new double[] {-9999.0});

        final ParameterValue<Boolean> direct = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        direct.setValue(false);

        final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
        elevation.setValue(Arrays.asList(100.0));

        ResourceInfo info = reader.getInfo(reader.getGridCoverageNames()[0]);
        assertTrue(info instanceof FileResourceInfo);
        FileResourceInfo fileInfo = (FileResourceInfo) info;

        // Testing the FileGroupProvider
        int groups = 0;
        CloseableIterator<FileGroup> filesIterator = null;
        try {
            Query query = new Query("water_temp3");
            query.setFilter(FF.like(FF.property("location"), "*100_20081031T00*"));
            filesIterator = fileInfo.getFiles(query);
            while (filesIterator.hasNext()) {
                FileGroup group = filesIterator.next();
                if (groups == 0) {
                    Map<String, Object> md = group.getMetadata();
                    DateRange metadataTime = (DateRange) md.get(Utils.TIME_DOMAIN);
                    NumberRange metadataElevation = (NumberRange) md.get(Utils.ELEVATION_DOMAIN);
                    ReferencedEnvelope metadataBBOX = (ReferencedEnvelope) md.get(Utils.BBOX);
                    assertEquals(metadataTime.getMinValue().getTime(), date.getTime());
                    assertEquals((Double) metadataElevation.getMinValue(), 100.0, DELTA);
                    assertEquals(envelope.getMinimum(0), metadataBBOX.getMinX(), DELTA);
                    assertEquals(envelope.getMinimum(1), metadataBBOX.getMinY(), DELTA);
                    assertEquals(envelope.getMaximum(0), metadataBBOX.getMaxX(), DELTA);
                    assertEquals(envelope.getMaximum(1), metadataBBOX.getMaxY(), DELTA);
                }
                groups++;
            }
        } finally {
            if (filesIterator != null) {
                filesIterator.close();
            }
        }
        // Check the fileGroupProvider returned 4 fileGroups
        assertEquals(1, groups);

        // Test the output coverage
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, time, bkg, elevation, direct},
                "Time-Elevation Test");
        reader.dispose();

        reader = getReader(timeElevURL, format);
        elevation.setValue(Arrays.asList(NumberRange.create(0.0, 10.0)));

        // Test the output coverage
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, time, bkg, elevation, direct},
                "Time-Elevation Test");

        // clean up
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, "water_temp3"));
        }
        reader.dispose();
    }

    /**
     * This test is used to check backward compatibility with old imagemosaics wich does not include
     * the TypeName=MOSAICNAME into the generated MOSAICNAME.properties file
     */
    @Test
    // @Ignore
    public void testTypeNameBackwardsCompatibility() throws Exception {

        final File workDir = new File(TestData.file(this, "."), "water_temp5");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, "water_temp5/watertemp.zip");
        final URL timeElevURL = TestData.url(this, "water_temp5");

        // place H2 file in the dir

        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), "/water_temp5/datastore.properties"))) {
            out.write("database=imagemosaic\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        // now start the test
        AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(reader);
        reader.dispose();
        format = null;

        // remove the TypeName=MOSAICNAME from MOSAICNAME.properties
        File mosaicFile = new File(TestData.file(this, "."), "/water_temp5/water_temp5.properties");
        Properties properties = new Properties();
        try (FileInputStream fin = new FileInputStream(mosaicFile)) {
            properties.load(fin);
        }
        try (FileWriter fw = new FileWriter(mosaicFile)) {
            assertNotNull(properties.remove("TypeName"));
            properties.store(fw, "");
        }

        // we should be able to load the mosaic also without the TypeName=MOSAICNAME
        format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        reader = getReader(timeElevURL, format);
        assertNotNull(reader);

        // clean up
        reader.dispose();
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, "water_temp5"));
        }
    }

    /**
     * This test is used to check backward compatibility with old imagemosaics wich does not include
     * the TypeName=MOSAICNAME into the generated MOSAICNAME.properties file
     */
    @Test
    public void testMixedTables() throws Exception {
        String mosaicName = "water_temp6";
        final File workDir = new File(TestData.file(this, "."), mosaicName);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, mosaicName + "/watertemp.zip");
        final URL timeElevURL = TestData.url(this, mosaicName);

        // place H2 file in the dir
        File datastoreProperties = new File(workDir, "datastore.properties");
        try (FileWriter out = new FileWriter(datastoreProperties)) {
            out.write("database=imagemosaic\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        // make it fill the tables
        AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(reader);
        reader.dispose();
        format = null;

        // setup the typename in the indexer
        File indexerProperties = new File(workDir, "indexer.properties");
        Properties indexer = new Properties();
        // tell it to use the existing schema
        indexer.put("UseExistingSchema", "true");
        indexer.put("TypeName", "customIndex");
        try (OutputStream os = new FileOutputStream(indexerProperties)) {
            indexer.store(os, null);
        }

        // get a connection to the db to create a few extra tables
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(datastoreProperties)) {
            props.load(is);
        }
        props.put("database", new File(workDir, "imagemosaic").getPath());
        JDBCDataStore store = (JDBCDataStore) DataStoreFinder.getDataStore(props);
        // H2 seems to return the table names in alphabetical order
        store.createSchema(DataUtilities.createType("aaa_noFootprint", "a:String,b:Integer"));
        store.createSchema(DataUtilities.createType("bbb_noLocation", "geom:Polygon,b:String"));
        try (Connection conn = store.getConnection(Transaction.AUTO_COMMIT);
                Statement st = conn.createStatement(); ) {
            st.execute("alter table \"" + mosaicName + "\" rename to \"customIndex\"");
            st.execute("UPDATE GEOMETRY_COLUMNS SET F_TABLE_NAME = 'customIndex'");
        }
        store.dispose();

        // remove all mosaic related files
        for (File file :
                FileUtils.listFiles(workDir, new RegexFileFilter(mosaicName + ".*"), null)) {
            assertTrue(file.delete());
        }

        // see that we can create the reader again
        format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        reader = getReader(timeElevURL, format);
        assertNotNull(reader);
        reader.dispose();
        format = null;
    }

    @Test
    //	@Ignore
    public void timeElevation() throws Exception {
        final File workDir = new File(TestData.file(this, "."), "watertemp2");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, "watertemp2/watertemp.zip");

        final URL timeElevURL = TestData.url(this, "watertemp2");

        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 12);

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
        assertNotNull(timeMetadata);
        assertEquals(2, timeMetadata.split(",").length);
        assertEquals(timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals(timeMetadata.split(",")[1], reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("java.util.Date", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
        assertNotNull(elevationMetadata);
        assertEquals("0,100", elevationMetadata);
        assertEquals(2, elevationMetadata.split(",").length);
        assertEquals(
                elevationMetadata.split(",")[0],
                reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM"));
        assertEquals(
                elevationMetadata.split(",")[1],
                reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("ELEVATION_DOMAIN_DATATYPE"));

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final List<Date> timeValues = new ArrayList<Date>();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = sdf.parse("2008-11-01T00:00:00.000Z");
        timeValues.add(date);
        time.setValue(timeValues);

        final ParameterValue<Boolean> direct = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        direct.setValue(false);

        final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bkg.setValue(new double[] {-9999.0});

        final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
        elevation.setValue(Arrays.asList(0.0));

        // Test the output coverage
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, time, bkg, elevation, direct},
                "Time-Elevation Test");

        reader = getReader(timeElevURL, format);
        elevation.setValue(Arrays.asList(NumberRange.create(0.0, 10.0)));

        // Test the output coverage
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, time, bkg, elevation, direct},
                "Time-Elevation Test");

        // clean up
        reader.dispose();
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, "watertemp2"));
        }
    }

    @Test
    //        @Ignore
    public void timeDoubleElevation() throws Exception {
        // Check we can have an integer elevation too
        final File workDir = new File(TestData.file(this, "."), "watertemp1");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));

        TestData.unzipFile(this, "watertemp1/watertemp.zip");
        final URL timeElevURL = TestData.url(this, "watertemp1");
        // place H2 file in the dir
        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), "/watertemp1/indexer.properties"))) {
            out.write("TimeAttribute=ingestion\n");
            out.write("ElevationAttribute=elevation\n");
            out.write(
                    "Schema=*the_geom:Polygon,location:String,ingestion:java.util.Date,elevation:Double\n");
            out.write(
                    "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](ingestion),DoubleFileNameExtractorSPI[elevationregex](elevation)\n");
            out.flush();
        }

        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 12);

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
        assertNotNull(timeMetadata);
        assertEquals(2, timeMetadata.split(",").length);
        assertEquals(timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals(timeMetadata.split(",")[1], reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        final String elevationMetadata = reader.getMetadataValue("ELEVATION_DOMAIN");
        assertNotNull(elevationMetadata);
        assertEquals(2, elevationMetadata.split(",").length);
        assertEquals("0.0", reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM"));
        assertEquals("100.0", reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM"));
        assertEquals("java.lang.Double", reader.getMetadataValue("ELEVATION_DOMAIN_DATATYPE"));

        // clean up
        reader.dispose();
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, "watertemp1"));
        }
    }

    @Test
    //    @Ignore
    public void imposedBBox() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(imposedEnvelopeURL);
        final ImageMosaicReader reader = getReader(imposedEnvelopeURL, format);

        // check envelope
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        assertNotNull(envelope);

        assertEquals(-180.0, envelope.getMinimum(0), 1E-6);
        assertEquals(-90.0, envelope.getMinimum(1), 1E-6);
        assertEquals(180.0, envelope.getMaximum(0), 1E-6);
        assertEquals(90.0, envelope.getMaximum(1), 1E-6);

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 3.0,
                reader.getOriginalGridRange().getSpan(1) / 3.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        // Test the output coverage
        GridCoverage2D coverage =
                TestUtils.checkCoverage(
                        reader, new GeneralParameterValue[] {gg, useJai}, "Imposed BBox");

        // check that the grid geometry is canonical
        GridGeometry2D ggCoverage = coverage.getGridGeometry();
        assertEquals(0, ggCoverage.getGridRange().getLow(0));
        assertEquals(0, ggCoverage.getGridRange().getLow(1));

        reader.dispose();
    }

    @Test
    //	@Ignore
    public void time() throws Exception {

        final AbstractGridFormat format = TestUtils.getFormat(timeURL);
        ImageMosaicReader reader = getReader(timeURL, format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 12);
        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals("2004-05-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals(
                "2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                reader.getMetadataValue(metadataNames[0]));
        assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();

        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Date timeD = formatD.parse("2004-02-01T00:00:00.000Z");
        time.setValue(
                new ArrayList() {
                    {
                        add(timeD);
                    }
                });

        // Test the output coverage
        TestUtils.checkCoverage(
                reader, new GeneralParameterValue[] {gg, useJai, time}, "time test");

        // specify time range
        // Test the output coverage
        reader = getReader(timeURL, format);
        time.setValue(
                new ArrayList() {
                    {
                        add(
                                new DateRange(
                                        formatD.parse("2004-02-01T00:00:00.000Z"),
                                        formatD.parse("2004-03-01T00:00:00.000Z")));
                    }
                });
        TestUtils.checkCoverage(
                reader, new GeneralParameterValue[] {gg, useJai, time}, "time test");

        reader.dispose();
    }

    @Test
    public void testTimeFormat() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(timeFormatURL);
        ImageMosaicReader reader = getReader(timeFormatURL, format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 12);
        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        assertEquals("2004-02-01T12:05:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals("2004-05-30T12:15:59.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals(
                "2004-02-01T12:05:00.000Z,2004-03-01T15:07:00.000Z,2004-04-15T19:05:00.000Z,2004-05-30T12:15:59.000Z",
                reader.getMetadataValue(metadataNames[0]));
        assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

        reader.dispose();
    }

    /** Simple test method accessing time and 2 custom dimensions for the sample dataset */
    @Test
    // @Ignore
    @SuppressWarnings("rawtypes")
    public void timeAdditionalDim() throws Exception {

        final AbstractGridFormat format = TestUtils.getFormat(timeAdditionalDomainsURL);
        ImageMosaicReader reader = getReader(timeAdditionalDomainsURL, format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 18);
        assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
        assertEquals("20081031T0000000,20081101T0000000", reader.getMetadataValue("DATE_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue("DATE_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_DEPTH_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_XX_DOMAIN"));
        assertEquals("20,100", reader.getMetadataValue("DEPTH_DOMAIN"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("DEPTH_DOMAIN_DATATYPE"));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Date timeD = formatD.parse("2008-10-31T00:00:00.000Z");
        time.setValue(
                new ArrayList() {
                    {
                        add(timeD);
                    }
                });

        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> dateValue = null;
        ParameterValue<List<String>> depthValue = null;
        final String selectedWaveLength = "020";
        final String selectedDate = "20081031T0000000";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("DATE")) {
                dateValue = param.createValue();
                dateValue.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedDate);
                            }
                        });
            } else if (param.getName().getCode().equalsIgnoreCase("DEPTH")) {
                depthValue = param.createValue();
                depthValue.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedWaveLength);
                            }
                        });
            }
        }
        assertNotNull(depthValue);
        assertNotNull(dateValue);

        // Test the output coverage
        GeneralParameterValue[] values =
                new GeneralParameterValue[] {useJai, time, dateValue, depthValue};
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, true);
        final String fileSource =
                (String) coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);

        // Check the proper granule has been read
        final String baseName = FilenameUtils.getBaseName(fileSource);
        assertEquals(baseName, "NCOM_wattemp_" + selectedWaveLength + "_" + selectedDate + "_12");
        TestUtils.testCoverage(reader, values, "domain test", coverage, null);
        reader.dispose();
    }

    /** Simple test method accessing time and 2 custom dimensions for the sample dataset */
    @Test
    @SuppressWarnings("rawtypes")
    public void timeAdditionalDimRanges() throws Exception {

        final AbstractGridFormat format = TestUtils.getFormat(timeAdditionalDomainsRangeURL);
        ImageMosaicReader reader = getReader(timeAdditionalDomainsRangeURL, format);
        try {

            final String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals(metadataNames.length, 18);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2008-10-31T00:00:00.000Z/2008-11-04T00:00:00.000Z/PT1S,2008-11-05T00:00:00.000Z/2008-11-07T00:00:00.000Z/PT1S",
                    reader.getMetadataValue("TIME_DOMAIN"));
            assertEquals(
                    "2008-10-31T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
            assertEquals(
                    "2008-11-07T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
            String expectedType =
                    Boolean.getBoolean("org.geotools.shapefile.datetime")
                            ? "java.sql.Timestamp"
                            : "java.util.Date";
            assertEquals(expectedType, reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

            assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
            assertEquals("20/99,100/150", reader.getMetadataValue("ELEVATION_DOMAIN"));
            assertEquals("20", reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM"));
            assertEquals("150", reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM"));
            assertEquals("java.lang.Integer", reader.getMetadataValue("ELEVATION_DOMAIN_DATATYPE"));

            assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
            assertEquals(
                    "20081031T000000,20081101T000000,20081105T000000",
                    reader.getMetadataValue("DATE_DOMAIN"));
            assertEquals("java.lang.String", reader.getMetadataValue("DATE_DOMAIN_DATATYPE"));

            assertEquals("true", reader.getMetadataValue("HAS_WAVELENGTH_DOMAIN"));
            assertEquals("12/24,25/80", reader.getMetadataValue("WAVELENGTH_DOMAIN"));
            assertEquals("12", reader.getMetadataValue("WAVELENGTH_DOMAIN_MINIMUM"));
            assertEquals("80", reader.getMetadataValue("WAVELENGTH_DOMAIN_MAXIMUM"));
            assertEquals(
                    "java.lang.Integer", reader.getMetadataValue("WAVELENGTH_DOMAIN_DATATYPE"));

            // use imageio with defined tiles
            final ParameterValue<Boolean> useJai =
                    AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
            useJai.setValue(false);

            // specify time
            final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
            final Date timeD = parseTimeStamp("2008-11-01T00:00:00.000Z");
            time.setValue(
                    new ArrayList() {
                        {
                            add(timeD);
                        }
                    });

            final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
            elevation.setValue(
                    new ArrayList() {
                        {
                            add(34); // Elevation
                        }
                    });

            // specify additional Dimensions
            Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
            ParameterValue<List<String>> dateValue = null;
            ParameterValue<List<String>> waveLength = null;
            final String selectedWaveLength = "20";
            final String selectedDate = "20081031T000000";
            for (ParameterDescriptor param : params) {
                if (param.getName().getCode().equalsIgnoreCase("DATE")) {
                    dateValue = param.createValue();
                    dateValue.setValue(
                            new ArrayList<String>() {
                                {
                                    add(selectedDate);
                                }
                            });
                } else if (param.getName().getCode().equalsIgnoreCase("WAVELENGTH")) {
                    waveLength = param.createValue();
                    waveLength.setValue(
                            new ArrayList<String>() {
                                {
                                    add(selectedWaveLength);
                                }
                            });
                }
            }
            assertNotNull(waveLength);
            assertNotNull(dateValue);

            // Test the output coverage
            GeneralParameterValue[] values =
                    new GeneralParameterValue[] {useJai, dateValue, time, waveLength, elevation};
            final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, true);
            final String fileSource =
                    (String)
                            coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);

            // Check the proper granule has been read
            final String baseName = FilenameUtils.getBaseName(fileSource);
            assertEquals(baseName, "temp_020_099_20081031T000000_20081103T000000_12_24");
            TestUtils.testCoverage(reader, values, "domain test", coverage, null);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    /** Simple test method to test emptyMosaic creation support followed by harvesting. dataset */
    @Test
    // @Ignore
    public void testEmpytMosaic() throws Exception {

        final File workDir = new File(TestData.file(this, "."), "emptyMosaic");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        File zipFile = new File(workDir, "temperature.zip");
        FileUtils.copyFile(TestData.file(this, "temperature.zip"), zipFile);
        TestData.unzipFile(this, "emptyMosaic/temperature.zip");
        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), "/emptyMosaic/datastore.properties"))) {
            out.write("database=imagemosaic\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        FileUtils.deleteQuietly(zipFile);

        final URL emptyMosaicURL = TestData.url(this, "emptyMosaic");
        final AbstractGridFormat mosaicFormat = TestUtils.getFormat(emptyMosaicURL);
        ImageMosaicReader reader = getReader(emptyMosaicURL, mosaicFormat);
        GranuleCatalog originalCatalog = reader.granuleCatalog;

        String[] metadataNames = reader.getMetadataNames();
        assertNull(metadataNames);

        File source = URLs.urlToFile(timeRangesURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "singleHarvest1");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);

        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = fileToUrl(directory1);
        File renamed = new File(directory1, "temp_020_099_20081101T000000_20081104T000000.tiff");

        try {
            // now go and harvest the other file
            List<HarvestedSource> summary = reader.harvest(null, renamed, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertEquals(renamed.getCanonicalFile(), ((File) hf.getSource()).getCanonicalFile());
            assertTrue(hf.success());

            // the harvest put the file in the same coverage
            reader = getReader(emptyMosaicURL, mosaicFormat);
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2008-11-01T00:00:00.000Z/2008-11-04T00:00:00.000Z/PT1S",
                    reader.getMetadataValue(metadataNames[0]));

            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            assertEquals(1, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            SimpleFeatureIterator fi = granules.getGranules(q).features();
            try {
                assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();
                String expected =
                        "../singleHarvest1/temp_020_099_20081101T000000_20081104T000000.tiff"
                                .replace('/', File.separatorChar);
                assertEquals(expected, f.getAttribute("location"));
                assertEquals(
                        "2008-11-01T00:00:00.000Z",
                        ConvertersHack.convert(f.getAttribute("time"), String.class));
            } finally {
                fi.close();
            }

        } finally {
            reader.dispose();
        }
    }

    /** Simple test method to test emptyMosaic creation support followed by harvesting */
    @Test
    public void testImageMosaicConfigFilePath() throws Exception {
        CatalogBuilderConfiguration builderConfig = new CatalogBuilderConfiguration();
        Indexer indexer = builderConfig.getIndexer();
        ParametersType parameters = indexer.getParameters();
        List<Parameter> parameterList = parameters.getParameter();
        File file = TestData.file(this, "merge");
        File auxFile = new File(file, "aux.xml");
        IndexerUtils.setParam(parameterList, Prop.ROOT_MOSAIC_DIR, file.getAbsolutePath());
        IndexerUtils.setParam(parameterList, Prop.INDEXING_DIRECTORIES, file.getAbsolutePath());
        IndexerUtils.setParam(parameterList, Prop.AUXILIARY_FILE, auxFile.getAbsolutePath());
        IndexerUtils.setParam(parameterList, Prop.ABSOLUTE_PATH, "true");
        ImageMosaicConfigHandler handler =
                new ImageMosaicConfigHandler(builderConfig, new ImageMosaicEventHandlers());
        Hints hints = handler.getRunConfiguration().getHints();
        String auxiliaryFilePath = (String) hints.get(Utils.AUXILIARY_FILES_PATH);
        assertEquals(auxFile.getAbsolutePath(), auxiliaryFilePath);
    }

    /** Simple test method to test emptyMosaic creation support followed by harvesting */
    @Test
    public void testEmpytMosaicXML() throws Exception {

        final File workDir = new File(TestData.file(this, "."), "emptyMosaicXML");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        File zipFile = new File(workDir, "temperature2.zip");
        FileUtils.copyFile(TestData.file(this, "temperature2.zip"), zipFile);
        TestData.unzipFile(this, "emptyMosaicXML/temperature2.zip");
        try (FileWriter out =
                new FileWriter(
                        new File(
                                TestData.file(this, "."),
                                "/emptyMosaicXML/datastore.properties"))) {
            out.write("database=imagemosaic\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        FileUtils.deleteQuietly(zipFile);

        final URL emptyMosaicURL = TestData.url(this, "emptyMosaicXML");
        final AbstractGridFormat mosaicFormat = TestUtils.getFormat(emptyMosaicURL);
        ImageMosaicReader reader = getReader(emptyMosaicURL, mosaicFormat);
        GranuleCatalog originalCatalog = reader.granuleCatalog;

        String[] metadataNames = reader.getMetadataNames();
        assertNull(metadataNames);

        final File tempDir = new File(TestData.file(this, "."), "water_temp4");
        if (!tempDir.mkdir()) {
            FileUtils.deleteDirectory(tempDir);
            assertTrue("Unable to create workdir:" + tempDir, tempDir.mkdir());
        }
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(tempDir, "watertemp.zip"));
        TestData.unzipFile(this, "water_temp4/watertemp.zip");
        final URL timeElevURL = TestData.url(this, "water_temp4");
        File source = URLs.urlToFile(timeElevURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "singleHarvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);

        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = fileToUrl(directory1);
        File renamed = new File(directory1, "NCOM_wattemp_000_20081031T0000000_12.tiff");

        try {
            // now go and harvest the other file
            List<HarvestedSource> summary = reader.harvest(null, renamed, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertEquals(renamed.getCanonicalFile(), ((File) hf.getSource()).getCanonicalFile());
            assertTrue(hf.success());

            // the harvest put the file in the same coverage
            reader = getReader(emptyMosaicURL, mosaicFormat);
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2008-10-31T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));

            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            assertEquals(1, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            SimpleFeatureIterator fi = granules.getGranules(q).features();
            try {
                assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();
                String expected =
                        "../singleHarvest2/NCOM_wattemp_000_20081031T0000000_12.tiff"
                                .replace('/', File.separatorChar);
                assertEquals(expected, f.getAttribute("location"));
                assertEquals(
                        "2008-10-31T00:00:00.000Z",
                        ConvertersHack.convert(f.getAttribute("time"), String.class));
            } finally {
                fi.close();
            }

        } finally {
            reader.dispose();
        }
    }

    /** Simple test method accessing time and 2 custom dimensions for the sample dataset */
    @Test
    @SuppressWarnings("rawtypes")
    public void granuleSourceTest() throws Exception {

        final AbstractGridFormat format = TestUtils.getFormat(timeAdditionalDomainsRangeURL);
        ImageMosaicReader reader = getReader(timeAdditionalDomainsRangeURL, format);

        GranuleSource source =
                ((StructuredGridCoverage2DReader) reader).getGranules("time_domainsRanges", true);
        final int granules = source.getCount(null);
        final SimpleFeatureType type = source.getSchema();
        assertEquals(
                "SimpleFeatureTypeImpl time_domainsRanges identified extends polygonFeature(the_geom:MultiPolygon,location:location,time:time,endtime:endtime,date:date,lowz:lowz,highz:highz,loww:loww,highw:highw)",
                type.toString());
        assertEquals(granules, 12);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 18);
        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        assertEquals(
                "2008-10-31T00:00:00.000Z/2008-11-04T00:00:00.000Z/PT1S,2008-11-05T00:00:00.000Z/2008-11-07T00:00:00.000Z/PT1S",
                reader.getMetadataValue("TIME_DOMAIN"));
        assertEquals("2008-10-31T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals("2008-11-07T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("java.util.Date", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("20/99,100/150", reader.getMetadataValue("ELEVATION_DOMAIN"));
        assertEquals("20", reader.getMetadataValue("ELEVATION_DOMAIN_MINIMUM"));
        assertEquals("150", reader.getMetadataValue("ELEVATION_DOMAIN_MAXIMUM"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("ELEVATION_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
        assertEquals(
                "20081031T000000,20081101T000000,20081105T000000",
                reader.getMetadataValue("DATE_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue("DATE_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_WAVELENGTH_DOMAIN"));
        assertEquals("12/24,25/80", reader.getMetadataValue("WAVELENGTH_DOMAIN"));
        assertEquals("12", reader.getMetadataValue("WAVELENGTH_DOMAIN_MINIMUM"));
        assertEquals("80", reader.getMetadataValue("WAVELENGTH_DOMAIN_MAXIMUM"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("WAVELENGTH_DOMAIN_DATATYPE"));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final Date timeD = parseTimeStamp("2008-11-01T00:00:00.000Z");
        time.setValue(
                new ArrayList() {
                    {
                        add(timeD);
                    }
                });

        final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
        elevation.setValue(
                new ArrayList() {
                    {
                        add(34); // Elevation
                    }
                });

        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> dateValue = null;
        ParameterValue<List<String>> waveLength = null;
        final String selectedWaveLength = "20";
        final String selectedDate = "20081031T000000";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("DATE")) {
                dateValue = param.createValue();
                dateValue.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedDate);
                            }
                        });
            } else if (param.getName().getCode().equalsIgnoreCase("WAVELENGTH")) {
                waveLength = param.createValue();
                waveLength.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedWaveLength);
                            }
                        });
            }
        }
        assertNotNull(waveLength);
        assertNotNull(dateValue);

        // Get the properties file and check if the SuggestedSPI parameter is set
        File covFile = URLs.urlToFile(timeAdditionalDomainsRangeURL);
        File propFile = new File(covFile, "time_domainsRanges.properties");
        // Ensure the file exists
        assertTrue(propFile.exists());
        Properties props = CoverageUtilities.loadPropertiesFromURL(fileToUrl(propFile));
        // ImageReaderSpi property
        String suggestedSpi = props.getProperty(Utils.Prop.SUGGESTED_SPI);
        // Check if the property exists
        assertNotNull(suggestedSpi);
        // Get the class indicated by the SPI
        Class<?> clazz = Class.forName(suggestedSpi);
        // Check if the class really exists
        assertNotNull(clazz);

        // Test the output coverage
        GeneralParameterValue[] values =
                new GeneralParameterValue[] {useJai, dateValue, time, waveLength, elevation};
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, true);
        final String fileSource =
                (String) coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);

        // Check the proper granule has been read
        final String baseName = FilenameUtils.getBaseName(fileSource);
        assertEquals(baseName, "temp_020_099_20081031T000000_20081103T000000_12_24");
        TestUtils.testCoverage(reader, values, "domain test", coverage, null);
        reader.dispose();
    }

    /** Simple test method testing dimensions Descriptor for the sample dataset */
    @Test
    public void testDimensionsDescriptor() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(timeAdditionalDomainsRangeURL);
        ImageMosaicReader reader = getReader(timeAdditionalDomainsRangeURL, format);
        List<DimensionDescriptor> descriptors =
                ((StructuredGridCoverage2DReader) reader)
                        .getDimensionDescriptors("time_domainsRanges");
        assertNotNull(descriptors);
        assertEquals(4, descriptors.size());

        Map<String, DimensionDescriptor> dds = new HashMap<String, DimensionDescriptor>();
        for (DimensionDescriptor dd : descriptors) {
            dds.put(dd.getName(), dd);
        }

        DimensionDescriptor descriptor = dds.get("wavelength");
        assertEquals("wavelength", descriptor.getName());
        assertEquals("loww", descriptor.getStartAttribute());
        assertEquals("highw", descriptor.getEndAttribute());

        descriptor = dds.get("date");
        assertEquals("date", descriptor.getName());
        assertEquals("date", descriptor.getStartAttribute());
        assertNull(descriptor.getEndAttribute());

        descriptor = dds.get("TIME");
        assertEquals("TIME", descriptor.getName());
        assertEquals("time", descriptor.getStartAttribute());
        assertEquals("endtime", descriptor.getEndAttribute());
        assertEquals(CoverageUtilities.UCUM.TIME_UNITS.getName(), descriptor.getUnits());
        assertEquals(CoverageUtilities.UCUM.TIME_UNITS.getSymbol(), descriptor.getUnitSymbol());

        descriptor = dds.get("ELEVATION");
        assertEquals("ELEVATION", descriptor.getName());
        assertEquals("lowz", descriptor.getStartAttribute());
        assertEquals("highz", descriptor.getEndAttribute());

        reader.dispose();
    }

    @Test
    public void testAdditionalDimRangesNoTimestamp() throws Exception {
        System.setProperty("org.geotools.shapefile.datetime", "false");
        timeAdditionalDimRanges();
    }

    /** Tests that selection by range works properly */
    @Test
    public void timeTimeRangeSelection() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(timeAdditionalDomainsRangeURL);
        ImageMosaicReader reader = getReader(timeAdditionalDomainsRangeURL, format);

        // specify a range that's below the available data
        GridCoverage2D coverage =
                readCoverageInDateRange(
                        reader, "2008-10-20T00:00:00.000Z", "2008-10-25T12:00:00.000Z");
        assertNull(coverage);

        // specify a range that's above the available data
        coverage =
                readCoverageInDateRange(
                        reader, "2008-11-20T00:00:00.000Z", "2008-11-25T12:00:00.000Z");
        assertNull(coverage);

        // specify a range that's in a hole where no data is available
        coverage =
                readCoverageInDateRange(
                        reader, "2008-11-04T12:00:00.000Z", "2008-11-04T18:00:00.000Z");
        assertNull(coverage);

        // specify a range that covers it all
        coverage =
                readCoverageInDateRange(
                        reader, "2008-10-20T00:00:00.000Z", "2008-11-20T00:00:00.000Z");
        assertNotNull(coverage);

        // specify a range that overlaps with the first range on the low side
        coverage =
                readCoverageInDateRange(
                        reader, "2008-10-28T00:00:00.000Z", "2008-10-31T18:00:00.000Z");
        assertNotNull(coverage);
        String fileSource =
                (String) coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        assertEquals(
                "temp_020_099_20081031T000000_20081103T000000_12_24",
                FilenameUtils.getBaseName(fileSource));

        // specify a range that overlaps in the middle with the second range
        coverage =
                readCoverageInDateRange(
                        reader, "2008-11-03T12:00:00.000Z", "2008-11-04T00:00:00.000Z");
        assertNotNull(coverage);
        fileSource =
                (String) coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        assertEquals(
                "temp_020_099_20081101T000000_20081104T000000_12_24",
                FilenameUtils.getBaseName(fileSource));

        // specify a range matching an exact start of a range
        coverage =
                readCoverageInDateRange(
                        reader, "2008-10-31T00:00:00.000Z", "2008-10-31T00:00:00.000Z");
        assertNotNull(coverage);
        fileSource =
                (String) coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        assertEquals(
                "temp_020_099_20081031T000000_20081103T000000_12_24",
                FilenameUtils.getBaseName(fileSource));

        // specify a range matching an exact end of a range
        coverage =
                readCoverageInDateRange(
                        reader, "2008-11-04T00:00:00.000Z", "2008-11-04T00:00:00.000Z");
        assertNotNull(coverage);
        fileSource =
                (String) coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        assertEquals(
                "temp_020_099_20081101T000000_20081104T000000_12_24",
                FilenameUtils.getBaseName(fileSource));

        reader.dispose();
    }

    private GridCoverage2D readCoverageInDateRange(
            ImageMosaicReader reader, String start, String end) throws Exception {
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        Date s = parseTimeStamp(start);
        Date e = parseTimeStamp(end);
        DateRange range = new DateRange(s, e);
        time.setValue(Arrays.asList(range));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
        elevation.setValue(
                new ArrayList() {
                    {
                        add(34); // Elevation
                    }
                });

        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> waveLength = null;
        final String selectedWaveLength = "20";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("WAVELENGTH")) {
                waveLength = param.createValue();
                waveLength.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedWaveLength);
                            }
                        });
            }
        }
        assertNotNull(waveLength);

        GeneralParameterValue[] values =
                new GeneralParameterValue[] {useJai, time, waveLength, elevation};
        return TestUtils.getCoverage(reader, values, false);
    }

    private Date parseTimeStamp(String timeStamp) throws ParseException {
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatD.parse(timeStamp);
    }

    /** Simple test method accessing time and 2 custom dimensions for the sample dataset */
    @Test
    // @Ignore
    @SuppressWarnings("rawtypes")
    public void multipleDimensionsStacked() throws Exception {

        final AbstractGridFormat format = TestUtils.getFormat(timeAdditionalDomainsURL);
        ImageMosaicReader reader = getReader(timeAdditionalDomainsURL, format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 18);
        assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
        assertEquals("20081031T0000000,20081101T0000000", reader.getMetadataValue("DATE_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue("DATE_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_DEPTH_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("20,100", reader.getMetadataValue("DEPTH_DOMAIN"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("DEPTH_DOMAIN_DATATYPE"));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);
        final ParameterValue<String> tileSize =
                AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        tileSize.setValue("128,128");

        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Date timeD = formatD.parse("2008-10-31T00:00:00.000Z");
        time.setValue(
                new ArrayList() {
                    {
                        add(timeD);
                    }
                });

        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> dateValue = null;
        final String selectedDate = "20081031T0000000";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("DATE")) {
                dateValue = param.createValue();
                dateValue.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedDate);
                            }
                        });
            }
        }

        // Stacked bands
        final ParameterValue<String> paramStacked = ImageMosaicFormat.MERGE_BEHAVIOR.createValue();
        paramStacked.setValue(MergeBehavior.STACK.toString());

        // Test the output coverage
        GeneralParameterValue[] values =
                new GeneralParameterValue[] {useJai, tileSize, time, dateValue, paramStacked};
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, false);
        assertNotNull(coverage);

        // inspect reanderedImage
        final RenderedImage image = coverage.getRenderedImage();
        assertEquals("wrong number of bands detected", 1, image.getSampleModel().getNumBands());

        reader.dispose();
    }

    /**
     * Tests the {@link ImageMosaicReader} with support for different resolutions/different number
     * of overviews.
     *
     * <p>world_a.tif => Pixel Size = (0.833333333333333,-0.833333333333333); 4 overviews
     * world_b1.tif => Pixel Size = (1.406250000000000,-1.406250000000000); 2 overviews world_b2.tif
     * => Pixel Size = (0.666666666666667,-0.666666666666667); 0 overviews
     */
    @Test
    // @Ignore
    public void testHeterogeneousGranules() throws Exception {

        final AbstractGridFormat format = TestUtils.getFormat(heterogeneousGranulesURL);
        ImageMosaicReader reader = getReader(heterogeneousGranulesURL, format);

        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(10, 10);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        final ParameterValue<OverviewPolicy> op = AbstractGridFormat.OVERVIEW_POLICY.createValue();

        LOGGER.info("\nTesting with OverviewPolicy = QUALITY");
        op.setValue(OverviewPolicy.QUALITY);
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, useJai, op},
                "heterogeneous granules test: OverviewPolicy=QUALITY",
                rasterArea);

        LOGGER.info("\nTesting with OverviewPolicy = SPEED");
        reader = getReader(heterogeneousGranulesURL, format);
        op.setValue(OverviewPolicy.SPEED);
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, useJai, op},
                "heterogeneous granules test: OverviewPolicy=SPEED",
                rasterArea);

        LOGGER.info("\nTesting with OverviewPolicy = NEAREST");
        reader = getReader(heterogeneousGranulesURL, format);
        op.setValue(OverviewPolicy.NEAREST);
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, useJai, op},
                "heterogeneous granules test: OverviewPolicy=NEAREST",
                rasterArea);

        LOGGER.info("\nTesting with OverviewPolicy = IGNORE");
        reader = getReader(heterogeneousGranulesURL, format);
        op.setValue(OverviewPolicy.IGNORE);
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, useJai, op},
                "heterogeneous granules test: OverviewPolicy=IGNORE",
                rasterArea);

        reader.dispose();
    }

    /** Tests the {@link ImageMosaicReader} with default parameters for the various input params. */
    @Test
    //  //@Ignore
    public void defaultParameterValue() throws Exception {

        final String baseTestName = "testDefaultParameterValue";
        imageMosaicSimpleParamsTest(rgbURL, null, null, baseTestName + rgbURL.getFile(), false);
        imageMosaicSimpleParamsTest(rgbAURL, null, null, baseTestName + rgbAURL.getFile(), false);
        imageMosaicSimpleParamsTest(
                overviewURL, null, null, baseTestName + overviewURL.getFile(), false);
        imageMosaicSimpleParamsTest(indexURL, null, null, baseTestName + indexURL.getFile(), false);
        imageMosaicSimpleParamsTest(grayURL, null, null, baseTestName + grayURL.getFile(), false);
        imageMosaicSimpleParamsTest(
                indexAlphaURL, null, null, baseTestName + indexAlphaURL.getFile(), false);
    }

    /** Testing one bit mosaics (black/white) */
    @Test
    public void oneBit() throws Exception {

        final String baseTestName = "oneBit";
        imageMosaicSimpleParamsTest(
                oneBitURL, null, null, baseTestName + oneBitURL.getFile(), false);
        imageMosaicSimpleParamsTest(
                oneBitURL, Color.white, null, baseTestName + oneBitURL.getFile(), false);
        imageMosaicSimpleParamsTest(
                oneBitURL, null, Color.white, baseTestName + oneBitURL.getFile(), false);
    }

    @Test
    // @Ignore
    public void errors() throws Exception {
        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, CRS.decode("EPSG:4326", true));

        ////
        //
        // MOSAIC_LOCATION_ATTRIBUTE
        //
        // error for location attribute
        AbstractGridCoverage2DReader reader = null;
        try {
            LOGGER.info(
                    "Testing Invalid location attribute. (A DataSourceException should be catched) ");
            //			reader=(AbstractGridCoverage2DReader) new ImageMosaicReader(rgbURL, new
            // Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "aaaa"));

            reader =
                    GridFormatFinder.findFormat(rgbURL, hints)
                            .getReader(rgbURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "aaaa"));
            Assert.assertNull(reader);
        } catch (Throwable e) {
            Assert.fail(e.getLocalizedMessage());
        }
        //		try {
        //			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat)
        // GridFormatFinder.findFormat(rgbJarURL)).getReader(rgbJarURL, new
        // Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "aaaa"));
        //			Assert.assertNull(reader);
        //		} catch (Throwable e) {
        //			Assert.fail(e.getLocalizedMessage());
        //		}

        try {
            reader =
                    GridFormatFinder.findFormat(rgbURL, hints)
                            .getReader(
                                    rgbURL, new Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "location"));
            Assert.assertNotNull(reader);
            reader.dispose();
            Assert.assertTrue(true);
        } catch (Throwable e) {
            Assert.fail(e.getLocalizedMessage());
        }

        //		try {
        //			reader=(AbstractGridCoverage2DReader) ((AbstractGridFormat)
        // GridFormatFinder.findFormat(rgbJarURL)).getReader(rgbJarURL, new
        // Hints(Hints.MOSAIC_LOCATION_ATTRIBUTE, "location"));
        //			Assert.assertNotNull(reader);
        //			reader.dispose();
        //			Assert.assertTrue(true);
        //		} catch (Throwable e) {
        //			Assert.fail(e.getLocalizedMessage());
        //		}

        ////
        //
        // MAX_ALLOWED_TILES
        //
        ////
        // error for num tiles
        try {
            reader =
                    GridFormatFinder.findFormat(rgbURL)
                            .getReader(
                                    rgbURL, new Hints(Hints.MAX_ALLOWED_TILES, Integer.valueOf(2)));
            Assert.assertNotNull(reader);

            // read the coverage
            @SuppressWarnings("unused")
            GridCoverage2D gc = reader.read(null);
            Assert.fail("MAX_ALLOWED_TILES was not respected");
        } catch (Throwable e) {

            if (reader != null) reader.dispose();

            Assert.assertTrue(true);
        }

        try {
            reader =
                    GridFormatFinder.findFormat(rgbURL)
                            .getReader(
                                    rgbURL,
                                    new Hints(Hints.MAX_ALLOWED_TILES, Integer.valueOf(1000)));
            Assert.assertNotNull(reader);
            // read the coverage
            GridCoverage2D gc = reader.read(null);
            Assert.assertTrue(true);
            gc.dispose(true);
            reader.dispose();
        } catch (Exception e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    /** Tests the {@link ImageMosaicReader} */
    private GridCoverage2D imageMosaicSimpleParamsTest(
            final URL testURL,
            final Color inputTransparent,
            final Color outputTransparent,
            final String title,
            final boolean blend)
            throws Exception {

        // Get the resources as needed.
        Assert.assertNotNull(testURL);
        final AbstractGridFormat format = TestUtils.getFormat(testURL);
        final ImageMosaicReader reader = getReader(testURL, format);

        // limit yourself to reading just a bit of it
        final ParameterValue<Color> inTransp =
                AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
        inTransp.setValue(inputTransparent);
        final ParameterValue<Color> outTransp =
                ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
        outTransp.setValue(outputTransparent);
        final ParameterValue<Boolean> blendPV = ImageMosaicFormat.FADING.createValue();
        blendPV.setValue(blend);

        // Test the output coverage
        GridCoverage2D gridCoverage2D =
                TestUtils.checkCoverage(
                        reader, new GeneralParameterValue[] {inTransp, blendPV, outTransp}, title);
        reader.dispose();
        return gridCoverage2D;
    }

    /**
     * Shows the provided {@link RenderedImage} ina {@link JFrame} using the provided <code>title
     * </code> as the frame's title.
     *
     * @param image to show.
     * @param title to use.
     */
    static void show(RenderedImage image, String title) {
        ImageIOUtilities.visualize(image, title);
    }

    /**
     * Tests {@link ImageMosaicReader} asking to crop the lower left quarter of the input coverage.
     *
     * @param testURL The location of the source mosaic
     * @param title to use when showing image.
     * @param acceptContainment true if the result is expected to be contained in the crop area,
     *     possibly because the mosaic is sparse
     */
    private void imageMosaicCropTest(URL testURL, String title, boolean acceptContainment)
            throws Exception {

        // Get the resources as needed.
        Assert.assertNotNull(testURL);
        final AbstractGridFormat format = TestUtils.getFormat(testURL);
        final ImageMosaicReader reader = getReader(testURL, format);

        // crop
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelope.getLowerCorner().getOrdinate(0)
                                    + oldEnvelope.getSpan(0) / 2,
                            oldEnvelope.getLowerCorner().getOrdinate(1) + oldEnvelope.getSpan(1) / 2
                        },
                        new double[] {
                            oldEnvelope.getUpperCorner().getOrdinate(0),
                            oldEnvelope.getUpperCorner().getOrdinate(1)
                        });
        cropEnvelope.setCoordinateReferenceSystem(reader.getCoordinateReferenceSystem());
        gg.setValue(
                new GridGeometry2D(
                        PixelInCell.CELL_CENTER,
                        reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),
                        cropEnvelope,
                        null));
        final ParameterValue<Color> outTransp =
                ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
        outTransp.setValue(Color.black);

        final double[] baseResolutions = reader.getResolutionLevels()[0];
        // test the coverage
        final double tolerance = Math.max(baseResolutions[0], baseResolutions[1]) * 10;
        GridCoverage2D coverage =
                TestUtils.checkCoverage(reader, new GeneralParameterValue[] {gg, outTransp}, title);
        // the envelope is the requested one
        if (acceptContainment) {
            assertContainsEnvelope(cropEnvelope, coverage.getEnvelope(), tolerance);
        } else {
            assertEnvelope(cropEnvelope, coverage.getEnvelope(), tolerance);
        }
        // the raster space ordinates are not far away from the origin
        RenderedImage ri = coverage.getRenderedImage();
        assertEquals(0, ri.getMinX(), 10);
        assertEquals(0, ri.getMinY(), 10);

        reader.dispose();
    }

    void assertEnvelope(Envelope expected, Envelope actual, double tolerance) {
        assertEquals(expected.getMinimum(0), actual.getMinimum(0), tolerance);
        assertEquals(expected.getMaximum(0), actual.getMaximum(0), tolerance);
        assertEquals(expected.getMinimum(1), actual.getMinimum(1), tolerance);
        assertEquals(expected.getMaximum(1), actual.getMaximum(1), tolerance);
    }

    void assertContainsEnvelope(Envelope expected, Envelope contained, double tolerance) {
        assertTrue(expected.getMinimum(0) < contained.getMinimum(0) + tolerance);
        assertTrue(expected.getMaximum(0) > contained.getMaximum(0) - tolerance);
        assertTrue(expected.getMinimum(1) < contained.getMinimum(1) + tolerance);
        assertTrue(expected.getMaximum(1) > contained.getMaximum(1) - tolerance);
    }

    @Test
    public void testRequestInHoleNoData() throws Exception {
        // create the base mosaic we are going to use
        File mosaicSource = TestData.file(this, "rgba");
        File targetRgba = new File("target", "rgba");
        FileUtils.deleteQuietly(targetRgba);
        FileUtils.copyDirectory(mosaicSource, targetRgba);
        // remove leftover files from other tests
        Arrays.stream(
                        targetRgba.listFiles(
                                (f, n) -> n.startsWith("rgba") || n.startsWith("sample_image")))
                .forEach(f -> f.delete());
        URL testMosaicUrl = fileToUrl(targetRgba);

        // setup the indexer with the nodata
        Properties properties = new Properties();
        properties.put(Prop.NO_DATA, "0");
        try (FileOutputStream fos =
                new FileOutputStream(new File(targetRgba, "indexer.properties"))) {
            properties.store(fos, null);
        }

        GridCoverage2D coverage = testMosaicHoleOn(testMosaicUrl);
        assertNoData(coverage, 0d);
    }

    private GridCoverage2D testMosaicHoleOn(URL testMosaicUrl)
            throws FactoryException, IOException {
        final ImageMosaicReader reader = getReader(testMosaicUrl);

        // ask to extract an area that is inside the coverage bbox, but in a hole (no data)
        final ParameterValue<GridGeometry2D> ggp =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        Envelope2D env =
                new Envelope2D(reader.getCoordinateReferenceSystem(), 500000, 3200000, 1000, 1000);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 100, 100), (Envelope) env);
        ggp.setValue(gg);

        // red background
        final ParameterValue<double[]> bgp = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bgp.setValue(new double[] {255, 0, 0, 255});

        // read and check we actually got a coverage in the requested area
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggp, bgp});
        assertNotNull(coverage);
        assertTrue(coverage.getEnvelope2D().intersects((Rectangle2D) env));

        // and that the color is the expected one given the background values provided
        int[] pixel = new int[4];
        coverage.evaluate(new Point2D.Double(497987, 3197819), pixel);
        assertEquals(255, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        assertEquals(255, pixel[3]);

        reader.dispose();
        return coverage;
    }

    @Test
    public void testRequestInHole() throws Exception {
        GridCoverage2D coverage = testMosaicHoleOn(rgbAURL);
        assertNull(CoverageUtilities.getNoDataProperty(coverage));
    }

    @Test
    public void testBlankResponseWithBandSelection() throws FactoryException, IOException {
        File source = URLs.urlToFile(rgbURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "rgbMosaicSource");
        File directory2 = new File(testDataDir, "rgbMosaicBandSelect");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // remove all mosaic related files
        for (File file : FileUtils.listFiles(directory1, new RegexFileFilter("rgb.*"), null)) {
            assertTrue(file.delete());
        }
        // Copy 2 not adjacent files (therefore creating void area between them)
        directory2.mkdirs();
        for (File file :
                FileUtils.listFiles(
                        directory1,
                        new OrFileFilter(
                                new RegexFileFilter("global_mosaic_10.*"),
                                new RegexFileFilter("global_mosaic_12.*")),
                        null)) {
            file.renameTo(new File(directory2, file.getName()));
        }

        final URL testUrl = URLs.fileToUrl(directory2);
        final ImageMosaicReader reader = getReader(testUrl);

        // ask to extract an area that is inside the coverage bbox, but in a hole (no data)
        final ParameterValue<GridGeometry2D> ggp =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        Envelope2D env = new Envelope2D(reader.getCoordinateReferenceSystem(), 10, 41, 1, 1);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 50, 50), (Envelope) env);
        ggp.setValue(gg);

        // Select 2 bands
        final ParameterValue<int[]> bands = ImageMosaicFormat.BANDS.createValue();
        bands.setValue(new int[] {0, 2});

        // Set a custom background values for the 3 original bands
        final ParameterValue<double[]> bgp = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bgp.setValue(new double[] {255, 127, 64});

        // read and check we actually got a coverage in the requested area
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggp, bgp, bands});
        assertNotNull(coverage);
        assertTrue(coverage.getEnvelope2D().intersects((Rectangle2D) env));

        // Since we have requested a void area (no data within the mosaic's definition area)
        // check that the returned image respects the number of components from band selection
        RenderedImage ri = coverage.getRenderedImage();
        ColorModel cm = ri.getColorModel();
        SampleModel sm = ri.getSampleModel();
        assertEquals(2, cm.getNumComponents());
        assertEquals(2, sm.getNumBands());

        // Check the constant values
        int[] pixel = new int[2];
        ri.getData().getPixel(0, 0, pixel);
        assertEquals(255, pixel[0]);
        assertEquals(64, pixel[1]);

        reader.dispose();
    }

    @Test
    // @Ignore
    public void testRequestInOut() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(rgbAURL, null);
        final ImageMosaicReader reader = getReader(rgbAURL, format);

        assertNotNull(reader);

        // ask to extract an area that is inside the coverage bbox, so that the area is partly
        // inside the raster, and partly outside
        final ParameterValue<GridGeometry2D> ggp =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        Envelope2D env =
                new Envelope2D(
                        reader.getCoordinateReferenceSystem(),
                        44887,
                        2299342,
                        646897 - 44887,
                        3155705 - 2299342);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 100, 100), (Envelope) env);
        ggp.setValue(gg);

        // red background
        final ParameterValue<double[]> bgp = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bgp.setValue(new double[] {255, 0, 0, 255});

        // read and check we actually got a coverage in the requested area
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggp, bgp});
        assertNotNull(coverage);
        final Envelope2D envelope2d = coverage.getEnvelope2D();
        assertTrue(envelope2d.contains((Rectangle2D) env));

        // and that the color is the expected one given the background values provided
        int[] pixel = new int[4];
        coverage.evaluate(new Point2D.Double(430000, 2700000), pixel);
        assertEquals(255, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        assertEquals(255, pixel[3]);

        reader.dispose();
    }

    @Test
    public void testRequestInAreaWithNoGranulesBecomesTransparent() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(rgbURL);
        final ImageMosaicReader reader = getReader(rgbURL, format);
        try {
            assertNotNull(reader);

            // ask to extract an area that is inside the coverage bbox, but it doesn't cover any
            // granule.
            // the output should be transparent
            final ParameterValue<GridGeometry2D> ggp =
                    AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            Envelope2D env = new Envelope2D(reader.getCoordinateReferenceSystem(), 19, 45, 1, 1);
            GridGeometry2D gg =
                    new GridGeometry2D(new GridEnvelope2D(0, 0, 50, 50), (Envelope) env);
            ggp.setValue(gg);

            // Setting transparency
            final ParameterValue<Color> transparent =
                    ImageMosaicFormat.INPUT_TRANSPARENT_COLOR.createValue();
            transparent.setValue(new Color(0, 0, 0));

            // read and check we actually got a coverage in the requested area
            GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggp, transparent});
            assertNotNull(coverage);
            assertTrue(coverage.getEnvelope2D().contains((Rectangle2D) env));

            int[] pixel =
                    new int[] {
                        Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE
                    };
            coverage.evaluate(new Point2D.Double(20, 45), pixel);
            assertEquals(0, pixel[0]);
            assertEquals(0, pixel[1]);
            assertEquals(0, pixel[2]);
            // We only have input RGB granules.
            // The mosaic should have been added the alpha component.
            // Moreover it should have been set to fully transparent (0)
            // since no granules are available in the requested area.
            assertEquals(0, pixel[3]);
        } finally {
            reader.dispose();
        }
    }

    @BeforeClass
    public static void init() {

        // make sure CRS ordering is correct
        CRS.reset("all");
        System.setProperty("org.geotools.referencing.forceXY", "true");
        System.setProperty("user.timezone", "GMT");
        System.setProperty("org.geotools.shapefile.datetime", "true");
        INTERACTIVE = TestData.isInteractiveTest();
    }

    @Before
    public void setUp() throws Exception {
        // remove generated file

        cleanUp();

        rgbURL = TestData.url(this, "rgb");
        mixedSampleModelURL = TestData.url(this, "mixed_sample_model");
        coverageBandsURL = TestData.url(this, "coverage_bands");
        heterogeneousGranulesURL = TestData.url(this, "heterogeneous");
        timeURL = TestData.url(this, "time_geotiff");
        timeFormatURL = TestData.url(this, "time_format_geotiff");
        timeAdditionalDomainsURL = TestData.url(this, "time_additionaldomains");
        timeAdditionalDomainsRangeURL = TestData.url(this, "time_domainsRanges");
        timeRangesURL = TestData.url(this, "time_ranges");
        overviewURL = TestData.url(this, "overview/");
        rgbAURL = TestData.url(this, "rgba/");
        oneBitURL = TestData.url(this, "onebit/");

        indexURL = TestData.url(this, "index/");
        index2URL = TestData.url(this, "index_palette_2/");
        indexAlphaURL = TestData.url(this, "index_alpha/");

        grayURL = TestData.url(this, "gray/");

        index_unique_paletteAlphaURL = TestData.url(this, "index_alpha_unique_palette/");

        imposedEnvelopeURL = TestData.url(this, "env");
        rgbAURLTiff = TestData.url(this, "tiff_rgba/");
        rgbaExtraURLTiff = TestData.url(this, "tiff_rgba_extra/");
    }

    /** Cleaning up the generated files (shape and properties so that we recreate them. */
    private void cleanUp() throws Exception {
        if (INTERACTIVE) return;
        File dir = TestData.file(this, "overview/");
        File[] files =
                dir.listFiles(
                        (FilenameFilter)
                                FileFilterUtils.notFileFilter(
                                        FileFilterUtils.or(
                                                FileFilterUtils.or(
                                                        FileFilterUtils.suffixFileFilter("tif"),
                                                        FileFilterUtils.suffixFileFilter("aux")),
                                                FileFilterUtils.nameFileFilter(
                                                        "datastore.properties"))));
        for (File file : files) {
            file.delete();
        }

        dir = TestData.file(this, "rgba/");
        files =
                dir.listFiles(
                        (FilenameFilter)
                                FileFilterUtils.notFileFilter(
                                        FileFilterUtils.or(
                                                FileFilterUtils.notFileFilter(
                                                        FileFilterUtils.suffixFileFilter("png")),
                                                FileFilterUtils.notFileFilter(
                                                        FileFilterUtils.suffixFileFilter("wld")))));
        for (File file : files) {
            file.delete();
        }

        dir = TestData.file(this, "time_domainsRanges");
        files =
                dir.listFiles(
                        (FilenameFilter)
                                FileFilterUtils.or(
                                        FileFilterUtils.suffixFileFilter("shp"),
                                        FileFilterUtils.suffixFileFilter("dbf"),
                                        FileFilterUtils.suffixFileFilter("qix"),
                                        FileFilterUtils.suffixFileFilter("shx"),
                                        FileFilterUtils.suffixFileFilter("prj")));
        for (File file : files) {
            file.delete();
        }
    }

    @After
    public void tearDown() throws Exception {
        cleanUp();
    }

    /** Simple test method accessing time and 2 custom dimensions for the sample dataset */
    @Test
    // @Ignore
    @SuppressWarnings("rawtypes")
    public void timeAdditionalDimNoResultsDueToWrongDim() throws Exception {

        final AbstractGridFormat format = TestUtils.getFormat(timeAdditionalDomainsURL);
        ImageMosaicReader reader = getReader(timeAdditionalDomainsURL, format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 18);
        assertEquals("true", reader.getMetadataValue("HAS_DATE_DOMAIN"));
        assertEquals("20081031T0000000,20081101T0000000", reader.getMetadataValue("DATE_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue("DATE_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_DEPTH_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("20,100", reader.getMetadataValue("DEPTH_DOMAIN"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("DEPTH_DOMAIN_DATATYPE"));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Date timeD = formatD.parse("2008-10-31T00:00:00.000Z");
        time.setValue(
                new ArrayList() {
                    {
                        add(timeD);
                    }
                });

        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> dateValue = null;
        ParameterValue<List<String>> depthValue = null;
        final String selectedWaveLength = "030";
        final String selectedDate = "20081031T0000000";
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("DATE")) {
                dateValue = param.createValue();
                dateValue.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedDate);
                            }
                        });
            } else if (param.getName().getCode().equalsIgnoreCase("DEPTH")) {
                depthValue = param.createValue();
                depthValue.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedWaveLength);
                            }
                        });
            }
        }
        assertNotNull(depthValue);
        assertNotNull(dateValue);
        // Test the output coverage
        GeneralParameterValue[] values =
                new GeneralParameterValue[] {useJai, time, dateValue, depthValue};
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, false);
        assertNull(coverage);

        reader.dispose();
    }

    /** Simple test method accessing time and 2 custom dimensions for the sample dataset */
    @Test
    // @Ignore
    @SuppressWarnings("rawtypes")
    public void multipleDimensionsStackedSar() throws Exception {

        final URL sourceURL = TestData.file(this, "merge").toURI().toURL();
        final AbstractGridFormat format = TestUtils.getFormat(sourceURL);
        ImageMosaicReader reader = getReader(sourceURL, format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(15, metadataNames.length);
        assertEquals("false", reader.getMetadataValue("HAS_POLARIZ_DOMAIN"));
        assertEquals("true", reader.getMetadataValue("HAS_POLARIZATION_DOMAIN"));
        assertEquals(
                "POLARIZATION",
                reader.getDynamicParameters().iterator().next().getName().getCode());
        assertEquals(
                "HH,HV,VH,VV",
                reader.getMetadataValue(
                        "POLARIZATION_DOMAIN")); // ten characters limitation overcome!
        assertEquals("java.lang.String", reader.getMetadataValue("POLARIZATION_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("2012-01-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN"));
        assertEquals("2012-01-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals("2012-01-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);
        final ParameterValue<String> tileSize =
                AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        tileSize.setValue("128,128");

        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Date timeD = formatD.parse("2012-01-01T00:00:00.000Z");
        time.setValue(
                new ArrayList() {
                    {
                        add(timeD);
                    }
                });

        // specify additional Dimensions
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        ParameterValue<List<String>> polariz = null;
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("POLARIZATION")) {
                polariz = param.createValue();
                polariz.setValue(
                        new ArrayList<String>() {
                            {
                                add("HH");
                                add("HV");
                                add("VV");
                            }
                        });
            }
        }

        // Stacked bands
        final ParameterValue<String> paramStacked = ImageMosaicFormat.MERGE_BEHAVIOR.createValue();
        paramStacked.setValue(MergeBehavior.STACK.toString());

        // Test the output coverage
        GeneralParameterValue[] values =
                new GeneralParameterValue[] {useJai, tileSize, time, polariz, paramStacked};
        final GridCoverage2D coverage = TestUtils.getCoverage(reader, values, false);
        assertNotNull(coverage);

        // inspect reanderedImage
        final RenderedImage image = coverage.getRenderedImage();
        assertEquals("wrong number of bands detected", 3, image.getSampleModel().getNumBands());
        assertEquals(DataBuffer.TYPE_SHORT, image.getSampleModel().getDataType());

        reader.dispose();
    }

    @Test
    public void testHarvestSingleFile() throws Exception {
        File source = URLs.urlToFile(timeURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "singleHarvest1");
        File directory2 = new File(testDataDir, "singleHarvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // remove all files besides month 2 and 5
        for (File file :
                FileUtils.listFiles(
                        directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            assertTrue(file.delete());
        }
        // remove all mosaic related files
        for (File file :
                FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }
        // move month 5 to another dir, we'll harvet it later
        String monthFiveName = "world.200405.3x5400x2700.tiff";
        File monthFive = new File(directory1, monthFiveName);
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        File renamed = new File(directory2, monthFiveName);
        assertTrue(monthFive.renameTo(renamed));

        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GranuleCatalog originalCatalog = reader.granuleCatalog;
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));

            // now go and harvest the other file
            List<HarvestedSource> summary = reader.harvest(null, renamed, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertEquals(renamed.getCanonicalFile(), ((File) hf.getSource()).getCanonicalFile());
            assertTrue(hf.success());

            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));

            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            assertEquals(2, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            SimpleFeatureIterator fi = granules.getGranules(q).features();
            try {
                assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();
                assertEquals("world.200402.3x5400x2700.tiff", f.getAttribute("location"));
                assertEquals(
                        "2004-02-01T00:00:00.000Z",
                        ConvertersHack.convert(f.getAttribute("time"), String.class));
                f = fi.next();
                String expected =
                        "../singleHarvest2/world.200405.3x5400x2700.tiff"
                                .replace('/', File.separatorChar);
                assertEquals(expected, f.getAttribute("location"));
                assertEquals(
                        "2004-05-01T00:00:00.000Z",
                        ConvertersHack.convert(f.getAttribute("time"), String.class));
            } finally {
                fi.close();
            }

        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testHarvestSpatial() throws Exception {
        File source = URLs.urlToFile(rgbURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "rgbHarvest1");
        File directory2 = new File(testDataDir, "rgbHarvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // remove all mosaic related files
        for (File file : FileUtils.listFiles(directory1, new RegexFileFilter("rgb.*"), null)) {
            assertTrue(file.delete());
        }
        // move all files except global_mosaic_0 to the second dir
        directory2.mkdirs();
        for (File file :
                FileUtils.listFiles(
                        directory1, new RegexFileFilter("global_mosaic_[^0].*"), null)) {
            assertTrue(file.renameTo(new File(directory2, file.getName())));
        }

        // crate a mosaic
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GeneralEnvelope singleGranuleEnvelope = reader.getOriginalEnvelope();
        // System.out.println(singleGranuleEnvelope);

        // now push back all the files, and harvest them
        for (File file : directory2.listFiles()) {
            assertTrue(file.renameTo(new File(directory1, file.getName())));
        }
        reader.harvest(null, directory1, null);

        // the envelope should have been updated
        GeneralEnvelope fullEnvelope = reader.getOriginalEnvelope();
        assertTrue(fullEnvelope.contains(singleGranuleEnvelope, true));
        assertTrue(fullEnvelope.getSpan(0) > singleGranuleEnvelope.getSpan(0));
        assertTrue(fullEnvelope.getSpan(1) > singleGranuleEnvelope.getSpan(1));

        // make a request in a bbox that's outside of the original envelope
        MathTransform mt = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        Envelope env = new Envelope2D(DefaultGeographicCRS.WGS84, 10, 40, 15, 45);
        GridEnvelope2D rasterEnvelope =
                new GridEnvelope2D(
                        new Envelope2D(CRS.transform(mt.inverse(), env)), PixelInCell.CELL_CORNER);
        GridGeometry2D gg = new GridGeometry2D(rasterEnvelope, env);
        final ParameterValue<GridGeometry2D> ggParameter =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParameter.setValue(gg);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggParameter});
        assertNotNull(coverage);
        coverage.dispose(true);

        // remove all the granules on the east side
        GranuleStore store = (GranuleStore) reader.getGranules(null, false);
        store.removeGranules(
                ECQL.toFilter(
                        "location = 'global_mosaic_19.png' "
                                + "OR location = 'global_mosaic_14.png' "
                                + "OR location = 'global_mosaic_9.png' "
                                + "OR location = 'global_mosaic_4.png'"));

        GeneralEnvelope reducedEnvelope = reader.getOriginalEnvelope();
        assertTrue(fullEnvelope.contains(reducedEnvelope, true));
        assertTrue(reducedEnvelope.contains(singleGranuleEnvelope, true));
        assertTrue(fullEnvelope.getSpan(0) > reducedEnvelope.getSpan(0));
        assertEquals(fullEnvelope.getSpan(1), reducedEnvelope.getSpan(1), 0d);

        reader.dispose();
    }

    @Test
    public void testHarvestSpatialTwoReaders() throws Exception {
        File source = URLs.urlToFile(rgbURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "rgbHarvest1_tr");
        File directory2 = new File(testDataDir, "rgbHarvest2_tr");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // remove all mosaic related files
        for (File file : FileUtils.listFiles(directory1, new RegexFileFilter("rgb.*"), null)) {
            assertTrue(file.delete());
        }
        // move all files except global_mosaic_0 to the second dir
        directory2.mkdirs();
        for (File file :
                FileUtils.listFiles(
                        directory1, new RegexFileFilter("global_mosaic_[^0].*"), null)) {
            assertTrue(file.renameTo(new File(directory2, file.getName())));
        }

        // crate the first reader
        URL harvestSingleURL = URLs.fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GeneralEnvelope singleGranuleEnvelope = reader.getOriginalEnvelope();
        // System.out.println(singleGranuleEnvelope);

        // now create a second reader that won't be informed of the harvesting changes
        // (simulating changes over a cluster, where the bbox information won't be updated from one
        // node to the other)
        ImageMosaicReader reader2 = getReader(harvestSingleURL, format);

        // harvest the other files with the first reader
        for (File file : directory2.listFiles()) {
            assertTrue(file.renameTo(new File(directory1, file.getName())));
        }
        reader.harvest(null, directory1, null);

        // make a request in a bbox that's outside of the original envelope, the
        // second reader does not have the metadata updated, but can still respond to the
        // request
        MathTransform mt = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        Envelope env = new Envelope2D(DefaultGeographicCRS.WGS84, 10, 40, 15, 45);
        GridEnvelope2D rasterEnvelope =
                new GridEnvelope2D(
                        new Envelope2D(CRS.transform(mt.inverse(), env)), PixelInCell.CELL_CORNER);
        GridGeometry2D gg = new GridGeometry2D(rasterEnvelope, env);
        final ParameterValue<GridGeometry2D> ggParameter =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParameter.setValue(gg);
        GridCoverage2D coverage = reader2.read(new GeneralParameterValue[] {ggParameter});
        assertNotNull(coverage);
        coverage.dispose(true);

        reader.dispose();
        reader2.dispose();
    }

    @Test
    public void testHarvestSingleFileRGBA() throws Exception {
        File source = URLs.urlToFile(rgbAURLTiff);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "singleHarvestRGBA1");
        File directory2 = new File(testDataDir, "singleHarvestRGBA2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // remove all mosaic related files
        for (File file : FileUtils.listFiles(directory1, new RegexFileFilter("rgba.*"), null)) {
            assertTrue(file.delete());
        }
        // move this file to another dir, we'll harvest it later
        String fileNameToMove = "passA2006128211927.tiff";
        File monthFive = new File(directory1, fileNameToMove);
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        File renamed = new File(directory2, fileNameToMove);
        assertTrue(monthFive.renameTo(renamed));

        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GranuleCatalog originalCatalog = reader.granuleCatalog;
        try {

            // now go and harvest the other file
            List<HarvestedSource> summary = reader.harvest(null, renamed, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertEquals(renamed.getCanonicalFile(), ((File) hf.getSource()).getCanonicalFile());
            assertTrue(hf.success());

            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            assertEquals(2, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            SimpleFeatureIterator fi = granules.getGranules(q).features();
            try {
                assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();
                assertEquals("passA2006128194218.tiff", f.getAttribute("location"));
                f = fi.next();
                String expected =
                        "../singleHarvestRGBA2/passA2006128211927.tiff"
                                .replace('/', File.separatorChar);
                assertEquals(expected, f.getAttribute("location"));
            } finally {
                fi.close();
            }
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testRenamedMosaicGranuleSource() throws Exception {
        File source = URLs.urlToFile(rgbAURLTiff);
        File testDataDir = TestData.file(this, ".");
        File mosaicDirectory = new File(testDataDir, "singleHarvestRGBA1");
        File storageDirectory = new File(testDataDir, "singleHarvestRGBA2");
        if (mosaicDirectory.exists()) {
            FileUtils.deleteDirectory(mosaicDirectory);
        }
        FileUtils.copyDirectory(source, mosaicDirectory);
        // remove all mosaic related files
        for (File file :
                FileUtils.listFiles(mosaicDirectory, new RegexFileFilter("rgba.*"), null)) {
            assertTrue(file.delete());
        }
        // create an indexer to rename the mosaic and type names
        Properties indexer = new Properties();
        indexer.put(Prop.NAME, "rgba");
        indexer.put(Utils.Prop.TYPENAME, "theTable");
        try (FileOutputStream fos =
                new FileOutputStream(new File(mosaicDirectory, "indexer.properties"))) {
            indexer.store(fos, null);
        }
        // for this to work we need a datastore.properties (a shapefile cannot contain a typename
        // other than its
        // name, but the name of the store is fixed to match the one of the coverage)
        // place H2 file in the dir
        try (FileWriter out = new FileWriter(new File(mosaicDirectory, "/datastore.properties"))) {
            out.write("database=imagemosaicremove\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        // move this file to another dir, we'll harvest it later
        String fileNameToMove = "passA2006128211927.tiff";
        File monthFive = new File(mosaicDirectory, fileNameToMove);
        if (storageDirectory.exists()) {
            FileUtils.deleteDirectory(storageDirectory);
        }
        storageDirectory.mkdirs();
        File renamed = new File(storageDirectory, fileNameToMove);
        assertTrue(monthFive.renameTo(renamed));

        // ok, let's create a mosaic with a single granule and check the granule source name
        URL harvestSingleURL = fileToUrl(mosaicDirectory);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GranuleSource gs = reader.getGranules("rgba", true);
        assertEquals("rgba", gs.getSchema().getTypeName());

        // this used to blow things out
        SimpleFeatureCollection features = gs.getGranules(new Query("rgba"));
        SimpleFeature first = DataUtilities.first(features);
        assertEquals("rgba", first.getType().getTypeName());

        // another variant that used to cause problems
        Query q = new Query("rgba");
        q.setPropertyNames(new String[] {"location"});
        features = gs.getGranules(q);
        first = DataUtilities.first(features);
        assertEquals("rgba", first.getType().getTypeName());
        assertEquals(1, first.getAttributes().size());

        reader.dispose();
    }

    @Test
    public void testHarvestMultipleFiles() throws Exception {
        File source = URLs.urlToFile(timeURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "harvest1");
        File directory2 = new File(testDataDir, "harvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        // Creation of a File Collection
        Collection<File> files = new ArrayList<File>();

        // move all files besides month 2 and 5 to the second directory and store them into a
        // Collection
        for (File file :
                FileUtils.listFiles(
                        directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            File renamed = new File(directory2, file.getName());
            assertTrue(file.renameTo(renamed));
            files.add(renamed);
        }
        // remove all mosaic related files
        for (File file :
                FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }

        // ok, let's create a mosaic with the two original granules
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GranuleCatalog originalCatalog = reader.granuleCatalog;
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));

            // now go and harvest the other directory
            List<HarvestedSource> summary = reader.harvest(null, files, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(2, summary.size());
            for (HarvestedSource hf : summary) {
                assertTrue(hf.success());
            }

            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testHarvestPalette() throws Exception {
        File source = URLs.urlToFile(index2URL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "mosaic");
        File directory2 = new File(testDataDir, "singleHarvest");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        for (File file :
                FileUtils.listFiles(directory1, FileFilterUtils.prefixFileFilter("c"), null)) {
            assertTrue(file.delete());
        }

        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        FileUtils.copyDirectory(source, directory2);
        for (File file :
                FileUtils.listFiles(
                        directory2,
                        FileFilterUtils.notFileFilter(FileFilterUtils.prefixFileFilter("c")),
                        null)) {
            assertTrue(file.delete());
        }

        // ok, let's create a mosaic
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL, null);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GranuleCatalog originalCatalog = reader.granuleCatalog;
        try {
            // now go and harvest the other files
            Collection<File> files = new ArrayList<File>();
            for (File file :
                    FileUtils.listFiles(directory2, FileFilterUtils.prefixFileFilter("c"), null)) {
                files.add(file);
            }

            List<HarvestedSource> summary = reader.harvest(null, files, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertTrue(hf.success());
            RasterManager manager = reader.getRasterManager(reader.getGridCoverageNames()[0]);

            // Palette should have been successfully loaded
            assertNotNull(manager.defaultPalette);
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testHarvestDirectory() throws Exception {
        File source = URLs.urlToFile(timeURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "harvest1");
        File directory2 = new File(testDataDir, "harvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        // move all files besides month 2 and 5 to the second directory
        for (File file :
                FileUtils.listFiles(
                        directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            File renamed = new File(directory2, file.getName());
            assertTrue(file.renameTo(renamed));
        }
        // remove all mosaic related files
        for (File file :
                FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }

        // ok, let's create a mosaic with the two original granules
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GranuleCatalog originalCatalog = reader.granuleCatalog;
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));

            // now go and harvest the other directory
            List<HarvestedSource> summary = reader.harvest(null, directory2, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(2, summary.size());
            for (HarvestedSource hf : summary) {
                assertTrue(hf.success());
            }

            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testHarvestListSingleDirectory() throws Exception {
        File source = URLs.urlToFile(timeURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "harvest1");
        File directory2 = new File(testDataDir, "harvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        // move all files besides month 2 and 5 to the second directory
        for (File file :
                FileUtils.listFiles(
                        directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            File renamed = new File(directory2, file.getName());
            assertTrue(file.renameTo(renamed));
        }
        // remove all mosaic related files
        for (File file :
                FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }

        // Create a List of Files containing only the directory to harvest and check if the Reader
        // reads it as a Directory
        List<File> files = new ArrayList<File>();
        files.add(directory2);

        // ok, let's create a mosaic with the two original granules
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GranuleCatalog originalCatalog = reader.granuleCatalog;
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));

            // now go and harvest the file list
            List<HarvestedSource> summary = reader.harvest(null, files, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(2, summary.size());
            for (HarvestedSource hf : summary) {
                assertTrue(hf.success());
            }

            // the harvest put the files in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testHarvestListSingleFileRelative() throws Exception {
        Function<File, String> expectedLocation1 = d -> "world.200402.3x5400x2700.tiff";
        Function<File, String> expectedLocation2 =
                d ->
                        "../singleHarvest2/world.200405.3x5400x2700.tiff"
                                .replace('/', File.separatorChar);
        Consumer<File> mosaicDirSetup = (dir) -> {};

        checkSingleFileHarvest(mosaicDirSetup, expectedLocation1, expectedLocation2);
    }

    @Test
    public void testHarvestListSingleFileAbsoluteLegacy() throws Exception {
        // setup a case with absolute paths based on "directory1"
        Function<File, String> expectedLocation1 =
                d -> new File(d, "world.200402.3x5400x2700.tiff").getAbsolutePath();
        Function<File, String> expectedLocation2 =
                d -> {
                    File pf = d.getParentFile();
                    return new File(pf, "singleHarvest2/world.200405.3x5400x2700.tiff")
                            .getAbsolutePath();
                };
        Consumer<File> mosaicDirSetup =
                (dir) -> {
                    File indexer = new File(dir, "indexer.properties");
                    try {
                        String indexerContents = FileUtils.readFileToString(indexer, "UTF-8");
                        indexerContents += "AbsolutePath=true\n";
                        FileUtils.writeStringToFile(indexer, indexerContents, "UTF-8");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                };

        checkSingleFileHarvest(mosaicDirSetup, expectedLocation1, expectedLocation2);
    }

    @Test
    public void testHarvestListSingleFileAbsolutePathType() throws Exception {
        // setup a case with absolute paths based on "directory1"
        Function<File, String> expectedLocation1 =
                d -> new File(d, "world.200402.3x5400x2700.tiff").getAbsolutePath();
        Function<File, String> expectedLocation2 =
                d ->
                        new File(d.getParentFile(), "singleHarvest2/world.200405.3x5400x2700.tiff")
                                .getAbsolutePath();
        Consumer<File> mosaicDirSetup =
                (dir) -> {
                    File indexer = new File(dir, "indexer.properties");
                    try {
                        String indexerContents = FileUtils.readFileToString(indexer, "UTF-8");
                        indexerContents += "PathType=ABSOLUTE\n";
                        FileUtils.writeStringToFile(indexer, indexerContents, "UTF-8");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                };

        checkSingleFileHarvest(mosaicDirSetup, expectedLocation1, expectedLocation2);
    }

    private void checkSingleFileHarvest(
            Consumer<File> mosaicDirSetup,
            Function<File, String> expectedLocation1,
            Function<File, String> expectedLocation2)
            throws IOException, FactoryException {
        File source = URLs.urlToFile(timeURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "singleHarvest1");
        File directory2 = new File(testDataDir, "singleHarvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // remove all files besides month 2 and 5
        for (File file :
                FileUtils.listFiles(
                        directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            assertTrue(file.delete());
        }
        // remove all mosaic related files
        for (File file :
                FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }
        // move month 5 to another dir, we'll harvest it later
        String monthFiveName = "world.200405.3x5400x2700.tiff";
        File monthFive = new File(directory1, monthFiveName);
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        File renamed = new File(directory2, monthFiveName);
        assertTrue(monthFive.renameTo(renamed));

        // setup the mosaic directory with extra files, if needed
        mosaicDirSetup.accept(directory1);

        // Create a List of Files containing only the directory to harvest and check if the Reader
        // reads it as a Directory
        List<File> files = new ArrayList<>();
        files.add(renamed);

        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GranuleCatalog originalCatalog = reader.granuleCatalog;
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));

            // now go and harvest the other file
            List<HarvestedSource> summary = reader.harvest(null, files, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertEquals(renamed.getCanonicalFile(), ((File) hf.getSource()).getCanonicalFile());
            assertTrue(hf.success());

            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));

            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            assertEquals(2, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            SimpleFeatureIterator fi = granules.getGranules(q).features();
            try {
                assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();

                assertEquals(expectedLocation1.apply(directory1), f.getAttribute("location"));
                assertEquals(
                        "2004-02-01T00:00:00.000Z",
                        ConvertersHack.convert(f.getAttribute("time"), String.class));
                f = fi.next();
                assertEquals(expectedLocation2.apply(directory2), f.getAttribute("location"));
                assertEquals(
                        "2004-05-01T00:00:00.000Z",
                        ConvertersHack.convert(f.getAttribute("time"), String.class));
            } finally {
                fi.close();
            }
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testRemoveCoverageNoDelete() throws Exception {

        final String referenceDir = "testRemove";
        final File workDir = new File(TestData.file(this, "."), referenceDir);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        final File zipFile = new File(workDir, "watertemp.zip");
        FileUtils.copyFile(TestData.file(this, "watertemp.zip"), zipFile);

        TestData.unzipFile(this, referenceDir + "/watertemp.zip");
        final URL timeElevURL = TestData.url(this, referenceDir);

        // place H2 file in the dir
        try (FileWriter out =
                new FileWriter(
                        new File(
                                TestData.file(this, "."),
                                referenceDir + "/datastore.properties"))) {
            out.write("database=imagemosaicremove\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(reader);

        try {

            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            File[] files = workDir.listFiles();
            assertNotNull(files);
            assertEquals(16, files.length);

            reader.removeCoverage(reader.getGridCoverageNames()[0], false);
            assertEquals(0, reader.getGridCoverageNames().length);
            assertEquals(16, files.length);
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testRemoveCoverageDelete() throws Exception {

        final String referenceDir = "testRemove2";
        final File workDir = new File(TestData.file(this, "."), referenceDir);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        final File zipFile = new File(workDir, "watertemp.zip");
        FileUtils.copyFile(TestData.file(this, "watertemp.zip"), zipFile);

        TestData.unzipFile(this, referenceDir + "/watertemp.zip");
        final URL timeElevURL = TestData.url(this, referenceDir);

        // place H2 file in the dir
        try (FileWriter out =
                new FileWriter(
                        new File(
                                TestData.file(this, "."),
                                referenceDir + "/datastore.properties"))) {
            out.write("database=imagemosaicremove2\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(reader);

        try {
            assertEquals(1, reader.getGridCoverageNames().length);
            File[] files = workDir.listFiles();
            assertNotNull(files);
            assertEquals(16, files.length);

            reader.removeCoverage(reader.getGridCoverageNames()[0], true);
            assertEquals(0, reader.getGridCoverageNames().length);
            files = workDir.listFiles();
            assertEquals(12, files.length);

        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testReaderDeleteAll() throws Exception {

        final String referenceDir = "testRemove3";
        final File workDir = new File(TestData.file(this, "."), referenceDir);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        final File zipFile = new File(workDir, "watertemp.zip");
        FileUtils.copyFile(TestData.file(this, "watertemp.zip"), zipFile);

        TestData.unzipFile(this, referenceDir + "/watertemp.zip");
        FileUtils.deleteQuietly(new File(workDir + "/watertemp.zip"));
        final URL timeElevURL = TestData.url(this, referenceDir);

        // place H2 file in the dir
        try (FileWriter out =
                new FileWriter(
                        new File(
                                TestData.file(this, "."),
                                referenceDir + "/datastore.properties"))) {
            out.write("database=imagemosaicremove3\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);

        StructuredGridCoverage2DReader reader = null;
        try {

            // the harvest put the file in the same coverage
            reader = new ImageMosaicReader(timeElevURL);

            // delete all files associated to that mosaic (granules, auxiliary files, DB entries,
            // ...)
            File[] files = workDir.listFiles();
            assertEquals(15, files.length);
            reader.delete(true);
            files = workDir.listFiles();
            assertEquals(0, files.length);
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testReaderDeleteNoGranules() throws Exception {

        final String referenceDir = "testRemove4";
        final File workDir = new File(TestData.file(this, "."), referenceDir);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        final File zipFile = new File(workDir, "watertemp.zip");
        FileUtils.copyFile(TestData.file(this, "watertemp.zip"), zipFile);

        TestData.unzipFile(this, referenceDir + "/watertemp.zip");
        FileUtils.deleteQuietly(new File(workDir + "/watertemp.zip"));
        final URL timeElevURL = TestData.url(this, referenceDir);

        // place H2 file in the dir
        try (FileWriter out =
                new FileWriter(
                        new File(
                                TestData.file(this, "."),
                                referenceDir + "/datastore.properties"))) {
            out.write("database=imagemosaicremove4\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }
        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);

        StructuredGridCoverage2DReader reader = null;
        try {

            reader = new ImageMosaicReader(timeElevURL);
            assertNotNull(reader);

            // delete metadata only (auxiliary files, DB entries, ...)
            File[] files = workDir.listFiles();
            assertEquals(15, files.length);
            reader.delete(false);
            files = workDir.listFiles();
            assertEquals(4, files.length);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    @Test
    public void testHarvestError() throws Exception {
        File source = URLs.urlToFile(timeURL);
        File testDataDir = TestData.file(this, ".");
        File directory = new File(testDataDir, "harvest-error");
        if (directory.exists()) {
            FileUtils.deleteDirectory(directory);
        }
        FileUtils.copyDirectory(source, directory);
        // remove all files besides month 2
        for (File file :
                FileUtils.listFiles(
                        directory, new RegexFileFilter("world\\.20040[^2].*\\.tiff"), null)) {
            assertTrue(file.delete());
        }
        // remove all mosaic related files
        for (File file :
                FileUtils.listFiles(directory, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }

        // ok, let's create a mosaic with the original granule
        URL harvestSingleURL = fileToUrl(directory);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GranuleCatalog originalCatalog = reader.granuleCatalog;
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));

            // now go and try to make it harvest an invalid file
            File bogus = new File(directory, "test.tiff");
            assertTrue(bogus.createNewFile());
            List<HarvestedSource> summary = reader.harvest(null, bogus, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertFalse(hf.success());
            assertEquals("test.tiff", ((File) hf.getSource()).getName());
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testHarvestWithExternalMosaicDir() throws Exception {

        File source = URLs.urlToFile(timeURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "externalindex");
        File directory2 = new File(testDataDir, "singleHarvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // remove all files besides month 2 and 5
        for (File file :
                FileUtils.listFiles(
                        directory1, new RegexFileFilter("world\\.20040[^25].*\\.tiff"), null)) {
            assertTrue(file.delete());
        }
        // remove all mosaic related files
        for (File file :
                FileUtils.listFiles(directory1, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }

        // Editing indexer RootMosaicDirectory path
        InputStream stream = null;
        OutputStream outStream = null;
        try {

            final String indexerPath = directory1.getCanonicalPath() + "/indexer.properties";
            stream = new FileInputStream(indexerPath);
            String path = directory1.getCanonicalPath();
            path = path.replace("\\", "/");
            Properties prop = new Properties();
            prop.load(stream);

            outStream = new FileOutputStream(indexerPath);
            prop.setProperty(Prop.ROOT_MOSAIC_DIR, path);
            prop.store(outStream, null);
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        }
        // move month 5 to another dir, we'll harvet it later
        String monthFiveName = "world.200405.3x5400x2700.tiff";
        File monthFive = new File(directory1, monthFiveName);
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        File renamed = new File(directory2, monthFiveName);
        assertTrue(monthFive.renameTo(renamed));

        // ok, let's create a mosaic with a single granule and check its times
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        GranuleCatalog originalCatalog = reader.granuleCatalog;
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals("2004-02-01T00:00:00.000Z", reader.getMetadataValue(metadataNames[0]));

            // now go and harvest the other file
            List<HarvestedSource> summary = reader.harvest(null, renamed, null);
            assertSame(originalCatalog, reader.granuleCatalog);
            assertEquals(1, summary.size());
            HarvestedSource hf = summary.get(0);
            assertEquals(renamed.getCanonicalFile(), ((File) hf.getSource()).getCanonicalFile());
            assertTrue(hf.success());

            // the harvest put the file in the same coverage
            assertEquals(1, reader.getGridCoverageNames().length);
            metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));

            // check the granule catalog
            String coverageName = reader.getGridCoverageNames()[0];
            GranuleSource granules = reader.getGranules(coverageName, true);
            assertEquals(2, granules.getCount(Query.ALL));
            Query q = new Query(Query.ALL);
            SimpleFeatureIterator fi = granules.getGranules(q).features();
            try {
                assertTrue(fi.hasNext());
                SimpleFeature f = fi.next();
                assertEquals("world.200402.3x5400x2700.tiff", f.getAttribute("location"));
                assertEquals(
                        "2004-02-01T00:00:00.000Z",
                        ConvertersHack.convert(f.getAttribute("time"), String.class));
                f = fi.next();
                String expected =
                        "../singleHarvest2/world.200405.3x5400x2700.tiff"
                                .replace('/', File.separatorChar);
                assertEquals(expected, f.getAttribute("location"));
                assertEquals(
                        "2004-05-01T00:00:00.000Z",
                        ConvertersHack.convert(f.getAttribute("time"), String.class));
            } finally {
                fi.close();
            }

        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testSetupExternalMosaicDir() throws Exception {
        File source = URLs.urlToFile(timeURL);
        File testDataDir = TestData.file(this, ".");
        File data = new File(testDataDir, "externaldata");
        File mosaic = new File(testDataDir, "mosaicexternal");
        if (data.exists()) {
            FileUtils.deleteDirectory(data);
        }
        FileUtils.copyDirectory(source, data);
        if (mosaic.exists()) {
            FileUtils.deleteDirectory(mosaic);
        }
        mosaic.mkdirs();
        // get rid of pre-configured data
        for (File file : FileUtils.listFiles(data, new RegexFileFilter("time_geotiff.*"), null)) {
            assertTrue(file.delete());
        }
        // move the indexer config files into the mosaic direcotry
        for (File file : FileUtils.listFiles(data, new RegexFileFilter(".*\\.properties"), null)) {
            File moved = new File(mosaic, file.getName());
            assertTrue(file.renameTo(moved));
        }

        // Editing indexer RootMosaicDirectory path
        InputStream stream = null;
        OutputStream outStream = null;
        try {
            final File indexer = new File(mosaic, "indexer.properties");
            stream = new FileInputStream(indexer);
            Properties prop = new Properties();
            prop.load(stream);

            outStream = new FileOutputStream(indexer);
            prop.setProperty(Prop.INDEXING_DIRECTORIES, data.getCanonicalPath());
            prop.store(outStream, null);
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        }

        // ok, let's create the mosaic and check it harvested the data in the "data" directory
        URL mosaicURL = fileToUrl(mosaic);
        final AbstractGridFormat format = TestUtils.getFormat(mosaicURL);
        ImageMosaicReader reader = getReader(mosaicURL, format);
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);
            assertEquals(metadataNames.length, 12);
            assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
            assertEquals(
                    "2004-05-01T00:00:00.000Z", reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
            assertEquals(
                    "2004-02-01T00:00:00.000Z,2004-03-01T00:00:00.000Z,2004-04-01T00:00:00.000Z,2004-05-01T00:00:00.000Z",
                    reader.getMetadataValue(metadataNames[0]));
            assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));
        } finally {
            reader.dispose();
        }
    }

    @Test
    @Ignore
    public void oracle() throws Exception {
        final File workDir = TestData.file(this, "wattemp");

        final AbstractGridFormat format = TestUtils.getFormat(workDir.toURI().toURL());
        assertNotNull(format);
        ImageMosaicReader reader = getReader(workDir.toURI().toURL(), format);
        assertNotNull(format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 18);

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
        assertNotNull(timeMetadata);
        assertEquals(2, timeMetadata.split(",").length);
        assertEquals(timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals(timeMetadata.split(",")[1], reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("java.util.Date", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_DAT_DOMAIN"));
        assertEquals("20081031T0000000,20081101T0000000", reader.getMetadataValue("DAT_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue("DAT_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue("HAS_DEPTH_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_ELEVATION_DOMAIN"));
        assertEquals("false", reader.getMetadataValue("HAS_XX_DOMAIN"));
        assertEquals("20,100", reader.getMetadataValue("DEPTH_DOMAIN"));
        assertEquals("java.lang.Integer", reader.getMetadataValue("DEPTH_DOMAIN_DATATYPE"));

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final List<Date> timeValues = new ArrayList<Date>();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = sdf.parse("2008-10-31T00:00:00.000Z");
        timeValues.add(date);
        time.setValue(timeValues);

        final ParameterValue<Boolean> direct = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        direct.setValue(false);

        final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bkg.setValue(new double[] {-9999.0});

        ParameterValue<List<String>> dateValue = null;
        ParameterValue<List<String>> depthValue = null;
        final String selectedWaveLength = "020";
        final String selectedDate = "20081031T0000000";
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters();
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("DAT")) {
                dateValue = param.createValue();
                dateValue.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedDate);
                            }
                        });
            } else if (param.getName().getCode().equalsIgnoreCase("DEPTH")) {
                depthValue = param.createValue();
                depthValue.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedWaveLength);
                            }
                        });
            }
        }
        // Test the output coverage
        TestUtils.checkCoverage(
                reader,
                new GeneralParameterValue[] {gg, bkg, direct, depthValue, dateValue},
                "oracle Test");

        reader.dispose();
    }

    @Test
    public void testExistingSchema() throws Exception {

        final File workDir = new File(TestData.file(this, "."), "water_temp4");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, "water_temp4/watertemp.zip");
        final URL timeElevURL = TestData.url(this, "water_temp4");

        // now start the test
        AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(reader);

        reader.dispose();

        // append the parameter to the indexer.properties
        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), "/water_temp4/indexer.properties"),
                        true)) {
            out.write("UseExistingSchema=true\n");
            out.flush();
        }

        // remove existing properties file and sample_image
        File sampleImage =
                new File(TestData.file(this, "."), "/water_temp4/" + Utils.SAMPLE_IMAGE_NAME);
        assertTrue(sampleImage.exists());
        sampleImage.delete();
        File mosaicProperties =
                new File(TestData.file(this, "."), "/water_temp4/water_temp4.properties");
        assertTrue(mosaicProperties.exists());
        mosaicProperties.delete();

        // now start the test
        format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        reader = getReader(timeElevURL, format);
        assertNotNull(reader);

        // the mosaic is correctly created
        assertTrue(sampleImage.exists());
        assertTrue(mosaicProperties.exists());

        // clean up
        reader.dispose();
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, "water_temp4"));
        }
    }

    @Test
    public void testUserProvidedName() throws Exception {
        final File workDir = new File(TestData.file(this, "."), "water_temp5");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, "water_temp5/watertemp.zip");
        final URL timeElevURL = TestData.url(this, "water_temp5");

        // force the name
        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), "/water_temp5/indexer.properties"),
                        true)) {
            out.write("Name=test\n");
            out.flush();
        }

        // now start the test
        AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(reader);

        // the mosaic is correctly created
        File sampleImage =
                new File(TestData.file(this, "."), "/water_temp5/" + Utils.SAMPLE_IMAGE_NAME);
        File mosaicProperties = new File(TestData.file(this, "."), "/water_temp5/test.properties");
        assertTrue(sampleImage.exists());
        assertTrue(mosaicProperties.exists());

        // clean up
        reader.dispose();
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, "water_temp5"));
        }
    }

    @Test
    @Ignore
    public void testExistingOracleSchema() throws Exception {

        final String folder = "mosaictemp";
        final String zipFile = "mosaictemp.zip";
        final File workDir = new File(TestData.file(this, "."), folder);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(TestData.file(this, zipFile), new File(workDir, zipFile));
        TestData.unzipFile(this, folder + File.separatorChar + zipFile);
        final URL timeElevURL = fileToUrl(workDir);
        //

        // now start the test
        AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(reader);

        reader.dispose();

        // append the parameter to the indexer.properties
        try (FileWriter out =
                new FileWriter(
                        new File(
                                TestData.file(this, "."),
                                folder + File.separatorChar + "indexer.properties"),
                        true)) {
            out.write("UseExistingSchema=true\n");
            out.flush();
        }

        // remove existing properties file and sample_image
        File sampleImage =
                new File(
                        TestData.file(this, "."),
                        folder + File.separatorChar + Utils.SAMPLE_IMAGE_NAME);
        File mosaicProperties =
                new File(
                        TestData.file(this, "."),
                        folder + File.separatorChar + folder + ".properties");

        // now start the test
        format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        reader = getReader(timeElevURL, format);
        assertNotNull(reader);

        // the mosaic is correctly created
        assertTrue(sampleImage.exists());
        assertTrue(mosaicProperties.exists());

        // clean up
        reader.dispose();
        if (!INTERACTIVE) {
            FileUtils.deleteDirectory(TestData.file(this, folder));
        }
    }

    @Test
    public void testPAMAuxiliaryFiles() throws Exception {
        final URL timePamURL = TestData.url(this, "pam");

        final AbstractGridFormat format = TestUtils.getFormat(timePamURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timePamURL, format);
        assertNotNull(format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);

        // use imageio with defined tiles
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final List<Date> timeValues = new ArrayList<Date>();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = sdf.parse("2008-10-31T00:00:00.000Z");
        timeValues.add(date);
        time.setValue(timeValues);

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {time});
        Object object = coverage.getProperty(Utils.PAM_DATASET);
        assertNotNull(object);
        assertTrue(object instanceof PAMDataset);
        PAMDataset dataset = (PAMDataset) object;
        PAMRasterBand band = dataset.getPAMRasterBand().get(0);

        PAMParser parser = PAMParser.getInstance();
        assertEquals(
                0, Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MINIMUM")), DELTA);
        assertEquals(
                255.0,
                Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MAXIMUM")),
                DELTA);
        assertEquals(
                73.0352,
                Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MEAN")),
                DELTA);
        assertEquals(
                84.3132,
                Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_STDDEV")),
                DELTA);

        reader.dispose();
    }

    @Test
    public void testPAMMerged() throws Exception {
        final URL timePamURL = TestData.url(this, "pam");

        final AbstractGridFormat format = TestUtils.getFormat(timePamURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timePamURL, format);
        assertNotNull(format);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);

        GridCoverage2D coverage = reader.read(null);
        Object object = coverage.getProperty(Utils.PAM_DATASET);
        assertNotNull(object);
        assertTrue(object instanceof PAMDataset);
        PAMDataset dataset = (PAMDataset) object;
        PAMRasterBand band = dataset.getPAMRasterBand().get(0);

        PAMParser parser = PAMParser.getInstance();
        assertEquals(
                0, Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MINIMUM")), DELTA);
        assertEquals(
                255.0,
                Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MAXIMUM")),
                DELTA);
        assertEquals(
                72.6912,
                Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_MEAN")),
                DELTA);
        assertEquals(
                83.2542,
                Double.parseDouble(parser.getMetadataValue(band, "STATISTICS_STDDEV")),
                DELTA);

        reader.dispose();
    }

    @Test
    public void testStopCreationWhileWalkingMosaicDir() throws Exception {

        final File workDir = new File(TestData.file(this, "."), "stop-it");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(
                TestData.file(this, "watertemp.zip"), new File(workDir, "watertemp.zip"));
        TestData.unzipFile(this, "stop-it/watertemp.zip");
        final URL timeElevURL = TestData.url(this, "stop-it");

        // place H2 file in the dir
        try (FileWriter out =
                new FileWriter(
                        new File(TestData.file(this, "."), "/stop-it/datastore.properties"))) {
            out.write("database=imagemosaic\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        // now start the test
        final AbstractGridFormat format = TestUtils.getFormat(timeElevURL);
        assertNotNull(format);
        ImageMosaicReader reader = getReader(timeElevURL, format);
        assertNotNull(reader);

        final String[] metadataNames = reader.getMetadataNames();
        assertNotNull(metadataNames);
        assertEquals(metadataNames.length, 12);

        // Getting some metadata
        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
        assertNotNull(timeMetadata);
        assertEquals(2, timeMetadata.split(",").length);
        assertEquals(timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals(timeMetadata.split(",")[1], reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));
        assertEquals("java.sql.Timestamp", reader.getMetadataValue("TIME_DOMAIN_DATATYPE"));

        // Disposing the reader before recreating the mosaic
        reader.dispose();

        // Deleting mosaic files so that the mosaic will be created again
        File mosaicFile = new File(TestData.file(this, "."), "/stop-it/stop-it.properties");
        mosaicFile.delete();
        File sampleImageFile =
                new File(TestData.file(this, "."), "/stop-it/" + Utils.SAMPLE_IMAGE_NAME);
        sampleImageFile.delete();

        // Since we have deleted some mosaic files but we didn't cleanup the DB tables
        // the reader should recreate a new mosaic. However the walker should fail and
        // stop while creating the DB tables (they already exist),  returning back no readers.
        reader = (ImageMosaicReader) format.getReader(timeElevURL);

        // No reader should have been created.
        assertNull(reader);
    }

    /**
     * Tests {@link ImageMosaicReader} asking to crop the lower left quarter of the input coverage.
     *
     * @param title to use when showing image.
     */
    @Test
    public void testExpandToRGB() throws Exception {

        // Get the resources as needed.
        URL testURL = TestData.url(this, "index_palette/");
        Assert.assertNotNull(testURL);

        File currentDir = URLs.urlToFile(testURL);

        // Get the reader
        final AbstractGridFormat format = TestUtils.getFormat(testURL);

        // Read the Directory
        ImageMosaicReader reader = getReader(testURL, format);

        // test the coverage
        GridCoverage2D coverage = reader.read(null);

        // Check that the coverage has an IndexColormodel
        assertTrue(coverage.getRenderedImage().getColorModel() instanceof IndexColorModel);

        // Ensure the properties file has been generated
        File props = new File(currentDir, "index_palette.properties");
        assertTrue(props.exists() && props.canRead() && props.canWrite());

        // Getting the written properties
        String properties = FileUtils.readFileToString(props, "UTF-8");

        // Ensure the ExpandToRGB property is set
        assertTrue(properties.contains("ExpandToRGB"));

        // Replace the ExpandToRGB property if set to false
        properties = properties.replace("ExpandToRGB=false", "ExpandToRGB=true");

        // Write it on the file
        FileUtils.write(props, properties, "UTF-8", false);

        // Read the Directory again
        reader = getReader(testURL, format);

        // test the coverage
        coverage = reader.read(null);

        // Check that the coverage has a component Colormodel
        assertTrue(coverage.getRenderedImage().getColorModel() instanceof ComponentColorModel);

        // Remove all the auxiliary files if present
        IOFileFilter prefixFileFilter = FileFilterUtils.prefixFileFilter("index_palette");
        IOFileFilter nameFileFilter = FileFilterUtils.nameFileFilter(Utils.SAMPLE_IMAGE_NAME);
        FileFilter ff = FileFilterUtils.or(prefixFileFilter, nameFileFilter);

        File[] listFiles = currentDir.listFiles(ff);
        if (listFiles != null && listFiles.length > 0) {
            for (File f : listFiles) {
                FileUtils.deleteQuietly(f);
            }
        }
        reader.dispose();
    }

    @Test
    public void testExpandToRGBBandSelection() throws Exception {
        // Delete test folder if present
        final File workDir = new File(TestData.file(this, "."), "index_palette_bandselect");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }

        File mosaicSource = TestData.file(this, "index_palette");
        FileUtils.copyDirectory(mosaicSource, workDir);
        URL testURL = fileToUrl(workDir);

        // grab the reader to force mosaic config creation
        final AbstractGridFormat format = TestUtils.getFormat(testURL);
        ImageMosaicReader reader = getReader(testURL, format);
        reader.dispose();

        // enable palette expansion
        File props = new File(workDir, "index_palette_bandselect.properties");
        assertTrue(props.exists() && props.canRead() && props.canWrite());
        String properties = FileUtils.readFileToString(props, "UTF-8");
        assertTrue(properties.contains("ExpandToRGB"));
        properties = properties.replace("ExpandToRGB=false", "ExpandToRGB=true");
        FileUtils.write(props, properties, "UTF-8", false);

        // grab the reader again
        reader = getReader(testURL, format);

        // prepare band selection
        ParameterValue<int[]> selectedBands = AbstractGridFormat.BANDS.createValue();
        selectedBands.setValue(new int[] {2});
        GridCoverage2D coverage =
                TestUtils.checkCoverage(reader, new GeneralParameterValue[] {selectedBands}, null);

        // Check that the coverage has a component Colormodel
        final RenderedImage ri = coverage.getRenderedImage();
        assertTrue(ri.getColorModel() instanceof ComponentColorModel);
        assertEquals(1, ri.getSampleModel().getNumBands());
        reader.dispose();
    }

    /**
     * Tests {@link ImageMosaicReader} when the native CRS differs from the requested CRS only
     * because of metadata. Test case uses 3857 data bt request with 900913
     */
    @Test
    public void testSameCRS() throws Exception {
        final String strangeWGS84 =
                "PROJCS[\"Google Mercator\", "
                        + "  GEOGCS[\"WGS 84\", "
                        + "    DATUM[\"World Geodetic System 1984\", "
                        + "      SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], "
                        + "      AUTHORITY[\"EPSG\",\"6326\"]], "
                        + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], "
                        + "    UNIT[\"degree\", 0.017453292519943295], "
                        + "    AXIS[\"Geodetic latitude\", NORTH], "
                        + "    AXIS[\"Geodetic longitude\", EAST], "
                        + "    AUTHORITY[\"EPSG\",\"4326\"]], "
                        + "  PROJECTION[\"Mercator (1SP)\", AUTHORITY[\"EPSG\",\"9804\"]], "
                        + "  PARAMETER[\"semi_major\", 6378137.0], "
                        + "  PARAMETER[\"semi_minor\", 6378137.0], "
                        + "  PARAMETER[\"latitude_of_origin\", 0.0], "
                        + "  PARAMETER[\"central_meridian\", 0.0], "
                        + "  PARAMETER[\"scale_factor\", 1.0], "
                        + "  PARAMETER[\"false_easting\", 0.0], "
                        + "  PARAMETER[\"false_northing\", 0.0], "
                        + "  UNIT[\"m\", 1.0], "
                        + "  AXIS[\"Easting\", EAST], "
                        + "  AXIS[\"Northing\", NORTH], "
                        + "  AUTHORITY[\"EPSG\",\"900913\"]]";

        // Get the resources as needed.
        URL testURL = TestData.url(this, "same_crs/");
        Assert.assertNotNull(testURL);

        // Get the reader
        final AbstractGridFormat format = TestUtils.getFormat(testURL, null);
        Assert.assertNotNull(format);
        Assert.assertFalse(format instanceof UnknownFormat);

        // Read the Directory
        ImageMosaicReader reader = getReader(testURL, format);

        // create the same BBOX with strange CRS
        final GeneralEnvelope targetBBOX = new GeneralEnvelope(reader.getOriginalEnvelope());
        targetBBOX.setCoordinateReferenceSystem(CRS.parseWKT(strangeWGS84));

        // create the GridGeometry
        final ParameterValue<GridGeometry2D> readGG =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        readGG.setValue(new GridGeometry2D(reader.getOriginalGridRange(), targetBBOX));

        // Test the output coverage
        TestUtils.checkCoverage(reader, new GeneralParameterValue[] {readGG}, "Test Same CRS");

        // test the coverage
        reader.dispose();
    }

    /**
     * Tests the {@link ImageMosaicReader} with external overviews (Actually only TIFF is supported)
     */
    @Test
    public void externalOverviews() throws Exception {
        // Delete test folder if present
        final File workDir = new File(TestData.file(this, "."), "mosaic-ext-ovr");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        // Populate again the test folder
        FileUtils.copyDirectory(TestData.file(this, "ext-overview"), workDir);

        // create url from file
        URL dirURL = fileToUrl(workDir);
        final AbstractGridFormat format = TestUtils.getFormat(dirURL);
        final ImageMosaicReader reader = getReader(dirURL, format);

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        final ParameterValue<String> tileSize =
                AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        tileSize.setValue("128,128");

        // Test the output coverage
        GeneralParameterValue[] values = new GeneralParameterValue[] {gg, useJai, tileSize};
        final GridCoverage2D coverage =
                TestUtils.checkCoverage(reader, values, "external overviews test");

        // Checking Overview Path
        Object fileLocation =
                coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        assertNotNull(fileLocation);
        assertTrue(fileLocation instanceof String);
        String path = (String) fileLocation;
        assertTrue(!path.isEmpty());
        assertTrue(path.endsWith(".ovr"));
        reader.dispose();
    }

    @Test
    public void testInitFromExistingStore() throws Exception {
        File testMosaic = new File(TestData.file(this, "."), "existingStore");
        if (testMosaic.exists()) {
            FileUtils.deleteDirectory(testMosaic);
        }
        File mosaicSource = TestData.file(this, "rgb");
        FileUtils.copyDirectory(mosaicSource, testMosaic);

        // create the base mosaic we are going to use
        cleanConfigurationFiles(testMosaic, "rgb");
        URL testMosaicUrl = fileToUrl(testMosaic);

        // place H2 file in the dir
        File dataStoreProperties = new File(testMosaic, "datastore.properties");
        try (FileWriter out = new FileWriter(dataStoreProperties)) {
            out.write("database=imagemosaic\n");
            out.write(H2_SAMPLE_PROPERTIES);
            out.flush();
        }

        // force its initialization the "normal" way
        final AbstractGridFormat format = TestUtils.getFormat(testMosaicUrl);
        final ImageMosaicReader reader = getReader(testMosaicUrl, format);
        reader.dispose();

        // cleanup configuration, test image, and so on
        cleanConfigurationFiles(testMosaic, "existingStore");

        // rename the table
        Properties h2Connection = new Properties();
        try (FileReader fr = new FileReader(dataStoreProperties)) {
            h2Connection.load(fr);
        }
        h2Connection.put("database", new File(testMosaic, "imagemosaic").getCanonicalPath());
        JDBCDataStore store = (JDBCDataStore) DataStoreFinder.getDataStore(h2Connection);
        try (Connection c = store.getConnection(Transaction.AUTO_COMMIT);
                Statement st = c.createStatement()) {
            st.execute("ALTER TABLE \"existingStore\" RENAME TO \"testMosaic\"");
            st.execute("UPDATE GEOMETRY_COLUMNS SET F_TABLE_NAME = 'testMosaic'");
        }
        store.dispose();

        // force it to use the existing schema
        Properties indexer = new Properties();
        indexer.put(Utils.Prop.USE_EXISTING_SCHEMA, "true");
        indexer.put(Utils.Prop.TYPENAME, "testMosaic");
        try (FileOutputStream fos =
                new FileOutputStream(new File(testMosaic, "indexer.properties"))) {
            indexer.store(fos, null);
        }

        // now read again, see if the config gets read properly
        final ImageMosaicReader reader2 = getReader(testMosaicUrl, format);
        GridCoverage2D coverage = reader2.read(null);
        coverage.dispose(true);
        reader2.dispose();
    }

    @Test
    public void testMixedSampleModels() throws Exception {
        File mosaicFolder = URLs.urlToFile(mixedSampleModelURL);
        cleanConfigurationFiles(mosaicFolder, mosaicFolder.getName());
        final AbstractGridFormat format = TestUtils.getFormat(mixedSampleModelURL);
        ImageMosaicReader reader = getReader(mixedSampleModelURL, format);

        GridCoverage2D coverage = reader.read(null);
        assertNotNull(coverage);
        RenderedImage ri = coverage.getRenderedImage();
        assertThat(ri.getSampleModel(), instanceOf(ComponentSampleModel.class));
        assertThat(ri.getColorModel(), instanceOf(ComponentColorModel.class));

        File sample =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/mixed-mosaic.png");
        // RenderedImageBrowser.showChain(coverage.getRenderedImage());
        ImageAssert.assertEquals(sample, ri, 100);
        coverage.dispose(true);

        // check the color models of small areas, it should be the one of the one granule
        // involved in the mosaic
        checkColorModel(
                IndexColorModel.class,
                1,
                DataBuffer.TYPE_BYTE,
                new ReferencedEnvelope(10, 10.1, 43, 43.1, DefaultGeographicCRS.WGS84),
                reader);
        checkColorModel(
                IndexColorModel.class,
                1,
                DataBuffer.TYPE_BYTE,
                new ReferencedEnvelope(13.5, 13.6, 43.5, 43.6, DefaultGeographicCRS.WGS84),
                reader);
        checkColorModel(
                ComponentColorModel.class,
                1,
                DataBuffer.TYPE_BYTE,
                new ReferencedEnvelope(8, 8.1, 45.5, 45.6, DefaultGeographicCRS.WGS84),
                reader);
        checkColorModel(
                ComponentColorModel.class,
                1,
                DataBuffer.TYPE_USHORT,
                new ReferencedEnvelope(10.5, 10.6, 45.5, 45.6, DefaultGeographicCRS.WGS84),
                reader);
        checkColorModel(
                ComponentColorModel.class,
                3,
                DataBuffer.TYPE_BYTE,
                new ReferencedEnvelope(13.5, 13.6, 45.5, 45.6, DefaultGeographicCRS.WGS84),
                reader);

        // check larger ares for combinations of tiles
        // ... gray 8 bit and gray 16 bit
        checkColorModel(
                ComponentColorModel.class,
                1,
                DataBuffer.TYPE_USHORT,
                new ReferencedEnvelope(8, 10, 45, 46, DefaultGeographicCRS.WGS84),
                reader);
        // ... gray 8 bit and RGB
        checkColorModel(
                ComponentColorModel.class,
                3,
                DataBuffer.TYPE_BYTE,
                new ReferencedEnvelope(7, 8, 43, 45, DefaultGeographicCRS.WGS84),
                reader);
        // ... gray 16 bit and RGB
        checkColorModel(
                ComponentColorModel.class,
                3,
                DataBuffer.TYPE_BYTE,
                new ReferencedEnvelope(11, 13, 45, 46, DefaultGeographicCRS.WGS84),
                reader);
        // ... gray 16 bit and indexed
        checkColorModel(
                ComponentColorModel.class,
                3,
                DataBuffer.TYPE_BYTE,
                new ReferencedEnvelope(10, 11, 43, 45, DefaultGeographicCRS.WGS84),
                reader);
        // ... RGB and indexed
        checkColorModel(
                ComponentColorModel.class,
                3,
                DataBuffer.TYPE_BYTE,
                new ReferencedEnvelope(7, 11, 43, 44, DefaultGeographicCRS.WGS84),
                reader);

        reader.dispose();
    }

    @Test
    public void testCoverageOnBands() throws Exception {
        File mosaicFolder = URLs.urlToFile(coverageBandsURL);
        for (File configFile :
                mosaicFolder.listFiles(
                        (FileFilter)
                                FileFilterUtils.or(
                                        FileFilterUtils.suffixFileFilter("db"),
                                        FileFilterUtils.suffixFileFilter(Utils.SAMPLE_IMAGE_NAME),
                                        FileFilterUtils.and(
                                                FileFilterUtils.suffixFileFilter(".properties"),
                                                FileFilterUtils.notFileFilter(
                                                        FileFilterUtils.or(
                                                                FileFilterUtils.nameFileFilter(
                                                                        "indexer.properties"),
                                                                FileFilterUtils.nameFileFilter(
                                                                        "datastore.properties"))))))) {
            configFile.delete();
        }
        AbstractGridFormat format = TestUtils.getFormat(coverageBandsURL);
        ImageMosaicReader reader = getReader(coverageBandsURL, format);

        testMultiCoverages(reader);
        reader.dispose();

        // Double check. Read it again after the mosaic configuration
        // has been created
        format = TestUtils.getFormat(coverageBandsURL);
        reader = getReader(coverageBandsURL, format);
        testMultiCoverages(reader);
        reader.dispose();
    }

    private void testMultiCoverages(ImageMosaicReader reader) throws IOException {
        String[] coverageNames = reader.getGridCoverageNames();
        Arrays.sort(coverageNames);
        assertNotNull(coverageNames);
        int coverageCount = coverageNames.length;
        assertEquals(2, coverageCount);
        String[] expectedNames = new String[] {"gray", "rgb"};
        int[] expectedTypes = new int[] {ColorSpace.TYPE_GRAY, ColorSpace.TYPE_RGB};
        for (int i = 0; i < coverageCount; i++) {
            String coverageName = coverageNames[i];
            assertEquals(expectedNames[i], coverageName);
            GridCoverage2D coverage = reader.read(coverageNames[i], null);
            assertNotNull(coverage);
            RenderedImage ri = coverage.getRenderedImage();
            assertThat(ri.getSampleModel(), instanceOf(ComponentSampleModel.class));
            ColorModel cm = ri.getColorModel();
            assertThat(cm, instanceOf(ComponentColorModel.class));
            assertEquals(expectedTypes[i], cm.getColorSpace().getType());
        }
    }

    private void checkColorModel(
            Class<? extends ColorModel> clazz,
            int bands,
            int dataType,
            ReferencedEnvelope box,
            ImageMosaicReader reader)
            throws NoninvertibleTransformException, TransformException, IOException {
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        MathTransform mt = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        GeneralEnvelope ge = CRS.transform(mt.inverse(), box);
        GridEnvelope2D range = new GridEnvelope2D(new Envelope2D(ge), PixelInCell.CELL_CENTER);
        gg.setValue(new GridGeometry2D(range, mt, box.getCoordinateReferenceSystem()));

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {gg});
        RenderedImage ri = coverage.getRenderedImage();
        // RenderedImageBrowser.showChain(ri);
        assertThat(ri.getColorModel(), instanceOf(clazz));
        assertEquals(bands, ri.getSampleModel().getNumBands());
        assertEquals(dataType, ri.getSampleModel().getDataType());
        coverage.dispose(true);
    }

    private void cleanConfigurationFiles(File testMosaic, String mosaicName) {
        new File(testMosaic, Utils.SAMPLE_IMAGE_NAME).delete();
        for (File configFile :
                testMosaic.listFiles((FileFilter) FileFilterUtils.prefixFileFilter(mosaicName))) {
            configFile.delete();
        }
    }

    @AfterClass
    public static void close() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    /**
     * Test if empty mosaic can be read and granules can be added and read
     *
     * @author Hendrik Peilke
     */
    @Test
    public void testEmptyShapefileMosaic() throws Exception {
        // get some test data
        final File testMosaic = TestData.file(this, "/empty_mosaic/empty_mosaic.shp");
        assertTrue(testMosaic.exists());

        ImageMosaicReader reader = new ImageMosaicFormat().getReader(testMosaic);

        // remove cached granules on error and reload
        reader.granuleCatalog.removeGranules(new Query("empty_mosaic", Filter.INCLUDE));
        reader.dispose();
        reader = new ImageMosaicFormat().getReader(testMosaic);

        // manager should have an empty bbox
        final RasterManager manager = reader.getRasterManager(reader.getGridCoverageNames()[0]);
        assertTrue(manager.spatialDomainManager.coverageBBox.isEmpty());

        // reading the mosaic with its own envelope (should be empty, so the request will be emtpy)
        // this should return an empty coverage
        ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {gg});
        assertNull(coverage);

        // read without parameters, should also give null, since the bbox of the coverage is used
        coverage = reader.read(null);
        assertNull(coverage);

        // use more complex parameters and own bbox --> should also return null coverage
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);
        final ParameterValue<String> tileSize =
                AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        tileSize.setValue("128,128");
        final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bkg.setValue(new double[] {-9999.0});
        gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        Envelope2D env = new Envelope2D(reader.getCoordinateReferenceSystem(), 0, 0, 1000, 1000);
        GridGeometry2D gg2D =
                new GridGeometry2D(new GridEnvelope2D(0, 0, 100, 100), (Envelope) env);
        gg.setValue(gg2D);
        coverage = reader.read(new GeneralParameterValue[] {bkg, gg, useJai, tileSize});
        assertNull(coverage);

        // now add a granule, reinitialize and test the opposite...
        SimpleFeatureType granuleType =
                reader.granuleCatalog.getType(reader.granuleCatalog.getTypeNames()[0]);
        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder sFB = new SimpleFeatureBuilder(granuleType);
        SimpleFeature f = sFB.buildFeature(null);
        f.setAttribute("location", "addedGranule.tif");
        LinearRing shell =
                gf.createLinearRing(
                        new Coordinate[] {
                            new Coordinate(0, 0),
                            new Coordinate(0, 5903),
                            new Coordinate(5662, 5903),
                            new Coordinate(5662, 0),
                            new Coordinate(0, 0)
                        });
        f.setDefaultGeometry(gf.createPolygon(shell));
        List<SimpleFeature> granules = new LinkedList<SimpleFeature>();
        granules.add(f);
        reader.granuleCatalog.addGranules("empty_mosaic", granules, null);
        manager.initialize(false);

        // manager should now have no empty bbox
        assertFalse(manager.spatialDomainManager.coverageBBox.isEmpty());

        // read without parameters, should give back the whole coverage
        coverage = reader.read(null);
        assertNotNull(coverage);
        assertNotNull(coverage.getRenderedImage());
        coverage.dispose(true);

        // use more complex parameters and own bbox --> should also return a coverage
        coverage = reader.read(new GeneralParameterValue[] {bkg, gg, useJai, tileSize});
        assertNotNull(coverage);
        assertNotNull(coverage.getRenderedImage());
        coverage.dispose(true);

        // now remove granule, reinitialize and test the first tests again
        reader.granuleCatalog.removeGranules(new Query("empty_mosaic", Filter.INCLUDE));
        manager.initialize(false);

        // manager should have an empty bbox
        assertTrue(manager.spatialDomainManager.coverageBBox.isEmpty());

        // read without parameters, should give back a null coverage
        coverage = reader.read(null);
        assertNull(coverage);

        // use more complex parameters and own bbox --> should also return a null coverage
        coverage = reader.read(new GeneralParameterValue[] {bkg, gg, useJai, tileSize});
        assertNull(coverage);

        reader.dispose();
    }

    /**
     * Test if empty mosaic with caching can be read
     *
     * @author Hendrik Peilke
     */
    @Test
    public void testEmptyShapefileMosaicWithCaching() throws Exception {
        // get some test data
        final File testMosaic = TestData.file(this, "/empty_mosaic/empty_mosaic_with_caching.shp");
        assertTrue(testMosaic.exists());

        final ImageMosaicReader reader = new ImageMosaicFormat().getReader(testMosaic);

        // manager should have an empty bbox
        final RasterManager manager = reader.getRasterManager(reader.getGridCoverageNames()[0]);
        assertTrue(manager.spatialDomainManager.coverageBBox.isEmpty());

        // reading the mosaic with its own envelope (should be empty, so the request will be emtpy)
        // this should return an empty coverage
        ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {gg});
        assertNull(coverage);

        // read without parameters, should also give null, since the bbox of the coverage is used
        coverage = reader.read(null);
        assertNull(coverage);

        // use more complex parameters and own bbox
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);
        final ParameterValue<String> tileSize =
                AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
        tileSize.setValue("128,128");
        final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bkg.setValue(new double[] {-9999.0});
        gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        Envelope2D env = new Envelope2D(reader.getCoordinateReferenceSystem(), 0, 0, 1000, 1000);
        GridGeometry2D gg2D =
                new GridGeometry2D(new GridEnvelope2D(0, 0, 100, 100), (Envelope) env);
        gg.setValue(gg2D);
        coverage = reader.read(new GeneralParameterValue[] {bkg, gg, useJai, tileSize});
        assertNull(coverage);

        reader.dispose();
    }

    @Test
    public void testIgnoreInvalidGranule() throws Exception {
        // Get the resources as needed.
        File directory = setupTestDirectory(this, rgbURL, "poisoned");
        URL poisonedURL = fileToUrl(directory);
        final AbstractGridFormat format = TestUtils.getFormat(poisonedURL);
        final ImageMosaicReader reader = getReader(poisonedURL, format);

        GranuleStore granules =
                (GranuleStore) reader.getGranules(reader.getGridCoverageNames()[0], false);
        SimpleFeature first = DataUtilities.first(granules.getGranules(Query.ALL));
        // poison it
        first.setAttribute("location", "global_mosaic_11-invalid.png");
        Transaction t = new DefaultTransaction();
        granules.setTransaction(t);
        granules.addGranules(DataUtilities.collection(first));
        t.commit();
        t.close();

        // Test the output coverage
        TestUtils.checkCoverage(reader, new GeneralParameterValue[0], "Ignore invalid granule");
    }

    @Test
    public void testReadSingleGranule() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(rgbURL);
        final ImageMosaicReader reader = getReader(rgbURL, format);

        // a bounding box that is matching one tile, while numerically touching the nearby
        // ones, but with no pixel contribution
        final double EPS = 1e-6;
        ReferencedEnvelope re =
                new ReferencedEnvelope(
                        9.2428766 - EPS,
                        12.1395782 + EPS,
                        42.5511689 - EPS,
                        44.5709679 + EPS,
                        DefaultGeographicCRS.WGS84);
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        gg.setValue(new GridGeometry2D(new GridEnvelope2D(0, 0, 50, 50), re));

        // Test the output coverage, should be made of a single granule
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {gg});
        RenderedImage ri = coverage.getRenderedImage();
        assertEquals(1, getSourceGranules(ri));

        reader.dispose();
    }

    private int getSourceGranules(RenderedImage ri) {
        if (ri instanceof RenderedOp) {
            RenderedOp ro = (RenderedOp) ri;
            if (ro.getOperationName().startsWith("ImageRead")) {
                return 1;
            }

            int count = 0;
            for (int i = 0; i < ro.getNumSources(); i++) {
                count += getSourceGranules(((RenderedOp) ri).getSourceImage(i));
            }
            return count;
        }

        // bufferedimage and friends
        return 1;
    }

    @Test
    public void testGIFSupportFiles() throws Exception {
        final AbstractGridFormat format = TestUtils.getFormat(indexURL);
        ImageMosaicReader reader = getReader(indexURL, format);
        ResourceInfo info = reader.getInfo(reader.getGridCoverageNames()[0]);
        assertTrue(info instanceof FileResourceInfo);
        FileResourceInfo fileInfo = (FileResourceInfo) info;
        CloseableIterator<FileGroup> files = fileInfo.getFiles(null);
        List<FileGroup> fileGroups = new ArrayList<FileGroup>();
        Set<File> mainFiles = new HashSet<File>();
        Set<File> supportFiles = new HashSet<File>();
        while (files.hasNext()) {
            FileGroup group = files.next();
            fileGroups.add(group);
            mainFiles.add(group.getMainFile());
            supportFiles.addAll(group.getSupportFiles());
        }
        assertEquals(3, fileGroups.size());
        assertEquals(3, mainFiles.size());
        assertEquals(6, supportFiles.size());
        File dir = URLs.urlToFile(indexURL);
        String[] mainFilesPaths = dir.list(FileFilterUtils.suffixFileFilter(".gif"));
        String[] supportFilesPaths =
                dir.list(
                        FileFilterUtils.and(
                                FileFilterUtils.or(
                                        FileFilterUtils.suffixFileFilter(".prj"),
                                        FileFilterUtils.suffixFileFilter(".wld")),
                                FileFilterUtils.notFileFilter(
                                        FileFilterUtils.prefixFileFilter("index"))));
        for (String filePath : mainFilesPaths) {
            final File myFile = new File(dir, filePath);
            assertTrue(mainFiles.contains(myFile));
        }
        for (String filePath : supportFilesPaths) {
            final File myFile = new File(dir, filePath);
            assertTrue(supportFiles.contains(myFile));
        }

        reader.dispose();
    }

    @Test
    public void testOverviewSupportFiles() throws Exception {
        final File overviewDir = TestData.file(this, "ext-overview");
        final URL overviewURL = fileToUrl(overviewDir);
        final AbstractGridFormat format = TestUtils.getFormat(overviewURL);
        ImageMosaicReader reader = getReader(overviewURL, format);
        ResourceInfo info = reader.getInfo(reader.getGridCoverageNames()[0]);
        assertTrue(info instanceof FileResourceInfo);
        FileResourceInfo fileInfo = (FileResourceInfo) info;
        CloseableIterator<FileGroup> files = fileInfo.getFiles(null);
        List<FileGroup> fileGroups = new ArrayList<FileGroup>();
        Set<File> mainFiles = new HashSet<File>();
        Set<File> supportFiles = new HashSet<File>();
        while (files.hasNext()) {
            FileGroup group = files.next();
            fileGroups.add(group);
            mainFiles.add(group.getMainFile());
            supportFiles.addAll(group.getSupportFiles());
        }
        assertEquals(2, fileGroups.size());
        assertEquals(2, mainFiles.size());
        assertEquals(2, supportFiles.size());
        File dir = URLs.urlToFile(overviewURL);
        String[] mainFilesPaths = dir.list(FileFilterUtils.suffixFileFilter(".tif"));
        String[] supportFilesPaths =
                dir.list(
                        FileFilterUtils.and(
                                FileFilterUtils.or(FileFilterUtils.suffixFileFilter(".ovr")),
                                FileFilterUtils.notFileFilter(
                                        FileFilterUtils.prefixFileFilter("index"))));
        for (String filePath : mainFilesPaths) {
            final File myFile = new File(dir, filePath);
            assertTrue(mainFiles.contains(myFile));
        }
        for (String filePath : supportFilesPaths) {
            final File myFile = new File(dir, filePath);
            assertTrue(supportFiles.contains(myFile));
        }

        reader.dispose();
    }

    @Test
    public void testBandsSelection() throws Exception {
        // instantiate image mosaic reader
        AbstractGridFormat format = TestUtils.getFormat(rgbURL);
        ImageMosaicReader reader = getReader(rgbURL, format);
        // reade the coverage select bands in different order and multiple times
        ParameterValue<int[]> selectedBands = AbstractGridFormat.BANDS.createValue();
        selectedBands.setValue(new int[] {2, 0, 1, 0, 1});
        GridCoverage2D coverage =
                TestUtils.checkCoverage(reader, new GeneralParameterValue[] {selectedBands}, null);
        // checking that we have five bands (the bands selection operation was delegated on JAI
        // BandsSelect operation)
        SampleModel sampleModel = coverage.getRenderedImage().getSampleModel();
        assertThat(sampleModel.getNumBands(), is(5));

        reader.dispose();
    }

    @Test
    public void testFilteredGranuleFootprint() throws Exception {
        AbstractGridFormat format = TestUtils.getFormat(rgbURL);
        ImageMosaicReader reader = getReader(rgbURL, format);
        ParameterValue<Filter> filter = ImageMosaicFormat.FILTER.createValue();
        filter.setValue(ECQL.toFilter("location = 'global_mosaic_16.png'"));
        GridCoverage2D coverage =
                TestUtils.checkCoverage(reader, new GeneralParameterValue[] {filter}, null);

        // now grab specific reader
        File file = new File(URLs.urlToFile(rgbURL), "global_mosaic_16.png");
        URL granuleUrl = fileToUrl(file);
        AbstractGridFormat granuleFormat = TestUtils.getFormat(granuleUrl);
        AbstractGridCoverage2DReader granuleReader = granuleFormat.getReader(granuleUrl);
        GridCoverage2D expected = granuleReader.read(null);

        // check footprint is the same
        final Envelope expectedEnvelope = expected.getEnvelope();
        final Envelope actualEnvelope = coverage.getEnvelope();
        final double EPS = 1e-6;
        assertEquals(expectedEnvelope.getMinimum(0), actualEnvelope.getMinimum(0), EPS);
        assertEquals(expectedEnvelope.getMinimum(1), actualEnvelope.getMinimum(1), EPS);
        assertEquals(expectedEnvelope.getMaximum(0), actualEnvelope.getMaximum(0), EPS);
        assertEquals(expectedEnvelope.getMaximum(1), actualEnvelope.getMaximum(1), EPS);

        reader.dispose();
    }

    @Test
    public void testFilteredNoResults() throws Exception {
        AbstractGridFormat format = TestUtils.getFormat(rgbURL);
        ImageMosaicReader reader = getReader(rgbURL, format);
        ParameterValue<Filter> filter = ImageMosaicFormat.FILTER.createValue();
        filter.setValue(ECQL.toFilter("location = 'abcdefghi'"));
        GridCoverage2D coverage =
                TestUtils.getCoverage(reader, new GeneralParameterValue[] {filter}, false);
        assertNull(coverage);

        reader.dispose();
    }

    @Test
    public void testSortOnCachedCatalogDescending() throws Exception {
        File timeCached = setupTimeCachedMosaic();

        // read reference image (the one that should be on top)
        BufferedImage expected =
                ImageIO.read(new File(timeCached, "world.200405.3x5400x2700.tiff"));

        // sort on time attribute
        final ParameterValue<String> sortBy = ImageMosaicFormat.SORT_BY.createValue();
        sortBy.setValue("time D");
        ImageMosaicReader reader = new ImageMosaicReader(timeCached);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {sortBy});
        ImageAssert.assertEquals(expected, coverage.getRenderedImage(), 0);
        coverage.dispose(true);
        reader.dispose();
    }

    @Test
    public void testSortOnCachedCatalogAscending() throws Exception {
        File timeCached = setupTimeCachedMosaic();

        // read reference image (the one that should be on top)
        BufferedImage expected =
                ImageIO.read(new File(timeCached, "world.200402.3x5400x2700.tiff"));

        // sort on time attribute
        final ParameterValue<String> sortBy = ImageMosaicFormat.SORT_BY.createValue();
        sortBy.setValue("time A");
        ImageMosaicReader reader = new ImageMosaicReader(timeCached);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {sortBy});
        ImageAssert.assertEquals(expected, coverage.getRenderedImage(), 0);
        coverage.dispose(true);
        reader.dispose();
    }

    @Test
    public void testSortOnCachedCatalogAscendingFiltered() throws Exception {
        File timeCached = setupTimeCachedMosaic();

        // read reference image (the one that should be on top)
        BufferedImage expected =
                ImageIO.read(new File(timeCached, "world.200403.3x5400x2700.tiff"));

        // sort on time attribute
        final ParameterValue<String> sortBy = ImageMosaicFormat.SORT_BY.createValue();
        sortBy.setValue("time A");
        final ParameterValue<Filter> filter = ImageMosaicFormat.FILTER.createValue();
        // during does not include the limits apparently, so we end up with a weird filter
        filter.setValue(ECQL.toFilter("time during 2004-02-28T23:59:59/2004-05-01T00:00:00"));
        ImageMosaicReader reader = new ImageMosaicReader(timeCached);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {sortBy, filter});
        ImageAssert.assertEquals(expected, coverage.getRenderedImage(), 0);
        coverage.dispose(true);
        reader.dispose();
    }

    private File setupTimeCachedMosaic() throws IOException, FactoryException {
        // copy the test data
        File source = URLs.urlToFile(timeURL);
        File timeCached = tempFolder.newFolder("timeCached");
        FileUtils.copyDirectory(source, timeCached);
        Arrays.stream(
                        timeCached.listFiles(
                                (dir, name) ->
                                        name.startsWith("time_geotiff")
                                                || "sample_image".equals(name)))
                .forEach(f -> f.delete());

        // make it create the index and config files
        ImageMosaicReader reader = getReader(timeCached);
        reader.dispose();

        // set it up so that it uses caching
        File indexerProperties = new File(timeCached, "timeCached.properties");
        Properties indexer = new Properties();
        try (InputStream is = new FileInputStream(indexerProperties)) {
            indexer.load(is);
        }
        indexer.put("Caching", "true");
        try (OutputStream os = new FileOutputStream(indexerProperties)) {
            indexer.store(os, null);
        }
        return timeCached;
    }

    @Test
    public void testMaintainNoData() throws Exception {
        String testLocation = "hetero_utm_footprint";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = new File("./target", "keep_nodata");
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        // clean up the WKT files
        Stream.of(testDirectory.listFiles())
                .filter(f -> f.getName().endsWith(".wkt"))
                .forEach(f -> f.delete());

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        Assert.assertNotNull(imReader);

        // read a coverage in deferred mode, check the nodata is there
        ParameterValue<Boolean> deferredLoading =
                AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        deferredLoading.setValue(true);
        GridCoverage2D coverageDeferred =
                imReader.read(new GeneralParameterValue[] {deferredLoading});
        assertNoData(coverageDeferred, 0d);

        // read in immediate mode, the nodata is also there
        deferredLoading.setValue(false);
        GridCoverage2D coverage = imReader.read(new GeneralParameterValue[] {deferredLoading});
        assertNoData(coverage, 0d);
        imReader.dispose();
    }

    @Test
    public void testMaintainNoDataIdentity() throws Exception {
        // For this test the input file has the nodata value set to 7.
        String testLocation = "nodata";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        ImageMosaicReader imReader = new ImageMosaicReader(testDataFolder, null);
        Assert.assertNotNull(imReader);

        // read a coverage in deferred mode, check the nodata is there
        ParameterValue<Boolean> deferredLoading =
                AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();

        deferredLoading.setValue(true);
        GridCoverage2D coverage = imReader.read(new GeneralParameterValue[] {deferredLoading});
        assertNoData(coverage, 7d);
        imReader.dispose();
    }

    public void assertNoData(GridCoverage2D coverageDeferred, Double expectedNoData) {
        NoDataContainer noDataContainer = CoverageUtilities.getNoDataProperty(coverageDeferred);
        if (expectedNoData != null) {
            assertNotNull(noDataContainer);
            assertEquals(expectedNoData, noDataContainer.getAsSingleValue(), 0d);
        } else {
            assertNull(noDataContainer);
        }
    }

    @Test
    public void testHarvestWithExtraNonSpatialFile() throws Exception {
        File source = URLs.urlToFile(rgbAURLTiff);
        File extras = URLs.urlToFile(rgbaExtraURLTiff);
        File testDataDir = TestData.file(this, ".");
        File directory = new File(testDataDir, "rgba_tiff_extra_test");
        if (directory.exists()) {
            FileUtils.deleteDirectory(directory);
        }
        FileUtils.copyDirectory(source, directory);
        FileUtils.copyDirectory(extras, directory);

        // ok, let's create a mosaic with a single granule and check its times
        ImageMosaicReader reader = getReader(directory);
        try {
            // the coverage name got parsed
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals("passA", names[0]);

            // the mosaic is referenced
            CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem();
            CoordinateReferenceSystem expected = CRS.decode("EPSG:4326", true);
            assertTrue(CRS.equalsIgnoreMetadata(expected, crs));

            // there are two granules in the mosaic, the tiffs, with the expected locations
            GranuleSource passA = reader.getGranules("passA", true);
            UniqueVisitor visitor = new UniqueVisitor("location");
            passA.getGranules(Query.ALL).accepts(visitor, null);
            Set<String> locations = visitor.getUnique();
            // System.out.println(locations);
            assertThat(locations, hasItem(equalTo("passA2006128211927.tiff")));
            assertThat(locations, hasItem(equalTo("passA2006128194218.tiff")));
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testNoDataRescaleStats() throws Exception {
        // For this test the input files havethe nodata value that is very high compared
        // to the valid data, ask for a geometry that requires some translation, and
        // check that did not result in interpolating the nodata
        String testLocation = "nodata_high";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        ImageMosaicReader reader = new ImageMosaicReader(testDataFolder, null);
        Assert.assertNotNull(reader);

        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(0, 0, 1, 3),
                        new ReferencedEnvelope(151, 152, 85, 88, DefaultGeographicCRS.WGS84));
        ParameterValue<GridGeometry2D> ggValue =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggValue.setValue(gg);
        ParameterValue<Interpolation> interpValue = AbstractGridFormat.INTERPOLATION.createValue();
        interpValue.setValue(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggValue, interpValue});
        NoDataContainer noData = (NoDataContainer) coverage.getProperty(NoDataContainer.GC_NODATA);
        assertEquals(9.96920996838686905e+36, noData.getAsSingleValue(), 0.1);

        // check the pixels have the expected values
        RenderedImage ri = coverage.getRenderedImage();
        assertEquals(1, ri.getWidth());
        assertEquals(3, ri.getHeight());
        float[] pixel = new float[1];
        // nodata got preserved (an interpolated value showed up before the fix)
        ri.getData().getPixel(0, 0, pixel);
        assertEquals(9.96920996838686905e+36, pixel[0], 0.1);
        ri.getData().getPixel(0, 1, pixel);
        assertEquals(311.92, pixel[0], 0.1);
        ri.getData().getPixel(0, 2, pixel);
        assertEquals(311.77, pixel[0], 0.1);

        coverage.dispose(true);
        reader.dispose();
    }

    @Test
    public void testGrayRGBAlpha() throws Exception {
        // instantiate image mosaic reader
        URL mosaicURL = TestData.url(this, "tiff_gray_rbg_alpha");
        AbstractGridFormat format = TestUtils.getFormat(mosaicURL);
        ImageMosaicReader reader = getReader(mosaicURL, format);
        // checking that we have the required bands in the right order
        Consumer<GridCoverage2D> verifier =
                c -> {
                    RenderedImage renderedImage = c.getRenderedImage();
                    SampleModel sampleModel = renderedImage.getSampleModel();
                    ColorModel colorModel = renderedImage.getColorModel();
                    assertThat(sampleModel.getNumBands(), is(4));
                    assertThat(colorModel.getNumComponents(), is(4));
                    assertThat(colorModel.getTransparency(), is(ColorModel.TRANSLUCENT));
                    c.dispose(true);
                };

        // the code was throwing exceptions while trying to mosaic them if the gray
        // was collected before the rgb... try both orders to ensure it works both ways

        // order them one way and check
        ParameterValue<String> ascending = ImageMosaicFormat.SORT_BY.createValue();
        ascending.setValue("location A");
        GridCoverage2D coverageA = reader.read(new GeneralParameterValue[] {ascending});
        verifier.accept(coverageA);

        // other them the opposite way and check
        ParameterValue<String> descending = ImageMosaicFormat.SORT_BY.createValue();
        descending.setValue("location D");
        GridCoverage2D coverageD = reader.read(new GeneralParameterValue[] {descending});
        verifier.accept(coverageD);

        reader.dispose();
    }

    @Test
    public void testCleanUpMetadataOnlyWorldImage() throws Exception {
        // copy the data and get the reader
        File directory = setupTestDirectory(this, this.rgbURL, "worldimage_clean_meta");
        ImageMosaicReader reader = getReader(directory);

        // collect the existing files matching the removal criteria
        FileFilter fileFilter = f -> f.getName().startsWith("global_mosaic_1");
        FileFilter otherFileFilter = f -> !fileFilter.accept(f) && !f.getName().endsWith(".qix");
        File[] existingFiles = directory.listFiles(fileFilter);
        assertThat(existingFiles, arrayWithSize(33));
        Set<File> otherFiles = new TreeSet<>(Arrays.asList(directory.listFiles(otherFileFilter)));

        GranuleStore store =
                (GranuleStore) reader.getGranules(reader.getGridCoverageNames()[0], false);
        int removed = store.removeGranules(FF.like(FF.property("location"), "*global_mosaic_1*"));
        assertEquals(11, removed);

        // collect again, no file should have been removed
        File[] existingFilesPastCleanup = directory.listFiles(fileFilter);
        assertArrayEquals(existingFiles, existingFilesPastCleanup);
        assertThat(
                otherFiles,
                equalTo(new TreeSet<>(Arrays.asList(directory.listFiles(otherFileFilter)))));
    }

    @Test
    public void testCleanUpMetadataAndDataWorldImage() throws Exception {
        // copy the data and get the reader
        File directory = setupTestDirectory(this, this.rgbURL, "worldimage_clean_data");
        ImageMosaicReader reader = getReader(directory);

        // collect the existing files matching the removal criteria
        FileFilter fileFilter = f -> f.getName().startsWith("global_mosaic_1");
        FileFilter otherFileFilter = f -> !fileFilter.accept(f) && !f.getName().endsWith(".qix");
        File[] existingFiles = directory.listFiles(fileFilter);
        assertThat(existingFiles, arrayWithSize(33));
        int otherFilesCount = directory.listFiles(otherFileFilter).length;

        GranuleStore store =
                (GranuleStore) reader.getGranules(reader.getGridCoverageNames()[0], false);
        Hints hints = new Hints(Hints.GRANULE_REMOVAL_POLICY, GranuleRemovalPolicy.ALL);
        int removed =
                store.removeGranules(FF.like(FF.property("location"), "*global_mosaic_1*"), hints);
        assertEquals(11, removed);

        // collect again, files should have been removed
        File[] existingFilesPastCleanup = directory.listFiles(fileFilter);
        assertThat(existingFilesPastCleanup, Matchers.emptyArray());
        assertEquals(otherFilesCount, directory.listFiles(otherFileFilter).length);
    }

    @Test
    public void testCleanUpMetadataAndDataOverviews() throws Exception {
        // copy the data and get the reader
        URL hetero_s2_ovr = TestData.url(this, "hetero_s2_ovr");
        File directory = setupTestDirectory(this, hetero_s2_ovr, "hetero_s2_ovr_clean");
        ImageMosaicReader reader = getReader(directory);

        // collect the existing files matching the removal criteria
        FileFilter fileFilter = f -> f.getName().startsWith("g1");
        FileFilter notFileFilter = f -> !fileFilter.accept(f) && !f.getName().endsWith(".qix");
        File[] existingFiles = directory.listFiles(fileFilter);
        assertThat(existingFiles, arrayWithSize(2));
        int otherFilesCount = directory.listFiles(notFileFilter).length;

        GranuleStore store =
                (GranuleStore) reader.getGranules(reader.getGridCoverageNames()[0], false);
        Hints hints = new Hints(Hints.GRANULE_REMOVAL_POLICY, GranuleRemovalPolicy.ALL);
        int removed = store.removeGranules(FF.like(FF.property("location"), "*g1*"), hints);
        assertEquals(1, removed);

        // collect again, files should have been removed
        File[] existingFilesPastCleanup = directory.listFiles(fileFilter);
        assertThat(existingFilesPastCleanup, Matchers.emptyArray());
        assertEquals(otherFilesCount, directory.listFiles(notFileFilter).length);
    }

    @Test
    @Ignore(
            "Does not work due to limitations in ContentDataStore transaction handling, not even with rw locking")
    public void testConcurrentHarvestAndRemoveShapefile() throws Exception {
        testConcurrentHarvestAndRemove(f -> {}, 20);
    }

    @Test
    public void testConcurrentHarvestAndRemoveH2() throws Exception {
        testConcurrentHarvestAndRemove(
                f -> {
                    // place H2 file in the dir
                    try (FileWriter out = new FileWriter(new File(f, "datastore.properties"))) {
                        out.write("database=imagemosaic\n");
                        out.write(H2_SAMPLE_PROPERTIES);
                        out.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                10);
    }

    public void testConcurrentHarvestAndRemove(Consumer<File> mosaicCustomizer, int loops)
            throws Exception {
        File source = URLs.urlToFile(rgbURL);
        File testDataDir = TestData.file(this, ".");
        File directory1 = new File(testDataDir, "harvest1-concurrent");
        File directory2 = new File(testDataDir, "harvest2-concurrent");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory2);
        }
        directory2.mkdirs();
        // Creation of a File Collection
        Collection<File> files = new ArrayList<File>();

        // move all files besides month 2 into the second directory and store them into a
        // Collection
        for (File file :
                FileUtils.listFiles(
                        directory1, new RegexFileFilter("global_mosaic_[^0].*"), null)) {
            File renamed = new File(directory2, file.getName());
            assertTrue(file.renameTo(renamed));
            if (file.getName().endsWith("png")) {
                files.add(renamed);
            }
        }
        // remove all mosaic related files
        for (File file : FileUtils.listFiles(directory1, new RegexFileFilter("rgb.*"), null)) {
            assertTrue(file.delete());
        }

        // customize mosaic creation
        mosaicCustomizer.accept(directory1);

        // ok, let's create a mosaic with the two original granules
        URL harvestSingleURL = fileToUrl(directory1);
        final AbstractGridFormat format = TestUtils.getFormat(harvestSingleURL);
        ImageMosaicReader reader = getReader(harvestSingleURL, format);
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            String[] metadataNames = reader.getMetadataNames();
            assertNotNull(metadataNames);

            // create a thread for each outstanding file that will remove and then add back
            // the file, thus creating a concurrent load on the catalog index
            final String coverageName = reader.getGridCoverageNames()[0];

            List<Future<Integer>> futures = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(1);
            for (File file : files) {
                Filter filter = FF.like(FF.property("location"), "*" + file.getName() + "*");
                Callable callable =
                        (Callable<Integer>)
                                () -> {
                                    // make all callables start toghether
                                    latch.await();
                                    int removedCount = 0;

                                    // remove if necessary
                                    GranuleStore store =
                                            (GranuleStore) reader.getGranules(coverageName, false);

                                    for (int i = 0; i < loops; i++) {
                                        final Query query = new Query(null, filter);
                                        if (store.getCount(query) > 0) {
                                            store.removeGranules(filter);
                                            removedCount++;
                                        }
                                        // and harvest back
                                        final List<HarvestedSource> harvested =
                                                reader.harvest(coverageName, file, null);
                                        assertThat(harvested, hasSize(1));
                                        assertTrue(
                                                "Feature not found after successful harvest? Loop is "
                                                        + i
                                                        + " and filter "
                                                        + filter,
                                                store.getCount(query) > 0);
                                    }
                                    return removedCount;
                                };
                final Future<Integer> future = executor.submit(callable);
                futures.add(future);
            }
            // let the callables do their job
            latch.countDown();

            // make sure nothing threw an exception
            boolean failed = false;
            for (Future<Integer> future : futures) {
                try {
                    final Integer removedCount = future.get();
                    assertEquals(loops - 1, removedCount.intValue());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Thread failed execution", e);
                    failed = true;
                }
            }
            assertFalse("Terminating test due to previus failures", failed);

            // check that all the files are there
            assertEquals(
                    files.size() + 1, reader.getGranules(coverageName, true).getCount(Query.ALL));
        } finally {
            // close up shop
            executor.shutdown();

            reader.dispose();
        }
    }

    @Test
    public void testScaleOffsetEnabled() throws Exception {
        URL scaleOffsetURL = TestData.url(this, "scaleOffset");
        final AbstractGridFormat format = TestUtils.getFormat(scaleOffsetURL);

        final ImageMosaicReader reader = getReader(scaleOffsetURL, format);
        try {
            // test one, read with scale/offset rescaling
            ParameterValue<Boolean> rescalePixels = AbstractGridFormat.RESCALE_PIXELS.createValue();
            rescalePixels.setValue(true);
            GridCoverage2D gc = reader.read(new GeneralParameterValue[] {rescalePixels});
            RenderedImage imScaled = gc.getRenderedImage();
            assertEquals(DataBuffer.TYPE_DOUBLE, imScaled.getSampleModel().getDataType());
            // ... checking pixels in the first image
            double[] pixelDouble = new double[6];
            imScaled.getData().getPixel(0, 0, pixelDouble);
            assertArrayEquals(new double[] {0.116, 0.116, 0.116, 0, 0, 1}, pixelDouble, 0d);
            // ... checking pixels in the second image
            imScaled.getData().getPixel(19, 9, pixelDouble);
            assertArrayEquals(new double[] {0.1957, 0.1957, 0.1957, 0, 0, 1}, pixelDouble, 0d);
            gc.dispose(true);
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testScaleOffsetDisabled() throws Exception {
        URL scaleOffsetURL = TestData.url(this, "scaleOffset");
        final AbstractGridFormat format = TestUtils.getFormat(scaleOffsetURL);

        final ImageMosaicReader reader = getReader(scaleOffsetURL, format);
        try {
            // test one, read with scale/offset rescaling
            ParameterValue<Boolean> rescalePixels = AbstractGridFormat.RESCALE_PIXELS.createValue();
            rescalePixels.setValue(false);
            GridCoverage2D gc = reader.read(new GeneralParameterValue[] {rescalePixels});
            RenderedImage imScaled = gc.getRenderedImage();
            assertEquals(DataBuffer.TYPE_INT, imScaled.getSampleModel().getDataType());
            // ... checking pixels in the first image
            double[] pixelDouble = new double[6];
            imScaled.getData().getPixel(0, 0, pixelDouble);
            assertArrayEquals(new double[] {1160, 1160, 1160, 0, 0, 10000}, pixelDouble, 0d);
            // ... checking pixels in the second image
            imScaled.getData().getPixel(19, 9, pixelDouble);
            assertArrayEquals(new double[] {1957, 1957, 1957, 0, 0, 10000}, pixelDouble, 0d);
            gc.dispose(true);
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testScaleOffsetEnabledWithBandSelection() throws Exception {
        URL scaleOffsetURL = TestData.url(this, "scaleOffset");
        final AbstractGridFormat format = TestUtils.getFormat(scaleOffsetURL);

        final ImageMosaicReader reader = getReader(scaleOffsetURL, format);
        try {
            // test one, read with scale/offset rescaling and band selection
            ParameterValue<Boolean> rescalePixels = AbstractGridFormat.RESCALE_PIXELS.createValue();
            rescalePixels.setValue(true);
            ParameterValue<int[]> bands = AbstractGridFormat.BANDS.createValue();
            bands.setValue(new int[] {5});
            GridCoverage2D gc = reader.read(new GeneralParameterValue[] {rescalePixels, bands});
            RenderedImage imScaled = gc.getRenderedImage();
            assertEquals(DataBuffer.TYPE_DOUBLE, imScaled.getSampleModel().getDataType());

            // ... checking pixels
            double[] pixelDouble = new double[1];
            imScaled.getData().getPixel(0, 0, pixelDouble);
            assertArrayEquals(new double[] {1}, pixelDouble, 0d);
            imScaled.getData().getPixel(19, 9, pixelDouble);
            assertArrayEquals(new double[] {1}, pixelDouble, 0d);

            bands.setValue(new int[] {1, 4});
            gc = reader.read(new GeneralParameterValue[] {rescalePixels, bands});
            imScaled = gc.getRenderedImage();
            // ... checking pixels
            pixelDouble = new double[2];
            imScaled.getData().getPixel(0, 0, pixelDouble);
            assertArrayEquals(new double[] {0.116, 0}, pixelDouble, 0d);
            imScaled.getData().getPixel(19, 9, pixelDouble);
            assertArrayEquals(new double[] {0.1957, 0}, pixelDouble, 0d);
            gc.dispose(true);
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testGranuleFileViewSidecars() throws Exception {
        // copy the data and get the reader
        File directory = setupTestDirectory(this, this.rgbURL, "rbgFileView");
        ImageMosaicReader reader = getReader(directory);
        try {
            GranuleSource source = reader.getGranules(reader.getGridCoverageNames()[0], true);
            assertThat(source.getSupportedHints(), hasItem(GranuleSource.FILE_VIEW));

            Query q = new Query();
            q.setHints(new Hints(GranuleSource.FILE_VIEW, true));
            SimpleFeatureCollection granules = source.getGranules(q);

            // no location attribute, just the geometry
            SimpleFeatureType schema = granules.getSchema();
            assertNull(schema.getDescriptor("location"));
            assertEquals(Arrays.asList(schema.getGeometryDescriptor()), schema.getDescriptors());
            // check the count, collect first and last
            SimpleFeature first = null;
            SimpleFeature last = null;
            int count = 0;
            try (SimpleFeatureIterator it = granules.features()) {
                while (it.hasNext()) {
                    count++;
                    SimpleFeature next = it.next();
                    if (first == null) first = next;
                    last = next;
                }
            }
            // all files present
            assertEquals(24, count);

            FileGroup groupFirst = (FileGroup) first.getUserData().get(GranuleSource.FILES);
            assertNotNull(groupFirst);
            assertThat(
                    groupFirst.getMainFile().getPath().toLowerCase(),
                    endsWith("global_mosaic_0.png"));
            System.out.println(groupFirst.getSupportFiles());
            assertThat(
                    groupFirst
                            .getSupportFiles()
                            .stream()
                            .map(f -> f.getName())
                            .collect(Collectors.toList()),
                    Matchers.containsInAnyOrder(
                            equalToIgnoringCase("global_mosaic_0.prj"),
                            equalToIgnoringCase("global_mosaic_0.pgw")));

            FileGroup groupLast = (FileGroup) last.getUserData().get(GranuleSource.FILES);
            assertNotNull(groupLast);
            // mind the alphabetic ordering, it's not group_mosaic_22
            assertThat(
                    groupLast.getMainFile().getPath().toLowerCase(),
                    endsWith("global_mosaic_9.png"));
            assertThat(
                    groupLast
                            .getSupportFiles()
                            .stream()
                            .map(f -> f.getName())
                            .collect(Collectors.toList()),
                    Matchers.containsInAnyOrder(
                            equalToIgnoringCase("global_mosaic_9.prj"),
                            equalToIgnoringCase("global_mosaic_9.pgw")));
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testGranuleFileViewPreserveAttributes() throws Exception {
        // copy the data and get the reader
        File directory =
                setupTestDirectory(
                        this, this.timeAdditionalDomainsURL, "additionalDomainsFileView");
        ImageMosaicReader reader = getReader(directory);
        try {
            GranuleSource source = reader.getGranules(reader.getGridCoverageNames()[0], true);
            assertThat(source.getSupportedHints(), hasItem(GranuleSource.FILE_VIEW));

            Query q = new Query();
            q.setHints(new Hints(GranuleSource.FILE_VIEW, true));
            SimpleFeatureCollection granules = source.getGranules(q);

            // no location attribute, just the geometry
            SimpleFeatureType schema = granules.getSchema();
            assertNull(schema.getDescriptor("location"));
            assertNotNull(schema.getDescriptor("the_geom"));
            assertNotNull(schema.getDescriptor("time"));
            assertNotNull(schema.getDescriptor("date"));
            assertNotNull(schema.getDescriptor("depth"));
            // check the count, collect first
            SimpleFeature first = null;
            int count = 0;
            try (SimpleFeatureIterator it = granules.features()) {
                while (it.hasNext()) {
                    count++;
                    SimpleFeature next = it.next();
                    if (first == null) first = next;
                }
            }
            // all files present
            assertEquals(4, count);

            // check the first feature
            assertEquals(20, first.getAttribute("depth"));
            FileGroup groupFirst = (FileGroup) first.getUserData().get(GranuleSource.FILES);
            assertNotNull(groupFirst);
            assertThat(
                    groupFirst.getMainFile().getPath(),
                    endsWith("NCOM_wattemp_020_20081031T0000000_12.tiff"));
            assertThat(groupFirst.getSupportFiles(), Matchers.nullValue());
        } finally {
            reader.dispose();
        }
    }
}
