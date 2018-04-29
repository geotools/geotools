/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gce.imagemosaic;

import it.geosolutions.imageio.pam.PAMDataset;
import java.awt.image.RenderedImage;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;

/**
 * Represents the input raster element for a mosaic operation, source {@link RenderedImage}, {@link
 * ROI} and alpha channel.
 *
 * <p>This class is just a simple bean that holds a single element for the mosaic.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class MosaicElement {

    PlanarImage alphaChannel;

    ROI roi;

    RenderedImage source;

    PAMDataset pamDataset;

    public MosaicElement(
            PlanarImage alphaChannel, ROI roi, RenderedImage source, PAMDataset pamDataset) {
        this.alphaChannel = alphaChannel;
        this.roi = roi;
        this.source = source;
        this.pamDataset = pamDataset;
    }

    public PlanarImage getAlphaChannel() {
        return alphaChannel;
    }

    public void setAlphaChannel(PlanarImage alphaChannel) {
        this.alphaChannel = alphaChannel;
    }

    public ROI getRoi() {
        return roi;
    }

    public void setRoi(ROI roi) {
        this.roi = roi;
    }

    public RenderedImage getSource() {
        return source;
    }

    public void setSource(RenderedImage source) {
        this.source = source;
    }

    public PAMDataset getPamDataset() {
        return pamDataset;
    }

    public void setPamDataset(PAMDataset pamDataset) {
        this.pamDataset = pamDataset;
    }
}
