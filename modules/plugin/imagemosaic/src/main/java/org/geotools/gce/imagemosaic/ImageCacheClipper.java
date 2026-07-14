/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

/**
 * Extracts, from an already decoded image, the exact pixel window an {@link javax.imageio.ImageReader} would have
 * produced for a given source region and subsampling.
 */
final class ImageCacheClipper {

    private ImageCacheClipper() {}

    /**
     * Returns the pixels an ImageIO read with the given {@code sourceRegion} and subsampling would have decoded, per
     * {@link javax.imageio.ImageReadParam#setSourceSubsampling}: output pixel (i, j) is source pixel
     * {@code (sourceRegion.x + i * subsamplingX, sourceRegion.y + j * subsamplingY)}, no interpolation.
     *
     * <p>The result may be a new image (in case of subsampling > 1), a subimage of the original image, or the original
     * image itself.
     */
    static RenderedImage clip(BufferedImage image, Rectangle sourceRegion, int subsamplingX, int subsamplingY) {
        if (subsamplingX == 1
                && subsamplingY == 1
                && sourceRegion.x == 0
                && sourceRegion.y == 0
                && sourceRegion.width == image.getWidth()
                && sourceRegion.height == image.getHeight()) {
            return image;
        }
        // zero-copy view over the source raster, already translated so the crop's own origin is (0, 0)
        BufferedImage cropped =
                image.getSubimage(sourceRegion.x, sourceRegion.y, sourceRegion.width, sourceRegion.height);
        if (subsamplingX == 1 && subsamplingY == 1) {
            return cropped;
        }
        return subsample(cropped, subsamplingX, subsamplingY);
    }

    /** Picks every {@code subsamplingX}-th/{@code subsamplingY}-th pixel of the already-cropped {@code image}. */
    private static RenderedImage subsample(BufferedImage image, int subsamplingX, int subsamplingY) {
        int outWidth = (image.getWidth() + subsamplingX - 1) / subsamplingX;
        int outHeight = (image.getHeight() + subsamplingY - 1) / subsamplingY;

        Raster source = image.getRaster();
        SampleModel targetSampleModel = source.getSampleModel().createCompatibleSampleModel(outWidth, outHeight);
        WritableRaster target = Raster.createWritableRaster(targetSampleModel, new Point(0, 0));
        // getDataElements moves native-typed samples with no double conversion (measured 3 times faster than
        // getPixel(double[]) on average; an ImageN affine/NN op was also tried and found to be slower)
        Object element = null;
        for (int y = 0; y < outHeight; y++) {
            int sy = y * subsamplingY;
            for (int x = 0; x < outWidth; x++) {
                int sx = x * subsamplingX;
                element = source.getDataElements(sx, sy, element);
                target.setDataElements(x, y, element);
            }
        }

        ColorModel colorModel = image.getColorModel();
        return new BufferedImage(colorModel, target, colorModel.isAlphaPremultiplied(), null);
    }
}
