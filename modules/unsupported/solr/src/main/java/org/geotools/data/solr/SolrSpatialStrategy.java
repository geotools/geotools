/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.geotools.geometry.jts.JTS;
import org.opengis.feature.type.GeometryDescriptor;

import java.io.IOException;

import static java.lang.Double.parseDouble;

/**
 * Strategy interface for interacting with solr spatial types.
 * <p>
 *  Instances of this interface must be thread safe.
 * </p>
 */
public abstract class SolrSpatialStrategy {

    static final SolrSpatialStrategy DEFAULT = new DefaultStrategy();
    static final SolrSpatialStrategy BBOX = new DefaultStrategy();

    static SolrSpatialStrategy createStrategy(GeometryDescriptor att) {
        String solrType = (String) att.getUserData().get(SolrFeatureSource.KEY_SOLR_TYPE);
        if (solrType != null && solrType.endsWith("BBoxField")) {
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
            return String.format("%f %f %f %f", env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY());
        }

        @Override
        public Geometry decode(String str) throws ParseException {
            String[] bbox = str.split("\\s+");
            if (bbox.length != 4) {
                throw new ParseException("Illegal bounding box: " + str);
            }

            Envelope env = new Envelope(parseDouble(bbox[0]), parseDouble(bbox[2]),
                parseDouble(bbox[1]), parseDouble(bbox[3]));
            return JTS.toGeometry(env);
        }
    }
}
