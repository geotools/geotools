/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

/**
 * {@linkplain org.opengis.referencing.datum.Datum Geodetic datum} (the relationship of a
 * {@linkplain org.opengis.referencing.cs.CoordinateSystem coordinate system} to the earth).
 * The following is adapted from
 * <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">OpenGIS&reg;
 * Spatial Referencing by Coordinates (Topic 2)</A> specification.
 *
 * <P ALIGN="justify">A datum specifies the relationship of a coordinate system
 * to the earth or, in some applications to an Engineering CRS, to a moving
 * platform, thus creating a coordinate reference system. A datum can be used
 * as the basis for one-, two- or three-dimensional systems.</P>
 *
 * <P ALIGN="justify">Five subtypes of datum are specified: geodetic, vertical,
 * engineering, image and temporal. Each datum subtype can be associated only
 * with specific types of coordinate reference systems. A geodetic datum is used
 * with three-dimensional or horizontal (two-dimensional) coordinate reference
 * systems, and requires an ellipsoid definition and a prime meridian definition.
 * It is used to describe large portions of the earth's surface up to the entire
 * earth's surface. A vertical datum can only be associated with a vertical
 * coordinate reference system. Image datum and engineering datum are both used
 * in a local context only: to describe the origin of an image and the origin of
 * an engineering (or local) coordinate reference system.</P>
 *
 * <P>&nbsp;</P>
 * <H3>Vertical datum</H3>
 * <P ALIGN="justify">Further sub-typing is required to describe vertical datums
 * adequately. The following types of vertical datum are distinguished:</P>
 * <UL>
 *   <LI><P ALIGN="justify"><STRONG>Geoidal</STRONG><BR>
 *   The zero value of the associated (vertical) coordinate system axis is defined
 *   to approximate a constant potential surface, usually the geoid. Such a reference
 *   surface is usually determined by a national or scientific authority and is then
 *   a wellknown, named datum. This is the default vertical datum type, because it is
 *   the most common one encountered.</P></LI>
 *
 *   <LI><P ALIGN="justify"><STRONG>Depth</STRONG><BR>
 *   The zero point of the vertical axis is defined by a surface that has meaning for
 *   the purpose the associated vertical measurements are used for. For hydrographic
 *   charts, this is often a predicted nominal sea surface (i.e., without waves or
 *   other wind and current effects) that occurs at low tide. Examples are Lowest
 *   Astronomical Tide and Lowest Low Water Spring. A different example is a sloping
 *   and undulating River Datum defined as the nominal river water surface occurring
 *   at a quantified river discharge.</P></LI>
 *
 *   <LI><P ALIGN="justify"><STRONG>Barometric</STRONG><BR>
 *   A vertical datum is of type "barometric" if atmospheric pressure is the basis
 *   for the definition of the origin. Atmospheric pressure may be used as the
 *   intermediary to determine height (barometric height determination) or it may
 *   be used directly as the vertical ordinate, against which other parameters are
 *   measured. The latter case is applied routinely in meteorology.</P>
 *
 *   <P ALIGN="justify">Barometric height determination is routinely used in aircraft.
 *   The altimeter (barometer) on board is set to the altitude of the airfield at the
 *   time of take-off, which corrects simultaneously for instantaneous air pressure and
 *   altitude of the airfield. The measured height value is commonly named "altitude".</P>
 *
 *   <P ALIGN="justify">In some land surveying applications height differences between
 *   points are measured with barometers. To obtain absolute heights the measured height
 *   differences are added to the known heights of control points. In that case the vertical
 *   datum type is not barometric, but is the same as that of the vertical control network
 *   used to obtain the heights of the new points and its vertical datum type.</P>
 *
 *   <P ALIGN="justify">The accuracy of this technique is limited, as it is affected
 *   strongly by the spatial and temporal variability of atmospheric pressure. This
 *   accuracy limitation impacts the precision of the associated vertical datum definition.
 *   The datum is usually the surface of constant atmospheric pressure approximately
 *   equating to mean sea level (MSL). The origin or anchor point is usually a point
 *   of known MSL height. The instruments are calibrated at this point by correcting
 *   for the instantaneous atmospheric pressure at sea level and the height of the
 *   point above MSL.</P>
 *
 *   <P ALIGN="justify">In meteorology, atmospheric pressure routinely takes the role
 *   as vertical ordinate in a CRS that is used as a spatial reference frame for
 *   meteorological parameters in the upper atmosphere. The origin of the datum
 *   is in that case the (hypothetical) zero atmospheric pressure and the positive
 *   vertical axis points down (to increasing pressure).</P></LI>
 *
 *   <LI><P ALIGN="justify"><STRONG>Other surface</STRONG><BR>
 *   In some cases, e.g. oil exploration and production, geological features,
 *   i.e., the top or bottom of a geologically identifiable and meaningful subsurface
 *   layer, are sometimes used as a vertical datum. Other variations to the above three
 *   vertical datum types may exist and are all bracketed in this category.</P></LI>
 * </UL>
 *
 * <P>&nbsp;</P>
 * <H3>Image datum</H3>
 * <P ALIGN="justify">The image pixel grid is defined as the set of lines of constant
 * integer ordinate values. The term "image grid" is often used in other standards to
 * describe the concept of Image CRS. However, care must be taken to correctly interpret
 * this term in the context in which it is used. The term "grid cell" is often used as a
 * substitute for the term "pixel".</P>
 *
 * <P ALIGN="justify">The grid lines of the image may be associated in two ways with
 * the data attributes of the pixel or grid cell (ISO CD 19123). The data attributes
 * of the image usually represent an average or integrated value that is associated
 * with the entire pixel.</P>
 *
 * <P ALIGN="justify">An image grid can be associated with this data in such a way
 * that the grid lines run through the centres of the pixels. The cell centres will
 * thus have integer coordinate values. In that case the attribute "pixel in cell"
 * will have the value "cell centre".</P>
 *
 * <P ALIGN="justify">Alternatively the image grid may be defined such that the
 * grid lines associate with the cell or pixel corners rather than the cell centres.
 * The cell centres will thus have noninteger coordinate values, the fractional parts
 * always being 0.5. ISO CD 19123 calls the grid points in this latter case "posts"
 * and associated image data: "matrix data". The attribute "pixel in cell" will now
 * have the value "cell corner".</P>
 *
 * <P ALIGN="justify">This difference in perspective has no effect on the image
 * interpretation, but is important for coordinate transformations involving this
 * defined image.</P>
 *
 * <P>&nbsp;</P>
 * <H3>Prime meridian</H3>
 * <P ALIGN="justify">A prime meridian defines the origin from which longitude values
 * are specified. Most geodetic datums use Greenwich as their prime meridian. A prime
 * meridian description is not needed for any datum type other than geodetic, or if the
 * datum type is geodetic and the prime meridian is Greenwich. The prime meridian
 * description is mandatory if the datum type is geodetic and its prime meridian
 * is not Greenwich.</P>
 *
 * <P>&nbsp;</P>
 * <H3>Ellipsoid</H3>
 * <P ALIGN="justify">An ellipsoid is defined that approximates the surface of the
 * geoid. Because of the area for which the approximation is valid - traditionally
 * regionally, but with the advent of satellite positioning often globally - the
 * ellipsoid is typically associated with Geographic and Projected CRSs. An ellipsoid
 * specification shall not be provided if the datum type not geodetic.</P>
 *
 * <P ALIGN="justify">One ellipsoid must be specified with every geodetic datum,
 * even if the ellipsoid is not used computationally. The latter may be the case
 * when a Geocentric CRS is used, e.g., in the calculation of satellite orbit and
 * ground positions from satellite observations. Although use of a Geocentric CRS
 * apparently obviates the need of an ellipsoid, the ellipsoid usually played a role
 * in the determination of the associated geodetic datum. Furthermore one or more
 * Geographic CRSs may be based on the same geodetic datum, which requires the correct
 * ellipsoid the associated with any given geodetic datum.</P>
 *
 * <P ALIGN="justify">An ellipsoid is defined either by its semi-major axis and
 * inverse flattening, or by its semimajor axis and semi-minor axis. For some
 * applications, for example small-scale mapping in atlases, a spherical approximation
 * of the geoid's surface is used, requiring only the radius of the sphere to be
 * specified.</P>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @since   GeoAPI 1.0
 */
package org.opengis.referencing.datum;
