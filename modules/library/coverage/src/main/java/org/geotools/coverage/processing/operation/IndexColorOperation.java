/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.text.MessageFormat;
import javax.media.jai.ImageLayout;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import org.geotools.api.coverage.Coverage;
import org.geotools.api.coverage.SampleDimension;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.Operation2D;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.util.ColorUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.util.Classes;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;

/**
 * Operation applied only on image's colors. This operation work only for source image using an {@link IndexColorModel}.
 *
 * @version $Id$
 * @author Martin Desruisseaux
 * @todo Consider moving this class to the {@link org.geotools.coverage.processing} package.
 */
abstract class IndexColorOperation extends Operation2D {
    /** Constructs an operation. */
    public IndexColorOperation(final DefaultParameterDescriptorGroup descriptor) {
        super(descriptor);
    }

    /**
     * Performs the color transformation. This method invokes the {@link #transformColormap transformColormap(...)}
     * method with current RGB colormap, the source {@link SampleDimension} and the supplied parameters.
     *
     * @param parameters The parameters.
     * @param hints Rendering hints (ignored in this implementation).
     * @throws IllegalArgumentException if the candidate image do not use an {@link IndexColorModel}.
     */
    @Override
    public Coverage doOperation(final ParameterValueGroup parameters, final Hints hints) {
        final GridCoverage2D source =
                (GridCoverage2D) parameters.parameter("Source").getValue();
        final GridCoverage2D visual = source;
        final RenderedImage image = visual.getRenderedImage();
        final GridSampleDimension[] bands = visual.getSampleDimensions();
        final int visibleBand = CoverageUtilities.getVisibleBand(image);
        ColorModel model = image.getColorModel();
        boolean colorChanged = false;
        for (int i = 0; i < bands.length; i++) {
            /*
             * Extracts the ARGB codes from the IndexColorModel and invokes the
             * transformColormap(...) method, which needs to be defined by subclasses.
             */
            GridSampleDimension band = bands[i];
            final ColorModel candidate = i == visibleBand ? image.getColorModel() : band.getColorModel();
            if (!(candidate instanceof IndexColorModel)) {
                // Current implementation supports only sources that use an index color model.
                throw new IllegalArgumentException(MessageFormat.format(
                        ErrorKeys.ILLEGAL_CLASS_$2, Classes.getClass(candidate), IndexColorModel.class));
            }
            final IndexColorModel colors = (IndexColorModel) candidate;
            final int mapSize = colors.getMapSize();
            final int[] ARGB = new int[mapSize];
            colors.getRGBs(ARGB);
            band = transformColormap(ARGB, i, band, parameters);
            /*
             * Checks if there is any change, either as a new GridSampleDimension instance or in
             * the ARGB array. Note that if the new GridSampleDimension is equals to the old one,
             * then the new one will be discarted since the old one is more likely to be a shared
             * instance.
             */
            if (!bands[i].equals(band)) {
                bands[i] = band;
                colorChanged = true;
            } else if (!colorChanged) {
                for (int j = 0; j < mapSize; j++) {
                    if (ARGB[j] != colors.getRGB(j)) {
                        colorChanged = true;
                        break;
                    }
                }
            }
            /*
             * If we changed the color of the visible band, then create immediately a new
             * color model for this band. The new color model will be given later to the
             * image operator.
             */
            if (colorChanged && i == visibleBand) {
                model = ColorUtilities.getIndexColorModel(ARGB, bands.length, visibleBand);
            }
        }
        if (!colorChanged) {
            return source;
        }
        /*
         * Gives the color model to the image layout and creates a new image using the Null
         * operation, which merely propagates its first source along the operation chain
         * unmodified (except for the ColorModel given in the layout in this case).
         */
        final ImageLayout layout = new ImageLayout().setColorModel(model);
        final RenderedImage newImage = new NullOpImage(image, layout, null, OpImage.OP_COMPUTE_BOUND);
        final GridCoverage2D target = CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints())
                .create(
                        visual.getName(),
                        newImage,
                        visual.getCoordinateReferenceSystem2D(),
                        visual.getGridGeometry().getGridToCRS(),
                        bands,
                        new GridCoverage[] {visual},
                        null);

        return target;
    }

    /**
     * Transforms the supplied RGB colors. This method is automatically invoked by {@link #doOperation(ParameterList)}
     * for each band in the source {@link GridCoverage2D}. The {@code ARGB} array contains the ARGB values from the
     * current source and should be overridden with new ARGB values for the destination image.
     *
     * @param ARGB Alpha, Red, Green and Blue components to transform.
     * @param band The band number, from 0 to the number of bands in the image -1.
     * @param sampleDimension The sample dimension of band {@code band}.
     * @param parameters The user-supplied parameters.
     * @return A sample dimension identical to {@code sampleDimension} except for the colors. Subclasses may
     *     conservatively returns {@code sampleDimension}.
     */
    protected abstract GridSampleDimension transformColormap(
            final int[] ARGB,
            final int band,
            final GridSampleDimension sampleDimension,
            final ParameterValueGroup parameters);
}
