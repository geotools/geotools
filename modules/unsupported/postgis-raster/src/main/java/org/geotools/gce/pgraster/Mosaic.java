/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.pgraster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.locationtech.jts.geom.Envelope;

/** Mosaics {@link Tile}'s together into a final image / coverage in response to a read request. */
class Mosaic {

    final ReadRequest read;
    final GridCoverageFactory factory;

    Dimension size;
    Point2D.Double rescale;
    BufferedImage image;

    /** Aggregate bounds of tiles */
    Envelope bounds;

    public Mosaic(ReadRequest read, GridCoverageFactory factory) {
        this.read = read;
        this.factory = factory;

        // calculate image size
        rescale =
                new Point.Double(
                        read.raster.scale.x / read.resolution.x,
                        read.raster.scale.y / read.resolution.y);
        this.size =
                new Dimension(
                        (int) Math.round(read.region.width / rescale.x),
                        (int) Math.round(read.region.height / rescale.y));

        bounds = new Envelope();
        bounds.setToNull();
    }

    public void accept(Tile tile) {
        if (image == null) {
            image = initImage(tile);
            fillBackground(image);
        }

        int x = (int) ((tile.bounds.getMinX() - read.nativeBounds.getMinX()) / read.raster.scale.x);
        int y = (int) ((read.nativeBounds.getMaxY() - tile.bounds.getMaxY()) / read.raster.scale.y);

        image.getRaster().setRect(x, y, tile.image.getRaster());

        bounds.expandToInclude(tile.bounds);
    }

    BufferedImage initImage(Tile tile) {
        BufferedImage from = tile.image;
        // copy over properties of this tile
        Hashtable<String, Object> props = null;
        if (from.getPropertyNames() != null) {
            props = new Hashtable<>();
            for (String name : from.getPropertyNames()) {
                props.put(name, from.getProperty(name));
            }
        }

        SampleModel sm =
                from.getSampleModel()
                        .createCompatibleSampleModel((int) size.getWidth(), (int) size.getHeight());
        WritableRaster raster = Raster.createWritableRaster(sm, null);

        ColorModel colorModel = from.getColorModel();

        return new BufferedImage(colorModel, raster, from.isAlphaPremultiplied(), props);
    }

    BufferedImage initImage(int type) {
        return new BufferedImage((int) size.getWidth(), (int) size.getHeight(), type);
    }

    void fillBackground(BufferedImage image) {
        if (read.backgroundColor != null) {
            Graphics2D g = (Graphics2D) image.getGraphics();
            Color current = g.getColor();
            g.setColor(read.backgroundColor);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            g.setColor(current);
        }
    }

    public GridCoverage2D coverage() {
        if (image == null) {
            return null;
        }

        RenderedImage finalImage = rescale(image);

        ReferencedEnvelope mapBounds = read.nativeBounds;

        // if bounds of loaded tiles smaller than bounds of map crop it
        // we do this so that the calling rendering code can handle filling
        // in backound transparency, etc...
        if (mapBounds.covers(bounds)) {
            // crop
            int x =
                    (int)
                            (finalImage.getWidth()
                                    * (bounds.getMinX() - mapBounds.getMinX())
                                    / mapBounds.getWidth());
            int y =
                    finalImage.getHeight()
                            - (int)
                                    (finalImage.getHeight()
                                            * (bounds.getMaxY() - mapBounds.getMinY())
                                            / mapBounds.getHeight());
            int h = (int) (finalImage.getHeight() * bounds.getHeight() / mapBounds.getHeight());
            int w = (int) (finalImage.getWidth() * bounds.getWidth() / mapBounds.getWidth());
            finalImage = crop(finalImage, x, y, w, h);
        }

        return factory.create(
                read.reader.name(),
                finalImage,
                new ReferencedEnvelope(bounds, mapBounds.getCoordinateReferenceSystem()));
    }

    RenderedImage crop(RenderedImage image, float x, float y, float w, float h) {
        return new ImageWorker(image).crop(x, y, w, h).getRenderedImage();
    }

    RenderedImage rescale(RenderedImage image) {
        PlanarImage planar = new TiledImage(image, image.getWidth(), image.getHeight());

        ImageWorker w = new ImageWorker(planar);
        w.scale((float) rescale.x, (float) rescale.y, 0.0f, 0.0f, read.interpolation());

        return w.getRenderedImage();
    }
}
