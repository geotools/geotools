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

import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.factory.Hints;
import org.geotools.image.ImageWorker;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.StyleVisitor;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.grid.GridCoverage;

/**
 * A helper class for rendering {@link GridCoverage} objects. It supports almost
 * all RasterSymbolizer options.
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 * @source $URL$
 */
public class RasterSymbolizerHelper extends
		SubchainStyleVisitorCoverageProcessingAdapter implements StyleVisitor {

	/**
	 * We are hacking here a solutions for whenever the user either did not
	 * specify a style or did specify a bad one and the resulting image seems
	 * not be drawable.
	 * @return {@link GridCoverage2D} the result of this operation
	 */
	public synchronized GridCoverage2D execute() {
		///////////////////////////////////////////////////////////////////////
		//
		// We get the non-geophysics view of this coverage in order to try to
		// preserve as much as possible the native color maps applied at reading
		// time. This is especially true if we do not want to use the
		// RasterSymbolizer capabilities; to achieve this we can just use an
		// empty raster symbolizer element.
		//
		///////////////////////////////////////////////////////////////////////
		//get the output coverage and its RenderedImage
		final GridCoverage2D output= (GridCoverage2D) super.execute();
		final GridCoverage2D outputNonGeo=output.geophysics(false);
		//if we have an index color model we are ok, this way we preserve the non geo view or whatever it is now called
		if(outputNonGeo.getRenderedImage().getColorModel() instanceof IndexColorModel)
			return output;
		RenderedImage outputImage=output.getRenderedImage();
		

		
		///////////////////////////////////////////////////////////////////////
		//
		// We are in the more general case hence it might be that we have
		// an image with too many bands and a bogus color space or an image with
		// a data type that is not drawable itself. We have to try and do our
		// best in order to show something as quickly as possible.
		//
		///////////////////////////////////////////////////////////////////////
		//let's check the number of bands
		final SampleModel outputImageSampleModel= outputImage.getSampleModel();
		final int numBands=outputImageSampleModel.getNumBands();
		final int dataType= outputImageSampleModel.getDataType();
		final GridSampleDimension sd[];
		if(numBands>4)
		{
			//get the visible band
			final int visibleBand=CoverageUtilities.getVisibleBand(outputImage);
			outputImage=
				new ImageWorker(outputImage).setRenderingHints(this.getHints()).retainBands(new int[]{visibleBand}).getRenderedImage();
			sd=new GridSampleDimension[]{(GridSampleDimension) output.getSampleDimension(visibleBand)};
		}
		else
			sd=output.getSampleDimensions();

		//more general case, let's check the data type
		switch(dataType){
		case DataBuffer.TYPE_DOUBLE:
			case DataBuffer.TYPE_FLOAT:
			case DataBuffer.TYPE_INT:
			case DataBuffer.TYPE_SHORT:
			//rescale to byte
			outputImage=
				new ImageWorker(outputImage).setRenderingHints(this.getHints()).rescaleToBytes().getRenderedImage();
				
		}
		//create a new grid coverage but preserve as much input as possible
		return this.getCoverageFactory().create(output.getName(), outputImage,(GridGeometry2D)output.getGridGeometry(),sd, new GridCoverage[]{output},output.getProperties());
	}


	/**
	 * 
	 * @param sourceCoverage
	 * @param hints
	 */
	public RasterSymbolizerHelper(GridCoverage2D sourceCoverage, Hints hints) {
		super(
				1,
				hints,
				SimpleInternationalString.wrap(Vocabulary.format(VocabularyKeys.RASTER_SYMBOLIZER_HELPER)),
				SimpleInternationalString
						.wrap("Simple Coverage Processing Node for RasterSymbolizerHelper"));

		// add a source that will just give me back my gridcoverage
		this.addSource(new RootNode(sourceCoverage, hints));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.StyleVisitorAdapter#visit(org.geotools.styling.RasterSymbolizer)
	 */
	public synchronized void visit(RasterSymbolizer rs) {
	        
		ColorMapUtilities.ensureNonNull("RasterSymbolizer", rs);

		// /////////////////////////////////////////////////////////////////////
		//
		// Create the various nodes we'll use for executing this
		// RasterSymbolizer
		//
		// /////////////////////////////////////////////////////////////////////
		// the source node for the internal chains
		// final RootNode sourceNode = new RootNode(sourceCoverage, adopt,
		// hints);
		final ChannelSelectionNode csNode = new ChannelSelectionNode();
		final ColorMapNode cmNode = new ColorMapNode(this.getHints());
		final ContrastEnhancementNode ceNode = new ContrastEnhancementNode(this.getHints());
		setSink(ceNode);

		// /////////////////////////////////////////////////////////////////////
		//
		// CHANNEL SELECTION
		//
		// /////////////////////////////////////////////////////////////////////
		final ChannelSelection cs = rs.getChannelSelection();
		csNode.addSource(this.getSource(0));
		csNode.addSink(cmNode);
		csNode.visit(cs);

		// /////////////////////////////////////////////////////////////////////
		//
		// COLOR MAP
		//
		// /////////////////////////////////////////////////////////////////////
		final ColorMap cm = rs.getColorMap();
		cmNode.addSource(csNode);
		csNode.addSink(cmNode);
		cmNode.visit(cm);

		// /////////////////////////////////////////////////////////////////////
		//
		// CONTRAST ENHANCEMENT
		//
		// /////////////////////////////////////////////////////////////////////
		final ContrastEnhancement ce = rs.getContrastEnhancement();
		ceNode.addSource(cmNode);
		cmNode.addSink(ceNode);
		ceNode.visit(ce);

		//
		// //
		// /////////////////////////////////////////////////////////////////////
		// //
		// // OPACITY
		// //
		// //
		// /////////////////////////////////////////////////////////////////////
		// final Expression op = rs.getOpacity();
		// if (op != null) {
		// final Number number = (Number) op.evaluate(null, Float.class);
		// if (number != null) {
		// opacity = number.floatValue();
		// }
		// }
	}



}
