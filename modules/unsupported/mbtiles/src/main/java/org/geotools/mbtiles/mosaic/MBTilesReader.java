package org.geotools.mbtiles.mosaic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.mbtiles.MBTilesFile;
import org.geotools.mbtiles.MBTilesMetadata;
import org.geotools.mbtiles.MBTilesTile;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class MBTilesReader extends AbstractGridCoverage2DReader {

    static final CoordinateReferenceSystem SPHERICAL_MERCATOR;

    static final CoordinateReferenceSystem WGS_84;

    static {
        try {
            SPHERICAL_MERCATOR = CRS.decode("EPSG:3857", true);
            WGS_84 = CRS.decode("EPSG:4326", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static final ReferencedEnvelope WORLD_ENVELOPE =
            new ReferencedEnvelope(
                    -20037508.34, 20037508.34, -20037508.34, 20037508.34, SPHERICAL_MERCATOR);

    protected static final int DEFAULT_TILE_SIZE = 256;

    protected static final int ZOOM_LEVEL_BASE = 2;

    protected GridCoverageFactory coverageFactory;

    protected MBTilesMetadata metadata;

    protected ReferencedEnvelope bounds;

    protected File sourceFile;

    public MBTilesReader(Object source, Hints hints) throws IOException {
        sourceFile = MBTilesFormat.getFileFromSource(source);

        MBTilesFile file = new MBTilesFile(sourceFile);

        metadata = file.loadMetaData();

        try {
            bounds =
                    ReferencedEnvelope.create(metadata.getBounds(), WGS_84)
                            .transform(SPHERICAL_MERCATOR, true);
        } catch (Exception e) {
            bounds = null;
        }
        originalEnvelope = new GeneralEnvelope(bounds == null ? WORLD_ENVELOPE : bounds);

        long maxZoom;
        try {
            maxZoom = file.maxZoom();
        } catch (SQLException e) {
            throw new IOException(e);
        }

        long size = Math.round(Math.pow(ZOOM_LEVEL_BASE, maxZoom)) * DEFAULT_TILE_SIZE;

        highestRes =
                new double[] {WORLD_ENVELOPE.getSpan(0) / size, WORLD_ENVELOPE.getSpan(1) / size};

        originalGridRange = new GridEnvelope2D(new Rectangle((int) size, (int) size));

        coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);

        crs = SPHERICAL_MERCATOR;
    }

    @Override
    public Format getFormat() {
        return new MBTilesFormat();
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue[] parameters)
            throws IllegalArgumentException, IOException {
        MBTilesFile file = new MBTilesFile(sourceFile);

        ReferencedEnvelope requestedEnvelope = null;
        Rectangle dim = null;

        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                final ParameterValue param = (ParameterValue) parameters[i];
                final ReferenceIdentifier name = param.getDescriptor().getName();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
                    final GridGeometry2D gg = (GridGeometry2D) param.getValue();
                    try {
                        requestedEnvelope =
                                ReferencedEnvelope.create(
                                                gg.getEnvelope(), gg.getCoordinateReferenceSystem())
                                        .transform(SPHERICAL_MERCATOR, true);
                        ;
                    } catch (Exception e) {
                        requestedEnvelope = null;
                    }

                    dim = gg.getGridRange2D().getBounds();
                    continue;
                }
            }
        }

        if (requestedEnvelope == null) {
            requestedEnvelope = bounds;
        }

        long zoomLevel = 0;
        long leftTile, topTile, rightTile, bottomTile;

        if (requestedEnvelope != null && dim != null) {
            // find the closest zoom based on horizontal resolution
            double ratioWidth =
                    requestedEnvelope.getSpan(0)
                            / WORLD_ENVELOPE.getSpan(
                                    0); // proportion of total width that is being requested
            double propWidth =
                    dim.getWidth()
                            / ratioWidth; // this is the width in pixels that the whole world would
            // have in the requested resolution
            zoomLevel =
                    Math.round(Math.log(propWidth / DEFAULT_TILE_SIZE) / Math.log(ZOOM_LEVEL_BASE));
            // the closest zoom level to the resolution, based on the formula width =
            // zoom_base^zoom_level * tile_size -> zoom_level = log(width /
            // tile_size)/log(zoom_base)
        }

        try { // now take a zoom level that is available in the database
            zoomLevel = file.closestZoom(zoomLevel);
        } catch (SQLException e1) {
            throw new IOException(e1);
        }

        long numberOfTiles =
                Math.round(
                        Math.pow(
                                ZOOM_LEVEL_BASE,
                                zoomLevel)); // number of tile columns/rows for chosen zoom level
        double resX = WORLD_ENVELOPE.getSpan(0) / numberOfTiles; // points per tile
        double resY = WORLD_ENVELOPE.getSpan(1) / numberOfTiles; // points per tile
        double offsetX = WORLD_ENVELOPE.getMinimum(0);
        double offsetY = WORLD_ENVELOPE.getMinimum(1);

        try { // take available tiles from database
            leftTile = file.minColumn(zoomLevel);
            rightTile = file.maxColumn(zoomLevel);
            bottomTile = file.minRow(zoomLevel);
            topTile = file.maxRow(zoomLevel);
        } catch (SQLException e) {
            throw new IOException(e);
        }

        if (requestedEnvelope != null) { // crop tiles to requested envelope
            leftTile =
                    Math.max(
                            leftTile,
                            Math.round(
                                    Math.floor(
                                            (requestedEnvelope.getMinimum(0) - offsetX) / resX)));
            bottomTile =
                    Math.max(
                            bottomTile,
                            Math.round(
                                    Math.floor(
                                            (requestedEnvelope.getMinimum(1) - offsetY) / resY)));
            rightTile =
                    Math.max(
                            leftTile,
                            Math.min(
                                    rightTile,
                                    Math.round(
                                            Math.floor(
                                                    (requestedEnvelope.getMaximum(0) - offsetX)
                                                            / resX))));
            topTile =
                    Math.max(
                            bottomTile,
                            Math.min(
                                    topTile,
                                    Math.round(
                                            Math.floor(
                                                    (requestedEnvelope.getMaximum(1) - offsetY)
                                                            / resY))));
        }

        int width = (int) (rightTile - leftTile + 1) * DEFAULT_TILE_SIZE;
        int height = (int) (topTile - bottomTile + 1) * DEFAULT_TILE_SIZE;

        // recalculate the envelope we are actually returning
        ReferencedEnvelope resultEnvelope =
                new ReferencedEnvelope(
                        offsetX + leftTile * resX,
                        offsetX + (rightTile + 1) * resX,
                        offsetY + bottomTile * resY,
                        offsetY + (topTile + 1) * resY,
                        SPHERICAL_MERCATOR);

        BufferedImage image = null;

        MBTilesFile.TileIterator it;
        try {
            it = file.tiles(zoomLevel, leftTile, bottomTile, rightTile, topTile);
        } catch (SQLException e) {
            throw new IOException(e);
        }

        while (it.hasNext()) {
            MBTilesTile tile = it.next();

            BufferedImage tileImage =
                    readImage(
                            tile.getData(),
                            metadata.getFormatStr() == null ? "png" : metadata.getFormatStr());

            if (image == null) {
                image = getStartImage(tileImage, width, height);
            }

            // coordinates
            int posx = (int) (tile.getTileColumn() - leftTile) * DEFAULT_TILE_SIZE;
            int posy = (int) (topTile - tile.getTileRow()) * DEFAULT_TILE_SIZE;

            image.getRaster().setRect(posx, posy, tileImage.getData());
        }

        it.close();

        if (image == null) { // no tiles ??
            image = getStartImage(width, height);
        }

        return coverageFactory.create(
                metadata.getName() == null ? "nameless mbtiles" : metadata.getName(),
                image,
                resultEnvelope);
    }

    protected static BufferedImage readImage(byte[] data, String format) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Iterator<?> readers = ImageIO.getImageReadersByFormatName(format);
        ImageReader reader = (ImageReader) readers.next();
        Object source = bis;
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();

        return reader.read(0, param);
    }

    protected BufferedImage getStartImage(BufferedImage copyFrom, int width, int height) {
        Map<String, Object> properties = null;

        if (copyFrom.getPropertyNames() != null) {
            properties = new HashMap<String, Object>();
            for (String name : copyFrom.getPropertyNames()) {
                properties.put(name, copyFrom.getProperty(name));
            }
        }

        SampleModel sm = copyFrom.getSampleModel().createCompatibleSampleModel(width, height);
        WritableRaster raster = Raster.createWritableRaster(sm, null);

        BufferedImage image =
                new BufferedImage(
                        copyFrom.getColorModel(),
                        raster,
                        copyFrom.isAlphaPremultiplied(),
                        (Hashtable<?, ?>) properties);

        // white background
        Graphics2D g2D = (Graphics2D) image.getGraphics();
        Color save = g2D.getColor();
        g2D.setColor(Color.WHITE);
        g2D.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2D.setColor(save);

        return image;
    }

    protected BufferedImage getStartImage(int imageType, int width, int height) {
        if (imageType == BufferedImage.TYPE_CUSTOM) imageType = BufferedImage.TYPE_3BYTE_BGR;

        BufferedImage image = new BufferedImage(width, height, imageType);

        // white background
        Graphics2D g2D = (Graphics2D) image.getGraphics();
        Color save = g2D.getColor();
        g2D.setColor(Color.WHITE);
        g2D.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2D.setColor(save);

        return image;
    }

    protected BufferedImage getStartImage(int width, int height) {
        return getStartImage(BufferedImage.TYPE_CUSTOM, width, height);
    }
}
