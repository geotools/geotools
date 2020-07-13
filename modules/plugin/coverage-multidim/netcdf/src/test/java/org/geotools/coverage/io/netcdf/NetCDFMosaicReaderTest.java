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

import static org.geotools.gce.imagemosaic.Utils.FF;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.emptyArray;

import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.media.jai.ImageLayout;
import javax.media.jai.PlanarImage;
import javax.swing.*;
import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GranuleRemovalPolicy;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GranuleStore;
import org.geotools.coverage.grid.io.HarvestedSource;
import org.geotools.data.DefaultRepository;
import org.geotools.data.Query;
import org.geotools.data.directory.DirectoryDataStore;
import org.geotools.data.h2.H2DataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStoreFactory.ShpFileStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.imageio.netcdf.NetCDFImageReaderSpi;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import ucar.nc2.Variable;

/**
 * Testing {@link ImageMosaicReader}.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de
 * @since 2.3
 */
public class NetCDFMosaicReaderTest extends Assert {

    @Rule public TemporaryFolder tempFolder = new TemporaryFolder();

    public static ParameterValue<Boolean> NO_DEFERRED_LOADING_PARAM;

    static {
        final ParameterValue<Boolean> imageRead =
                AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        imageRead.setValue(false);
        NO_DEFERRED_LOADING_PARAM = imageRead;
    }

    public static final GeneralParameterValue[] NO_DEFERRED_LOADING_PARAMS = {
        NO_DEFERRED_LOADING_PARAM
    };

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(NetCDFMosaicReaderTest.class);
    }

    @Test
    public void testHarvestAddTime() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this, "polyphemus_20130301_test.nc");
        File mosaic = tempFolder.newFolder("nc_harvest1");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
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
            assertEquals(
                    "2013-03-01T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-01T01:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();
            testHarvest(reader, mosaic, source, q);
        } finally {
            if (it != null) {
                it.close();
            }
            reader.dispose();
        }
    }

    private void testHarvest(ImageMosaicReader reader, File mosaic, GranuleSource source, Query q)
            throws IOException {
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        try {
            // now add another netcdf and harvest it
            File nc2 = TestData.file(this, "polyphemus_20130302_test.nc");
            FileUtils.copyFileToDirectory(nc2, mosaic);
            File fileToHarvest = new File(mosaic, "polyphemus_20130302_test.nc");
            List<HarvestedSource> harvestSummary = reader.harvest(null, fileToHarvest, null);
            assertEquals(1, harvestSummary.size());
            HarvestedSource hf = harvestSummary.get(0);
            assertEquals("polyphemus_20130302_test.nc", ((File) hf.getSource()).getName());
            assertTrue(hf.success());
            assertEquals(1, reader.getGridCoverageNames().length);

            // check that we have four times now
            SimpleFeatureCollection granules = source.getGranules(q);
            assertEquals(4, granules.size());
            it = granules.features();
            SimpleFeature f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-01T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-01T01:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            f = it.next();
            assertEquals("polyphemus_20130302_test.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-02T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130302_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-02T01:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));

        } finally {
            if (it != null) {
                it.close();
            }
        }
        ImageLayout layout = reader.getImageLayout();
        SampleModel sampleModel = layout.getSampleModel(null);
        assertEquals(DataBuffer.TYPE_FLOAT, sampleModel.getDataType());
    }

    @Test
    public void testHeterogeneous()
            throws IOException, InvalidParameterValueException, ParseException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this, "polyphemus_20130301_test.nc");
        File mosaic = tempFolder.newFolder("nc_poly_hetero");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        assertNotNull(reader);
        reader.dispose();

        // now force heterogeneous interpretation
        Properties mosaicProps = new Properties();
        File mosaicPropsFile = new File(mosaic, "O3.properties");
        try (FileInputStream fis = new FileInputStream(mosaicPropsFile)) {
            mosaicProps.load(fis);
        }
        mosaicProps.put("Heterogeneous", "true");
        try (FileOutputStream fos = new FileOutputStream(mosaicPropsFile)) {
            mosaicProps.store(fos, "Now with hetero flag up");
        }

        // load two different times, make sure we actually read two different slices
        String t1 = "2013-03-01T00:00:00.000Z";
        String t2 = "2013-03-01T01:00:00.000Z";
        reader = format.getReader(mosaic);
        try {
            // prepare params
            ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
            time.setValue(Arrays.asList(parseTimeStamp(t1)));
            GeneralParameterValue[] params =
                    new GeneralParameterValue[] {NO_DEFERRED_LOADING_PARAM, time};
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
        File nc1 = TestData.file(this, "polyphemus_20130301_NO2_time2.nc");
        File mosaic = tempFolder.newFolder("nc_time2");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        final String auxiliaryFilePath =
                mosaic.getAbsolutePath() + File.separatorChar + ".polyphemus_20130301_NO2_time2";
        final File auxiliaryFileDir = new File(auxiliaryFilePath);
        assertTrue(auxiliaryFileDir.mkdirs());

        File nc1Aux = TestData.file(this, "polyphemus_20130301_NO2_time2.xml");
        FileUtils.copyFileToDirectory(nc1Aux, auxiliaryFileDir);

        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        checkCustomTimeAttribute(nc1, reader);
        reader.dispose();
    }

    @Test
    public void testCustomTimeAttributeRepository() throws IOException {
        // setup repository
        ShpFileStoreFactory dialect =
                new ShpFileStoreFactory(new ShapefileDataStoreFactory(), new HashMap());
        File indexDirectory = new File("./target/custom_time_attribute_idx");
        FileUtils.deleteQuietly(indexDirectory);
        indexDirectory.mkdir();
        File auxiliaryDataStoreFile = new File(indexDirectory, "test.properties");
        String theStoreName = "testStore";
        FileUtils.writeStringToFile(
                auxiliaryDataStoreFile, NetCDFUtilities.STORE_NAME + "=" + theStoreName, "UTF-8");

        DirectoryDataStore dataStore = new DirectoryDataStore(indexDirectory, dialect);

        DefaultRepository repository = new DefaultRepository();
        repository.register(new NameImpl(theStoreName), dataStore);

        File nc1 = TestData.file(this, "polyphemus_20130301_NO2_time2.nc");
        File mosaic = tempFolder.newFolder("nc_time2");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        // The indexer
        Properties indexer = new Properties();
        indexer.put("TimeAttribute", "time");
        indexer.put(
                "Schema",
                "the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date");
        indexer.put("AuxiliaryDatastoreFile", auxiliaryDataStoreFile.getCanonicalPath());
        final String auxiliaryFilePath =
                mosaic.getAbsolutePath() + File.separatorChar + ".polyphemus_20130301_NO2_time2";
        final File auxiliaryFileDir = new File(auxiliaryFilePath);
        assertTrue(auxiliaryFileDir.mkdirs());

        File nc1Aux = TestData.file(this, "polyphemus_20130301_NO2_time2.xml");
        FileUtils.copyFileToDirectory(nc1Aux, auxiliaryFileDir);

        try (FileOutputStream fos = new FileOutputStream(new File(mosaic, "indexer.properties"))) {
            indexer.store(fos, null);
        }
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader =
                format.getReader(mosaic, new Hints(Hints.REPOSITORY, repository));
        checkCustomTimeAttribute(nc1, reader);

        // the index files have actually been created
        List<String> typeNames = Arrays.asList(dataStore.getTypeNames());
        assertEquals(1, typeNames.size());
        assertTrue(typeNames.contains("NO2"));
        dataStore.dispose();
        reader.dispose();
    }

    @Test
    public void testSharedRepository() throws IOException {
        // setup repository
        ShpFileStoreFactory dialect =
                new ShpFileStoreFactory(new ShapefileDataStoreFactory(), new HashMap());
        File indexDirectory = new File("./target/repo_idx");
        FileUtils.deleteQuietly(indexDirectory);
        indexDirectory.mkdir();
        File auxiliaryDataStoreFile = new File(indexDirectory, "test.properties");
        String theStoreName = "testStore";
        FileUtils.writeStringToFile(
                auxiliaryDataStoreFile, NetCDFUtilities.STORE_NAME + "=" + theStoreName, "UTF-8");

        AtomicBoolean disposed = new AtomicBoolean();
        DirectoryDataStore dataStore =
                new DirectoryDataStore(indexDirectory, dialect) {

                    @Override
                    public void dispose() {
                        super.dispose();
                        disposed.set(true);
                    }
                };

        DefaultRepository repository = new DefaultRepository();
        repository.register(new NameImpl(theStoreName), dataStore);

        File nc1 = TestData.file(this, "polyphemus_20130301_test.nc");
        File nc2 = TestData.file(this, "polyphemus_20130302_test.nc");
        File mosaic = tempFolder.newFolder("nc_repo");
        FileUtils.copyFileToDirectory(nc1, mosaic);
        FileUtils.copyFileToDirectory(nc2, mosaic);

        // The indexer
        Properties indexer = new Properties();
        indexer.put("TimeAttribute", "time");
        indexer.put(
                "Schema",
                "the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date");
        indexer.put("AuxiliaryDatastoreFile", auxiliaryDataStoreFile.getCanonicalPath());
        final String auxiliaryFilePath =
                mosaic.getAbsolutePath() + File.separatorChar + ".polyphemus_20130301_test";
        final File auxiliaryFileDir = new File(auxiliaryFilePath);
        assertTrue(auxiliaryFileDir.mkdirs());

        File nc1Aux = TestData.file(this, "polyphemus_test_aux.xml");
        FileUtils.copyFileToDirectory(nc1Aux, auxiliaryFileDir);

        try (FileOutputStream fos = new FileOutputStream(new File(mosaic, "indexer.properties"))) {
            indexer.store(fos, null);
        }
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader =
                format.getReader(mosaic, new Hints(Hints.REPOSITORY, repository));

        final String name = "O3";
        NetCDFImageReader imageReader = null;
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        GridCoverage2D coverage = null;
        try {
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals(name, names[0]);

            // check we can read
            coverage = reader.read(NO_DEFERRED_LOADING_PARAMS);

            // check we have the 4 granules we expect
            GranuleSource source = reader.getGranules(name, true);
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            Query q = new Query(Query.ALL);
            q.setSortBy(new SortBy[] {ff.sort("time", SortOrder.ASCENDING)});
            SimpleFeatureCollection granules = source.getGranules(q);
            assertEquals(4, granules.size());
            it = granules.features();
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
        } finally {
            if (coverage != null) {
                coverage.dispose(true);
            }

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

        // the index files have actually been created
        List<String> typeNames = Arrays.asList(dataStore.getTypeNames());
        assertFalse(disposed.get());
        assertEquals(1, typeNames.size());
        assertTrue(typeNames.contains(name));
        dataStore.dispose();
        assertTrue(disposed.get());
    }

    @Test
    public void testHarvestWithSharedRepository() throws IOException {
        // setup repository
        ShpFileStoreFactory dialect =
                new ShpFileStoreFactory(new ShapefileDataStoreFactory(), new HashMap());
        File indexDirectory = new File("./target/repo2_idx");
        FileUtils.deleteQuietly(indexDirectory);
        indexDirectory.mkdir();
        File auxiliaryDataStoreFile = new File(indexDirectory, "test.properties");
        String theStoreName = "testStore";
        FileUtils.writeStringToFile(
                auxiliaryDataStoreFile, NetCDFUtilities.STORE_NAME + "=" + theStoreName, "UTF-8");

        DirectoryDataStore dataStore = new DirectoryDataStore(indexDirectory, dialect);
        DefaultRepository repository = new DefaultRepository();
        repository.register(new NameImpl(theStoreName), dataStore);

        File nc1 = TestData.file(this, "polyphemus_20130301_test.nc");
        File mosaic = tempFolder.newFolder("nc_repo");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        // The indexer
        Properties indexer = new Properties();
        indexer.put("TimeAttribute", "time");
        indexer.put(
                "Schema",
                "the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date");
        indexer.put("AuxiliaryDatastoreFile", auxiliaryDataStoreFile.getCanonicalPath());
        final String auxiliaryFilePath =
                mosaic.getAbsolutePath() + File.separatorChar + ".polyphemus_20130301_test";
        final File auxiliaryFileDir = new File(auxiliaryFilePath);
        assertTrue(auxiliaryFileDir.mkdirs());

        File nc1Aux = TestData.file(this, "polyphemus_test_aux.xml");
        FileUtils.copyFileToDirectory(nc1Aux, auxiliaryFileDir);

        try (FileOutputStream fos = new FileOutputStream(new File(mosaic, "indexer.properties"))) {
            indexer.store(fos, null);
        }
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader =
                format.getReader(mosaic, new Hints(Hints.REPOSITORY, repository));

        final String name = "O3";
        NetCDFImageReader imageReader = null;
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        try {

            GranuleSource source = reader.getGranules(name, true);
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            Query q = new Query(Query.ALL);
            q.setSortBy(new SortBy[] {ff.sort("time", SortOrder.ASCENDING)});
            SimpleFeatureCollection granules = source.getGranules(q);
            assertEquals(2, granules.size());
            it = granules.features();
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));

            // now add another netcdf and harvest it
            testHarvest(reader, mosaic, source, q);
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

        dataStore.dispose();
    }

    public void checkCustomTimeAttribute(File nc1, ImageMosaicReader reader) throws IOException {
        NetCDFImageReader imageReader = null;
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        GridCoverage2D coverage = null;
        try {
            String[] names = reader.getGridCoverageNames();
            assertEquals(1, names.length);
            assertEquals("NO2", names[0]);

            // check we can read
            coverage = reader.read(NO_DEFERRED_LOADING_PARAMS);

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
            if (coverage != null) {
                coverage.dispose(true);
            }

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
        File nc1 = TestData.file(this, "polyphemus_20130301_test.nc");
        File mosaic = tempFolder.newFolder("nc_harvest4");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
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
            assertEquals(
                    "2013-03-01T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-01T01:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();

            // close the reader and re-open it
            reader.dispose();
            reader = format.getReader(mosaic);
            source = reader.getGranules("O3", true);

            // wait a bit, we have to make sure the old indexes are recognized as old
            Thread.sleep(1000);

            // now replace the netcdf file with a more up to date version of the same
            File nc2 = TestData.file(this, "polyphemus_20130301_test_more_times.nc");
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
            assertEquals(
                    "2013-03-01T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-01T01:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(2, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-01T02:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(3, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-01T03:00:00.000Z",
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
    public void testHarvestHDF5Data() throws IOException {
        File nc1 = TestData.file(this, "2DLatLonCoverage.nc");
        File nc2 = TestData.file(this, "2DLatLonCoverage2.nc");
        File mosaic = tempFolder.newFolder("simpleMosaic");
        FileUtils.copyFileToDirectory(nc1, mosaic);
        FileUtils.copyFileToDirectory(nc2, mosaic);

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        File xml = TestData.file(this, "hdf5Coverage2D.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        //  + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "hdf5Coverage2D.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        // simply test if the mosaic can be read without exceptions
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        reader.read("L1_V2", NO_DEFERRED_LOADING_PARAMS);
        reader.dispose();
    }

    @Test
    public void testHarvestAddVariable() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this, "polyphemus_20130301_test.nc");
        File mosaic = tempFolder.newFolder("nc_harvest2");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
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
            assertEquals(
                    "2013-03-01T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_test.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-01T01:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();

            // now add another netcdf and harvest it
            File nc2 = TestData.file(this, "polyphemus_20130301_NO2.nc");
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
            source = reader.getGranules("NO2", true);
            granules = source.getGranules(q);
            assertEquals(2, granules.size());
            it = granules.features();
            f = it.next();
            assertEquals("polyphemus_20130301_NO2.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-01T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("polyphemus_20130301_NO2.nc", f.getAttribute("location"));
            assertEquals(1, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-03-01T01:00:00.000Z",
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
    public void testMultipleGranules() throws IOException, ParseException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY_new.nc");
        File nc2 = TestData.file(this, "20130108.METOPA.GOME2.NO2.DUMMY_new.nc");
        File mosaic = tempFolder.newFolder("nc_heterogen");
        FileUtils.copyFileToDirectory(nc1, mosaic);
        FileUtils.copyFileToDirectory(nc2, mosaic);

        File xml = TestData.file(this, "GOME2.NO2_new.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                        + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "GOME2.NO2_new.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        SimpleFeatureIterator it = null;
        assertNotNull(reader);
        try {
            // specify time
            ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
            final Date timeD = parseTimeStamp("2013-01-01T00:00:00.000");
            time.setValue(
                    new ArrayList() {
                        {
                            add(timeD);
                        }
                    });
            GeneralParameterValue[] params =
                    new GeneralParameterValue[] {time, NO_DEFERRED_LOADING_PARAM};
            GridCoverage2D coverage1 = reader.read(params);
            assertNotData(coverage1, -999d);
            // Specify a new time (Check if two times returns two different coverages)
            final Date timeD2 = parseTimeStamp("2013-01-08T00:00:00.000");
            time.setValue(
                    new ArrayList() {
                        {
                            add(timeD2);
                        }
                    });
            params = new GeneralParameterValue[] {time, NO_DEFERRED_LOADING_PARAM};
            GridCoverage2D coverage2 = reader.read(params);
            assertNotData(coverage2, -999d);
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

    private void assertNotData(GridCoverage2D coverage, Double expectedNoData) {
        Object noData = coverage.getProperty("GC_NODATA");
        if (expectedNoData == null) {
            assertThat(noData, not(instanceOf(NoDataContainer.class)));
        } else {
            assertThat(noData, instanceOf(NoDataContainer.class));
            NoDataContainer container = (NoDataContainer) noData;
            assertEquals(expectedNoData, container.getAsSingleValue(), 0d);
        }
    }

    @Test
    public void testHarvest3Gome() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY.nc");
        File mosaic = tempFolder.newFolder("nc_harvest");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        File xml = TestData.file(this, ".DUMMY.GOME2.NO2.PGL/GOME2.NO2.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                        + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "GOME2.NO2.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
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
            q.setSortBy(new SortBy[] {ff.sort("time", SortOrder.DESCENDING)});
            SimpleFeatureCollection granules = source.getGranules(q);
            assertEquals(1, granules.size());
            it = granules.features();
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            assertEquals("20130101.METOPA.GOME2.NO2.DUMMY.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-01-01T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            it.close();

            // now add another netcdf and harvest it
            File nc2 = TestData.file(this, "20130116.METOPA.GOME2.NO2.DUMMY.nc");
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

            File nc3 = TestData.file(this, "20130108.METOPA.GOME2.NO2.DUMMY.nc");
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
            assertEquals(
                    "2013-01-16T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            assertTrue(it.hasNext());
            f = it.next();
            assertEquals("20130108.METOPA.GOME2.NO2.DUMMY.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-01-08T00:00:00.000Z",
                    ConvertersHack.convert(f.getAttribute("time"), String.class));
            f = it.next();
            assertEquals("20130101.METOPA.GOME2.NO2.DUMMY.nc", f.getAttribute("location"));
            assertEquals(0, f.getAttribute("imageindex"));
            assertEquals(
                    "2013-01-01T00:00:00.000Z",
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
        File nc1 = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY.nc");
        File mosaic = tempFolder.newFolder("nc_harvest3");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        File xml = TestData.file(this, ".DUMMY.GOME2.NO2.PGL/GOME2.NO2.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                        + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "GOME2.NO2.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
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

            assertTrue(
                    CRS.equalsIgnoreMetadata(
                            DefaultGeographicCRS.WGS84, reader.getCoordinateReferenceSystem()));
            GeneralEnvelope envelope = reader.getOriginalEnvelope("NO2");
            assertEquals(-360, envelope.getMinimum(0), 0d);
            assertEquals(360, envelope.getMaximum(0), 0d);
            assertEquals(-180, envelope.getMinimum(1), 0d);
            assertEquals(180, envelope.getMaximum(1), 0d);

            // check we can read a coverage out of it
            coverage = reader.read(NO_DEFERRED_LOADING_PARAMS);
            reader.dispose();

            // Checking we can read again from the coverage once it has been configured.
            reader = format.getReader(mosaic);
            coverage = reader.read(NO_DEFERRED_LOADING_PARAMS);
            assertNotNull(coverage);

        } finally {
            if (coverage != null) {
                ImageUtilities.disposePlanarImageChain((PlanarImage) coverage.getRenderedImage());
                coverage.dispose(true);
            }
            reader.dispose();
        }
    }

    @Test
    public void testNetCdfMosaicSingleVariableFiles()
            throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException,
                    IllegalAccessException {
        File ncMosaic = TestData.file(this, "imagemosaic");
        File mosaic = tempFolder.newFolder("ncmosaic");
        FileUtils.copyDirectory(ncMosaic, mosaic);
        File auxiliaryFile = new File(mosaic, "O3-NO2.xml");
        String auxiliaryFilePath = auxiliaryFile.getAbsolutePath();
        Hints hints = new Hints(Utils.AUXILIARY_FILES_PATH, auxiliaryFilePath);
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = null;
        GridCoverage2D coverage = null;
        try {
            reader = format.getReader(mosaic, hints);
            coverage = reader.read("O3", NO_DEFERRED_LOADING_PARAMS);
            reader.dispose();
            reader = format.getReader(mosaic, hints);
            coverage = reader.read("NO2", NO_DEFERRED_LOADING_PARAMS);
            assertNotNull(coverage);
        } finally {
            if (coverage != null) {
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
        File mosaic = tempFolder.newFolder("nc_harvestRP");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        final String auxFileName = "GOME2.NO2.xml";
        File xml = TestData.file(this, ".DUMMY.GOME2.NO2.PGL/" + auxFileName);
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                        + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "GOME2.NO2.xml\n";

        // Setting RelativePath behavior
        indexer += Prop.ABSOLUTE_PATH + "=" + "false";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
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
            try (InputStream inStream = new FileInputStream(file)) {
                props.load(inStream);
            }
            // Before the fix, the AuxiliaryFile was always an absolute path
            assertEquals(auxFileName, (String) props.getProperty(Prop.AUXILIARY_FILE));
        } finally {
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
        File nc1 = TestData.file(this, "O3-NO2.nc");
        File mosaic = tempFolder.newFolder("nc_deleteCoverage");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        File xml = TestData.file(this, ".O3-NO2/O3-NO2.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "O3-NO2.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
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
            if (coverage != null) {
                ImageUtilities.disposePlanarImageChain((PlanarImage) coverage.getRenderedImage());
                coverage.dispose(true);
            }
            reader.dispose();
        }
    }

    @Test
    public void testReadCoverageGome2Names() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY.nc");
        File mosaic = tempFolder.newFolder("nc_gome2");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        nc1 = TestData.file(this, "20130101.METOPA.GOME2.BrO.DUMMY.nc");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        File xml = TestData.file(this, "DUMMYGOME2.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                        + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "DUMMYGOME2.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
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

            assertTrue(
                    CRS.equalsIgnoreMetadata(
                            DefaultGeographicCRS.WGS84,
                            reader.getCoordinateReferenceSystem("NO2")));
            GeneralEnvelope envelope = reader.getOriginalEnvelope("NO2");
            assertEquals(-360, envelope.getMinimum(0), 0d);
            assertEquals(360, envelope.getMaximum(0), 0d);
            assertEquals(-180, envelope.getMinimum(1), 0d);
            assertEquals(180, envelope.getMaximum(1), 0d);

            // check we can read a coverage out of it
            coverage = reader.read("NO2", NO_DEFERRED_LOADING_PARAMS);
            reader.dispose();

            // Checking we can read again from the coverage (using a different name this time) once
            // it has been configured.
            reader = format.getReader(mosaic);
            coverage = reader.read("BrO", NO_DEFERRED_LOADING_PARAMS);
            assertNotNull(coverage);

        } finally {
            if (coverage != null) {
                ImageUtilities.disposePlanarImageChain((PlanarImage) coverage.getRenderedImage());
                coverage.dispose(true);
            }
            reader.dispose();
        }
    }

    @Test
    public void testCheckDifferentSampleImages() throws IOException {
        // prepare a "mosaic" with just one NetCDF
        File nc1 = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY.nc");
        File mosaic = tempFolder.newFolder("nc_sampleimages");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        nc1 = TestData.file(this, "20130101.METOPA.GOME2.BrO.DUMMY.nc");
        FileUtils.copyFileToDirectory(nc1, mosaic);

        File xml = TestData.file(this, "DUMMYGOME2.xml");
        FileUtils.copyFileToDirectory(xml, mosaic);

        // The indexer
        String indexer =
                "TimeAttribute=time\n"
                        + "Schema=the_geom:Polygon,location:String,imageindex:Integer,time:java.util.Date\n"
                        + "PropertyCollectors=TimestampFileNameExtractorSPI[timeregex](time)\n";
        indexer += Prop.AUXILIARY_FILE + "=" + "DUMMYGOME2.xml";
        FileUtils.writeStringToFile(new File(mosaic, "indexer.properties"), indexer, "UTF-8");

        String timeregex = "regex=[0-9]{8}";
        FileUtils.writeStringToFile(new File(mosaic, "timeregex.properties"), timeregex, "UTF-8");

        // the datastore.properties file is also mandatory...
        File dsp = TestData.file(this, "datastore.properties");
        FileUtils.copyFileToDirectory(dsp, mosaic);

        // have the reader harvest it
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(mosaic);
        assertNotNull(reader);

        // Checking whether different sample images have been created
        final File sampleImage1 = new File(mosaic, "BrOsample_image.dat");
        final File sampleImage2 = new File(mosaic, "NO2sample_image.dat");
        assertTrue(sampleImage1.exists());
        assertTrue(sampleImage2.exists());
        reader.dispose();
    }

    @Test
    @Ignore
    public void oracle()
            throws IOException, ParseException, NoSuchAuthorityCodeException, FactoryException {
        final File workDir = new File("C:\\data\\dlr\\ascatL1_mosaic");

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
        assertEquals("0,1,2", reader.getMetadataValue(name, "NUMSIGMA_DOMAIN"));
        assertEquals(
                "java.lang.Integer", reader.getMetadataValue(name, "NUMSIGMA_DOMAIN_DATATYPE"));

        assertEquals("true", reader.getMetadataValue(name, "HAS_RUNTIME_DOMAIN"));
        assertEquals("false", reader.getMetadataValue(name, "HAS_ELEVATION_DOMAIN"));
        assertEquals("false", reader.getMetadataValue(name, "HAS_XX_DOMAIN"));
        assertEquals("20110620020000", reader.getMetadataValue(name, "RUNTIME_DOMAIN"));
        assertEquals("java.lang.String", reader.getMetadataValue(name, "RUNTIME_DOMAIN_DATATYPE"));

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope(name);
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange(name).getSpan(0) / 2.0,
                reader.getOriginalGridRange(name).getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange(name));
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        final ParameterValue<double[]> bkg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
        bkg.setValue(new double[] {-9999.0});

        ParameterValue<List<String>> dateValue = null;
        ParameterValue<List<String>> sigmaValue = null;
        final String selectedSigma = "1";
        final String selectedRuntime = "20110620020000";
        Set<ParameterDescriptor<List>> params = reader.getDynamicParameters(name);
        for (ParameterDescriptor param : params) {
            if (param.getName().getCode().equalsIgnoreCase("RUNTIME")) {
                dateValue = param.createValue();
                dateValue.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedRuntime);
                            }
                        });
            } else if (param.getName().getCode().equalsIgnoreCase("NUMSIGMA")) {
                sigmaValue = param.createValue();
                sigmaValue.setValue(
                        new ArrayList<String>() {
                            {
                                add(selectedSigma);
                            }
                        });
            }
        }
        // Test the output coverage
        GridCoverage2D coverage =
                reader.read(
                        name,
                        new GeneralParameterValue[] {
                            gg, bkg, NO_DEFERRED_LOADING_PARAM, sigmaValue, dateValue
                        });
        assertNotNull(coverage);
        reader.dispose();
    }

    /**
     * Test that expected data values can be read from an ImageMosaic of multi-coverage NetCDF
     * files.
     */
    @Test
    public void testMultiCoverage() throws Exception {
        File testDir = tempFolder.newFolder("multi-coverage");
        URL testUrl = URLs.fileToUrl(testDir);
        FileUtils.copyDirectory(TestData.file(this, "multi-coverage"), testDir);
        ImageMosaicReader reader = null;
        try {
            reader = new ImageMosaicReader(testUrl);
            assertNotNull(reader);
            checkMultiCoverage(reader, "air_temperature", -85, 26, "2017-02-06T00:00:00.000", 295);
            checkMultiCoverage(
                    reader, "sea_surface_temperature", -85, 26, "2017-02-06T00:00:00.000", 296);
            checkMultiCoverage(reader, "air_temperature", -85, 26, "2017-02-06T12:00:00.000", 296);
            checkMultiCoverage(
                    reader, "sea_surface_temperature", -85, 26, "2017-02-06T12:00:00.000", 295);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    /**
     * Check that reading a single data value from an ImageMosaic of multi-coverage NetCDF files
     * yields the expected value.
     */
    private void checkMultiCoverage(
            ImageMosaicReader reader,
            String coverageName,
            double longitude,
            double latitude,
            String timestamp,
            double expected)
            throws Exception {
        @SuppressWarnings("rawtypes")
        ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        time.setValue(Arrays.asList(new Date[] {parseTimeStamp(timestamp)}));
        GeneralParameterValue[] params =
                new GeneralParameterValue[] {NO_DEFERRED_LOADING_PARAM, time};
        GridCoverage2D coverage = reader.read(coverageName, params);
        assertNotNull(coverage);
        // delta is zero because an exact match is expected
        assertEquals(
                expected,
                coverage.evaluate(new Point2D.Double(longitude, latitude), (double[]) null)[0],
                0);
    }

    private Date parseTimeStamp(String timeStamp) throws ParseException {
        final SimpleDateFormat formatD = getIsoFormat();
        return formatD.parse(timeStamp);
    }

    private SimpleDateFormat getIsoFormat() {
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        formatD.setTimeZone(TimeZone.getTimeZone("Zulu"));
        return formatD;
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

    /** @param args */
    public static void main(String[] args) {
        TestRunner.run(NetCDFMosaicReaderTest.suite());
    }

    @BeforeClass
    public static void init() {
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

    static ImageMosaicReader getReader(URL testURL, final AbstractGridFormat format, Hints hints) {
        // Get a reader
        final ImageMosaicReader reader = (ImageMosaicReader) format.getReader(testURL, hints);
        Assert.assertNotNull(reader);
        return reader;
    }

    /** Cleanup granules metadata */
    @Test
    public void testMultiCoverageCleanupMetadata() throws Exception {
        String folder = "gome-clean-meta";
        Hints hints = new Hints(Hints.GRANULE_REMOVAL_POLICY, GranuleRemovalPolicy.METADATA);

        File[] filesAfter = runNO2Removal(folder, hints);
        assertThat(filesAfter, arrayWithSize(1));
        assertThat(filesAfter[0].getName(), equalTo("20130101.NO2.DUMMY.nc"));
    }

    /** Cleanup granules metadata and data */
    @Test
    public void testMultiCoverageCleanupAll() throws Exception {
        String folder = "gome-clean-all";
        Hints hints = new Hints(Hints.GRANULE_REMOVAL_POLICY, GranuleRemovalPolicy.ALL);

        File[] filesAfter = runNO2Removal(folder, hints);
        assertThat(Arrays.toString(filesAfter), filesAfter, emptyArray());
    }

    private File[] runNO2Removal(String folder, Hints hints) throws IOException {
        File testDir = tempFolder.newFolder(folder);
        URL testUrl = URLs.fileToUrl(testDir);
        FileUtils.copyDirectory(TestData.file(this, "gome"), testDir);
        ImageMosaicReader reader = null;
        try {
            reader = new ImageMosaicReader(testUrl);
            assertNotNull(reader);
            // read and dispose the coverages to make sure sidecars are created
            reader.read("NO2", NO_DEFERRED_LOADING_PARAMS).dispose(true);
            reader.read("BrO", NO_DEFERRED_LOADING_PARAMS).dispose(true);
            FileFilter no2_20130101_filter = f -> f.getName().contains("20130101.NO2.DUMMY");
            File[] fileBefore = testDir.listFiles(no2_20130101_filter);
            assertEquals(2, fileBefore.length);

            // remove granule
            GranuleStore store = (GranuleStore) reader.getGranules("NO2", false);

            int removed =
                    store.removeGranules(FF.like(FF.property("location"), "*20130101*"), hints);
            assertEquals(1, removed);

            // return files after
            return testDir.listFiles(no2_20130101_filter);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    /** Cleanup granules metadata, fully */
    @Test
    public void testMultiCoverageCleanupAllInAuxDB() throws Exception {
        String folder = "poliaux-db-full";
        Hints hints = new Hints(Hints.GRANULE_REMOVAL_POLICY, GranuleRemovalPolicy.ALL);

        // clean up the netcdf aux database folder
        File netcdfAux = new File("target/clean-test");
        FileUtils.deleteQuietly(netcdfAux);

        File testDir = tempFolder.newFolder(folder);
        URL testUrl = URLs.fileToUrl(testDir);
        FileUtils.copyDirectory(TestData.file(this, "poliaux"), testDir);
        ImageMosaicReader reader = null;
        PropertyIsLike locationFilter = FF.like(FF.property("location"), "*Poli1*");
        try {
            reader = new ImageMosaicReader(testUrl);
            assertNotNull(reader);

            // read and dispose the coverages to make sure sidecars are created
            reader.read("NO2", NO_DEFERRED_LOADING_PARAMS).dispose(true);
            reader.read("O3", NO_DEFERRED_LOADING_PARAMS).dispose(true);
            FileFilter no2_20130101_filter = f -> f.getName().contains("Poli1");
            File[] fileBefore = testDir.listFiles(no2_20130101_filter);
            assertEquals(2, fileBefore.length);

            // remove granule from NO2
            GranuleStore no2store = (GranuleStore) reader.getGranules("NO2", false);
            int no2removed = no2store.removeGranules(locationFilter, hints);
            assertEquals(2, no2removed);

            // the poli1 file has not been removed, the other variable is still referencing it
            File[] filePartialRemoval = testDir.listFiles(no2_20130101_filter);
            assertThat(filePartialRemoval, arrayWithSize(2));
            // now to the same from 03
            GranuleStore o3store = (GranuleStore) reader.getGranules("O3", false);
            int o3removed = o3store.removeGranules(locationFilter, hints);
            assertEquals(2, o3removed);

            // the poli1 file has now been removed, nothing left to reference it
            File[] fileFullRemoval = testDir.listFiles(no2_20130101_filter);
            assertThat(Arrays.toString(fileFullRemoval), fileFullRemoval, emptyArray());
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }

        // check that the NetCDF database has been cleaned too
        Properties connectionParams = new Properties();
        try (FileReader fr = new FileReader(new File(testDir, "netcdf_datastore.properties"))) {
            connectionParams.load(fr);
        }
        JDBCDataStore store = new H2DataStoreFactory().createDataStore(connectionParams);
        assertEquals(0, store.getFeatureSource("NO2").getFeatures(locationFilter).size());
        assertEquals(0, store.getFeatureSource("O3").getFeatures(locationFilter).size());
    }

    /** Cleanup granules metadata, fully */
    @Test
    public void testMultiCoverageCleanupMetadataInAuxDB() throws Exception {
        String folder = "poliaux-db-meta";
        Hints hints = new Hints(Hints.GRANULE_REMOVAL_POLICY, GranuleRemovalPolicy.METADATA);

        // clean up the netcdf aux database folder
        File netcdfAux = new File("target/clean-test");
        FileUtils.deleteQuietly(netcdfAux);

        File testDir = tempFolder.newFolder(folder);
        URL testUrl = URLs.fileToUrl(testDir);
        FileUtils.copyDirectory(TestData.file(this, "poliaux"), testDir);
        ImageMosaicReader reader = null;
        PropertyIsLike locationFilter = FF.like(FF.property("location"), "*Poli1*");
        try {
            reader = new ImageMosaicReader(testUrl);
            assertNotNull(reader);
            // read and dispose the coverages to make sure sidecars are created
            reader.read("NO2", NO_DEFERRED_LOADING_PARAMS).dispose(true);
            reader.read("O3", NO_DEFERRED_LOADING_PARAMS).dispose(true);

            FileFilter no2_20130101_filter = f -> f.getName().contains("Poli1");
            File[] fileBefore = testDir.listFiles(no2_20130101_filter);
            assertEquals(2, fileBefore.length);

            // remove granule from NO2
            GranuleStore no2store = (GranuleStore) reader.getGranules("NO2", false);
            int no2removed = no2store.removeGranules(locationFilter, hints);
            assertEquals(2, no2removed);

            // the poli1 file has not been removed, the other variable is still referencing it
            File[] filePartialRemoval = testDir.listFiles(no2_20130101_filter);
            assertEquals(2, filePartialRemoval.length);

            // now to the same from 03
            GranuleStore o3store = (GranuleStore) reader.getGranules("O3", false);
            int o3removed = o3store.removeGranules(locationFilter, hints);
            assertEquals(2, o3removed);

            // the poli1 file is left, the metadata is gone
            File[] fileFullRemoval = testDir.listFiles(no2_20130101_filter);
            assertThat(fileFullRemoval, Matchers.arrayWithSize(1));
            assertEquals(fileFullRemoval[0].getName(), "Poli1.nc");
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }

        // check that the NetCDF database has been cleaned too
        Properties connectionParams = new Properties();
        try (FileReader fr = new FileReader(new File(testDir, "netcdf_datastore.properties"))) {
            connectionParams.load(fr);
        }
        JDBCDataStore store = new H2DataStoreFactory().createDataStore(connectionParams);
        assertEquals(0, store.getFeatureSource("NO2").getFeatures(locationFilter).size());
        assertEquals(0, store.getFeatureSource("O3").getFeatures(locationFilter).size());
    }

    @Test
    public void testGranuleSourceFileView() throws Exception {
        File testDir = tempFolder.newFolder("multi-coverage-fileview");
        URL testUrl = URLs.fileToUrl(testDir);
        FileUtils.copyDirectory(TestData.file(this, "multi-coverage"), testDir);
        ImageMosaicReader reader = new ImageMosaicReader(testUrl);
        try {
            assertNotNull(reader);

            Query q = new Query();
            q.setHints(new Hints(GranuleSource.FILE_VIEW, true));
            GranuleSource source = reader.getGranules("air_temperature", true);
            SimpleFeatureCollection granules = source.getGranules(q);

            // many slices but only two files
            assertEquals(2, granules.size());

            SimpleFeatureType schema = granules.getSchema();
            assertNull(schema.getDescriptor("location"));
            assertNull(schema.getDescriptor("imageindex"));
            assertNotNull(schema.getDescriptor("the_geom"));
            assertNotNull(schema.getDescriptor("time"));

            SimpleFeature nc1, nc2;
            try (SimpleFeatureIterator it = granules.features()) {
                nc1 = it.next();
                nc2 = it.next();
            }

            assertEquals("2017-02-06 00:00:00.0", nc1.getAttribute("time").toString());
            assertEquals("2017-02-06 12:00:00.0", nc2.getAttribute("time").toString());
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }
}
