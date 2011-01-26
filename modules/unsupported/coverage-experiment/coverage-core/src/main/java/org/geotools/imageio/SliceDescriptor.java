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
package org.geotools.imageio;

import org.geotools.geometry.GeneralEnvelope;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.temporal.TemporalGeometricPrimitive;

/**
 * Allows retrieving main properties of each 2D slice. Main properties are
 * Temporal Extent, Vertical Extent, BoundingBox, name.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 */
public interface SliceDescriptor {

    /** return the time for this slice as a {@link TemporalGeometricPrimitive} */
    public TemporalGeometricPrimitive getTemporalExtent();

    /**
     * return the vertical extent of this slice in case of not
     * geographic3D/projected3D crs
     */
    public VerticalExtent getVerticalExtent();

    /**
     * return the horizontal extent of this slice.
     */
    public BoundingBox getHorizontalExtent();

    /** return the ND general envelope */
    public GeneralEnvelope getGeneralEnvelope();

    /**
     * Returns the name of the element contained in this slice (usually, the
     * coverage name)
     */
    public String getElementName();

    /**
     * Return the Coordinate Reference System, usually a CompoundCRS. Common
     * cases are composed of
     * TemporalCRS+VerticalCRS+2DGeographicCRS/ProjectedCRS
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem();
}