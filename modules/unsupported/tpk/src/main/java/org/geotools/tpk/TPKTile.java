package org.geotools.tpk;

import java.util.Comparator;

/** Tile object returned to the reader */
public class TPKTile {
    public long col; // column -- longitude
    public long row; // row -- latitude
    public String imageFormat; // JPEG or PNG
    public byte[] tileData; // unconverted tile data
    public TileInfo tileInfo; // data length and offset for read
    public int bundleNum; // index into the zoom level bundles list

    public TPKTile(long col, long row, String imageFormat, TileInfo tileInfo, int bundleNum) {
        this.col = col;
        this.row = row;
        this.imageFormat = imageFormat;
        this.tileInfo = tileInfo;
        this.bundleNum = bundleNum;
    }

    public void setTileData(byte[] tileData) {
        this.tileData = tileData;
    }

    /** Sort comparator -- sorts tiles by bundle and then the tile data offset */
    public static class TPKTileSorter implements Comparator<TPKTile> {

        @Override
        public int compare(TPKTile a, TPKTile b) {
            int temp = a.bundleNum - b.bundleNum;
            if (temp != 0) {
                return temp;
            }
            return (int) (a.tileInfo.tileDataOffset - b.tileInfo.tileDataOffset);
        }
    }

    /**
     * Temporary utility object to keep tile location/length info
     *
     * <p>Note that in a V1 cache only the tileDataOffset field is utilized as the length is stored
     * at that location (32-bit int) and the actual tile data is at tileDataOffset+4
     *
     * <p>In a V2 cache both fields are used and populated directly from the tile's index entry
     */
    public static class TileInfo {
        int tileLength;
        long tileDataOffset;

        TileInfo(int tileLength, long tileDataOffset) {
            this.tileLength = tileLength;
            this.tileDataOffset = tileDataOffset;
        }
    }
}
