package org.geotools.data.ogr.jni;

import org.geotools.data.ogr.OGRDataStoreTest;
import org.junit.Ignore;

public class JniOGRDataStoreTest extends OGRDataStoreTest {

    public JniOGRDataStoreTest() {
        super(JniOGRDataStoreFactory.class);
    }

    @Ignore
    @Override
    public void testShapefileComparison() throws Exception {
        super.testShapefileComparison();
    }

    @Ignore
    @Override
    public void testOptimizedEnvelope() throws Exception {
        super.testOptimizedEnvelope();
    }

    @Ignore
    @Override
    public void testSchemaPop() throws Exception {
        super.testSchemaPop();
    }

    @Ignore
    @Override
    public void testAttributesWritingSqliteFromUpperCaseAttributes() throws Exception {
        super.testAttributesWritingSqliteFromUpperCaseAttributes();
    }

    @Ignore
    @Override
    public void testAttributesWritingSqliteWithSorting() throws Exception {
        super.testAttributesWritingSqliteWithSorting();
    }

    @Ignore
    @Override
    public void testAttributesWritingSqlite() throws Exception {
        super.testAttributesWritingSqlite();
    }
}
