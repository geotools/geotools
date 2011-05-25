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
 */
package org.geotools.coverage.processing.operation;

import org.geotools.util.NumberRange;
import org.geotools.coverage.processing.OperationJAI;


/**
 * Maps the sample values of a coverage from one range to another range. The rescaling is done by
 * multiplying each sample value by one of a set of constants and then adding another constant to
 * the result of the multiplication. The destination sample values are defined by the pseudocode:
 *
 * <BLOCKQUOTE><CODE>
 * dst[<var>x</var>][<var>y</var>][<var>b</var>] =
 * src[<var>x</var>][<var>y</var>][<var>b</var>]*<strong>constant</strong> + <strong>offset</strong>;
 * </CODE></BLOCKQUOTE>
 *
 * <P><STRONG>Name:</STRONG>&nbsp;<CODE>"Rescale"</CODE><BR>
 *    <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain javax.media.jai.operator.RescaleDescriptor Rescale}"</CODE><BR>
 *    <STRONG>Parameters:</STRONG></P>
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
 *     <td><code>double[]</code></td>
 *     <td align="center">1.0</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "offsets"}</td>
 *     <td><code>double[]</code></td>
 *     <td align="center">0.0</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 * </table>
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see org.geotools.coverage.processing.Operations#rescale
 * @see javax.media.jai.operator.RescaleDescriptor
 *
 * @todo Should operates on {@code sampleToGeophysics} transform when possible.
 *       See <A HREF="http://jira.codehaus.org/browse/GEOT-610">GEOT-610</A>.
 */
public class Rescale extends OperationJAI {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -9150531690336265741L;

    /**
     * Constructs a default {@code "Rescale"} operation.
     */
    public Rescale() {
        super("Rescale");
    }

    /**
     * Returns the expected range of values for the resulting image.
     *
     * @todo Not yet implemented.
     */
    @Override
    protected NumberRange deriveRange(final NumberRange[] ranges, final Parameters parameters) {
        return super.deriveRange(ranges, parameters);
    }
}
