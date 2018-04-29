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
import javax.media.jai.operator.InvertDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseMathOperationJAI;
import org.geotools.util.NumberRange;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

// Geotools dependencies

/**
 * Inverts the sample values of a coverage. For source coverages with signed data types, the sample
 * values of the destination coverage are defined by the pseudocode:
 *
 * <BLOCKQUOTE>
 *
 * <PRE>
 * dst[x][y][b] = -src[x][y][b]
 * </PRE>
 *
 * </BLOCKQUOTE>
 *
 * <p><STRONG>Name:</STRONG>&nbsp;<CODE>"Invert"</CODE><br>
 * <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain InvertDescriptor Invert}"</CODE><br>
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
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @see org.geotools.coverage.processing.Operations#invert
 * @see InvertDescriptor
 */
public class Invert extends BaseMathOperationJAI {
    private static final String ALGEBRIC = "algebric";
    private static final String INVERT = "Invert";
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 7297641092994880308L;

    /** Constructs a default {@code "Invert"} operation. */
    public Invert() {
        super(INVERT, getOperationDescriptor(JAIExt.getOperationName(INVERT)));
    }

    public String getName() {
        return INVERT;
    }

    /** Returns the expected range of values for the resulting image. */
    protected NumberRange deriveRange(final NumberRange[] ranges, final Parameters parameters) {
        final NumberRange range = ranges[0];
        final double min = -range.getMaximum();
        final double max = -range.getMinimum();
        return NumberRange.create(min, max);
    }

    protected void handleJAIEXTParams(
            ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        if (JAIExt.isJAIExtOperation(ALGEBRIC)) {
            parameters.set(Operator.INVERT, 0);
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
