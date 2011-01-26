/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.main;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * This class collects several examples on the use of DefaultFeature.
 * <p>
 * For the wiki page associated with these examples please visit:
 * <ul>
 * <li><a href="http://docs.codehaus.org/display/GEOTDOC/05+Main">Main Module Wiki Page</a>
 * <li><a href="http://docs.codehaus.org/display/GEOTDOC/04+Feature">Feature Wiki Page</a>
 * </ul>
 * Where possible we are restricting ourself to the formal api, and the use of
 * a FactoryFinder. Only the "raw" example makes use of they keyword "new".
 * </p>
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class FeatureExampleTest extends TestCase {

    public void testHowToCreateAFeature() throws Exception{
        GeometryFactory geomFactory = new GeometryFactory();
        SimpleFeatureType type = DataUtilities.createType("location","geom:Point,name:String");
        Object attributes[] = new Object[2];
        attributes[0] = geomFactory.createPoint( new Coordinate(40,50));
        attributes[1] = "fred";
        SimpleFeature feature = SimpleFeatureBuilder.build( type, attributes, null );
    }
    
    public void testDefaultAttributeValues() throws Exception {
        SimpleFeatureType type = DataUtilities.createType("location","geom:Point,name:String");
        
        Object defaultValues[] = new Object[ type.getAttributeCount() ];
        for( int i = 0 ; i < type.getAttributeCount(); i++) {
           AttributeDescriptor attributeType = type.getDescriptor( i );
           defaultValues[ i ] = attributeType.getDefaultValue();
        }

        SimpleFeature feature = SimpleFeatureBuilder.build(  type, defaultValues, null );
    }
}
