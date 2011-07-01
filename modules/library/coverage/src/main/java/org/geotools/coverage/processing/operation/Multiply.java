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
import javax.media.jai.operator.MultiplyDescriptor;

import org.geotools.coverage.processing.OperationJAI;
import org.geotools.util.NumberRange;


/**
 * Create a new coverage as the multiplication of two source coverages by doing pixel by pixel 
 * multiplication:
 * result[0][0] = source0[0][0] * source1[0][0]
 * ...
 * ...
 * result[i][j] = source0[i][j] * source1[i][j]
 * ...
 * ...
 * result[n-1][m-1] = source0[n-1][m-1] * source1[n-1][m-1]
 * 
 * Make sure coverages have same envelope and same resolution before using this operation.
 *
 * <P><STRONG>Name:</STRONG>&nbsp;<CODE>"Multiply"</CODE><BR>
 *    <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain MultiplyDescriptor Multiply}"</CODE><BR>
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
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/coverage/src/main/java/org/geotools/coverage/processing/operation/Multiply.java $
 *
 * @see org.geotools.coverage.processing.Operations#multiply(org.opengis.coverage.Coverage, org.opengis.coverage.Coverage)
 * @see Multiply
 *
 */
public class Multiply extends OperationJAI {
       /**
     * 
     */
    private static final long serialVersionUID = -3046913974864461717L;

    /**
     * Constructs a default {@code "MultiplyConst"} operation.
     */
    public Multiply() {
        super("Multiply");
    }
    
    /**
     * Returns the expected range of values for the resulting image.
     */
    protected NumberRange deriveRange(final NumberRange[] ranges, final Parameters parameters) {
        
        // Note that they will not be exact ranges since this will require really computing
        // the pixel by pixel operation
        if (ranges != null && ranges.length == 2){
            final NumberRange range0 = ranges[0];
            final NumberRange range1 = ranges[1];
            final double min0 = range0.getMinimum();
            final double min1 = range1.getMinimum();
            final double max0 = range0.getMaximum();
            final double max1 = range1.getMaximum();
            final double max = max0 * max1;
            final double min = min0 * min1;
            return new NumberRange(min, max);
        }
        return null;
    }

   
}
