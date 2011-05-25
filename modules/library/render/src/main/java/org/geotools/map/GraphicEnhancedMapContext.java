/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map;

import java.awt.Color;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Extends DefaultMapContext to provide the whole set of request parameters a
 * WMS GetMap request can have.
 * 
 * <p>
 * In particular, adds holding for the following parameter values:
 * 
 * <ul>
 * <li> WIDTH </li>
 * <li> HEIGHT </li>
 * <li> BGCOLOR </li>
 * <li> TRANSPARENT </li>
 * </ul>
 * </p>
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class GraphicEnhancedMapContext extends DefaultMapContext {

	/**
	 * requested map image width in output units (pixels)
	 * 
	 * @uml.property name="mapWidth" multiplicity="(0 1)"
	 */
	private int mapWidth;

	/**
	 * requested map image height in output units (pixels)
	 * 
	 * @uml.property name="mapHeight" multiplicity="(0 1)"
	 */
	private int mapHeight;

	/**
	 * Requested BGCOLOR, defaults to white according to WMS spec
	 * 
	 * @uml.property name="bgColor" multiplicity="(0 1)"
	 */
	private Color bgColor = Color.white;

	/**
	 * true if background transparency is requested
	 * 
	 * @uml.property name="transparent" multiplicity="(0 1)"
	 */
	private boolean transparent;

	/**
	 * Default constructor
	 * 
	 * @deprecated
	 */
	public GraphicEnhancedMapContext() {
		super();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param layers
	 * @deprecated
	 */
	public GraphicEnhancedMapContext(MapLayer[] layers) {
		super(layers);
	}

	/**
	 * Default constructor
	 */
	public GraphicEnhancedMapContext(final CoordinateReferenceSystem crs) {
		super(crs);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param layers
	 */
	public GraphicEnhancedMapContext(MapLayer[] layers,
			final CoordinateReferenceSystem crs) {
		super(layers, crs);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="bgColor"
	 */
	public Color getBgColor() {
		return this.bgColor;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param bgColor
	 *            DOCUMENT ME!
	 * 
	 * @uml.property name="bgColor"
	 */
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="mapHeight"
	 */
	public int getMapHeight() {
		return this.mapHeight;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param mapHeight
	 *            DOCUMENT ME!
	 * 
	 * @uml.property name="mapHeight"
	 */
	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="mapWidth"
	 */
	public int getMapWidth() {
		return this.mapWidth;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param mapWidth
	 *            DOCUMENT ME!
	 * 
	 * @uml.property name="mapWidth"
	 */
	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isTransparent() {
		return this.transparent;
	}

	/**
	 * Setting transparency for this wms context.
	 * 
	 * @param transparent
	 *            DOCUMENT ME!
	 * 
	 * @uml.property name="transparent"
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

}
