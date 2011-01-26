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
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.metadata.distribution.DigitalTransferOptions;
import org.opengis.metadata.distribution.Distributor;
import org.opengis.metadata.distribution.Format;
import org.opengis.metadata.distribution.StandardOrderProcess;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Information about the distributor.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class DistributorImpl extends MetadataEntity implements Distributor {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 7142984376823483766L;

    /**
     * Party from whom the resource may be obtained. This list need not be exhaustive.
     */
    private ResponsibleParty distributorContact;

    /**
     * Provides information about how the resource may be obtained, and related
     * instructions and fee information.
     */
    private Collection<StandardOrderProcess> distributionOrderProcesses;

    /**
     * Provides information about the format used by the distributor.
     */
    private Collection<Format> distributorFormats;

    /**
     * Provides information about the technical means and media used by the distributor.
     */
    private Collection<DigitalTransferOptions> distributorTransferOptions;

    /**
     * Constructs an initially empty distributor.
     */
    public DistributorImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public DistributorImpl(final Distributor source) {
        super(source);
    }

    /**
     * Creates a distributor with the specified contact.
     */
    public DistributorImpl(final ResponsibleParty distributorContact) {
        setDistributorContact(distributorContact);
    }

    /**
     * Party from whom the resource may be obtained. This list need not be exhaustive.
     */
    public ResponsibleParty getDistributorContact() {
        return distributorContact;
    }

    /**
     * Set the party from whom the resource may be obtained. This list need not be exhaustive.
     */
    public synchronized void setDistributorContact(final ResponsibleParty newValue) {
        checkWritePermission();
        distributorContact = newValue;
    }

    /**
     * Provides information about how the resource may be obtained, and related
     * instructions and fee information.
     */
    public synchronized Collection<StandardOrderProcess> getDistributionOrderProcesses() {
        return (distributionOrderProcesses = nonNullCollection(distributionOrderProcesses,
                StandardOrderProcess.class));
    }

    /**
     * Set information about how the resource may be obtained, and related
     * instructions and fee information.
     */
    public synchronized void setDistributionOrderProcesses(
            final Collection<? extends StandardOrderProcess> newValues)
    {
        distributionOrderProcesses = copyCollection(newValues, distributionOrderProcesses,
                                                    StandardOrderProcess.class);
    }

    /**
     * Provides information about the format used by the distributor.
     */
    public synchronized Collection<Format> getDistributorFormats() {
        return (distributorFormats = nonNullCollection(distributorFormats, Format.class));
    }

    /**
     * Set information about the format used by the distributor.
     */
    public synchronized void setDistributorFormats(final Collection<? extends Format> newValues) {
        distributorFormats = copyCollection(newValues, distributorFormats, Format.class);
    }

    /**
     * Provides information about the technical means and media used by the distributor.
     */
    public synchronized Collection<DigitalTransferOptions> getDistributorTransferOptions() {
        return (distributorTransferOptions = nonNullCollection(distributorTransferOptions,
                DigitalTransferOptions.class));
    }

    /**
     * Provides information about the technical means and media used by the distributor.
     */
    public synchronized void setDistributorTransferOptions(
            final Collection<? extends DigitalTransferOptions> newValues)
    {
        distributorTransferOptions = copyCollection(newValues, distributorTransferOptions,
                                                    DigitalTransferOptions.class);
    }

    /**
     * Sets the {@code xmlMarshalling} flag to {@code true}, since the marshalling
     * process is going to be done.
     * This method is automatically called by JAXB, when the marshalling begins.
     * 
     * @param marshaller Not used in this implementation.
     */
///    private void beforeMarshal(Marshaller marshaller) {
///        xmlMarshalling(true);
///    }

    /**
     * Sets the {@code xmlMarshalling} flag to {@code false}, since the marshalling
     * process is finished.
     * This method is automatically called by JAXB, when the marshalling ends.
     * 
     * @param marshaller Not used in this implementation
     */
///    private void afterMarshal(Marshaller marshaller) {
///        xmlMarshalling(false);
///    }
}
