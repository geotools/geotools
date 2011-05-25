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
package org.geotools.wfs;

import org.geotools.xml.XSDParserDelegate;

/**
 * Parser delegate for WFS.
 * 
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 *
 *
 * @source $URL$
 */
public class WFSParserDelegate extends XSDParserDelegate {

    public WFSParserDelegate() {
        super(new org.geotools.wfs.v1_1.WFSConfiguration());
    }

}
