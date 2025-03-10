/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import javax.media.jai.operator.BandSelectDescriptor; // For javadoc
import org.geotools.api.coverage.Coverage;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.coverage.processing.Operation2D;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.util.factory.Hints;

/**
 * Chooses <var>N</var> {@linkplain org.geotools.coverage.GridSampleDimension sample dimensions} from a grid coverage
 * and copies their sample data to the destination grid coverage in the order specified. The {@code "SampleDimensions"}
 * parameter specifies the source {@link org.geotools.coverage.GridSampleDimension} indices, and its size
 * ({@code SampleDimensions.length}) determines the number of sample dimensions of the destination grid coverage. The
 * destination coverage may have any number of sample dimensions, and a particular sample dimension of the source
 * coverage may be repeated in the destination coverage by specifying it multiple times in the
 * {@code "SampleDimensions"} parameter.
 *
 * <p><strong>Geotools extension:</strong><br>
 * This operation can also be used for selecting a different "visible sample dimension". Some images may contain useful
 * data in more than one sample dimension, but renderer the content using only 1 sample dimension at time. The
 * {@code "VisibleSampleDimension"} parameter can be used for selecting this sample dimension. If omitted, then the new
 * grid coverage will inherit its source's visible sample dimension.
 *
 * <p><STRONG>Name:</STRONG>&nbsp;<CODE>"SelectSampleDimension"</CODE><br>
 * <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain BandSelectDescriptor BandSelect}"</CODE> <br>
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
 *     <td>{@code "SampleDimensions"}</td>
 *     <td>{@code int[]}</td>
 *     <td align="center">Same as source</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "VisibleSampleDimension"}</td>
 *     <td>{@link java.lang.Integer}</td>
 *     <td align="center">Same as source</td>
 *     <td align="center">0</td>
 *     <td align="center">N/A</td>
 *   </tr>
 * </table>
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @see org.geotools.coverage.processing.Operations#selectSampleDimension
 * @see BandSelectDescriptor
 */
public class SelectSampleDimension extends Operation2D {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 6889502343896409135L;

    /** The parameter descriptor for the sample dimension indices. */
    public static final ParameterDescriptor<int[]> SAMPLE_DIMENSIONS = new DefaultParameterDescriptor<>(
            Citations.OGC,
            "SampleDimensions",
            int[].class, // Value class (mandatory)
            null, // Array of valid values
            null, // Default value
            null, // Minimal value
            null, // Maximal value
            null, // Unit of measure
            false); // Parameter is optional

    /** The parameter descriptor for the visible dimension indice. This is a Geotools-specific parameter. */
    public static final ParameterDescriptor<Integer> VISIBLE_SAMPLE_DIMENSION = new DefaultParameterDescriptor<>(
            Citations.GEOTOOLS,
            "VisibleSampleDimension",
            Integer.class, // Value class (mandatory)
            null, // Array of valid values
            null, // Default value
            0, // Minimal value
            null, // Maximal value
            null, // Unit of measure
            false); // Parameter is optional

    /** Constructs a default {@code "SelectSampleDimension"} operation. */
    public SelectSampleDimension() {
        super(new DefaultParameterDescriptorGroup(
                "SelectSampleDimension",
                new ParameterDescriptor[] {SOURCE_0, SAMPLE_DIMENSIONS, VISIBLE_SAMPLE_DIMENSION}));
    }

    /**
     * Applies the band select operation to a grid coverage. This method is invoked by
     * {@link org.geotools.coverage.processing.DefaultProcessor} for the {@code "SelectSampleDimension"} operation.
     *
     * @param parameters List of name value pairs for the parameters.
     * @param hints A set of rendering hints, or {@code null} if none.
     * @return The result as a grid coverage.
     */
    @Override
    public Coverage doOperation(final ParameterValueGroup parameters, final Hints hints) {
        return BandSelector2D.create(parameters, hints);
    }
}
