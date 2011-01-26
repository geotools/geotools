/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.constraint;

import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Handling restrictions imposed on the resource for national security or similar security concerns.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_SecurityConstraints", specification=ISO_19115)
public interface SecurityConstraints extends Constraints {
    /**
     * Name of the handling restrictions on the resource.
     *
     * @return Name of the handling restrictions on the resource.
     */
    @UML(identifier="classification", obligation=MANDATORY, specification=ISO_19115)
    Classification getClassification();

    /**
     * Explanation of the application of the legal constraints or other restrictions and legal
     * prerequisites for obtaining and using the resource.
     *
     * @return Explanation of the application of the legal constraints, or {@code null}.
     */
    @UML(identifier="userNote", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getUserNote();

    /**
     * Name of the classification system.
     *
     * @return Name of the classification system, or {@code null}.
     */
    @UML(identifier="classificationSystem", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getClassificationSystem();

    /**
     * Additional information about the restrictions on handling the resource.
     *
     * @return Additional information about the restrictions, or {@code null}.
     */
    @UML(identifier="handlingDescription", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getHandlingDescription();
}
