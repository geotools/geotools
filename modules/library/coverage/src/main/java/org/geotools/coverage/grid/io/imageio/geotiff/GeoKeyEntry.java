/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.imageio.geotiff;

import java.text.MessageFormat;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.util.Utilities;

/**
 * This class is a holder for a GeoKey record containing four short values as specified in the GeoTiff spec. The values
 * are a GeoKey ID, the TIFFTag number of the location of this data, the count of values for this GeoKey, and the offset
 * (or value if the location is 0).
 *
 * <p>If the Tiff Tag location is 0, then the value is a Short and is contained in the offset. Otherwise, there is one
 * or more value in the specified external Tiff tag. The number is specified by the count field, and the offset into the
 * record is the offset field.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @author Mike Nidel
 */
public final class GeoKeyEntry implements Comparable<GeoKeyEntry> {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GeoKeyEntry)) return false;
        final GeoKeyEntry that = (GeoKeyEntry) obj;
        if (this.keyID == that.keyID
                && this.count == that.count
                && this.valueOffset == that.valueOffset
                && this.tiffTagLocation == that.tiffTagLocation) return true;

        return false;
    }

    @Override
    public int hashCode() {
        int hash = Utilities.hash(this.keyID, 1);
        hash = Utilities.hash(this.count, hash);
        hash = Utilities.hash(this.valueOffset, hash);
        hash = Utilities.hash(this.tiffTagLocation, hash);
        return hash;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("GeoKeyEntry (").append(count == 0 ? "VALUE" : "OFFSET").append("\n");
        builder.append("ID: ").append(keyID).append("\n");
        builder.append("COUNT: ").append(count).append("\n");
        builder.append("LOCATION: ").append(tiffTagLocation).append("\n");
        builder.append("VALUE_OFFSET: ").append(valueOffset).append("\n");
        return builder.toString();
    }

    /**
     * "KeyID" gives the key-ID value of the Key (identical in function to TIFF tag ID, but completely independent of
     * TIFF tag-space)
     */
    private int keyID;

    /**
     * "TIFFTagLocation" indicates which TIFF tag contains the value(s) of the Key: if TIFFTagLocation is 0, then the
     * value is SHORT, and is contained in the "Value_Offset" entry. Otherwise, the type (format) of the value is
     * implied by the TIFF-Type of the tag containing the value.
     */
    private int tiffTagLocation;

    /** "Count" indicates the number of values in this key. */
    private int count;

    /**
     * "Value_Offset" Value_Offset indicates the index- offset *into* the TagArray indicated by TIFFTagLocation, if it
     * is nonzero. If TIFFTagLocation=0, then Value_Offset contains the actual (SHORT) value of the Key, and Count=1 is
     * implied. Note that the offset is not a byte-offset, but rather an index based on the natural data type of the
     * specified tag array.
     */
    private int valueOffset;

    /**
     * Constructor of a {@link GeoKeyEntry}.
     *
     * @param keyID the id of this {@link GeoKeyEntry}.
     * @param tagLoc the location of this tag.
     */
    public GeoKeyEntry(int keyID, int tagLoc, int count, int offset) {
        setKeyID(keyID);
        setCount(count);
        setTiffTagLocation(tagLoc);
        setValueOffset(offset);
    }

    private static void ensureNotNegative(final String argument, final int value) {
        if (value < 0)
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, argument, value));
    }

    public int getKeyID() {
        return keyID;
    }

    public int getTiffTagLocation() {
        return tiffTagLocation;
    }

    public int getCount() {
        return count;
    }

    public int getValueOffset() {
        return valueOffset;
    }

    public void setCount(int count) {
        ensureNotNegative("COUNT", count);
        this.count = count;
    }

    public void setKeyID(int keyID) {
        ensureNotNegative("ID", keyID);
        this.keyID = keyID;
    }

    public void setTiffTagLocation(int tagLoc) {
        ensureNotNegative("LOCATION", tagLoc);
        this.tiffTagLocation = tagLoc;
    }

    public void setValueOffset(int valueOffset) {
        ensureNotNegative("VALUE_OFFSET", valueOffset);
        this.valueOffset = valueOffset;
    }

    public int[] getValues() {
        return new int[] {keyID, tiffTagLocation, count, valueOffset};
    }

    /**
     * According to GeoTIff spec:
     *
     * <p>In the TIFF spec it is required that TIFF tags be written out to the file in tag-ID sorted order. This is done
     * to avoid forcing software to perform N-squared sort operations when reading and writing tags.
     */
    @Override
    public int compareTo(GeoKeyEntry o) {
        return this.keyID > o.keyID ? 1 : this.keyID == o.keyID ? 0 : 1;
    }
}
