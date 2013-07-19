package org.geotools.jtinv2.main;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class TinFaceUtilities 
{
	private TinFace subject;
	
	public boolean isCoordinateOnTinFace(Coordinate argCoordinate)
	{
		Polygon triangle = this.subject.getAsPolygon();
		GeometryFactory factory = new GeometryFactory();
		
		Point coordinateAsGeometry = factory.createPoint(argCoordinate);
		
		return triangle.contains(coordinateAsGeometry);
	}
	
	public void setSubject(TinFace argSubject)
	{
		this.subject = argSubject;
	}
}
