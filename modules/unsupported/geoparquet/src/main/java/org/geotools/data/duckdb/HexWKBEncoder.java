/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.IOException;
import java.util.HexFormat;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.io.OutStream;
import org.locationtech.jts.io.WKBWriter;

/**
 * Utility class for encoding JTS geometries as hexadecimal WKB (Well-Known Binary) strings for use with DuckDB's
 * spatial functions.
 *
 * <p>This encoder provides locale-independent geometry encoding that avoids issues with decimal separator parsing in
 * different system locales. Instead of using WKT (Well-Known Text) which can be misinterpreted in locales that use
 * comma as decimal separator, this class generates binary WKB data encoded as hexadecimal strings for use with DuckDB's
 * {@code ST_GeomFromHEXEWKB} function.
 *
 * <p><strong>Usage:</strong>
 *
 * <pre>{@code
 * Geometry geom = ...;
 * StringBuffer sql = new StringBuffer("ST_GeomFromHEXEWKB('");
 * HexWKBEncoder.encode(geom, sql);
 * sql.append("')");
 * }</pre>
 *
 * @see DuckDBDialect#encodeGeometryValue(Geometry, int, int, StringBuffer)
 * @see DuckDBFilterToSQL#visitLiteralGeometry(org.geotools.api.filter.expression.Literal)
 */
public class HexWKBEncoder {

    /** WKBWriter for locale-independent binary geometry encoding */
    private static final WKBWriter WKB_WRITER = new WKBWriter();

    /**
     * Encodes a JTS geometry as hexadecimal WKB and appends it to the provided writer.
     *
     * <p>LinearRing geometries are automatically converted to LineString since DuckDB doesn't support the LinearRing
     * type.
     *
     * @param g the geometry to encode
     * @param writer the target to append the hexadecimal WKB representation to
     * @throws IOException if there's an error writing to the appendable
     */
    public static void encode(Geometry g, Appendable writer) throws IOException {
        if (g instanceof LinearRing) {
            // DuckDB doesn't support LinearRing geometry type - convert to LineString
            g = g.getFactory().createLineString(((LinearRing) g).getCoordinateSequence());
        }
        // Use WKB (Well-Known Binary) format to avoid locale-specific text parsing issues
        // ST_GeomFromHEXEWKB is locale-independent and works with binary data
        WKB_WRITER.write(g, new OutStream() {
            @Override
            public void write(byte[] buf, int len) throws IOException {
                HexFormat.of().formatHex(writer, buf, 0, len);
            }
        });
    }
}
