/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCSpatialFiltersTest;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.DWithin;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class OracleSpatialFiltersTest extends JDBCSpatialFiltersTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new OracleDataStoreAPITestSetup(new OracleTestSetup());
    }

    public void testLooseBboxFilter() throws Exception {
        ((OracleDialect) dataStore.getSQLDialect()).setLooseBBOXEnabled(true);
        
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should match only "r2"
        BBOX bbox = ff.bbox(aname("geom"), 2, 3.5, 4, 4.5, "EPSG:4326");
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(bbox);
        checkSingleResult(features, "r2");
    }

    // As reported in GEOS-4384 (http://jira.codehaus.org/browse/GEOS-4384)
    public void testSDODWithinOGCUnits() throws Exception {
        // express the same distance in different ways and check results
        validateOGCUnitUsage(10, "kilometers");
        validateOGCUnitUsage(10, "km");
        validateOGCUnitUsage(10, "kilometer");
        // this one does not work... not sure why 
        // validateOGCUnitUsage(10000 * 1000, "mm");
        validateOGCUnitUsage(10000, "m");
        validateOGCUnitUsage(10000, "metre");
        validateOGCUnitUsage(10000, "meters");
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
        Coordinate coordinate = new Coordinate(3.031, 2.754);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(coordinate);
        Geometry[] geometries = {point};
        GeometryCollection geometry = new GeometryCollection(geometries, factory );

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        PropertyName geomName = ff.property(aname("geom"));
        Literal lit = ff.literal(geometry);
        
        DWithin dwithinGeomFilter  = ((FilterFactory2) ff).dwithin(geomName, lit, distance, unit);
        Query query = new Query(tname("road"), dwithinGeomFilter);
        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(query);
        assertEquals(1, features.size());
        checkSingleResult(features, "r2");
    }

}
