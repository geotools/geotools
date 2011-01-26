///*
// *    GeoTools - The Open Source Java GIS Toolkit
// *    http://geotools.org
// * 
// *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
// *
// *    This library is free software; you can redistribute it and/or
// *    modify it under the terms of the GNU Lesser General Public
// *    License as published by the Free Software Foundation;
// *    version 2.1 of the License.
// *
// *    This library is distributed in the hope that it will be useful,
// *    but WITHOUT ANY WARRANTY; without even the implied warranty of
// *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// *    Lesser General Public License for more details.
// */
//package org.geotools.coverage.processing.operation;
//
//// OpenGIS dependencies
//import org.opengis.coverage.grid.GridCoverage;
//
//// Geotools dependencies
//import org.geotools.coverage.processing.FilterOperation;
//
//
///**
// * Replaces {@link Float#NaN NaN} values by the weighted average of neighbors values. This
// * operation uses a box of {@code size}&times{@code size} pixels centered on each {@code NaN}
// * value, where {@code size} = 2&times;{@code padding}+1 (the <cite>padding</cite> is the number
// * of pixel above, below, to the left and to the right of central {@code NaN} pixel). The weighted
// * average is then computed, ignoring all {@code NaN} values. If the number of valid values is
// * greater than or equals to {@code validityThreshold}, then the center {@code NaN} is replaced by
// * the computed average. Otherwise, the {@code NaN} value is left unchanged.
// *
// * <P><STRONG>Name:</STRONG>&nbsp;<CODE>"NodataFilter"</CODE><BR>
// *    <STRONG>JAI operator:</STRONG>&nbsp;none<BR>
// *    <STRONG>Parameters:</STRONG></P>
// * <table border='3' cellpadding='6' bgcolor='F4F8FF'>
// *   <tr bgcolor='#B9DCFF'>
// *     <th>Name</th>
// *     <th>Class</th>
// *     <th>Default value</th>
// *     <th>Minimum value</th>
// *     <th>Maximum value</th>
// *   </tr>
// *   <tr>
// *     <td>{@code "Source"}</td>
// *     <td>{@link org.geotools.coverage.grid.GridCoverage2D}</td>
// *     <td align="center">N/A</td>
// *     <td align="center">N/A</td>
// *     <td align="center">N/A</td>
// *   </tr>
// *   <tr>
// *     <td>{@code "padding"}</td>
// *     <td>{@link java.lang.Integer}</td>
// *     <td>1</td>
// *     <td align="center">0</td>
// *     <td align="center">N/A</td>
// *   </tr>
// *   <tr>
// *     <td>{@code "validityThreshold"}</td>
// *     <td>{@link java.lang.Integer}</td>
// *     <td>4</td>
// *     <td align="center">0</td>
// *     <td align="center">N/A</td>
// *   </tr>
// * </table>
// *
// * @since 2.2
// * @source $URL$
// * @version $Id$
// * @author Martin Desruisseaux (IRD)
// *
// * @see org.geotools.coverage.processing.Operations#nodataFilter(GridCoverage,int,int)
// */
//public class NodataFilter extends FilterOperation {
//    /**
//     * Serial number for interoperability with different versions.
//     */
//    private static final long serialVersionUID = 6818008657792977519L;
//
//    /**
//     * Constructs a default {@code "NodataFilter"} operation.
//     */
//    public NodataFilter() {
//        super("org.geotools.NodataFilter");
//    }
//}
