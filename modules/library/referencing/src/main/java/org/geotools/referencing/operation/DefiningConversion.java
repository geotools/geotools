/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation;

import java.util.Collections;
import java.util.Map;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.OperationMethod;
import org.geotools.referencing.wkt.Formatter;

/**
 * A conversion used for the definition of a {@linkplain org.geotools.api.referencing.crs.GeneralDerivedCRS derived CRS}
 * (including projections). This conversion has no source and target CRS, and no math transform. Those elements are
 * created by the derived CRS itself.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Matthias Basler
 * @see org.geotools.api.referencing.operation.CoordinateOperationFactory#createDefiningConversion
 */
public class DefiningConversion extends DefaultConversion {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 7399026512478064721L;

    /** The parameter values. */
    private final ParameterValueGroup parameters;

    /**
     * Convenience constructor for creating a defining conversion with a default operation method. The operation method
     * is assumed two-dimensional.
     *
     * @param name The conversion name.
     * @param parameters The parameter values.
     * @since 2.2
     */
    public DefiningConversion(final String name, final ParameterValueGroup parameters) {
        this(Collections.singletonMap(NAME_KEY, name), getOperationMethod(parameters), parameters);
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database ("Relax constraint on placement of this()/super() call in
     * constructors").
     */
    private static OperationMethod getOperationMethod(final ParameterValueGroup parameters) {
        ensureNonNull("parameters", parameters);
        final ParameterDescriptorGroup descriptor = parameters.getDescriptor();
        return new DefaultOperationMethod(getProperties(descriptor, null), 2, 2, descriptor);
    }

    /**
     * Constructs a conversion from a set of parameters. The properties given in argument follow the same rules than for
     * the {@link AbstractCoordinateOperation} constructor.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param method The operation method.
     * @param parameters The parameter values.
     */
    public DefiningConversion(
            final Map<String, ?> properties, final OperationMethod method, final ParameterValueGroup parameters) {
        super(properties, null, null, null, method);
        ensureNonNull("parameters", parameters);
        this.parameters = parameters.clone();
    }

    /**
     * Constructs a conversion from a math transform. The properties given in argument follow the same rules than for
     * the {@link AbstractCoordinateOperation} constructor.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param method The operation method.
     * @param transform Transform from positions in the {@linkplain #getSourceCRS source CRS} to positions in the
     *     {@linkplain #getTargetCRS target CRS}.
     * @since 2.5
     */
    public DefiningConversion(
            final Map<String, ?> properties, final OperationMethod method, final MathTransform transform) {
        super(properties, null, null, transform, method);
        parameters = null;
    }

    /**
     * Invoked by the super-class constructor for checking argument validity. This special kind of conversion accepts
     * non-null {@code transform} even if {@code sourceCRS} and {@code targetCRS} are non-null.
     */
    @Override
    void validate() throws IllegalArgumentException {
        if (transform == null) {
            super.validate();
        }
    }

    /** Returns the parameter values. */
    @Override
    public ParameterValueGroup getParameterValues() {
        return parameters != null ? parameters.clone() : super.getParameterValues();
    }

    /** {@inheritDoc} */
    @Override
    protected String formatWKT(final Formatter formatter) {
        final String name = super.formatWKT(formatter);
        formatter.append(parameters);
        return name;
    }
}
