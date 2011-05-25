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
package org.geotools.metadata.iso.content;

import org.opengis.metadata.content.RangeDimension;
import org.opengis.util.InternationalString;
import org.opengis.util.MemberName;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Information on the range of each dimension of a cell measurement value.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class RangeDimensionImpl extends MetadataEntity implements RangeDimension {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 4365956866782010460L;

    /**
     * Number that uniquely identifies instances of bands of wavelengths on which a sensor
     * operates.
     */
    private MemberName sequenceIdentifier;

    /**
     * Description of the range of a cell measurement value.
     */
    private InternationalString descriptor;

    /**
     * Constructs an initially empty range dimension.
     */
    public RangeDimensionImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public RangeDimensionImpl(final RangeDimension source) {
        super(source);
    }

    /**
     * Returns the number that uniquely identifies instances of bands of wavelengths
     * on which a sensor operates.
     * 
     * @TODO: needs to annotate the package org.geotools.util before.
     */
    public MemberName getSequenceIdentifier() {
        return sequenceIdentifier;
    }

    /**
     * Set the number that uniquely identifies instances of bands of wavelengths
     * on which a sensor operates.
     */
    public synchronized void setSequenceIdentifier(final MemberName newValue) {
        checkWritePermission();
        sequenceIdentifier = newValue;
    }

    /**
     * Returns the description of the range of a cell measurement value.
     */
    public InternationalString getDescriptor() {
        return descriptor;
    }

    /**
     * Set the description of the range of a cell measurement value.
     */
    public synchronized void setDescriptor(final InternationalString newValue) {
        checkWritePermission();
        descriptor = newValue;
    }
}
