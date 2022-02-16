package org.geotools.geopkg;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.EnumMapper;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.referencing.CRS;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.FactoryException;
import org.sqlite.SQLiteConfig;

public class GeoPkgEnumTest extends JDBCTestSupport {

    @Override
    protected GeoPkgTestSetup createTestSetup() {
        return new GeoPkgTestSetup();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        // This test expects the write to happen right away. Disable buffering.
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 1);
        return params;
    }

    private void createEnumFeatureType() throws FactoryException, IOException {
        SimpleFeatureType featureType = getEnumFeatureType("ft2");
        dataStore.createSchema(featureType);
    }

    private SimpleFeatureType getEnumFeatureType(String typeName) throws FactoryException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname(typeName));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.options("one", "two", "three");
        builder.add(aname("enumProperty"), String.class);
        return builder.buildFeatureType();
    }

    @Test
    public void testCreateSchemaWithEnum() throws Exception {
        createEnumFeatureType();

        SimpleFeatureType ft2 = dataStore.getSchema(tname("ft2"));
        AttributeDescriptor enumDescriptor = ft2.getDescriptor(aname("enumProperty"));
        assertEquals(String.class, enumDescriptor.getType().getBinding());
        assertEquals(
                Arrays.asList("one", "two", "three"), FeatureTypes.getFieldOptions(enumDescriptor));

        // go low level and verify inputs
        try (GeoPackage geoPackage = new GeoPackage(dataStore)) {
            GeoPkgSchemaExtension schemas = geoPackage.getExtension(GeoPkgSchemaExtension.class);
            List<GeoPkgExtension.Association> extensionAssociations = schemas.getAssociations();
            assertThat(
                    extensionAssociations,
                    Matchers.hasItem(new GeoPkgExtension.Association("gpkg_data_columns")));
            assertThat(
                    extensionAssociations,
                    Matchers.hasItem(
                            new GeoPkgExtension.Association("gpkg_data_column_constraints")));
            assertThat(
                    extensionAssociations,
                    Matchers.hasItem(new GeoPkgExtension.Association("ft2", "enumProperty")));
            List<DataColumn> dataColumns =
                    geoPackage.getExtension(GeoPkgSchemaExtension.class).getDataColumns("ft2");
            assertEquals(1, dataColumns.size());
            DataColumn dc = dataColumns.get(0);
            assertEquals("enumProperty", dc.getColumnName());
            assertEquals("enumProperty", dc.getName());
            DataColumnConstraint.Enum dcc = (DataColumnConstraint.Enum) dc.getConstraint();
            assertNotNull(dcc);
            assertEquals("ft2_enumProperty_enum", dcc.getName());
            Map<String, String> values = dcc.getValues();
            assertEquals(3, values.size());
            assertEquals("one", values.get("0"));
            assertEquals("two", values.get("1"));
            assertEquals("three", values.get("2"));

            // check also the enum map is there, to support reading
            EnumMapper mapper =
                    (EnumMapper) enumDescriptor.getUserData().get(JDBCDataStore.JDBC_ENUM_MAP);
            assertNotNull(mapper);
            assertEquals("one", mapper.fromInteger(0));
            assertEquals("two", mapper.fromInteger(1));
            assertEquals("three", mapper.fromInteger(2));
        }
    }

    public void createTwoSchemaWithEnum() throws Exception {
        // create two tables with the same columns
        dataStore.createSchema(getEnumFeatureType("ft2"));
        dataStore.createSchema(getEnumFeatureType("ft3"));

        // there used to be a key violation, just check that both enums have been recored
        @SuppressWarnings("PMD.CloseResource") // dataStore is closed elsewhere
        GeoPackage geoPackage = new GeoPackage(dataStore);
        GeoPkgSchemaExtension schemas = geoPackage.getExtension(GeoPkgSchemaExtension.class);
        Map<String, DataColumn> ft2Columns =
                schemas.getDataColumns("ft2").stream()
                        .collect(Collectors.toMap(dc -> dc.getColumnName(), dc -> dc));
        DataColumn ft2Enum = ft2Columns.get("enumProperty");
        assertThat(ft2Enum.getConstraint(), instanceOf(DataColumnConstraint.Enum.class));
        assertEquals("enumProperty", ft2Enum.getColumnName());

        Map<String, DataColumn> ft3Columns =
                schemas.getDataColumns("ft3").stream()
                        .collect(Collectors.toMap(dc -> dc.getColumnName(), dc -> dc));
        DataColumn ft3Enum = ft3Columns.get("enumProperty");
        assertThat(ft3Enum.getConstraint(), instanceOf(DataColumnConstraint.Enum.class));
        assertEquals("enumProperty", ft3Enum.getColumnName());
    }

    @Test
    public void testCreateSchemaWithCustomEnum() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        DataColumn dc = new DataColumn();
        dc.setColumnName("enumProperty");
        dc.setName("enumProperty");
        dc.setConstraint(new DataColumnConstraint.Enum("myEnum", "one", "two", "three"));
        builder.userData(GeoPackage.DATA_COLUMN, dc);
        builder.add(aname("enumProperty"), String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);

        SimpleFeatureType ft2 = dataStore.getSchema(tname("ft2"));
        AttributeDescriptor enumDescriptor = ft2.getDescriptor(aname("enumProperty"));
        assertEquals(String.class, enumDescriptor.getType().getBinding());
        assertEquals(
                Arrays.asList("one", "two", "three"), FeatureTypes.getFieldOptions(enumDescriptor));

        // go low level and verify inputs
        @SuppressWarnings("PMD.CloseResource") // dataStore is closed elsewhere
        GeoPackage geoPackage = new GeoPackage(dataStore);
        GeoPkgSchemaExtension schemas = geoPackage.getExtension(GeoPkgSchemaExtension.class);
        List<GeoPkgExtension.Association> extensionAssociations = schemas.getAssociations();
        assertThat(
                extensionAssociations,
                Matchers.hasItem(new GeoPkgExtension.Association("gpkg_data_columns")));
        assertThat(
                extensionAssociations,
                Matchers.hasItem(new GeoPkgExtension.Association("gpkg_data_column_constraints")));
        assertThat(
                extensionAssociations,
                Matchers.hasItem(new GeoPkgExtension.Association("ft2", "enumProperty")));
        List<DataColumn> dataColumns =
                geoPackage.getExtension(GeoPkgSchemaExtension.class).getDataColumns("ft2");
        assertEquals(1, dataColumns.size());
        DataColumn adc = dataColumns.get(0);
        assertEquals("enumProperty", adc.getColumnName());
        assertEquals("enumProperty", adc.getName());
        DataColumnConstraint.Enum dcc = (DataColumnConstraint.Enum) adc.getConstraint();
        assertNotNull(dcc);
        assertEquals("myEnum", dcc.getName());
        Map<String, String> values = dcc.getValues();
        assertEquals(3, values.size());
        assertEquals("one", values.get("0"));
        assertEquals("two", values.get("1"));
        assertEquals("three", values.get("2"));

        // check also the enum map is there, to support reading
        EnumMapper mapper =
                (EnumMapper) enumDescriptor.getUserData().get(JDBCDataStore.JDBC_ENUM_MAP);
        assertNotNull(mapper);
        assertEquals("one", mapper.fromInteger(0));
        assertEquals("two", mapper.fromInteger(1));
        assertEquals("three", mapper.fromInteger(2));
    }

    @Test
    public void testCreateSchemaWithSharedContraint() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        DataColumn dc = new DataColumn();
        dc.setColumnName("enumProperty");
        dc.setName("enumProperty");
        DataColumnConstraint.Enum constraint =
                new DataColumnConstraint.Enum("myEnum", "one", "two", "three");
        dc.setConstraint(constraint);
        builder.userData(GeoPackage.DATA_COLUMN, dc);
        builder.add(aname("enumProperty"), String.class);
        DataColumn dc2 = new DataColumn();
        dc2.setColumnName("enumProperty2");
        dc2.setName("enumProperty2");
        dc2.setConstraint(constraint);
        builder.userData(GeoPackage.DATA_COLUMN, dc2);
        builder.add(aname("enumProperty2"), String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);

        SimpleFeatureType ft2 = dataStore.getSchema(tname("ft2"));
        AttributeDescriptor enumDescriptor = ft2.getDescriptor(aname("enumProperty"));
        assertEquals(String.class, enumDescriptor.getType().getBinding());
        assertEquals(
                Arrays.asList("one", "two", "three"), FeatureTypes.getFieldOptions(enumDescriptor));
        AttributeDescriptor enumDescriptor2 = ft2.getDescriptor(aname("enumProperty2"));
        assertEquals(String.class, enumDescriptor2.getType().getBinding());
        assertEquals(
                Arrays.asList("one", "two", "three"),
                FeatureTypes.getFieldOptions(enumDescriptor2));

        // go low level and verify inputs
        @SuppressWarnings("PMD.CloseResource") // dataStore is closed elsewhere
        GeoPackage geoPackage = new GeoPackage(dataStore);
        GeoPkgSchemaExtension schemas = geoPackage.getExtension(GeoPkgSchemaExtension.class);
        List<GeoPkgExtension.Association> extensionAssociations = schemas.getAssociations();
        assertThat(
                extensionAssociations,
                Matchers.hasItem(new GeoPkgExtension.Association("gpkg_data_columns")));
        assertThat(
                extensionAssociations,
                Matchers.hasItem(new GeoPkgExtension.Association("gpkg_data_column_constraints")));
        assertThat(
                extensionAssociations,
                Matchers.hasItem(new GeoPkgExtension.Association("ft2", "enumProperty")));
        assertThat(
                extensionAssociations,
                Matchers.hasItem(new GeoPkgExtension.Association("ft2", "enumProperty2")));
        List<DataColumn> dataColumns =
                geoPackage.getExtension(GeoPkgSchemaExtension.class).getDataColumns("ft2");
        assertEquals(2, dataColumns.size());
        // first column
        DataColumn adc = dataColumns.get(0);
        assertEquals("enumProperty", adc.getColumnName());
        assertEquals("enumProperty", adc.getName());
        DataColumnConstraint.Enum dcc = (DataColumnConstraint.Enum) adc.getConstraint();
        assertNotNull(dcc);
        assertEquals("myEnum", dcc.getName());
        Map<String, String> values = dcc.getValues();
        assertEquals(3, values.size());
        assertEquals("one", values.get("0"));
        assertEquals("two", values.get("1"));
        assertEquals("three", values.get("2"));
        // second enumerated column, should have the same constraint
        DataColumn adc2 = dataColumns.get(1);
        assertEquals("enumProperty2", adc2.getColumnName());
        assertEquals("enumProperty2", adc2.getName());
        DataColumnConstraint.Enum dcc2 = (DataColumnConstraint.Enum) adc2.getConstraint();
        assertEquals(dcc2, dcc);
    }

    @Test
    public void testWriteEnums() throws Exception {
        writeEnumeratedFeatures();

        // check they have been written as 1, 2, 3
        String sql = "SELECT * FROM ft2 where enumProperty is not null order by enumProperty";
        try (Connection c = dataStore.getConnection(Transaction.AUTO_COMMIT);
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            assertTrue(rs.next());
            assertEquals(Integer.valueOf(0), rs.getObject("enumProperty"));
            assertTrue(rs.next());
            assertEquals(Integer.valueOf(1), rs.getObject("enumProperty"));
            assertTrue(rs.next());
            assertEquals(Integer.valueOf(2), rs.getObject("enumProperty"));
        }
    }

    private void writeEnumeratedFeatures() throws FactoryException, IOException, ParseException {
        createEnumFeatureType();

        SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(tname("ft2"));
        SimpleFeatureType schema = dataStore.getSchema(tname("ft2"));
        ListFeatureCollection features = getEnumeratedFeatureCollection(schema);

        store.addFeatures(features);
    }

    private ListFeatureCollection getEnumeratedFeatureCollection(SimpleFeatureType schema)
            throws ParseException {
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);
        SimpleFeature f1 = fb.buildFeature(null, new WKTReader().read("POINT(0 0)"), 1, "one");
        SimpleFeature f2 = fb.buildFeature(null, new WKTReader().read("POINT(0 0)"), 2, "two");
        SimpleFeature f3 = fb.buildFeature(null, new WKTReader().read("POINT(0 0)"), 3, "three");
        SimpleFeature f4 = fb.buildFeature(null, new WKTReader().read("POINT(0 0)"), 4, null);
        return new ListFeatureCollection(schema, new SimpleFeature[] {f1, f2, f3, f4});
    }

    @Test
    public void testReadEnums() throws Exception {
        // write some features
        writeEnumeratedFeatures();

        // read them back, check they are properly mapped
        FilterFactory ff = dataStore.getFilterFactory();
        SimpleFeatureSource source = dataStore.getFeatureSource(tname("ft2"));
        Query q = new Query();
        q.setSortBy(ff.sort(aname("enumProperty"), SortOrder.ASCENDING));
        List<SimpleFeature> features = DataUtilities.list(source.getFeatures(q));
        assertEquals(4, features.size());
        assertNull(features.get(0).getAttribute("enumProperty"));
        assertEquals("one", features.get(1).getAttribute("enumProperty"));
        assertEquals("two", features.get(2).getAttribute("enumProperty"));
        assertEquals("three", features.get(3).getAttribute("enumProperty"));
    }

    @Test
    public void testFilterOnEnumsPropValue() throws Exception {
        // write some features
        writeEnumeratedFeatures();

        // Filter on an enumerated attribute
        FilterFactory ff = dataStore.getFilterFactory();
        SimpleFeatureSource source = dataStore.getFeatureSource(tname("ft2"));
        Query q = new Query();
        q.setFilter(ff.equal(ff.property("enumProperty"), ff.literal("one"), false));
        List<SimpleFeature> features = DataUtilities.list(source.getFeatures(q));
        assertEquals(1, features.size());
        assertEquals("one", features.get(0).getAttribute("enumProperty"));
    }

    @Test
    public void testFilterOnEnumsValueProp() throws Exception {
        // write some features
        writeEnumeratedFeatures();

        // Filter on an enumerated attribute
        FilterFactory ff = dataStore.getFilterFactory();
        SimpleFeatureSource source = dataStore.getFeatureSource(tname("ft2"));
        Query q = new Query();
        q.setFilter(ff.equal(ff.literal("one"), ff.property("enumProperty"), false));
        List<SimpleFeature> features = DataUtilities.list(source.getFeatures(q));
        assertEquals(1, features.size());
        assertEquals("one", features.get(0).getAttribute("enumProperty"));
    }

    @Test
    public void testFilterOnEnumsNull() throws Exception {
        // write some features
        writeEnumeratedFeatures();

        // Filter on an enumerated attribute
        FilterFactory ff = dataStore.getFilterFactory();
        SimpleFeatureSource source = dataStore.getFeatureSource(tname("ft2"));
        Query q = new Query();
        q.setFilter(ff.isNull(ff.property("enumProperty")));
        List<SimpleFeature> features = DataUtilities.list(source.getFeatures(q));
        assertEquals(1, features.size());
        assertNull(features.get(0).getAttribute("enumProperty"));
        assertEquals(4, features.get(0).getAttribute("intProperty"));
    }

    @Test
    public void testUpdateAllEnums() throws Exception {
        // write some features
        writeEnumeratedFeatures();

        // now update all enum values
        FilterFactory ff = dataStore.getFilterFactory();
        SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(tname("ft2"));
        store.modifyFeatures("enumProperty", "one", Filter.INCLUDE);

        // read back and confirm
        Query q = new Query();
        q.setSortBy(ff.sort(aname("enumProperty"), SortOrder.ASCENDING));
        List<SimpleFeature> features = DataUtilities.list(store.getFeatures());
        assertEquals(4, features.size());
        assertEquals("one", features.get(0).getAttribute("enumProperty"));
        assertEquals("one", features.get(1).getAttribute("enumProperty"));
        assertEquals("one", features.get(2).getAttribute("enumProperty"));
        assertEquals("one", features.get(3).getAttribute("enumProperty"));
    }

    @Test
    public void testCreateFeatureEntryEnumExclusive() throws Exception {
        // custom configuration optimizing write performance for a straigth GeoPackage creation,
        // e.g., the use case of the GeoPackage WPS process in GeoServer
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.setJournalMode(SQLiteConfig.JournalMode.MEMORY);
        config.setPragma(SQLiteConfig.Pragma.SYNCHRONOUS, "OFF");
        config.setTransactionMode(SQLiteConfig.TransactionMode.DEFERRED);
        config.setReadUncommited(true);
        config.setLockingMode(SQLiteConfig.LockingMode.EXCLUSIVE);

        // create a geopackage that will be accessed in creation mode at top write speed
        File tempFile = File.createTempFile("geopkg-exclusive", "db", new File("target"));
        Map<String, Object> params = new HashMap<>();
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 10000);
        try (GeoPackage geopkg = new GeoPackage(tempFile, config, params)) {
            geopkg.init();

            SimpleFeatureType schema = getEnumFeatureType("ft2");
            ListFeatureCollection features = getEnumeratedFeatureCollection(schema);

            FeatureEntry entry = new FeatureEntry();
            entry.setBounds(features.getBounds());
            geopkg.add(entry, features);
            geopkg.createSpatialIndex(entry);
        }

        // read, check everything is fine
        params = new HashMap<>();
        params.put("dbtype", "geopkg");
        params.put("database", tempFile.getAbsolutePath());
        JDBCDataStore dataStore = (JDBCDataStore) DataStoreFinder.getDataStore(params);
        try {
            FilterFactory ff = dataStore.getFilterFactory();
            SimpleFeatureSource source = dataStore.getFeatureSource(tname("ft2"));
            Query q = new Query();
            q.setSortBy(ff.sort(aname("enumProperty"), SortOrder.ASCENDING));
            List<SimpleFeature> features = DataUtilities.list(source.getFeatures(q));
            assertEquals(4, features.size());
            assertNull(features.get(0).getAttribute("enumProperty"));
            assertEquals("one", features.get(1).getAttribute("enumProperty"));
            assertEquals("two", features.get(2).getAttribute("enumProperty"));
            assertEquals("three", features.get(3).getAttribute("enumProperty"));
        } finally {
            dataStore.dispose();
        }
    }

    private void createEnumArrayFeatureType() throws FactoryException, IOException {
        SimpleFeatureType featureType = getEnumArrayFeatureType();
        dataStore.createSchema(featureType);
    }

    private SimpleFeatureType getEnumArrayFeatureType() throws FactoryException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft_array"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("id"), Integer.class);
        builder.add(aname("intArrayProperty"), Integer[].class);
        builder.add(aname("strArrayProperty"), String[].class);
        builder.options("one", "two", "three");
        builder.add(aname("enumArrayProperty"), String[].class);
        return builder.buildFeatureType();
    }

    private void writeEnumeratedArrayFeatures()
            throws FactoryException, IOException, ParseException {
        createEnumArrayFeatureType();

        SimpleFeatureStore store =
                (SimpleFeatureStore) dataStore.getFeatureSource(tname("ft_array"));
        SimpleFeatureType schema = dataStore.getSchema(tname("ft_array"));
        ListFeatureCollection features = getEnumeratedArrayFeatureCollection(schema);

        store.addFeatures(features);
    }

    private ListFeatureCollection getEnumeratedArrayFeatureCollection(SimpleFeatureType schema)
            throws ParseException {
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);
        SimpleFeature f1 =
                fb.buildFeature(
                        null,
                        new WKTReader().read("POINT(0 0)"),
                        1,
                        new Integer[] {1},
                        new String[] {"a"},
                        new String[] {"one"});
        SimpleFeature f2 =
                fb.buildFeature(
                        null,
                        new WKTReader().read("POINT(0 0)"),
                        2,
                        new Integer[] {2, 2},
                        new String[] {"a", "b"},
                        new String[] {"one", "two"});
        SimpleFeature f3 =
                fb.buildFeature(
                        null,
                        new WKTReader().read("POINT(0 0)"),
                        3,
                        new Integer[] {3, 3, 3},
                        new String[] {"a", "b", "c"},
                        new String[] {"one", "two", "three"});
        SimpleFeature f4 =
                fb.buildFeature(null, new WKTReader().read("POINT(0 0)"), 4, null, null, null);
        return new ListFeatureCollection(schema, new SimpleFeature[] {f1, f2, f3, f4});
    }

    @Test
    public void testWriteArraysAndEnumsLowLevel() throws Exception {
        writeEnumeratedArrayFeatures();

        // check they have been written as JSON arrays, eventually packed
        String sql = "SELECT * FROM ft_array order by fid";
        try (Connection c = dataStore.getConnection(Transaction.AUTO_COMMIT);
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            assertTrue(rs.next());
            // TODO: clarify data type once we write the actual data type in the mime type
            // so far it's just using application/json, no way to tell the inner type
            assertEquals("[\"1\"]", rs.getObject("intArrayProperty"));
            assertEquals("[\"a\"]", rs.getObject("strArrayProperty"));
            assertEquals("[0]", rs.getObject("enumArrayProperty"));
            assertTrue(rs.next());
            assertEquals("[\"2\", \"2\"]", rs.getObject("intArrayProperty"));
            assertEquals("[\"a\", \"b\"]", rs.getObject("strArrayProperty"));
            assertEquals("[0, 1]", rs.getObject("enumArrayProperty"));
            assertTrue(rs.next());
            assertEquals("[\"3\", \"3\", \"3\"]", rs.getObject("intArrayProperty"));
            assertEquals("[\"a\", \"b\", \"c\"]", rs.getObject("strArrayProperty"));
            assertEquals("[0, 1, 2]", rs.getObject("enumArrayProperty"));
            assertTrue(rs.next());
            assertNull(rs.getObject("intArrayProperty"));
            assertNull(rs.getObject("strArrayProperty"));
            assertNull(rs.getObject("enumArrayProperty"));
        }
    }

    @Test
    public void testWriteArraysAndEnumsHighLevel() throws Exception {
        writeEnumeratedArrayFeatures();

        // check we can read back in array form, with enums expanded
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("ft_array"));
        Query q = new Query(tname("ft_array"));
        FilterFactory ff = dataStore.getFilterFactory();
        q.setSortBy(ff.sort("id", SortOrder.ASCENDING));
        List<SimpleFeature> features = DataUtilities.list(fs.getFeatures(q));

        // TODO: clarify data type once we write the actual data type in the mime type
        // so far it's just using application/json, no way to tell the inner type
        SimpleFeature f0 = features.get(0);
        assertArrayEquals(new String[] {"1"}, (String[]) f0.getAttribute("intArrayProperty"));
        assertArrayEquals(new String[] {"a"}, (String[]) f0.getAttribute("strArrayProperty"));
        assertArrayEquals(new String[] {"one"}, (String[]) f0.getAttribute("enumArrayProperty"));

        SimpleFeature f1 = features.get(1);
        assertArrayEquals(new String[] {"2", "2"}, (String[]) f1.getAttribute("intArrayProperty"));
        assertArrayEquals(new String[] {"a", "b"}, (String[]) f1.getAttribute("strArrayProperty"));
        assertArrayEquals(
                new String[] {"one", "two"}, (String[]) f1.getAttribute("enumArrayProperty"));

        SimpleFeature f2 = features.get(2);
        assertArrayEquals(
                new String[] {"3", "3", "3"}, (String[]) f2.getAttribute("intArrayProperty"));
        assertArrayEquals(
                new String[] {"a", "b", "c"}, (String[]) f2.getAttribute("strArrayProperty"));
        assertArrayEquals(
                new String[] {"one", "two", "three"},
                (String[]) f2.getAttribute("enumArrayProperty"));
    }
}
