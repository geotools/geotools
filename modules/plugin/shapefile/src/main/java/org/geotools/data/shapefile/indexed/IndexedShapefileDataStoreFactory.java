/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
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
package org.geotools.data.shapefile.indexed;

import org.geotools.data.shapefile.ShapefileDataStoreFactory;

/**
 * This factory is maintained for GeoTools 2.3 code that made use of
 * IndexedShapefileDataStoreFactory directly.
 * 
 * @deprecated Please use ShapefileDataStoreFactory, it can create an
 *             IndexedDataStore if appropriate
 * @author Chris Holmes, TOPP
 *
 * @source $URL$
 * @version $Id: IndexedShapefileDataStoreFactory.java 27223 2007-09-29
 *          19:42:29Z jgarnett $
 */
public class IndexedShapefileDataStoreFactory extends ShapefileDataStoreFactory {

    public String getDisplayName() {
        return "Shapefile (Indexed)";
    }

}
