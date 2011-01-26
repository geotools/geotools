/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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

// J2SE dependencies
import java.lang.reflect.Array;

// JAI dependencies
import javax.media.jai.Interpolation;

// OpenGIS dependencies
import org.opengis.coverage.Coverage;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValueGroup;

// Geotools dependencies
import org.geotools.factory.Hints;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.Interpolator2D;
import org.geotools.coverage.processing.Operation2D;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.resources.image.ImageUtilities;


/**
 * Specifies the interpolation type to be used to interpolate values for points which fall between
 * grid cells. The default value is nearest neighbor. The new interpolation type operates on all
 * sample dimensions. Possible values for type are: {@code "NearestNeighbor"}, {@code "Bilinear"}
 * and {@code "Bicubic"} (the {@code "Optimal"} interpolation type is currently not supported).
 * <p>
 * <strong>Geotools extension:</strong><br>
 * The Geotools implementation provides two extensions to OpenGIS specification: First, it accepts
 * also an {@link javax.media.jai.Interpolation} argument type, for interoperability with
 * <A HREF="http://java.sun.com/products/java-media/jai/">Java Advanced Imaging</A>. Second, it
 * accepts also an array of {@link java.lang.String} or {@link javax.media.jai.Interpolation}
 * objects. When an array is specified, the first interpolation in the array is applied. If this
 * interpolation returns a {@code NaN} value, then the second interpolation is tried as a fallback.
 * If the second interpolation returns also a {@code NaN} value, then the third one is tried and so
 * on until an interpolation returns a real number or until we reach the end of interpolation list.
 * This behavior is convenient when processing remote sensing images of geophysics data, for example
 * <cite>Sea Surface Temperature</cite> (SST), in which clouds may mask many pixels (i.e. set them
 * to some {@code NaN} values). Because {@code "Bicubic"} interpolation needs 4&times;4 pixels while
 * {@code "Bilinear"} interpolation needs only 2x2 pixels, the {@code "Bilinear"} interpolation is
 * less likely to fails because of clouds ({@code NaN} values) than the {@code "Bicubic"} one
 * (note: only one {@code NaN} value is enough to make an interpolation fails). One can workaround
 * the problem by trying a bicubic interpolation first, then a linear interpolation if
 * {@code "Bicubic"} failed at a particular location, <cite>etc.</cite> This behavior can be
 * specified with the following {@code "Type"} argument:
 * <code>new String[]{"Bicubic", "Bilinear", "NearestNeighbor"}</code>.
 *
 * <P><STRONG>Name:</STRONG>&nbsp;<CODE>"Interpolate"</CODE><BR>
 *    <STRONG>JAI operator:</STRONG>&nbsp;N/A<BR>
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
 *     <td>{@code "Type"}</td>
 *     <td>{@link java.lang.CharSequence}</td>
 *     <td>"NearestNeighbor"</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 * </table>
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see org.geotools.coverage.processing.Operations#interpolate
 * @see Interpolator2D
 */
public class Interpolate extends Operation2D {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 6742127682733620661L;

    /**
     * The parameter descriptor for the interpolation type.
     */
    public static final ParameterDescriptor TYPE =
            new DefaultParameterDescriptor(Citations.OGC, "Type",
                Object.class,                       // Value class (mandatory)
                null,                               // Array of valid values
                "NearestNeighbor",                  // Default value
                null,                               // Minimal value
                null,                               // Maximal value
                null,                               // Unit of measure
                true);                              // Parameter is mandatory

    /**
     * Constructs an {@code "Interpolate"} operation.
     */
    public Interpolate() {
        super(new DefaultParameterDescriptorGroup(Citations.OGC, "Interpolate",
              new ParameterDescriptor[] {
                    SOURCE_0,
                    TYPE
        }));
    }

    /**
     * Applies an interpolation to a grid coverage. This method is invoked
     * by {@link org.geotools.coverage.processing.DefaultProcessor} for the
     * {@code "Interpolate"} operation.
     */
    public Coverage doOperation(final ParameterValueGroup parameters, final Hints hints) {
        final GridCoverage2D source = (GridCoverage2D)parameters.parameter("Source").getValue();
        final Object         type =                   parameters.parameter("Type"  ).getValue();
        final Interpolation[] interpolations;
        if (type.getClass().isArray()) {
            interpolations = new Interpolation[Array.getLength(type)];
            for (int i=0; i<interpolations.length; i++) {
                interpolations[i] = ImageUtilities.toInterpolation(Array.get(type, i));
            }
        } else {
            interpolations = new Interpolation[] {ImageUtilities.toInterpolation(type)};
        }
        return Interpolator2D.create(source, interpolations);
    }
}
