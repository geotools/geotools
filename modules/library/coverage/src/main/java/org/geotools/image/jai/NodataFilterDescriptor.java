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
package org.geotools.image.jai;

// JAI dependencies
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.registry.RenderedRegistryMode;
import javax.media.jai.util.Range;


/**
 * The descriptor for the {@link NodataFilter} operation.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Lionel Flahaut (2ie Technologie, IRD)
 */
public class NodataFilterDescriptor extends OperationDescriptorImpl {
    /**
     * The operation name, which is {@value}.
     */
    public static final String OPERATION_NAME = "org.geotools.NodataFilter";

    /**
     * The range of valid parameter values.
     */
    private static final Range ARGUMENT_RANGE = new Range(Integer.class, Integer.valueOf(0), null);

    /**
     * Constructs the descriptor.
     */
    public NodataFilterDescriptor() {
        super(new String[][]{{"GlobalName",  OPERATION_NAME},
                             {"LocalName",   OPERATION_NAME},
                             {"Vendor",      "org.geotools"},
                             {"Description", "Replace NaN values by a weighted average of neighbor values."},
                             {"DocURL",      "http://www.geotools.org/"}, // TODO: provides more accurate URL
                             {"Version",     "1.0"},
                             {"arg0Desc",    "The number of pixel above, below, to the left and " +
                                             "to the right of central NaN pixel."},
                             {"arg1Desc",    "The minimal number of valid neighbors required " +
                                             "in order to consider the average as valid."}},
              new String[]   {RenderedRegistryMode.MODE_NAME}, 1,
              new String[]   {"padding", "validityThreshold"},         // Argument names
              new Class []   {Integer.class,      Integer.class},      // Argument classes
              new Object[]   {Integer.valueOf(1), Integer.valueOf(4)}, // Default values for parameters
              new Range[]    {ARGUMENT_RANGE, ARGUMENT_RANGE}          // Valid range for parameters
        );
    }
}
