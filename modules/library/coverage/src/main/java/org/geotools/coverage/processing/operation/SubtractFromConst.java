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
import javax.media.jai.operator.SubtractFromConstDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.util.NumberRange;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

// Geotools dependencies

/**
 * Subtracts every sample values of the source coverage from constants (one for each band). If the
 * number of constants supplied is less than the number of bands of the destination, then the
 * constant from entry 0 is applied to all the bands. Otherwise, a constant from a different entry
 * is applied to each band.
 *
 * <p><STRONG>Name:</STRONG>&nbsp;<CODE>"SubtractFromConst"</CODE><br>
 * <STRONG>JAI operator:</STRONG>&nbsp;<CODE>
 * "{@linkplain SubtractFromConstDescriptor SubtractFromConst}"</CODE><br>
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
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 * </table>
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @see org.geotools.coverage.processing.Operations#subtractFrom
 * @see SubtractFromConstDescriptor
 * @todo Should operates on {@code sampleToGeophysics} transform when possible. See <A
 *     HREF="http://jira.codehaus.org/browse/GEOT-610">GEOT-610</A>.
 */
public class SubtractFromConst extends OperationJAI {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 6941277637814235474L;

    /** Constructs a default {@code "SubtractFromConst"} operation. */
    public SubtractFromConst() {
        super(
                "SubtractFromConst",
                getOperationDescriptor(JAIExt.getOperationName("SubtractFromConst")));
    }

    public String getName() {
        return "SubtractFromConst";
    }

    /** Returns the expected range of values for the resulting image. */
    protected NumberRange deriveRange(final NumberRange[] ranges, final Parameters parameters) {
        final double[] constants = (double[]) parameters.parameters.getObjectParameter("constants");
        if (constants.length == 1) {
            final double c = constants[0];
            final NumberRange range = ranges[0];
            final double min = c - range.getMaximum();
            final double max = c - range.getMinimum();
            return NumberRange.create(min, max);
        }
        return super.deriveRange(ranges, parameters);
    }

    protected void handleJAIEXTParams(
            ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        GridCoverage2D source = (GridCoverage2D) parameters2.parameter("source0").getValue();
        if (JAIExt.isJAIExtOperation("operationConst")) {
            parameters.set(Operator.SUBTRACT_FROM, 1);
        }
        handleROINoDataInternal(parameters, source, "operationConst", 2, 3);
    }

    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform gridToCRS,
            GridCoverage2D[] sources,
            Parameters parameters) {
        return handleROINoDataProperties(
                null, parameters.parameters, sources[0], "operationConst", 2, 3, 4);
    }
}
