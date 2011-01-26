/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.geometry.jts.LiteShape2;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.style.TextStyle2D;

import com.vividsolutions.jts.geom.Geometry;
 
/**
 * The Labelling information that is put in the label cache.
 * 
 * @author jeichar
 * @author dblasby
 * @author simone giannecchini  * @source $URL$
 * @deprecated Use {@link LabelCacheItem} instead (along with {@link LabelCacheImpl}
 *
 * @source $URL$
 */
public class LabelCacheItem implements Comparable<LabelCacheItem> {
	TextStyle2D textStyle;
	List<Geometry> geoms=new ArrayList<Geometry>();
	double priority = 0.0;
	int spaceAround = 0;
	String label;
	private Set<String> layerIds=new HashSet<String>();
	
	public String getLabel()
	{
		return label;
	}
	public void setLabel(String l)
	{
		label = l;
	}
	
	/**
	 * space around - "dont put any label near me by this # of pixels"
	 */
	public int getSpaceAround()
	{
		return spaceAround;
	}
	
	/**
	 * space around - "dont put any label near me by this # of pixels"
	 */
	public void setSpaceAround(int space)
	{
		spaceAround = space;
	}
	
	public double getPriority()
	{
		return priority;
	}
	
	public void setPriority(double d)
	{
		priority = d;
	}
	/**
	 * Construct <code>LabelCacheItem</code>.
	 */
	public LabelCacheItem(String layerId, TextStyle2D textStyle, LiteShape2 shape, String label) 
	{
		this.textStyle=textStyle;
		this.geoms.add(shape.getGeometry());
		this.label = label;
		this.layerIds.add(layerId);
	}
	
	/**
	 * Return a modifiable set of ids
	 * @return
	 */
	public Set<String> getLayerIds() {
		return Collections.synchronizedSet(layerIds);
	}
	
	/**
	 * The list of geometries this item maintains
	 */
	public List<Geometry> getGeoms() {
		return geoms;
	}

	/**
	 * The textstyle that is used to label the shape.
	 */
	public TextStyle2D getTextStyle() {
		return textStyle;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof String) {
			String label = (String) arg0;
			return label.equals(textStyle.getLabel());
		}
		if (arg0 instanceof LabelCacheItem) {
			LabelCacheItem item = (LabelCacheItem) arg0;
			return textStyle.getLabel().equals( item.getTextStyle().getLabel() );
		}
		if (arg0 instanceof TextStyle2D) {
			TextStyle2D text = (TextStyle2D) arg0;
			return textStyle.getLabel().equals(text.getLabel());
		}
		return false;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return textStyle.getLabel().hashCode();
	}

	/**
	 * Returns an example geometry from the list of geometries.
	 */
	public Geometry getGeometry() {
		return (Geometry) geoms.get(0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(LabelCacheItem o) 
	{
		LabelCacheItem other = (LabelCacheItem) o;
		return Double.compare(this.getPriority(),other.getPriority() );		
	}
}
