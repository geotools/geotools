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
package org.geotools.metadata.iso.lineage;

import java.util.Collection;
import java.util.Date;

import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.metadata.lineage.Source;
import org.opengis.metadata.lineage.ProcessStep;
import org.opengis.util.InternationalString;

import org.geotools.metadata.iso.MetadataEntity;


/**
 * Description of the event, including related parameters or tolerances.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class ProcessStepImpl extends MetadataEntity implements ProcessStep {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 4629429337326490722L;

    /**
     * Description of the event, including related parameters or tolerances.
     */
    private InternationalString description;

    /**
     * Requirement or purpose for the process step.
     */
    private InternationalString rationale;

    /**
     * Date and time or range of date and time on or over which the process step occurred,
     * in milliseconds ellapsed since January 1st, 1970. If there is no such date, then this
     * field is set to the special value {@link Long#MIN_VALUE}.
     */
    private long date;

    /**
     * Identification of, and means of communication with, person(s) and
     * organization(s) associated with the process step.
     */
    private Collection<ResponsibleParty> processors;

    /**
     * Information about the source data used in creating the data specified by the scope.
     */
    private Collection<Source> sources;

    /**
     * Creates an initially empty process step.
     */
    public ProcessStepImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ProcessStepImpl(final ProcessStep source) {
        super(source);
    }

    /**
     * Creates a process step initialized to the given description.
     */
    public ProcessStepImpl(final InternationalString description) {
        setDescription(description);
    }

     /**
     * Returns the description of the event, including related parameters or tolerances.
     */
    public InternationalString getDescription() {
        return description;
    }

    /**
     * Set the description of the event, including related parameters or tolerances.
     */
    public synchronized void setDescription(final InternationalString newValue) {
        checkWritePermission();
        description = newValue;
    }

    /**
     * Returns the requirement or purpose for the process step.
     */
    public InternationalString getRationale() {
        return rationale;
    }

    /**
     * Set the requirement or purpose for the process step.
     */
    public synchronized void setRationale(final InternationalString newValue) {
        checkWritePermission();
        rationale = newValue;
    }

    /**
     * Returns the date and time or range of date and time on or over which
     * the process step occurred.
     */
    public synchronized Date getDate() {
        return (date!=Long.MIN_VALUE) ? new Date(date) : null;
    }

    /**
     * Set the date and time or range of date and time on or over which the process
     * step occurred.
     */
    public synchronized void setDate(final Date newValue) {
        checkWritePermission();
        date = (newValue!=null) ? newValue.getTime() : Long.MIN_VALUE;
    }

    /**
     * Returns the identification of, and means of communication with, person(s) and
     * organization(s) associated with the process step.
     */
    public synchronized Collection<ResponsibleParty> getProcessors() {
        return (processors = nonNullCollection(processors, ResponsibleParty.class));
    }

    /**
     * Identification of, and means of communication with, person(s) and
     * organization(s) associated with the process step.
     */
    public synchronized void setProcessors(final Collection<? extends ResponsibleParty> newValues) {
        processors = copyCollection(newValues, processors, ResponsibleParty.class);
    }

    /**
     * Returns the information about the source data used in creating the data specified
     * by the scope.
     */
    public synchronized Collection<Source> getSources() {
        return (sources = nonNullCollection(sources, Source.class));
    }

    /**
     * Information about the source data used in creating the data specified by the scope.
     */
    public synchronized void setSources(final Collection<? extends Source> newValues) {
        sources = copyCollection(newValues, sources, Source.class);
    }
}
