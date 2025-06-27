/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.TestData;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.geojson.GeoJSONWriter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Informal test used to document expected functionality for workshop.
 *
 * <p>This test has a setup method used to copy locations.csv to a temporary file.
 */
@RunWith(Parameterized.class)
public class GeoJSONWriteTest {
    private Locale previousLocale;

    File tmp;

    File file;

    URL url;

    GeoJSONDataStoreFactory fac = new GeoJSONDataStoreFactory();

    private static Logger log = Logging.getLogger(GeoJSONWriter.class);

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> locales() {
        return Arrays.asList(new Object[][] {{Locale.ENGLISH}, {Locale.ITALIAN}});
    }

    public GeoJSONWriteTest(Locale locale) {
        this.previousLocale = Locale.getDefault();
        Locale.setDefault(locale);
    }

    @After
    public void resetLocale() {
        Locale.setDefault(previousLocale);
    }

    @Before
    public void createTemporaryLocations() throws IOException {
        // Setting the system-wide default at startup time

        tmp = File.createTempFile("example", "");
        boolean exists = tmp.exists();
        if (exists) {
            // System.err.println("Removing tempfile " + tmp);
            if (!tmp.delete()) {
                throw new IOException("could not delete: " + tmp);
            }
        }
        boolean created = tmp.mkdirs();
        if (!created) {
            // System.err.println("Could not create " + tmp);
            return;
        }

        file = new File(tmp, "locations.json");

        URL resource = TestData.getResource(GeoJSONWriteTest.class, "locations.json");
        url = resource;
        if (url == null) throw new RuntimeException("Input datafile not found");
        // System.out.println("copying " + resource.toExternalForm() + " to " +
        // file);
        Files.copy(resource.openStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        url = URLs.fileToUrl(file);
    }

    private String checkFileContents(File modified) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Files.copy(modified.toPath(), baos);
        String contents = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        return contents;
    }

    @After
    public void removeTemporaryLocations() throws IOException {

        File[] list = tmp.listFiles();
        if (list == null) {
            throw new IOException("no directory " + tmp);
        }
        for (File value : list) {
            if (value.exists()) {
                if (!value.delete()) {
                    throw new IOException("could not delete: " + value);
                }
            }
        }
        if (!tmp.delete()) {
            throw new IOException("could not delete: " + tmp);
        }
    }

    @Test
    public void featureStoreExample() throws Exception {
        Map<String, Serializable> params = new HashMap<>();
        params.put(GeoJSONDataStoreFactory.URL_PARAM.key, url);
        DataStore store = fac.createDataStore(params);

        SimpleFeatureSource featureSource = store.getFeatureSource("locations");

        assertTrue("Modification not supported", featureSource instanceof SimpleFeatureStore);
        store.dispose();
    }

    @Test
    public void transactionExample() throws Exception {
        Map<String, Serializable> params = new HashMap<>();
        params.put(GeoJSONDataStoreFactory.URL_PARAM.key, url);
        DataStore store = fac.createDataStore(params);

        try (Transaction t1 = new DefaultTransaction("transaction 1");
                Transaction t2 = new DefaultTransaction("transactoin 2")) {

            SimpleFeatureType type = store.getSchema("locations");
            SimpleFeatureStore auto = (SimpleFeatureStore) store.getFeatureSource("locations");
            SimpleFeatureStore featureStore1 = (SimpleFeatureStore) store.getFeatureSource("locations");
            SimpleFeatureStore featureStore2 = (SimpleFeatureStore) store.getFeatureSource("locations");

            featureStore1.setTransaction(t1);
            featureStore2.setTransaction(t2);
            assertNotNull(featureStore1);
            assertNotNull(featureStore1.getFeatures());

            assertNotNull(featureStore2);
            assertNotNull(featureStore2.getFeatures());
            // Before we edit everything should be the same
            assertEquals("featureStore1 before", 9, featureStore1.getFeatures().size());
            assertEquals("featureStore2 before", 9, featureStore2.getFeatures().size());

            // select feature to remove
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

            Filter filter1 = ff.equal(ff.property("CITY"), ff.literal("Trento"), false);

            featureStore1.removeFeatures(filter1); // removes "Trento" from fs1

            // Tests after removal only featureStore1 is affected
            assertEquals(
                    "auto after featureStore1 removes fid1",
                    9,
                    auto.getFeatures().size());
            assertEquals(
                    "featureStore1 after featureStore1 removes fid1",
                    8,
                    featureStore1.getFeatures().size());
            assertEquals(
                    "featureStore2 after featureStore1 removes fid1",
                    9,
                    featureStore2.getFeatures().size());

            // new feature to add!
            // 45.52, -122.681944, Portland, 800, 2014
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            Point point = geometryFactory.createPoint(new Coordinate(-122.681944, 45.52));

            SimpleFeature feature = SimpleFeatureBuilder.build(
                    type, new Object[] {point, 45.52, -122.681944, "Portland", 800, 2014}, "feature-10");

            SimpleFeatureCollection collection = DataUtilities.collection(feature);

            featureStore2.addFeatures(collection);

            // Tests after adding the feature
            assertEquals(
                    "auto after featureStore1 removes Trento and featureStore2 adds Portland",
                    9,
                    auto.getFeatures().size());
            assertEquals(
                    "featureStore1 after featureStore1 removes fid1 and featureStore2 adds fid5",
                    8,
                    featureStore1.getFeatures().size());
            assertEquals(
                    "featureStore2 after featureStore1 removes fid1 and featureStore2 adds fid5",
                    10,
                    featureStore2.getFeatures().size());

            // commit transaction one
            t1.commit();
            // Tests after first commit
            int size = auto.getFeatures().size();
            assertEquals("auto after featureStore1 commits removal of fid1 (featureStore2 has added fid5)", 9, size);

            assertEquals(
                    "featureStore1 after commiting removal of fid1 (featureStore2 has added fid5)",
                    9,
                    featureStore1.getFeatures().size());
            assertEquals(
                    "featureStore2 after featureStore1 commits removal of fid1 (featureStore2 has added fid5)",
                    10,
                    featureStore2.getFeatures().size());

            // commit transaction two
            t2.commit();

            // Tests after 2nd commit

            assertEquals(
                    "auto after featureStore2 commits addition of fid5 (fid1 previously removed)",
                    10,
                    auto.getFeatures().size());

            assertEquals(
                    "featureStore1 after featureStore2 commits addition of fid5 (fid1 previously removed)",
                    10,
                    featureStore1.getFeatures().size());
            assertEquals(
                    "featureStore2 after commiting addition of fid5 (fid1 previously removed)",
                    10,
                    featureStore2.getFeatures().size());
        }
        store.dispose(); // clear out any listeners
    }

    @Test
    @SuppressWarnings("PMD.UseTryWithResources") // transaction needed in catch
    public void removeAllExample() throws Exception {
        Map<String, Serializable> params = new HashMap<>();
        params.put(GeoJSONDataStoreFactory.URL_PARAM.key, url);
        DataStore store = fac.createDataStore(params);

        Transaction t = new DefaultTransaction("locations");
        try {

            try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    store.getFeatureWriter("locations", Filter.INCLUDE, t)) {
                while (writer.hasNext()) {
                    writer.next();
                    writer.remove(); // marking contents for removal
                }
            }

            // Test the contents have been removed
            SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("locations");
            assertEquals(
                    "featureStore should be empty",
                    0,
                    featureStore.getFeatures().size());
            // Make sure the file is empty
            assertEquals("file should have no content", "", checkFileContents(file));
            t.commit();
        } catch (Throwable eek) {
            t.rollback();
        } finally {
            t.close();
            store.dispose();
        }
    }

    @Test
    public void replaceAll() throws Exception {
        Map<String, Serializable> params = new HashMap<>();
        params.put(GeoJSONDataStoreFactory.URL_PARAM.key, url);
        // System.out.println("fetching store from " + url);
        DataStore store = fac.createDataStore(params);

        final SimpleFeatureType type = store.getSchema("locations");
        assertNotNull(type);

        DefaultFeatureCollection collection = new DefaultFeatureCollection();

        // 45.52, -122.681944, Portland, 800, 2014
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        Point portland = gf.createPoint(new Coordinate(45.52, -122.681944));

        SimpleFeature f = SimpleFeatureBuilder.build(
                type, new Object[] {portland, 45.52, -122.681944, "Portland", 800, 2014}, "locations.1");

        collection.add(f);

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                store.getFeatureWriter("locations", Transaction.AUTO_COMMIT)) {

            // remove all features
            while (writer.hasNext()) {
                writer.next();
                writer.remove();
            }
            // copy new features in
            try (SimpleFeatureIterator iterator = collection.features()) {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    SimpleFeature newFeature = writer.next(); // new blank feature
                    newFeature.setAttributes(feature.getAttributes());
                    writer.write();
                }
            }
        } finally {

            store.dispose();
        }

        // Test everything was replaced by the one feature we added
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("locations");
        assertEquals(
                "featureStore should only have the one feature we created",
                1,
                featureStore.getFeatures().size());
        String contents = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\""
                + ":{\"LAT\":45.52,\"LON\":-122.681944,\"CITY\":\"Portland\",\"NUMBER\":800,\"YEAR\":2014},"
                + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[45.52,-122.681944]},\"id\":\"locations.0\"}]}";
        assertEquals(
                "Ensure the file has only the one feature we created",
                contents.trim(),
                checkFileContents(file).trim());
    }

    @Test
    public void copyContent() throws Exception {
        File directory = tmp;
        Map<String, Serializable> params = new HashMap<>();
        params.put(GeoJSONDataStoreFactory.URL_PARAM.key, url);
        DataStore store = fac.createDataStore(params);
        SimpleFeatureType featureType = store.getSchema("locations");

        File file2 = new File(directory, "duplicate.geojson");

        // System.out.println("copying to " + file2);
        Map<String, Serializable> params2 = new HashMap<>();
        params2.put(GeoJSONDataStoreFactory.FILE_PARAM.key, file2);

        GeoJSONDataStoreFactory factory = new GeoJSONDataStoreFactory();
        DataStore duplicate = factory.createNewDataStore(params2);
        duplicate.createSchema(featureType);

        SimpleFeature feature, newFeature;

        Query query = new Query(featureType.getTypeName(), Filter.INCLUDE);
        assertNotNull(duplicate);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        store.getFeatureReader(query, Transaction.AUTO_COMMIT);
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                        duplicate.getFeatureWriterAppend(duplicate.getTypeNames()[0], Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                feature = reader.next();
                newFeature = writer.next();

                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        }

        // test the new store is the same as the original
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("locations");
        assertEquals(9, featureStore.getFeatures().size());

        SimpleFeatureStore featureStored =
                (SimpleFeatureStore) duplicate.getFeatureSource(duplicate.getTypeNames()[0]);
        assertEquals(9, featureStored.getFeatures().size());

        try (SimpleFeatureIterator original = featureStore.getFeatures().features();
                SimpleFeatureIterator dups = featureStored.getFeatures().features()) {
            while (original.hasNext() && dups.hasNext()) {
                SimpleFeature o = original.next();
                SimpleFeature d = dups.next();

                for (int i = 0; i < o.getAttributeCount(); i++) {
                    assertTrue(
                            "" + o.getAttribute(i) + " doesn't equal " + d.getAttribute(i),
                            DataUtilities.attributesEqual(o.getAttribute(i), d.getAttribute(i)));
                }
            }

        } finally {
            store.dispose();
        }
    }

    @Test
    public void testBoundsInWriter() throws IOException {
        URL states = TestData.url("shapes/statepop.shp");
        FileDataStore ds = FileDataStoreFinder.getDataStore(states);
        assertNotNull(ds);
        String name = ds.getTypeNames()[0];
        File out = new File(tmp, "statepop.json");

        HashMap<String, Serializable> params = new HashMap<>();
        params.put(GeoJSONDataStoreFactory.FILE_PARAM.key, out);
        params.put(GeoJSONDataStoreFactory.WRITE_BOUNDS.key, true);
        GeoJSONDataStoreFactory factory = new GeoJSONDataStoreFactory();
        DataStore outDS = factory.createNewDataStore(params);
        SimpleFeature feature, newFeature;

        SimpleFeatureType featureType = ds.getSchema();
        outDS.createSchema(featureType);
        Query query = new Query(featureType.getTypeName(), Filter.INCLUDE);
        assertNotNull(outDS);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                        outDS.getFeatureWriterAppend(name, Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                feature = reader.next();
                newFeature = writer.next();

                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        }

        try (BufferedReader r = new BufferedReader(new FileReader(out, StandardCharsets.UTF_8))) {
            assertTrue("missing bbox", r.readLine().endsWith("\"bbox\":[-124.731422,24.955967,-66.969849,49.371735]}"));
        }
    }

    @Test
    public void testProvidedBoundsInWriter() throws IOException {
        URL states = TestData.url("shapes/statepop.shp");
        FileDataStore ds = FileDataStoreFinder.getDataStore(states);
        assertNotNull(ds);
        String name = ds.getTypeNames()[0];
        File out = new File(tmp, "statepop2.json");
        ReferencedEnvelope bbox = ds.getFeatureSource().getBounds();

        HashMap<String, Serializable> params = new HashMap<>();
        params.put(GeoJSONDataStoreFactory.FILE_PARAM.key, out);
        params.put(GeoJSONDataStoreFactory.WRITE_BOUNDS.key, true);
        params.put(GeoJSONDataStoreFactory.BOUNDING_BOX.key, bbox);
        GeoJSONDataStoreFactory factory = new GeoJSONDataStoreFactory();
        DataStore outDS = factory.createNewDataStore(params);
        SimpleFeature feature, newFeature;

        SimpleFeatureType featureType = ds.getSchema();
        outDS.createSchema(featureType);
        Query query = new Query(featureType.getTypeName(), Filter.INCLUDE);
        assertNotNull(outDS);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                        outDS.getFeatureWriterAppend(name, Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                feature = reader.next();
                newFeature = writer.next();

                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        }

        try (BufferedReader r = new BufferedReader(new FileReader(out, StandardCharsets.UTF_8))) {

            assertTrue(
                    "missing bbox",
                    r.readLine()
                            .startsWith(
                                    "{\"type\":\"FeatureCollection\",\"bbox\":[-124.731422,24.955967,-66.969849,49.371735],"));
        }
    }

    @Test
    public void testFeatureWrite() throws IOException {
        URL states = TestData.url("shapes/statepop.shp");
        FileDataStore ds = FileDataStoreFinder.getDataStore(states);
        assertNotNull(ds);
        SimpleFeature feature = DataUtilities.first(ds.getFeatureSource().getFeatures());
        String out = GeoJSONWriter.toGeoJSON(feature);
        /*String expected =
                "{\"type\":\"Feature\",\"properties\":{\"STATE_NAME\":\"Illinois\",\"STATE_FIPS\":\"17\",\"SUB_REGION\":\"E N Cen\",\"STATE_ABBR\":\"IL\",\"LAND_KM\":143986.61,\"WATER_KM\":1993.335,\"PERSONS\":1.1430602E7,\"FAMILIES\":2924880.0,\"HOUSHOLD\":4202240.0,\"MALE\":5552233.0,\"FEMALE\":5878369.0,\"WHITE\":8952978.0,\"BLACK\":1694273.0,\"AMIND\":21836.0,\"ASIAN\":285311.0,\"O_ETHNIC\":476204.0,\"HISPANIC\":904446.0,\"AGELT_05\":848141.0,\"AGE05_09\":836619.0,\"AGE10_14\":796468.0,\"AGE15_19\":818001.0,\"AGE20_24\":860087.0,\"AGE25_34\":1993185.0,\"AGE35_44\":1700144.0,\"AGE45_54\":1166727.0,\"AGE55_64\":974685.0,\"AGE65_74\":821940.0,\"AGE75_84\":467056.0,\"AGEGT_84\":147549.0,\"AGE15_17\":465138.0,\"AGE18_19\":352863.0,\"AGEIS_20\":179108.0,\"AGE21_24\":680979.0,\"AGEGE_03\":1.0920493E7,\"AGEGE_05\":1.0582461E7,\"AGEGE_16\":8796110.0,\"AGEGE_25\":7271286.0,\"SINGLE\":2579731.0,\"MARRIED\":4772524.0,\"SEPARATE\":179679.0,\"WIDOWED\":695486.0,\"DIVORCED\":721954.0,\"ONEPERHH\":1081113.0,\"MARRWICH\":1111080.0,\"MARRNOCH\":1160882.0,\"SINGWICH\":391840.0,\"SINGNOCH\":261078.0,\"PERINFAM\":9434116.0,\"CORRINST\":37334.0,\"NURSHOME\":93662.0,\"MENTAL\":4408.0,\"JUVENILE\":3601.0,\"O_INSTIT\":10837.0,\"DORMITOR\":86777.0,\"MILIQUAR\":16091.0,\"SHELTERS\":7481.0,\"INSTREET\":1755.0,\"O_NOINST\":25010.0,\"ONLYENGL\":9086726.0,\"ABLEENGL\":1190909.0,\"CANTENGL\":308203.0,\"NATIVE\":1.047833E7,\"NATURAL\":423665.0,\"NONCITIZ\":528607.0,\"BORN_INS\":7897755.0,\"BORNNORT\":279339.0,\"BORNMIDW\":1119466.0,\"BORNSOUT\":876628.0,\"BORNWEST\":178887.0,\"BORN_FOR\":1078527.0,\"SAMEHOUS\":5892150.0,\"SAMECNTY\":2980108.0,\"SAMESTAT\":843018.0,\"OTH_STAT\":667778.0,\"FRM_ABRD\":202784.0,\"WORKERS\":4199206.0,\"WRKINCTY\":4200428.0,\"WRKEXCTY\":1150983.0,\"DRVALONE\":3741715.0,\"CARPOOL\":652603.0,\"PUBTRANS\":538071.0,\"WRK_HOME\":144245.0,\"COM_LT15\":1556165.0,\"COM_1529\":1680072.0,\"COM_3044\":1045405.0,\"COM_GT44\":925524.0,\"PREPRIMA\":231774.0,\"ELEMSCND\":1951184.0,\"COLLEGE\":848715.0,\"NODIPLOM\":1735789.0,\"HIGHSCHL\":2187342.0,\"SOMECOLG\":1835803.0,\"COLGGRAD\":989808.0,\"GRADSCHL\":545188.0,\"ARMDFORC\":37285.0,\"EMPLOYED\":5417967.0,\"UNEMPLOY\":385040.0,\"NOTINWRK\":2956318.0,\"PRIMARY\":412055.0,\"MANUFACT\":1055047.0,\"UTILITY\":421035.0,\"TRADE\":1158062.0,\"FIRE\":431683.0,\"SERVICES\":479570.0,\"PROFSERV\":1249221.0,\"PUBL\":211294.0,\"EXECPROF\":1435613.0,\"TECHSALE\":1793289.0,\"SERVICE\":1360159.0,\"MANUAL\":828906.0,\"PRIVWORK\":4386916.0,\"PUBLWORK\":695545.0,\"SELFWORK\":314603.0,\"FAMIWORK\":20903.0,\"INC_LT15\":926140.0,\"INC_1525\":680073.0,\"INC_2535\":650027.0,\"INC_3550\":799945.0,\"INC_5075\":701381.0,\"INC_GT75\":440154.0,\"INC_AGGR\":1.50840734225E11,\"WAGE_SAL\":3287868.0,\"SELF_INC\":443552.0,\"FARM_INC\":102294.0,\"INTE_INC\":1869113.0,\"SOCS_INC\":1098818.0,\"PUBL_INC\":307015.0,\"RETI_INC\":606874.0,\"CHILDPOV\":495505.0,\"INPOVRTY\":1326731.0,\"P_MALE\":0.486,\"P_FEMALE\":0.514,\"P_WHITE\":0.783,\"P_BLACK\":0.148,\"P_AMIND\":0.002,\"P_ASIAN\":0.025,\"P_OETHNIC\":0.042,\"P_HISPANIC\":0.079,\"P_AGELT_05\":0.074,\"P_AGE05_09\":0.073,\"P_AGE10_14\":0.07,\"P_AGE15_17\":0.041,\"P_AGE18_19\":0.031,\"P_AGEIS_20\":0.016,\"P_AGE21_24\":0.06,\"P_AGE25_34\":0.174,\"P_AGE35_44\":0.149,\"P_AGE45_54\":0.102,\"P_AGE55_64\":0.085,\"P_AGE65_74\":0.072,\"P_AGE75_84\":0.041,\"P_AGEGT_84\":0.013,\"P_AGEGE_03\":0.955,\"P_AGEGE_05\":0.926,\"P_AGEGE_16\":0.77,\"P_AGEGE_25\":0.0,\"P_SINGLE\":0.226,\"P_MARRIED\":0.418,\"P_SEPARATE\":0.016,\"P_WIDOWED\":0.061,\"P_DIVORCED\":0.063,\"P_PERINFAM\":0.825,\"P_CORRINST\":0.003,\"P_NURSHOME\":0.008,\"P_MENTAL\":0.0,\"P_JUVENILE\":0.0,\"P_O_INSTIT\":0.001,\"P_O_NOINST\":0.002,\"P_DORMITOR\":0.008,\"P_MILIQUAR\":0.001,\"P_SHELTERS\":0.001,\"P_INSTREET\":0.0,\"P_ONLYENGL\":0.795,\"P_ABLEENGL\":0.104,\"P_CANTENGL\":0.027,\"P_NATIVE\":0.917,\"P_NATURAL\":0.037,\"P_NONCITIZ\":0.046,\"P_BORN_INS\":0.691,\"P_BORNNORT\":0.024,\"P_BORNMIDW\":0.098,\"P_BORNSOUT\":0.077,\"P_BORNWEST\":0.016,\"P_BORN_FOR\":0.094,\"P_SAMEHOUS\":0.515,\"P_SAMECNTY\":0.261,\"P_SAMESTAT\":0.074,\"P_OTH_STAT\":0.058,\"P_FRM_ABRD\":0.018,\"P_WORKERS\":0.367,\"P_PREPRIMA\":0.02,\"P_ELEMSCND\":0.171,\"P_COLLEGE\":0.074,\"P_NODIPLOM\":0.152,\"P_HIGHSCHL\":0.191,\"P_SOMECOLG\":0.161,\"P_COLGGRAD\":0.087,\"P_GRADSCHL\":0.048,\"P_ARMDFORC\":0.003,\"P_EMPLOYED\":0.474,\"P_UNEMPLOY\":0.034,\"P_NOTINWRK\":0.259,\"P_CHILDPOV\":0.043,\"P_INPOVRTY\":0.116,\"P_ONEPERHH\":0.257,\"P_MARRWICH\":0.264,\"P_MARRNOCH\":0.276,\"P_SINGWICH\":0.093,\"P_SINGNOCH\":0.062,\"P_INC_LT15\":0.22,\"P_INC_1525\":0.162,\"P_INC_2535\":0.155,\"P_INC_3550\":0.19,\"P_INC_5075\":0.167,\"P_INC_GT75\":0.105,\"P_WAGE_SAL\":0.782,\"P_SELF_INC\":0.106,\"P_FARM_INC\":0.024,\"P_INTE_INC\":0.445,\"P_SOCS_INC\":0.261,\"P_PUBL_INC\":0.073,\"P_RETI_INC\":0.144,\"P_WRKINCTY\":1.0,\"P_WRKEXCTY\":0.274,\"P_DRVALONE\":0.891,\"P_CARPOOL\":0.155,\"P_PUBTRANS\":0.128,\"P_WRK_HOME\":0.034,\"P_COM_LT15\":0.371,\"P_COM_1529\":0.4,\"P_COM_3044\":0.249,\"P_COM_GT44\":0.22,\"P_PRIMARY\":0.076,\"P_MANUFACT\":0.195,\"P_UTILITY\":0.078,\"P_TRADE\":0.214,\"P_FIRE\":0.08,\"P_SERVICES\":0.089,\"P_PROFSERV\":0.231,\"P_PUBLIC\":0.039,\"P_EXECPROF\":0.265,\"P_TECHSALE\":0.331,\"P_SERVICE\":0.251,\"P_MANUAL\":0.153,\"P_PRIVWORK\":0.81,\"P_PUBLWORK\":0.128,\"P_SELFWORK\":0.058,\"P_FAMIWORK\":0.004,\"INCPRCAP\":15201.0,\"INC_MEDN\":32252.0,\"PER_SAMP\":15.3,\"SAMP_POP\":1747776.0},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[-88.071564,37.51098999908843],[-88.087883,37.47627299908872],[-88.31170699999998,37.44285199908901],[-88.359177,37.409308999089276],[-88.419853,37.4202919990892],[-88.467644,37.40075699908936],[-88.511322,37.296851999090265],[-88.50142700000002,37.25778199909061],[-88.45069899999999,37.20566899909105],[-88.422516,37.15690999909148],[-88.45047,37.098670999092],[-88.476799,37.07214399909224],[-88.4907,37.06817999909228],[-88.51727300000002,37.0647699990923],[-88.559273,37.072814999092245],[-88.61421999999999,37.109046999091916],[-88.68837,37.13540999909167],[-88.73911299999999,37.14118199909162],[-88.74650600000001,37.15210699909154],[-88.863289,37.202193999091094],[-88.93250300000001,37.218406999090945],[-88.993172,37.220035999090925],[-89.065033,37.185859999091235],[-89.116821,37.112136999091895],[-89.14634700000002,37.09318499909206],[-89.169548,37.06423599909231],[-89.174332,37.02571099909267],[-89.15024599999998,36.9984399990929],[-89.12986,36.988112999093],[-89.193512,36.98677099909301],[-89.210052,37.02897299909262],[-89.237679,37.04173299909251],[-89.264053,37.087123999092114],[-89.284233,37.09124399909208],[-89.303291,37.08538399909212],[-89.30969999999999,37.06090899909234],[-89.26424400000002,37.02773299909264],[-89.262001,37.008685999092805],[-89.282768,36.9992069990929],[-89.310982,37.0096819990928],[-89.38295,37.04921299909244],[-89.37999,37.099082999092],[-89.423798,37.13720299909167],[-89.44052099999999,37.16531799909141],[-89.468216,37.224265999090896],[-89.465309,37.25373099909064],[-89.48959399999998,37.25600099909062],[-89.51388499999999,37.27640199909044],[-89.51388499999999,37.30496199909019],[-89.50057999999999,37.32944099908998],[-89.468742,37.33940899908989],[-89.435738,37.35571699908974],[-89.427574,37.411017999089275],[-89.45362100000001,37.453185999088916],[-89.49478100000002,37.491725999088594],[-89.524971,37.5719569990879],[-89.513367,37.61592899908754],[-89.51918,37.650374999087255],[-89.513374,37.67983999908701],[-89.521523,37.694797999086894],[-89.581436,37.70610399908679],[-89.66645800000002,37.74545299908647],[-89.675858,37.78396999908615],[-89.691055,37.80479399908599],[-89.728447,37.84099199908569],[-89.851715,37.90506399908517],[-89.86104599999999,37.90548699908516],[-89.866814,37.89187599908528],[-89.90055100000001,37.875903999085416],[-89.93787400000001,37.8780439990854],[-89.978912,37.91188399908512],[-89.958229,37.963633999084706],[-90.010811,37.96931799908466],[-90.041924,37.99320599908447],[-90.11933899999998,38.03227199908417],[-90.134712,38.05395099908401],[-90.207527,38.08890499908373],[-90.254059,38.12216899908348],[-90.289635,38.166816999083125],[-90.336716,38.18871299908295],[-90.36476900000001,38.23429899908259],[-90.36934700000002,38.3235589990819],[-90.35868799999999,38.3653299990816],[-90.33960700000002,38.39084599908142],[-90.30184200000001,38.42735699908114],[-90.26578500000001,38.51868799908046],[-90.26123,38.53276799908037],[-90.24094400000001,38.562804999080136],[-90.18370799999998,38.61027099907981],[-90.183578,38.65877199907945],[-90.20224,38.700362999079154],[-90.196571,38.72396499907897],[-90.16339899999998,38.77309799907864],[-90.13517800000001,38.78548399907854],[-90.121727,38.80050999907844],[-90.113121,38.830466999078226],[-90.132812,38.85303099907806],[-90.243927,38.914508999077654],[-90.278931,38.92471699907756],[-90.31973999999998,38.92490799907757],[-90.41307100000002,38.96232999907733],[-90.46984099999999,38.959178999077345],[-90.53042600000002,38.89160899907779],[-90.570328,38.87132599907795],[-90.62721299999998,38.880794999077885],[-90.668877,38.9352529990775],[-90.70607,39.0377919990768],[-90.707588,39.05817799907666],[-90.690399,39.09369999907644],[-90.716736,39.1442109990761],[-90.718193,39.19587299907575],[-90.73233800000001,39.22474699907557],[-90.73808300000002,39.2478099990754],[-90.77934299999998,39.296802999075105],[-90.850494,39.350451999074735],[-90.94789099999998,39.40058499907444],[-91.03633900000001,39.44441199907415],[-91.06438400000002,39.473983999073965],[-91.093613,39.52892699907363],[-91.156189,39.552592999073475],[-91.203247,39.60002099907318],[-91.317665,39.68591699907266],[-91.367088,39.72463999907243],[-91.373421,39.7612719990722],[-91.381714,39.80377199907198],[-91.449188,39.8630489990716],[-91.450989,39.88524199907149],[-91.434052,39.901828999071384],[-91.43038900000002,39.92183699907129],[-91.44724300000001,39.94606399907115],[-91.48728899999999,40.0057529990708],[-91.50400500000002,40.06671099907045],[-91.51612900000002,40.134543999070075],[-91.50654600000001,40.20045899906971],[-91.49893199999998,40.25137699906943],[-91.48669399999999,40.30962399906914],[-91.448593,40.37190199906882],[-91.41881599999999,40.38687499906872],[-91.38575700000001,40.39236099906869],[-91.372757,40.40298799906863],[-91.385399,40.447249999068404],[-91.37479400000001,40.503653999068135],[-91.38210300000001,40.528495999067985],[-91.41287199999998,40.54799299906789],[-91.411118,40.57297099906777],[-91.37560999999998,40.603438999067635],[-91.26206200000001,40.63954499906744],[-91.214912,40.64381799906743],[-91.162498,40.65631099906735],[-91.12915800000002,40.68214799906725],[-91.11998699999998,40.705401999067114],[-91.092751,40.761546999066844],[-91.08890499999998,40.83372899906651],[-91.04921000000002,40.87958499906628],[-90.983276,40.92392699906607],[-90.96070900000001,40.95050399906596],[-90.954651,41.070361999065405],[-90.95778700000001,41.104358999065255],[-90.990341,41.14437099906509],[-91.01825699999999,41.16582499906501],[-91.05632000000001,41.176257999064944],[-91.101524,41.23152199906472],[-91.10234799999999,41.26781799906457],[-91.07328,41.334895999064265],[-91.055786,41.40137899906399],[-91.027489,41.423507999063915],[-91.00069400000001,41.43108399906389],[-90.949654,41.42123399906392],[-90.844139,41.44462199906384],[-90.7799,41.4498209990638],[-90.708214,41.45006199906381],[-90.658791,41.46231799906377],[-90.60069999999999,41.50958599906359],[-90.54083999999999,41.52596999906351],[-90.454994,41.527545999063506],[-90.43496699999999,41.54357899906344],[-90.42300400000002,41.56727199906335],[-90.348366,41.586848999063264],[-90.339348,41.60279799906321],[-90.34113300000001,41.64908999906304],[-90.326027,41.722735999062756],[-90.304886,41.75646599906262],[-90.25531000000001,41.78173799906253],[-90.19583900000002,41.80613699906247],[-90.154518,41.930774999062024],[-90.14266999999998,41.983962999061816],[-90.150536,42.033427999061644],[-90.168098,42.061042999061584],[-90.166649,42.10374499906143],[-90.17608599999998,42.12050199906136],[-90.191574,42.122687999061384],[-90.23093400000002,42.159720999061236],[-90.32360100000001,42.19731899906112],[-90.367729,42.21020899906108],[-90.40717299999999,42.24264499906101],[-90.41798399999999,42.26392399906093],[-90.427681,42.34063299906069],[-90.441597,42.36007299906063],[-90.49104299999999,42.388782999060524],[-90.563583,42.42183699906045],[-90.60582700000002,42.46055999906032],[-90.64834599999999,42.475642999060284],[-90.651772,42.49469799906023],[-90.63832900000001,42.509360999060206],[-90.419975,42.508361999060206],[-89.92356900000001,42.50410799906022],[-89.834618,42.503459999060205],[-89.400497,42.49748999906023],[-89.359444,42.49790599906023],[-88.93907899999999,42.49086399906024],[-88.764954,42.490905999060246],[-88.70652,42.48965499906026],[-88.29789699999999,42.49196999906025],[-88.19470200000002,42.48961299906025],[-87.79731,42.48913199906026],[-87.836945,42.314212999060764],[-87.76023899999998,42.15645599906125],[-87.67054699999998,42.05982199906157],[-87.61262500000001,41.847331999062305],[-87.529861,41.72359099906276],[-87.53264600000001,41.46971499906375],[-87.532448,41.3013039990644],[-87.53173099999998,41.17375599906496],[-87.532021,41.00992999906568],[-87.532669,40.74541099906691],[-87.53716999999999,40.49460999906816],[-87.535675,40.48324599906824],[-87.535339,40.166194999069894],[-87.53577399999999,39.88730199907148],[-87.535576,39.609340999073126],[-87.538567,39.47744799907395],[-87.54021500000002,39.35052499907476],[-87.597664,39.33826799907483],[-87.625237,39.30740399907502],[-87.610619,39.2976609990751],[-87.615799,39.281417999075195],[-87.60689499999998,39.25816299907534],[-87.584564,39.2487529990754],[-87.588593,39.208465999075656],[-87.594208,39.19812799907574],[-87.607925,39.196067999075744],[-87.644257,39.16850699907594],[-87.670326,39.14667899907608],[-87.659454,39.130652999076176],[-87.662262,39.113467999076306],[-87.63166799999999,39.10394299907635],[-87.63086700000001,39.08897399907647],[-87.612007,39.084605999076494],[-87.58531999999998,39.06243499907663],[-87.581749,38.995742999077095],[-87.591858,38.994082999077094],[-87.54790499999999,38.97707699907723],[-87.53347,38.96370299907732],[-87.53018199999998,38.93191899907752],[-87.5392,38.90486099907773],[-87.559059,38.86981199907795],[-87.550507,38.85789099907803],[-87.507889,38.795558999078466],[-87.51902799999999,38.77669899907862],[-87.50800299999999,38.769721999078655],[-87.508316,38.73663299907891],[-87.543892,38.68597399907924],[-87.588478,38.67216899907934],[-87.625191,38.64281099907956],[-87.628647,38.622916999079706],[-87.619827,38.59920899907988],[-87.640594,38.59317799907991],[-87.65285500000002,38.57387199908005],[-87.67294299999999,38.54742399908026],[-87.65139000000002,38.51536899908048],[-87.653534,38.50044299908059],[-87.679909,38.50400499908058],[-87.692818,38.481532999080756],[-87.756096,38.46612499908086],[-87.758659,38.45709599908092],[-87.738953,38.44547999908101],[-87.748428,38.417964999081214],[-87.784019,38.378123999081524],[-87.834503,38.35252399908168],[-87.850082,38.28609799908221],[-87.86300700000001,38.2853619990822],[-87.874039,38.31678799908197],[-87.88344599999999,38.315551999081976],[-87.888466,38.30065899908208],[-87.91410800000001,38.28104799908223],[-87.913651,38.302344999082074],[-87.925919,38.304770999082045],[-87.980019,38.24108499908254],[-87.986008,38.23481399908258],[-87.97792799999999,38.20071399908285],[-87.932289,38.17113099908308],[-87.93199200000001,38.15752799908319],[-87.95056900000002,38.13691299908334],[-87.973503,38.13175999908339],[-88.018547,38.10330199908362],[-88.012329,38.09234599908368],[-87.964867,38.09674799908365],[-87.975296,38.07330699908384],[-88.034729,38.05408499908398],[-88.04309100000002,38.04511999908406],[-88.041473,38.03830299908413],[-88.021698,38.033530999084164],[-88.02921300000001,38.00823599908436],[-88.02170600000001,37.97505599908461],[-88.042511,37.95626399908477],[-88.04177100000001,37.93449799908495],[-88.06462099999999,37.92978299908498],[-88.078941,37.94399999908487],[-88.08400000000002,37.92365999908502],[-88.030441,37.91759099908507],[-88.026588,37.905757999085175],[-88.044868,37.896003999085245],[-88.10008199999999,37.90616999908516],[-88.101456,37.89530599908526],[-88.07573700000002,37.86780899908547],[-88.034241,37.84374599908566],[-88.042137,37.827521999085796],[-88.08926400000001,37.83124899908577],[-88.086029,37.817611999085884],[-88.035576,37.80568299908599],[-88.07247200000002,37.73540099908654],[-88.13363599999998,37.70074499908684],[-88.15937,37.660685999087164],[-88.157631,37.628478999087434],[-88.134171,37.583571999087816],[-88.071564,37.51098999908843]]]]},\"id\":\"statepop.1\"}";
        */
        assertTrue(out.contains("\"Illinois\""));
    }

    @Test
    public void testBoundsinStore() throws IOException {
        URL states = TestData.url("shapes/statepop.shp");
        FileDataStore ds = FileDataStoreFinder.getDataStore(states);
        assertNotNull(ds);
        String name = ds.getTypeNames()[0];
        File out = new File(tmp, "statepop.json");
        out.createNewFile();
        HashMap<String, Serializable> params = new HashMap<>();
        params.put(GeoJSONDataStoreFactory.FILE_PARAM.key, out);
        GeoJSONDataStoreFactory factory = new GeoJSONDataStoreFactory();
        DataStore outDS = factory.createNewDataStore(params);

        assertNotNull(outDS);
        SimpleFeatureType schema = ds.getSchema(name);

        outDS.createSchema(schema);

        SimpleFeatureSource featureSource = null;
        try {
            featureSource = outDS.getFeatureSource(schema.getTypeName());
        } catch (IOException e1) {
            log.log(Level.INFO, "Didn't get a featureSource", e1);
            fail();
        }

        assertTrue("Modification not supported", featureSource instanceof SimpleFeatureStore);
        SimpleFeatureStore store = (SimpleFeatureStore) featureSource;

        store.addFeatures(ds.getFeatureSource().getFeatures());
        ds.dispose();

        /*try (BufferedReader r = new BufferedReader(new FileReader(out))) {
            String line;
            while ((line = r.readLine()) != null) {
                //   System.out.println(line);
            }
        }*/
    }
}
