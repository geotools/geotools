/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.palette;

// J2SE dependencies
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.registry.RenderedRegistryMode;

public class ColorReductionDescriptor extends OperationDescriptorImpl {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8931287738914712392L;
	/**
	 * The operation name, which is {@value}.
	 */
	public static final String OPERATION_NAME = "org.geotools.ColorReduction";

	/**
	 * Constructs the descriptor.
	 */
	public ColorReductionDescriptor() {
		super(new String[][] {
				{ "GlobalName", OPERATION_NAME },
				{ "LocalName", OPERATION_NAME },
				{ "Vendor", "it.geosolutions" },
				{ "Description",
						"Produce a paletted imge from an RGB or RGBA image." },
				{ "DocURL", "http://www.geo-solutions.it/" }, // TODO:
				// provides more
				// accurate URL
				{ "Version", "1.0" },
				{ "arg0Desc", "Number of colors after the reduction." },
				{ "arg1Desc", "Threshold for thresholding alpha" },
				{ "arg2Desc", "Subsample x." },
				{ "arg3Desc", "Subsample y." },
				{ "arg4Desc", "Transparency Threshoold." } },
				new String[] { RenderedRegistryMode.MODE_NAME }, 0, // Supported
				// modes
				new String[] { "numColors", "alphaThreshold" ,"subsampleX","subsampleY"}, // Parameter
				// names
				new Class[] { Integer.class, Integer.class, Integer.class, Integer.class }, // Parameter
				// classes
				new Object[] { new Integer(255), new Integer(1), new Integer(1), new Integer(1) }, // Default
				// values
				null // Valid parameter values
		);
	}

	/**
	 * Returns {@code true} if this operation supports the specified mode, and
	 * is capable of handling the given input source(s) for the specified mode.
	 * 
	 * @param modeName
	 *            The mode name (usually "Rendered").
	 * @param param
	 *            The parameter block for the operation to performs.
	 * @param message
	 *            A buffer for formatting an error message if any.
	 */
	protected boolean validateSources(final String modeName,
			final ParameterBlock param, final StringBuffer message) {
		if (super.validateSources(modeName, param, message)) {
			for (int i = param.getNumSources(); --i >= 0;) {
				final Object source = param.getSource(i);
				if (!(source instanceof RenderedImage)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns {@code true} if the parameters are valids. This implementation
	 * check that the number of bands in the source src1 is equals to the number
	 * of bands of source src2.
	 * 
	 * @param modeName
	 *            The mode name (usually "Rendered").
	 * @param param
	 *            The parameter block for the operation to performs.
	 * @param message
	 *            A buffer for formatting an error message if any.
	 */
	protected boolean validateParameters(final String modeName,
			final ParameterBlock param, final StringBuffer message) {
		if (!super.validateParameters(modeName, param, message)) {
			return false;
		}
		final int numColors = ((Integer) param.getObjectParameter(0)).intValue();
		final int alphaThreashold = ((Integer) param.getObjectParameter(1)).intValue();
		final int ssx = ((Integer) param.getObjectParameter(2)).intValue();
		final int ssy = ((Integer) param.getObjectParameter(3)).intValue();
		final RenderedImage source = (RenderedImage) param.getSource(0);
		if(ssx<=1&&ssx>=source.getWidth())
			return false;
		if(ssy<=1&&ssy>=source.getHeight())
			return false;
		if(alphaThreashold<0||alphaThreashold>255)
			return false;
		if(numColors<=0||(numColors>256))
			return false;
		return true;
	}
}
