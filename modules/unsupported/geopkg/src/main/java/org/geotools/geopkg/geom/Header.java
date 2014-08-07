package org.geotools.geopkg.geom;

import com.vividsolutions.jts.geom.Envelope;

class Header {
    byte verison;
    Flags flags;
    int srid;
    Envelope envelope;

    public byte getVerison() {
        return verison;
    }

    public void setVerison(byte verison) {
        this.verison = verison;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
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