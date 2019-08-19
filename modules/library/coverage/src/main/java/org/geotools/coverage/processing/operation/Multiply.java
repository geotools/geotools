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
import javax.media.jai.operator.MultiplyDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseMathOperationJAI;
import org.geotools.util.NumberRange;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

/**
 * Create a new coverage as the multiplication of two source coverages by doing pixel by pixel
 * multiplication: result[0][0] = source0[0][0] * source1[0][0] ... ... result[i][j] = source0[i][j]
 * * source1[i][j] ... ... result[n-1][m-1] = source0[n-1][m-1] * source1[n-1][m-1]
 *
 * <p>Make sure coverages have same envelope and same resolution before using this operation.
 *
 * <p><STRONG>Name:</STRONG>&nbsp;<CODE>"Multiply"</CODE><br>
 * <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain MultiplyDescriptor Multiply}"</CODE><br>
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
 * @see org.geotools.coverage.processing.Operations#multiply(org.opengis.coverage.Coverage,
 *     org.opengis.coverage.Coverage)
 * @see Multiply
 */
public class Multiply extends BaseMathOperationJAI {

    /** */
    private static final long serialVersionUID = 3559075474256896861L;

    /** Constructs a default {@code "MultiplyConst"} operation. */
    public Multiply() {
        super("Multiply", getOperationDescriptor(JAIExt.getOperationName("Multiply")));
    }

    public String getName() {
        return "Multiply";
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
            final double max = max0 * max1;
            final double min = min0 * min1;
            return NumberRange.create(min, max);
        }
        return null;
    }

    protected void handleJAIEXTParams(
            ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        if (JAIExt.isJAIExtOperation("algebric")) {
            parameters.set(Operator.MULTIPLY, 0);
            Collection<GridCoverage2D> sources =
                    (Collection<GridCoverage2D>) parameters2.parameter("sources").getValue();
            for (GridCoverage2D source : sources) {
                handleROINoDataInternal(parameters, source, "algebric", 1, 2);
            }
        }
    }

    @Override
    protected void extractSources(
            ParameterValueGroup parameters,
            Collection<GridCoverage2D> sources,
            String[] sourceNames)
            throws ParameterNotFoundException, InvalidParameterValueException {
        try {
            Collection<GridCoverage2D> paramSources =
                    (Collection<GridCoverage2D>) parameters.parameter("Sources").getValue();
            if (paramSources.size() >= 2) {
                sources.addAll(paramSources);
                return;
            }
        } catch (ParameterNotFoundException e) {
            // sources parameter from jai ext not set, try to continue?
        }
        super.extractSources(parameters, sources, sourceNames);
    }

    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform gridToCRS,
            GridCoverage2D[] sources,
            Parameters parameters) {
        return handleROINoDataProperties(
                null, parameters.parameters, sources[0], "algebric", 1, 2, 3);
    }
}
