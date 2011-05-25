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
package org.geotools.metadata.iso.extent;

import java.util.Date;
import org.opengis.metadata.extent.TemporalExtent;
import org.opengis.temporal.TemporalPrimitive;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Boundary enclosing the dataset, expressed as the closed set of
 * (<var>x</var>,<var>y</var>) coordinates of the polygon. The last
 * point replicates first point.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class TemporalExtentImpl extends MetadataEntity implements TemporalExtent {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3668140516657118045L;

    /**
     * The start date and time for the content of the dataset,
     * in milliseconds ellapsed since January 1st, 1970. A value
     * of {@link Long#MIN_VALUE} means that this attribute is not set.
     */
    private long startTime = Long.MIN_VALUE;

    /**
     * The end date and time for the content of the dataset,
     * in milliseconds ellapsed since January 1st, 1970. A value
     * of {@link Long#MIN_VALUE} means that this attribute is not set.
     */
    private long endTime = Long.MIN_VALUE;

    /**
     * The date and time for the content of the dataset.
     */
    private TemporalPrimitive extent;

    /**
     * Constructs an initially empty temporal extent.
     */
    public TemporalExtentImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public TemporalExtentImpl(final TemporalExtent source) {
        super(source);
    }

    /**
     * Creates a temporal extent initialized to the specified values.
     */
    public TemporalExtentImpl(final Date startTime, final Date endTime) {
        setStartTime(startTime);
        setEndTime  (endTime);
    }

    /**
     * The start date and time for the content of the dataset.
     */
    public synchronized Date getStartTime() {
        return (startTime!=Long.MIN_VALUE) ? new Date(startTime) : null;
    }

    /**
     * Set the start date and time for the content of the dataset.
     */
    public synchronized void setStartTime(final Date newValue) {
        checkWritePermission();
        startTime = (newValue!=null) ? newValue.getTime() : Long.MIN_VALUE;
    }

    /**
     * Returns the end date and time for the content of the dataset.
     */
    public synchronized Date getEndTime() {
        return (endTime!=Long.MIN_VALUE) ? new Date(endTime) : null;
    }

    /**
     * Set the end date and time for the content of the dataset.
     */
    public synchronized void setEndTime(final Date newValue) {
        checkWritePermission();
        endTime = (newValue!=null) ? newValue.getTime() : Long.MIN_VALUE;
    }

    /**
     * Returns the date and time for the content of the dataset.
     *
     * @since 2.4
     */
    // No implementing class for {@linkplain org.opengis.temporal.TemporalPrimitive}
    public TemporalPrimitive getExtent() {
        return extent;
    }

    /**
     * Set the date and time for the content of the dataset.
     *
     * @since 2.4
     */
    public synchronized void setExtent(final TemporalPrimitive newValue) {
        checkWritePermission();
        extent = newValue;
    }
}
