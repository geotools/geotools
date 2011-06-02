/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.couchdb.client;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.Geometries;
import org.json.simple.JSONObject;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Everyone loves some utilities.
 * @author Ian Schneider (OpenGeo)
 */
public final class CouchDBUtils {

    private CouchDBUtils() {
    }

    /**
     * @todo this was spiked from GeoJSONUtil to allow adding the 'featureClass' property.
     * I suppose another way to do this would be to wrap the geojson in another object
     * and rewrite the spatial views, etc.
     * @todo GeoJSONUtil could be made more efficient and flexible by using Appendable
     */
    public static String writeJSON(SimpleFeature feature, String featureClass, GeometryJSON gjson) {
        SimpleFeatureType featureType = feature.getFeatureType();
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        //type
        GeoJSONUtil.entry("type", "Feature", sb);
        sb.append(',');
        GeoJSONUtil.entry("featureClass", featureClass, sb);
        sb.append(',');


        //crs @todo
            /*
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
         */
        //bounding box @todo
            /*
        if (encodeFeatureBounds) {
        BoundingBox bbox = feature.getBounds();
        string("bbox", sb).append(":");
        sb.append(gjson.toString(bbox)).append(",");
        }
         */

        //geometry
        if (feature.getDefaultGeometry() != null) {
            GeoJSONUtil.string("geometry", sb).append(":").append(gjson.toString((Geometry) feature.getDefaultGeometry()));
            sb.append(",");
        }

        //properties
        int gindex = featureType.getGeometryDescriptor() != null
                ? featureType.indexOf(featureType.getGeometryDescriptor().getLocalName())
                : -1;

        GeoJSONUtil.string("properties", sb).append(":").append("{");
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
                GeoJSONUtil.array(ad.getLocalName(), gjson.toString((Envelope) value), sb);
            } else if (value instanceof BoundingBox) {
                GeoJSONUtil.array(ad.getLocalName(), gjson.toString((BoundingBox) value), sb);
            } else if (value instanceof Geometry) {
                GeoJSONUtil.string(ad.getLocalName(), sb).append(":").append(gjson.toString((Geometry) value));
            } else {
                GeoJSONUtil.entry(ad.getLocalName(), value, sb);
            }
            sb.append(",");
        }

        if (attributesWritten) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("},");

        //id
        GeoJSONUtil.entry("id", feature.getID(), sb);

        sb.append("}");
        return sb.toString();
    }

    public static SimpleFeatureType createFeatureType(JSONObject obj, String name) throws IOException {

        JSONObject props = (JSONObject) obj.get("properties");
        JSONObject geom = (JSONObject) obj.get("geometry");

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(name);
        for (Object k : props.keySet()) {
            builder.add(k.toString(), String.class);
        }
        String geomType = geom.get("type").toString();
        Class clazz = Geometries.getForName(geomType).getBinding();
        if (clazz == null) {
            throw new IOException("Unable to parse geometry type from " + geomType);
        }
        builder.add("geometry", clazz, (CoordinateReferenceSystem) null); // @todo find type from name?
        return builder.buildFeatureType();
    }

    public static String stripComments(String json) {
        Pattern pat = Pattern.compile("/\\*(?:.)*?\\*/", Pattern.MULTILINE | Pattern.DOTALL);
        return pat.matcher(json).replaceAll("");
    }

    public static String read(File f) throws FileNotFoundException {
        String text = null;
        Scanner s = new Scanner(f);
        try {
            text = s.useDelimiter("\\Z").next();
        } finally {
            s.close();
        }
        if (f.getName().endsWith(".json")) {
            // strip comments
            text = stripComments(text);
        }
        return text;
    }
}
