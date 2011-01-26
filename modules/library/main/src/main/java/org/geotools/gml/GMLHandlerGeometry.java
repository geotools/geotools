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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


/**
 * LEVEL2 saxGML4j GML handler: Gets basic alerts from GMLFilterDocument.
 * 
 * <p>
 * This handler is required for any parent of a GMLFilterDocument filter. It
 * receives basic element notifications and coordinates.
 * </p>
 *
 * @author Rob Hranac, Vision for New York
 * @source $URL$
 * @version $Id$
 */
public interface GMLHandlerGeometry extends ContentHandler {
    /**
     * Receives a geometry start element from the parent.
     */
    abstract void geometryStart(String localName, Attributes atts)
        throws SAXException;

    /**
     * Receives a geometry end element from the parent.
     */
    abstract void geometryEnd(String localName) throws SAXException;

    /**
     * Receives a geometry sub element from the parent.
     */
    abstract void geometrySub(String localName) throws SAXException;

    /**
     * Receives a finished coordinate from the parent (2-valued).
     */
    abstract void gmlCoordinates(double x, double y) throws SAXException;

    /**
     * Receives a finished coordinate from the parent (3-valued).
     */
    abstract void gmlCoordinates(double x, double y, double z)
        throws SAXException;
}
