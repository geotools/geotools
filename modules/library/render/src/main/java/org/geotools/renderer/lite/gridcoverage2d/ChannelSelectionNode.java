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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.factory.Hints;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.StyleVisitor;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

/**
 * {@link CoverageProcessingNode} that actually implement a
 * {@link ChannelSelection} operation as stated in SLD 1.0 spec from OGC.
 * <p>
 * This node internally creates a small chain that does all that�'s needed to
 * satisfy a {@link ChannelSelection} element.
 * 
 * @author Simone Giannecchini, GeoSolutions
 */
class ChannelSelectionNode extends SubchainStyleVisitorCoverageProcessingAdapter
		implements StyleVisitor, CoverageProcessingNode {
	/** Logger for this class. */
	private final static Logger LOGGER = Logger.getLogger(ChannelSelectionNode.class.getName());

	/*
	 * (non-Javadoc)
	 * @see CoverageProcessingNode#getName() 
	 */
	public InternationalString getName() {
		return Vocabulary.formatInternational(VocabularyKeys.CHANNEL_SELECTION);
	}

	/**
	 * Default Constructor
	 */
	public ChannelSelectionNode() {
		this(null);
	}

	/**
	 * Constructor with support for {@link Hints}
	 * 
	 * @param hints
	 *            control the internal machinery for factories.
	 */
	public ChannelSelectionNode(Hints hints) {
		super(
				3,
				hints,
				SimpleInternationalString.wrap("ChannelSelectionNode"),
				SimpleInternationalString.wrap("Node which applies a ChannelSelection following SLD 1.0 spec."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.StyleVisitorAdapter#visit(org.geotools.styling.ChannelSelection)
	 */
	public synchronized void visit(final ChannelSelection cs) {
		// /////////////////////////////////////////////////////////////////////
		//
		// Ensure that the ChannelSelection is not null and that the source is
		// not null
		//
		// /////////////////////////////////////////////////////////////////////
		final List <CoverageProcessingNode>localSources = getSources();
		final int length = localSources.size();
		if (length == 0)
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.SOURCE_CANT_BE_NULL_$1, "ChannelSelectionNode"));
		final GridCoverage2D source = (GridCoverage2D) getSource(0).getOutput();
		ensureSourceNotNull(source, this.getName().toString());

		// /////////////////////////////////////////////////////////////////////
		//
		// Get the channel selection and parse it in order to create the
		// subchain
		//
		// /////////////////////////////////////////////////////////////////////
		//creating a new separate chain
		final RootNode chainSource = new RootNode(source, getHints());
		final BandMergeNode subChainSink = new BandMergeNode(getHints());
		//anchoring the chain for later disposal
		setSink(subChainSink);
		final SelectedChannelType[] rgb=cs.getRGBChannels();
		final SelectedChannelType gray=cs.getGrayChannel();
		// both of them are set?
		if((rgb!=null&&rgb[0]!=null&&rgb[1]!=null&&rgb[2]!=null)&&(gray!=null))
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.ILLEGAL_ARGUMENT_$1, "Both gray and rgb channel selection are valid!"));
		final SelectedChannelType[] sc = gray==null?rgb:new SelectedChannelType[]{gray};
		
		// If we do not really select any bands from the original coverage, we try to entirely skip this operation
		// this means that either we have to select 1 real band, or we have to select 3 real bands
		// Notice that we also try to be as resilient as possible since 
		if (sc != null && ((sc.length ==1 &&sc[0]!=null)||(sc.length ==3 &&sc[0]!=null&&sc[1]!=null&&sc[2]!=null))) {
			// //
			//
			// Note that we can either select 1 (GRAY) or 3 (RGB) bands.
			//
			// //
			if (sc.length != 3 && sc.length != 1)
				throw new IllegalArgumentException(Errors.format(
						ErrorKeys.BAD_BAND_NUMBER_$1, Integer.valueOf(sc.length)));
			for (int i = 0; i < sc.length; i++) {

				// get the channel element
				final SelectedChannelType channel = sc[i];
				if(LOGGER.isLoggable(Level.FINE))
						LOGGER.fine("Channel "+i+" was "+ channel.getChannelName());
	
				// //
				//
				// BAND SELECTION
				
				//
				// //
				final BandSelectionNode bandSelectionNode = new BandSelectionNode();
				bandSelectionNode.addSource(chainSource);
				bandSelectionNode.visit(channel);

				// //
				//
				// CONTRAST ENHANCEMENT
				//
				// //
				final ContrastEnhancementNode contrastenhancementNode = new ContrastEnhancementNode();
				contrastenhancementNode.addSource(bandSelectionNode);
				bandSelectionNode.addSink(contrastenhancementNode);
				contrastenhancementNode.visit(channel != null ? channel.getContrastEnhancement() : null);

				// //
				//
				// BAND MERGE
				//
				// //
				contrastenhancementNode.addSink(subChainSink);
				subChainSink.addSource(contrastenhancementNode);

				
			}
			return;
		} 
		// no band selection, just forward this node
		subChainSink.addSource(chainSource);

	}





}
