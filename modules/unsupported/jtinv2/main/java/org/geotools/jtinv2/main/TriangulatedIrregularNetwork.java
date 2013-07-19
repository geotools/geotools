/**
 * 
 */
package org.geotools.jtinv2.main;

import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author paradox
 *
 */
public interface TriangulatedIrregularNetwork 
{
	
	/**
	 * Returns the number of vertices/points that make up this TIN.
	 * 
	 * @return	number of points in the TIN
	 */
	public int getNumberOfTinPoints();
	
	/**
	 * Returns the number of triangular faces that make up this TIN.
	 * 
	 * @return a count of triangular faces that make up this TIN.
	 */
	public int getNumberOfTriangles();
	
	
	/**
	 * Returns the number of breaklines in this TIN.
	 * 
	 * @return a count of breaklines present in this TIN.
	 */
	public int getNumberOfBreaklines();
	
	/**
	 * Returns the number of boundaries in this TIN.
	 * 
	 * @return a count of boundaries present in this TIN.
	 */
	public int getNumberOfBoundaries();
	
	/**
	 * Return an array containing all the points of this TIN. The returned
	 * value is not a copy.
	 * 
	 * @return all the vertices that compose this TIN.
	 */
	public Iterator<TinPoint> getTinPointsIterator();
		
	/**
	 * Returns a list of int arrays, each representing an ordered list of 
	 * indices to the point array, that each describe a single breakline.
	 * 
	 * @return a list of array indices that each represent a breakline
	 */
	public Iterator<TinBreakline> getTinBreaklinesIterator();
	
	/**
	 * Returns a list of int arrays, each representing an ordered list of 
	 * indices to the point array, that each describe a single boundary.
	 * 
	 * @return a list of array indices that each represent a boundary
	 */
	public Iterator<TinBoundary> getTinBoundariesIterator();
	
	public TinBoundary getOuterBoundary();
	
	public Iterator<TinBoundary> getHoleIterator();
	
	/**
	 * Returns the envelope that contains this TIN.
	 */
	public Envelope getEnvelope();
}
