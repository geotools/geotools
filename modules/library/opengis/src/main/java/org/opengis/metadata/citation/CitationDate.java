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

import java.util.Date;
import org.opengis.annotation.UML;
import org.opengis.annotation.Profile;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.ComplianceLevel.*;


/**
 * Reference date and event used to describe it.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/citation/CitationDate.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@Profile (level=CORE)
@UML(identifier="CI_Date", specification=ISO_19115)
public interface CitationDate {
    /**
     * Reference date for the cited resource.
     *
     * @return Reference date for the cited resource.
     */
    @Profile (level=CORE)
    @UML(identifier="date", obligation=MANDATORY, specification=ISO_19115)
    Date getDate();

    /**
     * Event used for reference date.
     *
     * @return Event used for reference date.
     */
    @Profile (level=CORE)
    @UML(identifier="dateType", obligation=MANDATORY, specification=ISO_19115)
    DateType getDateType();
}
