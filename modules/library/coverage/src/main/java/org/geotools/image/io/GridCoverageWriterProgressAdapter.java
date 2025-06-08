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

import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.event.IIOWriteWarningListener;
import org.geotools.api.util.ProgressListener;

/**
 * This class provide a means to wrap a GeoTools {@link ProgressListener} and have it control an {@link ImageWriter}
 * while it is actually doing a {@link ImageWriter#write(javax.imageio.IIOImage)} operation.
 *
 * <p>We also give user the ability to cancel the writing process
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class GridCoverageWriterProgressAdapter extends BaseGridCoverageProgressAdapter
        implements IIOWriteProgressListener, IIOWriteWarningListener {

    public GridCoverageWriterProgressAdapter(final ProgressListener monitor, int numImages) {
        super(monitor, numImages);
    }

    public GridCoverageWriterProgressAdapter(final ProgressListener monitor) {
        this(monitor, 1);
    }

    @Override
    public void warningOccurred(ImageWriter source, int imageIndex, String warning) {
        monitor.warningOccurred(source.getOutput().toString(), "Warning writing image:" + imageIndex, warning);
    }

    @Override
    public void imageStarted(ImageWriter source, int imageIndex) {
        if (imageIndex == 0) monitor.started();

        // register progress
        lastImageIndex = imageIndex;
        float progress = lastImageIndex * progressStep * 100;

        // report progress and check stop
        reportProgress(progress, source);
    }

    /** */
    private void reportProgress(float progress, ImageWriter writer) {
        monitor.progress(progress);
        if (monitor.isCanceled()) writer.abort();
    }

    @Override
    public void imageProgress(ImageWriter source, float percentageDone) {
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
    public void imageComplete(ImageWriter source) {

        // register progress
        float progress = (lastImageIndex + 1) * progressStep * 100;

        // report progress and check stop
        reportProgress(progress, source);

        // are we done?
        if (lastImageIndex == numImages - 1) monitor.complete();
    }

    @Override
    public void thumbnailStarted(ImageWriter source, int imageIndex, int thumbnailIndex) {
        // ignore

    }

    @Override
    public void thumbnailProgress(ImageWriter source, float percentageDone) {
        // ignore

    }

    @Override
    public void thumbnailComplete(ImageWriter source) {
        // ignore

    }

    @Override
    public void writeAborted(ImageWriter source) {
        // we should not do anything as this is triggered
        // mostly by us using #setCancel

    }
}
