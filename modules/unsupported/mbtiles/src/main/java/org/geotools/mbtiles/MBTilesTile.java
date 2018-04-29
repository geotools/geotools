package org.geotools.mbtiles;

public class MBTilesTile {

    // the ordinates
    protected final long ZoomLevel;
    protected final long TileColumn;
    protected final long TileRow;

    // the data
    protected byte[] data = null;

    public MBTilesTile(long zoomLevel, long tileColumn, long tileRow) {
        ZoomLevel = zoomLevel;
        TileColumn = tileColumn;
        TileRow = tileRow;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getZoomLevel() {
        return ZoomLevel;
    }

    public long getTileColumn() {
        return TileColumn;
    }

    public long getTileRow() {
        return TileRow;
    }
}
