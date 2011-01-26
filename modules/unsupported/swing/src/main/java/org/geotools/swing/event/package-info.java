/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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

/**
 * Mouse events and listener classes to work with both window and map positions.
 * <p>
 * Please keep in mind the following coordinate systems are in use at any point in time:
 * <ul>
 * <li>screen coordinates: the X,Y relative to the widget</li>
 * <li>map coordinates: the position in world coordinates as defined by the MapContext CoordinateReferenceSystem
 * (for "EPSG:4326" these would be on lon/lat)</li>
 * <li>data coordinates: the position in data coordinates as defined by a single MapLayer</li>
 * </ul>
 */
package org.geotools.swing.event;

