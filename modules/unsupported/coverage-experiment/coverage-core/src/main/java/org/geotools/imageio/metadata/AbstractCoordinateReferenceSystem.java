/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.metadata;

/**
 * An abstract {@code coordinateReferenceSystem} node. It will derived by
 * {@code CoordinateReferenceSystem}, {@code VerticalCRS} and {@code TemporalCRS} nodes in the
 * metadata tree.
 * <p>
 * Coordinate reference system consisting of one Coordinate System and one Datum
 * (as opposed to a Compound CoordinateReferenceSystem). Note: In ISO 19111:2003.
 * </p>
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 */
public abstract class AbstractCoordinateReferenceSystem extends
        IdentifiableMetadataAccessor {
    /**
     * The {@code "CoordinateReferenceSystem/Datum"} node.
     */
    protected final ChildChoice<IdentifiableMetadataAccessor> datum;

    /**
     * The {@code "CoordinateReferenceSystem/CoordinateSystem"} node.
     */
    protected final CoordinateSystem cs;

    protected AbstractCoordinateReferenceSystem(SpatioTemporalMetadata metadata,
            final String parentPath) {
        super(metadata, parentPath, null);
        datum = new Datum(this, parentPath);
        cs = new CoordinateSystem(metadata, parentPath);
    }
    
    /**
     * This constructor should be invoked only by BaseCRS to define ProjectedCRS/DerivedCRS
     */
    protected AbstractCoordinateReferenceSystem(final SpatioTemporalMetadata metadata, final String parentPath, final String childPath){
        super(metadata, parentPath, childPath);
        datum = new Datum(this, parentPath);
        cs = null;
    }

    /**
     * Sets the CoordinateReferenceSystem/Datum.
     * 
     * @param datumType
     *                {@link int} One of the available Datums (see
     *                {@link Datum Datum} for details)
     * @param identification
     *                {@link Identification} Specifies the name, aliases,
     *                identifiers and remarks for this Object.
     */
    public void setDatum(final int datumType,
            final Identification identification) {
        datum.addChild(datumType).setIdentification(identification);
    }

    /**
     * Returns the {@link Datum} datum identification.
     * 
     * @return ident {@link Identification}
     */
    public Identification getDatum() {
        return datum.getChild().getIdentification();
    }

    /**
     * Sets the {@linkplain Identification identification} of the
     * {@linkplain CoordinateSystem coordinate system}.
     * 
     * @param name
     *                The coordinate system name, or {@code null} if unknown.
     * 
     * @see CoordinateSystem
     */
    public void setCoordinateSystem(final Identification identification) {
        cs.setIdentification(identification);
    }

    /**
     * Returns the {@linkplain Identification identification} of the
     * {@linkplain CoordinateSystem coordinate system}.
     * 
     * @see CoordinateSystem
     */
    public Identification getCoordinateSystem() {
        return new Identification(cs);
    }

    /**
     * Returns the number of dimensions.
     */
    public int getDimension() {
        return cs.childCount();
    }

    /**
     * Returns the axis at the specified index.
     * 
     * @param index
     *                the axis index, ranging from 0 inclusive to
     *                {@link #getDimension} exclusive.
     * @throws IndexOutOfBoundsException
     *                 if the index is out of bounds.
     */
    public Axis getAxis(final int index) throws IndexOutOfBoundsException {
        return cs.getAxis(index);
    }

    /**
     * Adds an {@linkplain CoordinateSystemAxis axis} to the
     * {@linkplain CoordinateSystem coordinate system}.
     * 
     * @param name
     *                The axis name, or {@code null} if unknown.
     * @param direction
     *                The {@linkplain AxisDirection axis direction} (usually
     *                {@code "east"}, {@code "west"}, {@code "north"},
     *                {@code "south"}, {@code "up"} or {@code "down"}), or
     *                {@code null} if unknown.
     * @param units
     *                The axis units symbol, or {@code null} if unknown.
     * @param abbreviation
     *                The axis abbreviation, or {@code null} if unknown.
     * 
     * @see CoordinateSystemAxis
     * @see AxisDirection
     */
    public Axis addAxis(final Identification identification,
            final String direction, final String units,
            final String abbreviation) {
        final Axis axis = cs.addAxis();
        axis.setIdentification(identification);
        axis.setDirection(direction);
        axis.setUnits(units);
        axis.setAbbrev(abbreviation);
        return axis;
    }

    /**
     * A simple {@link MetadataAccessor} to access {@link Datum} information.
     * <p>
     * A datum specifies the relationship of a coordinate system to the earth
     * thus creating a coordinate reference system. A datum can be used as the
     * basis for one-, two- or three-dimensional systems. In some applications
     * for an Engineering CoordinateReferenceSystem, the relationship is to a moving platform. In
     * these applications the datum itself is not time-dependent, but any
     * transformations of the associated coordinates to a earth-fixed or other
     * coordinate reference system will contain time-dependent parameters.
     * </p>
     * <p>
     * Five subtypes of datum are specified: geodetic, vertical, engineering,
     * image and temporal. Each datum subtype can be associated only with
     * specific types of coordinate reference systems. A geodetic datum is used
     * with three-dimensional or horizontal (two-dimensional) coordinate
     * reference systems, and requires an ellipsoid definition and a prime
     * meridian definition. It is used to describe large portions of the earth�s
     * surface up to the entire earth�s surface. A vertical datum can only be
     * associated with a vertical coordinate reference system. Image datum and
     * engineering datum are both used in a local context only: to describe the
     * origin of an image and the origin of an engineering (or local) coordinate
     * reference system. A temporal datum is used to define the origin of the
     * time axis in a temporal coordinate reference system.
     * </p>
     * 
     * @author Alessio Fabiani, GeoSolutions
     */
    public static final class Datum extends
            ChildChoice<IdentifiableMetadataAccessor> {
        /** Child index related to a Datum element */
        public static final int DATUM = 0;

        /**
         * Child index related to a Engineering Datum element
         * <p>
         * An engineering datum defines the origin of an engineering coordinate
         * reference system, and is used in a region around that origin. This
         * origin can be fixed with respect to the earth (such as a defined
         * point at a construction site), or be a defined point on a moving
         * vehicle (such as on a ship or satellite).
         * </p>
         */
        public static final int ENGINEERING_DATUM = 1;

        /**
         * Child index related to a Temporal Datum element
         * <p>
         * A temporal datum defines the origin of a temporal reference system.
         * </p>
         */
        public static final int TEMPORAL_DATUM = 2;

        /**
         * Child index related to a Vertical Datum element.
         * <p>
         * A textual description and/or a set of parameters identifying a
         * particular reference level surface used as a zero-height or
         * zero-depth surface, including its position with respect to the Earth.
         * There are several types of vertical datum, and each may place
         * constraints on the coordinate axis with which it is combined to
         * create a Vertical CoordinateReferenceSystem.
         * </p>
         * <p>
         * Further sub-typing is required to describe vertical datums
         * adequately. The following types of vertical datum are distinguished:
         * <ul>
         * <li>Geoidal. The zero value of the associated (vertical) coordinate
         * system axis is defined to approximate a constant potential surface,
         * usually the geoid. Such a reference surface is usually determined by
         * a national or scientific authority and is then a well-known, named
         * datum. This is the default vertical datum type, because it is the
         * most common one encountered.</li>
         * <li>Depth. The zero point of the vertical axis is defined by a
         * surface that has meaning for the purpose for which the associated
         * vertical measurements are used. For hydrographic charts, this is
         * often a predicted nominal sea surface (that is, without waves or
         * other wind and current effects) which occurs at low tide. Examples
         * are Lowest Astronomical Tide (LAT) and Lowest Low Water Springs
         * (LLWS). A different example is a sloping and undulating River Datum
         * defined as the nominal river water surface occurring at a quantified
         * river discharge.</li>
         * <li>Barometric. A vertical datum is of type �barometric� if
         * atmospheric pressure is the basis for the definition of the origin.
         * Atmospheric pressure may be used as the intermediary to determine
         * height (barometric height determination) or it may be used directly
         * as the vertical coordinate, against which other parameters are
         * measured. The latter case is applied routinely in meteorology.</li>
         * <li>Other surface. In some cases, for example oil exploration and
         * production, geological features, such as the top or bottom of a
         * geologically identifiable and meaningful subsurface layer, are
         * sometimes used as a vertical datum. Other variations to the above
         * three vertical datum types may exist and are all bracketed in this
         * category.
         * </p>
         */
        public static final int VERTICAL_DATUM = 3;

        /**
         * Child index related to a Image Datum element.
         * <p>
         * An image datum defines the origin of an image coordinate reference
         * system, and is used in a local context only. For an image datum, the
         * anchor point is usually either the centre of the image or the corner
         * of the image.
         * </p>
         * <p>
         * The image pixel grid is defined as the set of lines of constant
         * integer coordinate values. The term �image grid� is often used in
         * other standards to describe the concept of Image CoordinateReferenceSystem. However, care
         * has to be taken to correctly interpret this term in the context in
         * which it is used. The term �grid cell� is often used as a substitute
         * for the term �pixel�. The grid lines of the image may be associated
         * in two ways with the data attributes of the pixel or grid cell (ISO
         * 19123). The data attributes of the image usually represent an average
         * or integrated value that is associated with the entire pixel.
         * </p>
         * <p>
         * An image grid can be associated with this data in such a way that the
         * grid lines run through the centres of the pixels. The cell centres
         * will thus have integer coordinate values. In that case the attribute
         * �pixel in cell� will have the value �cell centre�.
         * </p>
         * <p>
         * Alternatively the image grid may be defined such that the grid lines
         * associate with the cell or pixel corners rather than the cell
         * centres. The cell centres will thus have non-integer coordinate
         * values, the fractional parts always being 0.5. The attribute �pixel
         * in cell� will now have the value �cell corner�.
         * </p>
         * <p>
         * This difference in perspective has no effect on the image
         * interpretation, but is important for coordinate transformations
         * involving this defined image.
         * </p>
         */
        public static final int IMAGE_DATUM = 4;

        /**
         * Child index related to a Geodetic Datum element.
         * 
         * <p>
         * A geodetic datum defines the location and precise orientation in
         * 3-dimensional space of a defined ellipsoid (or sphere) that
         * approximates the shape of the earth, or of a Cartesian coordinate
         * system centered in this ellipsoid (or sphere).
         * </p>
         * <ul>
         * <li>A prime meridian defines the origin from which longitude values
         * are specified. The prime meridian description is mandatory if the
         * datum type is geodetic. Most geodetic datums use Greenwich as their
         * prime meridian. Default values for the attributes prime meridian name
         * and greenwichLongitude are "Greenwich" and 0 respectively. If the
         * prime meridian name is "Greenwich" then the value of Greenwich
         * longitude shall be 0 degrees. A prime meridian description is not
         * used for any datum type other than geodetic.</li>
         * <li>An ellipsoid is defined that approximates the surface of the
         * geoid. Because of the area for which the approximation is valid �
         * traditionally regionally, but with the advent of satellite
         * positioning often globally � the ellipsoid is typically associated
         * with Geographic and Projected CRSs. An ellipsoid specification shall
         * not be provided if the datum type is not geodetic. One ellipsoid
         * shall be specified with every geodetic datum, even if the ellipsoid
         * is not used computationally. The latter may be the case when a
         * Geocentric CoordinateReferenceSystem is used, for example in the calculation of satellite
         * orbit and ground positions from satellite observations. Although use
         * of a Geocentric CoordinateReferenceSystem apparently obviates the need of an ellipsoid, the
         * ellipsoid usually played a role in the determination of the
         * associated geodetic datum. Furthermore one or more Geographic CRSs
         * may be based on the same geodetic datum, which requires the correct
         * ellipsoid to be associated with that datum. An ellipsoid is defined
         * either by its semi-major axis and inverse flattening, or by its
         * semi-major axis and semi-minor axis. For some applications, for
         * example small scale mapping in atlases, a spherical approximation of
         * the geoid�s surface is used, requiring only the radius of the sphere
         * to be specified.</li>
         * </ul>
         */
        public static final int GEODETIC_DATUM = 5;

        public Datum(final MetadataAccessor parent, final String parentCRS) {
            super(parent, parentCRS, new String[] {
                    SpatioTemporalMetadataFormat.MD_DATUM,
                    SpatioTemporalMetadataFormat.MD_DTM_ENGINEERING,
                    SpatioTemporalMetadataFormat.MD_DTM_TEMPORAL,
                    SpatioTemporalMetadataFormat.MD_DTM_VERTICAL,
                    SpatioTemporalMetadataFormat.MD_DTM_IMAGE,
                    SpatioTemporalMetadataFormat.MD_DTM_GEODETIC });
        }

        @Override
        protected IdentifiableMetadataAccessor newChild(String choice) {
            return new IdentifiableMetadataAccessor(this, choice, null);
        }

    }
}
