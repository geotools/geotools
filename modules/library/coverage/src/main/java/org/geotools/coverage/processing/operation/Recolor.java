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

import java.awt.Color;
import java.util.Map;

import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValueGroup;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.processing.ColorMap;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;


/**
 * Operation replacing the colors of a {@link org.geotools.coverage.grid.GridCoverage}.
 * This operation accepts one argument, {@code ColorMaps}, which must be an instance of
 * {@link ColorMap}.
 *
 * @since 2.3
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @todo For compatibility with Geotools 2.3, this operator temporarily accepts an array of
 *       {@link Map} objects, where keys are category names as {@link String} and values are
 *       colors as {@code Color[]}. The {@code null} key is a special value meaning "any
 *       quantitative category". A future Geotools version may not accept {@link Map} anymore.
 */
public final class Recolor extends IndexColorOperation {
    /**
     * The parameter descriptor for the color map.
     */
    public static final ParameterDescriptor COLOR_MAPS = new DefaultParameterDescriptor(
            Citations.GEOTOOLS, "ColorMaps",
            Object[].class,   // Value class (mandatory): TODO: change to ColorMap[]
            null,             // Array of valid values
            new ColorMap[] {  // Default value - a gray scale
                new ColorMap(new Color[] {new Color(16, 16, 16), new Color(240, 240, 240)})},
            null,             // Minimal value
            null,             // Maximal value
            null,             // Unit of measure
            true);            // Parameter is mandatory

    /**
     * Constructs a new "Recolor" operation.
     */
    public Recolor() {
        super(new DefaultParameterDescriptorGroup(Citations.GEOTOOLS, "Recolor",
                new ParameterDescriptor[] { SOURCE_0, COLOR_MAPS }));
    }

    /**
     * Transforms the supplied RGB colors.
     *
     * @see ColorMap#recolor
     */
    protected GridSampleDimension transformColormap(final int[] ARGB, final int band,
            final GridSampleDimension sampleDimension, final ParameterValueGroup parameters)
    {
        final Object[] colorMaps = (Object[]) parameters.parameter("ColorMaps").getValue();
        if (colorMaps == null || colorMaps.length == 0) {
            return sampleDimension;
        }
        Object colorMap = colorMaps[Math.min(band, colorMaps.length - 1)];
        if (colorMap instanceof Map) {
            CoverageProcessor.LOGGER.warning("\"ColorMaps\" parameter of type java.util.Map[] is deprecated. " +
                    "Please use the org.geotools.coverage.processing.ColorMap type instead.");
            colorMap = new ColorMap((Map) colorMap);
        }
        return ((ColorMap) colorMap).recolor(sampleDimension, ARGB);
    }
}
