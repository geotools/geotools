/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.jdbc;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.concurrent.LinkedBlockingQueue;
import javax.media.jai.Interpolation;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.ImageWorker;

/**
 * this class is the base class for concrete thread classes
 *
 * @author mcr
 */
abstract class AbstractThread extends Thread {
    LinkedBlockingQueue<TileQueueElement> tileQueue;

    Config config;

    GeneralEnvelope requestEnvelope;

    Rectangle pixelDimension;

    ImageLevelInfo levelInfo;

    double rescaleX;

    double rescaleY;

    double resX;

    double resY;

    /**
     * Constructor
     *
     * @param pixelDimenison the requested pixel dimension
     * @param requestEnvelope the requested world rectangle
     * @param levelInfo levelinfo of selected pyramid
     * @param tileQueue queue for thread synchronization
     * @param config the configuraton of the plugin
     */
    AbstractThread(
            Rectangle pixelDimenison,
            GeneralEnvelope requestEnvelope,
            ImageLevelInfo levelInfo,
            LinkedBlockingQueue<TileQueueElement> tileQueue,
            Config config) {
        super();
        this.config = config;
        this.tileQueue = tileQueue;
        this.requestEnvelope = requestEnvelope;
        this.levelInfo = levelInfo;
        this.pixelDimension = pixelDimenison;

        resX = requestEnvelope.getSpan(0) / pixelDimenison.getWidth();
        resY = requestEnvelope.getSpan(1) / pixelDimenison.getHeight();
        rescaleX = levelInfo.getResX() / resX;
        rescaleY = levelInfo.getResY() / resY;
    }

    protected BufferedImage rescaleImageViaPlanarImage(BufferedImage image) {
        PlanarImage planarImage = new TiledImage(image, image.getWidth(), image.getHeight());

        int interpolation = Interpolation.INTERP_NEAREST;

        if (config.getInterpolation().intValue() == 2)
            interpolation = Interpolation.INTERP_BILINEAR;

        if (config.getInterpolation().intValue() == 3) interpolation = Interpolation.INTERP_BICUBIC;

        ImageWorker w = new ImageWorker(planarImage);
        w.scale(
                new Float(rescaleX),
                new Float(rescaleY),
                0.0f,
                0.0f,
                Interpolation.getInstance(interpolation));
        RenderedOp result = w.getRenderedOperation();
        Raster scaledImageRaster = result.getData();
        if (!(scaledImageRaster instanceof WritableRaster)) scaledImageRaster = result.copyData();

        ColorModel colorModel = image.getColorModel();

        BufferedImage scaledImage =
                new BufferedImage(
                        colorModel,
                        (WritableRaster) scaledImageRaster,
                        image.isAlphaPremultiplied(),
                        null);
        return scaledImage;
    }
}
