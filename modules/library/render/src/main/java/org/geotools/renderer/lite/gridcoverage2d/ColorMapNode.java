/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.jaiext.classifier.ColorMapTransform;
import it.geosolutions.jaiext.classifier.LinearColorMap;
import it.geosolutions.jaiext.classifier.LinearColorMapElement;
import it.geosolutions.jaiext.piecewise.Domain1D;
import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.Color;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.RenderedOp;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.renderer.i18n.Vocabulary;
import org.geotools.renderer.i18n.VocabularyKeys;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.StyleVisitor;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.InternationalString;

/**
 * This {@link CoverageProcessingNode} is responsible for visiting the supplied {@link
 * ColorMapTransform} and applying it to the source {@link GridCoverage2D} .
 *
 * <p><strong>What we support and how do we implement it</strong>
 *
 * <p>A ColorMapTransform is created in order to map categories to colors on a single band coverage
 * (or on the visible band of multiband coverage).
 *
 * <p>In this implementation we allow users to use either 256 or 65536 colors via the creation of a
 * paletted image with s suitable palette derived from the single {@link ColorMapEntry} that make up
 * the {@link ColorMapTransform} .
 *
 * @author Simone Giannecchini, GeoSolutions
 * @see ColorMapTransform
 */
class ColorMapNode extends StyleVisitorCoverageProcessingNodeAdapter
        implements StyleVisitor, CoverageProcessingNode {

    public static int TYPE_NONE = -1;
    /*
     * (non-Javadoc)
     * @see CoverageProcessingNode#getName()
     */
    public InternationalString getName() {
        return Vocabulary.formatInternational(VocabularyKeys.COLOR_MAP);
    }

    /**
     * Stores the type of {@link ColorMapTransform} we want to use for this node.
     *
     * <p>Possible types are
     *
     * <ol>
     *   <li>TYPE_RAMP which will build a ramp f colors using linear interpolation
     *   <li>TYPE_INTERVALS which would do classifications, i.e. would map intervals to colors
     *   <li>TYPE_VALUES, which would do single value slicing
     * </ol>
     */
    private int type = TYPE_NONE;

    /**
     * The {@link Domain1D} that we build while parsing the various {@link ColorMapEntry}s provided
     * by the {@link ColorMapTransform} we visit. This {@link LinearColorMap} we'll help us buld the
     * colormapped image.
     */
    private LinearColorMap colorMapTransform;

    /**
     * Do we want 16 bits or 8 bits colormap?
     *
     * @uml.property name="extendedColors"
     */
    private boolean extendedColors;

    /**
     * Visits the provided {@link ColorMapTransform} and build up a {@link Domain1D} for later
     * creation of a palette rendering for this coverage.
     */
    public void visit(ColorMap colorMap) {
        // //
        //
        // This allows this node to work in store-and-forward way in case we had
        // nothing to do
        //
        // //
        if (colorMap == null) return;

        // /////////////////////////////////////////////////////////////////////
        //
        // Get the type and check it. In case the type or the entries are
        // invalid we skip this node.
        //
        // /////////////////////////////////////////////////////////////////////
        this.type = colorMap.getType();
        this.extendedColors = colorMap.getExtendedColors();
        final ColorMapEntry[] cmEntries = colorMap.getColorMapEntries();
        if (cmEntries != null && cmEntries.length > 0) {

            // /////////////////////////////////////////////////////////////////////
            //
            // Check the source coverage and, if it has more than one band
            // reduce it to 1 band using the visible band.
            //
            // If do not manage to do so, let's throw an exception since as per
            // Craig Bruce email it is an error to apply a ColoMap to a
            // multiband coverage.
            //
            // /////////////////////////////////////////////////////////////////////
            final CoverageProcessingNode source = getSource(0);
            GridCoverageRendererUtilities.ensureSourceNotNull(source, "ColorMapNode");
            final GridCoverage2D sourceCoverage = (GridCoverage2D) source.getOutput();
            GridCoverageRendererUtilities.ensureSourceNotNull(sourceCoverage, "ColorMapNode");
            final int numSD = sourceCoverage.getNumSampleDimensions();
            if (numSD > 1) {
                // check it, is it gray + alpha?
                ColorModel cm = sourceCoverage.getRenderedImage().getColorModel();
                if (!cm.hasAlpha() && cm.getNumColorComponents() != 2) {
                    // let's check, it might
                    throw new IllegalArgumentException(
                            Errors.format(ErrorKeys.BAD_BAND_NUMBER_$1, Integer.valueOf(numSD)));
                }
            }
            // /////////////////////////////////////////////////////////////////////
            //
            // Check the sample dimension we are going to use for NoData
            // categories.
            //
            // It is important to have such categories since we might have holes
            // in the categories we are going to build hence it is important to
            // have a valid NoDataValue that we can use.
            //
            // /////////////////////////////////////////////////////////////////////
            final GridSampleDimension candidateSD = sourceCoverage.getSampleDimension(0);
            double[] candidateNoDataValues = preparaNoDataValues(candidateSD);

            // /////////////////////////////////////////////////////////////////////
            //
            // Main Loop
            //
            // /////////////////////////////////////////////////////////////////////
            // TODO MAKE THE COLORS CONFIGURABLE
            final SLDColorMapBuilder builder = new SLDColorMapBuilder();
            builder.setExtendedColors(this.extendedColors)
                    .setLinearColorMapType(this.type)
                    .setNumberColorMapEntries(cmEntries.length)
                    .setColorForValuesToPreserve(new Color(0, 0, 0, 0))
                    .setGapsColor(new Color(0, 0, 0, 0));
            for (int i = 0; i < cmEntries.length; i++) {
                builder.addColorMapEntry(cmEntries[i]);
            }

            // /////////////////////////////////////////////////////////////////////
            //
            // Create the list of no data colorMapTransform domain elements. Note that all of them
            //
            // /////////////////////////////////////////////////////////////////////
            if (candidateNoDataValues != null && candidateNoDataValues.length > 0) {
                final LinearColorMapElement noDataCategories[] =
                        new LinearColorMapElement[candidateNoDataValues.length];
                for (int i = 0; i < noDataCategories.length; i++) {
                    builder.addValueToPreserve(candidateNoDataValues[i]);
                }
            }

            // /////////////////////////////////////////////////////////////////////
            //
            // Create the list of colorMapTransform categories
            //
            // /////////////////////////////////////////////////////////////////////
            colorMapTransform = builder.buildLinearColorMap();

        } else this.type = TYPE_NONE;
    }

    /** */
    private static double[] preparaNoDataValues(final GridSampleDimension candidateSD)
            throws IllegalStateException {
        double[] candidateNoDataValues = candidateSD.getNoDataValues();
        // if no nodata categories are ready we'll add a fictitious one
        // @todo TODO make this code configurable
        if (candidateNoDataValues == null) {
            candidateNoDataValues = new double[1];
            final SampleDimensionType sdType = candidateSD.getSampleDimensionType();
            final int dataBufferType = TypeMap.getDataBufferType(sdType);
            switch (dataBufferType) {
                case DataBuffer.TYPE_SHORT:
                    candidateNoDataValues[0] = Short.MIN_VALUE;
                    break;
                case DataBuffer.TYPE_INT:
                    candidateNoDataValues[0] = Integer.MIN_VALUE;
                    break;
                case DataBuffer.TYPE_FLOAT:
                    candidateNoDataValues[0] = Float.NaN;
                    break;
                case DataBuffer.TYPE_DOUBLE:
                case DataBuffer.TYPE_UNDEFINED:
                    candidateNoDataValues[0] = Double.NaN;
                    break;
                default: // BYTE, USHORT
                    candidateNoDataValues = null;
                    break;
            }
        }
        return candidateNoDataValues;
    }

    /** Default constructor for the {@link ColorMapNode} processing node. */
    public ColorMapNode() {
        this(null);
    }

    /** Default constructor for the {@link ColorMapNode} processing node. */
    public ColorMapNode(Hints hints) {
        super(
                1,
                hints,
                SimpleInternationalString.wrap("ColorMapNode"),
                SimpleInternationalString.wrap(
                        "Node which applies a ColorMapTransform following SLD 1.0 spec."));
    }

    /**
     * Note that the color map can be applied only to a single band hence, in principle, applying
     * the {@link ColorMapTransform} element to a coverage with more than one band is an error.
     */
    protected GridCoverage2D execute() {
        ///////////////////////////////////////////////////////////////////
        //
        // get the source for this node and ensure it is correct
        //
        ///////////////////////////////////////////////////////////////////
        final CoverageProcessingNode sourceNode = getSource(0);
        GridCoverageRendererUtilities.ensureSourceNotNull(sourceNode, this.getName().toString());
        final GridCoverage2D sourceCoverage = (GridCoverage2D) sourceNode.getOutput();
        GridCoverageRendererUtilities.ensureSourceNotNull(
                sourceCoverage, this.getName().toString());

        ///////////////////////////////////////////////////////////////////
        //
        // now apply the colormap if one exists
        //
        ///////////////////////////////////////////////////////////////////
        if (colorMapTransform != null) {
            // get input image
            final RenderedImage sourceImage = sourceCoverage.getRenderedImage();
            GridCoverageRendererUtilities.ensureSourceNotNull(
                    sourceImage, this.getName().toString());
            // prepare the colorMapTransform operation
            ImageWorker w = new ImageWorker(sourceImage);
            if (w.getNumBands() == 2) {
                w.retainBands(1);
            }
            w.setROI(CoverageUtilities.getROIProperty(sourceCoverage));
            NoDataContainer noDataProperty = CoverageUtilities.getNoDataProperty(sourceCoverage);
            w.setNoData(noDataProperty != null ? noDataProperty.getAsRange() : null);
            w.classify(colorMapTransform, null);
            final RenderedOp classified =
                    w
                            .getRenderedOperation(); // JAI.create(RasterClassifierOpImage.OPERATION_NAME,pbj);

            ////
            //
            // prepare the output coverage by specifying its bands
            //
            ////
            final int outputChannels = classified.getColorModel().getNumComponents();
            final int numBands = classified.getSampleModel().getNumBands();
            assert outputChannels == 1 || outputChannels == 3 || outputChannels == 4;
            final GridSampleDimension[] sd = new GridSampleDimension[numBands];
            for (int i = 0; i < numBands; i++)
                sd[i] =
                        new GridSampleDimension(
                                TypeMap.getColorInterpretation(classified.getColorModel(), i)
                                        .name());

            ////
            //
            // Create the the output coverage by preserving its gridgeometry and its bands
            //
            ////
            Map properties = sourceCoverage.getProperties();
            if (properties == null) {
                properties = new HashMap<>();
            }
            CoverageUtilities.setROIProperty(properties, w.getROI());
            CoverageUtilities.setNoDataProperty(properties, w.getNoData());

            return getCoverageFactory()
                    .create(
                            "color_mapped_" + sourceCoverage.getName().toString(),
                            classified,
                            sourceCoverage.getGridGeometry(),
                            sd,
                            new GridCoverage[] {sourceCoverage},
                            properties);
        }
        return sourceCoverage;
    }

    /**
     * It tells me whether or not we are using extended colors for this colormap.
     *
     * @return the extendedColors
     * @uml.property name="extendedColors"
     */
    public boolean isExtendedColors() {
        return extendedColors;
    }

    public int getType() {
        return type;
    }
}
