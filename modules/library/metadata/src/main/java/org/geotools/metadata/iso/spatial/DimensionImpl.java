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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.spatial;

import org.opengis.metadata.spatial.Dimension;
import org.opengis.metadata.spatial.DimensionNameType;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Axis properties.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class DimensionImpl extends MetadataEntity implements Dimension {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -2572515000574007266L;

    /**
     * Name of the axis.
     */
    private DimensionNameType dimensionName;

    /**
     * Number of elements along the axis.
     */
    private Integer dimensionSize;

    /**
     * Degree of detail in the grid dataset.
     */
    private Double resolution;

    /**
     * Constructs an initially empty dimension.
     */
    public DimensionImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public DimensionImpl(final Dimension source) {
        super(source);
    }

    /*
     * Creates a dimension initialized to the given type.
     */
    public DimensionImpl(final DimensionNameType dimensionName, final int dimensionSize) {
        setDimensionName(dimensionName);
        setDimensionSize(dimensionSize);
    }

    /**
     * Name of the axis.
     */
    public DimensionNameType getDimensionName() {
        return dimensionName;
    }

    /**
     * Set the name of the axis.
     */
    public synchronized void setDimensionName(final DimensionNameType newValue) {
        checkWritePermission();
        dimensionName = newValue;
    }

    /**
     * Number of elements along the axis.
     */
    public Integer getDimensionSize() {
        return dimensionSize;
    }

    /**
     * Set the number of elements along the axis.
     */
    public synchronized void setDimensionSize(final Integer newValue) {
        checkWritePermission();
        dimensionSize = newValue;
    }

    /**
     * Degree of detail in the grid dataset.
     */
    public Double getResolution() {
        return resolution;
    }

    /**
     * Set the degree of detail in the grid dataset.
     */
    public synchronized void setResolution(final Double newValue) {
        checkWritePermission();
        resolution = newValue;
    }
}
