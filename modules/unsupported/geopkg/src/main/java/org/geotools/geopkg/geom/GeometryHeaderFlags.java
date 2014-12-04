package org.geotools.geopkg.geom;

import com.vividsolutions.jts.io.ByteOrderValues;

public class GeometryHeaderFlags {
    private byte b;

    public GeometryHeaderFlags(byte b) {
        this.b = b;
    }

    public byte getVersion() {
        return (byte) ((b & 0xf0) >> 4);
    }

    public void setVersion(byte ver) {
        b |= ((ver << 4) & 0xf0);
    }

    public EnvelopeType getEnvelopeIndicator() {
        return EnvelopeType.valueOf((byte) ((b & 0x0e) >> 1));
    }

    public void setEnvelopeIndicator(EnvelopeType e) {
        b |= ((e.getValue() << 1) & 0x0e);
    }

    public int getEndianess() {
        return (b & 0x01) == 1 ? ByteOrderValues.LITTLE_ENDIAN : ByteOrderValues.BIG_ENDIAN;
    }

    public void setEndianess(int endian) {
        byte e = (byte) (endian == ByteOrderValues.LITTLE_ENDIAN ? 1 : 0); 
        b |= (e & 0x01);
    }

    public byte toByte() {
        return b;
    }
}