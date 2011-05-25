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

/**
 * LEVEL3 saxGML4j GML handler: Gets JTS objects.
 * 
 * <p>
 * This handler must be implemented by the parent of a GMLFilterGeometry filter
 * in order to handle the JTS objects passed to it from the child.
 * </p>
 *
 * @author Rob Hranac, Vision for New York
 *
 * @source $URL$
 * @version $Id$
 */
public interface GMLHandlerJTS extends org.xml.sax.ContentHandler {
    /**
     * Receives OGC simple feature type geometry from parent.
     *
     * @param geometry the simple feature geometry
     */
    void geometry(com.vividsolutions.jts.geom.Geometry geometry);
}
