package org.geotools.jtinv2.index;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.geotools.jtinv2.main.TinBoundary;
import org.geotools.jtinv2.main.TinBreakline;
import org.geotools.jtinv2.main.TinFace;
import org.geotools.jtinv2.main.TinFaceUtilities;
import org.geotools.jtinv2.main.TinPoint;
import org.geotools.jtinv2.main.TriangulatedIrregularNetwork;
import org.geotools.jtinv2.pointutils.BasicTinPointSpatialIndex;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class InMemoryTinIndex implements TinIndex 
{
	
	private Quadtree faceSpatialIndex;
	private Quadtree breaklineSpatialIndex;
	private BasicTinPointSpatialIndex pointSpatialIndex;
	private Quadtree boundarySpatialIndex;
	
	public InMemoryTinIndex(double argPointSpatialIndexDim)
	{
		this.faceSpatialIndex = new Quadtree();
		this.breaklineSpatialIndex = new Quadtree();
		this.boundarySpatialIndex = new Quadtree();
		this.pointSpatialIndex = new BasicTinPointSpatialIndex(argPointSpatialIndexDim);
	}

	@Override
	public TinFace getTinFaceAtLocation(Coordinate argCoordinate, double argRadius) 
	{
		// Create the envelope.
		double minEasting = argCoordinate.x - argRadius;
		double maxEasting = argCoordinate.x + argRadius;
		double minNorthing = argCoordinate.y - argRadius;
		double maxNorthing =argCoordinate.y + argRadius;

				
		// Create the envelope.
		Envelope envelope = new Envelope(minEasting, maxEasting, minNorthing, maxNorthing);
		
		List<TinFace> faces = faceSpatialIndex.query(envelope);
		
		Iterator<TinFace> goOverEach = faces.iterator();
		
		TinFaceUtilities utils = new TinFaceUtilities();
		
		while(goOverEach.hasNext() == true)
		{
			TinFace currentFace = goOverEach.next();
			utils.setSubject(currentFace);
			
			if(utils.isCoordinateOnTinFace(argCoordinate) == true)
			{
				return currentFace;
			}
		}
		
		IllegalStateException error = new IllegalStateException("There was no Tin Face at that location.");
		throw error;
	}
	
	public boolean hasTinFaceAtLocation(Coordinate argCoordinate, double argRadius)
	{
		// Create the envelope.
		double minEasting = argCoordinate.x - argRadius;
		double maxEasting = argCoordinate.x + argRadius;
		double minNorthing = argCoordinate.y - argRadius;
		double maxNorthing =argCoordinate.y + argRadius;

						
		// Create the envelope.
		Envelope envelope = new Envelope(minEasting, maxEasting, minNorthing, maxNorthing);
				
		List<TinFace> faces = faceSpatialIndex.query(envelope);
				
		if(faces.isEmpty() == true)
		{
			return false;
		}
				
		Iterator<TinFace> goOverEach = faces.iterator();
				
		TinFaceUtilities utils = new TinFaceUtilities();
				
		while(goOverEach.hasNext() == true)
		{
			TinFace currentFace = goOverEach.next();
			utils.setSubject(currentFace);
					
			if(utils.isCoordinateOnTinFace(argCoordinate) == true)
			{
				return true;
			}
		}
				
		return false;
	}

	@Override
	public Iterator<TinFace> getTinFacesInEnvelope(Envelope argEnvelope) 
	{
		List<TinFace> faces = this.faceSpatialIndex.query(argEnvelope);
		
		return faces.iterator();
	}
	
	public boolean hasTinFacesInEnvelope(Envelope argEnvelope) 
	{
		List<TinFace> faces = this.faceSpatialIndex.query(argEnvelope);
		
		if(faces.isEmpty() == true)
		{
			return false;
		}
		
		else
		{
			return true;
		}
	}
	
	@Override
	public TriangulatedIrregularNetwork subset(Envelope envelope)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<TinFace> getTrianglesAtHeight(double argElevation) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<TinFace> getTrianglesWithinRange(double argBottomElevation,
			double argTopElevation) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Iterator<TinPoint> getTinPointsInEnvelope(Envelope argEnvelope) 
	{
		return this.pointSpatialIndex.getTinPointsInEnvelope(argEnvelope);
	}

	@Override
	public Iterator<TinBreakline> getTinBreaklinesInEnvelope
		(Envelope argEnvelope) 
	{
		List<TinBreakline> breaklines = this.breaklineSpatialIndex.query(argEnvelope);
		return breaklines.iterator();
	}

	@Override
	public Iterator<TinBoundary> getTinBoundariesInEnvelope(Envelope argEnvelope)
	{
		List<TinBoundary> boundaries = this.boundarySpatialIndex.query(argEnvelope);
		
		return boundaries.iterator();
	}

	public void indexTinFace(TinFace argFace)
	{	
		Polygon triangle = argFace.getAsPolygon();		
		Envelope envelope = triangle.getEnvelopeInternal();
		
		this.faceSpatialIndex.insert(envelope, argFace);
	}
	
	public void indexTinPoint(TinPoint argPoint)
	{
		this.pointSpatialIndex.indexTinPoint(argPoint);		
	}
	
	public void indexTinBreakline(TinBreakline argBreakline)
	{
		LineString lineString = argBreakline.getLineString();
		
		Envelope envelope = lineString.getEnvelopeInternal();
		
		this.breaklineSpatialIndex.insert(envelope, argBreakline);
	}
	
	public void indexTinBoundary(TinBoundary argBoundary)
	{
		LineString lineString = argBoundary.getLineString();
		
		Envelope envelope = lineString.getEnvelopeInternal();
		
		this.breaklineSpatialIndex.insert(envelope, argBoundary);
	}

	@Override
	public boolean hasTrianglesAtHeight() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasTriangesWithinRange() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasTinPointInEnvelope(Envelope argEnvelope) 
	{
		Iterator<TinPoint> goOverEach = this.getTinPointsInEnvelope(argEnvelope);
		return goOverEach.hasNext();
	}

	@Override
	public boolean hasTinBreaklinesInEnvelope(Envelope argEnvelope) 
	{
		List<TinBreakline> breaklines = this.breaklineSpatialIndex.query(argEnvelope);
		return breaklines.isEmpty();
	}

	@Override
	public boolean hasTinBoundariesInEnvelope(Envelope argEnvelope) 
	{
		Iterator<TinBoundary> goOverEach = this.getTinBoundariesInEnvelope(argEnvelope);
		return goOverEach.hasNext();
	}
}
