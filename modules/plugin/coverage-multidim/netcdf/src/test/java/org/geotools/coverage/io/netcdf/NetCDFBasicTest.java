/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.core.CoreCommonImageMetadata;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.api.data.Query;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.io.CoverageSource.AdditionalDomain;
import org.geotools.coverage.io.CoverageSource.DomainType;
import org.geotools.coverage.io.CoverageSourceDescriptor;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultRepository;
import org.geotools.data.directory.DirectoryDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStoreFactory.ShpFileStoreFactory;
import org.geotools.feature.NameImpl;
import org.geotools.imageio.netcdf.AncillaryFileManager;
import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.imageio.netcdf.NetCDFImageReaderSpi;
import org.geotools.imageio.netcdf.Slice2DIndex;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.NetcdfDatasets;

/**
 * Testing Low level reader infrastructure.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public final class NetCDFBasicTest extends NetCDFBaseTest {

    private static final Logger LOGGER = Logger.getLogger(NetCDFBasicTest.class.toString());

    @Test
    public void testImageReaderPolyphemunsComplex() throws Exception {
        File file = null;
        try {
            file = TestData.file(this, "polyphemus_20130301.nc");
        } catch (IOException e) {
            LOGGER.warning("Unable to find file polyphemus_20130301.nc");
            return;
        }
        if (!file.exists()) {
            LOGGER.warning("Unable to find file polyphemus_20130301.nc");
            return;
        }
        final NetCDFImageReaderSpi unidataImageReaderSpi = new NetCDFImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        NetCDFImageReader reader = null;
        try {
            reader = (NetCDFImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            assertEquals(1008, numImages);
            for (int i = 0; i < numImages; i++) {
                Slice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }

            // check coverage names
            final List<Name> names = reader.getCoveragesNames();
            assertNotNull(names);
            assertFalse(names.isEmpty());
            assertEquals(3, names.size());
            assertTrue(names.contains(new NameImpl("NO2")));
            assertTrue(names.contains(new NameImpl("O3")));
            assertTrue(names.contains(new NameImpl("V")));

            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);

            // get typenames
            final String[] typeNames = cs.getTypeNames();
            for (String typeName : typeNames) {
                final List<CoverageSlice> granules = cs.getGranules(new Query(typeName, Filter.INCLUDE));
                assertNotNull(granules);
                assertFalse(granules.isEmpty());
                for (CoverageSlice slice : granules) {
                    final SimpleFeature sf = slice.getOriginator();
                    if (TestData.isInteractiveTest()) {
                        LOGGER.info(DataUtilities.encodeFeature(sf));
                    }

                    // checks
                    for (Property p : sf.getProperties()) {
                        assertNotNull("Property " + p.getName() + " had a null value!", p.getValue());
                    }
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void testImageReaderPolyphemusSimple() throws Exception {
        testImageReaderPolyphemusSimple(null);
    }

    @Test
    public void testImageReaderPolyphemusSimple2() throws Exception {
        // setup repository
        ShpFileStoreFactory dialect = new ShpFileStoreFactory(new ShapefileDataStoreFactory(), new HashMap<>());
        File indexDirectory = new File("./target/polyphemus_simple_idx");
        FileUtils.deleteQuietly(indexDirectory);
        indexDirectory.mkdir();
        File properties = new File(indexDirectory, "test.properties");
        String theStoreName = "testStore";
        FileUtils.writeStringToFile(properties, NetCDFUtilities.STORE_NAME + "=" + theStoreName, "UTF-8");

        DirectoryDataStore dataStore = new DirectoryDataStore(indexDirectory, dialect);

        DefaultRepository repository = new DefaultRepository();
        repository.register(new NameImpl(theStoreName), dataStore);

        testImageReaderPolyphemusSimple(reader -> {
            reader.setRepository(repository);
            reader.setAuxiliaryDatastorePath(properties.getAbsolutePath());
        });

        // the index files have actually been created
        List<String> typeNames = Arrays.asList(dataStore.getTypeNames());
        assertEquals(2, typeNames.size());
        assertTrue(typeNames.contains("O3"));
        assertTrue(typeNames.contains("NO2"));
        dataStore.dispose();
    }

    protected void testImageReaderPolyphemusSimple(Consumer<NetCDFImageReader> readerCustomizer) throws Exception {
        final File file = TestData.file(this, "O3-NO2.nc");
        final NetCDFImageReaderSpi unidataImageReaderSpi = new NetCDFImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        NetCDFImageReader reader = null;
        try {

            // checking low level
            reader = (NetCDFImageReader) unidataImageReaderSpi.createReaderInstance();
            if (readerCustomizer != null) {
                readerCustomizer.accept(reader);
            }
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            LOGGER.info("Found " + numImages + " images.");
            for (int i = 0; i < numImages; i++) {
                Slice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }

            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);

            // get typenames
            final String[] typeNames = cs.getTypeNames();
            for (String typeName : typeNames) {
                final List<CoverageSlice> granules = cs.getGranules(new Query(typeName, Filter.INCLUDE));
                assertNotNull(granules);
                assertFalse(granules.isEmpty());
                for (CoverageSlice slice : granules) {
                    final SimpleFeature sf = slice.getOriginator();
                    if (TestData.isInteractiveTest()) {
                        LOGGER.info(DataUtilities.encodeFeature(sf));
                    }

                    // checks
                    for (Property p : sf.getProperties()) {
                        assertNotNull("Property " + p.getName() + " had a null value!", p.getValue());
                    }
                }
            }

            // check metadata
            CoreCommonImageMetadata metadata = (CoreCommonImageMetadata) reader.getImageMetadata(0);
            assertEquals(80, metadata.getWidth());
            assertEquals(48, metadata.getHeight());
            assertEquals(80, metadata.getTileWidth());
            assertEquals(1, metadata.getTileHeight());
            assertEquals(48, metadata.getSampleModel().getNumBands());
            assertEquals(48, metadata.getNumBands());
            assertEquals(
                    "FloatDoubleColorModel", metadata.getColorModel().getClass().getSimpleName());
            assertEquals("EPSG:4326", metadata.getProjection());
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void testNoValid2DVariable() throws Exception {
        final File file = TestData.file(this, "noVars.nc");
        NetCDFImageReader reader = null;
        try (NetcdfDataset dataset = NetcdfDatasets.openDataset(file.getAbsolutePath())) {
            List<Variable> variables = dataset.getVariables();
            boolean speedVariableIsPresent = false;
            String speedVariableName = "";

            for (Variable variable : variables) {
                if (variable.getShortName().equals("spd")) {
                    speedVariableIsPresent = true;
                    speedVariableName = variable.getFullName();
                    break;
                }
            }

            assertTrue(speedVariableIsPresent);

            final NetCDFImageReaderSpi unidataImageReaderSpi = new NetCDFImageReaderSpi();
            assertTrue(unidataImageReaderSpi.canDecodeInput(file));

            // sample dataset containing a water_speed variable having
            // only time, depth dimensions. No lon/lat dims are present
            // resulting into variable not usable.
            reader = (NetCDFImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            final List<Name> names = reader.getCoveragesNames();

            boolean isSpeedCoverageAvailable = false;
            for (Name name : names) {
                if (name.toString().equals(speedVariableName)) {
                    isSpeedCoverageAvailable = true;
                    break;
                }
            }
            // Checking that only "mask" variable is found
            assertFalse(isSpeedCoverageAvailable);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    /** recursively delete indexes */
    private void removeIndexes(final File file) {
        if (file != null) {
            if (file.isFile()) {
                final String absolutePath = file.getAbsolutePath().toLowerCase();
                if (absolutePath.endsWith(".idx") || absolutePath.endsWith(".db")) {
                    file.delete();
                }
            } else {
                final File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        removeIndexes(f);
                    }
                }
            }
        }
    }

    private void cleanUp() throws FileNotFoundException, IOException {

        final File dir = TestData.file(this, ".");
        removeIndexes(dir);
    }

    @After
    public void tearDown() throws FileNotFoundException, IOException {
        if (TestData.isInteractiveTest()) {
            return;
        }
        cleanUp();
    }

    @Test
    public void testImageReaderIASI() throws Exception {
        File file = null;
        try {
            file = TestData.file(this, "IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc");
        } catch (IOException e) {
            LOGGER.warning("Unable to find file IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc");
            return;
        }
        if (!file.exists()) {
            LOGGER.warning("Unable to find file IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc");
            return;
        }
        final NetCDFImageReaderSpi unidataImageReaderSpi = new NetCDFImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        NetCDFImageReader reader = null;
        try {
            reader = (NetCDFImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            for (int i = 0; i < numImages; i++) {
                Slice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }

            // cloud_phase
            CoverageSourceDescriptor cd = reader.getCoverageDescriptor(new NameImpl("cloud_phase"));
            final List<AdditionalDomain> additionalDomains = cd.getAdditionalDomains();
            assertFalse(additionalDomains.isEmpty());
            final AdditionalDomain ad = additionalDomains.get(0);
            assertEquals(ad.getType(), DomainType.NUMBER);
            assertEquals("cloud_phase", ad.getName());

        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void testImageReaderGOME2() throws Exception {
        final File file = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY.nc");
        final NetCDFImageReaderSpi unidataImageReaderSpi = new NetCDFImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        NetCDFImageReader reader = null;
        try {

            // checking low level
            reader = (NetCDFImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            assertEquals(1, numImages);
            LOGGER.info("Found " + numImages + " images.");
            for (int i = 0; i < numImages; i++) {
                Slice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }

            // check coverage names
            final List<Name> names = reader.getCoveragesNames();
            assertNotNull(names);
            assertFalse(names.isEmpty());
            assertEquals(1, names.size());
            assertEquals("NO2", names.get(0).toString());

            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);

            // get typenames
            final String[] typeNames = cs.getTypeNames();
            for (String typeName : typeNames) {
                final List<CoverageSlice> granules = cs.getGranules(new Query(typeName, Filter.INCLUDE));
                assertNotNull(granules);
                assertFalse(granules.isEmpty());
                for (CoverageSlice slice : granules) {
                    final SimpleFeature sf = slice.getOriginator();
                    if (TestData.isInteractiveTest()) {
                        LOGGER.info(DataUtilities.encodeFeature(sf));
                    }

                    // checks
                    for (Property p : sf.getProperties()) {
                        final String pName = p.getName().toString();
                        if (!pName.equalsIgnoreCase("time") && !pName.equalsIgnoreCase("elevation")) {
                            assertNotNull("Property " + p.getName() + " had a null value!", p.getValue());
                        } else {
                            assertNull("Property " + p.getName() + " did not have a null value!", p.getValue());
                        }
                    }
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void testImageReaderGOME2AncillaryFiles() throws Exception {
        final File file = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY.nc");
        final NetCDFImageReaderSpi unidataImageReaderSpi = new NetCDFImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        NetCDFImageReader reader = null;
        try {

            // checking low level
            reader = (NetCDFImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            assertEquals(1, numImages);
            LOGGER.info("Found " + numImages + " images.");
            for (int i = 0; i < numImages; i++) {
                Slice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }

            // check coverage names
            final List<Name> names = reader.getCoveragesNames();
            assertNotNull(names);
            assertFalse(names.isEmpty());
            assertEquals(1, names.size());
            assertEquals("NO2", names.get(0).toString());

            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(file.getCanonicalPath().getBytes(StandardCharsets.UTF_8));
            String hashCode = AncillaryFileManager.convertToHex(md.digest());

            // Check if the auxiliary files directory is present
            File parentDir = file.getParentFile();

            String auxiliaryDirPath =
                    parentDir + File.separator + "." + FilenameUtils.getBaseName(file.getName()) + "_" + hashCode;

            File auxiliaryDir = new File(auxiliaryDirPath);

            assertTrue(auxiliaryDir.exists());
            assertTrue(auxiliaryDir.isDirectory());

            // Check if the Auxiliary File Directory contains the origin.txt file
            FilenameFilter nameFileFilter = FileFilterUtils.nameFileFilter("origin.txt");
            File[] files = auxiliaryDir.listFiles(nameFileFilter);
            assertNotNull(files);
            assertTrue(files[0].exists());
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void testImageReaderAscat() throws Exception {
        File file = null;
        try {
            file = TestData.file(this, "ascatl1.nc");
        } catch (IOException e) {
            LOGGER.warning("Unable to find file ascatl1.nc");
            return;
        }
        if (!file.exists()) {
            LOGGER.warning("Unable to find file ascatl1.nc");
            return;
        }
        final NetCDFImageReaderSpi unidataImageReaderSpi = new NetCDFImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        NetCDFImageReader reader = null;
        try {
            reader = (NetCDFImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            for (int i = 0; i < numImages; i++) {
                Slice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }

            // check coverage names
            final List<Name> names = reader.getCoveragesNames();
            assertNotNull(names);
            assertFalse(names.isEmpty());
            assertEquals(2, names.size());
            assertEquals("cell_index", names.get(0).toString());
            assertEquals("f_land", names.get(1).toString());

            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);

            // get typenames
            final String[] typeNames = cs.getTypeNames();
            for (String typeName : typeNames) {
                final List<CoverageSlice> granules = cs.getGranules(new Query(typeName, Filter.INCLUDE));
                assertNotNull(granules);
                assertFalse(granules.isEmpty());
                for (CoverageSlice slice : granules) {
                    final SimpleFeature sf = slice.getOriginator();
                    if (TestData.isInteractiveTest()) {
                        LOGGER.info(DataUtilities.encodeFeature(sf));
                    }

                    // checks
                    for (Property p : sf.getProperties()) {
                        final String pName = p.getName().toString();
                        if (!pName.equalsIgnoreCase("time") && !pName.equalsIgnoreCase("elevation")) {
                            assertNotNull("Property " + p.getName() + " had a null value!", p.getValue());
                        } else {
                            assertNull("Property " + p.getName() + " did not have a null value!", p.getValue());
                        }
                    }
                }
            }

        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    /** */
    private void spitOutSliceInformation(int i, Slice2DIndex sliceIndex) {
        if (TestData.isInteractiveTest()) {
            String variableName = sliceIndex.getVariableName();
            StringBuilder sb = new StringBuilder();
            sb.append("\n").append("\n").append("\n");
            sb.append("IMAGE: ").append(i).append("\n");
            sb.append(" Variable Name = ").append(variableName);
            sb.append(" ( Z = ");
            sb.append(sliceIndex.getNIndex(0));
            sb.append("; T = ");
            sb.append(sliceIndex.getNIndex(1));
            sb.append(")");
            LOGGER.info(sb.toString());
        }
    }

    @Test
    public void testImageReaderPolyphemunsComplex2() throws Exception {
        File file = null;
        try {
            file = TestData.file(this, "polyphemus_20130301.nc");
        } catch (IOException e) {
            LOGGER.warning("Unable to find file polyphemus_20130301.nc");
            return;
        }
        if (!file.exists()) {
            LOGGER.warning("Unable to find file polyphemus_20130301.nc");
            return;
        }
        FileUtils.copyFile(file, new File(TestData.file(this, null), "polyphemus.nc"));
        file = TestData.file(this, "polyphemus.nc");
        final NetCDFImageReaderSpi unidataImageReaderSpi = new NetCDFImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        NetCDFImageReader reader = null;
        try {
            reader = (NetCDFImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            assertEquals(1008, numImages);
            for (int i = 0; i < numImages; i++) {
                Slice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }

            // check dimensions
            CoverageSourceDescriptor cd = reader.getCoverageDescriptor(new NameImpl("NO2"));
            final List<AdditionalDomain> additionalDomains = cd.getAdditionalDomains();
            assertNull(additionalDomains);

            final List<DimensionDescriptor> dimensions = cd.getDimensionDescriptors();
            assertNotNull(dimensions);
            assertFalse(dimensions.isEmpty());
            assertEquals("wrong dimensions", 2, dimensions.size());
            DimensionDescriptor dim = dimensions.get(0);
            assertEquals("TIME", dim.getName());
            assertEquals("time", dim.getStartAttribute());
            dim = dimensions.get(1);
            assertEquals("ELEVATION", dim.getName());
            assertEquals("z", dim.getStartAttribute());

            // check coverage names
            final List<Name> names = reader.getCoveragesNames();
            assertNotNull(names);
            assertFalse(names.isEmpty());
            assertEquals(3, names.size());
            assertTrue(names.contains(new NameImpl("NO2")));
            assertTrue(names.contains(new NameImpl("O3")));
            assertTrue(names.contains(new NameImpl("V")));

            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);

            // get typenames
            final String[] typeNames = cs.getTypeNames();
            for (String typeName : typeNames) {
                final List<CoverageSlice> granules = cs.getGranules(new Query(typeName, Filter.INCLUDE));
                assertNotNull(granules);
                assertFalse(granules.isEmpty());
                for (CoverageSlice slice : granules) {
                    final SimpleFeature sf = slice.getOriginator();
                    if (TestData.isInteractiveTest()) {
                        LOGGER.info(DataUtilities.encodeFeature(sf));
                    }

                    // checks
                    for (Property p : sf.getProperties()) {
                        assertNotNull("Property " + p.getName() + " had a null value!", p.getValue());
                    }
                }
            }
        } finally {

            // close reader
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }

            // specific clean up
            FileUtils.deleteDirectory(TestData.file(this, ".polyphemus"));
        }
    }

    @Test
    public void testReadRegularNetCDF() throws IOException {
        NetCDFImageReaderSpi readerSpi = new NetCDFImageReaderSpi();
        File file = null;
        String name = "2DLatLonCoverageHDF5.nc";
        try {
            file = TestData.file(this, name);
        } catch (IOException e) {
            warnNoFile(name);
            return;
        }
        assertTrue(readerSpi.canDecodeInput(file));
    }

    @Test
    public void testReadNcML() throws IOException {
        NetCDFImageReaderSpi readerSpi = new NetCDFImageReaderSpi();
        File file = null;
        String name = "2DLatLonCoverage.ncml";
        try {
            file = TestData.file(this, name);
        } catch (IOException e) {
            warnNoFile(name);
            return;
        }
        assertTrue(readerSpi.canDecodeInput(file));
    }

    @Test
    public void testReadNC4() throws IOException {
        NetCDFImageReaderSpi readerSpi = new NetCDFImageReaderSpi();
        boolean isNC4available = NetCDFUtilities.isNC4CAvailable();
        if (!isNC4available) {
            LOGGER.warning("NetCDF4 reading test will be skipped due to "
                    + "missing NetCDF C library.\nIf you want test to be executed, make sure you have "
                    + "added the NetCDF C libraries location to the PATH environment variable");
            return;
        }
        String name = "temperatureisobaricNC4.nc";
        File file = null;
        try {
            file = TestData.file(this, name);
        } catch (IOException e) {
            warnNoFile(name);
            return;
        }
        assertTrue(readerSpi.canDecodeInput(file));
    }

    /** We can NOT read a CDL file */
    @Test
    public void testReadCDL() throws IOException {
        NetCDFImageReaderSpi readerSpi = new NetCDFImageReaderSpi();
        File file = null;
        String name = "2DLatLonCoverage.cdl";
        try {
            file = TestData.file(this, name);
        } catch (IOException e) {
            warnNoFile(name);
            return;
        }
        assertFalse(readerSpi.canDecodeInput(file));
    }

    private void warnNoFile(String name) {
        LOGGER.warning("Unable to find file " + name);
    }

    @Test
    public void testNetCDFWithDifferentTimeDimensions() throws IOException {
        // Selection of the input file
        final File workDir = new File(TestData.file(this, "."), "times");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }

        FileUtils.copyFile(TestData.file(this, "times.zip"), new File(workDir, "times.zip"));
        TestData.unzipFile(this, "times/times.zip");

        final File inputFile = TestData.file(this, "times/times.nc");
        // Get format
        final AbstractGridFormat format =
                GridFormatFinder.findFormat(inputFile.toURI().toURL(), null);
        final NetCDFReader reader = new NetCDFReader(inputFile, null);
        Assert.assertNotNull(format);
        Assert.assertNotNull(reader);
        try {
            // Selection of all the Coverage names
            String[] names = reader.getGridCoverageNames();
            assertNotNull(names);
            assertEquals(2, names.length);

            // Parsing metadata values
            assertEquals("true", reader.getMetadataValue(names[0], "HAS_TIME_DOMAIN"));

            List<DimensionDescriptor> descriptors = reader.getDimensionDescriptors(names[0]);
            assertEquals(1, descriptors.size());
            DimensionDescriptor descriptor = descriptors.get(0);
            assertEquals("time", descriptor.getStartAttribute());
            assertEquals("TIME", descriptor.getName());

            descriptors = reader.getDimensionDescriptors(names[1]);
            assertEquals(1, descriptors.size());
            descriptor = descriptors.get(0);
            assertEquals("time1", descriptor.getStartAttribute());
            assertEquals("TIME", descriptor.getName());

            assertEquals("true", reader.getMetadataValue(names[1], "HAS_TIME_DOMAIN"));
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
            FileUtils.deleteDirectory(TestData.file(this, "times"));
        }
    }

    @Test
    public void testNetCDFCoordinateAxisOrder() throws MalformedURLException, IOException {
        final File inputFile = TestData.file(this, "axisorder.nc");
        // Get format
        final AbstractGridFormat format =
                GridFormatFinder.findFormat(inputFile.toURI().toURL(), null);
        final NetCDFReader reader = new NetCDFReader(inputFile, null);
        Assert.assertNotNull(format);
        Assert.assertNotNull(reader);
        try {
            // Selection of all the Coverage names
            String[] names = reader.getGridCoverageNames();
            assertNotNull(names);
            assertEquals(1, names.length);

            // Parsing metadata values
            assertEquals("true", reader.getMetadataValue(names[0], "HAS_TIME_DOMAIN"));

            List<DimensionDescriptor> descriptors = reader.getDimensionDescriptors(names[0]);
            assertEquals(1, descriptors.size());
            DimensionDescriptor descriptor = descriptors.get(0);
            assertEquals("time", descriptor.getStartAttribute());
            assertEquals("TIME", descriptor.getName());
            assertEquals(
                    "1983-09-28T00:00:00.000Z/1983-09-28T00:00:00.000Z",
                    reader.getMetadataValue(names[0], "TIME_DOMAIN"));

        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }
}
