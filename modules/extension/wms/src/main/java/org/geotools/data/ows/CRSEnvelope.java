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
package org.geotools.data.ows;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A pair of coordinates and a reference system that represents a section of the Earth
 * 
 * @author Richard Gould
 * @source $URL:
 *         http://svn.osgeo.org/geotools/branches/2.6.x/modules/extension/wms/src/main/java/org
 *         /geotools/data/ows/CRSEnvelope.java $
 */
public class CRSEnvelope implements Envelope {
    /**
     * Represents the Coordinate Reference System this bounding box is in. This is usually an EPSG
     * code such as "EPSG:4326"
     */
    private String srsName;

    protected double minX;

    protected double minY;

    protected double maxX;

    protected double maxY;

    private CoordinateReferenceSystem crs;

    /**
     * Construct an empty BoundingBox
     */
    public CRSEnvelope() {        
    }

    /**
     * Create a bounding box with the specified properties
     * 
     * @param epsgCode
     *            The Coordinate Reference System this bounding box is in
     * @param minX
     * @param minY
     * @param maxX
     * @param maxY
     */
    public CRSEnvelope(String epsgCode, double minX, double minY, double maxX, double maxY) {
        this.srsName = epsgCode;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
    public CRSEnvelope(Envelope envelope) {
        this.srsName = envelope.getCoordinateReferenceSystem().getIdentifiers().iterator().next().toString();
        //this.srsName = epsgCode;
        this.minX = envelope.getMinimum(0);
        this.maxX = envelope.getMaximum(0);
        this.minY = envelope.getMinimum(1);       
        this.maxY = envelope.getMaximum(1);
    }

    /**
     * Returns the coordinate reference system for this envelope (if known).
     * return CoordinateReferenceSystem if known, or {@code null}
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        synchronized (this) {
            if (crs == null) {
                try {
                    crs = CRS.decode(srsName);
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
     * The CRS is bounding box's Coordinate Reference System
     * 
     * @return the CRS/SRS value
     */
    public String getSRSName() {
        return srsName;
    }

    /**
     * @see setSRSName
     */
    public void setEPSGCode(String epsgCode) {
        setSRSName(epsgCode);
    }

    /**
     * @see getSRSName
     */
    public String getEPSGCode() {
        return srsName;
    }

    /**
     * The CRS is bounding box's Coordinate Reference System
     * 
     * @param srsName
     *            The SRSName for this envelope; usually an EPSG code
     */
    public void setSRSName(String epsgCode) {
        this.srsName = epsgCode;
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
        double min;//, max;
        if (dimension == 0) {
            min = getMinX();
            //max = getMaxX();
        } else {
            min = getMinY();
            //max = getMaxY();
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
     * @param maxX
     *            the new value for maxX. Should be greater than minX.
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
     * @param maxY
     *            the new value for maxY. Should be greater than minY.
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
     * @param minX
     *            the new value for minX. Should be less than maxX.
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
     * @param minY
     *            the new value for minY. Should be less than maxY.
     */
    public void setMinY(double minY) {
        this.minY = minY;
    }

    public String toString() {
        return srsName.toString() + " [" + minX + "," + minY + " " + maxX + "," + maxY + "]";
    }

}
