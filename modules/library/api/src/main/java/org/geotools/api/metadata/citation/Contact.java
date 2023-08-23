/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.citation;

import org.geotools.api.util.InternationalString;

/**
 * Information required to enable contact with the responsible person and/or organization.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface Contact {
    /**
     * Telephone numbers at which the organization or individual may be contacted. Returns {@code
     * null} if none.
     *
     * @return Telephone numbers at which the organization or individual may be contacted, or {@code
     *     null}.
     */
    Telephone getPhone();

    /**
     * Physical and email address at which the organization or individual may be contacted. Returns
     * {@code null} if none.
     *
     * @return Physical and email address at which the organization or individual may be contacted,
     *     or {@code null}.
     */
    Address getAddress();

    /**
     * On-line information that can be used to contact the individual or organization. Returns
     * {@code null} if none.
     *
     * @return On-line information that can be used to contact the individual or organization, or
     *     {@code null}.
     */
    OnLineResource getOnLineResource();

    /**
     * Time period (including time zone) when individuals can contact the organization or
     * individual. Returns {@code null} if unspecified.
     *
     * @return Time period when individuals can contact the organization or individual, or {@code
     *     null}.
     */
    InternationalString getHoursOfService();

    /**
     * Supplemental instructions on how or when to contact the individual or organization. Returns
     * {@code null} if none.
     *
     * @return Supplemental instructions on how or when to contact the individual or organization,
     *     or {@code null}.
     */
    InternationalString getContactInstructions();
}
