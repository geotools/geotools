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

package org.geotools.jtinv2.index;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.geotools.jtinv2.main.TinPoint;

/**
 * Allows objects implementing the HasElevation interface to be indexed. This
 * is a one-dimensional spatial index that ignores the horizontal position of
 * spatial objects.
 */
public class ElevationIndex 
{
	
	private HashMap<Integer, LinkedList<HasElevation>> levels;
	private double interval;
	private int highestLevel;
	private int lowestLevel;
	private LinkedList<HasElevation> allIndexedElements;
	
	/**
	 * Constructs a new ElevationIndex. The double argument specifies 
	 * the interval used to build the index and sort its elements. The 
	 * interval is used to divide the vertical envelope containing all
	 * the elements added to an index. 
	 * 
	 * For example: If the total vertical change 
	 * between all of the elements in the index is 200 units, and you pass
	 * a value of 10 as the interval, the elements in the index will be
	 * sorted into 20 units or levels. 
	 * 
	 * A small interval will result in less
	 * filtering of indexed items by algorithms. A small interval may
	 * require more space occupy in computer memory and may be slower for some
	 * operations.
	 */
	public ElevationIndex(double argInterval)
	{
		this.interval = argInterval;
		this.lowestLevel = 0;
		this.highestLevel = 0;
		this.allIndexedElements = new LinkedList<HasElevation>();
		this.levels = new HashMap<Integer, LinkedList<HasElevation>>();
	}
	
	/**
	 * Adds an element to the index by its elevation range.
	 */
	public void indexByElevationRange(HasElevation argTarget)
	{
		boolean hasPrimaryElevation = argTarget.hasPrimaryElevation();
		
		if(hasPrimaryElevation == true)
		{
			this.indexByPrimaryElevation(argTarget);
		}
		
		double minElevation = argTarget.getLowestElevation();
		double maxElevation = argTarget.getHighestElevation();
		
		int minLevel = this.calculateMinLevel(minElevation);
		int maxLevel = this.calculateMaxLevel(maxElevation);
		
		// Add this element to all the levels between min and max.
		int counter = minLevel;
		
		while(counter <= maxLevel)
		{
			Integer counterAsInteger = new Integer(counter);
			
			// Does the level already exist?
			if(this.levels.containsKey(counterAsInteger) == true)
			{
				LinkedList<HasElevation> elements = this.levels.get(counterAsInteger);
				elements.add(argTarget);
			}
			
			// The level doesn't exist. We need to create it.
			else
			{
				LinkedList<HasElevation> elements = new LinkedList<HasElevation>();
				elements.add(argTarget);
				this.levels.put(counterAsInteger, elements);
				
				// Is this now the highest or lowest level?
				if(counter < this.lowestLevel)
				{
					this.lowestLevel = counter;
				}
				
				if(counter > this.highestLevel)
				{
					this.highestLevel = counter;
				}
			}
			
			counter++;
		}
		
		this.allIndexedElements.add(argTarget);
	}
	
	/** 
	 * Adds an element to the index using its primary elevation.
	 */
	public void indexByPrimaryElevation(HasElevation argTarget)
	{
		boolean hasPrimaryElevation = argTarget.hasPrimaryElevation();
		
		if(hasPrimaryElevation == false)
		{
			this.indexByElevationRange(argTarget);
		}
		
		double primaryElevation = argTarget.getPrimaryElevation();
		
		int level = this.calculateMinLevel(primaryElevation);
		Integer levelAsInteger = new Integer(level);
		
		// Does the level already exist?
		if(this.levels.containsKey(levelAsInteger) == true)
		{
			LinkedList<HasElevation> elements = this.levels.get(levelAsInteger);
			elements.add(argTarget);
		}
		
		// The level doesn't exist. We need to create it.
		else
		{
			LinkedList<HasElevation> elements = new LinkedList<HasElevation>();
			elements.add(argTarget);
			this.levels.put(levelAsInteger, elements);
			
			if(level > this.highestLevel)
			{
				this.highestLevel = level;
			}
		}
		
		this.allIndexedElements.add(argTarget);
	}
	
	/**
	 * Returns an iterator over all the elements that fall within the
	 * given elevation range.
	 */
	public Iterator<HasElevation> getElementsInElevationRange(double argFloor, double argCeiling)
	{
		int minLevel = this.calculateMinLevel(argFloor);
		int maxLevel = this.calculateMaxLevel(argCeiling);
		boolean elementsWereFound = false;
		
		// Return value.
		LinkedList<HasElevation> foundElements = new LinkedList<HasElevation>();
		
		// Add this element to all the levels between min and max.
		int counter = minLevel;
		
		while(counter <= maxLevel)
		{
			Integer counterAsInteger = new Integer(counter);
			
			// Does the level already exist?
			if(this.levels.containsKey(counterAsInteger) == true)
			{
				LinkedList<HasElevation> elements = this.levels.get(counterAsInteger);
				foundElements.addAll(elements);
				elementsWereFound = true;
			}
			
			// The level doesn't exist. 
			else
			{
				// Do nothing here.
			}
			
			counter++;
		}
		
		if(elementsWereFound == false)
		{
			IllegalStateException error = new IllegalStateException("There are no elements at that elevation.");
			throw error;
		}
		
		return foundElements.iterator();
	}
	
	/**
	 * Returns all the elements that exist at the given elevation. The tolerance
	 * passed as an argument is used to determine this for objects that
	 * are stored in the index by a single, or primary elevation. 
	 * If the object's primary elevation is within the tolerance of the target elevation
	 * passed to the method, the object will be included in the elements 
	 * returned by the iterator.
	 */
	public Iterator<HasElevation> getElementsAtElevation(double argElevation, double argTolerance)
	{
		int level = this.calculateMinLevel(argElevation);
		
		if(this.hasElementsInLevel(level) == true)
		{
			LinkedList<HasElevation> elements = this.levels.get(level);
			LinkedList<HasElevation> filteredElements = new LinkedList<HasElevation>();
			
			// Filter the elements. 
			Iterator<HasElevation> goOverEach = elements.iterator();
			
			while(goOverEach.hasNext() == true)
			{
				HasElevation currentElement = goOverEach.next();
				
				// Do we have a single elevation or an elevation range?
				if(currentElement.hasPrimaryElevation() == true)
				{
					double primaryElevation = currentElement.getPrimaryElevation();
					
					// Are we within tolerance?
					double diff = primaryElevation - argElevation;
					double diffAbs = Math.abs(diff);
					
					if(diff < argTolerance)
					{
						filteredElements.add(currentElement);
					}
				}
				
				else
				{
					// We are dealing with an elevation range.
					double lowestElevation = currentElement.getLowestElevation();
					double highestElevation = currentElement.getHighestElevation();
					
					// Are we between the low and high?
					if(argElevation > lowestElevation)
					{
						if(argElevation < highestElevation)
						{
							filteredElements.add(currentElement);
						}
					}
				}
			}
			
			return filteredElements.iterator();
		}
		
		else
		{
			IllegalStateException error = new IllegalStateException("There are no elements at that elevation.");
			throw error;
		}
	}
	
	/**
	 * Returns all the elements in the index below the elevation provided as an argument.
	 */
	public Iterator<HasElevation> getElementsBelowElevation(double argElevation)
	{
		int maxLevel = this.calculateMaxLevel(argElevation);
		int counter = this.lowestLevel;
		boolean elementsFound = false;
		
		// Return value.
		LinkedList<HasElevation> foundElements = new LinkedList<HasElevation>();
		
		while(counter < maxLevel)
		{
			Integer counterAsInteger = new Integer(counter);
			
			// Does the level already exist?
			if(this.levels.containsKey(counterAsInteger) == true)
			{
				LinkedList<HasElevation> elements = this.levels.get(counterAsInteger);
				foundElements.addAll(elements);
				elementsFound = true;
			}
			
			counter++;
		}
		
		if(elementsFound == false)
		{
			IllegalStateException error = new IllegalStateException("There are no elements at that elevation.");
			throw error;
		}
		
		// Filter the found elements.
		LinkedList<HasElevation> filteredElements = new LinkedList<HasElevation>();
		
		Iterator<HasElevation> goOverEach = foundElements.iterator();
		
		while(goOverEach.hasNext() == true)
		{
			HasElevation currentElement = goOverEach.next();
			double elevation = 0;
			
			boolean hasHighestElevation = currentElement.hasHighestElevation();
			
			if(hasHighestElevation == true)
			{
				elevation = currentElement.getHighestElevation();
			}
			
			else
			{
				elevation = currentElement.getPrimaryElevation();
			}
			
			if(elevation < argElevation)
			{
				filteredElements.add(currentElement);
			}
		}
		
		return filteredElements.iterator();
	}
	
	/**
	 * Returns all the elements in the index above the elevation provided as an argument.
	 */
	public Iterator<HasElevation> getElementsAboveElevation(double argElevation)
	{
		int minLevel = this.calculateMinLevel(argElevation);
		int counter = this.highestLevel;
		boolean elementsFound = false;

		// Return value.
		LinkedList<HasElevation> foundElements = new LinkedList<HasElevation>();
		
		while(counter >= minLevel)
		{
			Integer counterAsInteger = new Integer(counter);
			
			// Does the level already exist?
			if(this.levels.containsKey(counterAsInteger) == true)
			{;
				LinkedList<HasElevation> elements = this.levels.get(counterAsInteger);
				
				foundElements.addAll(elements);
				elementsFound = true;				
			}
			
			counter--;
		}
		
		if(elementsFound == false)
		{
			IllegalStateException error = new IllegalStateException("There are no elements at that elevation.");
			throw error;
		}
		
		// Filter the found elements.
		LinkedList<HasElevation> filteredElements = new LinkedList<HasElevation>();
		
		Iterator<HasElevation> goOverEach = foundElements.iterator();
		
		while(goOverEach.hasNext() == true)
		{
			HasElevation currentElement = goOverEach.next();
			double elevation = 0;
			
			boolean hasLowestElevation = currentElement.hasLowestElevation();
			
			if(hasLowestElevation == true)
			{
				elevation = currentElement.getLowestElevation();
			}
			
			else
			{
				elevation = currentElement.getPrimaryElevation();			
			}
			
			if(elevation > argElevation)
			{
				filteredElements.add(currentElement);
			}
		}
		
		return filteredElements.iterator();
	}
	
	/**
	 * Returns the number of items indexed.
	 * @return
	 */
	public int getNumberOfItemsIndexed()
	{
		return this.allIndexedElements.size();
	}
	
	/**
	 * Returns an iterator over all the items in the index.
	 */
	public Iterator<HasElevation> getIterator()
	{
		return this.allIndexedElements.iterator();
	}
	
	private int calculateMinLevel(double argElevation)
	{
		double level = argElevation / this.interval;
		return (int) Math.floor(level);
	}
	
	private int calculateMaxLevel(double argElevation)
	{
		double level = argElevation / this.interval;
		return (int) Math.ceil(level);
	}
	
	private boolean hasElementsInLevel(int argLevel)
	{
		return this.levels.containsKey(argLevel);
	}
}
