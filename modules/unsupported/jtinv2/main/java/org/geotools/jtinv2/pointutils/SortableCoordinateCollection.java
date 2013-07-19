package org.geotools.jtinv2.pointutils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

public class SortableCoordinateCollection 
{

	private ArrayList<Coordinate> coords;
	
	
	// Constructors.
	
	public SortableCoordinateCollection(List<Coordinate> argCoordinates)
	{
		this.coords.addAll(argCoordinates);
	}
	
	// Public method definitions.
	
	public int getNumberOfCoordinates()
	{
		return this.coords.size();
	}
	
	public Iterator<Coordinate> getCoordinatesSortedByNorthingBiggerFirst()
	{
		// This method uses the insert sort algorithm.
		
		// Create a new collection to hold the sorted coordinates.
		LinkedList<Coordinate> sortedCoords = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> goOverEach = this.coords.iterator();
		
		while(goOverEach.hasNext() == true)
		{
			// Get the coordinate to insert.
			Coordinate currentCoordinate = goOverEach.next();
			
			// Iterate over all the coordinates in the sorted list and find the correct 
			// spot to insert the current coordinate.
			
			// Is the list empty?
			if(sortedCoords.isEmpty() == true)
			{
				// Just add the first coordinate.
				sortedCoords.add(currentCoordinate);
			}
			
			// It isn't empty. Find the right slot for the currentCoordinate.
			Iterator<Coordinate> goOverEachSortedCoord = sortedCoords.iterator();
			
			int counter = 1; //There should already be one coordinate in the list.
			
			while(goOverEachSortedCoord.hasNext() == true)
			{
				Coordinate currentSortedCoordinate = goOverEachSortedCoord.next();
				
				double currentSortedCoordinateNorthing = currentSortedCoordinate.y;
				double currentCoordinateNorthing = currentCoordinate.y;
				
				if(currentCoordinateNorthing >= currentSortedCoordinateNorthing)
				{
					int position = counter - 1; // List starts at position 0.
					sortedCoords.add(position, currentCoordinate);
				}
				
				counter++;		
			}
			
			// If we get to here, the Coordinate needs to go at the end of the sorted list.
			sortedCoords.add(currentCoordinate);
		}
		
		return sortedCoords.iterator();
	}
	
	public Iterator<Coordinate> getCoordinatesSortedByNorthingSmallerFirst()
	{
		LinkedList<Coordinate> backwardsList = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> goOverEach = this.getCoordinatesSortedByNorthingBiggerFirst();
		
		int counter = 1;
		
		while(goOverEach.hasNext() == true)
		{
			Coordinate currentCoordinate = goOverEach.next();
			int numberOfCoordinates = this.getNumberOfCoordinates();
			int position = numberOfCoordinates - (counter + 1);
			
			backwardsList.add(position, currentCoordinate);
			
			counter++;
		}
		
		return backwardsList.iterator();
	}
	
	public Iterator<Coordinate> getCoordinatesSortedByEastingBiggerFirst()
	{
		// This method uses the insert sort algorithm.
		
		// Create a new collection to hold the sorted coordinates.
		LinkedList<Coordinate> sortedCoords = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> goOverEach = this.coords.iterator();
		
		while(goOverEach.hasNext() == true)
		{
			// Get the coordinate to insert.
			Coordinate currentCoordinate = goOverEach.next();
			
			// Iterate over all the coordinates in the sorted list and find the correct 
			// spot to insert the current coordinate.
			
			// Is the list empty?
			if(sortedCoords.isEmpty() == true)
			{
				// Just add the first coordinate.
				sortedCoords.add(currentCoordinate);
			}
			
			// It isn't empty. Find the right slot for the currentCoordinate.
			Iterator<Coordinate> goOverEachSortedCoord = sortedCoords.iterator();
			
			int counter = 1; //There should already be one coordinate in the list.
			
			while(goOverEachSortedCoord.hasNext() == true)
			{
				Coordinate currentSortedCoordinate = goOverEachSortedCoord.next();
				
				double currentSortedCoordinateEasting = currentSortedCoordinate.x;
				
				double currentCoordinateEasting = currentCoordinate.x;
				
				if(currentCoordinateEasting >= currentSortedCoordinateEasting)
				{
					int position = counter - 1; // List starts at position 0.
					sortedCoords.add(position, currentCoordinate);
				}
				
				counter++;		
			}
			
			// If we get to here, the Coordinate needs to go at the end of the sorted list.
			sortedCoords.add(currentCoordinate);
		}
		
		return sortedCoords.iterator();
	}
	
	public Iterator<Coordinate> getCoordinatesSortedByEastingSmallerFirst()
	{
		LinkedList<Coordinate> backwardsList = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> goOverEach = this.getCoordinatesSortedByEastingBiggerFirst();
		
		int counter = 1;
		
		while(goOverEach.hasNext() == true)
		{
			Coordinate currentCoordinate = goOverEach.next();
			int numberOfCoordinates = this.getNumberOfCoordinates();
			int position = numberOfCoordinates - (counter + 1);
			
			backwardsList.add(position, currentCoordinate);
			
			counter++;
		}
		
		return backwardsList.iterator();
	}
	
	public Iterator<Coordinate> getCoordinatesSoretedByElevationBiggerFirst()
	{
		// This method uses the insert sort algorithm.
		
		// Create a new collection to hold the sorted coordinates.
		LinkedList<Coordinate> sortedCoords = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> goOverEach = this.coords.iterator();
		
		while(goOverEach.hasNext() == true)
		{
			// Get the coordinate to insert.
			Coordinate currentCoordinate = goOverEach.next();
			
			// Iterate over all the coordinates in the sorted list and find the correct 
			// spot to insert the current coordinate.
			
			// Is the list empty?
			if(sortedCoords.isEmpty() == true)
			{
				// Just add the first coordinate.
				sortedCoords.add(currentCoordinate);
			}
			
			// It isn't empty. Find the right slot for the currentCoordinate.
			Iterator<Coordinate> goOverEachSortedCoord = sortedCoords.iterator();
			
			int counter = 1; //There should already be one coordinate in the list.
			
			while(goOverEachSortedCoord.hasNext() == true)
			{
				Coordinate currentSortedCoordinate = goOverEachSortedCoord.next();
				
				double currentSortedCoordinateElevation = currentSortedCoordinate.z;
				double currentCoordinateElevation = currentCoordinate.z;
				
				if(currentCoordinateElevation >= currentSortedCoordinateElevation)
				{
					int position = counter - 1; // List starts at position 0.
					sortedCoords.add(position, currentCoordinate);
				}
				
				counter++;		
			}
			
			// If we get to here, the Coordinate needs to go at the end of the sorted list.
			sortedCoords.add(currentCoordinate);
		}
		
		return sortedCoords.iterator();
	}
	
	public Iterator<Coordinate> getCoordinatesSortedByElevationSmallerFirst()
	{
		LinkedList<Coordinate> backwardsList = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> goOverEach = this.getCoordinatesSoretedByElevationBiggerFirst();
		
		int counter = 1;
		
		while(goOverEach.hasNext() == true)
		{
			Coordinate currentCoordinate = goOverEach.next();
			int numberOfCoordinates = this.getNumberOfCoordinates();
			int position = numberOfCoordinates - (counter + 1);
			
			backwardsList.add(position, currentCoordinate);
			
			counter++;
		}
		
		return backwardsList.iterator();
	}
	
	public Iterator<Coordinate> getCoordinatesSortedByDistanceBiggerFirst(Coordinate argEndCoordinate)
	{
		// This method uses the insert sort algorithm.
		
		// Create a new collection to hold the sorted coordinates.
		LinkedList<Coordinate> sortedCoords = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> goOverEach = this.coords.iterator();
		
		while(goOverEach.hasNext() == true)
		{
			// Get the coordinate to insert.
			Coordinate currentCoordinate = goOverEach.next();
			
			// Iterate over all the coordinates in the sorted list and find the correct 
			// spot to insert the current coordinate.
			
			// Is the list empty?
			if(sortedCoords.isEmpty() == true)
			{
				// Just add the first coordinate.
				sortedCoords.add(currentCoordinate);
			}
			
			// It isn't empty. Find the right slot for the currentCoordinate.
			Iterator<Coordinate> goOverEachSortedCoord = sortedCoords.iterator();
			
			int counter = 1; //There should already be one coordinate in the list.
			
			while(goOverEachSortedCoord.hasNext() == true)
			{
				Coordinate currentSortedCoordinate = goOverEachSortedCoord.next();
				
				double distance1 = argEndCoordinate.distance(currentCoordinate);
				double distance2 = argEndCoordinate.distance(currentSortedCoordinate);
				
				if(distance1 >= distance2)
				{
					int position = counter - 1; // List starts at position 0.
					sortedCoords.add(position, currentCoordinate);
				}
				
				counter++;		
			}
			
			// If we get to here, the Coordinate needs to go at the end of the sorted list.
			sortedCoords.add(currentCoordinate);
		}
		
		return sortedCoords.iterator();
	}
	
	public Iterator<Coordinate> getCoordinatesSortedByDistanceSmallerFirst(Coordinate argEndPoint)
	{
		LinkedList<Coordinate> backwardsList = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> goOverEach = this.getCoordinatesSortedByDistanceBiggerFirst(argEndPoint);
		
		int counter = 1;
		
		while(goOverEach.hasNext() == true)
		{
			Coordinate currentCoordinate = goOverEach.next();
			int numberOfCoordinates = this.getNumberOfCoordinates();
			int position = numberOfCoordinates - (counter + 1);
			
			backwardsList.add(position, currentCoordinate);
			
			counter++;
		}
		
		return backwardsList.iterator();
	}
	
	public Iterator<Coordinate> getCoordinatesSortedByChangeInElevationBiggerFirst(Coordinate argEndCoordinate)
	{
		// This method uses the insert sort algorithm.
		
		// Create a new collection to hold the sorted coordinates.
		LinkedList<Coordinate> sortedCoords = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> goOverEach = this.coords.iterator();
		
		while(goOverEach.hasNext() == true)
		{
			// Get the coordinate to insert.
			Coordinate currentCoordinate = goOverEach.next();
			
			// Iterate over all the coordinates in the sorted list and find the correct 
			// spot to insert the current coordinate.
			
			// Is the list empty?
			if(sortedCoords.isEmpty() == true)
			{
				// Just add the first coordinate.
				sortedCoords.add(currentCoordinate);
			}
			
			// It isn't empty. Find the right slot for the currentCoordinate.
			Iterator<Coordinate> goOverEachSortedCoord = sortedCoords.iterator();
			
			int counter = 1; //There should already be one coordinate in the list.
			
			while(goOverEachSortedCoord.hasNext() == true)
			{
				Coordinate currentSortedCoordinate = goOverEachSortedCoord.next();
				
				double change1 = argEndCoordinate.z - currentCoordinate.z;
				double change2 = argEndCoordinate.z - currentSortedCoordinate.z;
				
				
				double change1Abs = Math.abs(change1);
				double change2Abs = Math.abs(change2);
				
				if(change1Abs >= change2Abs)
				{
					int position = counter - 1; // List starts at position 0.
					sortedCoords.add(position, currentCoordinate);
				}
				
				counter++;		
			}
			
			// If we get to here, the Coordinate needs to go at the end of the sorted list.
			sortedCoords.add(currentCoordinate);
		}
		
		return sortedCoords.iterator();
	}
	
	public Iterator<Coordinate> getCoordinatesSortedByChangeInElevationSmallerFirst(Coordinate argBenchmark)
	{
		LinkedList<Coordinate> backwardsList = new LinkedList<Coordinate>();
		
		Iterator<Coordinate> goOverEach = this.getCoordinatesSortedByChangeInElevationBiggerFirst(argBenchmark);
		
		int counter = 1;
		
		while(goOverEach.hasNext() == true)
		{
			Coordinate currentCoordinate = goOverEach.next();
			int numberOfCoordinates = this.getNumberOfCoordinates();
			int position = numberOfCoordinates - (counter + 1);
			
			backwardsList.add(position, currentCoordinate);
			
			counter++;
		}
		
		return backwardsList.iterator();
	}
}
