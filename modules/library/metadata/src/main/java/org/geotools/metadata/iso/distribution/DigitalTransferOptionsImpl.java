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

import java.util.Collection;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.metadata.distribution.DigitalTransferOptions;
import org.opengis.metadata.distribution.Medium;
import org.opengis.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Technical means and media by which a resource is obtained from the distributor.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class DigitalTransferOptionsImpl extends MetadataEntity implements DigitalTransferOptions {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -1533064478468754337L;

    /**
     * Tiles, layers, geographic areas, etc., in which data is available.
     */
    private InternationalString unitsOfDistribution;

    /**
     * Estimated size of a unit in the specified transfer format, expressed in megabytes.
     * The transfer size is &gt; 0.0.
     * Returns {@code null} if the transfer size is unknown.
     */
    private Double transferSize;

    /**
     * Information about online sources from which the resource can be obtained.
     */
    private Collection<OnLineResource> onLines;

    /**
     * Information about offline media on which the resource can be obtained.
     */
    private Medium offLines;

    /**
     * Constructs an initially empty digital transfer options.
     */
    public DigitalTransferOptionsImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public DigitalTransferOptionsImpl(final DigitalTransferOptions source) {
        super(source);
    }

    /**
     * Returne tiles, layers, geographic areas, etc., in which data is available.
     */
    public InternationalString getUnitsOfDistribution() {
        return unitsOfDistribution;
    }

    /**
     * Set tiles, layers, geographic areas, etc., in which data is available.
     */
    public synchronized void setUnitsOfDistribution(final InternationalString newValue) {
        checkWritePermission();
        unitsOfDistribution = newValue;
    }

    /**
     * Returns an estimated size of a unit in the specified transfer format, expressed in megabytes.
     * The transfer size is &gt; 0.0.
     * Returns {@code null} if the transfer size is unknown.
     */
    public Double getTransferSize() {
        return transferSize;
    }

    /**
     * Set an estimated size of a unit in the specified transfer format, expressed in megabytes.
     * The transfer size is &gt; 0.0.
     */
    public synchronized void setTransferSize(final Double newValue) {
        checkWritePermission();
        transferSize = newValue;
    }

    /**
     * Returns information about online sources from which the resource can be obtained.
     */
    public synchronized Collection<OnLineResource> getOnLines() {
        return (onLines = nonNullCollection(onLines, OnLineResource.class));
    }

    /**
     * Set information about online sources from which the resource can be obtained.
     */
    public synchronized void setOnLines(final Collection<? extends OnLineResource> newValues) {
        onLines = copyCollection(newValues, onLines, OnLineResource.class);
    }

    /**
     * Returns information about offline media on which the resource can be obtained.
     */
    public Medium getOffLine() {
        return offLines;
    }

    /**
     * Set information about offline media on which the resource can be obtained.
     */
    public synchronized void setOffLine(final Medium newValue) {
        checkWritePermission();
        offLines = newValue;
    }
}
