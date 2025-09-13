/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing.operation;

// JAI dependencies (for javadoc)

import java.awt.image.RenderedImage;
import java.io.Serial;
import java.util.Collection;
import java.util.Map;
import org.eclipse.imagen.ParameterBlockJAI;
import org.eclipse.imagen.media.algebra.AlgebraDescriptor.Operator;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.util.InternationalString;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseMathOperationJAI;
import org.geotools.util.NumberRange;

// Geotools dependencies

/**
 * Takes the natural logarithm of the sample values of a coverage.
 *
 * <p><STRONG>Name:</STRONG>&nbsp;<CODE>"Log"</CODE><br>
 * <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain LogDescriptor Log}"</CODE><br>
 * <STRONG>Parameters:</STRONG>
 *
 * <table border='3' cellpadding='6' bgcolor='F4F8FF'>
 *   <tr bgcolor='#B9DCFF'>
 *     <th>Name</th>
 *     <th>Class</th>
 *     <th>Default value</th>
 *     <th>Minimum value</th>
 *     <th>Maximum value</th>
 *   </tr>
 *   <tr>
 *     <td>{@code "Source"}</td>
 *     <td>{@link org.geotools.coverage.grid.GridCoverage2D}</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 * </table>
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @see org.geotools.coverage.processing.Operations#log
 * @see LogDescriptor
 */
public class Log extends BaseMathOperationJAI {
    /** Serial number for interoperability with different versions. */
    @Serial
    private static final long serialVersionUID = -3622176942444895367L;

    private static final String ALGEBRIC = "algebric";

    /** Constructs a default {@code "Log"} operation. */
    public Log() {
        super("Log", getOperationDescriptor(ALGEBRIC));
    }

    @Override
    public String getName() {
        return "Log";
    }

    /** Returns the expected range of values for the resulting image. */
    @Override
    protected NumberRange<? extends Number> deriveRange(
            final NumberRange<? extends Number>[] ranges, final Parameters parameters) {
        final NumberRange range = ranges[0];
        final double min = Math.log(range.getMinimum());
        final double max = Math.log(range.getMaximum());
        return NumberRange.create(min, max);
    }

    @Override
    protected void handleJAIEXTParams(ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        parameters.set(Operator.LOG, 0);
        @SuppressWarnings("unchecked")
        Collection<GridCoverage2D> sources =
                (Collection<GridCoverage2D>) parameters2.parameter("sources").getValue();
        for (GridCoverage2D source : sources) {
            handleROINoDataInternal(parameters, source, "algebric", 1, 2);
        }
    }

    @Override
    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform gridToCRS,
            GridCoverage2D[] sources,
            Parameters parameters) {
        return handleROINoDataProperties(null, parameters.parameters, sources[0], "algebric", 1, 2, 3);
    }
}
