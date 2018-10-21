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
import java.util.Collection;
import java.util.Map;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.operator.AddDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseMathOperationJAI;
import org.geotools.util.NumberRange;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

/**
 * Create a new coverage as the sum of two source coverages by doing pixel by pixel addition:
 * result[0][0] = source0[0][0] + source1[0][0] ... ... result[i][j] = source0[i][j] + source1[i][j]
 * ... ... result[n-1][m-1] = source0[n-1][m-1] + source1[n-1][m-1]
 *
 * <p>Make sure coverages have same envelope and same resolution before using this operation.
 *
 * <p><STRONG>Name:</STRONG>&nbsp;<CODE>"Add"</CODE><br>
 * <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain AddDescriptor Add}"</CODE><br>
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
 *     <td>{@code "Source0"}</td>
 *     <td>{@link org.geotools.coverage.grid.GridCoverage2D}</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "Source1"}</td>
 *     <td>{@link org.geotools.coverage.grid.GridCoverage2D}</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 * </table>
 *
 * @since 8.x
 * @see org.geotools.coverage.processing.Operations#add(org.opengis.coverage.Coverage,
 *     org.opengis.coverage.Coverage)
 * @see Add
 */
public class Add extends BaseMathOperationJAI {

    private static final String ALGEBRIC = "algebric";
    private static final String ADD = "Add";
    /** */
    private static final long serialVersionUID = -4029879745691129215L;

    /** Constructs a default {@code "Add"} operation. */
    public Add() {
        super(ADD, getOperationDescriptor(JAIExt.getOperationName(ADD)));
    }

    public String getName() {
        return ADD;
    }

    /** Returns the expected range of values for the resulting image. */
    protected NumberRange deriveRange(final NumberRange[] ranges, final Parameters parameters) {

        // Note that they will not be exact ranges since this will require really computing
        // the pixel by pixel operation
        if (ranges != null && ranges.length == 2) {
            final NumberRange range0 = ranges[0];
            final NumberRange range1 = ranges[1];
            final double min0 = range0.getMinimum();
            final double min1 = range1.getMinimum();
            final double max0 = range0.getMaximum();
            final double max1 = range1.getMaximum();
            final double max = max0 + max1;
            final double min = min0 + min1;
            return NumberRange.create(min, max);
        }
        return null;
    }

    protected void handleJAIEXTParams(
            ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        if (JAIExt.isJAIExtOperation(ALGEBRIC)) {
            parameters.set(Operator.SUM, 0);
            Collection<GridCoverage2D> sources =
                    (Collection<GridCoverage2D>) parameters2.parameter("sources").getValue();
            for (GridCoverage2D source : sources) {
                handleROINoDataInternal(parameters, source, ALGEBRIC, 1, 2);
            }
        }
    }

    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform gridToCRS,
            GridCoverage2D[] sources,
            Parameters parameters) {
        return handleROINoDataProperties(
                null, parameters.parameters, sources[0], ALGEBRIC, 1, 2, 3);
    }
}
