/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.crs;

import org.opengis.referencing.cs.VerticalCS;
import org.opengis.referencing.datum.VerticalDatum;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A 1D coordinate reference system used for recording heights or depths. Vertical CRSs make use
 * of the direction of gravity to define the concept of height or depth, but the relationship with
 * gravity may not be straightforward.
 * <p>
 * By implication, ellipsoidal heights (<var>h</var>) cannot be captured in a vertical coordinate
 * reference system. Ellipsoidal heights cannot exist independently, but only as inseparable part
 * of a 3D coordinate tuple defined in a geographic 3D coordinate reference system. However GeoAPI
 * does not enforce this rule. Some applications may relax this rule and accept ellipsoidal heights
 * in the following context:
 *
 * <ul>
 *   <li><p>As a transient state while parsing <A HREF="../doc-files/WKT.html">Well Known Text</A>,
 *       or any other format based on legacy specifications where ellipsoidal heights were allowed
 *       as an independant axis.</p></li>
 *
 *   <li><p>As short-lived objects to be passed or returned by methods enforcing type safety, for
 *       example {@link org.opengis.metadata.extent.VerticalExtent#getVerticalCRS}.</p></li>
 *
 *   <li><p>Other cases at implementor convenience. However implementors are encouraged to
 *       assemble the full 3D CRS as soon as they can.</p></li>
 * </ul>
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.opengis.referencing.cs.VerticalCS Vertical}
 * </TD></TR></TABLE>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="SC_VerticalCRS", specification=ISO_19111)
public interface VerticalCRS extends SingleCRS {
    /**
     * Returns the coordinate system, which must be vertical.
     */
    @UML(identifier="usesCS", obligation=MANDATORY, specification=ISO_19111)
    VerticalCS getCoordinateSystem();

    /**
     * Returns the datum, which must be vertical.
     */
    @UML(identifier="usesDatum", obligation=MANDATORY, specification=ISO_19111)
    VerticalDatum getDatum();
}
