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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * This example shows how to perform some common JTS Geometry tasks as per <a
 * href="http://docs.codehaus.org/display/GEOTDOC/03+JTS+Topology+Suite">JTS Topology Suite</a>.
 * <p>
 * The <b>JTS Topology Suite</b> is a library of topology abstractions and operations
 * used by GeoTools to represent "Simple Feature for SQL" constructs like Point, Line
 * and Polygon. GeoTools itself includes an "ISO Geometry" module representing Point, Curve
 * and Surface.
 * <p>
 * Use JTS it works! Leave ISO Geometry for experimenta only.
 * 
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class GeometryExample {

    /**
     * Example of creating a Point.
     */
    public void createPoint(){
        GeometryFactory gf = new GeometryFactory();

        Coordinate coord = new Coordinate( 1, 1 );
        Point point = gf.createPoint( coord );
        
        System.out.println( point );
    }

    public void wktWriter(){
        GeometryFactory gf = new GeometryFactory();

        Coordinate coord = new Coordinate( 1, 1 );
        Point point = gf.createPoint( coord );
        
        StringWriter writer = new StringWriter();
        WKTWriter wktWriter = new WKTWriter(2);
        
        try {
            wktWriter.write( point, writer );
        } catch (IOException e) {            
        }
        
        String wkt = writer.toString();
                
        System.out.println( wkt );
    }
    public void parseWKT(){
        GeometryFactory geometryFactory = new GeometryFactory();

        WKTReader reader = new WKTReader( geometryFactory );
        Point point = null;
        try {
            point = (Point) reader.read("POINT (1 1)");
        } catch (ParseException e) {            
        }
        System.out.println( point );
    }
    
    /**
     * @param args
     */
    public static void main( String[] args ) {
        List<String> examples = new ArrayList<String>();
        if( args.length == 0 || "-all".equalsIgnoreCase(args[0]) ){
            for( Method method : getExamples() ){
                examples.add(method.getName());
            }
        }
        else if( "-help".equalsIgnoreCase(args[0]) ){
            System.out.println("Usage: java org.geotools.demo.jts.GeometryExample <example>");
            System.out.println("Where <example> is one of the following:");
            System.out.println("-help");
            System.out.println("-all");
            for( Method method : getExamples()){
                System.out.println( method.getName() );
            }
            System.exit(0);
        }
        else {
            for( String arg : args ){
                examples.add( arg );
            }
        }        
        GeometryExample example = new GeometryExample();
        for( Method method : getExamples() ){
            if( !examples.contains( method.getName() )){
                continue;
            }
            System.out.println( "Running example "+method.getName() );
            try {
                method.invoke( example, new Object[0] );
            } catch (IllegalArgumentException e) {                
            } catch (IllegalAccessException e) {                
            } catch (InvocationTargetException e) {                
            }
        }
                
    }
    
    public static List<Method> getExamples(){
        List<Method> examples = new ArrayList<Method>();
        for( Method method : GeometryExample.class.getDeclaredMethods() ){
            if( !Modifier.isPublic( method.getModifiers() ) ) {
                continue;
            }
            if( method.getReturnType() != Void.TYPE ){
                continue;
            }
            if( method.getParameterAnnotations().length != 0){
                continue;
            }            
            examples.add( method );
        }
        return examples;
    }

}
