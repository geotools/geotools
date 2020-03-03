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

import it.geosolutions.jaiext.range.Range;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.ROI;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.ShadedRelief;
import org.geotools.styling.StyleVisitor;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.filter.expression.Expression;

/**
 * A helper class for rendering {@link GridCoverage} objects. It supports almost all
 * RasterSymbolizer options.
 *
 * @author Simone Giannecchini, GeoSolutions
 */
public class RasterSymbolizerHelper extends SubchainStyleVisitorCoverageProcessingAdapter
        implements StyleVisitor {

    private float opacity = 1.0F;

    /**
     * We are hacking here a solutions for whenever the user either did not specify a style or did
     * specify a bad one and the resulting image seems not be drawable.
     *
     * @return {@link GridCoverage2D} the result of this operation
     */
    public GridCoverage2D execute() {
        ///////////////////////////////////////////////////////////////////////
        //
        // We get the geophysics view of this coverage and we check if we give away
        // something that can visualized directly. If the final data type is still floating
        // point numbers (no direct rendering) we do our best to cope with it doing a local
        //
        ///////////////////////////////////////////////////////////////////////
        // get the output coverage and its RenderedImage
        final GridCoverage2D output = (GridCoverage2D) super.execute();
        //		final GridCoverage2D outputNonGeo=output.geophysics(false);
        //		//if we have an index color model we are ok, this way we preserve the non geo view or
        // whatever it is now called
        //		if(outputNonGeo.getRenderedImage().getColorModel() instanceof IndexColorModel)
        //			return output;
        RenderedImage outputImage = output.getRenderedImage();

        // Getting NoData
        Range nodata =
                CoverageUtilities.getNoDataProperty(output) != null
                        ? CoverageUtilities.getNoDataProperty(output).getAsRange()
                        : null;
        ROI roiProp = CoverageUtilities.getROIProperty(output);

        ///////////////////////////////////////////////////////////////////////
        //
        // We are in the more general case hence it might be that we have
        // an image with too many bands and a bogus color space or an image with
        // a data type that is not drawable itself. We have to try and do our
        // best in order to show something as quickly as possible.
        //
        ///////////////////////////////////////////////////////////////////////
        // let's check the number of bands
        final SampleModel outputImageSampleModel = outputImage.getSampleModel();
        int numBands = outputImageSampleModel.getNumBands();
        final int dataType = outputImageSampleModel.getDataType();
        GridSampleDimension sd[];
        if (numBands > 4) {
            // get the visible band
            final int visibleBand = CoverageUtilities.getVisibleBand(outputImage);
            outputImage =
                    new ImageWorker(outputImage)
                            .setRenderingHints(this.getHints())
                            .retainBands(new int[] {visibleBand})
                            .getRenderedImage();
            sd =
                    new GridSampleDimension[] {
                        (GridSampleDimension) output.getSampleDimension(visibleBand)
                    };
        } else {
            sd = output.getSampleDimensions();
        }

        // more general case, let's check the data type and let go only USHORT and BYTE
        // TODO I am not sure this will work with multipacked types (using INT for an RGB as an
        // instance)
        // TODO should we go to component color model also?
        // TODO use JAI TOOLS statistics and ignore no data properly.
        switch (dataType) {
                // in case the original image has a USHORT pixel type without being associated
                // with an index color model I would still go to 8 bits
            case DataBuffer.TYPE_USHORT:
                if (outputImage.getColorModel() instanceof IndexColorModel) {
                    break;
                }
            case DataBuffer.TYPE_DOUBLE:
            case DataBuffer.TYPE_FLOAT:
            case DataBuffer.TYPE_INT:
            case DataBuffer.TYPE_SHORT:
                // rescale to byte
                ImageWorker w = new ImageWorker(outputImage);
                outputImage =
                        w.setROI(roiProp)
                                .setNoData(nodata)
                                .setRenderingHints(this.getHints())
                                .rescaleToBytes()
                                .getRenderedImage();
                roiProp = w.getROI();
                nodata = w.getNoData();
        }

        // ///////////////////////////////////////////////////////////////////
        // Apply opacity if needed
        // ///////////////////////////////////////////////////////////////////
        final RenderedImage finalImage;
        Map properties = output.getProperties();
        if (properties == null) {
            properties = new HashMap<>();
        }
        CoverageUtilities.setNoDataProperty(properties, nodata);
        CoverageUtilities.setROIProperty(properties, roiProp);
        if (opacity < 1) {
            ImageWorker ow = new ImageWorker(outputImage);
            ow.setROI(roiProp);
            ow.setNoData(nodata);
            finalImage = ow.applyOpacity(opacity).getRenderedImage();

            numBands = finalImage.getSampleModel().getNumBands();
            sd = new GridSampleDimension[numBands];
            for (int i = 0; i < numBands; i++) {
                sd[i] =
                        new GridSampleDimension(
                                TypeMap.getColorInterpretation(finalImage.getColorModel(), i)
                                        .name());
            }

            CoverageUtilities.setNoDataProperty(properties, ow.getNoData());
            CoverageUtilities.setROIProperty(properties, ow.getROI());
            // create a new grid coverage but preserve as much input as possible
            return this.getCoverageFactory()
                    .create(
                            output.getName(),
                            finalImage,
                            (GridGeometry2D) output.getGridGeometry(),
                            sd,
                            new GridCoverage[] {output},
                            properties);
        }

        // create a new grid coverage but preserve as much input as possible
        return this.getCoverageFactory()
                .create(
                        output.getName(),
                        outputImage,
                        (GridGeometry2D) output.getGridGeometry(),
                        sd,
                        new GridCoverage[] {output},
                        properties);
    }

    /** */
    public RasterSymbolizerHelper(GridCoverage2D sourceCoverage, Hints hints) {
        super(
                1,
                hints,
                SimpleInternationalString.wrap(
                        Vocabulary.format(VocabularyKeys.RASTER_SYMBOLIZER_HELPER)),
                SimpleInternationalString.wrap(
                        "Simple Coverage Processing Node for RasterSymbolizerHelper"));

        // add a source that will just give me back my gridcoverage
        this.addSource(new RootNode(sourceCoverage, hints));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.renderer.lite.gridcoverage2d.StyleVisitorAdapter#visit(org.geotools.styling.RasterSymbolizer)
     */
    public void visit(RasterSymbolizer rs) {

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

        CoverageProcessingNode currNode;
        CoverageProcessingNode prevNode = this.getSource(0);

        // /////////////////////////////////////////////////////////////////////
        //
        // CHANNEL SELECTION
        //
        // /////////////////////////////////////////////////////////////////////

        final ChannelSelectionNode csNode = new ChannelSelectionNode();
        currNode = csNode;

        currNode.addSource(prevNode);
        prevNode = currNode;

        final ChannelSelection cs = rs.getChannelSelection();
        csNode.visit(cs);

        // /////////////////////////////////////////////////////////////////////
        //
        // COLOR MAP
        //
        // /////////////////////////////////////////////////////////////////////

        final ColorMapNode cmNode = new ColorMapNode(this.getHints());
        currNode = cmNode;

        currNode.addSource(prevNode);
        prevNode.addSink(currNode);
        prevNode = currNode;

        final ColorMap cm = rs.getColorMap();
        cmNode.visit(cm);

        // /////////////////////////////////////////////////////////////////////
        //
        // CONTRAST ENHANCEMENT
        //
        // /////////////////////////////////////////////////////////////////////

        final ContrastEnhancementNode ceNode = new ContrastEnhancementNode(this.getHints());
        currNode = ceNode;

        currNode.addSource(prevNode);
        prevNode.addSink(currNode);
        prevNode = currNode;

        final ContrastEnhancement ce = rs.getContrastEnhancement();
        ceNode.visit(ce);

        // /////////////////////////////////////////////////////////////////////
        //
        // SHADED RELIEF
        //
        // /////////////////////////////////////////////////////////////////////

        final ShadedReliefNode srNode = new ShadedReliefNode(this.getHints());
        final ShadedRelief sr = rs.getShadedRelief();
        srNode.visit(sr);
        currNode = srNode;

        // TODO: Think about ContrastEnhancement and shadedRelief conflicts
        // signal them through an Exception
        boolean applyShadedRelief = !Double.isNaN(srNode.getReliefFactor());
        boolean applyContrastEnhancement = ceNode.getType() != null;
        if (applyShadedRelief && applyContrastEnhancement) {
            throw new IllegalArgumentException(
                    "ContrastEnhancement and ShadedRelief can't be applied at the same time. ");
        }

        if (applyShadedRelief) {
            // not the usual prev/curr links here, since SR does not need CE processing
            currNode.addSource(cmNode);
            prevNode.addSink(currNode);
            setSink(currNode);

        } else {

            setSink(prevNode);
        }

        /////////////////////////////////////////////////////////////////////
        //
        // OPACITY
        //
        /////////////////////////////////////////////////////////////////////
        final Expression op = rs.getOpacity();
        if (op != null) {
            final Number number = (Number) op.evaluate(null, Float.class);
            if (number != null) {
                opacity = number.floatValue();
            }
        }
    }
}
