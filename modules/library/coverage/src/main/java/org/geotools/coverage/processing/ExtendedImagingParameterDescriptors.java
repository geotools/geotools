/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import java.util.Collection;
import javax.media.jai.RegistryElementDescriptor;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.ImagingParameterDescriptors;
import org.geotools.referencing.NamedIdentifier;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.referencing.ReferenceIdentifier;

/**
 * Extension of the {@link ImagingParameterDescriptors} class used for setting the right operation
 * name for the operation associated to the input {@link RegistryElementDescriptor}.
 *
 * @author Nicola Lagomarsini Geosolutions
 */
public class ExtendedImagingParameterDescriptors extends ImagingParameterDescriptors {

    private ReferenceIdentifier operationName;

    public ExtendedImagingParameterDescriptors(
            String operationName, RegistryElementDescriptor operation) {
        this(operationName, operation, null);
    }

    ExtendedImagingParameterDescriptors(
            String operationName,
            RegistryElementDescriptor operation,
            Collection<ParameterDescriptor> extension) {
        super(operation, extension);
        this.operationName = new NamedIdentifier(Citations.JAI, operationName);
    }

    @Override
    public ReferenceIdentifier getName() {
        return operationName;
    }
}
