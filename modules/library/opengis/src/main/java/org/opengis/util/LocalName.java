/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.util;

import java.util.List;
import java.util.Collections;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Identifier within a {@linkplain NameSpace name space} for a local object. Local names are names
 * which are directly accessible to and maintained by a {@linkplain NameSpace name space}. Names are
 * local to one and only one name space. The name space within which they are local is indicated by
 * the {@linkplain #scope scope}.
 *
 * @author Martin Desruisseaux (IRD)
 * @author Bryce Nordgren (USDA)
 * @since GeoAPI 2.0
 *
 * @see NameFactory#createLocalName
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/util/LocalName.java $
 */
@UML(identifier="LocalName", specification=ISO_19103)
public interface LocalName extends GenericName {
    /**
     * Returns the depth, which is always 1 for a local name.
     */
    int depth();

    /**
     * Returns the sequence of local name. Since this object is itself a locale name,
     * this method always returns a {@linkplain Collections#singleton singleton}
     * containing only {@code this}.
     */
    @UML(identifier="parsedName", obligation=MANDATORY, specification=ISO_19103)
    List<? extends LocalName> getParsedNames();

    /**
     * Returns {@code this} since this object is already a local name.
     *
     * @since GeoAPI 2.2
     */
/// @Override
    LocalName head();

    /**
     * Returns {@code this} since this object is already a local name.
     *
     * @since GeoAPI 2.1
     */
/// @Override
    @Extension
    LocalName tip();

    /**
     * Returns a locale-independant string representation of this local name.
     */
/// @Override
    @UML(identifier="aName", obligation=MANDATORY, specification=ISO_19103)
    String toString();
}
