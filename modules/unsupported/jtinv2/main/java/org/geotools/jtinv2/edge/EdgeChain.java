package org.geotools.jtinv2.edge;

import java.util.Iterator;

import org.geotools.jtinv2.main.TinPoint;


public interface EdgeChain
{
	public abstract Iterator<TinEdge> getEdgeIterator();
	
	public abstract int getNumberOfEdges();
	
	public abstract double getTotalSlopeLength();
	
	public abstract double getTotal2dLength();
	
	public abstract Iterator<TinPoint> getVertices();
	
	public abstract double getElevationRange();
	
	public abstract Iterator<TinPoint> getPointsSortedByElevation();
}
