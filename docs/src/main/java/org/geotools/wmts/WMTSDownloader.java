/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.wmts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Set;
import javax.imageio.ImageIO;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.client.WMTSTileService;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.tile.Tile;

/**
 * The program will download all the tile's that covers a given extent.
 *
 * <p>You must provide the path to where the tile should be stored.
 *
 * @author Roar Brænden
 */
public class WMTSDownloader {

    private static String serverUrl = "https://opencache.statkart.no/gatekeeper/gk/gk.open_wmts";

    private static String layerName = "norges_grunnkart";

    private static String matrixSetName = "EPSG:32632";

    private static double minX = 595042.0;
    private static double maxX = 611310.0;
    private static double minY = 6645322.0;
    private static double maxY = 6661883.0;

    private static double scaleFactor = 100000.0;

    private static File rootFolder;

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            throw new IllegalArgumentException("You must provide the path to a root folder.");
        }
        rootFolder = new File(args[0]);
        // start wmtsTileService example
        WebMapTileServer server = new WebMapTileServer(new URL(serverUrl));
        System.out.println("Layers:");
        server.getCapabilities()
                .getLayerList()
                .forEach(layer -> System.out.println(layer.getName()));

        WMTSLayer layer = server.getCapabilities().getLayer(layerName);

        System.out.println("Matrix sets:");
        layer.getTileMatrixLinks().keySet().forEach(System.out::println);
        TileMatrixSet matrixSet = server.getCapabilities().getMatrixSet(matrixSetName);

        WMTSTileService service =
                new WMTSTileService(serverUrl, WMTSServiceType.KVP, layer, null, matrixSet);

        ReferencedEnvelope extent =
                new ReferencedEnvelope(
                        minX, maxX, minY, maxY, matrixSet.getCoordinateReferenceSystem());

        Set<Tile> tiles = service.findTilesInExtent(extent, scaleFactor, true, 100);
        for (Tile tile : tiles) {
            BufferedImage image = tile.getBufferedImage();
            ImageIO.write(image, "png", new File(rootFolder, tile.getId() + ".png"));
        }
        // end wmtsTileService example
    }
}
