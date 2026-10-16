/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs.clickhouse;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.DefaultTransaction;
import org.geotools.dggs.DGGSInstance;
import org.geotools.dggs.datastore.DGGSDataStore;
import org.geotools.dggs.h3.H3DGGSFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.jdbc.JDBCDataStore;
import org.locationtech.jts.geom.Polygon;

/** H3 time series table with an UInt16 resolution, hex Int64 zone ids and DateTime columns, as found in the field. */
@SuppressWarnings("PMD.UnitTestShouldUseTestAnnotation")
public class ClickHouseH3TimeSeriesOnlineTest extends ClickHouseOnlineTestCase<Long> {

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    private static final String TABLE = "sample_table";

    @Override
    protected String getDGGSId() {
        return new H3DGGSFactory().getId();
    }

    @Override
    protected void setupTestData(DGGSDataStore<Long> dataStore) throws Exception {
        try (Connection cx = ((JDBCDataStore) dataStore.getDelegate()).getConnection(Transaction.AUTO_COMMIT);
                Statement st = cx.createStatement()) {
            st.execute("DROP TABLE IF EXISTS " + TABLE);
            st.execute(
                    """
                    CREATE TABLE sample_table (
                        resolution UInt16,
                        zoneId Int64,
                        up_time DateTime,
                        down_time DateTime,
                        value Float64,
                        created_at DateTime
                    )
                    ENGINE = MergeTree
                    ORDER BY (resolution, zoneId, up_time)""");
            // three hourly batches over the same 13 cells, a res 1 cell with its res 2 children and their res 3
            // children
            st.execute(
                    """
                    INSERT INTO sample_table VALUES
                    (1, 0x8148bffffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 50, '2026-10-09 13:00:00'),
                    (2, 0x8248a7fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 35, '2026-10-09 13:00:00'),
                    (3, 0x8348a3fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 30, '2026-10-09 13:00:00'),
                    (3, 0x8348a2fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 35, '2026-10-09 13:00:00'),
                    (3, 0x8348a6fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 25, '2026-10-09 13:00:00'),
                    (2, 0x8248b7fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 50, '2026-10-09 13:00:00'),
                    (3, 0x8348b4fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 50, '2026-10-09 13:00:00'),
                    (3, 0x8348b6fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 35, '2026-10-09 13:00:00'),
                    (3, 0x8348b1fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 30, '2026-10-09 13:00:00'),
                    (2, 0x824897fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 25, '2026-10-09 13:00:00'),
                    (3, 0x834895fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 10, '2026-10-09 13:00:00'),
                    (3, 0x834894fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 15, '2026-10-09 13:00:00'),
                    (3, 0x834891fffffffff, '2026-10-09 12:00:00', '2026-10-09 12:00:00', 25, '2026-10-09 13:00:00'),

                    (1, 0x8148bffffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 55, '2026-10-09 14:00:00'),
                    (2, 0x8248a7fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 40, '2026-10-09 14:00:00'),
                    (3, 0x8348a3fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 35, '2026-10-09 14:00:00'),
                    (3, 0x8348a2fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 40, '2026-10-09 14:00:00'),
                    (3, 0x8348a6fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 30, '2026-10-09 14:00:00'),
                    (2, 0x8248b7fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 55, '2026-10-09 14:00:00'),
                    (3, 0x8348b4fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 55, '2026-10-09 14:00:00'),
                    (3, 0x8348b6fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 40, '2026-10-09 14:00:00'),
                    (3, 0x8348b1fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 35, '2026-10-09 14:00:00'),
                    (2, 0x824897fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 30, '2026-10-09 14:00:00'),
                    (3, 0x834895fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 15, '2026-10-09 14:00:00'),
                    (3, 0x834894fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 20, '2026-10-09 14:00:00'),
                    (3, 0x834891fffffffff, '2026-10-09 12:00:00', '2026-10-09 13:00:00', 30, '2026-10-09 14:00:00'),

                    (1, 0x8148bffffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 60, '2026-10-09 15:00:00'),
                    (2, 0x8248a7fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 45, '2026-10-09 15:00:00'),
                    (3, 0x8348a3fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 40, '2026-10-09 15:00:00'),
                    (3, 0x8348a2fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 45, '2026-10-09 15:00:00'),
                    (3, 0x8348a6fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 35, '2026-10-09 15:00:00'),
                    (2, 0x8248b7fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 60, '2026-10-09 15:00:00'),
                    (3, 0x8348b4fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 60, '2026-10-09 15:00:00'),
                    (3, 0x8348b6fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 45, '2026-10-09 15:00:00'),
                    (3, 0x8348b1fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 40, '2026-10-09 15:00:00'),
                    (2, 0x824897fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 35, '2026-10-09 15:00:00'),
                    (3, 0x834895fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 20, '2026-10-09 15:00:00'),
                    (3, 0x834894fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 25, '2026-10-09 15:00:00'),
                    (3, 0x834891fffffffff, '2026-10-09 12:00:00', '2026-10-09 14:00:00', 35, '2026-10-09 15:00:00')""");
        }
    }

    public void testSchema() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema(TABLE);
        assertEquals(7, schema.getAttributeDescriptors().size());
        assertBinding(schema, "resolution", Integer.class);
        assertBinding(schema, "zoneId", Long.class);
        assertBinding(schema, "up_time", Timestamp.class);
        assertBinding(schema, "down_time", Timestamp.class);
        assertBinding(schema, "value", Double.class);
        assertBinding(schema, "created_at", Timestamp.class);
        assertBinding(schema, "geometry", Polygon.class);
    }

    private void assertBinding(SimpleFeatureType schema, String name, Class<?> binding) {
        assertNotNull(name + " not found", schema.getDescriptor(name));
        assertEquals(name, binding, schema.getDescriptor(name).getType().getBinding());
    }

    public void testCountByResolution() throws Exception {
        Query query = new Query(TABLE, FF.equals(FF.property("resolution"), FF.literal(3)));
        assertEquals(27, dataStore.getFeatureSource(TABLE).getCount(query));
    }

    public void testReadThroughTransaction() throws Exception {
        // a non auto-commit transaction makes gt-jdbc call setAutoCommit(false), unsupported by jdbc-v2
        try (Transaction tx = new DefaultTransaction();
                FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        dataStore.getDelegate().getFeatureReader(new Query(TABLE), tx)) {
            int count = 0;
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
            assertEquals(39, count);
        }
    }

    public void testMaxValueByZone() throws Exception {
        GroupByVisitor visitor =
                new GroupByVisitor(Aggregate.MAX, FF.property("value"), List.of(FF.property("geometry")), null);
        dataStore.getFeatureSource(TABLE).getFeatures().accepts(visitor, null);

        @SuppressWarnings("PMD.CloseResource") // the store manages the dggs lifecycle
        DGGSInstance<Long> dggs = dataStore.getDggs();
        Polygon res1 =
                dggs.getZone(Long.parseUnsignedLong("8148bffffffffff", 16)).getBoundary();
        @SuppressWarnings("unchecked")
        Map<List<Object>, Object> results = visitor.getResult().toMap();
        assertEquals(13, results.size());
        assertEquals(60d, ((Number) results.get(List.of(res1))).doubleValue(), 0d);
    }
}
