/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.literal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Test case to watch this thing turn over.
 * 
 * @author Jody, gdavis
 *
 * @source $URL$
 */
public class ProcessTest extends TestCase {

    public void testIntersectProcess() throws Exception {
        WKTReader reader = new WKTReader( new GeometryFactory() );
        
        Geometry geom1 = (Polygon) reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        Geometry geom2 = (Polygon) reader.read("POLYGON((20 10, 30 0, 40 10, 20 10))");
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( IntersectionFactory.GEOM1.key, geom1 );
        map.put( IntersectionFactory.GEOM2.key, geom2 );
        
        IntersectionProcess process = new IntersectionProcess( null );           
        Map<String, Object> resultMap = process.execute(map, null);
        
        assertNotNull( resultMap );
        Object result = resultMap.get(IntersectionFactory.RESULT.key);
        assertNotNull( result );
        assertTrue( "expected geometry", result instanceof Geometry );
        Geometry intersection = geom1.intersection(geom2);
        assertTrue( intersection.equals( (Geometry) result ) );
    }
    
    public void testUnionProcessA() throws Exception {
        WKTReader reader = new WKTReader( new GeometryFactory() );
        List<Geometry> list = new ArrayList<Geometry>();
        list.add( reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))") );
        list.add( reader.read("POLYGON((20 10, 30 0, 40 10, 20 10))") );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( UnionFactory.GEOM1.key, list );
        
        UnionProcess process = new UnionProcess();        
        Map<String, Object> resultMap = process.execute( map, null );
        
        assertNotNull( resultMap );
        Object result = resultMap.get(UnionFactory.RESULT.key);
        assertNotNull( result );
        assertTrue( "expected geometry", result instanceof Geometry );        
    } 
    
    public void testUnionProcessB() throws Exception {
        WKTReader reader = new WKTReader( new GeometryFactory() );
        List<Geometry> list = new ArrayList<Geometry>();
        list.add( reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))") );
        list.add( reader.read("POLYGON((20 10, 30 0, 40 10, 20 10))") );
        list.add( reader.read("POLYGON((30 10, 30 0, 40 10, 30 10))") );
        list.add( reader.read("POLYGON((40 20, 30 0, 40 10, 40 20))") );
        list.add( reader.read("POLYGON((50 30, 30 0, 40 10, 50 30))") );
        list.add( reader.read("POLYGON((60 40, 30 0, 40 10, 60 40))") );
        list.add( reader.read("POLYGON((70 50, 30 0, 40 10, 70 50))") );
        list.add( reader.read("POLYGON((80 60, 30 0, 40 10, 80 60))") );
        list.add( reader.read("POLYGON((90 70, 30 0, 40 10, 90 70))") );
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( UnionFactory.GEOM1.key, list );
        
        UnionProcess process = new UnionProcess();        
        Map<String, Object> resultMap = process.execute( map, null );
        
        assertNotNull( resultMap );
        Object result = resultMap.get(UnionFactory.RESULT.key);
        assertNotNull( result );
        assertTrue( "expected geometry", result instanceof Geometry );        
    }   
    
    public void testBufferProcess() throws Exception {
        WKTReader reader = new WKTReader( new GeometryFactory() );
        
        Geometry geom1 = (Polygon) reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        Double buffer = new Double(213.78);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( BufferFactory.GEOM1.key, geom1 );
        map.put( BufferFactory.BUFFER.key, buffer );
        
        BufferProcess process = new BufferProcess( null );        
        Map<String, Object> resultMap = process.execute( map, null );
        
        assertNotNull( resultMap );
        Object result = resultMap.get(BufferFactory.RESULT.key);
        assertNotNull( result );
        assertTrue( "expected geometry", result instanceof Geometry );
        Geometry bufferedGeom = geom1.buffer(buffer);
        assertTrue( bufferedGeom.equals( (Geometry) result ) );
    }    
}
