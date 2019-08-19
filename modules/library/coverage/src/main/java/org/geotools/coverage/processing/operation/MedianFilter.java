/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import javax.media.jai.operator.MedianFilterDescriptor;
import javax.media.jai.operator.MedianFilterShape;
import org.geotools.coverage.processing.OperationJAI;

/**
 * For each position of the mask, replaces the center pixel by the median of the pixel values
 * covered by the mask.
 *
 * <p><STRONG>Name:</STRONG>&nbsp;<CODE>"MedianFilter"</CODE><br>
 * <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain MedianFilterDescriptor MedianFilter}"
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
 *     <td>{@code "maskShape"}</td>
 *     <td>{@link MedianFilterShape}</td>
 *     <td>{@link MedianFilterDescriptor#MEDIAN_MASK_SQUARE}</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "maskSize"}</td>
 *     <td>{@link Integer}</td>
 *     <td>{@code 3}</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 * </table>
 *
 * @since 2.3
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class MedianFilter extends OperationJAI {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -8604321975842276962L;

    /** Constructs a default {@code "MedianFilter"} operation. */
    public MedianFilter() {
        super("MedianFilter");
    }
}
