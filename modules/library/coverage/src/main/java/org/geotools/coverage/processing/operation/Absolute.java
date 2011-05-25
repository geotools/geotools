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

// JAI dependencies (for javadoc)
import javax.media.jai.operator.AbsoluteDescriptor;

// Geotools dependencies
import org.geotools.util.NumberRange;
import org.geotools.coverage.processing.OperationJAI;


/**
 * Computes the mathematical absolute value of each sample value.
 *
 * <P><STRONG>Name:</STRONG>&nbsp;<CODE>"Absolute"</CODE><BR>
 *    <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain AbsoluteDescriptor Absolute}"</CODE><BR>
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
 * </table>
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see org.geotools.coverage.processing.Operations#absolute
 * @see AbsoluteDescriptor
 */
public class Absolute extends OperationJAI {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3723059532452772794L;

    /**
     * Constructs a default {@code "Absolute"} operation.
     */
    public Absolute() {
        super("Absolute");
    }

    /**
     * Returns the expected range of values for the resulting image.
     */
    protected NumberRange deriveRange(final NumberRange[] ranges, final Parameters parameters) {
        final NumberRange range = ranges[0];
        final double min = Math.abs(range.getMinimum());
        final double max = Math.abs(range.getMaximum());
        return (max<min) ? new NumberRange(max, min) : new NumberRange(min, max);
    }
}
