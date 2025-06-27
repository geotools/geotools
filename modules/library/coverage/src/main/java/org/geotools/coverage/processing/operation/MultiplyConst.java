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
import javax.media.jai.operator.MultiplyConstDescriptor;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.util.InternationalString;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.util.NumberRange;

/**
 * Multiplies every sample values of the source coverage by constants (one for each band). If the number of constants
 * supplied is less than the number of bands of the destination, then the constant from entry 0 is applied to all the
 * bands. Otherwise, a constant from a different entry is applied to each band.
 *
 * <p><STRONG>Name:</STRONG>&nbsp;<CODE>"MultiplyConst"</CODE><br>
 * <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain MultiplyConstDescriptor MultiplyConst}"
 * </CODE><br>
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
 *     <td align="center">1.0</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 * </table>
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @see org.geotools.coverage.processing.Operations#multiply(org.geotools.api.coverage.Coverage, double[])
 * @see MultiplyConstDescriptor
 * @todo Should operates on {@code sampleToGeophysics} transform when possible. See <A
 *     HREF="http://jira.codehaus.org/browse/GEOT-610">GEOT-610</A>.
 */
public class MultiplyConst extends OperationJAI {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -1077713495023498436L;

    /** Constructs a default {@code "MultiplyConst"} operation. */
    public MultiplyConst() {
        super("MultiplyConst", getOperationDescriptor(JAIExt.getOperationName("MultiplyConst")));
    }

    @Override
    public String getName() {
        return "MultiplyConst";
    }

    /** Returns the expected range of values for the resulting image. */
    @Override
    protected NumberRange<? extends Number> deriveRange(
            final NumberRange<? extends Number>[] ranges, final Parameters parameters) {
        final double[] constants = (double[]) parameters.parameters.getObjectParameter("constants");
        if (constants.length == 1) {
            final double c = constants[0];
            final NumberRange range = ranges[0];
            final double min = range.getMinimum() * c;
            final double max = range.getMaximum() * c;
            return max < min ? NumberRange.create(max, min) : NumberRange.create(min, max);
        }
        return super.deriveRange(ranges, parameters);
    }

    @Override
    protected void handleJAIEXTParams(ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        GridCoverage2D source =
                (GridCoverage2D) parameters2.parameter("source0").getValue();
        if (JAIExt.isJAIExtOperation("operationConst")) {
            parameters.set(Operator.MULTIPLY, 1);
        }
        handleROINoDataInternal(parameters, source, "operationConst", 2, 3);
    }

    @Override
    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform gridToCRS,
            GridCoverage2D[] sources,
            Parameters parameters) {
        return handleROINoDataProperties(null, parameters.parameters, sources[0], "operationConst", 2, 3, 4);
    }
}
