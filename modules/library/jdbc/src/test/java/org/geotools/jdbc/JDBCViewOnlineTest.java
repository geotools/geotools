package org.geotools.jdbc;

import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public abstract class JDBCViewOnlineTest extends JDBCTestSupport {

    protected static final String LAKESVIEW = "lakesview";
    protected static final String LAKESVIEWPK = "lakesviewpk";
    protected static final String FID = "fid";
    protected static final String ID = "id";
    protected static final String NAME = "name";
    protected static final String GEOM = "geom";

    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    protected SimpleFeatureType lakeViewSchema;
    protected SimpleFeatureType lakeViewPkSchema;

    @Override
    protected abstract JDBCViewTestSetup createTestSetup();

    @Override
    protected void connect() throws Exception {
        super.connect();

        // we need to use the type builder because the pk has min occurs = 1 on Oracle
        AttributeTypeBuilder atb = new AttributeTypeBuilder();
        atb.setMinOccurs(isPkNillable() ? 0 : 1);
        atb.setMaxOccurs(1);
        atb.setNillable(isPkNillable());
        atb.setName(FID);
        atb.setBinding(Integer.class);
        AttributeDescriptor fidDescriptor = atb.buildDescriptor(FID);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setNamespaceURI(dataStore.getNamespaceURI());
        tb.setName(LAKESVIEW);
        tb.add(fidDescriptor);
        tb.add(ID, Integer.class);
        tb.add(GEOM, Polygon.class, (CoordinateReferenceSystem) null);
        tb.add(NAME, String.class);
        lakeViewSchema = tb.buildFeatureType();

        lakeViewPkSchema = tb.retype(lakeViewSchema, new String[] {ID, GEOM, NAME});
    }

    /**
     * Whether the pk field in a view is nillable or not (it is for most databases, but not for
     * Oracle for example).
     */
    protected boolean isPkNillable() {
        return true;
    }

    /** Whether the database supports primary keys defined on views (Oracle does) */
    protected boolean supportsPkOnViews() {
        return false;
    }

    public void testSchema() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname(LAKESVIEW));
        assertFeatureTypesEqual(lakeViewSchema, ft);
    }

    public void testSchemaPk() throws Exception {
        if (!supportsPkOnViews()) return;

        SimpleFeatureType ft = dataStore.getSchema(tname(LAKESVIEWPK));
        assertFeatureTypesEqual(lakeViewPkSchema, ft);
    }

    public void testReadFeatures() throws Exception {
        SimpleFeatureCollection fc = dataStore.getFeatureSource(tname(LAKESVIEW)).getFeatures();
        assertEquals(1, fc.size());
        try (SimpleFeatureIterator fr = fc.features()) {
            assertTrue(fr.hasNext());
            SimpleFeature f = fr.next();
            assertFalse(fr.hasNext());
        }
    }

    public void testGetBounds() throws Exception {
        // GEOT-2067 Make sure it's possible to compute bounds out of a view
        ReferencedEnvelope reference = dataStore.getFeatureSource(tname(LAKESVIEW)).getBounds();
        assertEquals(12.0, reference.getMinX());
        assertEquals(16.0, reference.getMaxX());
        assertEquals(4.0, reference.getMinY());
        assertEquals(8.0, reference.getMaxY());
    }

    /**
     * Subclasses may want to override this in case the database has a native way, other than the
     * pk, to identify a row
     */
    public void testReadOnly() throws Exception {
        try {
            dataStore.getFeatureWriter(tname(LAKESVIEW), Transaction.AUTO_COMMIT);
            fail("Should not be able to pick a writer without a pk");
        } catch (Exception e) {
            // ok, fine
        }

        assertFalse(dataStore.getFeatureSource(tname(LAKESVIEW)) instanceof FeatureStore);
    }
}
