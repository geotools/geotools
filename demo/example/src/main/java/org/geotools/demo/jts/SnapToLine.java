/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.jts;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.linearref.LinearLocation;
import com.vividsolutions.jts.linearref.LocationIndexedLine;

/**
 * This example shows how to snap a point to a line.
 * <p>
 * This is a real world solution; we are going to cache
 * internal JTS data structures (the same ones used behind the
 * getDistance( Geometry ) method). We are also going to
 * throw these into a spatial index.
 * </p>
 * <p>
 * This kind of code is needed when you have low accuracy information
 * (like GPS in a city) that you wish to snap to a road network.
 * </p>
 * 
 * @author Jody Garnett (Refractions Research)
 *
 * @source $URL$
 */
public class SnapToLine {
    
    public static void main(String[] args) throws Exception {
        if( args.length != 1 ){
            System.out.println("Please provide a filename");
            return;
        }
        File file = new File( args[0]);
        System.out.println("Snapping against:"+file);
        Map<String,Serializable> params = new HashMap<String,Serializable>();
        if( file.getName().endsWith(".properties")){
            Properties properties = new Properties();
            FileInputStream inStream = new FileInputStream(file);
            try {
                properties.load( inStream);
            }
            finally {
                inStream.close();
            }
            for( Map.Entry<Object,Object> property : properties.entrySet() ){
                params.put( (String) property.getKey(), (String) property.getValue() );
            }
        }
        else {
            params.put("url", file.toURI().toURL() );            
        }        
        DataStore data = DataStoreFinder.getDataStore(params);        
        List<Name> names = data.getNames();
        
        SimpleFeatureSource source = data.getFeatureSource( names.get(0));
        
        final SpatialIndex index = new STRtree();     
        SimpleFeatureCollection features = source.getFeatures();
        System.out.println("Slurping in features ...");
        features.accepts( new FeatureVisitor(){
            public void visit(Feature feature) {
                SimpleFeature simpleFeature = (SimpleFeature) feature;                
                Geometry geom = (MultiLineString) simpleFeature.getDefaultGeometry();
                Envelope bounds = geom.getEnvelopeInternal();
                if( bounds.isNull() ) return; // must be empty geometry?                
                index.insert( bounds, new LocationIndexedLine( geom ));
            }
        }, new NullProgressListener() );
        final int DURATION = 6000;
        System.out.println("we now have our spatial index and are going to snap for "+DURATION);
        ReferencedEnvelope limit = features.getBounds();
        Coordinate[] points = new Coordinate[10000];
        Random rand = new Random(file.hashCode());
        for( int i=0; i<10000;i++){
            points[i] = new Coordinate(
                    limit.getMinX()+rand.nextDouble()*limit.getWidth(),
                    limit.getMinY()+rand.nextDouble()*limit.getHeight()
            );
        }
        double distance = limit.getSpan(0) / 100.0;
        long now = System.currentTimeMillis();
        long then = now+DURATION;
        int count = 0;
        System.out.println("we now have our spatial index and are going to snap for "+DURATION);

        while( System.currentTimeMillis()<then){
            Coordinate pt = points[rand.nextInt(10000)];
            Envelope search = new Envelope(pt);
            search.expandBy(distance);
            
            List<LocationIndexedLine> hits = index.query( search );
            double d = Double.MAX_VALUE;
            Coordinate best = null;
            for( LocationIndexedLine line : hits ){
                LinearLocation here = line.project( pt );                
                Coordinate point = line.extractPoint( here );
                double currentD = point.distance( pt );
                if( currentD < d ){
                    best = point;
                }
            }
            if( best == null ){
                // we did not manage to snap to a line? with real data sets this happens all the time...
                System.out.println( pt + "-X");
            }
            else {
                System.out.println( pt + "->" + best );
            }
            count++;
        }
        System.out.println("snapped "+count+" times - and now I am tired");
        System.out.println("snapped "+count/DURATION+" per milli?");
    }
}
