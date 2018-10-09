/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.tile.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.tile.Tile;
import org.geotools.tile.TileService;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 * This Layer is an attempt to speed rendering by using a CountDownLatch and threads to render each
 * tile. The performance improvement is minimal, though.
 *
 * @author Ugo Taddei
 * @since 12
 */
public class AsyncTileLayer extends TileLayer {

    private CountDownLatch countDownLatch;

    public AsyncTileLayer(TileService service) {
        super(service);
    }

    @Override
    protected void renderTiles(
            Collection<Tile> tiles,
            Graphics2D g2d,
            ReferencedEnvelope viewportExtent,
            AffineTransform worldToImageTransform) {

        long t = System.currentTimeMillis();

        this.countDownLatch = new CountDownLatch(tiles.size());

        localRenderTiles(tiles, g2d, viewportExtent, worldToImageTransform);
        try {
            this.countDownLatch.await();
        } catch (InterruptedException ie) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", ie);
        }
        this.countDownLatch = null;
    }

    protected void renderTile(final Tile tile, final Graphics2D g2d, final double[] points) {

        Runnable r =
                new Runnable() {

                    @Override
                    public void run() {
                        BufferedImage img = getTileImage(tile);

                        g2d.drawImage(
                                img,
                                (int) points[0],
                                (int) points[1],
                                (int) Math.ceil(points[2] - points[0]),
                                (int) Math.ceil(points[3] - points[1]),
                                null);

                        AsyncTileLayer.this.countDownLatch.countDown();
                    }
                };
        new Thread(r).start();
    }

    protected void localRenderTiles(
            Collection<Tile> tiles,
            Graphics2D g2d,
            ReferencedEnvelope viewportExtent,
            AffineTransform worldToImageTransform) {

        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        for (Tile tile : tiles) {
            ReferencedEnvelope nativeTileEnvelope = tile.getExtent();

            ReferencedEnvelope tileEnvViewport;
            try {
                tileEnvViewport =
                        nativeTileEnvelope.transform(
                                viewportExtent.getCoordinateReferenceSystem(), true);
            } catch (TransformException | FactoryException e) {
                throw new RuntimeException(e);
            }
            double[] points = new double[4];
            points[0] = tileEnvViewport.getMinX();
            points[3] = tileEnvViewport.getMinY();
            points[2] = tileEnvViewport.getMaxX();
            points[1] = tileEnvViewport.getMaxY();

            worldToImageTransform.transform(points, 0, points, 0, 2);

            renderTile(tile, g2d, points);
        }
    }
}
