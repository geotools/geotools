/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.datum;

import java.util.Date;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.metadata.extent.Extent;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Specifies the relationship of a coordinate system to the earth, thus creating a {@linkplain
 * org.opengis.referencing.crs.CoordinateReferenceSystem coordinate reference system}. A datum uses a
 * parameter or set of parameters that determine the location of the origin of the coordinate
 * reference system. Each datum subtype can be associated with only specific types of
 * {@linkplain org.opengis.referencing.cs.CoordinateSystem coordinate systems}.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/datum/Datum.java $
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see org.opengis.referencing.cs.CoordinateSystem
 * @see org.opengis.referencing.crs.CoordinateReferenceSystem
 */
@UML(identifier="CD_Datum", specification=ISO_19111)
public interface Datum extends IdentifiedObject {
    /**
     * Key for the <code>{@value}</code> property to be given to the
     * {@linkplain DatumFactory datum factory} <code>createFoo(&hellip;)</code> methods.
     * This is used for setting the value to be returned by {@link #getAnchorPoint}.
     *
     * @see #getAnchorPoint
     */
    String ANCHOR_POINT_KEY = "anchorPoint";

    /**
     * Key for the <code>{@value}</code> property to be given to the
     * {@linkplain DatumFactory datum factory} <code>createFoo(&hellip;)</code> methods.
     * This is used for setting the value to be returned by {@link #getRealizationEpoch}.
     *
     * @see #getRealizationEpoch
     */
    String REALIZATION_EPOCH_KEY = "realizationEpoch";

    /**
     * Key for the <code>{@value}</code> property to be given to the
     * {@linkplain DatumFactory datum factory} <code>createFoo(&hellip;)</code> methods.
     * This is used for setting the value to be returned by {@link #getDomainOfValidity}.
     *
     * @see #getDomainOfValidity
     *
     * @since GeoAPI 2.1
     */
    String DOMAIN_OF_VALIDITY_KEY = "domainOfValidity";

    /**
     * Key for the <code>{@value}</code> property to be given to the
     * {@linkplain DatumFactory datum factory} <code>createFoo(&hellip;)</code> methods.
     * This is used for setting the value to be returned by {@link #getScope}.
     *
     * @see #getScope
     */
    String SCOPE_KEY = "scope";

    /**
     * Description, possibly including coordinates, of the point or points used to anchor the datum
     * to the Earth. Also known as the "origin", especially for Engineering and Image Datums.
     * <p>
     * <ul>
     *   <li>For a geodetic datum, this point is also known as the fundamental point, which is
     *       traditionally the point where the relationship between geoid and ellipsoid is defined.
     *       In some cases, the "fundamental point" may consist of a number of points. In those
     *       cases, the parameters defining the geoid/ellipsoid relationship have then been averaged
     *       for these points, and the averages adopted as the datum definition.</li>
     *
     *   <li>For an engineering datum, the anchor point may be a physical point, or it may be a
     *       point with defined coordinates in another CRS.</li>
     *
     *   <li>For an image datum, the anchor point is usually either the centre of the image or the
     *       corner of the image.</li>
     *
     *   <li>For a temporal datum, this attribute is not defined. Instead of the anchor point,
     *       a temporal datum carries a separate time origin of type {@link Date}.</li>
     * </ul>
     *
     * @return A description of the anchor point, or {@code null} if none.
     */
    @UML(identifier="anchorPoint", obligation=OPTIONAL, specification=ISO_19111)
    InternationalString getAnchorPoint();

    /**
     * The time after which this datum definition is valid. This time may be precise (e.g. 1997
     * for IRTF97) or merely a year (e.g. 1983 for NAD83). In the latter case, the epoch usually
     * refers to the year in which a major recalculation of the geodetic control network, underlying
     * the datum, was executed or initiated. An old datum can remain valid after a new datum is
     * defined. Alternatively, a datum may be superseded by a later datum, in which case the
     * realization epoch for the new datum defines the upper limit for the validity of the
     * superseded datum.
     *
     * @return The datum realization epoch, or {@code null} if not available.
     */
    @UML(identifier="realizationEpoch", obligation=OPTIONAL, specification=ISO_19111)
    Date getRealizationEpoch();

    /**
     * Area or region or timeframe in which this datum is valid.
     *
     * @return The datum valid domain, or {@code null} if not available.
     *
     * @since GeoAPI 2.1
     */
    @UML(identifier="domainOfValidity", obligation=OPTIONAL, specification=ISO_19111)
    Extent getDomainOfValidity();

    /**
     * Description of domain of usage, or limitations of usage, for which this
     * datum object is valid.
     *
     * @return A description of domain of usage, or {@code null} if none.
     */
    @UML(identifier="scope", obligation=OPTIONAL, specification=ISO_19111)
    InternationalString getScope();
}
