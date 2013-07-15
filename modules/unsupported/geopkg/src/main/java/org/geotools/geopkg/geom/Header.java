package org.geotools.geopkg.geom;

import com.vividsolutions.jts.geom.Envelope;

class Header {
    Flags flags;
    int srid;
    Envelope envelope;

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