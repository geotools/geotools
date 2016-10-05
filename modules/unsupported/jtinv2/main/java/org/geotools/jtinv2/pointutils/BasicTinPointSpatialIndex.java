/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.jtinv2.pointutils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.geotools.jtinv2.main.TinPoint;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * An implementation of CoordinateSpatialIndex.
 */
public class BasicTinPointSpatialIndex implements TinPointSpatialIndex
{
	// Each value in the rows HashMap represents a list of cells.
	// Each cell holds a list of Coordinate objects.
	private HashMap<Integer, HashMap<Integer, List<TinPoint>>> rows;
	private double cellDimension;
	private int numberOfPointsIndexed;

	public BasicTinPointSpatialIndex(double argCellDimension)
	{
		this.cellDimension = argCellDimension;
		this.rows = new HashMap<Integer, HashMap<Integer, List<TinPoint>>>();
	}
	
	@Override
	public void indexTinPoint(TinPoint argTarget) 
	{
		this.numberOfPointsIndexed++;
		
		Coordinate targetCoordinate = argTarget.getCoordinate();
		
		Integer rowAsInteger = this.calculateMinRowNumber(targetCoordinate.y);
		Integer columnAsInteger = this.calculateMinColumnNumber(targetCoordinate.x);
		
		// Get the row.
		if(this.rows.containsKey(rowAsInteger) == true)
		{
			// The row exists. Get it.
			HashMap<Integer, List<TinPoint>> row = this.rows.get(rowAsInteger);
			
			// Get the cell;
			if(row.containsKey(columnAsInteger) == true)
			{
				// The cell already exists. Get it.
				List<TinPoint> points = row.get(columnAsInteger);
				points.add(argTarget);
			}
			
			else
			{;
				// The cell doesn't exist.
				LinkedList<TinPoint> points = new LinkedList<TinPoint>();
				points.add(argTarget);
				
				// Add the cell.
				row.put(columnAsInteger, points);
			}
		}
		
		else
		{		
			// The row doesn't exist.
			HashMap<Integer, List<TinPoint>> row = new HashMap<Integer, List<TinPoint>>();
			
			// If the row doesn't exist, the cell doesn't exist.
			LinkedList<TinPoint> coords = new LinkedList<TinPoint>();
			coords.add(argTarget);
			row.put(columnAsInteger, coords);
			
			// Add the new row.
			this.rows.put(rowAsInteger, row);
		}
	}

	@Override
	public Iterator<TinPoint> getTinPointsInEnvelope(Envelope argEnvelope) 
	{
		double maxEasting = argEnvelope.getMaxX();
		double minEasting = argEnvelope.getMinX();
		
		double maxNorthing = argEnvelope.getMaxY();
		double minNorthing = argEnvelope.getMinY();
		
		// Calculate max row.
		int maxRow = this.calculateMaxRowNumber(maxNorthing);
		System.err.println("The max row was: ");
		System.err.println(maxRow);
		
		// Calculate min row.
		int minRow = this.calculateMinRowNumber(minNorthing);
		System.err.println("The min row was: ");
		System.err.println(minRow);
		
		// Calculate max column.
		int maxColumn = this.calculateMaxColumnNumber(maxEasting);
		System.err.println("The max column was: ");
		System.err.println(maxColumn);
		
		// Calculate min column.
		int minColumn = this.calculateMinColumnNumber(minEasting);
		System.err.println("The min column was: ");
		System.err.println(minColumn);
		
		LinkedList<TinPoint> foundPoints = new LinkedList<TinPoint>();
		
		// Get coordinates in envelope.
		// Iterate through each selected row and get the points from the selected columns.
		while(minRow <= maxRow)
		{
			// Get the current row.
			Integer rowId = new Integer(minRow);
			
			// Is there a row?
			boolean hasRow = this.rows.containsKey(rowId);
			
			if(hasRow == true)
			{			
				HashMap<Integer, List<TinPoint>> cells = this.rows.get(rowId);
			
				// Iterate through the cells.
				int counter = minColumn; // We need this because minColumn needs to be the same when we loop through the next row.
				while(counter <= maxColumn)
				{
					Integer columnId = new Integer(counter);
					
					// Does this cell exist?
					boolean hasCell = cells.containsKey(columnId);
					
					if(hasCell == true)
					{						
						List<TinPoint> cellPoints = cells.get(columnId);
						foundPoints.addAll(cellPoints);
					}
					
					counter++;
				}
			}
			
			minRow++;
		}
	
		if(foundPoints.isEmpty() == true)
		{
			IllegalStateException error = new IllegalStateException("There were no points in that envelope.");
			{
				throw error;
			}
		}
		
		// Filter found points for only those inside the envelope.		
		Iterator<TinPoint> goOverEach = foundPoints.iterator();
		
		LinkedList<TinPoint> filteredPoints = new LinkedList<TinPoint>();
		
		while(goOverEach.hasNext() == true)
		{
			TinPoint currentPoint = goOverEach.next();
			Coordinate currentCoordinate = currentPoint.getCoordinate();
			
			boolean isInEnvelope = argEnvelope.contains(currentCoordinate);
			
			if(isInEnvelope == true)
			{
				filteredPoints.add(currentPoint);
			}
		}
		
		return filteredPoints.iterator();
	}
	
	public Iterator<TinPoint> getTinPointsInRangeOfTinPoint(TinPoint argTarget, double argRange)
	{
		// Get the coordinate for the target point.
		Coordinate targetCoordinate = argTarget.getCoordinate();
		
		// Create the envelope.
		double minEasting = targetCoordinate.x - argRange;
		double maxEasting = targetCoordinate.x + argRange;
		double minNorthing = targetCoordinate.y - argRange;
		double maxNorthing = targetCoordinate.y + argRange;
		
		LinkedList<TinPoint> foundPoints = new LinkedList<TinPoint>();
		
		// Create the envelope.
		Envelope envelope = new Envelope(maxEasting, minEasting, maxNorthing, minNorthing);
		
		Iterator<TinPoint> pointsInEnvelope = this.getTinPointsInEnvelope(envelope);
		
		// Get all the points from the envelope within range.
		while(pointsInEnvelope.hasNext() == true)
		{
			TinPoint currentPoint = pointsInEnvelope.next();
			
			Coordinate currentCoordinate = currentPoint.getCoordinate();
			
			double distance = currentCoordinate.distance(targetCoordinate);
			
			if(distance <= argRange)
			{
				foundPoints.add(currentPoint);
			}
		}
		
		return foundPoints.iterator();
	}
	
//	public void listTinPointsInCell(int argRow, int cell)
//	{
//		HashMap<Integer, List<TinPoint>> row = this.rows.get(argRow);
//		List<TinPoint> pointsInCell = row.get(cell);
//		
//		Iterator<TinPoint> goOverEach = pointsInCell.iterator();
//		
//		System.err.println("About to list points in cell: ");
//		
//		while(goOverEach.hasNext() == true)
//		{
//			TinPoint currentPoint = goOverEach.next();
//			System.err.println(currentPoint.getIdentifier());
//		}
//		
//	}
	
	public int getTotalNumberOfIndexedPoints()
	{
		Collection<HashMap<Integer, List<TinPoint>>> rows = this.rows.values();
		Iterator<HashMap<Integer, List<TinPoint>>> goOverEachRow = rows.iterator();
		
		int totalCount = 0;
		
		while(goOverEachRow.hasNext() == true)
		{
			HashMap<Integer, List<TinPoint>> row = goOverEachRow.next();
			
			Collection<List<TinPoint>> cells = row.values();
			
			Iterator<List<TinPoint>> goOverEachCell = cells.iterator();
			
			while(goOverEachCell.hasNext() == true)
			{
				List<TinPoint> points = goOverEachCell.next();
				int cellCount = points.size();
				
				totalCount = totalCount + cellCount;
			}
		}
		
		return totalCount;
	}
	
	private Integer calculateMinRowNumber(double argNorthing)
	{		
		// Calculate the row number.
		double rowAsDouble = argNorthing / this.cellDimension;
		double rowFloored = Math.floor(rowAsDouble);
		
		Double rowFlooredAsDouble = new Double(rowFloored);
		
		int rowAsInt = rowFlooredAsDouble.intValue();
		
		Integer rowAsInteger = new Integer(rowAsInt);
		
		return rowAsInteger;
	}
	
	private Integer calculateMaxRowNumber(double argNorthing)
	{		
		// Calculate the row number.
		double rowAsDouble = argNorthing / this.cellDimension;
		double rowCeiling = Math.ceil(rowAsDouble);
		
		Double rowCeilingAsDouble = new Double(rowCeiling);
		
		int rowAsInt = rowCeilingAsDouble.intValue();
		
		Integer rowAsInteger = new Integer(rowAsInt);
		
		return rowAsInteger;
	}
	
	
	private Integer calculateMaxColumnNumber(double argEasting)
	{
		// Calculate the column number.
		double columnAsDouble = argEasting / this.cellDimension;
		double columnCeiling = Math.ceil(columnAsDouble);
				
		Double columnCeilingAsDouble = new Double(columnCeiling);
				
		int columnAsInt = columnCeilingAsDouble.intValue();
				
		Integer columnAsInteger = new Integer(columnAsInt);
		
		return columnAsInteger;
	}
	
	private Integer calculateMinColumnNumber(double argEasting)
	{
		// Calculate the column number.
		double columnAsDouble = argEasting / this.cellDimension;
		double columnFloored = Math.floor(columnAsDouble);
				
		Double columnFlooredAsDouble = new Double(columnFloored);
				
		int columnAsInt = columnFlooredAsDouble.intValue();
				
		Integer columnAsInteger = new Integer(columnAsInt);
		
		return columnAsInteger;
	}

}
