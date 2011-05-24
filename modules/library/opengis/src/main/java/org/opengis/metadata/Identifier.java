/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata;

import org.opengis.metadata.citation.Citation;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Value uniquely identifying an object within a namespace.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/Identifier.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_Identifier", specification=ISO_19115)
public interface Identifier {
    /**
     * Key for the <code>{@value}</code> property to be given to the
     * {@linkplain org.opengis.referencing.ObjectFactory CRS factory} <code>createFoo(&hellip;)</code>
     * methods. This is used for setting the value to be returned by {@link #getCode}.
     *
     * @see #getCode
     */
    String CODE_KEY = "code";

    /**
     * Key for the <code>{@value}</code> property to be given to the
     * {@linkplain org.opengis.referencing.ObjectFactory CRS factory} <code>createFoo(&hellip;)</code>
     * methods. This is used for setting the value to be returned by {@link #getAuthority}.
     *
     * @see #getAuthority
     */
    String AUTHORITY_KEY = "authority";

    /**
     * Alphanumeric value identifying an instance in the namespace.
     *
     * @return Value identifying an instance in the namespace.
     */
    @UML(identifier="code", obligation=MANDATORY, specification=ISO_19115)
    String getCode();

    /**
     * Organization or party responsible for definition and maintenance of the
     * {@linkplain #getCode code}.
     *
     * @return Party responsible for definition and maintenance of the code.
     */
    @UML(identifier="authority", obligation=OPTIONAL, specification=ISO_19115)
    Citation getAuthority();
}
