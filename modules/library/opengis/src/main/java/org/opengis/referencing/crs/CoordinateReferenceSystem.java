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

import org.opengis.referencing.ReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Specification.*;


/**
 * Abstract coordinate reference system, usually defined by a
 * {@linkplain org.opengis.referencing.cs.CoordinateSystem coordinate system} and a
 * {@linkplain org.opengis.referencing.datum.Datum datum}. The concept of a coordinate
 * reference system (CRS) captures the choice of values for the parameters that constitute
 * the degrees of freedom of the coordinate space. The fact that such a choice has to be made,
 * either arbitrarily or by adopting values from survey measurements, leads to the large number
 * of coordinate reference systems in use around the world. It is also the cause of the little
 * understood fact that the latitude and longitude of a point are not unique. Without the full
 * specification of the coordinate reference system, coordinates are ambiguous at best and
 * meaningless at worst. However for some interchange purposes it is sufficient to confirm the
 * {@linkplain #getName identity of the system} without necessarily having the full system
 * definition.
 * <p>
 * The concept of coordinates may be expanded from a strictly spatial context to include time.
 * Time is then added as another coordinate to the coordinate tuple. It is even possible to add
 * two time-coordinates, provided the two coordinates describe different independent quantities.
 * An example of the latter is the time/space position of a subsurface point of which the vertical
 * coordinate is expressed as the two-way travel time of a sound signal in milliseconds, as is
 * common in seismic imaging. A second time-coordinate indicates the time of observation, usually
 * expressed in whole years.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/crs/CoordinateReferenceSystem.java $
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="SC_CRS", specification=ISO_19111)
public interface CoordinateReferenceSystem extends ReferenceSystem {
    /**
     * Returns a relevant coordinate system instance. Special cases:
     *
     * <ul>
     *   <li><p>If the CRS instance on which this method is invoked is an instance of the
     *       {@linkplain SingleCRS single CRS} interface, then the CS instance which is
     *       returned shall be one of the defined sub-interfaces of {@linkplain CoordinateSystem
     *       coordinate system}.</p></li>
     *
     *   <li><p>If the CRS instance on which this method is invoked is an instance of the
     *       {@linkplain CompoundCRS compound CRS} interface, then the CS instance which is
     *       returned shall have dimension and axis components obtained from different
     *       {@linkplain CompoundCRS#getCoordinateReferenceSystems components} of the instance
     *       CRS.</p></li>
     * </ul>
     *
     * @departure
     *   Strictly speaking, this method is defined by ISO 19111 for {@linkplain SingleCRS single CRS}
     *   only. GeoAPI declares this method in this parent interface for user convenience, since CS
     *   {@linkplain CoordinateSystem#getDimension dimension} and {@linkplain CoordinateSystem#getAxis axis}
     *   are commonly requested information and shall be available, directly or indirectly, in all cases
     *   (including {@linkplain CompoundCRS compound CRS}).
     *
     * @return The coordinate system.
     */
    @Extension
    CoordinateSystem getCoordinateSystem();
}
