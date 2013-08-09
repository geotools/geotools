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

import java.util.Iterator;
import java.util.List;

import org.geotools.jtinv2.main.TinPoint;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Provides a spatial index for efficient access to TinPoints based on location.
 */
public interface TinPointSpatialIndex 
{	
	/**
	 * Returns the TinPoints within the envelope.
	 */
	public abstract  Iterator<TinPoint> getTinPointsInEnvelope(Envelope argEnvelope);

	/**
	 * Returns all TinPoints within the range or distance of the target TinPoint.
	 */
	public Iterator<TinPoint> getTinPointsInRangeOfTinPoint(TinPoint argTarget, double argRange);
	
	/**
	 * Adds a TinPoint to this index.
	 * 
	 * Implementations should be of this interface should be immutable.
	 * This method that should only be used by factory classes.
	 */
	public void indexTinPoint(TinPoint argTarget);
}
