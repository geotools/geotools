package org.geotools.jdbc;

import junit.framework.TestCase;

import java.io.IOException;

public class JDBCDataStoreTest extends TestCase {
    public void testCheckAllInsertedPositive() throws IOException {
        JDBCDataStore.checkAllInserted(new int[0], 0);
        JDBCDataStore.checkAllInserted(new int[] {1,1,1}, 3);
        JDBCDataStore.checkAllInserted(new int[] {3}, 3);
        try {
            JDBCDataStore.checkAllInserted(new int[] {1, 1, 0}, 3);
            fail();
        } catch (IOException e) {
            // expected
        }
    }
}
