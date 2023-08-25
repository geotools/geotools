/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.util;

import java.util.Collections;
import java.util.List;

/**
 * Identifier within a {@linkplain NameSpace name space} for a local object. Local names are names
 * which are directly accessible to and maintained by a {@linkplain NameSpace name space}. Names are
 * local to one and only one name space. The name space within which they are local is indicated by
 * the {@linkplain #scope scope}.
 *
 * @author Martin Desruisseaux (IRD)
 * @author Bryce Nordgren (USDA)
 * @since GeoAPI 2.0
 * @see NameFactory#createLocalName
 */
public interface LocalName extends GenericName {
    /** Returns the depth, which is always 1 for a local name. */
    @Override
    int depth();

    /**
     * Returns the sequence of local name. Since this object is itself a locale name, this method
     * always returns a {@linkplain Collections#singleton singleton} containing only {@code this}.
     */
    @Override
    List<? extends LocalName> getParsedNames();

    /**
     * Returns {@code this} since this object is already a local name.
     *
     * @since GeoAPI 2.2
     */
    @Override
    /// @Override
    LocalName head();

    /**
     * Returns {@code this} since this object is already a local name.
     *
     * @since GeoAPI 2.1
     */
    @Override
    /// @Override

    LocalName tip();

    /** Returns a locale-independant string representation of this local name. */
    @Override
    /// @Override

    String toString();
}
