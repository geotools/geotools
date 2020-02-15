/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import javax.media.jai.Interpolation;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.renderer.composite.BlendComposite;
import org.geotools.renderer.composite.BlendComposite.BlendingMode;
import org.opengis.coverage.grid.GridCoverage;

/**
 * A compositing element used in rendering.
 *
 * <p>It may specify a Simple {@link BlendComposite} instance to define the type of compositing to
 * be applied to the coverage holding this property. It may also specify a compositing image to be
 * drawn on the current rendering as part of the compositing process.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
class Compositing {

    private static final Composite DEFAULT_COMPOSITE =
            BlendComposite.getInstance(BlendingMode.MULTIPLY, 1.0f);

    /** IDENTITY */
    private static final AffineTransform IDENTITY = AffineTransform2D.getTranslateInstance(0, 0);

    /** The compositing image to be used in compositing */
    private RenderedImage compositingImage;

    /** The type of {@link Composite} to be used in compositing */
    private Composite composite;

    public Compositing(RenderedImage compositingImage) {
        this.compositingImage = compositingImage;
        this.composite = DEFAULT_COMPOSITE;
    }

    /** Compose this {@link Compositing} element with the source GridCoverage. */
    public GridCoverage2D composeGridCoverage(GridCoverage2D source, GridCoverageFactory factory) {
        if (compositingImage != null) {
            // Make sure to transform the compositingImage to RGB
            RenderedImage applyImage = forceToRGB((RenderedImage) compositingImage, true);
            RenderedImage sourceImage = forceToRGB(source.getRenderedImage(), true);

            // TODO: Setup a JAI operation to do the composition without need
            // to use Graphics2D which requires a BufferedImage which loads all data
            // in memory
            BufferedImage composedImage =
                    PlanarImage.wrapRenderedImage(sourceImage).getAsBufferedImage();

            // prepare for composition
            Graphics2D graphics = (Graphics2D) composedImage.createGraphics();
            graphics.setComposite(composite);

            // BufferedImage always have minX,minY = 0,0
            final int minX = sourceImage.getMinX();
            final int minY = sourceImage.getMinY();
            AffineTransform transform = IDENTITY;
            boolean translate = false;
            if (minX != 0 || minY != 0) {
                // I'm going to draw a RenderedImage on the BufferedImage source which has (0,0)
                // origin.
                transform = AffineTransform2D.getTranslateInstance(-minX, -minY);
                translate = true;
            }
            graphics.drawRenderedImage(applyImage, transform);
            int numBands = composedImage.getSampleModel().getNumBands();
            GridSampleDimension[] sd = new GridSampleDimension[numBands];
            for (int i = 0; i < numBands; i++) {
                sd[i] =
                        new GridSampleDimension(
                                TypeMap.getColorInterpretation(composedImage.getColorModel(), i)
                                        .name());
            }
            RenderedImage gridCoverageImage = composedImage;
            if (translate) {
                AffineTransform tx = AffineTransform2D.getTranslateInstance(minX, minY);
                ImageWorker worker =
                        new ImageWorker(composedImage)
                                .affine(
                                        tx,
                                        Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                                        null);
                gridCoverageImage = worker.getRenderedImage();
                gridCoverageImage.getMinX();
            }
            // Return an updatedGridCoverage with composed image
            return factory.create(
                    source.getName().toString(),
                    gridCoverageImage,
                    (GridGeometry2D) source.getGridGeometry(),
                    sd,
                    new GridCoverage[] {source},
                    source.getProperties());
        }
        return source;
    }

    /** BlendCompositing requires the images to be composed are RGB */
    public static RenderedImage forceToRGB(RenderedImage finalImage, boolean asByte) {
        if (finalImage != null) {
            SampleModel sm = finalImage.getSampleModel();
            final int sampleSize = sm.getSampleSize(0);
            ImageWorker iw = new ImageWorker(finalImage);
            if (sampleSize > 8 && asByte) {
                iw = iw.rescaleToBytes();
                finalImage = iw.getRenderedImage();
            }
            ColorModel cm = finalImage.getColorModel();
            int numComponents = cm.getNumComponents();
            if (!(cm instanceof ComponentColorModel) || numComponents < 3) {
                if (cm instanceof IndexColorModel) {
                    iw = iw.forceComponentColorModel();
                } else {
                    iw = iw.bandMerge(3);
                }
                // We need to go to RGB for Blending
                iw = iw.forceColorSpaceRGB();
                finalImage = iw.getRenderedImage();
            }
        }
        return finalImage;
    }
}
