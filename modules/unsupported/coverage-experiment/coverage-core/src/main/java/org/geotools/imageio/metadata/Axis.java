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
 * An {@code <Axis>} element in {@linkplain ImageMetadata metadata format}.
 * <p>
 * A two- or three-dimensional coordinate system that consists of any
 * combination of coordinate axes not covered by any other Coordinate System
 * type. An example is a multilinear coordinate system which contains one
 * coordinate axis that may have any 1-D shape which has no intersections with
 * itself. This non-straight axis is supplemented by one or two straight axes to
 * complete a 2 or 3 dimensional coordinate system. The non-straight axis is
 * typically incrementally straight or curved. A UserDefinedCS shall have two or
 * three usesAxis associations; the number of associations shall equal the
 * dimension of the CS.
 * </p>
 *
 * @author Martin Desruisseaux 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 */
public class Axis extends IdentifiableMetadataAccessor {

    /**
     * Creates a parser for an {@link Axis axis}. This constructor should not
     * be invoked directly; use {@link ImageReferencing#getAxis} instead.
     * 
     * @param parent
     *                The set of all axis.
     * @param index
     *                The axis index for this instance.
     */
    Axis(final ChildList<Axis> parent, final int index) {
        super(parent);
        selectChild(index);
    }

    /**
     * Returns the direction for this {@link Axis} axis, or {@code null} if
     * none.
     */
    public String getDirection() {
        return getString(SpatioTemporalMetadataFormat.MD_AX_DIRECTION);
    }

    /**
     * Sets the direction for this {@link Axis} axis. Direction of this
     * coordinate system axis (or in the case of Cartesian projected
     * coordinates, the direction of this coordinate system axis locally).
     * Examples: north or south, east or west, up or down. Within any set of
     * coordinate system axes, only one of each pair of terms can be used. For
     * earth-fixed CRSs, this direction is often approximate and intended to
     * provide a human interpretable meaning to the axis. When a geodetic datum
     * is used, the precise directions of the axes may therefore vary slightly
     * from this approximate direction. Note that an EngineeringCRS often
     * requires specific descriptions of the directions of its coordinate system
     * axes.
     * 
     * @param direction
     *                The axis direction, or {@code null} if none.
     */
    public void setDirection(final String direction) {
        setEnum(SpatioTemporalMetadataFormat.MD_AX_DIRECTION, direction,
                SpatioTemporalMetadataFormat.DIRECTIONS);
    }

    /**
     * Returns the units for this {@link Axis} axis, or {@code null} if none.
     * Identifier of the unit of measure used for this coordinate system axis.
     * The value of a coordinate in a coordinate tuple shall be recorded using
     * this unit of measure.
     * 
     * @return UoM {@link String}
     */
    public String getUnits() {
        return getString(SpatioTemporalMetadataFormat.MD_AX_UOM);
    }

    /**
     * Sets the units for this {@link Axis} axis.
     * 
     * @param units
     *                The axis units, or {@code null} if none.
     */
    public void setUnits(final String units) {
        setString(SpatioTemporalMetadataFormat.MD_AX_UOM, units);
    }

    /**
     * Returns the abbreviation for this {@link Axis} axis, or {@code null} if
     * none.
     */
    public String getAbbrev() {
        return getString(SpatioTemporalMetadataFormat.MD_AX_ABBREVIATION);
    }

    /**
     * Sets the abbreviation for this {@link Axis} axis. The abbreviation used
     * for this coordinate system {@link Axis} axis; this abbreviation is also
     * used to identify the coordinates in the coordinate tuple. Examples are X
     * and Y.
     * 
     * @param abbreviation
     *                The axis abbreviation, or {@code null} if none.
     */
    public void setAbbrev(final String abbreviation) {
        setString(SpatioTemporalMetadataFormat.MD_AX_ABBREVIATION, abbreviation);
    }

    /**
     * Sets the minimum value allowable for the {@link Axis} axis. The minimum
     * value normally allowed for this {@link Axis} axis, in the unit of measure
     * for the {@link Axis} axis.
     * 
     * @param value
     */
    public void setMinimumValue(final String value) {
        setString(SpatioTemporalMetadataFormat.MD_AX_MIN, value);
    }

    /**
     * Sets the maximum value allowable for the {@link Axis} axis. The maximum
     * value normally allowed for this {@link Axis} axis, in the unit of measure
     * for the {@link Axis} axis.
     * 
     * @param value
     */
    public void setMaximumValue(final String value) {
        setString(SpatioTemporalMetadataFormat.MD_AX_MAX, value);
    }

    /**
     * Sets the range meaning for the {@link Axis} axis. Meaning of axis value
     * range specified by minimumValue and maximumValue. This element shall be
     * omitted when both minimumValue and maximumValue are omitted. It may be
     * included when minimumValue and/or maximumValue are included. If this
     * element is omitted when minimumValue or maximumValue are included, the
     * meaning is unspecified. The allowed values usually are:
     * <ul>
     * <li>exact: Any value between and including minimumValue and
     * maximiumValue is valid.</li>
     * <li>wraparound: The axis is continuous with values wrapping around at
     * the minimumValue and maximumValue. Values with the same meaning repeat
     * modulo the difference between maximumValue and minimumValue.</li>
     * </ul>
     * 
     * @param value
     */
    public void setRangeMeaning(final String value) {
        setString(SpatioTemporalMetadataFormat.MD_AX_RANGEMEANING, value);
    }

    /**
     * Returns the minimum value allowable for the {@link Axis} axis.
     * 
     * @return
     */
    public String getMinimumValue() {
        return getString(SpatioTemporalMetadataFormat.MD_AX_MIN);
    }

    /**
     * Returns the maximum value allowable for the {@link Axis} axis.
     * 
     * @return
     */
    public String getMaximumValue() {
        return getString(SpatioTemporalMetadataFormat.MD_AX_MAX);
    }

    /**
     * Returns the range meaning for the {@link Axis} axis.
     * 
     * @return
     */
    public String getRangeMeaning() {
        return getString(SpatioTemporalMetadataFormat.MD_AX_RANGEMEANING);
    }

}