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
package org.geotools.referencing.piecewise;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.DataBuffer;
import java.awt.image.RasterFormatException;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.CRIFImpl;
import javax.media.jai.ColormapOpImage;
import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RectIterFactory;
import javax.media.jai.iterator.WritableRectIter;
import javax.media.jai.registry.RenderedRegistryMode;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.image.TransfertRectIter;
import org.geotools.image.jai.Registry;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.opengis.referencing.operation.TransformException;

import com.sun.media.jai.opimage.RIFUtil;
import com.sun.media.jai.util.ImageUtil;

/**
 * Images are created using the    {@code            GenericPiecewise.CRIF}    inner class, where "CRIF" stands for   {@link java.awt.image.renderable.ContextualRenderedImageFactory}    . The image operation name is "org.geotools.GenericPiecewise".
 *
 * @source $URL$
 * @version    $Id$
 * @author    Simone Giannecchini - GeoSolutions
 * @since    2.4
 */
public class GenericPiecewise<T extends PiecewiseTransform1DElement> extends ColormapOpImage {

	/**
	 * The operation name.
	 */
	public static final String OPERATION_NAME = "org.geotools.GenericPiecewise";

	/**
	 * DefaultPiecewiseTransform1D that we'll use to transform this image. We'll apply it ato all of its bands.
	 */
	private final PiecewiseTransform1D<T> piecewise;

	private final boolean isByteData;

	private byte[][] lut;
	
	private double gapsValue = Double.NaN;
	private boolean hasGapsValue = false;

	private final boolean useLast;


	/**
	 * Constructs a new {@code RasterClassifier}.
	 * 
	 * @param image
	 *            The source image.
	 * @param lic
	 *            The DefaultPiecewiseTransform1D.
	 * @param bandIndex
	 * @param hints
	 *            The rendering hints.
	 */
	private GenericPiecewise(final RenderedImage image,
			final PiecewiseTransform1D<T> lic,
			final RenderingHints hints) {
		super(image, RIFUtil.getImageLayoutHint(hints), hints, false);
		this.piecewise = lic;
        // Ensure that the number of sets of breakpoints is either unity
        // or equal to the number of bands.
        final int numBands = sampleModel.getNumBands();


        // Set the byte data flag.
        isByteData = sampleModel.getTransferType() == DataBuffer.TYPE_BYTE;
        
        

		// ////////////////////////////////////////////////////////////////////
		//
		// Check if we can make good use of a default piecewise element for filling gaps
		// in the input range
		//
		// ////////////////////////////////////////////////////////////////////
		if (this.piecewise.hasDefaultValue()) {
			gapsValue=piecewise.getDefaultValue();
			hasGapsValue = true;
		}
		
		// ////////////////////////////////////////////////////////////////////
		//
		// Check if we can optimize this operation by reusing the last used
		// piecewise element first. The speed up we get can be substantial since we avoid
		// an explicit search in the piecewise element list for the fitting piecewise element given
		// a certain sample value.
		//
		//
		// ////////////////////////////////////////////////////////////////////
		useLast = piecewise instanceof DefaultDomain1D;
		

        // Perform byte-specific initialization.
        if(isByteData) {
            // Initialize the lookup table.
            try {
				createLUT(numBands);
			} catch (final TransformException e) {
				final RuntimeException re= new RuntimeException(e);
				throw re;
			}

        }

        // Set flag to permit in-place operation.
        permitInPlaceOperation();

        // Initialize the colormap if necessary.
        initializeColormapOperation();
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

		PiecewiseTransform1DElement last = null;
		int bandNumber=0;
		do {
			try {
				iterator.startLines();
				if (!iterator.finishedLines())
					do {
						iterator.startPixels();
						if (!iterator.finishedPixels())
							do {
								if(isByteData) 
								{
									final int in=iterator.getSample()&0xff;
									final int out=0xff&lut[bandNumber][in];
									iterator.setSample(out);
								}
								else
									last = domainSearch(iterator, last,bandNumber);

							} while (!iterator.nextPixelDone());
					} while (!iterator.nextLineDone());
			} catch (final Exception cause) {
				final RasterFormatException exception = new RasterFormatException(
						cause.getLocalizedMessage());
				exception.initCause(cause);
				throw exception;
			}
			bandNumber++;
		} while (iterator.finishedBands());
	}


	private PiecewiseTransform1DElement domainSearch(final WritableRectIter iterator,
			PiecewiseTransform1DElement last, final int bandNumber) throws TransformException {
		
		 
			 
		// //
		//
		// get the input value to be transformed
		//
		// //
		final double value = iterator.getSampleDouble();
		
		
		
		
		
		// //
		//
		// get the correct piecewise element for this
		// transformation
		//
		// //
		final PiecewiseTransform1DElement transformElement;
		if (useLast) {
			if (last != null && last.contains(value))
				transformElement = last;
			else {
				last = transformElement = (PiecewiseTransform1DElement) piecewise
						.findDomainElement(value);
			}
		} else
			transformElement = (PiecewiseTransform1DElement) piecewise
					.findDomainElement(value);

		// //
		//
		// in case everything went fine let's apply the
		// transform.
		//
		// //
		if (transformElement != null)
			iterator.setSample(transformElement
					.transform(value));
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
		return last;
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
	 * The supplied {@code GridSampleDimension} objects describe the piecewise
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
									"Generic Piecewise Transformation" },
							{ "DocURL", "http://www.geotools.org/" },
							{ "Version", "1.0" } },
					new String[] { RenderedRegistryMode.MODE_NAME }, 1,
					new String[] { "Domain1D", "bandIndex" }, // Argument
					// names
					new Class[] { DefaultPiecewiseTransform1D.class,
							Integer.class }, // Argument
					// classes
					new Object[] { NO_PARAMETER_DEFAULT, new Integer(-1) }, 
					// Default values for parameters,
					null // No restriction on valid parameter values.
			);
		}

		/**
		 * Returns {@code true} if the parameters are valids. This
		 * implementation check that the number of bands in the source image is
		 * equals to the number of supplied sample dimensions, and that all
		 * sample dimensions has piecewise.
		 * 
		 * @param modeName
		 *            The mode name (usually "Rendered").
		 * @param param
		 *            The parameter block for the operation to performs.
		 * @param message
		 *            A buffer for formatting an error message if any.
		 */
	
		@SuppressWarnings("unchecked")
                protected boolean validateParameters(final String modeName,
				final ParameterBlock param, final StringBuffer message) {
			if (!super.validateParameters(modeName, param, message)) {
				return false;
			}
			final RenderedImage source = (RenderedImage) param.getSource(0);
			final PiecewiseTransform1D lic =  (PiecewiseTransform1D) param.getObjectParameter(0);
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
	        @SuppressWarnings("unchecked")
		public RenderedImage create(final ParameterBlock param,
				final RenderingHints hints) {
			final RenderedImage image = (RenderedImage) param.getSource(0);
			final PiecewiseTransform1D lic = (PiecewiseTransform1D) param.getObjectParameter(0);
			return new GenericPiecewise(image, lic, hints);
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





	/**
	 * Create a lookup table to be used in the case of byte data.
	 * @param numBands 
	 * @throws TransformException 
	 */
	private void createLUT(final int numBands) throws TransformException {
	    // Allocate memory for the data array references.
	    final byte[][] data = new byte[numBands][];
	
	    // Generate the data for each band.
	    for(int band = 0; band < numBands; band++) {
	        // Allocate memory for this band.
	        data[band] = new byte[256];
	
	        // Cache the references to avoid extra indexing.
	        final byte[] table = data[band];

	
	        // Initialize the lookup table data.
			PiecewiseTransform1DElement lastPiecewiseElement = null;
	        for(int value = 0; value < 256; value++) {
				
				
				// //
				//
				// get the correct piecewise element for this
				// transformation
				//
				// //
				final PiecewiseTransform1DElement piecewiseElement;
				if (useLast) {
					if (lastPiecewiseElement != null && lastPiecewiseElement.contains(value))
						piecewiseElement = lastPiecewiseElement;
					else {
						lastPiecewiseElement = piecewiseElement = (PiecewiseTransform1DElement) piecewise
								.findDomainElement(value);
					}
				} else
					piecewiseElement = (PiecewiseTransform1DElement) piecewise
							.findDomainElement(value);

				// //
				//
				// in case everything went fine let's apply the
				// transform.
				//
				// //
				if (piecewiseElement != null)
					table[value] =
		                ImageUtil.clampRoundByte(piecewiseElement
							.transform(value));
				else {
					// //
					//
					// if we did not find one let's try to use
					// one of the nodata ones to fill the gaps,
					// if we are allowed to (see above).
					//
					// //
					if (hasGapsValue)
						table[value] =
			                ImageUtil.clampRoundByte(gapsValue);
					else

						// //
						//
						// if we did not find one let's throw a
						// nice error message
						//
						// //
						throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, Double.toString(value)));
				}
	            
	        }
	        



		      
	    }
	
	    // Construct the lookup table.
	    lut = data;
	}



	/**
	 * Transform the colormap according to the rescaling parameters.
	 */
	protected void transformColormap(final byte[][] colormap) {
	
	    for(int b = 0; b < 3; b++) {
	        final byte[] map = colormap[b];
	    final byte[] luTable = lut[b >= lut.length ? 0 : b];
	        final int mapSize = map.length;
	
	        for(int i = 0; i < mapSize; i++) {
	            map[i] = luTable[(map[i] & 0xFF)];
	        }
	    }
	}

}
