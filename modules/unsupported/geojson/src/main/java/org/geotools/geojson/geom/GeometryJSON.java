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
package org.geotools.geojson.geom;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.IContentHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Reads and writes geometry objects to and from geojson.
 * <p>
 * <pre>
 * Point point = new Point(1,2);
 * 
 * GeometryJSON g = new GeometryJSON();
 * g.writePoint(point, "point.json"));
 * Point point2 = g.readPoint("point.json");
 * 
 * Geometry geometry = ...;
 * g.write(geometry, new File("geometry.json"));
 * geometry = g.read("geometry.json");
 * 
 * </pre>
 * </p>
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/geojson/src/main/java/org/geotools/geojson/geom/GeometryJSON.java $
 */
public class GeometryJSON {

    GeometryFactory factory = new GeometryFactory();
    boolean trace = false;
    int decimals;
    double scale;

    /**
     * Constructs a geometry json instance.
     */
    public GeometryJSON() {
        this(4);
    }
    
    /**
     * Constructs a geometry json instance specifying the number of decimals
     * to use when encoding floating point numbers.
     */
    public GeometryJSON(int decimals) {
        this.decimals = decimals;
        this.scale = Math.pow(10, decimals);
    }
    
    /**
     * Sets trace flag.
     */
    public void setTrace(boolean trace) {
        this.trace = trace;
    }
    
    /**
     * Tracing flag. 
     * <p>
     * When this flag is set parsed documents will be echoed to stdout 
     * during parsing.
     * </p>
     */
    public boolean isTrace() {
        return trace;
    }
    
    /**
     * Writes a Geometry instance as GeoJSON.
     * 
     * @param geometry The geometry.
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     */
    public void write(Geometry geometry, Object output) throws IOException {
        GeoJSONUtil.encode(create(geometry), output);
    }
    
    /**
     * Writes a Geometry instance as GeoJSON returning the result as a string.
     * 
     * @param geometry The geometry.
     * 
     * @return The geometry encoded as GeoJSON 
     */
    public String toString(Geometry geometry) {
        StringWriter w = new StringWriter();
        try {
            write(geometry, w);
            return w.toString();
        } 
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    Map<String,Object> create(Geometry geometry) {
        if (geometry instanceof Point) {
            return createPoint((Point)geometry);
        }
        
        if (geometry instanceof LineString) {
            return createLine((LineString)geometry);
        }
        
        if (geometry instanceof Polygon) {
            return createPolygon((Polygon)geometry);
        }
        
        if (geometry instanceof MultiPoint) {
            return createMultiPoint((MultiPoint)geometry);
        }
        
        if (geometry instanceof MultiLineString) {
            return createMultiLine((MultiLineString)geometry);
        }
        
        if (geometry instanceof MultiPolygon) {
            return createMultiPolygon((MultiPolygon)geometry);
        }
        
        if (geometry instanceof GeometryCollection) {
            return createGeometryCollection((GeometryCollection)geometry);
        }
        
        throw new IllegalArgumentException("Unable to encode object " + geometry);
    }
    
    /**
     * Reads a Geometry instance from GeoJSON.
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * 
     * @return The geometry instance.
     */
    public Geometry read(Object input) throws IOException {
        return parse(new GeometryHandler(factory), input); 
    }
    
    /**
     * Writes a Point as GeoJSON.
     * 
     * @param point The point.
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public void writePoint(Point point, Object output) throws IOException {
        encode(createPoint(point), output);
    }

    Map<String,Object> createPoint(Point point) {
        LinkedHashMap obj = new LinkedHashMap();
        
        obj.put("type", "Point");
        obj.put("coordinates", new CoordinateSequenceEncoder(point.getCoordinateSequence(), scale));        
        return obj;
    }
    
    /**
     * Reads a Point from GeoJSON.
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * 
     * @return The point.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public Point readPoint(Object input) throws IOException {
        return parse(new PointHandler(factory), input);
    }
    
    /**
     * Writes a LineString as GeoJSON.
     * 
     * @param line The line string.
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     */
    public void writeLine(LineString line, Object output) throws IOException {
        encode(createLine(line), output);
    }
    
    Map<String,Object> createLine(LineString line) {
        LinkedHashMap obj = new LinkedHashMap();
        
        obj.put("type", "LineString");
        obj.put("coordinates", new CoordinateSequenceEncoder(line.getCoordinateSequence(), scale));
        return obj;
    }
    
    /**
     * Reads a LineString from GeoJSON.
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * 
     * @return The line string.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public LineString readLine(Object input) throws IOException {
        return parse(new LineHandler(factory), input);
    }
    
    /**
     * Writes a Polygon as GeoJSON.
     * 
     * @param poly The polygon.
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     */
    public void writePolygon(Polygon poly, Object output) throws IOException {
        encode(createPolygon(poly), output);
    }
    
    Map<String,Object> createPolygon(Polygon poly) {
        LinkedHashMap obj = new LinkedHashMap();
        
        obj.put("type", "Polygon");
        obj.put("coordinates", toList(poly));
        return obj;
    }
    
    /**
     * Reads a Polygon from GeoJSON.
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * 
     * @return The polygon.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public Polygon readPolygon(Object input) throws IOException {
        return parse(new PolygonHandler(factory), input);
    }
    
    /**
     * Writes a MultiPoint as GeoJSON.
     * 
     * @param mpoint The multi point.
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     */
    public void writeMultiPoint(MultiPoint mpoint, Object output) throws IOException {
        encode(createMultiPoint(mpoint), output);
    }
    
    Map<String,Object> createMultiPoint(MultiPoint mpoint) {
        LinkedHashMap obj = new LinkedHashMap();
        
        obj.put("type", "MultiPoint");
        obj.put("coordinates", toList(mpoint));
        return obj;
    }
    
    /**
     * Reads a MultiPoint from GeoJSON.
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * 
     * @return The multi point.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public MultiPoint readMultiPoint(Object input) throws IOException {
        return parse(new MultiPointHandler(factory), input);
    }
    
    /**
     * Writes a MultiLineString as GeoJSON.
     * 
     * @param mline The multi line string.
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     */
    public void writeMultiLine(MultiLineString mline, Object output) throws IOException {
        encode(createMultiLine(mline), output);
    }
    
    Map<String,Object> createMultiLine(MultiLineString mline) {
        LinkedHashMap obj = new LinkedHashMap();
        
        obj.put("type", "MultiLineString");
        obj.put("coordinates", toList(mline));
        return obj;
    }
    
    /**
     * Reads a MultiLineString from GeoJSON.
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * 
     * @return The multi line string.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public MultiLineString readMultiLine(Object input) throws IOException {
        return parse(new MultiLineHandler(factory), input);
    }
    
    /**
     * Writes a MultiPolygon as GeoJSON.
     * 
     * @param mpoly The multi polygon.
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     */
    public void writeMultiPolygon(MultiPolygon mpoly, Object output) throws IOException {
        encode(createMultiPolygon(mpoly), output);
    }
    
    Map<String,Object> createMultiPolygon(MultiPolygon mpoly) {
        LinkedHashMap obj = new LinkedHashMap();

        obj.put("type", "MultiPolygon");
        obj.put("coordinates", toList(mpoly));
        return obj;
    }
    
    /**
     * Reads a MultiPolygon from GeoJSON.
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * 
     * @return The multi polygon.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public MultiPolygon readMultiPolygon(Object input) throws IOException {
        return parse(new MultiPolygonHandler(factory), input);
    }
    
    /**
     * Writes a GeometryCollection as GeoJSON.
     * 
     * @param gcol The geometry collection.
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     */
    public void writeGeometryCollection(GeometryCollection gcol, Object output) throws IOException {
        encode(createGeometryCollection(gcol), output);
    }
    
    Map<String,Object> createGeometryCollection(GeometryCollection gcol) {
        LinkedHashMap obj = new LinkedHashMap();
        
        ArrayList geoms = new ArrayList(gcol.getNumGeometries());
        for (int i = 0; i < gcol.getNumGeometries(); i++) {
            geoms.add(create(gcol.getGeometryN(i)));
        }
        
        obj.put("type", "GeometryCollection");
        obj.put("geometries", geoms);
        return obj;
    }
    
    /**
     * Reads a GeometryCollection from GeoJSON.
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * 
     * @return The geometry collection.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public GeometryCollection readGeometryCollection(Object input) throws IOException {
        return parse(new GeometryCollectionHandler(factory), input);
    }
    
    /**
     * Writes an BoundingBox instance as GeoJSON returning the result as a string.
     * 
     * @param bbox The bounding box.
     * 
     * @return The bounding box encoded as GeoJSON 
     */
    public String toString(BoundingBox bbox) {
        return new StringBuffer().append("[").append(bbox.getMinX()).append(",")
            .append(bbox.getMinY()).append(",").append(bbox.getMaxX()).append(",")
            .append(bbox.getMaxY()).append("]").toString();
    }
    
    /**
     * Writes an Envelope instance as GeoJSON returning the result as a string.
     * 
     * @param e The envelope
     * 
     * @return The envelope encoded as GeoJSON 
     */
    public String toString(Envelope e) {
        return new StringBuffer().append("[").append(e.getMinX()).append(",")
            .append(e.getMinY()).append(",").append(e.getMaxX()).append(",")
            .append(e.getMaxY()).append("]").toString();
    }
    
    <G extends Geometry> G parse(IContentHandler<G> handler, Object input) throws IOException {
        return GeoJSONUtil.parse(handler, input, trace);
    }
    
    void encode(Map<String,Object> obj, Object output) throws IOException {
        GeoJSONUtil.encode(obj, output);
    }
    
    List toList(Polygon poly) {
        ArrayList list = new ArrayList();
        list.add(new CoordinateSequenceEncoder(poly.getExteriorRing().getCoordinateSequence(), scale));
        
        for (int i = 0; i < poly.getNumInteriorRing(); i++) {
            list.add(new CoordinateSequenceEncoder(poly.getInteriorRingN(i).getCoordinateSequence(), scale));
        }
        
        return list;
    }
    
    List toList(GeometryCollection mgeom) {
        ArrayList list = new ArrayList(mgeom.getNumGeometries());
        for (int i = 0; i < mgeom.getNumGeometries(); i++) {
            Geometry g = mgeom.getGeometryN(i);
            if (g instanceof Polygon) {
                list.add(toList((Polygon)g));
            }
            else if (g instanceof LineString){
                list.add(new CoordinateSequenceEncoder(((LineString)g).getCoordinateSequence(), scale));
            }
            else if (g instanceof Point) {
                list.add(new CoordinateSequenceEncoder(((Point)g).getCoordinateSequence(), scale));
            }
        }
        return list;
    }
    static class CoordinateSequenceEncoder implements JSONAware /*, JSONStreamAware*/ {

        /**
         * The min value at which the decimal notation is used 
         * (below it, the computerized scientific one is used instead)
         */
        private static final double DECIMAL_MIN = Math.pow(10, -3);
        
        /**
         * The max value at which the decimal notation is used 
         * (above it, the computerized scientific one is used instead)
         */
        private static final double DECIMAL_MAX = Math.pow(10, 7);
        
        CoordinateSequence seq;
        double scale;
        
        CoordinateSequenceEncoder(CoordinateSequence seq, double scale) {
            this.seq = seq;
            this.scale = scale;
        }
        
        public String toJSONString() {
            int size = seq.size();
            
            StringBuilder sb = new StringBuilder();
            if (size > 1) {
                sb.append("["); 
            }
            
            for (int i = 0; i < seq.size(); i++) {
                sb.append("[");
                formatDecimal(seq.getX(i), sb);
                
                sb.append(",");
                formatDecimal(seq.getY(i), sb);
                
                sb.append("],");
            }
            sb.setLength(sb.length()-1);
            
            if (size > 1) {
                sb.append("]");
            }
            
            return sb.toString();
        }

        public void writeJSONString(Writer out) throws IOException {
            int size = seq.size();
            
            if (size > 1) {
                out.write("[");
            }
            
            for (int i = 0; i < seq.size(); i++) {
                out.write("[");
                out.write(String.valueOf(seq.getX(i)));
                out.write(",");
                out.write(String.valueOf(seq.getY(i)));
                out.write("]");
                if (i < seq.size()-1) {
                    out.write(",");
                }
            }
            
            if (size > 1) {
                out.write("]");
            }
            
        }
        
        private void formatDecimal(double x, StringBuilder sb) {
            if(Math.abs(x) >= DECIMAL_MIN && x < DECIMAL_MAX) {
                x = Math.floor(x * scale + 0.5) / scale;
                long lx = (long) x;
                if(lx == x)
                    sb.append(lx);
                else
                    sb.append(x);
            } else {
                sb.append(x);
            }
        }
    }
}
