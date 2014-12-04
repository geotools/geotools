package org.geotools.geopkg.geom;

import com.vividsolutions.jts.geom.Envelope;

public class GeometryHeader {
    private byte version;
    private GeometryHeaderFlags flags;
    private int srid;
    private Envelope envelope;

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public GeometryHeaderFlags getFlags() {
        return flags;
    }

    public void setFlags(GeometryHeaderFlags flags) {
        this.flags = flags;
    }

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }
}