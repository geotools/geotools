/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.distribution;

import java.util.Date;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Common ways in which the resource may be obtained or received, and related instructions
 * and fee information.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/distribution/StandardOrderProcess.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_StandardOrderProcess", specification=ISO_19115)
public interface StandardOrderProcess {
    /**
     * Fees and terms for retrieving the resource.
     * Include monetary units (as specified in ISO 4217).
     *
     * @return Fees and terms for retrieving the resource, or {@code null}.
     */
    @UML(identifier="fees", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getFees();

    /**
     * Date and time when the dataset will be available.
     *
     * @return Date and time when the dataset will be available, or {@code null}.
     */
    @UML(identifier="plannedAvailableDateTime", obligation=OPTIONAL, specification=ISO_19115)
    Date getPlannedAvailableDateTime();

    /**
     * General instructions, terms and services provided by the distributor.
     *
     * @return General instructions, terms and services provided by the distributor, or {@code null}.
     */
    @UML(identifier="orderingInstructions", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getOrderingInstructions();

    /**
     * Typical turnaround time for the filling of an order.
     *
     * @return Typical turnaround time for the filling of an order, or {@code null}.
     */
    @UML(identifier="turnaround", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getTurnaround();
}
