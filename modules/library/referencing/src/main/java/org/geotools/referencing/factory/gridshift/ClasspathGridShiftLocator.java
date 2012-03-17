/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.gridshift;

import java.net.URL;

import org.geotools.factory.AbstractFactory;
import org.geotools.metadata.iso.citation.Citations;
import org.opengis.metadata.citation.Citation;

/**
 * Default grid shift file locator, looks up grids in the classpath
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
public class ClasspathGridShiftLocator extends AbstractFactory implements GridShiftLocator {

    public ClasspathGridShiftLocator() {
        super(NORMAL_PRIORITY);
    }

    @Override
    public Citation getVendor() {
        return Citations.GEOTOOLS;
    }

    @Override
    public URL locateGrid(String grid) {
        return getClass().getResource(grid);
    }

}
