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
package org.geotools.jdbc;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/library/jdbc/src/test/java/org/geotools/
 *         jdbc/JDBCEmptyGeometryTest.java $
 */
public abstract class JDBCEmptyGeometryTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCEmptyGeometryTestSetup createTestSetup();

    public void testEmptyPoint() throws Exception {
        testInsertEmptyGeometry("POINT");
    }
    
    public void testEmptyLine() throws Exception {
        testInsertEmptyGeometry("LINESTRING");
    }

    public void testEmptyPolygon() throws Exception {
        testInsertEmptyGeometry("POLYGON");
    }
    
    public void testEmptyMultiPoint() throws Exception {
        testInsertEmptyGeometry("MULTIPOINT");
    }
    
    public void testEmptyMultiLine() throws Exception {
        testInsertEmptyGeometry("MULTILINESTRING");
    }
    
    public void testEmptyMultiPolygon() throws Exception {
        testInsertEmptyGeometry("MULTIPOLYGON");
    }
    
    private void testInsertEmptyGeometry(String type) throws Exception {
        WKTReader reader = new WKTReader();
        Geometry emptyGeometry = reader.read(type.toUpperCase() + " EMPTY");

        Transaction tx = new DefaultTransaction();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriterAppend(
                tname("empty"), tx);
        SimpleFeature feature = writer.next();
        feature.setAttribute(aname("id"), new Integer(100));
        feature.setAttribute(aname("geom_" + type.toLowerCase()), emptyGeometry);
        feature.setAttribute(aname("name"), new String("empty " + type));
        writer.write();
        writer.close();
        tx.commit();
        tx.close();

        SimpleFeatureCollection fc = dataStore.getFeatureSource(tname("empty")).getFeatures();
        assertEquals(1, fc.size());
        SimpleFeatureIterator fi = fc.features();
        SimpleFeature nf = fi.next();
        fi.close();
        Geometry geometry = (Geometry) nf.getDefaultGeometry();
        // either null or empty, we don't really care
        assertTrue(geometry == null || geometry.isEmpty());
    }
}
