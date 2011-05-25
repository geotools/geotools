/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml;

import org.xml.sax.helpers.XMLFilterImpl;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Simple test implementation of <code>GMLHandlerJTS</code>. This very simple
 * handler just prints every JTS geometry that it gets to the standard output.
 *
 * @author Rob Hranac, Vision for New York
 *
 * @source $URL$
 * @version $Id$
 */
public class TestHandler extends XMLFilterImpl implements GMLHandlerJTS {
    public void geometry(Geometry geometry) {
        System.out.println("here is the geometry: " + geometry.toString());
    }
}
