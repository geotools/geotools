/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.annotation;

import static org.opengis.annotation.Specification.*;


/**
 * Obligation of the element or entity. The enum values declared here are an exact copy of
 * the code list elements declared in the {@link org.opengis.metadata.Obligation} code list
 * from the metadata package.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/annotation/Obligation.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_ObligationCode", specification=ISO_19115)
public enum Obligation {
    /**
     * Element is required when a specific condition is met.
     */
    ///@UML(identifier="conditional", obligation=CONDITIONAL, specification=ISO_19115)
    CONDITIONAL,

    /**
     * Element is not required.
     */
    @UML(identifier="optional", obligation=CONDITIONAL, specification=ISO_19115)
    OPTIONAL,

    /**
     * Element is always required.
     */
    @UML(identifier="mandatory", obligation=CONDITIONAL, specification=ISO_19115)
    MANDATORY,

    /**
     * The element should always be {@code null}. This obligation code is used only when
     * a subinterface overrides an association and force it to a {@code null} value.
     * An example is {@link org.opengis.referencing.datum.TemporalDatum#getAnchorPoint}.
     */
    @Extension
    FORBIDDEN
}
