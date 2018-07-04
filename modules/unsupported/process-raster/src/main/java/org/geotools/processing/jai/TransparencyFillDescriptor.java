/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.processing.jai;

import com.sun.media.jai.util.AreaOpPropertyGenerator;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import javax.media.jai.EnumeratedParameter;
import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PropertyGenerator;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RenderedRegistryMode;

@SuppressWarnings("serial")
public class TransparencyFillDescriptor extends OperationDescriptorImpl {

    public static final FillType FILL_AVERAGE = new FillType("FILL_AVERAGE", 0);

    public static final FillType FILL_CLONE_FIRST = new FillType("FILL_CLONE_FIRST", 1);

    public static final FillType FILL_CLONE_SECOND = new FillType("FILL_CLONE_SECOND", 2);

    public static class FillType extends EnumeratedParameter {
        FillType(String name, int value) {
            super(name, value);
        }
    }

    /**
     * The resource strings that provide the general documentation and specify the parameter list
     * for a TransparencyFill operation.
     */
    private static final String[][] resources = {
        {"GlobalName", "TransparencyFill"},
        {"LocalName", "TransparencyFill"},
        {"Vendor", "it.geosolutions.jaiext"},
        {"Description", "Transparency pixels Filler"},
        {"DocURL", ""},
        {"Version", "1.0"},
        {"arg0Desc", "Type of fill"}
    };

    /** The parameter names for the TransparencyFill operation. */
    private static final String[] paramNames = {"type"};

    /** The parameter class types for the TransparencyFill operation. */
    private static final Class[] paramClasses = {FillType.class};

    /** The parameter default values for the TransparencyFill operation. */
    private static final Object[] paramDefaults = {FILL_AVERAGE};

    /** Constructor. */
    public TransparencyFillDescriptor() {
        super(resources, 1, paramClasses, paramNames, paramDefaults);
    }

    /**
     * Returns an array of <code>PropertyGenerators</code> implementing property inheritance for the
     * "TransparencyFill" operation.
     *
     * @return An array of property generators.
     */
    public PropertyGenerator[] getPropertyGenerators() {
        PropertyGenerator[] pg = new PropertyGenerator[1];
        pg[0] = new AreaOpPropertyGenerator();
        return pg;
    }

    public static RenderedOp create(RenderedImage source0, FillType type, RenderingHints hints) {
        ParameterBlockJAI pb =
                new ParameterBlockJAI("TransparencyFill", RenderedRegistryMode.MODE_NAME);

        pb.setSource("source0", source0);
        pb.setParameter("type", type);
        return JAI.create("TransparencyFill", pb, hints);
    }
}
