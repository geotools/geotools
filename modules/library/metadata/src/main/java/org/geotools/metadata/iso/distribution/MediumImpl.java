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
import javax.measure.unit.Unit;
import org.opengis.metadata.distribution.Medium;
import org.opengis.metadata.distribution.MediumFormat;
import org.opengis.metadata.distribution.MediumName;
import org.opengis.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Information about the media on which the resource can be distributed.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class MediumImpl extends MetadataEntity implements Medium {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -2838122926367921673L;

    /**
     * Name of the medium on which the resource can be received.
     */
    private MediumName name;

    /**
     * Density at which the data is recorded.
     * Returns {@code null} if unknown.
     * If non-null, then the number should be greater than zero.
     */
    private Collection<Double> densities;

    /**
     * Units of measure for the recording density.
     */
    private Unit densityUnits;

    /**
     * Number of items in the media identified.
     * Returns {@code null} if unknown.
     */
    private Integer volumes;

    /**
     * Methods used to write to the medium.
     */
    private Collection<MediumFormat> mediumFormats;

    /**
     * Description of other limitations or requirements for using the medium.
     */
    private InternationalString mediumNote;

    /**
     * Constructs an initially empty medium.
     */
    public MediumImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public MediumImpl(final Medium source) {
        super(source);
    }

    /**
     * Returns the name of the medium on which the resource can be received.
     */
    public MediumName getName() {
        return name;
    }

    /**
     * Set the name of the medium on which the resource can be received.
     */
    public synchronized void setName(final MediumName newValue) {
        checkWritePermission();
        name = newValue;
    }

    /**
     * Returns the units of measure for the recording density.
     */
    public Unit getDensityUnits() {
        return densityUnits;
    }

    /**
     * Set the units of measure for the recording density.
     */
    public synchronized void setDensityUnits(final Unit newValue) {
        checkWritePermission();
        densityUnits = newValue;
    }

    /**
     * Returns the number of items in the media identified.
     * Returns {@code null} if unknown.
     */
    public Integer getVolumes() {
        return volumes;
    }

    /**
     * Set the number of items in the media identified.
     * Returns {@code null} if unknown.
     */
    public synchronized void setVolumes(final Integer newValue) {
        checkWritePermission();
        volumes = newValue;
    }

    /**
     * Returns the method used to write to the medium.
     */
    public synchronized Collection<MediumFormat> getMediumFormats() {
        return (mediumFormats = nonNullCollection(mediumFormats, MediumFormat.class));
    }

    /**
     * Set the method used to write to the medium.
     */
    public synchronized void setMediumFormats(final Collection<? extends MediumFormat> newValues) {
        mediumFormats = copyCollection(newValues, mediumFormats, MediumFormat.class);
    }

    /**
     * Returns a description of other limitations or requirements for using the medium.
     */
    public InternationalString getMediumNote() {
        return mediumNote;
    }

    /**
     * Set a description of other limitations or requirements for using the medium.
     */
    public synchronized void setMediumNote(final InternationalString newValue) {
        checkWritePermission();
        mediumNote = newValue;
    }

    /**
     * Returns the density at which the data is recorded.
     * The numbers should be greater than zero.
     */
    public synchronized Collection<Double> getDensities() {
        return (densities = nonNullCollection(densities, Double.class));
    }

    /**
     * Set density at which the data is recorded.
     * The numbers should be greater than zero.
     */
    public synchronized void setDensities(final Collection<? extends Double> newValues) {
        densities = copyCollection(newValues, densities, Double.class);
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
