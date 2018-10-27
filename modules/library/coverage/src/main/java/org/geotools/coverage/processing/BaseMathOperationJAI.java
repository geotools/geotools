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

import it.geosolutions.jaiext.JAIExt;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.media.jai.OperationDescriptor;
import javax.media.jai.registry.RenderedRegistryMode;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.ImagingParameterDescriptors;
import org.geotools.parameter.ImagingParameters;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;

/**
 * This class is the root class for the Maths operations. It provides basic capabilities for
 * management of geospatial parameters like {@link javax.media.jai.ROI}s and subsampling factors.
 *
 * @author Nicola Lagomarsini, GeoSolutions SAS
 * @since 14.x
 */
public abstract class BaseMathOperationJAI extends OperationJAI {

    private static final long serialVersionUID = 6830028735162290160L;

    /** {@link Logger} for this class. */
    public static final Logger LOGGER = Logging.getLogger(BaseMathOperationJAI.class);

    /** Name for the Sources parameter */
    public static final String SOURCES_NAME = "Sources";

    /** The parameter descriptor for the Sources. */
    public static final ParameterDescriptor SOURCES =
            new DefaultParameterDescriptor(
                    Citations.JAI,
                    SOURCES_NAME,
                    Collection.class, // Value class (mandatory)
                    null, // Array of valid values
                    null, // Default value
                    null, // Minimal value
                    null, // Maximal value
                    null, // Unit of measure
                    true);

    private static Set<ParameterDescriptor> REPLACED_DESCRIPTORS;

    static {
        final Set<ParameterDescriptor> replacedDescriptors = new HashSet<ParameterDescriptor>();
        replacedDescriptors.add(SOURCES);
        REPLACED_DESCRIPTORS = Collections.unmodifiableSet(replacedDescriptors);
    }

    /**
     * Constructor for {@link BaseMathOperationJAI}.
     *
     * @param operationDescriptor {@link OperationDescriptor} for the underlying JAI operation.
     */
    public BaseMathOperationJAI(OperationDescriptor operationDescriptor) {
        super(
                operationDescriptor,
                new ImagingParameterDescriptors(
                        getOperationDescriptor(operationDescriptor.getName()),
                        REPLACED_DESCRIPTORS));
    }

    /**
     * Constructor for {@link BaseMathOperationJAI}.
     *
     * @param operationDescriptor {@link OperationDescriptor} for the underlying JAI operation.
     * @param replacements {@link ImagingParameterDescriptors} that should replace the correspondent
     *     {@link ImagingParameters} in order to change the default behavior they have inside JAI.
     */
    public BaseMathOperationJAI(
            OperationDescriptor operationDescriptor, ImagingParameterDescriptors replacements) {
        super(
                operationDescriptor,
                new ImagingParameterDescriptors(
                        ImagingParameterDescriptors.properties(operationDescriptor),
                        operationDescriptor,
                        RenderedRegistryMode.MODE_NAME,
                        ImagingParameterDescriptors.DEFAULT_SOURCE_TYPE_MAP,
                        REPLACED_DESCRIPTORS));
    }

    /**
     * Constructor for {@link BaseMathOperationJAI}.
     *
     * @param name of the underlying JAI operation.
     */
    public BaseMathOperationJAI(String name, OperationDescriptor operationDescriptor) {
        super(
                getOperationDescriptor(JAIExt.getOperationName(name)),
                new ExtendedImagingParameterDescriptors(
                        name,
                        operationDescriptor,
                        new HashSet<ParameterDescriptor>(REPLACED_DESCRIPTORS)));
    }

    /**
     * Constructor for {@link BaseMathOperationJAI}.
     *
     * @param name of the underlying JAI operation.
     */
    public BaseMathOperationJAI(String name) {
        super(
                getOperationDescriptor(name),
                new ImagingParameterDescriptors(
                        getOperationDescriptor(name),
                        new HashSet<ParameterDescriptor>(REPLACED_DESCRIPTORS)));
    }

    protected void extractSources(
            final ParameterValueGroup parameters,
            final Collection<GridCoverage2D> sources,
            final String[] sourceNames)
            throws ParameterNotFoundException, InvalidParameterValueException {
        if (!JAIExt.isJAIExtOperation(JAIExt.getOperationName(getName()))) {
            super.extractSources(parameters, sources, sourceNames);
        } else {
            Utilities.ensureNonNull("parameters", parameters);
            Utilities.ensureNonNull("sources", sources);

            // Extraction of the sources from the parameters
            Object srcCoverages = parameters.parameter("Sources").getValue();

            if (!(srcCoverages instanceof Collection)
                    || ((Collection) srcCoverages).isEmpty()
                    || !(((Collection) srcCoverages).iterator().next() instanceof GridCoverage2D)) {
                throw new InvalidParameterValueException(
                        Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, "sources"),
                        "sources",
                        srcCoverages);
            }
            // Collection of the sources to use
            Collection<GridCoverage2D> sourceCoverages = (Collection<GridCoverage2D>) srcCoverages;
            sources.addAll(sourceCoverages);
        }
    }
}
