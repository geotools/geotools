/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.geom;

import org.locationtech.jts.geom.Envelope;

/**
 * The Geopackage Geometry BLOB Header (see Geopackage specs).
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
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
