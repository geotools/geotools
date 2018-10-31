/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.util.concurrent.Callable;
import javax.imageio.ImageReadParam;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import org.geotools.gce.imagemosaic.GranuleDescriptor.GranuleLoadingResult;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.util.factory.Hints;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.operation.MathTransform2D;

/**
 * Specific {@link Callable} implementation that can be used to load the result of a request on a
 * single {@link GranuleDescriptor} via {@link GranuleLoadingResult}.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class GranuleLoader implements Callable<GranuleLoadingResult> {

    final ReferencedEnvelope cropBBox;

    final MathTransform2D mosaicWorldToGrid;

    final GranuleDescriptor granuleDescriptor;

    final ImageReadParam readParameters;

    final int imageIndex;

    final Hints hints;

    RasterLayerRequest request;

    public GranuleLoader(
            final ImageReadParam readParameters,
            final int imageIndex,
            final ReferencedEnvelope cropBBox,
            final MathTransform2D mosaicWorldToGrid,
            final GranuleDescriptor granuleDescriptor,
            final RasterLayerRequest request,
            final Hints hints) {
        this.readParameters = ImageUtilities.cloneImageReadParam(readParameters);
        this.imageIndex = imageIndex;
        this.cropBBox = cropBBox;
        this.mosaicWorldToGrid = mosaicWorldToGrid;
        this.granuleDescriptor = granuleDescriptor;
        this.request = request;
        this.hints = new Hints(hints);
        if (request.getTileDimensions() != null) {
            final Dimension tileDimension = request.getTileDimensions();
            if (hints != null && hints.containsKey(JAI.KEY_IMAGE_LAYOUT)) {
                final Object layout = this.hints.get(JAI.KEY_IMAGE_LAYOUT);
                if (layout != null && layout instanceof ImageLayout) {
                    final ImageLayout imageLayout = (ImageLayout) layout;
                    imageLayout.setTileHeight(tileDimension.height);
                    imageLayout.setTileWidth(tileDimension.width);
                }
            } else {
                final ImageLayout layout = new ImageLayout();
                layout.setTileWidth(tileDimension.width).setTileHeight(tileDimension.height);
                this.hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
            }
        }
    }

    public BoundingBox getCropBBox() {
        return cropBBox;
    }

    public MathTransform2D getMosaicWorldToGrid() {
        return mosaicWorldToGrid;
    }

    public GranuleDescriptor getGranule() {
        return granuleDescriptor;
    }

    public ImageReadParam getReadParameters() {
        return readParameters;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public GranuleLoadingResult call() throws Exception {
        try {
            return granuleDescriptor.loadRaster(
                    readParameters, imageIndex, cropBBox, mosaicWorldToGrid, request, hints);
        } catch (Exception e) {
            throw new GranuleLoadingException(
                    "Failed to load granule " + granuleDescriptor.getGranuleUrl(), e);
        }
    }
}
