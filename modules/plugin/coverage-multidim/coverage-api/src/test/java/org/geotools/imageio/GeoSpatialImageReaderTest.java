/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.FileUtils;
import org.geotools.TestData;
import org.geotools.coverage.io.CoverageSourceDescriptor;
import org.geotools.coverage.io.RasterLayoutTest;
import org.geotools.coverage.io.TestCoverageSourceDescriptor;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/** @author Nicola Lagomarsini Geosolutions */
public class GeoSpatialImageReaderTest {

    /** Default Logger * */
    private static final Logger LOGGER = Logging.getLogger(GeoSpatialImageReaderTest.class);

    private static File file;

    private static String databaseName;

    private static File parentLocation;

    static final PrecisionModel PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING);

    static final GeometryFactory GEOM_FACTORY = new GeometryFactory(PRECISION_MODEL);

    @BeforeClass
    public static void setup() throws FileNotFoundException, IOException {
        // Setting of the input
        file = TestData.file(TestCoverageSourceDescriptor.class, "img.tiff");

        // connect to test catalog
        parentLocation = new File(TestData.file(TestCoverageSourceDescriptor.class, "."), "db");
        if (parentLocation.exists()) {
            FileUtils.deleteDirectory(parentLocation);
        }
        parentLocation.mkdir();
        databaseName = "test";
    }

    @Test
    public void testReader() throws IOException {
        // Reader creation
        GeoSpatialImageReader reader = new TestGeospatialImageReader();

        reader.setInput(file);

        // Test unsupported methods (that can return null!)
        assertNull(reader.getImageMetadata(0));
        assertNull(reader.getStreamMetadata());

        // Reader Catalog
        CoverageSlicesCatalog sliceCat = reader.getCatalog();
        assertNotNull(sliceCat);

        final Transaction t = new DefaultTransaction(Long.toString(System.nanoTime()));
        try {
            String[] typeNames = sliceCat.getTypeNames();
            assertNull(typeNames);

            // create new schema 1
            final String schemaDef1 = "the_geom:Polygon,coverage:String,imageindex:Integer";
            sliceCat.createType("1", schemaDef1);
            typeNames = sliceCat.getTypeNames();
            assertNotNull(typeNames);
            assertEquals(1, typeNames.length);

            // add features to it
            SimpleFeatureType schema = DataUtilities.createType("1", schemaDef1);
            SimpleFeature feat = DataUtilities.template(schema);
            feat.setAttribute("coverage", "a");
            feat.setAttribute("imageindex", Integer.valueOf(0));
            ReferencedEnvelope referencedEnvelope =
                    new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84);
            feat.setAttribute("the_geom", GEOM_FACTORY.toGeometry(referencedEnvelope));
            sliceCat.addGranule("1", feat, t);
            t.commit();

            // Check if present

            Query q = new Query("1");
            q.setFilter(Filter.INCLUDE);
            // Get ImageIndexes
            List<Integer> indexes = reader.getImageIndex(q);
            assertNotNull(indexes);
            assertTrue(!indexes.isEmpty());
        } catch (Exception e) {
            t.rollback();
        } finally {
            if (sliceCat != null) {
                sliceCat.dispose();
            }

            t.close();

            FileUtils.deleteDirectory(parentLocation);
        }

        String auxiliaryFilesPath = "file:/path";
        reader.setAuxiliaryFilesPath(auxiliaryFilesPath);
        assertTrue(reader.getAuxiliaryFilesPath().equalsIgnoreCase(auxiliaryFilesPath));

        // Disposal
        reader.dispose();
        try {
            reader.finalize();
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * A simple GeoSpatialImageReader only supporting 2 testCoverages: testCoverage1, testCoverage2
     */
    public static class TestGeospatialImageReader extends GeoSpatialImageReader {

        /** Default Logger * */
        private static final Logger LOGGER = Logging.getLogger(TestGeospatialImageReader.class);

        protected TestGeospatialImageReader() {
            super(new TestGeospatialImageReaderSpi());
        }

        private static Map<Name, CoverageSourceDescriptor> descriptors =
                new HashMap<Name, CoverageSourceDescriptor>();

        private static List<Name> coverageNames = new ArrayList<Name>();

        static {
            coverageNames.add(new NameImpl("testCoverage1"));
            coverageNames.add(new NameImpl("testCoverage2"));
            for (Name coverageName : coverageNames) {
                descriptors.put(
                        coverageName, new TestCoverageSourceDescriptor(coverageName.toString()));
            }
        }

        @Override
        public void addFile(String filePath) {}

        @Override
        public List<String> list() {
            return null;
        }

        @Override
        public void removeFile(String filePath) {}

        @Override
        public void purge() {}

        @Override
        public Collection<Name> getCoveragesNames() {
            return descriptors.keySet();
        }

        @Override
        public int getCoveragesNumber() {
            return descriptors.size();
        }

        @Override
        public CoverageSourceDescriptor getCoverageDescriptor(Name name) {
            if (descriptors.containsKey(name)) {
                return descriptors.get(name);
            }
            return null;
        }

        @Override
        public int getWidth(int imageIndex) throws IOException {
            return RasterLayoutTest.testRasterLayout.getWidth();
        }

        @Override
        public int getHeight(int imageIndex) throws IOException {
            return RasterLayoutTest.testRasterLayout.getHeight();
        }

        @Override
        public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex) throws IOException {
            return null;
        }

        @Override
        public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
            return null;
        }

        @Override
        public void setInput(Object input) {
            super.setInput(input);
            numImages = 1;
            try {
                initCatalog(parentLocation, databaseName);
            } catch (IOException e) {
                LOGGER.log(Level.FINER, e.getMessage(), e);
            }
        }
    }

    public static class TestGeospatialImageReaderSpi extends ImageReaderSpi {

        public static final Class<?>[] STANDARD_INPUT_TYPES =
                new Class[] {ImageInputStream.class, File.class, URL.class, URI.class};

        public static final String VENDOR_NAME = "GeoTools";

        /** Default Logger * */
        private static final Logger LOGGER = Logging.getLogger(TestGeospatialImageReaderSpi.class);

        static final String[] suffixes = new String[] {".tiff", ".tif"};

        static final String[] formatNames = new String[] {"TIFF", "TIF"};

        static final String[] MIMETypes = new String[] {"image/tiff", "image/geotiff"};

        static final String version = "1.0";

        static final String readerCN = "org.geotools.imageio.TestGeospatialImageReader";

        // writerSpiNames
        static final String[] wSN = {null};

        // StreamMetadataFormatNames and StreamMetadataFormatClassNames
        static final boolean supportsStandardStreamMetadataFormat = false;

        static final String nativeStreamMetadataFormatName = null;

        static final String nativeStreamMetadataFormatClassName = null;

        static final String[] extraStreamMetadataFormatNames = {null};

        static final String[] extraStreamMetadataFormatClassNames = {null};

        // ImageMetadataFormatNames and ImageMetadataFormatClassNames
        static final boolean supportsStandardImageMetadataFormat = false;

        static final String nativeImageMetadataFormatName = null;

        static final String nativeImageMetadataFormatClassName = null;

        static final String[] extraImageMetadataFormatNames = {null};

        static final String[] extraImageMetadataFormatClassNames = {null};

        /** Default Constructor * */
        public TestGeospatialImageReaderSpi() {
            super(
                    VENDOR_NAME,
                    version,
                    formatNames,
                    suffixes,
                    MIMETypes,
                    readerCN,
                    STANDARD_INPUT_TYPES,
                    wSN,
                    supportsStandardStreamMetadataFormat,
                    nativeStreamMetadataFormatName,
                    nativeStreamMetadataFormatClassName,
                    extraStreamMetadataFormatNames,
                    extraStreamMetadataFormatClassNames,
                    supportsStandardImageMetadataFormat,
                    nativeImageMetadataFormatName,
                    nativeImageMetadataFormatClassName,
                    extraImageMetadataFormatNames,
                    extraImageMetadataFormatClassNames);

            LOGGER.fine("TestGeospatialImageReaderSpi Constructor");
        }

        @Override
        public boolean canDecodeInput(Object source) throws IOException {
            return true;
        }

        @Override
        public ImageReader createReaderInstance(Object extension) throws IOException {
            return new TestGeospatialImageReader();
        }

        @Override
        public String getDescription(Locale locale) {
            return null;
        }
    }
}
