/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.palette;

import java.awt.image.IndexColorModel;

/**
 * A tool transforming a generic RGBA color into an index into a palette represented by a
 * IndexedColorModel
 * 
 * @author Andrea Aime - GeoSolutions
 */
public interface ColorIndexer {

    public IndexColorModel toIndexColorModel();

    public int getClosestIndex(int r, int g, int b, int a);
}
