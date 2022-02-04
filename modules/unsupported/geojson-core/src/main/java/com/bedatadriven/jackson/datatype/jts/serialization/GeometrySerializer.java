/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */
package com.bedatadriven.jackson.datatype.jts.serialization;

import static com.bedatadriven.jackson.datatype.jts.GeoJson.COORDINATES;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.GEOMETRIES;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.GEOMETRY_COLLECTION;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.LINE_STRING;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.MULTI_LINE_STRING;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.MULTI_POINT;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.MULTI_POLYGON;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.POINT;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.POLYGON;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.TYPE;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * When an EMPTY geometery is passed in we will write out an empty coordinates array (see
 * https://datatracker.ietf.org/doc/html/rfc7946#section-3.1)
 */
public class GeometrySerializer extends JsonSerializer<Geometry> {

    private RoundingMode roundingMode = RoundingMode.HALF_UP;
    NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);

    int maximumFractionDigits;

    int minimumFractionDigits;

    public GeometrySerializer(int minDecimals, int maxDecimals, RoundingMode rounding) {
        this.maximumFractionDigits = maxDecimals;
        this.minimumFractionDigits = minDecimals;
        format.setMinimumFractionDigits(minDecimals);
        format.setMaximumFractionDigits(maxDecimals);
        format.setRoundingMode(rounding);
        format.setGroupingUsed(false);
    }

    @Override
    public void serialize(Geometry value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        writeGeometry(jgen, value);
    }

    public void writeGeometry(JsonGenerator jgen, Geometry value) throws IOException {

        if (value instanceof Polygon) {
            writePolygon(jgen, (Polygon) value);

        } else if (value instanceof Point) {
            writePoint(jgen, (Point) value);

        } else if (value instanceof MultiPoint) {
            writeMultiPoint(jgen, (MultiPoint) value);

        } else if (value instanceof MultiPolygon) {
            writeMultiPolygon(jgen, (MultiPolygon) value);

        } else if (value instanceof LineString) {
            writeLineString(jgen, (LineString) value);

        } else if (value instanceof MultiLineString) {
            writeMultiLineString(jgen, (MultiLineString) value);

        } else if (value instanceof GeometryCollection) {
            writeGeometryCollection(jgen, (GeometryCollection) value);

        } else {
            throw new JsonMappingException(
                    jgen,
                    "Geometry type "
                            + value.getClass().getName()
                            + " cannot be serialized as GeoJSON."
                            + "Supported types are: "
                            + Arrays.asList(
                                    Point.class.getName(),
                                    LineString.class.getName(),
                                    Polygon.class.getName(),
                                    MultiPoint.class.getName(),
                                    MultiLineString.class.getName(),
                                    MultiPolygon.class.getName(),
                                    GeometryCollection.class.getName()));
        }
    }

    private void writeGeometryCollection(JsonGenerator jgen, GeometryCollection value)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(TYPE, GEOMETRY_COLLECTION);
        jgen.writeArrayFieldStart(GEOMETRIES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writeGeometry(jgen, value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writeMultiPoint(JsonGenerator jgen, MultiPoint value) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(TYPE, MULTI_POINT);
        jgen.writeArrayFieldStart(COORDINATES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePointCoords(jgen, (Point) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writeMultiLineString(JsonGenerator jgen, MultiLineString value)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(TYPE, MULTI_LINE_STRING);
        jgen.writeArrayFieldStart(COORDINATES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writeLineStringCoords(jgen, (LineString) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    @Override
    public Class<Geometry> handledType() {
        return Geometry.class;
    }

    private void writeMultiPolygon(JsonGenerator jgen, MultiPolygon value) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(TYPE, MULTI_POLYGON);
        jgen.writeArrayFieldStart(COORDINATES);

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePolygonCoordinates(jgen, (Polygon) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writePolygon(JsonGenerator jgen, Polygon value) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(TYPE, POLYGON);
        jgen.writeFieldName(COORDINATES);
        writePolygonCoordinates(jgen, value);

        jgen.writeEndObject();
    }

    private void writePolygonCoordinates(JsonGenerator jgen, Polygon value) throws IOException {
        jgen.writeStartArray();
        writeLineStringCoords(jgen, value.getExteriorRing());

        for (int i = 0; i < value.getNumInteriorRing(); ++i) {
            writeLineStringCoords(jgen, value.getInteriorRingN(i));
        }
        jgen.writeEndArray();
    }

    private void writeLineStringCoords(JsonGenerator jgen, LineString ring) throws IOException {
        jgen.writeStartArray();
        if (!ring.isEmpty()) {
            for (int i = 0; i != ring.getNumPoints(); ++i) {
                Point p = ring.getPointN(i);
                writePointCoords(jgen, p);
            }
        }
        jgen.writeEndArray();
    }

    private void writeLineString(JsonGenerator jgen, LineString lineString) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(TYPE, LINE_STRING);
        jgen.writeFieldName(COORDINATES);
        writeLineStringCoords(jgen, lineString);
        jgen.writeEndObject();
    }

    private void writePoint(JsonGenerator jgen, Point p) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(TYPE, POINT);
        jgen.writeFieldName(COORDINATES);
        writePointCoords(jgen, p);
        jgen.writeEndObject();
    }

    private void writePointCoords(JsonGenerator jgen, Point p) throws IOException {

        jgen.writeStartArray();
        if (!p.isEmpty()) {
            writeNumber(jgen, p.getCoordinate().x);
            writeNumber(jgen, p.getCoordinate().y);

            if (!Double.isNaN(p.getCoordinate().getZ())) {
                writeNumber(jgen, p.getCoordinate().getZ());
            }
        }
        jgen.writeEndArray();
    }

    private void writeNumber(final JsonGenerator jgen, final double n) throws IOException {
        jgen.writeNumber(format.format(n));
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    public int getMaximumFractionDigits() {
        return maximumFractionDigits;
    }

    public void setMaximumFractionDigits(int maximumFractionDigits) {
        this.maximumFractionDigits = maximumFractionDigits;
        format.setMaximumFractionDigits(maximumFractionDigits);
    }

    public int getMinimumFractionDigits() {
        return minimumFractionDigits;
    }

    public void setMinimumFractionDigits(int minimumFractionDigits) {
        this.minimumFractionDigits = minimumFractionDigits;
        format.setMinimumFractionDigits(minimumFractionDigits);
    }
}
