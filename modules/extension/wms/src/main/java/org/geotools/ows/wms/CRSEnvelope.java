/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.ows.wms.request.AbstractGetMapRequest;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A pair of coordinates and a reference system that represents a section of the Earth.
 *
 * <p>Represents one of the following:
 *
 * <ul>
 *   <li>EX_GeographicBoundingBox: (implicit CRS:84) limits of the enclosing rectangle in longitude
 *       and latitude decimal degrees
 *   <li>BoundingBox: The BoundingBox attributes indicate the limits of the bounding box in units of
 *       the specified coordinate reference system.
 * </ul>
 *
 * The interpretation of the srsName is based on the version of WMS specification used:
 *
 * <ul>
 *   <li>After WMS 1.3.0: axis order be returned with forceXY=false
 *   <li>Before WMS 1.3.0: axis order defined using forceXY=true
 * </ul>
 *
 * @author Richard Gould
 */
public class CRSEnvelope implements Envelope {
    /**
     * Represents the Coordinate Reference System this bounding box is in. This is usually an EPSG
     * code such as "EPSG:4326"
     */
    private String srsName;

    /** Min of axis 0 as specified by CRS */
    protected double minX;
    /** Min of axis 1 as specified by CRS */
    protected double minY;
    /** Max of axis 0 as specified by CRS */
    protected double maxX;
    /** Max of axis 1 as specified by CRS */
    protected double maxY;

    /** CRS as defined my WebMapServer (CRS:84 implicit if null) */
    private CoordinateReferenceSystem crs;

    /** optional spatial resolution in the units of crs */
    protected double resX;

    /** optional spatial resolution in the units of crs */
    protected double resY;

    /**
     * Indicate how srsName is defined. Use <code>null</code> if unknown (will default to global
     * GeoTools setting), <code>True</code> to forceXY axis order (used prior to WMS 1.3.0), <code>
     * False</code> to use provided axis order (WMS 1.3.0 and later )
     */
    private Boolean forceXY = null;

    /** Construct an empty BoundingBox */
    public CRSEnvelope() {}

    /**
     * Create a bounding box with the specified properties
     *
     * @param epsgCode The Coordinate Reference System this bounding box is in
     */
    public CRSEnvelope(String epsgCode, double minX, double minY, double maxX, double maxY) {
        this.srsName = epsgCode;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public CRSEnvelope(Envelope envelope) {
        this.srsName = CRS.toSRS(envelope.getCoordinateReferenceSystem());
        // this.srsName = epsgCode;
        this.minX = envelope.getMinimum(0);
        this.maxX = envelope.getMaximum(0);
        this.minY = envelope.getMinimum(1);
        this.maxY = envelope.getMaximum(1);
    }

    /**
     * Returns the coordinate reference system for this envelope (if known). return
     * CoordinateReferenceSystem if known, or {@code null}
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        synchronized (this) {
            if (crs == null) {
                try {
                    String srs = srsName != null ? srsName : "CRS:84";
                    if (forceXY == null) {
                        crs = CRS.decode(srs);
                    } else {
                        crs = AbstractGetMapRequest.toServerCRS(srsName, forceXY);
                    }
                } catch (NoSuchAuthorityCodeException e) {
                    crs = DefaultEngineeringCRS.CARTESIAN_2D;
                } catch (FactoryException e) {
                    crs = DefaultEngineeringCRS.CARTESIAN_2D;
                }
            }
            return crs == DefaultEngineeringCRS.CARTESIAN_2D ? null : crs;
        }
    }

    /**
     * The CRS is bounding box's Coordinate Reference System.
     *
     * @return the CRS/SRS value, or null for implicit CRS:84
     */
    public String getSRSName() {
        return srsName;
    }

    /**
     * Helper method to set srsName.
     *
     * @see setSRSName
     */
    public void setEPSGCode(String epsgCode) {
        setSRSName(epsgCode);
    }

    /** @see getSRSName */
    public String getEPSGCode() {
        return srsName;
    }

    private void setSRSName(String srsName) {
        this.srsName = srsName;
        this.forceXY = null;
    }

    /**
     * The CRS is bounding box's Coordinate Reference System.
     *
     * <p>Examples from WMS specification:
     *
     * <ul>
     *   <li>CRS:84: default in lon / lat order
     * </ul>
     *
     * @param srsName The SRSName for this envelope; usually an EPSG code
     * @param forceXY True to forceXY axis order (used prior to WMS 1.3.0), False to use provided
     *     axis order (WMS 1.3.0 and later )
     */
    public void setSRSName(String srsName, boolean forceXY) {
        this.srsName = srsName;
        this.forceXY = forceXY;
    }

    public int getDimension() {
        return 2;
    }

    public double getMinimum(int dimension) {
        if (dimension == 0) {
            return getMinX();
        }

        return getMinY();
    }

    public double getMaximum(int dimension) {
        if (dimension == 0) {
            return getMaxX();
        }

        return getMaxY();
    }

    public double getCenter(int dimension) {
        return getMedian(dimension);
    }

    public double getMedian(int dimension) {
        double min; // , max;
        if (dimension == 0) {
            min = getMinX();
            // max = getMaxX();
        } else {
            min = getMinY();
            // max = getMaxY();
        }
        return min + (getLength(dimension) / 2);
    }

    public double getLength(int dimension) {
        return getSpan(dimension);
    }

    public double getSpan(int dimension) {
        double min, max;
        if (dimension == 0) {
            min = getMinX();
            max = getMaxX();
        } else {
            min = getMinY();
            max = getMaxY();
        }

        return max - min;
    }

    public DirectPosition getUpperCorner() {
        return new GeneralDirectPosition(getMaxX(), getMaxY());
    }

    public DirectPosition getLowerCorner() {
        return new GeneralDirectPosition(getMinX(), getMinY());
    }

    /**
     * The maxX value is the higher X coordinate value
     *
     * @return the bounding box's maxX value
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * The maxX value is the higher X coordinate value
     *
     * @param maxX the new value for maxX. Should be greater than minX.
     */
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    /**
     * The maxY value is the higher Y coordinate value
     *
     * @return the bounding box's maxY value
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * The maxY value is the higher Y coordinate value
     *
     * @param maxY the new value for maxY. Should be greater than minY.
     */
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    /**
     * The minX value is the lower X coordinate value
     *
     * @return the bounding box's minX value
     */
    public double getMinX() {
        return minX;
    }

    /**
     * The minX value is the lower X coordinate value
     *
     * @param minX the new value for minX. Should be less than maxX.
     */
    public void setMinX(double minX) {
        this.minX = minX;
    }

    /**
     * The minY value is the lower Y coordinate value
     *
     * @return the bounding box's minY value
     */
    public double getMinY() {
        return minY;
    }

    /**
     * The minY value is the lower Y coordinate value
     *
     * @param minY the new value for minY. Should be less than maxY.
     */
    public void setMinY(double minY) {
        this.minY = minY;
    }

    /**
     * Optional spatial resolution in the units of crs.
     *
     * @return spatial resolutionm, or Double.NaN if not provided
     */
    public double getResX() {
        return resX;
    }

    /**
     * Optional spatial resolution in the units of crs.
     *
     * @param resX spatial resolutionm, or Double.NaN if not provided
     */
    public void setResX(double resX) {
        this.resX = resX;
    }
    /**
     * Optional spatial resolution in the units of crs.
     *
     * @return spatial resolutionm, or Double.NaN if not provided
     */
    public double getResY() {
        return resY;
    }
    /**
     * Optional spatial resolution in the units of crs.
     *
     * @param resY spatial resolutionm, or Double.NaN if not provided
     */
    public void setResY(double resY) {
        this.resY = resY;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("[");
        build.append(minX);
        build.append(",");
        build.append(maxX);
        if (!Double.isNaN(resX)) {
            build.append(",");
            build.append(resX);
        }
        build.append(" ");
        build.append(minY);
        build.append(",");
        build.append(maxY);
        if (!Double.isNaN(resY)) {
            build.append(",");
            build.append(resY);
        }
        if (srsName != null) {
            build.append(" crs=");
            build.append(srsName);
        } else {
            build.append(" default=CRS:84");
        }
        build.append("]");

        return build.toString();
    }
}
