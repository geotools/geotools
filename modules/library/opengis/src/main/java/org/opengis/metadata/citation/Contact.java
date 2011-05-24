/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.citation;

import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Information required to enable contact with the responsible person and/or organization.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/citation/Contact.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="CI_Contact", specification=ISO_19115)
public interface Contact {
    /**
     * Telephone numbers at which the organization or individual may be contacted.
     * Returns {@code null} if none.
     *
     * @return Telephone numbers at which the organization or individual may be contacted,
     *         or {@code null}.
     */
    @UML(identifier="phone", obligation=OPTIONAL, specification=ISO_19115)
    Telephone getPhone();

    /**
     * Physical and email address at which the organization or individual may be contacted.
     * Returns {@code null} if none.
     *
     * @return Physical and email address at which the organization or individual may be contacted,
     *         or {@code null}.
     */
    @UML(identifier="address", obligation=OPTIONAL, specification=ISO_19115)
    Address getAddress();

    /**
     * On-line information that can be used to contact the individual or organization.
     * Returns {@code null} if none.
     *
     * @return On-line information that can be used to contact the individual or organization,
     *         or {@code null}.
     */
    @UML(identifier="onLineResource", obligation=OPTIONAL, specification=ISO_19115)
    OnLineResource getOnLineResource();

    /**
     * Time period (including time zone) when individuals can contact the organization or
     * individual. Returns {@code null} if unspecified.
     *
     * @return Time period when individuals can contact the organization or individual,
     *         or {@code null}.
     */
    @UML(identifier="hoursOfService", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getHoursOfService();

    /**
     * Supplemental instructions on how or when to contact the individual or organization.
     * Returns {@code null} if none.
     *
     * @return Supplemental instructions on how or when to contact the individual or organization,
     *         or {@code null}.
     */
    @UML(identifier="contactInstructions", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getContactInstructions();
}
