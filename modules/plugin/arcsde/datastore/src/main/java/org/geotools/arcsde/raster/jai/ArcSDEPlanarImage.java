package org.geotools.arcsde.raster.jai;

import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.TileCache;
import javax.media.jai.TileFactory;

import org.geotools.arcsde.raster.io.TileReader;
import org.geotools.coverage.grid.io.imageio.RecyclingTileFactory;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;

import com.sun.media.jai.util.ImageUtil;

@SuppressWarnings("unchecked")
public class ArcSDEPlanarImage extends PlanarImage {

    private static final Logger LOGGER = Logging.getLogger(ArcSDEPlanarImage.class);

    private TileReader tileReader;

    private final SampleModel tileSampleModel;

    private final BigInteger UID;

    private final int hashCode;

    /**
     * We use an internal TileCache for each ArcSDEPlanarImage because RenderedOp's use to call
     * getTile(x,y) repeteadly for the same tile and since we're hitting a database it's not easy to
     * go fetch a tile from the stream that has already been fetch. Anyway, if a tile is requested
     * that's past the current tile index in the stream and it's not on this cache, NativeTileReader
     * will perform an extra database query to fetch that single tile, but this is to avoid that
     * situation to the maximum extent possible.
     */
    private final TileCache tileCache;

    public ArcSDEPlanarImage(TileReader tileReader, int minX, int minY, int width, int height,
            int tileGridXOffset, int tileGridYOffset, SampleModel tileSampleModel,
            ColorModel colorModel) {

        this.tileReader = tileReader;
        this.tileSampleModel = tileSampleModel;

        super.minX = minX;
        super.minY = minY;
        super.width = width;
        super.height = height;
        super.tileGridXOffset = tileGridXOffset;
        super.tileGridYOffset = tileGridYOffset;
        super.tileWidth = tileReader.getTileWidth();
        super.tileHeight = tileReader.getTileHeight();

        super.colorModel = colorModel;
        super.sampleModel = tileSampleModel;

        {
            int result = 17;
            // collect the contributions of various fields
            result = Utilities.hash(tileReader.getServerName(), result);
            result = Utilities.hash(tileReader.getRasterTableName(), result);
            result = Utilities.hash(tileReader.getRasterId(), result);
            result = Utilities.hash(tileReader.getPyramidLevel(), result);
            this.hashCode = result;
        }
        this.UID = (BigInteger) ImageUtil.generateID(this);

        int bytesPerPixel;
        switch (tileSampleModel.getDataType()) {
        case DataBuffer.TYPE_BYTE:
            bytesPerPixel = 1;
            break;
        case DataBuffer.TYPE_DOUBLE:
            bytesPerPixel = 8;
            break;
        case DataBuffer.TYPE_FLOAT:
        case DataBuffer.TYPE_INT:
            bytesPerPixel = 4;
            break;
        case DataBuffer.TYPE_SHORT:
        case DataBuffer.TYPE_USHORT:
            bytesPerPixel = 2;
            break;
        default:
            throw new IllegalArgumentException("Unknown DataBuffer type: "
                    + tileSampleModel.getDataType());
        }

        int numTilesInCache = 16;
        long memCapacity = numTilesInCache * bytesPerPixel * tileWidth * tileHeight
                * tileSampleModel.getNumBands();
        this.tileCache = JAI.createTileCache(memCapacity);

        final JAI jai = JAI.getDefaultInstance();
        TileFactory tileFactory = (TileFactory) jai.getRenderingHint(JAI.KEY_TILE_FACTORY);
        if (tileFactory == null) {
            if (tileCache instanceof Observable) {
                super.tileFactory = new RecyclingTileFactory((Observable) tileCache);
            } else {
                // not a SunTileCache?
                super.tileFactory = new javax.media.jai.RecyclingTileFactory();
            }
        } else {
            super.tileFactory = tileFactory;
        }
    }

    // @Override
    // public boolean equals(Object o) {
    // return super.equals(o);
    // }

    @Override
    public SampleModel getSampleModel() {
        return sampleModel;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public BigInteger getImageID() {
        return UID;
    }

    /**
     * @see java.awt.image.RenderedImage#getTile(int, int)
     */
    @Override
    public synchronized Raster getTile(final int tileX, final int tileY) {
        Raster currentTile = tileCache.getTile(this, tileX, tileY);
        if (currentTile != null) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("! GOT TILE FROM TileCache " + tileX + ", " + tileY + ", plevel "
                        + tileReader.getPyramidLevel());
            }
            return currentTile;
        }

        final int xOrigin = tileXToX(tileX);
        final int yOrigin = tileYToY(tileY);

        currentTile = tileFactory.createTile(tileSampleModel, new Point(xOrigin, yOrigin));

        if (shallIgnoreTile(tileX, tileY)) {
            // not a requested tile
            return currentTile;
        }

        final int readerTileX = tileX;// - tileReader.getMinTileX();
        final int readerTileY = tileY;// - tileReader.getMinTileY();

        try {
            switch (tileSampleModel.getDataType()) {
            case DataBuffer.TYPE_BYTE: {
                DataBufferByte dataBuffer = (DataBufferByte) currentTile.getDataBuffer();
                byte[][] bankData = dataBuffer.getBankData();
                tileReader.getTile(readerTileX, readerTileY, bankData);
            }
                break;
            case DataBuffer.TYPE_USHORT: {
                DataBufferUShort dataBuffer = (DataBufferUShort) currentTile.getDataBuffer();
                short[][] bankData = dataBuffer.getBankData();
                tileReader.getTile(readerTileX, readerTileY, bankData);
            }
                break;
            case DataBuffer.TYPE_SHORT: {
                DataBufferShort dataBuffer = (DataBufferShort) currentTile.getDataBuffer();
                short[][] bankData = dataBuffer.getBankData();
                tileReader.getTile(readerTileX, readerTileY, bankData);
            }
                break;
            case DataBuffer.TYPE_INT: {
                DataBufferInt dataBuffer = (DataBufferInt) currentTile.getDataBuffer();
                int[][] bankData = dataBuffer.getBankData();
                tileReader.getTile(readerTileX, readerTileY, bankData);
            }
                break;
            case DataBuffer.TYPE_FLOAT: {
                DataBufferFloat dataBuffer = (DataBufferFloat) currentTile.getDataBuffer();
                float[][] bankData = dataBuffer.getBankData();
                tileReader.getTile(readerTileX, readerTileY, bankData);
            }
                break;
            case DataBuffer.TYPE_DOUBLE: {
                DataBufferDouble dataBuffer = (DataBufferDouble) currentTile.getDataBuffer();
                double[][] bankData = dataBuffer.getBankData();
                tileReader.getTile(readerTileX, readerTileY, bankData);
            }
                break;
            default:
                throw new IllegalStateException("Unrecognized DataBuffer type: "
                        + tileSampleModel.getDataType());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

//        if (tileX == tileReader.getMinTileX() || tileX == tileReader.getMaxTileX()
//                || tileY == tileReader.getMinTileY() || tileY == tileReader.getMaxTileY()) {
            tileCache.add(this, tileX, tileY, currentTile);
//        }

        return currentTile;
    }

    private boolean shallIgnoreTile(int tx, int ty) {
        int minTileX = tileReader.getMinTileX();
        int minTileY = tileReader.getMinTileY();
        int maxTileX = tileReader.getMaxTileX();
        int maxTileY = tileReader.getMaxTileY();

        boolean ignore = tx < minTileX || ty < minTileY || tx > maxTileX || ty > maxTileY;
        return ignore;
    }

    @Override
    public synchronized void dispose(){
        super.dispose();
        this.tileReader.dispose();
    }
    
    @Override
    protected void finalize(){
        dispose();
    }
}
