/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.feature.gs;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.process.feature.gs.AggregateProcess.AggregationFunction;
import org.geotools.process.feature.gs.AggregateProcess.Results;
import org.geotools.process.feature.gs.ReShapeProcess.Definition;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.expression.PropertyName;

/**
 * 
 *
 * @source $URL$
 */
public class ReShapeProcessTest {
    DataStore bugs;
    
    @Before
    public void setup() throws IOException {
        File file = TestData.file(this, null );
        bugs = new PropertyDataStore( file );
    }
    @After
    public void tearDown(){
        bugs.dispose();
    }

    
    @Test
    public void testDefinition() throws Exception {
        String definition = "the_geom=the_geom";
        List<Definition> def = ReShapeProcess.toDefinition( definition );

        assertEquals( 1, def.size() );
        
        assertEquals( "the_geom", def.get(0).name );
        assertTrue( def.get(0).expression instanceof PropertyName );
    }

    @Test
    public void testSum() throws Exception {
        SimpleFeatureSource source = bugs.getFeatureSource("bugsites");

        
        ReShapeProcess process = new ReShapeProcess();
        
        String definition = "the_geom=the_geom\nnumber=cat";
        SimpleFeatureCollection origional = source.getFeatures();
        SimpleFeatureCollection result = process.execute( origional, definition );
        
        assertEquals( origional.size(), result.size() );
        
        SimpleFeatureType schema = result.getSchema();
        AttributeDescriptor number = schema.getDescriptor("number");
        assertTrue( Long.class.isAssignableFrom( number.getType().getBinding() ) );

    }

}
