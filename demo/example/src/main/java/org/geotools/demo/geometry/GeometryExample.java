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

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.Hints;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.GeometryFactoryFinder;
import org.geotools.geometry.text.WKTParser;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;

public class GeometryExample {
	
	public void createEnvWithFactories() {
		Hints hints = new Hints( Hints.CRS, DefaultGeographicCRS.WGS84 );

		PositionFactory positionFactory = GeometryFactoryFinder.getPositionFactory( hints );
		GeometryFactory geometryFactory = GeometryFactoryFinder.getGeometryFactory( hints );

		DirectPosition upper = positionFactory.createDirectPosition(new double[]{-180,-90});
		DirectPosition lower = positionFactory.createDirectPosition(new double[]{180,90});        
		Envelope envelope = geometryFactory.createEnvelope( upper, lower );  
		
		System.out.println(envelope);
	}
	
	public void createEnvWithGB() {
		GeometryBuilder builder = new GeometryBuilder( DefaultGeographicCRS.WGS84 ); 

		DirectPosition upper = builder.createDirectPosition(new double[]{-180,-90});
		DirectPosition lower = builder.createDirectPosition(new double[]{180,90});        
		Envelope envelope = builder.createEnvelope( upper, lower );   	
		
		System.out.println(envelope);
	}
	
    public void createCurveWithGB(){
        GeometryBuilder builder = new GeometryBuilder( DefaultGeographicCRS.WGS84 ); 
        
        // create directpositions
        DirectPosition start = builder.createDirectPosition(new double[]{ 48.44, -123.37 });
        DirectPosition middle = builder.createDirectPosition(new double[]{ 47, -122 });
        DirectPosition end = builder.createDirectPosition(new double[]{ 46.5, -121.5 });        
        
        // add directpositions to a list
        ArrayList<Position> positions = new ArrayList<Position>();
        positions .add(start);
        positions.add(middle);
        positions.add(end);    
        
        // create linestring from directpositions
        LineString line = builder.createLineString(positions);
        
        // create curvesegments from line
        ArrayList<CurveSegment> segs = new ArrayList<CurveSegment>();
        segs.add(line);

        // create curve
        Curve curve = builder.createCurve(segs);
        
        System.out.println( curve );
    }
    
    public void createSurfaceWithGB() {
    	// create GB
    	GeometryBuilder builder = new GeometryBuilder( DefaultGeographicCRS.WGS84 );   	
    	
    	// create a list of connected positions
    	List<Position> dps = new ArrayList<Position>();
    	dps.add(builder.createDirectPosition( new double[] {20, 10} ));
    	dps.add(builder.createDirectPosition( new double[] {40, 10} ));
    	dps.add(builder.createDirectPosition( new double[] {50, 40} ));
    	dps.add(builder.createDirectPosition( new double[] {30, 50} ));
    	dps.add(builder.createDirectPosition( new double[] {10, 30} ));
    	dps.add(builder.createDirectPosition( new double[] {20, 10} ));

    	// create linestring from directpositions
    	LineString line = builder.createLineString(dps);

    	// create curvesegments from line
    	ArrayList<CurveSegment> segs = new ArrayList<CurveSegment>();
    	segs.add(line);

    	// Create list of OrientableCurves that make up the surface
    	OrientableCurve curve = builder.createCurve(segs);
    	List<OrientableCurve> orientableCurves = new ArrayList<OrientableCurve>();
    	orientableCurves.add(curve);

    	// create the interior ring and a list of empty interior rings (holes)
    	Ring extRing = builder.createRing(orientableCurves);
    	List<Ring> intRings = new ArrayList<Ring>();

    	// create the surfaceboundary from the rings
    	SurfaceBoundary sb = builder.createSurfaceBoundary(extRing, intRings);
    			
    	// create the surface
    	Surface surface = builder.createSurface(sb);  
    	
    	System.out.println( surface );
    }
    
    public void createSurfaceWithFactory() {
    	// create factories
        Hints hints = new Hints( Hints.CRS, DefaultGeographicCRS.WGS84 );
        
        PositionFactory posF = GeometryFactoryFinder.getPositionFactory(hints);
        GeometryFactory geomF = GeometryFactoryFinder.getGeometryFactory(hints);
        PrimitiveFactory primF = GeometryFactoryFinder.getPrimitiveFactory(hints);   	
    	
    	// create a list of connected positions
    	List<Position> dps = new ArrayList<Position>();
    	dps.add(posF.createDirectPosition( new double[] {20, 10} ));
    	dps.add(posF.createDirectPosition( new double[] {40, 10} ));
    	dps.add(posF.createDirectPosition( new double[] {50, 40} ));
    	dps.add(posF.createDirectPosition( new double[] {30, 50} ));
    	dps.add(posF.createDirectPosition( new double[] {10, 30} ));
    	dps.add(posF.createDirectPosition( new double[] {20, 10} ));

    	// create linestring from directpositions
    	LineString line = geomF.createLineString(dps);

    	// create curvesegments from line
    	ArrayList<CurveSegment> segs = new ArrayList<CurveSegment>();
    	segs.add(line);

    	// Create list of OrientableCurves that make up the surface
    	OrientableCurve curve = primF.createCurve(segs);
    	List<OrientableCurve> orientableCurves = new ArrayList<OrientableCurve>();
    	orientableCurves.add(curve);

    	// create the interior ring and a list of empty interior rings (holes)
    	Ring extRing = primF.createRing(orientableCurves);
    	List<Ring> intRings = new ArrayList<Ring>();

    	// create the surfaceboundary from the rings
    	SurfaceBoundary sb = primF.createSurfaceBoundary(extRing, intRings);
    			
    	// create the surface
    	Surface surface = primF.createSurface(sb);  
    	
    	System.out.println( surface );
    }

    public void createPointWithGB(){
        GeometryBuilder builder = new GeometryBuilder( DefaultGeographicCRS.WGS84 );        
        Point point = builder.createPoint( 48.44, -123.37 );
        
        System.out.println( point );
    }
    
    public void createPointWithFactory(){
        Hints hints = new Hints( Hints.CRS, DefaultGeographicCRS.WGS84 );
        PositionFactory positionFactory = GeometryFactoryFinder.getPositionFactory( hints );
        PrimitiveFactory primitiveFactory = GeometryFactoryFinder.getPrimitiveFactory( hints );
        
        DirectPosition here = positionFactory.createDirectPosition( new double[]{48.44, -123.37} );        
        Point point1 = primitiveFactory.createPoint( here );
        
        System.out.println( point1 );
        
        Point point2 = primitiveFactory.createPoint(  new double[]{48.44, -123.37} );
        System.out.println( point2 );
    }
    
    public void createPointWithWKT() throws Exception {
        Hints hints = new Hints( Hints.CRS, DefaultGeographicCRS.WGS84 );
        
        PositionFactory positionFactory = GeometryFactoryFinder.getPositionFactory(hints);
        GeometryFactory geometryFactory = GeometryFactoryFinder.getGeometryFactory(hints);
        PrimitiveFactory primitiveFactory = GeometryFactoryFinder.getPrimitiveFactory(hints);
        AggregateFactory aggregateFactory = GeometryFactoryFinder.getAggregateFactory(hints);
        
        WKTParser parser = new WKTParser( geometryFactory, primitiveFactory, positionFactory, aggregateFactory );
        
        Point point = (Point) parser.parse("POINT( 48.44 -123.37)");
        
        System.out.println( point );
    }
    
    public static void main( String args[] ) throws Exception {
        GeometryExample example = new GeometryExample();
        
        example.createPointWithGB();
        example.createPointWithFactory();
        example.createPointWithWKT();
        example.createCurveWithGB();
        example.createSurfaceWithGB();
        example.createSurfaceWithFactory();
    }
}
