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
package org.geotools.geometry.iso.index;

import java.util.ArrayList;

/**
 * 
 *
 *
 * @source $URL$
 */
public class ArrayListVisitor implements ItemVisitor {

	private ArrayList items = new ArrayList();

	public ArrayListVisitor() {
	}

	public void visitItem(Object item) {
		items.add(item);
	}

	public ArrayList getItems() {
		return items;
	}

}
