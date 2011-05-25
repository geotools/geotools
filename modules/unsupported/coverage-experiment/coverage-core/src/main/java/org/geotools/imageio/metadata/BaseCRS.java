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

import org.geotools.coverage.io.util.Utilities;

/**
 * A BaseCRS mainly represents a SpatialCRS (as an instance, a
 * GeographicCRS/3DGeographicCRS) which should also be used to defined a
 * conversion to a DerivedCRS/ProjectedCRS.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-core/src/main/java/org/geotools/imageio/metadata/BaseCRS.java $
 */
public class BaseCRS extends AbstractCoordinateReferenceSystem {

    /**
     * PrimeMeridian element node. It is valid only for Geodetic Datums.
     */
    private IdentifiableMetadataAccessor primeMeridian = null;

    /**
     * Ellipsoid element node. It is valid only for Geodetic Datums.
     */
    private IdentifiableMetadataAccessor ellipsoid = null;

    /**
     * SemiMajorAxis node.
     */
    private MetadataAccessor semiMajorAxis = null;

    private MetadataAccessor secondDefiningParameter = null;

    protected String crsType;

    protected BaseCRS(SpatioTemporalMetadata metadata, final String crsType, final String parentPath) {
        super(metadata, parentPath);
    }

    /**
     * This constructor should only be invoked by ProjectedCRS/DerivedCRS to
     * define the BaseCRS
     */
    protected BaseCRS(final SpatioTemporalMetadata metadata) {
        super(metadata, SpatioTemporalMetadataFormat.MD_CRS + "/" + SpatioTemporalMetadataFormat.MD_SCRS_BASE_CRS, null);
    }

    /**
     * Sets the {@linkplain Identification identification} of the CoordinateReferenceSystem
     */
    public void setCRS(final Identification identification) {
        setIdentification(identification);
    }

    /**
     * Returns the {@linkplain Identification identification}
     */
    public Identification getCRS() {
        return getIdentification();
    }

    /**
     * <p>
     * A prime meridian defines the origin from which longitude values are
     * determined. Note: Default value for prime meridian name is "Greenwich".
     * When default applies, value for greenwichLongitude shall be 0 (degrees).
     * </p>
     * <ul>
     * <li>Prime meridian Greenwich longitude: Longitude of the prime meridian
     * measured from the Greenwich meridian, positive eastward.
     * <ul>
     * <li>Default value: 0 degrees. </li>
     * <li>Note: If the value of the prime meridian name is "Greenwich" then
     * the value of greenwichLongitude shall be 0 degrees.</li>
     * </ul>
     * </li>
     * </ul>
     */
    public void addPrimeMeridian(final String greenwichLon,
            Identification primeMeridianId) {
        if (datum.getSelectedChoice() == Datum.GEODETIC_DATUM) {
            primeMeridian = new IdentifiableMetadataAccessor(datum.getChild(),
                    SpatioTemporalMetadataFormat.MD_DTM_GEODETIC_PRIMEMERIDIAN,
                    null, primeMeridianId);

            primeMeridian.setString(SpatioTemporalMetadataFormat.MD_DTM_GD_PM_GREENWICHLONGITUDE, greenwichLon);
        } else
            throw new IllegalArgumentException("Could not set Prime Meridian for non-Geodetic Datum type");
    }

    /**
     * <p>
     * An ellipsoid is a geometric figure that can be used to describe the
     * approximate shape of the earth. In mathematical terms, it is a surface
     * formed by the rotation of an ellipse about its minor axis.
     * </p>
     * <p>
     * <ul>
     * <li>semiMajorAxis: Length of the semi-major axis of the ellipsoid.</li>
     * <li>secondDefiningParameter: Definition of the second parameter that
     * describes the shape of this ellipsoid.
     * <ul>
     * <li>Inverse flattening: Inverse flattening value of the ellipsoid.</li>
     * <li>Length of semi-minor axis: Length of the semi-minor axis of the
     * ellipsoid.</li>
     * <li>�Ellipsoid = Sphere� indicator: The ellipsoid is degenerate and is
     * actually a sphere. The sphere is completely defined by the semi-major
     * axis, which is the radius of the sphere. This attribute has the fixed
     * text value "sphere".</li>
     * </ul>
     * </li>
     * </ul>
     * </p>
     */
    public void addEllipsoid(final String semiMajorAxis, final String semiMinorAxis, final String invFlattening,
            final String unit, Identification ellipsoidId) {
        if (datum.getSelectedChoice() == Datum.GEODETIC_DATUM) {
            ellipsoid = new IdentifiableMetadataAccessor(datum.getChild(),
                    SpatioTemporalMetadataFormat.MD_DTM_GD_ELLIPSOID, null, ellipsoidId);

            this.semiMajorAxis = new MetadataAccessor(ellipsoid, SpatioTemporalMetadataFormat.MD_DTM_GD_EL_SEMIMAJORAXIS, null);
            this.semiMajorAxis.setString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, semiMajorAxis);
            ellipsoid.setString(SpatioTemporalMetadataFormat.MD_DTM_GD_EL_UNIT, unit);

            this.secondDefiningParameter = new MetadataAccessor(ellipsoid,
                    SpatioTemporalMetadataFormat.MD_DTM_GD_EL_SECONDDEFPARAM, null);
            if (Utilities.ensureValidString(semiMajorAxis, semiMinorAxis) && semiMajorAxis.equals(semiMinorAxis)) {
                this.secondDefiningParameter.setString(SpatioTemporalMetadataFormat.MD_DTM_GD_EL_SPHERE, "true");
            } else if (Utilities.ensureValidString(semiMinorAxis)) {
                this.secondDefiningParameter.setString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTETYPE, SpatioTemporalMetadataFormat.MD_DTM_GD_EL_SEMIMINORAXIS);
                this.secondDefiningParameter.setString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, semiMinorAxis);
            } else if (Utilities.ensureValidString(invFlattening)) {
                this.secondDefiningParameter.setString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTETYPE,
                                SpatioTemporalMetadataFormat.MD_DTM_GD_EL_INVERSEFLATTENING);
                this.secondDefiningParameter.setString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, invFlattening);
            } else {
                throw new IllegalArgumentException("Second Defining Parameter is mandatory for Geodetic Datum Ellipsoid!");
            }
        } else
            throw new IllegalArgumentException("Could not set Prime Meridian for non-Geodetic Datum type");
    }

    /**
     * See {@link #addPrimeMeridian(String, Identification)}.
     * 
     * @return GreenwichLongitude {@link String}
     */
    public String getGreenwichLongitude() {
        if (datum.getSelectedChoice() == Datum.GEODETIC_DATUM && primeMeridian != null) {
            return primeMeridian.getString(SpatioTemporalMetadataFormat.MD_DTM_GD_PM_GREENWICHLONGITUDE);
        } else
            throw new IllegalArgumentException("Prime Meridian not set or non-Geodetic Datum type selected");
    }

    /**
     * See {@link #addEllipsoid(String, String, String, String, Identification)}.
     * 
     * @return ellipsoid unit {@link String}
     */
    public String getEllipsoidUnit() {
        if (datum.getSelectedChoice() == Datum.GEODETIC_DATUM&& ellipsoid != null) {
            return ellipsoid.getString(SpatioTemporalMetadataFormat.MD_DTM_GD_EL_UNIT);
        } else
            throw new IllegalArgumentException("Ellipsoid not set or non-Geodetic Datum type selected");
    }

    /**
     * See {@link #addEllipsoid(String, String, String, String, Identification)}.
     * 
     * @return SemiMajorAxis {@link String}
     */
    public String getSemiMajorAxis() {
        if (datum.getSelectedChoice() == Datum.GEODETIC_DATUM && semiMajorAxis != null) {
            return semiMajorAxis.getString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE);
        } else
            throw new IllegalArgumentException("Ellipsoid not set or non-Geodetic Datum type selected");
    }

    /**
     * See {@link #addEllipsoid(String, String, String, String, Identification)}.
     * 
     * @return SecondDefinigParameterType {@link String}
     */
    public String getSecondDefinigParameterType() {
        if (datum.getSelectedChoice() == Datum.GEODETIC_DATUM && secondDefiningParameter != null) {
            return secondDefiningParameter.getString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTETYPE);
        } else
            throw new IllegalArgumentException("Ellipsoid not set or non-Geodetic Datum type selected");
    }

    /**
     * See {@link #addEllipsoid(String, String, String, String, Identification)}.
     * 
     * @return SecondDefinigParameterValue {@link String}
     */
    public String getSecondDefinigParameterValue() {
        if (datum.getSelectedChoice() == Datum.GEODETIC_DATUM && secondDefiningParameter != null) {
            return secondDefiningParameter.getString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE);
        } else
            throw new IllegalArgumentException("Ellipsoid not set or non-Geodetic Datum type selected");
    }

    /**
     * See {@link #addPrimeMeridian(String, Identification)}.
     * 
     * @return PrimeMeridian {@link String}
     */
    public Identification getPrimeMeridian() {
        if (primeMeridian != null)
            return new Identification(primeMeridian);
        else
            throw new IllegalArgumentException("Prime Meridian not set or non-Geodetic Datum type selected");
    }

    /**
     * See {@link #setDatum(int, Identification)}.
     * 
     * @param value
     *                PixelInCell {@link String}
     */
    public void addPixelInCell(String value) {
        if (datum.getSelectedChoice() == Datum.IMAGE_DATUM)
            if ("cell_corner".equals(value) || "cell_center".equals(value))
                datum.getChild().setString(SpatioTemporalMetadataFormat.MD_DTM_ID_PIXELINCELL, value);
            else
                throw new IllegalArgumentException("Pixel in Cell must be one of the {cell_corner; cell_center}");
        else
            throw new IllegalArgumentException("Could not set Pixel in Cell for non-Image Datum type");
    }

    /**
     * See {@link #setDatum(int, Identification)}.
     * 
     * @return value {@link String}
     */
    public String getPixelInCell() {
        if (datum.getSelectedChoice() == Datum.IMAGE_DATUM)
            return datum.getChild().getString(SpatioTemporalMetadataFormat.MD_DTM_ID_PIXELINCELL);
        else
            throw new IllegalArgumentException("Could not get Pixel in Cell for non-Image Datum type");
    }

    /**
     * Returns the
     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system} in <A
     * HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> format</A>, or {@code null} if none.
     */
    public String getWKT() {
        return getString("WKT");
    }

    /**
     * Sets the
     * {@linkplain AbstractCoordinateReferenceSystem coordinate reference system} in <A
     * HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> format</A>.
     */
    public void setWKT(final String wkt) {
        setString("WKT", wkt);
    }

    /**
     * See {@link #addEllipsoid(String, Identification)}.
     * 
     * @return Ellipsoid {@link String}
     */
    public Identification getEllipsoid() {
        if (ellipsoid != null)
            return new Identification(ellipsoid);
        else
            throw new IllegalArgumentException("Ellipsoid no set!");
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
    public void setDatum(final int datumType, final Identification identification) {
        if (datumType == Datum.TEMPORAL_DATUM || datumType == Datum.VERTICAL_DATUM)
            throw new IllegalArgumentException("Use a TemporalCRS/VerticalCRS to set Temporal/Vertical datums");
        super.setDatum(datumType, identification);
    }

    public String getCrsType() {
        return crsType;
    }
}
