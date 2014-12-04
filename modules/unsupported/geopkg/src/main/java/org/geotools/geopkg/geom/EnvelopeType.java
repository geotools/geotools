package org.geotools.geopkg.geom;

public enum EnvelopeType {
    NONE(0, 0), XY(1, 32), XYZ(2, 48), XYM(3, 48), XYZM(4, 64);

    private byte value;
    private byte length;

    private EnvelopeType(int value, int length) {
        this.value = (byte) value;
        this.length = (byte) length;
    }

    public static EnvelopeType valueOf(byte b) {
        for (EnvelopeType et : values()) {
            if (et.value == b) return et;
        }
        return null;
    }

    public byte getValue() {
        return value;
    }

    public byte getLength() {
        return length;
    }
}
