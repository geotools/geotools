package org.geotools.geopkg.geom;

import com.vividsolutions.jts.io.ByteOrderValues;

class Flags {
    byte b;

    Flags(byte b) {
        this.b = b;
    }

    byte getVersion() {
        return (byte) ((b & 0xf0) >> 4);
    }

    void setVersion(byte ver) {
        b |= ((ver << 4) & 0xf0);
    }

    EnvelopeType getEnvelopeIndicator() {
        return EnvelopeType.valueOf((byte) ((b & 0x0e) >> 1));
    }

    void setEnvelopeIndicator(EnvelopeType e) {
        b |= ((e.value << 1) & 0x0e);
    }

    int getEndianess() {
        return (b & 0x01) == 1 ? ByteOrderValues.LITTLE_ENDIAN : ByteOrderValues.BIG_ENDIAN;
    }

    void setEndianess(int endian) {
        byte e = (byte) (endian == ByteOrderValues.LITTLE_ENDIAN ? 1 : 0); 
        b |= (e & 0x01);
    }

    byte toByte() {
        return b;
    }
}