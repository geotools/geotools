/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2017, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General  License for more details.
 */
package org.geotools.data.db2;

import java.util.HashMap;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCSpatialFiltersOnlineTest;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.DWithin;

public class DB2SpatialFiltersOnlineTest extends JDBCSpatialFiltersOnlineTest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new DB2DataStoreAPITestSetup();
    }

    @Override
    protected void connect() throws Exception {
        super.connect();
        dataStore.setDatabaseSchema("geotools");
    }

    public void testGeometryCollection() throws Exception {
        PrecisionModel precisionModel = new PrecisionModel();

        int SRID = 4326;
        GeometryFactory gf = new GeometryFactory(precisionModel, SRID);
        Coordinate[] points = {new Coordinate(1, 1), new Coordinate(50, 60)};
        LineString[] geometries = new LineString[2];
        geometries[0] = gf.createLineString(points);
        Coordinate[] points2 = {new Coordinate(40, 30), new Coordinate(70, 40)};
        geometries[1] = gf.createLineString(points2);

        // TODO, DB2 does not support instantiating a geometry collection from wkb,
        // wkb type 7, replace GeometryCollection with MultiLineString
        // code in superclass: GeometryCollection geometry = new GeometryCollection(geometries,
        // factory );
        // For DB2, we must use the following line of code
        MultiLineString ml = gf.createMultiLineString(geometries);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        PropertyName p = ff.property(aname("geom"));
        Literal collect = ff.literal(ml);

        DWithin dwithinGeomCo = ((FilterFactory2) ff).dwithin(p, collect, 5, "meter");
        Query dq = new Query(tname("road"), dwithinGeomCo);
        SimpleFeatureCollection features =
                dataStore.getFeatureSource(tname("road")).getFeatures(dq);
        int numFeatures = features.size();
        assertEquals(2, numFeatures);

        Beyond beyondGeomCo = ((FilterFactory2) ff).beyond(p, collect, 5, "meter");
        dq = new Query(tname("road"), beyondGeomCo);
        features = dataStore.getFeatureSource(tname("road")).getFeatures(dq);
        numFeatures = features.size();
        assertEquals(1, numFeatures);
    }

    public void testBboxFilter() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should  match  "r2" and "r3"
        BBOX bbox = ff.bbox(aname("geom"), 2, 3, 4, 5, "EPSG:4326");
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(bbox);
        assertEquals(2, features.size());
    }

    public void testBboxFilterDefault() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should  match  "r2" and "r3"
        BBOX bbox = ff.bbox("", 2, 3, 4, 5, "EPSG:4326");
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(bbox);
        assertEquals(2, features.size());
    }

    @Override
    protected HashMap createDataStoreFactoryParams() throws Exception {
        HashMap params = super.createDataStoreFactoryParams();
        params.put(DB2NGDataStoreFactory.USE_SELECTIVITY.key, true);
        return params;
    }
}
