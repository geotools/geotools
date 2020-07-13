/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

import java.awt.image.BufferedImage;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.event.IIOReadUpdateListener;
import javax.imageio.event.IIOReadWarningListener;
import org.opengis.util.ProgressListener;

/**
 * This class provide a means to wrap a GeoTools {@link ProgressListener} and have it control an
 * {@link ImageReader} while it is actually doing a {@link ImageReader#read(int)} operation.
 *
 * <p>We also give user the ability to cancel the reading process
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class GridCoverageReaderProgressAdapter extends BaseGridCoverageProgressAdapter
        implements IIOReadProgressListener, IIOReadUpdateListener, IIOReadWarningListener {

    public GridCoverageReaderProgressAdapter(ProgressListener monitor, int numImages) {
        super(monitor, numImages);
    }

    public GridCoverageReaderProgressAdapter(ProgressListener monitor) {
        this(monitor, 1);
    }

    @Override
    public void warningOccurred(ImageReader source, String warning) {
        monitor.warningOccurred(
                source.getInput().toString(), "Warning writing image:" + lastImageIndex, warning);
    }

    @Override
    public void passStarted(
            ImageReader source,
            BufferedImage theImage,
            int pass,
            int minPass,
            int maxPass,
            int minX,
            int minY,
            int periodX,
            int periodY,
            int[] bands) {
        // ignore

    }

    @Override
    public void imageUpdate(
            ImageReader source,
            BufferedImage theImage,
            int minX,
            int minY,
            int width,
            int height,
            int periodX,
            int periodY,
            int[] bands) {
        // ignore

    }

    @Override
    public void passComplete(ImageReader source, BufferedImage theImage) {
        // ignore

    }

    @Override
    public void thumbnailPassStarted(
            ImageReader source,
            BufferedImage theThumbnail,
            int pass,
            int minPass,
            int maxPass,
            int minX,
            int minY,
            int periodX,
            int periodY,
            int[] bands) {
        // ignore

    }

    @Override
    public void thumbnailUpdate(
            ImageReader source,
            BufferedImage theThumbnail,
            int minX,
            int minY,
            int width,
            int height,
            int periodX,
            int periodY,
            int[] bands) {
        // ignore

    }

    @Override
    public void thumbnailPassComplete(ImageReader source, BufferedImage theThumbnail) {
        // ignore

    }

    @Override
    public void sequenceStarted(ImageReader source, int minIndex) {
        // ignore

    }

    @Override
    public void sequenceComplete(ImageReader source) {
        // ignore

    }

    @Override
    public void imageStarted(ImageReader source, int imageIndex) {
        if (imageIndex == 0) monitor.started();

        // register progress
        lastImageIndex = imageIndex;
        float progress = lastImageIndex * progressStep * 100;

        // report progress and check stop
        reportProgress(progress, source);
    }

    @Override
    public void imageProgress(ImageReader source, float percentageDone) {
        // register progress
        float tempProgress = lastImageIndex * progressStep * 100 + percentageDone * progressStep;
        if (tempProgress - progress > 5.0) {
            // report progress and check stop
            reportProgress(tempProgress, source);

            // update
            progress = tempProgress;
        }
    }

    @Override
    public void imageComplete(ImageReader source) {

        // register progress
        float progress = (lastImageIndex + 1) * progressStep * 100;

        // report progress and check stop
        reportProgress(progress, source);

        // are we done?
        if (lastImageIndex == (numImages - 1)) monitor.complete();
    }

    @Override
    public void thumbnailStarted(ImageReader source, int imageIndex, int thumbnailIndex) {
        // ignore

    }

    @Override
    public void thumbnailProgress(ImageReader source, float percentageDone) {
        // ignore

    }

    @Override
    public void thumbnailComplete(ImageReader source) {
        // ignore

    }

    @Override
    public void readAborted(ImageReader source) {
        // we should not do anything as this is triggered
        // mostly by us using #setCancel

    }

    /** */
    private void reportProgress(float progress, ImageReader reader) {
        monitor.progress(progress);
        if (monitor.isCanceled()) reader.abort();
    }
}
