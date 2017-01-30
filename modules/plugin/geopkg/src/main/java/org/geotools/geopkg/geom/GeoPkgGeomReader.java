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

import java.io.IOException;
import java.io.InputStream;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ByteArrayInStream;
import com.vividsolutions.jts.io.ByteOrderDataInStream;
import com.vividsolutions.jts.io.InStream;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

/**
 * Translates a GeoPackage geometry BLOB to a vividsolutions Geometry.
 * 
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class GeoPkgGeomReader {
    
    protected InStream input;
    
    protected GeometryHeader header = null;
    
    protected Geometry geometry = null;
    
    private GeometryFactory factory = new GeometryFactory();
    
    public GeoPkgGeomReader(InStream input) {
        this.input = input;
    }
    
    public GeoPkgGeomReader(InputStream input) throws IOException {
        this.input = new InputStreamInStream(input);
    }
    
    public GeoPkgGeomReader(byte[] bytes) {
        this.input = new ByteArrayInStream(bytes);
    }
        
    public GeometryHeader getHeader() throws IOException {
        if (header == null) {
            header = readHeader();
        }
        return header;
    }
    
    public Geometry get() throws IOException {
        if (header == null) {
            header = readHeader();
        }
        if (geometry == null) {
            geometry = read();
        }
        return geometry;
    }
    
    public Envelope getEnvelope() throws IOException {
        if (getHeader().getFlags().getEnvelopeIndicator() == EnvelopeType.NONE) {
            return get().getEnvelopeInternal();
        } else {
            return getHeader().getEnvelope();
        }
    }
    
    protected Geometry read() throws IOException { //header must be read!      
        // read the geometry
        try {
            WKBReader wkbReader = new WKBReader(factory);
            Geometry g = wkbReader.read(input);
            g.setSRID(header.getSrid());
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
     *   use     -       -       X       Y       E       E       E       B

     *   use:
     *   X: GeoPackageBinary type (0: StandardGeoPackageBinary, 1: ExtendedGeoPackageBinary)
     *   Y: 0: non-empty geometry, 1: empty geometry
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
    protected GeometryHeader readHeader() throws IOException {
        GeometryHeader h = new GeometryHeader();

        // read first 4 bytes  
        // TODO: something with the magic number
        byte[] buf = new byte[4];
        input.read(buf);

        // next byte flags
        h.setFlags(new GeometryHeaderFlags((byte)buf[3]));

        // set endianess
        ByteOrderDataInStream din = new ByteOrderDataInStream(input);
        din.setOrder(h.getFlags().getEndianess());

        // read the srid
        h.setSrid(din.readInt());

        // read the envelope
        if (h.getFlags().getEnvelopeIndicator() != EnvelopeType.NONE) {
            double x1 = din.readDouble();
            double x2 = din.readDouble();
            double y1 = din.readDouble();
            double y2 = din.readDouble();
    
            if (h.getFlags().getEnvelopeIndicator().getValue() > 1) {
                // 2 = minz,maxz; 3 = minm,maxm - we ignore these for now 
                din.readDouble();
                din.readDouble();
            }
    
            if (h.getFlags().getEnvelopeIndicator().getValue() > 3) {
                // 4 = minz,maxz,minm,maxm - we ignore these for now
                din.readDouble();
                din.readDouble();
            }
    
            h.setEnvelope (new Envelope(x1, x2, y1, y2));
        }
        return h;
    }

    /**
     * @return the factory
     */
    public GeometryFactory getFactory() {
        return factory;
    }

    /**
     * @param factory the factory to set
     */
    public void setFactory(GeometryFactory factory) {
        if(factory!=null) {
            this.factory = factory;
        }
    }
    

}
