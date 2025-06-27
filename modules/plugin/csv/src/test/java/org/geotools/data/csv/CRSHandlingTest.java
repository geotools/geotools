package org.geotools.data.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CRSHandlingTest {

    private static File tmp;
    private static File statesfile;
    private static DataStore referenceStore;

    /** Check that CRS are handled correctly */
    @Before
    public void setUp() throws Exception {}

    @AfterClass
    public static void cleanUp() {
        if (tmp.exists()) {
            File[] allContents = tmp.listFiles();
            if (allContents != null) {
                for (File file : allContents) {
                    file.delete();
                }
            }
            tmp.delete();
        }
    }

    @BeforeClass
    public static void createTemporaryLocations() throws IOException {
        tmp = File.createTempFile("example", "");
        boolean exists = tmp.exists();
        if (exists) {
            tmp.delete();
        }
        boolean created = tmp.mkdirs();
        if (!created) {
            System.exit(1);
        }
        statesfile = new File(tmp, "coastal2.csv");
        File statesfilep = new File(tmp, "coastal2.prj");

        URL resource = TestData.getResource(CSVWriteTest.class, "coastal2.csv");
        URL resourcep = TestData.getResource(CSVWriteTest.class, "coastal2.prj");
        Files.copy(resource.openStream(), statesfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(resourcep.openStream(), statesfilep.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Map<String, Object> params = new HashMap<>();
        params.put(CSVDataStoreFactory.FILE_PARAM.key, statesfile.toString());
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params.put(CSVDataStoreFactory.WKTP.key, "WKT");
        referenceStore = DataStoreFinder.getDataStore(params);
    }

    @Test
    public void testPrjFileRead()
            throws FileNotFoundException, IOException, NoSuchAuthorityCodeException, FactoryException {
        File f = TestData.file(this, "coastal2.csv");
        Map<String, Object> params = new HashMap<>();
        params.put(CSVDataStoreFactory.FILE_PARAM.key, f.toString());
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params.put(CSVDataStoreFactory.WKTP.key, "WKT");

        DataStore store = DataStoreFinder.getDataStore(params);
        String name = store.getTypeNames()[0];
        CoordinateReferenceSystem crs = store.getFeatureSource(name).getSchema().getCoordinateReferenceSystem();
        CoordinateReferenceSystem expected = CRS.decode("EPSG:27700");
        assertEquals(
                expected.getIdentifiers().iterator().next().getCode(),
                crs.getIdentifiers().iterator().next().getCode());
    }

    @Test
    public void testWritePrj() throws IOException, FactoryException {
        File f = new File(tmp, "testcrs.csv");
        Map<String, Object> params = new HashMap<>();
        params.put(CSVDataStoreFactory.FILE_PARAM.key, f.toString());
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params.put(CSVDataStoreFactory.WKTP.key, "WKT");
        params.put(CSVDataStoreFactory.WRITEPRJ.key, "true");
        DataStore store = DataStoreFinder.getDataStore(params);
        String typeName = referenceStore.getTypeNames()[0];
        SimpleFeatureType schema = referenceStore.getSchema(typeName);
        store.createSchema(schema);
        SimpleFeatureSource source = store.getFeatureSource(store.getTypeNames()[0]);
        if (!(source instanceof SimpleFeatureStore)) {
            fail("can't create output file");
        }
        SimpleFeatureStore outstore = (SimpleFeatureStore) source;
        outstore.addFeatures(referenceStore.getFeatureSource(typeName).getFeatures());
        store.dispose();
        String prjName = FilenameUtils.getBaseName(f.getName()) + ".prj";
        File prj = new File(f.getParent(), prjName);
        assertTrue(prj.exists());

        CoordinateReferenceSystem expected = schema.getCoordinateReferenceSystem();

        try (BufferedReader reader = new BufferedReader(new FileReader(prj, StandardCharsets.UTF_8))) {
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String string = buffer.toString();

            CoordinateReferenceSystem crs = CRS.parseWKT(string);
            assertTrue(CRS.equalsIgnoreMetadata(expected, crs));
        }
    }
}
