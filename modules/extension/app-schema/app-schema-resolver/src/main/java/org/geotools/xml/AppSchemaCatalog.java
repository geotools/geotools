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

import java.net.URL;

import org.apache.xml.resolver.Catalog;
import org.geotools.xml.resolver.SchemaCatalog;

/**
 * Support for application schema resolution in an <a
 * href="http://www.oasis-open.org/committees/entity/spec-2001-08-06.html">OASIS Catalog</a> (with
 * URI resolution semantics).
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @see
 *
 *
 *
 * @source $URL$
 */
public class AppSchemaCatalog extends SchemaCatalog {
    
    /**
     * Use {@link #build(URL)} to construct an instance.
     * 
     * @param catalog
     */
    protected AppSchemaCatalog(Catalog catalog) {
        super(catalog);
    }    

    /**
     * Build an catalog using the given OASIS Catalog file URL.
     * 
     * @param catalogLocation
     *            local file URL to an OASIS cCtalog
     * @return
     */
    public static AppSchemaCatalog build(URL catalogLocation) {
        return new AppSchemaCatalog(buildPrivateCatalog(catalogLocation));
    }

}
