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

package org.geotools.xml.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;

/**
 * Support for XML schema resolution in an <a
 * href="http://www.oasis-open.org/committees/entity/spec-2001-08-06.html">OASIS Catalog</a> (with URI resolution
 * semantics).
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @see
 */
public class SchemaCatalog {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(SchemaCatalog.class);

    private final Catalog catalog;

    /** Use {@link #build(URL)} to construct an instance. */
    private SchemaCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Return schema location resolved in the catalog if possible. <tt>rewriteURI</tt> semantics are used.
     *
     * @param location typically an absolute http/https URL.
     * @return null if location not found in the catalog
     */
    @SuppressWarnings("PMD.UnusedLocalVariable")
    public String resolveLocation(String location) {
        String resolvedLocation = null;
        try {
            resolvedLocation = catalog.resolveURI(location);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (resolvedLocation != null) {
            try (InputStream input = new URL(resolvedLocation).openStream()) {
                // verify existence of resource
                // could be a file, jar resource, or other
                // catalog hit
                LOGGER.fine("Catalog resolved " + location + " to " + resolvedLocation);
            } catch (IOException e) {
                // catalog miss
                LOGGER.fine("Catalog did not resolve "
                        + location
                        + " to "
                        + resolvedLocation
                        + " despite matching catalog entry because an error occurred: "
                        + e.getMessage());
                resolvedLocation = null;
            }
        }
        return resolvedLocation;
    }

    /**
     * Build a private {@link Catalog}, that is, not the static instance that {@link CatalogManager} returns by default.
     *
     * <p>Care must be taken to use only private {@link Catalog} instances if there will ever be more than one OASIS
     * Catalog used in a single class loader (i.e. a single maven test run), otherwise {@link Catalog} contents will be
     * an amalgam of the entries of both OASIS Catalog files, with likely unintended or incorrect results. See
     * GEOT-2497.
     *
     * @param catalogLocation URL of OASIS Catalog
     * @return a private Catalog
     */
    static Catalog buildPrivateCatalog(URL catalogLocation) {
        CatalogManager catalogManager = new CatalogManager();
        catalogManager.setUseStaticCatalog(false);
        catalogManager.setVerbosity(0);
        catalogManager.setIgnoreMissingProperties(true);
        Catalog catalog = catalogManager.getCatalog();
        try {
            catalog.parseCatalog(catalogLocation);
        } catch (IOException e) {
            throw new RuntimeException("Error trying to load OASIS catalog from URL " + catalogLocation.toString(), e);
        }
        return catalog;
    }

    /**
     * Build an catalog using the given OASIS Catalog file URL.
     *
     * @param catalogLocation local file URL to an OASIS cCtalog
     */
    public static SchemaCatalog build(URL catalogLocation) {
        return new SchemaCatalog(buildPrivateCatalog(catalogLocation));
    }
}
