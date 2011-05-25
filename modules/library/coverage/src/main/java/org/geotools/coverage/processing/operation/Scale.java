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

import java.awt.RenderingHints;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;

import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.processing.BaseScaleOperationJAI;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.image.jai.Registry;


/**
 * This operation is simply a wrapper for the JAI scale operation which allows
 * me to arbitrarly scale and translate a rendered image.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 * @since 2.3
 *
 * @see javax.media.jai.operator.ScaleDescriptor
 */
public class Scale extends BaseScaleOperationJAI {


	/**
	 * Serial number for cross-version compatibility.
	 */
	private static final long serialVersionUID = -3212656385631097713L;

	/** Lock for unsetting native acceleration. */
	private final static int[] lock = new int[1];

	/**
	 * Default constructor.
	 */
	public Scale() {
		super("Scale");

	}


	@Override
	protected RenderedImage createRenderedImage(ParameterBlockJAI parameters, RenderingHints hints) {
		final RenderedImage source = (RenderedImage) parameters.getSource(0);
		final Interpolation interpolation;
		if(parameters.getObjectParameter("Interpolation")!=null)
			interpolation=(Interpolation) parameters.getObjectParameter("Interpolation");
		else
			if(hints.get(JAI.KEY_INTERPOLATION)!=null)
				interpolation=(Interpolation) hints.get(JAI.KEY_INTERPOLATION);
			else
			{
				//I am pretty sure this should not happen. However I am not sure we should throw an error
				interpolation=null;
			}
		final int transferType = source.getSampleModel().getDataType();
		final JAI processor = OperationJAI.getJAI(hints);
		PlanarImage image;
		if (interpolation!=null&&!(interpolation instanceof InterpolationNearest)
				&& (transferType == DataBuffer.TYPE_FLOAT || transferType == DataBuffer.TYPE_DOUBLE)) {

			synchronized (lock) {

				/**
				 * Disables the native acceleration for the "Scale" operation.
				 * In JAI 1.1.2, the "Scale" operation on TYPE_FLOAT datatype
				 * with INTERP_BILINEAR interpolation cause an exception in the
				 * native code of medialib, which halt the Java Virtual Machine.
				 * Using the pure Java implementation instead resolve the
				 * problem.
				 * 
				 * @todo Remove this hack when Sun will fix the medialib bug.
				 *       See
				 *       http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4906854
				 */

				Registry.setNativeAccelerationAllowed(getName(), false);
				image = processor.createNS(getName(),
						parameters, hints).getRendering();


				/**
				 * see above
				 */
				Registry.setNativeAccelerationAllowed(getName(), true);
			}

		} else
			image = processor.createNS(getName(), parameters, hints);

		return image;
	}
	
}
