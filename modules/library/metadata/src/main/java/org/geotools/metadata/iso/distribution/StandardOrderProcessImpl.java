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
package org.geotools.metadata.iso.distribution;

import java.util.Date;
import org.opengis.util.InternationalString;
import org.opengis.metadata.distribution.StandardOrderProcess;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Common ways in which the resource may be obtained or received, and related instructions
 * and fee information.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Jody Garnett
 *
 * @since 2.1
 */
public class StandardOrderProcessImpl extends MetadataEntity implements StandardOrderProcess {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -6503378937452728631L;

    /**
     * Fees and terms for retrieving the resource.
     * Include monetary units (as specified in ISO 4217).
     */
    private InternationalString fees;

    /**
     * Date and time when the dataset will be available,
     * in milliseconds ellapsed since January 1st, 1970.
     */
    private long plannedAvailableDateTime = Long.MIN_VALUE;

    /**
     * General instructions, terms and services provided by the distributor.
     */
    private InternationalString orderingInstructions;

    /**
     * Typical turnaround time for the filling of an order.
     */
    private InternationalString turnaround;

    /**
     * Constructs an initially empty standard order process.
     */
    public StandardOrderProcessImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public StandardOrderProcessImpl(final StandardOrderProcess source) {
        super(source);
    }

    /**
     * Returns fees and terms for retrieving the resource.
     * Include monetary units (as specified in ISO 4217).
     */
    public InternationalString getFees() {
        return fees;
    }

    /**
     * Set fees and terms for retrieving the resource.
     * Include monetary units (as specified in ISO 4217).
     */
    public synchronized void setFees(final InternationalString newValue) {
        checkWritePermission();
        fees = newValue;
    }

    /**
     * Returns the date and time when the dataset will be available.
     */
    public synchronized Date getPlannedAvailableDateTime() {
        return (plannedAvailableDateTime!=Long.MIN_VALUE) ?
                new Date(plannedAvailableDateTime) : null;
    }

    /**
     * Set the date and time when the dataset will be available.
     */
    public synchronized void setPlannedAvailableDateTime(final Date newValue) {
        checkWritePermission();
        plannedAvailableDateTime = (newValue!=null) ? newValue.getTime() : Long.MIN_VALUE;
    }

    /**
     * Returns general instructions, terms and services provided by the distributor.
     */
    public InternationalString getOrderingInstructions() {
        return orderingInstructions;
    }

    /**
     * Set general instructions, terms and services provided by the distributor.
     */
    public synchronized void setOrderingInstructions(final InternationalString newValue) {
        checkWritePermission();
        orderingInstructions = newValue;
    }

    /**
     * Returns typical turnaround time for the filling of an order.
     */
    public InternationalString getTurnaround() {
        return turnaround;
    }

    /**
     * Set typical turnaround time for the filling of an order.
     */
    public synchronized void setTurnaround(final InternationalString newValue) {
        checkWritePermission();
        turnaround = newValue;
    }
}
