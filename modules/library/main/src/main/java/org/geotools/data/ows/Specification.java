/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows;

import java.net.URL;

/**
 * Provides support for the Open Web Service Specificaitons.
 * 
 * <p>
 * This class operates as a Factory creating request for Open Web Services.
 * 
 * <p>
 * The idea is that this class operates a Toolkit for all things assocated with
 * an Open Web Service specification. The various objects produced by this
 * toolkit are used as strategy objects for the top level AbstractOpenWebService
 * subclass. Example: 
 * <ul>
 * <li>
 * WebMapServer - uses a GetCapabilitiesRequest during version negotiation.
 * </li>
 * </ul>
 * </p>
 * 
 * <p>
 * Both name and version information that may be checked against a
 * GetCapabilities document during version negotiation.
 * </p>
 * 
 * Specific Open Web Service implementations can extend this interface to
 * include details that are specific to their specification. Example:
 * The Web Map Service specification includes more operations such as GetMap
 * and GetFeatureInfo.
 * 
 * <p>
 * <b>Q:</b> Why are these not static?<br>
 * <b>A:</b> Because we want to place new specifications into a data structure
 * for WebMapServer to search through dynamically
 * </p>
 *
 * @author Jody Garnett, Refractions Reasearch
 * @author rgould
 * @source $URL$
 */
public abstract class Specification {

    /**
     * Expected version attribute for root element.
     * @return the version as a String
     */
    public abstract String getVersion();
    
    /**
     * Factory method to create GetCapabilities Request
     * @param server the URL that points to the server's getCapabilities document
     * @return a configured GetCapabilitiesRequest that can be used to access the Document 
     */
    public abstract GetCapabilitiesRequest createGetCapabilitiesRequest(
        URL server);
}
