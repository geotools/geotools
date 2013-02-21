/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.xml.resolver.SchemaResolver;

/**
 * Application schema resolver that maps absolute URLs to local URL resources.
 * 
 * <p>
 * 
 * Resources are sought, in order:
 * 
 * <ol>
 * 
 * <li>In an <a href="http://www.oasis-open.org/committees/entity/spec-2001-08-06.html">OASIS
 * Catalog</a> (with URI resolution semantics), which maps URLs to arbitrary filesystem locations.</li>
 * 
 * <li>On the classpath, where resources are located by their Simple HTTP Resource Path (see
 * {@link #getSimpleHttpResourcePath(URI)}).
 * 
 * <li>In a cache, with optional downloading support.
 * 
 * </ol>
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 *
 *
 *
 * @source $URL$
 */
public class AppSchemaResolver extends SchemaResolver {

    
    /**
     * Constructor.
     * 
     * @param catalog
     * @param classpath whether schemas can be located on the classpath
     * @param cache
     */
    public AppSchemaResolver(AppSchemaCatalog catalog, boolean classpath,
            AppSchemaCache cache) {
        super(catalog, classpath, cache);
    }

    /**
     * Constructor.
     * 
     * @param catalog
     * @param cache
     */
    public AppSchemaResolver(AppSchemaCatalog catalog, AppSchemaCache cache) {
        super(catalog, cache);
    }

    /**
     * Convenience constructor for a resolver with neither catalog nor cache (just classpath).
     */
    public AppSchemaResolver() {
        super();
    }

    /**
     * Convenience constructor for a resolver with no cache.
     * 
     * @param catalog
     */
    public AppSchemaResolver(AppSchemaCatalog catalog) {
        super(catalog);
    }

    /**
     * Convenience constructor for a resolver with no catalog.
     * 
     * @param cache
     */
    public AppSchemaResolver(AppSchemaCache cache) {
        super(cache);
    }    
}
