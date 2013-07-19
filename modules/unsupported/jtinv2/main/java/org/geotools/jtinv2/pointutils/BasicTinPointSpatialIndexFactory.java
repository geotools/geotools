package org.geotools.jtinv2.pointutils;

import org.geotools.jtinv2.main.TinPoint;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Creates a BasicCoordinateSpatialIndexFactory. To use this factory, add points
 * with the "addCoordinate" method. When you are done, call the "build" method.
 */
public class BasicTinPointSpatialIndexFactory 
{
	private BasicTinPointSpatialIndex index;
	
	public BasicTinPointSpatialIndexFactory(double argDimension)
	{
		this.index = new BasicTinPointSpatialIndex(argDimension);
	}
	
	/**
	 * Indexes the coordinate passed as an argument.
	 */
	public void indexTinPoint(TinPoint argTinPoint)
	{
		this.index.indexTinPoint(argTinPoint);
	}
	
	/**
	 * Returns the BasicCoordinateSpatialIndex just built by this factory.
	 */
	public BasicTinPointSpatialIndex build()
	{
		return this.index;
	}
}
