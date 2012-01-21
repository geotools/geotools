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
package org.geotools.styling;

import java.net.URL;

/**
 * Provides a hook to locate online resources in {@link ExternalGraphic} instances. To be used by parser implementors.
 *
 * @source $URL: http://svn.osgeo.org/geotools/tags/2.7.4/modules/library/api/src/main/java/org/geotools/styling/ResourceLocator.java $
 */
public interface ResourceLocator {
	
	/**
	 * Locate the specified resource.
	 * 
	 * @param uri uri of the resource
	 * @return the fully resolved URL of the resource or null, if the resource cannot be located.
	 */
	URL locateResource(String uri);
}
