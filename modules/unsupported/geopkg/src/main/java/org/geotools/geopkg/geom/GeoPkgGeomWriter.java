package org.geotools.geopkg.geom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ByteOrderValues;
import com.vividsolutions.jts.io.OutStream;
import com.vividsolutions.jts.io.OutputStreamOutStream;
import com.vividsolutions.jts.io.WKBWriter;

public class GeoPkgGeomWriter {

    public byte[] write(Geometry g) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        write(g, bout);
        return  bout.toByteArray();
    }

    public void write(Geometry g, OutputStream out) throws IOException {
        write(g, new OutputStreamOutStream(out));
        out.flush();
    }

    void write(Geometry g, OutStream out) throws IOException {
        if (g == null) {
            return;
        }

        Flags flags = new Flags((byte)0);
        flags.setVersion((byte)0);
        flags.setEndianess(ByteOrderValues.BIG_ENDIAN);
        flags.setEnvelopeIndicator(g instanceof Point ? EnvelopeType.NONE : EnvelopeType.XY);

        Header h = new Header();
        h.setFlags(flags);
        h.setEnvelope(g.getEnvelopeInternal());
        h.setSrid(g.getSRID());
        
        //write out magic + flags + srid + envelope
        byte[] buf = new byte[8];
        //byte[] buf = new byte[4 + 4 + flags.getEnvelopeIndicator().length];
        buf[0] = 0x47;
        buf[1] = 0x50;
        buf[2] = 0x42;
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
        
        //out.write(buf, buf.length);

        new WKBWriter(2, order).write(g, out);
    }
}
