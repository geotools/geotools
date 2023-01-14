/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.simple.SimpleFeatureLocking;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Test;
import org.opengis.filter.Filter;

public class TransformFeatureLockingTest extends AbstractTransformTest {

    @Test
    public void testFeatureLocking() throws Exception {
        // these are easy, selection and renaming, they can be inverted
        assertTrue(transformWithSelection() instanceof SimpleFeatureLocking);
        assertTrue(transformWithRename() instanceof SimpleFeatureLocking);
        // this one does not have any definition that can be inverted
        assertTrue(transformWithExpressions() instanceof SimpleFeatureSource);
    }

    @Test
    public void testLockOnSelection() throws Exception {
        performLockingTest(
                (SimpleFeatureLocking) transformWithSelection(STATES),
                (SimpleFeatureLocking) transformWithSelection(STATES2),
                CQL.toFilter("state_name = 'Delaware'"));
    }

    @Test
    public void testLockOnRename() throws Exception {
        performLockingTest(
                (SimpleFeatureLocking) transformWithRename(STATES),
                (SimpleFeatureLocking) transformWithRename(STATES2),
                CQL.toFilter("name = 'Delaware'"));
    }

    private void performLockingTest(
            SimpleFeatureLocking fl1, SimpleFeatureLocking fl2, Filter filter)
            throws CQLException, IOException {
        final FeatureLock lock1 = new FeatureLock("lock", 10 * 60 * 1000);
        final FeatureLock lock2 = new FeatureLock("lock", 10 * 60 * 1000);

        try (DefaultTransaction t1 = new DefaultTransaction();
                DefaultTransaction t2 = new DefaultTransaction()) {
            t1.addAuthorization(lock1.getAuthorization());
            fl1.setTransaction(t1);
            fl1.setFeatureLock(lock1);

            t2.addAuthorization(lock2.getAuthorization());
            fl2.setTransaction(t2);
            fl2.setFeatureLock(lock2);

            // first attempt will lock just the one Delaware, works with one but not the other
            assertEquals(1, fl1.lockFeatures(filter));
            assertEquals(0, fl2.lockFeatures(filter));

            // now unlock and try again, this time transformed2 should be able to lock
            fl1.unLockFeatures(filter);
            assertEquals(1, fl2.lockFeatures(filter));
            fl2.unLockFeatures(filter);
        }
    }
}
