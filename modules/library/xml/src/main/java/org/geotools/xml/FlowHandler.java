/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.util.Map;

/**
 * Provides a mechanism to indicate that the XMLSAXHandler should stop 
 * parsing. It will periodically call shouldStop(), passing in its hints 
 * object. If shouldStop() returns true, if will abort parsing.
 * 
 * @author Richard Gould
 *
 *
 * @source $URL$
 */
public interface FlowHandler {
    /**
     * If this method returns true, the XMLSAXHandler will abort parsing.
     *
     * @param hints the hints that were passed into the XML parser
     * @return true if the XML parser should abort parsing
     */
    public boolean shouldStop(Map hints);
}
