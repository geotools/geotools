/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.HashMap;
import java.util.Map;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.ViewType;
import org.geotools.factory.Hints;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.IdentifiedObject;


/**
 * An operation working on {@link GridCoverage2D} sources.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class Operation2D extends AbstractOperation {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 574096338873406394L;

    /**
     * Index of the source {@link GridCoverage2D} to use as a model. The destination grid coverage
     * will reuse the same coordinate reference system, envelope and qualitative categories than
     * this primary source.
     * <p>
     * For operations expecting only one source, there is no ambiguity. But for operations
     * expecting more than one source, the choice of a primary source is somewhat arbitrary.
     * This constant is used merely as a flag for spotting those places in the code.
     *
     * @since 2.4
     */
    protected static final int PRIMARY_SOURCE_INDEX = 0;

    /**
     * Convenience constant for the first source {@link GridCoverage2D}. The parameter name
     * is {@code "Source"} (as specified in OGC implementation specification) and the alias
     * is {@code "source0"} (for compatibility with <cite>Java Advanced Imaging</cite>).
     */
    public static final ParameterDescriptor SOURCE_0;
    static {
        final Map<String,Object> properties = new HashMap<String,Object>(4);
        properties.put(IdentifiedObject.NAME_KEY,  new NamedIdentifier(Citations.OGC, "Source"));
        properties.put(IdentifiedObject.ALIAS_KEY, new NamedIdentifier(Citations.JAI, "source0"));
        SOURCE_0 = new DefaultParameterDescriptor(properties, GridCoverage2D.class,
                        null, null, null, null, null, true);
    }

    /**
     * Constructs an operation. The operation name will be the same than the
     * parameter descriptor name.
     *
     * @param descriptor The parameters descriptor.
     */
    public Operation2D(final ParameterDescriptorGroup descriptor) {
        super(descriptor);
    }

    /**
     * Returns {@code true} if grid coverage content should be converted from sample values
     * to geophysics value before to apply an operation. This method is invoked automatically
     * by {@link OperationJAI#doOperation doOperation}. If this method returns {@code true},
     * then the computation will be performed on the <cite>geophysics</cite> view as returned
     * by <code>{@linkplain GridCoverage2D#geophysics GridCoverage2D.geophysics}(true)</code>.
     * If this method returns {@code false}, then the view will <strong>not</strong> be changed
     * before the operation is applied (i.e. the {@code geophysics} method is not invoked at all).
     * The default implementation always returns {@code true}.
     *
     * @param  parameters The parameters supplied by the user to the {@code doOperation} method.
     * @return {@code true} if this operation should be applied on geophysics values.
     *
     * @see GridCoverage2D#geophysics
     *
     * @since 2.4
     */
    protected boolean computeOnGeophysicsValues(final ParameterValueGroup parameters) {
        return true;
    }

    /**
     * Extracts and prepares the sources for this {@code Operation2D}, taking into account the
     * need for going to the geophysics view of the data in case this operation requires so.
     * <p>
     * This method fills the {@code sources} array with needed sources, changing to their
     * geophysics view if needed.
     *
     * @param parameters  Parameters that will control this operation.
     * @param sourceNames Names of the sources to extract from {@link ParameterValueGroup}.
     * @param sources     On input, an array with the same length than {@code sourceNames}.
     *                    On output, the {@link GridCoverage2D} to be used as sources for
     *                    this operation.
     * @return            The type of the {@linkplain #PRIMARY_SOURCE_INDEX primary source},
     *                    or {@code null} if unknow or if the type should not be changed.
     *
     * @throws IllegalArgumentException
     *                  if an argument is {@code null}, or if {@code sources} and
     *                  {@code sourceNames} doesn't have length.
     * @throws ParameterNotFoundException
     *                  if a required source has not been found.
     * @throws InvalidParameterValueException
     *                  if a source doesn't contain a value of type {@link GridCoverage2D}.
     *
     * @since 2.4
     */
    protected ViewType extractSources(final ParameterValueGroup parameters,
                                      final String[]            sourceNames,
                                      final GridCoverage2D[]    sources)
            throws ParameterNotFoundException, InvalidParameterValueException
    {
        Utilities.ensureNonNull("parameters",  parameters);
        Utilities.ensureNonNull("sourceNames", sourceNames);
        Utilities.ensureNonNull("sources",     sources);
        if (sources.length != sourceNames.length) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.MISMATCHED_ARRAY_LENGTH));
        }
        ViewType type = null;
        final boolean computeOnGeophysicsValues = computeOnGeophysicsValues(parameters);
        for (int i=0; i<sourceNames.length; i++) {
            Object candidate = parameters.parameter(sourceNames[i]).getValue();
            if (candidate == null) {
                // assume it is an optional parameter
                continue;
            }
            if (!(candidate instanceof GridCoverage2D)) {
                throw new InvalidParameterValueException(Errors.format(ErrorKeys.ILLEGAL_CLASS_$2,
                        Classes.getClass(candidate), GridCoverage2D.class), sourceNames[i], candidate);
            }
            GridCoverage2D source = (GridCoverage2D) candidate;
            if (computeOnGeophysicsValues) {
                final GridCoverage2D old = source;
                source = source.view(ViewType.GEOPHYSICS);
                if (i == PRIMARY_SOURCE_INDEX) {
                    type = (old == source) ? ViewType.GEOPHYSICS : ViewType.PACKED;
                }
            }
            sources[i] = source;
        }
        return type;
    }

    /**
     * Returns the factory to use for creating new {@link GridCoverage2D} objects.
     *
     * @since 2.2
     */
    protected static GridCoverageFactory getFactory(final Hints hints) {
        return CoverageFactoryFinder.getGridCoverageFactory(hints);
    }
}
