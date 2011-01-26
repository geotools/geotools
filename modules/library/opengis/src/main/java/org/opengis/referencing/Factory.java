/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing;

import org.opengis.metadata.citation.Citation;
import org.opengis.annotation.Extension;


/**
 * Base interface for all factories. Factories can be grouped in two categories:
 * <p>
 * <UL>
 *   <LI>{@linkplain AuthorityFactory Authority factories} creates objects from
 *       a compact string defined by an authority.</LI>
 *   <LI>{@linkplain ObjectFactory Object factories} allows applications
 *       to make objects that cannot be created by an authority factory.
 *       This factory is very flexible, whereas the authority factory is
 *       easier to use.</LI>
 * </UL>
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@Extension
public interface Factory {
    /**
     * Returns the vendor responsible for creating this factory implementation. Many implementations
     * may be available for the same factory interface. Implementations are usually managed by a
     * {@linkplain javax.imageio.spi.ServiceRegistry service registry}.
     *
     * @return The vendor for this factory implementation.
     */
    @Extension
    Citation getVendor();
}
