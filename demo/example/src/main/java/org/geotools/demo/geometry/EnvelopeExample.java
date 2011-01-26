/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.geometry;

import org.geotools.factory.Hints;
import org.geotools.geometry.GeometryFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.coordinate.GeometryFactory;

public class EnvelopeExample {
    public void createEnvelope(){
        Hints hints = new Hints( Hints.CRS, DefaultGeographicCRS.WGS84 );

        PositionFactory positionFactory = GeometryFactoryFinder.getPositionFactory( hints );
        GeometryFactory geometryFactory = GeometryFactoryFinder.getGeometryFactory( hints );

        DirectPosition upper = positionFactory.createDirectPosition(new double[]{-180,-90});
        DirectPosition lower = positionFactory.createDirectPosition(new double[]{180,90});        
        Envelope world = geometryFactory.createEnvelope( upper, lower );
        
        System.out.println( world );
        
        double width = world.getLength(0);
        double height = world.getLength(1);
        
        System.out.println( "width:"+width);
        System.out.println( "height:"+height);
    }

    public static void main( String args[] ){
        EnvelopeExample example = new EnvelopeExample();
        
        example.createEnvelope();
    }
    
}
