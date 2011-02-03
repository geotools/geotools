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
package org.geotools.renderer.lite.gridcoverage2d;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.CRIFImpl;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.PlanarImage;
import javax.media.jai.PointOpImage;
import javax.media.jai.iterator.RectIterFactory;
import javax.media.jai.iterator.WritableRectIter;
import javax.media.jai.registry.RenderedRegistryMode;
import javax.media.jai.util.ImagingException;

import org.geotools.image.TransfertRectIter;
import org.geotools.image.jai.Registry;
import org.geotools.referencing.piecewise.DefaultDomain1D;
import org.geotools.referencing.piecewise.Domain1D;
import org.geotools.referencing.piecewise.PiecewiseTransform1DElement;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;

/**
 * Images are created using the        {@code                LinearClassifier.CRIF}        inner class, where "CRIF" stands for       {@link java.awt.image.renderable.ContextualRenderedImageFactory}        . The image operation name is "org.geotools.RasterClassifier".
 *
 * @source $URL$
 * @version        $Id$
 * @author        Simone Giannecchini - GeoSolutions
 * @since        2.4
 */
@SuppressWarnings("unchecked")
public class RasterClassifier extends PointOpImage {

	/**
	 * The operation name.
	 */
	public static final String OPERATION_NAME = "org.geotools.RasterClassifier";

	/**
	 * DomainElement1D lists for each bands. The array length must matches the number
	 * of bands in source image.
	 */
	private final ColorMapTransform<ColorMapTransformElement> pieces;

	/**
	 * The index (zero based) of the band we are going to classify
	 */
	private int bandIndex;

	/**
	 * Constructs a new {@code RasterClassifier}.
	 * 
	 * @param image
	 *            The source image.
	 * @param lic
	 *            The category lists, one for each image's band.
	 * @param bandIndex
	 * @param hints
	 *            The rendering hints.
	 */
	private RasterClassifier(final RenderedImage image,
			final ColorMapTransform<ColorMapTransformElement> lic, int bandIndex,
			final RenderingHints hints) {
		super(image, prepareLayout(image, (ImageLayout) hints
				.get(JAI.KEY_IMAGE_LAYOUT), lic), hints, false);
		this.pieces = lic;
		this.bandIndex = bandIndex;
		permitInPlaceOperation();
	}

	/**
	 * Prepare the {@link ImageLayout} for the final image by building the
	 * {@link ColorModel} from the input
	 * 
	 * @param image
	 *            the image to classify.
	 * @param layout
	 *            a proposed layout.
	 * @param lic
	 *            the pieces we are asked to use.
	 * @return a layout suitable for the image that we'll create after this
	 */
	private static ImageLayout prepareLayout(RenderedImage image,
			ImageLayout layout, ColorMapTransform<ColorMapTransformElement> lic) {
		// //
		//
		// Get the final color model from the pieces and from that one
		// create the sample model
		//
		// ///
		final ColorModel finalColorModel = lic.getColorModel();
		// create a good sample model for the output raster
		final SampleModel finalSampleModel = lic.getSampleModel(image
				.getWidth(), image.getHeight());
		if (layout == null)
			layout = new ImageLayout();
		layout.setColorModel(finalColorModel);
		layout.setSampleModel(finalSampleModel);
		return layout;
	}

	/**
	 * Computes one of the destination image tile.
	 * 
	 * @todo There are two optimisations we could do here:
	 *       <ul>
	 *       <li>If source and destination are the same raster, then a single
	 *       {@link WritableRectIter} object would be more efficient (the hard
	 *       work is to detect if source and destination are the same).</li>
	 *       <li>If the destination image is a single-banded, non-interleaved
	 *       sample model, we could apply the transform directly in the
	 *       {@link java.awt.image.DataBuffer}. We can even avoid to copy
	 *       sample value if source and destination raster are the same.</li>
	 *       </ul>
	 * 
	 * @param sources
	 *            An array of length 1 with source image.
	 * @param dest
	 *            The destination tile.
	 * @param destRect
	 *            the rectangle within the destination to be written.
	 */
	protected void computeRect(final PlanarImage[] sources,
			final WritableRaster dest, final Rectangle destRect) {
		final PlanarImage source = sources[0];
		WritableRectIter iterator = RectIterFactory.createWritable(dest,
				destRect);
		if (true) {
			// TODO: Detect if source and destination rasters are the same. If
			// they are the same, we should skip this block. Iteration will then
			// be faster.
			iterator = TransfertRectIter.create(RectIterFactory.create(source,
					destRect), iterator);
		}

		// ////////////////////////////////////////////////////////////////////
		//
		// Prepare the iterator to work on the correct bands, if this is
		// requested.
		//
		// ////////////////////////////////////////////////////////////////////
		if (!iterator.finishedBands()) {
			for (int i = 0; i < bandIndex; i++)
				iterator.nextBand();
		}

		// ////////////////////////////////////////////////////////////////////
		//
		// Check if we can make good use of a no data category for filling gaps
		// in the input range
		//
		// ////////////////////////////////////////////////////////////////////
		double gapsValue = Double.NaN;
		boolean hasGapsValue = false;
		if (this.pieces.hasDefaultValue()) {
				gapsValue = this.pieces.getDefaultValue();
				hasGapsValue = true;
		}

		// ////////////////////////////////////////////////////////////////////
		//
		// Check if we can optimize this operation by reusing the last used
		// category first. The speed up we get can be substantial since we avoid
		// n explicit search in the category list for the fitting category given
		// a certain sample value.
		//
		// This is not possible when the NoDataCategories range overlaps with
		// the range of the valid values. In this case we have ALWAYS to check
		// first the NoDataRange when applying transformations. If we optimized
		// in this case we would get erroneous results given to the fact that we
		// might be reusing a valid sample category while we should be using a
		// no data one.
		//
		// ////////////////////////////////////////////////////////////////////
		PiecewiseTransform1DElement last = null;
		final boolean useLast = pieces instanceof DefaultDomain1D;
		do {
			try {
				iterator.startLines();
				if (!iterator.finishedLines())
					do {
						iterator.startPixels();
						if (!iterator.finishedPixels())
							do {
								// //
								//
								// get the input value to be transformed
								//
								// //
								final double value = iterator.getSampleDouble();
								// //
								//
								// get the correct category for this
								// transformation
								//
								// //
								final PiecewiseTransform1DElement transform;
								if (useLast) {
									if (last != null && last.contains(value))
										transform = last;
									else {
										last = transform = pieces.findDomainElement(value);
									}
								} else
									transform = (PiecewiseTransform1DElement) pieces.findDomainElement(value);

								// //
								//
								// in case everything went fine let's apply the
								// transform.
								//
								// //
								if (transform != null)
									iterator.setSample(transform.transform(value));
								else {
									// //
									//
									// if we did not find one let's try to use
									// one of the nodata ones to fill the gaps,
									// if we are allowed to (see above).
									//
									// //
									if (hasGapsValue)
										iterator.setSample(gapsValue);
									else

										// //
										//
										// if we did not find one let's throw a
										// nice error message
										//
										// //
										throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, Double.toString(value)));
								}

							} while (!iterator.nextPixelDone());
					} while (!iterator.nextLineDone());
			} catch (Throwable cause) {
				throw  new ImagingException(
						cause.getLocalizedMessage(),cause);
			}
			if (bandIndex != -1)
				break;
		} while (iterator.finishedBands());
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// ////// ////////
	// ////// REGISTRATION OF "SampleTranscode" IMAGE OPERATION ////////
	// ////// ////////
	// ///////////////////////////////////////////////////////////////////////////////
	/**
	 * The operation descriptor for the "SampleTranscode" operation. This
	 * operation can apply the
	 * {@link GridSampleDimension#getSampleToGeophysics sampleToGeophysics}
	 * transform on all pixels in all bands of an image. The transformations are
	 * supplied as a list of {@link GridSampleDimension}s, one for each band.
	 * The supplied {@code GridSampleDimension} objects describe the pieces
	 * in the <strong>source</strong> image. The target image will matches
	 * sample dimension
	 * 
	 * <code>{@link GridSampleDimension#geophysics geophysics}(!isGeophysics)</code>,
	 * 
	 * where {@code isGeophysics} is the previous state of the sample dimension.
	 */
	private static final class Descriptor extends OperationDescriptorImpl {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7954257625240335874L;

		/**
		 * Construct the descriptor.
		 */
		public Descriptor() {
			super(
					new String[][] {
							{ "GlobalName", OPERATION_NAME },
							{ "LocalName", OPERATION_NAME },
							{ "Vendor", "Geotools 2" },
							{ "Description",
									"Transformation from sample to geophysics values" },
							{ "DocURL", "http://www.geotools.org/" },
							{ "Version", "1.0" } },
					new String[] { RenderedRegistryMode.MODE_NAME }, 1,
					new String[] { "Domain1D", "bandIndex" }, // Argument
					// names
					new Class[] { ColorMapTransform.class,
							Integer.class }, // Argument
					// classes
					new Object[] { NO_PARAMETER_DEFAULT, Integer.valueOf(-1) }, // Default
					// values
					// for parameters,
					null // No restriction on valid parameter values.
			);
		}

		/**
		 * Returns {@code true} if the parameters are valids. This
		 * implementation check that the number of bands in the source image is
		 * equals to the number of supplied sample dimensions, and that all
		 * sample dimensions has pieces.
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
			final RenderedImage source = (RenderedImage) param.getSource(0);
			final Domain1D lic = (Domain1D) param.getObjectParameter(0);
			if (lic == null)
				return false;
			final int numBands = source.getSampleModel().getNumBands();
			final int bandIndex = param.getIntParameter(1);
			if (bandIndex == -1)
				return true;
			if (bandIndex < 0 || bandIndex >= numBands) {
				return false;
			}
			return true;
		}
	}

	/**
	 * The {@link RenderedImageFactory} for the "SampleTranscode" operation.
	 */
	private static final class CRIF extends CRIFImpl {
		/**
		 * Creates a {@link RenderedImage} representing the results of an
		 * imaging operation for a given {@link ParameterBlock} and
		 * {@link RenderingHints}.
		 */
		public RenderedImage create(final ParameterBlock param,
				final RenderingHints hints) {
			final RenderedImage image = (RenderedImage) param.getSource(0);
			final ColorMapTransform<ColorMapTransformElement> lic = (ColorMapTransform<ColorMapTransformElement>) param.getObjectParameter(0);
			final int bandIndex = param.getIntParameter(1);
			return new RasterClassifier(image, lic, bandIndex, hints);
		}

	}

	/**
	 * Register the RasterClassifier operation to the operation registry of the
	 * specified JAI instance. This method is invoked by the static initializer
	 * of {@link GridSampleDimension}.
	 * 
	 * @param jai
	 *            JAI instance in which we want to register the RasterClassifier
	 *            operation.
	 * @return <code>true</code> if everything goes fine, <code>false</code>
	 *         otherwise.
	 */
	public static boolean register(final JAI jai) {
		return Registry.registerRIF(jai, new Descriptor(), OPERATION_NAME,
				new CRIF());
	}
}
