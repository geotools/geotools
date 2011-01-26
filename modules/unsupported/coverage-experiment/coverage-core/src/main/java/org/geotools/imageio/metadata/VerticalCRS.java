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
 * A {@code VerticalCRS} node in the metadata tree.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 */
public class VerticalCRS extends AbstractCoordinateReferenceSystem {
    /**
     * // * The {@code "VerticalCRS"} node. //
     */
    protected VerticalCRS(SpatioTemporalMetadata metadata) {
        super(metadata, SpatioTemporalMetadataFormat.MD_VERTICALCRS);
    }

    /**
     * Adds Datum {@link Identification} identification.
     */
    public void setDatum(Identification identification) {
        setDatum(Datum.VERTICAL_DATUM, identification);
    }

    /**
     * {@link AbstractCoordinateReferenceSystem.Datum}
     * <p>
     * Description, possibly including coordinates, of the point or points used
     * to anchor the datum to the Earth. Also known as the "origin", especially
     * for Engineering and Image Datums.
     * <ul>
     * <li> For a geodetic datum, this point is also known as the fundamental
     * point, which is traditionally the point where the relationship between
     * geoid and ellipsoid is defined. In some cases, the "fundamental point"
     * may consist of a number of points. In those cases, the parameters
     * defining the geoid/ellipsoid relationship have then been averaged for
     * these points, and the averages adopted as the datum definition. </li>
     * <li> For an engineering datum, the anchor point may be a physical point,
     * or it may be a point with defined coordinates in another CRS. </li>
     * <li> For an image datum, the anchor point is usually either the centre of
     * the image or the corner of the image. </li>
     * <li> For a temporal datum, this attribute is not defined. Instead of the
     * anchor point, a temporal datum carries a separate time origin of type
     * DateTime. </li>
     * </ul>
     * </p>
     */
    public void addAnchorPoint(String value) {
        if (datum.getSelectedChoice() != Datum.VERTICAL_DATUM)
            datum.getChild().setString(SpatioTemporalMetadataFormat.MD_DTM_ANCHORPOINT, value);
        else
            throw new IllegalArgumentException("Could not set Anchor Point for Vertical Datum type");
    }

    /**
     * {@link AbstractCoordinateReferenceSystem.Datum}
     * <p>
     * The time after which this datum definition is valid. This time may be
     * precise (e.g. 1997.0 for IRTF97) or merely a year (e.g. 1986 for
     * NAD83(86)). In the latter case, the epoch usually refers to the year in
     * which a major recalculation of the geodetic control network, underlying
     * the datum, was executed or initiated. An old datum may remain valid after
     * a new datum is defined. Alternatively, a datum may be replaced by a later
     * datum, in which case the realization epoch for the new datum defines the
     * upper limit for the validity of the replaced datum.
     * </p>
     */
    public void addRealizationEpoch(String value) {
        if (datum.getSelectedChoice() != Datum.VERTICAL_DATUM)
            datum.getChild().setString(SpatioTemporalMetadataFormat.MD_DTM_REALIZATIONEPOCH, value);
        else
            throw new IllegalArgumentException("Could not set Realization Epoch for Temporal Datum type");
    }

    /**
     * {@link AbstractCoordinateReferenceSystem.Datum}
     * <p>
     * Type of a vertical datum.
     * <ul>
     * <li>Geoidal: The zero value of the associated vertical coordinate system
     * axis is defined to approximate a constant potential surface, usually the
     * geoid. Such a reference surface is usually determined by a national or
     * scientific authority, and is then a well-known, named datum.</li>
     * <li>Depth: The zero point of the vertical axis is defined by a surface
     * that has meaning for the purpose which the associated vertical
     * measurements are used for. For hydrographic charts, this is often a
     * predicted nominal sea surface (i.e., without waves or other wind and
     * current effects) that occurs at low tide.</li>
     * <li>Barometric: Atmospheric pressure is the basis for the definition of
     * the origin of the associated vertical coordinate system axis.</li>
     * <li>Other surface: In some cases, e.g. oil exploration and production, a
     * geological feature, such as the top or bottom of a geologically
     * identifiable and meaningful subsurface layer, is used as a vertical
     * datum. Other variations to the above three vertical datum types may exist
     * and are all included in this type.</li>
     * </p>
     */
    public void addVerticalDatumType(String value) {
        if (datum.getSelectedChoice() == Datum.VERTICAL_DATUM)
            if ("geoidal".equals(value) || "depth".equals(value) || "barometric".equals(value)
                    || "other_surface".equals(value) || "unspecified".equals(value))
                datum.getChild().setString(SpatioTemporalMetadataFormat.MD_DTM_VD_TYPE,value);
            else
                throw new IllegalArgumentException("Vertical Datum Type must be one of the {geoidal; depth; barometric; other_surface; unspecified}");
        else
            throw new IllegalArgumentException("Could not set Vertical Datum Type for non-Vertical Datum type");
    }

    /**
     * {@link AbstractCoordinateReferenceSystem.Datum}
     */
    public String getAnchorPoint() {
        if (datum.getSelectedChoice() != Datum.VERTICAL_DATUM)
            return datum.getChild().getString(SpatioTemporalMetadataFormat.MD_DTM_ANCHORPOINT);
        else
            throw new IllegalArgumentException("Invalid selection for Temporal Datum type");
    }

    /**
     * {@link AbstractCoordinateReferenceSystem.Datum}
     */
    public String getRealizationEpoch() {
        if (datum.getSelectedChoice() != Datum.VERTICAL_DATUM)
            return datum.getChild().getString(SpatioTemporalMetadataFormat.MD_DTM_REALIZATIONEPOCH);
        else
            throw new IllegalArgumentException("Invalid selection for Temporal Datum type");
    }

    /**
     * {@link AbstractCoordinateReferenceSystem.Datum}
     */
    public String getVerticalDatumType() {
        if (datum.getSelectedChoice() == Datum.VERTICAL_DATUM)
            return datum.getChild().getString(SpatioTemporalMetadataFormat.MD_DTM_VD_TYPE);
        else
            throw new IllegalArgumentException("Could not get Vertical Datum Type for non-Vertical Datum type");
    }
}