/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.topograph2D.index;

/**
 * Models an Event point within the Sweep Line Intersection Algorithm
 *
 *
 * @source $URL$
 */
public class SweepLineEvent implements Comparable {
	public static final int INSERT = 1;

	public static final int DELETE = 2;

	Object edgeSet; // used for red-blue intersection detection

	private double xValue;

	private int eventType;

	private SweepLineEvent insertEvent; // null if this is an INSERT event

	private int deleteEventIndex;

	private Object obj;

	/**
	 * 
	 * @param edgeSet
	 * @param x
	 * @param insertEvent
	 * @param obj
	 */
	public SweepLineEvent(Object edgeSet, double x, SweepLineEvent insertEvent,
			Object obj) {
		this.edgeSet = edgeSet;
		xValue = x;
		this.insertEvent = insertEvent;
		this.eventType = INSERT;
		if (insertEvent != null)
			eventType = DELETE;
		this.obj = obj;
	}

	public boolean isInsert() {
		return insertEvent == null;
	}

	public boolean isDelete() {
		return insertEvent != null;
	}

	public SweepLineEvent getInsertEvent() {
		return insertEvent;
	}

	public int getDeleteEventIndex() {
		return deleteEventIndex;
	}

	public void setDeleteEventIndex(int deleteEventIndex) {
		this.deleteEventIndex = deleteEventIndex;
	}

	public Object getObject() {
		return obj;
	}

	/**
	 * ProjectionEvents are ordered first by their x-value, and then by their
	 * eventType. It is important that Insert events are sorted before Delete
	 * events, so that items whose Insert and Delete events occur at the same
	 * x-value will be correctly handled.
	 */
	public int compareTo(Object o) {
		SweepLineEvent pe = (SweepLineEvent) o;
		if (xValue < pe.xValue)
			return -1;
		if (xValue > pe.xValue)
			return 1;
		if (eventType < pe.eventType)
			return -1;
		if (eventType > pe.eventType)
			return 1;
		return 0;
	}

}
