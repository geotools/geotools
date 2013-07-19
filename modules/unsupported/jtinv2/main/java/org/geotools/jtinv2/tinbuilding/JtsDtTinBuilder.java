package org.geotools.jtinv2.tinbuilding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.geotools.jtinv2.main.InMemoryTinFactory;
import org.geotools.jtinv2.main.TinBoundary;
import org.geotools.jtinv2.main.TinBreakline;
import org.geotools.jtinv2.main.TinFace;
import org.geotools.jtinv2.main.TinPoint;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.triangulate.ConformingDelaunayTriangulationBuilder;

public class JtsDtTinBuilder implements TriangleCalculator
{
	private InMemoryTinFactory tinFactory;
	private int faceCounter;
	
	public JtsDtTinBuilder()
	{
		this.faceCounter = 0;
	}
	
	@Override
	public String getName() 
	{
		return "JTS DT TIN Builder";
	}

	@Override
	public boolean supportsAnOuterBoundary() 
	{
		return true;
	}

	@Override
	public boolean supportsHoles() 
	{
		return true;
	}

	@Override
	public boolean supportsBreaklines() 
	{
		return true;
	}

	@Override
	public void calculateTriangles(InMemoryTinFactory argFactory) 
	{
		// Store a reference to our InMemoryTinFactory.
		this.tinFactory = argFactory;
		
		ConformingDelaunayTriangulationBuilder builder = new ConformingDelaunayTriangulationBuilder();
		
		// Build the constraints.
		GeometryFactory geometryFactory = new GeometryFactory();
		
		Iterator<TinBreakline> goOverEach1 = argFactory.getTinBreaklines();
		
		ArrayList<LineString> linestrings = new ArrayList<LineString>();
		
		while(goOverEach1.hasNext() == true)
		{
			TinBreakline currentBreakline = goOverEach1.next();
			LineString currentLineString = currentBreakline.getLineString();
		
			linestrings.add(currentLineString);
		}
		
		Iterator<TinBoundary> goOverEach2 = argFactory.getInnerBoundaries();
		
		while(goOverEach2.hasNext() == true)
		{
			TinBoundary boundary = goOverEach2.next();
			LineString currentLineString = boundary.getLineString();
			
			linestrings.add(currentLineString);
		}
		
		TinBoundary outerBoundary = argFactory.getOuterBoundary();
		LineString outerBoundaryLineString = outerBoundary.getLineString();
		linestrings.add(outerBoundaryLineString);
		
		LineString[] linestringsArray = (LineString[]) linestrings.toArray();
		
		GeometryCollection linestringsCollection = geometryFactory.createGeometryCollection(linestringsArray);
		
		builder.setConstraints(linestringsCollection);
		
		// Set the sites.
		
		Iterator<TinPoint> goOverEach3 = argFactory.getTinPoints();
		
		LinkedList<Coordinate> coords = new LinkedList<Coordinate>();
		
		while(goOverEach3.hasNext() == true)
		{
			TinPoint currentPoint = goOverEach3.next();
			Coordinate currentCoordinate = currentPoint.getCoordinate();
			
			coords.add(currentCoordinate);
		}
		Coordinate[] coordsArray = (Coordinate[]) coords.toArray();
		MultiPoint multiPoint = geometryFactory.createMultiPoint(coordsArray);
		
		builder.setSites(multiPoint);
		
		GeometryCollection triangles = (GeometryCollection) builder.getTriangles(geometryFactory);
	
		this.convertTrianglesToTinFaces(triangles);
	}
	
	private void convertTrianglesToTinFaces(GeometryCollection argTriangles)
	{
		// Get the number of geometries.
		int numberOfTriangles = argTriangles.getNumGeometries();
		int counter = 0;
		
		while(counter < numberOfTriangles)
		{
			Polygon currentTriangle = (Polygon) argTriangles.getGeometryN(counter);
			TinFace currentFace = this.convertTriangleToTinFace(currentTriangle);
			
			this.tinFactory.addTinFace(currentFace);
			counter++;
		}
	}
	
	private TinFace convertTriangleToTinFace(Polygon argTriangle)
	{
		Coordinate[] coords = argTriangle.getCoordinates();
		TinPoint[] points = new TinPoint[3];
		int counter = 0;
		
		while(counter < 3)
		{
			points[counter] = this.tinFactory.getTinPointAtLocation(coords[counter], 0.10);
			
			counter++;
		}
		
		TinFace face = new TinFace(faceCounter, points);
		
		this.faceCounter++;
		return face;
	}

}
