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

import it.geosolutions.jaiext.JAIExt;
import it.geosolutions.jaiext.algebra.AlgebraDescriptor.Operator;
import java.awt.image.RenderedImage;
import java.util.Map;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.operator.AddConstDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.util.NumberRange;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

// Geotools dependencies

/**
 * Adds constants (one for each band) to every sample values of the source coverage. If the number
 * of constants supplied is less than the number of bands of the destination, then the constant from
 * entry 0 is applied to all the bands. Otherwise, a constant from a different entry is applied to
 * each band.
 *
 * <p><STRONG>Name:</STRONG>&nbsp;<CODE>"AddConst"</CODE><br>
 * <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain AddConstDescriptor AddConst}"</CODE><br>
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
 *   <tr>
 *     <td>{@code "constants"}</td>
 *     <td>{@code double[]}</td>
 *     <td align="center">0.0</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 * </table>
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @see org.geotools.coverage.processing.Operations#add(org.opengis.coverage.Coverage, double[])
 * @see AddConstDescriptor
 * @todo Should operates on {@code sampleToGeophysics} transform when possible. See <A
 *     HREF="http://jira.codehaus.org/browse/GEOT-610">GEOT-610</A>.
 */
public class AddConst extends OperationJAI {
    private static final String OPERATION_CONST = "operationConst";
    private static final String ADD_CONST = "AddConst";
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 5443686039059774671L;

    /** Constructs a default {@code "AddConst"} operation. */
    public AddConst() {
        super(ADD_CONST, getOperationDescriptor(JAIExt.getOperationName(ADD_CONST)));
    }

    public String getName() {
        return ADD_CONST;
    }

    /** Returns the expected range of values for the resulting image. */
    protected NumberRange deriveRange(final NumberRange[] ranges, final Parameters parameters) {
        final double[] constants = (double[]) parameters.parameters.getObjectParameter("constants");
        if (constants.length == 1) {
            final double c = constants[0];
            final NumberRange range = ranges[0];
            final double min = range.getMinimum() + c;
            final double max = range.getMaximum() + c;
            return NumberRange.create(min, max);
        }
        return super.deriveRange(ranges, parameters);
    }

    protected void handleJAIEXTParams(
            ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        GridCoverage2D source = (GridCoverage2D) parameters2.parameter("source0").getValue();
        if (JAIExt.isJAIExtOperation(OPERATION_CONST)) {
            parameters.set(Operator.SUM, 1);
        }
        handleROINoDataInternal(parameters, source, OPERATION_CONST, 2, 3);
    }

    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform gridToCRS,
            GridCoverage2D[] sources,
            Parameters parameters) {
        return handleROINoDataProperties(
                null, parameters.parameters, sources[0], OPERATION_CONST, 2, 3, 4);
    }
}
