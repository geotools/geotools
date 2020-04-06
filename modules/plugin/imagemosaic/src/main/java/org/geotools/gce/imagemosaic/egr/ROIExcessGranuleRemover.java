/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.egr;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import org.geotools.gce.imagemosaic.GranuleDescriptor.GranuleLoadingResult;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Support class helping to remove images not contributing pixels to the final mosaic based on the
 * image ROIs (thus, working in raster space after the images are read)
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ROIExcessGranuleRemover {

    /** Default size for excess granule removal tiles */
    public static final int DEFAULT_TILE_SIZE = 256;

    Binarizator binarizator;

    Rectangle rasterBounds;

    int tileWidth;

    int tileHeight;

    CoordinateReferenceSystem targetCRS;

    /**
     * Holds the granule loading results until it becomes evident actual geometric computation is
     * needed
     */
    List<ROI> roiBuffer = new ArrayList<ROI>();

    public ROIExcessGranuleRemover(
            Rectangle rasterBounds,
            int tileWidth,
            int tileHeight,
            CoordinateReferenceSystem targetCRS) {
        this.rasterBounds = rasterBounds;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.targetCRS = targetCRS;
    }

    public boolean addGranule(GranuleLoadingResult result) {
        // check we are working in the uniform CRS case
        CoordinateReferenceSystem granuleCRS =
                result.getGranuleDescriptor().getGranuleEnvelope().getCoordinateReferenceSystem();
        if (granuleCRS != null
                && targetCRS != null
                && !CRS.equalsIgnoreMetadata(granuleCRS, targetCRS)) {
            throw new UnsupportedOperationException(
                    "Excess granule removal not yet supported with heterogeneous CRS");
        }

        // early bail out if possible
        if (binarizator != null && binarizator.isComplete()) {
            return false;
        }

        // get the ROI
        ROI roi = result.getFootprint();
        if (roi == null) {
            // TODO: check if the image has some form transparency (alpha band, palette
            // with fully transparent pixels), binarize it and use it as a ROI?
            final RenderedImage raster = result.getRaster();
            Rectangle bounds =
                    new Rectangle(
                            raster.getMinX(),
                            raster.getMinY(),
                            raster.getWidth(),
                            raster.getHeight());
            roi = new ROIShape(bounds);
        }

        boolean essential;
        if (!roi.getBounds().intersects(rasterBounds)) {
            essential = false;
        } else {
            if (binarizator == null) {
                binarizator =
                        new Binarizator(
                                JTS.toPolygon(rasterBounds),
                                rasterBounds.width,
                                rasterBounds.height,
                                tileWidth,
                                tileHeight);
            }
            essential = binarizator.add(roi);
        }

        return essential;
    }

    /** Returns true if the rendering area is complete */
    public boolean isRenderingAreaComplete() {
        // TODO: handle case in which the binarization is not yet generated? Like single tile
        // covering everything?
        return binarizator != null && binarizator.isComplete();
    }
}
