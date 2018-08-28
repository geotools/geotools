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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.geotools.geopkg.geom.GeometryHeaderFlags.GeopackageBinaryType;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ByteOrderValues;
import org.locationtech.jts.io.OutStream;
import org.locationtech.jts.io.OutputStreamOutStream;
import org.locationtech.jts.io.WKBWriter;

/**
 * Translates a vividsolutions Geometry to a GeoPackage geometry BLOB.
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class GeoPkgGeomWriter {

    public static class Configuration {
        protected boolean writeEnvelope = true;

        public boolean isWriteEnvelope() {
            return writeEnvelope;
        }

        public void setWriteEnvelope(boolean writeEnvelope) {
            this.writeEnvelope = writeEnvelope;
        }
    }

    protected Configuration config;
    protected int dim;

    public GeoPkgGeomWriter() {
        this(2, new Configuration());
    }

    public GeoPkgGeomWriter(int dim) {
        this(dim, new Configuration());
    }

    public GeoPkgGeomWriter(Configuration config) {
        this(2, config);
    }

    public GeoPkgGeomWriter(int dim, Configuration config) {
        this.config = config;
        this.dim = dim;
    }

    public byte[] write(Geometry g) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        write(g, bout);
        return bout.toByteArray();
    }

    public void write(Geometry g, OutputStream out) throws IOException {
        write(g, new OutputStreamOutStream(out));
        out.flush();
    }

    void write(Geometry g, OutStream out) throws IOException {
        if (g == null) {
            return;
        }

        GeometryHeaderFlags flags = new GeometryHeaderFlags((byte) 0);

        flags.setBinaryType(GeopackageBinaryType.StandardGeoPackageBinary);
        flags.setEmpty(g.isEmpty());
        flags.setEndianess(ByteOrderValues.BIG_ENDIAN);
        flags.setEnvelopeIndicator(config.isWriteEnvelope() ? EnvelopeType.XY : EnvelopeType.NONE);

        GeometryHeader h = new GeometryHeader();
        h.setVersion((byte) 0);
        h.setFlags(flags);
        h.setSrid(g.getSRID());
        if (config.isWriteEnvelope()) {
            h.setEnvelope(g.getEnvelopeInternal());
        }

        // write out magic + flags + srid + envelope
        byte[] buf = new byte[8];
        buf[0] = 0x47;
        buf[1] = 0x50;
        buf[2] = h.getVersion();
        buf[3] = flags.toByte();
        out.write(buf, 4);

        int order = flags.getEndianess();
        ByteOrderValues.putInt(g.getSRID(), buf, order);
        out.write(buf, 4);

        if (flags.getEnvelopeIndicator() != EnvelopeType.NONE) {
            Envelope env = g.getEnvelopeInternal();
            ByteOrderValues.putDouble(env.getMinX(), buf, order);
            out.write(buf, 8);

            ByteOrderValues.putDouble(env.getMaxX(), buf, order);
            out.write(buf, 8);

            ByteOrderValues.putDouble(env.getMinY(), buf, order);
            out.write(buf, 8);

            ByteOrderValues.putDouble(env.getMaxY(), buf, order);
            out.write(buf, 8);
        }

        new WKBWriter(dim, order).write(g, out);
    }
}
