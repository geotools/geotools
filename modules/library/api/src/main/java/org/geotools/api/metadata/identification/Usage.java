/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.identification;

import java.util.Collection;
import java.util.Date;
import org.geotools.api.metadata.citation.ResponsibleParty;
import org.geotools.api.util.InternationalString;

/**
 * Brief description of ways in which the resource(s) is/are currently used.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface Usage {
    /**
     * Brief description of the resource and/or resource series usage.
     *
     * @return Description of the resource usage.
     */
    InternationalString getSpecificUsage();

    /**
     * Date and time of the first use or range of uses of the resource and/or resource series.
     *
     * @return Date of the first use of the resource, or {@code null}.
     */
    Date getUsageDate();

    /**
     * Applications, determined by the user for which the resource and/or resource series is not
     * suitable.
     *
     * @return Applications for which the resource and/or resource series is not suitable, or {@code
     *     null}.
     */
    InternationalString getUserDeterminedLimitations();

    /**
     * Identification of and means of communicating with person(s) and organization(s) using the
     * resource(s).
     *
     * @return Means of communicating with person(s) and organization(s) using the resource(s).
     */
    Collection<? extends ResponsibleParty> getUserContactInfo();
}
