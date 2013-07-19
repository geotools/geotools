package org.geotools.jtinv2.io;

import java.util.Iterator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

public interface TinBuildingBlocks 
{
	public abstract Iterator<Polygon> getHoles();
	
	public abstract Polygon getOuterBoundary();
	
	public abstract Iterator<LineString> getBreaklines();
	
	public abstract Iterator<Coordinate> getVertices();
}
