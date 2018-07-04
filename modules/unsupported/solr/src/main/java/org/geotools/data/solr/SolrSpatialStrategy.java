/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.solr;

import static java.lang.Double.parseDouble;

import java.util.Locale;
import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * Strategy interface for interacting with solr spatial types.
 *
 * <p>Instances of this interface must be thread safe.
 */
public abstract class SolrSpatialStrategy {

    static final SolrSpatialStrategy DEFAULT = new DefaultStrategy();
    static final SolrSpatialStrategy BBOX = new BBoxStrategy();

    static SolrSpatialStrategy createStrategy(GeometryDescriptor att) {
        String solrType = (String) att.getUserData().get(SolrFeatureSource.KEY_SOLR_TYPE);
        if (solrType != null && solrType.contains("BBoxField")) {
            return BBOX;
        }
        return DEFAULT;
    }

    public abstract String encode(Geometry geometry);

    public abstract Geometry decode(String str) throws ParseException;

    public static class DefaultStrategy extends SolrSpatialStrategy {

        @Override
        public String encode(Geometry geometry) {
            WKTWriter writer = new WKTWriter();
            return writer.write(geometry);
        }

        @Override
        public Geometry decode(String str) throws ParseException {
            return new WKTReader().read(str);
        }
    }

    public static class BBoxStrategy extends SolrSpatialStrategy {
        @Override
        public String encode(Geometry geometry) {
            Envelope env = geometry.getEnvelopeInternal();

            // JD: as of Solr 5 there is a new syntax for bbox.
            // TODO: add a version check to the datastore used to enable which format to use
            // return String.format(Locale.ENGLISH, "%f %f %f %f", env.getMinX(), env.getMinY(),
            //    env.getMaxX(), env.getMaxY());
            return String.format(
                    Locale.ENGLISH,
                    "ENVELOPE(%f,%f,%f,%f)",
                    env.getMinX(),
                    env.getMaxX(),
                    env.getMaxY(),
                    env.getMinY());
        }

        @Override
        public Geometry decode(String str) throws ParseException {
            if (str.toUpperCase().startsWith("ENVELOPE")) {
                return decodeCql(str);
            } else {
                return decodeBbox(str);
            }
        }

        private Geometry decodeCql(String str) throws ParseException {
            String[] bbox = str.split(",");
            if (bbox.length != 4) {
                throw new ParseException("Illegal bounding box: " + str);
            }
            bbox[0] = bbox[0].substring(bbox[0].indexOf("(") + 1, bbox[0].length());
            bbox[3] = bbox[3].substring(0, bbox[3].indexOf(")"));
            Envelope env =
                    new Envelope(
                            parseDouble(bbox[0]),
                            parseDouble(bbox[1]),
                            parseDouble(bbox[2]),
                            parseDouble(bbox[3]));
            return JTS.toGeometry(env);
        }

        private Geometry decodeBbox(String str) throws ParseException {
            String[] bbox = str.split("\\s+");
            if (bbox.length != 4) {
                throw new ParseException("Illegal bounding box: " + str);
            }
            Envelope env =
                    new Envelope(
                            parseDouble(bbox[0]),
                            parseDouble(bbox[2]),
                            parseDouble(bbox[1]),
                            parseDouble(bbox[3]));
            return JTS.toGeometry(env);
        }
    }
}
