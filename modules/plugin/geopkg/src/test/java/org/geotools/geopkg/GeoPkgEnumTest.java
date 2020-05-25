package org.geotools.geopkg;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.EnumMapper;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.referencing.CRS;
import org.hamcrest.Matchers;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.FactoryException;

public class GeoPkgEnumTest extends JDBCTestSupport {

    @Override
    protected GeoPkgTestSetup createTestSetup() {
        return new GeoPkgTestSetup();
    }

    @Override
    protected HashMap createDataStoreFactoryParams() throws Exception {
        HashMap params = super.createDataStoreFactoryParams();
        // This test expects the write to happen right away. Disable buffering.
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 1);
        return params;
    }

    private void createEnumFeatureType() throws FactoryException, IOException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.options("one", "two", "three");
        builder.add(aname("enumProperty"), String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);
    }

    public void testCreateSchemaWithEnum() throws Exception {
        createEnumFeatureType();

        SimpleFeatureType ft2 = dataStore.getSchema(tname("ft2"));
        AttributeDescriptor enumDescriptor = ft2.getDescriptor(aname("enumProperty"));
        assertEquals(String.class, enumDescriptor.getType().getBinding());
        assertEquals(
                Arrays.asList("one", "two", "three"), FeatureTypes.getFieldOptions(enumDescriptor));

        // go low level and verify inputs
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
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);
        SimpleFeature f1 =
                fb.buildFeature(null, new Object[] {new WKTReader().read("POINT(0 0)"), 1, "one"});
        SimpleFeature f2 =
                fb.buildFeature(null, new Object[] {new WKTReader().read("POINT(0 0)"), 2, "two"});
        SimpleFeature f3 =
                fb.buildFeature(
                        null, new Object[] {new WKTReader().read("POINT(0 0)"), 3, "three"});
        SimpleFeature f4 =
                fb.buildFeature(null, new Object[] {new WKTReader().read("POINT(0 0)"), 4, null});

        store.addFeatures(new ListFeatureCollection(schema, new SimpleFeature[] {f1, f2, f3, f4}));
    }

    public void testReadEnums() throws Exception {
        // write some features
        writeEnumeratedFeatures();

        // read them back, check they are properly mapped
        FilterFactory ff = dataStore.getFilterFactory();
        SimpleFeatureSource source = dataStore.getFeatureSource(tname("ft2"));
        Query q = new Query();
        q.setSortBy(new SortBy[] {ff.sort(aname("enumProperty"), SortOrder.ASCENDING)});
        List<SimpleFeature> features = DataUtilities.list(source.getFeatures(q));
        assertEquals(4, features.size());
        assertEquals(null, features.get(0).getAttribute("enumProperty"));
        assertEquals("one", features.get(1).getAttribute("enumProperty"));
        assertEquals("two", features.get(2).getAttribute("enumProperty"));
        assertEquals("three", features.get(3).getAttribute("enumProperty"));
    }

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
        assertEquals(null, features.get(0).getAttribute("enumProperty"));
        assertEquals(4, features.get(0).getAttribute("intProperty"));
    }

    public void testUpdateAllEnums() throws Exception {
        // write some features
        writeEnumeratedFeatures();

        // now update all enum values
        FilterFactory ff = dataStore.getFilterFactory();
        SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(tname("ft2"));
        store.modifyFeatures("enumProperty", "one", Filter.INCLUDE);

        // read back and confirm
        Query q = new Query();
        q.setSortBy(new SortBy[] {ff.sort(aname("enumProperty"), SortOrder.ASCENDING)});
        List<SimpleFeature> features = DataUtilities.list(store.getFeatures());
        assertEquals(4, features.size());
        assertEquals("one", features.get(0).getAttribute("enumProperty"));
        assertEquals("one", features.get(1).getAttribute("enumProperty"));
        assertEquals("one", features.get(2).getAttribute("enumProperty"));
        assertEquals("one", features.get(3).getAttribute("enumProperty"));
    }
}
