/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml2.simple;

import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Helper class that encodes the geometries within GeometryCollection
 *
 * @author
 */
public class GenericGeometryEncoder extends GeometryEncoder<Geometry> {

    Encoder encoder;
    String gmlPrefix;

    public GenericGeometryEncoder(Encoder encoder) {
        super(encoder);
        this.encoder = encoder;
    }

    /** */
    public GenericGeometryEncoder(Encoder encoder, String gmlPrefix) {
        super(encoder);
        this.encoder = encoder;
        this.gmlPrefix = gmlPrefix;
    }

    @Override
    public void encode(Geometry geometry, AttributesImpl atts, GMLWriter handler) throws Exception {
        if (geometry instanceof LineString) {
            LineStringEncoder lineString =
                    new LineStringEncoder(encoder, LineStringEncoder.LINE_STRING);
            lineString.encode((LineString) geometry, atts, handler);
        } else if (geometry instanceof Point) {
            PointEncoder pt = new PointEncoder(encoder, gmlPrefix == null ? "gml" : gmlPrefix);
            pt.encode((Point) geometry, atts, handler);
        } else if (geometry instanceof Polygon) {
            PolygonEncoder polygon = new PolygonEncoder(encoder, gmlPrefix);
            polygon.encode((Polygon) geometry, atts, handler);
        } else if (geometry instanceof MultiLineString) {
            MultiLineStringEncoder multiLineString = new MultiLineStringEncoder(encoder, gmlPrefix);
            multiLineString.encode((MultiLineString) geometry, atts, handler);
        } else if (geometry instanceof MultiPoint) {
            MultiPointEncoder multiPoint = new MultiPointEncoder(encoder, gmlPrefix);
            multiPoint.encode((MultiPoint) geometry, atts, handler);
        } else if (geometry instanceof MultiPolygon) {
            MultiPolygonEncoder multiPolygon = new MultiPolygonEncoder(encoder, gmlPrefix);
            multiPolygon.encode((MultiPolygon) geometry, atts, handler);
        } else if (geometry instanceof LinearRing) {
            LinearRingEncoder linearRing = new LinearRingEncoder(encoder, gmlPrefix);
            linearRing.encode((LinearRing) geometry, atts, handler);
        } else {
            throw new Exception("Unsupported geometry " + geometry.toString());
        }
    }
}
