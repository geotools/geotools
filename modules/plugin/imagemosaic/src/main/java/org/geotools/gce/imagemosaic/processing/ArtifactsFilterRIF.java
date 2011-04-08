/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gce.imagemosaic.processing;

import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.ImageLayout;
import javax.media.jai.ROI;
import javax.media.jai.RasterFactory;

import com.sun.media.jai.opimage.RIFUtil;
import com.sun.media.jai.util.ImageUtil;

/**
 * The image factory for the {@link ArtifactsFilterOpImage} operation.
 */
public class ArtifactsFilterRIF implements RenderedImageFactory {

    /** Constructor */
    public ArtifactsFilterRIF() {
    }

    /**
     * Create a new instance of ArtifactsFilterOpImage in the rendered layer.
     *
     * @param paramBlock specifies the source image, and the following parameters: 
     * "roi", "backgroundValues", "threshold", "filterSize"
     *
     * @param renderingHints optional RenderingHints object
     */
    public RenderedImage create(ParameterBlock paramBlock, RenderingHints renderingHints) {

        final RenderedImage dataImage = paramBlock.getRenderedSource(0);

        ImageLayout layout = RIFUtil.getImageLayoutHint(renderingHints);
        if (layout == null) { 
            layout = new ImageLayout();
        }

        final int threshold = (Integer) paramBlock.getObjectParameter(ArtifactsFilterDescriptor.THRESHOLD_ARG);

        final int filterSize = (Integer) paramBlock.getObjectParameter(ArtifactsFilterDescriptor.FILTERSIZE_ARG);

        final double[] bgValues = (double[]) paramBlock.getObjectParameter(ArtifactsFilterDescriptor.BACKGROUND_ARG);

        SampleModel sm = layout.getSampleModel(null);
        if (sm == null) {
            final SampleModel dataSampleModel = dataImage.getSampleModel();
            final int dataType = dataSampleModel.getDataType();
            sm = RasterFactory.createComponentSampleModel(dataSampleModel, dataType,
                    dataImage.getWidth(), dataImage.getHeight(), dataSampleModel.getNumBands());

            layout.setSampleModel(sm);
            if (layout.getColorModel(null) != null) {
                final ColorModel cm = ImageUtil.getCompatibleColorModel(sm, renderingHints);
                layout.setColorModel(cm);
            }
        }

        final ROI roi = (ROI) paramBlock.getObjectParameter(ArtifactsFilterDescriptor.ROI_ARG);

        return new ArtifactsFilterOpImage(dataImage, layout, renderingHints, roi, 
                bgValues, threshold, filterSize);
    }
}

