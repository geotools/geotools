/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.identification;

import org.opengis.annotation.UML;
import org.opengis.annotation.Profile;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.ComplianceLevel.*;


/**
 * Level of detail expressed as a scale factor or a ground distance.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/identification/Resolution.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 2.0
 */
@Profile (level=CORE)
@UML(identifier="MD_Resolution", specification=ISO_19115)
public interface Resolution {
    /**
     * Level of detail expressed as the scale of a comparable hardcopy map or chart.
     * Only one of {@linkplain #getEquivalentScale equivalent scale} and
     * {@linkplain #getDistance ground sample distance} may be provided.
     *
     * @return Level of detail expressed as the scale of a comparable hardcopy, or {@code null}.
     */
    @Profile (level=CORE)
    @UML(identifier="equivalentScale", obligation=CONDITIONAL, specification=ISO_19115)
    RepresentativeFraction getEquivalentScale();

    /**
     * Ground sample distance.
     * Only one of {@linkplain #getEquivalentScale equivalent scale} and
     * {@linkplain #getDistance ground sample distance} may be provided.
     *
     * @return The ground sample distance, or {@code null}.
     *
     * @todo change return type to ISO 19103 {@code Distance} or to JScience {@code Measure}.
     * @unitof Distance
     */
    @Profile (level=CORE)
    @UML(identifier="distance", obligation=CONDITIONAL, specification=ISO_19115)
    Double getDistance();
}
