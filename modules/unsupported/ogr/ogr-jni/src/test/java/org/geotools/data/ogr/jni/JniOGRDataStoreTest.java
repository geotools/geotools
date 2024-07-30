package org.geotools.data.ogr.jni;

import org.geotools.data.ogr.OGRDataStoreTest;
import org.junit.Test;

public class JniOGRDataStoreTest extends OGRDataStoreTest {

    public JniOGRDataStoreTest() {
        super(JniOGRDataStoreFactory.class);
    }

    @Test
    @Override
    public void testShapefileComparison() {}

    @Test
    @Override
    public void testOptimizedEnvelope() {}

    @Test
    @Override
    public void testSchemaPop() {}

    @Test
    @Override
    public void testAttributesWritingSqliteFromUpperCaseAttributes() {}

    @Test
    @Override
    public void testAttributesWritingSqliteWithSorting() {}

    @Test
    @Override
    public void testAttributesWritingSqlite() {}
}
