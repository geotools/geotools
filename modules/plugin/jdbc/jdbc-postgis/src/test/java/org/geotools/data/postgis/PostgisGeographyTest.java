/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCGeographyTest;
import org.geotools.jdbc.JDBCGeographyTestSetup;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.DWithin;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class PostgisGeographyTest extends JDBCGeographyTest {

    @Override
    protected JDBCGeographyTestSetup createTestSetup() {
        return new PostgisGeographyTestSetup(new PostGISTestSetup());
    }

    @Override
    public void testSchema() throws Exception {
        super.testSchema();
        
        if (!isGeographySupportAvailable()) {
            return;
        }
        
        // extra check, pg specific: the native typename is actually geography
        SimpleFeatureType ft = dataStore.getFeatureSource(tname("geopoint")).getSchema();
        assertEquals("geography", ft.getGeometryDescriptor().getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME));
    }
    
    // As reported in GEOS-4384 (http://jira.codehaus.org/browse/GEOS-4384)
    public void testDWithinOGCUnits() throws Exception {
        validateOGCUnitUsage(10000, "m");
        validateOGCUnitUsage(10000, "metre");
        validateOGCUnitUsage(10000, "meters");
        validateOGCUnitUsage(10000 * 1000, "mm");
        validateOGCUnitUsage(10, "kilometers");
        validateOGCUnitUsage(10, "km");
        validateOGCUnitUsage(10, "kilometer");
        validateOGCUnitUsage(10000 / 0.0254, "in");
        validateOGCUnitUsage(10000 / 0.3048, "feet");
        validateOGCUnitUsage(10000 / 0.3048, "foot");
        validateOGCUnitUsage(10000 / 0.3048, "ft");
        validateOGCUnitUsage(10000 / 1609.344, "mi");
        validateOGCUnitUsage(10000 / 1609.344, "mile");
        validateOGCUnitUsage(10000 / 1609.344, "miles");
        validateOGCUnitUsage(10000 / 1852, "NM");
      }

    private void validateOGCUnitUsage(double distance, String unit) throws Exception {
        Coordinate coordinate = new Coordinate(-110, 30);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(coordinate);
        Geometry[] geometries = {point};
        GeometryCollection geometry = new GeometryCollection(geometries, factory );

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        PropertyName geomName = ff.property(aname("geo"));
        Literal lit = ff.literal(geometry);
        
        DWithin dwithinGeomFilter  = ((FilterFactory2) ff).dwithin(geomName, lit, distance, unit);
        Query query = new Query(tname("geopoint"), dwithinGeomFilter);
        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("geopoint")).getFeatures(query);
        assertEquals(1, features.size());
        checkSingleResult(features, "Town");
    }
    
    protected void checkSingleResult(FeatureCollection features, String name) {
        assertEquals(1, features.size());
        FeatureIterator fr = features.features();
        assertTrue(fr.hasNext());
        SimpleFeature f = (SimpleFeature) fr.next();
        assertNotNull(f);
        assertEquals(name, f.getAttribute(aname("name")));
        assertFalse(fr.hasNext());
        fr.close();
    }
}
