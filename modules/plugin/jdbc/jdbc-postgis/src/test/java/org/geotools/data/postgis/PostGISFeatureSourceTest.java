/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;
import org.geotools.data.DefaultQuery;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCFeatureSourceTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;


/**
 * 
 *
 * @source $URL$
 */
public class PostGISFeatureSourceTest extends JDBCFeatureSourceTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISTestSetup();
    }
    
    
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
    }
    
    public void testEstimatedBounds() throws Exception {
        // enable fast bbox
        ((PostGISDialect) ((JDBCDataStore) dataStore).getSQLDialect()).setEstimatedExtentsEnabled(true);
        
        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds();
        assertEquals(0l, Math.round(bounds.getMinX()));
        assertEquals(0l, Math.round(bounds.getMinY()));
        assertEquals(2l, Math.round(bounds.getMaxX()));
        assertEquals(2l, Math.round(bounds.getMaxY()));
    
        assertTrue(areCRSEqual(CRS.decode("EPSG:4326"), bounds.getCoordinateReferenceSystem()));
    }
    
    public void testEstimatedBoundsWithQuery() throws Exception {
        // enable fast bbox
        ((PostGISDialect) ((JDBCDataStore) dataStore).getSQLDialect()).setEstimatedExtentsEnabled(true);
        
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        DefaultQuery query = new DefaultQuery();
        query.setFilter(filter);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds(query);
        assertEquals(1l, Math.round(bounds.getMinX()));
        assertEquals(1l, Math.round(bounds.getMinY()));
        assertEquals(1l, Math.round(bounds.getMaxX()));
        assertEquals(1l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(CRS.decode("EPSG:4326"), bounds.getCoordinateReferenceSystem()));
    }
    
    public void testSridFirstGeometry() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema(tname("ft3"));
        GeometryDescriptor gd = schema.getGeometryDescriptor();
        assertTrue(areCRSEqual(CRS.decode("EPSG:4326"), gd.getCoordinateReferenceSystem()));
    }

}
