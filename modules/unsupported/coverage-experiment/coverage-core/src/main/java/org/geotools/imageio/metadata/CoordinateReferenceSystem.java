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

import org.geotools.imageio.metadata.DefinedByConversion.ParameterValue;
import org.opengis.referencing.crs.DerivedCRS;

/**
 * A {@code CoordinateReferenceSystem} node in the metadata tree.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 */
public class CoordinateReferenceSystem extends BaseCRS {

    /** An optional BaseCRS node in case of Projected/Derived CoordinateReferenceSystem */
    private BaseCRS baseCRS;

    /** The optional DefinedByConversion node in case of Projected/Derived CoordinateReferenceSystem */
    private DefinedByConversion definedByConversion;

    private boolean isProjectedOrDerived;

    protected CoordinateReferenceSystem(SpatioTemporalMetadata metadata, final String crsType) {
        super(metadata, crsType, SpatioTemporalMetadataFormat.MD_CRS);
        if (crsType != null
                && (crsType.equalsIgnoreCase(SpatioTemporalMetadataFormat.DERIVED) || crsType
                        .equalsIgnoreCase(SpatioTemporalMetadataFormat.PROJECTED))) {
            isProjectedOrDerived = true;
            definedByConversion = new DefinedByConversion(this);
            baseCRS = new BaseCRS(metadata);
        } else {
            isProjectedOrDerived = false;
            definedByConversion = null;
            baseCRS = null;
        }
    }

    /** See {@link DefinedByConversion}. */
    public ParameterValue addParameterValue(
            final Identification identification, final String value) {
        if (!isProjectedOrDerived)
            throw new IllegalArgumentException("Could not set definedByConversion parameters since this CoordinateReferenceSystem is not a Derived/Projected CoordinateReferenceSystem");
        return definedByConversion.addParameterValue(identification, value);
    }

    /** See {@link DefinedByConversion}. */
    public void setDefinedByConversion(final Identification identification,
            final String formula, final String srcDim, final String targetDim) {
        if (!isProjectedOrDerived)
            throw new IllegalArgumentException("Could not set definedByConversion parameters since this CoordinateReferenceSystem is not a Derived/Projected CoordinateReferenceSystem");
        definedByConversion.setDefinedByConversion(identification, formula, srcDim, targetDim);
    }

    /**
     * Set the Base CRS.
     * 
     * @param identification
     *                the identification of the BaseCRS.
     */
    public void setBaseCRS(final Identification identification) {
        if (!isProjectedOrDerived)
            throw new IllegalArgumentException("Could not set BaseCRS since this CoordinateReferenceSystem is not a Derived/Projected CoordinateReferenceSystem");
        baseCRS.setIdentification(identification);
    }

    /**
     * Sets the {@linkplain Identification identification} of the
     * {@linkplain DerivedCRS derived crs}.
     * 
     * @param name
     *                The coordinate system name, or {@code null} if unknown.
     * @param type
     *                The coordinate system type (usually
     *                {@value SpatioTemporalMetadataFormat#ELLIPSOIDAL} or
     *                {@value SpatioTemporalMetadataFormat#CARTESIAN}), or
     *                {@code null} if unknown.
     * 
     * @see CoordinateSystem
     */
    public void setCRS(final Identification identification) {
        setIdentification(identification);
    }

    /**
     * Return the CRS identification.
     * 
     * @return the CRS {@link Identification}
     */
    public Identification getCRS() {
        return getIdentification();
    }

    /**
     * Return the BaseCRS identification.
     * 
     * @return BaseCRS {@link Identification}, or {@code null} in case there 
     * isn't a BaseCRS.
     */
    public Identification getBaseCRS() {
        if (!isProjectedOrDerived)
            return null;
        return baseCRS.getIdentification();
    }

    /**
     * Add Prime Meridian 
     */
    public void addPrimeMeridian(final String greenwichLon,
            Identification primeMeridianId) {
        if (!isProjectedOrDerived) {
            super.addPrimeMeridian(greenwichLon, primeMeridianId);
        } else
            baseCRS.addPrimeMeridian(greenwichLon, primeMeridianId);
    }

    /**
     * Add Ellipsoid
     */
    public void addEllipsoid(final String semiMajorAxis,
            final String semiMinorAxis, final String invFlattening,
            final String unit, Identification ellipsoidId) {
        if (!isProjectedOrDerived) {
            super.addEllipsoid(semiMajorAxis, semiMinorAxis, invFlattening, unit, ellipsoidId);
        } else
            baseCRS.addEllipsoid(semiMajorAxis, semiMinorAxis, invFlattening, unit, ellipsoidId);
    }

    /**
     * Get Ellipsoid identification
     * 
     * @return Ellipsoid {@link Identification}
     */
    public Identification getEllipsoid() {
        if (!isProjectedOrDerived)
            return super.getEllipsoid();
        else
            return baseCRS.getEllipsoid();
    }

    /**
     * Get Greenwich Longitude
     * 
     * @return GreenwichLongitude {@link String}
     */
    public String getGreenwichLongitude() {
        if (!isProjectedOrDerived)
            return super.getGreenwichLongitude();
        else
            return baseCRS.getGreenwichLongitude();
    }

    /**
     * Get Semi Major Axis
     * 
     * @return SemiMajorAxis {@link String}
     */
    public String getSemiMajorAxis() {
        if (!isProjectedOrDerived)
            return super.getSemiMajorAxis();
        return baseCRS.getSemiMajorAxis();
    }

    /**
     * Get Second Defining Parameter Type
     */
    public String getSecondDefinigParameterType() {
        if (!isProjectedOrDerived)
            return super.getSecondDefinigParameterType();
        return baseCRS.getSecondDefinigParameterType();
    }

    /**
     * Get Second Defining Parameter Value
     */
    public String getSecondDefinigParameterValue() {
        if (!isProjectedOrDerived)
            return super.getSecondDefinigParameterValue();
        return baseCRS.getSecondDefinigParameterValue();
    }

    /**
     * Get the Prime Meridian
     * 
     * @return PrimeMeridian {@link Identification}
     */
    public Identification getPrimeMeridian() {
        if (!isProjectedOrDerived)
            return super.getPrimeMeridian();
        return baseCRS.getPrimeMeridian();
    }

    /**
     * Add the PixelInCell information.
     */
    public void addPixelInCell(String value) {
        if (!isProjectedOrDerived)
            super.addPixelInCell(value);
        else
            baseCRS.addPixelInCell(value);
    }

    /**
     * Get the Pixel In Cell information.
     * 
     * @return PixelInCell {@link String}
     */
    public String getPixelInCell() {
        if (!isProjectedOrDerived)
            return super.getPixelInCell();
        return baseCRS.getPixelInCell();
    }

    public DefinedByConversion getDefinedByConversion() {
        return definedByConversion;
    }

    /**
     * Set the Datum by specifying its type and identification
     * 
     * @param datumType
     *                the datum type. Note that temporal datum and vertical
     *                datum need to be specified within a TemporalCRS or
     *                VerticalCRS instance.
     */
    public void setDatum(final int datumType, final Identification identification) {
        if (datumType == Datum.TEMPORAL_DATUM || datumType == Datum.VERTICAL_DATUM)
            throw new IllegalArgumentException("Use a TemporalCRS/VerticalCRS to set Temporal/Vertical datums");
        if (!isProjectedOrDerived)
            super.setDatum(datumType, identification);
        else
            baseCRS.setDatum(datumType, identification);
    }

    /**
     * Get the Unit of the Ellipsoid.
     * 
     * @return EllipsoidUnit {@link String}
     */
    public String getEllipsoidUnit() {
        if (!isProjectedOrDerived)
            return super.getEllipsoidUnit();
        else
            return baseCRS.getEllipsoidUnit();
    }

}
