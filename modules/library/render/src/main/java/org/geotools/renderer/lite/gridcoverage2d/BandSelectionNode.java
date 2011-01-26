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

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.List;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.SelectSampleDimension;
import org.geotools.factory.Hints;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.geotools.styling.SelectedChannelType;
import org.geotools.util.SimpleInternationalString;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.util.InternationalString;

/**
 * This  {@link CoverageProcessingNode}  has been built for taking care of the band select operation within the context of applying SLD 1.0.
 * @author  Simone Giannecchini, GeoSolutions.
 */
class BandSelectionNode extends StyleVisitorCoverageProcessingNodeAdapter
		implements CoverageProcessingNode {

	/*
	 * (non-Javadoc)
	 * @see CoverageProcessingNode#getName() 
	 */
	public InternationalString getName() {
		return Vocabulary.formatInternational(VocabularyKeys.BAND_SELECTION);
	}
	
	/**
     * Index of the band we want to select.
     * @uml.property  name="bandIndex"
     */
	private int bandIndex = -1;

	/**
	 * Default constructor.
	 * 
	 * <p>
	 * A band selection node can have at most one source node.
	 */
	public BandSelectionNode() {
		this(null);
	}

	/**
	 * Default constructor.
	 * 
	 * <p>
	 * A band selection node can have at most one source node.
	 * 
	 * @param hints
	 *            {@link Hints} to contro internal machinery for factories,
	 */
	public BandSelectionNode(Hints hints) {
		super(
				1,
				hints,
				SimpleInternationalString.wrap("BandSelectionNode"),
				SimpleInternationalString
						.wrap("Node which applies a BandSelection following SLD 1.0 spec."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.BaseCoverageProcessingNode#execute()
	 */
	protected GridCoverage2D execute() {
		// preconditions
		assert Thread.holdsLock(this);
		assert this.getSources().size() <= 1;

		// /////////////////////////////////////////////////////////////////////
		//
		// Get the sources and see what we got to do. Note that if we have more
		// than once source we'll use only the first one but we'll
		//
		// /////////////////////////////////////////////////////////////////////
		final List<CoverageProcessingNode> sources = this.getSources();
		if (sources != null && !sources.isEmpty()) {
			final GridCoverage2D source = (GridCoverage2D) getSource(0).getOutput();
			ensureSourceNotNull(source, this.getName().toString());
			GridCoverage2D output = null;
			if (bandIndex != -1) {
				// /////////////////////////////////////////////////////////////////////
				//
				// We have a valid band index, go ahead and to a band select
				//
				// /////////////////////////////////////////////////////////////////////

				// //
				//
				// Is the index correct?
				//
				// //
				final int numSampleDimensions = source.getNumSampleDimensions();
				if (bandIndex < 1 || bandIndex > numSampleDimensions)
					throw new IllegalArgumentException(
							Errors.format(ErrorKeys.BAD_BAND_NUMBER_$1,Integer.valueOf(bandIndex)));

				// //
				//
				// SHORTCUT
				//
				// Are we trying to do a band select on a single band coverage?
				// Note that we already checked that the index is valid.
				//
				// //
				if (bandIndex == 1 && numSampleDimensions == 1) {
					output = source;

				} else {
					// //
					//
					// Do the actual band selection.
					//
					// //
					final CoverageProcessor processor = new CoverageProcessor(
							this.getHints());
					// get the source
					final ParameterValueGroup parameters = processor
							.getOperation("SelectSampleDimension")
							.getParameters();
					parameters.parameter("SampleDimensions").setValue(
							new int[] { bandIndex - 1 });
					parameters.parameter("Source").setValue(source);

					// //
					//
					// Save the output
					//
					// //
					final Hints hints = new Hints(getHints());
					final ImageLayout layout = new ImageLayout();
					final RenderedImage sourceRaster = source
							.getRenderedImage();
					final SampleModel oldSM = sourceRaster.getSampleModel();
					final ColorModel cm = new ComponentColorModel(ColorSpace
							.getInstance(ColorSpace.CS_GRAY), false, false,
							Transparency.OPAQUE, oldSM.getDataType());
					layout.setColorModel(cm);
					layout.setSampleModel(cm.createCompatibleSampleModel(oldSM
							.getWidth(), oldSM.getHeight()));
					hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
					output = (GridCoverage2D) new SelectSampleDimension()
							.doOperation(parameters, hints);
				}

				// postcondition
				assert output.getNumSampleDimensions() == 1;
			} else
				// /////////////////////////////////////////////////////////////////////
				//
				// We do not have a valid band index, let's try with a
				// conservative approach that is, let's forward the source
				// coverage intact.
				// TODO better throwing an error?
				// /////////////////////////////////////////////////////////////////////
				output = source;


			return output;

		}
		throw new IllegalStateException("No source was set for this Node.");

	}

	public synchronized void visit(SelectedChannelType sct) {
		// /////////////////////////////////////////////////////////////////////
		//
		// If a SelectedChannelType was provided, let's try to parse it.
		//
		// /////////////////////////////////////////////////////////////////////
		if (sct != null) {
			final String name = sct.getChannelName();
			try {
				bandIndex = Integer.parseInt(name);
			} catch (NumberFormatException e) {
				// something bad happened
				final IllegalArgumentException iee = new IllegalArgumentException(
						Errors.format(ErrorKeys.BAD_BAND_NUMBER_$1,Integer.valueOf(bandIndex)));
				iee.initCause(e);
				throw iee;
			}
			return;
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// If no SelectedChannelType was provided, let's just forward the
		// source.
		//
		// /////////////////////////////////////////////////////////////////////
		// do nothing
	}

	/**
     * Retrieves the band index we use for this  {@link BandSelectionNode} . <p> <code>-1</code> warns about a possible error.
     * @return  the bandIndex
     * @uml.property  name="bandIndex"
     */
	public int getBandIndex() {
		return bandIndex;
	}

}
