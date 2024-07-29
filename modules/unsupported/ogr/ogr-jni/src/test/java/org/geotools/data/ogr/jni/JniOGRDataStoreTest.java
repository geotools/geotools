package org.geotools.data.ogr.jni;

import org.geotools.data.ogr.OGRDataStoreTest;
import org.junit.Ignore;

public class JniOGRDataStoreTest extends OGRDataStoreTest {

    public JniOGRDataStoreTest() {
        super(JniOGRDataStoreFactory.class);
    }

    @Override
    public void testShapefileComparison() {
    }

    @Ignore
    @Override
    public void testOptimizedEnvelope() {
    }

    @Ignore
    @Override
    public void testSchemaPop() {
    }

    @Ignore
    @Override
    public void testAttributesWritingSqliteFromUpperCaseAttributes() {
    }

    @Ignore
    @Override
    public void testAttributesWritingSqliteWithSorting() {
    }

    @Ignore
    @Override
    public void testAttributesWritingSqlite() {
    }
}
