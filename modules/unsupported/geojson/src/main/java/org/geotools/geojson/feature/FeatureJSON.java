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
package org.geotools.geojson.feature;

import static org.geotools.geojson.GeoJSONUtil.array;
import static org.geotools.geojson.GeoJSONUtil.entry;
import static org.geotools.geojson.GeoJSONUtil.string;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONStreamAware;
import org.json.simple.parser.JSONParser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Reads and writes feature objects to and from geojson.
 * <p>
 * <pre>
 * SimpleFeature feature = ...;
 * 
 * FeatureJSON io = new FeatureJSON();
 * io.writeFeature(feature, "feature.json"));
 * 
 * Iterator<Feature> features = io.streamFeatureCollection("features.json");
 * while(features.hasNext()) {
 *   feature = features.next();
 *   ...
 * }
 * </pre>
 * </p>
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/geojson/src/main/java/org/geotools/geojson/feature/FeatureJSON.java $
 */
public class FeatureJSON {

    GeometryJSON gjson;
    SimpleFeatureType featureType;
    AttributeIO attio;
    boolean encodeFeatureBounds = false;
    boolean encodeFeatureCollectionBounds = false;
    boolean encodeFeatureCRS = false;
    boolean encodeFeatureCollectionCRS = false;
    
    public FeatureJSON() {
        this(new GeometryJSON());
    }

    public FeatureJSON(GeometryJSON gjson) {
        this.gjson = gjson; 
        attio = new DefaultAttributeIO();
    }

    /**
     * Sets the target feature type for parsing.
     * <p>
     * Setting the target feature type will help the geojson parser determine the type of feature
     * properties during properties. When the type is not around all properties are returned as 
     * a string.
     * </p>
     *  
     * @param featureType The feature type. Parsed features will reference this feature type.
     */
    public void setFeatureType(SimpleFeatureType featureType) {
        this.featureType = featureType;
        this.attio = new FeatureTypeAttributeIO(featureType);
    }

    /**
     * Sets the flag controlling whether feature bounds are encoded.
     *  
     * @see #isEncodeFeatureBounds()
     */
    public void setEncodeFeatureBounds(boolean encodeFeatureBounds) {
        this.encodeFeatureBounds = encodeFeatureBounds;
    }
    
    /**
     * The flag controlling whether feature bounds are encoded.
     * <p>
     * When set each feature object will contain a "bbox" attribute whose value is an array  
     * containing the elements of the bounding box (in x1,y1,x2,y2 order) of the feature 
     * </p>
     */
    public boolean isEncodeFeatureBounds() {
        return encodeFeatureBounds;
    }
    
    /**
     * Sets the flag controlling whether feature collection bounds are encoded.
     *  
     * @see #isEncodeFeatureCollectionBounds()
     */
    public void setEncodeFeatureCollectionBounds(boolean encodeFeatureCollectionBounds) {
        this.encodeFeatureCollectionBounds = encodeFeatureCollectionBounds;
    }
    
    /**
     * The flag controlling whether feature collection bounds are encoded.
     * <p>
     * When set the feature collection object will contain a "bbox" attribute whose value is an 
     * array containing elements of the bounding box (in x1,y1,x2,y2 order) of the feature 
     * collection.
     * </p>
     */
    public boolean isEncodeFeatureCollectionBounds() {
        return encodeFeatureCollectionBounds;
    }
    
    /**
     * Sets the flag controlling whether feature coordinate reference systems are encoded.
     *  
     * @see #isEncodeFeatureCRS()
     */
    public void setEncodeFeatureCRS(boolean encodeFeatureCRS) {
        this.encodeFeatureCRS = encodeFeatureCRS;
    }
    
    /**
     * The flag controlling whether feature coordinate reference systems are encoded.
     * <p> 
     * When set each feature object will contain a "crs" attribute describing the 
     * coordinate reference system of the feature.
     * </p>
     *
     */
    public boolean isEncodeFeatureCRS() {
        return encodeFeatureCRS;
    }
    
    /**
     * Sets the flag controlling whether feature collection coordinate reference systems are encoded.
     *  
     * @see #isEncodeFeatureCollectionCRS()
     */
    public void setEncodeFeatureCollectionCRS(boolean encodeFeatureCollectionCRS) {
        this.encodeFeatureCollectionCRS = encodeFeatureCollectionCRS;
    }
    
    /**
     * The flag controlling whether feature collection coordinate reference systems are encoded.
     * <p>
     * When set the feature collection object will contain a "crs" attribute describing the 
     * coordinate reference system of the feature collection.
     * </p>
     */
    public boolean isEncodeFeatureCollectionCRS() {
        return encodeFeatureCollectionCRS;
    }
    
    /**
     * Writes a feature as GeoJSON.
     * 
     * @param feature The feature.
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     */
    public void writeFeature(SimpleFeature feature, Object output) throws IOException {
        GeoJSONUtil.encode(new FeatureEncoder(feature).toJSONString(), output);
    }

    /**
     * Writes a feature as GeoJSON returning the result as a string.
     * 
     * @param geometry The geometry.
     * 
     * @return The geometry encoded as GeoJSON 
     */
    public String toString(SimpleFeature feature) throws IOException {
        StringWriter w = new StringWriter();
        writeFeature(feature, w);
        return w.toString();
    }
    
    /**
     * Reads a feature from GeoJSON.
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * @return The feature.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public SimpleFeature readFeature(Object input) throws IOException {
        return GeoJSONUtil.parse(new FeatureHandler(
            featureType != null ? new SimpleFeatureBuilder(featureType): null, attio
        ), input, false);
    }
    
    /**
     * Writes a feature collection as GeoJSON.
     * 
     * @param features The feature collection.
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     */
    public void writeFeatureCollection(FeatureCollection features, Object output) throws IOException {
        LinkedHashMap obj = new LinkedHashMap();
        obj.put("type", "FeatureCollection");
        if (encodeFeatureCollectionBounds || encodeFeatureCollectionCRS) {
            final ReferencedEnvelope bounds = features.getBounds();
            
            if (encodeFeatureCollectionBounds) {
                obj.put("bbox", new JSONStreamAware() {
                    public void writeJSONString(Writer out) throws IOException {
                        JSONArray.writeJSONString(Arrays.asList(bounds.getMinX(),
                                bounds.getMinY(),bounds.getMaxX(),bounds.getMaxY()), out);
                    }
                });
            }
            
            if (encodeFeatureCollectionCRS) {
                obj.put("crs", createCRS(bounds.getCoordinateReferenceSystem()));
            }
        }
        obj.put("features", new FeatureCollectionEncoder(features, gjson));
        GeoJSONUtil.encode(obj, output);
    }
    
    /**
     * Reads a feature collection from GeoJSON.
     * <p>
     * Warning that this method will load the entire feature collection into memory. For large 
     * feature collections {@link #streamFeatureCollection(Object)} should be used. 
     * </p>
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * @return The feature collection.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public FeatureCollection readFeatureCollection(Object input) throws IOException {
        DefaultFeatureCollection features = new DefaultFeatureCollection(null, null);
        FeatureCollectionIterator it = (FeatureCollectionIterator) streamFeatureCollection(input);
        while(it.hasNext()) {
            features.add(it.next());
        }
        return features;
    }
    
    /**
     * Reads a feature collection from GeoJSON streaming back the contents via an iterator.
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * 
     * @return A feature iterator.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public FeatureIterator<SimpleFeature> streamFeatureCollection(Object input) throws IOException {
        return new FeatureCollectionIterator(input);
    }

    /**
     * Writes a feature collection as GeoJSON returning the result as a string.
     * 
     * @param features The feature collection.
     * 
     * @return The feature collection encoded as GeoJSON 
     */
    public String toString(FeatureCollection features) throws IOException {
        StringWriter w = new StringWriter();
        writeFeatureCollection(features, w);
        return w.toString();
    }
    
    /**
     * Writes a coordinate reference system as GeoJSON.
     * 
     * @param crs The coordinate reference system. 
     * @param output The output. See {@link GeoJSONUtil#toWriter(Object)} for details.
     */
    public void writeCRS(CoordinateReferenceSystem crs, Object output) throws IOException {
        GeoJSONUtil.encode(createCRS(crs), output);
    }
    
    Map<String,Object> createCRS(CoordinateReferenceSystem crs) throws IOException {
        Map<String,Object> obj = new LinkedHashMap<String,Object>();
        obj.put("type", "name");
        
        Map<String,Object> props = new LinkedHashMap<String, Object>();
        try {
            props.put("name", CRS.lookupIdentifier(crs, true));
        } 
        catch (FactoryException e) {
            throw (IOException) new IOException("Error looking up crs identifier").initCause(e);
        }
        
        obj.put("properties", props);
        return obj;
    }
    
    /**
     * Reads a coordinate reference system from GeoJSON.
     * <p>
     * This method only handles named coordinate reference system objects.
     * </p>
     * 
     * @param input The input. See {@link GeoJSONUtil#toReader(Object)} for details.
     * @return The coordinate reference system.
     * 
     * @throws IOException In the event of a parsing error or if the input json is invalid.
     */
    public CoordinateReferenceSystem readCRS(Object input) throws IOException {
        return GeoJSONUtil.parse(new CRSHandler(), input, false);
    }
    
    /**
     * Writes a coordinate reference system as GeoJSON returning the result as a string.
     * 
     * @param crs The coordinate reference system.
     * 
     * @return The coordinate reference system encoded as GeoJSON 
     */
    public String toString(CoordinateReferenceSystem crs) throws IOException {
        StringWriter writer = new StringWriter();
        writeCRS(crs, writer);
        return writer.toString();
   }

    class FeatureEncoder implements JSONAware {

        SimpleFeatureType featureType;
        SimpleFeature feature;
        
        public FeatureEncoder(SimpleFeature feature) {
            this(feature.getType());
            this.feature = feature;
        }
        
        public FeatureEncoder(SimpleFeatureType featureType) {
            this.featureType = featureType;
        }
        
        public String toJSONString(SimpleFeature feature) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            
            //type
            entry("type", "Feature", sb);
            sb.append(",");
            
            //crs
            if (encodeFeatureCRS) {
                CoordinateReferenceSystem crs = 
                    feature.getFeatureType().getCoordinateReferenceSystem();
                if (crs != null) {
                    try {
                        string("crs", sb).append(":");
                        sb.append(FeatureJSON.this.toString(crs)).append(",");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            //bounding box
            if (encodeFeatureBounds) {
                BoundingBox bbox = feature.getBounds();
                string("bbox", sb).append(":");
                sb.append(gjson.toString(bbox)).append(",");
            }
            
            //geometry
            if (feature.getDefaultGeometry() != null) {
                string("geometry", sb).append(":")
                .append(gjson.toString((Geometry) feature.getDefaultGeometry()));    
                sb.append(",");
            }
            
            //properties
            int gindex = featureType.getGeometryDescriptor() != null ? 
                    featureType.indexOf(featureType.getGeometryDescriptor().getLocalName()) : 
                    -1;
            
            string("properties", sb).append(":").append("{");
            boolean attributesWritten = false;
            for (int i = 0; i < featureType.getAttributeCount(); i++) {
                AttributeDescriptor ad = featureType.getDescriptor(i);
                
                // skip the default geometry, it's already encoded
                if (i == gindex) {
                    continue;
                }
                
                Object value = feature.getAttribute(i);
                if (value == null) {
                    //skip
                    continue;
                }
                
                attributesWritten = true;
                
                // handle special types separately, everything else as a string or literal
                if (value instanceof Envelope) {
                    array(ad.getLocalName(), gjson.toString((Envelope)value), sb);
                } else if (value instanceof BoundingBox) {
                    array(ad.getLocalName(), gjson.toString((BoundingBox)value), sb);
                } else if (value instanceof Geometry) {
                    string(ad.getLocalName(), sb).append(":")
                    .append(gjson.toString((Geometry) value));
                } else {
                    entry(ad.getLocalName(), value, sb);
                }
                sb.append(",");
            }
            
            if(attributesWritten) {
                sb.setLength(sb.length()-1);
            }
            sb.append("},");
            
            //id
            entry("id", feature.getID(), sb);
            
            sb.append("}");
            return sb.toString();
        }
        
        public String toJSONString() {
            return toJSONString(feature);
        }
    }
    
    class FeatureCollectionEncoder implements JSONStreamAware {

        FeatureCollection features;
        GeometryJSON gjson;
        
        public FeatureCollectionEncoder(FeatureCollection features, GeometryJSON gjson) {
            this.features = features;
            this.gjson = gjson;
        }
        
        public void writeJSONString(Writer out) throws IOException {
            FeatureEncoder featureEncoder = 
                new FeatureEncoder((SimpleFeatureType) features.getSchema());
            
            out.write("[");
            FeatureIterator i = features.features();
            try {
                if (i.hasNext()) {
                    SimpleFeature f = (SimpleFeature) i.next();
                    out.write(featureEncoder.toJSONString(f));
                    
                    while(i.hasNext()) {
                        out.write(",");
                        f = (SimpleFeature) i.next();
                        out.write(featureEncoder.toJSONString(f));
                    }
                }
            }
            finally {
                features.close(i);
            }
            out.write("]");
            out.flush();
        }
    }

    class FeatureCollectionIterator implements FeatureIterator<SimpleFeature> {

        Reader reader;
        IFeatureCollectionHandler handler;
        JSONParser parser;
        SimpleFeature next;
        
        FeatureCollectionIterator(Object input) {
            try {
                this.reader = GeoJSONUtil.toReader(input);
            } 
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.parser = new JSONParser();
        }
        
        public boolean hasNext() {
            if (next != null) {
                return true;
            }
            
            if (handler == null) {
                handler = new FeatureCollectionHandler(featureType,  attio);
                //handler = GeoJSONUtil.trace(handler, IFeatureCollectionHandler.class);
            }
            next = readNext();
            return next != null;
        }

        public SimpleFeature next() {
            SimpleFeature feature = next;
            next = null;
            return feature;
        }
        
        SimpleFeature readNext() { 
            try {
                parser.parse(reader, handler, true);
                return handler.getValue(); 
            } 
            catch(Exception e) {
                throw new RuntimeException(e);
            }
            
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void close() {
            reader = null;
            parser = null;
            handler = null;
        }
    }
}
