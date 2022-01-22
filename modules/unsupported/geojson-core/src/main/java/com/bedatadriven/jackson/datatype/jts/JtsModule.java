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
 * */
package com.bedatadriven.jackson.datatype.jts;

import com.bedatadriven.jackson.datatype.jts.parsers.GenericGeometryParser;
import com.bedatadriven.jackson.datatype.jts.parsers.GeometryCollectionParser;
import com.bedatadriven.jackson.datatype.jts.parsers.LineStringParser;
import com.bedatadriven.jackson.datatype.jts.parsers.MultiLineStringParser;
import com.bedatadriven.jackson.datatype.jts.parsers.MultiPointParser;
import com.bedatadriven.jackson.datatype.jts.parsers.MultiPolygonParser;
import com.bedatadriven.jackson.datatype.jts.parsers.PointParser;
import com.bedatadriven.jackson.datatype.jts.parsers.PolygonParser;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.math.RoundingMode;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class JtsModule extends SimpleModule {

    /** serialVersionUID */
    private static final long serialVersionUID = 3387512874441967803L;

    public static int DEFAULT_MAX_DECIMALS = 4;
    public static int DEFAULT_MIN_DECIMALS = 0;
    public static RoundingMode DEFAULT_ROUND_MODE = RoundingMode.HALF_UP;

    private GeometrySerializer geometrySerializer;

    public JtsModule(int maxDecimals) {
        this(new GeometryFactory(), maxDecimals, DEFAULT_MIN_DECIMALS, DEFAULT_ROUND_MODE);
    }

    public JtsModule() {
        this(new GeometryFactory());
    }

    public JtsModule(GeometryFactory geometryFactory) {
        this(geometryFactory, DEFAULT_MAX_DECIMALS, DEFAULT_MIN_DECIMALS, DEFAULT_ROUND_MODE);
    }

    public JtsModule(
            GeometryFactory geometryFactory,
            int maxDecimals,
            int minDecimals,
            RoundingMode rounding) {
        super("JtsModule", new Version(1, 0, 0, null, "com.bedatadriven", "jackson-datatype-jts"));

        geometrySerializer = new GeometrySerializer(minDecimals, maxDecimals, rounding);
        addSerializer(Geometry.class, geometrySerializer);
        GenericGeometryParser genericGeometryParser = new GenericGeometryParser(geometryFactory);
        addDeserializer(Geometry.class, new GeometryDeserializer<>(genericGeometryParser));
        addDeserializer(Point.class, new GeometryDeserializer<>(new PointParser(geometryFactory)));
        addDeserializer(
                MultiPoint.class,
                new GeometryDeserializer<>(new MultiPointParser(geometryFactory)));
        addDeserializer(
                LineString.class,
                new GeometryDeserializer<>(new LineStringParser(geometryFactory)));
        addDeserializer(
                MultiLineString.class,
                new GeometryDeserializer<>(new MultiLineStringParser(geometryFactory)));
        addDeserializer(
                Polygon.class, new GeometryDeserializer<>(new PolygonParser(geometryFactory)));
        addDeserializer(
                MultiPolygon.class,
                new GeometryDeserializer<>(new MultiPolygonParser(geometryFactory)));
        addDeserializer(
                GeometryCollection.class,
                new GeometryDeserializer<>(
                        new GeometryCollectionParser(geometryFactory, genericGeometryParser)));
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
    }

    public void setMaxDecimals(int number) {
        geometrySerializer.setMaximumFractionDigits(number);
    }
}
