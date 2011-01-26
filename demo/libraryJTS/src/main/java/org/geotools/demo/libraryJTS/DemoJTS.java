/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.libraryJTS;

//JTS imports
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;




/**
 * This is DemoJTS.java a class to examine the JTS Geometric model of the
 * GeoTools code base.
 *
 *
 * This tutorial was written in April 2006 against the Geotools 2.2RC2 release.
 * It was updated in November 2006 against the 1.7.2 JTS release.
 *
 *
 * The Geotools Users' Manual:
 * ---------------------------
 *
 * This tutorial is written as part of the Geotools Users' Manual which
 * will be available on the Geotools web site. The tutorial and manual aim to
 * get new programmers started in using the Geotools library. Programmers who
 * wish to extend the library should look at the Developpers' Guide instead.
 *
 *
 * The Geotools Geometric Model:
 * -----------------------------
 *
 * This tutorial introduces one of the two geometric models used by Geotools.
 * The JTS model is used to describe the spatial definition of the 'Features' in
 * the GIS.
 *
 * The JTS model is a strictly cartesian, two dimensional, orthogonal axis
 * model; while a third, 'z', coordinate is allowed in each coordinate tuple,
 * these 'z' coordinates are all ignored in the geometric operators. Similarly,
 * while the geometry objects can hold a spatial reference identification (SRID)
 * number, the library does not take that identification into account.
 *
 *
 * Tutorial Outline:
 * -----------------
 *
 * The tutorial starts by building geometries from scratch. This section uses
 * the Java primitive double data type to create coordinates, then assembles
 * these coordinates into arrays, and uses those arrays to make JTS Geometries.
 *
 * The tutorial continues by building geometries from Well Known Text (WKT)
 * String elements.
 *
 *
 * Note on Code Structure:
 * -----------------------
 *
 * For reading simplicity, this tutorial does not use any Java method or object
 * structure; it consists of only a single long main(.) method.
 *
 *
 * Further Reading:
 * ---------------
 *
 * Read the Geotools Users' Manual
 * Read the tutorials in the JTS distribution.
 *
 * Material for this tutorial was taken from:
 *   http://www.geotools.org/CreateAGeometry
 *     retrived on 24 Sept 2005
 *
 * @author Adrian Custer
 *
 * @source $URL$
 * @version 0.0.1   April 2006
 *
 */

public class DemoJTS {

    public static void main(String[] args) {

        System.out.println("Start of the output for the tutorial: DemoJTS.\n");

//      PART I: Create Coordinates and Coordinate Arrays
        System.out.println("PART I: Coordinates from scratch");

//      Create a coordinate for a point
        Coordinate ptc = new Coordinate(14.0d,14.0d);
        System.out.println("The Coordinate is:            "+ ptc);

//      Create an array and add the coordinates for the line
        Coordinate[] lsc = new Coordinate[8];
        lsc[0] = new Coordinate(5.0d,5.0d);
        lsc[1] = new Coordinate(6.0d,5.0d);
        lsc[2] = new Coordinate(6.0d,6.0d);
        lsc[3] = new Coordinate(7.0d,6.0d);
        lsc[4] = new Coordinate(7.0d,7.0d);
        lsc[5] = new Coordinate(8.0d,7.0d);
        lsc[6] = new Coordinate(8.0d,8.0d);
        lsc[7] = new Coordinate(9.0d,9.0d);

//      Create an array and add the coordinates for the polygon
//      Note that the last coordinate is the same as the first
        Coordinate[] pgc = new Coordinate[10];
        pgc[0] = new Coordinate(7,7);
        pgc[1] = new Coordinate(6,9);
        pgc[2] = new Coordinate(6,11);
        pgc[3] = new Coordinate(7,12);
        pgc[4] = new Coordinate(9,11);
        pgc[5] = new Coordinate(11,12);
        pgc[6] = new Coordinate(13,11);
        pgc[7] = new Coordinate(13,9);
        pgc[8] = new Coordinate(11,7);
        pgc[9] = new Coordinate(7,7);




//      PART II: Create Envelopes and operations
        System.out.println("\nPART II: Envelopes from scratch and operations");

        // Construct with doubles x1,x2,y1,y2
        Envelope e1 = new Envelope(8.0d, 20.0d, 4.0d, 12.0d);
        // Construct with Coordinates lower left, upper right
        Envelope e2 = new Envelope(new Coordinate(12.0d,6.0d),
                                   new Coordinate(16.0d,16.0d) );
        System.out.println("The first Envelope is:       "+ e1);
        System.out.println("It contains the coordinate?  "+ e1.contains(ptc));
        System.out.println("Does the second?             "+ e2.contains(ptc));
        Envelope e1prime = new Envelope(e1);
        e1prime.expandToInclude(ptc);
        System.out.println("The first Envelope expanded  "+ e1prime );
        System.out.println("Two Envelopes intersected    "+ e1.intersection(e2));




//      PART III: Creating Geometry objects from doubles
        System.out.println("\nPART III: Geometries from Coordinates");

//      Create a com.vividsolutions.jts.geom.GeometryFactory
//
//      Geotools uses the idea of factories a lot. It's known as a 'pattern,'
//      a common setup that gives a particular kind of flexibility. The idea
//      is: you make the factory, change the settings of the factory, and
//      then make a new object based on those settings. Factories make it
//      easy to create lots of objects with similar settings.
//
//      The JTS Geometry factory provides different public methods from which
//      the various geometry types can be instantiated.
//
        GeometryFactory geomFac = new GeometryFactory();

//      TODO: HOW? "Here if we wanted to, we could tweak the factory"

//      Use the factory to make the jts geometries
        Point      ptG = geomFac.createPoint(ptc);
        LineString lnG = geomFac.createLineString(lsc);
        LinearRing rgG = geomFac.createLinearRing(pgc);
        Polygon    pgG = geomFac.createPolygon(rgG,null);

//      TODO: add some query stuff.

//      Just to see how far we have gotten
        System.out.println("The point is:                "+ ptG);
        System.out.println("The line string is:          "+ lnG);
        System.out.println("The linear ring is:          "+ rgG);
        System.out.println("The polygon is:              "+ pgG);
//      Note that the JTS Geometry Objects' toString() method outputs the
//      Coordinates in the Well Known Text (WKT) String format.




//      PART IV: Creating Geometry objects from Well Known Text
        System.out.println("\nPART IV: Geometry Creation from WKT");
//
//      The Well Known Text (WKT) format is a String format used to define
//      geometry Objects. The WKT format is defined in ...TODO

//      Create another LineString from Well Known Text. The LineString is
//      created, not as a LineString object directly, but as a more generic
//      Geometry object.
        Geometry lnGwkt = null;
        try{
          lnGwkt = new WKTReader().read("LINESTRING (0 0, 30 30, 0 7, 5 10)");
        } catch (ParseException pe){
            System.out.println("Couldn't parse the linestring");
        }

//      The geometry can also be output as well known text, allowing
//      for output of all geometry types
        System.out.print("The WKT line string is:      ");
        WKTWriter wrt = new WKTWriter();
        String str = wrt.write(lnGwkt);
        System.out.println(str);
//      A more compact version of the same would be.
        System.out.println(" or with one statement:      " + (new WKTWriter()).write(lnGwkt));




//      PART V: Compare Geometry Objects
        System.out.println("\nPART V: Geometry Comparisons");

//      Geometry equality. (Note that the 'z' coordinate is ignored.)
        Coordinate cNew = new Coordinate(14.000001,14.0,3.0);
        Point ptNew = geomFac.createPoint(cNew);
        System.out.println("Are the pt geometries equal? "+ ptG.equals(ptNew));
        System.out.println(" exactly equal?              "+ ptG.equalsExact(ptNew));
        System.out.println(" near to exactly equal?      "+ ptG.equalsExact(ptNew,0.0000000001));
        System.out.println(" inexactly equal?            "+ ptG.equalsExact(ptNew,0.001));

        Geometry g_one = null;
        Geometry g_two = null;
        try{
            g_one = new WKTReader().read("LINESTRING (0 0, 10 10, 0 20, 0 0)");
            g_two = new WKTReader().read("LINESTRING (10 10, 0 20, 0 0, 10 10)");
        } catch (ParseException pe){
            System.out.println("Couldn't parse the linestring");
        }
        System.out.println("Are the triangles equal?     "+g_one.equals(g_two));

//      Geometry overlap: envelopes, convex hulls, geometries
//      TODO: why don't the geoms overlap?
        System.out.println("Do the envelopes overalp?    "+ lnG.getEnvelope().overlaps(pgG.getEnvelope()));
        System.out.println("Do the convex hulls overalp? "+ lnG.convexHull().overlaps(pgG.convexHull()));
        System.out.println("Do the geometries overalp?   "+ lnG.overlaps(pgG));




//      PART VI: Operations on Geometry objects
        System.out.println("\nPART VI: Geometry Operations");

//      Intersection
//      Test the line string for self intersection.
        System.out.println("Does it self intersect?      " + lnGwkt.isSimple());
//      LineString intersection.
//      Note that the result is a complex geometry.
        System.out.println("Line strings intersection:   " + lnGwkt.intersection(lnG));
        System.out.println("Line, polygon intersection:  " + lnG.intersects(pgG));

//      Union
        System.out.println("Union of two LineStrings:    " + lnG.union(lnGwkt));




//      PART VII: Complex Operations on Geometry sets
        System.out.println("\nPART VII: Operations on Geometry Sets");
        System.out.println("...TODO...");

        System.out.println("\nEnd of the tutorial output.");

        System.out.println("Testing...");

    }

    public static void codeExample(Geometry fire, Geometry cities, Geometry me, Geometry he, Geometry you){
        // Create Geometry using other Geometry
        Geometry smoke = fire.buffer( 10 );        
        Geometry evacuate = cities.intersection( smoke );
        
        // test important relationships
        boolean onFire = me.intersects( fire );
        boolean thatIntoYou = he.disjoint( you );
        boolean run = you.isWithinDistance( fire, 2 );
        
        // relationships actually captured as a fancy
        // String called an intersection matrix
        //
        IntersectionMatrix matrix = he.relate( you );        
        thatIntoYou = matrix.isDisjoint();
        
        // it really is a fancy string; you can do
        // pattern matching to sort out what the geometry
        // being compared are up to
        boolean disjoint = matrix.matches("FF*FF****");        
    }
}

