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
import org.opengis.metadata.distribution.Distributor;
import org.opengis.metadata.distribution.Format;
import org.opengis.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Description of the computer language construct that specifies the representation
 * of data objects in a record, file, message, storage device or transmission channel.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class FormatImpl extends MetadataEntity implements Format {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 6498897239493553607L;

    /**
     * Name of the data transfer format(s).
     */
    private InternationalString name;

    /**
     * Version of the format (date, number, etc.).
     */
    private InternationalString version;

    /**
     * Amendment number of the format version.
     */
    private InternationalString amendmentNumber;

    /**
     * Name of a subset, profile, or product specification of the format.
     */
    private InternationalString specification;

    /**
     * Recommendations of algorithms or processes that can be applied to read or
     * expand resources to which compression techniques have been applied.
     */
    private InternationalString fileDecompressionTechnique;

    /**
     * Provides information about the distributors format.
     */
    private Collection<Distributor> formatDistributors;

    /**
     * Constructs an initially empty format.
     */
    public FormatImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public FormatImpl(final Format source) {
        super(source);
    }

    /**
     * Creates a format initialized to the given name.
     */
    public FormatImpl(final InternationalString name, final InternationalString version) {
        setName   (name   );
        setVersion(version);
    }

    /**
     * Returns the name of the data transfer format(s).
     */
    public InternationalString getName() {
        return name;
    }

    /**
     * Set the name of the data transfer format(s).
     */
    public synchronized void setName(final InternationalString newValue) {
         checkWritePermission();
         name = newValue;
     }

    /**
     * Returne the version of the format (date, number, etc.).
     */
    public InternationalString getVersion() {
        return version;
    }

    /**
     * Set the version of the format (date, number, etc.).
     */
    public synchronized void setVersion(final InternationalString newValue) {
        checkWritePermission();
        version = newValue;
    }

    /**
     * Returns the amendment number of the format version.
     */
    public InternationalString getAmendmentNumber() {
        return amendmentNumber;
    }

    /**
     * Set the amendment number of the format version.
     */
    public synchronized void setAmendmentNumber(final InternationalString newValue) {
        checkWritePermission();
        amendmentNumber = newValue;
    }

    /**
     * Returns the name of a subset, profile, or product specification of the format.
     */
    public InternationalString getSpecification() {
        return specification;
    }

    /**
     * Set the name of a subset, profile, or product specification of the format.
     */
    public synchronized void setSpecification(final InternationalString newValue) {
        checkWritePermission();
        specification = newValue;
    }

    /**
     * Returns recommendations of algorithms or processes that can be applied to read or
     * expand resources to which compression techniques have been applied.
     */
    public InternationalString getFileDecompressionTechnique() {
        return fileDecompressionTechnique;
    }

    /**
     * Set recommendations of algorithms or processes that can be applied to read or
     * expand resources to which compression techniques have been applied.
     */
    public synchronized void setFileDecompressionTechnique(final InternationalString newValue) {
        checkWritePermission();
        fileDecompressionTechnique = newValue;
    }

    /**
     * Provides information about the distributors format.
     */
    public synchronized Collection<Distributor> getFormatDistributors() {
        return (formatDistributors = nonNullCollection(formatDistributors, Distributor.class));
    }

    /**
     * Set information about the distributors format.
     */
    public synchronized void setFormatDistributors(
            final Collection<? extends Distributor> newValues)
    {
        formatDistributors = copyCollection(newValues, formatDistributors, Distributor.class);
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
///       xmlMarshalling(false);
///    }
}
