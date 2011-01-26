/**
 * 
 */
package org.geotools.arcsde.raster.io;

public final class TileInfo {
    private long bandId;

    private byte[] bitmaskData;

    private int numPixelsRead;

    private int columnIndex;

    private int rowIndex;

    private byte[] tileDataBytes;

    private short[] tileDataShorts;

    private int[] tileDataInts;

    private float[] tileDataFloats;

    private double[] tileDataDoubles;

    private final int numPixels;

    private Number noData;

    public TileInfo(int pixelsPerTile) {
        this.numPixels = pixelsPerTile;
    }

    public Long getBandId() {
        return bandId;
    }

    public byte[] getBitmaskData() {
        return bitmaskData;
    }

    /**
     * @return number of pixels in the tile data
     */
    public int getNumPixels() {
        return numPixels;
    }

    /**
     * @return number of pixels actually read. It shall be either {@code 0} or equal to
     *         {@link #getNumPixels()}
     */
    public int getNumPixelsRead() {
        return numPixelsRead;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setTileData(final byte[] pixelData) {
        this.tileDataBytes = pixelData;
    }

    public void setTileData(short[] pixelData) {
        this.tileDataShorts = pixelData;
    }

    public void setTileData(int[] pixelData) {
        this.tileDataInts = pixelData;
    }

    public void setTileData(float[] pixelData) {
        this.tileDataFloats = pixelData;
    }

    public void setTileData(double[] pixelData) {
        this.tileDataDoubles = pixelData;
    }

    public byte[] getTileDataAsBytes() {
        if (tileDataBytes == null) {
            tileDataBytes = new byte[numPixels];
        }
        return tileDataBytes;
    }

    public short[] getTileDataAsUnsignedShorts() {
        if (tileDataShorts == null) {
            tileDataShorts = new short[numPixels];
        }
        return tileDataShorts;
    }

    public short[] getTileDataAsShorts() {
        if (tileDataShorts == null) {
            tileDataShorts = new short[numPixels];
        }

        return tileDataShorts;
    }

    public int[] getTileDataAsIntegers() {
        if (tileDataInts == null) {
            tileDataInts = new int[numPixels];
        }
        return tileDataInts;
    }

    public float[] getTileDataAsFloats() {
        if (tileDataFloats == null) {
            tileDataFloats = new float[numPixels];
        }
        return tileDataFloats;
    }

    public double[] getTileDataAsDoubles() {
        if (tileDataDoubles == null) {
            tileDataDoubles = new double[numPixels];
        }
        return tileDataDoubles;
    }

    public void setBandId(final long bandId) {
        this.bandId = bandId;
    }

    public void setColumnIndex(final int colIndex) {
        this.columnIndex = colIndex;
    }

    public void setRowIndex(final int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void setNumPixelsRead(final int numPixelsRead) {
        this.numPixelsRead = numPixelsRead;
    }

    public void setBitmaskData(final byte[] bitMaskData) {
        this.bitmaskData = bitMaskData;
    }

    public void setNoDataValue(final Number noData) {
        this.noData = noData;
    }

    public Number getNoDataValue() {
        return this.noData;
    }

    public boolean hasNoDataPixels() {
        return bitmaskData.length > 0;
    }
}