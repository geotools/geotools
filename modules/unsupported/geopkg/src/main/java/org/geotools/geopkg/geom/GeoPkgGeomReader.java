package org.geotools.geopkg.geom;

import java.io.IOException;
import java.io.InputStream;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ByteArrayInStream;
import com.vividsolutions.jts.io.ByteOrderDataInStream;
import com.vividsolutions.jts.io.InStream;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

public class GeoPkgGeomReader {

    public Geometry read(byte[] bytes) throws IOException {
        return read(new ByteArrayInStream(bytes));
    }

    public Geometry read(InputStream in) throws IOException {
        return read(new InputStreamInStream(in));
    }
    
    Geometry read(InStream input) throws IOException {
        // read the header
        Header h = readHeader(input);
        
        // read the geometry
        try {
            Geometry g = new WKBReader().read(input);
            g.setSRID(h.srid);
            return g;
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }

    /*
     * OptimizedGeoPackageBinary {
     * byte[3] magic = 0x47504230; // 'GPB'
     * byte flags;                 // see flags layout below
     * unit32 srid;
     * double[] envelope;          // see flags envelope contents indicator code below
     * WKBGeometry geometry;       // per OGC 06-103r4 clause 8
     *
     * 
     * flags layout:
     *   bit     7       6       5       4       3       2       1       0
     *   use     V       V       V       V       E       E       E       B

     *   use:
     *   V: version number (4-bit unsigned integer)
     *      0 = version 1
     *      
     *   E: envelope contents indicator code (3-bit unsigned integer)
     *     value |                    description                               | envelope length (bytes)
     *       0   | no envelope (space saving slower indexing option)            |      0
     *       1   | envelope is [minx, maxx, miny, maxy]                         |      32
     *       2   | envelope is [minx, maxx, miny, maxy, minz, maxz]             |      48
     *       3   | envelope is [minx, maxx, miny, maxy, minm, maxm]             |      48
     *       4   | envelope is [minx, maxx, miny, maxy, minz, maxz, minm, maxm] |      64
     *   B: byte order for header values (1-bit Boolean)
     *       0 = Big Endian   (most significant bit first)
     *       1 = Little Endian (least significant bit first)
     */
    Header readHeader(InStream in) throws IOException {
        Header h = new Header();

        // read first 4 bytes  
        // TODO: something with the magic number
        byte[] buf = new byte[4];
        in.read(buf);

        // next byte flags
        h.flags = new Flags((byte)buf[3]);

        // set endianness
        ByteOrderDataInStream din = new ByteOrderDataInStream(in);
        din.setOrder(h.flags.getEndianess());

        // read the srid
        h.srid = din.readInt();

        // read the envlope
        if (h.flags.getEnvelopeIndicator() != EnvelopeType.NONE) {
            double x1 = din.readDouble();
            double x2 = din.readDouble();
            double y1 = din.readDouble();
            double y2 = din.readDouble();
    
            if (h.flags.getEnvelopeIndicator().value > 1) {
                // 2 = minz,maxz; 3 = minm,maxm - we ignore these for now 
                din.readDouble();
                din.readDouble();
            }
    
            if (h.flags.getEnvelopeIndicator().value > 3) {
                // 4 = minz,maxz,minm,maxm - we ignore these for now
                din.readDouble();
                din.readDouble();
            }
    
            h.envelope = new Envelope(x1, x2, y1, y2);
        }
        return h;
    }
    

}
