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

/**
 * Represents a GetCapabilitiesRequest.
 * 
 * The SECTION_* constants represent different sections of the Capabilities
 * document. Specific sections can be retrieved using these parameters. This is
 * useful because some Capabilities documents can be very large.
 *  
 * @author Richard Gould
 *
 * @source $URL$
 */
public interface GetCapabilitiesRequest extends Request {
	public static String GET_CAPABILITIES = "GetCapabilities";
    public static String SECTION_ALL = "/";
    public static String SECTION_SERVICE = "/OGC_CAPABILITIES/ServiceMetadata";
    public static String SECTION_OPERATIONS = "/OGC_CAPABILITIES/OperationSignatures";
    public static String SECTION_CONTENT = "/OGC_CAPABILITIES/ContentMetadata";
    public static String SECTION_COMMON = "/OGC_CAPABILITIES/Common";
}
