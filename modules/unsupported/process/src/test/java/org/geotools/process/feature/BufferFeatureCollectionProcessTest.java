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
package org.geotools.process.feature;

import java.util.HashMap;
import java.util.Map;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import org.junit.Test;
import static org.junit.Assert.*;

public class BufferFeatureCollectionProcessTest {
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    /**
     * FIXME: test fails at line 79
     */
    @Test
    public void test() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( "featureType" );
        tb.add( "geometry", Point.class );
        tb.add( "integer", Integer.class );
        
        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder( tb.buildFeatureType() );
        
        DefaultFeatureCollection features = new DefaultFeatureCollection( null, b.getFeatureType() );
        for ( int i = 0; i < 2; i++ ) {
            b.add( gf.createPoint( new Coordinate( i, i ) ) );
            b.add( i );
            features.add( b.buildFeature( i + "" ) );
        }
        
        Map<String,Object> input = new HashMap();
        input.put( BufferFeatureCollectionFactory.FEATURES.key, features );
        input.put( BufferFeatureCollectionFactory.BUFFER.key, 10d );

        BufferFeatureCollectionFactory factory = new BufferFeatureCollectionFactory();
        BufferFeatureCollectionProcess process = factory.create();
        Map<String,Object> output = process.execute( input, null );
        
        FeatureCollection buffered = (FeatureCollection) output.get( BufferFeatureCollectionFactory.RESULT.key );
        
        assertEquals(2, buffered.size());
        for (int i = 0; i < 2; i++) {
            Geometry expected = gf.createPoint( new Coordinate( i, i ) ).buffer( 10d );
            FeatureCollection sub = buffered.subCollection(ff.equals(ff.property("integer"), ff.literal(i)));
            assertEquals(1, sub.size());
            FeatureIterator iterator = sub.features();
            SimpleFeature sf = (SimpleFeature) iterator.next();
            assertTrue(expected.equals((Geometry) sf.getDefaultGeometry()));
            iterator.close();
        }
    }

    /**
     * Tests finding the process via the Processors class
     */
    @Test
    public void testFindProcess() {
        String GT_NAMESPACE = "gt";
        Process p = Processors.createProcess(new NameImpl(GT_NAMESPACE, "BufferFeatureCollection"));
        assertNotNull(p);
        assertTrue(p instanceof BufferFeatureCollectionProcess);
    }
}
